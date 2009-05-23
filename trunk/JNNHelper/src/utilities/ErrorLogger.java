/*
 * Copyright (c) 2008-2009 Kotikalapudi Raghavendra. All Rights Reserved.
 *
 * Licensed under the Creative Commons License Attribution-NonCommercial-ShareAlike 3.0,
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://creativecommons.org/
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package utilities;

import java.util.Calendar;
import java.io.*;

public class ErrorLogger 
{
    private static String excludeList[] = 
        {"java.","javax.","com.","org.","sun.","sunw."};
    
    public static void logError(final Throwable e, final String additional)
    {
        new Thread()
        {
            @Override
            public void run()
            {
                saveLog(e, additional);
            }                    
        }.start();
    }
    
    private static void saveLog(Throwable e, String additional)
    {
        Calendar cal = Calendar.getInstance();
        String fname ="errLog - "+ cal.get(Calendar.DAY_OF_MONTH)
        +"."+(cal.get(Calendar.MONTH)+1)+"--"+cal.get(Calendar.HOUR)+"."
        +cal.get(Calendar.MINUTE)+"."
        +cal.get(Calendar.SECOND)+".rtf";
    
        FileOutputStream fout = null;
        PrintWriter pw = null;
    
        try
        {      
            String currDir = System.getProperty("user.dir");
            String path = currDir + File.separatorChar + "Error Logs";
            File f = new File(path);
            f.mkdir();
        
            fout = new FileOutputStream(path + File.separatorChar + fname);
            pw = new PrintWriter(fout);
        
            System.err.println("----Error Logger----\n");            
            String details = processException(e);
            
            pw.print(details);                       
            System.err.println(details);            
                    
            pw.print("\nADDITIONAL INFO : \n");
            pw.print(additional);           
            System.err.println("ADDITIONAL INFO : \n"+additional);
            
            pw.close();                
            fout.close();        
        }
        catch(Exception ex)
        {
            System.err.println("error saving log to hdd");
        }
    }
    
    private static String processException(Throwable e)
    {
        String details = "";
            
        details += "EXCEPTION : \n"+e.getClass().getName()+"\n\n";
        details += "CAUSE : \n"+e.getLocalizedMessage()+"\n\n";
        details += "FILTERED STACK TRACE : \n";
    
        String completeTrace = "";
        StackTraceElement elt[] = e.getStackTrace();
        for(StackTraceElement st : elt)
        {
            completeTrace += st.toString()+"\n";            
            if(!isInExcludeList(st.toString()))
                details += st.toString()+"\n";        
        }    
           
        details += "\nCOMPLETE STACK TRACE : \n";
        details += completeTrace;
        return details;
    }        

    private static boolean isInExcludeList(String exep)
    {
        for(String s : excludeList)
            if(exep.contains(s))
                return true;
    
        return false;
    }
}
