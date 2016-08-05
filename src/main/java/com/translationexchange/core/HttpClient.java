
/**
 * Copyright (c) 2016 Translation Exchange, Inc. All rights reserved.
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

package com.translationexchange.core;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Request.Builder;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.translationexchange.core.cache.CacheVersion;
public class HttpClient {
    /** Constant <code>API_PATH="v1/"</code> */
	public static final String API_PATH = "v1/";
	public static final String EXTENSIONS_KEY = "extensions";
	
    /**
     * Current cache version
     */
	private CacheVersion cacheVersion = null;

	/**
     * Application that uses the HttpClient
     */
    private Application application;

    /**
     * 3rd Party client to be used in the implementation
     */
    private OkHttpClient client;

    /**
     * Default constructor
     *
     * @param application a {@link com.translationexchange.core.Application} object.
     */
    public HttpClient(Application application) {
        this.application = application;
    }

    /**
     * Instantiates and return OkHttp Client
     * @return
     */
    private OkHttpClient getOkHttpClient() {
        if (client == null) {
            client = new OkHttpClient();
            client.setConnectTimeout(10, TimeUnit.SECONDS);
            client.setWriteTimeout(10, TimeUnit.SECONDS);
            client.setReadTimeout(30, TimeUnit.SECONDS);            
        }
        return client;
    }

    /**
     * @return Access Token
     * @throws Exception
     */
    private String getAccessToken() throws Exception {
        return getApplication().getAccessToken();
    }

    /** 
     * @return Application Key
     * @throws Exception
     */
    private String getCdnPath(String path) throws Exception {
		if (!path.startsWith(File.separator))
			path = File.separator + path;
		
        return getApplication().getKey() + path;
    }
    
    /**
     * Prepares URL params
     * 
     * @param params
     * @param options
     * @throws Exception
     */
    private void prepareParams(Map<String, Object> params, Map<String, Object> options) throws Exception {
        if (options != null) {
        	if (options.get("oauth") != null)
        		return;
        }

        params.put("access_token", this.getAccessToken());
    }

	/**
	 * Requests data from URL and returns JSON Map
	 *
	 * @param path a {@link java.lang.String} object.
	 * @param params a {@link java.util.Map} object.
	 * @param options a {@link java.util.Map} object.
	 * @throws java.lang.Exception if any.
	 * @return a {@link java.util.Map} object.
	 */
	public Map<String, Object> getJSONMap(String path, Map<String, Object> params, Map<String, Object> options) throws Exception {
        return getJSON(path, params, options);
    }

	/**
	 * Requests data from URL and returns JSON Map
	 *
	 * @param path a {@link java.lang.String} object.
	 * @param params a {@link java.util.Map} object.
	 * @throws java.lang.Exception if any.
	 * @return a {@link java.util.Map} object.
	 */
	public Map<String, Object> getJSONMap(String path, Map<String, Object> params) throws Exception {
        return getJSON(path, params);
    }

    /**
     * <p>getJSON.</p>
     *
     * @param path a {@link java.lang.String} object.
     * @throws java.lang.Exception if any.
     * @return a {@link java.util.Map} object.
     */
    public Map<String, Object> getJSON(String path) throws Exception {
        return getJSON(path, Utils.buildMap());
    }
    
    /**
     * Requests data from URL and returns JSON Map
     *
     * @param path a {@link java.lang.String} object.
     * @param params a {@link java.util.Map} object.
     * @throws java.lang.Exception if any.
     * @return a {@link java.util.Map} object.
     */
    public Map<String, Object> getJSON(String path, Map<String, Object> params) throws Exception {
        return getJSON(path, params, Utils.buildMap());
    }

    /**
     * Checks if cache should be used
     *
     * @param options a {@link java.util.Map} object.
     * @return a boolean.
     */
    public boolean isLiveApi() {
    	if (getApplication().getAccessToken() == null)
    		return false;
    	
    	if (Tml.getConfig().isKeyRegistrationModeEnabled())
    		return true;
    	
		Session session = getApplication().getSession();
		if (session == null) return false;
		return session.isInlineModeEnabled();
    }
    
    /**
     * Checks if cache is enabled 
     * 
     * @param options
     * @return
     */
    public boolean isCacheEnabled(Map<String, Object> options) {
    	if (!Tml.getConfig().isCacheEnabled()) return false;
    	if (options == null) return false;

    	if (options.get("cache_key") == null) 
			return false;
    	
    	return true;
    }    
    
    /**
     * Returns cache version
     * 
     * @return
     */
    public CacheVersion getCacheVersion() {
		return cacheVersion;
	}

    /**
     * Sets cache version
     * 
     * @param cacheVersion
     */
	public void setCacheVersion(CacheVersion cacheVersion) {
		this.cacheVersion = cacheVersion;
	}
    
    /**
     * Verify that the current cache version is correct
     * Check it against the API 
     */
    private void verifyCacheVersion() throws Exception {
    	// If version has already been fetched in the current request, use it
    	if (getCacheVersion() != null)
    		return;
    	
    	setCacheVersion(new CacheVersion());

    	// Fetch from local cache
    	getCacheVersion().fetchFromCache();
    	
    	// If no version in cache or it is expired, fetch it from the CDN
    	if (getCacheVersion().isExpired()) {
    		Tml.getLogger().debug("Fetching version from CDN...");
    		getCacheVersion().updateFromCDN(getFromCDN("version", Utils.buildMap("uncompressed", true)));
    	}

		Tml.getLogger().debug("Cache version: " + cacheVersion.getVersion() + " " + cacheVersion.getExpirationMessage());
    }
    
    /**
     * Fetch data from the CDN
     * 
     * @param cacheKey
     * @return
     */
    private String getFromCDN(String cacheKey, Map<String, Object> options) throws Exception {
    	if (getCacheVersion().isUnreleased() && !cacheKey.equals("version"))
    		return null;
    	
    	try {
    		String cachePath = cacheKey;
    		
    		if (!cacheKey.startsWith(File.separator))
    			cachePath = File.separator + cachePath;
    		
    		if (cacheKey.equals("version")) {
    			cachePath = getCdnPath(cachePath) + ".json";
    		} else
    			cachePath = getCdnPath(getCacheVersion().getVersion() + cachePath) + ".json.gz";
    		
    		String response = get(Utils.buildURL(getApplication().getCdnHost(), cachePath), options); 
    		
    		// check if CDN responded with an error, and return an empty JSON result
    		if (response.indexOf("<?xml") != -1) {
				response = cacheKey.equals("version") ? null : "{}";
    		}
    		
    		return response; 
    	} catch (Exception ex) {
    		Tml.getLogger().debug("Failed to get from CDN " + cacheKey + " with error: " + ex.getMessage());
    		return cacheKey.equals("version") ? null : "{}";
    	}
    }
    
    /**
     * <p>getJSON.</p>
     *
     * @param path a {@link java.lang.String} object.
     * @param params a {@link java.util.Map} object.
     * @param options a {@link java.util.Map} object.
     * @throws java.lang.Exception if any.
     * @return a {@link java.util.Map} object.
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getJSON(String path, Map<String, Object> params, Map<String, Object> options) throws Exception {
    	String responseText = null;
    	String cacheKey = (String) options.get("cache_key");
    	Map<String, Object> result = null;
    	
    	// for live requests, process them immediately
    	if (isLiveApi()) 
        	return processJSONResponse(get(path, params, options), options);
    	
    	// if cache is not enabled, return null
    	if (!isCacheEnabled(options)) 
    		return null;
    	
    	// verify that cache version is up to date
		verifyCacheVersion();
		
		if (getCacheVersion().isUnreleased())
			return null;
		
		// get key with version prefix
		String versionedKey = getCacheVersion().getVersionedKey(cacheKey);
		
		responseText = (String) Tml.getCache().fetch(versionedKey, options);
		
		if (responseText != null)
			return processJSONResponse(responseText, options);
		
		// if no data in the local cache
		responseText = getFromCDN(cacheKey, options);

		if (responseText == null)
			return null;

		result = processJSONResponse(responseText, options);

		Map<String, Object> extensions = (Map<String, Object>) result.get(EXTENSIONS_KEY);

		// never store extension in cache
		if (extensions != null) {
			result.remove(EXTENSIONS_KEY);
			responseText = Utils.buildJSON(result);
			result.put(EXTENSIONS_KEY, extensions);
		}
		
		Tml.getCache().store(versionedKey, responseText, options);

		return result;
    }
    
    /**
     * Converts response text to JSON
     * 
     * @param responseText
     * @param options
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> processJSONResponse(String responseText, Map<String, Object> options) throws Exception {
    	String cacheKey = (String) options.get("cache_key");

    	Map<String, Object> result = (Map<String, Object>) Utils.parseJSON(responseText);
    	
    	if (!(result instanceof Map)) {
    		throw new Exception("Invalid response type: response must always be a map.");
    	}

    	Map<String, Object> data = (Map<String, Object>) Utils.parseJSON(responseText);
	
        if (data != null && data.get("error") != null) {
        	if (isCacheEnabled(options)) 
        		Tml.getCache().delete(cacheKey, options);
            throw new Exception((String) data.get("error"));
        }
    	
    	return result;
    }
    
    /**
     * Gets data from an API path.
     *
     * @param path a {@link java.lang.String} object.
     * @param params a {@link java.util.Map} object.
     * @param options a {@link java.util.Map} object.
     * @throws java.lang.Exception if any.
     * @return a {@link java.lang.String} object.
     */
    public String get(String path, Map<String, Object> params, Map<String, Object> options) throws Exception {
        prepareParams(params, options);
        return get(Utils.buildURL(getApplication().getHost(), API_PATH + path, params), options);
    }

    /**
     * Gets data from a URL
     *
     * @param url Where to load data from
     * @param options  Request options
     * @throws java.lang.Exception if any.
     * @return a {@link java.lang.String} object.
     */
    public String get(URL url, Map<String, Object> options) throws Exception {
        Tml.getLogger().debug("HTTP Get: " + url.toString());
        
        long t0 = new Date().getTime();
        
        Builder builder = new Request.Builder().url(url.toString()).header("User-Agent", Tml.getFullVersion());
        
        builder = builder.addHeader("Accept", "application/json");
        builder = builder.addHeader("Accept-Encoding", "gzip, deflate");
        
        Request request = builder.build();        
        Response response = getOkHttpClient().newCall(request).execute();
        
        long t1 = new Date().getTime();

        Tml.getLogger().debug("HTTP Get took: " + (t1-t0) + " mls");
        
        Boolean uncompressed = (Boolean) options.get("uncompressed");
        if (uncompressed != null && uncompressed.booleanValue())
        	return new String(response.body().bytes());
        
        return decompress(response.body().bytes());
    }

    /**
     * Posts to a path
     *
     * @param path a {@link java.lang.String} object.
     * @param params a {@link java.util.Map} object.
     * @throws java.lang.Exception if any.
     * @return a {@link java.lang.Object} object.
     */
    public Object post(String path, Map<String, Object> params) throws Exception {
        return post(path, params, null);
    }
    
    /**
     * Posts data to an API URL. Posts are never cached.
     *
     * @param path a {@link java.lang.String} object.
     * @param params a {@link java.util.Map} object.
     * @param options a {@link java.util.Map} object.
     * @throws java.lang.Exception if any.
     * @return a {@link java.lang.Object} object.
     */
    @SuppressWarnings("rawtypes")
    public Object post(String path, Map<String, Object> params, Map<String, Object> options) throws Exception {
        URL url = Utils.buildURL(getApplication().getHost(), API_PATH + path, Utils.buildMap("access_token", this.getAccessToken()));

        Tml.getLogger().debug("HTTP Post: " + url.toString());
        
        long t0 = new Date().getTime();

//        Tml.getLogger().debug(Utils.buildJSON(params));

        FormEncodingBuilder formBuilder = new FormEncodingBuilder();

        Iterator entries = params.entrySet().iterator();
        while (entries.hasNext()) {
			Map.Entry entry = (Map.Entry) entries.next();
			formBuilder = formBuilder.add((String) entry.getKey(), (String) entry.getValue());
        }
        RequestBody formBody = formBuilder.build();
                
        Builder builder = new Request.Builder().url(url.toString()).header("User-Agent", Tml.getFullVersion());
        builder = builder.post(formBody);
        Request request = builder.build();
        
        Response response = getOkHttpClient().newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
//        String responseStr = response.body().string(); 
//        Tml.getLogger().debug(responseStr);
        
        long t1 = new Date().getTime();
        
        Tml.getLogger().debug("HTTP Post took: " + (t1-t0) + " mls");
        
        return response.body().string();
    }

    /**
     * <p>Getter for the field <code>application</code>.</p>
     *
     * @return a {@link com.translationexchange.core.Application} object.
     */
    public Application getApplication() {
        return application;
    }

    /**
     * <p>Setter for the field <code>application</code>.</p>
     *
     * @param application a {@link com.translationexchange.core.Application} object.
     */
    public void setApplication(Application application) {
        this.application = application;
    }
    
    /**
     * Compress a string using GZIP
     *
     * @param str a {@link java.lang.String} object.
     * @throws java.io.IOException if any.
     * @return a {@link java.lang.String} object.
     */
    public static String compress(String str) throws IOException {
        if (str == null || str.length() == 0) {
            return str;
        }
//        System.out.println("String length : " + str.length());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(out);
        gzip.write(str.getBytes());
        gzip.close();
        String outStr = out.toString("ISO-8859-1");
//        System.out.println("Output String length : " + outStr.length());
        return outStr;
     }
    
    /**
     * Decompress a string using GZIP
     *
     * @throws java.io.IOException if any.
     * @param bytes an array of byte.
     * @return a {@link java.lang.String} object.
     */
    public static String decompress(byte[] bytes) throws IOException {
        GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(bytes));
        BufferedReader bf = new BufferedReader(new InputStreamReader(gis, "UTF-8"));
        String outStr = "";
        String line;
        while ((line=bf.readLine())!=null) {
          outStr += line;
        }
        Tml.getLogger().debug("Compressed: " + bytes.length + " Uncompressed: " + outStr.length());
        return outStr;
     }    
  
}
