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

package com.tr8n.core;

import com.squareup.okhttp.OkHttpClient;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpClient {
    public static final String TR8N_API_PATH = "tr8n/api/";

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
        if (getApplication().getAccessToken() == null) {
            // TODO: check if access token is expired
            Map accessTokenData = getJSONMap("oauth/request_token", Utils.buildMap(
                    "client_id", getApplication().getKey(),
                    "client_secret", getApplication().getSecret(),
                    "grant_type", "client_credentials"),
                    Utils.buildMap("oauth", true)
            );

            getApplication().setAccessToken(Utils.buildMap(
                    "token", accessTokenData.get("access_token"),
                    "expires_at", new Date()
            ));
//            "expires_in": 7905428
        }

        return (String) getApplication().getAccessToken().get("token");
    }

    private void prepareParams(Map params, Map options) throws Exception {
        if (options != null && options.get("oauth") != null)
            return;

        params.put("access_token", this.getAccessToken());
    }

    public List getJSONList(String path, Map params) throws Exception {
        return (List) getJSON(path, params);
    }

    public Map<String, Object> getJSONMap(String path, Map params, Map options) throws Exception {
        return (Map<String, Object>) getJSON(path, params, options);
    }

    public Map<String, Object> getJSONMap(String path, Map params) throws Exception {
        return (Map<String, Object>) getJSON(path, params);
    }

    public Object getJSON(String path, Map params) throws Exception {
        return getJSON(path, params, null);
    }

    public Object getJSON(String path, Map params, Map options) throws Exception {
        String responseText = get(path, params, options);
        Map data = (Map) Utils.parseJSON(responseText);
        if (data.get("error") != null)
            throw new Exception((String) data.get("error"));

        return data;
    }

    public String get(String path, Map params, Map options) throws Exception {
        prepareParams(params, options);

        URL url = Utils.buildURL(getApplication().getHost(), TR8N_API_PATH + path, params);
        Tr8n.getLogger().debug("Requesting: " + url.toString());

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

            Tr8n.getLogger().debug("Received data: " + responseText);

            return responseText;
        } finally {
            if (in != null) in.close();
        }
    }

    public Object post(String path, Map params) throws Exception {
        return post(path, params, null);
    }

    public Object post(String path, Map params, Map options) throws Exception {
        URL url = Utils.buildURL(getApplication().getHost(), TR8N_API_PATH + path, Utils.buildMap("access_token", this.getAccessToken()));

        String bodyStr = Utils.buildQueryString(params);
        Tr8n.getLogger().debug(bodyStr);

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
            Tr8n.getLogger().debug(responseStr);
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