package br.uefs.ecomp.IoTCoracaoServidor.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.util.LinkedList;

import br.uefs.ecom.IoTCoracaoSensorCliente.util.Mensagem;
import br.uefs.ecomp.IoTCoracaoMedicoCliente.model.Medico;
import br.uefs.ecomp.IoTCoracaoSensorCliente.model.Paciente;

public class MedicoThread extends Thread {
	private Servidor servidorResponsavel;
	private InetAddress ipMedicoCliente;
	private int portaMedicoCliente;
	private Medico medicoLogado;
	Mensagem mensagemRecebida;
	
	public MedicoThread(Servidor servidorResponsavel, InetAddress ipMedicoCliente, int portaMedicoCliente) {
		this.servidorResponsavel = servidorResponsavel;
		this.ipMedicoCliente = ipMedicoCliente;
		this.portaMedicoCliente = portaMedicoCliente;
		this.medicoLogado = null;
		this.mensagemRecebida = null;
	}
	
	public void run() {
		while(true) {
			if(mensagemRecebida != null) {
				Object[] comandoParticionado = descriptografarComando(mensagemRecebida.getComando());
				
				String comando = (String) comandoParticionado[0];
				Integer idPaciente = (Integer) comandoParticionado[1];
				
				if(comando.compareTo("MEDICO-ADD-PACIENTES") == 0) { //verifica se o cliente sensor está solicitado a adição de um novo paciente
					//
				} else if(comando.compareTo("MEDICO-RESET-MONITORAMENTO") == 0) {
					//
				}
				
				
				this.mensagemRecebida = null;				
			}
		}
		
	}
	
	public boolean logarMedico(Object[] dadosLogin) throws IOException {
		String emailInformado = (String) dadosLogin[0];
		String senhaInformada = (String) dadosLogin[1];
		String nomeLido = null;
		String CRMLido = null;
		String emailLido = null;
		String senhaLida = null;
		boolean loginOk = false;
		
		try(BufferedReader bufferedReader = new BufferedReader(new FileReader("C:/Users/Vinicius/Desktop/PBL - Concorrência e Conectividade/ServidorSimples/IoTCoracaoServidor/src/br/uefs/ecomp/IoTCoracaoServidor/util/cadastros-medicos.txt"))) {
		    String linha = bufferedReader.readLine();
	
		    while (linha != null || linha == "$") {
		    	emailLido = bufferedReader.readLine();
		    	senhaLida = bufferedReader.readLine();
	    		nomeLido = bufferedReader.readLine();
	    		CRMLido = bufferedReader.readLine();
		    	
		    	if(emailLido.compareTo(emailInformado) == 0 && senhaLida.compareTo(senhaInformada) == 0) {
		    		loginOk = true;
		    		break;
		    	}
		    }
		}
		
		if(loginOk) {
			medicoLogado = new Medico(nomeLido, CRMLido, emailInformado, senhaInformada);
			servidorResponsavel.enviarPacote(new Mensagem("MEDICO-LOGIN-FEEDBACK-TRUE", medicoLogado), ipMedicoCliente, portaMedicoCliente);
		} else {
			servidorResponsavel.enviarPacote(new Mensagem("MEDICO-LOGIN-FEEDBACK-FALSE", null), ipMedicoCliente, portaMedicoCliente);
		}
		
		return loginOk;
	}
	
	public void setMensagemRecebida(Mensagem mensagemRecebida) {
		this.mensagemRecebida = mensagemRecebida;
	}	
	public Medico getMedicoLogado() {
		return this.medicoLogado;
	}
	
	/**
	 * Descriptografa um comando recebido.
	 * @param comandoCompleto o comando completo, na forma em que foi recebido pelo cliente 
	 * @return array cuja primeira posição possui o comando propriamente dito e cuja segunda posição 
	 * contém o id do paciente ao qual o comando refere-se, se esse for o caso, se não houver um paciente 
	 * associado a segunda posição tem o valor -1
	 */
	public Object[] descriptografarComando(String comandoCompleto) {
		Object[] resposta = new Object[2];
		
		if(comandoCompleto.indexOf("#") == -1) { //caso seja um comando geral
			resposta[0] = comandoCompleto;
			resposta[1] = -1;
		} else { //caso possua um caractere, indicando que o comando refere-se a um usuário específico
			int hashtagIndex = comandoCompleto.indexOf("#");
			
			String comando = comandoCompleto.substring(0, hashtagIndex-1);
			Integer id = Integer.parseInt(comandoCompleto.substring(hashtagIndex+1));
			
			resposta[0] = comando;
			resposta[1] = id;
		}

		return resposta;
	}
}
