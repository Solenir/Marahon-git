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

	public void sendObject(Object obj){
		outputQueue.add(obj);
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
				// Se aconteceu isso, não vamos tratar algo que não existe.
			} catch (IOException e) {
				System.err.println("Erro de E/S em um dos unicasts do servidor (entrada). Caiu?"); //TODO: parar tudo caso dê merda. E anunciar que caiu.
				MulticastVirtualizer.getInstance().serverDisconnected(this);
				return;
			}
		}	
		
	}
	
	
	private class Sender extends Thread{
		Sender(){
			
		}
		
		public void run() {
			while(true){
				while(outputQueue.isEmpty()){
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						
					}
				}
				
				try {
					outputStream.writeObject(outputQueue.peek());
					outputQueue.poll();
				} catch (IOException e) {
					System.err.println("Erro de E/S em um dos unicasts do servidor (saída). Caiu?"); //TODO: mesma coisa do outro to-do.
					MulticastVirtualizer.getInstance().serverDisconnected(BufferedUnicast.this);
					return;
				}
			}
			
		}
	}
	
}
