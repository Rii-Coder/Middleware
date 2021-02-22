/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socket;

import CommaObjectNotation.CommaObjectNotation;
import interpreter.Context;
import interpreter.FormatosEnum;
import interpreter.InterpreterClient;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Lenovo
 */
public class MiddlewareListener implements Runnable{

    @Override
    public void run() {
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
                
                if(texto.contains(",")){
                    this.mandarAlumno(texto);
                }else{
                    this.mandarMaestro();
                }
        
            }

        } catch (Exception e) {
            e.printStackTrace();
        }    
    }
    
    public void mandarAlumno(String texto){
        Context context = new Context(texto, FormatosEnum.CON, FormatosEnum.DON);
        String transformado = InterpreterClient.interpretar(context);
        
        try {
            Socket alumnoSocket = new Socket("localhost",4446);


            CommaObjectNotation con = new CommaObjectNotation();
            DataOutputStream salida = new DataOutputStream(alumnoSocket.getOutputStream());
            salida.writeUTF(transformado);

            salida.close();


        } catch (IOException ex) {
                
        }
    }
    
    public void mandarMaestro(){
        
    }
    
}
