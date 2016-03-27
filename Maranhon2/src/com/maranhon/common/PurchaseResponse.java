package com.maranhon.common;

import java.io.Serializable;

public class PurchaseResponse implements Serializable {

	private static final long serialVersionUID = 316_000000_5L;
	
	private boolean success;
	private String requestID;
	private int serverID;
	
	public PurchaseResponse(String requestID, int serverID, boolean success){
		this.requestID = requestID;
		this.success = success;
		this.serverID = serverID;
	}

	public boolean isSuccessful() {
		return success;
	}

	public String getRequestID() {
		return requestID;
	}
	
	public int getServerID(){
		return serverID;
	}
	
	
}
