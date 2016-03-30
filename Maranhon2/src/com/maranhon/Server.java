package com.maranhon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.maranhon.balancer.DomainController;
import com.maranhon.common.ServerData;
import com.maranhon.server.DatabaseController;
import com.maranhon.server.ServerController;
import com.maranhon.server.multicast.MulticastData;
import com.maranhon.server.multicast.MulticastVirtualizer;

public class Server {

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("> ID do servidor: ");

		String input = "0";
		try {
			input = reader.readLine();
		} catch (IOException e1) {
			
		}
		int sID = Integer.parseInt(input);
		
		
		
		ServerData data = new ServerData("127.0.0.1", 31680+2*sID, 31681+2*sID);
		data.setServerID(sID);
		
		ServerController.initializeServer(data);
		
		ServerController sc = ServerController.getInstance();
		ObjectOutputStream heartBeatStream = null;
		ObjectInputStream iis = null;
		
		DatabaseController.getInstance();
		
		try{
			Socket s = new Socket(DomainController.balancerHost, DomainController.balancerPort);
			
			OutputStream os = s.getOutputStream();
			heartBeatStream = new ObjectOutputStream(os);
			
			InputStream is = s.getInputStream();
			iis = new ObjectInputStream(is);
			
			heartBeatStream.writeObject(data);
			
			MulticastData md = (MulticastData) iis.readObject();
			MulticastVirtualizer.getInstance().setMulticastData(md);
		}catch(Exception e){
			System.err.println("Erro na comunicação com o controlador. Fechando.");
			
		}
		
		MulticastVirtualizer.getInstance().setHeartBeatStream(heartBeatStream, iis);
		
		ServerSocket socket = null;
		
		try {
			socket = new ServerSocket(data.getServerHostPort());
		} catch (IOException e) {
			System.err.println("Não deu pra ouvir nessa porta. Muda aí no código a bagaça");
			System.exit(1);
		}
		
		while(true){
			try {
				Socket s = socket.accept();
				System.out.println("Conexão de "+s.getInetAddress().getHostAddress()+" na porta "+s.getPort());
				
				sc.handleRequest(s);
			} catch (IOException e) {
				System.err.println("Conexão deu ruim.");
			}
		}
		
	}
	
}
