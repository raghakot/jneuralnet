/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jnnhelper.ui.actions;

import java.util.Collection;
import jneuralnet.core.NeuralNetwork;
import jneuralnet.util.FileManager;
import org.jnnhelper.ui.Configuration;
import org.jnnhelper.ui.trainer.TrainerTopComponent;
import org.openide.awt.StatusDisplayer;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;

public final class LoadFromXML extends CallableSystemAction implements LookupListener {

    private Lookup.Result result;

    public LoadFromXML() {
        result = TrainerTopComponent.getDefault().getLookup().lookupResult(Boolean.class);
        result.addLookupListener(this);
        resultChanged(new LookupEvent(result));
    }

    public String getName() {
        return NbBundle.getMessage(LoadNN.class, "CTL_LoadFromXML");
    }

    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    public void performAction() {
        String fname = FileManager.getLoadFile("XML Files", "xml");
        if (fname == null) {
            return;
        }

        try {
            Configuration.getInstance().setNeuralNetwork(
                    NeuralNetwork.loadFromXML(fname));
            StatusDisplayer.getDefault().setStatusText(fname + "successfully loaded");
        } catch (Exception ex) {

            StatusDisplayer.getDefault().setStatusText("Failed: " + ex,
                    StatusDisplayer.IMPORTANCE_ERROR_HIGHLIGHT);
        }
    }

    public void resultChanged(LookupEvent ev) {
        Lookup.Result r = (Lookup.Result) ev.getSource();
        Collection c = r.allInstances();
        if (!c.isEmpty()) {
            Boolean b = (Boolean) c.iterator().next();
            setEnabled(!b);
        } else {
            setEnabled(true);
        }
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }
}
