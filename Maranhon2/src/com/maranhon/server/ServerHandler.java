package com.maranhon.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.maranhon.common.ErrorMessage;
import com.maranhon.common.ErrorMessage.ErrorType;
import com.maranhon.common.PurchaseRequest;

public class ServerHandler extends Thread{
	
	private Socket clientSocket;
	
	public ServerHandler(Socket socket) {
		this.clientSocket = socket;
	}

	public void run() {
		DatabaseController dbcontrol = DatabaseController.getInstance();
		
		try{
			OutputStream os = clientSocket.getOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(os);
		
			InputStream is = clientSocket.getInputStream();
			ObjectInputStream ois = new ObjectInputStream(is);
			
			Object x = ois.readObject();
			
			if(x instanceof PurchaseRequest){
				// TODO: return handlePurchase
				PurchaseRequest req = (PurchaseRequest)x;
				String requestID = dbcontrol.publishRequest(req);
				
				while(!dbcontrol.isQueryDone(requestID)){ // Espera ocupada.
					try {
						Thread.sleep(10); 
					} catch (InterruptedException e) {
						//Nada.
					} 
				}
				
				oos.writeObject(dbcontrol.getQueryResponse(requestID));
			} else{
				oos.writeObject(new ErrorMessage(ErrorType.InvalidRequest));
			}
			
		} catch(IOException | ClassNotFoundException e){
			
		}	
	}

}
