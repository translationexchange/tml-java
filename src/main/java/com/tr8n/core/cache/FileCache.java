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

import org.apache.commons.lang.StringUtils;

import com.tr8n.core.Cache;
import com.tr8n.core.Tr8n;

public class FileCache extends Cache {
	private File applicationPath;
	private File cachePath;
	
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
	        Tr8n.getLogger().debug("Application path: " + applicationPath.toString());
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
		File fileCachePath = null;
		if (parts.size() > 1) {
			String fileName = parts.remove(parts.size()-1);
			fileCachePath = new File(getCachePath(), StringUtils.join(parts.toArray(), File.separator));
			fileCachePath.mkdirs();
			fileCachePath = new File(fileCachePath, fileName + ".json");
		} else {
			fileCachePath = new File(getCachePath(), parts.get(0) + ".json");
		}
		
		return fileCachePath;
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
    		Tr8n.getLogger().logException(ex);
    	}
    }

    public void delete(String key, Map<String, Object> options) {
    	File cacheFile = getCachePath(key);
    	if (!cacheFile.exists()) 
    		return;

    	cacheFile.delete();
    }

}
