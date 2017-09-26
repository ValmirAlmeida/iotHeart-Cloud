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
	 * @param movimentacaoInicial indica se o paciente est� ou n�o movimentado-se
	 * @param ritmoCardiacoInicial ritmo cardiaco inicial do paciente
	 * @param pressaoSanguineaInicial press�o sanguinea inicial do paciente
	 * @return objeto paciente criado com base nos dados passados
	 */
	public static Paciente gerarPaciente(String nomePaciente, boolean movimentacaoInicial, float ritmoCardiacoInicial, float pressaoSanguineaInicial) {
		Paciente novoPaciente = new Paciente(nomePaciente, movimentacaoInicial, ritmoCardiacoInicial, pressaoSanguineaInicial);
		return novoPaciente;
	}
	
	/**
	 * Altera o estado de movimenta��o do paciente, ou seja, se o sensor indica que ele est� movendo-se, passar� a indicar que n�o est� movendo-se e vice e versa.
	 * @param paciente paciente do qual o estado de movimenta��o ser� alterado
	 */
	public void alteraMovimentacao(Paciente paciente) {
		paciente.setMovimentacao(!paciente.isMovimentacao()); //altera o estado de movimenta��o do paciente
	}
	
	/**
	 * Altera o valor do ritmo cardiaco do paciente.
	 * @param paciente paciente do qual o ritmo card�aco ser� alterado
	 * @param variacao valor a ser variado no ritmo cardiaco; caso seja positivo, o ritmo card�aco aumentar�, caso contr�rio o ritmo card�aco ser� diminuido.
	 * @throws IllegalArgumentException - caso o valor de ritmo card�aco informado resulte em ritmo card�aco negativo	 
	 */
	public void alteraRitmoCardiaco(Paciente paciente, float variacao) throws IllegalArgumentException{
		if(paciente.getRitmoCardiaco()+variacao < 0) {
			throw new IllegalArgumentException("O valor informado � inv�lido");
		}
		
		paciente.setRitmoCardiaco(paciente.getRitmoCardiaco()+variacao); //altera o ritmo cardiaco do paciente
	}
	
	/**
	 * Altera o valor da press�o sangu�nea do paciente.
	 * @param paciente paciente do qual a press�o sangu�nea ser� alterada
	 * @param variacao valor a ser variado na press�o sangu�nea; caso seja positivo, a press�o sangu�nea aumentar�, caso contr�rio a press�o sangu�nea ser� diminuida.
	 * @throws IllegalArgumentException - caso o valor de press�o sangu�nea informado resulte em press�o sangu�nea negativa
	 */
	public void alterarPressaoSanguinea(Paciente paciente, float variacao) throws IllegalArgumentException {
		if(paciente.getPressaoSanguinea()+variacao < 0) {
			throw new IllegalArgumentException("O valor informado � inv�lido");
		}
		
		paciente.setPressaoSanguinea(paciente.getPressaoSanguinea()+variacao); //altera a press�o sanguinea do paciente
	}
}
