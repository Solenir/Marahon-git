package com.maranhon.server.model;

import java.io.Serializable;

public class QueryApproval implements Serializable{

	private static final long serialVersionUID = 316_000000_9L;
	
	private String requestID;
	private int serverID; 
	private boolean success;
	
	public QueryApproval(String requestID, int serverID, boolean success){
		this.requestID = requestID;
		this.serverID = serverID;
		this.success = success;
	}
	
	public String getRequestID(){
		return requestID;
	}
	
	public int getServerID(){
		return serverID;
	}
	
	public boolean wasExecuted(){
		return success;
	}
	
}
