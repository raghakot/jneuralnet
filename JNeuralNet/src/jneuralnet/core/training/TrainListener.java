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

/**
 * The listener interface for receiving training thread status
 * (on every epoch and finish) with a {@link Teacher}.
 *
 * <p>The class that is interested in processing events must either
 * implement this interface (and all the methods it contains) or
 * extend the abstract {@link TrainListenerAdapter} class
 * (overriding only the methods of interest).
 *
 * <p>The listener object created from that class is then 
 * registered with the teacher using
 * {@link Teacher#addTrainListener(jneuralnet.core.training.TrainListenerAdapter) addTrainListener(...)} method.
 *
 * @see TrainListenerAdapter
 * @see Teacher
 */
public interface TrainListener
{
    /**
     * Once registered with Teacher's object,this method is called by the
     * Teacher object on every training iteration.
     *
     * <p>This overidden method can be used to enquire the training status.
     * The ValidationError is equal to  trainingError
     * if validation data is not set in the train data repository associated
     * with the teacher.
     *
     * <p>The error on cost function is computed using the
     * LearningAlgo.getCostFunction() class.
     *
     * @param epoch The current epoch...
     * @param trainingErrorOnCostFunction The training error computed by using the cost function.
     * @param validationErrorOnCostFunction The validation error computed by using the cost function.
     * @param trainingErrorPercent The training error in percentage form.
     * @param validationErrorPercent The validation error in percentage form.
     */
    public void trainCyclePerformed(Long epoch, 
            Double trainingErrorOnCostFunction,
            Double validationErrorOnCostFunction,
            Double trainingErrorPercent, Double validationErrorPercent);
    

    /**
     * Once registered with Teacher's object, this method is called
     * on completion of training.
     *
     * @param finalError The final error value when training ends.
     */
    public void trainingCompleted(Double finalError);
}
