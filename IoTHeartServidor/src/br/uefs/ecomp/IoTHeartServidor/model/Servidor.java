package br.uefs.ecomp.IoTHeartServidor.model;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.SocketException;

import br.uefs.ecomp.IoTHeartUtil.util.ConexaoTCP;
import br.uefs.ecomp.IoTHeartUtil.util.ConexaoUDP;
import br.uefs.ecomp.IoTHeartUtil.util.LidarComRecebimentoTCP;
import br.uefs.ecomp.IoTHeartUtil.util.LidarComRecebimentoUDP;
import br.uefs.ecomp.IoTHeartUtil.util.Mensagem;

/**
 * Classe responsável por representar o servidor no contexto do sistema.
 * @author Valmir Vinicius
 */
public class Servidor {

    /** Porta do processo em questão no servidor */
	private int porta;
    /** Conexão UDP do servidor */
	private ConexaoUDP conexaoServidorUDP;
    /** Conexão TCP do servidor */
	private ConexaoTCP conexaoServidorTCP;
    /** Indica se o servidor está atualmente aceitando requisições UDP */
	private boolean executandoUDP;
    /** Indica se o servidor está atualmente aceitando requisições TCP */
	private boolean executandoTCP;
    /** Servidor TCP para aceitar novas requisições TCP no servidor */
	private ServerSocket socketServidorTCP;
	
	/**
	 * Obtém uma instância de servidor
	 * @param porta porta do processo 
	 */
	public Servidor(int porta) {
		this.porta = porta;
	}
	
	/**
	 * Executa o servidor
	 * @throws ClassNotFoundException caso alguma classe fundamental à execução não seja encontrada
	 * @throws IOException caso ocorra algum erro durante a entrada ou saída de dados na comunicação
	 */
	public void executar() throws ClassNotFoundException, IOException {
		new Thread(new Runnable() { //inicia uma nova thread para aceitar requisições UDP
			@Override
			public void run() {
				try {
					conexaoServidorUDP = new ConexaoUDP(porta); //obtém uma nova  conexão UDP
					executandoUDP = true; //servidor está rodando UDP
				} catch (SocketException e1) {
					executandoUDP = false; //servidor não está rodando UDP
				}
				
				while(executandoUDP) { //enquanto o servidor estiver aceitando requisições UDP
					try {
						conexaoServidorUDP.receber(new LidarComRecebimentoUDP() { //recebe uma requisição
							@Override
							public void pacoteChegou(DatagramPacket pacoteRecebido) { //novo pacote recebido
								Thread thread = new Thread(new AtendenteUDP(pacoteRecebido)); //cria uma nova thread para atender a requisição recebida
								thread.start(); //inicia a thread
							}
						});
					} catch (ClassNotFoundException | IOException e) {
						executandoUDP = false; //servidor não está rodando UDP
						break; //encerra a função
					}
				}
				
				//encerra o servidor
				if(conexaoServidorUDP != null && conexaoServidorUDP.fecharConexao()) { 
					executandoUDP = false;
				}
			}
		}).start();
		
		new Thread(new Runnable() { //inicia uma nova thread para aceitar requisições TCP
			@Override
			public void run() {
				try {
					socketServidorTCP = new ServerSocket(porta); //cria um socket de servidor TCP
					executandoTCP = true; //servidor está aceitando requisições TCP
				} catch (IOException e2) {
					executandoTCP = false; //servidor não está aceitando requisições TCP
				}
				
				while(executandoTCP && !socketServidorTCP.isClosed()) { //enquanto o socket servidor estiver aberto e o servidor aceitando requisições TCP
					try {
						conexaoServidorTCP = new ConexaoTCP(socketServidorTCP.accept()); //aceita uma nova conexão
					} catch (IOException e2) {
						//encerra o servidor em relação a requisições TCP
						executandoTCP = false;
						break;
					}
					
					try {
						conexaoServidorTCP.receber(new LidarComRecebimentoTCP() { //espera receber um pacote TCP
							@Override
							public void pacoteChegou(Mensagem mensagemRecebida) { //mensagem TCP chegou
								Thread thread = new Thread(new AtendenteTCP(conexaoServidorTCP, mensagemRecebida)); //cria uma nova thread com base no atendente de requisições TCP
								thread.start();	//inicia a thread atendente 
							}
						});
					} catch (ClassNotFoundException | IOException e) {
						//encerra o servidor em relação a requisições TCP
						executandoTCP = false;
						break;
					}
				}
			}				
		}).start();	
	}
	
	/**
	 * Para a aceitação de requisições UDP pelo servidor 
	 */
	public void pararUDP() {
		conexaoServidorUDP.fecharConexao();
		executandoUDP = false;
	}
	
	/**
	 * Para a aceitação de requisições TCP pelo servidor 
	 * @throws IOException caso ocorra algum erro durante a entrada ou saída de dados na comunicação
	 */
	public void pararTCP() throws IOException {
		if(!socketServidorTCP.isClosed()) {
			try {
				socketServidorTCP.close();
			} catch (Throwable e) {
				throw e;
			}
			executandoTCP = false;
		}
	}
	
	/**
	 * Indica se o servidor está executando
	 * @return <code>true</code>, caso o servidor esteja executando em TCP e ou UDP; <code>false</code>, caso contrário
	 */
	public boolean isExecutando() {
		return executandoTCP || executandoUDP;
	}
}
