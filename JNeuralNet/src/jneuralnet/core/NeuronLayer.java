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
import jneuralnet.core.activation.AbstractActivation;

/**
 * Represents a Neural Layer. A Neural Layer consists of a set of <code>Neurons
 * </code>. This class implements the <tt>Serializable</tt> interface for
 * saving the neural network.
 *
 * <p>It can use any activation function by using
 * {@link #setActivationFunction(jneuralnet.core.activation.AbstractActivation) setActivationFunction(...)} method.
 * Newer activation functions can be defined by implementing <tt>AbstractActivation</tt> interface.
 *
 * <p>This class extends the <tt>Bufferable</tt> and hence supports
 * dynamic property addition.
 *
 * @author Ragha
 * @see AbstractActivation
 * @see Neuron
 * @see Bufferable
 * @version 1.1
 */
public class NeuronLayer extends Bufferable implements Serializable
{
    private static final long serialVersionUID = 3911085246197338475L;

    private ArrayList<Neuron> arrNeurons = new ArrayList<Neuron>();
    private int numNeurons;
    
    /**
     * Creates a neural network with default LogisticSigmoid activation function.
     * Other activation functions can be set using
     * {@link #setActivationFunction(jneuralnet.core.activation.AbstractActivation) setActivationFunction(...)} method.
     * 
     * @param num The number of neurons in this layer.
     * @throws java.lang.IllegalArgumentException If number of neurons is less than 1
     */
    public NeuronLayer(int num) 
            throws IllegalArgumentException
    {
        setNumNeurons(num);
    }

    /**
     *
     * @return The number of neurons in this layer.
     */
    public int getNumNeurons()
    {
        return numNeurons;
    }

    /**
     * Sets the num of neurons in this layer.
     *
     * <p>If number of neurons are automatically pruned or added to the
     * layer accordingly.
     *
     * @param numNeurons The num of neurons to be used in this layer.
     * @throws java.lang.IllegalArgumentException If number of neurons is less than 1.
     */
    public void setNumNeurons(int numNeurons) throws IllegalArgumentException
    {
        if(numNeurons < 1)
        {
            throw new IllegalArgumentException("NeuronLayer cannot have "
                    +numNeurons+" neurons...");
        }
        else
        {
            set("numNeurons", numNeurons);
            int existing = arrNeurons.size();

            if(numNeurons > existing)
            {
                for(int i=0; i<numNeurons - existing;i++)
                {
                    arrNeurons.add(new Neuron());
                }
            }
            else
            {
                for(int i=0; i<existing - numNeurons;i++)
                {
                    arrNeurons.remove(numNeurons + 1);
                }
            }
        }            
    }

    /**
     * Adds a neuron to this layer.
     * @param n The Neuron to be added
     *
     * @see Neuron
     */
    public void addNeuron(Neuron n)
    {        
        arrNeurons.add(n);
        numNeurons++;
    }

    /**
     * Removes a neuron from the layer. The function simply returns
     * if an invalid index value is used.
     * 
     * @param index The index of the neuron to be removed
     * @see Neuron
     */
    public void removeNeuron(int index)
    {        
        if(index < arrNeurons.size())
        {
            arrNeurons.remove(index);
            numNeurons--;
        }
    }

    /**
     * Removes a neuron from this layer. The function simply returns
     * if invalid object reference is used.
     *
     * @param n The neuron to be removed.
     * @see Neuron
     */
    public void removeNeuron(Neuron n)
    {
        if(arrNeurons.contains(n))
        {
            arrNeurons.remove(n);
            numNeurons--;
        }        
    }
    
    /**
     * Sets the activation function of this Layer. Newer activation types
     * can be defined by implementing the <tt>AbstractActivation</tt> interface.
     *
     * <p>A Function <code>getActivationFunction</code> is not defined as
     * individual <code>Neurons</code> can be set up with different activation
     * values using
     * {@link Neuron#setActivationFunction(jneuralnet.core.activation.AbstractActivation)
     * neuron.setActivationFunction(...)} method.
     *
     * <p>The choice of activation function determines the output vector
     * range of this layer.
     *
     * @param act The activation function to be used by this layer.
     * @see AbstractActivation
     */
    public void setActivationFunction(AbstractActivation act)
    {     
        for(Neuron n : arrNeurons)
            n.setActivationFunction(act);
    }
    
    /**
     * Estabilishes a full synaptic connection between each neuron in 
     * this layer to neurons in other layer. It creates new
     * Synaptic connections in the process.
     *
     * @param l Neuron layer to be connected with.
     * @see Neuron
     * @see SynapticConnection
     */
    public void connectTo(NeuronLayer l)
    {
        for(Neuron thisNeuron : arrNeurons)
            for(Neuron n : l.getNeurons())
                thisNeuron.connectTo(n);        
    }

    /**
     * Removes all the output synaptic connections of this layer.
     * i.e, it clears all the output synaptic connections of every <code>
     * Neuron</code> in this layer. This function is internally used by the
     * <code> NeuralNetwork </code> class in
     * {@link NeuralNetwork#refreshConnections() refreshConnections} method.
     *
     * @see SynapticConnection
     */
    void flushOutputConn()
    {
        for(Neuron n : arrNeurons)
            n.getOutputConnections().clear();
    }
    
    /**
     * Removes all the input synaptic connections of this layer.
     * i.e, it clears all the input synaptic connections of every <code>
     * Neuron</code> in this layer. This function is internally used by the
     * <code> NeuralNetwork </code> class in
     * {@link NeuralNetwork#refreshConnections() refreshConnections} method.
     *
     * @see SynapticConnection
     */
    void flushInputConn()
    {
        for(Neuron n : arrNeurons)
            n.getInputConnections().clear();
    }

    /**
     * Gets the neurons used in this layer.
     * @return <tt>ArrayList</tt> of <code>Neuron</code> objects added to this layer.
     *
     * @see Neuron
     */
    public ArrayList<Neuron> getNeurons()
    {
        return arrNeurons;
    }
    
    /**
     * Gets the output vector generated by this layer. It does so by
     * creating an array of outputs generated by individual <code>
     * Neuron</code> objects added to this layer.
     *
     * @param input The input vector presented to this layer.
     * @see Neuron
     */
    public Double[] getOutput(Double input[])
    {
        Double output[] = new Double[numNeurons];
        
        for(int i=0; i<arrNeurons.size();i++)
            output[i] = arrNeurons.get(i).getOutput(input);
        
        return output;
    }
    
    @Override
    public String toString()
    {
        String s = "";
        for(Neuron n : arrNeurons) {
            s += n.toString()+"\n";
        }        
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
        final NeuronLayer other = (NeuronLayer) obj;
        if (this.arrNeurons != other.arrNeurons && (this.arrNeurons == null || !this.arrNeurons.equals(other.arrNeurons))) {
            return false;
        }
        if (this.numNeurons != other.numNeurons) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + (this.arrNeurons != null ? this.arrNeurons.hashCode() : 0);
        hash = 97 * hash + this.numNeurons;
        return hash;
    }
}
