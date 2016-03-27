package com.maranhon.server.model;

import java.io.Serializable;

public class Client implements Serializable{
	
	private static final long serialVersionUID = 316_000000_7L;

	private double valueBought;
	
	public Client(){
		valueBought = 0;
	}
	

	public synchronized void increaseValueBought(double price){
		valueBought += price;
	}
	
	public double getAmountBought(){
		return valueBought;
	}
	
}
