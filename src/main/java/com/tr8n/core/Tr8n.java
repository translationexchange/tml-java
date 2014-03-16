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

import java.text.AttributedString;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents either a singleton or a request session object in multi-threaded application
 */
public class Tr8n {
    /**
     * Singleton Methods - should ever only be used in a non-multi threaded application
     */

    private static Tr8n instance = null;

    public static Tr8n getInstance() {
        if(instance == null) {
            instance = new Tr8n();
        }
        return instance;
    }

    /**
     *
     * @param label
     * @return
     */
    public static String tr(String label) {
        return tr(label, "");
    }

    public static String tr(String label, String description) {
        return tr(label, description, null);
    }

    public static String tr(String label, String description, Map<String, Object> tokens) {
        return getInstance().translate(label, description, null, null);
    }

    public static String tr(String label, Map<String, Object> tokens) {
        return getInstance().translate(label, "", tokens, null);
    }

    public static String tr(String label, Map<String, Object> tokens, Map<String, Object> options) {
        return getInstance().translate(label, "", tokens, options);
    }

    /**
     *
     * @param label
     * @return
     */
    public static AttributedString tras(String label) {
        return tras(label, "");
    }

    public static AttributedString tras(String label, String description) {
        return tras(label, description, null);
    }

    public static AttributedString tras(String label, String description, Map<String, Object> tokens) {
        return getInstance().translateAttributedString(label, description, null, null);
    }

    public static AttributedString tras(String label, Map<String, Object> tokens) {
        return getInstance().translateAttributedString(label, "", tokens, null);
    }

    public static AttributedString tras(String label, Map<String, Object> tokens, Map<String, Object> options) {
        return getInstance().translateAttributedString(label, "", tokens, options);
    }


    public static void init(String key) {
        init(key, null);
    }

    public static void init(String key, String secret) {
        init(key, null, null);
    }

    public static void init(String key, String secret, String host) {
        getInstance().initialize(key, secret, host);
    }

    /**
     * Static attributes
     */

    /**
     *
     */
    private static Configuration config;

    /**
     *
     */
    private static Cache cache;

    /**
     *
     */
    private static Logger logger;


    public static Configuration getConfig() {
        if (config == null)
            config = new Configuration();

        return config;
    }

    public static Cache getCache() {
        if (cache  == null)
            cache = new Cache();
        return cache;
    }

    public static Logger getLogger() {
        if (logger == null)
            logger = new Logger();

        return logger;
    }


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
    private List<Map> blockOptions;

    /**
     * Default constructor
     */
    public Tr8n() {
        // Do nothing
    }

    /**
     * Initializes current application
     * @param key
     * @param secret
     * @param host
     */
    public void initialize(String key, String secret, String host) {
        setApplication(new Application(Utils.buildMap(
                "key", key,
                "secret", secret,
                "host", host
        )));
        getApplication().load();
        getApplication().setSession(this);
        setCurrentSource("undefined");
        setCurrentLanguage(getApplication().getLanguage());
    }

    public Language getCurrentLanguage() {
        return currentLanguage;
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

    public void beginBlockWithOptions(Map options) {
        if (this.blockOptions == null) {
            this.blockOptions = new ArrayList<Map>();
        }

        this.blockOptions.add(0, options);
    }

    public Map getBlockOptions() {
        if (this.blockOptions == null)
            this.blockOptions = new ArrayList<Map>();

        if (this.blockOptions.size() == 0)
            return new HashMap();

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

    /**
     * Translates label with description
     *
     * @param label         Label to be translated
     * @param description   Contextual description of the label
     * @return
     */
    public String translate(String label, String description) {
        return translate(label, description, null);
    }

    /**
     * Translates label with description and tokens
     *
     * @param label
     * @param description
     * @param tokens
     * @return
     */
    public String translate(String label, String description, Map<String, Object> tokens) {
        return translate(label, description, null, null);
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
    public AttributedString translateAttributedString(String label) {
        return translateAttributedString(label, "");
    }

    public AttributedString translateAttributedString(String label, String description) {
        return translateAttributedString(label, description, null);
    }

    public AttributedString translateAttributedString(String label, String description, Map<String, Object> tokens) {
        return getInstance().translateAttributedString(label, description, null, null);
    }

    public AttributedString translateAttributedString(String label, Map<String, Object> tokens) {
        return getInstance().translateAttributedString(label, "", tokens, null);
    }

    public AttributedString translateAttributedString(String label, Map<String, Object> tokens, Map<String, Object> options) {
        return getInstance().translateAttributedString(label, "", tokens, options);
    }

    public AttributedString translateAttributedString(String label, String description, Map<String, Object> tokens, Map<String, Object> options) {
        if (options == null)
            options = new HashMap<String, Object>();

        options.put("tokenizer", "attributed");
        return (AttributedString) getCurrentLanguage().translate(label, description, tokens, options);
    }

}
