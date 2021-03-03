/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Frames;

import CommaObjectNotation.CommaObjectNotation;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.JTextArea;

/**
 *
 * @author Lenovo
 */
public class ClienteAlumno implements Framer{
    
    private Socket socket;
    private DataInputStream bufferDeEntrada = null;
    private DataOutputStream bufferDeSalida = null;
    private InputStream in = null;
    private OutputStream out = null;
    private JTextArea textArea;
    private final int LONGITUD_MAX = 10;
    private final byte BYTE_RELLENO = 126;
    
    public ClienteAlumno(JTextArea textArea){
        this.textArea = textArea;
    }
    
    
    public void levantarConexion(String ip, int puerto) {
        try {
            socket = new Socket(ip, puerto);
            this.abrirFlujos();
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
        byte[] bytes = serializar(s);
        in = new DataInputStream(new ByteArrayInputStream(bytes));
        out = new DataOutputStream(new ByteArrayOutputStream());
        try {
            if (!s.equalsIgnoreCase("alumno")) {
                
                System.out.println("Mensaje serializado: " + bytes.toString());
                byte[][] divisionesMensaje = this.dividirMensaje(bytes);
                ArrayList<Byte> mensajeArmado = new ArrayList();
                for(int f = 0; f < divisionesMensaje.length; f++){
                    byte[] mensaje = this.nextMsg(divisionesMensaje[f]);
                    for(byte b : mensaje){
                        mensajeArmado.add(b);
                    }
                }


                out.close();
                in.close();
                byte[] deserealizado = new byte[mensajeArmado.size()];
          
                for(int f = 0; f < deserealizado.length; f++){
                    
                    deserealizado[f] = mensajeArmado.get(f);
                }
                String entregado = deserializar(bytes);
                System.out.println("mensaje" + entregado);
                System.out.println("Mensaje deserializado: " + entregado);
                System.out.println("Se envio");
                bufferDeSalida.writeUTF(entregado);
                bufferDeSalida.flush();
            }else {
                bufferDeSalida.writeUTF(s);
                bufferDeSalida.flush();
            }
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

    public void ejecutarConexion() {
        Thread hilo = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
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
                
                this.textArea.setText(this.determinarClase(st).toString());
                System.out.println("\n[Servidor] => " + st);
            } while (true);
        } catch (IOException e) {}
    }
    
    private Object determinarClase(String format){
        int contador = 0;
        for(char c : format.toCharArray()){
            if(c == ',') contador++;
        }
        
        CommaObjectNotation con = new CommaObjectNotation();
        if(contador == 3){
            return con.transformaMaestro(format);
        }else{
            return con.transformaDirector(format);
        }
    }
    
        @Override
    public void frameMsg(byte[] mensaje, OutputStream out) throws IOException {
        if(mensaje.length > this.LONGITUD_MAX){
            throw new IOException("Mensaje demasiado largo.");
        }
        out.write(mensaje);
        out.flush();
    }

    @Override
    public byte[] nextMsg(byte[] mensaje) throws IOException {
        ByteArrayOutputStream messageBuffer = new ByteArrayOutputStream();
        int nextByte;
        int contador = 0;
        while (contador < this.LONGITUD_MAX) {
            nextByte = mensaje[contador];
            messageBuffer.write(nextByte);
//            if(nextByte != this.BYTE_RELLENO){
//                
//            }
            contador++;
        }
        
        return messageBuffer.toByteArray();
    }
    
    public byte[][] dividirMensaje(byte[] mensaje){
        int cantMensajes = (int)Math.ceil(mensaje.length/this.LONGITUD_MAX);
        byte[][] mensajes = new byte[cantMensajes][this.LONGITUD_MAX];
        int cont = 0;
        for(int f = 0; f < cantMensajes; f++){
            for(int c = 0; c < this.LONGITUD_MAX; c++){
                if(cont < mensaje.length){
                    mensajes[f][c] = mensaje[cont];
                }else{
                    mensajes[f][c] = this.BYTE_RELLENO;
                }
                cont++;
            }
        }
        return mensajes;
    }
    
    private static byte[] serializar(String cadena) {
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        DataOutputStream os = new DataOutputStream(bs);
        try {
            os.writeUTF(cadena);  // this es de tipo DatoUdp
            os.close();
        } catch (Exception e) {
        }

        return bs.toByteArray(); // devuelve byte[]
    }
    
        private String deserializar(byte[] mensaje) {
            ByteArrayInputStream bs = new ByteArrayInputStream(mensaje); // bytes es el byte[]
            DataInputStream is = new DataInputStream(bs);
            try {
                String cadena = (String) is.readUTF();
                is.close();
                return cadena;
            } catch (Exception e) {
            }
            return null;
        }

    
}
