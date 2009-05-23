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

package jnnhelper;

import java.util.HashMap;
import jneuralnet.core.NeuralNetwork;
import jneuralnet.core.activation.*;
import jneuralnet.core.datastore.*;
import jneuralnet.core.learning.*;
import jneuralnet.core.learning.costfunction.*;
import jneuralnet.core.preprocessor.*;
import jneuralnet.util.AbstractSerializableBean;
import pluginmanager.PluginMap;

/**
 *
 * @author Ragha
 */
public class Configuration extends AbstractSerializableBean
{
    private static Configuration instance;

    //Plugin maps for the pluggable modules...
    private PluginMap<AbstractActivation> mapActivations;
    private PluginMap<AbstractLearningAlgo> mapLearningAlgos;
    private PluginMap<AbstractDataStore> mapDataStores;
    private PluginMap<AbstractInputPreprocessor> mapInputPreprocessors;
    private PluginMap<AbstractOutputPreprocessor> mapOutputPreprocessors;
    private PluginMap<AbstractCostFunction> mapCostFunction;

    //class not instantiable...
    private Configuration()
    {
        neuralNetwork = new NeuralNetwork(2, 2);        
        initPluginMaps();
    }

    public static Configuration getInstance()
    {
        if(instance == null)
            instance = new Configuration();

        return instance;
    }

    private NeuralNetwork neuralNetwork;

    public NeuralNetwork getNeuralNetwork() {
        return neuralNetwork;
    }

    public void setNeuralNetwork(NeuralNetwork neuralNetwork) {
        set("neuralNetwork", neuralNetwork);        
    }

    private void initPluginMaps()
    {
        mapActivations = new PluginMap<AbstractActivation>() {

            @Override
            protected HashMap<String, AbstractActivation> getDefault() {
                HashMap<String, AbstractActivation> mapDefault
                        = new HashMap<String, AbstractActivation>();

                AbstractActivation act = new Linear();
                mapDefault.put(act.getName(), act);
                act = new Sigmoid();
                mapDefault.put(act.getName(), act);
                act = new LogisticSigmoid();
                mapDefault.put(act.getName(), act);

                return mapDefault;
            }
        };

        mapLearningAlgos = new PluginMap<AbstractLearningAlgo>() {

            @Override
            protected HashMap<String, AbstractLearningAlgo> getDefault() {
                HashMap<String, AbstractLearningAlgo> mapDefault
                        = new HashMap<String, AbstractLearningAlgo>();

                AbstractLearningAlgo algo = new BackPropagation();
                mapDefault.put(algo.getName(), algo);
                algo = new OpticalBP();
                mapDefault.put(algo.getName(), algo);
                algo = new QuickProp();
                mapDefault.put(algo.getName(), algo);
                
                return mapDefault;
            }
        };

        mapDataStores = new PluginMap<AbstractDataStore>() {

            @Override
            protected HashMap<String, AbstractDataStore> getDefault() {
                HashMap<String, AbstractDataStore> mapDefault
                        = new HashMap<String, AbstractDataStore>();
                
                AbstractDataStore ads = new FileDataStore();
                mapDefault.put(ads.getName(), ads);
                ads = new MySQLDataStore();
                mapDefault.put(ads.getName(), ads);

                return mapDefault;
            }
        };

        mapInputPreprocessors = new PluginMap<AbstractInputPreprocessor>() {

            @Override
            protected HashMap<String, AbstractInputPreprocessor> getDefault() {
                HashMap<String, AbstractInputPreprocessor> mapDefault
                        = new HashMap<String, AbstractInputPreprocessor>();
                        
                AbstractInputPreprocessor ip = new InputVariancePreprocessor();
                mapDefault.put(ip.getName(), ip);
                
                return mapDefault;
            }
        };

        mapOutputPreprocessors = new PluginMap<AbstractOutputPreprocessor>() {

            @Override
            protected HashMap<String, AbstractOutputPreprocessor> getDefault() {
                HashMap<String, AbstractOutputPreprocessor> mapDefault
                        = new HashMap<String, AbstractOutputPreprocessor>();

                AbstractOutputPreprocessor op = new OutputRangePreprocessor();
                mapDefault.put(op.getName(), op);

                return mapDefault;
            }
        };

        mapCostFunction = new PluginMap<AbstractCostFunction>() {

            @Override
            protected HashMap<String, AbstractCostFunction> getDefault() {
                HashMap<String, AbstractCostFunction> mapDefault
                        = new HashMap<String, AbstractCostFunction>();

                AbstractCostFunction cf = new SumOfSquaresError();
                mapDefault.put(cf.getName(), cf);
                cf = new AverageSquaredError();
                mapDefault.put(cf.getName(), cf);
                
                return mapDefault;
            }
        };
    }

    public PluginMap<AbstractActivation> getMapActivations() {
        return mapActivations;
    }

    public PluginMap<AbstractDataStore> getMapDataStores() {
        return mapDataStores;
    }

    public PluginMap<AbstractInputPreprocessor> getMapInputPreprocessors() {
        return mapInputPreprocessors;
    }

    public PluginMap<AbstractLearningAlgo> getMapLearningAlgos() {
        return mapLearningAlgos;
    }

    public PluginMap<AbstractOutputPreprocessor> getMapOutputPreprocessors() {
        return mapOutputPreprocessors;
    }

    public PluginMap<AbstractCostFunction> getMapCostFunction() {
        return mapCostFunction;
    }
}
