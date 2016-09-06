/**
 * Copyright (c) 2016 Translation Exchange, Inc. All rights reserved.
 * <p/>
 * _______                  _       _   _             ______          _
 * |__   __|                | |     | | (_)           |  ____|        | |
 * | |_ __ __ _ _ __  ___| | __ _| |_ _  ___  _ __ | |__  __  _____| |__   __ _ _ __   __ _  ___
 * | | '__/ _` | '_ \/ __| |/ _` | __| |/ _ \| '_ \|  __| \ \/ / __| '_ \ / _` | '_ \ / _` |/ _ \
 * | | | | (_| | | | \__ \ | (_| | |_| | (_) | | | | |____ >  < (__| | | | (_| | | | | (_| |  __/
 * |_|_|  \__,_|_| |_|___/_|\__,_|\__|_|\___/|_| |_|______/_/\_\___|_| |_|\__,_|_| |_|\__, |\___|
 * __/ |
 * |___/
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * @author Michael Berkovich
 * @version $Id: $Id
 */

package com.translationexchange.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.translationexchange.core.languages.Language;

public class Application extends Base {
    /**
     * Constant <code>TREX_API_HOST="https://api.translationexchange.com"</code>
     */
    public static final String TREX_API_HOST = "https://api.translationexchange.com";

    /**
     * Constant <code>TREX_CDN_HOST="https://cdn.translationexchange.com"</code>
     */
    public static final String TREX_CDN_HOST = "https://cdn.translationexchange.com";

    /**
     * Constant <code>TREX_AUTH_URL="https://sandbox-gateway.translationexchange.com/?s=android"</code>
     */
    public static final String TREX_AUTH_URL = "https://sandbox-gateway.translationexchange.com/?s=android";

    /**
     * Constant <code>UNDEFINED_SOURCE="undefined"</code>
     */
    public static final String UNDEFINED_SOURCE = "undefined";

    /**
     * Current TrEx session
     */
    private Session session;

    /**
     * Application host
     */
    private String host;

    /**
     * CDN host
     */
    private String cdnHost;

    /**
     * Auth url
     */
    private String authUrl;

    /**
     * Application key - must always be specified
     */
    private String key;

    /**
     * Application access token for API calls
     */
    private String accessToken;

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
     * Application threshold
     */
    private Long threshold;

    /**
     * CSS classes for decorator
     */
    private String css;

    /**
     * Application features
     */
    private Map<String, Boolean> features;

    /**
     * List of languages enabled for the application. The languages are used for the language selector.
     * The do not contain all of the language details. When you get an individual language from an
     * application, the language will be reloaded from the server to get all rules and definitions.
     */
    private List<Language> languages;

    /**
     * List of featured locales
     */
    private List<Language> featuredLanguages;

    /**
     * Languages by locale
     */
    private Map<String, Language> languagesByLocales;

    /**
     * Sources by keys
     */
    private Map<String, Source> sourcesByKeys;

    /**
     * Application Translation keys
     */
    private Map<String, TranslationKey> translationKeys;

    /**
     * Missing translation keys
     */
    private Map<String, Map<String, TranslationKey>> missingTranslationKeysBySources;

    /**
     * API Client
     */
    private HttpClient httpClient;

    /**
     * Default constructor
     */
    public Application() {
        super();
    }

    /**
     * <p>Constructor for Application.</p>
     *
     * @param attributes a {@link java.util.Map} object.
     */
    public Application(Map<String, Object> attributes) {
        super(attributes);
    }


    /**
     * <p>Getter for the field <code>key</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getKey() {
        return key;
    }

    /**
     * <p>Getter for the field <code>name</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getName() {
        return name;
    }

    /**
     * <p>Getter for the field <code>description</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getDescription() {
        return description;
    }

    /**
     * <p>Getter for the field <code>tokens</code>.</p>
     *
     * @return a {@link java.util.Map} object.
     */
    public Map<String, Object> getTokens() {
        return tokens;
    }

    /**
     * <p>Getter for the field <code>threshold</code>.</p>
     *
     * @return a {@link java.lang.Long} object.
     */
    public Long getThreshold() {
        return threshold;
    }

    /**
     * <p>Getter for the field <code>css</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getCss() {
        return css;
    }

    /**
     * <p>Getter for the field <code>features</code>.</p>
     *
     * @return a {@link java.util.Map} object.
     */
    public Map<String, Boolean> getFeatures() {
        return features;
    }

    /**
     * <p>Getter for the field <code>languages</code>.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<Language> getLanguages() {
        return languages;
    }

    /**
     * <p>Getter for the field <code>session</code>.</p>
     *
     * @return a {@link com.translationexchange.core.Session} object.
     */
    public Session getSession() {
        return session;
    }

    /**
     * <p>Setter for the field <code>session</code>.</p>
     *
     * @param session a {@link com.translationexchange.core.Session} object.
     */
    public void setSession(Session session) {
        this.session = session;
    }

    /**
     * <p>Setter for the field <code>host</code>.</p>
     *
     * @param host a {@link java.lang.String} object.
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * <p>Setter for the field <code>host</code>.</p>
     *
     * @param cdnHost a {@link java.lang.String} object.
     */
    public void setCdnHost(String cdnHost) {
        this.cdnHost = cdnHost;
    }

    /**
     * <p>Setter for the field <code>key</code>.</p>
     *
     * @param key a {@link java.lang.String} object.
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * <p>Setter for the field <code>accessToken</code>.</p>
     *
     * @param accessToken a {@link java.lang.String} object.
     */
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    /**
     * <p>Setter for the field <code>name</code>.</p>
     *
     * @param name a {@link java.lang.String} object.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * <p>Setter for the field <code>description</code>.</p>
     *
     * @param description a {@link java.lang.String} object.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * <p>Setter for the field <code>defaultLocale</code>.</p>
     *
     * @param defaultLocale a {@link java.lang.String} object.
     */
    public void setDefaultLocale(String defaultLocale) {
        this.defaultLocale = defaultLocale;
    }

    /**
     * <p>Setter for the field <code>tokens</code>.</p>
     *
     * @param tokens a {@link java.util.Map} object.
     */
    public void setTokens(Map<String, Object> tokens) {
        this.tokens = tokens;
    }

    /**
     * <p>Setter for the field <code>threshold</code>.</p>
     *
     * @param threshold a {@link java.lang.Long} object.
     */
    public void setThreshold(Long threshold) {
        this.threshold = threshold;
    }

    /**
     * <p>Setter for the field <code>css</code>.</p>
     *
     * @param css a {@link java.lang.String} object.
     */
    public void setCss(String css) {
        this.css = css;
    }

    /**
     * <p>Setter for the field <code>features</code>.</p>
     *
     * @param features a {@link java.util.Map} object.
     */
    public void setFeatures(Map<String, Boolean> features) {
        this.features = features;
    }

    /**
     * <p>Getter for the field <code>featuredLanguages</code>.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<Language> getFeaturedLanguages() {
        return featuredLanguages;
    }

    /**
     * <p>Getter for the field <code>accessToken</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getAccessToken() {
        return accessToken;
    }

    /**
     * <p>Getter for the field <code>defaultLocale</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getDefaultLocale() {
        return defaultLocale;
    }


    /**
     * {@inheritDoc}
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public void updateAttributes(Map<String, Object> attributes) {
        if (attributes.get("key") != null)
            setKey((String) attributes.get("key"));

        if (attributes.get("token") != null)
            setAccessToken((String) attributes.get("token"));

        if (attributes.get("access_token") != null)
            setAccessToken((String) attributes.get("access_token"));

        if (attributes.get("host") != null)
            setHost((String) attributes.get("host"));

        if (attributes.get("cdn_host") != null)
            setCdnHost((String) attributes.get("cdn_host"));

        if (attributes.get("auth_url") != null)
            setCdnHost((String) attributes.get("auth_url"));

        setName((String) attributes.get("name"));
        setDescription((String) attributes.get("description"));
        setThreshold((Long) attributes.get("threshold"));
        setDefaultLocale((String) attributes.get("default_locale"));
        setCss((String) attributes.get("css"));

        if (attributes.get("tokens") != null)
            setTokens(new HashMap<String, Object>((Map) attributes.get("tokens")));

        if (attributes.get("features") != null) {
            Map map = new HashMap<String, Boolean>((Map) attributes.get("features"));
            setFeatures(map);
//            if(map.containsKey("inline_translations") && getSession()!=null){
//                Translator translation = new Translator();
//                translation.setInline((boolean) map.get("inline_translations"));
//                getSession().setCurrentTranslator(translation);
//            }

        }

        if (attributes.get("languages") != null) {
            for (Object data : ((List) attributes.get("languages"))) {
                Language language = new Language((Map) data);
                addLanguage(language);

//                if (!language.hasDefinition()) {
//                    language.load();
//                }
            }
        }

        if (attributes.get("sources") != null) {
            for (Object data : ((List) attributes.get("sources"))) {
                Source source = new Source((Map) data);
//                source.load(null);
                addSource(source);
            }
        }

        if (attributes.get("extensions") != null) {
            loadExtensions((Map<String, Object>) attributes.get("extensions"));
        }
    }

    /**
     * Loads application from the service with extra parameters
     *
     * @param params Options for loading application
     */
    public void load(Map<String, Object> params) {
        try {
            Tml.getLogger().debug("Loading application...");
            Map<String, Object> data = getHttpClient().getJSONMap("projects/" + getKey() + "/definition",
                    params,
                    Utils.buildMap("cache_key", "application")
            );
            if (data == null) {
                setDefaultLocale(Tml.getConfig().getDefaultLocale());
                addLanguage(Tml.getConfig().getDefaultLanguage());
                Tml.getLogger().debug("No release has been published or no cache has been provided");
                setLoaded(false);
            } else {
                this.updateAttributes(data);
                setLoaded(true);
            }
        } catch (Exception ex) {
            setLoaded(false);
            addLanguage(Tml.getConfig().getDefaultLanguage());
            Tml.getLogger().logException("Failed to load application", ex);
        }
    }

    /**
     * Loads application from the service
     */
    public void load() {
        load(Utils.buildMap());
    }

    /**
     * Only happens during API calls,
     *
     * @param extensions
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private void loadExtensions(Map<String, Object> extensions) {
        String sourceLocale = getDefaultLocale();

        if (extensions.get("languages") != null) {
            Map<String, Object> languages = (Map<String, Object>) extensions.get("languages");
            Iterator entries = languages.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry entry = (Map.Entry) entries.next();
                String locale = (String) entry.getKey();
                if (!locale.equals(getDefaultLocale()))
                    sourceLocale = locale;

                Map<String, Object> data = (Map<String, Object>) entry.getValue();

                Language language = getLanguagesByLocale().get(locale);
                if (language == null) {
                    language = new Language(Utils.buildMap("application", this));
                    getLanguagesByLocale().put(locale, language);
                }
                language.updateAttributes(data);
                language.setLoaded(true);
            }
        }

        if (extensions.get("sources") != null) {
            Map<String, Object> sources = (Map<String, Object>) extensions.get("sources");
            Iterator entries = sources.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry entry = (Map.Entry) entries.next();
                String key = (String) entry.getKey();
                Map<String, Object> data = (Map<String, Object>) entry.getValue();
                Source source = getSourcesByKeys().get(key);
                if (source == null) {
                    source = new Source(Utils.buildMap("application", this, "key", key, "locale", sourceLocale));
                    getSourcesByKeys().put(key, source);
                }
                source.updateTranslationKeys(data);
                source.setLoaded(true);
            }
        }
    }

    /**
     * Returns the first accepted locale from the application languages
     *
     * @param locale Set of locales to be searched for
     * @return a {@link java.lang.String} object.
     */
    public String getFirstAcceptedLocale(String locale) {
        if (locale == null)
            return getDefaultLocale();

        String[] locales = locale.split(",");

        for (String loc : locales) {
            if (getLanguagesByLocale().get(loc) != null)
                return loc;
        }

        return getDefaultLocale();
    }

    /**
     * <p>isKeyRegistrationEnabled.</p>
     *
     * @return true/false based on whether the app is in translation mode
     */
    public boolean isKeyRegistrationEnabled() {
        if (Tml.getConfig().isKeyRegistrationModeEnabled())
            return true;

        if (getSession() == null)
            return false;

        return getSession().isInlineModeEnabled();
    }

    /**
     * Resets cached translation keys for the application scope
     */
    public void resetTranslations() {
        this.translationKeys = null;
        this.sourcesByKeys = null;
    }

    /**
     * Returns translations cache key
     *
     * @param locale a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public String getTranslationsCacheKey(String locale) {
        return locale + "/translations";
    }

    /**
     * <p>updateTranslationKeys.</p>
     *
     * @param language a {@link com.translationexchange.core.languages.Language} object.
     * @param data     a {@link java.util.Map} object.
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void updateTranslationKeys(Language language, Map<String, Object> data) {
        if (data == null || data.isEmpty()) {
            return;
        }
        Iterator entries = ((Map) data.get("results")).entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry entry = (Map.Entry) entries.next();
            String key = (String) entry.getKey();
            List<Map<String, Object>> keyTranslations = (List<Map<String, Object>>) entry.getValue();

            TranslationKey tkey = getTranslationKey(key);

            if (tkey == null) {
                tkey = new TranslationKey(key);
                addTranslationKey(tkey);
            }

            List<Translation> translations = new ArrayList<Translation>();
            for (Map<String, Object> translationData : (List<Map<String, Object>>) keyTranslations) {
                Translation translation = new Translation(translationData);
                String locale = (String) translationData.get("locale");

                if (locale == null)
                    locale = language.getLocale();

                translation.setLanguage(getLanguage(locale));

                if (tkey.getLabel() == null || tkey.getLabel().equals("")) {
                    tkey.setLabel(translation.getLabel());
                    tkey.setLocale(translation.getLanguage().getLocale());
                }

                translations.add(translation);
            }
            tkey.setTranslations(language.getLocale(), translations);
            addTranslationKey(tkey);
        }
    }


    /**
     * Loads translations from the service for a given language and caches them in the application
     *
     * @param language a {@link com.translationexchange.core.languages.Language} object.
     */
    public void loadTranslations(Language language) {
        try {
            this.updateTranslationKeys(language, getHttpClient().getJSONMap("projects/current/translations",
                    Utils.buildMap("all", "true", "locale", language.getLocale()),
                    Utils.buildMap("cache_key", getTranslationsCacheKey(language.getLocale()))
            ));
        } catch (Exception ex) {
            Tml.getLogger().logException(ex);
        }
    }

    /**
     * @return
     */
    public Map<String, Map<String, TranslationKey>> getMissingTranslationKeysBySources() {
        if (missingTranslationKeysBySources == null)
            missingTranslationKeysBySources = new HashMap<String, Map<String, TranslationKey>>();
        return missingTranslationKeysBySources;
    }

    /**
     * <p>registerMissingTranslationKey.</p>
     *
     * @param translationKey a {@link com.translationexchange.core.TranslationKey} object.
     */
    public synchronized void registerMissingTranslationKey(TranslationKey translationKey) {
        registerMissingTranslationKey(translationKey, "undefined");
    }

    /**
     * <p>registerMissingTranslationKey.</p>
     *
     * @param translationKey a {@link com.translationexchange.core.TranslationKey} object.
     * @param sourceKey      a {@link java.lang.String} object.
     */
    public synchronized void registerMissingTranslationKey(TranslationKey translationKey, String sourceKey) {
//        if (!isKeyRegistrationEnabled())
//            return;

        Map<String, TranslationKey> translationKeys = getMissingTranslationKeysBySources().get(sourceKey);
        if (translationKeys == null) {
            translationKeys = new HashMap<String, TranslationKey>();
            getMissingTranslationKeysBySources().put(sourceKey, translationKeys);
        }

        if (translationKeys.get(translationKey.getKey()) == null) {
            translationKeys.put(translationKey.getKey(), translationKey);
        }
    }

    /**
     * Submits missing translations keys to the server
     */
    public synchronized void submitMissingTranslationKeys() {
        if (getMissingTranslationKeysBySources().size() == 0)
            return;
//        if (!isKeyRegistrationEnabled() || getMissingTranslationKeysBySources().size() == 0)
//            return;

        Tml.getLogger().debug("Submitting missing translation keys...");

        List<Map<String, Object>> params = new ArrayList<Map<String, Object>>();

        List<String> sourceKeys = new ArrayList<String>();

        Iterator<Map.Entry<String, Map<String, TranslationKey>>> entries = missingTranslationKeysBySources.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, Map<String, TranslationKey>> entry = entries.next();
            String source = entry.getKey();

            if (!sourceKeys.contains(source))
                sourceKeys.add(source);

            Map<String, TranslationKey> translationKeys = entry.getValue();
            List<Object> keys = new ArrayList<Object>();

            for (Object object : translationKeys.values()) {
                TranslationKey translationKey = (TranslationKey) object;
                keys.add(translationKey.toMap());
            }

            params.add(Utils.buildMap("source", source, "keys", keys));
        }

        registerKeys(Utils.buildMap("source_keys", Utils.buildJSON(params), "app_id", getKey()));

        this.missingTranslationKeysBySources.clear();
    }

    /**
     * Registers keys on the server
     *
     * @param map a {@link java.util.Map} object.
     * @return a boolean.
     */
    public boolean registerKeys(Map<String, Object> map) {
        try {
            getHttpClient().post("sources/register_keys", map);
            return true;
        } catch (Exception ex) {
            Tml.getLogger().logException("Failed to register missing translation keys", ex);
            return false;
        }
    }

    /**
     * Checks if the locale is in the list of supported locales
     *
     * @param locale a {@link java.lang.String} object.
     * @return a boolean.
     */
    public boolean isSupportedLocale(String locale) {
        for (Language language : getLanguages()) {
            if (language.getLocale().equals(locale))
                return true;
        }
        return false;
    }

    /**
     * <p>getLanguage.</p>
     *
     * @return a {@link com.translationexchange.core.languages.Language} object.
     */
    public Language getLanguage() {
        return getLanguage(defaultLocale);
    }

    /**
     * Returns languages by locale map
     *
     * @return a {@link java.util.Map} object.
     */
    protected Map<String, Language> getLanguagesByLocale() {
        if (languagesByLocales == null)
            languagesByLocales = new HashMap<String, Language>();
        return languagesByLocales;
    }

    /**
     * <p>getLanguage.</p>
     *
     * @param locale a {@link java.lang.String} object.
     * @return a {@link com.translationexchange.core.languages.Language} object.
     */
    public Language getLanguage(String locale) {
        if (getLanguagesByLocale().get(locale) == null) {
            getLanguagesByLocale().put(locale, new Language(Utils.buildMap("application", this, "locale", locale)));
        }

        Language language = getLanguagesByLocale().get(locale);
        if (!language.hasDefinition()) {
            language.load();
        }
        return language;
    }

    /**
     * Returns a map of sources by keys
     *
     * @return a {@link java.util.Map} object.
     */
    public Map<String, Source> getSourcesByKeys() {
        if (sourcesByKeys == null) {
            sourcesByKeys = new HashMap<String, Source>();
        }

        return sourcesByKeys;
    }

    /**
     * Get source with translations for a specific locale
     *
     * @param key     a {@link java.lang.String} object.
     * @param locale  a {@link java.lang.String} object.
     * @param options a {@link java.util.Map} object.
     * @return a {@link com.translationexchange.core.Source} object.
     */
    public Source getSource(String key, String locale, Map<String, Object> options) {
        if (getSourcesByKeys().get(key) == null) {
            Source source = new Source(Utils.buildMap("application", this, "key", key, "locale", locale));
            source.load(options);
            getSourcesByKeys().put(key, source);
        }

        return getSourcesByKeys().get(key);
    }

    /**
     * Adds a language to the list of application languages.
     * The language may contain basic information or entire definition.
     *
     * @param language a {@link com.translationexchange.core.languages.Language} object.
     */
    public void addLanguage(Language language) {
        if (languages == null)
            languages = new ArrayList<Language>();

        if (languagesByLocales == null)
            languagesByLocales = new HashMap<String, Language>();

        language.setApplication(this);
        languages.add(language);
        languagesByLocales.put(language.getLocale(), language);
    }

    /**
     * Adds a locale to a list of featured languages.
     *
     * @param locale a {@link java.lang.String} object.
     */
    public void addFeaturedLanguage(String locale) {
        if (featuredLanguages == null)
            featuredLanguages = new ArrayList<Language>();

        if (languagesByLocales == null)
            languagesByLocales = new HashMap<String, Language>();

        Language language = languagesByLocales.get(locale);
        if (language != null)
            featuredLanguages.add(language);
    }

    /**
     * Adds a new source
     *
     * @param source a {@link com.translationexchange.core.Source} object.
     */
    public void addSource(Source source) {
        getSourcesByKeys().put(source.getKey(), source);
    }

    /**
     * Caches translation key in the application scope for source fallback
     *
     * @param translationKey a {@link com.translationexchange.core.TranslationKey} object.
     * @return a {@link com.translationexchange.core.TranslationKey} object.
     */
    public TranslationKey cacheTranslationKey(TranslationKey translationKey) {
        TranslationKey cachedKey = getTranslationKey(translationKey.getKey());
        if (cachedKey != null) {
            for (String locale : translationKey.getTranslationLocales()) {
                List<Translation> translations = translationKey.getTranslations(locale);
                Language language = getLanguage(locale);
                cachedKey.setTranslations(language.getLocale(), translations);
            }
            return cachedKey;
        }

        addTranslationKey(translationKey);
        return translationKey;
    }

    /**
     * Returns translation key map
     *
     * @return
     */
    private Map<String, TranslationKey> getTranslationKeys() {
        if (translationKeys == null)
            translationKeys = new HashMap<String, TranslationKey>();
        return translationKeys;
    }

    /**
     * Returns a translation key by hash
     *
     * @param key a {@link java.lang.String} object.
     * @return a {@link com.translationexchange.core.TranslationKey} object.
     */
    public TranslationKey getTranslationKey(String key) {
        return getTranslationKeys().get(key);
    }

    /**
     * Adds a new translation key to the application
     *
     * @param translationKey a {@link com.translationexchange.core.TranslationKey} object.
     */
    public synchronized void addTranslationKey(TranslationKey translationKey) {
        translationKey.setApplication(this);
        getTranslationKeys().put(translationKey.getKey(), translationKey);
    }

    /**
     * Checks if feature is enabled
     *
     * @param feature a {@link java.lang.String} object.
     * @return a boolean.
     */
    public boolean isFeatureEnabled(String feature) {
        if (getFeatures() == null)
            return false;

        if (getFeatures().get(feature) == null)
            return false;

        return getFeatures().get(feature);
    }

    /**
     * Returns API host
     *
     * @return a {@link java.lang.String} object.
     */
    public String getHost() {
        if (host == null)
            return TREX_API_HOST;
        return host;
    }

    /**
     * Returns CDN host
     *
     * @return a {@link java.lang.String} object.
     */
    public String getCdnHost() {
        if (cdnHost == null)
            return TREX_CDN_HOST;
        return cdnHost;
    }

    /**
     * Returns AUTH url
     *
     * @return a {@link java.lang.String} object.
     */
    public String getAuthUrl() {
        String url = TREX_AUTH_URL;
        if (authUrl != null)
            url = authUrl;
        url += "&app_id=" + key;
        return url;
//        return TREX_AUTH_URL + "app_id=";
//        return authUrl;
    }

    /**
     * Returns HTTP client
     *
     * @return a {@link com.translationexchange.core.HttpClient} object.
     */
    public HttpClient getHttpClient() {
        if (httpClient == null)
            httpClient = new HttpClient(this);

        return httpClient;
    }

    /**
     * Returns a string representation of the object
     *
     * @return a {@link java.lang.String} object.
     */
    public String toString() {
        return this.name + " (" + this.key + ")";
    }

}
