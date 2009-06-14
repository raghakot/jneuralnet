/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jnnhelper.ui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import jneuralnet.util.FileManager;
import org.jnnhelper.ui.Configuration;
import org.openide.awt.StatusDisplayer;

public final class SaveAsXML implements ActionListener {

    public void actionPerformed(ActionEvent e) {
                String fname = FileManager.getLoadFile("XML Files", "xml");
        if (fname == null) {
            return;
        }

        try {
            if (!fname.toLowerCase().endsWith(".xml")) {
                fname += ".xml";
            }

            Configuration.getInstance().getNeuralNetwork().saveToXML(fname);
            StatusDisplayer.getDefault().setStatusText("Successfully saved " +
                    "to " + fname);
        } catch (Exception ex) {
            StatusDisplayer.getDefault().setStatusText("Failed: " + ex,
                    StatusDisplayer.IMPORTANCE_ERROR_HIGHLIGHT);
        }
    }
}
