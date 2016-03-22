/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maranhon.Facade;

import com.maranhon.Controller.Controller;

/**
 *
 * @author solenir
 */
public class Facade {
    private Controller controller;
    
    public Facade(){
        this.controller = new Controller();
    }
    
    public int registeringClient(String login, String passWord){
        return this.controller.registeringClient(login, passWord);
        
    }
    
    
}
