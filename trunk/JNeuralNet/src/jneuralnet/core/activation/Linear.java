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

package jneuralnet.core.activation;

/**
 * Implements the Linear activation function. It extends
 * <tt>AbstractActivation</tt> class.
 *
 * <p> This function provides and ouput range of (-infinity, +infinity).
 *
 * @author Ragha
 * @see AbstractActivation
 * @version 1.0
 */
public class Linear extends AbstractActivation
{
    private static final long serialVersionUID = 5779697963677837281L;
    
    /**
     * The slope of the line...
     */
    private Double slope;

    /**
     * The tern 'c' in equation y = mx + c.
     */
    private Double c;

    /**
     * Creates a linear activation function
     * with slope = 1.0 and c = 0.0
     */
    public Linear()
    {
        this(1.0, 0.0);
    }

    /**
     * Creates a linear activation function given by the expression:
     * <p> <tt> y = slope * x + c </tt>
     *
     * @param slope Slope of the line.
     * @param c The value 'c' in the equation <tt> y = slope * x + c </tt>
     */
    public Linear(Double slope, Double c)
    {
        setSlope(slope);
        setC(c);
    }

    /**
     * Sets the slope of the line. The line is represented by the
     * equation <tt> y = slope * x + c </tt>
     * 
     * @param slope The slope value to be set.
     */
    public void setSlope(Double slope)
    {
        this.slope = slope;
    }

    /**
     * Sets the value 'c' in the equation <tt> y = slope * x + c </tt>
     *
     * @param c The 'c' value to be set.
     */
    public void setC(Double c)
    {
        this.c = c;
    }

    /**
     * Represents linear activation function. It is given by the
     * expression:
     *
     * <p><tt> y = slope * x + c </tt>
     *
     * @param input The input value to this function
     * @return The value returned by the function
     */
    public Double activation(Double input)
    {
        return (slope * input + c);
    }

    /**
     * Represents the derivative of the linear function. It simply returns
     * the slope of the line.
     *
     * @param input The input value to this function
     * @return The value returned by the function
     */
    public Double activationDerviative(Double input)
    {
        return slope;
    }

    @Override
    public String getName()
    {
        return "Linear";
    }

    @Override
    public String getDescription() {
        return "Implementation of linear activation function.\n" +
                " This function provides an output" +
                " range of (-infinity, +infinity)";
    }

    @Override
    public String getAuthor() {
        return "Ragha";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Linear other = (Linear) obj;
        if (this.slope != other.slope && (this.slope == null || !this.slope.equals(other.slope))) {
            return false;
        }
        if (this.c != other.c && (this.c == null || !this.c.equals(other.c))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + (this.slope != null ? this.slope.hashCode() : 0);
        hash = 13 * hash + (this.c != null ? this.c.hashCode() : 0);
        return hash;
    }
}
