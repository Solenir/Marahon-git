package com.maranhon.model;

public class ServerData implements Comparable<ServerData>{

	private String serverIP;
	private int serverPort;
	private int serverID;
	private int connectedClients;
	
	public ServerData(String ip, int port){
		serverIP = ip;
		serverPort = port;
		serverID = 0;
		connectedClients = 0;
	}
	
	public synchronized void incrementConnectedClients(int increment){
		connectedClients += increment;
	}
	
	public int getConnectedClients(){
		return connectedClients;
	}

	public int compareTo(ServerData other) {
		return this.connectedClients - other.connectedClients;
	}
	
	public int getPort(){
		return serverPort;
	}
	
	public String getIP(){
		return serverIP;
	}
	
	public void setID(int id){
		this.serverID = id;
	}
	
	public int getID(){
		return serverID;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ServerData)
			return ((ServerData)obj).serverID == this.serverID;
		else 
			return false;
	}

}
