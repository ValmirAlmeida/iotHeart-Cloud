package br.uefs.ecomp.IoTHeartUtil.util;
import java.net.*;
import java.io.*;

/**
 * Classe que representa uma conexão UDP no contexto do sistema
 * @author Valmir Vinicius
 *
 */
public class ConexaoUDP {
	
	/** Socket da conexão */
	private DatagramSocket conexaoSocket;
	
	/**
	 * Obtém uma nova instância de conexão UDP
	 * @throws SocketException caso ocorra algum erro de socket
	 */
	public ConexaoUDP() throws SocketException {
		this.conexaoSocket = new DatagramSocket();
	};
	
	/**
	 * Obtém uma nova instância de conexão UDP na qual o número da porta já está definido
	 * @param porta porta da conexão
	 * @param porta porta do host de destino
	 * @throws SocketException caso ocorra algum erro de socket
	 */
	public ConexaoUDP(int porta) throws SocketException {
		this.conexaoSocket = new DatagramSocket(porta);
	};
	
	/**
	 * Envia uma mensagem através de conexão UDP
	 * @param mensagem mensagem a ser enviada
	 * @param ip ip do destino
	 * @param porta porta do host de destino
	 * @throws IOException caso ocorra algum erro durante a entrada ou saída de dados na comunicação
	 */
	public void enviar(Mensagem mensagem, InetAddress ip, int porta) throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
		objectOutputStream.writeObject(mensagem);
		DatagramPacket pacoteEnviado = new DatagramPacket(outputStream.toByteArray(), outputStream.toByteArray().length, ip, porta);
		conexaoSocket.send(pacoteEnviado);
	}
	
	/**
	 * Recebe mensagem através de conexão UDP
	 * @param callback função de callback a ser executada após o recebimento do pacote
	 * @throws IOException caso ocorra algum erro durante a entrada ou saída de dados na comunicação
	 * @throws ClassNotFoundException
	 */
	public void receber(LidarComRecebimentoUDP callback) throws IOException, ClassNotFoundException {
		byte[] dadosRecebidos = new byte[1024]; 
		DatagramPacket pacoteRecebido = new DatagramPacket(dadosRecebidos, dadosRecebidos.length);
		conexaoSocket.receive(pacoteRecebido);
		callback.pacoteChegou(pacoteRecebido);
	}

	/**
	 * Verifica se o pacote recebido é integro e o desmonta, retornando a mensagem contida
	 * @param pacoteRecebido pacote recebido
	 * @return mensagem recebida, caso o pacote seja integro; <code>null</code>, caso contrário
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Mensagem verificaDesmontaPacote(DatagramPacket pacoteRecebido) throws IOException, ClassNotFoundException {
		ByteArrayInputStream inputStream = new ByteArrayInputStream(pacoteRecebido.getData());
		ObjectInputStream objectInputStream = null;
		Mensagem mensagem = null;

		try {
			objectInputStream = new ObjectInputStream(inputStream);
			
			Object objetoRecebido = objectInputStream.readObject();
			
			if(!(objetoRecebido instanceof Mensagem)) {
				return null;
			} else {
				mensagem = (Mensagem) objetoRecebido;
			}
			
		} catch (IOException e) {
			return null;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		try {
			new Integer(mensagem.getAcaoProtocolo());
		} catch(NumberFormatException e) {
			return null;
		}
		
		if(!(mensagem.getAlvo() instanceof String || mensagem.getAlvo() == null)) {
			return null;
		}
		
		return mensagem;
	}
	
	/**
	 * Encerra a conexão UDP
	 * @return <code>true</code>, caso a conexão esteja previamente aberta e seja encerrada; <code>false</code>, caso a conexão não esteja previamente aberta
	 */
	public boolean fecharConexao() {
		if(!conexaoSocket.isClosed()) {
			conexaoSocket.close();
			return true;
		}
		
		return false;
	}
}
