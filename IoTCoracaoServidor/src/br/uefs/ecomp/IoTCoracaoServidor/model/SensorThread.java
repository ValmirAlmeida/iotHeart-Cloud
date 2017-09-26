package br.uefs.ecomp.IoTCoracaoServidor.model;

import java.io.IOException;
import java.net.*;
import java.util.LinkedList;
import java.util.Iterator;
import br.uefs.ecom.IoTCoracaoSensorCliente.util.Mensagem;
import br.uefs.ecomp.IoTCoracaoSensorCliente.model.Paciente;

public class SensorThread extends Thread {
	
	private Servidor servidorResponsavel;
	private InetAddress ipSensor;
	private int portaSensor;
	private LinkedList<Paciente> pacientesRecebidos;
	Mensagem mensagemRecebida;
	
	public SensorThread(Servidor servidorResponsavel, InetAddress ipSensor, int portaSensor) {
		this.servidorResponsavel = servidorResponsavel;
		this.ipSensor = ipSensor;
		this.portaSensor = portaSensor;
		this.pacientesRecebidos = new LinkedList<Paciente>();
		this.mensagemRecebida = null;
	}
	
	public void setMensagemRecebida(Mensagem mensagemRecebida) {
		this.mensagemRecebida = mensagemRecebida;
	}
	
	public LinkedList<Paciente> getPacientesRecebidos() {
		return this.pacientesRecebidos;
	}
	
	public void run() {
		while(true) {
			if(mensagemRecebida != null) {
				Object[] comandoParticionado = descriptografarComando(mensagemRecebida.getComando());
				
				String comando = (String) comandoParticionado[0];
				Integer idPaciente = (Integer) comandoParticionado[1];
				
				if(comando.compareTo("SENSOR-ADD-PACIENTE") == 0) { //verifica se o cliente sensor está solicitado a adição de um novo paciente
					pacientesRecebidos.add((Paciente) mensagemRecebida.getDados());
				} else if(comando.compareTo("SENSOR-ATT-MOVIMENTACAO") == 0) {
					Paciente paciente = buscarPaciente(idPaciente);
					paciente.setMovimentacao(!paciente.isMovimentacao());
				} else if(comando.compareTo("SENSOR-ATT-RITMOCAR") == 0) {
					Paciente paciente = buscarPaciente(idPaciente);
					paciente.setRitmoCardiaco((Float) mensagemRecebida.getDados());
				} else if(comando.compareTo("SENSOR-ATT-PRESSAO") == 0) {
					Paciente paciente = buscarPaciente(idPaciente);
					paciente.setPressaoSanguinea((Float) mensagemRecebida.getDados());
				}
				
				
				this.mensagemRecebida = null;
			}
		}
		
	}
	

	
	/**
	 * Busca um paciente na lista de pacientes recebidos
	 * @param id id do paciente buscado
	 * @return objeto paciente cujo id foi especificado; null, caso não exista um paciente com o id informado
	 */
	public Paciente buscarPaciente(int id) {
		Iterator<Paciente> iterador = pacientesRecebidos.iterator(); //obtém o iterador da lista
		Paciente paciente = null;
		
		while(iterador.hasNext()) { //enquanto há pacientes na lista
			paciente = iterador.next();
			
			if(paciente.getId() == id) { //se o id do paciente for igual ao id do paciente buscado
				return paciente;
			}
		}
		
		return null;
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
