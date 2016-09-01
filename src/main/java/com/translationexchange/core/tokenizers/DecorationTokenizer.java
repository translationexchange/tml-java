
/**
 * Copyright (c) 2016 Translation Exchange, Inc. All rights reserved.
 * <p/>
 * _______                  _       _   _             ______          _
 * |__   __|                | |     | | (_)           |  ____|        | |
 * | |_ __ __ _ _ __  ___| | __ _| |_ _  ___  _ __ | |__  __  _____| |__   __ _ _ __   __ _  ___
 * | | '__/ _` | '_ \/ __| |/ _` | __| |/ _ \| '_ \|  __| \ \/ / __| '_ \ / _` | '_ \ / _` |/ _ \
 * | | | | (_| | | | \__ \ | (_| | |_| | (_) | | | | |____ >  < (__| | | | (_| | | | | (_| |  __/
 * |_|_|  \__,_|_| |_|___/_|\__,_|\__|_|\___/|_| |_|______/_/\_\___|_| |_|\__,_|_| |_|\__, |\___|
 * __/ |
 * |___/
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * @author Berk
 * @version $Id: $Id
 */

package com.translationexchange.core.tokenizers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.translationexchange.core.Utils;
import com.translationexchange.core.languages.Language;

public class DecorationTokenizer extends Tokenizer {
    /** Constant <code>TOKEN_BRACKET</code> */
    public static final String TOKEN_BRACKET = "[";

    /** Constant <code>RESERVED_TOKEN</code> */
    public static final String RESERVED_TOKEN = "tml";
    /** Constant <code>RE_SHORT_TOKEN_START</code> */
    public static final String RE_SHORT_TOKEN_START = "\\[[\\w]*:";                // [link:
    /** Constant <code>RE_SHORT_TOKEN_END</code> */
    public static final String RE_SHORT_TOKEN_END = "\\]";                    // ]
    /** Constant <code>RE_LONG_TOKEN_STAR</code> */
    public static final String RE_LONG_TOKEN_START = "\\[[\\w]*\\]";            // [link]
    /** Constant <code>RE_LONG_TOKEN_END</code> */
    public static final String RE_LONG_TOKEN_END = "\\[\\/[\\w]*\\]";        // [/link]
    /** Constant <code>RE_HTML_TOKEN_START</code> */
    public static final String RE_HTML_TOKEN_START = "<[^\\>]*>";              // <link>
    /** Constant <code>RE_HTML_TOKEN_END</code> */
    public static final String RE_HTML_TOKEN_END = "<\\/[^\\>]*>";            // </link>
    /** Constant <code>RE_TEXT</code> */
    public static final String RE_TEXT = "[^\\[\\]<>]+";            // anything that is left

    /** Constant <code>TOKEN_TYPE_SHORT</code> */
    public static final String TOKEN_TYPE_SHORT = "short";
    /** Constant <code>TOKEN_TYPE_LONG</code> */
    public static final String TOKEN_TYPE_LONG = "long";
    /** Constant <code>TOKEN_TYPE_HTML</code> */
    public static final String TOKEN_TYPE_HTML = "html";
    /** Constant <code>PLACEHOLDER</code> */
    public static final String PLACEHOLDER = "{$0}";


    /**
     * Compiled expression extracted from the label
     */
    protected Object expression;

    /**
     * List of fragments, used internally
     */
    protected List<String> fragments;

    /**
     * List of elements, used internally
     */
    protected List<String> elements;

    /**
     * <p>Constructor for DecorationTokenizer.</p>
     */
    public DecorationTokenizer() {
        super();
    }

    /**
     * <p>Constructor for DecorationTokenizer.</p>
     *
     * @param label a {@link java.lang.String} object.
     */
    public DecorationTokenizer(String label) {
        super(label, null);
    }

    /**
     * <p>Constructor for DecorationTokenizer.</p>
     *
     * @param label a {@link java.lang.String} object.
     * @param allowedTokenNames a {@link java.util.List} object.
     */
    public DecorationTokenizer(String label, List<String> allowedTokenNames) {
        super(label, allowedTokenNames);
    }

    /** {@inheritDoc} */
    public void tokenize(String label, List<String> allowedTokenNames) {
        this.label = "[" + RESERVED_TOKEN + "]" + label + "[/" + RESERVED_TOKEN + "]";
        this.tokenNames = new ArrayList<String>();
        this.allowedTokenNames = allowedTokenNames;
        tokenize();
        this.expression = parse();
    }

    /**
     * Tokenizes the expression
     */
    protected void tokenize() {
        String regex = Utils.join(Utils.buildList(
                RE_SHORT_TOKEN_START,
                RE_SHORT_TOKEN_END,
                RE_LONG_TOKEN_START,
                RE_LONG_TOKEN_END,
                RE_HTML_TOKEN_START,
                RE_HTML_TOKEN_END,
                RE_TEXT
                ).toArray(), "|"
        );

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(this.label);

        this.elements = new ArrayList<String>();

        while (matcher.find()) {
            this.elements.add(matcher.group());
        }

        // keep a copy for reference
        this.fragments = new ArrayList<String>(this.elements);
    }

    /**
     * <p>Getter for the field <code>expression</code>.</p>
     *
     * @return a {@link java.lang.Object} object.
     */
    protected Object getExpression() {
        return expression;
    }

    /**
     * <p>Setter for the field <code>expression</code>.</p>
     *
     * @param expression a {@link java.lang.Object} object.
     */
    protected void setExpression(Object expression) {
        this.expression = expression;
    }

    /**
     * <p>Getter for the field <code>fragments</code>.</p>
     *
     * @return a {@link java.util.List} object.
     */
    protected List<String> getFragments() {
        return fragments;
    }

    /**
     * <p>Setter for the field <code>fragments</code>.</p>
     *
     * @param fragments a {@link java.util.List} object.
     */
    protected void setFragments(List<String> fragments) {
        this.fragments = fragments;
    }

    /**
     * <p>Getter for the field <code>elements</code>.</p>
     *
     * @return a {@link java.util.List} object.
     */
    protected List<String> getElements() {
        return elements;
    }

    /**
     * <p>Setter for the field <code>elements</code>.</p>
     *
     * @param elements a {@link java.util.List} object.
     */
    protected void setElements(List<String> elements) {
        this.elements = elements;
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
     * <p>parse.</p>
     *
     * @return a {@link java.lang.Object} object.
     */
    protected Object parse() {
        String token = pop();

        if (isMatchingExpression(token, RE_SHORT_TOKEN_START)) {
            return parseTree(
                    token.trim().replaceAll("[\\[:]", ""),
                    TOKEN_TYPE_SHORT
            );
        }

        if (isMatchingExpression(token, RE_LONG_TOKEN_START)) {
            return parseTree(
                    token.trim().replaceAll("[\\[\\]]", ""),
                    TOKEN_TYPE_LONG
            );
        }

        if (isMatchingExpression(token, RE_HTML_TOKEN_START)) {
            if (token.indexOf("/>") != -1) return token;
            return parseTree(
                    token.trim().replaceAll("[<>]", ""),
                    TOKEN_TYPE_HTML
            );
        }

        return token;
    }

    /**
     * <p>parseTree.</p>
     *
     * @param name a {@link java.lang.String} object.
     * @param type a {@link java.lang.String} object.
     * @return a {@link java.lang.Object} object.
     */
    protected Object parseTree(String name, String type) {
        List<Object> tree = new ArrayList<Object>();
        tree.add(name);

        if (!tokenNames.contains(name) && !name.equals(RESERVED_TOKEN))
            tokenNames.add(name);

        if (type.equals(TOKEN_TYPE_SHORT)) {
            boolean first = true;
            while (peek() != null && !isMatchingExpression(peek(), RE_SHORT_TOKEN_END)) {
                Object value = parse();
                if (first && value instanceof String) {
                    value = ((String) value).replaceAll("^\\s+", "");
                    first = false;
                }
                tree.add(value);
            }

        } else if (type.equals(TOKEN_TYPE_LONG)) {
            while (peek() != null && !isMatchingExpression(peek(), RE_LONG_TOKEN_END)) {
                tree.add(parse());
            }
        } else if (type.equals(TOKEN_TYPE_HTML)) {
            while (peek() != null && !isMatchingExpression(peek(), RE_HTML_TOKEN_END)) {
                tree.add(parse());
            }
        }

        pop();
        return tree;
    }

    /**
     * <p>isTokenAllowed.</p>
     *
     * @param token a {@link java.lang.String} object.
     * @return a boolean.
     */
    protected boolean isTokenAllowed(String token) {
        return this.allowedTokenNames == null || allowedTokenNames.contains(token);
    }

    /**
     * <p>applyToken.</p>
     *
     * @param token a {@link java.lang.String} object.
     * @param value a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    protected String applyToken(String token, String value) {
        return value;
    }

    /**
     * <p>evaluate.</p>
     *
     * @param expr a {@link java.lang.Object} object.
     * @return a {@link java.lang.String} object.
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    protected String evaluate(Object expr) {
        if (!(expr instanceof List))
            return expr.toString();

        List<Object> args = new ArrayList<Object>((List) expr);
        String token = (String) args.remove(0);

        List<Object> processedValues = new ArrayList<Object>();
        for (Object arg : args) {
            processedValues.add(evaluate(arg));
        }

        String value = Utils.join(processedValues.toArray(), "");
        return applyToken(token, value);
    }

    /** {@inheritDoc} */
    public Object substitute(Map<String, Object> tokensData, Language language, Map<String, Object> options) {
        this.tokensData = tokensData;
        this.options = options;
        return evaluate(this.expression).replaceAll("\\[/tml\\]", "");
    }

    /** {@inheritDoc} */
    public static boolean isApplicable(String label) {
        return label != null && label.contains(TOKEN_BRACKET);
    }

}
