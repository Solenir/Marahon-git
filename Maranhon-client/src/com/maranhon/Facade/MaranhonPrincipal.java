
package com.maranhon.Facade;

import com.maranhon.Model.CartPurchase;
import java.util.Scanner;

/**
 *
 * @author solenir
 */
public class MaranhonPrincipal {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Facade facade = new Facade();
        
        Scanner input = new Scanner(System.in);
        String login, passWord, cpf = null;
        String title,author;
        int edicao, quantidade;
        System.out.println("Informe seu nome por favor.");
        login = input.nextLine();
        System.out.println("Informe sua senha por favor.");
        passWord = input.nextLine();
        
        System.out.println("Informe seu cpf");
        cpf = input.nextLine();
        if (facade.registeringClient(login, passWord, cpf) == 1)
            System.out.println("Cadastrado com sucesso!");
        else
            System.out.println("Impossível cadastrar novo usuário!");
        
        System.out.println("Livro que deseja comprar? ");
        title = input.nextLine();
        
        System.out.println("Autor do livro? ");
        author = input.nextLine();
        
        System.out.println("Edicao do livro? ");
        edicao = input.nextInt();
        
        System.out.println("Quantidade livros? ");
        quantidade = input.nextInt();
        int response = facade.addBookCartPurchase(title, author, edicao, quantidade);
        if ( response > 1)
            System.out.println("Impossivel comprar esse livro. Quantidade no estoque: "+ response);
        else{
            CartPurchase.getUniqueInstance().insertBookCartPurchase(title, author, edicao, quantidade);
            System.out.println("Colocado com sucesso no carrinho "+ response);
        
        }
            
        
            
        
        
    }
    
}
