/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socket;

import interpreter.Context;
import interpreter.FormatosEnum;
import interpreter.InterpreterClient;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Lenovo
 */
public class Servidor {


    private Socket socketAlumno;
    private ServerSocket serverSocketAlumno;
    private DataInputStream bufferDeEntradaAlumno = null;
    private DataOutputStream bufferDeSalidaAlumno = null;
    private ArrayList<Socket> conexiones;
    
    public Servidor(int puertoAlumno){
        try {
            this.conexiones = new ArrayList();
            System.out.println("Iniciando servidor...");
            serverSocketAlumno = new ServerSocket(puertoAlumno);
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void levantarConexion() {
        try {
            
            
            System.out.println("Esperando conexion...");
            this.socketAlumno = this.serverSocketAlumno.accept();
            this.conexiones.add(this.socketAlumno);
            System.out.println("Cliente " + this.socketAlumno.getPort() + " se ha conectado.");
        } catch (Exception e) {
            System.out.println("Error en levantarConexion(): " + e.getMessage());
            System.exit(0);
        }
    }


    public void flujos() {
        try {
            bufferDeEntradaAlumno = new DataInputStream(socketAlumno.getInputStream());
            bufferDeSalidaAlumno = new DataOutputStream(socketAlumno.getOutputStream());
            bufferDeSalidaAlumno.flush();
        } catch (IOException e) {
            System.out.println("Error en la apertura de flujos");
        }
    }

    public void recibirDatos() {
        String st = "";

        try {
            do {
                System.out.println("Leyendo...");
                st = (String) bufferDeEntradaAlumno.readUTF();
                enviar(st);
                System.out.println("\n[Cliente] => " + st);
            } while (true);
        } catch (IOException e) {
            cerrarConexion();
        }
    }

    public void enviar(String s) {
        try {
            Context context = null;
            if(true){
                context = new Context(s, FormatosEnum.CON, FormatosEnum.DON);
            }else{
                context = new Context(s, FormatosEnum.DON, FormatosEnum.CON);
                
            }
            int index = this.conexiones.indexOf(this.socketAlumno);
            
            DataOutputStream destino = null;
            if(true){
                destino = new DataOutputStream(this.conexiones.get(1).getOutputStream());
            }else{
                destino = new DataOutputStream(this.conexiones.get(0).getOutputStream());
            }
            
            String transformado = InterpreterClient.interpretar(context);
            System.out.println("Formato transformado: " + transformado);
            destino.writeUTF(transformado);
            destino.flush();
        } catch (IOException e) {
            System.out.println("Error en enviar(): " + e.getMessage());
        }
    }

    public void cerrarConexion() {
        try {
            bufferDeEntradaAlumno.close();
            bufferDeSalidaAlumno.close();
            socketAlumno.close();
        } catch (IOException e) {
            System.out.print("Excepción en cerrarConexion(): " + e.getMessage());
        } finally {
            System.out.print("Conversación finalizada....");
            System.exit(0);

        }
    }

    public void ejecutarConexion() {
        Thread hilo = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        levantarConexion();
                        flujos();
                        recibirDatos();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });
        hilo.start();
    }

}
