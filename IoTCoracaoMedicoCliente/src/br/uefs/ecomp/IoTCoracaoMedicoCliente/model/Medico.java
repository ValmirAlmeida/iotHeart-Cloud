package br.uefs.ecomp.IoTCoracaoMedicoCliente.model;

import java.io.Serializable;

public class Medico implements Serializable {
	
	private String nome;
	private String CRM;
	private String email;
	private String senha;
	
	public Medico(String nome, String CRM, String email, String senha) {
		this.nome = nome;
		this.CRM = CRM;
		this.email = email;
		this.senha = senha;
	}
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getCRM() {
		return CRM;
	}
	public void setCRM(String cRM) {
		CRM = cRM;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}

}
