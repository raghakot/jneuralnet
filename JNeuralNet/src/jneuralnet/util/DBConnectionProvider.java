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

import java.sql.*;
import java.util.ArrayList;

/**
 * A Utility class to manage connections for a MySQL Database.
 * @author Ragha
 */
public class DBConnectionProvider {

    private Connection connection = null;
    private static boolean isDriverInit = false;
    private static String user,  pass;
    private static String host;
    private static Integer port;

    public static String getHost() {
        return host;
    }

    public static void setHost(String host) {
        DBConnectionProvider.host = host;
    }

    public static String getPass() {
        return pass;
    }

    public static void setPass(String pass) {
        DBConnectionProvider.pass = pass;
    }

    public static Integer getPort() {
        return port;
    }

    public static void setPort(Integer port) {
        DBConnectionProvider.port = port;
    }

    public static String getUser() {
        return user;
    }

    public static void setUser(String user) {
        DBConnectionProvider.user = user;
    }

    public Connection getConnection(String db) throws Exception {
        checkParams();
        if(isStringNullOrEmpty(db))
            throw new IllegalArgumentException("Null or empty database name");

        if (connection != null) {
            return connection;
        } else {
            if (isDriverInit) {
                initDriver();
            }
            connection = DriverManager.getConnection(
                    "jdbc:mysql://" + host + ":" + port + "/" + db, user, pass);
            return connection;
        }
    }

    public Connection getConnectionWithoutDB() throws Exception {
        checkParams();
        if (connection != null) {
            return connection;
        } else {
            if (isDriverInit) {
                initDriver();
            }
            connection = DriverManager.getConnection(
                    "jdbc:mysql://" + host + ":" + port + "/", user, pass);
            return connection;
        }
    }

    private void checkParams() throws Exception {
        if (isStringNullOrEmpty(host)) {
            throw new IllegalStateException("Host is not set...");
        }
        if (port == null) {
            throw new IllegalStateException("Port is not set...");
        }
        if (isStringNullOrEmpty(user)) {
            throw new IllegalStateException("Username is not set...");
        }
        if (isStringNullOrEmpty(pass)) {
            throw new IllegalStateException("Password is not set...");
        }
    }

    private boolean isStringNullOrEmpty(String s) {
        if (s == null) {
            return true;
        }
        if (s.isEmpty()) {
            return true;
        }
        return false;
    }

    public static boolean checkConnection(String host, int port,
            String user, String pass) throws Exception
    {
        if (!isDriverInit) {
            initDriver();
        }

        Connection conn = null;
        try
        {
            conn = DriverManager.getConnection(
                "jdbc:mysql://" + host + ":" + port + "/", user, pass);
            return true;
        }
        catch(Exception e)
        {
            throw e;
        }
        finally
        {
            if(conn != null)
                conn.close();
        }        
    }

    private static void initDriver() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            isDriverInit = true;
        } catch (Exception e) {
            System.out.println("error init driver...");
        }
    }

    /*
    private static ResultSetMetaData getMetaData(
            String dbName, String tblName) throws Exception
    {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try
        {
            conn = new DBConnectionProvider().getConnection(dbName);
            ps = conn.prepareStatement("select * from "+tblName);
            rs = ps.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            return rsmd;
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
    */

    public static ArrayList<String> getDBList() throws Exception
    {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try
        {
            conn = new DBConnectionProvider().getConnectionWithoutDB();
            ps = conn.prepareStatement("show databases");
            rs = ps.executeQuery();

            ArrayList<String> arrDBList = new ArrayList<String>();
            while(rs.next())
            {
                arrDBList.add(rs.getString(1));
            }
            return arrDBList;
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

    public static ArrayList<String> getTables(String dbName) throws Exception
    {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try
        {
            conn = new DBConnectionProvider().getConnection(dbName);
            ps = conn.prepareStatement("show tables");
            rs = ps.executeQuery();

            ArrayList<String> arrTblList = new ArrayList<String>();
            while(rs.next())
            {
                arrTblList.add(rs.getString(1));
            }
            return arrTblList;
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
}
