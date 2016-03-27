package com.maranhon.server.model;

public class QueryTimer extends Thread{
	
	private static final int QUERY_DELAY_MS = 100;
	
	private boolean finished;
	
	public QueryTimer(){
		finished = false;
	}
	
	public boolean isFinished(){
		return finished;
	}
	
	public void run() {
		try {
			Thread.sleep(QUERY_DELAY_MS);
		} catch (InterruptedException e) {
			
		} finally{
			finished = true;
		}
	}

}
