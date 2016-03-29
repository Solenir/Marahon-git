package com.maranhon.server.multicast;

import java.io.Serializable;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.maranhon.common.ServerData;

public class MulticastData implements Serializable{

	private static final long serialVersionUID = 316_000000_1L;

	private ConcurrentLinkedQueue<ServerData> serverList;
	private int numOperations; // Para verificar se algo mudou.
	
	
	public MulticastData(){
		serverList = new ConcurrentLinkedQueue<>();
	}
	
	public synchronized void removeServer(ServerData server){
		serverList.remove(server);
		numOperations++;
	}
	
	public synchronized void insertServer(ServerData server){
		serverList.add(server);
		numOperations++;
	}
	
	public int getOperationsTotal(){
		return numOperations;
	}
	
	public Iterable<ServerData> getServerList(){
		return serverList;
	}
	
}
