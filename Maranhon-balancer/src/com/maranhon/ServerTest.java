package com.maranhon;

import com.maranhon.control.ThreadClient;
import com.maranhon.control.ThreadServer;



public class ServerTest {
	
	public static void main(String[] args) {
            
            new Thread (new ThreadClient()).start();
            new Thread (new ThreadServer()).start();       

		
	}
	
}
