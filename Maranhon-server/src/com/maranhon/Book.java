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
    private int controlId;
    private String title;
    private String author;
    private int edition;
    private double value;
    private int amount;
    
    public Book (int controlId, String title, String author, int edition, double value, int amount) {
        this.controlId = controlId;
        this.title = title;
        this.author = author;
        this.value = value;
        this.amount = amount;
        
    }
    
    public Book (String title) {
       
        this.title = title;
    
    }

    public Book (String title, String author, int edition) {
        this.author = author;
        this.title = title;
        this.edition = edition;
    
    }
    
    public String getValidate(){
        return this.title+""+this.author;
    }
    
    public int getAmount (){
    
        return this.amount;
    }
    @Override
    public boolean equals (Object obj){
        
        return ((Book)obj).getValidate().equals(getValidate());
             
    }
    
    void setAmount(int amount) {
        this.amount += amount;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getEdition() {
        return edition;
    }

    void setValue(double value) {
        this.value = value;
    }

    public int getId() {
        return this.controlId;
    }
    
    
}
