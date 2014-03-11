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

import java.util.List;
import java.util.Map;

public class TranslationKey {

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
    Integer level;

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
     * Generates unique hash key for the translation key using label
     * @param label
     * @return
     */
    public String generateKey(String label) {
        return generateKey(label, null);
    }

    /**
     * Generates unique hash key for the translation key using label and description
     * @param label
     * @param description
     * @return
     */
    public String generateKey(String label, String description) {
        return "";
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
        return false;
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
