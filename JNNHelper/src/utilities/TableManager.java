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

import java.util.ArrayList;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class TableManager 
{
    public static void populateTable(JTable tbl,ArrayList<Object[]> list)
    {
        DefaultTableModel dm = (DefaultTableModel) tbl.getModel();
        
        if(list == null)
        {            
            return;
        }
        
        dm.setRowCount(0);
        
        for(Object obj[] : list)
        {
            dm.addRow(obj);
        }
            
        tbl.repaint();                    
    } 
    
    public static void addTableData(JTable tbl,Object[] data)
    {
        DefaultTableModel dm = (DefaultTableModel) tbl.getModel();        
        dm.addRow(data);
        
        tbl.repaint();
    }
    
    public static void removeSelectedItems(JTable tbl)
    {
        DefaultTableModel dm = (DefaultTableModel) tbl.getModel();
        int rows[] = tbl.getSelectedRows();
                        
        int numRemoved = 0;
        for(int row : rows)
        {
            dm.removeRow(row - numRemoved);
            numRemoved++;
        }
          
        tbl.repaint();        
    }
    
    public static void removeRow(JTable tbl, int row)
    {
        try
        {
            DefaultTableModel dm = (DefaultTableModel) tbl.getModel();
            dm.removeRow(row);
        }
        catch(ArrayIndexOutOfBoundsException e)
        {
            //ignore this exception...
            return;            
        }
    }

    public static void clearTable(JTable tbl)
    {
        ((DefaultTableModel)tbl.getModel()).setRowCount(0);
    }
}
