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

package jneuralnet.core.learning.costfunction;

import jneuralnet.util.MathUtil;

/**
 * Represents the popular sum of squares error function.
 * The overall error is given as the sum of the squares
 * of the residual errors for each output neuron.
 *
 * @author Ragha
 * @version 1.0
 * @see AbstractCostFunction
 */
public class SumOfSquaresError extends AbstractCostFunction
{
    private static final long serialVersionUID = 6562900988756601459L;

    public Double getErrorValue(Double desired[], Double actual[])
    {
        Double overallError = 0.0;
        for(int i=0;i<desired.length;i++)
        {
            overallError += MathUtil.square(desired[i] - actual[i]);
        }

        overallError /= 2.0;
        return overallError;
    }

    public Double getOutputDelta(Double desired, Double actual,
            Double derivativeOfOutputWithWeight) {
        return -1.0 * (desired - actual) * derivativeOfOutputWithWeight;
    }

    @Override
    public String getName() {
        return "Sum of squares error";
    }

    @Override
    public String getDescription() {
        return "Provides the standard sum of squares error implemetation.\n" +
                " The overall error is given as the sum of the squares" +
                " of the residual errors on individial output vector";
    }

    @Override
    public String getAuthor() {
        return "Ragha";
    }
}
