/**
 * Componente Curricular: Módulo integrador de Concorrência e Conectiviade
 * Autor: Joel pinto de Carvalho Filho e José Solenir Lima Figuerêdo
 * Data:  20/03/2016
 *
 * Declaramos que este código foi elaborado por nós em dupla e
 * não contém nenhum trecho de código de outro colega ou de outro autor, 
 * tais como provindos de livros e apostilas, e páginas ou documentos 
 * eletrônicos da Internet. Qualquer trecho de código de outra autoria que
 * uma citação para o não a nossa está destacado com autor e a fonte do
 * código, e estamos cientes que estes trechos não serão considerados para fins
 * de avaliação. Alguns trechos do código podem coincidir com de outros
 * colegas pois estes foram discutidos em sessões tutorias.
 */
package com.maranhon;

/**
 *
 * @author Joel Filho e Solenir Figuerêdo
 */
public class Client {
    private int iD;
    private String login;
    private String passWord;
    private String cpf;
    private int amountBooks;
    

    public Client(int iD, String login, String passWord, String cpf, int amountBooks) {
        this.iD = iD;
        this.login = login;
        this.passWord = passWord;
        this.cpf = cpf;
        this.amountBooks = amountBooks;
    }
       
    public Client(String login) {
        this.login = login;
    }

    public int getId() {
        return iD;
    }

    public boolean authenticateLogin(String passWord) {
        return this.passWord == passWord;
    }
    
    public boolean equals(Object obj){
          return ((Client)obj).cpf.equals(this.cpf);
    }
    
}
