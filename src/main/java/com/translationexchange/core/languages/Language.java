
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
 * @author Berk
 * @version $Id: $Id
 */

package com.translationexchange.core.languages;

import com.translationexchange.core.Application;
import com.translationexchange.core.Base;
import com.translationexchange.core.Configuration;
import com.translationexchange.core.Source;
import com.translationexchange.core.Tml;
import com.translationexchange.core.TranslationKey;
import com.translationexchange.core.Utils;
import com.translationexchange.core.cache.CacheVersion;

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
     *
     * @param attributes a {@link java.util.Map} object.
     */
    public Language(Map<String, Object> attributes) {
        super(attributes);
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
     * <p>Getter for the field <code>locale</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getLocale() {
        return locale;
    }

    /**
     * <p>Setter for the field <code>locale</code>.</p>
     *
     * @param locale a {@link java.lang.String} object.
     */
    public void setLocale(String locale) {
        this.locale = locale;
    }

    /**
     * <p>Getter for the field <code>englishName</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getEnglishName() {
        return englishName;
    }

    /**
     * <p>Setter for the field <code>englishName</code>.</p>
     *
     * @param englishName a {@link java.lang.String} object.
     */
    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    /**
     * <p>Getter for the field <code>nativeName</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getNativeName() {
        return nativeName;
    }

    /**
     * <p>Setter for the field <code>nativeName</code>.</p>
     *
     * @param nativeName a {@link java.lang.String} object.
     */
    public void setNativeName(String nativeName) {
        this.nativeName = nativeName;
    }

    /**
     * <p>isRightToLeft.</p>
     *
     * @return a {@link java.lang.Boolean} object.
     */
    public boolean isRightToLeft() {
        if (rightToLeft == null)
            return false;
        return rightToLeft.booleanValue();
    }

    /**
     * <p>Setter for the field <code>rightToLeft</code>.</p>
     *
     * @param rightToLeft a {@link java.lang.Boolean} object.
     */
    public void setRightToLeft(Boolean rightToLeft) {
        this.rightToLeft = rightToLeft;
    }

    /**
     * <p>Getter for the field <code>flagUrl</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getFlagUrl() {
        return flagUrl;
    }

    /**
     * <p>Setter for the field <code>flagUrl</code>.</p>
     *
     * @param flagUrl a {@link java.lang.String} object.
     */
    public void setFlagUrl(String flagUrl) {
        this.flagUrl = flagUrl;
    }

    /**
     * <p>Getter for the field <code>contexts</code>.</p>
     *
     * @return a {@link java.util.Map} object.
     */
    public Map<String, LanguageContext> getContexts() {
        if (contexts == null)
            contexts = new HashMap<String, LanguageContext>();

        return contexts;
    }

    /**
     * <p>Getter for the field <code>cases</code>.</p>
     *
     * @return a {@link java.util.Map} object.
     */
    public Map<String, LanguageCase> getCases() {
        if (cases == null)
            cases = new HashMap<String, LanguageCase>();

        return cases;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void updateAttributes(Map<String, Object> attributes) {
        if (attributes == null || attributes.isEmpty())
            return;
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
                context.setKeyword((String) entry.getKey());
                addContext(context);
            }
        }

        if (attributes.get("cases") != null) {
            Iterator entries = ((Map) attributes.get("cases")).entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry entry = (Map.Entry) entries.next();
                LanguageCase lcase = new LanguageCase((Map) entry.getValue());
                lcase.setKeyword((String) entry.getKey());
                addLanguageCase(lcase);
            }
        }
    }

    /**
     * Returns cache key of the language
     *
     * @return a {@link java.lang.String} object.
     */
    public String getCacheKey() {
        return getLocale() + File.separator + "language";
    }

    /**
     * Loads language from the server
     */
    public void load() {
        try {
            Map<String, Object> attributes = getApplication().getHttpClient().getJSONMap("languages/" + getLocale() + "/definition",
                    Utils.buildMap(),
                    Utils.buildMap("cache_key", getCacheKey())
            );
            updateAttributes(attributes);
            setLoaded(true);
        } catch (Exception ex) {
            setLoaded(false);
            Tml.getLogger().logException(ex);
        }
    }

    public void loadLocal(String cacheVersion) {
        try {
            Map<String, Object> attributes = getApplication().getHttpClient().getJSONMap(Utils.buildMap("cache_key", getCacheKey(), CacheVersion.VERSION_KEY, cacheVersion));
            updateAttributes(attributes);
            setLoaded(true);
        } catch (Exception ex) {
            setLoaded(false);
            Tml.getLogger().logException(ex);
        }
    }


    /**
     * Adds a context for the language
     *
     * @param languageContext a {@link com.translationexchange.core.languages.LanguageContext} object.
     */
    public void addContext(LanguageContext languageContext) {
        if (contexts == null)
            contexts = new HashMap<String, LanguageContext>();

        languageContext.setLanguage(this);

        contexts.put(languageContext.getKeyword(), languageContext);
    }


    /**
     * Returns language context based on the keyword
     *
     * @param keyword a {@link java.lang.String} object.
     * @return a {@link com.translationexchange.core.languages.LanguageContext} object.
     */
    public LanguageContext getContextByKeyword(String keyword) {
        if (contexts == null)
            return null;
        return this.contexts.get(keyword);
    }

    /**
     * Returns language context based on the token name
     *
     * @param tokenName a {@link java.lang.String} object.
     * @return a {@link com.translationexchange.core.languages.LanguageContext} object.
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
     *
     * @param keyword a {@link java.lang.String} object.
     * @return a {@link com.translationexchange.core.languages.LanguageCase} object.
     */
    public LanguageCase getLanguageCaseByKeyword(String keyword) {
        if (cases == null)
            return null;
        return cases.get(keyword);
    }

    /**
     * Adds a language case for the language
     *
     * @param languageCase a {@link com.translationexchange.core.languages.LanguageCase} object.
     */
    public void addLanguageCase(LanguageCase languageCase) {
        if (cases == null)
            cases = new HashMap<String, LanguageCase>();

        languageCase.setLanguage(this);

        cases.put(languageCase.getKeyword(), languageCase);
    }

    /**
     * Languages are loaded without definition by default, this will tell if the language has definition or it needs to be loaded
     *
     * @return a boolean.
     */
    public boolean hasDefinition() {
        return !getContexts().isEmpty();
    }

    /**
     * Returns a value from options or block options
     *
     * @param key
     * @param options
     * @param defaultValue
     * @return
     */
    protected Object getOptionsValue(String key, Map<String, Object> options, Object defaultValue) {
        Object value = null;
        if (options != null) {
            value = options.get(key);
            if (value != null) return value;
        }

        value = getApplication().getSession().getBlockOption(key);
        if (value != null) return value;

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
    protected TranslationKey createTranslationKey(String keyHash, String label, String description, Map<String, Object> options) {
        Map<String, Object> attributes = Utils.buildMap(
                "application", getApplication(),
                "key", keyHash,
                "label", label,
                "description", description,
                "locale", getOptionsValue("locale", options, getApplication().getDefaultLocale())
        );

        return new TranslationKey(attributes);
    }

    /**
     * <p>translate.</p>
     *
     * @param label a {@link java.lang.String} object.
     * @return a {@link java.lang.Object} object.
     */
    public Object translate(String label) {
        return translate(label, "");
    }

    /**
     * <p>translate.</p>
     *
     * @param label       a {@link java.lang.String} object.
     * @param description a {@link java.lang.String} object.
     * @return a {@link java.lang.Object} object.
     */
    public Object translate(String label, String description) {
        return translate(label, description, null, null);
    }

    /**
     * <p>translate.</p>
     *
     * @param label  a {@link java.lang.String} object.
     * @param tokens a {@link java.util.Map} object.
     * @return a {@link java.lang.Object} object.
     */
    public Object translate(String label, Map<String, Object> tokens) {
        return translate(label, "", tokens, null);
    }

    /**
     * Translation method
     *
     * @param label       a {@link java.lang.String} object.
     * @param description a {@link java.lang.String} object.
     * @param tokens      a {@link java.util.Map} object.
     * @param options     a {@link java.util.Map} object.
     * @return a {@link java.lang.Object} object.
     */
    public Object translate(String label, String description, Map<String, Object> tokens, Map<String, Object> options) {
        String keyHash = TranslationKey.generateKey(label, description);

        if (options == null) {
            options = new HashMap<String, Object>();
        }

        String sourceKey = (String) getOptionsValue("source", options, getApplication().getSession().getCurrentSource());

        // source key can never be empty
        if (sourceKey == null || sourceKey.equals("") || sourceKey.equals("/"))
            sourceKey = "index";

        // Source based keys in mobile or desktop environments require sources to be pre-loading in a separate thread
        Source source = getApplication().getSource(sourceKey, getLocale(), options);
        TranslationKey matchedKey = null;
        if (source != null && (matchedKey = source.getTranslationKey(keyHash)) != null) {
            if (matchedKey.getLabel() == null) {
                matchedKey.setLabel(label);
                matchedKey.setDescription(description);
            }
            return matchedKey.translate(this, tokens, options);
        } else {
            Map<String, Object> opts = new HashMap<String, Object>(options);
            opts.put("pending", "true");
            String sourcePath = Utils.join(getApplication().getSession().getSourcePath(), Configuration.SOURCE_SEPARATOR);
            TranslationKey tempKey = createTranslationKey(keyHash, label, description, opts);
            getApplication().registerMissingTranslationKey(tempKey, sourcePath);
            return tempKey.translate(this, tokens, options);
        }
    }

    /**
     * Translation method
     *
     * @param label       a {@link java.lang.String} object.
     * @param description a {@link java.lang.String} object.
     * @param tokens      a {@link java.util.Map} object.
     * @param options     a {@link java.util.Map} object.
     * @return a {@link java.lang.Object} object.
     */
    public Object translateLocal(String label, String description, Map<String, Object> tokens, Map<String, Object> options) {
        String keyHash = TranslationKey.generateKey(label, description);

        if (options == null) {
            options = new HashMap<String, Object>();
        }

        // Source based keys in mobile or desktop environments require sources to be pre-loading in a separate thread
        Source source = getApplication().getSource("index", getLocale(), options);
        TranslationKey matchedKey = null;
        if (source != null && (matchedKey = source.getTranslationKey(keyHash)) != null) {
            if (matchedKey.getLabel() == null) {
                matchedKey.setLabel(label);
                matchedKey.setDescription(description);
            }
            return matchedKey.translate(this, tokens, options);
        } else {
            String log = "Label not found" + "\nLabel: " + label + "\nDescription: " + description + "\nKey Hash: " + keyHash;
            Tml.getLogger().warn("Language", log);

            Map<String, Object> opts = new HashMap<String, Object>(options);
            opts.put("pending", "true");
            String sourcePath = Utils.join(getApplication().getSession().getSourcePath(), Configuration.SOURCE_SEPARATOR);
            TranslationKey tempKey = createTranslationKey(keyHash, label, description, opts);
            getApplication().registerMissingTranslationKey(tempKey, sourcePath);
            return tempKey.translate(this, tokens, options);
        }
    }

    /**
     * <p>getAlignment.</p>
     *
     * @param target a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public String getAlignment(String target) {
        if (isRightToLeft()) return target;
        return target.equals("left") ? "right" : "left";
//        return target == "left" ? "right" : "left";
    }

    /**
     * <p>getDirection.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getDirection() {
        return isRightToLeft() ? "rtl" : "ltr";
    }

    /**
     * <p>toString.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String toString() {
        return this.getEnglishName() + " (" + getLocale() + ")";
    }
}
