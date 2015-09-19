
/**
 * Copyright (c) 2015 Translation Exchange, Inc. All rights reserved.
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
 *
 * @author Michael Berkovich, michael@translationexchange.com
 * @version $Id: $Id
 */

package com.translationexchange.core.rulesengine;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.translationexchange.core.Utils;
public class Parser {

    private static final String OPENING_PAREN = "(";
    private static final String CLOSING_PAREN = ")";
    private static final String SINGLE_QUOTE = "'";
    private static final String DOUBLE_QUOTE = "\"";
    
    private static final String REGEX_JOINER = "|";
    private static final String REGEX_PARENS = "[" + OPENING_PAREN + CLOSING_PAREN + "]";
    private static final String REGEX_CONSTANTS = "\\w+";
    private static final String REGEX_VARIABLES = "@\\w+";
    private static final String REGEX_OPERATORS = "[\\+\\-\\!\\|\\=>&<\\*\\/%]+";
    private static final String REGEX_STRINGS = "\\\".*?\\\"|'.*?'";

    private ArrayList<String> tokens;

    /**
     * Default constructor
     */
    public Parser() {
        this.tokens = new ArrayList<String>();
    }

    /**
     * Default constructor
     *
     * @param expression a {@link java.lang.String} object.
     */
    public Parser(String expression) {
        tokenize(expression);
    }

    /**
     * Tokenizes an expression
     * (+ 1 1) becomes ["(", "1", "1" + ")"]
     *
     * @param expression a {@link java.lang.String} object.
     */
    public void tokenize(String expression) {
        String regex = Utils.join(new String[] {REGEX_PARENS, REGEX_CONSTANTS, REGEX_VARIABLES, REGEX_OPERATORS, REGEX_STRINGS}, REGEX_JOINER);
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(expression);

        this.tokens = new ArrayList<String>();
        while(m.find()){
            String match = m.group();
            if (match.trim().equals("")) continue;
            this.tokens.add(match);
        }
    }

    /**
     * Returns a list of expression tokens
     *
     * @return a {@link java.util.ArrayList} object.
     */
    public ArrayList<String> getTokens() {
        return tokens;
    }

    /**
     * Looks up the token at the top of the stack
     * 
     * @return
     */
    private String peek() {
        if (tokens.size() == 0) return null;
        return tokens.get(0);
    }

    /**
     * Fetches the token from the top of the stack
     * 
     * @return
     */
    private String nextToken() {
        if (tokens.size() == 0) return null;
        return tokens.remove(0);
    }

    /**
     * Matches regular expression
     * 
     * @param regex
     * @param subject
     * @return
     */
    private boolean pregMatch(String regex, String subject) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(subject);
        return m.matches();
    }

    /**
     * Parses the expression into prefix form
     * (+ 1 1) becomes ["+", 1, 1]
     *
     * @param expression a {@link java.lang.String} object.
     * @return a {@link java.lang.Object} object.
     */
    public Object parse(String expression) {
        tokenize(expression);
        return parse();
    }

    /**
     * Parses the expression
     *
     * @return a {@link java.lang.Object} object.
     */
    public Object parse() {
        String token = nextToken();
        if (token == null) return null;

        if (token.equals(OPENING_PAREN)) {
            return parseList();
        }

        if (token.startsWith(SINGLE_QUOTE) || token.startsWith(DOUBLE_QUOTE)) {
            return token.substring(1, token.length()-1);
        }

        if (pregMatch("\\d+", token)) {
            return new Long(token);
        }
        return token;
    }

    /**
     * Parses a list
     * 
     * @return
     */
    private List<Object> parseList() {
        List<Object> list = new ArrayList<Object>();
        while (peek() != null && !peek().equals(CLOSING_PAREN)) {
            list.add(parse());
        }
        nextToken();
        return list;
    }

}
