package com.maranhon.server.multicast;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

public class BufferedUnicast extends Thread{

	private ConcurrentLinkedQueue<Object> inputQueue;
	private ConcurrentLinkedQueue<Object> outputQueue;
	private Sender dataSender;
	
	private ObjectInputStream inputStream;
	private ObjectOutputStream outputStream;
	
	public BufferedUnicast(Socket socket) throws IOException{
		inputQueue = new ConcurrentLinkedQueue<>();
		outputQueue = new ConcurrentLinkedQueue<>();
		dataSender = new Sender();
		
		outputStream = new ObjectOutputStream(socket.getOutputStream());
		inputStream = new ObjectInputStream(socket.getInputStream());
		
	}

	public boolean isEmpty(){
		return inputQueue.isEmpty();
	}
	
	public Object nextObject(){
		return inputQueue.poll();
	}
	
	public void run() {
		dataSender.start();

		while(true){
			try {
				Object obj = inputStream.readObject();
				inputQueue.add(obj);
			} catch (ClassNotFoundException e) {
				// Se aconteceu isso, n�o vamos tratar algo que n�o existe.
			} catch (IOException e) {
				System.err.println("Erro de E/S em um dos unicasts do servidor (entrada). Caiu?"); //TODO: parar tudo caso d� merda. E anunciar que caiu.
			}
		}	
		
	}
	
	
	private class Sender extends Thread{
		Sender(){
			
		}
		
		public void run() {
			while(true){
				while(outputQueue.isEmpty());
				
				try {
					outputStream.writeObject(outputQueue.peek());
					outputQueue.poll();
				} catch (IOException e) {
					System.err.println("Erro de E/S em um dos unicasts do servidor (sa�da). Caiu?"); //TODO: mesma coisa do outro to-do.
				}
			}
			
		}
	}
	
}
