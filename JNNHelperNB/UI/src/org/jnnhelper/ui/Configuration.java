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
package org.jnnhelper.ui;

import jneuralnet.core.NeuralNetwork;
import jneuralnet.util.AbstractSerializableBean;

/**
 *
 * @author Ragha
 */
public class Configuration extends AbstractSerializableBean
{
    private static Configuration instance;

    //class not instantiable...
    private Configuration()
    {        
        neuralNetwork = new NeuralNetwork(2, 2);
    }

    public static Configuration getInstance()
    {
        if (instance == null)
        {
            instance = new Configuration();
        }

        return instance;
    }
    private NeuralNetwork neuralNetwork;

    public NeuralNetwork getNeuralNetwork()
    {
        return neuralNetwork;
    }

    public void setNeuralNetwork(NeuralNetwork neuralNetwork)
    {
        set("neuralNetwork", neuralNetwork);
    }
}
