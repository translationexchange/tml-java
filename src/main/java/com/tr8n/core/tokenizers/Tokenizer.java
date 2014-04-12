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

package com.tr8n.core.tokenizers;

import java.util.List;
import java.util.Map;

import com.tr8n.core.Language;
import com.tr8n.core.tokenizers.tokens.Token;

/**
 * Base class for all tokenizers
 */
public abstract class Tokenizer {

    /**
     * Label from which the tokens were extracted (original or translated)
     */
    protected String label;

    /**
     * List of allowed token names from the original label
     */
    protected List<String> allowedTokenNames;

    /**
     * Names of all registered tokens
     */
    protected List<String> tokenNames;

    /**
     * Tokens data map, used internally
     */
    protected Map<String, Object> tokensData;

    /**
     * Substitution options, used internally
     */
    protected Map<String, Object> options;

    /**
     * Default constructor
     */
    public Tokenizer() {
    }

    /**
     * Constructs tokenizer with label
     *
     * @param label label to be tokenized
     */
    public Tokenizer(String label) {
        this(label, null);
    }

    /**
     * Constructs tokenizer with label and list of allowed token names
     *   
     * @param label
     * @param allowedTokenNames
     */
    public Tokenizer(String label, List<String> allowedTokenNames) {
        tokenize(label, allowedTokenNames);
    }

    /**
     *
     * @param label
     */
    public void tokenize(String label) {
        tokenize(label, null);
    }

    /**
     *
     * @param label
     * @param allowedTokenNames
     */
    public void tokenize(String label, List<String> allowedTokenNames) {
        this.label = label;
        this.allowedTokenNames = allowedTokenNames;
        this.tokenNames = null;
        tokenize();
    }

    /**
     * Tokenizes the expression
     */
    protected abstract void tokenize();

    public List<String> getTokenNames() {
        return this.tokenNames;
    }

    /**
     *
     * @return
     */
    public List<String> getAllowedTokenNames() {
        return this.allowedTokenNames;
    }

    /**
     *
     * @param token
     * @return
     */
    public boolean isTokenAllowed(Token token) {
        if (this.getAllowedTokenNames() == null)
            return true;

        return this.getAllowedTokenNames().contains(token.getName());
    }

    /**
     *
     * @param tokensData
     * @return
     */
    public Object substitute(Map<String, Object> tokensData) {
        return substitute(tokensData, null);
    }

    /**
     *
     * @param tokensData
     * @param language
     * @return
     */
    public Object substitute(Map<String, Object> tokensData, Language language) {
        return substitute(tokensData, language, null);
    }

    /**
     *
     * @param tokensData
     * @param language
     * @param options
     * @return
     */
    public abstract Object substitute(Map<String, Object> tokensData, Language language, Map<String, Object> options);

    /**
     * Returns true/false whether the tokenizer is applicable to the label
     * 
     * @param label
     * @return
     */
    public static boolean isApplicable(String label) {
        return false;
    }
    
}

