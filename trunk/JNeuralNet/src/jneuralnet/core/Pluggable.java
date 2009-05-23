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

package jneuralnet.core;

import jneuralnet.util.AbstractSerializableBean;
import org.openide.util.Lookup;

/**
 * This is the base class for all the pluggable SPI's. The methods present
 * in this class are used by the PluginManager (JNNHelper Tool) to show
 * descriptive information for the pluggable components.
 *
 * <p>This class extends <code>AbstractSerializableBean</code> for supporting
 * property change listeners and also serializes the listeners.
 *
 * @author Ragha
 * @see AbstractSerializableBean
 * @version 1.0
 */
public abstract class Pluggable extends AbstractSerializableBean implements Lookup.Provider
{
    /**
     * Override this method to provide the name of this Pluggable class...
     *
     * @return The name or ID for the class. Returns the class name by default.
     */
    public String getName()
    {
        Class cls = this.getClass();
        return cls.getName();
    }

    /**
     * Override this method to provide a description of this class
     *
     * @return The description of this class. Returns 'classname Impl'
     * as the default string.
     */
    public String getDescription()
    {
        Class cls = this.getClass();
        return cls.getName() + " Impl";
    }

    /**
     * Oveeride this method to provide the version number for this class.
     *
     * @return The version number. Returns 1.0 by default.
     */
    public String getVersion()
    {
        return "1.0";
    }

    /**
     * Oveeride this method to mention the author of this class.     
     *
     * @return The author of this class. Returns 'Undefined' by default.
     */
    public String getAuthor()
    {
        return "Undefined";
    }

    @Override
    public String toString() {
        return getName();
    }

    /**
     * Returns this classes lookup. This method has been included
     * to make this class flexible. Oveeride this emthod to return
     * your desired lookup. Returns null by default.
     * 
     * @return The lookup used.
     */
    public Lookup getLookup() {        
        return null;        
    }
}
