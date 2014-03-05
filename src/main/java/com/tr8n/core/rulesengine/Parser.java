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

    public static void main(String[] args) {
        Parser parser = new Parser("(mod @n 10)");
        System.out.println(StringUtils.join(parser.getTokens().toArray(), ", "));
        System.out.println(StringUtils.join(((List<Object>)parser.parse()).toArray(), ", "));
    }

}