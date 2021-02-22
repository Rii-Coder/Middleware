/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socket;

import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author R2
 */
public class Run {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        try {

            ServerSocket servidorMiddleware;
        
            
            while (true) {
                servidorMiddleware = new ServerSocket(4444);
                System.out.println("En espera.");
                
                
                
                Socket socket = servidorMiddleware.accept();
                System.out.println("Aceptado");
                
                

                DataInputStream entrada = new DataInputStream(socket.getInputStream());
                String texto = entrada.readUTF(); 
                System.out.println("Resultado:"+texto);
                
                entrada.close();
                
                
                servidorMiddleware.close();
        
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
