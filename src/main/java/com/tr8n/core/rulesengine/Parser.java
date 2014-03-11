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

package com.tr8n.core.rulesengine;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

public class Parser {

    private static final String REGEX_PARENS = "[()]";
    private static final String REGEX_CONSTANTS = "\\w+";
    private static final String REGEX_VARIABLES = "@\\w+";
    private static final String REGEX_OPERATORS = "[\\+\\-\\!\\|\\=>&<\\*\\/%]+";
    private static final String REGEX_STRINGS = "\\\".*?\\\"|'.*?'";

    private ArrayList<String> tokens;

    /**
     *
     */
    public Parser() {
        this.tokens = new ArrayList<String>();
    }

    /**
     * Default constructor
     * @param expression
     */
    public Parser(String expression) {
        tokenize(expression);
    }

    public void tokenize(String expression) {
        String regex = StringUtils.join(new String[] {REGEX_PARENS, REGEX_CONSTANTS, REGEX_VARIABLES, REGEX_OPERATORS, REGEX_STRINGS}, "|");
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(expression);

        this.tokens = new ArrayList<String>();
        while(m.find()){
            String match = m.group();
            if (match.trim().equals("")) continue;
            this.tokens.add(match);
        }
    }

    public ArrayList<String> getTokens() {
        return tokens;
    }

    private String peek() {
        if (tokens.size() == 0) return null;
        return tokens.get(0);
    }

    private String nextToken() {
        if (tokens.size() == 0) return null;
        return tokens.remove(0);
    }

    private boolean pregMatch(String regex, String subject) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(subject);
        return m.matches();
    }

    public Object parse(String expression) {
        tokenize(expression);
        return parse();
    }

    public Object parse() {
        String token = nextToken();
        if (token == null) return null;

        if (token.equals("(")) {
            return parseList();
        }

        if (token.startsWith("'") || token.startsWith("\"")) {
            return token.substring(1, token.length()-1);
        }

        if (pregMatch("\\d+", token)) {
            return new Long(token);
        }
        return token;
    }

    private List<Object> parseList() {
        List<Object> list = new ArrayList<Object>();
        while (peek() != null && !peek().equals(")")) {
            list.add(parse());
        }
        nextToken();
        return list;
    }

}