/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author R2
 */
public class Run {
    private ServerSocket serverSocket;
    private ArrayList<Conexion> conexiones;
    
    public Run(int port){
        this.conexiones = new ArrayList();
        try {
            System.out.println("Iniciando servidor...");
            this.serverSocket = new ServerSocket(port);
        } catch (IOException ex) {
            Logger.getLogger(Run.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void start(){
        try {
            while(true){
                System.out.println("Esperando por conexion...");
                Conexion conexion = new Conexion(this.serverSocket.accept(), this.conexiones);
                conexion.establecerConexion();
                System.out.println("Cliente " + conexion.getNombre() + " se ha conectado.");
                conexion.ejecutarConexion();
                this.conexiones.add(conexion);
            }
        } catch (IOException ex) {
            Logger.getLogger(Run.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        Run servidor = new Run(4444);
        servidor.start();

    }

}
