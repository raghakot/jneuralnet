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

package jneuralnet.core.datastore;

import javax.swing.JPanel;

/**
 * Represents the configuration panel for setting up parameters
 * in the data store.
 *
 * @author Ragha
 * @see AbstractDataStore
 * @version 1.0
 */
public class DataStoreConfigPanel extends JPanel
{
    private static final long serialVersionUID = 670383837193388678L;

    /**
     * The data store on which the settings are to be applied.
     */
    protected AbstractDataStore dataStore;

    /**
     * Creates a config panel; for the given data store.
     * @param dataStore The data store to be used.
     * @throws NullPointerException If dataStore is null
     */
    protected DataStoreConfigPanel(AbstractDataStore dataStore) throws NullPointerException
    {
        if(dataStore == null)
            throw new NullPointerException("dataStore cant be null");
        this.dataStore = dataStore;
    }

    /**
     * The data store associated with this config panel...
     * @return The data store associated.
     */
    public AbstractDataStore getDataStore() {
        return this.dataStore;
    }
}
