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

import com.tr8n.core.tokenizers.AttributedStringTokenizer;
import com.tr8n.core.tokenizers.DataTokenizer;
import com.tr8n.core.tokenizers.DecorationTokenizer;
import com.tr8n.core.tokenizers.HtmlTokenizer;

import java.math.BigInteger;
import java.text.AttributedString;
import java.util.*;
import java.security.*;

public class TranslationKey extends Base {

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

    /*
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
     * Holds all data tokens found in the translation key
     */
    private List dataTokens;

    /**
     * Holds all decoration tokens found in the translation key
     */
    private List decorationTokens;


    /**
     * List of data token names from the original label
     */
    private List<String> allowedDataTokenNames;

    /**
     * List of decoration token names from the original label
     */
    private List<String> allowedDecorationTokenNames;

    /****************************************************************************************************/
    /**
     *
     * @param attributes
     */
    public TranslationKey(Map<String, Object> attributes) {
        super(attributes);
    }

    /**
     *
     * @param attributes
     */
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

        setLevel((Long) attributes.get("level"));

        if (attributes.get("translations") != null && this.application != null) {
            Iterator entries = ((Map) attributes.get("translations")).entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry entry = (Map.Entry) entries.next();
                List localeData = (List) entry.getValue();
                String locale = (String) entry.getKey();
                Language language = getApplication().getLanguage(locale);

                List<Translation> translations = new ArrayList<Translation>();
                for (Object translationData : localeData) {
                    Map attrs = new HashMap((Map) translationData);
                    attrs.put("language", language);
                    attrs.put("translation_key", this);
                    Translation translation = new Translation(attrs);
                    translations.add(translation);
                }
                setTranslations(locale, translations);
            }
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
            MessageDigest m=MessageDigest.getInstance("MD5");
            m.update(s.getBytes(),0,s.length());
            return new BigInteger(1,m.digest()).toString(16);
        } catch (Exception ex) {
            Tr8n.getLogger().error("Failed to generate md5 key for " + label + " " + description);
            Tr8n.getLogger().error(ex);
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
     *
     * @param language
     * @param tokens
     * @return
     */
    private Translation findFirstAcceptableTranslation(Language language, Map tokens) {
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
    public Object translate(Language language, Map tokens) {
        return translate(language, null, null);
    }

    /**
     *
     * @param language
     * @param tokens
     * @param options
     * @return
     */
    public Object translate(Language language, Map tokens, Map options) {
        if (getLocale().equals(language.getLocale())) {
            return substitute(label, tokens, language, options);
        }

        Translation translation = findFirstAcceptableTranslation(language, tokens);

        // TODO: add support for decorators in J2EE

        if (translation != null)
            return substitute(translation.getLabel(), tokens, language, options);

        return substitute(getLabel(), tokens, getApplication().getLanguage(), options);
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

    /**
     *
     * @param translatedLabel
     * @param tokens
     * @param language
     * @param options
     * @return
     */
    public Object substitute(String translatedLabel, Map tokens, Language language, Map options) {
        if (DataTokenizer.shouldBeUsed(translatedLabel)) {
            DataTokenizer dt = new DataTokenizer(translatedLabel, getAllowedDataTokenNames());
            translatedLabel = dt.substitute(tokens, language, options);
        }

        if (options != null && options.get("tokenizer") != null && options.get("tokenizer").equals("attributed")) {
            if (DecorationTokenizer.shouldBeUsed(translatedLabel)) {
                AttributedStringTokenizer ht = new AttributedStringTokenizer(translatedLabel, getAllowedDecorationTokenNames());
                return ht.generateAttributedString(tokens, options);
            }
            return new AttributedString(translatedLabel);
        }

        if (DecorationTokenizer.shouldBeUsed(translatedLabel)) {
            HtmlTokenizer ht = new HtmlTokenizer(translatedLabel, getAllowedDecorationTokenNames());
            return ht.substitute(tokens, language, options);
        }

        return translatedLabel;
    }


    /****************************************************************************************************/

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

    public List<Translation> getTranslations(String locale) {
        return getTranslationsByLocale().get(locale);
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

    public List getDataTokens() {
        return dataTokens;
    }

    public void setDataTokens(List dataTokens) {
        this.dataTokens = dataTokens;
    }

    public List getDecorationTokens() {
        return decorationTokens;
    }

    public void setDecorationTokens(List decorationTokens) {
        this.decorationTokens = decorationTokens;
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

    public Map toMap() {
        Map data = new HashMap();
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

