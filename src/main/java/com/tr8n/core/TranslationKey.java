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

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.tr8n.core.decorators.Decorator;
import com.tr8n.core.tokenizers.DataTokenizer;
import com.tr8n.core.tokenizers.DecorationTokenizer;
import com.tr8n.core.tokenizers.Tokenizer;

public class TranslationKey extends Base {

	public static final String TOKENIZER_KEY = "tokenizer";
	public static final String DEFAULT_TOKENIZERS_DATA = "data";
	public static final String DEFAULT_TOKENIZERS_HTML = "html";
	public static final String DEFAULT_TOKENIZERS_STYLED = "styled";
	
    /**
     * Reference to the application where the key came from
     */
    private Application application;

    /**
     * Unique key (md5 hash) identifying this translation key
     */
    private String key;
    
    /**
     * Text to be translated
     */
    private String label;

    /**
     * Description of the text to be translated
     */
    private String description;

    /**
     * Locale of the text to be translated
     */
    private String locale;

    /**
     * Level of the key
     */
    private Long level;

    /**
     * Hash of translations for each locale needed by the application
     */
    private Map<String, List<Translation>> translationsByLocale;

    /**
     * Indicates whether the key is locked
     */
    private Boolean locked;
    
    /**
     * Indicates whether the key is registered
     */
    private Boolean registered;
    
    /**
     * List of data token names from the original label
     */
    private List<String> allowedDataTokenNames;

    /**
     * List of decoration token names from the original label
     */
    private List<String> allowedDecorationTokenNames;

    /**
     * Default constructor
     */
    public TranslationKey() {
        super();
    }
    
    /**
     *
     * @param attributes
     */
    public TranslationKey(Map<String, Object> attributes) {
        super(attributes);
    }

    public String getLabel() {
        return label;
    }

    public String getDescription() {
        return description;
    }

    public String getKey() {
        return key;
    }

    public String getLocale() {
        return locale;
    }

    public Long getLevel() {
        return level;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }
    
    /**
     *
     * @return
     */
    public List<String> getAllowedDataTokenNames() {
       if (allowedDataTokenNames == null) {
           DataTokenizer dt = new DataTokenizer(getLabel());
           allowedDataTokenNames = dt.getTokenNames();
       }
       return allowedDataTokenNames;
    }

    /**
     *
     * @return
     */
    public List<String> getAllowedDecorationTokenNames() {
       if (allowedDecorationTokenNames == null) {
           DecorationTokenizer dt = new DecorationTokenizer(getLabel());
           allowedDecorationTokenNames = dt.getTokenNames();
       }
       return allowedDecorationTokenNames;
    }
    
    public Map<String, List<Translation>> getTranslationsByLocale() {
        if (translationsByLocale == null)
            translationsByLocale = new HashMap<String, List<Translation>>();

        return translationsByLocale;
    }

    /**
     * Sets translations for a specific locale. Translation key can have translations for multiple locales.
     * @param locale
     * @param translations
     */
    public void setTranslations(String locale, List<Translation> translations) {
        for (Translation translation : translations) {
            translation.setTranslationKey(this);
        }

        getTranslationsByLocale().put(locale, translations);
    }

    public List<String> getTranslationLocales() {
        return new ArrayList<String>(getTranslationsByLocale().keySet());
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public void setLevel(Long level) {
        this.level = level;
    }

    public void setAllowedDataTokenNames(List<String> allowedDataTokenNames) {
        this.allowedDataTokenNames = allowedDataTokenNames;
    }

    public void setAllowedDecorationTokenNames(List<String> allowedDecorationTokenNames) {
        this.allowedDecorationTokenNames = allowedDecorationTokenNames;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Boolean isLocked() {
    	if (locked == null)
    		return false;
		return locked;
	}

	public void setLocked(Boolean locked) {
		this.locked = locked;
	}
	
	public Boolean isRegistered() {
		return registered;
	}

	public void setRegistered(Boolean registered) {
		this.registered = registered;
	}
	
    /**
     *
     * @param attributes
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public void updateAttributes(Map<String, Object> attributes) {
        if (attributes.get("application") != null)
            setApplication((Application) attributes.get("application"));

        setLabel((String) attributes.get("label"));
        setDescription((String) attributes.get("description"));

        if (attributes.get("key") != null) {
            setKey((String) attributes.get("key"));
        } else {
            setKey(generateKey(getLabel(), getDescription()));
        }

        setLocale((String) attributes.get("locale"));
        if (getLocale() == null)
            setLocale(Tr8n.getConfig().getDefaultLocale());

        setRegistered(attributes.get("id") != null);
        
        setLevel((Long) attributes.get("level"));

        setLocked((Boolean) attributes.get("locked"));

        if (attributes.get("translations") != null && getApplication() != null) {
            Iterator<Map.Entry<String, List<Map<String, Object>>>> entries = ((Map) attributes.get("translations")).entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry<String, List<Map<String, Object>>> entry = entries.next();
                Language language = getApplication().getLanguage(entry.getKey());

                for (Map<String, Object> translationData : entry.getValue()) {
                    addTranslation(new Translation(Utils.extendMap(translationData, "language", language, "translation_key", this)));
                }
            }
        }
    }
    
    /**
     * Returns a list of translations for a locale
     * 
     * @param locale
     * @return
     */
    public List<Translation> getTranslations(String locale) {
        if (getTranslationsByLocale().get(locale) == null) {
        	getTranslationsByLocale().put(locale, new ArrayList<Translation>());
        }

        return getTranslationsByLocale().get(locale);
    }
    
    /**
     * Adds a translation
     * 
     * @param translation
     */
    public void addTranslation(Translation translation) {
    	List<Translation> translations = getTranslations(translation.getLocale());
        translation.setTranslationKey(this);
        translations.add(translation);
    }
    
    /**
     * Adds translations
     * @param locale
     * @param translations
     */
    public void addTranslations(List<Translation> translations) {
        for (Translation translation : translations) {
        	addTranslation(translation);
        }
    }
    
    /**
     * Generates unique hash key for the translation key using label
     * @param label
     * @return
     */
    public static String generateKey(String label) {
        return generateKey(label, null);
    }

    /**
     * Generates unique hash key for the translation key using label and description
     * @param label
     * @param description
     * @return
     */
    public static String generateKey(String label, String description) {
        StringBuilder sb = new StringBuilder();
        sb.append(label); sb.append(";;;");
        if (description != null) sb.append(description);
        String s = sb.toString();

        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            String hashText = new BigInteger(1,m.digest(s.getBytes("UTF-8"))).toString(16);
            while(hashText.length() < 32 ) hashText = "0" + hashText;
            return hashText;
        } catch (Exception ex) {
            Tr8n.getLogger().logException("Failed to generate md5 key for " + label + " " + description, ex);
            return null;
        }
    }

    /**
     * Returns YES if there are translations available for the key
     * @return
     */
    public boolean hasTranslations() {
        return getTranslationLocales().size() > 0;
    }

    /**
     * Returns frist acceptable translation based on the token values and language rules
     * 
     * @param language
     * @param tokens
     * @return
     */
    private Translation findFirstAcceptableTranslation(Language language, Map<String, Object> tokens) {
        List<Translation> availableTranslations = getTranslations(language.getLocale());
        if (availableTranslations == null || availableTranslations.size() == 0)
            return null;

        if (availableTranslations.size() == 1) {
            Translation translation = availableTranslations.get(0);
            if (!translation.hasContext()) return translation;
        }

        for (Translation translation : availableTranslations) {
            if (translation.isValidTranslationForTokens(tokens))
                return translation;
        }

        return null;
    }

    /**
     *
     * @param language
     * @return
     */
    public Object translate(Language language) {
        return translate(language, null);
    }

    /**
     *
     * @param language
     * @param tokens
     * @return
     */
    public Object translate(Language language, Map<String, Object> tokens) {
        return translate(language, null, null);
    }

    /**
     *
     * @param language
     * @param tokens
     * @param options
     * @return
     */
    public Object translate(Language language, Map<String, Object> tokens, Map<String, Object> options) {
        if (getLocale().equals(language.getLocale())) {
            return substitute(label, tokens, language, language, options);
        }

        Translation translation = findFirstAcceptableTranslation(language, tokens);

        if (translation != null)
            return substitute(translation.getLabel(), tokens, translation.getLanguage(), language, options);

        return substitute(getLabel(), tokens, getApplication().getLanguage(), language, options);
    }

    /**
     * 
     * @param key
     * @param translatedLabel
     * @param translationLanguage
     * @param tokens
     * @param options
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected Object applyTokenizer(String key, String translatedLabel, Language translationLanguage, List<String> allowedTokenNames, Map<String, Object> tokens, Map<String, Object> options) {
    	try {
			Class tokenizerClass = Class.forName(Tr8n.getConfig().getTokenizerClass(key));
			if (tokenizerClass == null) return translatedLabel;
			
			Method method = tokenizerClass.getMethod("isApplicable", String.class);
	        if ((Boolean) method.invoke(null, translatedLabel)) {
				Constructor<Tokenizer> constructor = tokenizerClass.getConstructor(String.class, List.class);            
	        	Tokenizer tokenizer = (Tokenizer) constructor.newInstance(translatedLabel, allowedTokenNames);
	        	return tokenizer.substitute(tokens, translationLanguage, options);
	        }
    	} catch (Exception ex) {
    		Tr8n.getLogger().logException("Failed to tokenize \"" + translatedLabel + "\" using " + key, ex);
    	}
		return translatedLabel;
    }
    
    /**
     *
     * @param translatedLabel
     * @param tokens
     * @param language
     * @param options
     * @return
     */
	public Object substitute(String translatedLabel, Map<String, Object> tokens, Language translationLanguage, Language targetLanguage, Map<String, Object> options) {
		// Data tokenizer must always be present
		translatedLabel = (String) applyTokenizer(DEFAULT_TOKENIZERS_DATA, translatedLabel, translationLanguage, getAllowedDataTokenNames(), tokens, options);
		
		String tokenizerKey = DEFAULT_TOKENIZERS_HTML; 
        if (options != null && options.get(TOKENIZER_KEY) != null)
        	tokenizerKey = (String) options.get(TOKENIZER_KEY); 
        
        Object result = applyTokenizer(tokenizerKey, translatedLabel, translationLanguage, getAllowedDecorationTokenNames(), tokens, options);
        
        if (result instanceof String) {
        	Decorator decorator = Tr8n.getConfig().getDecorator();
        	return decorator.decorate(result, translationLanguage, targetLanguage, this, options);
        }
        
        return result;
    }

	/**
	 * Returns map representation for the translation key
	 * @return
	 */
	public Map<String, Object> toMap() {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("label", label);
        if (description != null)
            data.put("description", description);
        if (locale != null)
            data.put("locale", locale);
        if (level != null)
            data.put("level", level);
        return data;
    }

    public String toString() {
        return label + " (" + locale + ")";
    }
}

