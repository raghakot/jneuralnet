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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import jneuralnet.core.NeuralNetwork;
import jneuralnet.core.preprocessor.AbstractInputPreprocessor;
import jneuralnet.core.preprocessor.AbstractOutputPreprocessor;
import jneuralnet.util.AbstractSerializableBean;

/**
 * A central training data repository to manage the available data. It enables 
 * you to cluster all the available training data into random clusters of 
 * training, validation and test data as per the specified percentages.
 *
 * <p>An instance of this class is automatically created by the teacher and
 * you can retreive it using {@link Teacher#getTrainingDataRepository() }
 * method. The associated teacher can be retreived using {@link NeuralNetwork#getTeacher() }
 * method. This class is not instantiable for the same reason.
 *
 * <p>This class also manages lazy preprocessing of data as and when required
 * through one of the getPreprocessedXXXSet() methods.
 *
 * <p>This class extends <code>AbstractSerializableBean</code>
 * for serialization and property change support.
 *
 * @see AbstractSerializableBean
 * @see TrainingSet
 * @author Ragha
 * @version 1.0
 */
public class TrainingDataRepository extends AbstractSerializableBean implements Serializable
{
    private static final long serialVersionUID = -8011520886809581323L;

    //The clustered data sets...
    private TrainingSet trainingSet;
    private TrainingSet validationSet;
    private TrainingSet testSet;
    //The total available data sets...
    private TrainingSet totalAvailableSet;

    //The preprocessed sets...
    private TrainingSet preprocessedTrainingSet;
    private TrainingSet preprocessedValidationSet;
    private TrainingSet preprocessedTestSet;

    //The neural network whose preprocessors are to be used...
    private NeuralNetwork nn;
    
    
    //These flags are used so that preprocessing of data can be done lazily...
    private boolean isTrainingInputDataDirty = false;
    private boolean isTrainingOutputDataDirty = false;
    private boolean isValidationInputDataDirty = false;
    private boolean isValidationOutputDataDirty = false;
    private boolean isTestInputDataDirty = false;
    private boolean isTestOutputDataDirty = false;

   
    //These flags are used to compute the required settings
    //lazily when required...
    private boolean isInputPreprocessorComputed = false;
    private boolean isOutputPreprocessorComputed = false;

    //The clustering percentages to be used...
    private float trainingSetPercent, validationSetPercent, testSetPercent;

    /**
     * Creates a training data repository with the give neural network
     * as the preprocessor provider. This class is automatically instantiated
     * by the teacher object. This constructor is package private
     * the same reason.
     *
     * <p>It Uses a default training set, validation set, test set percentages
     * of 60, 25, 15 percent respectively.
     *
     * @see NeuralNetwork
     */
    TrainingDataRepository(NeuralNetwork nn)
    {
        this.totalAvailableSet = new TrainingSet();
        this.trainingSet = new TrainingSet();
        this.validationSet = new TrainingSet();
        this.testSet = new TrainingSet();

        preprocessedTrainingSet = new TrainingSet();
        preprocessedValidationSet = new TrainingSet();
        preprocessedTestSet = new TrainingSet();

        this.trainingSetPercent = 60;
        this.validationSetPercent = 25;
        this.testSetPercent = 15;
        this.nn = nn;

        //whenever totalAvailableSet is changed...redistribute the data sets...
        addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                if(evt.getPropertyName().equals("totalAvailableSet"))
                    distributeSets();
            }
        });
    }

    /**
     * Sets the total available data into the repository...
     * Also computes the required settings for the data preprocessors
     * if set on the neural network object used to construct this instance.
     * 
     * @param totalAvailableSet The training set to be used as the total data...
     * @throws java.lang.IllegalArgumentException If ts and
     * neural network are incompatible...
     */
    public void setTotalAvailableSet(TrainingSet totalAvailableSet)
            throws IllegalArgumentException
    {        
        if(!totalAvailableSet.validate(nn)) {
            throw new IllegalArgumentException("TrainingData," +
                    " NeuralNetwork mismatch");
        }
        set("totalAvailableSet", totalAvailableSet);
    }

    /**
     * Randomly distributes the total available data among the data sets
     * training data, validation data and test data with the
     * percentages as specified.
     *
     * @param trainSetPercent A number between 0 - 100. Indicates the
     * percentage data allocation to training set.
     *
     * @param validationSetPercent A number between 0 - 100. Indicates the
     * percentage data allocation to validation set.
     * 
     * @param testSetPercent A number between 0 - 100. Indicates the
     * percentage data allocation to test set.
     *
     * @throws IllegalArgumentException If sum of the percentages
     * is > 100 or if any of the percentages are negative valued.
     */
    public void randomlyDistributeSets(float trainSetPercent,
            float validationSetPercent, float testSetPercent)
            throws IllegalArgumentException
    {
        if(trainSetPercent < 0 || validationSetPercent < 0 || testSetPercent < 0)
            throw new IllegalArgumentException("percentage value cannot be negative");

        if(trainSetPercent + validationSetPercent + testSetPercent > 100.0)
            throw new IllegalArgumentException("Sum of percentages cannot exceed 100");
        
        this.trainingSetPercent = trainSetPercent;
        this.validationSetPercent = validationSetPercent;
        this.testSetPercent = testSetPercent;
        distributeSets();
    }

    /**
     * Distributes the total available data randomly across the
     * training, validation and test sets according to the set percentages...
     */
    private void distributeSets()
    {
        trainingSet.getTrainingPatterns().clear();
        validationSet.getTrainingPatterns().clear();
        testSet.getTrainingPatterns().clear();
        
        if(totalAvailableSet == null)
            return;

        int numPatterns = totalAvailableSet.getPatternSetSize();
        int trainingSetNos = (int) (trainingSetPercent * numPatterns * 1.0f / 100f);
        int validationSetNos = (int) (validationSetPercent * numPatterns * 1.0f / 100f);
        int testSetNos = (int) (testSetPercent * numPatterns * 1.0f / 100f);
        
        if(numPatterns > 0)
        {
            trainingSet.getTrainingPatterns().clear();
            validationSet.getTrainingPatterns().clear();
            testSet.getTrainingPatterns().clear();

            TrainingSet temp = new TrainingSet();
            copy(totalAvailableSet, temp);
            temp.shuffle();            

            //randomly select training set data...
            for(int index=0; index<trainingSetNos; index++)
                trainingSet.add(temp.getTrainingPattern(index));            
            
            //randomly select validation set data...
            for(int index=trainingSetNos; index<trainingSetNos
                    + validationSetNos; index++)
                validationSet.add(temp.getTrainingPattern(index));            
            
            //randomly select test set data...
            for(int index=trainingSetNos + validationSetNos;
            index<trainingSetNos + validationSetNos + testSetNos; index++)            
                testSet.add(temp.getTrainingPattern(index));            

            setIsInputDataDirty(true);
            setIsOutputDataDirty(true);
        }
    }

    /**
     * Syncs source and dest training sets...
     * @param source The source training set to be used.
     * @param destination The training set to be synchronised.
     */
    private void copy(TrainingSet source, TrainingSet destination)
    {
        destination.getTrainingPatterns().clear();
        for(TrainingPattern tp : source.getTrainingPatterns()) {
            destination.add(tp);
        }
    }

    /**
     * Called internally by the teacher to notify an
     * input preprocessor change.
     * @param isInputDataDirty true, indicates an input preprocessor change.
     */
    void setIsInputDataDirty(boolean isInputDataDirty) {
        isTrainingInputDataDirty = true;        
        isValidationInputDataDirty = true;        
        isTestInputDataDirty = true;
        isInputPreprocessorComputed = false;
    }

    /**
     * Called internally by the teacher to notify an
     * output preprocessor change.
     * @param isOutputDataDirty true, indicates an output preprocessor change.
     */
    void setIsOutputDataDirty(boolean isOutputDataDirty) {        
        isTrainingOutputDataDirty = true;        
        isValidationOutputDataDirty = true;        
        isTestOutputDataDirty = true;
        isOutputPreprocessorComputed = false;
    }    

    /**
     *
     * @return The percentage used for test set.
     */
    public float getTestSetPercent() {
        return testSetPercent;
    }

    /**
     *
     * @return The percentage used for training set.
     */
    public float getTrainingSetPercent() {
        return trainingSetPercent;
    }

    /**
     *
     * @return The percentage used for validation set.
     */
    public float getValidationSetPercent() {
        return validationSetPercent;
    }    

    /**
     * Gives the most updated copy of processed training set. i.e., it
     * lazily processes data if required, this is triggered if the
     * preprocessor is modified on the neural network
     * or total training data is changed.
     *
     * @return The preprocessed training set.
     */
    public TrainingSet getProcessedTrainingSet()
    {
        AbstractInputPreprocessor ip = nn.getInputPreprocessor();
        AbstractOutputPreprocessor op = nn.getOutputPreprocessor();

        if(isTrainingInputDataDirty || isTrainingOutputDataDirty) {
            copy(trainingSet, preprocessedTrainingSet);
            recomputeSettingsIfRequired();
        }
            

        if(isTrainingInputDataDirty) {
            if(ip != null)                
                preprocessedTrainingSet.process(ip);               
            isTrainingInputDataDirty = false;
        }

        if(isTrainingOutputDataDirty) {
            if(op != null)
                preprocessedTrainingSet.process(op);
            isTrainingOutputDataDirty = false;
        }

        return preprocessedTrainingSet;
    }

    /**
     * Gives the most updated copy of processed validation set. ie,
     * lazily processes data if required, this is triggered if the
     * preprocessor is modified on the neural network
     * or total training data is changed.
     *
     * @return The preprocessed validation set.
     */
    public TrainingSet getProcessedValidationSet()
    {
        AbstractInputPreprocessor ip = nn.getInputPreprocessor();
        AbstractOutputPreprocessor op = nn.getOutputPreprocessor();

        if(isValidationInputDataDirty || isValidationOutputDataDirty) {
            copy(validationSet, preprocessedValidationSet);
            recomputeSettingsIfRequired();
        }
        
        if(isValidationInputDataDirty) {
            if(ip != null)
                preprocessedValidationSet.process(ip);
            isValidationInputDataDirty = false;
        }

        if(isValidationOutputDataDirty) {
            if(op != null)
                preprocessedValidationSet.process(op);
            isValidationOutputDataDirty = false;
        }

        return preprocessedValidationSet;
    }

    /**
     * Gives the most updated copy of processed test set. ie,
     * lazily processes data if required,  this is triggered if the
     * preprocessor is modified on the neural network
     * or total training data is changed.
     *
     * @return The preprocessed test set.
     */
    public TrainingSet getProcessedTestSet()
    {
        AbstractInputPreprocessor ip = nn.getInputPreprocessor();
        AbstractOutputPreprocessor op = nn.getOutputPreprocessor();

        if(isTestInputDataDirty || isTestOutputDataDirty) {
            copy(testSet, preprocessedTestSet);
            recomputeSettingsIfRequired();
        }
        
        if(isTestInputDataDirty) {
            if(ip != null)
                preprocessedTestSet.process(ip);
            isTestInputDataDirty = false;
        }

        if(isTestOutputDataDirty) {
            if(op != null)
                preprocessedTestSet.process(op);
            isTestOutputDataDirty = false;
        }

        return preprocessedTestSet;
    }

    /**
     * recomputes the settings required, this method is typically triggered
     * whenever an input preprocessor or total training set is changed.
     */
    private void recomputeSettingsIfRequired()
    {
        AbstractInputPreprocessor ip = nn.getInputPreprocessor();
        AbstractOutputPreprocessor op = nn.getOutputPreprocessor();

        if(!isInputPreprocessorComputed) {
            if(ip != null)
            ip.computeSettings(totalAvailableSet);
            isInputPreprocessorComputed = true;
        }
        if(!isOutputPreprocessorComputed) {
            op.computeSettings(totalAvailableSet);
            isOutputPreprocessorComputed = true;
        }
    }

    /**
     *
     * @return The num of training patterns available in the
     * total available data set.
     */
    public Integer getTotalAvailablePatternSize()
    {
        return totalAvailableSet.getPatternSetSize();
    }

    /**
     *
     * @return The num of training patterns available in the training set.
     */
    public Integer getTrainingDataPatternSize()
    {
        return trainingSet.getPatternSetSize();
    }

    /**
     *
     * @return The num of training patterns available in the validation set.
     */
    public Integer getValidationDataPatternSize()
    {
        return validationSet.getPatternSetSize();
    }

    /**
     *
     * @return The num of training patterns available in the test set.
     */
    public Integer getTestDataPatternSize()
    {
        return testSet.getPatternSetSize();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TrainingDataRepository other = (TrainingDataRepository) obj;
        if (this.trainingSet != other.trainingSet && (this.trainingSet == null || !this.trainingSet.equals(other.trainingSet))) {
            return false;
        }
        if (this.validationSet != other.validationSet && (this.validationSet == null || !this.validationSet.equals(other.validationSet))) {
            return false;
        }
        if (this.testSet != other.testSet && (this.testSet == null || !this.testSet.equals(other.testSet))) {
            return false;
        }
        if (this.totalAvailableSet != other.totalAvailableSet && (this.totalAvailableSet == null || !this.totalAvailableSet.equals(other.totalAvailableSet))) {
            return false;
        }
        if (this.preprocessedTrainingSet != other.preprocessedTrainingSet && (this.preprocessedTrainingSet == null || !this.preprocessedTrainingSet.equals(other.preprocessedTrainingSet))) {
            return false;
        }
        if (this.preprocessedValidationSet != other.preprocessedValidationSet && (this.preprocessedValidationSet == null || !this.preprocessedValidationSet.equals(other.preprocessedValidationSet))) {
            return false;
        }
        if (this.preprocessedTestSet != other.preprocessedTestSet && (this.preprocessedTestSet == null || !this.preprocessedTestSet.equals(other.preprocessedTestSet))) {
            return false;
        }
        if (this.nn != other.nn && (this.nn == null || !this.nn.equals(other.nn))) {
            return false;
        }
        if (this.trainingSetPercent != other.trainingSetPercent) {
            return false;
        }
        if (this.validationSetPercent != other.validationSetPercent) {
            return false;
        }
        if (this.testSetPercent != other.testSetPercent) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + (this.trainingSet != null ? this.trainingSet.hashCode() : 0);
        hash = 79 * hash + (this.validationSet != null ? this.validationSet.hashCode() : 0);
        hash = 79 * hash + (this.testSet != null ? this.testSet.hashCode() : 0);
        hash = 79 * hash + (this.totalAvailableSet != null ? this.totalAvailableSet.hashCode() : 0);
        hash = 79 * hash + (this.preprocessedTrainingSet != null ? this.preprocessedTrainingSet.hashCode() : 0);
        hash = 79 * hash + (this.preprocessedValidationSet != null ? this.preprocessedValidationSet.hashCode() : 0);
        hash = 79 * hash + (this.preprocessedTestSet != null ? this.preprocessedTestSet.hashCode() : 0);
        hash = 79 * hash + (this.nn != null ? this.nn.hashCode() : 0);
        hash = 79 * hash + Float.floatToIntBits(this.trainingSetPercent);
        hash = 79 * hash + Float.floatToIntBits(this.validationSetPercent);
        hash = 79 * hash + Float.floatToIntBits(this.testSetPercent);
        return hash;
    }
}
