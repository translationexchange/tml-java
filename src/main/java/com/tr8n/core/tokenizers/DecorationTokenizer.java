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

import com.tr8n.core.Language;
import com.tr8n.core.Utils;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DecorationTokenizer extends Tokenizer {
    public static final String TOKEN_BRACKET = "[";

    public static final String RESERVED_TOKEN  = "tr8n";
    public static final String RE_SHORT_TOKEN_START = "\\[[\\w]*:";
    public static final String RE_SHORT_TOKEN_END   = "\\]";
    public static final String RE_LONG_TOKEN_START  = "\\[[\\w]*\\]";
    public static final String RE_LONG_TOKEN_END    = "\\[\\/[\\w]*\\]";
    public static final String RE_TEXT              = "[^\\[\\]]+";

    public static final String TOKEN_TYPE_SHORT     = "short";
    public static final String TOKEN_TYPE_LONG      = "long";
    public static final String PLACEHOLDER          = "{$0}";


    /**
     * Compiled expression extracted from the label
     */
    Object expression;

    /**
     * List of fragments, used internally
     */
    List<String> fragments;

    /**
     * List of elements, used internally
     */
    List<String> elements;

    /**
     *
     */
    public DecorationTokenizer() {
        super();
    }

    /**
     *
     * @param label
     */
    public DecorationTokenizer(String label) {
        super(label, null);
    }

    /**
     *
     * @param label
     * @param allowedTokenNames
     */
    public DecorationTokenizer(String label, List<String> allowedTokenNames) {
        super(label, allowedTokenNames);
    }

    /**
     * @param label Label to be tokenized
     * @param allowedTokenNames List of allowed tokens
     */
    public void tokenize(String label, List<String> allowedTokenNames) {
        this.label =  "[" + RESERVED_TOKEN + "]" + label + "[/" + RESERVED_TOKEN + "]";
        this.tokenNames = new ArrayList<String>();
        this.allowedTokenNames = allowedTokenNames;
        tokenize();
        this.expression = parse();
    }

    /**
     *
     */
    protected void tokenize() {
        String regex = StringUtils.join(Utils.buildList(
                RE_SHORT_TOKEN_START,
                RE_SHORT_TOKEN_END,
                RE_LONG_TOKEN_START,
                RE_LONG_TOKEN_END,
                RE_TEXT
            ).toArray(), "|"
        );

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(this.label);

        this.elements = new ArrayList<String>();

        while(matcher.find()) {
            this.elements.add(matcher.group());
        }

        // keep a copy for reference
        this.fragments = new ArrayList<String>(this.elements);
    }

    /**
     *
     * @return
     */
    private boolean isEmpty() {
        return this.elements.size() == 0;
    }

    /**
     *
     * @return
     */
    private String peek() {
        if (isEmpty()) return null;
        return (String) this.elements.get(0);
    }

    /**
     *
     * @return
     */
    private String pop() {
        String element = peek();
        if (element == null) return null;
        this.elements.remove(0);
        return element;
    }

    /**
     *
     * @param token
     * @param regex
     * @return
     */
    private boolean isMatchingExpression(String token, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(token);
        return matcher.find();
    }

    /**
     *
     * @return
     */
    protected Object parse() {
        String token = pop();

        if (isMatchingExpression(token, RE_SHORT_TOKEN_START)) {
            token = token.trim().replaceAll("[\\[:]", "");
            return parseTree(token, TOKEN_TYPE_SHORT);
        }

        if (isMatchingExpression(token, RE_LONG_TOKEN_START)) {
            token = token.trim().replaceAll("[\\[\\]]", "");
            return parseTree(token, TOKEN_TYPE_LONG);
        }

        return token;
    }

    /**
     *
     * @param name
     * @param type
     * @return
     */
    protected Object parseTree(String name, String type) {
        List tree = new ArrayList();
        tree.add(name);

        if (!tokenNames.contains(name) && !name.equals(RESERVED_TOKEN))
            tokenNames.add(name);

        if (type.equals(TOKEN_TYPE_SHORT)) {
            boolean first = true;

            while (peek() != null && ! isMatchingExpression(peek(), RE_SHORT_TOKEN_END)) {
                Object value = parse();
                if (first && value instanceof String) {
                    value = ((String) value).trim();
                    first = false;
                }
                tree.add(value);
            }

        } else if (type.equals(TOKEN_TYPE_LONG)) {
            while (peek() != null && ! isMatchingExpression(peek(), RE_LONG_TOKEN_END)) {
                tree.add(parse());
            }
        }

        pop();
        return tree;
    }

    /**
     *
     * @param token
     * @return
     */
    protected boolean isTokenAllowed(String token) {
        return this.allowedTokenNames == null || allowedTokenNames.contains(token);
    }

    /**
     *
     * @param token
     * @param value
     * @return
     */
    protected String applyToken(String token, String value) {
        return value;
    }

    /**
     *
     * @param expr
     * @return
     */
    protected String evaluate(Object expr) {
        if (!(expr instanceof List))
            return expr.toString();

        List args = new ArrayList((List) expr);
        String token = (String) args.remove(0);


        List processedValues = new ArrayList();
        for (Object arg : args) {
            processedValues.add(evaluate(arg));
        }

        String value = StringUtils.join(processedValues.toArray(), "");
        return applyToken(token, value);
    }

    /**
     *
     * @param tokensData
     * @param options
     * @return
     */
    public String substitute(Map tokensData, Language language, Map options) {
        this.tokensData = tokensData;
        this.options = options;
        return evaluate(this.expression);
    }

    /**
     *
     * @param label
     * @return
     */
    public static boolean shouldBeUsed(String label) {
        return label.contains(TOKEN_BRACKET);
    }

}
