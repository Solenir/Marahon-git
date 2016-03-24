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

package com.maranhon;

import java.io.IOException;
import java.net.*;


/**
 *
 * @author Joel Filho e Solenir Figuerêdo
 */
public class ServerOperatorThread extends Thread {
    private int porta;
    
    public ServerOperatorThread(int porta){
        this.porta = porta;
        
    } 
    
    @Override
    public void run(){
               
        
        System.out.println("HOJEEEEEEE "+ porta);
        /* Instanciando objeto do tipo ServerSocket utilizado
         * para estabelecer conexao TCP com algum cliente que
         * deseje se conectar ao servidor.
         */
        try {
            ServerSocket servidor = new ServerSocket(porta); 
        
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
            new Thread(new ClientThread(conexaoServidor)).start();
                     
        }
        
        } catch (IOException ex) {
            System.err.println("Erro em ServerOperatorThread "+ex.getMessage());
        }
        
   
    }
    
    
}
