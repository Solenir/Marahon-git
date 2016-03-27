package com.maranhon.common;

import java.io.Serializable;

public class PurchaseRequest implements Serializable{

	private static final long serialVersionUID = 316_000000_4L;
	
	private int clientID;
	private int bookID;
	private int quantity;
	
	public PurchaseRequest(int clientID, int bookID, int quantity) {
		this.clientID = clientID;
		this.bookID = bookID;
		this.quantity = quantity;
	}
	
	public int getClientID() {
		return clientID;
	}
	
	public int getBookID() {
		return bookID;
	}
	public int getQuantity() {
		return quantity;
	}
	
	
	
}
