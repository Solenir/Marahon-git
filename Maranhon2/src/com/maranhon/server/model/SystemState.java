package com.maranhon.server.model;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

public class SystemState implements Serializable{
	private static final long serialVersionUID = 316_000000_12L;
	
	public int logicalClock;
	public ConcurrentHashMap<Integer, Client> clientList;
	public ConcurrentHashMap<Integer, Book> bookList;
	
}
