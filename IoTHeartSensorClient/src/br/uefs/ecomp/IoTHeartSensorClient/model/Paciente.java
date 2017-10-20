package br.uefs.ecomp.IoTHeartSensorClient.model;

import java.io.Serializable;

/**
 * Classe responsável por modelar um paciente no contexto do sistema.
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
	 * Obtém uma nova instância de paciente
	 * @param nome nome do pacientre
	 * @param cpf cpf do paciente
	 * @param movimentacao <code>true</code>, se o paciente estiver movendo-se; <code>false</code>, caso contrário
	 * @param ritmoCardiaco ritmo cardíaco do paciente
	 * @param pressaoSanguineaMax pressão sanguínea máxima (sistólica) do paciente
	 * @param pressaoSanguineaMin pressão sanguínea mínima (diastólica) do paciente
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
	 * Obtém o nome do paciente 
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
	 * Indica se o paciente está movendo-se
	 * @return <code>true</code>, se o paciente estiver movendo-se; <code>false</code>, caso contrário
	 */
	public boolean isMovimentacao() {
		return movimentacao;
	}
	
	/**
	 * Configura movimentação do paciente 
	 * @param movimentacao <code>true</code>, se o paciente estiver movendo-se; <code>false</code>, caso contrário
	 */
	public void setMovimentacao(boolean movimentacao) {
		this.movimentacao = movimentacao;
	}
	
	/**
	 * Obtém o ritmo cardíaco do paciente
	 * @return ritmo cardíaco do paciente
	 */
	public double getRitmoCardiaco() {
		return ritmoCardiaco;
	}
	
	/**
	 * Configura o ritmo cardíaco do paciente
	 * @param ritmoCardiaco ritmo cardíaco do paciente
	 */
	public void setRitmoCardiaco(double ritmoCardiaco) {
		this.ritmoCardiaco = ritmoCardiaco;
	}
	
	/**
	 * Obtém a pressão sanguínea completa do paciente
	 * @return array contendo a pressão mínima (diastolica) na primeira posição e a pressão máxima (sistolica) na segunda
	 */
	public double[] getPressaoSanguinea() {
		return pressaoSanguinea;
	}
	
	/**
	 * Configura a pressão sanguínea
	 * @param pressaoSanguinea valor da pressão
	 * @param isMaxima <code>true</code>, se a pressão for máxima; <code>false</code>, caso contrário
	 */
	public void setPressaoSanguinea(double pressaoSanguinea, boolean isMaxima) {
		if(isMaxima) {
			this.pressaoSanguinea[1] = pressaoSanguinea;
		} else {
			this.pressaoSanguinea[0] = pressaoSanguinea;
		}
	}
	
	/**
	 * Configura a pressão sanguínea
	 * @param pressaoSanguinea array contendo a pressão mínima (diastolica) na primeira posição e a pressão máxima (sistolica) na segunda
	 */
	public void setPressaoSanguinea(double[] pressaoSanguinea) {
		this.pressaoSanguinea = pressaoSanguinea;
	}
	
	/**
	 * Obtém o cpf do paciente
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
	 * Indica se o paciente está em perigo
	 * @return <code>true</code>, se o paciente estiver em perigo; <code>false</code>, caso contrário
	 */
	public boolean isEmPerigo() {
		return emPerigo;
	}

	/**
	 * Configura se o paciente está em perigo 
	 * @param emPerigo <code>true</code>, se o paciente estiver em perigo; <code>false</code>, caso contrário
	 */
	public void setEmPerigo(boolean emPerigo) {
		this.emPerigo = emPerigo;
	}
}
