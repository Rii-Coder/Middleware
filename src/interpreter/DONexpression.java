/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interpreter;

import DotObjectNotatiton.DotObjectNotatiton;
import alumnomaestro.Maestro;
import com.google.gson.Gson;

/**
 *
 * @author Lenovo
 */
public class DONexpression extends FormatoExpression{

    
    @Override
    public String DON(String formato) {
        return formato;
    }

    @Override
    public String CON(String formato) {
        return formato.replace("/", ",");
    }

    @Override
    public String JSON(String formato) {
        DotObjectNotatiton dot = new DotObjectNotatiton();
        Maestro maestro= dot.transformaMaestro(formato);
        
        Gson gson = new Gson();
        return gson.toJson(maestro);
    }
    
}
