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

public class LanguageCase {

    /**
     * Holds reference back to the language it belongs to
     */
    Language language;

    /**
     * How to apply the case: "phrase" or "words"
     */
    String application;

    /**
     * Unique key identifying the language case
     */
    String keyword;

    /**
     * General name in Latin
     */
    String latinName;

    /**
     * Name in native language
     */
    String nativeName;

    /**
     * Description of the language case
     */
    String description;

    /**
     * List of all rules for the language case
     */
    List<LanguageCaseRule> rules;


    /**
     * Finds matching rule for value
     * @param value
     * @return
     */
    public LanguageCaseRule findMatchingRule(String value) {
        return findMatchingRule(value, null);
    }

    /**
     * Finds matching rule for value and object
     * @param value
     * @param object
     * @return
     */
    public LanguageCaseRule findMatchingRule(String value, Object object) {
        return null;
    }

    /**
     * Applies rule for value
     * @param value
     * @return
     */
    public String apply(String value) {
        return apply(value, null);
    }

    /**
     * Applies rule for value based on object properties
     * @param value
     * @param object
     * @return
     */
    public String apply(String value, Object object) {
        return "";
    }
}
