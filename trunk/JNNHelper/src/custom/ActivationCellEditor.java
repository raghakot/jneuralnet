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

package custom;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import jneuralnet.core.activation.AbstractActivation;

/**
 *
 * @author Ragha
 */
public class ActivationCellEditor extends DefaultCellEditor
{
    public ActivationCellEditor(final JComboBox comboBox)
    {
        super(comboBox);
        //remove listener added by the super class...
        comboBox.removeActionListener(delegate);

        delegate = new EditorDelegate()
        {
            @Override
            public void setValue(Object value) {
                comboBox.setSelectedItem(value);
            }

            @Override
            public Object getCellEditorValue() {
                AbstractActivation act = (AbstractActivation) comboBox.getSelectedItem();
                return act.getName();
            }

            @Override
            public boolean shouldSelectCell(EventObject anEvent) {
                if (anEvent instanceof MouseEvent) {
                    MouseEvent e = (MouseEvent)anEvent;
                    return e.getID() != MouseEvent.MOUSE_DRAGGED;
                }
                return true;
            }
            
            @Override
            public boolean stopCellEditing() {
                if (comboBox.isEditable()) {
                    // Commit edited value.
                    comboBox.actionPerformed(new ActionEvent(
                                     ActivationCellEditor.this, 0, ""));
                }
                return super.stopCellEditing();
            }
        };
        comboBox.addActionListener(delegate);
    }
}
