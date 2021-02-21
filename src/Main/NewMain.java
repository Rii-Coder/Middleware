package Main;

import interpreter.Context;
import interpreter.FormatosEnum;
import interpreter.InterpreterClient;

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
        InterpreterClient client =  new InterpreterClient();
        Context contexto = new Context("perro/hola/2", FormatosEnum.DON, FormatosEnum.CON);
        System.out.println(client.interpretar(contexto));
    }
}
