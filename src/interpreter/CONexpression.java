/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interpreter;

import CommaObjectNotation.CommaObjectNotation;
import alumnomaestro.Alumno;
import com.google.gson.Gson;


/**
 *
 * @author Lenovo
 */
public class CONexpression extends FormatoExpression{

    @Override
    public String DON(String formato) {
        return formato.replace(",", "/");
        
    }

    @Override
    public String CON(String formato) {
        return formato;
    }

    @Override
    public String JSON(String formato) {
        CommaObjectNotation comma = new CommaObjectNotation();
        Alumno alumno = comma.transformaAlumno(formato);
        
        Gson gson = new Gson();
        return gson.toJson(alumno);
    }
    
}
