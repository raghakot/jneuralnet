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

package jneuralnet.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import jneuralnet.core.activation.AbstractActivation;
import jneuralnet.core.activation.LogisticSigmoid;
import jneuralnet.core.learning.AbstractBPBasedAlgo;
import jneuralnet.core.learning.AbstractLearningAlgo;

/**
 * The building block of the neural network. A <code>Neuron</code> has certain
 * input and output synapses.It caches input, output values along with error gradients
 * to enable training process to be Object Oriented.This class
 * implements <tt>Serializable</tt> interface for saving the neural network.
 *
 * <p>It can use any activation function by using
 * {@link Neuron#setActivationFunction(jneuralnet.core.activation.AbstractActivation)
 * setActivationFunction} method. Newer activation functions can
 * be defined by implementing <tt>AbstractActivation</tt> interface.
 *
 * <p>This class extends the <tt>Bufferable</tt> and hence supports
 * dynamic property addition.
 *
 * @author Ragha
 * @see AbstractActivation
 * @see SynapticConnection
 * @see Bufferable
 * @version 1.0
 */
public final class Neuron extends Bufferable implements Serializable
{
    private static final long serialVersionUID = -4579026829515877805L;

    /**
     * Represents the bias connection of this neuron. Also known as
     * threshold value in some books. It has a constant input of -1.
     */
    private SynapticConnection bias;
    
    /**
     * Delta values are intended set and get by the 
     * <code>learningAlgo</code>. <code>BPBasedAlgo</code> uses this logic
     * during back propogation.
     * 
     * @see AbstractLearningAlgo
     * @see AbstractBPBasedAlgo#backPropogate(jneuralnet.core.NeuralNetwork, java.lang.Double[], java.lang.Double[])
     * java.util.ArrayList) 
     */    
    private Double delta;
    
    /**
     * Cached values of the input vectors encountered by the neuron during
     * forward propogation.
     */
    private Double cachedInputs[];
    
    /**
     * Cached values of recent output generated by the neuron during
     * forward propogation.
     */    
    private Double cachedOutput;  

    /**
     * Cached value of the weighted sum <tt>summation (Wi * Xi)</tt>
     * generated during forward propogation.
     */
    private Double cachedSum;
    
    /**
     * Indicates the activation function to be used by this neuron.
     * @see AbstractActivation
     */
    private AbstractActivation act;
    
    /**
     * Maintains the input synaptic connections to this neuron.
     * @see SynapticConnection
     */
    private ArrayList<SynapticConnection> arrInputConn
            = new ArrayList<SynapticConnection>();
    
    /**
     * Maintains the output synaptic connections of this neuron.
     * @see SynapticConnection
     */    
    private ArrayList<SynapticConnection> arrOutputConn
            = new ArrayList<SynapticConnection>();  
    
    /**
     * Initializes the <code>neuron</code> with the given activation function. 
     * The activation function can be changed later by using
     * {@link Neuron#setActivationFunction(jneuralnet.core.activation.AbstractActivation) 
     * setActivationFunction} method.
     * 
     * @param act
     * The activation function to be used by the neuron. Uses
     * <tt>LogisticSigmoid</tt> activation function by default.
     *
     * @see AbstractActivation
     */
    public Neuron(AbstractActivation act)
    {           
        //range = -1 to +1        
        bias = new SynapticConnection(-1.0, 1.0);
        delta = 0.0;        
        setActivationFunction(act);
    }
    
    /**
     * Initializes the <code>neuron</code> with <tt>LogisticSigmoid</tt> as
     * the default activation function. Default learning rate = 0.7 and default
     * momentum = 0.5.
     *
     * @see AbstractActivation
     * @see LogisticSigmoid
     */
    public Neuron()
    {        
        this(new LogisticSigmoid());
    }
      
    /**
     * Generates the output of the neuron. It caches the input data
     * and the weighted sum <tt>summation (Wi * Xi)</tt> be used in the learning process.
     *
     * <p>The range of output values generated depends on the
     * activation function used. The output generated is equivalent to
     * <code>act.activation(∑(Wi * Xi))</code>
     * 
     * @param input The input vector. This vector is passes automatically
     * by the framework to evaluate the overall output of the network.
     * 
     * @return The output value of the neuron.
     * @see AbstractActivation
     */
    Double getOutput(Double input[])
    {
        cachedInputs = input;
        cachedSum = 0.0;

        int i = 0;
        for(Double val : input)
        {
            cachedSum += val * arrInputConn.get(i).getWeight();            
            i++;
        }
        
        cachedSum -= bias.getWeight();
        cachedOutput = act.activation(cachedSum);
        return cachedOutput;
    }
    
    /**
     * Connects this <code>neuron</code> to another. It creates
     * a <code>SynapticConnection</code> which is the
     * output synapse to this neuron and input to the other.
     * 
     * @param other The neuron to which a connection is to be estabilished.
     * @see SynapticConnection
    */
    void connectTo(Neuron other)
    {
        SynapticConnection conn = new SynapticConnection();
        arrOutputConn.add(conn);
        other.addInputConnection(conn);        
    }            
    
    /**
     * This method is useful to get all the input connections this neuron.
     * 
     * @return The input synaptic connections to this neuron
     * @see SynapticConnection
     */
    public ArrayList<SynapticConnection> getInputConnections()
    {
        return arrInputConn;
    }
    
    /**
     * This method is useful to get all the output connections this neuron.
     * 
     * @return The output synaptic connections to this neuron
     * @see SynapticConnection
     */
    public ArrayList<SynapticConnection> getOutputConnections()
    {
        return arrOutputConn;
    }
    
    /**
     * This method is internally used by 
     * {@link Neuron#connectTo(jneuralnet.core.Neuron) connectTo} 
     * method.
     * 
     * @param conn The synapse to be added to the input connections.
     * @see SynapticConnection
     */
    public void addInputConnection(SynapticConnection conn)
    {        
        arrInputConn.add(conn);
    }
    
    /**
     * Intended to be used by the <Code>LearningAlgo</code> during
     * the training process.
     * 
     * @return The recent input vector encountered by this neuron during
     * forward propogation.
     */
    public Double[] getCachedInputs()
    {        
        return cachedInputs;
    }
    
    /**
     * Intended to be used by the <Code>LearningAlgo</code> during
     * the training process.
     * 
     * @return The recent output generated by the neuron during
     * forward propogation.
     */
    public Double getCachedOutput()
    {
        return cachedOutput;
    }

    /**
     * Intended to be used by the <Code>LearningAlgo</code> during
     * the training process.
     *
     * @return The weighted sum ∑(Wi * Xi) encountered by the neuron during
     * forward propogation.
     */
    public Double getCachedSum()
    {
        return cachedSum;
    }

    /**
     * Can be used to set bias value of this neuron.
     * @param bias The Synaptic connection to be used as the bias.
     */
    public void setBias(SynapticConnection bias)
    {
        set("bias", bias);
    }

    /**
     * Returns the synaptic connection represented by this bias.
     * @return The bias used by the neuron.
     */
    public SynapticConnection getBias()
    {
        return bias;
    }

    /**
     * Intended to be used by the <Code>LearningAlgo</code> during
     * the training process to set error gradient values in an
     * object oriented manner.
     */
    public void setDelta(Double delta)
    {
        set("delta", delta);
    }

    /**
     * Intended to be used by the <Code>LearningAlgo</code> during
     * the training process to get error gradient values in an
     * object oriented manner.
     */
    public Double getDelta()
    {
        return delta;
    }
    
    /**
     * Sets the activation function of this neuron. Newer activation types
     * can be defined by implementing the <tt>AbstractActivation</tt> interface.
     *
     * <p>The choice of activation function determines the output values
     * of the neuron.
     *
     * @param act The activation function to be used by this neuron.
     * @see AbstractActivation
     */
    public void setActivationFunction(AbstractActivation act)
    {
        set("act", act);
    }

    /**
     * Gets the <code>Activation</code> used by this neuron. Newer activation
     * types can be defined by implementing the <tt>AbstractActivation</tt> interface.
     *
     * <p>The choice of activation function determines the output values
     * of the neuron.
     *
     * @return The activation function used by this neuron.
     * @see AbstractActivation
     */
    public AbstractActivation getActivation()
    {
        return act;
    }
    
    @Override
    public String toString()
    {           
        String s = "";
        s += "Bias weight = "+bias.getWeight() + "\n";
        s += "Activation Function = "+act.toString() + "\n";
        s += "Input connections = "+arrInputConn.toString() + "\n";
        s += "Output connections = "+arrOutputConn.toString() + "\n";
        return s;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Neuron other = (Neuron) obj;
        if (this.bias != other.bias && (this.bias == null || !this.bias.equals(other.bias))) {
            return false;
        }
        if (this.delta != other.delta && (this.delta == null || !this.delta.equals(other.delta))) {
            return false;
        }
        if (this.cachedInputs != other.cachedInputs && (this.cachedInputs == null || !Arrays.equals(this.cachedInputs,other.cachedInputs))) {
            return false;
        }
        if (this.cachedOutput != other.cachedOutput && (this.cachedOutput == null || !this.cachedOutput.equals(other.cachedOutput))) {
            return false;
        }
        if (this.cachedSum != other.cachedSum && (this.cachedSum == null || !this.cachedSum.equals(other.cachedSum))) {
            return false;
        }
        if (this.act != other.act && (this.act == null || !this.act.equals(other.act))) {
            return false;
        }
        if (this.arrInputConn != other.arrInputConn && (this.arrInputConn == null || !this.arrInputConn.equals(other.arrInputConn))) {
            return false;
        }
        if (this.arrOutputConn != other.arrOutputConn && (this.arrOutputConn == null || !this.arrOutputConn.equals(other.arrOutputConn))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + (this.bias != null ? this.bias.hashCode() : 0);
        hash = 53 * hash + (this.delta != null ? this.delta.hashCode() : 0);
        hash = 53 * hash + (this.cachedInputs != null ? this.cachedInputs.hashCode() : 0);
        hash = 53 * hash + (this.cachedOutput != null ? this.cachedOutput.hashCode() : 0);
        hash = 53 * hash + (this.cachedSum != null ? this.cachedSum.hashCode() : 0);
        hash = 53 * hash + (this.act != null ? this.act.hashCode() : 0);
        hash = 53 * hash + (this.arrInputConn != null ? this.arrInputConn.hashCode() : 0);
        hash = 53 * hash + (this.arrOutputConn != null ? this.arrOutputConn.hashCode() : 0);
        return hash;
    }
}
