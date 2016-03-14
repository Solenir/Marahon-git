package com.maranhon;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class ClientTester extends Thread{

	private static String ip = "127.0.0.1";
	private static int port = 31699;
	private static int errors = 0;
	private static int[] results = new int[10];
	private static int finalized = 0;
	
	public static void main(String[] args) {
		int tests = 90;
		
		for(int i = 0; i<tests;i++)
			new ClientTester().start();
		
		System.out.println("Esperando finalizar os testes...");
		
		while(finalized < tests){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				
			}
			System.out.println("Finalized ="+finalized);
		}
		
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println("Finalizado. Erros: "+errors);
		for(int i = 0; i < 10; i++){
			System.out.println("Server "+i+": "+results[i]+" conexões feitas.");
		}
		
	}
	
	public void run() {
		try {
			Socket s = new Socket(ip, port);
			
			OutputStream os = s.getOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(os);
			
			InputStream is = s.getInputStream();
			ObjectInputStream ois = new ObjectInputStream(is);
			
			oos.writeObject(0);
			
			Object x = ois.readObject();
			
			Thread.sleep(100); // Para fins de teste, claro
			s.close();
			if(x instanceof Integer){
				Integer y = (Integer)x;
				results[y]++;
				System.out.println("Atendido por "+y+" "+results[y]+" vezes. Finished="+(finalized+1));
			}
			
		} catch (Exception e) {
			errors++;
		} finally{
			finalized++;
		}
	}
	
}
