/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Frames;

import DotObjectNotatiton.DotObjectNotatiton;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import javax.swing.JTextArea;
import static sun.security.krb5.Confounder.bytes;

/**
 *
 * @author Dhtey
 */
public class ClienteMaestro implements Framer {

    private Socket socket;
    private DataInputStream bufferDeEntrada = null;
    private DataOutputStream bufferDeSalida = null;
    private InputStream in = null;
    private OutputStream out = null;
    private JTextArea textArea;
    private static final byte DELIMITADOR = '\n';

    public ClienteMaestro(JTextArea textArea) {
        this.textArea = textArea;
    }

    
    private byte[] serializar(String cadena) {
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
        
//        byte[] bytes = serializar(s);
//        
//        System.out.println("Mensaje serializado: "+bytes);
        
        try {
            
//            frameMsg(bytes, bufferDeSalida);
//            bytes = nextMsg(bufferDeEntrada);
//            
//            String entregado = deserializar(bytes);
//            System.out.println("Mensaje deserializado: "+entregado);
//            System.out.println("Se envio");
            
            bufferDeSalida.writeUTF(s);
            bufferDeSalida.flush();
            
        } catch (Exception e) {
            e.printStackTrace();
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
        } catch (IOException e) {
        }
    }

    private Object determinarClase(String format) {
        int contador = 0;
        for (char c : format.toCharArray()) {
            if (c == '/') {
                contador++;
            }
        }

        DotObjectNotatiton don = new DotObjectNotatiton();
        if (contador == 2) {
            return don.transformaAlumno(format);
        } else {
            return don.transformaDirector(format);
        }
    }

    @Override
    public void frameMsg(byte[] mensaje, OutputStream out) throws IOException {
        for (byte b : mensaje) {
            if (b == DELIMITADOR) {
                throw new IOException("Mensaje contiene delimitador");
            }
        }
        out.write(mensaje);
        out.write(DELIMITADOR);
        out.flush();
    }

    @Override
    public byte[] nextMsg(InputStream in) throws IOException {
        ByteArrayOutputStream messageBuffer = new ByteArrayOutputStream();
        int nextByte;
        while ((nextByte = in.read()) != DELIMITADOR) {
            if (nextByte == -1) {
                if (messageBuffer.size() == 0) {
                    return null;
                } else {
                    throw new EOFException("mensaje sin delimitador");
                }

            }
            messageBuffer.write(nextByte);
        }

        return messageBuffer.toByteArray();
    }
}
