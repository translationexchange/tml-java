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

import java.util.Map;

public class Language {

    /**
     * Holds reference to the application it belongs to
     */
    Application application;

    /**
     * Language locale based on Tr8n notation
     */
    String locale;

    /**
     * Language name in English
     */
    String englishName;

    /**
     * Language name in the native form
     */
    String nativeName;

    /**
     * Whether the language rtl or ltr
     */
    Boolean rightToLeft;

    /**
     * Url of the language flag image
     */
    String flagUrl;

    /**
     * Hash of all language contexts
     */
    Map<String, LanguageContext> contexts;

    /**
     * Hash of all language cases
     */
    Map<String, LanguageCase> cases;

    public String name() {
        return "";
    }

    public String fullName() {
        return "";
    }

    /**
     * Returns language context based on the keyword
     * @param keyword
     * @return
     */
    public LanguageContext contextByKeyword(String keyword) {
        return null;
    }

    /**
     * Returns language context based on the token name
     * @param tokenName
     * @return
     */
    public LanguageContext contextByTokenName(String tokenName) {
        return null;
    }

    /**
     * Returns language case based on the keyword
     * @param keyword
     * @return
     */
    public LanguageCase languageCaseByKeyword(String keyword) {
        return null;
    }


    /**
     * Languages are loaded without definition by default, this will tell if the language has definition or it needs to be loaded
     * @return
     */
    public boolean hasDefinition() {
        return true;
    }

    /**
     * Check if the language is application default
     * @return
     */
    public boolean isDefault() {
        return false;
    }

    /**
     * If browser is used, this will give HTML direction
     * @return
     */
    public String htmlDirection() {
        return "";
    }

    /**
     * If browser is used, this will give HTML alignment
     * @param defaultAlignment
     * @return
     */
    public String htmlAlignmentWithLtrDefault(String defaultAlignment) {
        return "";
    }

    /**
     * Retrieves value from options, then block, then defaults
     * @param options
     * @param key
     * @param defaultValue
     * @return
     */
    public Object valueFromOptions(Map options, String key, Object defaultValue) {
        return null;
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
     * Translation method
     * @param label
     * @param description
     * @param tokens
     * @param options
     * @return
     */
    public String translate(String label, String description, Map tokens, Map options) {
        return "";
    }

    /**
     *
     * @param label
     * @return
     */
    public String translate(String label) {
        return translate(label, "");
    }

    /**
     *
     * @param label
     * @param description
     * @return
     */
    public String translate(String label, String description) {
        return translate(label, description, null, null);
    }

    /**
     *
     * @param label
     * @param tokens
     * @return
     */
    public String translate(String label, Map tokens) {
        return translate(label, "", tokens, null);
    }

}
