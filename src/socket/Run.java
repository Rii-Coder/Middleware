/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socket;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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

            ServerSocket servidorAlumno;
            ServerSocket servidorMaestro;
            
            while (true) {
                servidorAlumno = new ServerSocket(4444);
                System.out.println("En espera alumno");
                
                servidorMaestro = new ServerSocket(4445);
                System.out.println("En espera maestro");
                
                Socket socketAlumno = servidorAlumno.accept();
                System.out.println("Alumno aceptado");
                
                Socket socketMaestro = servidorMaestro.accept();
                System.out.println("maestro aceptado");

                DataInputStream entradaAlumno = new DataInputStream(socketAlumno.getInputStream());
                String mensajeAlumno = entradaAlumno.readUTF();
                System.out.println("alumno:"+mensajeAlumno);
                
                entradaAlumno.close();
                
                DataInputStream entradaMaestro = new DataInputStream(socketMaestro.getInputStream());
                String mensajeMaestro = entradaMaestro.readUTF();
                System.out.println("maestro:"+mensajeMaestro);
                
                entradaMaestro.close();
                
                servidorAlumno.close();
                servidorMaestro.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
