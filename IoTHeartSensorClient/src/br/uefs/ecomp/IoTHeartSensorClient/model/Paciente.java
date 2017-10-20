package br.uefs.ecomp.IoTHeartSensorClient.model;

import java.io.Serializable;

/**
 * Classe respons�vel por modelar um paciente no contexto do sistema.
 * @author Valmir Vinicius
 */
public class Paciente implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 537212919386723779L;
	private String nome;
	private String cpf;
	private boolean movimentacao;
	private double ritmoCardiaco;
	private double pressaoSanguinea[];
	private boolean emPerigo;

	/**
	 * Obt�m uma nova inst�ncia de paciente
	 * @param nome nome do pacientre
	 * @param cpf cpf do paciente
	 * @param movimentacao <code>true</code>, se o paciente estiver movendo-se; <code>false</code>, caso contr�rio
	 * @param ritmoCardiaco ritmo card�aco do paciente
	 * @param pressaoSanguineaMax press�o sangu�nea m�xima (sist�lica) do paciente
	 * @param pressaoSanguineaMin press�o sangu�nea m�nima (diast�lica) do paciente
	 */
	public Paciente(String nome, String cpf, boolean movimentacao, double ritmoCardiaco, double pressaoSanguineaMax, double pressaoSanguineaMin) {
		this.nome = nome;
		this.cpf = cpf;
		this.movimentacao = movimentacao;
		this.ritmoCardiaco = ritmoCardiaco;
		this.pressaoSanguinea = new double[2];
		this.pressaoSanguinea[0] = pressaoSanguineaMin;
		this.pressaoSanguinea[1] = pressaoSanguineaMax;
		this.emPerigo = false;
	}
	
	/**
	 * Obt�m o nome do paciente 
	 * @return nome do paciente
	 */
	public String getNome() {
		return nome;
	}
	
	/**
	 * Configura o nome do paciente
	 * @param nome nome do paciente
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	/**
	 * Indica se o paciente est� movendo-se
	 * @return <code>true</code>, se o paciente estiver movendo-se; <code>false</code>, caso contr�rio
	 */
	public boolean isMovimentacao() {
		return movimentacao;
	}
	
	/**
	 * Configura movimenta��o do paciente 
	 * @param movimentacao <code>true</code>, se o paciente estiver movendo-se; <code>false</code>, caso contr�rio
	 */
	public void setMovimentacao(boolean movimentacao) {
		this.movimentacao = movimentacao;
	}
	
	/**
	 * Obt�m o ritmo card�aco do paciente
	 * @return ritmo card�aco do paciente
	 */
	public double getRitmoCardiaco() {
		return ritmoCardiaco;
	}
	
	/**
	 * Configura o ritmo card�aco do paciente
	 * @param ritmoCardiaco ritmo card�aco do paciente
	 */
	public void setRitmoCardiaco(double ritmoCardiaco) {
		this.ritmoCardiaco = ritmoCardiaco;
	}
	
	/**
	 * Obt�m a press�o sangu�nea completa do paciente
	 * @return array contendo a press�o m�nima (diastolica) na primeira posi��o e a press�o m�xima (sistolica) na segunda
	 */
	public double[] getPressaoSanguinea() {
		return pressaoSanguinea;
	}
	
	/**
	 * Configura a press�o sangu�nea
	 * @param pressaoSanguinea valor da press�o
	 * @param isMaxima <code>true</code>, se a press�o for m�xima; <code>false</code>, caso contr�rio
	 */
	public void setPressaoSanguinea(double pressaoSanguinea, boolean isMaxima) {
		if(isMaxima) {
			this.pressaoSanguinea[1] = pressaoSanguinea;
		} else {
			this.pressaoSanguinea[0] = pressaoSanguinea;
		}
	}
	
	/**
	 * Configura a press�o sangu�nea
	 * @param pressaoSanguinea array contendo a press�o m�nima (diastolica) na primeira posi��o e a press�o m�xima (sistolica) na segunda
	 */
	public void setPressaoSanguinea(double[] pressaoSanguinea) {
		this.pressaoSanguinea = pressaoSanguinea;
	}
	
	/**
	 * Obt�m o cpf do paciente
	 * @return cpf do paciente
	 */
	public String getCpf() {
		return cpf;
	}

	/**
	 * Configura o cpf do paciente
	 * @param cpf cpf do paciente
	 */
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	
	/**
	 * Indica se o paciente est� em perigo
	 * @return <code>true</code>, se o paciente estiver em perigo; <code>false</code>, caso contr�rio
	 */
	public boolean isEmPerigo() {
		return emPerigo;
	}

	/**
	 * Configura se o paciente est� em perigo 
	 * @param emPerigo <code>true</code>, se o paciente estiver em perigo; <code>false</code>, caso contr�rio
	 */
	public void setEmPerigo(boolean emPerigo) {
		this.emPerigo = emPerigo;
	}
}
