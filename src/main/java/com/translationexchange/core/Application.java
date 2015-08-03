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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Application extends Base {
    public static final String TREX_API_HOST = "https://api.translationexchange.com";

    /**
     * Current TrEx session
     */
    private Session session;

    /**
     * Application host - points to the Tr8nHub server
     */
    private String host;

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
     * Application tools
     */
    private Map<String, String> tools;

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

    public Application() {
    	super();
    }
    
    public Application(Map<String, Object> attributes) {
        super(attributes);
    }
    
    
    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Map<String, Object> getTokens() {
        return tokens;
    }

    public Long getThreshold() {
        return threshold;
    }

    public String getCss() {
        return css;
    }

    public Map<String, String> getShortcuts() {
        return shortcuts;
    }

    public Map<String, Boolean> getFeatures() {
        return features;
    }

    public List<Language> getLanguages() {
        return languages;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDefaultLocale(String defaultLocale) {
        this.defaultLocale = defaultLocale;
    }

    public void setTokens(Map<String, Object> tokens) {
        this.tokens = tokens;
    }

    public void setTranslatorLevel(Long translatorLevel) {
        this.translatorLevel = translatorLevel;
    }

    public void setThreshold(Long threshold) {
        this.threshold = threshold;
    }

    public void setCss(String css) {
        this.css = css;
    }

    public void setShortcuts(Map<String, String> shortcuts) {
        this.shortcuts = shortcuts;
    }

    public void setFeatures(Map<String, Boolean> features) {
        this.features = features;
    }

    public List<Language> getFeaturedLanguages() {
        return featuredLanguages;
    }

    public String getAccessToken() {
        return accessToken;
    }    
    
    public String getDefaultLocale() {
        return defaultLocale;
    }

    public Long getTranslatorLevel() {
        return translatorLevel;
    }
    
    protected Map<String, String> getTools() {
		return tools;
	}

	protected void setTools(Map<String, String> tools) {
		this.tools = tools;
	}
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
    public void updateAttributes(Map<String, Object> attributes) {
        if (attributes.get("token") != null)
            setAccessToken((String) attributes.get("token"));

        if (attributes.get("access_token") != null)
            setAccessToken((String) attributes.get("access_token"));
        
        if (attributes.get("host") != null)
            setHost((String) attributes.get("host"));
        
        if (attributes.get("key") != null)
            setKey((String) attributes.get("key"));

        setName((String) attributes.get("name"));
        setDescription((String) attributes.get("description"));
        setThreshold((Long) attributes.get("threshold"));
        setDefaultLocale((String) attributes.get("default_locale"));
        setTranslatorLevel((Long) attributes.get("translator_level"));
        setCss((String) attributes.get("css"));

        if (attributes.get("tokens") != null)
            setTokens(new HashMap<String, Object>((Map) attributes.get("tokens")));

        if (attributes.get("shortcuts") != null)
            setShortcuts(new HashMap<String, String>((Map) attributes.get("shortcuts")));

        if (attributes.get("features") != null)
            setFeatures(new HashMap<String, Boolean>((Map) attributes.get("features")));

        if (attributes.get("tools") != null)
            setTools(new HashMap<String, String>((Map) attributes.get("tools")));
        
        if (attributes.get("languages") != null) {
            for (Object data : ((List) attributes.get("languages"))) {
                addLanguage(new Language((Map) data));
            }
        }

        if (attributes.get("featured_locales") != null) {
            for (Object data : ((List) attributes.get("featured_locales"))) {
                addFeaturedLanguage((String) data);
            }
        }

        if (attributes.get("sources") != null) {
            for (Object data : ((List) attributes.get("sources"))) {
                addSource(new Source((Map) data));
            }
        }
    }

    /**
     * Loads application from the service
     */
    public void load() {
        try {
        	Tml.getLogger().debug("Loading application...");
        	
            this.updateAttributes(getHttpClient().getJSONMap("projects/current/definition", 
            		Utils.buildMap(),
            		Utils.buildMap("cache_key", "application")
            ));
            
            setLoaded(true);
        } catch (Exception ex) {
        	setLoaded(false);
//            Tml.getLogger().logException("Failed to load application", ex);
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
        return true;
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
     * @return
     */
    public String getTranslationsCacheKey(String locale) {
    	return locale + "/translations";
    }
    
    /**
     * Loads translations from the service for a given language and caches them in the application
     * @param language
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public void loadTranslations(Language language) {
        try {
            Map<String, Object> results = getHttpClient().getJSONMap("projects/current/translations", 
            		Utils.buildMap("locale", language.getLocale()),
            		Utils.buildMap("cache_key", getTranslationsCacheKey(language.getLocale()))
            );
			List keys = (List) results.get("translation_keys");
            for (Object key : keys) {
				Map<String, Object> attributes = new HashMap<String, Object>((Map)key);
                attributes.put("application", this);
                cacheTranslationKey(new TranslationKey(attributes));
            }
        } catch (Exception ex) {
            Tml.getLogger().logException(ex);
        }
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
    public synchronized void registerMissingTranslationKey(TranslationKey translationKey) {
    	registerMissingTranslationKey(translationKey, "undefined");
    }
    
    public synchronized void registerMissingTranslationKey(TranslationKey translationKey, String sourceKey) {
        if (!isKeyRegistrationEnabled())
            return;

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
        if (!isKeyRegistrationEnabled() || getMissingTranslationKeysBySources().size() == 0)
            return;

        Tml.getLogger().debug("Submitting missing translation keys...");

        List<Map<String, Object>> params = new ArrayList<Map<String, Object>>();

        List<String> sourceKeys = new ArrayList<String>(); 
        
        Iterator<Map.Entry<String, Map<String, TranslationKey>>> entries = missingTranslationKeysBySources.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, Map<String, TranslationKey>> entry = entries.next();
            String source = (String) entry.getKey();
            
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

        registerKeys(Utils.buildMap("source_keys", Utils.buildJSON(params)));
        
        this.missingTranslationKeysBySources.clear();

        // All source caches must be reset for all languages, since the keys have changed
        for (Language language : getLanguages()) {
            for (String sourceKey : sourceKeys) {
            	Tml.getCache().delete(Source.getCacheKey(language.getLocale(), sourceKey), null);
            }
        }            
    }

    /**
     * Registers keys on the server
     * @param map
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
     * @param locale
     * @return
     */
    public boolean isSupportedLocale(String locale) {
    	for (Language language : getLanguages()) {
    		if (language.getLocale().equals(locale))
    			return true;
    	}
    	return false;
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
        if (languagesByLocales == null)
            languagesByLocales = new HashMap<String, Language>();

        if (languagesByLocales.get(locale) == null) {
            languagesByLocales.put(locale, new Language(Utils.buildMap("application", this, "locale", locale)));
        }

        Language language = languagesByLocales.get(locale);
        if (!language.hasDefinition()) language.load();

        return language;
    }

    /**
     * Returns a map of sources by keys
     * 
     * @return
     */
    public Map<String, Source> getSourcesByKeys() {
        if (sourcesByKeys == null) {
            sourcesByKeys = new HashMap<String, Source>();
        }

        return sourcesByKeys;
    }
    
    /**
     * Get source with translations for a specific locale
     * @param key
     * @param locale
     * @return
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
     * @param language
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
     * @param locale
     */
    public void addFeaturedLanguage(String locale) {
        if (featuredLanguages == null)
            featuredLanguages = new ArrayList<Language>();

        if (languagesByLocales == null)
            languagesByLocales = new HashMap<String, Language>();

        Language language = languagesByLocales.get(locale);
        if (language!=null)
            featuredLanguages.add(language);
    }

    public void addSource(Source source) {
        getSourcesByKeys().put(source.getKey(), source);
    }

    /**
     * Caches translation key in the application scope for source fallback
     *
     * @param translationKey
     * @return
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

    private Map<String, TranslationKey> getTranslationKeys() {
        if (translationKeys == null)
            translationKeys = new HashMap<String, TranslationKey>();
        return translationKeys;
    }

    public TranslationKey getTranslationKey(String key) {
        return getTranslationKeys().get(key);
    }

    public void addTranslationKey(TranslationKey translationKey) {
        translationKey.setApplication(this);
        getTranslationKeys().put(translationKey.getKey(), translationKey);
    }

    public boolean isFeatureEnabled(String feature) {
    	if (getFeatures() == null)
    		return false;
    	
    	if (getFeatures().get(feature) == null)
    		return false;
    	
    	return getFeatures().get(feature);
    }
    
    public String getTools(String type) {
    	if (getTools() == null) return "";
    	return getTools().get(type);
    }
    
    public String getHost() {
        if (host == null)
            return TREX_API_HOST;
        return host;
    }

    public HttpClient getHttpClient() {
        if (httpClient == null)
            httpClient = new HttpClient(this);

        return httpClient;
    }

    public String toString() {
        return  this.name + " (" + this.key + ")";
    }

}
