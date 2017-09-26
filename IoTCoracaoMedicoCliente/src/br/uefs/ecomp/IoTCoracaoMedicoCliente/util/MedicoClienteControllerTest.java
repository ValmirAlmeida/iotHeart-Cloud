package br.uefs.ecomp.IoTCoracaoMedicoCliente.util;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

import br.uefs.ecomp.IoTCoracaoMedicoCliente.controller.MedicoClienteController;

public class MedicoClienteControllerTest {
	
	public static void main(String[] args) throws ClassNotFoundException, IOException {
		MedicoClienteController mc = new MedicoClienteController(550, "localhost");
		System.out.println(mc.logarMedico("emil@medico.com", "senha"));
	}

}
