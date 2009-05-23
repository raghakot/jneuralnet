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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Represents a set of pluggable modules...
 * @author Ragha
 */
public abstract class PluginMap<T>
{
    private HashMap<String, Plugin<T>> mapPlugins;
    private HashMap<String, Plugin<T>> mapDefaultPlugins;

    public PluginMap()
    {
        mapPlugins = new HashMap<String, Plugin<T>>();
        mapDefaultPlugins = new HashMap<String, Plugin<T>>();
        for(T plugin : getDefault().values())
        {
            mapDefaultPlugins.put(plugin.toString(), new Plugin<T>(
                    Plugin.INBUILT, plugin));
        }
    }

    private boolean isScannedOnce;
    /**
     *
     * @param cls The class type of the plugin map to be returned
     * @param rescan If true...it adds any new plugins found in the classpath.
     * has additional info of the type...ie, if its an inbuild or external plugin...
     * 
     * @return
     * TODO check return on 1st time
     */
    public HashMap<String, Plugin<T>> getWrappedPluginMap(Class cls, boolean rescan)
    {
        if(!isScannedOnce)
        {
            rescan = true;
            isScannedOnce = true;
        }

        if(rescan)
        {
            mapPlugins.clear();
            HashMap<String, T> mapExternal = new PluginManager
                    .ItemsMap<T>().getAllImpl(cls, true);
            for(String s : mapExternal.keySet())
            {
                T plg = mapExternal.get(s);
                mapPlugins.put(s, new Plugin<T>(Plugin.EXTERNAL, plg));
            }
            mapPlugins.putAll(mapDefaultPlugins);
        }
        
        return mapPlugins;
    }

    public HashMap<String, T> getUnWrappedPluginMap(Class cls, boolean rescan)
    {
        getWrappedPluginMap(cls, rescan);
        HashMap<String, T> map = new HashMap<String, T>();
        for(String key : mapPlugins.keySet())
            map.put(key, mapPlugins.get(key).getPlugin());
        return map;
    }

    /**
     * Hash addition info regarding the type of the plugin...ie,
     * inbuilt or external.
     * @param cls
     * @param rescan
     * @return
     */
    public List<Plugin<T>> getWrappedPluginList(Class cls, boolean rescan)
    {
        getWrappedPluginMap(cls, rescan);
        List<Plugin<T>> arrPlugins = new ArrayList<Plugin<T>>();
        for(Plugin<T> plg : mapPlugins.values())
            arrPlugins.add(plg);
        return arrPlugins;
    }

    public List<T> getUnWrappedPluginList(Class cls, boolean rescan)
    {
        getWrappedPluginMap(cls, rescan);
        List<T> arrPlugins = new ArrayList<T>();
        for(Plugin<T> plg : mapPlugins.values())
            arrPlugins.add(plg.getPlugin());
        return arrPlugins;
    }

    protected abstract HashMap<String, T> getDefault();
}
