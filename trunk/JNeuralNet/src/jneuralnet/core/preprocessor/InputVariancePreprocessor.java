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

import java.util.Arrays;
import jneuralnet.core.training.TrainingPattern;
import jneuralnet.core.training.TrainingSet;
import jneuralnet.util.MathUtil;

/**
 * Normalizes the input data to be approximately centered around zero 
 * (evenly distributed).
 *
 * <p>This preprocessor is almost useful in every situation as it
 * helps prevent saturation of weights during the training process.
 *
 * @author Ragha
 * @version 1.0
 */
public class InputVariancePreprocessor extends AbstractInputPreprocessor
{
    private static final long serialVersionUID = -1468953552935844656L;

    //the settings to be used...
    private double averages[];
    private double variances[];

    @Override
    public void computeSettings(TrainingSet ts)
    {
        int inputSize = ts.getTrainingPattern(0).getInputData().length;
        int numPatterns = ts.getTrainingPatterns().size();

        //cleanup...
        averages = null;
        variances = null;

        //init...
        averages = new double[inputSize];
        variances = new double[inputSize];
        Arrays.fill(averages, 0.0);
        Arrays.fill(variances, 0.0);

        //calculate averages...
        for(TrainingPattern tp : ts.getTrainingPatterns()) {
            for(int i=0; i<tp.getInputData().length; i++) {
                averages[i] += tp.getInputData()[i];
            }
        }
        for(int i=0;i<averages.length;i++) {
            averages[i] /= numPatterns;
        }

        //variance^2 calculation...
        for(TrainingPattern tp : ts.getTrainingPatterns()) {
            for(int i=0; i<tp.getInputData().length; i++) {
                variances[i] += MathUtil
                        .square(tp.getInputData()[i] - averages[i]);
            }
        }
        //compute variance...
        for(int i=0;i<variances.length;i++) {
            variances[i] /= numPatterns;
            variances[i] = Math.sqrt(variances[i]);
        }
    }

    @Override
    public Double[] process(Double data[])
    {   
        Double newInputs[] = new Double[data.length];
            
        for(int i=0; i<data.length; i++)        
            newInputs[i] = (data[i] - averages[i]) / variances[i];
              
        return newInputs;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final InputVariancePreprocessor other = (InputVariancePreprocessor) obj;
        if (this.averages != other.averages && (this.averages == null || !Arrays.equals(this.averages,other.averages))) {
            return false;
        }
        if (this.variances != other.variances && (this.variances == null || !Arrays.equals(this.variances,other.variances))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.averages != null ? this.averages.hashCode() : 0);
        hash = 97 * hash + (this.variances != null ? this.variances.hashCode() : 0);
        return hash;
    }

    @Override
    public String getName() {
        return "Input variance centering";
    }

    @Override
    public String getDescription() {
        String desc = "Normalizes the input data to be approximately " +
                "centered around zero (evenly distributed)";
        return desc;
    }

    @Override
    public String getAuthor() {
        return "Ragha";
    }
}
