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

package jneuralnet.core.training;

import java.io.Serializable;
import java.util.Arrays;
import jneuralnet.core.NeuralNetwork;

/**
 * This class encapsulates a training pattern, i.e., a single row of input
 * and output vectors.
 *
 * @author Ragha
 * @version 1.0
 */
public class TrainingPattern implements Serializable
{
    private static final long serialVersionUID = -7830656492384574154L;

    // The input and output vectors...
    private Double inputData[], outputData[];
    
    public TrainingPattern() {
        
    }

    /**
     * Constructs a training pattern with the given input and output vectors.
     * @param inputData The input vector to be used.
     * @param outputData The output vector to be used.
     */
    public TrainingPattern(Double inputData[], Double outputData[])
    {
        setInputData(inputData);
        setOutputData(outputData);    
    }

    /**
     * Sets the input vector for this pattern.
     * @param inputData The input vector to be used.
     */
    public void setInputData(Double inputData[])
    {
        this.inputData = new Double[inputData.length];
        System.arraycopy(inputData, 0, this.inputData, 0, inputData.length);        
    }

    /**
     * 
     * @return The input vector used in this pattern.
     */
    public Double[] getInputData()
    {
        return inputData;
    }

    /**
     * Sets the output vector for this pattern.
     * @param outputData The output vector to be used.
     */
    public void setOutputData(Double outputData[])
    {
        this.outputData = new Double[outputData.length];
        System.arraycopy(outputData, 0, this.outputData, 0, outputData.length);
    }

    /**
     *
     * @return The output vector used in this pattern.
     */
    public Double[] getOutputData()
    {
        return outputData;
    }    
    
    @Override
    public String toString()
    {
        String s = "";
        if(inputData != null) {
            for(Double d : inputData)
                s += d.toString() +", ";
            s = s.substring(0, s.length() - 2);
        }

        s += "; ";
        if(outputData != null) {
            for(Double d : outputData)
                s += d.toString() +", ";
            s = s.substring(0, s.length() - 2);
        }

        return s;        
    }

    /**
     * Validates the compatibility of a neural network with this training
     * pattern. The training pattern is said to be compatible
     * with a neural network if:<br/><br/>
     *
     * 1) The length of the input vector is same as the number 
     * of inputs of the given neural network.<br/>
     * 2) The length of the output vector is same as the number
     * of outputs of the given neural network.
     *
     * @param nn The neural network to be validated.
     * @return true, if the above compatibility condition is satisfied.
     */
    boolean isValidate(NeuralNetwork nn)
    {
        if(inputData.length == nn.getNumInputs() &&
                outputData.length == nn.getNumOutputs())        
            return true;        
        else
            return false;        
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TrainingPattern other = (TrainingPattern) obj;
        if (this.inputData != other.inputData && (this.inputData == null || !Arrays.equals(this.inputData,other.inputData))) {
            return false;
        }
        if (this.outputData != other.outputData && (this.outputData == null || !Arrays.equals(this.outputData,other.outputData))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (this.inputData != null ? this.inputData.hashCode() : 0);
        hash = 31 * hash + (this.outputData != null ? this.outputData.hashCode() : 0);
        return hash;
    }
}