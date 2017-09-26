package br.uefs.ecom.IoTCoracaoSensorCliente.util;

import java.net.SocketException;
import java.util.NoSuchElementException;

import br.uefs.ecomp.IoTCoracaoSensorCliente.model.Paciente;

public class Sensor extends Cliente {
	
	/**
	 * 
	 * @throws SocketException 
	 */
	public Sensor() throws SocketException {
		super();
	}; 
	
	/**
	 * Gera um novo paciente com base nos dados passados.
	 * @param nomePaciente nome do paciente
	 * @param movimentacaoInicial indica se o paciente está ou não movimentado-se
	 * @param ritmoCardiacoInicial ritmo cardiaco inicial do paciente
	 * @param pressaoSanguineaInicial pressão sanguinea inicial do paciente
	 * @return objeto paciente criado com base nos dados passados
	 */
	public static Paciente gerarPaciente(String nomePaciente, boolean movimentacaoInicial, float ritmoCardiacoInicial, float pressaoSanguineaInicial) {
		Paciente novoPaciente = new Paciente(nomePaciente, movimentacaoInicial, ritmoCardiacoInicial, pressaoSanguineaInicial);
		return novoPaciente;
	}
	
	/**
	 * Altera o estado de movimentação do paciente, ou seja, se o sensor indica que ele está movendo-se, passará a indicar que não está movendo-se e vice e versa.
	 * @param paciente paciente do qual o estado de movimentação será alterado
	 */
	public void alteraMovimentacao(Paciente paciente) {
		paciente.setMovimentacao(!paciente.isMovimentacao()); //altera o estado de movimentação do paciente
	}
	
	/**
	 * Altera o valor do ritmo cardiaco do paciente.
	 * @param paciente paciente do qual o ritmo cardíaco será alterado
	 * @param variacao valor a ser variado no ritmo cardiaco; caso seja positivo, o ritmo cardíaco aumentará, caso contrário o ritmo cardíaco será diminuido.
	 * @throws IllegalArgumentException - caso o valor de ritmo cardíaco informado resulte em ritmo cardíaco negativo	 
	 */
	public void alteraRitmoCardiaco(Paciente paciente, float variacao) throws IllegalArgumentException{
		if(paciente.getRitmoCardiaco()+variacao < 0) {
			throw new IllegalArgumentException("O valor informado é inválido");
		}
		
		paciente.setRitmoCardiaco(paciente.getRitmoCardiaco()+variacao); //altera o ritmo cardiaco do paciente
	}
	
	/**
	 * Altera o valor da pressão sanguínea do paciente.
	 * @param paciente paciente do qual a pressão sanguínea será alterada
	 * @param variacao valor a ser variado na pressão sanguínea; caso seja positivo, a pressão sanguínea aumentará, caso contrário a pressão sanguínea será diminuida.
	 * @throws IllegalArgumentException - caso o valor de pressão sanguínea informado resulte em pressão sanguínea negativa
	 */
	public void alterarPressaoSanguinea(Paciente paciente, float variacao) throws IllegalArgumentException {
		if(paciente.getPressaoSanguinea()+variacao < 0) {
			throw new IllegalArgumentException("O valor informado é inválido");
		}
		
		paciente.setPressaoSanguinea(paciente.getPressaoSanguinea()+variacao); //altera a pressão sanguinea do paciente
	}
}
