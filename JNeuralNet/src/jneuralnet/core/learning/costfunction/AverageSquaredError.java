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

/**
 * Represents the average squared error function.
 * The overall error is given as the average of sum of the squares
 * error.
 *
 * @author Ragha
 * @version 1.0
 * @see AbstractCostFunction
 * @see SumOfSquaresError
 */
public class AverageSquaredError extends SumOfSquaresError
{
    private static final long serialVersionUID = -1622710303186505548L;

    @Override
    public Double getErrorValue(Double desired[], Double actual[])
    {
        return super.getErrorValue(desired, actual)/ desired.length;
    }

    @Override
    public Double getOutputDelta(Double desired, Double actual,
            Double derivativeOfOutputWithWeight) {
        return super.getOutputDelta(desired, actual,
                derivativeOfOutputWithWeight);
    }

    @Override
    public String getName() {
        return "Average squared error";
    }

    @Override
    public String getDescription() {
        return " Represents the average squared error function.\n" +
                " The overall error is given as the average of sum " +
                "of the squares error.";
    }

    @Override
    public String getAuthor() {
        return "Ragha";
    }
}
