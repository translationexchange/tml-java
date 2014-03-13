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

package com.tr8n.core.tokenizers.tokens;

import com.tr8n.core.Language;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public abstract class BaseToken {

    /**
     * Label from which the key was created
     */
    String originalLabel;

    /**
     * Full value of the token, used for replacement
     */
    String fullName;

    /**
     * Just the name of the token
     */
    String shortName;

    /**
     * Returns token's regular expression
     * @return
     */
    public static String getExpression() {
        return null;
    }

    /**
     * Default constructor
     * @param token
     */
    public BaseToken(String token) {
        this(token, null);
    }

    /**
     * Constructor for token and label
     * @param token
     * @param label
     */
    public BaseToken(String token, String label) {
        this.fullName = token;
        this.originalLabel = label;
    }

    /**
     * Returns full name of the token as it appeared in the label
     * @return
     */
    public String getFullName() {
        return this.fullName;
    }

    /**
     *
     * @return original label from where the token was created
     */
    public String getOriginalLabel() {
        return this.originalLabel;
    }

    /**
     * Used internally for generating token names
     *
     * @return Name without parens
     */
    protected String getParenslessName() {
        return getFullName().replaceAll("[\\{\\}]", "");
    }

    /**
     * Returns just the name of the token
     * @return
     */
    public String getName() {
        if (this.shortName == null) {
            this.shortName = getParenslessName().split(":")[0].trim();
        }
        return this.shortName;
    }

    /**
     * Allows you to customize the returned token name
     * @param options
     * @return
     */
    public String getName(Map options) {
        StringBuilder sb = new StringBuilder();
        if (options.get("parens").equals(true))
            sb.append("{");
        sb.append(getName());
        if (options.get("parens").equals(true))
            sb.append("}");
        return sb.toString();
    }

    /**
     * Utility for debugging tokens
     * @return
     */
    public String toString() {
        return getFullName();
    }

    /**
     * Substitution method that must be implemented by each token
     * @param tokensData
     * @param language
     * @param options
     * @return
     */
    public abstract String substitute(String translatedLabel, Map tokensData, Language language, Map options);
}
