/**
 * Componente Curricular: Módulo integrador de Concorrência e Conectiviade
 * Autor: Joel pinto de Carvalho Filho e José Solenir Lima Figuerêdo
 * Data:  14/03/2016
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

package com.maranhon.server;

import com.maranhon.common.ServerData;
import java.io.IOException;
import java.net.*;


/**
 *
 * @author Joel Filho e Solenir Figuerêdo
 */
public class ServerOperatorThread extends Thread {
    private int port1;
    private int port2;
    
    public ServerOperatorThread(int port1 , int port2){
        this.port1 = port1;
        this.port2 = port2;
        
    } 
    
    @Override
    public void run(){
       
        ServerData data = new ServerData("127.0.0.1", port1, port2);
        ServerController.initializeServer(data);
        ServerController sc = ServerController.getInstance();
	DatabaseController.getInstance();
        try {
            ServerSocket servidor = new ServerSocket(data.getServerHostPort()); 
        
        /* Laço de repetição utilizado para permitir que mais 
         * de um hospeiro cliente se conecte.
         */
        while (true){
            
            /* Iniciando conexão com algum hospedeiro. O método accept()
             * é utilizado para permitir que uma conexão TCP seja criada
             * quando for recebida uma solicitação de abertura de conexão
             * por parte de um cliente. Esta solicitação é feita através
             * da primitiva de serviço Socket.
             */
            Socket conexaoServidor = servidor.accept();
            
            /* Criação de fluxo de execução independente para cada hospedeiro 
             * cliente que solicite alguma conexão.Sendo que o método start  
             * é o responsável por iniciar de de fato a nova thread. É importante 
             * perceber que estamos passando o objeto Socket da comunicação
             * por parametro, já que cada cliente poderá enviar e receber informações
            */
            sc.handleRequest(conexaoServidor);
                     
        }
        
        } catch (IOException ex) {
            System.err.println("Erro em ServerOperatorThread "+ex.getMessage());
        }
        
   
    }
    
    
}
