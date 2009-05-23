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

/**
 *
 * @author Ragha
 */
public final class MathUtil
{
    private MathUtil() {

    }

    public static Double square(Double d) {
        return d * d;
    }

    /**
     * generates 'num' list of random numbers
     * between lo and hi. lo is inclusive and hi is exclusive
     * @param lo
     * @param hi
     * @param arr If not null then this arr is cleared and added with
     * random indices. if null new arraylist is created and returned
     * @return
     */
    /*
    public static ArrayList<Integer> getRandomIndices(ArrayList<Integer> arr,
            int num, int lo, int hi)
    {
        Random rand = new Random();
        ArrayList<Integer> randomIndices;

        if(arr == null)
            randomIndices = new ArrayList<Integer>();
        else
            randomIndices = arr;

        randomIndices.clear();
        while(true)
        {
            int randIndex = lo + rand.nextInt(hi - lo);

            if(!randomIndices.contains(randIndex))
                randomIndices.add(randIndex);
            if(randomIndices.size() == num)
                break;
        }

        return randomIndices;
    }
*/
}
