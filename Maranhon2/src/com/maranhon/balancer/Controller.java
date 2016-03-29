package com.maranhon.balancer;

public class Controller {

	@SuppressWarnings("resource") // Nem a� se o programa parar de funcionar e o socket ficar aberto at� o momento em que eu fechar ele.
	public static void main(String[] args) {
            
            new Thread (new ThreadConnectionClient()).start();
            new Thread (new ThreadConnectionServer()).start();       
        }	
}
