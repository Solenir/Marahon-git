/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maranhon.Controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


/**
 *
 * @author solenir
 */
public class Controller {

    public int registeringClient(String login, String passWord, String cpf) {
        return (int)connectServer("0"+"!"+login+"/"+passWord+"/"+cpf+"/"+"-1");
    }
    
    public int addBookCartPurchase(String title, String author, int edition, int amount) {
        return (int)connectServer("2"+"!"+title+"/"+author+"/"+edition+"/"+amount);
    }
    
    private Object connectServer(String data){ 
        ObjectOutputStream send = null;
        ObjectInputStream receive = null;
        Object response = null;
        Socket socket = null;
        try {
            socket = new Socket("127.0.0.1",31699);
            send = new ObjectOutputStream(socket.getOutputStream());
            receive = new ObjectInputStream(socket.getInputStream());
            send.writeObject(data);
           
            response = receive.readObject();
               
        } catch (Exception ex){
            System.err.println("Erro ao criar fluxo de dados");
        }
        
        finally{
            try {
                socket.close();
            } catch (IOException ex) {
                System.err.println("Erro ao fechar socket");
            }
        }
        return response;
    }

 
        
        
    
}
