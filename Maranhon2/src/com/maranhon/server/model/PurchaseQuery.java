package com.maranhon.server.model;

import java.io.Serializable;

import com.maranhon.common.PurchaseRequest;

public class PurchaseQuery implements Serializable, Comparable<PurchaseQuery>{

	private static final long serialVersionUID = 316_000000_8L;
	
	private String requestID;
	private int serverID;
	private int logicalClock;
	private int clientID;
	private int bookID;
	private int bookCount;
	
	public PurchaseQuery(String requestID, PurchaseRequest request, int logicalClock, int serverID){
		this.requestID = requestID;
		this.logicalClock = logicalClock;
		this.serverID = serverID;
		this.clientID = request.getClientID();
		this.bookCount = request.getQuantity();
		this.bookID = request.getBookID();
	}
	
	public String getRequestID(){
		return requestID;
	}
	public int getServerID() {
		return serverID;
	}
	public int getLogicalClock() {
		return logicalClock;
	}
	public int getClientID() {
		return clientID;
	}
	public int getBookID() {
		return bookID;
	}
	public int getBookCount() {
		return bookCount;
	}

	@Override
	public int compareTo(PurchaseQuery o) {
		if(logicalClock == o.logicalClock) // Única específica. Escolhi por ordem alfabética em vez de ordem de servidor nesse caso.
			return requestID.compareTo(o.requestID);
		return logicalClock - o.logicalClock;
	}
	
}
