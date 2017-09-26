package br.uefs.ecomp.IoTCoracaoServidor.util;


import java.io.Serializable;

public class Mensagem implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String comando;
	private Object dados;
	
	public Mensagem(String comando, Object dados) {
		this.setComando(comando);
		this.setDados(dados);
	}

	public String getComando() {
		return comando;
	}

	public void setComando(String comando) {
		this.comando = comando;
	}

	public Object getDados() {
		return dados;
	}

	public void setDados(Object dados) {
		this.dados = dados;
	}

}
