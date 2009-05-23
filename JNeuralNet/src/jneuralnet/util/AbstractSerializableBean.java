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
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * This subclass enhances {@code AbstractBean} by implementing the
 * {@code Serializable} interface. {@code AbstractSerializableBean} correctly
 * serializes all {@code Serializable} listeners that it contains. Implementors
 * that need to extends {@code AbstractBean} or one of its subclasses and
 * require serialization should use this class if possible. If it is not
 * possible to extend this class, the implementation can guide implementors on
 * how to properly serialize the listeners.
 *
 * @see AbstractBean
 * @author Ragha
 */
public class AbstractSerializableBean extends AbstractBean implements Serializable
{
    private static final long serialVersionUID = -3459406004204097480L;

    protected AbstractSerializableBean()
    {
        super();
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();

        for (PropertyChangeListener l : getPropertyChangeListeners()) {
            if (l instanceof Serializable) {
                s.writeObject(l);
            }
        }
        s.writeObject(null);
    }

    private void readObject(ObjectInputStream s) throws ClassNotFoundException,
            IOException {
        s.defaultReadObject();

        Object listenerOrNull;
        while (null != (listenerOrNull = s.readObject())) {
            if (listenerOrNull instanceof PropertyChangeListener) {
                addPropertyChangeListener((PropertyChangeListener) listenerOrNull);
            }
        }
    }
}
