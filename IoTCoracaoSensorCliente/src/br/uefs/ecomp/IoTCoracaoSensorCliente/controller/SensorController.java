package br.uefs.ecomp.IoTCoracaoSensorCliente.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import br.uefs.ecom.IoTCoracaoSensorCliente.util.Cliente;
import br.uefs.ecom.IoTCoracaoSensorCliente.util.Mensagem;
import br.uefs.ecomp.IoTCoracaoSensorCliente.model.Paciente;

public class SensorController {
	
	LinkedList<Paciente> pacientesGerados = new LinkedList<Paciente>();
	Cliente cliente;
	
	public SensorController(int porta, String endereco) throws UnknownHostException, SocketException {
		cliente = new Cliente(porta, endereco);
	}
	
	public LinkedList<Paciente> getPacientesGerados() {
		return pacientesGerados;
	}
	
	public void iniciarSensor() throws IOException, ClassNotFoundException {
		cliente.enviarMensagem("SENSOR-START", null);

	}
	
	/**
	 * Gera um novo paciente com base nos dados passados.
	 * @param nomePaciente nome do paciente
	 * @param movimentacaoInicial indica se o paciente está ou não movimentado-se
	 * @param ritmoCardiacoInicial ritmo cardiaco inicial do paciente
	 * @param pressaoSanguineaInicial pressão sanguinea inicial do paciente
	 * @return objeto paciente criado com base nos dados passados
	 * @throws IOException 
	 */
	public void gerarPaciente(String nomePaciente, boolean movimentacaoInicial, float ritmoCardiacoInicial, float pressaoSanguineaInicial) throws IOException {
		Paciente novoPaciente = new Paciente(nomePaciente, movimentacaoInicial, ritmoCardiacoInicial, pressaoSanguineaInicial);
		pacientesGerados.add(novoPaciente);
		cliente.enviarMensagem("SENSOR-ADD-PACIENTE", novoPaciente);
	}
	
	/**
	 * Busca por um paciente no sistema.
	 * @param id id do paciente a ser buscado
	 * @return objeto paciente cujo id é igual ao informado
	 * @throws NoSuchElementException - caso não exista um paciente com o id informado
	 */
	private Paciente buscarPaciente(int id) throws NoSuchElementException {
		ListIterator<Paciente> iterador = pacientesGerados.listIterator();
		Paciente paciente = null;
		
		while(iterador.hasNext()) { //enquanto há pacientes na lista
			paciente = iterador.next();
			
			if(paciente.getId() == id) { //busca até encontrar o paciente cujo id foi informado
				break;
			}
		}
		
		if(paciente == null) { //caso não seja encontrado um paciente
			throw new NoSuchElementException("Paciente não foi cadastrado no sistema");
		}
		
		return paciente;
	}
	
	/**
	 * Altera o estado de movimentação do paciente, ou seja, se o sensor indica que ele está movendo-se, passará a indicar que não está movendo-se e vice e versa.
	 * @param id id do paciente do qual o estado de movimentação será alterado
	 * @throws IOException 
	 */
	public void alteraMovimentacao(int id) throws NoSuchElementException, IOException {
		try {
			Paciente paciente = buscarPaciente(id);
			paciente.setMovimentacao(!paciente.isMovimentacao()); //altera o estado de movimentação do paciente
			cliente.enviarMensagem("SENSOR-ATT-MOVIMENTACAO#"+paciente.getId(), null);
		} catch (NoSuchElementException e) {
			throw e;
		}
	}
	
	/**
	 * Altera o valor do ritmo cardiaco do paciente.
	 * @param id id do paciente do qual o ritmo cardíaco será alterado
	 * @param variacao valor a ser variado no ritmo cardiaco; caso seja positivo, o ritmo cardíaco aumentará, caso contrário o ritmo cardíaco será diminuido.
	 * @throws NoSuchElementException - caso não exista um paciente com o id informado
	 * @throws IOException 
	 * @throws IllegalArgumentException - caso o valor de ritmo cardíaco informado resulte em ritmo cardíaco negativo	 	  
	 */
	public void alteraRitmoCardiaco(int id, float variacao) throws NoSuchElementException, IOException {
		try {
			Paciente paciente = buscarPaciente(id);
			
			if(paciente.getRitmoCardiaco() + variacao < 0) {
				throw new IllegalArgumentException("O valor informado é inválido");
			}
			
			paciente.setRitmoCardiaco(paciente.getRitmoCardiaco()+variacao); //altera o ritmo cardiaco do paciente
			cliente.enviarMensagem("SENSOR-ATT-RITMOCAR#"+paciente.getId(), paciente.getRitmoCardiaco());
		} catch (NoSuchElementException e) {
			throw e;
		}
	}
	
	/**
	 * Altera o valor da pressão sanguínea do paciente.
	 * @param id id do paciente do qual a pressão sanguínea será alterada
	 * @param variacao valor a ser variado na pressão sanguínea; caso seja positivo, a pressão sanguínea aumentará, caso contrário a pressão sanguínea será diminuida.
	 * @throws NoSuchElementException - caso não exista um paciente com o id informado
	 * @throws IOException 
	 * @throws IllegalArgumentException - caso o valor de pressão sanguínea informado resulte em pressão sanguínea negativa
	 */
	public void alterarPressaoSanguinea(int id, float variacao) throws NoSuchElementException, IOException, IllegalArgumentException {
		try {
			Paciente paciente = buscarPaciente(id);
			
			if(paciente.getPressaoSanguinea() + variacao < 0) {
				throw new IllegalArgumentException("O valor informado é inválido");
			}			

			paciente.setPressaoSanguinea(paciente.getPressaoSanguinea()+variacao); //altera a pressão sanguinea do paciente
			cliente.enviarMensagem("SENSOR-ATT-PRESSAO#"+paciente.getId(), paciente.getPressaoSanguinea());
		} catch (NoSuchElementException e) {
			throw e;
		}
	}


}
