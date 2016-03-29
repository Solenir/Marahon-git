/**
 * Componente Curricular: Módulo integrador de Concorrência e Conectiviade
 * Autor: Joel pinto de Carvalho Filho e José Solenir Lima Figuerêdo
 * Data:  21/03/2016
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
package com.maranhon.balancer;

import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author solenir
 */
public class ThreadConnectionServer extends Thread {
    private ServerSocket serverSocket;
    private int idServer = 0;
    
    
    @Override
    public void run(){
        
        try {
            serverSocket = new ServerSocket(DomainController.balancerPort);
            while (true){
                Socket socket = serverSocket.accept();
                new Thread(new ServerOnlineThread(socket, ++idServer)).start();
            
            }
        
        
        } catch (Exception ex){
        
        
        }
    
    
    }
    
}
