/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jnnhelper.ui.actions;

import org.openide.cookies.OpenCookie;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.NbBundle;
import org.openide.util.actions.CookieAction;

public final class LoadNN extends CookieAction {

    protected void performAction(Node[] activatedNodes) {
        OpenCookie openCookie = activatedNodes[0].getLookup().lookup(OpenCookie.class);
        // TODO use openCookie
        
    }

    protected int mode() {
        return CookieAction.MODE_EXACTLY_ONE;
    }

    public String getName() {
        return NbBundle.getMessage(LoadNN.class, "CTL_LoadNN");
    }

    protected Class[] cookieClasses() {
        return new Class[]{OpenCookie.class};
    }

    @Override
    protected void initialize() {
        super.initialize();
        // see org.openide.util.actions.SystemAction.iconResource() Javadoc for more details
        putValue("noIconInMenu", Boolean.TRUE);
    }

    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }
}

