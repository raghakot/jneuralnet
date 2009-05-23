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

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Scanner;
import jneuralnet.core.training.TrainingPattern;
import jneuralnet.core.training.TrainingSet;

/**
 * This class implements the File data storage medium. It can read and write
 * training set from text files.
 *
 * <p> The following text format is used to represent the training data:<br/> 
 * &nbsp;&nbsp;&nbsp;&nbsp;1) The inputs and outputs are separated by ',' charater.<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;2) The input set and output set are separated by ';' character.
 *
 * <p>For example: Consider the training pattern (1.0, 1.3; 1.5, 1.8)
 * Notice how inputs and outputs are separated by ',' and the set by ';'.
 * Empty lines and '//' comments are ignored while reading 
 * the data from a file.
 * 
 * <p> This class extends the <tt>AbstractDataStore</tt> class.
 *
 * @author Ragha
 * @see AbstractDataStore
 * @version 1.0
 */
public class FileDataStore extends AbstractDataStore
{
    private static final long serialVersionUID = -5605931334023425963L;

    /**
     * The read and write filename's
     */
    private String readName, writeName;    

    /**
     *
     * @return The file name from where data is to be read.
     */
    public String getReadName() {
        return readName;
    }

    /**
     * 
     * @param readName The file name from where data is to be read.
     */
    public void setReadName(String readName) {
        set("readName", readName);
    }

    /**
     *
     * @return The file name where data is to be written.
     */
    public String getWriteName() {
        return writeName;
    }

    /**
     *
     * @param writeName The file name where data is to be written.
     */
    public void setWriteName(String writeName) {
        set("writeName", writeName);
    }    

    @Override
    public TrainingSet loadTrainingSet() throws Exception
    {
        FileInputStream fin = new FileInputStream(readName);
        Scanner sc = new Scanner(fin);
        TrainingSet ts = new TrainingSet();

        while(sc.hasNext())
        {
            String line = sc.nextLine();

            boolean isValid = false;
            if(line.startsWith("//") || line.isEmpty())
                isValid = false;
            else
                isValid = true;

            if(isValid) {
                TrainingPattern p = FileDataStore.parseLine(line);
                ts.add(p);
            }
        }

        sc.close();
        fin.close();
        return ts;
    }

    /**
     * Parses the line to a TrainingPattern object.
     * <p> The String must be in the following format:<br/>
     * &nbsp;&nbsp;&nbsp;&nbsp;1) The inputs and outputs are separated by ',' charater.<br/>
     * &nbsp;&nbsp;&nbsp;&nbsp;2) The input set and output set are separated by ';' character.
     *
     * @param line A String in the above specified format.
     * @return The parsed training pattern.
     * @throws java.lang.IllegalArgumentException If the file data is not
     * in the specified format.
     */
    public static TrainingPattern parseLine(String line)
            throws IllegalArgumentException
    {
        line.trim();
        String arrSets[] = line.split(";");
        if(arrSets.length != 2) {
            throw new IllegalArgumentException("Invalid data format, " +
                    "error parsing line: "+line);
        }

        String inputs[] = arrSets[0].split(",");
        String outputs[] = arrSets[1].split(",");

        if(inputs.length < 1 || outputs.length < 1) {
            throw new IllegalArgumentException("Invalid data format, " +
                    "error parsing line: "+line);
        }

        Double inputData[] = new Double[inputs.length];
        Double outputData[] = new Double[outputs.length];

        for(int i=0; i<inputs.length; i++)
        {
            String s = inputs[i];
            try
            {
                inputData[i] = Double.parseDouble(s);
            }
            catch(NumberFormatException e)
            {                
                throw new IllegalArgumentException("Invalid number "+s
                        +" in the training data."
                        +" Error in parsing line: " + line);
            }
        }

        for(int i=0; i<outputs.length; i++)
        {
            String s = outputs[i];
            try
            {
                outputData[i] = Double.parseDouble(s);
            }
            catch(NumberFormatException e)
            {
                throw new IllegalArgumentException("Invalid number "+s
                        +" in the training data");
            }
        }

        TrainingPattern p = new TrainingPattern(inputData, outputData);
        return p;
    }

    @Override
    public void saveTrainingSet(TrainingSet ts) throws Exception {
        FileOutputStream fout = new FileOutputStream(writeName);
        PrintWriter pw = new PrintWriter(fout);
        String s;

        for(TrainingPattern tp : ts.getTrainingPatterns())
        {
            s = "";
            for(Double in : tp.getInputData())
                s += in + ", ";
            if(s.length() > 2)
                s = s.substring(0, s.length() - 2);

            s += " ; ";
            
            for(Double out : tp.getOutputData())
                s += out + ", ";
            if(s.length() > 2)
                s = s.substring(0, s.length() - 2);

            pw.println(s);
        }

        pw.flush();
        pw.close();
        fout.close();
    }

    private DataStoreConfigPanel pnl;
    @Override
    public DataStoreConfigPanel getConfigPanel() {
        if(pnl == null)
            pnl = new FileDataStoreConfigPanel(this);
        return pnl;
    }

    @Override
    public String getName() {
        return "File Data Store";
    }

    @Override
    public String getDescription() {
        return "Lets you read and write training data from a file.\n"
                + " The data format should be as follows: \n\n"
                + " input1, input2, ... ; output1, output2\n"
                + " Note the ';' character used to separate input sets"
                + " from output sets";
    }

    @Override
    public String getAuthor() {
        return "Ragha";
    }
}
