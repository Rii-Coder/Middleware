/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Frames;

import alumnomaestro.Director;
import com.google.gson.Gson;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import javax.swing.JTextArea;

/**
 *
 * @author Lenovo
 */
public class ClienteDirector{
    
    private Socket socket;
    private DataInputStream bufferDeEntrada = null;
    private DataOutputStream bufferDeSalida = null;
    private JTextArea textArea;
    
    public ClienteDirector(JTextArea textArea){
        this.textArea = textArea;
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
                Gson gson = new Gson();
                this.textArea.setText(gson.fromJson(st,Director.class).toString());
                System.out.println("\n[Servidor] => " + st);
            } while (true);
        } catch (IOException e) {}
    }
    
}
