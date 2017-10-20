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
 * Classe respons�vel por representar o servidor no contexto do sistema.
 * @author Valmir Vinicius
 */
public class Servidor {

    /** Porta do processo em quest�o no servidor */
	private int porta;
    /** Conex�o UDP do servidor */
	private ConexaoUDP conexaoServidorUDP;
    /** Conex�o TCP do servidor */
	private ConexaoTCP conexaoServidorTCP;
    /** Indica se o servidor est� atualmente aceitando requisi��es UDP */
	private boolean executandoUDP;
    /** Indica se o servidor est� atualmente aceitando requisi��es TCP */
	private boolean executandoTCP;
    /** Servidor TCP para aceitar novas requisi��es TCP no servidor */
	private ServerSocket socketServidorTCP;
	
	/**
	 * Obt�m uma inst�ncia de servidor
	 * @param porta porta do processo 
	 */
	public Servidor(int porta) {
		this.porta = porta;
	}
	
	/**
	 * Executa o servidor
	 * @throws ClassNotFoundException caso alguma classe fundamental � execu��o n�o seja encontrada
	 * @throws IOException caso ocorra algum erro durante a entrada ou sa�da de dados na comunica��o
	 */
	public void executar() throws ClassNotFoundException, IOException {
		new Thread(new Runnable() { //inicia uma nova thread para aceitar requisi��es UDP
			@Override
			public void run() {
				try {
					conexaoServidorUDP = new ConexaoUDP(porta); //obt�m uma nova  conex�o UDP
					executandoUDP = true; //servidor est� rodando UDP
				} catch (SocketException e1) {
					executandoUDP = false; //servidor n�o est� rodando UDP
				}
				
				while(executandoUDP) { //enquanto o servidor estiver aceitando requisi��es UDP
					try {
						conexaoServidorUDP.receber(new LidarComRecebimentoUDP() { //recebe uma requisi��o
							@Override
							public void pacoteChegou(DatagramPacket pacoteRecebido) { //novo pacote recebido
								Thread thread = new Thread(new AtendenteUDP(pacoteRecebido)); //cria uma nova thread para atender a requisi��o recebida
								thread.start(); //inicia a thread
							}
						});
					} catch (ClassNotFoundException | IOException e) {
						executandoUDP = false; //servidor n�o est� rodando UDP
						break; //encerra a fun��o
					}
				}
				
				//encerra o servidor
				if(conexaoServidorUDP != null && conexaoServidorUDP.fecharConexao()) { 
					executandoUDP = false;
				}
			}
		}).start();
		
		new Thread(new Runnable() { //inicia uma nova thread para aceitar requisi��es TCP
			@Override
			public void run() {
				try {
					socketServidorTCP = new ServerSocket(porta); //cria um socket de servidor TCP
					executandoTCP = true; //servidor est� aceitando requisi��es TCP
				} catch (IOException e2) {
					executandoTCP = false; //servidor n�o est� aceitando requisi��es TCP
				}
				
				while(executandoTCP && !socketServidorTCP.isClosed()) { //enquanto o socket servidor estiver aberto e o servidor aceitando requisi��es TCP
					try {
						conexaoServidorTCP = new ConexaoTCP(socketServidorTCP.accept()); //aceita uma nova conex�o
					} catch (IOException e2) {
						//encerra o servidor em rela��o a requisi��es TCP
						executandoTCP = false;
						break;
					}
					
					try {
						conexaoServidorTCP.receber(new LidarComRecebimentoTCP() { //espera receber um pacote TCP
							@Override
							public void pacoteChegou(Mensagem mensagemRecebida) { //mensagem TCP chegou
								Thread thread = new Thread(new AtendenteTCP(conexaoServidorTCP, mensagemRecebida)); //cria uma nova thread com base no atendente de requisi��es TCP
								thread.start();	//inicia a thread atendente 
							}
						});
					} catch (ClassNotFoundException | IOException e) {
						//encerra o servidor em rela��o a requisi��es TCP
						executandoTCP = false;
						break;
					}
				}
			}				
		}).start();	
	}
	
	/**
	 * Para a aceita��o de requisi��es UDP pelo servidor 
	 */
	public void pararUDP() {
		conexaoServidorUDP.fecharConexao();
		executandoUDP = false;
	}
	
	/**
	 * Para a aceita��o de requisi��es TCP pelo servidor 
	 * @throws IOException caso ocorra algum erro durante a entrada ou sa�da de dados na comunica��o
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
	 * Indica se o servidor est� executando
	 * @return <code>true</code>, caso o servidor esteja executando em TCP e ou UDP; <code>false</code>, caso contr�rio
	 */
	public boolean isExecutando() {
		return executandoTCP || executandoUDP;
	}
}
