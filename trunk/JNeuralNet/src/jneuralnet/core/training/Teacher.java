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
import jneuralnet.core.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import jneuralnet.core.learning.AbstractLearningAlgo;
import jneuralnet.core.learning.BackPropagation;
import jneuralnet.util.AbstractSerializableBean;

/**
 * A teacher is responsible for training <code>NeuralNetwork</code> object.
 * It provides control over several training parameters and the
 * training thread. A teacher gets its training data via
 * {@link TrainingDataRepository}.
 *
 * <p>It is recommended that you do not instantiate this class, instead use
 * {@link NeuralNetwork#getTeacher() } method for obtaining the teacher object.
 *
 * <p><b>NOTE: If more than two teachers simultaneously train the 
 * neural network, it will lead to concurrency issues
 * and will result in un-expected behavior.</b>
 *
 * <p>This class extends <code>AbstractSerializableBean</code> for supporting
 * property change listeners and also serializes the listeners.
 *
 * @see AbstractSerializableBean
 * @see TrainingDataRepository
 * @see NeuralNetwork
 * @author Ragha
 * @version 1.0
 */
public class Teacher extends AbstractSerializableBean implements Serializable
{
    private static final long serialVersionUID = -2495499032798124438L;

    // The default learning algo to be used...
    private static AbstractLearningAlgo DEFAULT_ALGO = new BackPropagation();

    //possible types of stop errors...
    public static final int COST_ERROR = 0;
    public static final int PERCENTAGE_ERROR = 1;

    //The error type to be used for evaluating the error...
    private int stopErrorType;

    //the neural network to be trained...
    private NeuralNetwork neuralNetwork;

    //Learning algo to be trained with...
    private AbstractLearningAlgo learningAlgo;

    //transient because trainListener can reference swing components
    //(like groupLayout) and cause notserializable exception to be thrown...
    private transient List<TrainListenerAdapter> arrListeners;
    
    //Dont want to persist all that thread stuff in XML do you?
    private transient TrainingThread trainerDelegate;

    //Training data repository associated with this teacher...
    private TrainingDataRepository trainingDataRepository;

    /**
     *
     * @return The training data repository associated with this teacher...
     * @see TrainingDataRepository
     */
    public TrainingDataRepository getTrainingDataRepository()
    {
        return trainingDataRepository;
    }
        
    /**
     * Creates a teacher for the given neural network. A Null value is not
     * permitted. This constructor is invoked automatically whenever a neural
     * network is created and you normally never have to
     * invoke this constructor. You typically access the teacher for a neural
     * network using {@link NeuralNetwork#getTeacher() } method.
     *
     * <p>By default, {@link BackPropagation } algorithm is used for learning.     *
     *
     * @param net The neural network whose teacher is to be created.
     * @throws java.lang.NullPointerException If a null argument is passed.
     * @throws java.lang.IllegalArgumentException If the neural network
     * already has an associated teacher.
     *
     * @see NeuralNetwork
     */
    public Teacher(NeuralNetwork net) throws NullPointerException, IllegalArgumentException
    {
        if(net.getTeacher() != null) {
            throw new IllegalArgumentException("The neural network already has " +
                    "an associated teacher...");
        }
        else if(net == null) {
            throw new NullPointerException("Neural Network cannot be null");
        }
        else
        {
            this.neuralNetwork = net;
            neuralNetwork.addPropertyChangeListener(new PropertyChangeListener() {

                public void propertyChange(PropertyChangeEvent evt) {
                    if(evt.getPropertyName().equals("inputPreprocessor"))
                        trainingDataRepository.setIsInputDataDirty(true);
                    else if(evt.getPropertyName().equals("outputPreprocessor"))
                        trainingDataRepository.setIsOutputDataDirty(true);
                }
            });

            arrListeners = new ArrayList<TrainListenerAdapter>();
            trainerDelegate = new TrainingThread();
            trainingDataRepository = new TrainingDataRepository(net);
            learningAlgo = DEFAULT_ALGO;
            stopErrorType = COST_ERROR;
        }
    }
    
    /**
     * sets the learning algo to be used by the teacher. A null value is not
     * permissible.     
     * 
     * @param learningAlgo The learning algo to be used.
     * @throws NullPointerException If a null argument is used...
     */
    public void setLearningAlgo(AbstractLearningAlgo learningAlgo) throws NullPointerException
    {
        if(learningAlgo != null)
            set("learningAlgo", learningAlgo);
        else
            throw new NullPointerException("Argument cannot be null");
    }

    /**
     *
     * @return The algo used by the teacher
     */
    public AbstractLearningAlgo getLearningAlgo()
    {
        return learningAlgo;
    }

    /**
     * Returns the status of the trainer thread.
     * @return true, if the teacher is currently traininhg the network.
     */
    public boolean isTraining()
    {
        return trainerDelegate.isTraining;
    }

    /**
     * Returns the status of the trainer thread.
     * @return true, if the training thread is paused.
     */
    public boolean isPaused()
    {
        return trainerDelegate.isPaused;
    }

    /**
     * Returns the status of the trainer thread.
     * @return true, if the training thread is stopped.
     */
    public boolean isStopped()
    {
        return trainerDelegate.isStopped;
    }
    
    /**
     * Starts the training thread with the set parameters. The training
     * thread can be controlled using {@link #pauseTraining() },
     * {@link #resumeTraining()}, {@link #stopTraining() } methods.
     *
     * <p>The current activity of the trainer thread can be further inquired by
     * {@link #isPaused() }, {@link #isStopped() }, {@link #isTraining() }
     * methods.
     *
     * <p>Notifications regarding the training process can be setup using
     * {@link #addTrainListener(jneuralnet.core.training.TrainListenerAdapter) addTrainListener(...)}
     * method.
     *
     * @param stopError The error below which the training is to be halted.
     * @param minCycles The minimum number of epochs to be run for training.
     * @param maxCycles The maximum no of epochs to be run for training,
     * a value of -1 indicates indefinite number of cycles until
     * convergence is acheived.
     * @param stopErrorType The error type to be used of the stop error.
     * @throws IllegalArgumentException If minCycles and maxCycles is an absurd value...
     * @throws IllegalStateException If the associated training data repository
     * dosent have any training data or if no learning algorithm is set...
     *
     * @see TrainListener
     * @see TrainingDataRepository
     * @see AbstractLearningAlgo
     */
    public void startTraining(Double stopError, long minCycles,
            long maxCycles, int stopErrorType)
            throws IllegalStateException, IllegalArgumentException
    {
        if(isTraining())
            return;

        //Thread has completed execution or has been stopped
        //Re create thread object so that it can be re run...
        if(trainerDelegate.isStopped)
            trainerDelegate = new TrainingThread();

        trainerDelegate.stopError = stopError;
        trainerDelegate.maxCycles = maxCycles;
        trainerDelegate.minCycles = minCycles;

        if(stopErrorType == COST_ERROR || stopErrorType == PERCENTAGE_ERROR)
            this.stopErrorType = stopErrorType;
        else
            throw new IllegalArgumentException("Invalid stop error type");

        if(trainingDataRepository == null)
            throw new IllegalStateException("Training data repository is not set");
        if(trainingDataRepository == null)
            throw new IllegalStateException("Training data repository is empty");
        else if(trainingDataRepository.getProcessedTrainingSet() == null)
            throw new IllegalStateException("No training data found");
        else if(trainingDataRepository.getProcessedTrainingSet().getPatternSetSize() == 0)
            throw new IllegalStateException("No training data found");
        else if(learningAlgo == null)
            throw new IllegalStateException("No learning algorithm set...");
        else if(minCycles <= 0)
            throw new IllegalArgumentException("minCycles atleast has to be 1");
        else if(maxCycles <=0 && maxCycles != -1)
            throw new IllegalArgumentException("maxCycles has to be > 0 or equal to -1");
        else        
        {
            trainerDelegate.cycleCount = 0;
            trainerDelegate.isStopped = false;
            trainerDelegate.isPaused = false;            
            trainerDelegate.start();
        }        
    }    

    /**
     * Stops the training thread, if it is running.
     */
    public void stopTraining()
    {
        trainerDelegate.stopTraining();
    }

    /**
     * Resumes a paused training thread.
     */
    public void resumeTraining()
    {
        trainerDelegate.resumeTraining();
    }

    /**
     * Pauses the training thread. This method waits till the thread is
     * actually paused.
     */
    @SuppressWarnings("empty-statement")
    public void pauseTraining()
    {
        trainerDelegate.pauseTraining();
        while(!trainerDelegate.isPauseCompleted);
    }    

    /**
     * Adds the specified TrainingListener to receive training status
     * notifications from the training thread. If listener is null,
     * no exception is thrown and no action is performed.
     * 
     * @param listener The listener to be used for notifications.
     * @see TrainListener
     */
    public void addTrainListener(TrainListenerAdapter listener)
    {
        if(arrListeners == null)
            arrListeners = new ArrayList<TrainListenerAdapter>();
        if(listener != null)
            arrListeners.add(listener);
    }

    /**
     * Returns true if the object exists and is removed...
     * @param listener The listener object to be removed,
     * @return true, if the listener is removed.
     */
    public boolean removeTrainListener(TrainListenerAdapter listener)
    {
        return arrListeners.remove(listener);
    }

    /**
     * Removes the listener at 'index'. If index is out of bounds
     * then this method simply returns.
     * @param index The index at which listener is to be removed.
     */
    public void removeTrainListener(int index)
    {
        if(index >= 0 && index < arrListeners.size())
            arrListeners.remove(index);
    }

    /**
     * Checks if the listener l is registered.
     * @param l The listener instance to be checked.
     * @return true, if the listener is registered.
     */
    public boolean containsTrainListener(TrainListenerAdapter l)
    {
        return arrListeners.contains(l);
    }

    /**
     * Removes all the registered listeners for the list.
     */
    public void removeAllTrainListeners()
    {
        arrListeners.clear();
    }

    /**
     *
     * @return The neural network associated with this teacher...
     */
    public NeuralNetwork getNeuralNet()
    {
        return neuralNetwork;
    }

    /**
     * The training thread for this teacher. This class handles thread's 
     * start, stop, pause and resumes...
     */
    private class TrainingThread extends Thread
    {
        private long cycleCount;
        private long maxCycles;
        private long minCycles;
        
        private Double stopError;
        private Double trainingErrorOnCostFunction = 0.0;
        private Double validationErrorOnCostFunction = 0.0;
        private Double trainingErrorPercent = 0.0;
        private Double validationErrorPercent = 0.0;

        //used to control thread...
        private volatile boolean isStopped = false;
        private volatile boolean isPaused = false;
        private volatile boolean isTraining = false;
        private volatile boolean isPauseCompleted = false;
                
        /**
         * Provides batch training based on set parameters
         */
        @Override
        public void run()
        {
            isTraining = true;
            TrainingSet trainData = trainingDataRepository.getProcessedTrainingSet();
            TrainingSet validationData = trainingDataRepository.getProcessedValidationSet();

            while(true)
            {
                trainingErrorOnCostFunction = 0.0;
                trainingErrorPercent = 0.0;

                trainData.shuffle();
                for(TrainingPattern tp : trainData.getTrainingPatterns())
                {
                    Double actualOutput[] = neuralNetwork
                            .getOutputOnPreprocessedData(tp.getInputData());                    
                    learningAlgo.trainNet(neuralNetwork, actualOutput, tp.getOutputData());
                    trainingErrorOnCostFunction += learningAlgo.getCostFunction()
                            .getErrorValue(tp.getOutputData(), actualOutput);
                    trainingErrorPercent = getErrorPercent(actualOutput, tp.getOutputData());
                }
                trainingErrorOnCostFunction /= trainData.getPatternSetSize();
                trainingErrorOnCostFunction /= 2;

                validationErrorOnCostFunction = 0.0;
                validationErrorPercent = 0.0;
                if(validationData != null)
                {
                    if(validationData.getPatternSetSize() != 0)
                    {
                        validationData.shuffle();
                        for(TrainingPattern tp : validationData.getTrainingPatterns())
                        {
                            Double[] actualOutput = neuralNetwork
                                    .getOutputOnPreprocessedData(tp.getInputData());
                            validationErrorOnCostFunction += learningAlgo.getCostFunction()
                                    .getErrorValue(tp.getOutputData(), actualOutput);
                            validationErrorPercent = getErrorPercent(actualOutput, tp.getOutputData());
                        }
                        validationErrorOnCostFunction /= validationData.getPatternSetSize();
                        validationErrorOnCostFunction /= 2;
                    }
                    else {
                        validationErrorOnCostFunction = trainingErrorOnCostFunction;
                        validationErrorPercent = trainingErrorPercent;
                    }
                }
                else {
                    validationErrorOnCostFunction = trainingErrorOnCostFunction;
                    validationErrorPercent = trainingErrorPercent;
                }

                cycleCount ++;
                if(arrListeners.size() > 0)
                {
                    for(TrainListenerAdapter listener : arrListeners)
                    {   
                        listener.trainCyclePerformed(cycleCount,
                                trainingErrorOnCostFunction,
                                validationErrorOnCostFunction,
                                trainingErrorPercent, validationErrorPercent);
                    }
                }

                if(isStopped)
                {
                    isStopped = false;
                    break;
                }

                synchronized(this)
                {                    
                    try
                    {
                        isPauseCompleted = true;
                        isTraining = false;

                        //while(), instead of if() is used to handle spurious
                        //wakeup's...see SUN's Thread documentation for more info...
                        while(isPaused)
                            wait();
                        
                        isPauseCompleted = false;
                    }
                    catch(Exception e){}
                }

                if(maxCycles != -1)
                if(cycleCount == maxCycles)
                    break;

                //Loop termiation condition on success...
                //while ensuring minCycles...
                if(stopErrorType == COST_ERROR) {
                    if(trainingErrorOnCostFunction <= stopError && cycleCount >= minCycles)
                        break;
                }
                else {
                    if(trainingErrorPercent <= stopError && cycleCount >= minCycles)
                        break;
                }
            }

            for(TrainListenerAdapter listener : arrListeners)
                listener.trainingCompleted(trainingErrorOnCostFunction);

            isStopped = true;
            isTraining = false;
        }

        //reduce obj creation...
        double percent;

        /**
         * Calculates the percentage error between guessed and actual ouput.
         * @param actual The output guessed by the nwural network.
         * @param desired The required output
         * @return Average percentage error.
         */
        private Double getErrorPercent(Double actual[], Double desired[])
        {
            percent = 0.0;
            for(int i=0; i<actual.length; i++)
            {
                if(desired[i] == actual[i])
                    percent += 0;
                else if(desired[i] == 0 && actual[i] != 0)
                    percent += actual[i] * 100;
                else if(actual[i] == 0 && desired[i] != 0)
                    percent += desired[i] * 100;
                else if(desired[i] > actual[i])
                    percent += Math.abs( ((desired[i] - actual[i]) / actual[i]) * 100 );
                else
                    percent += Math.abs( ((desired[i] - actual[i]) / desired[i]) * 100 );
            }            
            percent /= actual.length;
            return percent;
        }
        
        public synchronized void stopTraining()
        {
            isStopped = true;
            if(isPaused)
                resumeTraining();
        }

        public synchronized void resumeTraining()
        {
            isPaused = false;
            notify();
        }

        public synchronized void pauseTraining()
        {
            isPaused = true;
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
        final Teacher other = (Teacher) obj;
        if (this.neuralNetwork != other.neuralNetwork && (this.neuralNetwork == null || !this.neuralNetwork.equals(other.neuralNetwork))) {
            return false;
        }
        if (this.learningAlgo != other.learningAlgo && (this.learningAlgo == null || !this.learningAlgo.equals(other.learningAlgo))) {
            return false;
        }
        if (this.trainingDataRepository != other.trainingDataRepository && (this.trainingDataRepository == null || !this.trainingDataRepository.equals(other.trainingDataRepository))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + (this.neuralNetwork != null ? this.neuralNetwork.hashCode() : 0);
        hash = 79 * hash + (this.learningAlgo != null ? this.learningAlgo.hashCode() : 0);
        hash = 79 * hash + (this.trainingDataRepository != null ? this.trainingDataRepository.hashCode() : 0);
        return hash;
    }
}
