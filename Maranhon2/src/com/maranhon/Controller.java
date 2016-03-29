package com.maranhon;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.maranhon.balancer.DomainController;
import com.maranhon.balancer.DomainHandler;

public class Controller {

	@SuppressWarnings("resource") // Nem a� se o programa parar de funcionar e o socket ficar aberto at� o momento em que eu fechar ele.
	public static void main(String[] args) {
		
		DomainController bc = DomainController.getInstance();
		int port = DomainController.servicePort;
		
		ServerSocket socket = null;
		
		try {
			socket = new ServerSocket(port);
		} catch (IOException e) {
			System.err.println("N�o deu pra ouvir nessa porta. Muda a� no c�digo a baga�a");
			System.exit(1);
		}
		
		DomainHandler domainHandler = new DomainHandler();
		domainHandler.start();
		
		while(true){
			try {
				Socket s = socket.accept();
				System.out.println("Conex�o de "+s.getRemoteSocketAddress()+" na porta "+s.getPort());
				
				bc.handleRequest(s);
			} catch (IOException e) {
				System.err.println("Conex�o deu ruim.");
			}
		}
		
	}
	
}
