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

import java.net.Socket;


/**
 *
 * @author solenir
 */
public class ClientOperatorThread extends Thread{
    private Socket socket;
    private ObjectInputStream receive;
    
    public void run (){
        
        try {
            socket = new Socket("127.0.0.1", 31600);
            receive = new ObjectInputStream(socket.getInputStream());
            
          
            while(true){
                String data = (String)receive.readObject();
                System.err.println(data+""+"socorooooooooooooooo");
                if(data.length() > 1){
                    TransmissionControl.getInstance().setIpServerOnline(data);
                }
                else 
                    if (Integer.parseInt(data) != 0)
                        TransmissionControl.getInstance().setIpServerOnline(data);
                      
            }
      
            
        }
        catch (Exception ex) {
            System.err.println("Erro em ClienteOperatorThread em algum momento "+ex.getMessage());  
        } 
    
    
    }
        
        
        
    
}
