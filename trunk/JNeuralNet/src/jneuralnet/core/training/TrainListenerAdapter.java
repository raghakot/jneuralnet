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
 * An abstract adapter class for receiving training notifications. 
 * The methods in this class are empty. This class exists as convenience 
 * for creating listener objects.
 * 
 * This abstract class defines null methods for them all, 
 * so you can only have to define methods you care about.)
 * Create a listener object using the extended class and 
 * then register it with a component using the teacher using
 * {@link Teacher#addTrainListener(jneuralnet.core.training.TrainListenerAdapter) teacher.addTrainListener(...)} method.
 *
 * @see TrainListener
 * @see Teacher
 * @author Ragha
 * @version 1.0
 */
public class TrainListenerAdapter implements TrainListener
{
    public void trainingCompleted(Double finalError)
    {
        
    }

    public void trainCyclePerformed(Long epoch,
            Double trainingErrorOnCostFunction,
            Double validationErrorOnCostFunction,
            Double trainingErrorPercent, Double validationErrorPercent)
    {

    }
}
