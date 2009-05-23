package test;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SineSetGenerator 
{
    public static void main(String args[])
    {
        FileOutputStream fout = null;
        try 
        {
            fout = new FileOutputStream("c:\\sine.txt");
            PrintWriter pw = new PrintWriter(fout);
            
            Double degree = -90.0;
            
            for(int i = 0;i < 360;i++)
            {
                pw.println(degree+";"+Math.sin(Math.toRadians(degree)));
                degree+=5;
            }            
            
            pw.close();
            fout.close();            
        } 
        catch (IOException ex) {
            Logger.getLogger(SineSetGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
