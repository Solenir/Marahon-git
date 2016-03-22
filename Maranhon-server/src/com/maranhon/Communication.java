
package com.maranhon;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


/**
 *
 * @author solenir
 */
public class Communication {
    private Socket socket;
    private ObjectOutputStream send;
    private ObjectInputStream receive;
    private Object response;
    
    public Communication(Socket socket){
        this.socket = socket;
        try {
            this.send = new ObjectOutputStream(socket.getOutputStream());
            this.receive = new ObjectInputStream(socket.getInputStream());
        } catch (Exception ex) {
            System.err.println("Erro ao criar os fluxos de dados");
        }
        
    } 
    
    public void send(String data) throws IOException{
        send.writeObject(data);
    }
    
    public void receive() throws Exception {
        this.response = receive.readObject();
    }
    
}
