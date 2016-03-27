package com.maranhon.server;

import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;

import com.maranhon.common.PurchaseRequest;
import com.maranhon.common.PurchaseResponse;
import com.maranhon.server.model.Book;
import com.maranhon.server.model.Client;
import com.maranhon.server.model.PurchaseQuery;
import com.maranhon.server.model.QueryApproval;
import com.maranhon.server.model.QueryTimer;
import com.maranhon.server.util.RequestIDGenerator;

public class DatabaseController {

	private static DatabaseController instance;
	
	private RequestIDGenerator idgenerator;
	
	private ConcurrentHashMap<Integer, Client> clientList;
	private ConcurrentHashMap<Integer, Book> bookList;
	
	private int logicalClock;
	private int serverID;
	
	private ConcurrentHashMap<String, Integer> queryAuthorizationCounter; // Contador da quantidade de autorizações recebidas para executar
	private ConcurrentHashMap<String, QueryTimer> queryDelay;
	
	private ConcurrentHashMap<String, PurchaseResponse> outputList; //Requests que já foram executadas e estão esperando serem chamadas para sair
	
	private PriorityQueue<PurchaseQuery> executionQueue;
	
	private DatabaseController(){
		clientList = new ConcurrentHashMap<>();
		bookList = new ConcurrentHashMap<>();
		queryAuthorizationCounter = new ConcurrentHashMap<>();
		outputList = new ConcurrentHashMap<>();
		queryDelay = new ConcurrentHashMap<>();
		
		executionQueue = new PriorityQueue<>();
		
		idgenerator = new RequestIDGenerator();
		
		serverID = ServerController.getInstance().getData().getServerID();
		
		initializeDatabase();
		QueryExecutioner qex = new QueryExecutioner();
		qex.start();
	}
	
	public synchronized void authorizationReceived(QueryApproval approval){
		Integer i = queryAuthorizationCounter.get(approval.getRequestID());
		if(i == null)
			return; //Já deve ter passado do mínimo, executou a bagaça e o cara chegou nesse caso especial. TODO: Imprimir algo para testar 
		queryAuthorizationCounter.put(approval.getRequestID(), i+1);
	}
	
	private int minimumRequiredApprovals(){
		//TODO: pegar a quantidade de servidores em Multicast
		return 1;
	}
	
	private void initializeDatabase() {
		//TODO: usar a partir de arquivo
		logicalClock = 0;
		for(int i=0; i<100; i++){
			clientList.put(i, new Client());
		}
		for(int i=0; i<200; i++){
			bookList.put(i, new Book(10f*(i+1)/25f, 100));
		}	
	}

	public static DatabaseController getInstance(){
		if(instance == null)
			instance = new DatabaseController();
		return instance;
	}
	
	public synchronized String publishRequest(PurchaseRequest req){
		String requestID = idgenerator.nextID();
		logicalClock++;
		PurchaseQuery query = new PurchaseQuery(requestID, req, logicalClock, ServerController.getInstance().getData().getServerID());
		queryAuthorizationCounter.put(requestID, 1);
		QueryTimer timer = new QueryTimer();
		queryDelay.put(requestID, timer);
		timer.start();
		
		enqueue(query);
		
		//TODO: Enviar a query no MulticastVirtualizer
		
		return requestID;
	}

	private synchronized void enqueue(PurchaseQuery query) {
		concurrentQueryQueueProcessing(true, query);
		
	}

	public boolean isQueryDone(String requestID){
		return outputList.containsKey(requestID);
	}
	
	public PurchaseResponse getQueryResponse(String requestID){
		return outputList.remove(requestID);
	}
	
	private synchronized PurchaseQuery concurrentQueryQueueProcessing(boolean put, PurchaseQuery query){
		if(put){
			executionQueue.add(query);
			return query;
		} else{
			return executionQueue.poll();
		}
	}
	
	public synchronized void executeNextQuery(){
		PurchaseQuery query = executionQueue.peek();
		if(query == null)
			return;
		
		String rID = query.getRequestID();
		if(!queryDelay.get(rID).isFinished())
			return;
		
		if(query.getServerID() == serverID && queryAuthorizationCounter.get(rID) < minimumRequiredApprovals())
			return;
			
		concurrentQueryQueueProcessing(false, null);
		
		Book book = bookList.get(query.getBookID());
		
		boolean done = book.buyBook(query.getBookCount());
		if(done){
			clientList.get(query.getClientID()).increaseValueBought(query.getBookCount() * book.getPrice());
		}
		
		if(done)
			System.out.println("Comprado: "+query.getBookID()+"x"+query.getBookCount()+". Restante: "+book.getAvailable());
		else
			System.out.println("Tentou comprar: "+query.getBookID()+"x"+query.getBookCount()+". Restante: "+book.getAvailable());
		
		PurchaseResponse response = new PurchaseResponse(rID, serverID, done);
		//TODO: enviar response no MulticastVirtualizer
		
		if(query.getServerID() == serverID){
			queryAuthorizationCounter.remove(rID);
			queryDelay.remove(rID);
			outputList.put(rID, response);
		}
		
	} 
	
	private class QueryExecutioner extends Thread{
		
		public void run() {
			while(true){
				executeNextQuery();
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// Espero que nunca chegue aqui. Senão, ferrou.
				}
			}
		}
	}

	
}
