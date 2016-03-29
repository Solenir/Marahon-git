package com.maranhon.balancer;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.maranhon.common.ServerData;
import com.maranhon.server.multicast.MulticastData;

public class DomainHandler extends Thread{

	private MulticastData domainData;
	
	public DomainHandler(){
		domainData = new MulticastData();
	}
	
	@SuppressWarnings("resource")
	public void run() {
		int port = DomainController.balancerPort;
		ServerSocket socket = null;
		
		try {
			socket = new ServerSocket(port);
		} catch (IOException e) {
			System.err.println("Não deu pra ouvir nessa porta. Muda aí no código a bagaça");
			System.exit(1);
		}
		
		while(true){
			try {
				Socket s = socket.accept();
				System.out.println("Conexão de "+s.getInetAddress().getHostAddress()+" na porta "+s.getPort());
				
				new DomainWatcher(s).start();
				
			} catch (IOException e) {
				System.err.println("Conexão deu ruim.");
			}
		}
	}
	
	private class DomainWatcher extends Thread{
		private Socket socket;
		private boolean initialized;
		DomainWatcher(Socket sock){
			socket=sock;
			initialized=false;
		}
		
		public void run() {
			ServerData sd = null;
			DomainController dc = DomainController.getInstance();
			int numOperations = domainData.getOperationsTotal();
			try{
				OutputStream os = socket.getOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(os);
			
				InputStream is = socket.getInputStream();
				ObjectInputStream ois = new ObjectInputStream(is);
				
				Object x = ois.readObject();
				
				if(x instanceof ServerData){
					sd = (ServerData)x;
					sd.setServerIP(socket.getInetAddress().getHostAddress());
					domainData.insertServer(sd);
					oos.writeObject(domainData);
					initialized=true;
					numOperations = domainData.getOperationsTotal();
					System.out.println("Servidor "+sd.getServerID()+" entrou no domínio.");
				} else{
					return;
				}
				
				while(true){
					Object o = ois.readObject();
					if(o instanceof Integer)
						break;
				}
				
				dc.addServer(sd);
				
				while(true){
					if(domainData.getOperationsTotal() != numOperations){
						// Houve mudança
						oos.writeObject(1);
					} else{
						// Heartbeat invertido
						oos.writeObject(0);
					}
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						
					}
				}
				
			} catch(IOException e){
				if(initialized){
					System.err.println("Servidor "+sd.getServerID()+" caiu.");
					dc.removeServer(sd);
					domainData.removeServer(sd);
				}
			} catch (ClassNotFoundException e) {
				//Faz nada. Só desconecta.
			}
		}
	}
	
}
