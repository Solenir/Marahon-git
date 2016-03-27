package com.maranhon.server.model;

import java.io.Serializable;

public class Book implements Serializable{

	private static final long serialVersionUID = 316_000000_6L;
	
	private int available;
	private double price;

	public Book(double price, int available){
		this.price = price;
		this.available = available;
	}
	
	public synchronized boolean buyBook(int amount){
		if(amount > available)
			return false;
		available -= amount;
		return true;
	}
	
	public double getPrice(){
		return price;
	}
	
	public double getAvailable(){
		return available;
	}
}
