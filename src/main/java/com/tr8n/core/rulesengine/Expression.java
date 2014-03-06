package com.tr8n.core.rulesengine;

import java.util.List;
import java.util.regex.Pattern;

public abstract class Expression {

    public static boolean isNumeric(Object obj) {
        return obj.toString().matches("[+-]?\\d*(\\.\\d+)?");
    }

    public static boolean isInteger(Object obj) {
        return obj.toString().matches("[+-]?\\d*?");
    }

    public static Integer parseInt(Object obj) {
        return Integer.parseInt(obj.toString());
    }

    public static Double parseDouble(Object obj) {
        return Double.parseDouble(obj.toString());
    }

    public static Pattern parsePattern(String str) {
        String regex = str;
        if (regex.startsWith("/"))
            regex = regex.substring(1, regex.length());
        if (regex.endsWith("/i"))
            regex = regex.substring(0, regex.length() - 2);
        else if (regex.endsWith("/"))
            regex = regex.substring(0, regex.length() - 1);

        return Pattern.compile(regex);
    }

    public abstract Object evaluate(Evaluator evaluator, List params);
}
   
