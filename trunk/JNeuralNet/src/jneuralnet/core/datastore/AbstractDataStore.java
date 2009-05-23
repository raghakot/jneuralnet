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

package jneuralnet.core.datastore;

import java.io.Serializable;
import jneuralnet.core.Pluggable;
import jneuralnet.core.training.TrainingPattern;
import jneuralnet.core.training.TrainingSet;

/**
 * Represents the storage medium from where training data can be read
 * or saved. Newer data stores can be defined by extending this class.
 *
 * <p> Also associated with the data store is an optional
 * {@link #getConfigPanel() } method. You can return a configurable panel
 * for setting the parameters for the data store.
 *
 * <p> This class extends the <tt>Pluggable</tt> class making it
 * self descriptive in nature.
 *
 * <P>TODO: define property sheets to set parameters.
 
 * @author Ragha
 * @see Pluggable
 * @see FileDataStore
 * @see MySQLDataStore
 * @version 1.0
 */
public abstract class AbstractDataStore extends Pluggable implements Serializable
{
    /**
     * Loads the TrainingSet from the storage medium...
     * @return The loaded TrainingSet.
     * @throws java.lang.Exception Throw any exception deemed necessary
     *
     * @see TrainingPattern
     * @see TrainingSet     
     */
    public abstract TrainingSet loadTrainingSet() throws Exception;

    /**
     * Saves the training data to the storage medium.
     * <p>Individual TrainingPatterns within the training set can 
     * be accessed using {@link TrainingSet#getTrainingPatterns() } method.
     *
     * @param ts The trainingSet to be saved.
     * @throws java.lang.Exception Throw any exception deemed necessary.
     *
     * @see TrainingPattern
     * @see TrainingSet
     */
    public abstract void saveTrainingSet(TrainingSet ts) throws Exception;
    
    /**
     * Optinal, can be used to provide a GUI for
     * configiguring paramaters of the data store.
     * 
     * @return The configuration panel.
     * @see DataStoreConfigPanel
     */
    public abstract DataStoreConfigPanel getConfigPanel();    
}