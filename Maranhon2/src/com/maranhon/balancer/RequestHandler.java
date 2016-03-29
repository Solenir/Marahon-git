package com.maranhon.balancer;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.maranhon.common.ErrorMessage;

import com.maranhon.common.ErrorMessage.ErrorType;

public class RequestHandler extends Thread{

	private ServerData server;
	private Socket clientSocket;

	
	public RequestHandler(ServerData server, Socket socket) {
		this.server = server;
		this.clientSocket = socket;
	}

	@Override
	public void run() {

		Socket serverSocket = null;
		try{
			OutputStream os = clientSocket.getOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(os);
		
			InputStream is = clientSocket.getInputStream();
			ObjectInputStream ois = new ObjectInputStream(is);
			
			Object x = ois.readObject();
			
			if(server == null){
				ErrorMessage err = new ErrorMessage(ErrorType.NoServerOnline);
				oos.writeObject(err);
				return;
			}
	 
                        System.out.println("Aquiiiiii"+server.getServerIP()+" "+server.getServerHostPort());
			serverSocket = new Socket(server.getServerIP(), server.getServerHostPort());
			
			OutputStream sos = serverSocket.getOutputStream();
			ObjectOutputStream soos = new ObjectOutputStream(sos);
			
			InputStream sis = serverSocket.getInputStream();
			ObjectInputStream sois = new ObjectInputStream(sis);
			
			soos.writeObject(x);
			
			Object y = sois.readObject();
			oos.writeObject(y);
			
		} catch(Exception e){
			
		} finally{
			try {
				clientSocket.close();
				if(serverSocket != null)
					serverSocket.close();
			} catch (IOException e) {
				//Se falhou em fechar algo, deixa aberto eternamente. Ou ent�o � porque j� fechou, mesmo...
			}
		}
		
	}


}
