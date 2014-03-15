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
import java.util.*;

public class Application extends Base {
    public static final String TR8N_HOST = "https://tr8nhub.com";
    public static final String TR8N_API_PATH = "tr8n/api/";

    /**
     * Application host - points to the Tr8nHub server
     */
    private String host;

    /**
     * Application key - must always be specified
     */
    private String key;

    /**
     * Application secret - should only be used in development/test mode
     * Keys should only need to be registered during testing
     */
    private String secret;

    /**
     * Application access token for API calls
     */
    private Map<String, Object> accessToken;

    /**
     * Application name
     */
    private String name;

    /**
     * Application description
     */
    private String description;

    /**
     * Application default locale
     */
    private String defaultLocale;

    /**
     * Default data and decoration tokens
     */
    private Map<String, Object> tokens;

    /**
     * Default key level
     */
    private Long translatorLevel;

    /**
     * Application threshold
     */
    private Long threshold;

    /**
     * CSS classes for decorator
     */
    private String css;

    /**
     * Application shortcuts
     */
    private Map<String, String> shortcuts;

    /**
     * Application features
     */
    private Map<String, Boolean> features;

    /**
     * List of languages enabled for the application
     */
    private List<Language> languages;

    /**
     * List of application sources
     */
    private List<Source> sources;

    /**
     * List of application components
     */
    private List<Component> components;

    /**
     * List of featured locales
     */
    private List<String> featuredLocales;

    /**
     * Languages by locale
     */
    private Map<String, Language> languagesByLocales;

    /**
     * Sources by keys
     */
    private Map<String, Source> sourcesByKeys;

    /**
     * Components by keys
     */
    private Map<String, Component> componentsByKeys;

    /**
     * Application Translation keys
     */
    private Map<String, TranslationKey> translationKeys;

    /**
     * Missing translation keys
     */
    private Map<String, Map<String, TranslationKey>> missingTranslationKeysBySources;

    /**
     * Periodically send missing keys to the server
     */
    private TimerTask scheduler;

    /**
     * API Client
     */
    private OkHttpClient client;

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
        app.load();

//        app.scheduler = new TimerTask() {
//            @Override
//            public void run() {
////                app.sub
//            }
//        };

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
        this.threshold = (Long) attributes.get("threshold");
        this.defaultLocale = (String) attributes.get("default_locale");
        this.translatorLevel = (Long) attributes.get("translator_level");
        this.css = (String) attributes.get("css");

        if (attributes.get("tokens") != null)
            this.tokens = new HashMap<String, Object>((Map) attributes.get("tokens"));
        if (attributes.get("shortcuts") != null)
            this.shortcuts = new HashMap<String, String>((Map) attributes.get("shortcuts"));
        if (attributes.get("features") != null)
            this.features = new HashMap<String, Boolean>((Map) attributes.get("features"));
        if (attributes.get("featured_locales") != null)
            this.featuredLocales = new ArrayList<String>((List) attributes.get("featured_locales"));

        this.languages = new ArrayList<Language>();
        if (attributes.get("languages") != null) {
            for (Object data : ((List) attributes.get("languages"))) {
                this.languages.add(new Language((Map) data));
            }
        }

        this.sources = new ArrayList<Source>();
        if (attributes.get("sources") != null) {
            for (Object data : ((List) attributes.get("sources"))) {
                this.sources.add(new Source((Map) data));
            }
        }

        this.components = new ArrayList<Component>();
        if (attributes.get("components") != null) {
            for (Object data : ((List) attributes.get("components"))) {
                this.components.add(new Component((Map) data));
            }
        }

        this.sourcesByKeys =  new HashMap<String, Source>();
        this.componentsByKeys =  new HashMap<String, Component>();
        this.languagesByLocales = new HashMap<String, Language>();
    }

    /**
     * Loads application from the service
     */
    public void load() {
        try {
            Map attributes = (Map) get("application", Utils.buildMap("client_id", key, "definition", "true"));
            this.updateAttributes(attributes);
        } catch (Exception ex) {
            Tr8n.getLogger().logException("Failed to load application", ex);
        }
    }

    /**
     * When application is in production mode, secret should not be provided.
     * Without the secret, the app will not be submitting missing keys to the server,
     * and only use the default language values.
     *
     * @return true/false based on whether the app is in production mode
     */
    public boolean isKeyRegistrationEnabled() {
        return this.secret != null;
    }

    public void resetTranslationsCacheForLocale(String locale) {

    }

    public void resetTranslations() {

    }

    /**
     *
     * @return
     */
    private Map<String, Map<String, TranslationKey>> getMissingTranslationKeysBySources() {
        if (missingTranslationKeysBySources == null)
            missingTranslationKeysBySources = new HashMap<String, Map<String, TranslationKey>>();
        return missingTranslationKeysBySources;
    }

    /**
     *
     * @param translationKey
     * @param source
     */
    public synchronized void registerMissingTranslationKey(TranslationKey translationKey, Source source) {
        if (!isKeyRegistrationEnabled())
            return;

        Map translationKeys = getMissingTranslationKeysBySources().get(source.getKey());
        if (translationKeys == null) {
            translationKeys = new HashMap();
            getMissingTranslationKeysBySources().put(source.getKey(), translationKeys);
        }

        if (translationKeys.get(translationKey.getKey()) == null) {
            translationKeys.put(translationKey.getKey(), translationKey);
        }
    }

    /**
     *
     */
    public synchronized void submitMissingTranslationKeys() {
        if (!isKeyRegistrationEnabled() || getMissingTranslationKeysBySources().size() == 0)
            return;

        Map params = new HashMap();


        // TODO: finish implementation

    }


    /**
     *
     * @return
     */
    public Language getLanguage() {
        return getLanguage(defaultLocale);
    }

    /**
     *
     * @param locale
     * @return
     */
    public Language getLanguage(String locale) {
        if (this.languagesByLocales.get(locale) == null) {
            Language language = new Language(Utils.buildMap("application", this, "locale", locale));
            language.load();
            this.languagesByLocales.put(locale, language);
        }

        return this.languagesByLocales.get(locale);
    }


    /**
     *
     * @param key
     * @return
     */
    public Source getSource(String key) {
        return this.getSource(key, this.defaultLocale);
    }

    /**
     * Get source with translations for a specific locale
     * @param key
     * @param locale
     * @return
     */
    public Source getSource(String key, String locale) {
        if (this.sourcesByKeys.get(key) == null) {
            Source source = new Source(Utils.buildMap("application", this, "key", key, "locale", locale));
            source.load();
            this.sourcesByKeys.put(key, source);
        }

        return this.sourcesByKeys.get(key);
    }

    /**
     *  API Functions
     *
     */

    private OkHttpClient getOkHttpClient() {
        if (this.client == null) {
            this.client = new OkHttpClient();
        }
        return this.client;
    }

    private String getAccessToken() throws Exception {
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

    private void prepareParams(Map params, Map options) throws Exception {
        if (options != null && options.get("oauth") != null)
            return;

        params.put("access_token", this.getAccessToken());
    }

    public Object get(String path, Map params) throws Exception {
        return get(path, params, null);
    }

    public Object get(String path, Map params, Map options) throws Exception {
        prepareParams(params, options);

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

            Map data = (Map) Utils.parseJSON(responseText);
            if (data.get("error") != null)
                throw new Exception((String) data.get("error"));

            return data;
        } finally {
            if (in != null) in.close();
        }
    }

    public Object post(String path, Map params, Map options) throws Exception {
        prepareParams(params, options);

        URL url = Utils.buildURL(this.host, TR8N_API_PATH + path);
        byte[] body = Utils.buildQueryString(params).getBytes("UTF-8");

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
            reader.readLine();
            return reader.toString();
        } finally {
            if (out != null) out.close();
            if (in != null) in.close();
        }
    }

    public TranslationKey cacheTranslationKey(TranslationKey translationKey) {
        return translationKey;
    }

    public static void main(String[] args) {
        Application app = Application.init("37f812fac93a71088", "a9dc95ff798e6e1d1", "https://sandbox.tr8nhub.com");
        Language lang = app.getLanguage("ru");
        Source source = app.getSource("undefined");

    }

    public String getDefaultLocale() {
        return defaultLocale;
    }

    public Long getTranslatorLevel() {
        return translatorLevel;
    }

    public String toString() {
        return  this.name + "(" + this.key + ")";
    }

    private Map<String, TranslationKey> getTranslationKeys() {
        if (translationKeys == null)
            translationKeys = new HashMap<String, TranslationKey>();
        return translationKeys;
    }

    public TranslationKey getTranslationKey(String key) {
        return getTranslationKeys().get(key);
    }
}
