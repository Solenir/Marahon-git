package com.maranhon;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.maranhon.common.ServerData;
import com.maranhon.server.DatabaseController;
import com.maranhon.server.ServerController;

public class Server {

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		ServerController sc = ServerController.getInstance();
		DatabaseController.getInstance();
		ServerData data = sc.getData();
		data.setServerID(0);
		
		ServerSocket socket = null;
		
		try {
			socket = new ServerSocket(data.getServerHostPort());
		} catch (IOException e) {
			System.err.println("N�o deu pra ouvir nessa porta. Muda a� no c�digo a baga�a");
			System.exit(1);
		}
		
		//TODO: entrar no dom�nio, conectar aos outros, atualizar banco de dados
		
		while(true){
			try {
				Socket s = socket.accept();
				System.out.println("Conex�o de "+s.getRemoteSocketAddress()+" na porta "+s.getPort());
				
				sc.handleRequest(s);
			} catch (IOException e) {
				System.err.println("Conex�o deu ruim.");
			}
		}
		
	}
	
}
