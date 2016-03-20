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
package com.maranhon.Model;

/**
 * Classe que representa um carrinho de compras essa classe será muito útil, 
 * pois se o cliente estiver conectado com um servidor em um dado momento
 * e esse servidor "cair", o cliente poderá se conectar a outro servidor
 * e continuar comprando normalmente, pois quando for finlizar a compra
 * ele vai enviar a relação dos livros que está no carrinho.
 * @author Joel Filho e Solenir Figuerêdo
 */
public class CartPurchase {
    
    private String livros; //Armazena o nome do livro que o cliente deseja.
    
    /**
     * Construtor da classe CartPurchase. Quando esta classe é instanciada
     * atribuimos o caractere "" no atributo livros. Caso contrário quando
     * fossemos concatenar teriámos o valor null no inicio da concatenação
     * das Strings com o título do livro que o cliente deseja.
     */
    public CartPurchase (){
    this.livros = "";
    }
    
    /**
     * Insere um livro no carrinho de compras;
     * @param livro String com o nome do livro que o cliente deseja.
     */
    public void insertBookCartPurchase (String livro){
        
        this.livros += "/"+livro;// Concatenado o livro que o cliente deseja.
    
    }
    /**
     * Retorna uma string contendo título do livro que o client deseja adquirir.
     * @return a String com os titulo de cada livro. 
     */
    public String returnCartPurchase(){
        return livros;
    } 
}
