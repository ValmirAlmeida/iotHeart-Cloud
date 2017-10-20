package br.uefs.ecomp.IoTHeartMedicoClient.model;

import java.io.Serializable;

/**
 * Classe respons�vel por modelar um m�dico no contexto do sistema.
 * @author Valmir Vinicius
 */
public class Medico implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/** Nome do m�dico */
	private String nome;
    /** CRM do m�dico */
	private String CRM;
    /** Senha do m�dico */
	private String senha;
	
	/**
	 * Obt�m uma inst�ncia de m�dico
	 * @param nome nome do m�dico
	 * @param CRM crm do m�dico
	 * @param senha senha do m�dico
	 */
	public Medico(String nome, String CRM, String senha) {
		this.nome = nome;
		this.CRM = CRM;
		this.senha = senha;
	}
	
	/**
	 * Obt�m o nome do m�dico
	 * @return nome do m�dico
	 */
	public String getNome() {
		return nome;
	}
	
	/**
	 * Configura o nome do m�dico
	 * @param nome nome do m�dico
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	/**
	 * Obt�m o CRM do m�dico
	 * @return CRM do m�dico
	 */
	public String getCRM() {
		return CRM;
	}
	
	/**
	 * Configura o CRM do m�dico
	 * @param CRM CRM do m�dico
	 */
	public void setCRM(String CRM) {
		this.CRM = CRM;
	}
	
	/**
	 * Obt�m a senha do m�dico
	 * @return senha do m�dico
	 */
	public String getSenha() {
		return senha;
	}
	
	/**
	 * Configura a senha do m�dico
	 * @param senha senha do m�dico
	 */
	public void setSenha(String senha) {
		this.senha = senha;
	}

}
