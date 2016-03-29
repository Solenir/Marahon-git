package com.maranhon.server.multicast;

import java.io.IOException;
import java.io.ObjectOutputStream;
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
	
	private static MulticastVirtualizer instance;
	HeartBeatListener hblistener;
	
	private MulticastVirtualizer(){
		this.multicastData = new MulticastData();
		this.connections = new ConcurrentLinkedQueue<>();
		this.listener = new MulticastListener();
	}
	
	public static MulticastVirtualizer getInstance(){
		if(instance==null)
			instance = new MulticastVirtualizer();
		return instance;
	}
	
	public void setMulticastData(MulticastData data){
		this.multicastData = data;
		this.start();
	}
	
	public void setHeartBeatStream(ObjectOutputStream heartbeatStream){
		hblistener = new HeartBeatListener(heartbeatStream);
	}
	
	public void sendHeartBeat(){
		try{
			hblistener.start();
		} catch(Exception e){
			
		}
	}
	
	public int getNumConnections(){
		return connections.size();
	}
	
	public synchronized void serverDisconnected(BufferedUnicast u){
		connections.remove(u);
	}
	
	public void run() {
		this.openConnections();
		listener.start();
		
		if(connections.size() == 0)
			 DatabaseController.getInstance().startRunning();
		
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
			if(s.getServerID() == ServerController.getInstance().getData().getServerID())
				continue; // Não conectar a si mesmo.
			
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
	
	private class HeartBeatListener extends Thread{
		private ObjectOutputStream stream;
		HeartBeatListener(ObjectOutputStream stream){
			this.stream = stream;
		}
		
		public void run() {
			try{
				stream.writeObject(new Integer(0));
				Thread.sleep(1);
			} catch(Exception e){
				System.err.println("Falha na conexão com o controlador. Hora de sair.");
				//TODO: Salvar dados antes de sair.
				System.exit(1);
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
					System.out.println("Conexão de "+s.getInetAddress().getHostAddress()+" na porta "+s.getPort());
					
					BufferedUnicast conn = new BufferedUnicast(s);
					connections.add(conn);
					conn.start();
					
					DatabaseController.getInstance().newServer(conn);
					DatabaseController.getInstance().executeNextQuery();
				} catch (IOException e) {
					System.err.println("Conexão deu ruim.");
				}
			}
		}
		
	}

}
