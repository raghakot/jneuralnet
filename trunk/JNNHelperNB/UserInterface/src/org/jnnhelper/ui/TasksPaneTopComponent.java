/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jnnhelper.ui;

import java.util.Collection;
import java.util.logging.Logger;
import javax.swing.JComponent;
import org.jnnhelper.ui.analyzer.*;
import org.jnnhelper.ui.databuilder.*;
import org.jnnhelper.ui.networkbuilder.*;
import org.jnnhelper.ui.trainer.*;
import org.openide.util.LookupEvent;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
//import org.openide.util.ImageUtilities;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.util.Lookup;
import org.openide.util.LookupListener;
import ui.coolsidepanel.Category;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//org.jnnhelper.ui//TasksPane//EN",
autostore = false)
public final class TasksPaneTopComponent extends TopComponent implements LookupListener {

    private static TasksPaneTopComponent instance;
    /** path to the icon used by the component and its open action */
//    static final String ICON_PATH = "SET/PATH/TO/ICON/HERE";
    private static final String PREFERRED_ID = "TasksPaneTopComponent";
    private Lookup.Result result;

    public TasksPaneTopComponent() {
        initComponents();
        setName(NbBundle.getMessage(TasksPaneTopComponent.class, "CTL_TasksPaneTopComponent"));
        setToolTipText(NbBundle.getMessage(TasksPaneTopComponent.class, "HINT_TasksPaneTopComponent"));
//        setIcon(ImageUtilities.loadImage(ICON_PATH, true));
        putClientProperty(TopComponent.PROP_CLOSING_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_DRAGGING_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_MAXIMIZATION_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_UNDOCKING_DISABLED, Boolean.TRUE);
        initMyComponents();

        //Listen to changes in training state...
        result = TrainerTopComponent.getDefault().getLookup().lookupResult(Boolean.class);
        result.addLookupListener(this);
        resultChanged(new LookupEvent(result));
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jXHeader1 = new org.jdesktop.swingx.JXHeader();
        coolSidebar1 = new ui.coolsidepanel.CoolSidebar();

        setLayout(new java.awt.BorderLayout());

        jXHeader1.setTitle(org.openide.util.NbBundle.getMessage(TasksPaneTopComponent.class, "TasksPaneTopComponent.jXHeader1.title")); // NOI18N
        add(jXHeader1, java.awt.BorderLayout.PAGE_START);
        add(coolSidebar1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void initMyComponents() {
        jXHeader1.setBackgroundPainter(MyPainters.getHeaderPainter());

        //Build cool side panel...
        Category networkBuilder = new Category(NetworkBuilder.ID, NetworkBuilderTopComponent.getDefault()) {

            @Override
            public void onComponentSwitch(String arg0, JComponent arg1) {
                TopComponent tc = (TopComponent) arg1;
                tc.open();
                tc.requestVisible();
            }
        };
        coolSidebar1.addCategory(networkBuilder);

        //Adding data builder...
        Category dataBuilder = new Category(DataBuilderStatus.ID, DataBuilderTopComponent.getDefault()) {

            @Override
            public void onComponentSwitch(String arg0, JComponent arg1) {
                DataBuilderTopComponent.getDefault().showPanel(arg0);
                DataBuilderTopComponent.getDefault().requestVisible();
            }
        };
        dataBuilder.addSubTitle(DataBuilder.ID, DataBuilderTopComponent.getDefault());
        dataBuilder.addSubTitle(DataClustering.ID, DataBuilderTopComponent.getDefault());
        dataBuilder.addSubTitle(DataPreprocessing.ID, DataBuilderTopComponent.getDefault());
        coolSidebar1.addCategory(dataBuilder);

        //Adding Trainer...
        Category trainer = new Category(Trainer.ID, TrainerTopComponent.getDefault()) {

            @Override
            public void onComponentSwitch(String arg0, JComponent arg1) {
                TopComponent tc = (TopComponent) arg1;
                tc.open();
                tc.requestVisible();
            }
        };
        coolSidebar1.addCategory(trainer);

        //Adding analyzer...
        Category analyzer = new Category(Analyzer.ID, AnalyzerTopComponent.getDefault()) {

            @Override
            public void onComponentSwitch(String arg0, JComponent arg1) {
                TopComponent tc = (TopComponent) arg1;
                tc.open();
                tc.requestVisible();
            }
        };
        coolSidebar1.addCategory(analyzer);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private ui.coolsidepanel.CoolSidebar coolSidebar1;
    private org.jdesktop.swingx.JXHeader jXHeader1;
    // End of variables declaration//GEN-END:variables

    /**
     * Gets default instance. Do not use directly: reserved for *.settings files only,
     * i.e. deserialization routines; otherwise you could get a non-deserialized instance.
     * To obtain the singleton instance, use {@link #findInstance}.
     */
    public static synchronized TasksPaneTopComponent getDefault() {
        if (instance == null) {
            instance = new TasksPaneTopComponent();
        }
        return instance;
    }

    /**
     * Obtain the TasksPaneTopComponent instance. Never call {@link #getDefault} directly!
     */
    public static synchronized TasksPaneTopComponent findInstance() {
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            Logger.getLogger(TasksPaneTopComponent.class.getName()).warning(
                    "Cannot find " + PREFERRED_ID + " component. It will not be located properly in the window system.");
            return getDefault();
        }
        if (win instanceof TasksPaneTopComponent) {
            return (TasksPaneTopComponent) win;
        }
        Logger.getLogger(TasksPaneTopComponent.class.getName()).warning(
                "There seem to be multiple components with the '" + PREFERRED_ID +
                "' ID. That is a potential source of errors and unexpected behavior.");
        return getDefault();
    }

    @Override
    public int getPersistenceType() {
        return TopComponent.PERSISTENCE_ALWAYS;
    }

    @Override
    public void componentOpened() {
        // TODO add custom code on component opening
    }

    @Override
    public void componentClosed() {
        // TODO add custom code on component closing
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    Object readProperties(java.util.Properties p) {
        TasksPaneTopComponent singleton = TasksPaneTopComponent.getDefault();
        singleton.readPropertiesImpl(p);
        return singleton;
    }

    private void readPropertiesImpl(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }

    @Override
    protected String preferredID() {
        return PREFERRED_ID;
    }

    public void resultChanged(LookupEvent ev) {
        Lookup.Result r = (Lookup.Result) ev.getSource();
        Collection c = r.allInstances();
        if (!c.isEmpty()) {
            Boolean b = (Boolean) c.iterator().next();
            if (b) {
                coolSidebar1.getCategory(0).getLabelComponent(NetworkBuilder.ID).setEnabled(false);
                coolSidebar1.getCategory(1).getLabelComponent(DataBuilderStatus.ID).setEnabled(false);
                coolSidebar1.getCategory(3).getLabelComponent(Analyzer.ID).setEnabled(false);
            } else {
                coolSidebar1.getCategory(0).getLabelComponent(NetworkBuilder.ID).setEnabled(true);
                coolSidebar1.getCategory(1).getLabelComponent(DataBuilderStatus.ID).setEnabled(true);
                coolSidebar1.getCategory(3).getLabelComponent(Analyzer.ID).setEnabled(true);
            }
        } else {
            coolSidebar1.getCategory(0).getLabelComponent(NetworkBuilder.ID).setEnabled(true);
            coolSidebar1.getCategory(1).getLabelComponent(DataBuilderStatus.ID).setEnabled(true);
            coolSidebar1.getCategory(3).getLabelComponent(Analyzer.ID).setEnabled(true);
        }
    }
}