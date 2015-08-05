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
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import com.squareup.okhttp.OkHttpClient;

public class HttpClient {
    public static final String API_PATH = "v1/";
    public static final String UNRELEASED_VERSION = "0";
//	protected static String CDN_URL      = "https://cdn.translationexchange.com";
	protected static String CDN_URL      = "https://trex-snapshots.s3-us-west-1.amazonaws.com";

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
     * Prepares URL params
     * 
     * @param params
     * @param options
     * @throws Exception
     */
    private void prepareParams(Map<String, Object> params, Map<String, Object> options) throws Exception {
        if (options != null && options.get("oauth") != null)
            return;

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
    @SuppressWarnings("unchecked")
	public Map<String, Object> getJSONMap(String path, Map<String, Object> params, Map<String, Object> options) throws Exception {
        return (Map<String, Object>) getJSON(path, params, options);
    }

    /**
     * Requests data from URL and returns JSON Map
     * 
     * @param path
     * @param params
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
	public Map<String, Object> getJSONMap(String path, Map<String, Object> params) throws Exception {
        return (Map<String, Object>) getJSON(path, params);
    }

    
    /**
     * 
     * @param path
     * @return
     * @throws Exception
     */
    public Object getJSON(String path) throws Exception {
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
    public Object getJSON(String path, Map<String, Object> params) throws Exception {
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
     * Retrieves cache version from the API
     * 
     * @return
     */
    private String getCacheVersion() {
    	try {
    		return get("projects/current/version", Utils.buildMap(), Utils.buildMap());
    	} catch(Exception ex) {
    		Tml.getLogger().logException("Failed to retrieve cache version", ex);
    		return null;
    	}
    }
    
    /**
     * Verify that the current cache version is correct
     * Check it against the API 
     */
    private void verifyCacheVersion() {
    	if (Tml.getCache().getVersion() != null)
    		return;
    	
    	// Fetch version from cache itself
    	String currentVersion = Tml.getCache().fetchVersion();
    	
    	// If it is present, use it, otherwise fetch it from API
    	if (currentVersion != null)
    		Tml.getCache().setVersion(currentVersion);
    	else
    		Tml.getCache().storeVersion(getCacheVersion());
    	
    	Tml.getLogger().debug("Cache Version: " + Tml.getCache().getVersion());
    }
    
    /**
     * Fetch data from the CDN
     * 
     * @param cacheKey
     * @return
     */
    private String getFromCDN(String cacheKey) throws Exception {
    	String version = Tml.getCache().getVersion(); 
    	if (version == null || version.equals(UNRELEASED_VERSION))
    		return null;
    	
    	try {
    		if (!cacheKey.startsWith(File.separator))
    			cacheKey = File.separator + cacheKey;
    		
    		// TODO: switch to application key instead of token
    		return get(Utils.buildURL(CDN_URL, getAccessToken() + cacheKey + ".json"));
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
    public Object getJSON(String path, Map<String, Object> params, Map<String, Object> options) throws Exception {
    	String responseText = null;
    	String cacheKey = (String) options.get("cache_key");
    	
    	if (isCacheEnabled(options)) {
    		verifyCacheVersion();
    		
    		responseText = (String) Tml.getCache().fetch(cacheKey, options);
    		if (responseText == null) {
    			responseText = getFromCDN(cacheKey);
    			
    			if (responseText == null)
    				responseText = get(path, params, options);
    			
    			Tml.getCache().store(cacheKey, responseText, options);
    		}
    	} else {
    		responseText = get(path, params, options);
    	}
    	
    	Object result = (Map<String, Object>) Utils.parseJSON(responseText);
    	
    	if (result instanceof Map) {
    		Map<String, Object> data = (Map<String, Object>) Utils.parseJSON(responseText);
		
	        if (data.get("error") != null) {
	        	if (isCacheEnabled(options)) 
	        		Tml.getCache().delete(cacheKey, options);
	            throw new Exception((String) data.get("error"));
	        }
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
        return get(Utils.buildURL(getApplication().getHost(), API_PATH + path, params));
    }

    /** 
     * Gets data from a URL
     * 
     * @param url
     * @return
     * @throws Exception
     */
    public String get(URL url) throws Exception {
        Tml.getLogger().debug("Requesting: " + url.toString());
		HttpURLConnection connection = getOkHttpClient().open(url);
		InputStream in = null;
		try {
			in = connection.getInputStream();
		    ByteArrayOutputStream out = new ByteArrayOutputStream();
		    byte[] buffer = new byte[1024];
		    for (int count; (count = in.read(buffer)) != -1; ) {
		         out.write(buffer, 0, count);
		    }
		    String responseText = new String(out.toByteArray(), "UTF-8");
		
		    Tml.getLogger().debug("Received data: " + responseText);

		    return responseText;
		} finally {
			if (in != null) in.close();
		}
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

    /**
     * Posts data to an API URL. Posts are never cached.
     * 
     * @param path
     * @param params
     * @param options
     * @return
     * @throws Exception
     */
    public Object post(String path, Map<String, Object> params, Map<String, Object> options) throws Exception {
        URL url = Utils.buildURL(getApplication().getHost(), API_PATH + path, Utils.buildMap("access_token", this.getAccessToken()));

        String bodyStr = Utils.buildQueryString(params);
        Tml.getLogger().debug(bodyStr);

        byte[] body = bodyStr.getBytes("UTF-8");

        HttpURLConnection connection = getOkHttpClient().open(url);
        OutputStream out = null;
        InputStream in = null;
        try {
            connection.setRequestMethod("POST");
            out = connection.getOutputStream();
            out.write(body);
            out.close();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException("Unexpected HTTP response: "
                        + connection.getResponseCode() + " " + connection.getResponseMessage());
            }
            in = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            StringBuilder builder = new StringBuilder();
            String aux = "";
            while ((aux = reader.readLine()) != null) {
                builder.append(aux);
            }
            String responseStr = builder.toString();
            Tml.getLogger().debug(responseStr);
            return responseStr;
        } finally {
            if (out != null) out.close();
            if (in != null) in.close();
        }
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }
}
