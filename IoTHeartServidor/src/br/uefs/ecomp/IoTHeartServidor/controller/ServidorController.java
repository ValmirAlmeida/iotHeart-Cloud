package br.uefs.ecomp.IoTHeartServidor.controller;

import java.io.IOException;
import br.uefs.ecomp.IoTHeartServidor.model.Servidor;

/**
 * Classe respons�vel por gerenciar as a��es do servidor no sistema
 * @author Valmir Vinicius
 */
public class ServidorController {
    /** Porta do processo */
	private int portaProcesso;
    /** Inst�ncia do servidor */
	private Servidor servidor;
	
	/**
	 * Obt�m a porta do processo
	 * @return porta do processo
	 */
	public int getPortaProcesso() {
		return portaProcesso;
	}
	
	/**
	 * Define a porta do processo
	 * @param portaProcessoServidor porta do processo 
	 */
	public void definirPortaDoProcesso(int portaProcessoServidor) {
		this.portaProcesso = portaProcessoServidor;
	}
	
	/**
	 * Inicia a execu��o do servidor
	 * @throws ClassNotFoundException caso alguma classe fundamental � execu��o n�o seja encontrada
	 * @throws IOException caso ocorra algum erro durante a entrada ou sa�da de dados na comunica��o
	 */
	public void executar() throws ClassNotFoundException, IOException {
		servidor = new Servidor(portaProcesso); //obt�m uma inst�ncia de servidor
		servidor.executar(); //inicia o servidor
	}
	
	/**
	 * Solicita que o servidor pare de executar
	 * @return <code>true</code>, caso n�o ocorra erro na finaliza��o do sensor por conta dele n�o estar iniciado; <code>false</code>, caso contr�rio
	 * @throws IOException caso ocorra algum erro durante a entrada ou sa�da de dados na comunica��o
	 */
	public boolean pararServidor() throws IOException {
		if(servidor != null && servidor.isExecutando()) { //caso o servidor esteja realmente sendo executado
			servidor.pararTCP(); //para a sess�o TCP do servidor
			servidor.pararUDP(); //para a sess�o UDP do servidor
			return true;
		} 
		
		return false;
	}
	
	/**
	 * Verifica se o servidor est� sendo executado
	 * @return <code>true</code>, caso o servidor esteja ativo; <code>false</code>, caso contr�rio
	 */
	public boolean isExecutando() {
		return servidor.isExecutando();
	}
}