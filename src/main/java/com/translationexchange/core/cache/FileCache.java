
/**
 * Copyright (c) 2015 Translation Exchange, Inc. All rights reserved.
 *
 *  _______                  _       _   _             ______          _
 * |__   __|                | |     | | (_)           |  ____|        | |
 *    | |_ __ __ _ _ __  ___| | __ _| |_ _  ___  _ __ | |__  __  _____| |__   __ _ _ __   __ _  ___
 *    | | '__/ _` | '_ \/ __| |/ _` | __| |/ _ \| '_ \|  __| \ \/ / __| '_ \ / _` | '_ \ / _` |/ _ \
 *    | | | | (_| | | | \__ \ | (_| | |_| | (_) | | | | |____ >  < (__| | | | (_| | | | | (_| |  __/
 *    |_|_|  \__,_|_| |_|___/_|\__,_|\__|_|\___/|_| |_|______/_/\_\___|_| |_|\__,_|_| |_|\__, |\___|
 *                                                                                        __/ |
 *                                                                                       |___/
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * @author Berk
 * @version $Id: $Id
 */

package com.translationexchange.core.cache;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import com.translationexchange.core.Tml;
import com.translationexchange.core.Utils;
public class FileCache extends CacheAdapter implements Cache {
	protected File applicationPath;
	protected File cachePath;
	
	/**
	 * <p>Constructor for FileCache.</p>
	 *
	 * @param config a {@link java.util.Map} object.
	 */
	public FileCache(Map<String, Object> config) {
		super(config);
	}

	/**
	 * <p>Getter for the field <code>applicationPath</code>.</p>
	 *
	 * @return a {@link java.io.File} object.
	 */
	protected File getApplicationPath() {
		if (applicationPath == null) {
			String path = null;
	        String osName = System.getProperty("os.name").toLowerCase();
	        if (osName.indexOf("windows")>-1) {
	            path = System.getenv("APPDATA");
	//        } else if (osName.indexOf("mac")>-1) {
	//            path = System.getProperty("user.home");
	        } else { //anything else
	            path = System.getProperty("user.home");
	        }
	        applicationPath = new File(path, Tml.getConfig().getApplicationName());
	        applicationPath.mkdirs();
		}
		return applicationPath;
	}

	/**
	 * <p>Getter for the field <code>cachePath</code>.</p>
	 *
	 * @return a {@link java.io.File} object.
	 */
	protected File getCachePath() {
		if (cachePath == null) {
			cachePath = new File(getApplicationPath(), "Cache");
			cachePath.mkdirs();
	        Tml.getLogger().debug("Cache path: " + cachePath.toString());
		}
		return cachePath;
	}
	
	/**
	 * <p>Getter for the field <code>cachePath</code>.</p>
	 *
	 * @param cacheKey a {@link java.lang.String} object.
	 * @return a {@link java.io.File} object.
	 */
	protected File getCachePath(String cacheKey) {
		List<String> parts = new ArrayList<String>(Arrays.asList(cacheKey.split(Pattern.quote("/"))));
		String fileName = parts.remove(parts.size()-1);

		File fileCachePath = getCachePath();
		if (parts.size() > 0)
			fileCachePath = new File(getCachePath(), Utils.join(parts.toArray(), File.separator));

		fileCachePath.mkdirs();
		return new File(fileCachePath, fileName + ".json");
	}
	
	/**
	 * <p>readFile.</p>
	 *
	 * @param file a {@link java.io.File} object.
	 * @return a {@link java.lang.String} object.
	 * @throws java.lang.Exception if any.
	 */
	protected String readFile(File file) throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(file));
	    try {
			BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
	        StringBuilder sb = new StringBuilder();
			String str;
			while ((str = in.readLine()) != null) {
	            sb.append(str);
//	            sb.append(System.getProperty("line.separator"));
			}
			in.close();
	        return sb.toString();
	    } catch (Exception ex) {
	    	return null;
	    } finally {
	        br.close();
	    }		
	}
	
    /** {@inheritDoc} */
    public Object fetch(String key, Map<String, Object> options) {
    	File cacheFile = getCachePath(key);
    	if (!cacheFile.exists()) { 
    		Tml.getLogger().debug("Cache miss: " + key);
    		return null;
    	}

    	try {
    		Tml.getLogger().debug("Cache hit: " + key);
    		return readFile(cacheFile);	
    	} catch (Exception ex) {
    		Tml.getLogger().logException(ex);
    		return null;
    	}
    }
    
    /**
     * <p>writeFile.</p>
     *
     * @param file a {@link java.io.File} object.
     * @param data a {@link java.lang.Object} object.
     * @throws java.lang.Exception if any.
     */
    protected void writeFile(File file, Object data) throws Exception {
    	PrintWriter writer = new PrintWriter(file, "UTF-8");
    	writer.print(data.toString());
    	writer.close();    	
    }
    
    /** {@inheritDoc} */
    public void store(String key, Object data, Map<String, Object> options) {
    	File cacheFile = getCachePath(key);
    	
    	try {
    		Tml.getLogger().debug("Writing cache to :" + cacheFile);
    		writeFile(cacheFile, data);	
    	} catch (Exception ex) {
    		Tml.getLogger().logException("Failed to write cache to file", ex);
    	}
    }

    /** {@inheritDoc} */
    public void delete(String key, Map<String, Object> options) {
    	File cacheFile = getCachePath(key);

    	if (options!=null && options.get("directory") != null && options.get("directory").equals(Boolean.TRUE)) {
    		cacheFile = new File(cacheFile.getAbsolutePath().replaceAll(Pattern.quote(".json"), ""));
        	if (!cacheFile.exists()) return;
        	if (cacheFile.isDirectory())
    			deleteDirectory(cacheFile);
    	} else {	
        	if (!cacheFile.exists()) return;
    		cacheFile.delete();
    	}
    }

    /**
     * <p>deleteDirectory.</p>
     *
     * @param directory a {@link java.io.File} object.
     * @return a boolean.
     */
    public static boolean deleteDirectory(File directory) {
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (null!=files) {
	            for (int i=0; i<files.length; i++) {
	                if (files[i].isDirectory()) {
	                    deleteDirectory(files[i]);
	                } else {
	                    files[i].delete();
	                }
	            }
            }
        }
        return directory.delete();
    }
      
    /**
     * <p>resetVersion.</p>
     */
    public void resetVersion() {
    	deleteDirectory(getCachePath());
    }
    
}
