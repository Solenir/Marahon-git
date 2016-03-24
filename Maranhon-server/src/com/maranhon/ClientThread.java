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
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;



/**
 * Classe que representa o fluxo de execução de cada conexão .
 * Ela implementa a interface Runnable. Nessa interface existe
 * apenas um método de nome Run(). É partir dele que a thread é
 * de fato é iniciada. Nela é aramzenado o Socket da conexao e o dado
 * recebido do cliente. Tem também um atributo que é responsável
 * por enviar dados para o cliente que iniciou a comunicação
 * 
 * @author Joel Filho e Solenir Figuerêdo
 */
class ClientThread implements Runnable {
    private Socket connectionClient; //Armazena socke to balanceador-servidor.
    private ObjectOutputStream send; // Armazena os dados enviados pelo cliente.
    private ObjectInputStream receive; // Envia dados para o cliente.
    private String dataReceived;
    

    
    /**
     * Construtor da classe ClientThread.
     * 
     * @param connectionClient  Socket da comunicação entre balanceador(cliente)/servidor;
     * @exception IOException pode lançar exceção caso a comunicação não 
     * tenha sido estabelicida com sucesso.
     */
    public ClientThread(Socket connectionClient) throws IOException {
        this.connectionClient = connectionClient;
        this.send = new ObjectOutputStream(connectionClient.getOutputStream());
        this.receive = new ObjectInputStream(connectionClient.getInputStream());
        
    }

     /**
     * Inicia a execução das principais "funções" que o cliente pode realizar
     * Sendo que cada serviço será executado a partir do protocolo de comunicação
     * que foi anteriormente estabelecido na criação da aplicação.
     * Nesse caso específico, o protocolo de aplicação utiliza números int
     * para representar uma determinada operação. 0 para cadastrar um nonvo
     * usuário, 1 autenticar, 2 adicionar livro no carrinho, 3 finalizar compra.
     */
    @Override
    public void run() {
        
        try {
            dataReceived = (String)receive.readObject();
            String separateData [] = dataReceived.split("!");
            
            
            /* 
             * Switch utilizado para determinar, a partir do valor atribuido
             * a variável operacao, a operação que o usário quer executar.
             */
            switch (Integer.parseInt(separateData[0])){
                
                //cadastra um novo cliente;
                case 0:
                    
                    customerRegister(separateData[1]);
                    connectionClient.close();
                    break;
                
                //cliente é autenticado;
                case 1:
                    loginClient(separateData[1]);
                    connectionClient.close();
                    break;
                
                //cliente adiciona um livro no carrinho;
                case 2:
                    addBook(separateData[1]);
                    connectionClient.close();
                    break;
                    
                //Finaliza coompra dos livros que estão no carrinho;
                case 3:
                    chekout(separateData[1]);
                    connectionClient.close();                     
            }
                              
            
        } catch (Exception ex) {
            System.err.println("Qual será o erro"+ex.toString());
        } 
        
    }

    private void customerRegister(String dataReceived) throws IOException {
        
        String data [] = dataReceived.split("/");
     
        
        int response = ControlClient.getInstance().registeringClient(data[0],data[1], data[2]);
        if (response == 1 && data[3].equals("-1")) {
            send.writeObject(response);
            //TransmissionControl.getInstance().customerRegister(this.dataReceived.replace("-1",""+TransmissionControl.getInstance().getserverIdControlled()+""));
                       
        } else
            if (response == 0 && data[3].equals("-1"))
                send.writeObject(response);
        
        
    }

    private void loginClient(String dataReceived) throws IOException {
        String data [] = dataReceived.split("/");
        send.writeObject(ControlClient.getInstance().authenticateClient(data[0],data[1]));
        
    }

    private void addBook(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void chekout(String separateData) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
