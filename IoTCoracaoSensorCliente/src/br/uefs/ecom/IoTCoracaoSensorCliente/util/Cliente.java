package br.uefs.ecom.IoTCoracaoSensorCliente.util;
import java.net.*;
import java.io.*;

public class Cliente {
	
	private int id;
	private DatagramSocket clienteSocket;
	private static int contadorId = 0;

	
	public Cliente() throws SocketException {
		id = gerarId();
		clienteSocket = new DatagramSocket();
	}
	
	private int gerarId() {
		return contadorId++;
	}
	
	public void enviarMensagem(String comando, Object dados, InetAddress enderecoServidor, int portaProcesso) throws IOException {
		Mensagem mensagem = new Mensagem(comando, dados);
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
		objectOutputStream.writeObject(mensagem);
		
		
		DatagramPacket sendPacket = new DatagramPacket(outputStream.toByteArray(), outputStream.toByteArray().length, enderecoServidor, portaProcesso);
		clienteSocket.send(sendPacket);
	}
	
	public Mensagem receberMensagem() throws IOException, ClassNotFoundException {
		byte[] dadosRecebidos = new byte[1024]; 
		DatagramPacket pacoteRecebido = new DatagramPacket(dadosRecebidos, dadosRecebidos.length);
		clienteSocket.receive(pacoteRecebido);

		ByteArrayInputStream inputStream = new ByteArrayInputStream(pacoteRecebido.getData());
		ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
		return (Mensagem) objectInputStream.readObject();
	}
	
	public boolean fecharClienteSocket() {
		if(!clienteSocket.isClosed()) {
			clienteSocket.close();
			return true;
		} 
		
		return false;
	}

	public int getId() {
		return id;
	}


}
