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
public class Book {
    private String title;
    private String author;
    private double value;
    private int amount;
    
    public Book (String title, String author, double value, int amount) {
        this.title = title;
        this.author = author;
        this.value = value;
        this.amount = amount;
    }
    
    public Book (String title) {
       
        this.title = title;
    
    }
    
    public String getTitleAndAuthor(){
        return this.title+""+this.author;
    }
    
    public int getAmount (){
    
        return this.amount;
    }
    @Override
    public boolean equals (Object obj){
        
        if (obj instanceof Book)
            return ((Book)obj).getTitleAndAuthor() == getTitleAndAuthor();
        
        return false;
       
    }
    
    
}
