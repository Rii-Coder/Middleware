package Main;

import CommaObjectNotation.CommaObjectNotation;
import DotObjectNotatiton.DotObjectNotatiton;
import alumnomaestro.Alumno;
import alumnomaestro.Maestro;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author R2
 */
public class NewMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Alumno alumno = new Alumno("Pepe", "Junaito", 123);
        Maestro maestro = new Maestro("Hola", "El", "Reprueba", 123456);
        CommaObjectNotation con = new CommaObjectNotation();
        DotObjectNotatiton don = new DotObjectNotatiton();
        
        System.out.println(alumno.toString());
        System.out.println(con.transformar(alumno));
        
        System.out.println("");
        System.out.println(maestro.toString());
        System.out.println(don.transformar(maestro.toString()));
        
        System.out.println("");
        String abc =con.transformar(alumno);
        System.out.println(con.transformaAlumno(abc).getNombre());
        
        System.out.println("");
        String maestro1 = con.transformar(maestro);
        System.out.println(con.transformaMaestro(maestro1).getNombre());
        
        System.out.println("");
        String alumno1 = don.transformar(alumno);
        System.out.println(alumno1);
        System.out.println(don.transformaAlumno(alumno1).getNombre());
        
        System.out.println("");
        String def =don.transformar(maestro);
        System.out.println(def);
        System.out.println(don.transformaMaestro(def).getNombre());
    }
    
}
