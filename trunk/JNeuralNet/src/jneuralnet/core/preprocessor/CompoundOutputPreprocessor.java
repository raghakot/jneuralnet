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

package jneuralnet.core.preprocessor;

import java.util.Arrays;
import jneuralnet.core.training.TrainingSet;

/**
 * Enables you to create a more complex data preprocessor by
 * combining existing preprocessors in order. ie, the process and
 * deprocess methods are executed <i>inorder</i> for normalization
 * and de-normalization tasks...
 *
 * <p>This mechanism is similar to <code>CompoundPainter</code> implemented
 * in the SwingX project.
 *
 * @author Ragha
 * @version 1.0
 * @see AbstractOutputPreprocessor
 */
public class CompoundOutputPreprocessor extends AbstractOutputPreprocessor
{
    private static final long serialVersionUID = 7393333667936338869L;

    // The preprocessors to be used (in-order)...
    private AbstractOutputPreprocessor preprocessors[];
    
    public CompoundOutputPreprocessor() {
        preprocessors = new AbstractOutputPreprocessor[0];
    }

    public CompoundOutputPreprocessor(AbstractOutputPreprocessor... preprocessors) {
        setPreprocessors(preprocessors);
    }

    /**
     * Set the input preprocessors to be compounded
     * @param preprocessors The preprocessors to be used.
     */
    public void setPreprocessors(AbstractOutputPreprocessor... preprocessors) {
        this.preprocessors = new AbstractOutputPreprocessor
                [preprocessors == null ? 0 : preprocessors.length];
        if(preprocessors != null) {
            System.arraycopy(preprocessors, 0, this.preprocessors,
                    0, preprocessors.length);
        }
    }

    /**
     * The basic preprocessors used in this compound processor.
     * @return The basic preprocessors used for compiunding the effect.
     */
    public AbstractOutputPreprocessor[] getPreprocessors() {
        return preprocessors;
    } 

    @Override
    public void computeSettings(TrainingSet ts) {
        for(AbstractOutputPreprocessor p : preprocessors)
            p.computeSettings(ts);
    }

    @Override
    public Double[] process(Double[] data) {
        for(AbstractOutputPreprocessor p : preprocessors)
            data = p.process(data);
        return data;
    }

    @Override
    public Double[] deProcess(Double[] data) {
        //de process in the reverse order...
        for(int i = preprocessors.length - 1; i>=0; i--)
            data = preprocessors[i].deProcess(data);            
        return data;
    }

    @Override
    public String toString()
    {
        String ret = "";
        for(AbstractOutputPreprocessor op : preprocessors)
            ret += op.getName() + ", ";

        if(preprocessors.length > 0)
            ret = ret.substring(0, ret.length() - 2);
        return ret;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CompoundOutputPreprocessor other = (CompoundOutputPreprocessor) obj;
        if (this.preprocessors != other.preprocessors && (this.preprocessors == null || !Arrays.equals(this.preprocessors,other.preprocessors))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + (this.preprocessors != null ? this.preprocessors.hashCode() : 0);
        return hash;
    }
}
