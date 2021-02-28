/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socket;

import CommaObjectNotation.CommaObjectNotation;
import DotObjectNotatiton.DotObjectNotatiton;
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
public class Servidor {

    private Socket socketMaestro;
    private Socket socketAlumno;
    private ServerSocket serverSocketMaestro;
    private ServerSocket serverSocketAlumno;
    private DataInputStream bufferDeEntradaAlumno = null;
    private DataOutputStream bufferDeSalidaAlumno = null;
    private DataInputStream bufferDeEntradaMaestro = null;
    private DataOutputStream bufferDeSalidaMaestro = null;

    public Servidor() {
        ejecutarConexion(4445, 4444);
    }

    public void levantarConexion(int puertoMaestro, int puertoAlumno) {
        try {
            serverSocketMaestro = new ServerSocket(puertoMaestro);
            serverSocketAlumno = new ServerSocket(puertoAlumno);
            
            System.out.println("Esperando conexión entrante en el puerto alumno: " + puertoAlumno);
            acceptAlumno();
            System.out.println("Esperando conexión entrante en el puerto maestro: " + puertoMaestro);
            acceptMaestro();

        } catch (Exception e) {
            System.out.println("Error en levantarConexion(): " + e.getMessage());
            System.exit(0);
        }
    }

    public void acceptMaestro() {
        Thread hilo = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        socketMaestro = serverSocketMaestro.accept();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        hilo.start();
    }

    public void acceptAlumno() {
        Thread hilo = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        socketAlumno = serverSocketAlumno.accept();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        hilo.start();
    }

    public void flujos() {
        try {
            bufferDeEntradaAlumno = new DataInputStream(socketAlumno.getInputStream());
            bufferDeSalidaAlumno = new DataOutputStream(socketAlumno.getOutputStream());
            bufferDeEntradaMaestro = new DataInputStream(socketMaestro.getInputStream());
            bufferDeSalidaMaestro = new DataOutputStream(socketMaestro.getOutputStream());
            bufferDeSalidaAlumno.flush();
            bufferDeSalidaMaestro.flush();
        } catch (IOException e) {
            System.out.println("Error en la apertura de flujos");
        }
    }

    public void recibirDatos(String tipo) {
        String st = "";

        try {

            if (tipo.equals("maestro")) {

                do {
                    st = (String) bufferDeEntradaMaestro.readUTF();
                    enviar(st, "alumno");
                    System.out.println("\n[Cliente] => " + st);
                    System.out.print("\n[Usted] => ");
                } while (true);

            } else if (tipo.equals("alumno")) {

                do {
                    st = (String) bufferDeEntradaAlumno.readUTF();
                    enviar(st, "maestro");
                    System.out.println("\n[Cliente] => " + st);
                    System.out.print("\n[Usted] => ");
                } while (true);

            }

        } catch (IOException e) {
            cerrarConexion();
        }
    }

    public void enviar(String s, String tipo) {
        try {

            if (tipo.equals("maestro")) {
                Context context = new Context(s, FormatosEnum.DON, FormatosEnum.CON);
                String transformado = InterpreterClient.interpretar(context);
                System.out.println("formato transformado: " + transformado);

                bufferDeSalidaMaestro.writeUTF(transformado);
                bufferDeSalidaMaestro.flush();

            } else if (tipo.equals("alumno")) {
                Context context = new Context(s, FormatosEnum.CON, FormatosEnum.DON);
                String transformado = InterpreterClient.interpretar(context);
                System.out.println("formato transformado: " + transformado);

                bufferDeSalidaAlumno.writeUTF(transformado);
                bufferDeSalidaAlumno.flush();
            }

        } catch (IOException e) {
            System.out.println("Error en enviar(): " + e.getMessage());
        }
    }

    public void cerrarConexion() {
        try {
            bufferDeEntradaMaestro.close();
            bufferDeSalidaMaestro.close();
            bufferDeEntradaAlumno.close();
            bufferDeSalidaAlumno.close();
            socketMaestro.close();
            socketAlumno.close();
        } catch (IOException e) {
            System.out.print("Excepción en cerrarConexion(): " + e.getMessage());
        } finally {
            System.out.print("Conversación finalizada....");
            System.exit(0);

        }
    }

    public void ejecutarConexion(int puertoMaestro, int puertoAlumno) {
        Thread hilo = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        levantarConexion(puertoMaestro, puertoAlumno);
                        flujos();
                        recibirDatos("maestro");
                        recibirDatos("alumno");
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });
        hilo.start();
    }

}
