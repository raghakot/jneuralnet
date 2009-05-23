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
package jneuralnet.core.learning;

import java.util.ArrayList;
import jneuralnet.core.NeuralNetwork;
import jneuralnet.core.Neuron;
import jneuralnet.core.NeuronLayer;
import jneuralnet.core.SynapticConnection;
import jneuralnet.core.learning.costfunction.AbstractCostFunction;
import jneuralnet.core.training.TrainingPattern;

/**
 * This class can be used to create all the possible variants of
 * back propagation algo. All the trivial tasks involved in the
 * back propagation algo such as feed forwarding, output and hidden
 * layer delta calculations are taken care of.
 *
 * <p>Use the {@link #computeWeightChange(jneuralnet.core.SynapticConnection, java.lang.Double) }
 * method for defining weight change of a connection. 
 *
 * <p>Standardized Output delta calculation rule can be modified by overriding
 * {@link #getOutputDelta(jneuralnet.core.Neuron, double, double) } method.
 *
 * <p>To use a modified cost function, use 
 * {@code this.costFunction = yourFunction} in the overridden constructor,
 * where costFunction is a protected variable.
 *
 * @author Ragha
 * @version 1.0
 * @see AbstractLearningAlgo
 * @see AbstractCostFunction
 */
public abstract class AbstractBPBasedAlgo extends AbstractLearningAlgo
{
    private static final long serialVersionUID = 2009050217L;

    //flags to indicate momentum and dynamic learning usages.
    private boolean isMomentumUsed;
    private boolean isLearningRateDynamic;

    /**
     * By default, momentum and dynamic learning rate is disabled. Use
     * {@link #setIsLearningRateDynamic(boolean) } and {@link #setIsMomentumUsed(boolean) }
     * methods to use dynamic learning rate and momentum in this algo.
     *
     * <p>Maximum and minimum permissible learning rates can be set using
     * {@link #setMaxLearningRate(java.lang.Double) } and {@link #setMinLearningRate(java.lang.Double) }
     * methods. By default 5.0 and 0.001 is used respectively.
     */
    protected AbstractBPBasedAlgo()
    {
        super();
        isMomentumUsed = false;
        isLearningRateDynamic = false;
        maxLearningRate = 5.0;
        minLearningRate = 0.001;
    }

    /**
     *
     * @return True, if momentum term is used.
     */
    public boolean isIsMomentumUsed()
    {
        return isMomentumUsed;
    }

    /**
     * Lets you specify if momentum term is to be used or not.
     * This parameter can only be set if momentum is permissible.
     *
     * @param isMomentumUsed True, indicates that momentum is to be used.
     * @see #isMomentumPermissible()
     */
    public void setIsMomentumUsed(boolean isMomentumUsed)
    {
        if (isMomentumPermissible())
        {
            this.isMomentumUsed = isMomentumUsed;
        }
    }

    /**
     *
     * @return True, if dymanic learning rate is used.
     */
    public boolean isIsLearningRateDynamic()
    {
        return isLearningRateDynamic;
    }

    /**
     * Lets you specify is dymanic adjustment of learning rate is to be used.
     * This parameter can only be set if dynamic learning is permissible.
     * 
     * @param isLearningRateDynamic True, indicates that dynamic learning rate
     * is to be used.
     * @see #isDynamicLearningPermissible()
     */
    public void setIsLearningRateDynamic(boolean isLearningRateDynamic)
    {
        if (isDynamicLearningPermissible())
        {
            this.isLearningRateDynamic = isLearningRateDynamic;
        }
    }

    //max and minimum permissible learning rates, i.e., if dynamic
    //learning is enabled...
    private Double maxLearningRate,  minLearningRate;

    public Double getMaxLearningRate()
    {
        return maxLearningRate;
    }

    public void setMaxLearningRate(Double maxLearningRate)
    {
        this.maxLearningRate = maxLearningRate;
    }

    public Double getMinLearningRate()
    {
        return minLearningRate;
    }

    public void setMinLearningRate(Double minLearningRate)
    {
        this.minLearningRate = minLearningRate;
    }

    /**
     * Override this method to specify desired method of changing the
     * weights. The error gradient calculation can be modified by
     * overriding {@link #getOutputDelta(jneuralnet.core.Neuron, double, double) getOutputDelta(...)}
     * method.
     *
     * @return The weight change to be used.
     */
    public abstract Double computeWeightChange(SynapticConnection conn, Double errGradient);

    /**
     * Allows you to put a constraint on momentum. This is useful for algorithms
     * where momentum cant be applied.
     * 
     * @return True to indicate that momentum can
     * be used by this algorithm.
     */
    public abstract boolean isMomentumPermissible();

    /**
     * Lets you specify if dynamic learning is appropriate for this algorithm.
     * @return True to indicate that dynamic learning rate is applicable.
     */
    public abstract boolean isDynamicLearningPermissible();

    //saving on object creation...
    private Double errGradient = 0.0;

    private void adaptNeuron(Double delta, Neuron n)
    {
        for (int i = 0; i < n.getInputConnections().size(); i++)
        {
            SynapticConnection conn = n.getInputConnections().get(i);
            errGradient = delta * n.getCachedInputs()[i];
            processAdjustments(conn, errGradient);
        }
        //adjust the bias weight...
        processAdjustments(n.getBias(), -delta);
    }

    //Again...to reduce obj creation overload...
    private Double newLearningRate = 0.0;
    private Double weightChange = 0.0;

    /**
     * Adjusts the connection weight by taking momentum and dynamic learning
     * rates into consideration, i.e., if they're set.
     *
     * @param conn The connection to be adapted.
     * @param errGradient The error gradient to be used for
     * computing weight change.
     *
     * @see SynapticConnection
     */
    private void processAdjustments(SynapticConnection conn, Double errGradient)
    {
        weightChange = computeWeightChange(conn, errGradient);

        //Add momentum, if used...
        if (isMomentumUsed)
            weightChange += conn.getMomentum() * (conn.getPrevWeightChange());
        //is learning rate dynamic??
        if (isLearningRateDynamic)
        {
            //same signs...increase learning rate...
            if ((conn.isIsLastErrGradientPositive() && errGradient >= 0) ||
                    (!conn.isIsLastErrGradientPositive() && errGradient < 0))
            {
                //increase learning rate by 1%
                newLearningRate = conn.getLearningRate() * 1.01;
                //check permissible bounds...
                if (newLearningRate >= maxLearningRate)
                    newLearningRate = conn.getLearningRate();                
            }
            else
            {
                //diff signs decrease learning rate by 1%
                newLearningRate = conn.getLearningRate() * 0.99;
                //check permissible bounds...
                if (newLearningRate <= minLearningRate)                
                    newLearningRate = conn.getLearningRate();                
            }

            //update for next iteration...
            conn.setIsLastErrGradientPositive(errGradient >= 0);
            //Adapt learning rate...
            conn.setLearningRate(newLearningRate);
        }

        //Save last weight change...
        conn.setPrevWeightChange(weightChange);
        //Adapt weight...
        conn.setWeight(weightChange + conn.getWeight());
    }

    /**
     * Trains the neural network for a single training pattern.
     * @param net The neural network to be used.
     * @param actualOutput The output guessed by the network.
     * @param expectedOutput The expected output that is provided 
     * in the training pattern.
     *
     * @see TrainingPattern
     * @see NeuralNetwork
     */
    public void trainNet(NeuralNetwork net,
            Double actualOutput[], Double expectedOutput[])
    {
        backPropogate(net, expectedOutput, actualOutput);
        adaptWeights(net);
    }

    /**
     * Uses the {@link #computeWeightChange(jneuralnet.core.SynapticConnection, java.lang.Double) }
     * to compute weight changes and then adapts the weight accordingly.
     * 
     * @param net The neural network to adjusted.
     * @see SynapticConnection
     * @see NeuralNetwork
     */
    protected void adaptWeights(NeuralNetwork net)
    {
        NeuronLayer layer;
        layer = net.getOutputLayer();
        int neuronCount = 0;

        //Output layer...
        for (neuronCount = 0; neuronCount < layer.getNeurons().size(); neuronCount++)
        {
            Neuron n = layer.getNeurons().get(neuronCount);
            adaptNeuron(n.getDelta(), n);
        }

        //all the remaining hidden layers...  
        //in the reverse order ie...
        if (net.getHiddenLayers().size() > 0)
        {
            for (int layerCount = net.getHiddenLayers().size() - 1; layerCount >= 0; layerCount--)
            {
                NeuronLayer l = net.getHiddenLayers().get(layerCount);

                for (neuronCount = 0; neuronCount < l.getNeurons().size(); neuronCount++)
                {
                    Neuron n = l.getNeurons().get(neuronCount);
                    adaptNeuron(n.getDelta(), n);
                }
            }
        }
    }

    /**
     * Defines the method to generate delta value.
     * Oveeride this method to define a variant for
     * computing delta values....
     *
     * @param n The output neuron for which delta is to be evaluated.
     * @param desiredOutput The desired output vector...
     * @param actualOutput The output guesssed by the network.
     * @return The delta value.
     * @see Neuron
     */
    public double getOutputDelta(Neuron n, double desiredOutput, double actualOutput)
    {
        return -1.0 * getCostFunction().getOutputDelta(
                desiredOutput, actualOutput, n.getActivation().activationDerviative(n.getCachedSum()));
    }
    //Variables used in gradient calculation...
    //Kept outside to minimize obj creation...since this method will
    //be called within a loop...

    private ArrayList<Double> upperLayerDeltas = new ArrayList<Double>();
    private ArrayList<Double> newDeltas = new ArrayList<Double>();

    /**
     * Calculates the delta values in the output layer.
     * It then back propagates the delta values to all the previous layers
     * and evaluates their delta values.
     * 
     * @param net The neural network to be used
     * @param desiredOutputs The desired output vector for the pattern
     * @param actualOutputs The actual output guessed by the network.
     * @see NeuralNetwork
     */
    private void backPropogate(NeuralNetwork net,
            Double desiredOutputs[], Double actualOutputs[])
    {
        NeuronLayer layer;
        layer = net.getOutputLayer();
        int neuronCount = 0;

        upperLayerDeltas.clear();
        Double delta;

        //Output layer...
        for (neuronCount = 0; neuronCount < layer.getNeurons().size(); neuronCount++)
        {
            Neuron n = layer.getNeurons().get(neuronCount);
            delta = getOutputDelta(n, desiredOutputs[neuronCount],
                    actualOutputs[neuronCount]);
            n.setDelta(delta);
            upperLayerDeltas.add(delta);
        }

        //all the remaining hidden layers...  
        //in the reverse order ie...
        if (net.getHiddenLayers().size() > 0)
        {
            for (int layerCount = net.getHiddenLayers().size() - 1; layerCount >= 0; layerCount--)
            {
                NeuronLayer l = net.getHiddenLayers().get(layerCount);
                newDeltas.clear();

                for (neuronCount = 0; neuronCount < l.getNeurons().size(); neuronCount++)
                {
                    Neuron n = l.getNeurons().get(neuronCount);

                    delta = 0.0;
                    int gradCount = 0;

                    for (gradCount = 0; gradCount < upperLayerDeltas.size(); gradCount++)
                    {
                        delta += upperLayerDeltas.get(gradCount) *
                                n.getOutputConnections().get(gradCount).getWeight();
                    }
                    delta *= n.getActivation().activationDerviative(n.getCachedSum());

                    n.setDelta(delta);
                    newDeltas.add(delta);
                }

                upperLayerDeltas.clear();
                for (Double d : newDeltas)
                {
                    upperLayerDeltas.add(d);
                }
            }
        }
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final AbstractBPBasedAlgo other = (AbstractBPBasedAlgo) obj;
        if (this.errGradient != other.errGradient && (this.errGradient == null || !this.errGradient.equals(other.errGradient)))
        {
            return false;
        }
        if (this.upperLayerDeltas != other.upperLayerDeltas && (this.upperLayerDeltas == null || !this.upperLayerDeltas.equals(other.upperLayerDeltas)))
        {
            return false;
        }
        if (this.newDeltas != other.newDeltas && (this.newDeltas == null || !this.newDeltas.equals(other.newDeltas)))
        {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 97 * hash + (this.errGradient != null ? this.errGradient.hashCode() : 0);
        hash = 97 * hash + (this.upperLayerDeltas != null ? this.upperLayerDeltas.hashCode() : 0);
        hash = 97 * hash + (this.newDeltas != null ? this.newDeltas.hashCode() : 0);
        return hash;
    }

}
