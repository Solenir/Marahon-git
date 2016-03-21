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
import java.io.ObjectOutputStream;
import java.net.Socket;


/**
 *
 * @author solenir
 */
public class ServerOnlineThread extends Thread {
    private Socket socketServer;
    private ObjectOutputStream send;
    private static final int serverOnline = 1;
    private ServerData serverData;
   
    
    public ServerOnlineThread(Socket socket) {
        this.socketServer = socket;
        this.serverData = new ServerData(socketServer.getInetAddress().getHostAddress(), 9999);    
    }
    
    @Override
    public void run(){
       addServer();
        try {
            send = new ObjectOutputStream(socketServer.getOutputStream());
                  
            while(true){
                pause(2000);
                send.writeObject(serverOnline);
            }
                    
            
        } catch (IOException ex) {
            BalancerController.getInstance().disconnectedServer(serverData);
            
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
