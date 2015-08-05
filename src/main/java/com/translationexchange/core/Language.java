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

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Language extends Base {

    /**
     * Holds reference to the application it belongs to
     */
    private Application application;

    /**
     * Language locale based on Tr8n notation
     */
    private String locale;

    /**
     * Language name in English
     */
    private String englishName;

    /**
     * Language name in the native form
     */
    private String nativeName;

    /**
     * Whether the language rtl or ltr
     */
    private Boolean rightToLeft;

    /**
     * Url of the language flag image
     */
    private String flagUrl;

    /**
     * Hash of all language contexts
     */
    private Map<String, LanguageContext> contexts;

    /**
     * Hash of all language cases
     */
    private Map<String, LanguageCase> cases;

    /**
     * Default constructor
     */
    public Language() {
    	super();
    }

    /**
     * Default language constructor
     * @param attributes
     */
    public Language(Map<String, Object> attributes) {
        super(attributes);
    }
    
    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public String getNativeName() {
        return nativeName;
    }

    public void setNativeName(String nativeName) {
        this.nativeName = nativeName;
    }

    public Boolean isRightToLeft() {
        return rightToLeft;
    }

    public void setRightToLeft(Boolean rightToLeft) {
        this.rightToLeft = rightToLeft;
    }

    public String getFlagUrl() {
        return flagUrl;
    }

    public void setFlagUrl(String flagUrl) {
        this.flagUrl = flagUrl;
    }

    public Map<String, LanguageContext> getContexts() {
    	if (contexts == null)
    		contexts = new HashMap<String, LanguageContext>();
    	
        return contexts;
    }

    public Map<String, LanguageCase> getCases() {
    	if (cases == null)
    		cases = new HashMap<String, LanguageCase>();
    	
        return cases;
    }
    
    /**
     *
     * @param attributes
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public void updateAttributes(Map<String, Object> attributes) {
        if (attributes.get("application") != null)
            setApplication((Application) attributes.get("application"));

        setLocale((String) attributes.get("locale"));
        setEnglishName((String) attributes.get("english_name"));
        setNativeName((String) attributes.get("native_name"));
        setRightToLeft((Boolean) attributes.get("right_to_left"));
        setFlagUrl((String) attributes.get("flag_url"));

        if (attributes.get("contexts") != null) {
            Iterator entries = ((Map) attributes.get("contexts")).entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry entry = (Map.Entry) entries.next();
                LanguageContext context = new LanguageContext((Map) entry.getValue());
                context.setKeyword((String)entry.getKey());
                addContext(context);
            }
        }

        if (attributes.get("cases") != null) {
            Iterator entries = ((Map) attributes.get("cases")).entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry entry = (Map.Entry) entries.next();
                LanguageCase lcase = new LanguageCase((Map) entry.getValue());
                lcase.setKeyword((String)entry.getKey());
                addLanguageCase(lcase);
            }
        }
    }

    /**
     * Returns cache key of the language
     * @return
     */
    public String getCacheKey() {
    	return getLocale() + File.separator + "language";
    }
    
    /**
     * Loads language from the server
     */
    public void load() {
        try {
            this.updateAttributes(getApplication().getHttpClient().getJSONMap("languages/" + getLocale() + "/definition", 
        		Utils.buildMap(),
        		Utils.buildMap("cache_key", getCacheKey())
            ));
            setLoaded(true);
        } catch (Exception ex) {
        	setLoaded(false);
            Tml.getLogger().logException(ex);
        }
    }


    /**
     * Adds a context for the language
     * @param languageContext
     */
    public void addContext(LanguageContext languageContext) {
        if (contexts == null)
            contexts = new HashMap<String, LanguageContext>();
        
        languageContext.setLanguage(this);
        
        contexts.put(languageContext.getKeyword(), languageContext);
    }


    /**
     * Returns language context based on the keyword
     * @param keyword
     * @return
     */
    public LanguageContext getContextByKeyword(String keyword) {
        if (contexts == null)
            return null;
        return this.contexts.get(keyword);
    }

    /**
     * Returns language context based on the token name
     * @param tokenName
     * @return
     */
    public LanguageContext getContextByTokenName(String tokenName) {
        for (LanguageContext context : getContexts().values()) {
            if (context.isApplicableToTokenName(tokenName))
                return context;
        }
        return null;
    }

    /**
     * Returns language case based on the keyword
     * @param keyword
     * @return
     */
    public LanguageCase getLanguageCaseByKeyword(String keyword) {
        if (cases == null)
            return null;
        return cases.get(keyword);
    }

    /**
     * Adds a language case for the language
     * @param languageCase
     */
    public void addLanguageCase(LanguageCase languageCase) {
        if (cases == null)
            cases = new HashMap<String, LanguageCase>();
        
        languageCase.setLanguage(this);
        
        cases.put(languageCase.getKeyword(), languageCase);
    }

    /**
     * Languages are loaded without definition by default, this will tell if the language has definition or it needs to be loaded
     * @return
     */
    public boolean hasDefinition() {
        return !getContexts().isEmpty();
    }

    /**
     * Returns a value from options or block options
     * @param key
     * @param options
     * @param defaultValue
     * @return
     */
    private Object getOptionsValue(String key, Map<String, Object> options, Object defaultValue) {
        Object value = null;
        if (options != null) {
            value = options.get(key);
            if (value!=null) return value;
        }

        value = getApplication().getSession().getBlockOption(key);
        if (value!=null) return value;

        return defaultValue;
    }


    /**
     * Creates a new translation key
     * 
     * @param keyHash
     * @param label
     * @param description
     * @param options
     * @return
     */
    private TranslationKey createTranslationKey(String keyHash, String label, String description, Map<String, Object> options) {
        Map<String, Object> attributes = Utils.buildMap(
                "application", getApplication(),
                "key", keyHash,
                "label", label,
                "description", description,
                "locale", getOptionsValue("locale", options, getApplication().getDefaultLocale()),
                "level", getOptionsValue("level", options, getApplication().getTranslatorLevel())
        );

        return new TranslationKey(attributes);
    }

    /**
     *
     * @param label
     * @return
     */
    public Object translate(String label) {
       return translate(label, "");
    }
    
    /**
     *
     * @param label
     * @param description
     * @return
     */
    public Object translate(String label, String description) {
       return translate(label, description, null, null);
    }

	/**
	 *
	 * @param label
	 * @param tokens
	 * @return
	 */
    public Object translate(String label, Map<String, Object> tokens) {
       return translate(label, "", tokens, null);
    }    
    
    /**
     * Translation method
     * @param label
     * @param description
     * @param tokens
     * @param options
     * @return
     */
    public Object translate(String label, String description, Map<String, Object> tokens, Map<String, Object> options) {
        String keyHash = TranslationKey.generateKey(label, description);

        if (getApplication().isKeyRegistrationEnabled()) {
            String sourceKey = (String) getOptionsValue("source", options, getApplication().getSession().getCurrentSource());

            // Application keys without a source belong directly to the application, the source does not need to be loaded
            if (sourceKey == null) {
                TranslationKey matchedKey = getApplication().getTranslationKey(keyHash);
                if (matchedKey != null) return matchedKey.translate(this, tokens, options);

                TranslationKey tempKey = createTranslationKey(keyHash, label, description, options);
                getApplication().registerMissingTranslationKey(tempKey);
                getApplication().cacheTranslationKey(tempKey);
                return tempKey.translate(this, tokens, options);
            } 
            
            // Source based keys in mobile or desktop environments require sources to be pre-loading in a separate thread
            Source source = getApplication().getSource(sourceKey, this.getLocale(), options);
            if (source != null) {
                TranslationKey matchedKey = source.getTranslationKey(keyHash);
                
                if (matchedKey != null)  
                	return matchedKey.translate(this, tokens, options);

                HashMap<String, Object> opts = new HashMap<String, Object>(options);
                opts.put("pending", "true");
                
                TranslationKey tempKey = createTranslationKey(keyHash, label, description, opts);
                getApplication().registerMissingTranslationKey(tempKey, source.getKey());
            }
        }

        TranslationKey matchedKey = getApplication().getTranslationKey(keyHash);
        if (matchedKey != null) return matchedKey.translate(this, tokens, options);

        TranslationKey tempKey = createTranslationKey(keyHash, label, description, options);
        return tempKey.translate(this, tokens, options);
    }

    public String getAlignment(String target) {
    	if (isRightToLeft()) return target;
    	return target == "left" ? "right" : "left"; 
    }

    public String getDirection() {
    	return isRightToLeft() ? "rtl" : "ltr";
    }
    
    public String toString() {
        return this.getEnglishName() + " (" + getLocale() + ")";
    }
}
