/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interpreter;

import CommaObjectNotation.CommaObjectNotation;
import DotObjectNotatiton.DotObjectNotatiton;
import alumnomaestro.Alumno;
import alumnomaestro.Director;
import alumnomaestro.Maestro;
import com.google.gson.Gson;

/**
 *
 * @author R2
 */
public class JSONexpression extends FormatoExpression {

    @Override
    public String DON(String formato) {
        Object maestro = this.determinarClase(formato, '/');
        DotObjectNotatiton dot = new DotObjectNotatiton();
        return dot.transformar(maestro);
    }

    @Override
    public String CON(String formato) {
        Object alumno = this.determinarClase(formato, ',');
        CommaObjectNotation comma = new CommaObjectNotation();
        return comma.transformar(alumno);
    }

    @Override
    public String JSON(String formato) {
        return formato;
    }

    private Object determinarClase(String format, char caracter) {
        
        int contador = 0;
        for (char c : format.toCharArray()) {
            if (c == caracter) {
                contador++;
            }
        }

        Gson gson = new Gson();
        if (contador == 2) {
            return gson.fromJson(format, Alumno.class);
        } else if (contador == 3) {
            return gson.fromJson(format, Maestro.class);
        } else {
            return gson.fromJson(format, Director.class);
        }
    }
}
