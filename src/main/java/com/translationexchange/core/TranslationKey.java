
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

package com.translationexchange.core;

import com.translationexchange.core.decorators.Decorator;
import com.translationexchange.core.languages.Language;
import com.translationexchange.core.tokenizers.DataTokenizer;
import com.translationexchange.core.tokenizers.DecorationTokenizer;
import com.translationexchange.core.tokenizers.Tokenizer;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class TranslationKey extends Base {

    /**
     * Constant <code>TOKENIZER_KEY="tokenizer"</code>
     */
    public static final String TOKENIZER_KEY = "tokenizer";
    /**
     * Constant <code>DEFAULT_TOKENIZERS_DATA="data"</code>
     */
    public static final String DEFAULT_TOKENIZERS_DATA = "data";
    /**
     * Constant <code>DEFAULT_TOKENIZERS_HTML="html"</code>
     */
    public static final String DEFAULT_TOKENIZERS_HTML = "html";
    /**
     * Constant <code>DEFAULT_TOKENIZERS_STYLED="styled"</code>
     */
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
     * @param key
     */
    public TranslationKey(String key) {
        this.key = key;
    }

    /**
     * <p>Constructor for TranslationKey.</p>
     *
     * @param attributes a {@link java.util.Map} object.
     */
    public TranslationKey(Map<String, Object> attributes) {
        super(attributes);
    }

    /**
     * <p>Getter for the field <code>label</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getLabel() {
        return label;
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
     * <p>Getter for the field <code>key</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getKey() {
        return key;
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
     * <p>Getter for the field <code>level</code>.</p>
     *
     * @return a {@link java.lang.Long} object.
     */
    public Long getLevel() {
        return level;
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
     * <p>Getter for the field <code>allowedDataTokenNames</code>.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<String> getAllowedDataTokenNames() {
        if (allowedDataTokenNames == null) {
            DataTokenizer dt = new DataTokenizer(getLabel());
            allowedDataTokenNames = dt.getTokenNames();
        }
        return allowedDataTokenNames;
    }

    /**
     * <p>Getter for the field <code>allowedDecorationTokenNames</code>.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<String> getAllowedDecorationTokenNames() {
        if (allowedDecorationTokenNames == null) {
            DecorationTokenizer dt = new DecorationTokenizer(getLabel());
            allowedDecorationTokenNames = dt.getTokenNames();
        }
        return allowedDecorationTokenNames;
    }

    /**
     * <p>Getter for the field <code>translationsByLocale</code>.</p>
     *
     * @return a {@link java.util.Map} object.
     */
    public Map<String, List<Translation>> getTranslationsByLocale() {
        if (translationsByLocale == null)
            translationsByLocale = new HashMap<String, List<Translation>>();

        return translationsByLocale;
    }

    /**
     * Sets translations for a specific locale. Translation key can have translations for multiple locales.
     *
     * @param locale       a {@link java.lang.String} object.
     * @param translations a {@link java.util.List} object.
     */
    public void setTranslations(String locale, List<Translation> translations) {
        for (Translation translation : translations) {
            translation.setTranslationKey(this);
        }

        getTranslationsByLocale().put(locale, translations);
    }

    /**
     * <p>getTranslationLocales.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<String> getTranslationLocales() {
        return new ArrayList<String>(getTranslationsByLocale().keySet());
    }

    /**
     * <p>Setter for the field <code>label</code>.</p>
     *
     * @param label a {@link java.lang.String} object.
     */
    public void setLabel(String label) {
        this.label = label;
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
     * <p>Setter for the field <code>locale</code>.</p>
     *
     * @param locale a {@link java.lang.String} object.
     */
    public void setLocale(String locale) {
        this.locale = locale;
    }

    /**
     * <p>Setter for the field <code>level</code>.</p>
     *
     * @param level a {@link java.lang.Long} object.
     */
    public void setLevel(Long level) {
        this.level = level;
    }

    /**
     * <p>Setter for the field <code>allowedDataTokenNames</code>.</p>
     *
     * @param allowedDataTokenNames a {@link java.util.List} object.
     */
    public void setAllowedDataTokenNames(List<String> allowedDataTokenNames) {
        this.allowedDataTokenNames = allowedDataTokenNames;
    }

    /**
     * <p>Setter for the field <code>allowedDecorationTokenNames</code>.</p>
     *
     * @param allowedDecorationTokenNames a {@link java.util.List} object.
     */
    public void setAllowedDecorationTokenNames(List<String> allowedDecorationTokenNames) {
        this.allowedDecorationTokenNames = allowedDecorationTokenNames;
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
     * <p>isLocked.</p>
     *
     * @return a {@link java.lang.Boolean} object.
     */
    public Boolean isLocked() {
        if (locked == null)
            return false;
        return locked;
    }

    /**
     * <p>Setter for the field <code>locked</code>.</p>
     *
     * @param locked a {@link java.lang.Boolean} object.
     */
    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    /**
     * <p>isRegistered.</p>
     *
     * @return a {@link java.lang.Boolean} object.
     */
    public Boolean isRegistered() {
        return registered;
    }

    /**
     * <p>Setter for the field <code>registered</code>.</p>
     *
     * @param registered a {@link java.lang.Boolean} object.
     */
    public void setRegistered(Boolean registered) {
        this.registered = registered;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
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
            setLocale(Tml.getConfig().getDefaultLocale());

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
     * @param locale a {@link java.lang.String} object.
     * @return a {@link java.util.List} object.
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
     * @param translation a {@link com.translationexchange.core.Translation} object.
     */
    public void addTranslation(Translation translation) {
        List<Translation> translations = getTranslations(translation.getLocale());
        translation.setTranslationKey(this);
        translations.add(translation);
    }

    /**
     * Adds translations
     *
     * @param translations a {@link java.util.List} object.
     */
    public void addTranslations(List<Translation> translations) {
        for (Translation translation : translations) {
            addTranslation(translation);
        }
    }

    /**
     * Generates unique hash key for the translation key using label
     *
     * @param label a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public static String generateKey(String label) {
        return generateKey(label, null);
    }

    /**
     * Generates unique hash key for the translation key using label and description
     *
     * @param label       a {@link java.lang.String} object.
     * @param description a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public static String generateKey(String label, String description) {
        StringBuilder sb = new StringBuilder();
        sb.append(label);
        sb.append(";;;");
        if (description != null) sb.append(description);
        String s = sb.toString();

        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            String hashText = new BigInteger(1, m.digest(s.getBytes("UTF-8"))).toString(16);
            while (hashText.length() < 32) hashText = "0" + hashText;
            return hashText;
        } catch (Exception ex) {
            Tml.getLogger().logException("Failed to generate md5 key for " + label + " " + description, ex);
            return null;
        }
    }

    /**
     * Returns YES if there are translations available for the key
     *
     * @return a boolean.
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

    public Translation findAcceptableTranslation(String locale) {
        List<Translation> availableTranslations = getTranslations(locale);
        if (availableTranslations == null || availableTranslations.size() == 0)
            return null;

        if (availableTranslations.size() == 1) {
            Translation translation = availableTranslations.get(0);
            if (!translation.hasContext()) return translation;
        }

        for (Translation translation : availableTranslations) {
            if (translation.getLocale().equals(locale))
                return translation;
        }

        return null;
    }

    /**
     * <p>translate.</p>
     *
     * @param language a {@link com.translationexchange.core.languages.Language} object.
     * @return a {@link java.lang.Object} object.
     */
    public Object translate(Language language) {
        return translate(language, null);
    }

    /**
     * <p>translate.</p>
     *
     * @param language a {@link com.translationexchange.core.languages.Language} object.
     * @param tokens   a {@link java.util.Map} object.
     * @return a {@link java.lang.Object} object.
     */
    public Object translate(Language language, Map<String, Object> tokens) {
        return translate(language, null, null);
    }

    /**
     * <p>translate.</p>
     *
     * @param language a {@link com.translationexchange.core.languages.Language} object.
     * @param tokens   a {@link java.util.Map} object.
     * @param options  a {@link java.util.Map} object.
     * @return a {@link java.lang.Object} object.
     */
    public Object translate(Language language, Map<String, Object> tokens, Map<String, Object> options) {
        if (getLocale() != null && getLocale().equals(language.getLocale())) {
            return substitute(label, tokens, language, language, options);
        }

        Translation translation = findFirstAcceptableTranslation(language, tokens);

        if (translation != null)
            return substitute(translation.getLabel(), tokens, translation.getLanguage(), language, options);

        return substitute(getLabel(), tokens, getApplication().getLanguage(), language, options);
    }

    /**
     * <p>applyTokenizer.</p>
     *
     * @param key                 a {@link java.lang.String} object. It defines tokenizer class by key e.g. `data`, `html`
     * @param translatedLabel     a {@link java.lang.String} object.
     * @param translationLanguage a {@link com.translationexchange.core.languages.Language} object.
     * @param tokens              a {@link java.util.Map} object.
     * @param options             a {@link java.util.Map} object.
     * @param allowedTokenNames   a {@link java.util.List} object.
     * @return a {@link java.lang.Object} object.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    protected Object applyTokenizer(String key, String translatedLabel, Language translationLanguage,
                                    List<String> allowedTokenNames, Map<String, Object> tokens, Map<String, Object> options) {
        try {
            Class tokenizerClass = getTokenizerByKey(key);
            if (tokenizerClass == null) return translatedLabel;
            Method method = tokenizerClass.getMethod("isApplicable", String.class);
            if ((Boolean) method.invoke(null, translatedLabel)) {
                Constructor<Tokenizer> constructor = tokenizerClass.getConstructor(String.class, List.class);
                Tokenizer tokenizer = (Tokenizer) constructor.newInstance(translatedLabel, allowedTokenNames);
                return tokenizer.substitute(tokens, translationLanguage, options);
            }
        } catch (Exception ex) {
            Tml.getLogger().logException("Failed to tokenize \"" + translatedLabel + "\" using " + key, ex);
        }
        return translatedLabel;
    }

    /**
     * <p>substitute.</p>
     *
     * @param translatedLabel     a {@link java.lang.String} object.
     * @param tokens              a {@link java.util.Map} object.
     * @param options             a {@link java.util.Map} object.
     * @param translationLanguage a {@link com.translationexchange.core.languages.Language} object.
     * @param targetLanguage      a {@link com.translationexchange.core.languages.Language} object.
     * @return a {@link java.lang.Object} object.
     */
    public Object substitute(String translatedLabel, Map<String, Object> tokens, Language translationLanguage, Language targetLanguage, Map<String, Object> options) {
        String tokenizerKey = DEFAULT_TOKENIZERS_HTML;
        if (options != null && options.get(TOKENIZER_KEY) != null)
            tokenizerKey = (String) options.get(TOKENIZER_KEY);

        Object result = null;

        // HTML Tokenizer should always be invoked before the data tokenizer for web pages, to avoid HTML injection through data tokens
        // Non-HTML tokenizer must reverse the order to properly decorate labels
        if (tokenizerKey.equals(DEFAULT_TOKENIZERS_HTML)) {
            translatedLabel = (String) applyTokenizer(DEFAULT_TOKENIZERS_HTML, translatedLabel, translationLanguage, getAllowedDecorationTokenNames(), tokens, options);
            result = applyTokenizer(DEFAULT_TOKENIZERS_DATA, translatedLabel, translationLanguage, getAllowedDataTokenNames(), tokens, options);
        } else {
            translatedLabel = (String) applyTokenizer(DEFAULT_TOKENIZERS_DATA, translatedLabel, translationLanguage, getAllowedDataTokenNames(), tokens, options);
            result = applyTokenizer(tokenizerKey, translatedLabel, translationLanguage, getAllowedDecorationTokenNames(), tokens, options);
        }

        if (result instanceof String) {
            Decorator decorator = Tml.getConfig().getDecorator();
            return decorator.decorate((String) result, translationLanguage, targetLanguage, this, options);
        }

        return result;
    }

    /**
     * Returns map representation for the translation key
     *
     * @return a {@link java.util.Map} object.
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

    /**
     * <p>toString.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String toString() {
        return label + " (" + locale + ")";
    }

    @SuppressWarnings("rawtypes")
    private Class getTokenizerByKey(String key) throws ClassNotFoundException {
        return Class.forName(Tml.getConfig().getTokenizerClass(key));
    }
}

