/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Frames;

import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Lenovo
 */
public class AlumnoListener implements Runnable{

    @Override
    public void run() {
        try {

            ServerSocket servidorMiddleware;
        
            
            while (true) {
                System.out.println("Servidor alumno");
                servidorMiddleware = new ServerSocket(4445);
                
                
                
                
                Socket socket = servidorMiddleware.accept();
                System.out.println("Aceptado maestro");
                
                

                DataInputStream entrada = new DataInputStream(socket.getInputStream());
                String texto = entrada.readUTF(); 
                System.out.println("Resultado maestro:"+texto);
                
                entrada.close();
                
                
                servidorMiddleware.close();
        
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
