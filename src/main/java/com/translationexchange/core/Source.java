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
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
     * Updates translation keys in the app and in the source
     * 
     * @param data
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void updateTranslationKeys(Map<String, Object> data) {
    	Iterator entries = ((Map) data.get("results")).entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry entry = (Map.Entry) entries.next();
            String key = (String) entry.getKey();
            Map<String, Object> keyData = (Map<String, Object>) entry.getValue();
            
            TranslationKey tkey = null;
            if (getApplication() != null)
            	tkey = getApplication().getTranslationKey(key);
            
            if (tkey == null) {
            	tkey = new TranslationKey((Map<String, Object>) keyData.get("original"));
            	
            	if (getApplication() != null)
            		getApplication().addTranslationKey(tkey);
            }
            
            List<Translation> translations = new ArrayList<Translation>();
            for (Map<String, Object> translationData : (List<Map<String, Object>>) keyData.get("translations")) {
                Translation translation = new Translation(translationData);
                String locale = (String) translationData.get("locale");
                
                if (locale == null)
                	locale = getLocale();
                
                if (getApplication() != null)
                	translation.setLanguage(getApplication().getLanguage(locale));
                
                translations.add(translation);
            }
            
            tkey.setTranslations(getLocale(), translations);
            addTranslationKey(tkey);
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
    	return locale + File.separator + "sources" + (key.startsWith(File.separator) ? "" : File.separator) + key;
    }
    
    /**
     * Creates cache key for the source
     * @return
     */
    public String getCacheKey() {
    	return getCacheKey(getLocale(), getKey());
    }
    
    /**
     * Generates MD5 representation of the key - used for API calls 
     * @return
     */
    public String generateMD5Key() {
    	try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            String hashText = new BigInteger(1,m.digest(this.getKey().getBytes("UTF-8"))).toString(16);
            while(hashText.length() < 32 ) hashText = "0" + hashText;
            return hashText;
        } catch (Exception ex) {
            Tml.getLogger().logException("Failed to generate md5 key for source: " + this.getKey(), ex);
            return null;
        }    	
    }
    
    /**
     * Loading source from service
     */
    public void load(Map<String, Object> options) {
        try {
        	if (options==null) options = new HashMap<String, Object>();
        	options.put("cache_key", getCacheKey());

        	this.updateTranslationKeys(getApplication().getHttpClient().getJSONMap("sources/" + this.generateMD5Key() + "/translations", 
	    		Utils.buildMap("original", "true", "per_page", "10000", "locale", getLocale()),
	    		options
            ));
            
            setLoaded(true);
        } catch (Exception ex) {
            setLoaded(false);
//            Tml.getLogger().logException("Failed to load source", ex);
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
