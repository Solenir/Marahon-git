package com.maranhon.server;

import java.net.Socket;

import com.maranhon.common.ServerData;

public class ServerController {

	private static ServerController instance;
	private static ServerData serverData;
	
	private ServerController(){
		//this.serverData = new ServerData("127.0.0.1", 31680, 31681);
		//this.serverData.setServerID(0);
	}
	
	public static void initializeServer(ServerData data){
		serverData = data;
	}
	
	public static ServerController getInstance(){
		if(instance==null)
			instance = new ServerController();
		return instance;
	}

	public synchronized void handleRequest(Socket s) {
		ServerHandler handler = new ServerHandler(s);
		handler.start();
		
	}
	
	public ServerData getData(){
		return serverData;
	}
	
}
