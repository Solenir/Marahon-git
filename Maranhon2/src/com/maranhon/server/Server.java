package com.maranhon.server;


import java.util.Random;

public class Server {

	@SuppressWarnings("resource")
	public static void main(String[] args) {
            int port1 = new Random().nextInt(80) + 50000; 
            int port2 = new Random().nextInt(80) + 50000;
            System.out.println(port1+" "+ port2);
            new Thread(new ServerOperatorThread(port1, port2)).start();
            new Thread(new ClientOperatorThread(port1)).start();

	}
	
}
