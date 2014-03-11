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

public class Translation {

    /**
     * Reference to the translation key it belongs to
     */
    TranslationKey translationKey;

    /**
     * Reference to the language it belongs to
     */
    Language language;

    /**
     * Locale of the language it belongs to
     */
    String locale;

    /**
     * Translation label
     */
    String label;

    /**
     * Translation context hash:
     * {token1: {context1: rule1}, token2: {context2: rule2}}
     */
    Map context;

    /**
     * Precedence of the translation.
     * The higher the precedence the higher the order of the translation.
     */
    Integer precedence;

    /**
     * Check if the rule key is of a default rule. Usually "other" rules.
     * @param ruleKey
     * @return
     */
    public boolean isDefaultRule(String ruleKey) {
        return false;
    }

    /**
     * Check if the translation has context rules
     * @return
     */
    public boolean hasContext() {
        return false;
    }

    /**
     * Checks if the translation is valid for the given tokens
     * @param tokens
     * @return
     */
    public boolean isValidTranslationForTokens(Map tokens) {
        return false;
    }

}
