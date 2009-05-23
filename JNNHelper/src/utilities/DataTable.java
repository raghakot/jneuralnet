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

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import jneuralnet.core.training.TrainingPattern;
import jneuralnet.core.training.TrainingSet;

public class DataTable
{
    private JTable tbl;
    private DefaultTableModel dm;  
    
    public DataTable(JTable tbl)
    {
        this.tbl = tbl;
        dm = (DefaultTableModel) tbl.getModel();
    }    
    
    public void addRow(TrainingPattern tp)
    {
        String str[] = {"",""};
        
        for(Double d : tp.getInputData())
        {         
            str[0] += d.toString() + ",";                                
        }
        //removing the last comma
        str[0] = str[0].substring(0, str[0].length() - 1);
             
        for(Double d : tp.getOutputData())
        {            
            str[1] += d.toString() + ",";                    
        }
        //removing the last comma
        str[1] = str[1].substring(0, str[1].length() - 1);
        
        dm.addRow(str);
        tbl.repaint();
    }   

    public void populateData(final TrainingSet ts)
    {
        new Thread()
        {
            @Override
            public void run()
            {
                for(TrainingPattern tp : ts.getTrainingPatterns())
                    addRow(tp);
            }
        }.start();
    }
}