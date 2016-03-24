package com.maranhon.control;

import java.io.IOException;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.Socket;




public class BridgeConnector extends Thread{

	private ServerData server;
	private Socket clientSocket;
	
	public BridgeConnector(ServerData server, Socket socket) {
		this.server = server;
		this.clientSocket = socket;
	}

	@Override
	public void run() {
	    //TODO:Abre um novo socket com o servidor (serverSocket), ouve um Object de clientSocket, envia para serverSocket,
            // ouve a resposta de serverSocket, envia para clientSocket, fecha as conexões
            // Enquanto isso, só vai responder o ID que foi passado, sem fazer nada.
            try {

                ObjectOutputStream send = new ObjectOutputStream(clientSocket.getOutputStream());
                ObjectInputStream receive = new ObjectInputStream(clientSocket.getInputStream());

                send.writeObject(connectToServer((String) receive.readObject()));

            } catch (Exception e) {
                System.err.println("Erro em Bridge no momento em que ira estabelecer conexão com serverOperator "+ e.getMessage());

            }

		
	     BalancerController.getInstance().disconnectedServer(server);
		
	}
        
        public Object connectToServer(String data) {
            
            Object response = null;
            Socket serverSocket = null;
            try {
                serverSocket = new Socket(server.getIP(), server.getPort());
                ObjectInputStream receive = new ObjectInputStream(serverSocket.getInputStream());
                ObjectOutputStream send = new ObjectOutputStream(serverSocket.getOutputStream());
                
                
                send.writeObject(data);
                return receive.readObject();
                
                
                
            } catch (Exception ex) {
                BalancerController.getInstance().disconnectedServer(server);
            }
            finally {
                try {
                    serverSocket.close();
                } catch (IOException ex) {
                    System.err.println("Erro em Bridge Connector no momento que foi finalizar o Socket");
                }
            }
            
            return response;
        
        }
	
}
