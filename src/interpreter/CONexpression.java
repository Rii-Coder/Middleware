/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interpreter;


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
    
}
