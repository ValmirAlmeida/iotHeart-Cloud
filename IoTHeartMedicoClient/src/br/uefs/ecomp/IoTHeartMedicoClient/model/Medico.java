package br.uefs.ecomp.IoTHeartMedicoClient.model;

import java.io.Serializable;

/**
 * Classe responsável por modelar um médico no contexto do sistema.
 * @author Valmir Vinicius
 */
public class Medico implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/** Nome do médico */
	private String nome;
    /** CRM do médico */
	private String CRM;
    /** Senha do médico */
	private String senha;
	
	/**
	 * Obtém uma instância de médico
	 * @param nome nome do médico
	 * @param CRM crm do médico
	 * @param senha senha do médico
	 */
	public Medico(String nome, String CRM, String senha) {
		this.nome = nome;
		this.CRM = CRM;
		this.senha = senha;
	}
	
	/**
	 * Obtém o nome do médico
	 * @return nome do médico
	 */
	public String getNome() {
		return nome;
	}
	
	/**
	 * Configura o nome do médico
	 * @param nome nome do médico
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	/**
	 * Obtém o CRM do médico
	 * @return CRM do médico
	 */
	public String getCRM() {
		return CRM;
	}
	
	/**
	 * Configura o CRM do médico
	 * @param CRM CRM do médico
	 */
	public void setCRM(String CRM) {
		this.CRM = CRM;
	}
	
	/**
	 * Obtém a senha do médico
	 * @return senha do médico
	 */
	public String getSenha() {
		return senha;
	}
	
	/**
	 * Configura a senha do médico
	 * @param senha senha do médico
	 */
	public void setSenha(String senha) {
		this.senha = senha;
	}

}
