/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jneuralnet.core.learning;

import jneuralnet.core.learning.costfunction.SumOfSquaresError;

/**
 *
 * @author Ragha
 */
public class OpticalBP extends BackPropagation
{
    private class OpticalBPModifiedCost extends SumOfSquaresError
    {
        private static final long serialVersionUID = 2009050236L;

        @Override
        public Double getErrorValue(Double desired[], Double actual[])
        {
            return super.getErrorValue(desired, actual);
        }

        @Override
        public Double getOutputDelta(Double desired, Double actual,
                Double derivativeOfOutputWithWeight)
        {
            double error = desired - actual;
            error = 1 + Math.exp(error * error);
            if (error < 0)
            {
                error = -error;
            }
            return -1.0 * error * derivativeOfOutputWithWeight;
        }
    }

    public OpticalBP()
    {
        this.costFunction = new OpticalBPModifiedCost();
    }

    @Override
    public String getName()
    {
        return "Optical BP";
    }

    @Override
    public String getDescription()
    {
        return "This algorithm was developed by Otair & Salameh, 2004." +
                " It uses a modified error fucntion to speed up the" +
                " convergence rate of the network.\n\n" +
                "Avoid the usage of this algo for now as " +
                "its not yet reviewed";
    }

    @Override
    public String getAuthor()
    {
        return "Ragha";
    }

    @Override
    public boolean isCostFunctionModifiable()
    {
        return false;
    }
}
