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

import jneuralnet.core.training.TrainingPattern;
import jneuralnet.core.training.TrainingSet;

/**
 * Scales the output range to a specified interval. This preprocessor is
 * useful to normalize the output vectors in accordance to the activation
 * function used for the output layer.
 *
 * @author Ragha
 * @version 1.0
 */
public class OutputRangePreprocessor extends AbstractOutputPreprocessor
{
    private static final long serialVersionUID = -4912988055888060453L;

    //Settins to be used...
    private double min, max;

    /**
     * sets the default range of -0.85 to +0.85
     */
    public OutputRangePreprocessor() {
//        this(-0.85, 0.85);
        this(0, 0.8);
    }

    public OutputRangePreprocessor(double min, double max) {
        this.min = min;
        this.max = max;
    }

    //indicates movement factor in +ve x-direction...
    private Double translateFactor;
    //indicates the scaling factor...
    private Double scaleFactor;
    
    @Override
    public void computeSettings(TrainingSet ts) {
        //compute max and min...
        Double finalMax = Double.MIN_VALUE, finalMin = Double.MAX_VALUE;
        for(TrainingPattern tp : ts.getTrainingPatterns()) {
            for(Double d : tp.getOutputData()) {
                if(d >= finalMax)
                    finalMax = d;
                if(d < finalMin)
                    finalMin = d;
            }                
        }        
        translateFactor = -finalMin;
        //compute new max value after translation...
        finalMax += translateFactor;
        
        //compute scaling factor...
        scaleFactor = 1.0 / finalMax;        
    }

    @Override
    public Double[] process(Double[] data)
    {
        Double newInputs[] = new Double[data.length];
        
        for(int i=0; i<data.length; i++)
        {            
            newInputs[i] = (data[i] + translateFactor) * scaleFactor
                    * (max - min) + min;         
        }

        return newInputs;
    }

    @Override
    public Double[] deProcess(Double[] data)
    {
        Double newInputs[] = new Double[data.length];

        for(int i=0; i<data.length; i++)
        {
            newInputs[i] = (data[i] - min) / (scaleFactor * (max - min))
                    - translateFactor;
        }

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
        final OutputRangePreprocessor other = (OutputRangePreprocessor) obj;
        if (this.min != other.min) {
            return false;
        }
        if (this.max != other.max) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + (int) (Double.doubleToLongBits(this.min) ^ (Double.doubleToLongBits(this.min) >>> 32));
        hash = 23 * hash + (int) (Double.doubleToLongBits(this.max) ^ (Double.doubleToLongBits(this.max) >>> 32));
        return hash;
    }

    @Override
    public String getName() {
        return "Output range scaler";
    }

    @Override
    public String getDescription() {
        String desc = "Scales the output range to a specified interval";
        return desc;
    }

    @Override
    public String getAuthor() {
        return "Ragha";
    }
}
