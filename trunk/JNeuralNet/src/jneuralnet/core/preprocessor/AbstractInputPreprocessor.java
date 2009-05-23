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

package jneuralnet.core.preprocessor;

import java.io.Serializable;
import jneuralnet.core.NeuralNetwork;
import jneuralnet.core.Pluggable;
import jneuralnet.core.training.TrainingDataRepository;
import jneuralnet.core.training.TrainingSet;

/**
 * This class defines the base for all the input preprocessors.
 * An input preprocessor is used whenever an <i>input data</i> is
 * feed forwarded by the network.
 *
 * <p>Once setup to the neural network by a call to
 * {@link NeuralNetwork#setInputPreprocessor(jneuralnet.core.preprocessor.AbstractInputPreprocessor) setInputPreprocessor(...)}
 * method, the <code>TrainingDataRepository</code> automatically makes
 * necessary calls to the {@link #computeSettings(jneuralnet.core.training.TrainingSet) computeSettings(...)}
 * method whenever necessary.
 *
 * <p>{@link #process(java.lang.Double[]) process(...)} method must make
 * use the settings computed by the {@link #computeSettings(jneuralnet.core.training.TrainingSet) computeSettings(...)}
 * method, undesirable results may occur otherwise.
 *
 * <p> This class extends the <tt>Pluggable</tt> class making it
 * self descriptive in nature.
 *
 * @see TrainingDataRepository
 * @see Pluggable
 * @author Ragha
 * @version 1.0
 */
public abstract class AbstractInputPreprocessor extends Pluggable implements Serializable
{
    /**
     * Calculate the settings(averages, sum etc...) required for use in
     * {@link #process(java.lang.Double[]) process(...)} method.
     */
    public abstract void computeSettings(TrainingSet ts);

    /**
     * Override this method to specify how the input data
     * is to be preprocessed. This method must make use of the
     * settings computed by the {@link #computeSettings(jneuralnet.core.training.TrainingSet) computeSettings(...)}
     * method, undesirable results may occur otherwise.
     *
     * <p> This method is automatically called by the {@link TrainingDataRepository}
     * class whenever data is to be preprocessed.
     *
     * @param data The input vector pattern
     * @return The processed input data...     
     */
    public abstract Double[] process(Double data[]);    
}