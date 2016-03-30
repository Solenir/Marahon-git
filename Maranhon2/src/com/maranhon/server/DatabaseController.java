package com.maranhon.server;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.maranhon.common.PurchaseRequest;
import com.maranhon.common.PurchaseResponse;
import com.maranhon.server.model.Book;
import com.maranhon.server.model.Client;
import com.maranhon.server.model.PurchaseQuery;
import com.maranhon.server.model.QueryApproval;
import com.maranhon.server.model.QueryTimer;
import com.maranhon.server.model.SystemState;
import com.maranhon.server.multicast.BufferedUnicast;
import com.maranhon.server.multicast.MulticastVirtualizer;
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
	
	private MulticastVirtualizer multicast; 
	
	private int successes=0, failures=0;

	private int locked = 0;
	private ConcurrentLinkedQueue<BufferedUnicast> waiters;

	private boolean isRunning;
	
	private DatabaseController(){
		clientList = new ConcurrentHashMap<>();
		bookList = new ConcurrentHashMap<>();
		queryAuthorizationCounter = new ConcurrentHashMap<>();
		outputList = new ConcurrentHashMap<>();
		queryDelay = new ConcurrentHashMap<>();
		
		executionQueue = new PriorityQueue<>();
		
		idgenerator = new RequestIDGenerator();
	
		waiters = new ConcurrentLinkedQueue<>();
		
		serverID = ServerController.getInstance().getData().getServerID();
		isRunning = false;
		
		initializeDatabase();
		
		multicast = MulticastVirtualizer.getInstance();
	}
	
	public synchronized void authorizationReceived(QueryApproval approval){
		Integer i = queryAuthorizationCounter.get(approval.getRequestID());
		if(i == null)
			return; //Já deve ter passado do mínimo, executou a bagaça e o cara chegou nesse caso especial.
		queryAuthorizationCounter.put(approval.getRequestID(), i+1);
	}
	
	private int minimumRequiredApprovals(){
		int minness = MulticastVirtualizer.getInstance().getNumConnections() + 1 - locked;
		System.out.println("Mínimo necessário: "+minness);
		return minness;
	}
	
	private void initializeDatabase() {
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
		PurchaseQuery query = new PurchaseQuery(requestID, req, logicalClock, serverID);
		queryAuthorizationCounter.put(requestID, 1);
		QueryTimer timer = new QueryTimer();
		queryDelay.put(requestID, timer);
		timer.start();
		
		enqueue(query);
		multicast.sendObject(query);
		
		System.out.println("Purchase request received. And retransmitted. I hope.");
		
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
		if(!isRunning)
			return;
		
		PurchaseQuery query = executionQueue.peek();
		if(query == null && locked==0)
			return;
		
		if(query == null && locked > 0){
			SystemState ss = new SystemState();
			ss.logicalClock = logicalClock;
			ss.bookList = bookList;
			ss.clientList = clientList;
			waiters.poll().sendObject(ss);
			locked--;
			System.out.println("Locked #"+locked);
			return;
		}
		
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
		
		if(done)
			successes++;
		else
			failures++;
		
		System.out.println("Bem-sucedidas: "+successes+", Negadas: "+failures);
		
		if(query.getServerID() == serverID){
			queryAuthorizationCounter.remove(rID);
			queryDelay.remove(rID);
			PurchaseResponse response = new PurchaseResponse(rID, serverID, done);
			outputList.put(rID, response);
		} else{
			QueryApproval approval = new QueryApproval(rID, serverID, done);
			multicast.sendObject(approval);
			//System.out.println("Aprovação Enviada");
		}
		
	} 
	
	public synchronized void processClusterData(Object obj){
		if(obj instanceof QueryApproval){
			//System.out.println("Query approved");
			authorizationReceived((QueryApproval)obj);
		} else if(obj instanceof PurchaseQuery){
			//System.out.println("Query received");
			PurchaseQuery q = (PurchaseQuery)obj;
			logicalClock = Math.max(logicalClock, q.getLogicalClock());
			enqueue(q);
			QueryTimer timer = new QueryTimer();
			queryDelay.put(q.getRequestID(), timer);
			timer.start();
		} else if(obj instanceof SystemState){
			initializeData((SystemState)obj);
		}
	} 
	
	private class QueryExecutioner extends Thread{
		
		public void run() {
			while(true){
				executeNextQuery();
				try {
					//System.out.println("Testando... ");
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// Espero que nunca chegue aqui. Senão, ferrou.
				}
			}
		}
	}

	public synchronized void newServer(BufferedUnicast conn) {
		locked++;
		waiters.add(conn);
		System.out.println("Servidor entrou no domínio. Estou trancado");
	}

	public boolean isLocked(){
		return locked > 0 || !isRunning;
	}

	public synchronized void startRunning() {
		/*SystemState ss = new SystemState();
		ss.logicalClock = logicalClock;
		ss.bookList = bookList;
		ss.clientList = clientList;
		MulticastVirtualizer.getInstance().sendObject(ss);*/
		if(isRunning == false){
			QueryExecutioner qex = new QueryExecutioner();
			qex.start();
		}
		isRunning = true;
		MulticastVirtualizer.getInstance().sendHeartBeat();
		System.out.println("I'm free");
	}
	
	public synchronized void initializeData(SystemState ss){
		System.out.println("Recebido dado");
		if(ss.logicalClock >= logicalClock){
			logicalClock = ss.logicalClock;
			bookList = ss.bookList;
			clientList = ss.clientList;
			startRunning();
		}
		
	}
	
	public int getLocked(){return locked;}
	public boolean getRunning(){return isRunning;}

	public synchronized void writeFiles(){
		isRunning = false;
		try{
			writerFileBook();
			writerFileClient();
		} catch(IOException e){
			System.err.println("Erro de entrada e saída:");
			e.printStackTrace();
		} catch(Exception e){
			System.err.println("FFFFFFFFUUUUUUUUUUUUUUUUUUUUUUU");
			e.printStackTrace();
		}
		isRunning=true;
	}
	
	public synchronized void writerFileBook() throws IOException{
		 FileWriter file = new FileWriter(serverID+"_livros.txt");
	     PrintWriter writer = new PrintWriter(file);
	     
	     writer.println(logicalClock);
	      for (int i = 0; i < 200; i++){
	          Book book = bookList.get(i);
	          //String dado =""+i+" "+book.getPrice()+" "+book.getAvailable();
	          //writer.println(dado);
	          writer.printf("%d\t%.2f\t%d\r\n", i, book.getPrice(), book.getAvailable());
	      }
	      
	      writer.close();
	        
		
	}
	
	public synchronized void writerFileClient() throws IOException{
		 FileWriter file = new FileWriter(serverID+"_clientes.txt");
	     PrintWriter writer = new PrintWriter(file);
	     
	     writer.println(logicalClock);
	      for (int i = 0; i < 100; i++){
	          Client client = clientList.get(i);
	          //String dado =""+i+" "+client.getAmountBought();
	          //writer.println(dado);
	          writer.printf("%d\t%.2f\r\n", i, client.getAmountBought());
	      }
	      
	      writer.close();
	        
		
	}
	
}
