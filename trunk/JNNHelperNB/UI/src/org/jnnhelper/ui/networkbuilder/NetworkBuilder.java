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
 * NetworkBuilder.java
 *
 * Created on Feb 7, 2009, 4:38:51 PM
 */
package org.jnnhelper.ui.networkbuilder;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.Iterator;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import jneuralnet.core.NeuralNetwork;
import jneuralnet.core.NeuronLayer;
import jneuralnet.core.activation.AbstractActivation;
import jneuralnet.core.activation.LogisticSigmoid;
import jneuralnet.core.activation.Sigmoid;
import jneuralnet.util.VisualUtil;
import org.jnnhelper.ui.Configuration;
import org.jnnhelper.ui.MyPainters;
import org.openide.awt.StatusDisplayer;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import util.TableManager;

/**
 *
 * @author Ragha
 */
public class NetworkBuilder extends javax.swing.JPanel implements LookupListener
{
    public static final String ID = "Network Builder";
    private int hiddenLayerIndex = 0;
    private JComboBox cbxActivationFunctions;
    private boolean isUpdateRequired;
    private BufferedImage cached;
    private LogisticSigmoid logisticSigmoid = new LogisticSigmoid();
    private Configuration cfg;
    private Lookup.Result result = null;

    public NetworkBuilder()
    {
        initComponents();
        cfg = Configuration.getInstance();

        jXHeader1.setBackgroundPainter(MyPainters.getHeaderPainter());
        cbxActivationFunctions = new JComboBox();
        cbxActivationFunctions.setRenderer(new ActivationComboRenderer());
        
        //detect and manage available activation functions...
        result = Lookup.getDefault().lookupResult(AbstractActivation.class);
        result.addLookupListener(this);
        resultChanged(new LookupEvent(result));

        tblNetworkDesign.getColumnModel().getColumn(2).setCellEditor(new ActivationCellEditor(cbxActivationFunctions));
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        btnAddHiddenLayer = new javax.swing.JButton();
        btnCreateNet = new javax.swing.JButton();
        btnRemoveHiddenLayer = new javax.swing.JButton();
        txtNumInputs = new javax.swing.JFormattedTextField();
        txtNumOutputs = new javax.swing.JFormattedTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jXHeader1 = new org.jdesktop.swingx.JXHeader();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblNetworkDesign = new javax.swing.JTable();
        pnlVisualView = new javax.swing.JPanel()
        {
            public void paintComponent(Graphics g)
            {
                super.paintComponent(g);
                if(isUpdateRequired)
                {
                    //trigger gc on previous cached image...
                    cached = null;
                    //update cache...
                    cached = VisualUtil.getNetworkImage(
                        Configuration.getInstance().getNeuralNetwork(),
                        pnlVisualView.getWidth(),
                        pnlVisualView.getHeight());
                    isUpdateRequired = false;
                }

                if(cached != null)
                g.drawImage(cached, 0, 0, null);
            }
        }
        ;

        setLayout(new java.awt.BorderLayout(0, 10));

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel2.setText("Number of outputs");

        jLabel1.setText("Number of inputs");

        btnAddHiddenLayer.setText("Add Hidden Layer");
        btnAddHiddenLayer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddHiddenLayerActionPerformed(evt);
            }
        });

        btnCreateNet.setText("Refresh Network");
        btnCreateNet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreateNetActionPerformed(evt);
            }
        });

        btnRemoveHiddenLayer.setText("Remove Hidden Layer");
        btnRemoveHiddenLayer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveHiddenLayerActionPerformed(evt);
            }
        });

        txtNumInputs.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        txtNumInputs.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtNumInputs.setText("2");

        txtNumOutputs.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        txtNumOutputs.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtNumOutputs.setText("2");
        txtNumOutputs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNumOutputsActionPerformed(evt);
            }
        });
        txtNumOutputs.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtNumOutputsFocusLost(evt);
            }
        });
        txtNumOutputs.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNumOutputsKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(85, 85, 85)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(txtNumInputs, javax.swing.GroupLayout.DEFAULT_SIZE, 108, Short.MAX_VALUE)
                .addGap(118, 118, 118)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(txtNumOutputs, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(83, 83, 83))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 728, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(134, 134, 134)
                .addComponent(btnCreateNet)
                .addGap(18, 18, 18)
                .addComponent(btnAddHiddenLayer)
                .addGap(18, 18, 18)
                .addComponent(btnRemoveHiddenLayer)
                .addContainerGap(167, Short.MAX_VALUE))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnAddHiddenLayer, btnCreateNet, btnRemoveHiddenLayer});

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {txtNumInputs, txtNumOutputs});

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel1, jLabel2});

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 14, Short.MAX_VALUE)
                    .addComponent(txtNumInputs)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 17, Short.MAX_VALUE)
                    .addComponent(txtNumOutputs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(36, 36, 36)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 7, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(btnRemoveHiddenLayer)
                    .addComponent(btnAddHiddenLayer)
                    .addComponent(btnCreateNet))
                .addGap(18, 18, 18))
        );

        add(jPanel2, java.awt.BorderLayout.PAGE_END);

        jXHeader1.setTitle("<html>\n<h3>Neural Network Builder</h3>\n<p>\nLets you construct a custom neural network\n</html>");
        jXHeader1.setTitleFont(new java.awt.Font("Tahoma", 0, 11));
        jXHeader1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jXHeader1.setPreferredSize(new java.awt.Dimension(51, 81));
        add(jXHeader1, java.awt.BorderLayout.PAGE_START);

        tblNetworkDesign.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"Output", new Integer(2), null}
            },
            new String [] {
                "Layer", "Number of Neurons", "Activation Function"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblNetworkDesign.setColumnSelectionAllowed(true);
        tblNetworkDesign.setFillsViewportHeight(true);
        jScrollPane3.setViewportView(tblNetworkDesign);
        tblNetworkDesign.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        pnlVisualView.setBorder(javax.swing.BorderFactory.createTitledBorder("Visual Network View"));

        javax.swing.GroupLayout pnlVisualViewLayout = new javax.swing.GroupLayout(pnlVisualView);
        pnlVisualView.setLayout(pnlVisualViewLayout);
        pnlVisualViewLayout.setHorizontalGroup(
            pnlVisualViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 736, Short.MAX_VALUE)
        );
        pnlVisualViewLayout.setVerticalGroup(
            pnlVisualViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 170, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 752, Short.MAX_VALUE)
            .addComponent(pnlVisualView, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlVisualView, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void btnAddHiddenLayerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddHiddenLayerActionPerformed
        Object list[] = new Object[3];
        list[0] = "Hidden Layer";
        list[1] = 2;
        list[2] = logisticSigmoid.getName();

        TableManager.addTableData(tblNetworkDesign, list);
        hiddenLayerIndex++;
}//GEN-LAST:event_btnAddHiddenLayerActionPerformed

    private void btnRemoveHiddenLayerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveHiddenLayerActionPerformed
        int selRows[] = tblNetworkDesign.getSelectedRows();
        int numRemoved = 0;

        for (int i : selRows)
        {
            if (i > 0)
            {
                TableManager.removeRow(tblNetworkDesign, i - numRemoved);
                numRemoved++;
                hiddenLayerIndex--;
            }
        }
}//GEN-LAST:event_btnRemoveHiddenLayerActionPerformed

    private void btnCreateNetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateNetActionPerformed
        try
        {
            Integer numInputs =
                    Integer.parseInt(txtNumInputs.getText());
            Integer numOutputs =
                    Integer.parseInt(txtNumOutputs.getText());

            NeuralNetwork nn = new NeuralNetwork(numInputs, numOutputs);
            AbstractActivation act = (AbstractActivation) tblNetworkDesign.getValueAt(0, 2);
            nn.getOutputLayer().setActivationFunction(act);

            for (int i = 0; i < hiddenLayerIndex; i++)
            {
                Integer numNeurons = Integer.parseInt(
                        tblNetworkDesign.getValueAt(i + 1, 1).toString());

                NeuronLayer hiddenLayer = new NeuronLayer(numNeurons);
                act = (AbstractActivation) tblNetworkDesign.getValueAt(i + 1, 2);
                hiddenLayer.setActivationFunction(act);
                nn.addLayer(hiddenLayer);
            }
            Configuration.getInstance().setNeuralNetwork(nn);

            isUpdateRequired = true;
            pnlVisualView.repaint();
            StatusDisplayer.getDefault().setStatusText(
                    "Neural Network is refreshed with " + " Inputs = " + numInputs + "," + " Outputs = " + numOutputs + " and Hidden Layers = " + hiddenLayerIndex);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Action failed due to '" + e.getMessage() + "'",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
}//GEN-LAST:event_btnCreateNetActionPerformed

    private void txtNumOutputsKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNumOutputsKeyReleased
        tblNetworkDesign.setValueAt(txtNumOutputs.getText(), 0, 1);
    }//GEN-LAST:event_txtNumOutputsKeyReleased

    private void txtNumOutputsFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNumOutputsFocusLost
        tblNetworkDesign.setValueAt(txtNumOutputs.getText(), 0, 1);
    }//GEN-LAST:event_txtNumOutputsFocusLost

    private void txtNumOutputsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumOutputsActionPerformed
        tblNetworkDesign.setValueAt(txtNumOutputs.getText(), 0, 1);
    }//GEN-LAST:event_txtNumOutputsActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddHiddenLayer;
    private javax.swing.JButton btnCreateNet;
    private javax.swing.JButton btnRemoveHiddenLayer;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private org.jdesktop.swingx.JXHeader jXHeader1;
    private javax.swing.JPanel pnlVisualView;
    private javax.swing.JTable tblNetworkDesign;
    private javax.swing.JFormattedTextField txtNumInputs;
    private javax.swing.JFormattedTextField txtNumOutputs;
    // End of variables declaration//GEN-END:variables

    @Override
    public void resultChanged(LookupEvent ev)
    {        
        Lookup.Result r = (Lookup.Result) ev.getSource();
        Collection c = r.allInstances();     
        boolean isCbxCleared = false;

        if (!c.isEmpty())
        {            
            Iterator it = c.iterator();
            while (it.hasNext())
            {
                Object obj = it.next();
                if (obj instanceof AbstractActivation)
                {
                    if (!isCbxCleared)
                    {
                        cbxActivationFunctions.removeAllItems();
                        isCbxCleared = true;
                    }         
                    cbxActivationFunctions.addItem((AbstractActivation) obj);
                }
            }
        }
    }
}
