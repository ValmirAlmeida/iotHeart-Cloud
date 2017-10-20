package br.uefs.ecomp.IoTHeartSensorClient.controller;

import java.io.IOException;
import java.net.InetAddress;

import br.uefs.ecomp.IoTHeartSensorClient.exceptions.PacienteJaConectadoException;
import br.uefs.ecomp.IoTHeartSensorClient.exceptions.PacienteNaoConectadoException;
import br.uefs.ecomp.IoTHeartSensorClient.model.Paciente;
import br.uefs.ecomp.IoTHeartSensorClient.model.Sensor;
import br.uefs.ecomp.IoTHeartUtil.util.ComunicacaoImpossivelException;

/**
 * Classe respons�vel por gerenciar as a��es executadas pelo sensor no sistema.
 * @author Valmir Vinicius
 */
public class SensorController {
    /** IP do servidor ao qual o sensor est� conectado ou se conectar� */
	private InetAddress ipServidor;
    /** Porta do processo na qual o sensor est� conectado ou se conectar� */
	private int portaProcesso;
    /** Inst�ncia do sensor em atividade no momento */
	private Sensor sensorAtivo;
	
	/**
	 * Obt�m o ip do servidor
	 * @return ip do servidor
	 */
	public InetAddress getIpServidor() {
		return ipServidor;
	}
	
	/**
	 * Obt�m a porta do processo ao qual o sensor est� conectado
	 * @return porta do processo
	 */
	public int getPortaProcesso() {
		return portaProcesso;
	}
	
	/**
	 * Obt�m uma inst�ncia do controller do sensor
	 */
	public SensorController()  {
		//define valores iniciais e inv�lidos para o ip e a porta do processo
		ipServidor = null;
		portaProcesso = -1;
	};

	/**
	 * Define o caminho, isto � ip e porta do processo
	 * @param ipServidor ip do servidor
	 * @param portaProcessoServidor porta do processo no servidor
	 */
	public void definirCaminhoServidor(InetAddress ipServidor, int portaProcessoServidor) {
		this.ipServidor = ipServidor;
		this.portaProcesso = portaProcessoServidor;
	}
	
	/**
	 * Ativa um novo sensor 
	 * @param nomePacienteMonitorado nome do paciente associado ao sensor
	 * @param cpf cpf do sensor
	 * @param movimentacaoInicial valor inicial do sensor de movimenta��o
	 * @param ritmoCardiacoInicial ritmo cardiaco inicial do paciente
	 * @param pressaoSanguineaMax press�o sanguinea m�xima inicial do paciente
	 * @param pressaoSanguineaMin press�o sanguinea m�nima inicial do paciente
	 * @return <code>true</code>, caso o sensor seja ativado normalmente; <code>false</code>, caso ocorra algum erro durante a ativa��o do sensor
	 * @throws IOException caso ocorra algum erro durante a entrada ou sa�da de dados na comunica��o
	 * @throws IllegalArgumentException caso algum dos argumentos informados seja incorreto/inv�lido
	 * @throws ClassNotFoundException caso alguma classe fundamental � execu��o n�o seja encontrada
	 * @throws ComunicacaoImpossivelException caso o servidor n�o tenha sido iniciado
	 * @throws PacienteJaConectadoException caso o cpf informado esteja relacionado a um paciente j� conectado
	 */
	public boolean ativarSensor(String nomePacienteMonitorado, String cpf, boolean movimentacaoInicial, double ritmoCardiacoInicial, double pressaoSanguineaMax, double pressaoSanguineaMin) throws IOException, IllegalArgumentException, ClassNotFoundException, ComunicacaoImpossivelException, PacienteJaConectadoException {
		if(ipServidor == null || portaProcesso == -1) { //caso o ip e a porta do processo estejam inicialmente com valores inv�lidos
			throw new ComunicacaoImpossivelException();
		}

		if(nomePacienteMonitorado == null || cpf == null || ritmoCardiacoInicial < 0 || pressaoSanguineaMax < 0 || pressaoSanguineaMin < 0) { //caso algum dos valores informados esteja em formado/seja incorreto
			throw new IllegalArgumentException("Um ou mais dados est�o ausentes ou foram informados incorretamente");
		}

		Paciente paciente = new Paciente(nomePacienteMonitorado, cpf, movimentacaoInicial, ritmoCardiacoInicial, pressaoSanguineaMax, pressaoSanguineaMin); //cria uma nova inst�ncia de paciente com base nos dados informados
		sensorAtivo = new Sensor(paciente, ipServidor, portaProcesso); //cria uma nova inst�ncia de sensor com base no paciente criado e a define como sensor desse controller
		sensorAtivo.iniciarSensor(); //inicia o sensor
		
		if(!sensorAtivo.isConectado()) { //caso o sensor n�o esteja conectado
			throw new PacienteJaConectadoException();
		}
		
		return sensorAtivo.isConectado(); //indica se o processo ocorreu corretamente
	}
	
	/**
	 * Realiza o desativamento de um sensor
	 * @param cpf cpf do paciente associado ao sensor que se deseja desativar
	 * @return <code>true</code>, caso o processo ocorra normalmente; <code>false</code>, caso contr�rio
	 * @throws ClassNotFoundException caso alguma classe fundamental � execu��o n�o seja encontrada
	 * @throws IOException caso ocorra algum erro durante a entrada ou sa�da de dados na comunica��o
	 */
	public boolean desativarSensor(String cpf) throws ClassNotFoundException, IOException {
		sensorAtivo.desativarSensor(); //faz com que o sensor desativa
		
		if(!sensorAtivo.isConectado()) { //verifica se o sensor est� realmente inativo
			sensorAtivo = null; //faz com que a refer�ncia de  sensor do controller seja nula
			return true;
		} 
		
		return false; //retorna falso indicando que o processo n�o ocorreu corretamente
	}
	
	/**
	 * Realiza a altera��o da movimenta��o do paciente
	 * @param isMovimentacao <code>true</code>, caso o paciente esteja em movimento; <code>false</code>, caso contr�rio
	 * @throws IOException caso ocorra algum erro durante a entrada ou sa�da de dados na comunica��o
	 * @throws PacienteNaoConectadoException caso n�o exista um paciente conectado
	 */
	public void alterarMovimentacao(boolean isMovimentacao) throws IOException, PacienteNaoConectadoException {
		if(sensorAtivo != null && sensorAtivo.isConectado()) { //caso o sensor esteja realmente conectado
			sensorAtivo.alteraMovimentacao(isMovimentacao); //realiza a altera��o efetiva da movimenta��o
		} else {
			throw new PacienteNaoConectadoException(); 
		}
	}
	
	/**
	 * 
	 * @param batimentos
	 * @throws IOException caso ocorra algum erro durante a entrada ou sa�da de dados na comunica��o
	 * @throws PacienteNaoConectadoException caso n�o exista um paciente conectado
	 */
	public void alterarBatimentosCardiacos(double batimentos) throws IOException, PacienteNaoConectadoException {
		if(sensorAtivo != null && sensorAtivo.isConectado()) { //caso o sensor esteja realmente conectado
			sensorAtivo.alteraRitmoCardiaco(batimentos); //realiza a altera��o efetiva da movimenta��o
		} else {
			throw new PacienteNaoConectadoException();
		}
	}
	
	/**
	 * 
	 * @param pressao
	 * @param isMaxima
	 * @throws IOException caso ocorra algum erro durante a entrada ou sa�da de dados na comunica��o
	 * @throws PacienteNaoConectadoException caso n�o exista um paciente conectado
	 */
	public void alterarPressao(double pressao, boolean isMaxima) throws IOException, PacienteNaoConectadoException {
		if(sensorAtivo != null && sensorAtivo.isConectado()) { //caso o sensor esteja realmente conectado
			sensorAtivo.alterarPressaoSanguinea(pressao, isMaxima); //realiza a altera��o efetiva da movimenta��o
		} else {
			throw new PacienteNaoConectadoException();
		}
	}
	
	/**
	 * Verifica se o sensor est� conectado
	 * @return <code>true</code>, caso o sensor esteja conectado; <code>false</code>, caso contr�rio
	 */
	public boolean isSensorConectado() {
		if(sensorAtivo != null && sensorAtivo.isConectado()) { //caso o sensor esteja realmente conectado
			return true;
		} 
		
		return false;
	}
}
