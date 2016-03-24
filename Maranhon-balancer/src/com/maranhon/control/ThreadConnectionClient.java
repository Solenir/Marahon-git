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
package com.maranhon.control;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


/**
 *
 * @author Joel Filho e Solenir Figuerêdo
 */
public class ThreadConnectionClient extends Thread {
    private ServerSocket serverSocket;
    
    
    @Override
    public void run (){
        
        try {
            serverSocket = new ServerSocket(BalancerController.servicePort);
            
            while(true) {
                Socket socket = serverSocket.accept();
                System.out.println("Conexão de "+socket.getRemoteSocketAddress()+" na porta "+socket.getPort());
                try {
                    BalancerController.getInstance().handleRequest(socket);
                } catch (NoServerOnlineException ex) {
                    System.err.println("Erro na classe ThreadClient"+ ex.getMessage());
                }
            }    
            
        } catch (IOException ex) {
            System.err.println("Não deu pra ouvir nessa porta. Muda aí no código a bagaça");
            System.exit(1);
        }
    
    
    }
    
}
