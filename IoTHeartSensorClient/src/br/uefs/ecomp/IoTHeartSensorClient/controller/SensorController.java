package br.uefs.ecomp.IoTHeartSensorClient.controller;

import java.io.IOException;
import java.net.InetAddress;

import br.uefs.ecomp.IoTHeartSensorClient.exceptions.PacienteJaConectadoException;
import br.uefs.ecomp.IoTHeartSensorClient.exceptions.PacienteNaoConectadoException;
import br.uefs.ecomp.IoTHeartSensorClient.model.Paciente;
import br.uefs.ecomp.IoTHeartSensorClient.model.Sensor;
import br.uefs.ecomp.IoTHeartUtil.util.ComunicacaoImpossivelException;

/**
 * Classe responsável por gerenciar as ações executadas pelo sensor no sistema.
 * @author Valmir Vinicius
 */
public class SensorController {
    /** IP do servidor ao qual o sensor está conectado ou se conectará */
	private InetAddress ipServidor;
    /** Porta do processo na qual o sensor está conectado ou se conectará */
	private int portaProcesso;
    /** Instância do sensor em atividade no momento */
	private Sensor sensorAtivo;
	
	/**
	 * Obtém o ip do servidor
	 * @return ip do servidor
	 */
	public InetAddress getIpServidor() {
		return ipServidor;
	}
	
	/**
	 * Obtém a porta do processo ao qual o sensor está conectado
	 * @return porta do processo
	 */
	public int getPortaProcesso() {
		return portaProcesso;
	}
	
	/**
	 * Obtém uma instância do controller do sensor
	 */
	public SensorController()  {
		//define valores iniciais e inválidos para o ip e a porta do processo
		ipServidor = null;
		portaProcesso = -1;
	};

	/**
	 * Define o caminho, isto é ip e porta do processo
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
	 * @param movimentacaoInicial valor inicial do sensor de movimentação
	 * @param ritmoCardiacoInicial ritmo cardiaco inicial do paciente
	 * @param pressaoSanguineaMax pressão sanguinea máxima inicial do paciente
	 * @param pressaoSanguineaMin pressão sanguinea mínima inicial do paciente
	 * @return <code>true</code>, caso o sensor seja ativado normalmente; <code>false</code>, caso ocorra algum erro durante a ativação do sensor
	 * @throws IOException caso ocorra algum erro durante a entrada ou saída de dados na comunicação
	 * @throws IllegalArgumentException caso algum dos argumentos informados seja incorreto/inválido
	 * @throws ClassNotFoundException caso alguma classe fundamental à execução não seja encontrada
	 * @throws ComunicacaoImpossivelException caso o servidor não tenha sido iniciado
	 * @throws PacienteJaConectadoException caso o cpf informado esteja relacionado a um paciente já conectado
	 */
	public boolean ativarSensor(String nomePacienteMonitorado, String cpf, boolean movimentacaoInicial, double ritmoCardiacoInicial, double pressaoSanguineaMax, double pressaoSanguineaMin) throws IOException, IllegalArgumentException, ClassNotFoundException, ComunicacaoImpossivelException, PacienteJaConectadoException {
		if(ipServidor == null || portaProcesso == -1) { //caso o ip e a porta do processo estejam inicialmente com valores inválidos
			throw new ComunicacaoImpossivelException();
		}

		if(nomePacienteMonitorado == null || cpf == null || ritmoCardiacoInicial < 0 || pressaoSanguineaMax < 0 || pressaoSanguineaMin < 0) { //caso algum dos valores informados esteja em formado/seja incorreto
			throw new IllegalArgumentException("Um ou mais dados estão ausentes ou foram informados incorretamente");
		}

		Paciente paciente = new Paciente(nomePacienteMonitorado, cpf, movimentacaoInicial, ritmoCardiacoInicial, pressaoSanguineaMax, pressaoSanguineaMin); //cria uma nova instância de paciente com base nos dados informados
		sensorAtivo = new Sensor(paciente, ipServidor, portaProcesso); //cria uma nova instância de sensor com base no paciente criado e a define como sensor desse controller
		sensorAtivo.iniciarSensor(); //inicia o sensor
		
		if(!sensorAtivo.isConectado()) { //caso o sensor não esteja conectado
			throw new PacienteJaConectadoException();
		}
		
		return sensorAtivo.isConectado(); //indica se o processo ocorreu corretamente
	}
	
	/**
	 * Realiza o desativamento de um sensor
	 * @param cpf cpf do paciente associado ao sensor que se deseja desativar
	 * @return <code>true</code>, caso o processo ocorra normalmente; <code>false</code>, caso contrário
	 * @throws ClassNotFoundException caso alguma classe fundamental à execução não seja encontrada
	 * @throws IOException caso ocorra algum erro durante a entrada ou saída de dados na comunicação
	 */
	public boolean desativarSensor(String cpf) throws ClassNotFoundException, IOException {
		sensorAtivo.desativarSensor(); //faz com que o sensor desativa
		
		if(!sensorAtivo.isConectado()) { //verifica se o sensor está realmente inativo
			sensorAtivo = null; //faz com que a referência de  sensor do controller seja nula
			return true;
		} 
		
		return false; //retorna falso indicando que o processo não ocorreu corretamente
	}
	
	/**
	 * Realiza a alteração da movimentação do paciente
	 * @param isMovimentacao <code>true</code>, caso o paciente esteja em movimento; <code>false</code>, caso contrário
	 * @throws IOException caso ocorra algum erro durante a entrada ou saída de dados na comunicação
	 * @throws PacienteNaoConectadoException caso não exista um paciente conectado
	 */
	public void alterarMovimentacao(boolean isMovimentacao) throws IOException, PacienteNaoConectadoException {
		if(sensorAtivo != null && sensorAtivo.isConectado()) { //caso o sensor esteja realmente conectado
			sensorAtivo.alteraMovimentacao(isMovimentacao); //realiza a alteração efetiva da movimentação
		} else {
			throw new PacienteNaoConectadoException(); 
		}
	}
	
	/**
	 * 
	 * @param batimentos
	 * @throws IOException caso ocorra algum erro durante a entrada ou saída de dados na comunicação
	 * @throws PacienteNaoConectadoException caso não exista um paciente conectado
	 */
	public void alterarBatimentosCardiacos(double batimentos) throws IOException, PacienteNaoConectadoException {
		if(sensorAtivo != null && sensorAtivo.isConectado()) { //caso o sensor esteja realmente conectado
			sensorAtivo.alteraRitmoCardiaco(batimentos); //realiza a alteração efetiva da movimentação
		} else {
			throw new PacienteNaoConectadoException();
		}
	}
	
	/**
	 * 
	 * @param pressao
	 * @param isMaxima
	 * @throws IOException caso ocorra algum erro durante a entrada ou saída de dados na comunicação
	 * @throws PacienteNaoConectadoException caso não exista um paciente conectado
	 */
	public void alterarPressao(double pressao, boolean isMaxima) throws IOException, PacienteNaoConectadoException {
		if(sensorAtivo != null && sensorAtivo.isConectado()) { //caso o sensor esteja realmente conectado
			sensorAtivo.alterarPressaoSanguinea(pressao, isMaxima); //realiza a alteração efetiva da movimentação
		} else {
			throw new PacienteNaoConectadoException();
		}
	}
	
	/**
	 * Verifica se o sensor está conectado
	 * @return <code>true</code>, caso o sensor esteja conectado; <code>false</code>, caso contrário
	 */
	public boolean isSensorConectado() {
		if(sensorAtivo != null && sensorAtivo.isConectado()) { //caso o sensor esteja realmente conectado
			return true;
		} 
		
		return false;
	}
}
