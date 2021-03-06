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

/*
 * DataClustering.java
 *
 * Created on Apr 11, 2009, 11:59:45 AM
 */

package org.jnnhelper.ui.databuilder;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import jneuralnet.core.NeuralNetwork;
import jneuralnet.core.preprocessor.AbstractInputPreprocessor;
import jneuralnet.core.preprocessor.AbstractOutputPreprocessor;
import jneuralnet.core.preprocessor.CompoundInputPreprocessor;
import jneuralnet.core.preprocessor.CompoundOutputPreprocessor;
import org.jnnhelper.ui.Configuration;
import org.jnnhelper.ui.MyPainters;
import org.openide.awt.StatusDisplayer;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;

/**
 *
 * @author Ragha
 */
public class DataPreprocessing extends javax.swing.JPanel implements LookupListener
{
    public static final String ID = "Setup Preprocessors";    
    private DefaultListModel availableInputPreprocessors, selectedInputPreprocessors;
    private DefaultListModel availableOutputPreprocessors, selectedOutputPreprocessors;

    //Current selection of cip and cop...
    private CompoundInputPreprocessor cip = new CompoundInputPreprocessor();
    private CompoundOutputPreprocessor cop = new CompoundOutputPreprocessor();

    //To manage dynamic chages in ther lookup...
    private Lookup.Result<AbstractInputPreprocessor> aipResult;
    private Lookup.Result<AbstractOutputPreprocessor> aopResult;
        
    public DataPreprocessing()
    {
        initComponents();

        //init list components...
        availableInputPreprocessors = new DefaultListModel();
        selectedInputPreprocessors = new DefaultListModel();
        availableOutputPreprocessors = new DefaultListModel();
        selectedOutputPreprocessors= new DefaultListModel();
        lstAvailableInputPreprocessors.setModel(availableInputPreprocessors);
        lstSelectedInputPreprocessors.setModel(selectedInputPreprocessors);
        lstAvailableOutputPreprocessors.setModel(availableOutputPreprocessors);
        lstSelectedOutputPreprocessors.setModel(selectedOutputPreprocessors);

        //Manage dynamic changes in the lookup...
        aipResult = Lookup.getDefault().lookupResult(AbstractInputPreprocessor.class);
        aopResult = Lookup.getDefault().lookupResult(AbstractOutputPreprocessor.class);
        aipResult.addLookupListener(this);
        aopResult.addLookupListener(this);
        resultChanged(new LookupEvent(aipResult));
        resultChanged(new LookupEvent(aopResult));

        Configuration.getInstance().addPropertyChangeListener(new PropertyChangeListener() {
            
            public void propertyChange(PropertyChangeEvent evt) {
                if(evt.getPropertyName().equals("neuralNetwork"))
                {                    
                    NeuralNetwork nn = (NeuralNetwork) evt.getNewValue();
                    if(nn != null) {
                        nn.setInputPreprocessor(cip);
                        nn.setOutputPreprocessor(cop);
                    }
                }
            }
        });
        
        jXHeader1.setBackgroundPainter(MyPainters.getHeaderPainter());
        jXPanel1.setBackgroundPainter(MyPainters.getFooterPanelPainter());       
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jXBusyLabel1 = new org.jdesktop.swingx.JXBusyLabel();
        jXHeader1 = new org.jdesktop.swingx.JXHeader();
        jXPanel1 = new org.jdesktop.swingx.JXPanel();
        btnApply = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        lstAvailableInputPreprocessors = new javax.swing.JList();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        lstSelectedInputPreprocessors = new javax.swing.JList();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jXTitledSeparator1 = new org.jdesktop.swingx.JXTitledSeparator();
        jXTitledSeparator2 = new org.jdesktop.swingx.JXTitledSeparator();
        jScrollPane3 = new javax.swing.JScrollPane();
        lstAvailableOutputPreprocessors = new javax.swing.JList();
        jLabel3 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        lstSelectedOutputPreprocessors = new javax.swing.JList();
        jLabel4 = new javax.swing.JLabel();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();

        jXBusyLabel1.setText(org.openide.util.NbBundle.getMessage(DataPreprocessing.class, "DataPreprocessing.jXBusyLabel1.text")); // NOI18N

        setLayout(new java.awt.BorderLayout());

        jXHeader1.setTitle("<html>\n<h3>Data Preprocessing</h3>\n<p>\nLets you add compound input and output preprocessors\n</html>"); // NOI18N
        jXHeader1.setTitleFont(new java.awt.Font("Tahoma", 0, 11));
        jXHeader1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jXHeader1.setPreferredSize(new java.awt.Dimension(307, 81));
        add(jXHeader1, java.awt.BorderLayout.PAGE_START);

        jXPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btnApply.setText("Apply"); // NOI18N
        btnApply.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnApplyActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 102, 0));
        jLabel5.setText(org.openide.util.NbBundle.getMessage(DataPreprocessing.class, "DataPreprocessing.jLabel5.text")); // NOI18N

        javax.swing.GroupLayout jXPanel1Layout = new javax.swing.GroupLayout(jXPanel1);
        jXPanel1.setLayout(jXPanel1Layout);
        jXPanel1Layout.setHorizontalGroup(
            jXPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jXPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 642, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(btnApply, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jXPanel1Layout.setVerticalGroup(
            jXPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jXPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jXPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnApply)
                    .addComponent(jLabel5))
                .addContainerGap())
        );

        add(jXPanel1, java.awt.BorderLayout.PAGE_END);

        lstAvailableInputPreprocessors.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(lstAvailableInputPreprocessors);

        jLabel1.setText(org.openide.util.NbBundle.getMessage(DataPreprocessing.class, "DataPreprocessing.jLabel1.text")); // NOI18N

        jButton1.setText(org.openide.util.NbBundle.getMessage(DataPreprocessing.class, "DataPreprocessing.jButton1.text")); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        lstSelectedInputPreprocessors.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane2.setViewportView(lstSelectedInputPreprocessors);

        jButton2.setText(org.openide.util.NbBundle.getMessage(DataPreprocessing.class, "DataPreprocessing.jButton2.text")); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText(org.openide.util.NbBundle.getMessage(DataPreprocessing.class, "DataPreprocessing.jButton3.text")); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText(org.openide.util.NbBundle.getMessage(DataPreprocessing.class, "DataPreprocessing.jButton4.text")); // NOI18N
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel2.setText(org.openide.util.NbBundle.getMessage(DataPreprocessing.class, "DataPreprocessing.jLabel2.text")); // NOI18N

        jXTitledSeparator1.setTitle(org.openide.util.NbBundle.getMessage(DataPreprocessing.class, "DataPreprocessing.jXTitledSeparator1.title")); // NOI18N

        jXTitledSeparator2.setTitle(org.openide.util.NbBundle.getMessage(DataPreprocessing.class, "DataPreprocessing.jXTitledSeparator2.title")); // NOI18N

        lstAvailableOutputPreprocessors.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane3.setViewportView(lstAvailableOutputPreprocessors);

        jLabel3.setText(org.openide.util.NbBundle.getMessage(DataPreprocessing.class, "DataPreprocessing.jLabel3.text")); // NOI18N

        jButton5.setText(org.openide.util.NbBundle.getMessage(DataPreprocessing.class, "DataPreprocessing.jButton5.text")); // NOI18N
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        lstSelectedOutputPreprocessors.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane4.setViewportView(lstSelectedOutputPreprocessors);

        jLabel4.setText(org.openide.util.NbBundle.getMessage(DataPreprocessing.class, "DataPreprocessing.jLabel4.text")); // NOI18N

        jButton6.setText(org.openide.util.NbBundle.getMessage(DataPreprocessing.class, "DataPreprocessing.jButton6.text")); // NOI18N
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setText(org.openide.util.NbBundle.getMessage(DataPreprocessing.class, "DataPreprocessing.jButton7.text")); // NOI18N
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setText(org.openide.util.NbBundle.getMessage(DataPreprocessing.class, "DataPreprocessing.jButton8.text")); // NOI18N
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jXTitledSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 779, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButton2)))
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jXTitledSeparator2, javax.swing.GroupLayout.DEFAULT_SIZE, 779, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButton7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButton8)))
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jScrollPane3, jScrollPane4});

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jScrollPane1, jScrollPane2});

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jButton5, jButton6, jButton7, jButton8});

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jButton1, jButton2, jButton3, jButton4});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jXTitledSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4)))
                .addGap(18, 18, 18)
                .addComponent(jXTitledSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton5)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton6)))
                .addContainerGap(35, Short.MAX_VALUE))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jScrollPane3, jScrollPane4});

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jScrollPane1, jScrollPane2});

        add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents
    
    private void btnApplyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnApplyActionPerformed
        AbstractInputPreprocessor aip[] = new AbstractInputPreprocessor
                [selectedInputPreprocessors.size()];
        AbstractOutputPreprocessor aop[] = new AbstractOutputPreprocessor
                [selectedOutputPreprocessors.size()];
        for(int i=0; i<aip.length; i++)
            aip[i] = (AbstractInputPreprocessor) selectedInputPreprocessors.get(i);
        for(int i=0; i<aop.length; i++)
            aop[i] = (AbstractOutputPreprocessor) selectedOutputPreprocessors.get(i);

        cip.setPreprocessors(aip);
        cop.setPreprocessors(aop);
        Configuration.getInstance().getNeuralNetwork()
                    .setInputPreprocessor(cip);
        Configuration.getInstance().getNeuralNetwork()
                    .setOutputPreprocessor(cop);

        String msg = "";
        if(cip.getPreprocessors().length > 0)
            msg += "Applied " + cip.toString();
        else
            msg += "No input preprocessor";

        if(cop.getPreprocessors().length > 0)
            msg += " AND " + cop.toString();
        else
            msg += " AND no output preprocessor";

        StatusDisplayer.getDefault().setStatusText(msg);
        DataBuilderStatus.getInstance().refreshStatus();        
}//GEN-LAST:event_btnApplyActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        int index = lstAvailableInputPreprocessors.getSelectedIndex();
        if(index < 0)
            return;
        //@TODO: Might have to clone the object over here to create multiple copies...
        selectedInputPreprocessors.addElement(availableInputPreprocessors.getElementAt(index));
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        int index = lstAvailableOutputPreprocessors.getSelectedIndex();
        if(index < 0)
            return;
        //@TODO: Might have to clone the object over here to create multiple copies...
        selectedOutputPreprocessors.addElement(availableOutputPreprocessors.getElementAt(index));
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        int index = lstSelectedInputPreprocessors.getSelectedIndex();
        if(index < 0)
            return;
        selectedInputPreprocessors.remove(index);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        int index = lstSelectedOutputPreprocessors.getSelectedIndex();
        if(index < 0)
            return;
        selectedOutputPreprocessors.remove(index);
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        int index = lstSelectedInputPreprocessors.getSelectedIndex();
        if(index <= 0)
            return;
        Object item = selectedInputPreprocessors.remove(index);
        selectedInputPreprocessors.add(index - 1, item);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        int index = lstSelectedOutputPreprocessors.getSelectedIndex();
        if(index <= 0)
            return;
        Object item = selectedOutputPreprocessors.remove(index);
        selectedOutputPreprocessors.add(index - 1, item);
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        int index = lstSelectedInputPreprocessors.getSelectedIndex();
        if(index < 0 || index == selectedInputPreprocessors.getSize() - 1)
            return;
        Object item = selectedInputPreprocessors.remove(index);
        selectedInputPreprocessors.add(index + 1, item);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        int index = lstSelectedOutputPreprocessors.getSelectedIndex();
        if(index < 0 || index == selectedOutputPreprocessors.getSize() - 1)
            return;
        Object item = selectedOutputPreprocessors.remove(index);
        selectedOutputPreprocessors.add(index + 1, item);
    }//GEN-LAST:event_jButton6ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnApply;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private org.jdesktop.swingx.JXBusyLabel jXBusyLabel1;
    private org.jdesktop.swingx.JXHeader jXHeader1;
    private org.jdesktop.swingx.JXPanel jXPanel1;
    private org.jdesktop.swingx.JXTitledSeparator jXTitledSeparator1;
    private org.jdesktop.swingx.JXTitledSeparator jXTitledSeparator2;
    private javax.swing.JList lstAvailableInputPreprocessors;
    private javax.swing.JList lstAvailableOutputPreprocessors;
    private javax.swing.JList lstSelectedInputPreprocessors;
    private javax.swing.JList lstSelectedOutputPreprocessors;
    // End of variables declaration//GEN-END:variables

    public void resultChanged(LookupEvent ev)
    {
        Lookup.Result r = (Lookup.Result) ev.getSource();
        Collection c = r.allInstances();
        boolean isLstAipCleared = false, isLstAopCleared = false;

        if (!c.isEmpty())
        {
            Iterator it = c.iterator();
            while (it.hasNext())
            {
                Object obj = it.next();
                if (obj instanceof AbstractInputPreprocessor)
                {
                    if (!isLstAipCleared)
                    {
                        availableInputPreprocessors.removeAllElements();
                        selectedInputPreprocessors.removeAllElements();
                        isLstAipCleared = true;
                    }
                    availableInputPreprocessors.addElement((AbstractInputPreprocessor) obj);
                }
                if (obj instanceof AbstractOutputPreprocessor)
                {
                    if (!isLstAopCleared)
                    {
                        availableOutputPreprocessors.removeAllElements();
                        selectedOutputPreprocessors.removeAllElements();
                        isLstAopCleared = true;
                    }
                    availableOutputPreprocessors.addElement((AbstractOutputPreprocessor) obj);
                }
            }
        }
    }
}
