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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.LinkedList;


/**
 *
 * @author Joel Filho e Solenir Figuerêdo
 */
public class StockControl {
    
    private static StockControl uniqueInstance = new StockControl();
    private LinkedList<Book> books;
    private int controlId;
    
    
    private StockControl (){
        this.books = new LinkedList<>();
        readingBooks();
        this.controlId = books.size()+1;
        
            
    }
    
    public static StockControl getInstance () {
 
        return uniqueInstance;
    }
    
    private void readingBooks() {
            try{
            
            FileReader reading = new FileReader(new File("Livros.txt"));
            BufferedReader sequentialRead = new BufferedReader(reading);
            String line = sequentialRead.readLine();
            String dataBook[] = null;
            
            while(line != null) {
                
                dataBook = line.split(" ");
                books.add(new Book(Integer.parseInt(dataBook[0]), dataBook[1], dataBook[2], Double.parseDouble(dataBook[3]), Integer.parseInt(dataBook[4])));
                
                line = sequentialRead.readLine();
            }
            reading.close();
        }
        catch (Exception ex) {
            System.err.println(ex.toString());
        }
    
    
    }
  
    
    public synchronized int registerBook (String title, String author, double value, int amount) {
    
        Book book = searchForBook(title, author);
        if (book == null){
            Book newBook = new Book(this.controlId++, title, author, value, amount);
            books.add(newBook);
            try {
                //Abre arquivo onde tem Livros ja cadastrados.
                FileWriter file = new FileWriter(new File("Livros.txt"), true);
                PrintWriter write = new PrintWriter(file);
                String register = ""+newBook.getId()+" "+title+" "+author+" "+value+" "+amount;
                //Laço de repetiçao utilizado para inserir novos usuarios no arquivo de texto.
                while(register.length() < 50)
                    register += " ";
                write.write(register+"\r\n");
                write.close();
            } catch (Exception ex) {
                
                System.err.println("Erro em cadastrarCliente() de ControlClient.\n"+ex.toString());
                return 0;
            }
                 
            return 1;         
        
        }
        
        book.setAmount(amount);
        book.setValue(value);
        return 1;     
    
    } 
    
    private Book searchForBook(String title, String author) {
        
        if (books.indexOf(new Book(title, author)) != -1)
            return books.get(books.indexOf(new Book (title, author)));
        return null;
            
    
    }    
    public synchronized int cartPurchase (String title, String author, int amount) {
        
        Book book = searchForBook(title, author);
        
        if (book != null)
            if (book.getAmount() >= amount)
                return 1;
            else
                return book.getAmount();        
        return 0;
    }
    
    
    
}
