/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Frames;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author Lenovo
 */
public class ClienteAlumno{
    
    private Socket socket;
    private DataInputStream bufferDeEntrada = null;
    private DataOutputStream bufferDeSalida = null;

    public ClienteAlumno(String ip, int puerto){
        ejecutarConexion(ip, puerto);
    }
    
    public void levantarConexion(String ip, int puerto) {
        try {
            socket = new Socket(ip, puerto);
            System.out.println("Conectado a :" + socket.getInetAddress().getHostName());
        } catch (Exception e) {
            System.out.println("Excepción al levantar conexión: " + e.getMessage());
            System.exit(0);
        }
    }


    public void abrirFlujos() {
        try {
            bufferDeEntrada = new DataInputStream(socket.getInputStream());
            bufferDeSalida = new DataOutputStream(socket.getOutputStream());
            bufferDeSalida.flush();
        } catch (IOException e) {
            System.out.println("Error en la apertura de flujos");
        }
    }

    public void enviar(String s) {
        try {
            bufferDeSalida.writeUTF(s);
            bufferDeSalida.flush();
        } catch (IOException e) {
            System.out.println("IOException on enviar");
        }
    }

    public void cerrarConexion() {
        try {
            bufferDeEntrada.close();
            bufferDeSalida.close();
            socket.close();
            System.out.println("Conexión terminada");
        } catch (IOException e) {
            System.out.println("IOException on cerrarConexion()");
        }
    }

    public void ejecutarConexion(String ip, int puerto) {
        Thread hilo = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    levantarConexion(ip, puerto);
                    abrirFlujos();
                    recibirDatos();
                } finally {
                    cerrarConexion();
                }
            }
        });
        hilo.start();
    }

    public void recibirDatos() {
        String st = "";
        try {
            do {
                st = (String) bufferDeEntrada.readUTF();
                System.out.println("\n[Servidor] => " + st);
                System.out.print("\n[Usted] => ");
            } while (true);
        } catch (IOException e) {}
    }

    public void escribirDatos() {
        String entrada = "";
        while (true) {
            System.out.print("[Usted] => ");
         //   entrada = teclado.nextLine();
            if(entrada.length() > 0)
                enviar(entrada);
        }
    }
    
}
