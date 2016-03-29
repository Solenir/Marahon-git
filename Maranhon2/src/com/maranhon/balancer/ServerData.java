package com.maranhon.balancer;

import java.io.Serializable;

public class ServerData implements Serializable{

	private static final long serialVersionUID = 316_000000_2L;
	
	private int serverID; // ID do servidor, dado pelo controlador
	private String serverIP; // 
	private int serverHostPort; //Responde requisi��es por aqui
	private int IdServidor; //Recebe conex�o dos outros servidores por aqui
	
	public ServerData(String IP, int hostPort, int idServidor){
		this.serverIP = IP;
		this.serverHostPort = hostPort;
		this.IdServidor = idServidor;
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
	
		
	
}
