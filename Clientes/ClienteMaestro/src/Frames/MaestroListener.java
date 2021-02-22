/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Frames;

import DotObjectNotatiton.DotObjectNotatiton;
import alumnomaestro.Alumno;
import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JTextArea;

/**
 *
 * @author Dhtey
 */
public class MaestroListener implements Runnable{
    private JTextArea jtaText;
    
    public MaestroListener(JTextArea area){
        this.jtaText = area;
    }
    @Override
    public void run() {
         try {
            ServerSocket servidorMiddleware;
            while (true) {
                System.out.println("Servidor maestro");
                servidorMiddleware = new ServerSocket(4446);
                
                Socket socket = servidorMiddleware.accept();
                System.out.println("Aceptado alumno");
                
                DataInputStream entrada = new DataInputStream(socket.getInputStream());
                String texto = entrada.readUTF(); 
                System.out.println("Resultado maestro:"+texto);
                
              
                
                entrada.close();
                servidorMiddleware.close();
                DotObjectNotatiton don = new DotObjectNotatiton();
                Alumno alumno = don.transformaAlumno(texto);
                this.jtaText.setText(alumno.toString());
                
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
