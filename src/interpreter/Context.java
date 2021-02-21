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
public class Context {
    
    private String conversionQues = "";
    private String conversionResponse = "";
    private FormatosEnum fromConversion;
    private FormatosEnum toConversion;
    
    public Context(String conversionQues, FormatosEnum fromConversion, FormatosEnum toConversion){
        this.conversionQues = conversionQues;
        this.fromConversion = fromConversion;
        this.toConversion = toConversion;
    }

    public String getConversionQues() {
        return conversionQues;
    }

    public String getConversionResponse() {
        return conversionResponse;
    }

    public FormatosEnum getFromConversion() {
        return fromConversion;
    }

    public FormatosEnum getToConversion() {
        return toConversion;
    }
    
    
}
