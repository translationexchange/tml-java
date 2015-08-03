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
import java.util.List;
import java.util.Map;
import java.util.Observable;

/**
 * Represents a TML application session or a web request
 */
public class Session extends Observable {

    /**
     * Current application
     */
    private Application application;

    /**
     * Stores the current language selected by the user
     */
    private Language currentLanguage;

    /**
     * Stores the current translator info
     */
    private Translator currentTranslator;

    /**
     * Allows to set a source for the entire request
     */
    private String currentSource;

    /**
     * Allows developers to group translation keys for management only
     */
    private List<Map<String, Object>> blockOptions;

    /**
     * Default constructor
     */
    public Session() {
    	this(Tml.getConfig().getApplication());
    }

    /**
     * Initialize with a configuration
     * 
     * @param config
     */
    public Session(Map<String, Object> config) {
    	this((String) config.get("token"), config);
    }

    /**
     * Initializes a session with token only
     * 
     * @param token
     */
    public Session(String token) {
    	this(token, null);
    }

    /**
     * Initializes current application
     * @param token
     * @param options
     */
    public Session(String token, Map <String, Object> options) {
    	if (options == null) {
    		options = new HashMap<String, Object>();
    	}
    	options.put("token", token);
        setApplication(new Application(options));
        getApplication().load();
        getApplication().setSession(this);
        setCurrentLanguage(getApplication().getLanguage());
    }

    
    @SuppressWarnings("unchecked")
	public void init(Map <String, Object> options) {
    	if (options == null)
    		return;

    	Tml.getConfig().setDecorator("html");
    	
//    	{"translator":{"id":3,"manager":true,"email":"theiceberk@gmail.com","inline":true,"name":"Safari","features":{"show_locked_keys":false,"fallback_language":false}},"locale":"ru","code":"a0f135d6b42818c46"}    	

    	if (options.get("locale") != null)
    		setCurrentLocale((String)options.get("locale"));
    	
    	if (options.get("translator") != null) {
    		setCurrentTranslator(new Translator((Map<String, Object>) options.get("translator")));
    	}
    }
    
    public Language getCurrentLanguage() {
        return currentLanguage;
    }

    public void switchLanguage(Language language) {
//        if (getCurrentLanguage().equals(language))
//            return;
        
        setCurrentLanguage(language);

        getApplication().resetTranslations();
        getApplication().loadTranslations(language);

        setChanged();
        notifyObservers(language);
    }

    /**
     * Sets current language in the singleton instance
     * @param locale
     */
    public void setCurrentLocale(String locale) {
        setCurrentLanguage(getApplication().getLanguage(locale));
    }

    public void setCurrentLanguage(Language language) {
        this.currentLanguage = language;
    }

    public String getCurrentSource() {
        return currentSource;
    }

    public void setCurrentSource(String currentSource) {
        this.currentSource = currentSource;
    }

    public void beginBlockWithOptions(Map<String, Object> options) {
        if (this.blockOptions == null) {
            this.blockOptions = new ArrayList<Map<String, Object>>();
        }

        this.blockOptions.add(0, options);
    }

    public Map<String, Object> getBlockOptions() {
        if (this.blockOptions == null)
            this.blockOptions = new ArrayList<Map<String, Object>>();

        if (this.blockOptions.size() == 0)
            return new HashMap<String, Object>();

        return this.blockOptions.get(0);
    }

    public void endBlock() {
        if (this.blockOptions == null || this.blockOptions.size() == 0)
            return;

        this.blockOptions.remove(0);
    }

    public Object getBlockOption(String key) {
        return getBlockOptions().get(key);
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public Translator getCurrentTranslator() {
        return currentTranslator;
    }

    public void setCurrentTranslator(Translator currentTranslator) {
        this.currentTranslator = currentTranslator;
    }

    /**
     * Translates a simple label
     *
     * @param label Label to be translated
     * @return
     */
    public String translate(String label) {
        return translate(label, "");
    }

    public String translate(String label, String description) {
        return translate(label, description, null);
    }

    public String translate(String label, String description, Map<String, Object> tokens) {
        return translate(label, description, tokens, null);
    }

    public String translate(String label, Map<String, Object> tokens) {
        return translate(label, "", tokens, null);
    }

    public String translate(String label, Map<String, Object> tokens, Map<String, Object> options) {
        return translate(label, "", tokens, options);
    }

    public String translate(String label, String description, Map<String, Object> tokens, Map<String, Object> options) {
        return (String) getCurrentLanguage().translate(label, description, tokens, options);
    }

    /**
     *
     * @param label
     * @return
     */
    public Object translateStyledString(String label) {
        return translateStyledString(label, "");
    }

    public Object translateStyledString(String label, String description) {
        return translateStyledString(label, description, null);
    }

    public Object translateStyledString(String label, String description, Map<String, Object> tokens) {
        return  translateStyledString(label, description, tokens, null);
    }

    public Object translateStyledString(String label, Map<String, Object> tokens) {
        return translateStyledString(label, "", tokens, null);
    }

    public Object translateStyledString(String label, Map<String, Object> tokens, Map<String, Object> options) {
        return translateStyledString(label, "", tokens, options);
    }

    public Object translateStyledString(String label, String description, Map<String, Object> tokens, Map<String, Object> options) {
        if (options == null)
            options = new HashMap<String, Object>();

        options.put(TranslationKey.TOKENIZER_KEY, TranslationKey.DEFAULT_TOKENIZERS_STYLED);
        return getCurrentLanguage().translate(label, description, tokens, options);
    }

}
