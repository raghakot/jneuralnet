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

public final class SaveNN implements ActionListener {

    public void actionPerformed(ActionEvent e) {
        String fname = FileManager.getSaveFile("NeuralNet Files", "nn");
        if (fname == null) {
            return;
        }

        try {
            if (!fname.toLowerCase().endsWith(".nn")) {
                fname += ".nn";
            }
            Configuration.getInstance().getNeuralNetwork().saveNet(fname);
            StatusDisplayer.getDefault().setStatusText("Successfully saved " +
                    "to " + fname);
        } catch (Exception ex) {
            StatusDisplayer.getDefault().setStatusText("Failed: " + ex,
                    StatusDisplayer.IMPORTANCE_ERROR_HIGHLIGHT);
        }

    }
}
