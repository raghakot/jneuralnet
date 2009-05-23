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

package jnnhelper;

import java.io.File;
import javax.swing.SwingUtilities;
import pluginmanager.PluginManager;
import javax.swing.UIManager;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;
import utilities.ErrorLogger;

public class Main {    
    private static final long SPLASH_TIME = 2500;

    public static void main(String args[]) {

        final Splash s = new Splash();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {                
                s.setVisible(true);
            }
        });
        
        final long start = System.nanoTime();

        //setup error handler...
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {

            public void uncaughtException(Thread t, final Throwable e) {
                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {                        
                        ErrorLogger.logError(e, "Unexpected error");
                        JXErrorPane pane = new JXErrorPane();
                        pane.setErrorInfo(new ErrorInfo("Unexpected Error",
                                "<html> This program has performed an unexpected operation.<br>" +
                                "You can however continue to work.<br><br>" +
                                "Please send the log of this message stored in '" +
                                System.getProperty("user.dir") + File.separatorChar
                                + "Error Logs' to the developer to help "+
                                "mitigate this issue.<br><br>" +
                                "<b>Email: ragha_unique2000@yahoo.com</b> </html>",
                                null,
                                null, e,
                                null,
                                null));
                        JXErrorPane.showDialog(null, pane);
                    }
                });
            }
        });       

        try {
            UIManager.setLookAndFeel(new org.jvnet.substance.SubstanceLegacyDefaultLookAndFeel());
        } catch (Exception e) {
            System.out.println(e);
        //ignore...
        }

        try {
            PluginManager.getDefault().rescan("plugins");
        } catch (Exception e) {
            System.err.println("unable to load plugins");
        }

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                MainWindow mainWindow = new MainWindow();

                long end = System.nanoTime();
                long millis = ((end - start) / 1000000);                
                if(millis < SPLASH_TIME) {
                    try {
                        Thread.sleep(SPLASH_TIME - millis);
                    }
                    catch(Exception e) {}
                }

                s.dispose();
                mainWindow.setVisible(true);                
            }
        });
        
    }
}
