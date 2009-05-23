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

package jneuralnet.core.activation;

/**
 * Implements the Logistic Sigmoid activation function. It implements the
 * <tt>AbstractActivation</tt> class.
 *
 * <p> This function provides an output range of (-1, 1).
 *
 * @author Ragha
 * @see AbstractActivation
 * @version 1.0
 */
public class LogisticSigmoid extends AbstractActivation
{
    private static final long serialVersionUID = 5281438296848650593L;

    /**
     * Represents logistic sigmoid activation function. It is given by the
     * expression:
     *
     * <p><tt> tanh(x) </tt>
     *
     * @param input The input value to this function
     * @return The value returned by the function. The range of output
     * values is (-1, 1)
     */
    public Double activation(Double input)
    {
        return Math.tanh(input);
    }

    /**
     * Represents the derivative of logistic sigmoid function. It is given
     * by the expression:
     *
     * <p><tt> 1 - (tanh(x))^2 </tt>
     *
     * @param input The input value to this function
     * @return The value returned by the function
     */
    public Double activationDerviative(Double input)
    {
        return 1 - (activation(input) * activation(input));
    }

    @Override
    public String getName()
    {
        return "Logistic Sigmoid";
    }

    @Override
    public String getDescription() {
        return "Implementation of logistic sigmoid function.\n" +
                " This function is given by ' Tanh(x) '.\n"
                + " It provides an output range of (-1, +1)";
    }

    @Override
    public String getAuthor() {
        return "Ragha";
    }
}
