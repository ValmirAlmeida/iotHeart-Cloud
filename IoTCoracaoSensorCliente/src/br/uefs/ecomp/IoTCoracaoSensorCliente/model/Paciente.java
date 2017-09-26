package br.uefs.ecomp.IoTCoracaoSensorCliente.model;

import java.io.Serializable;

public class Paciente implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 537212919386723779L;
	private String nome;
	private int id;
	private boolean movimentacao;
	private float ritmoCardiaco;
	private float pressaoSanguinea;
	private static int contadorPacientes = 0; 

	public Paciente(String nome, boolean movimentacao, float ritmoCardiaco, float pressaoSanguinea) {
		this.nome = nome;
		this.setId(contadorPacientes++);
		this.movimentacao = movimentacao;
		this.ritmoCardiaco = ritmoCardiaco;
		this.pressaoSanguinea = pressaoSanguinea;
	}
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public boolean isMovimentacao() {
		return movimentacao;
	}
	public void setMovimentacao(boolean movimentacao) {
		this.movimentacao = movimentacao;
	}
	public float getRitmoCardiaco() {
		return ritmoCardiaco;
	}
	public void setRitmoCardiaco(float ritmoCardiaco) {
		this.ritmoCardiaco = ritmoCardiaco;
	}
	public float getPressaoSanguinea() {
		return pressaoSanguinea;
	}
	public void setPressaoSanguinea(float pressaoSanguinea) {
		this.pressaoSanguinea = pressaoSanguinea;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	

}
