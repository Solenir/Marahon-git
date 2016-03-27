package com.maranhon.server.multicast;

import java.util.concurrent.ConcurrentLinkedQueue;

public class MulticastVirtualizer extends Thread{
	
	private MulticastData multicastData;
	private ConcurrentLinkedQueue<BufferedUnicast> connections;
	private MulticastListener listener;
	
	public MulticastVirtualizer(MulticastData initialData){
		this.multicastData = initialData;
		this.connections = new ConcurrentLinkedQueue<>();
		this.listener = new MulticastListener();
	}
	
	public void run() {
		this.openConnections();
		listener.start();
		
		//TODO: iterar pelas conexões, vendo quais têm dados e enviar esses dados de volta para o DatabaseController
	}

	public void sendObject(Object obj){
		//TODO
	}
	
	private void openConnections() {
		//TODO: do.
		
	}
	
	private class MulticastListener extends Thread{
		MulticastListener(){
			//TODO: abrir porta para ouvir as conexões de outros servidores
		}
		
		public void run() {
			//TODO: ouvir a porta aberta, criar um novo BufferedUnicast, adicionar na lista de conexões
		}
		
	}

}
