/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Frames;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author R2
 */
public interface Framer {
    
    public void frameMsg(byte[] mensaje, OutputStream out)throws IOException;
    
    public byte[] nextMsg(InputStream in)throws IOException;
    
}
