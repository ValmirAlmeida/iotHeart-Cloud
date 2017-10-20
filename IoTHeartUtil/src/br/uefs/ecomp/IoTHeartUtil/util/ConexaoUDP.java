package br.uefs.ecomp.IoTHeartUtil.util;
import java.net.*;
import java.io.*;

/**
 * Classe que representa uma conex�o UDP no contexto do sistema
 * @author Valmir Vinicius
 *
 */
public class ConexaoUDP {
	
	/** Socket da conex�o */
	private DatagramSocket conexaoSocket;
	
	/**
	 * Obt�m uma nova inst�ncia de conex�o UDP
	 * @throws SocketException caso ocorra algum erro de socket
	 */
	public ConexaoUDP() throws SocketException {
		this.conexaoSocket = new DatagramSocket();
	};
	
	/**
	 * Obt�m uma nova inst�ncia de conex�o UDP na qual o n�mero da porta j� est� definido
	 * @param porta porta da conex�o
	 * @param porta porta do host de destino
	 * @throws SocketException caso ocorra algum erro de socket
	 */
	public ConexaoUDP(int porta) throws SocketException {
		this.conexaoSocket = new DatagramSocket(porta);
	};
	
	/**
	 * Envia uma mensagem atrav�s de conex�o UDP
	 * @param mensagem mensagem a ser enviada
	 * @param ip ip do destino
	 * @param porta porta do host de destino
	 * @throws IOException caso ocorra algum erro durante a entrada ou sa�da de dados na comunica��o
	 */
	public void enviar(Mensagem mensagem, InetAddress ip, int porta) throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
		objectOutputStream.writeObject(mensagem);
		DatagramPacket pacoteEnviado = new DatagramPacket(outputStream.toByteArray(), outputStream.toByteArray().length, ip, porta);
		conexaoSocket.send(pacoteEnviado);
	}
	
	/**
	 * Recebe mensagem atrav�s de conex�o UDP
	 * @param callback fun��o de callback a ser executada ap�s o recebimento do pacote
	 * @throws IOException caso ocorra algum erro durante a entrada ou sa�da de dados na comunica��o
	 * @throws ClassNotFoundException
	 */
	public void receber(LidarComRecebimentoUDP callback) throws IOException, ClassNotFoundException {
		byte[] dadosRecebidos = new byte[1024]; 
		DatagramPacket pacoteRecebido = new DatagramPacket(dadosRecebidos, dadosRecebidos.length);
		conexaoSocket.receive(pacoteRecebido);
		callback.pacoteChegou(pacoteRecebido);
	}

	/**
	 * Verifica se o pacote recebido � integro e o desmonta, retornando a mensagem contida
	 * @param pacoteRecebido pacote recebido
	 * @return mensagem recebida, caso o pacote seja integro; <code>null</code>, caso contr�rio
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
	 * Encerra a conex�o UDP
	 * @return <code>true</code>, caso a conex�o esteja previamente aberta e seja encerrada; <code>false</code>, caso a conex�o n�o esteja previamente aberta
	 */
	public boolean fecharConexao() {
		if(!conexaoSocket.isClosed()) {
			conexaoSocket.close();
			return true;
		}
		
		return false;
	}
}
