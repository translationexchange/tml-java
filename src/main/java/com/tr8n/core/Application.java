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
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Application extends Base {
    public static final String TR8N_HOST = "https://tr8nhub.com";
    public static final String TR8N_API_PATH = "tr8n/api/";

    /**
     * Application host - points to the Tr8nHub server
     */
    String host;

    /**
     * Application key - must always be specified
     */
    String key;

    /**
     * Application secret - should only be used in development/test mode
     * Keys should only need to be registered during testing
     */
    String secret;

    /**
     * Application access token for API calls
     */
    Map<String, Object> accessToken;

    /**
     * Application name
     */
    String name;

    /**
     * Application description
     */
    String description;

    /**
     * Application default locale
     */
    String defaultLocale;

    /**
     * Default key level
     */
    Integer defaultLevel;

    /**
     * Application threshold
     */
    Integer threshold;

    /**
     * Application features
     */
    Map<String, Boolean> features;

    /**
     * Languages by locale
     */
    Map<String, Language> languagesByLocales;

    /**
     * Sources by keys
     */
    Map<String, Source> sourcesByKeys;

    /**
     * Application Translation keys
     */
    Map<String, TranslationKey> translationKeys;

    /**
     * Missing translation keys
     */
    Map<String, List> missingTranslationKeysBySources;

    /**
     * API Client
     */
    OkHttpClient client;

    public static Application init(String key, String secret) {
        return init(key, secret, null);
    }

    public static Application init(String key, String secret, String host) {
        if (host == null) host = TR8N_HOST;

        Tr8n.getCache().resetVersion();

        Tr8n.getLogger().debug("Initializing application...");

        Application app = new Application(Utils.buildMap(
            "host", host,
            "key", key,
            "secret", secret
        ));

        try {
            app.init();
        } catch (Exception ex) {
            Tr8n.getLogger().error("Failed to initialize application " + ex.toString());
            Tr8n.getLogger().error(ex);
        }

        return app;
    }

    public Application(Map attributes) {
        super(attributes);
    }

    public void updateAttributes(Map attributes) {
        if (attributes.get("host") != null)
            this.host = (String) attributes.get("host");
        if (attributes.get("key") != null)
            this.key = (String) attributes.get("key");
        if (attributes.get("secret") != null)
            this.secret = (String) attributes.get("secret");

        this.name = (String) attributes.get("name");
        this.description = (String) attributes.get("description");
        this.threshold = (Integer) attributes.get("threshold");
        this.defaultLocale = (String) attributes.get("default_locale");
        this.defaultLevel = (Integer) attributes.get("default_level");

        this.features = (Map<String, Boolean>) attributes.get("features");

//        has_many :features, :languages, :featured_locales, :sources, :components, :tokens, :css, :shortcuts
//
//        self.attributes[:languages] = []
//        if hash_value(attrs, :languages)
//        self.attributes[:languages] = hash_value(attrs, :languages).collect{ |l| Tr8n::Language.new(l.merge(:application => self)) }
//        end
//
//        self.attributes[:sources] = []
//        if hash_value(attrs, :sources)
//        self.attributes[:sources] = hash_value(attrs, :sources).collect{ |l| Tr8n::Source.new(l.merge(:application => self)) }
//        end
//
//        self.attributes[:components] = []
//        if hash_value(attrs, :components)
//        self.attributes[:components] = hash_value(attrs, :components).collect{ |l| Tr8n::Component.new(l.merge(:application => self)) }
//        end
//
//        @translation_keys         = {}
//        @sources_by_key           = {}
//        @components_by_key        = {}
//
//        @languages_by_locale      = nil
//        @missing_keys_by_sources  = nil
    }

    public String getAccessToken() throws Exception {
        if (this.accessToken == null) {
            // TODO: check if access token is expired
            Map accessTokenData = (Map) get("oauth/request_token", Utils.buildMap(
                                        "client_id", key,
                                        "client_secret", secret,
                                        "grant_type", "client_credentials"),
                                        Utils.buildMap("oauth", true)
                                  );

            this.accessToken = Utils.buildMap(
              "token", accessTokenData.get("access_token"),
              "expires_at", new Date()
            );
//            "expires_in": 7905428
        }

        return (String) this.accessToken.get("token");
    }

    public void init() throws Exception {
        getAccessToken();

        Map attributes = (Map) get("application", Utils.buildMap("client_id", key, "definition", "true"));
        this.updateAttributes(attributes);
    }

//    public void loadTranslations(String locale, Map options, )
//
//    - (void) loadTranslationsForLocale: (NSString *) locale
//    withOptions: (NSDictionary *) options
//    success: (void (^)()) success
//    failure: (void (^)(NSError *error)) failure;
//
    public void resetTranslationsCacheForLocale(String locale) {

    }

    public void resetTranslations() {

    }

    public Language language() {
        return language(defaultLocale);
    }

    public Language language(String local) {
        return null;
    }

    private OkHttpClient getOkHttpClient() {
        if (this.client == null) {
            this.client = new OkHttpClient();
        }
        return this.client;
    }

    public Object get(String path, Map params) throws Exception {
        return get(path, params, null);
    }

    public Object get(String path, Map params, Map options) throws Exception {
        if (options != null && options.get("oauth") == null) {
            params.put("access_token", this.getAccessToken());
        }
        URL url = Utils.buildURL(this.host, TR8N_API_PATH + path, params);
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

            Object data = Utils.parseJSON(responseText);

            if (options == null || options.get("class_name") == null)
                return data;

            Tr8n.getLogger().debug("Constructing object... ");

            // TODO: construct object
            return data;
        } finally {
            if (in != null) in.close();
        }
    }

    public void post(String path, Map params, Map options) throws Exception {
        URL url = Utils.buildURL(this.host, TR8N_API_PATH + path);
        byte[] body = Utils.buildQueryString(params).getBytes("UTF-8");
        HttpURLConnection connection = getOkHttpClient().open(url);
        OutputStream out = null;
        InputStream in = null;
        try {
            // Write the request.
            connection.setRequestMethod("POST");
            out = connection.getOutputStream();
            out.write(body);
            out.close();

            // Read the response.
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException("Unexpected HTTP response: "
                        + connection.getResponseCode() + " " + connection.getResponseMessage());
            }
            in = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            reader.readLine();
        } finally {
            if (out != null) out.close();
            if (in != null) in.close();
        }
    }


    public static void main(String[] args) {
        Application app = Application.init("37f812fac93a71088", "a9dc95ff798e6e1d1", "https://sandbox.tr8nhub.com");

    }

}
