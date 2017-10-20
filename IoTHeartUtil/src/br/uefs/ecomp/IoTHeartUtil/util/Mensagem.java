package br.uefs.ecomp.IoTHeartUtil.util;

import java.io.Serializable;

/**
 * Classe que representa uma mensagem que pode ser enviada ou recebida no contrexto da comunicação entre
 * cliente(s) e servidor no sistema.
 * @author Vinicius
 *
 */
public class Mensagem implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int acaoProtocolo;
	private Object dados;
	private String alvo;
	
	public Mensagem(int acaoProtocolo, Object dados) {
		this.setAcaoProtocolo(acaoProtocolo);
		this.setDados(dados);
	}
	
	public Mensagem(int acaoProtocolo, Object dados, String alvo) {
		this.setAcaoProtocolo(acaoProtocolo);
		this.setDados(dados);
		this.setAlvo(alvo);
	}
	
	public Mensagem(int acaoProtocolo, String alvo) {
		this.setAcaoProtocolo(acaoProtocolo);
		this.setAlvo(alvo);
	}

	public Object getDados() {
		return dados;
	}

	public void setDados(Object dados) {
		this.dados = dados;
	}

	public String getAlvo() {
		return alvo;
	}

	public void setAlvo(String alvo) {
		this.alvo = alvo;
	}

	public int getAcaoProtocolo() {
		return acaoProtocolo;
	}


	public void setAcaoProtocolo(int acaoProtocolo) {
		this.acaoProtocolo = acaoProtocolo;
	}

}
