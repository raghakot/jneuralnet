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

/**
 * Represents a pluggable module...
 * The pluggable module or abstract class can either be external
 * (loaded from plugins folder) or a default impl provided in the lib...
 * 
 * @author Ragha
 */
public class Plugin<T>
{
    public static final String INBUILT = "Inbuilt";
    public static final String EXTERNAL = "External";

    private String type;
    private T plugin;

    public Plugin(String type, T plugin) {
        this.type = type;
        this.plugin = plugin;
    }

    public Plugin() {

    }        
    
    public T getPlugin() {
        return plugin;
    }

    public void setPlugin(T plugin) {
        this.plugin = plugin;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
