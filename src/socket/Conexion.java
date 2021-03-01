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
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author Lenovo
 */
public class Conexion {
    private String nombre;
    private Socket socket;
    private DataInputStream bufferDeEntrada = null;
    private DataOutputStream bufferDeSalida = null;
    private ArrayList<Conexion> conexiones;
    
    
    
    public Conexion(Socket socket, ArrayList<Conexion> conexiones){
        this.socket = socket;
        this.conexiones = conexiones;
    }
    
    public void flujos() {
        try {
            bufferDeEntrada = new DataInputStream(socket.getInputStream());
            bufferDeSalida = new DataOutputStream(socket.getOutputStream());
            bufferDeSalida.flush();
        } catch (IOException e) {
            System.out.println("Error en la apertura de flujos");
        }
    }

    public void recibirDatos() {
        String st = "";

        try {
            do {
                System.out.println("Leyendo...");
                st = (String) bufferDeEntrada.readUTF();
                String[] datos = st.split("]");
                String[] direcciones = datos[0].split("-");
                this.nombre = direcciones[0].replace("[", "");
                st = datos[1];
                
                
                enviar(st, direcciones[1]);
                System.out.println("\n[Cliente] => " + st);
                
                System.out.println(this.nombre);
            } while (true);
        } catch (IOException e) {
            cerrarConexion();
        }
    }

    public void enviar(String s, String destino) {
        try {
            Context context = null;
           
            if(s.contains(",")){
                context = new Context(s, FormatosEnum.CON, FormatosEnum.DON);
            }else{
                context = new Context(s, FormatosEnum.DON, FormatosEnum.CON);
            }
            int index = this.conexiones.indexOf(this);
            DataOutputStream salidaDestino = null;
            if(index == 0){
                salidaDestino = new DataOutputStream(this.conexiones.get(1).getSocket().getOutputStream());
            }else{
                salidaDestino = new DataOutputStream(this.conexiones.get(0).getSocket().getOutputStream());
            }
            
            String transformado = InterpreterClient.interpretar(context);
            System.out.println("Formato transformado: " + transformado);
            salidaDestino.writeUTF(transformado);
            salidaDestino.flush();
        } catch (IOException e) {
            System.out.println("Error en enviar(): " + e.getMessage());
        }
    }

    public void cerrarConexion() {
        try {
            bufferDeEntrada.close();
            bufferDeSalida.close();
            socket.close();
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
                try {
                    flujos();
                    recibirDatos();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
        hilo.start();
    }
    
    public Socket getSocket(){
        return this.socket;
    }
}
