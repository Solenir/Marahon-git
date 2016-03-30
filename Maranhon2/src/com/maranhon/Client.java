package com.maranhon;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Random;

import com.maranhon.balancer.DomainController;
import com.maranhon.common.ErrorMessage;
import com.maranhon.common.PurchaseRequest;
import com.maranhon.common.PurchaseResponse;

public class Client extends Thread{


	private static String ip = DomainController.balancerHost;
	private static int port = DomainController.servicePort;
	private static int errors = 0;
	private static int[] results = new int[10];
	private static int finalized = 0;
	
	public static void main(String[] args) throws InterruptedException {
		int tests = 60;
		
		Thread.sleep(1000);
		
		for(int i = 1; i<=tests;i++){
			new Client().start();
			if(tests%100 == 0) // Pausa de 100 em 100 conexões, só pra não ter tanta thread rodando instantaneamente
				Thread.sleep(100);
		}
		
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
		Socket s = null;
		try {
			s = new Socket(ip, port);
			
			OutputStream os = s.getOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(os);
			
			InputStream is = s.getInputStream();
			ObjectInputStream ois = new ObjectInputStream(is);
			
			Random r = new Random();
			int clientID = r.nextInt(100);
			int bookID = r.nextInt(200);
			int quantity = 1+r.nextInt(10);
			PurchaseRequest req = new PurchaseRequest(clientID, bookID, quantity);
			
			oos.writeObject(req);
			
			Object x = ois.readObject();
			
			//Thread.sleep(100); // Para fins de teste, claro
			
			if(x instanceof ErrorMessage){
				System.out.println("Mensagem de erro recebida");
				errors++;
			} else if(x instanceof PurchaseResponse){
				PurchaseResponse resp = ((PurchaseResponse)x);
				int y=resp.getServerID();
				results[y]++;
				System.out.println("Atendido por "+y+" "+results[y]+" vezes. Finished="+(finalized+1));
			}
		} catch (Exception e) {
			System.out.println("Erro desconhecido");
			errors++;
		} finally{
			if(s!=null)
				try {
					s.close();
				} catch (IOException e) {
					// Meh
				}
			finalized++;
		}
	}

	
}
