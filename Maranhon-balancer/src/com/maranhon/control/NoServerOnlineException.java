package com.maranhon.control;

public class NoServerOnlineException extends Exception{
    
       
    /**
    * Construtor da classe ConexaoPerdidaException.
    */
    public NoServerOnlineException() {
        super("Conexão com o servidor foi perdida!");
    }
    
    /**
    * Construtor da clase ConexaoPerdidaException, sendo que o mesmo
    * está fazendo uma chamada para a sua superclasse passando uma mensagem
    * para a mesma.
    * 
    * @param msg String com uma mensagem de erro a ser passada para o construtor da superclasse.
    */
    public NoServerOnlineException(String msg) {
        super(msg);
    }

    /**
     * Construtor da classe ConexaoPerdidaException, sendo que o mesmo
     * está fazendo uma chamada para a sua superclasse passando uma exceção
     * para a mesma.
     * 
     * @param exception Exceção a ser passada para o construtor da supeclasse.
     */
    public NoServerOnlineException(Throwable exception) {
        super(exception);
    }

}
