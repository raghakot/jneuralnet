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

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FileManager 
{
    public static String getLoadFile(String desc,String ext)
    {
        JFileChooser fc = new JFileChooser();
        fc.setName("Load File");
        
        fc.setFileFilter(new FileNameExtensionFilter(desc,ext));
        
        String fname;
        int ret = fc.showOpenDialog(null);
        
        if(ret == JFileChooser.APPROVE_OPTION)
            fname = fc.getSelectedFile().getPath();
        else
            fname = null;
        
        return(fname);
    }
    
    public static String getSaveFile(String desc,String ext)
    {
        JFileChooser fc = new JFileChooser();
        fc.setName("Save File");
        
        fc.setFileFilter(new FileNameExtensionFilter(desc,ext));
        
        String fname;
        int ret = fc.showSaveDialog(null);
        
        if(ret == JFileChooser.APPROVE_OPTION)
            fname = fc.getSelectedFile().getPath();      
        else
            fname = null;
        
        return(fname);
    }
}
