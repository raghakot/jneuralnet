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
package jneuralnet.core.learning;

import jneuralnet.core.SynapticConnection;

/**
 * Implements the simplistic classical BackProp algo. It does not make use
 * of the momentum term.
 *
 * <p>Learning rate for individual synaptic connections are used in the
 * weight update procedure.
 *
 * @see AbstractBPBasedAlgo
 * @see AbstractLearningAlgo
 * @see SynapticConnection
 *
 * @author Ragha
 * @version 1.0 
 */
public class BackPropagation extends AbstractBPBasedAlgo
{
    private static final long serialVersionUID = 2009050221L;

    @Override
    public Double computeWeightChange(SynapticConnection conn, Double errGradient)
    {
        return conn.getLearningRate() * errGradient;
    }

    @Override
    public String getName()
    {
        return "Back Propagation";
    }

    @Override
    public String getDescription()
    {
        return "Provides an implementation of the basic" +
                " back propogation algorithm";
    }

    @Override
    public String getAuthor()
    {
        return "Ragha";
    }

    @Override
    public boolean isMomentumPermissible()
    {
        return true;
    }

    @Override
    public boolean isDynamicLearningPermissible()
    {
        return true;
    }

    @Override
    public boolean isCostFunctionModifiable()
    {
        return true;
    }
}
