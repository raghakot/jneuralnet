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

package custom;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JList;
import jneuralnet.core.activation.AbstractActivation;
import jneuralnet.util.VisualUtil;

/**
 *
 * @author Ragha
 */
public class ActivationComboRenderer extends DefaultListCellRenderer
{
    private HashMap<String, Icon> actIconsMap;

    public ActivationComboRenderer()
    {
        super();
        actIconsMap = new HashMap<String, Icon>();
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value,
            int index, boolean isSelected, boolean cellHasFocus)
    {        
        AbstractActivation item = (AbstractActivation) value;
        String actName = item.getName();

        Icon icon = null;
        if(actIconsMap.containsKey(actName))
            icon = actIconsMap.get(actName);
        else
        {
            BufferedImage img = GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getDefaultScreenDevice().getDefaultConfiguration()
                .createCompatibleImage(20, 20, Transparency.TRANSLUCENT);

            Graphics g = img.createGraphics();
            g.setColor(Color.BLACK);
            VisualUtil.plotActivation(g, 10, 10, 10, 10, item);
            g.dispose();
            icon = new ImageIcon(img);
            actIconsMap.put(actName, icon);
        }

        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        this.setText(actName);
        this.setIcon(icon);
        return this;        
    }
}
