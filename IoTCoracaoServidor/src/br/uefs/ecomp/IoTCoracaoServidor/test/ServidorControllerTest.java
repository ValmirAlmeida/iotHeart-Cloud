package br.uefs.ecomp.IoTCoracaoServidor.test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.SocketException;

import br.uefs.ecomp.IoTCoracaoServidor.controller.ServidorController;

public class ServidorControllerTest {
	
	public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, IOException {
		ServidorController sc = new ServidorController(550);
		sc.rodarServidor();
	}

}
