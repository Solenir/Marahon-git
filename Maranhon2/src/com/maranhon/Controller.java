package com.maranhon;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.maranhon.balancer.DomainController;
import com.maranhon.balancer.DomainHandler;

public class Controller {

	@SuppressWarnings("resource") // Nem aí se o programa parar de funcionar e o socket ficar aberto até o momento em que eu fechar ele.
	public static void main(String[] args) {
		
		DomainController bc = DomainController.getInstance();
		int port = DomainController.servicePort;
		
		ServerSocket socket = null;
		
		try {
			socket = new ServerSocket(port);
		} catch (IOException e) {
			System.err.println("Não deu pra ouvir nessa porta. Muda aí no código a bagaça");
			System.exit(1);
		}
		
		DomainHandler domainHandler = new DomainHandler();
		domainHandler.start();
		
		while(true){
			try {
				Socket s = socket.accept();
				System.out.println("Conexão de "+s.getRemoteSocketAddress()+" na porta "+s.getPort());
				
				bc.handleRequest(s);
			} catch (IOException e) {
				System.err.println("Conexão deu ruim.");
			}
		}
		
	}
	
}
