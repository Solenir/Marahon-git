package com.maranhon.server.multicast;

import java.io.Serializable;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.maranhon.common.ServerData;

public class MulticastData implements Serializable{

	private static final long serialVersionUID = 316_000000_1L;

	private ConcurrentLinkedQueue<ServerData> serverList;
	
	public MulticastData(){
		serverList = new ConcurrentLinkedQueue<>();
	}
	
	public void removeServer(ServerData server){
		serverList.remove(server);
	}
	
	public void insertServer(ServerData server){
		serverList.add(server);
		
	}
	
	public Iterable<ServerData> getServerList(){
		return serverList;
	}
	
}
