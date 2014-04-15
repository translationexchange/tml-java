/*
 *  Copyright (c) 2014 Michael Berkovich, http://tr8nhub.com All rights reserved.
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */

package com.tr8n.core.cache;

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

import com.tr8n.core.Tr8n;
import com.tr8n.core.Utils;

public class FileCache extends CacheAdapter implements Cache {
	protected File applicationPath;
	protected File cachePath;
	
	public FileCache(Map<String, Object> config) {
		super(config);
	}

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
	        applicationPath = new File(path, Tr8n.getConfig().getApplicationName());
	        applicationPath.mkdirs();
		}
		return applicationPath;
	}

	protected File getCachePath() {
		if (cachePath == null) {
			cachePath = new File(getApplicationPath(), "Cache");
			cachePath.mkdirs();
	        Tr8n.getLogger().debug("Cache path: " + cachePath.toString());
		}
		return cachePath;
	}
	
	protected File getCachePath(String cacheKey) {
		List<String> parts = new ArrayList<String>(Arrays.asList(cacheKey.split(Pattern.quote("/"))));
		String fileName = parts.remove(parts.size()-1);

		File fileCachePath = getCachePath();
		if (parts.size() > 0)
			fileCachePath = new File(getCachePath(), Utils.join(parts.toArray(), File.separator));

		fileCachePath.mkdirs();
		return new File(fileCachePath, fileName + ".json");
	}
	
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
	
    public Object fetch(String key, Map<String, Object> options) {
    	File cacheFile = getCachePath(key);
    	if (!cacheFile.exists()) { 
    		Tr8n.getLogger().debug("Cache miss: " + key);
    		return null;
    	}

    	try {
    		Tr8n.getLogger().debug("Cache hit: " + key);
    		return readFile(cacheFile);	
    	} catch (Exception ex) {
    		Tr8n.getLogger().logException(ex);
    		return null;
    	}
    }
    
    protected void writeFile(File file, Object data) throws Exception {
    	PrintWriter writer = new PrintWriter(file, "UTF-8");
    	writer.print(data.toString());
    	writer.close();    	
    }
    
    public void store(String key, Object data, Map<String, Object> options) {
    	File cacheFile = getCachePath(key);
    	
    	try {
    		Tr8n.getLogger().debug("Writing cache to :" + cacheFile);
    		writeFile(cacheFile, data);	
    	} catch (Exception ex) {
    		Tr8n.getLogger().logException("Failed to write cache to file", ex);
    	}
    }

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
      
    public void reset() {
    	deleteDirectory(getCachePath());
    }
    
}