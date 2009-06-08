/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jnnhelper.ui.databuilder;

import java.io.Serializable;
import java.util.logging.Logger;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
//import org.openide.util.Utilities;

/**
 * Top component which displays something.
 */
public final class DataBuilderTopComponent extends TopComponent {

    private static DataBuilderTopComponent instance;
    /** path to the icon used by the component and its open action */
//    static final String ICON_PATH = "SET/PATH/TO/ICON/HERE";

    private static final String PREFERRED_ID = "DataBuilderTopComponent";

    //Panels permissible...
    private DataBuilderStatus pnlDataBuilderStatus;
    private DataBuilder pnlDataBuilder;
    private DataClustering pnlDataClustering;
    private DataPreprocessing pnlDataPreprocessing;

    private DataBuilderTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(DataBuilderTopComponent.class, "CTL_DataBuilderTopComponent"));
        setToolTipText(NbBundle.getMessage(DataBuilderTopComponent.class, "HINT_DataBuilderTopComponent"));
//        setIcon(Utilities.loadImage(ICON_PATH, true));

        pnlDataBuilderStatus = new DataBuilderStatus();
        pnlDataBuilder = new DataBuilder();
        pnlDataClustering = new DataClustering();
        pnlDataPreprocessing = new DataPreprocessing();
        jScrollPane1.setViewportView(pnlDataBuilderStatus);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();

        setLayout(new java.awt.BorderLayout());
        add(jScrollPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
    /**
     * Gets default instance. Do not use directly: reserved for *.settings files only,
     * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
     * To obtain the singleton instance, use {@link #findInstance}.
     */
    public static synchronized DataBuilderTopComponent getDefault()
    {
        if (instance == null)
        {
            instance = new DataBuilderTopComponent();
        }
        return instance;
    }

    /**
     * Obtain the DataBuilderTopComponent instance. Never call {@link #getDefault} directly!
     */
    public static synchronized DataBuilderTopComponent findInstance()
    {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null)
        {
            Logger.getLogger(DataBuilderTopComponent.class.getName()).warning(
                    "Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (win instanceof DataBuilderTopComponent)
        {
            return (DataBuilderTopComponent) win;
        }
        Logger.getLogger(DataBuilderTopComponent.class.getName()).warning(
                "There seem to be multiple components with the '" + PREFERRED_ID +
                "' ID. That is a potential source of errors and unexpected behavior.");
        return getDefault();
    }

    @Override
    public int getPersistenceType()
    {
        return TopComponent.PERSISTENCE_ALWAYS;
    }

    @Override
    public void componentOpened()
    {
        // TODO add custom code on component opening
    }

    @Override
    public void componentClosed()
    {
        // TODO add custom code on component closing
    }

    /** replaces this in object stream */
    @Override
    public Object writeReplace()
    {
        return new ResolvableHelper();
    }

    @Override
    protected String preferredID()
    {
        return PREFERRED_ID;
    }

    final static class ResolvableHelper implements Serializable
    {
        private static final long serialVersionUID = 1L;

        public Object readResolve()
        {
            return DataBuilderTopComponent.getDefault();
        }
    }

    public void showPanel(String pnlID)
    {
        if(!isOpened())
            open();        

        if(pnlID.equals(DataBuilderStatus.ID))
            jScrollPane1.setViewportView(pnlDataBuilderStatus);
        if(pnlID.equals(DataBuilder.ID))
            jScrollPane1.setViewportView(pnlDataBuilder);
        if(pnlID.equals(DataClustering.ID))
            jScrollPane1.setViewportView(pnlDataClustering);
        if(pnlID.equals(DataPreprocessing.ID))
            jScrollPane1.setViewportView(pnlDataPreprocessing);
    }
}
