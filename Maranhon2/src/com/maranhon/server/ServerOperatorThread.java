/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maranhon.server;

import com.maranhon.common.ServerData;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author solenir
 */
class ServerOperatorThread extends Thread {
    private int portRequestClient;
    private int porRequestServer;
    private int IdServer;

    public ServerOperatorThread(int portRequestClient, int portRequestServer, int IdServer) {
        this.portRequestClient = portRequestClient;
        this.porRequestServer = portRequestServer;
        this.IdServer = IdServer;
    }

    @Override
    public void run() {
        
        ServerData data = new ServerData("127.0.0.1", portRequestClient, porRequestServer);
        data.setServerID(IdServer);
        
        ServerController.initializeServer(data);
		
	ServerController serverController = ServerController.getInstance();
	DatabaseController.getInstance();
        
        ServerSocket serverSocket = null;
        System.out.println("SERVIDOR COM ID:"+ IdServer);
		
	try {
       	    serverSocket = new ServerSocket(data.getServerHostPort());
 	} catch (IOException e) {
		System.err.println("N�o deu pra ouvir nessa porta. Muda a� no c�digo a baga�a");
		System.exit(1);
	}
        
        while(true){
            try {
	        Socket socket = serverSocket.accept();
		System.out.println("Conex�o de "+socket.getRemoteSocketAddress()+" na porta "+socket.getPort());
		serverController.handleRequest(socket);
		} catch (IOException e) {
		System.err.println("Conex�o deu ruim.");
		}
	}
        
        
    }
    
}
