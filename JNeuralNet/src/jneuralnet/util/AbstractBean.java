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

package jneuralnet.util;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.lang.reflect.Field;

/**
 * <p>
 * A convenience class from which to extend all non-visual AbstractBeans. It
 * manages the PropertyChange notification system, making it relatively trivial
 * to add support for property change events in getters/setters.
 *
 * <p>
 * {@code AbstractBean} is not {@link java.io.Serializable}. Special care must
 * be taken when creating {@code Serializable} subclasses, as the
 * {@code Serializable} listeners will not be saved.  Subclasses will need to
 * manually save the serializable listeners.  The {@link AbstractSerializableBean}
 * is {@code Serializable} and already handles the listeners correctly.  If
 * possible, it is recommended that {@code Serializable} beans should extend
 * {@code AbstractSerializableBean}.  If it is not possible, the
 * {@code AbstractSerializableBean} bean implementation provides details on
 * how to correctly serialize an {@code AbstractBean} subclass.
 *
 * @see AbstractSerializableBean
 * @author Ragha
 */
public class AbstractBean
{
    protected PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    protected AbstractBean()
    {
        pcs = new PropertyChangeSupport(this);
    }   

    /**
     * Add a PropertyChangeListener to the listener list.
     * The listener is registered for all properties.
     * The same listener object may be added more than once, and will be called
     * as many times as it is added.
     * If <code>listener</code> is null, no exception is thrown and no action
     * is taken.
     *
     * @param listener  The PropertyChangeListener to be added
     */
    public final void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    /**
     * Remove a PropertyChangeListener from the listener list.
     * This removes a PropertyChangeListener that was registered
     * for all properties.
     * If <code>listener</code> was added more than once to the same event
     * source, it will be notified one less time after being removed.
     * If <code>listener</code> is null, or was never added, no exception is
     * thrown and no action is taken.
     *
     * @param listener  The PropertyChangeListener to be removed
     */
    public final void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

    /**
     * Returns an array of all the listeners that were added to the
     * PropertyChangeSupport object with addPropertyChangeListener().
     * <p>
     * If some listeners have been added with a named property, then
     * the returned array will be a mixture of PropertyChangeListeners
     * and <code>PropertyChangeListenerProxy</code>s. If the calling
     * method is interested in distinguishing the listeners then it must
     * test each element to see if it's a
     * <code>PropertyChangeListenerProxy</code>, perform the cast, and examine
     * the parameter.
     *
     * <pre>
     * PropertyChangeListener[] listeners = bean.getPropertyChangeListeners();
     * for (int i = 0; i < listeners.length; i++) {
     *     if (listeners[i] instanceof PropertyChangeListenerProxy) {
     *     PropertyChangeListenerProxy proxy =
     *                    (PropertyChangeListenerProxy)listeners[i];
     *     if (proxy.getPropertyName().equals("foo")) {
     *       // proxy is a PropertyChangeListener which was associated
     *       // with the property named "foo"
     *     }
     *   }
     * }
     *</pre>
     *
     * @see java.beans.PropertyChangeListenerProxy
     * @return all of the <code>PropertyChangeListeners</code> added or an
     *         empty array if no listeners have been added
     */
    public final PropertyChangeListener[] getPropertyChangeListeners() {
        return pcs.getPropertyChangeListeners();
    }

    /**
     * Returns an array of all the listeners which have been associated
     * with the named property.
     *
     * @param propertyName  The name of the property being listened to
     * @return all of the <code>PropertyChangeListeners</code> associated with
     *         the named property.  If no such listeners have been added,
     *         or if <code>propertyName</code> is null, an empty array is
     *         returned.
     */
    public final PropertyChangeListener[] getPropertyChangeListeners(String propertyName) {
            return pcs.getPropertyChangeListeners(propertyName);
    }

    /**
     * Use this method in your bean's setter method to make it compatible with
     * this library...
     * <p>
     * Example -
     * 
     * <code>
     * <pre>     
     * public class Bean extends Bindable {
     *     private String field;
     *
     *     public void setField(String field) {
     *        //this.field = field;
     *        <b>set("field", field);</b>
     *     }
     *
     *     public String getField() {
     *         return this.field;
     *     }
     * }      
     * </pre>
     * </code>
     *
     * @param fieldName The name of the field     
     * @param value New value to be set.
     *
     * @throws IllegalArgumentException if <i>fieldName</i> is invalid.
     */
    protected void set(String fieldName, Object value)
    {
        Class cls = this.getClass();
        try
        {
            Field f = cls.getDeclaredField(fieldName);
            if(!f.isAccessible())
                f.setAccessible(true);

            Object old = f.get(this);
            f.set(this, value);
            pcs.firePropertyChange(fieldName, old, value);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            throw new IllegalArgumentException("Invalid field name : '"+fieldName+"'");
        }
    }

    /**
     * Gets the 'fieldName'. Initializes it if its null.
     * @param fieldName The fieldName whose value is to be fetched.
     * @return the value of the 'fieldName'. Of its null, then the value is
     * initialized and then returned.
     */
    protected Object get(String fieldName)
    {
        Class cls = this.getClass();

        try
        {
            Field f = cls.getDeclaredField(fieldName);
            if(!f.isAccessible())
                f.setAccessible(true);

            //Class type of the field...
            Class fieldClass = f.getType();            

            Object value = f.get(this);
            if(value == null)
            {
                value = fieldClass.newInstance();
                f.set(this, value);
            }                                   
            
            return value;
        }
        catch(Exception e)
        {
            throw new IllegalArgumentException("Invalid field name : '"+fieldName+"'");
        }
    }
}
