/**
 * Componente Curricular: Módulo integrador de Concorrência e Conectiviade
 * Autor: Joel pinto de Carvalho Filho e José Solenir Lima Figuerêdo
 * Data:  20/03/2016
 *
 * Declaramos que este código foi elaborado por nós em dupla e
 * não contém nenhum trecho de código de outro colega ou de outro autor, 
 * tais como provindos de livros e apostilas, e páginas ou documentos 
 * eletrônicos da Internet. Qualquer trecho de código de outra autoria que
 * uma citação para o não a nossa está destacado com autor e a fonte do
 * código, e estamos cientes que estes trechos não serão considerados para fins
 * de avaliação. Alguns trechos do código podem coincidir com de outros
 * colegas pois estes foram discutidos em sessões tutorias.
 */
package com.maranhon.control;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;



/**
 *
 * @author solenir
 */
public class ServerOnlineThread extends Thread {
    private Socket socketServer;
    private ObjectOutputStream send;
    private ObjectInputStream receive;
    private ServerData serverData;
    private int serverId;
    
    
    public ServerOnlineThread(Socket socket, int serverId) {
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
            send.writeObject(""+serverData.getID()+"");
            send.writeObject(BalancerController.getInstance().ipServerList());
            
            int serverConnectedAnterior = BalancerController.getInstance().sizeQueue() ;
            
            
            while(true){
                pause(2000);
                
                if (serverConnectedAnterior != BalancerController.getInstance().sizeQueue()){
                     send.writeObject(""+BalancerController.getInstance().ipServerList());
                     serverConnectedAnterior = BalancerController.getInstance().sizeQueue() ;
                }
                else
                    send.writeObject("0");
                                   
            }
                    
            
        } catch (Exception ex) {
            BalancerController.getInstance().disconnectedServer(serverData);
            
        try {
               socketServer.close();
           } catch (IOException ex1) {
               System.err.println("Erro incomum em ServerOnlineThread");
           }
        return;
        }
        
    }
    
    public void addServer(){
        BalancerController.getInstance().addServer(serverData);
    }
    
    private void pause(int timeWait) {
        
        try {
                Thread.sleep(timeWait);
        } catch (InterruptedException ex) {
                   
        }
    }
    
}