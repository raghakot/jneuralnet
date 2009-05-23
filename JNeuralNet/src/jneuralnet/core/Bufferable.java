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

import java.util.HashMap;
import jneuralnet.util.AbstractSerializableBean;

/**
 * A Utility class to support addition of new properties to a java bean
 * at the runtime. Extend your class with this to make it bufferable. It is
 * useful if you want to associate certain properties with an object and
 * maintain the OOP nature of your code.
 *
 * <p>This class extends <code>AbstractSerializableBean</code> 
 * for serialization and property change support.
 * 
 * @author Ragha
 * @see AbstractSerializableBean
 * @version 1.0
 */
public class Bufferable extends AbstractSerializableBean
{
    private static final long serialVersionUID = 506835437375346326L;

    /**
     * This map is used to store property name and object as key-value pairs.
     */
    private HashMap<String, Object> buffer = new HashMap<String, Object>();

    /**
     * This method is used as a getter for the associated property in the
     * buffer.
     *
     * <p>You must typically create the property using
     * {@link #createPropertyInBuffer(java.lang.String) createPropertyInBuffer(...)} method
     * before using this method.
     *
     * @param property The property to be used
     * @return The value of the property.
     * @throws java.lang.IllegalArgumentException If there is no such property.
     */
    public Object getValueFromBuffer(String property)
            throws IllegalArgumentException
    {
        if(buffer.containsKey(property))
            return buffer.get(property);
        else
        {
            throw new IllegalArgumentException("Property: '"+property+"' " +
                    "does not exist...");
        }
    }

    /**
     * This method can be used as a setter for the associated property.
     * in the buffer.
     *
     * <p>You must typically create the property using
     * {@link #createPropertyInBuffer(java.lang.String) createPropertyInBuffer(...)} method
     * before using this method.
     *
     * @param property The property value to be set
     * @param Value The value to be set
     * @throws java.lang.IllegalArgumentException If there is no such property
     */
    public void putValueInBuffer(String property, Object Value)
            throws IllegalArgumentException
    {
        if(buffer.containsKey(property))
            buffer.put(property, Value);
        else
        {
            throw new IllegalArgumentException("Property: '"+property+"' " +
                    "does not exist...");
        }        
    }

    /**
     * Creates a new property in the buffer. Once the property is created,
     * it can be get or set using the 
     * {@link #putValueInBuffer(java.lang.String, java.lang.Object) putValueInBuffer(...)} and
     * {@link #getValueFromBuffer(java.lang.String) getValueFromBuffer(...)} methods
     *
     * <p>It is recommended that you use Classname-property name as property
     * string to avoid conflicts with other existing property names...
     *
     * @param property The property name to be associated with the buffer.
     * @throws java.lang.IllegalArgumentException If the property name already exists.
     */
    public void createPropertyInBuffer(String property)
            throws IllegalArgumentException
    {
        if(buffer.containsKey(property))
        {
            throw new IllegalArgumentException("Property: '"+property+"' " +
                    "already exists...");
        }
        else
        {
            buffer.put(property, new Object());
        }
    }

    /**
     * This method can be used to check if a given property already exists.
     * @param property The property to be checked.
     * @return true, if the property exists.
     */
    public boolean isPropertyInBuffer(String property)
    {
        return buffer.containsKey(property);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Bufferable other = (Bufferable) obj;
        if (this.buffer != other.buffer && (this.buffer == null || !this.buffer.equals(other.buffer))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + (this.buffer != null ? this.buffer.hashCode() : 0);
        return hash;
    }    
}
