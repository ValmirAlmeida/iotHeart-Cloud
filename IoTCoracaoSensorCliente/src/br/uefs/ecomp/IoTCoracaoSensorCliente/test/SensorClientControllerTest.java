package br.uefs.ecomp.IoTCoracaoSensorCliente.test;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.LinkedList;

import br.uefs.ecomp.IoTCoracaoSensorCliente.controller.*;
import br.uefs.ecomp.IoTCoracaoSensorCliente.model.Paciente;

public class SensorClientControllerTest {
	
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		SensorController c = new SensorController(550, "localhost");
		c.iniciarSensor();
		c.gerarPaciente("Valmir", false, 23, 30);
		c.gerarPaciente("Vinicius", false, 40, 50);
		c.gerarPaciente("De", false, 13, 10);
		c.gerarPaciente("Almeida", false, 32, 3);
		
		LinkedList<Paciente> pacientesGerados = c.getPacientesGerados();
		
		Iterator<Paciente> i = pacientesGerados.iterator();
		
		while(i.hasNext()) {
			Paciente paciente = i.next();
			
			System.out.println("ID " + paciente.getId());
			System.out.println("Nome " + paciente.getNome());
			System.out.println("Pressao Sanguinea " + paciente.getPressaoSanguinea());
			System.out.println("Ritmo Cardiaco " + paciente.getRitmoCardiaco());
			System.out.println("Movimentacao " + paciente.isMovimentacao());
		}
		
		System.out.println("MUDA A MOVIMENTAÇÃO DOS DOIS PRIMEIROS");
		
		c.alteraMovimentacao(0);
		c.alteraMovimentacao(1);

		pacientesGerados = c.getPacientesGerados();
		
		i = pacientesGerados.iterator();
		
		while(i.hasNext()) {
			Paciente paciente = i.next();
			
			System.out.println("ID " + paciente.getId());
			System.out.println("Nome " + paciente.getNome());
			System.out.println("Pressao Sanguinea " + paciente.getPressaoSanguinea());
			System.out.println("Ritmo Cardiaco " + paciente.getRitmoCardiaco());
			System.out.println("Movimentacao " + paciente.isMovimentacao());
		}
		
		System.out.println("ADD 5 NA PRESSÃO DOS DOIS ÚLTIMOS");

		
		c.alterarPressaoSanguinea(2, 5);
		c.alterarPressaoSanguinea(3, 5);

		pacientesGerados = c.getPacientesGerados();
		
		i = pacientesGerados.iterator();
		
		while(i.hasNext()) {
			Paciente paciente = i.next();
			
			System.out.println("ID " + paciente.getId());
			System.out.println("Nome " + paciente.getNome());
			System.out.println("Pressao Sanguinea " + paciente.getPressaoSanguinea());
			System.out.println("Ritmo Cardiaco " + paciente.getRitmoCardiaco());
			System.out.println("Movimentacao " + paciente.isMovimentacao());
		}
		
		
		System.out.println("ADD 10 NO RITMO CARDIACO DOS DOIS PRIMEIROS");

		
		c.alteraRitmoCardiaco(0, 10);
		c.alteraRitmoCardiaco(1, 10);

		pacientesGerados = c.getPacientesGerados();
		
		i = pacientesGerados.iterator();
		
		while(i.hasNext()) {
			Paciente paciente = i.next();
			
			System.out.println("ID " + paciente.getId());
			System.out.println("Nome " + paciente.getNome());
			System.out.println("Pressao Sanguinea " + paciente.getPressaoSanguinea());
			System.out.println("Ritmo Cardiaco " + paciente.getRitmoCardiaco());
			System.out.println("Movimentacao " + paciente.isMovimentacao());
		}
	}
}

