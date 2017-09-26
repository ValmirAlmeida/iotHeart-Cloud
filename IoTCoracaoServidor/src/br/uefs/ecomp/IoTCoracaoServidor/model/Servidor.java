package br.uefs.ecomp.IoTCoracaoServidor.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.LinkedList;

import br.uefs.ecom.IoTCoracaoSensorCliente.util.Mensagem;

public class Servidor {
	
	private int porta;
	private DatagramSocket socketServidor;
	private boolean ligado;
	
	public Servidor(int porta) throws SocketException {
		this.porta = porta;
		this.socketServidor = new DatagramSocket(this.porta);
	}
	
	public Object[] receberPacote() throws IOException, ClassNotFoundException {
		byte[] receiveData = new byte[1024];
		DatagramPacket pacoteRecebido = new DatagramPacket(receiveData, receiveData.length);
		socketServidor.receive(pacoteRecebido);
		
		ByteArrayInputStream inputStream = new ByteArrayInputStream(pacoteRecebido.getData());
		ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
		
		Mensagem mensagem = (Mensagem) objectInputStream.readObject();
		
		Object[] respostaRecebimento = new Object[3];
		respostaRecebimento[0] = mensagem;
		respostaRecebimento[1] = pacoteRecebido.getAddress();
		respostaRecebimento[2] = pacoteRecebido.getPort();
		
		return respostaRecebimento;
	}
	
	public void enviarPacote(Mensagem mensagem, InetAddress enderecoDestino, int portaDestino) throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
		objectOutputStream.writeObject(mensagem);  

		DatagramPacket pacoteEnviado = new DatagramPacket(outputStream.toByteArray(), outputStream.toByteArray().length, enderecoDestino, portaDestino);
		socketServidor.send(pacoteEnviado);
	}
	
	public void iniciarThread(String nomeDaThread, InetAddress enderecoIPCliente, Mensagem mensagemRecebida) throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Class classe = Class.forName(nomeDaThread);
		Constructor construtor = classe.getConstructor(DatagramSocket.class, InetAddress.class, Integer.class, Mensagem.class);
		((Thread) construtor.newInstance(socketServidor, enderecoIPCliente, porta, mensagemRecebida)).start();
	}
	
	public void fecharServidor() {
		socketServidor.close();
	}
		

}
