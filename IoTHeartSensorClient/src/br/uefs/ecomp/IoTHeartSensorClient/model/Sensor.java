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
 * Classe respons�vel por modelar um sensor no contexto do sistema. Um sensor � caracterizado pelo paciente
 * monitorado pelo sensor.
 * @author Valmir Vinicius
 */
public class Sensor {
    /** IP do servidor ao qual o sensor est� conectado ou se conectar� */
	private Paciente paciente;
    /** IP do servidor ao qual o sensor se conectar� */
	private InetAddress ipServidor;
    /** Porta do processo a qual o sensor se conecta� */
	private int portaProcessoNoServidor;
    /** Indica se o sensor encontra-se conectado */
	private boolean conectado;
	

	/**
	 * Obt�m uam inst�ncia de sensor
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
	 * @throws ClassNotFoundException caso alguma classe fundamental � execu��o n�o seja encontrada
	 * @throws IOException caso ocorra algum erro durante a entrada ou sa�da de dados na comunica��o
	 */
	public void iniciarSensor() throws ClassNotFoundException, IOException {
		ConexaoUDP conexaoDoCliente = new ConexaoUDP(); //obt�m uma nova conex�o UDP
		conexaoDoCliente.enviar(new Mensagem(Protocolo.SENSOR_START, paciente, paciente.getCpf()), ipServidor, portaProcessoNoServidor); //envia uma solicita��o de in�cio do sensor
		conectado = false; //o sensor n�o est� conectado
		conexaoDoCliente.receber(new LidarComRecebimentoUDP() { //espera receber um pacote
			@Override
			public void pacoteChegou(DatagramPacket pacoteRecebido) { //recebe uma resposta do servidor
				try {
					Mensagem mensagemRecebida = (Mensagem) ConexaoUDP.verificaDesmontaPacote(pacoteRecebido); //desmonta o pacote recebido
					if(mensagemRecebida.getAcaoProtocolo() == Protocolo.SENSOR_CONNECTED) { //caso a resposta do servidor indique o sensor est� corretamente conectado
						conectado = true; //o sensor est� conectado
					} else if(mensagemRecebida.getAcaoProtocolo() == Protocolo.SENSOR_JA_EXISTE){ //caso a resposta do servidor indique o cpf do paciente monitorado pelo sensor j� esteja vinculado a um paciente ativo no sistema
						conectado = false; //o sensor n�o est� conectado
					}
				} catch (ClassNotFoundException | IOException e) {
					e.printStackTrace();
				}
			}
		});
		conexaoDoCliente.fecharConexao(); //encerra a conex�o
	}
	
	/**
	 * Realiza o desativamento de um sensor
	 * @return <code>true</code>, caso o sensor tenha sido desativado corretamente; <code>false</code>, caso contr�rio
	 * @throws ClassNotFoundException caso alguma classe fundamental � execu��o n�o seja encontrada
	 * @throws IOException caso ocorra algum erro durante a entrada ou sa�da de dados na comunica��o
	 */
	public boolean desativarSensor() throws IOException, ClassNotFoundException {
		ConexaoUDP conexao = new ConexaoUDP(); //obt�m uma nova conex�o UDP
		conexao.enviar(new Mensagem(Protocolo.SENSOR_DISCONNECT, null, paciente.getCpf()), ipServidor, portaProcessoNoServidor); //envia uma mensagem solicitando o encerramento do sensor
		conexao.receber(new LidarComRecebimentoUDP() { //espera uma resposta do servidor
 			@Override
			public void pacoteChegou(DatagramPacket pacoteRecebido) { //chegou a resposta do servidor
				try {
					Mensagem mensagemRecebida = (Mensagem) ConexaoUDP.verificaDesmontaPacote(pacoteRecebido); //verifica e desmonta o pacote recebido do servidor
					if(mensagemRecebida.getAcaoProtocolo() == Protocolo.SENSOR_DISCONNECTED) { //caso a mensagem recebida indique que o sensor foi corretamente desconectado
						conectado = false; //o sensor est� desativado
						conexao.fecharConexao(); //encerra a conex�o
					} else if(mensagemRecebida.getAcaoProtocolo() == Protocolo.SENSOR_DISCONNECT_ERRO) { //caso a mensagem recebida indique que o sensor n�o foi corretamente desconectado
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
	 * Altera o estado de movimenta��o do paciente
	 * @param isMovimentacao <code>true</code>, caso o paciente esteja em movimento; <code>false</code>, caso contr�rio
	 * @throws IOException caso ocorra algum erro durante a entrada ou sa�da de dados na comunica��o
	 */
	public void alteraMovimentacao(boolean isMovimentacao) throws IOException {
		ConexaoUDP conexao = new ConexaoUDP();
		paciente.setMovimentacao(isMovimentacao); //altera o estado de movimenta��o do paciente
		conexao.enviar(new Mensagem(Protocolo.SENSOR_ATT_MOVIMENTACAO, isMovimentacao, paciente.getCpf()), ipServidor, portaProcessoNoServidor); //envia mensagem indicando atualiza��o do movimento do paciente ao servidor	
		conexao.fecharConexao(); //encerra a conex�o
	}
	
	/**
	 * Altera o valor do ritmo cardiaco do paciente.
	 * @param variacao valor a ser variado no ritmo cardiaco; caso seja positivo, o ritmo card�aco aumentar�, caso contr�rio o ritmo card�aco ser� diminuido.
	 * @throws IllegalArgumentException - caso o valor de ritmo card�aco informado resulte em ritmo card�aco negativo	 
	 * @throws IOException 
	 */
	public void alteraRitmoCardiaco(double novoValor) throws IllegalArgumentException, IOException{
		ConexaoUDP conexao = new ConexaoUDP(); 
		if(novoValor < 0) {
			throw new IllegalArgumentException("O valor informado � inv�lido");
		}
		
		paciente.setRitmoCardiaco(novoValor); //altera o ritmo cardiaco do paciente
		conexao.enviar(new Mensagem(Protocolo.SENSOR_ATT_RITMOCARDIACO, paciente.getRitmoCardiaco(), paciente.getCpf()), ipServidor, portaProcessoNoServidor); //envia mensagem indicando atualiza��o dos batimentos cardiacos ao servidor	
		conexao.fecharConexao(); //encerra a conex�o
	}
	
	/**
	 * Altera o valor da press�o sangu�nea do paciente.
	 * @param variacao valor a ser variado na press�o sangu�nea; caso seja positivo, a press�o sangu�nea aumentar�, caso contr�rio a press�o sangu�nea ser� diminuida.
	 * @throws IllegalArgumentException - caso o valor de press�o sangu�nea informado resulte em press�o sangu�nea negativa
	 * @throws IOException 
	 */
	public void alterarPressaoSanguinea(double pressao, boolean isMaxima) throws IllegalArgumentException, IOException {
		ConexaoUDP conexao = new ConexaoUDP();
		if(pressao < 0) {
			throw new IllegalArgumentException("O valor informado � inv�lido");
		}
		
		paciente.setPressaoSanguinea(pressao, isMaxima); //altera a press�o sanguinea do paciente
		
		if(isMaxima) {
			conexao.enviar(new Mensagem(Protocolo.SENSOR_ATT_PRESSAOMAX, paciente.getPressaoSanguinea()[1], paciente.getCpf()), ipServidor, portaProcessoNoServidor); //envia mensagem indicando atualiza��o da press�o sanguinea maxima do paciente
		} else {
			conexao.enviar(new Mensagem(Protocolo.SENSOR_ATT_PRESSAOMIN, paciente.getPressaoSanguinea()[0], paciente.getCpf()), ipServidor, portaProcessoNoServidor); //envia mensagem indicando atualiza��o da press�o sanguinea mininma do paciente
		}
		conexao.fecharConexao(); //encerra a conex�o
	}
	
	/**
	 * Obt�m a inst�ncia do paciente monitorado pelo sensor
	 * @return inst�ncia do paciente monitorado pelo sensor; <code>null</code>, caso o sensor esteja inativo
	 */
	public Paciente getPaciente() {
		return paciente;
	}

	/**
	 * Define o paciente monitorado pelo sensor
	 * @param paciente inst�ncia paciente a ser monitorado pelo sensor
	 */
	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
	}

	/**
	 * Indica se o sensor est� ativo
	 * @return <code>true</code>, caso o sensor esteja conectado; <code>false</code>, caso contr�rio
	 */
	public boolean isConectado() {
		return conectado;
	}
}
