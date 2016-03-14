package com.maranhon;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.maranhon.control.BalancerController;
import com.maranhon.exception.NoServerOnlineException;
import com.maranhon.model.ServerData;

public class ServerTest {
	
	public static void main(String[] args) {
		// Só um teste para a primeira meta. Deletar depois.
		
		BalancerController bc = BalancerController.getInstance();
		int port = BalancerController.servicePort;
		int servers = 10; //TODO: Nada pra fazer aqui, só algo pra você prestar atenção. Coloque o quanto quiser, menor que 10.
		
		ServerSocket socket = null;
		
		try {
			socket = new ServerSocket(port);
		} catch (IOException e) {
			System.err.println("Não deu pra ouvir nessa porta. Muda aí no código a bagaça");
			System.exit(1);
		}
		
		for(int i = 0; i < servers; i++){
			ServerData s = new  ServerData("127.0.0.1", 999);
			bc.addServer(s);
			System.out.println("Adicionado server com ID="+s.getID());
		}
		
		while(true){
			try {
				Socket s = socket.accept();
				System.out.println("Conexão de "+s.getRemoteSocketAddress()+" na porta "+s.getPort());
				try {
					bc.handleRequest(s);
				} catch (NoServerOnlineException e) {
					System.err.println("OOPS. 0 servers = deu ruim.");
				}
			} catch (IOException e) {
				System.err.println("Conexão deu ruim.");
			}
		}
		
	}
	
}
