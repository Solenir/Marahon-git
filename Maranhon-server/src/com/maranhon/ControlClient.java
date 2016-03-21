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
package com.maranhon;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.LinkedList;


/**
 *
 * @author Joel Filho e Solenir Figuerêdo
 */
public class ControlClient {
    private static ControlClient uniqueInstance = new ControlClient(); // Armazena o endereço de sua única instância
    private LinkedList<Client> clients; // Lista de clientes cadastrados
    private int controlID; // Varíavel responsável por gerenciar o ID dos clientes
    
/**
     * Construtor da classe ControleCliente.
     */
    private ControlClient() {
        this.clients = new LinkedList<>();
          
        readingCustomers();
       
        controlID = clients.size()+1; // Atribui a controleID o próximo ID disponível.
    } 
    
    /**
     * Método que retorna a instância da classe controladora.
     * 
     * @return ControleCliente com o endereço.
     */
    public static ControlClient getInstance() { 
        return uniqueInstance; 
    }
    
    /**
     * Método que realiza a leitura dos clientes contidos no arquivo de texto
     * onde são armazenados e os coloca em uma lista de clientes.
     */
    private void readingCustomers() {
        
        try{
            
            FileReader reading = new FileReader(new File("Clientes.txt"));
            BufferedReader sequentialRead = new BufferedReader(reading);
            String line = sequentialRead.readLine();
            String dataClient[] = null;
            
            while(line != null) {
                
                dataClient = line.split(" ");
                clients.add(new Client(Integer.parseInt(dataClient[0]), dataClient[1], dataClient[2], Integer.parseInt(dataClient[3])));
                
                line = sequentialRead.readLine();
            }
            reading.close();
        }
        catch (Exception ex) {
            System.err.println(ex.toString());
        }
    }
    
    /**
     * Método que realiza a autenticação de um login feito pela aplicação do cliente.
     * Retorna 1, se autenticado, 2, se a senha estivar incorreta, ou 3, quando
     * o nome de login não for encontrado.
     * 
     * @param login String com o nome de login.
     * @param passWord String com a senha.
     * @return Int com o valor da autenticação.
     */
    public int authenticateClient(String login, String passWord) {
        
        Client client = searchForClient(login);
        
        if(client != null){
            
            if(client.authenticateLogin(passWord)) 
                return 1;
            return 2;
        }
        return 3;
    }
        
    /**
     * Método que realiza busca de clientes. Procura um determinado cliente na
     * lista de clientes cadastrados.
     * 
     * @param login String com o nome de login.
     * @return Cliente, se encontrado. Null, caso o contrário.
     */
    public Client searchForClient(String login){
        if(clients.indexOf(new Client(login)) == -1)
            return null;
        return clients.get(clients.indexOf(new Client(login)));
    }
      
    /**
     * Método que verifica se um novo cadastro é possível, se não houver outro
     * com o mesmo nome de login, e o registra. Caso seja possível, retorna
     * o valor 1. Retorna 0 se o contrário. Possui o modificador Synchronized
     * permitindo apenas que um cliente acesse essa região por vez.
     * 
     * @param login String com o nome de login.
     * @param passWord  String com a senha.
     * @return 1 se cadastrado ou 0 se não.
     */
    public synchronized int registeringClient(String login, String passWord){
        
        if(searchForClient(login) == null){
            
            Client newClient = new Client(controlID++, login, passWord, 0);
            clients.add(newClient);
            try {
                //Abre arquivo onde tem usuários ja cadastrados.
                FileWriter file = new FileWriter(new File("Clientes.txt"), true);
                PrintWriter write = new PrintWriter(file);
                String register = ""+newClient.getId()+" "+login+" "+passWord+" "+"0";
                //Laço de repetiçao utilizado para inserir novos usuarios no arquivo de texto.
                while(register.length() < 50)
                    register += " ";
                write.write(register+"\r\n");
                write.close();
            } catch (Exception ex) {
                System.err.println("Erro em cadastrarCliente() de ControlClient.\n"+ex.toString());
            }
            
            return 1; 
        }
        
        return 0;
    }
    
}