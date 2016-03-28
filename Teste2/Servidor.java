package Teste2;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Lucas iorio - http://www.byiorio.com
 *
 * @author Lucas iorio - http://www.byiorio.com
 *
 */
public class Servidor {

    public static void main(String[] args) {

        // Criando servidor
        Servidor server = new Servidor();

        // Aguardar conexao de cliente para transferia
        server.waitForClient();

    }

    public void waitForClient() {
        // Checa se a transferencia foi completada com sucesso
        OutputStream socketOut = null;
        ServerSocket servsock = null;
        FileInputStream fileIn = null;
        ObjectOutputStream enviar = null;
        try {
            // Abrindo porta para conexao de clients
            servsock = new ServerSocket(13267);
            System.out.println("Porta de conexao aberta 13267");

            // Cliente conectado
            Socket sock = servsock.accept();
            System.out.println("Conexao recebida pelo cliente");

            // Criando arquivo que sera transferido pelo servidor
            File file = new File("Teste.txt");
            fileIn = new FileInputStream(file);
            System.out.println("Lendo arquivo...");
            
            // Criando tamanho de leitura
            byte[] cbuffer = new byte[(int)file.length()];
            fileIn.read(cbuffer);

            // Criando canal de transferencia
            socketOut = sock.getOutputStream();

            // enviado para o canal de transferencia
            System.out.println("Enviando Arquivo...");
            socketOut.write(cbuffer.length);
            socketOut.write(cbuffer);
            socketOut.flush();
            
            System.out.println("Arquivo Enviado!");
            fileIn.close();
            
            file = new File("Teste.txt");
            fileIn = new FileInputStream(file);
            cbuffer = new byte[(int)file.length()];
            fileIn.read(cbuffer);
            System.out.println("Enviando Arquivo...");
            socketOut.write(cbuffer.length);
            socketOut.write(cbuffer);
            socketOut.flush();
            
            System.out.println("Arquivo Enviado!");
            
            enviar = new ObjectOutputStream(socketOut);
            enviar.writeObject("OiTeste");
        } catch (Exception e) {
            // Mostra erro no console
            e.printStackTrace();
        } finally {
            if (socketOut != null) {
                try {
                    enviar.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (servsock != null) {
                try {
                    servsock.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (fileIn != null) {
                try {
                    fileIn.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
