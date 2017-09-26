package br.uefs.ecomp.IoTCoracaoServidor.controller;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Iterator;
import java.util.LinkedList;
import br.uefs.ecom.IoTCoracaoSensorCliente.util.Mensagem;
import br.uefs.ecomp.IoTCoracaoMedicoCliente.model.Medico;
import br.uefs.ecomp.IoTCoracaoServidor.model.MedicoThread;
import br.uefs.ecomp.IoTCoracaoServidor.model.SensorThread;
import br.uefs.ecomp.IoTCoracaoServidor.model.Servidor;

public class ServidorController {
	
	private Servidor servidor;
	private SensorThread sensorThread;
	private LinkedList<MedicoThread> medicoThreads;
	
	public ServidorController(int porta) throws SocketException {
		this.servidor = new Servidor(porta);
		this.sensorThread = null;
		this.medicoThreads = null;
	}
	
	public void rodarServidor() throws ClassNotFoundException, IOException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		while(true) {
			
			Object[] resultadoRecebimento = (Object[]) servidor.receberPacote();
			new Thread(){
				public void run() {
					boolean executando = true;
					
					while(executando) {
						Mensagem mensagemRecebida = (Mensagem) resultadoRecebimento[0];
						InetAddress ipOrigem = (InetAddress) resultadoRecebimento[1];
						int portaOrigem = (Integer) resultadoRecebimento[2];

						
						if(mensagemRecebida.getComando().compareTo("SENSOR-START") == 0) {
							sensorThread = new SensorThread(servidor, ipOrigem, portaOrigem);
							sensorThread.start();
						} else if(mensagemRecebida.getComando().compareTo("MEDICO-LOGIN") == 0) {
							MedicoThread medicoThread = new MedicoThread(servidor, ipOrigem, portaOrigem);
							
							try {
								if(medicoThread.logarMedico((Object[]) mensagemRecebida.getDados())) {
									medicoThread.start();
								} else {
									medicoThread = null;
								}
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
						} else if(mensagemRecebida.getComando().contains("SENSOR")) {
							if(sensorThread != null && sensorThread.isAlive()) {
								sensorThread.setMensagemRecebida(mensagemRecebida);
							}
						} else if(mensagemRecebida.getComando().contains("MEDICO")) {
							MedicoThread medAtualThread = getMedicoThread(obterCRM(mensagemRecebida.getComando()));
							
							if(medAtualThread.getMedicoLogado() != null && medAtualThread != null && medAtualThread.isAlive()) {
								medAtualThread.setMensagemRecebida(mensagemRecebida);
							}
						}
						
						executando = false;
					}
				}
			}.start();

		}
	}
	
	private MedicoThread getMedicoThread(String CRM) {
		Iterator<MedicoThread> iterador = medicoThreads.iterator();
		MedicoThread medicoThread = null;
		
		while(iterador.hasNext()) {
			medicoThread = iterador.next();
			
			if(iterador.next().getMedicoLogado().getCRM().compareTo(CRM) == 0) {
				
			}
		}
		
		
		return null;
	}
	
	private String obterCRM(String comando) {
		int indexHashtag = comando.indexOf("#");
		
		if(indexHashtag != -1) {
			return comando.substring(indexHashtag+1);
		}
		
		return null;
	}
	
/*	private Medico loginMedico(String email, String senha) throws FileNotFoundException, IOException {
		boolean loginOk = false;
		String emailLido = null;
		String senhaLida = null;
		String nomeLido = null;
		String CRMLido = null;
		
		try(BufferedReader bufferedReader = new BufferedReader(new FileReader("cadastros-medicos.txt"))) {
		    String linha = bufferedReader.readLine();
	
		    while (linha != null || linha == "#") {
		    	emailLido = bufferedReader.readLine();
		    	senhaLida = bufferedReader.readLine();
	    		nomeLido = bufferedReader.readLine();
	    		CRMLido = bufferedReader.readLine();
		    	
		    	if(emailLido.compareTo(email) == 0 && senhaLida.compareTo(senha) == 0) {
		    		loginOk = true;
		    		break;
		    	}
		    }
		}
		
		if(loginOk) {
			return new Medico(nomeLido, CRMLido, emailLido, senhaLida);
		} else {
			return null;
		}
	}*/

}
