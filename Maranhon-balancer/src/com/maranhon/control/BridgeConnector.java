package com.maranhon.control;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.maranhon.model.ServerData;

public class BridgeConnector extends Thread{

	private ServerData server;
	private Socket clientSocket;
	
	public BridgeConnector(ServerData server, Socket socket) {
		this.server = server;
		this.clientSocket = socket;
	}

	@Override
	public void run() {
		//TODO:	Abre um novo socket com o servidor (serverSocket), ouve um Object de clientSocket, envia para serverSocket,
		// 		ouve a resposta de serverSocket, envia para clientSocket, fecha as conexões
		// Enquanto isso, só vai responder o ID que foi passado, sem fazer nada.
		try{
			OutputStream os = clientSocket.getOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(os);
		
			InputStream is = clientSocket.getInputStream();
			ObjectInputStream ois = new ObjectInputStream(is);
			
			Object x = ois.readObject();
			
			oos.writeObject(new Integer(server.getID()));
		} catch(Exception e){
			
		}
		
		
		BalancerController bc = BalancerController.getInstance();
		bc.disconnectedServer(server);
	}
	
}
