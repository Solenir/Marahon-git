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
package com.maranhon;


import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.Socket;


/**
 *
 * @author solenir
 */
public class ClientOperatorThread extends Thread{
    private Socket socket;
    private ObjectInputStream receive;
    private ObjectOutputStream send;
    private int porta;
    private String dataReceived;
    
    public ClientOperatorThread(int porta){
        System.out.println("Chegouuuuu a porta "+porta);
        this.porta = porta;
    }
    
    public void run (){
        
        try {
            socket = new Socket("127.0.0.1", 31600);
            System.out.println("naoooooooooooooooooooooooooooo");
            receive = new ObjectInputStream(socket.getInputStream());
            send = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("AQUIIIIIIIIIII !!!!!!!!!!!!!!!");
            send.writeObject(porta);
            System.out.println("AGUARDAAAAAAAA");
            dataReceived = (String) receive.readObject();
            System.out.println("DADOOOO RECEBIDOOOO "+ dataReceived);
            TransmissionControl.getInstance().setserverIdControlled(dataReceived);
            System.out.println("DADOOOO RECEBIDOOOO "+ dataReceived);
            while(true){
                dataReceived = (String)receive.readObject();
                if(dataReceived.length() > 1)
                    TransmissionControl.getInstance().setIpServerOnline(dataReceived);
                      
            }
      
            
        }
        catch (Exception ex) {
            System.err.println("Erro em ClienteOperatorThread em algum momento "+ex.getMessage());  
        } 
    
    
    }
        
        
        
    
}
