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

import jneuralnet.core.learning.costfunction.SumOfSquaresError;
import jneuralnet.core.learning.costfunction.AbstractCostFunction;
import java.io.Serializable;
import jneuralnet.core.Pluggable;
import jneuralnet.core.NeuralNetwork;

/**
 * Provides the base for implementing a supervised learning algorithm.
 * Use this class only if you are going to implement an altogether
 * new algorithm. For implementing a back propagation variant such as
 * RProp and QuickProp use <code>AbstractBPBasedAlgo</code> class.
 *
 * <p>The learning algo you implement must honor the cost function
 * by using the {@link #getCostFunction() } method. For calculating the
 * error gradients you must use the given cost function. By default,
 * {@link SumOfSquaresError} is used as the cost function.
 *
 * <p> This class extends the <tt>Pluggable</tt> class making it
 * self descriptive in nature.
 *
 * @author Ragha
 * @version 1.0
 * @see Pluggable
 * @see AbstractCostFunction
 * @see AbstractBPBasedAlgo
 */
public abstract class AbstractLearningAlgo extends Pluggable implements Serializable
{
    private static final long serialVersionUID = 2009050218L;

    private static final AbstractCostFunction DEFAULT_FUNCTION = new SumOfSquaresError();

    protected AbstractLearningAlgo()
    {
        this.costFunction = DEFAULT_FUNCTION;
    }

    /**
     * Define how to train the neural network...
     */
    public abstract void trainNet(NeuralNetwork n,
            Double actualOutput[], Double expectedOutput[]);

    /**
     * Allows you to place a constraint on modifying cost function
     * by using {@link #setCostFunction(jneuralnet.core.learning.costfunction.AbstractCostFunction) setCostFunction(...)}
     * method.
     *
     * <p>This feature is particularly useful if you have a learning algo
     * which uses a modified cost function. In such cases you must return
     * false to indicate that the cost fucntion is not modifiable for this
     * particular learning algorithm...
     */
    public abstract boolean isCostFunctionModifiable();

    /**
     * The cost function associated with this algo...
     */
    protected AbstractCostFunction costFunction;

    /**
     * Sets the cost function to be used. A Null value is not permitted.
     * If null value is used, then it is simply ignored.
     *
     * <p>This parameter can only be set if cost function is modifiable.
     *
     * @param costFunction The cost function to be used.
     * @see #isCostFunctionModifiable() 
     */
    public void setCostFunction(AbstractCostFunction costFunction)
    {
        if(costFunction != null && isCostFunctionModifiable())
            this.costFunction = costFunction;            
    }

    /**
     * Return the cost function to be used. The cost function is also
     * termed as error function in some books.
     *
     * @return The cost function in use.
     */
    public AbstractCostFunction getCostFunction()
    {
        return costFunction;
    }    

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AbstractLearningAlgo other = (AbstractLearningAlgo) obj;
        if (this.costFunction != other.costFunction && (this.costFunction == null || !this.costFunction.equals(other.costFunction))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + (this.costFunction != null ? this.costFunction.hashCode() : 0);
        return hash;
    }
}
