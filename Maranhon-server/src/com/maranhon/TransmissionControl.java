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
import java.io.IOException;
import java.net.Socket;


/**
 *
 * @author Joel Filho e Solenir Figuerêdo
 */
public class TransmissionControl {
    private static TransmissionControl uniqueInstance = new TransmissionControl();
    private String listIpServerOnline[]; 
    private int serverIdControlled; 
    
    
    private TransmissionControl (){
    
    }
    
    public static TransmissionControl getInstance () {
 
       return uniqueInstance;
    }

    public void setIpServerOnline(String listIpServerOnline) {
        this.listIpServerOnline = listIpServerOnline.split(" ");
    }
    
    public void setserverIdControlled(String serverId){
        this.serverIdControlled = Integer.parseInt(serverId);
    } 
    
    public int getserverIdControlled(){
        return serverIdControlled;
    }
    
    public void customerRegister(String data){
        createConnection(data);
    
    
    }
    private void createConnection(String data){
        
        for (int controle = 0; controle < listIpServerOnline.length; controle++){
            Socket socket;
            try {
                socket = new Socket(listIpServerOnline[controle], 9999);
                new Thread(new TransmissionControlThread(new Communication(socket), data)).start();
            } catch (IOException ex) {
                System.err.println("Não foi possivel estabelecer Conexão");
            }
               
        }
          
    }
        
    
}
