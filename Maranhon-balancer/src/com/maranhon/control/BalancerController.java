package com.maranhon.control;

import java.net.Socket;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;



public class BalancerController {

	// constantes úteis para o futuro. Ou não.
	public static final String	balancerHost = "127.0.0.1"; // Provavelmente nunca necessário. Não aqui.
	public static final int	balancerPort = 31600; // Porta que os servers deverão acessar para entrar no grupo
	public static final int	servicePort  = 31699; // Porta que os clientes vão acessar
	
	// Singleton FTW
	private static BalancerController instance;
	
	// --- 
	private int lastServer;
	private ConcurrentHashMap<Integer, ServerData> connectedServers;
	private Queue<ServerData> serverQueue;
	
	
	private BalancerController(){
		connectedServers = new ConcurrentHashMap<>();
		serverQueue = new LinkedList<ServerData>();
		lastServer = 0;
	}
	
	public static BalancerController getInstance(){
		if(instance == null)
			instance = new BalancerController();
		return instance;
	} 
	
	public synchronized void handleRequest(Socket socket) throws NoServerOnlineException{
		if(serverQueue.size() == 0)
			throw new NoServerOnlineException();
		
		// Tem que remover e colocar de novo para manter ordenado
		ServerData sd = serverQueue.poll();
		sd.incrementConnectedClients(1);
		serverQueue.add(sd);
		
		BridgeConnector bc = new BridgeConnector(sd, socket);
		
		bc.start();
		
	}

	public synchronized void disconnectedServer(ServerData server) {
		server.incrementConnectedClients(-1);
		//serverQueue.remove(server);
		//serverQueue.add(server);
	}
	
	public synchronized void addServer(ServerData s){
		s.setID(lastServer++);
		connectedServers.put(lastServer, s);
		serverQueue.add(s);
	}
	
	public synchronized boolean removeServer(ServerData s){
		return false; //TODO: Quando for remover servers, usar aqui como base
	}
	
	
}
