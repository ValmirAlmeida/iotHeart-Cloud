package br.uefs.ecomp.IoTHeartMedicoClient.controller;

import java.io.IOException;
import java.net.InetAddress;
import br.uefs.ecomp.IoTHeartMedicoClient.exceptions.LoginNegadoException;
import br.uefs.ecomp.IoTHeartMedicoClient.exceptions.MedicoJaExisteException;
import br.uefs.ecomp.IoTHeartMedicoClient.exceptions.PacienteNaoExisteException;
import br.uefs.ecomp.IoTHeartMedicoClient.exceptions.PainelNaoIniciadoException;
import br.uefs.ecomp.IoTHeartMedicoClient.model.PainelDeControle;
import br.uefs.ecomp.IoTHeartUtil.util.ComunicacaoImpossivelException;

/**
 * Classe respons�vel por gerenciar as a��es que um cliente m�dico pode realizar no sistema
 * @author Valmir Vinicius
 */
public class MedicoController {
    /** IP do servidor ao qual o m�dico se conectar� */
	private InetAddress ipServidor;
    /** Porta do processo a qual o m�dico se conectar� */
	private int portaProcessoServidor;
    /** Painel gerenciado pelo m�dico */
	private PainelDeControle painel;

	/**
	 * Obt�m o IP do servidor ao qual o m�dico est� conectado
	 * @return IP do servidor
	 */
	public InetAddress getIpServidor() {
		return ipServidor;
	}
	
	/**
	 * Obt�m a porta do processo ao qual o m�dico est� conectado
	 * @return porta do processo ao qual o m�dico est� conectado
	 */
	public int getPortaProcesso() {
		return portaProcessoServidor;
	}
	
	/**
	 * Obt�m a inst�ncia do painel de controle que um m�dico est� utilizando
	 * @return inst�ncia do painel de controle que um m�dico est� utilizando
	 */
	public PainelDeControle getPainel() {
		return painel;
	}
	
	/**
	 * Obt�m uma inst�ncia do controller do m�dico
	 */
	public MedicoController()  {
		//define as configura��es inicias da classe
		ipServidor = null;
		portaProcessoServidor = -1;
	};
	
	
	/**
	 * Define o caminho do servidor ao qual o m�dico poder� conectar-se
	 * @param ipServidor ip do servidor
	 * @param portaProcessoServidor porta do processo na qual o m�dico pode conectar-se
	 */
	public void definirCaminhoServidor(InetAddress ipServidor, int portaProcessoServidor) {
		this.ipServidor = ipServidor;
		this.portaProcessoServidor = portaProcessoServidor;
	}
	
	/**
	 * Inicia o plant�o de um m�dico, criando um painel pr�prio para ele 
	 * @param crm crm do m�dico
	 * @param senhaMedico senha do m�dico 
	 * @throws ComunicacaoImpossivelException caso o servidor n�o tenha sido iniciado
	 * @throws ClassNotFoundException caso alguma classe fundamental � execu��o n�o seja encontrada
	 * @throws IOException caso ocorra algum erro durante a entrada ou sa�da de dados na comunica��o
	 * @throws LoginNegadoException caso o crm e/ou senha informados sejam incorretos
	 * @throws MedicoJaExisteException caso o m�dico que deseja logar j� esteja conectado
	 */
	public void iniciarPlantao(String crm, String senhaMedico) throws ComunicacaoImpossivelException, ClassNotFoundException, IOException, LoginNegadoException, MedicoJaExisteException {
		if(ipServidor == null || portaProcessoServidor == -1) { //caso o endere�o do servidor ainda n�o esteja definido
			throw new ComunicacaoImpossivelException();
		}
		
		if(crm == null || senhaMedico == null || crm.trim().compareTo("") == 0 || senhaMedico.trim().compareTo("") == 0) { //caso sejam inseridos dados em formatos incorretos ou faltando informa��es
			throw new IllegalArgumentException("Um ou mais dados est�o ausentes ou foram informados incorretamente");
		}
		
		if(painel != null) { //caso o m�dico em quest�o j� esteja conectado
			throw new MedicoJaExisteException();
		}
		
		painel = new PainelDeControle(ipServidor, portaProcessoServidor); //cria uma nova inst�ncia de painel
		painel.loginMedico(crm, senhaMedico); //tenta realizar o login do m�dico
		
		if(!painel.isConectado()) { //caso as informa��es do m�dico estejam incorretas e o login n�o seja permitido
			throw new LoginNegadoException();
		}
	}
	
	/**
	 * Finaliza o plant�o do m�dico associado a esta inst�ncia de controller
	 * @throws ClassNotFoundException caso alguma classe fundamental � execu��o n�o seja encontrada
	 * @throws IOException caso ocorra algum erro de entrada ou sa�da
	 * @throws PainelNaoIniciadoException caso o painel ainda n�o tenha sido iniciado
	 */
	public void finalizarPlantao() throws ClassNotFoundException, IOException, PainelNaoIniciadoException {
		if(painel != null && painel.getMedicoResponsavel() != null) { //caso o painel do m�dico ainda n�o tenha sido iniciado
			painel.logoutMedico(); //solicita o logout do m�dico
			if(painel.isConectado()) { //caso o logout n�o tenha sido bem sucedido, indicando que o m�dico n�o havia logado anteriormente
				throw new PainelNaoIniciadoException();
			}
			painel = null; //torna o painel nulo ap�s a finaliza��o do plant�o
		} else { //caso o painel ainda n�o tenha sido iniciado
			throw new PainelNaoIniciadoException();
		}
	}
	
	/**
	 * Inicia o monitoramento de um paciente
	 * @param cpf cpf do paciente que ser� monitorado
	 * @throws IOException caso ocorra algum erro de entrada ou sa�da
	 * @throws PainelNaoIniciadoException caso o painel ainda n�o tenha sido iniciado
	 * @throws ClassNotFoundException caso alguma classe fundamental � execu��o n�o seja encontrada
	 * @throws PacienteNaoExisteException caso o cpf informado esteja associado a um paciente existente no sistema
	 */
	public void iniciarMonitoramentoDePaciente(String cpf) throws IOException, PainelNaoIniciadoException, ClassNotFoundException, PacienteNaoExisteException {
		if(painel != null && painel.getMedicoResponsavel() != null) { //caso o painel tenha sido iniciado
			if(!painel.definirPacienteMonitorado(cpf)) { //solicita o inicio do monitoramento ao painel e verifica se ele n�o foi bem sucedido
				throw new PacienteNaoExisteException();
			}
		} else { //caso o painel n�o tenha sido iniciado
			throw new PainelNaoIniciadoException();
		}
	}
}
