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
 * Implements the popular sigmoid activation function. It implements the
 * <tt>AbstractActivation</tt> interface.
 *
 * <p> This function provides an output range of (0, 1)
 * 
 * @author Ragha
 * @see AbstractActivation
 * @version 1.0
 */
public class Sigmoid extends AbstractActivation
{
    private static final long serialVersionUID = 5835560542523384408L;

    /**
     * Represents sigmoid activation function. It is given by the
     * expression:
     *
     * <p><tt> 1 / (1 + e ^ (-x)) </tt>
     * 
     * @param input The input value to this function
     * @return The value returned by the function. The range of output
     * values is (0, 1)
     * 
     */
    public Double activation(Double input) 
    {
        return( 1.0 /(1.0 + Math.exp(-input)) );
    }

    /**
     * Represents the derivative of the sigmoid function. It is given
     * by the expression:
     *
     * <p><tt>sigmoid(x) * ( 1 - sigmoid(x) )</tt>, where sigmoid is defined
     * by <code>activation</code> method.
     *
     * @param input The input value to this function
     * @return The value returned by the function
     */
    public Double activationDerviative(Double input) 
    {
        return (activation(input) * (1 - activation(input)));
    }

    @Override
    public String getName()
    {
        return "Sigmoid";
    }

    @Override
    public String getDescription()
    {
        return "Implementation of sigmoid function.\n" +
                " This function is given by ' 1 / (1 + e ^ (-x)) '.\n" +
                " It provides an output range of (0, 1)";
    }

    @Override
    public String getAuthor() {
        return "Ragha";
    }
}
