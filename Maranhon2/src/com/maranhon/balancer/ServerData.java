package com.maranhon.balancer;


import java.io.Serializable;

public class ServerData implements Serializable{

	private static final long serialVersionUID = 316_000000_2L;
	
	private int serverID; // ID do servidor, dado pelo controlador
	private String serverIP; // 
	private int serverHostPort; //Responde requisi��es por aqui
	private int serverClusterPort; //Recebe conex�o dos outros servidores por aqui
	
	public ServerData(String IP, int hostPort, int serVerId){
		this.serverIP = IP;
		this.serverHostPort = hostPort;
		this.serverID = serVerId;
	}
	
	public int getServerID() {
		return serverID;
	}
	public void setServerID(int serverID) {
		this.serverID = serverID;
	}
	public String getServerIP() {
		return serverIP;
	}
	public int getServerHostPort() {
		return serverHostPort;
	}
	public int getServerClusterPort() {
		return serverClusterPort;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ServerData){
			ServerData sd = (ServerData)obj;
			return sd.serverIP.equals(this.serverIP) && sd.serverClusterPort==this.serverClusterPort;
		}
		return false;
	}
	
}
