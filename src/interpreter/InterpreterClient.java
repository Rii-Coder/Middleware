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
public class InterpreterClient {
    
    public InterpreterClient(){
    }
    
    public static String interpretar(Context contexto){
        FormatoExpression expresion = determinarExpression(contexto.getFromConversion());
                
        switch (contexto.getToConversion()) {
            case CON:
                return expresion.CON(contexto.getConversionQues());
            case DON:
                return expresion.DON(contexto.getConversionQues());
            default:
                return null;
        }
    }
    
    private static FormatoExpression determinarExpression(FormatosEnum fromConversion){
        switch (fromConversion) {
            case CON:
                return new CONexpression();
            case DON:
                return new DONexpression();
            default:
                return null;
        }
    }
}
