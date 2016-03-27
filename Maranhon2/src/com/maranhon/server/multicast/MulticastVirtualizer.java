package com.maranhon.server.multicast;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.maranhon.common.ServerData;
import com.maranhon.server.DatabaseController;
import com.maranhon.server.ServerController;

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
		DatabaseController dbcontrol = DatabaseController.getInstance();
		
		while(true){
			for(BufferedUnicast conn : connections){
				if(conn.isEmpty())
					continue;
				Object obj = conn.nextObject();
				dbcontrol.processClusterData(obj);
			}
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				
			}
		}		
	}

	public void sendObject(Object obj){
		for(BufferedUnicast conn : connections){
			conn.sendObject(obj);
		}
	}
	
	private void openConnections() {
		for(ServerData s : multicastData.getServerList()){
			for(int tries = 0; tries < 10; tries++){
				Socket socket;
				try {
					socket = new Socket(s.getServerIP(), s.getServerClusterPort());
					BufferedUnicast u = new BufferedUnicast(socket);
					connections.add(u);
					u.start();
					break;
				} catch (Exception e) {
					System.out.println("Não conseguiu conectar no servidor "+s.getServerID()+". Tentativas: "+(tries+1));
					try {
						Thread.sleep(2500);
					} catch (InterruptedException e1) {
						
					}
					continue;
				}
			}
		}
		
	}
	
	private class MulticastListener extends Thread{
		private ServerSocket socket;
		MulticastListener(){
			socket = null;
			ServerData data = ServerController.getInstance().getData();
			try {
				socket = new ServerSocket(data.getServerClusterPort());
			} catch (IOException e) {
				System.err.println("Não deu pra ouvir nessa porta. Muda aí no código a bagaça");
				System.exit(1);
			}
		}
		
		public void run() {
			while(true){
				try {
					Socket s = socket.accept();
					System.out.println("Conexão de "+s.getRemoteSocketAddress()+" na porta "+s.getPort());
					
					BufferedUnicast conn = new BufferedUnicast(s);
					connections.add(conn);
					conn.start();
				} catch (IOException e) {
					System.err.println("Conexão deu ruim.");
				}
			}
		}
		
	}

}
