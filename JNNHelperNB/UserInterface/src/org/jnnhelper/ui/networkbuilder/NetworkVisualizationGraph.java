/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jnnhelper.ui.networkbuilder;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import jneuralnet.core.NeuralNetwork;
import jneuralnet.core.Neuron;
import jneuralnet.core.SynapticConnection;
import jneuralnet.core.activation.AbstractActivation;
import jneuralnet.core.activation.Linear;
import jneuralnet.util.VisualUtil;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.ConnectProvider;
import org.netbeans.api.visual.action.ConnectorState;
import org.netbeans.api.visual.anchor.AnchorFactory;
import org.netbeans.api.visual.anchor.AnchorShape;
import org.netbeans.api.visual.graph.GraphScene;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.api.visual.widget.general.IconNodeWidget;

/**
 *
 * @author Ragha
 */
public class NetworkVisualizationGraph extends GraphScene<Neuron, SynapticConnection> {

    private LayerWidget mainLayer;
    private LayerWidget interactionLayer;
    private LayerWidget connectionLayer;

    public NetworkVisualizationGraph() {
        mainLayer = new LayerWidget(this);
        connectionLayer = new LayerWidget(this);
        interactionLayer = new LayerWidget(this);
        addChild(mainLayer);
        addChild(connectionLayer);
        addChild(interactionLayer);
        getActions().addAction(ActionFactory.createZoomAction());
    }

    @Override
    protected Widget attachNodeWidget(Neuron neuron) {
        IconNodeWidget widget = new IconNodeWidget(this);

        //Image of the neuron...
        BufferedImage img = VisualUtil.getCompatibleImage(20, 20, Transparency.TRANSLUCENT);

        //init settings...
        Graphics2D g2d = img.createGraphics();
        g2d.setColor(Color.BLACK);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        //Draw neuron...
        g2d.drawOval(startX - rad, startY - rad, rad2X, rad2X);
        //plot activation function...        
        plotActivation(g2d, startX, startY, length, length, neuron.getActivation());
        g2d.dispose();

        //set widget properties..
        widget.setImage(img);
        widget.setLabel("Neuron");

        //Hooking up actions...
        widget.getActions().addAction(
                ActionFactory.createExtendedConnectAction(
                connectionLayer, new MyConnectProvider()));
        widget.getActions().addAction(
                ActionFactory.createAlignWithMoveAction(
                mainLayer, interactionLayer,
                ActionFactory.createDefaultAlignWithMoveDecorator()));

        mainLayer.addChild(widget);
        return widget;
    }

    @Override
    protected Widget attachEdgeWidget(SynapticConnection arg0) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void attachEdgeSourceAnchor(SynapticConnection arg0, Neuron arg1, Neuron arg2) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void attachEdgeTargetAnchor(SynapticConnection arg0, Neuron arg1, Neuron arg2) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    private Linear linearAct = new Linear();
    private int startX, startY, rad, rad2X, length;
    // @TODO: use listeners to listen for property changes of net and trigger redraw..

    public void showNetwork(NeuralNetwork nn, int width, int height) {
        mainLayer.removeChildren();

        int totalLayers = nn.getHiddenLayers().size() + 2;
        float offsetX = width / (totalLayers + 1);
        float offsetY[] = new float[totalLayers];

        //selecting minimum among y offsets and calculating required offsets...
        float min;
        offsetY[0] = height / (nn.getNumInputs() + 1);

        min = offsetY[0];
        for (int i = 0; i < nn.getNumHiddenLayers(); i++) {
            offsetY[i + 1] = height / (nn.getHiddenLayerNeurons(i) + 1);
            if (min > offsetY[i + 1]) {
                min = offsetY[i + 1];
            }
        }
        offsetY[totalLayers - 1] = height / (nn.getNumOutputs() + 1);
        if (min > offsetY[totalLayers - 1]) {
            min = offsetY[totalLayers - 1];
        }
        if (min > offsetX) {
            min = offsetX;
        }

        rad = Math.round(min / 3f);
        startX = Math.round(offsetX);

        rad2X = rad * 2;
        length = (int) Math.round(rad / 1.414);
        System.out.println("rad = "+rad);

        //draw input layer...
        startY = Math.round(offsetY[0]);
        for (int neurons = 0; neurons < nn.getNumInputs(); neurons++) {
            //draw neural input line...
//            g2d.drawLine(startX - rad, startY,
//                    (int) (startX - rad - (offsetX / 2)), startY);

            addNode(new Neuron(linearAct)).setPreferredLocation(new Point(startX, startY));

            int num = (height / Math.round(offsetY[1])) - 1;
//            drawLine(g2d, startX, startY, Math.round(offsetX),
//                    Math.round(offsetY[1]), rad, num);
            startY += Math.round(offsetY[0]);
        }
        startX += offsetX;

        //draw hidden layers...
        for (int layer = 0; layer < totalLayers - 2; layer++) {
            startY = Math.round(offsetY[layer + 1]);
            for (int neuron = 0; neuron < nn.getHiddenLayerNeurons(layer); neuron++) {
                addNode(nn.getHiddenLayers().get(layer).getNeurons().get(neuron)).setPreferredLocation(new Point(startX, startY));

                int num = (height / Math.round(offsetY[layer + 2])) - 1;
//                drawLine(g2d, startX, startY, Math.round(offsetX),
//                    Math.round(offsetY[layer + 2]), rad, num);
                startY += Math.round(offsetY[layer + 1]);
            }
            startX += offsetX;
        }

        //draw output layer...
        startY = Math.round(offsetY[totalLayers - 1]);
        for (int neuron = 0; neuron < nn.getNumOutputs(); neuron++) {
            //draw neural output lines...
//            g2d.drawLine(startX + rad, startY,
//                    (int) (startX + rad + (offsetX / 2)), startY);

            addNode(nn.getOutputLayer().getNeurons().get(neuron)).setPreferredLocation(new Point(startX, startY));


//            g2d.drawOval(startX - rad, startY - rad, rad2X, rad2X);
//            plotActivation(g2d, startX, startY, length, length,
//                        nn.getOutputLayer().getNeurons()
//                        .get(neuron).getActivation());
            startY += Math.round(offsetY[totalLayers - 1]);
        }
    }

    private class MyConnectProvider implements ConnectProvider {

        public boolean isSourceWidget(Widget source) {
            return source instanceof IconNodeWidget && source != null ? true : false;
        }

        public ConnectorState isTargetWidget(Widget src, Widget trg) {
            return src != trg && trg instanceof IconNodeWidget ? ConnectorState.ACCEPT : ConnectorState.REJECT;
        }

        public boolean hasCustomTargetWidgetResolver(Scene arg0) {
            return false;
        }

        public Widget resolveTargetWidget(Scene arg0, Point arg1) {
            return null;
        }

        public void createConnection(Widget source, Widget target) {
            ConnectionWidget conn = new ConnectionWidget(NetworkVisualizationGraph.this);
            conn.setTargetAnchorShape(AnchorShape.TRIANGLE_FILLED);
            conn.setTargetAnchor(AnchorFactory.createRectangularAnchor(target));
            conn.setSourceAnchor(AnchorFactory.createRectangularAnchor(source));
            connectionLayer.addChild(conn);
        }
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
            int nextOffsetY, int rad, int numCircles) {
        float nextY;
        nextY = nextOffsetY;
        for (int i = 0; i < numCircles; i++) {
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
    public static void plotActivation(Graphics2D g2d, int centerX, int centerY,
            int plotWidth, int plotHeight, AbstractActivation act) {
        //The output data points...
        float y[] = new float[2 * plotWidth];

        //determine data points and get max amongst them...
        float max = Float.MIN_VALUE;
        for (int i = 0; i < 2 * plotWidth; i++) {
            y[i] = act.activation((-plotWidth + i) * 1.0).floatValue();
            if (max < y[i]) {
                max = y[i];
            }
        }

        //scale data points to fit within 'plotHeight'
        float scaleFactor = plotHeight / max;
        
        //draw plots...
        int prevX = centerX - plotWidth;
        int prevY = (int) (centerY - (y[0] * scaleFactor));
        for (int i = 1; i < 2 * plotWidth; i++) {
            int xNew = prevX + 1;
            int yNew = (int) (centerY - (y[i] * scaleFactor));
            g2d.drawLine(prevX, prevY, xNew, yNew);
            prevX = xNew;
            prevY = yNew;
        }       
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
    public static BufferedImage getCompatibleImage(int width, int height, int transperancy) {
        return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().createCompatibleImage(width, height, transperancy);
    }
}
