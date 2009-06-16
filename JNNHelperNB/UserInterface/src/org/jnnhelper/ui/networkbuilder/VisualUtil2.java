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

package org.jnnhelper.ui.networkbuilder;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import jneuralnet.core.NeuralNetwork;
import jneuralnet.core.activation.AbstractActivation;

/**
 * This class provides utilities for providing a viaul representation
 * of neural network and activation functions.
 *
 * @author Ragha
 * @version 1.0
 */
public class VisualUtil2
{
    /**
     * Endure that the class is not instantiable.
     */
    private VisualUtil2() {
        
    }    
        
    /**
     * Returns a visual representation of the neural network in a given width
     * and height.     
     *
     * @return The visual representation of the neural network.
     */
    public static BufferedImage getNetworkImage(NeuralNetwork nn, int width,
            int height)
    {
        BufferedImage img = getCompatibleImage(width, height, Transparency.TRANSLUCENT);
                
        int totalLayers = nn.getHiddenLayers().size() + 2;
        float offsetX = width / (totalLayers + 1);
        float offsetY[] = new float[totalLayers];

        //selecting minimum among y offsets and calculating required offsets...
        float min;
        offsetY[0] = height / (nn.getNumInputs() + 1);
        
        min = offsetY[0];
        for(int i=0; i<nn.getNumHiddenLayers();i++)
        {
            offsetY[i+1] = height / (nn.getHiddenLayerNeurons(i) + 1);
            if(min > offsetY[i+1])
                min = offsetY[i+1];
        }
        offsetY[totalLayers - 1] = height / (nn.getNumOutputs() + 1);
        if(min > offsetY[totalLayers - 1])
            min = offsetY[totalLayers - 1];
        if(min > offsetX)
            min = offsetX;
        
        int rad = Math.round(min / 3f);
        int startX = Math.round(offsetX);
        int startY;
        int rad2X = rad * 2;
        int length = (int) Math.round(rad / 1.414);

        Graphics g = img.getGraphics();
        g.setColor(Color.BLACK);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        //draw input layer...
        startY = Math.round(offsetY[0]);        
        for(int neurons = 0; neurons < nn.getNumInputs(); neurons++)
        {
            //draw neural input line...
            g2d.drawLine(startX - rad, startY,
                    (int) (startX - rad - (offsetX / 2)), startY);

            g2d.drawOval(startX - rad, startY - rad, rad2X, rad2X);
            int num = (height / Math.round(offsetY[1])) - 1;
            drawLine(g2d, startX, startY, Math.round(offsetX),
                    Math.round(offsetY[1]), rad, num);
            startY += Math.round(offsetY[0]);            
        }        
        startX += offsetX;

        //draw hidden layers...
        for(int layer = 0;layer < totalLayers - 2; layer++)
        {
            startY = Math.round(offsetY[layer + 1]);
            for(int neuron = 0; neuron < nn.getHiddenLayerNeurons(layer); neuron++)
            {
                g2d.drawOval(startX - rad, startY - rad, rad2X, rad2X);
                plotActivation(g2d, startX, startY, length, length,
                        nn.getHiddenLayers().get(layer)
                        .getNeurons().get(neuron).getActivation());
                int num = (height / Math.round(offsetY[layer + 2])) - 1;
                drawLine(g2d, startX, startY, Math.round(offsetX),
                    Math.round(offsetY[layer + 2]), rad, num);
                startY += Math.round(offsetY[layer + 1]);                
            }
            startX += offsetX;
        }

        //draw output layer...
        startY = Math.round(offsetY[totalLayers - 1]);
        for(int neuron = 0; neuron < nn.getNumOutputs(); neuron++)
        {
            //draw neural output lines...
            g2d.drawLine(startX + rad, startY,
                    (int) (startX + rad + (offsetX / 2)), startY);

            g2d.drawOval(startX - rad, startY - rad, rad2X, rad2X);
            plotActivation(g2d, startX, startY, length, length,
                        nn.getOutputLayer().getNeurons()
                        .get(neuron).getActivation());
            startY += Math.round(offsetY[totalLayers - 1]);           
        }

        g.dispose();
        return img;
    }

    /**
     * Draws the synaptic connections from one layer to another.
     * @param g2d The graphics context to be used.
     * @param startX The x-center of the circle
     * @param startY The y-center of the circle
     * @param offsetX The x-gap between this and the next layer.
     * @param nextOffsetY  The y-gap between thi and the next layer.
     * @param rad The radius of the circle used.
     * @param numCircles num of circles in the next layer.
     */
    private static void drawLine(Graphics2D g2d, int startX, int startY, int offsetX,
                    int nextOffsetY, int rad, int numCircles)
    {
        float nextY;
        nextY = nextOffsetY;
        for(int i=0; i<numCircles; i++)
        {
            float heightDiff = nextY - startY;
            float diag = (float) Math.sqrt((offsetX * offsetX) + (heightDiff * heightDiff));
            float cosThetha = offsetX / diag;
            float sinThehta = heightDiff / diag;

            g2d.drawLine(Math.round(startX + rad * cosThetha),
                    Math.round(startY + rad * sinThehta),
                    Math.round(startX + (diag - rad) * cosThetha),
                    Math.round(startY + (diag - rad) * sinThehta));

            nextY += nextOffsetY;
        }
    }

    /**
     * Plots the given activation function in the Graphics context.
     *
     * @param g The graphics container onto which the plot is drawn
     * @param centerX The x - origin of the plot
     * @param centerY The y - origin of the plot
     * @param plotWidth The length of +ve x axis...
     * @param plotHeight The length of +ve y axis...
     * @param act The Activation to be plotted.
     */
    public static void plotActivation(Graphics g, int centerX, int centerY,
            int plotWidth, int plotHeight, AbstractActivation act)
    {        
        //The output data points...
        float y[] = new float[2 * plotWidth];

        //determine data points and get max amongst them...
        float max = Float.MIN_VALUE;
        for(int i=0; i<2 * plotWidth; i++)
        {
            y[i] = act.activation((-plotWidth + i) * 1.0).floatValue();
            if(max < y[i])
                max = y[i];
        }

        //scale data points to fit within 'plotHeight'
        float scaleFactor = plotHeight / max;

        //create temp graphics...
        Graphics gTemp = g.create();
        ((Graphics2D)gTemp).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        //draw plots...
        int prevX = centerX - plotWidth;
        int prevY = (int) (centerY - (y[0] * scaleFactor));
        for(int i=1; i<2 * plotWidth; i++)
        {            
            int xNew = prevX + 1;
            int yNew = (int) (centerY - (y[i] * scaleFactor));
            gTemp.drawLine(prevX, prevY, xNew, yNew);
            prevX = xNew;
            prevY = yNew;
        }

        gTemp.dispose();
    }

    /**
     * Returns a BufferedImage with a data layout and color model
     * compatible with the current GraphicsConfiguration.
     *
     * <p>This method has nothing to do with memory-mapping a device.
     * The returned BufferedImage has a layout and color model that is
     * closest to this native device configuration and can therefore
     * be optimally blitted to this device.
     *
     * @param width width of the buffer
     * @param height height of the buffer
     * @param transperancy specifies the transperancy.
     * @return a BufferedImage whose data layout and color model
     * is compatible with the current GraphicsConfiguration.
     */
    public static BufferedImage getCompatibleImage (int width, int height, int transperancy)
    {
        return GraphicsEnvironment.getLocalGraphicsEnvironment()
               .getDefaultScreenDevice().getDefaultConfiguration()
               .createCompatibleImage(width, height, transperancy);
    }
}
