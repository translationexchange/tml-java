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

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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
    }

    /**
     * Default language constructor
     * @param attributes
     */
    public Language(Map<String, Object> attributes) {
        super(attributes);
    }

    /**
     *
     * @param attributes
     */
    public void updateAttributes(Map<String, Object> attributes) {
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
                context.setLanguage(this);
                addContext(context);
            }
        }

        if (attributes.get("cases") != null) {
            Iterator entries = ((Map) attributes.get("cases")).entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry entry = (Map.Entry) entries.next();
                LanguageCase languageCase = new LanguageCase((Map) entry.getValue());
                languageCase.setLanguage(this);
                addLanguageCase(languageCase);
            }
        }
    }

    public void load() {
        try {
            Map data = (Map) application.get("language", Utils.buildMap("locale", this.locale, "definition", "true"));
            this.updateAttributes(data);
        } catch (Exception ex) {
            Tr8n.getLogger().error("Failed to load language");
            Tr8n.getLogger().error(ex);
            Tr8n.getLogger().error(StringUtils.join(ex.getStackTrace(), "\n"));
        }
    }


    public void addContext(LanguageContext languageContext) {
        if (contexts == null)
            contexts = new HashMap<String, LanguageContext>();
        contexts.put(languageContext.getKeyword(), languageContext);
    }


    /**
     * Returns language context based on the keyword
     * @param keyword
     * @return
     */
    public LanguageContext getContextByKeyword(String keyword) {
        if (contexts == null)
            return null;
        return this.contexts.get(keyword);
    }

    /**
     * Returns language context based on the token name
     * @param tokenName
     * @return
     */
    public LanguageContext getContextByTokenName(String tokenName) {
        if (contexts == null)
            return null;

        for (LanguageContext context : contexts.values()) {
            if (context.isApplicableToTokenName(tokenName))
                return context;
        }
        return null;
    }


    /**
     * Returns language case based on the keyword
     * @param keyword
     * @return
     */
    public LanguageCase getLanguageCaseByKeyword(String keyword) {
        if (cases == null)
            return null;
        return cases.get(keyword);
    }

    public void addLanguageCase(LanguageCase languageCase) {
        if (cases == null)
            cases = new HashMap<String, LanguageCase>();
        cases.put(languageCase.getKeyword(), languageCase);
    }

    /**
     * Languages are loaded without definition by default, this will tell if the language has definition or it needs to be loaded
     * @return
     */
    public boolean hasDefinition() {
        return this.contexts != null;
    }

    /**
     * Check if the language is application default
     * @return
     */
    public boolean isDefault() {
        return false;
    }

    /**
     * Generates new translation key
     * @param key
     * @param label
     * @param description
     * @param options
     * @return
     */
    public TranslationKey getTranslationKey(String key, String label, String description, Map options) {
        return null;
    }

    /**
     *
     * @param key
     * @param options
     * @param defaultValue
     * @return
     */
    private Object getOptionsValue(String key, Map options, Object defaultValue) {
        Object value = options.get(key);
        if (value!=null) return value;

        value = getApplication().getSession().getBlockOption(key);
        if (value!=null) return value;

        return defaultValue;
    }


    private TranslationKey createTranslationKey(String keyHash, String label, String description, Map options) {
        String locale = (String) getOptionsValue("locale", options, getApplication().getDefaultLocale());
        Long level = (Long) getOptionsValue("level", options, getApplication().getTranslatorLevel());

        Map attributes = Utils.buildMap(
                "application", getApplication(),
                "key", keyHash,
                "label", label,
                "description", description,
                "locale", locale,
                "level", level
        );

        return new TranslationKey(attributes);
    }

    /**
     * Translation method
     * @param label
     * @param description
     * @param tokens
     * @param options
     * @return
     */
    public Object translate(String label, String description, Map tokens, Map options) {
        String keyHash = TranslationKey.generateKey(label, description);

        if (getApplication().isKeyRegistrationEnabled()) {
            String sourceKey = (String) getOptionsValue("source", options, getApplication().getSession().getCurrentSource());

            if (sourceKey == null)
                sourceKey = "undefined source";

            Source source = getApplication().getSource(sourceKey, this.getLocale());
            if (source != null) {
                TranslationKey matchedKey = source.getTranslationKey(keyHash);
                if (matchedKey != null) return matchedKey.translate(this, tokens, options);

                TranslationKey tempKey = createTranslationKey(keyHash, label, description, options);
                getApplication().registerMissingTranslationKey(tempKey, source);
            }
        }

        TranslationKey matchedKey = getApplication().getTranslationKey(keyHash);
        if (matchedKey != null) return matchedKey.translate(this, tokens, options);

        TranslationKey tempKey = createTranslationKey(keyHash, label, description, options);
        return tempKey.translate(this, tokens, options);
    }


    /**
     *
     * @param label
     * @return
     */
    public Object translate(String label) {
        return translate(label, "");
    }

    /**
     *
     * @param label
     * @param description
     * @return
     */
    public Object translate(String label, String description) {
        return translate(label, description, null, null);
    }

    /**
     *
     * @param label
     * @param tokens
     * @return
     */
    public Object translate(String label, Map tokens) {
        return translate(label, "", tokens, null);
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String toString() {
        return  this.englishName + "(" + this.locale + ")";
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public String getNativeName() {
        return nativeName;
    }

    public void setNativeName(String nativeName) {
        this.nativeName = nativeName;
    }

    public Boolean getRightToLeft() {
        return rightToLeft;
    }

    public void setRightToLeft(Boolean rightToLeft) {
        this.rightToLeft = rightToLeft;
    }

    public String getFlagUrl() {
        return flagUrl;
    }

    public void setFlagUrl(String flagUrl) {
        this.flagUrl = flagUrl;
    }

    public Map<String, LanguageContext> getContexts() {
        return contexts;
    }

    public void setContexts(Map<String, LanguageContext> contexts) {
        this.contexts = contexts;
    }

    public Map<String, LanguageCase> getCases() {
        return cases;
    }

    public void setCases(Map<String, LanguageCase> cases) {
        this.cases = cases;
    }
}
