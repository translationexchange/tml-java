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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Source extends Base {

    /**
     * Reference back to the application it belongs to
     */
    private Application application;

    /**
     * Locale of the language it currently holds
     */
    private String locale;

    /**
     * Source key
     */
    private String key;

    /**
     * Source name given by the admin or developer
     */
    private String name;

    /**
     * Source description
     */
    private String description;

    /**
     * Translation keys registered with the source
     */
    private Map<String, TranslationKey> translationKeys;

    /**
     * Default constructor
     */
    public Source() {
        super();
    }

    /**
     *
     * @param attributes
     */
    public Source(Map<String, Object> attributes) {
        super(attributes);
    }

    public String getKey() {
        return key;
    }

    public Application getApplication() {
        return application;
    }

    public String getLocale() {
        return locale;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    /**
     *
     * @param attributes
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public void updateAttributes(Map<String, Object> attributes) {
        if (attributes.get("application") != null)
            setApplication((Application) attributes.get("application"));

        setKey((String) attributes.get("key"));
        setLocale((String) attributes.get("locale"));
        setName((String) attributes.get("name"));
        setDescription((String) attributes.get("description"));

        if (attributes.get("translation_keys") != null) {
            for (Object data : ((List) attributes.get("translation_keys"))) {
                addTranslationKey(new TranslationKey(Utils.extendMap((Map<String, Object>) data, "application", getApplication())));
            }
        }
    }

    /**
     * Creates cache key for source
     * 
     * @param locale
     * @param key
     * @return
     */
    public static String getCacheKey(String locale, String key) {
    	return locale + "/" + key;
    }
    
    /**
     * Creates cache key for the source
     * @return
     */
    public String getCacheKey() {
    	return getCacheKey(getLocale(), getKey());
    }
    
    /**
     * Loading source from service
     */
    public void load(Map<String, Object> options) {
        try {
        	if (options==null) options = new HashMap<String, Object>();
        	options.put("cache_key", getCacheKey());
        	
            this.updateAttributes(getApplication().getHttpClient().getJSONMap("source", 
            		Utils.buildMap("source", getKey(), "locale", getLocale(), "translations", "true"),
            		options
            ));
            
            setLoaded(true);
        } catch (Exception ex) {
            setLoaded(false);
//            Tr8n.getLogger().logException("Failed to load source", ex);
        }
    }

    /**
     * Returns a map of translation keys 
     * 
     * @return
     */
    public Map<String, TranslationKey> getTranslationKeys() {
        if (translationKeys == null) {
            translationKeys = new HashMap<String, TranslationKey>();
        }
        
        return translationKeys;
    }
    
    /**
     * Adds translation key to the source
     * @param translationKey
     */
    public void addTranslationKey(TranslationKey translationKey) {
        if (getApplication() != null)
        	translationKey = getApplication().cacheTranslationKey(translationKey);
        
        getTranslationKeys().put(translationKey.getKey(), translationKey);
    }

    /**
     * Returns translation key from the source
     * @param key
     * @return
     */
    public TranslationKey getTranslationKey(String key) {
        if (translationKeys == null)
            return null;

        return translationKeys.get(key);
    }
    
}
