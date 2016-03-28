/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Teste2;

import java.io.File;

import java.io.FileOutputStream;

import java.io.IOException;

import java.io.InputStream;
import java.io.ObjectInputStream;

import java.net.Socket;

public class Cliente {

    public static void main(String[] args) {

        //Criando Classe cliente para receber arquivo
        Cliente cliente = new Cliente();

        //Solicitando arquivo
        cliente.getFileFromServeR();

    }

    private void getFileFromServeR() {

        Socket sockServer = null;

        FileOutputStream fos = null;

        InputStream is = null;
        
        ObjectInputStream receber = null;

        try {

            // Criando conexão com o servidor
            System.out.println("Conectando com Servidor porta 13267");

            sockServer = new Socket("127.0.0.1", 13267);

            is = sockServer.getInputStream();

            // Cria arquivo local no cliente
            fos = new FileOutputStream(new File("ArquivoRecebido.txt"));

            // Prepara variaveis para transferencia
            System.out.println("Recebendo arquivo...");
            byte[] cbuffer = new byte[is.read()];
            is.read(cbuffer);
            fos.write(cbuffer);
            fos.flush();
            System.out.println("Arquivo recebido!");
            fos.close();
            
            fos = new FileOutputStream(new File("ArquivoRecebido2.txt"));
            System.out.println("Recebendo arquivo...");
            cbuffer = new byte[is.read()];
            is.read(cbuffer);
            fos.write(cbuffer);
            fos.flush();

            System.out.println("Arquivo 2 recebido!");
            
            receber = new ObjectInputStream(is);
            System.out.println((String)receber.readObject());
            

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            if (sockServer != null) {

                try {

                    sockServer.close();

                } catch (IOException e1) {

                    e1.printStackTrace();

                }

            }

            if (fos != null) {

                try {

                    fos.close();

                } catch (IOException e1) {

                    e1.printStackTrace();

                }

            }

            if (receber != null) {

                try {

                    receber.close();

                } catch (IOException e1) {

                    e1.printStackTrace();

                }

            }

        }

    }

}
