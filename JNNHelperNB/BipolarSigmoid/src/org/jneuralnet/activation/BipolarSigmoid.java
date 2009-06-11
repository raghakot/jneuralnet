package org.jneuralnet.activation;

import jneuralnet.core.activation.AbstractActivation;
import org.openide.util.Lookup;

/**
 *
 * @author Ragha
 */
public class BipolarSigmoid extends AbstractActivation
{
    @Override
    public Double activation(Double input)
    {
        return ( 2.0 / (1 + Math.exp(-input)) ) - 1;
    }

    @Override
    public Double activationDerviative(Double input)
    {
        return (1 - activation(input) * activation(input)) / 2;
    }

    @Override
    public String getName() {
        return "Bipolar Sigmoid";
    }

    @Override
    public String getDescription() {
        return "The implementation of bipolar sigmoid function.\n" +
                " It is given by ' 2.0 / ( ( 1 + Math.exp(-x) )  - 1 ) '.\n" +
                " It has an output range of (-1, 1)";
    }

    @Override
    public String getAuthor() {
        return "Ragha";
    }

    @Override
    public Lookup getLookup()
    {
        return null;
    }
}
