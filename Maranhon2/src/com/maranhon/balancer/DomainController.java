package com.maranhon.balancer;

import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;



public class DomainController {

	// constantes �teis para o futuro. Ou n�o.
	public static final String	balancerHost = "127.0.0.1"; // Provavelmente nunca necess�rio. N�o aqui.
	public static final int	balancerPort = 31600; // Porta que os servers dever�o acessar para entrar no grupo
	public static final int	servicePort  = 31699; // Porta que os clientes v�o acessar
	
	// Singleton FTW
	private static DomainController instance;
	
	// --- 
	private int lastServer;
	private ConcurrentHashMap<Integer, ServerData> connectedServers;
	private Queue<ServerData> serverQueue;
	
	
	private DomainController(){
		connectedServers = new ConcurrentHashMap<>();
		serverQueue = new LinkedList<ServerData>();
		lastServer = 0;
	}
	
	public static DomainController getInstance(){
		if(instance == null)
			instance = new DomainController();
		return instance;
	} 
	
	public synchronized void handleRequest(Socket socket){
		
		// Round robin!
		ServerData sd = serverQueue.poll();
		if(sd!=null)
			serverQueue.add(sd);
		
		RequestHandler handler = new RequestHandler(sd, socket);
		
		handler.start();
		
	}
	
	public synchronized void addServer(ServerData s){
		s.setServerID(lastServer++);
		connectedServers.put(lastServer, s);
		serverQueue.add(s);
	}
	
	public synchronized boolean removeServer(ServerData s){
           
		return serverQueue.remove(s); //TODO: Quando for remover servers, usar aqui como base
	}

	
}
