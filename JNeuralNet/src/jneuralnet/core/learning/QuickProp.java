/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jneuralnet.core.learning;

import jneuralnet.core.SynapticConnection;

/**
 * Implements the Quickprop algorithm. It is loosely based on Newton's method.
 * It is quicker than standard backpropagation because it uses an
 * approximation to the error curve, and second order derivative information
 * which allow a quicker evaluation.
 * 
 * @author Ragha
 * @version 1.0
 */
public class QuickProp extends AbstractBPBasedAlgo
{
    //kept outside to reduce classloader overload
    //as looping operations are involved...
    private transient Double newWeight = 0.0;
    private transient Double prevErrGradient = 0.0;

    @Override
    public Double computeWeightChange(SynapticConnection conn, Double errGradient)
    {
        //associate a new property 'QuickProp-prevErrGradient' with a connection...
        if (!conn.isPropertyInBuffer("QuickProp-prevErrGradient"))
        {
            conn.createPropertyInBuffer("QuickProp-prevErrGradient");
            conn.putValueInBuffer("QuickProp-prevErrGradient", 0.0);
        }

        prevErrGradient = (Double) conn.getValueFromBuffer("QuickProp-prevErrGradient");        
        newWeight = ( errGradient / (prevErrGradient - errGradient) ) *
                conn.getPrevWeightChange();

        //update for next iteration...        
        conn.putValueInBuffer("QuickProp-prevErrGradient", errGradient);
        return newWeight;
    }

    @Override
    public String getName()
    {
        return "Quickprop";
    }

    @Override
    public String getAuthor()
    {
        return "Ragha";
    }

    @Override
    public String getDescription()
    {
        return "The Quickprop algorithm is loosely based on Newton's method." +
                " It is quicker than standard backpropagation because it uses " +
                "an approximation to the error curve, and second order " +
                "derivative information which allow a quicker evaluation.\n" +
                "f the slope of the error curve is less than that of the " +
                "previous one, then the weight will change in the same direction " +
                "(positive or negative). However, there needs to be some " +
                "control to prevent the weights from growing too large.";
    }

    @Override
    public boolean isMomentumPermissible()
    {
        return false;
    }

    @Override
    public boolean isDynamicLearningPermissible()
    {
        return false;
    }

    @Override
    public boolean isCostFunctionModifiable()
    {
        return true;
    }
}
