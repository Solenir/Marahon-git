package com.maranhon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import com.maranhon.common.ServerData;
import com.maranhon.server.DatabaseController;
import com.maranhon.server.ServerController;

public class Server {

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("> ID do servidor: ");

		String input = "0";
		try {
			input = reader.readLine();
		} catch (IOException e1) {
			
		}
		int sID = Integer.parseInt(input);
		
		
		
		ServerData data = new ServerData("127.0.0.1", 31680+2*sID, 31681+2*sID);
		data.setServerID(sID);
		
		ServerController.initializeServer(data);
		
		ServerController sc = ServerController.getInstance();
		DatabaseController.getInstance();
		
		
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
