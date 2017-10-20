package br.uefs.ecomp.IoTHeartServidor.controller;

import java.io.IOException;
import br.uefs.ecomp.IoTHeartServidor.model.Servidor;

/**
 * Classe responsável por gerenciar as ações do servidor no sistema
 * @author Valmir Vinicius
 */
public class ServidorController {
    /** Porta do processo */
	private int portaProcesso;
    /** Instância do servidor */
	private Servidor servidor;
	
	/**
	 * Obtém a porta do processo
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
	 * Inicia a execução do servidor
	 * @throws ClassNotFoundException caso alguma classe fundamental à execução não seja encontrada
	 * @throws IOException caso ocorra algum erro durante a entrada ou saída de dados na comunicação
	 */
	public void executar() throws ClassNotFoundException, IOException {
		servidor = new Servidor(portaProcesso); //obtém uma instância de servidor
		servidor.executar(); //inicia o servidor
	}
	
	/**
	 * Solicita que o servidor pare de executar
	 * @return <code>true</code>, caso não ocorra erro na finalização do sensor por conta dele não estar iniciado; <code>false</code>, caso contrário
	 * @throws IOException caso ocorra algum erro durante a entrada ou saída de dados na comunicação
	 */
	public boolean pararServidor() throws IOException {
		if(servidor != null && servidor.isExecutando()) { //caso o servidor esteja realmente sendo executado
			servidor.pararTCP(); //para a sessão TCP do servidor
			servidor.pararUDP(); //para a sessão UDP do servidor
			return true;
		} 
		
		return false;
	}
	
	/**
	 * Verifica se o servidor está sendo executado
	 * @return <code>true</code>, caso o servidor esteja ativo; <code>false</code>, caso contrário
	 */
	public boolean isExecutando() {
		return servidor.isExecutando();
	}
}