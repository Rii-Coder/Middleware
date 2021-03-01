/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interpreter;

import CommaObjectNotation.CommaObjectNotation;
import DotObjectNotatiton.DotObjectNotatiton;
import alumnomaestro.Alumno;
import alumnomaestro.Maestro;
import com.google.gson.Gson;

/**
 *
 * @author R2
 */
public class JSONexpression extends FormatoExpression{

    @Override
    public String DON(String formato) {
        Gson gson = new Gson();
        Maestro maestro = gson.fromJson(formato, Maestro.class);
        DotObjectNotatiton dot = new DotObjectNotatiton();
        return dot.transformar(maestro);
    }

    @Override
    public String CON(String formato) {
        Gson gson = new Gson();
        Alumno alumno = gson.fromJson(formato, Alumno.class);
        CommaObjectNotation comma = new CommaObjectNotation();
        return comma.transformar(alumno);
    }

    @Override
    public String JSON(String formato) {
        return formato;
    }
    
}
