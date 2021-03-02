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
import java.util.logging.Level;
import java.util.logging.Logger;

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
    
    public void establecerConexion(){
        try {
            this.flujos();
            this.nombre = (String) bufferDeEntrada.readUTF();
        } catch (IOException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
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
                String destino = datos[0].replace("[", "");
                st = datos[1];
                
                System.out.println("Nombre: " + this.nombre );
                System.out.println("Destino: " + destino);
                
                enviar(st, destino);
                System.out.println("\n[Cliente] => " + st);
                
                System.out.println(this.nombre);
            } while (true);
        } catch (IOException e) {
            cerrarConexion();
        }
    }

    public void enviar(String s, String destino) {
        try {
            Context context = new Context(s, this.determinarFormato(this.nombre), this.determinarFormato(destino));
           
            int index = this.conexiones.indexOf(this);
            DataOutputStream salidaDestino = new DataOutputStream(this.determinarDestinatario(destino).getOutputStream());
            
            String transformado = InterpreterClient.interpretar(context);
            System.out.println("Formato transformado: " + transformado);
            salidaDestino.writeUTF(transformado);
            salidaDestino.flush();
        } catch (IOException e) {
            System.out.println("Error en enviar(): " + e.getMessage());
        }
    }
    
    private FormatosEnum determinarFormato(String tipo){
        switch(tipo){
            case "Alumno":
                return FormatosEnum.CON;
            case "Maestro":
                return FormatosEnum.DON;
            case "Director": 
                return FormatosEnum.JSON;
            default:
                return null;
        }
    }
    
    private Socket determinarDestinatario(String nombre){
        for(int f = 0; f < this.conexiones.size(); f++){

            if(this.conexiones.get(f).getNombre().equals(nombre) ){
                return this.conexiones.get(f).getSocket();
            }
        }
        return null;
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
    
    public String getNombre(){
        return this.nombre;
    }
}
