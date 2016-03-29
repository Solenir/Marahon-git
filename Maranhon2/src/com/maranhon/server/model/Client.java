package com.maranhon.server.model;

import java.io.Serializable;

public class Client implements Serializable{
	
	private static final long serialVersionUID = 316_000000_7L;

	private double valueBought;
	
	public Client(double valueBought){
		this.valueBought = valueBought ;
	}
	

	public synchronized void increaseValueBought(double price){
		this.valueBought += price;
	}
	
	public double getAmountBought(){
		return valueBought;
	}
	
}
