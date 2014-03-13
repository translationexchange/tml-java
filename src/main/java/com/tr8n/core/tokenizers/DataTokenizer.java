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

import com.tr8n.core.Base;
import com.tr8n.core.Language;
import com.tr8n.core.Tr8n;
import com.tr8n.core.tokenizers.tokens.BaseToken;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataTokenizer {

    /**
     * Label from which the tokens were extracted (original or translated)
     */
    String label;

    /**
     * List of allowed token names from the original label
     */
    List<String> allowedTokenNames;

    /**
     * Token objects generated from the label
     */
    List<BaseToken> tokens;

    /**
     * Names of all registered tokens
     */
    List<String> tokenNames;

    /**
     * Default constructor
     */
    public DataTokenizer() {
        // Do nothing here
    }

    /**
     * Constructs Tokenizer with label
     *
     * @param label label to be tokenized
     */
    public DataTokenizer(String label) {
        this(label, null);
    }

    /**
     *
     * @param label label to be tokenized
     * @param allowedTokenNames list of allowed token names
     */
    public DataTokenizer(String label, List<String> allowedTokenNames) {
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

    // TODO: verify if reflection is too slow and switch to method invocation instead
    private void tokenize() {
        try {
            this.tokens = new ArrayList<BaseToken>();
            List<String> tokenMatches = new ArrayList<String>();
            String matchingLabel = this.label;
            for (String token : Tr8n.getConfig().getTokenClasses()) {
                Class tokenClass = Class.forName(token);
                Method method = tokenClass.getMethod("getExpression");
                String expression = (String) method.invoke(null);

                Pattern pattern = Pattern.compile(expression);
                Matcher matcher = pattern.matcher(matchingLabel);

                while(matcher.find()) {
                    String match = matcher.group();
                    if (tokenMatches.contains(match)) continue;
                    tokenMatches.add(match);
                    Constructor constructor = tokenClass.getConstructor(String.class, String.class);
                    BaseToken registeredToken = (BaseToken) constructor.newInstance(match, this.label);
                    this.tokens.add(registeredToken);
                    matchingLabel = matchingLabel.replaceAll(Pattern.quote(match), "");
                }
            }
        } catch (Exception ex) {
            Tr8n.getLogger().logException("Failed to tokenize a label: " + label, ex);
        }
    }

    public List getTokenNames() {
        if (this.tokenNames == null) {
            this.tokenNames = new ArrayList<String>();
            for (BaseToken token : this.tokens) {
                this.tokenNames.add(token.getName());
            }
        }
        return this.tokenNames;
    }

    public List<String> getAllowedTokenNames() {
        return this.allowedTokenNames;
    }

    public boolean isTokenAllowed(BaseToken token) {
        if (this.getAllowedTokenNames() == null)
            return true;

        return this.getAllowedTokenNames().contains(token.getName());
    }

    public String substitute(Map tokensData, Language language) {
        return substitute(tokensData, language, null);
    }

    public String substitute(Map tokensData, Language language, Map options) {
        String translatedLabel = this.label;
        for (BaseToken token : this.tokens) {
            translatedLabel = token.substitute(translatedLabel, tokensData, language, options);
        }
        return translatedLabel;
    }
}
