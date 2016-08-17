/**
 * Copyright (c) 2016 Translation Exchange, Inc. All rights reserved.
 *
 *  _______                  _       _   _             ______          _
 * |__   __|                | |     | | (_)           |  ____|        | |
 *    | |_ __ __ _ _ __  ___| | __ _| |_ _  ___  _ __ | |__  __  _____| |__   __ _ _ __   __ _  ___
 *    | | '__/ _` | '_ \/ __| |/ _` | __| |/ _ \| '_ \|  __| \ \/ / __| '_ \ / _` | '_ \ / _` |/ _ \
 *    | | | | (_| | | | \__ \ | (_| | |_| | (_) | | | | |____ >  < (__| | | | (_| | | | | (_| |  __/
 *    |_|_|  \__,_|_| |_|___/_|\__,_|\__|_|\___/|_| |_|______/_/\_\___|_| |_|\__,_|_| |_|\__, |\___|
 *                                                                                        __/ |
 *                                                                                       |___/
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.translationexchange.core.tokens;

import java.util.List;
import java.util.Map;

import com.translationexchange.core.languages.Language;

/**
 * Base abstract class for all tokens supported by TML
 *
 * @author Berk
 * @version $Id: $Id
 */
public abstract class Token {
    /** Constant <code>OPTIONS_PARENS="parens"</code> */
    public static final String OPTIONS_PARENS = "parens";

    /**
     * Label from which the key was created
     */
    protected String originalLabel;

    /**
     * Full value of the token, used for replacement
     */
    protected String fullName;

    /**
     * Just the name of the token
     */
    protected String shortName;

    /**
     * Returns token's regular expression
     *
     * @return a {@link java.lang.String} object.
     */
    public static String getExpression() {
        return null;
    }

    /**
     * Default constructor
     *
     * @param token a {@link java.lang.String} object.
     */
    public Token(String token) {
        this(token, null);
    }

    /**
     * Constructor for token and label
     *
     * @param token a {@link java.lang.String} object.
     * @param label a {@link java.lang.String} object.
     */
    public Token(String token, String label) {
        this.fullName = token;
        this.originalLabel = label;
    }

    /**
     * Returns full name of the token as it appeared in the label
     *
     * @return a {@link java.lang.String} object.
     */
    public String getFullName() {
        return this.fullName;
    }

    /**
     * <p>Getter for the field <code>originalLabel</code>.</p>
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
     *
     * @return a {@link java.lang.String} object.
     */
    public String getName() {
        if (this.shortName == null) {
            this.shortName = getParenslessName().split(":")[0].trim();
        }
        return this.shortName;
    }

    /**
     * <p>getObjectName.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getObjectName() {
        return getName();
    }

    /**
     * Allows you to customize the returned token name
     *
     * @param options a {@link java.util.Map} object.
     * @return a {@link java.lang.String} object.
     */
    public String getName(Map<String, Object> options) {
        StringBuilder sb = new StringBuilder();
        if (options.get(OPTIONS_PARENS).equals(true))
            sb.append("{");
        sb.append(getName());
        if (options.get(OPTIONS_PARENS).equals(true))
            sb.append("}");
        return sb.toString();
    }

    /**
     * Utility for debugging tokens
     *
     * @return a {@link java.lang.String} object.
     */
    public String toString() {
        return getFullName();
    }

    /**
     * Returns the name of the decoration class
     *
     * @return a {@link java.lang.String} object.
     */
    public String getDecorationName() {
    	return "data";
    }
    
    /**
     * Returns language context keys
     *
     * @return a {@link java.util.List} object.
     */
    public abstract List<String> getLanguageContextKeys();

    /**
     * Returns language case keys
     *
     * @return a {@link java.util.List} object.
     */
    public abstract List<String> getLanguageCaseKeys();
    
    /**
     * Substitution method that must be implemented by each token
     *
     * @param tokensData a {@link java.util.Map} object.
     * @param language a {@link com.translationexchange.core.languages.Language} object.
     * @param options a {@link java.util.Map} object.
     * @param translatedLabel a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public abstract String substitute(String translatedLabel, Map<String, Object> tokensData, Language language, Map<String, Object> options);
    
    /**
     * Logs error message
     * 
     * @param msg a {@link java.lang.String} object
     */
    protected void logError(String msg) {
//    	Tml.getLogger().error(msg);
    }
}
