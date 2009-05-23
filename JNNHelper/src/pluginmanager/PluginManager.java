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

package pluginmanager;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.HashMap;
import org.openide.util.Lookup;

/**
 * A Utility class to manage plugins in a desktop based application...
 * @author Ragha
 */
public class PluginManager
{
    private static PluginManager pm = null;

    //Class is not instantiable...use factory method
    private PluginManager()
    {
        
    }

    //Returns the default plugin manager for this application...
    public static PluginManager getDefault()
    {
        //Lazy initialization...
        if(pm == null)
            pm = new PluginManager();

        return pm;
    }
  
    /**
     * Rescans the given folder and adds all the Jar files (plugins)
     * to class path...simply ignores if a jar file is already added...
     *
     * @param path The folder containing plugins...
     * @throws java.io.IOException
     */
    public void rescan(String path) throws IOException
    {
        File pluginFolder = new File(path);        
        File plugins[] = pluginFolder.listFiles(new FileFilter()
        {
            public boolean accept(File pathname)
            {
                if(pathname.getPath().endsWith(".jar"))
                    return true;
                else
                    return false;
            }
        });

        for(File f : plugins)
        {            
            addPlugin(f.toURI().toURL());
        }        
    }

    /**
     * The url (preferably jar) to be added to the classpath.
     * This is like an install plugin thingy...
     * Must be used in place of {@link #rescan(java.lang.String) rescan} method
     * if a single new plugin is to be installed...gives good performance ups
     *
     * @param url The url to be added to classpath
     */
    public void addPlugin(URL url) throws IOException
    {
        addURLToClassPath(url);
    }

    /**
     * Helps to get implementations of an interface as a HashMap...
     * Implementations of <T> is available if the plugin jars
     * are loaded onto the classpath.
     *
     * @param <T> The interface whose implementations map is required...
     *
     * @author Ragha
     */
    public static class ItemsMap<T>
    {        
        //Added param cls so that we dont use a deprecated method in
        //Template constructor...
        /**
         * Creates a map for using plugins with lazy loading...
         * @param cls
         * @return
         */
        public HashMap<String, Lookup.Item<T>> getAllImplLazily(Class cls)
        {
            Lookup.Result<T> result = Lookup.getDefault()
                .lookup(new Lookup.Template<T>(cls));
            
            //using allItems to avoid class initialization...
            Collection<? extends Lookup.Item<T>> items = result.allItems();
            
            HashMap<String, Lookup.Item<T>> map =
                    new HashMap<String, Lookup.Item<T>>();
            for(Lookup.Item<T> item : items)
            {
                map.put(item.getDisplayName(), item);
            }
            
            return map;
        }

        /**
         * Creates a map of instantiated plugins...
         * @param cls
         *
         * @param useToString keep it as true if plugins
         * have an overriden method.Creates a Hashmap with
         * obj.toString as its key.
         *
         * @return
         */
        public HashMap<String, T> getAllImpl(Class cls, boolean useToString)
        {
            Lookup.Result<T> result = Lookup.getDefault()
                .lookup(new Lookup.Template<T>(cls));
            
            Collection<? extends T> items = result.allInstances();

            HashMap<String, T> map = new HashMap<String, T>();
            for(T item : items)
            {
                if(useToString)
                    map.put(item.toString(), item);
                else
                    map.put(item.getClass().getName(), item);
            }

            return map;
        }
    }

    /**
     * To avoid un-necessary object creation on method calls...
     */
    private static final Class[] parameters = new Class[]{URL.class};
    /**
     * Adds a URL, prefferably a JAR file to classpath. If URL already exists,
     * then this methos simply returns...
     * 
     * @param u The URL t be added to classpath
     * @throws java.io.IOException
     */
    private void addURLToClassPath(URL u) throws IOException
    {
        URLClassLoader sysloader = (URLClassLoader)ClassLoader.getSystemClassLoader();

        boolean isAdded = false;
        for(URL url : sysloader.getURLs())
        {
            if(url.equals(u))
                isAdded = true;
        }
        if(isAdded)
            return;

        Class sysclass = URLClassLoader.class;
        try
        {
            Method method = sysclass.getDeclaredMethod("addURL",parameters);
            method.setAccessible(true);
            method.invoke(sysloader, new Object[]{ u });
        }
        catch (Throwable t)
        {
            t.printStackTrace();
            throw new IOException("Error, could not add URL to system classloader");
        }
    }
}
