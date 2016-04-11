/**
 * Copyright (c) 2016 Translation Exchange, Inc. All rights reserved.
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

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import com.translationexchange.core.languages.Language;
import com.translationexchange.core.tokenizers.Tokenizer;

/**
 * Represents a TML application session or a web request
 *
 * @author Berk
 * @version $Id: $Id
 */

public class Session extends Observable {

	public static final String SESSION_KEY = "session";

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
     * Initializes current application
     *
     * @param options a {@link java.util.Map} object.
     */
    @SuppressWarnings("unchecked")
	public Session(Map <String, Object> options) {
    	if (options == null) {
    		options = new HashMap<String, Object>();
    	}

    	String[] keys = {"key", "token", "host", "cdn_host"};
    	for (String key :  keys) {
        	if (options.get(key) == null)
        		options.put(key, Tml.getConfig().getApplication().get(key));
    	}

    	// translator token takes precedence
    	if (options.get("oauth") != null) {
    		Map<String, String> oauth = (Map<String, String>) options.get("oauth");
    		options.put("token", oauth.get("token"));
    	}
    	
    	// create translator object
    	if (options.get("translator") != null) {
    		setCurrentTranslator(new Translator((Map<String, Object>) options.get("translator")));
    	}
    	
    	Map<String, Object> applicationParams = Utils.buildMap();
    	if (options.get("locale") != null) {
    		applicationParams.put("locale", options.get("locale"));
    	}

    	if (options.get("source") != null) {
    		applicationParams.put("source", options.get("source"));
    		setCurrentSource((String)options.get("source"));
    	}
    	try {
    	    setApplication(initializeApplication(options));
    	    getApplication().setSession(this);
            getApplication().load(applicationParams);
            setCurrentLocale(getApplication().getFirstAcceptedLocale((String) options.get("locale")));
    	} catch(Exception ex) {
    	    Tml.getLogger().logException("Failed to load application. Therefore session could not be loaded", ex);
    	    // todo: might be a good way to re-raise custom exception SessionLoadException()
    	}
    	Tml.getConfig().setDecorator("html");
    }
    
    public static Application initializeApplication(Map<String, Object> options) throws Exception {
        String applicationClass = "";
        if(options.get("applicationClass") != null) {
            applicationClass = (String) options.get("applicationClass");
        } else {
            applicationClass = Tml.getConfig().getApplicationClass();
        }
        Class appClass = Utils.loadClassByName(applicationClass);
        Constructor<Application> constructor = appClass.getConstructor(Map.class);            
        Application application = (Application) constructor.newInstance(options);
        return application;
    }
    
    /**
     * <p>Getter for the field <code>currentLanguage</code>.</p>
     *
     * @return a {@link com.translationexchange.core.languages.Language} object.
     */
    public Language getCurrentLanguage() {
        return currentLanguage;
    }

    /**
     * <p>switchLanguage.</p>
     *
     * @param language a {@link com.translationexchange.core.languages.Language} object.
     */
    public void switchLanguage(Language language) {
//        if (getCurrentLanguage().equals(language))
//            return;
        
    	language = getApplication().getLanguage(language.getLocale());
        setCurrentLanguage(language);

        getApplication().resetTranslations();
        getApplication().loadTranslations(language);

        setChanged();
        notifyObservers(language);
    }

    /**
     * Sets current language in the singleton instance
     *
     * @param locale a {@link java.lang.String} object.
     */
    public void setCurrentLocale(String locale) {
        setCurrentLanguage(getApplication().getLanguage(locale));
    }

    /**
     * <p>Setter for the field <code>currentLanguage</code>.</p>
     *
     * @param language a {@link com.translationexchange.core.languages.Language} object.
     */
    public void setCurrentLanguage(Language language) {
        this.currentLanguage = language;
    }

    /**
     * <p>Getter for the field <code>currentSource</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getCurrentSource() {
        if(currentSource == null) {
            return "";
        }
        return currentSource;
    }

    /**
     * <p>Setter for the field <code>currentSource</code>.</p>
     *
     * @param currentSource a {@link java.lang.String} object.
     */
    public void setCurrentSource(String currentSource) {
        this.currentSource = currentSource;
    }

    /**
     * <p>beginBlockWithOptions.</p>
     *
     * @param options a {@link java.util.Map} object.
     */
    public void beginBlockWithOptions(Map<String, Object> options) {
        if (this.blockOptions == null) {
            this.blockOptions = new ArrayList<Map<String, Object>>();
        }

        this.blockOptions.add(0, options);
    }

    /**
     * <p>Getter for the field <code>blockOptions</code>.</p>
     *
     * @return a {@link java.util.Map} object.
     */
    public Map<String, Object> getBlockOptions() {
        if (this.blockOptions == null)
            this.blockOptions = new ArrayList<Map<String, Object>>();

        if (this.blockOptions.size() == 0)
            return new HashMap<String, Object>();

        return this.blockOptions.get(0);
    }

    /**
     * <p>endBlock.</p>
     */
    public void endBlock() {
        if (this.blockOptions == null || this.blockOptions.size() == 0)
            return;

        this.blockOptions.remove(0);
    }

    /**
     * <p>getBlockOption.</p>
     *
     * @param key a {@link java.lang.String} object.
     * @return a {@link java.lang.Object} object.
     */
    public Object getBlockOption(String key) {
        return getBlockOptions().get(key);
    }

    /**
     * <p>getSourcePath.</p>
     *
     * @return a {@link java.util.List} object.
     */
    @SuppressWarnings("rawtypes")
    public List<String> getSourcePath() {
    	List<String> path = new ArrayList<String>();
    	path.add(getCurrentSource());
    	if (this.blockOptions == null)
    		return path;
    	
    	List<String> subpath = new ArrayList<String>();
    	for (Map options : this.blockOptions) {
    		if (options.get("source") != null)
    			subpath.add((String)options.get("source"));	
    	}
    	
    	Collections.reverse(subpath);
    	path.addAll(subpath);
    	return path;
    }
    
    /**
     * <p>Getter for the field <code>application</code>.</p>
     *
     * @return a {@link com.translationexchange.core.Application} object.
     */
    public Application getApplication() {
        return application;
    }

    /**
     * <p>Setter for the field <code>application</code>.</p>
     *
     * @param application a {@link com.translationexchange.core.Application} object.
     */
    public void setApplication(Application application) {
        this.application = application;
    }

    /**
     * <p>Getter for the field <code>currentTranslator</code>.</p>
     *
     * @return a {@link com.translationexchange.core.Translator} object.
     */
    public Translator getCurrentTranslator() {
        return currentTranslator;
    }

    /**
     * <p>Setter for the field <code>currentTranslator</code>.</p>
     *
     * @param currentTranslator a {@link com.translationexchange.core.Translator} object.
     */
    public void setCurrentTranslator(Translator currentTranslator) {
        this.currentTranslator = currentTranslator;
    }

    /**
     * <p>isInlineModeEnabled.</p>
     *
     * @return a boolean.
     */
    public boolean isInlineModeEnabled() {
    	if (getCurrentTranslator() == null) return false;
    	return getCurrentTranslator().isInline();
    }
    
    /**
     * Translates a simple label
     *
     * @param label Label to be translated
     * @return a {@link java.lang.String} object.
     */
    public String translate(String label) {
        return translate(label, "");
    }

    /**
     * <p>translate.</p>
     *
     * @param label a {@link java.lang.String} object.
     * @param description a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public String translate(String label, String description) {
        return translate(label, description, null);
    }

    /**
     * <p>translate.</p>
     *
     * @param label a {@link java.lang.String} object.
     * @param description a {@link java.lang.String} object.
     * @param tokens a {@link java.util.Map} object.
     * @return a {@link java.lang.String} object.
     */
    public String translate(String label, String description, Map<String, Object> tokens) {
        return translate(label, description, tokens, null);
    }

    /**
     * <p>translate.</p>
     *
     * @param label a {@link java.lang.String} object.
     * @param tokens a {@link java.util.Map} object.
     * @return a {@link java.lang.String} object.
     */
    public String translate(String label, Map<String, Object> tokens) {
        return translate(label, "", tokens, null);
    }

    /**
     * <p>translate.</p>
     *
     * @param label a {@link java.lang.String} object.
     * @param tokens a {@link java.util.Map} object.
     * @param options a {@link java.util.Map} object.
     * @return a {@link java.lang.String} object.
     */
    public String translate(String label, Map<String, Object> tokens, Map<String, Object> options) {
        return translate(label, "", tokens, options);
    }

    /**
     * <p>translate.</p>
     *
     * @param label a {@link java.lang.String} object.
     * @param description a {@link java.lang.String} object.
     * @param tokens a {@link java.util.Map} object.
     * @param options a {@link java.util.Map} object.
     * @return a {@link java.lang.String} object.
     */
    public String translate(String label, String description, Map<String, Object> tokens, Map<String, Object> options) {
        if (options == null)
        	options = new HashMap<String, Object>();
    	options.put(Session.SESSION_KEY, this);
        return (String) getCurrentLanguage().translate(label, description, tokens, options);
    }

    /**
     * <p>translateStyledString.</p>
     *
     * @param label a {@link java.lang.String} object.
     * @return a {@link java.lang.Object} object.
     */
    public Object translateStyledString(String label) {
        return translateStyledString(label, "");
    }

    /**
     * <p>translateStyledString.</p>
     *
     * @param label a {@link java.lang.String} object.
     * @param description a {@link java.lang.String} object.
     * @return a {@link java.lang.Object} object.
     */
    public Object translateStyledString(String label, String description) {
        return translateStyledString(label, description, null);
    }

    /**
     * <p>translateStyledString.</p>
     *
     * @param label a {@link java.lang.String} object.
     * @param description a {@link java.lang.String} object.
     * @param tokens a {@link java.util.Map} object.
     * @return a {@link java.lang.Object} object.
     */
    public Object translateStyledString(String label, String description, Map<String, Object> tokens) {
        return  translateStyledString(label, description, tokens, null);
    }

    /**
     * <p>translateStyledString.</p>
     *
     * @param label a {@link java.lang.String} object.
     * @param tokens a {@link java.util.Map} object.
     * @return a {@link java.lang.Object} object.
     */
    public Object translateStyledString(String label, Map<String, Object> tokens) {
        return translateStyledString(label, "", tokens, null);
    }

    /**
     * <p>translateStyledString.</p>
     *
     * @param label a {@link java.lang.String} object.
     * @param tokens a {@link java.util.Map} object.
     * @param options a {@link java.util.Map} object.
     * @return a {@link java.lang.Object} object.
     */
    public Object translateStyledString(String label, Map<String, Object> tokens, Map<String, Object> options) {
        return translateStyledString(label, "", tokens, options);
    }

    /**
     * <p>translateStyledString.</p>
     *
     * @param label a {@link java.lang.String} object.
     * @param description a {@link java.lang.String} object.
     * @param tokens a {@link java.util.Map} object.
     * @param options a {@link java.util.Map} object.
     * @return a {@link java.lang.Object} object.
     */
    public Object translateStyledString(String label, String description, Map<String, Object> tokens, Map<String, Object> options) {
        if (options == null)
            options = new HashMap<String, Object>();

    	options.put(Session.SESSION_KEY, this);
        options.put(TranslationKey.TOKENIZER_KEY, TranslationKey.DEFAULT_TOKENIZERS_STYLED);
        return getCurrentLanguage().translate(label, description, tokens, options);
    }

}
