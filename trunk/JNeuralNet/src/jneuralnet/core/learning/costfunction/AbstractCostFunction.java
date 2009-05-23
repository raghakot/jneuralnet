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

package jneuralnet.core.learning.costfunction;

import java.io.Serializable;
import jneuralnet.core.Pluggable;
import jneuralnet.core.learning.AbstractBPBasedAlgo;

/**
 * This class defines the cost function to be used with the learning algo.
 * The cost function is also termed as the error function in some of the
 * books.
 *
 * <p>The cost function of choice is problem dependent. 
 * It largely effects the convergence rate of the learning algorithm
 * and more importantly effects the generalization capabilities
 * of the neural network. for more details about cost function,
 * refer to:<br/> 
 * <pre> S. Haykin, Neural networks - A comprehensive foundation. </pre>
 *
 * <p> This class extends the <tt>Pluggable</tt> class making it
 * self descriptive in nature.
 *
 * @see Pluggable
 * @author Ragha
 * @version 1.0
 */
public abstract class AbstractCostFunction extends Pluggable implements Serializable
{
    /**
     * Defines the method for generating an error value between the desired
     * and the actual output vectors.
     *
     * <p>The error value thus generated is used to represent the overall
     * error of a neural network on a training pattern.
     * 
     * @param desired The desired output vector.
     * @param actual The guessed output of the network.
     * @return The error value between derired and actual output.
     * 
     * @see AbstractBPBasedAlgo
     */
    public abstract Double getErrorValue(Double desired[], Double actual[]);

    /**
     * The output delta is the partial derivative of the cost function
     * with respect to output weight connection divided by the input
     * throught the connection.
     *
     * <p>For an output neuron j, the delta is defined as partial
     * derivative of output of neuron j with the connection weight Wij
     * divided by Xj, where Xj is the input propogated throughthe connection.
     *
     * <p><b>NOTE: Ignore any constant values found while taking derivatives.
     * The framework substitutes constant values by learning rate.</b>
     *
     * @param desired The desired output of an output neuron in the output layer
     * @param actual The actual output of an output neuron in the output layer.
     * @param derivativeOfOutputWithWeight The partial derivative of
     * output with respect to weight.
     * @return The derivative value...
     */
    public abstract Double getOutputDelta(Double desired, Double actual,
            Double derivativeOfOutputWithWeight);
}
