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
 */

package com.translationexchange.core;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
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

public class HttpClient {
    public static final String API_PATH = "v1/";
    public static final String UNRELEASED_VERSION = "0";
//	protected static String CDN_URL      = "https://cdn.translationexchange.com";
	protected static String CDN_URL      = "https://trex-snapshots.s3-us-west-1.amazonaws.com";

	private String cacheVersion = null;
	
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
     * @param application
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
    private String getCdnPath(String version) throws Exception {
        return getApplication().getKey() + "/" + version;
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
     * @param path
     * @param params
     * @param options
     * @return
     * @throws Exception
     */
	public Map<String, Object> getJSONMap(String path, Map<String, Object> params, Map<String, Object> options) throws Exception {
        return getJSON(path, params, options);
    }

    /**
     * Requests data from URL and returns JSON Map
     * 
     * @param path
     * @param params
     * @return
     * @throws Exception
     */
	public Map<String, Object> getJSONMap(String path, Map<String, Object> params) throws Exception {
        return getJSON(path, params);
    }

	/**
     * 
     * @param path
     * @return
     * @throws Exception
     */
    public Map<String, Object> getJSON(String path) throws Exception {
        return getJSON(path, Utils.buildMap());
    }
    
    /**
     * Requests data from URL and returns JSON Map
     * 
     * @param path
     * @param params
     * @return
     * @throws Exception
     */
    public Map<String, Object> getJSON(String path, Map<String, Object> params) throws Exception {
        return getJSON(path, params, Utils.buildMap());
    }

    /**
     * Checks if cache should be used
     * 
     * @param options
     * @return
     */
    public boolean isCacheEnabled(Map<String, Object> options) {
    	if (!Tml.getConfig().isCacheEnabled()) return false;
    	if (options == null) return false;

    	if (options.get("cache_key") == null) 
			return false;
    	
		Session session = getApplication().getSession(); 
		return !session.isInlineModeEnabled();
    }
    
    /**
     * Verify that the current cache version is correct
     * Check it against the API 
     */
    private void verifyCacheVersion() throws Exception {
    	if (cacheVersion != null)
    		return;
    	
    	// Fetch version from cache itself
    	cacheVersion = Tml.getCache().fetchVersion();
    	
    	// If no version in cache, fetch it from the API
    	if (cacheVersion == null) {
    		cacheVersion = get("projects/current/version", Utils.buildMap(), Utils.buildMap());
    		Tml.getCache().storeVersion(cacheVersion);
    	}

    	Tml.getCache().setVersion(cacheVersion);
    	Tml.getLogger().debug("Cache Version: " + Tml.getCache().getVersion());
    }
    
    /**
     * Fetch data from the CDN
     * 
     * @param cacheKey
     * @return
     */
    private String getFromCDN(String cacheKey, Map<String, Object> options) throws Exception {
    	String version = Tml.getCache().getVersion(); 
    	if (version == null || version.equals(UNRELEASED_VERSION))
    		return null;
    	
    	try {
    		if (!cacheKey.startsWith(File.separator))
    			cacheKey = File.separator + cacheKey;
    		
    		String responseText = get(Utils.buildURL(CDN_URL, getCdnPath(version) + cacheKey + ".json.gz"), options);

    		return responseText; 
    	} catch (Exception ex) {
    		return null;
    	}
    }
    
    /**
     * 
     * @param path
     * @param params
     * @param options
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getJSON(String path, Map<String, Object> params, Map<String, Object> options) throws Exception {
    	String responseText = null;
    	String cacheKey = (String) options.get("cache_key");
    	Map<String, Object> result = null;
    	
    	if (isCacheEnabled(options)) {
    		verifyCacheVersion();
    		
    		responseText = (String) Tml.getCache().fetch(cacheKey, options);
    		if (responseText == null) {
    			responseText = getFromCDN(cacheKey, options);

    			if (responseText == null) {
    				responseText = get(path, params, options);
    			}

    			result = processJSONResponse(responseText, options);

    			Map<String, Object> extensions = (Map<String, Object>) result.get("extensions");
    			// never store extension in cache
    			if (extensions != null) {
    				result.remove("extensions");
    				responseText = Utils.buildJSON(result);
    				result.put("extensions", extensions);
    			}
    			Tml.getCache().store(cacheKey, responseText, options);
    		} else {
    			result = processJSONResponse(responseText, options);
    		}

    		return result;
    	} 
    		
    	responseText = get(path, params, options);
    	return processJSONResponse(responseText, options);
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
     * @param path
     * @param params
     * @param options
     * @return
     * @throws Exception
     */
    public String get(String path, Map<String, Object> params, Map<String, Object> options) throws Exception {
        prepareParams(params, options);
        return get(Utils.buildURL(getApplication().getHost(), API_PATH + path, params), options);
    }

    /** 
     * Gets data from a URL
     * 
     * @param url
     * @return
     * @throws Exception
     */
    public String get(URL url, Map<String, Object> options) throws Exception {
        Tml.getLogger().debug("Requesting: " + url.toString());
        
        Builder builder = new Request.Builder().url(url.toString()).header("User-Agent", getUserAgent());
        
        builder = builder.addHeader("Accept", "application/json");
        builder = builder.addHeader("Accept-Encoding", "gzip, deflate");
        
        Request request = builder.build();        
        Response response = getOkHttpClient().newCall(request).execute();
        
        return decompress(response.body().bytes());
    }

    /**
     * Posts to a path
     * 
     * @param path
     * @param params
     * @return
     * @throws Exception
     */
    public Object post(String path, Map<String, Object> params) throws Exception {
        return post(path, params, null);
    }

    private String getUserAgent() {
    	return "tml-java v" + Tml.VERSION + " (OkHttp v2.4.0)";	
    }
    
    /**
     * Posts data to an API URL. Posts are never cached.
     * 
     * @param path
     * @param params
     * @param options
     * @return
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    public Object post(String path, Map<String, Object> params, Map<String, Object> options) throws Exception {
        URL url = Utils.buildURL(getApplication().getHost(), API_PATH + path, Utils.buildMap("access_token", this.getAccessToken()));

//        Tml.getLogger().debug(Utils.buildJSON(params));

        FormEncodingBuilder formBuilder = new FormEncodingBuilder();

        Iterator entries = params.entrySet().iterator();
        while (entries.hasNext()) {
			Map.Entry entry = (Map.Entry) entries.next();
			formBuilder = formBuilder.add((String) entry.getKey(), (String) entry.getValue());
        }
        RequestBody formBody = formBuilder.build();
                
        Builder builder = new Request.Builder().url(url.toString()).header("User-Agent", getUserAgent());
        builder = builder.post(formBody);
        Request request = builder.build();
        
        Response response = getOkHttpClient().newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
//        String responseStr = response.body().string(); 
//        Tml.getLogger().debug(responseStr);
        return response.body().string();
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }
    
    /**
     * Compress a string using GZIP 
     * 
     * @param str
     * @return
     * @throws IOException
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
     * @param str
     * @return
     * @throws IOException
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
