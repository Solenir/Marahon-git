/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maranhon.balancer;

import com.maranhon.common.ServerData;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.Socket;

/**
 *
 * @author solenir
 */
class ServerThread extends Thread {
    private Socket socketServer;
    private ServerData serverData;
    private int serverId;
    private ObjectOutputStream send;
    private ObjectInputStream receive;

    public ServerThread(Socket socket, int serverId) {
       this.socketServer = socket;
        this.serverId = serverId;    
    }
    
    @Override
    public void run(){
       
      
        try {
            send = new ObjectOutputStream(socketServer.getOutputStream());
            receive = new ObjectInputStream(socketServer.getInputStream());
            int porta = (int) receive.readObject();
            
            serverData = new ServerData(socketServer.getInetAddress().getHostAddress(), porta, serverId);
            
            addServer();
            send.writeObject(serverData.getServerID());
            
            
            
           
        } catch (Exception ex) {
            DomainController.getInstance().getServerQueue().remove(serverData);
            try {
                socketServer.close();
            } catch (IOException ex1) {
                System.err.println("Erro inesperado em Server OnlineThread "+ex1.getMessage());
            }
            return;
        }   
        
       
        
    }
    
    public void addServer(){
        DomainController.getInstance().addServer(serverData);
    }
    
    
}
