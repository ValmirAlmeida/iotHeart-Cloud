package br.uefs.ecomp.IoTCoracaoMedicoCliente.controller;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

import br.uefs.ecom.IoTCoracaoSensorCliente.util.Cliente;
import br.uefs.ecom.IoTCoracaoSensorCliente.util.Mensagem;
import br.uefs.ecomp.IoTCoracaoMedicoCliente.model.Medico;

public class MedicoClienteController {
	
	private Cliente cliente;
	private Medico medicoLogado;
	
	public MedicoClienteController(int porta, String endereco) throws UnknownHostException, SocketException {
		cliente = new Cliente(porta, endereco); 
		medicoLogado = null;
	}
	
	public boolean logarMedico(String email, String senha) throws IOException, ClassNotFoundException {
		Object[] array = new Object[2];
		array[0] = email;
		array[1] = senha;
		
		cliente.enviarMensagem("MEDICO-LOGIN", array);
		Mensagem respostaServidor = (Mensagem) cliente.receberMensagem();
		
		if(respostaServidor.getComando().compareTo("MEDICO-LOGIN-FEEDBACK-TRUE") == 0) {
			medicoLogado = (Medico) respostaServidor.getDados();
			return true;
		} else {
			return false;
		}
	}
	

	
	

}
