/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maranhon.balancer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


/**
 *
 * @author solenir
 */
class ServerOnlineThread extends Thread {
    private Socket socket;
    private ServerData serverData;
    private int IdServidor;
    private ObjectOutputStream send;
    private ObjectInputStream receive;

    public ServerOnlineThread(Socket socket, int IdServidor) {
        this.socket = socket;
        this.IdServidor = IdServidor;
    }

    @Override
    public void run() {
        
        try {
            send = new ObjectOutputStream(socket.getOutputStream());
            receive = new ObjectInputStream(socket.getInputStream());
            
            int porta = (int) receive.readObject();
            
            serverData = new ServerData(socket.getInetAddress().getHostAddress(), porta, IdServidor);
            DomainController.getInstance().addServer(serverData);
              
            System.out.println("SERVIDOR COM ID "+ serverData.getServerID());
            send.writeObject(serverData.getServerID());
           
            //send.writeObject(BalancerController.getInstance().ipServerList());
            
            //int serverConnectedAnterior = BalancerController.getInstance().sizeQueue() ;
            
            //talvez use este código
            /*
            while(true){
                pause(2000);
                
                if (serverConnectedAnterior != BalancerController.getInstance().sizeQueue()){
                     send.writeObject(""+BalancerController.getInstance().ipServerList());
                     serverConnectedAnterior = BalancerController.getInstance().sizeQueue() ;
                }
                else
                    send.writeObject("0");
            }
                
            */
            
        } catch (Exception ex) {
           DomainController.getInstance().removeServer(serverData);
            try {
                socket.close();
            } catch (IOException ex1) {
                System.err.println("Erro inesperado em Server OnlineThread "+ex1.getMessage());
            }
            return;
        }
        
    }

    
}
