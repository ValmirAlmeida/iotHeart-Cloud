package br.uefs.ecomp.IoTHeartSensorClient.model;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import br.uefs.ecomp.IoTHeartUtil.util.ConexaoUDP;
import br.uefs.ecomp.IoTHeartUtil.util.LidarComRecebimentoUDP;
import br.uefs.ecomp.IoTHeartUtil.util.Mensagem;
import br.uefs.ecomp.IoTHeartUtil.util.Protocolo;

/**
 * Classe responsável por modelar um sensor no contexto do sistema. Um sensor é caracterizado pelo paciente
 * monitorado pelo sensor.
 * @author Valmir Vinicius
 */
public class Sensor {
    /** IP do servidor ao qual o sensor está conectado ou se conectará */
	private Paciente paciente;
    /** IP do servidor ao qual o sensor se conectará */
	private InetAddress ipServidor;
    /** Porta do processo a qual o sensor se conectaá */
	private int portaProcessoNoServidor;
    /** Indica se o sensor encontra-se conectado */
	private boolean conectado;
	

	/**
	 * Obtém uam instância de sensor
	 * @param paciente paciente que o sensor acompanha
	 * @param ipServidor ip do servidor
	 * @param portaProcessoNoServidor porta do processo
	 * @throws IOException
	 */
	public Sensor(Paciente paciente, InetAddress ipServidor, int portaProcessoNoServidor) throws IOException {
		super();
		this.paciente = paciente;
		this.ipServidor = ipServidor;
		this.portaProcessoNoServidor = portaProcessoNoServidor;
		this.conectado = false;
	}; 
	
	/**
	 * Inicia o funcionamento do sensor
	 * @throws ClassNotFoundException caso alguma classe fundamental à execução não seja encontrada
	 * @throws IOException caso ocorra algum erro durante a entrada ou saída de dados na comunicação
	 */
	public void iniciarSensor() throws ClassNotFoundException, IOException {
		ConexaoUDP conexaoDoCliente = new ConexaoUDP(); //obtém uma nova conexão UDP
		conexaoDoCliente.enviar(new Mensagem(Protocolo.SENSOR_START, paciente, paciente.getCpf()), ipServidor, portaProcessoNoServidor); //envia uma solicitação de início do sensor
		conectado = false; //o sensor não está conectado
		conexaoDoCliente.receber(new LidarComRecebimentoUDP() { //espera receber um pacote
			@Override
			public void pacoteChegou(DatagramPacket pacoteRecebido) { //recebe uma resposta do servidor
				try {
					Mensagem mensagemRecebida = (Mensagem) ConexaoUDP.verificaDesmontaPacote(pacoteRecebido); //desmonta o pacote recebido
					if(mensagemRecebida.getAcaoProtocolo() == Protocolo.SENSOR_CONNECTED) { //caso a resposta do servidor indique o sensor está corretamente conectado
						conectado = true; //o sensor está conectado
					} else if(mensagemRecebida.getAcaoProtocolo() == Protocolo.SENSOR_JA_EXISTE){ //caso a resposta do servidor indique o cpf do paciente monitorado pelo sensor já esteja vinculado a um paciente ativo no sistema
						conectado = false; //o sensor não está conectado
					}
				} catch (ClassNotFoundException | IOException e) {
					e.printStackTrace();
				}
			}
		});
		conexaoDoCliente.fecharConexao(); //encerra a conexão
	}
	
	/**
	 * Realiza o desativamento de um sensor
	 * @return <code>true</code>, caso o sensor tenha sido desativado corretamente; <code>false</code>, caso contrário
	 * @throws ClassNotFoundException caso alguma classe fundamental à execução não seja encontrada
	 * @throws IOException caso ocorra algum erro durante a entrada ou saída de dados na comunicação
	 */
	public boolean desativarSensor() throws IOException, ClassNotFoundException {
		ConexaoUDP conexao = new ConexaoUDP(); //obtém uma nova conexão UDP
		conexao.enviar(new Mensagem(Protocolo.SENSOR_DISCONNECT, null, paciente.getCpf()), ipServidor, portaProcessoNoServidor); //envia uma mensagem solicitando o encerramento do sensor
		conexao.receber(new LidarComRecebimentoUDP() { //espera uma resposta do servidor
 			@Override
			public void pacoteChegou(DatagramPacket pacoteRecebido) { //chegou a resposta do servidor
				try {
					Mensagem mensagemRecebida = (Mensagem) ConexaoUDP.verificaDesmontaPacote(pacoteRecebido); //verifica e desmonta o pacote recebido do servidor
					if(mensagemRecebida.getAcaoProtocolo() == Protocolo.SENSOR_DISCONNECTED) { //caso a mensagem recebida indique que o sensor foi corretamente desconectado
						conectado = false; //o sensor está desativado
						conexao.fecharConexao(); //encerra a conexão
					} else if(mensagemRecebida.getAcaoProtocolo() == Protocolo.SENSOR_DISCONNECT_ERRO) { //caso a mensagem recebida indique que o sensor não foi corretamente desconectado
						conectado = true; //o sensor permanece ativo
					}
				} catch (ClassNotFoundException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		});
		
		return !conectado; //indica se o sensor foi realmente desativado
	}
	
	/**
	 * Altera o estado de movimentação do paciente
	 * @param isMovimentacao <code>true</code>, caso o paciente esteja em movimento; <code>false</code>, caso contrário
	 * @throws IOException caso ocorra algum erro durante a entrada ou saída de dados na comunicação
	 */
	public void alteraMovimentacao(boolean isMovimentacao) throws IOException {
		ConexaoUDP conexao = new ConexaoUDP();
		paciente.setMovimentacao(isMovimentacao); //altera o estado de movimentação do paciente
		conexao.enviar(new Mensagem(Protocolo.SENSOR_ATT_MOVIMENTACAO, isMovimentacao, paciente.getCpf()), ipServidor, portaProcessoNoServidor); //envia mensagem indicando atualização do movimento do paciente ao servidor	
		conexao.fecharConexao(); //encerra a conexão
	}
	
	/**
	 * Altera o valor do ritmo cardiaco do paciente.
	 * @param variacao valor a ser variado no ritmo cardiaco; caso seja positivo, o ritmo cardíaco aumentará, caso contrário o ritmo cardíaco será diminuido.
	 * @throws IllegalArgumentException - caso o valor de ritmo cardíaco informado resulte em ritmo cardíaco negativo	 
	 * @throws IOException 
	 */
	public void alteraRitmoCardiaco(double novoValor) throws IllegalArgumentException, IOException{
		ConexaoUDP conexao = new ConexaoUDP(); 
		if(novoValor < 0) {
			throw new IllegalArgumentException("O valor informado é inválido");
		}
		
		paciente.setRitmoCardiaco(novoValor); //altera o ritmo cardiaco do paciente
		conexao.enviar(new Mensagem(Protocolo.SENSOR_ATT_RITMOCARDIACO, paciente.getRitmoCardiaco(), paciente.getCpf()), ipServidor, portaProcessoNoServidor); //envia mensagem indicando atualização dos batimentos cardiacos ao servidor	
		conexao.fecharConexao(); //encerra a conexão
	}
	
	/**
	 * Altera o valor da pressão sanguínea do paciente.
	 * @param variacao valor a ser variado na pressão sanguínea; caso seja positivo, a pressão sanguínea aumentará, caso contrário a pressão sanguínea será diminuida.
	 * @throws IllegalArgumentException - caso o valor de pressão sanguínea informado resulte em pressão sanguínea negativa
	 * @throws IOException 
	 */
	public void alterarPressaoSanguinea(double pressao, boolean isMaxima) throws IllegalArgumentException, IOException {
		ConexaoUDP conexao = new ConexaoUDP();
		if(pressao < 0) {
			throw new IllegalArgumentException("O valor informado é inválido");
		}
		
		paciente.setPressaoSanguinea(pressao, isMaxima); //altera a pressão sanguinea do paciente
		
		if(isMaxima) {
			conexao.enviar(new Mensagem(Protocolo.SENSOR_ATT_PRESSAOMAX, paciente.getPressaoSanguinea()[1], paciente.getCpf()), ipServidor, portaProcessoNoServidor); //envia mensagem indicando atualização da pressão sanguinea maxima do paciente
		} else {
			conexao.enviar(new Mensagem(Protocolo.SENSOR_ATT_PRESSAOMIN, paciente.getPressaoSanguinea()[0], paciente.getCpf()), ipServidor, portaProcessoNoServidor); //envia mensagem indicando atualização da pressão sanguinea mininma do paciente
		}
		conexao.fecharConexao(); //encerra a conexão
	}
	
	/**
	 * Obtém a instância do paciente monitorado pelo sensor
	 * @return instância do paciente monitorado pelo sensor; <code>null</code>, caso o sensor esteja inativo
	 */
	public Paciente getPaciente() {
		return paciente;
	}

	/**
	 * Define o paciente monitorado pelo sensor
	 * @param paciente instância paciente a ser monitorado pelo sensor
	 */
	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
	}

	/**
	 * Indica se o sensor está ativo
	 * @return <code>true</code>, caso o sensor esteja conectado; <code>false</code>, caso contrário
	 */
	public boolean isConectado() {
		return conectado;
	}
}
