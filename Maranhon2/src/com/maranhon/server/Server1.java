/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maranhon.server;

import java.util.Random;

/**
 *
 * @author solenir
 */
public class Server1 {

    /**
     * @param args the command line arguments
     */
    @SuppressWarnings("resource")
    public static void main (String arg []){
        int portRequestClient = new Random().nextInt(200) + 50000;
        int portRequestServer = new Random().nextInt(200) + 50000;
        System.out.println(portRequestClient+ " "+ portRequestServer);
        new Thread(new ClientOperatorThread(portRequestClient, portRequestServer)).start();
    
    }
    
}
