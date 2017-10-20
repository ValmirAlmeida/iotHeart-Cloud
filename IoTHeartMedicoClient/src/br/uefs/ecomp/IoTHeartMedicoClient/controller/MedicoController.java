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
 * Classe responsável por gerenciar as ações que um cliente médico pode realizar no sistema
 * @author Valmir Vinicius
 */
public class MedicoController {
    /** IP do servidor ao qual o médico se conectará */
	private InetAddress ipServidor;
    /** Porta do processo a qual o médico se conectará */
	private int portaProcessoServidor;
    /** Painel gerenciado pelo médico */
	private PainelDeControle painel;

	/**
	 * Obtém o IP do servidor ao qual o médico está conectado
	 * @return IP do servidor
	 */
	public InetAddress getIpServidor() {
		return ipServidor;
	}
	
	/**
	 * Obtém a porta do processo ao qual o médico está conectado
	 * @return porta do processo ao qual o médico está conectado
	 */
	public int getPortaProcesso() {
		return portaProcessoServidor;
	}
	
	/**
	 * Obtém a instância do painel de controle que um médico está utilizando
	 * @return instância do painel de controle que um médico está utilizando
	 */
	public PainelDeControle getPainel() {
		return painel;
	}
	
	/**
	 * Obtém uma instância do controller do médico
	 */
	public MedicoController()  {
		//define as configurações inicias da classe
		ipServidor = null;
		portaProcessoServidor = -1;
	};
	
	
	/**
	 * Define o caminho do servidor ao qual o médico poderá conectar-se
	 * @param ipServidor ip do servidor
	 * @param portaProcessoServidor porta do processo na qual o médico pode conectar-se
	 */
	public void definirCaminhoServidor(InetAddress ipServidor, int portaProcessoServidor) {
		this.ipServidor = ipServidor;
		this.portaProcessoServidor = portaProcessoServidor;
	}
	
	/**
	 * Inicia o plantão de um médico, criando um painel próprio para ele 
	 * @param crm crm do médico
	 * @param senhaMedico senha do médico 
	 * @throws ComunicacaoImpossivelException caso o servidor não tenha sido iniciado
	 * @throws ClassNotFoundException caso alguma classe fundamental à execução não seja encontrada
	 * @throws IOException caso ocorra algum erro durante a entrada ou saída de dados na comunicação
	 * @throws LoginNegadoException caso o crm e/ou senha informados sejam incorretos
	 * @throws MedicoJaExisteException caso o médico que deseja logar já esteja conectado
	 */
	public void iniciarPlantao(String crm, String senhaMedico) throws ComunicacaoImpossivelException, ClassNotFoundException, IOException, LoginNegadoException, MedicoJaExisteException {
		if(ipServidor == null || portaProcessoServidor == -1) { //caso o endereço do servidor ainda não esteja definido
			throw new ComunicacaoImpossivelException();
		}
		
		if(crm == null || senhaMedico == null || crm.trim().compareTo("") == 0 || senhaMedico.trim().compareTo("") == 0) { //caso sejam inseridos dados em formatos incorretos ou faltando informações
			throw new IllegalArgumentException("Um ou mais dados estão ausentes ou foram informados incorretamente");
		}
		
		if(painel != null) { //caso o médico em questão já esteja conectado
			throw new MedicoJaExisteException();
		}
		
		painel = new PainelDeControle(ipServidor, portaProcessoServidor); //cria uma nova instância de painel
		painel.loginMedico(crm, senhaMedico); //tenta realizar o login do médico
		
		if(!painel.isConectado()) { //caso as informações do médico estejam incorretas e o login não seja permitido
			throw new LoginNegadoException();
		}
	}
	
	/**
	 * Finaliza o plantão do médico associado a esta instância de controller
	 * @throws ClassNotFoundException caso alguma classe fundamental à execução não seja encontrada
	 * @throws IOException caso ocorra algum erro de entrada ou saída
	 * @throws PainelNaoIniciadoException caso o painel ainda não tenha sido iniciado
	 */
	public void finalizarPlantao() throws ClassNotFoundException, IOException, PainelNaoIniciadoException {
		if(painel != null && painel.getMedicoResponsavel() != null) { //caso o painel do médico ainda não tenha sido iniciado
			painel.logoutMedico(); //solicita o logout do médico
			if(painel.isConectado()) { //caso o logout não tenha sido bem sucedido, indicando que o médico não havia logado anteriormente
				throw new PainelNaoIniciadoException();
			}
			painel = null; //torna o painel nulo após a finalização do plantão
		} else { //caso o painel ainda não tenha sido iniciado
			throw new PainelNaoIniciadoException();
		}
	}
	
	/**
	 * Inicia o monitoramento de um paciente
	 * @param cpf cpf do paciente que será monitorado
	 * @throws IOException caso ocorra algum erro de entrada ou saída
	 * @throws PainelNaoIniciadoException caso o painel ainda não tenha sido iniciado
	 * @throws ClassNotFoundException caso alguma classe fundamental à execução não seja encontrada
	 * @throws PacienteNaoExisteException caso o cpf informado esteja associado a um paciente existente no sistema
	 */
	public void iniciarMonitoramentoDePaciente(String cpf) throws IOException, PainelNaoIniciadoException, ClassNotFoundException, PacienteNaoExisteException {
		if(painel != null && painel.getMedicoResponsavel() != null) { //caso o painel tenha sido iniciado
			if(!painel.definirPacienteMonitorado(cpf)) { //solicita o inicio do monitoramento ao painel e verifica se ele não foi bem sucedido
				throw new PacienteNaoExisteException();
			}
		} else { //caso o painel não tenha sido iniciado
			throw new PainelNaoIniciadoException();
		}
	}
}
