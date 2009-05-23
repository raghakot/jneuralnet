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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import jneuralnet.core.training.TrainingPattern;
import jneuralnet.core.training.TrainingSet;
import jneuralnet.util.DBConnectionProvider;

/**
 * This class implements the data storage medium
 * (specifically for MySQL database). It can read or write
 * training data from the user defined database, table and column names.
 * The training data must be a number, otherwise 
 * <tt>NumberFormatException</tt> will be thrown.
 *
 * <p>A sample use case is shown below:<br/><br/>
 * <pre>
 *      MySQLDataStore ds = new MySQLDataStore();
 *      //set up connection parameters
 *      ds.setDBInfo("localhost", 3306);
 *      ds.setUserInfo("root", "pass");
 *
 *      //setup db parameters...
 *      ds.setReadDBName("database name");
 *      ds.setReadTblName("table name");
 *
 *      //setup column parameters...
 *      ds.setWriteInputCols(new String[]{"inp1", "inp2", "inp3"});
 *      ds.setWriteOutputCols(new String[]{"out1", "out2"});
 *
 *      //read data...
 *      TrainingSet ts = ds.loadTrainingSet();
 * </pre>
 *
 * TODO: Extend this class for any generic database...
 * @author Ragha
 * @version 1.0
 * @see AbstractDataStore
 */
public class MySQLDataStore extends AbstractDataStore
{
    private static final long serialVersionUID = 1736567246153532343L;

    //read and write database names
    private String readDBName, writeDBName;
    //read and write table names
    private String readTblName, writeTblName;
    //read input and output cols...
    private String readInputCols[], readOutputCols[];
    //write input and output cols...
    private String writeInputCols[], writeOutputCols[];    

    /**
     * Sets the username and password to be used
     * for connecting to the database.
     *
     * @param user The username to be used.
     * @param pass The password to be used.
     */
    public void setUserInfo(String user, String pass)
    {
        DBConnectionProvider.setUser(user);
        DBConnectionProvider.setPass(pass);
    }

    /**
     * Sets the database host and connecting port...
     * @param host The host to be used
     * @param port The port to be used.
     */
    public void setDBInfo(String host, int port)
    {        
        DBConnectionProvider.setHost(host);
        DBConnectionProvider.setPort(port);
    }

    public String getReadDBName() {
        return readDBName;
    }

    public void setReadDBName(String readDBName) {
        set("readDBName", readDBName);
    }

    public String[] getReadInputCols() {
        return readInputCols;
    }

    public void setReadInputCols(String[] readInputCols) {        
        this.readInputCols = new String[readInputCols.length];
        System.arraycopy(readInputCols, 0, this.readInputCols, 0, readInputCols.length);
    }

    public String[] getReadOutputCols() {
        return readOutputCols;
    }

    public void setReadOutputCols(String[] readOutputCols) {
        this.readOutputCols = new String[readOutputCols.length];
        System.arraycopy(readOutputCols, 0, this.readOutputCols, 0, readOutputCols.length);
    }

    public String getReadTblName() {
        return readTblName;
    }

    public void setReadTblName(String readTblName) {
        set("readTblName", readTblName);
    }

    public String getWriteDBName() {
        return writeDBName;
    }

    public void setWriteDBName(String writeDBName) {
        set("writeDBName", writeDBName);
    }

    public String[] getWriteInputCols() {
        return writeInputCols;
    }

    public void setWriteInputCols(String[] writeInputCols) {
        this.writeInputCols = new String[writeInputCols.length];
        System.arraycopy(writeInputCols, 0, this.writeInputCols, 0, writeInputCols.length);
    }

    public String[] getWriteOutputCols() {
        return writeOutputCols;
    }

    public void setWriteOutputCols(String[] writeOutputCols) {
        this.writeOutputCols = new String[writeOutputCols.length];
        System.arraycopy(writeOutputCols, 0, this.writeOutputCols, 0, writeOutputCols.length);
    }

    public String getWriteTblName() {
        return writeTblName;
    }

    public void setWriteTblName(String writeTblName) {
        set("writeTblName", writeTblName);
    }

    public String getHost() {
        return DBConnectionProvider.getHost();
    }

    public String getPass() {
        return DBConnectionProvider.getPass();
    }

    public int getPort() {
        return DBConnectionProvider.getPort();
    }

    public String getUser() {
        return DBConnectionProvider.getUser();
    }

    private boolean isFlushOnWrite;
    public void setFlushOnWrite(boolean isFlushOnWrite)
    {
        this.isFlushOnWrite = isFlushOnWrite;
    }

    public boolean isFlushOnWrite()
    {
        return isFlushOnWrite;
    }

    @Override
    public TrainingSet loadTrainingSet() throws Exception 
    {
        if(isStringNullOrEmpty(readDBName))
            throw new IllegalStateException("Read database name not set...");
        if(isStringNullOrEmpty(readTblName))
            throw new IllegalStateException("Read table name not set...");

        if(readInputCols == null)
            throw new IllegalStateException("Read input cols not set...");
        if(readInputCols.length == 0)
            throw new IllegalStateException("Read input cols not set...");
        if(readOutputCols == null)
            throw new IllegalStateException("Read output cols not set...");
        if(readOutputCols.length == 0)
            throw new IllegalStateException("Read output cols not set...");

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;        
        
        try
        {
            conn = new DBConnectionProvider().getConnection(readDBName);
            ps = conn.prepareStatement("select * from " + readTblName);
            rs = ps.executeQuery();

            String inputs, outputs;
            TrainingSet ts = new TrainingSet();
            while(rs.next())
            {
                inputs = "";
                for(String col : readInputCols)
                    inputs += rs.getDouble(col) + ", ";
                inputs = inputs.substring(0, inputs.length() - 2);

                outputs = "";
                for(String col : readOutputCols)
                    outputs += rs.getDouble(col) + ", ";
                outputs = outputs.substring(0, outputs.length() - 2);

                TrainingPattern tp = FileDataStore.parseLine(inputs + ";" + outputs);
                ts.add(tp);
            }
            return ts;
        }
        catch(Exception e)
        {
            throw e;
        }
        finally
        {
            if(conn != null)
                conn.close();
            if(rs != null)
                rs.close();
            if(ps != null)
                ps.close();
        }        
    }
    
    @Override
    public void saveTrainingSet(TrainingSet ts) throws Exception 
    {
        if(isStringNullOrEmpty(writeDBName))
            throw new IllegalStateException("Write database name not set...");
        if(isStringNullOrEmpty(writeTblName))
            throw new IllegalStateException("Write table name not set...");

        if(writeInputCols == null)
            throw new IllegalStateException("Write input cols not set...");
        if(writeInputCols.length == 0)
            throw new IllegalStateException("Write input cols not set...");
        if(writeOutputCols == null)
            throw new IllegalStateException("Write output cols not set...");
        if(writeOutputCols.length == 0)
            throw new IllegalStateException("Write output cols not set...");

        Connection conn = null;
        PreparedStatement ps = null;        

        try
        {
            conn = new DBConnectionProvider().getConnection(writeDBName);
            conn.setAutoCommit(false);
            
            if(isFlushOnWrite)
            {
                ps =  conn.prepareStatement("delete from " + writeTblName);
                ps.executeUpdate();
                ps.close();
            }

            ps = conn.prepareStatement("");
            for(TrainingPattern tp : ts.getTrainingPatterns())
            {
                if(tp.getInputData().length != writeInputCols.length)
                {
                    throw new IllegalArgumentException("Database write input " +
                            "cols and training data mismatch");
                }
                if(tp.getOutputData().length != writeOutputCols.length)
                {
                    throw new IllegalArgumentException("Database read Input " +
                            "cols and training data mismatch");
                }

                String sql = "insert into " + writeTblName +" (";
                for(String col : writeInputCols)
                    sql += col + ",";
                for(String col : writeOutputCols)
                    sql += col + ",";
                sql = sql.substring(0, sql.length() - 1);
                sql += ") values (";

                for(double data : tp.getInputData())
                    sql += "'" + data +"',";
                for(double data : tp.getOutputData())
                    sql += "'" + data +"',";
                sql = sql.substring(0, sql.length() - 1);
                sql += ")";          
                ps.addBatch(sql);                
            }
            
            ps.executeBatch();
            conn.commit();
            conn.setAutoCommit(true);
        }
        catch(Exception e)
        {
            throw e;
        }
        finally
        {
            if(conn != null)
                conn.close();
            if(ps != null)
                ps.close();
        }  
    }

    private MySQLConfigPanel pnl;
    @Override
    public DataStoreConfigPanel getConfigPanel() {
        if(pnl == null)
            pnl = new MySQLConfigPanel(this);
        return pnl;
    }

    private boolean isStringNullOrEmpty(String s)
    {
        if(s == null)
            return true;
        if(s.isEmpty())
            return true;
        return false;
    }

    @Override
    public String getName() {
        return "MySQL Data Store";
    }

    @Override
    public String getDescription() {
        return "Lets you write and read training data from" +
                " a MySQL database.\n" +
                " You can configure the database name, database table, " +
                " input and output columns from where the data is " +
                " to be read or written";
    }

    @Override
    public String getAuthor() {
        return "Ragha";
    }
}
