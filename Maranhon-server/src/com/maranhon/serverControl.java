/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maranhon;

import java.util.Random;

/**
 *
 * @author solenir
 */
public class serverControl {
    
    public static void main (String arg []){
        int porta = new Random().nextInt(80) + 50000;
        System.out.println(porta);
        new Thread(new ServerOperatorThread(porta)).start();
        new Thread(new ClientOperatorThread(porta)).start();
    
    }
    
}
