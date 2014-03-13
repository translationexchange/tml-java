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

import java.math.BigInteger;
import java.util.*;
import java.security.*;

public class TranslationKey extends Base {

    /**
     * Reference to the application where the key came from
     */
    Application application;

    /**
     * Unique key (md5 hash) identifying this translation key
     */
    String key;

    /**
     * Text to be translated
     */
    String label;

    /**
     * Description of the text to be translated
     */
    String description;

    /*
     * Locale of the text to be translated
     */
    String locale;

    /**
     * Level of the key
     */
    Long level;

    /**
     * Hash of translations for each locale needed by the application
     */
    Map<String, List> translations;

    /**
     * Holds all data tokens found in the translation key
     */
    List dataTokens;

    /**
     * Holds all decoration tokens found in the translation key
     */
    List decorationTokens;

    /**
     *
     * @param attributes
     */
    public TranslationKey(Map attributes) {
        super(attributes);
    }

    /**
     *
     * @param attributes
     */
    public void updateAttributes(Map attributes) {
        if (attributes.get("application") != null)
            this.application = (Application) attributes.get("application");

        this.label = (String) attributes.get("label");
        this.description = (String) attributes.get("description");

        if (attributes.get("key") != null) {
            this.key = (String) attributes.get("key");
        } else {
            this.key = generateKey(label, description);
        }

        this.locale = (String) attributes.get("locale");
        if (this.locale == null)
            this.locale = Tr8n.getConfig().defaultLocale;

        this.level = (Long) attributes.get("level");

        this.translations = new HashMap<String, List>();
        if (attributes.get("translations") != null) {
            Iterator entries = ((Map<String, Object>) attributes.get("translations")).entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry entry = (Map.Entry) entries.next();
                List localeData = (List) entry.getValue();
                String locale = (String) entry.getKey();
                Language language = this.application.getLanguage(locale);

                if (this.translations.get(locale) == null) {
                    this.translations.put(locale, new ArrayList());
                }

                for (Object translationData : localeData) {
                    Map attrs = new HashMap((Map) translationData);
                    attrs.put("language", language);
                    attrs.put("translation_key", this);

                    Translation translation = new Translation(attrs);
                    this.translations.get(locale).add(translation);
                }
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
     * Sets translations for language
     * @param newTranslation
     * @param language
     */
    public void updateTranslations(List<Translation> newTranslation, Language language) {

    }

    /**
     * Returns YES if there are translations available for the key
     * @return
     */
    public boolean hasTranslations() {
        return this.translations.size() > 0;
    }

    /**
     *
     * @param language
     * @return
     */
    public String translate(Language language) {
        return translate(language, null);
    }

    /**
     *
     * @param language
     * @param tokens
     * @return
     */
    public String translate(Language language, Map tokens) {
        return translate(language, null, null);
    }

    /**
     *
     * @param language
     * @param tokens
     * @param options
     * @return
     */
    public String translate(Language language, Map tokens, Map options) {
        return "";
    }

}
