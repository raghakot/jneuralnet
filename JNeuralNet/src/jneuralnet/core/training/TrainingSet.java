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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import jneuralnet.core.NeuralNetwork;
import jneuralnet.core.datastore.AbstractDataStore;
import jneuralnet.core.preprocessor.AbstractInputPreprocessor;
import jneuralnet.core.preprocessor.AbstractOutputPreprocessor;

/**
 * This class encapsulates a list of training patterns. It can therefore
 * be used to represent a complete data set.
 *
 * <p>A training set is loaded or saved to an external storage medium using
 * {@link AbstractDataStore} instance.
 *
 * @see TrainingPattern
 * @see AbstractDataStore
 * @author Ragha
 * @version 1.0
 */
public class TrainingSet implements Serializable
{
    private static final long serialVersionUID = -1020341507562170702L;

    //The training set...
    private List<TrainingPattern> patternSet;

    public TrainingSet() {
        patternSet = new ArrayList<TrainingPattern>();
    }

    /**
     * Adds a training data pattern
     */
    public void add(TrainingPattern pattern)
    {

        patternSet.add(pattern);
    }
    
    /**
     * ignores if invalid index value is provided
     */
    public void remove(int index)
    {
        if( index < patternSet.size() )
        {
            patternSet.remove(index);
        }
    }

    public void remove(Object o)
    {
        if(patternSet.contains(o))
        {
            patternSet.remove(o);
        }
    }
    
    public List<TrainingPattern> getTrainingPatterns()
    {
        return patternSet;
    }

    public Integer getPatternSetSize()
    {
        return patternSet.size();
    }
    
    /**     
     * returns corresponding inputset
     * throws IndexOutOfBoundsException on invalid index
     */
    public TrainingPattern getTrainingPattern(int index)
            throws IndexOutOfBoundsException
    {
        return patternSet.get(index);
    }

    /**
     * Validates compatibility with respect to NeuralNetwork passed and the
     * train data...ie, checks if num of input and output sets match with 
     * numInputs and numOutputs of the neural network
     */
    public boolean validate(NeuralNetwork nn)
    {
        boolean isDataValid = true;        
        
        for(TrainingPattern p : patternSet)
        {
            if(!p.isValidate(nn))
            {                
                isDataValid = false;
                break;
            }
        }
        
        return isDataValid;
    }
    
    /**
     * Shuffle's the data in a random order...
     */
    public void shuffle()
    {
        Collections.shuffle(patternSet);
    }

    /**
     * Processes the given set with the processor
     * @param ip
     */
    void process(AbstractInputPreprocessor ip) {
        for(int i=0; i<patternSet.size(); i++) {
            TrainingPattern tp = patternSet.get(i);
            tp.setInputData(ip.process(tp.getInputData()));
            patternSet.set(i, tp);
        }
    }

    /**
     * Processes the given set with the processor
     * @param op
     */
    void process(AbstractOutputPreprocessor op) {
        for(int i=0; i<patternSet.size(); i++) {
            TrainingPattern tp = patternSet.get(i);
            tp.setOutputData(op.process(tp.getOutputData()));
            patternSet.set(i, tp);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TrainingSet other = (TrainingSet) obj;
        if (this.patternSet != other.patternSet && (this.patternSet == null || !this.patternSet.equals(other.patternSet))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + (this.patternSet != null ? this.patternSet.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString()
    {
        String desc = "";
        for(TrainingPattern tp : patternSet)
            desc += tp.toString();
        return desc;
    }
}