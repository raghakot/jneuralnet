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

package org.jnnhelper.ui;

import java.awt.Color;
import org.jdesktop.swingx.painter.*;
import org.jdesktop.swingx.painter.effects.*;

/**
 *
 * @author Ragha
 */
public class MyPainters
{
    private MyPainters() {

    }

    private static Painter headerPainter;
    public static Painter getHeaderPainter()
    {
        if(headerPainter == null)
        {
            MattePainter mp = new MattePainter(new Color(245, 248, 253));
            mp.setAreaEffects(new GlowPathEffect());
            headerPainter = mp;
        }
        return headerPainter;
    }

    private static Painter statusPanelPainter;
    public static Painter getSummaryPanelPainter()
    {
        if(statusPanelPainter == null)
        {
            MattePainter mp = new MattePainter(new Color(238, 246, 241));
            mp.setAreaEffects(new NeonBorderEffect());
            statusPanelPainter = mp;
        }
        return statusPanelPainter;
    }

    private static Painter footerPanelPainter;
    public static Painter getFooterPanelPainter()
    {
        if(footerPanelPainter == null)
        {
            MattePainter mp = new MattePainter(new Color(239, 239, 239));
            mp.setAreaEffects(new InnerShadowPathEffect());
            footerPanelPainter = mp;
        }
        return footerPanelPainter;
    }
}
