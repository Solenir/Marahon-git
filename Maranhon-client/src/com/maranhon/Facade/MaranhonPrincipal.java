/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maranhon.Facade;

import java.util.Scanner;

/**
 *
 * @author solenir
 */
public class MaranhonPrincipal {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Facade facade = new Facade();
        
        Scanner input = new Scanner(System.in);
        String login, passWord = null;
        
        System.out.println("Informe seu nome por favor.");
        login = input.nextLine();
        System.out.println("Informe sua senha por favor.");
        passWord = input.nextLine();
        
        if (facade.registeringClient(login, passWord) == 1)
            System.out.println("Cadastrado com sucesso!");
        
        
    }
    
}
