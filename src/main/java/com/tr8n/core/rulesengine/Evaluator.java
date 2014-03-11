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

import com.google.common.collect.Range;
import com.tr8n.core.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Evaluator {

    private Map<String, Expression> context;
    private Map<String, Object> variables;
    private List<String> nestedFunctions;

    /**
     * Default constructor
     */
    public Evaluator() {
        this.context = defaultContext();
        this.nestedFunctions = new ArrayList<String>();
        this.nestedFunctions.addAll(defaultContextNestedFunctions());
        this.variables = new HashMap<String, Object>();
    }

    /**
     * Constructs the evaluator with default variables
     * @param defaultVariables
     */
    public Evaluator(Map<String, Object> defaultVariables) {
        this();
        this.variables = defaultVariables;
    }

    /**
     * Constructs the evaluator with default variables and context extension expressions
     *
     * @param defaultVariables
     * @param contextExtensions
     */
    public Evaluator(Map<String, Object> defaultVariables, Map<String, Expression> contextExtensions) {
        this(defaultVariables);
        this.context.putAll(contextExtensions);
    }

    public Object getVariable(String name) {
        return this.variables.get(name);
    }

    public void setVariable(String name, Object value) {
        this.variables.put(name, value);
    }

    public static List<String> defaultContextNestedFunctions() {
        return Arrays.asList("quote", "car", "cdr", "cond", "if", "&&", "||",
            "and", "or", "true", "false", "let", "count", "all", "any");
    }

    public static Map<String, Expression> defaultContext() {
        Map<String, Expression> defaultContext = new HashMap<String, Expression>();

        /**
         * McCarthy's Elementary S-functions and Predicates
         */

        // ["label", "greeting", "hello world"] => greeting="hello world"
        defaultContext.put("label", new Expression() {
            public Object evaluate(Evaluator evaluator, List params) {
                String name = (String) params.get(0);
                Object value = params.get(1);
                evaluator.setVariable(name, value);
                return value;
            }
        });

        // ["quote", [1,2,3]] => [1,2,3]
        defaultContext.put("quote", new Expression() {
            public Object evaluate(Evaluator evaluator, List params) {
                return params.get(0);
            }
        });

        // ["car", ["+", 1, 2]] => 1
        defaultContext.put("car", new Expression() {
            public Object evaluate(Evaluator evaluator, List params) {
                @SuppressWarnings("unchecked")
                List value = (List) params.get(0);
                return value.get(1);
            }
        });

        // ["cdr", ["+", 1, 2]] => [1, 2]
        defaultContext.put("cdr", new Expression() {
            public Object evaluate(Evaluator evaluator, List params) {
                @SuppressWarnings("unchecked")
                List value = (List) params.get(0);
                List list = new ArrayList<Object>(value);
                list.remove(0);
                return list;
            }
        });

        // ["cons", 1, ["quote", [2, 3]]] => [1, 2, 3]
        defaultContext.put("cons", new Expression() {
            public Object evaluate(Evaluator evaluator, List params) {
                @SuppressWarnings("unchecked")
                List value2 = (List) params.get(1);
                List list = new ArrayList();
                list.add(params.get(0));
                list.addAll(value2);
                return list;
            }
        });

        // ["eq", 1, 1] => true
        defaultContext.put("eq", new Expression() {
            public Object evaluate(Evaluator evaluator, List params) {
                Object value1 = params.get(0);
                Object value2 = params.get(1);
                return value1.toString().equals(value2.toString());
            }
        });

        // ["atom", "hello"] => true
        // ["atom", ["eq", 2, 3]] => false
        defaultContext.put("atom", new Expression() {
            public Object evaluate(Evaluator evaluator, List params) {
                Object value1 = params.get(0);
                return !(value1 instanceof ArrayList);
            }
        });

        // ["cond", ["eq", 1, 1], "yes", "no"] => "yes"
        defaultContext.put("cond", new Expression() {
            public Object evaluate(Evaluator evaluator, List params) {
                Boolean result = (Boolean) evaluator.evaluate(params.get(0));
                return (result ? evaluator.evaluate(params.get(1)) : evaluator.evaluate(params.get(2)));
            }
        });

        /**
         * Tr8n Extensions
         */

        /**
         * Assignments
         */

        // ["let", "@n", 11] => @n = 11 => 11
        defaultContext.put("let", new Expression() {
            public Object evaluate(Evaluator evaluator, List params) {
                return evaluator.getExpression("label").evaluate(evaluator, params);
            }
        });

        /**
         * Boolean expressions
         */

        // ["=", 1, 1] => true
        defaultContext.put("=", new Expression() {
            public Object evaluate(Evaluator evaluator, List params) {
                return evaluator.getExpression("eq").evaluate(evaluator, params);
            }
        });

        // ["!=", "2", "1"] => true
        defaultContext.put("!=", new Expression() {
            public Object evaluate(Evaluator evaluator, List params) {
                Boolean result = (Boolean) evaluator.getExpression("eq").evaluate(evaluator, params);
                return !result.booleanValue();
            }
        });

        // ["<", "2", "1"] => false
        defaultContext.put("<", new Expression() {
            public Object evaluate(Evaluator evaluator, List params) {
                Object v1 = params.get(0);
                Object v2 = params.get(1);

                if (v1 instanceof Date && v2 instanceof Date)
                    return ((Date) v1).before((Date)v2);

                if (Utils.isNumeric(v1) && Utils.isNumeric(v2))
                    return Double.parseDouble(v1.toString()) < Double.parseDouble(v2.toString());

                return v1.toString().length() < v2.toString().length();
            }
        });

        // ["<=", "2", "5"] => true
        defaultContext.put("<=", new Expression() {
            public Object evaluate(Evaluator evaluator, List params) {
                Boolean result1 = (Boolean) evaluator.getExpression("=").evaluate(evaluator, params);
                return result1 || (Boolean) evaluator.getExpression("<").evaluate(evaluator, params);
            }
        });

        // [">", "2", "5"] => false
        defaultContext.put(">", new Expression() {
            public Object evaluate(Evaluator evaluator, List params) {
                return !(Boolean) evaluator.getExpression("<=").evaluate(evaluator, params);
            }
        });

        // [">=", "5", "5"] => false
        defaultContext.put(">=", new Expression() {
            public Object evaluate(Evaluator evaluator, List params) {
                Boolean result1 = (Boolean) evaluator.getExpression("=").evaluate(evaluator, params);
                return result1 || (Boolean) evaluator.getExpression(">").evaluate(evaluator, params);
            }
        });

        // ["true"] => true
        defaultContext.put("true", new Expression() {
            public Object evaluate(Evaluator evaluator, List params) {
                return true;
            }
        });

        // ["false"] => false
        defaultContext.put("false", new Expression() {
            public Object evaluate(Evaluator evaluator, List params) {
                return false;
            }
        });

        // ["!", ["=", "1", "2"]] => true
        defaultContext.put("!", new Expression() {
            public Object evaluate(Evaluator evaluator, List params) {
                return !(Boolean) params.get(0);
            }
        });

        // ["not", ["eq", "1", "2"]] => true
        defaultContext.put("not", new Expression() {
            public Object evaluate(Evaluator evaluator, List params) {
                return evaluator.getExpression("!").evaluate(evaluator, params);
            }
        });

        // ["&&", ["=", "1", "1"], ["=", 10, ["/", 20, 2]]] => true
        defaultContext.put("&&", new Expression() {
            public Object evaluate(Evaluator evaluator, List params) {
                for(Object value : params) {
                    Boolean result = (Boolean) evaluator.evaluate(value);
                    if (!result) return false;
                }
                return true;
            }
        });

        // ["and", ["=", "1", "1"], ["=", 10, ["/", 20, 2]]] => true
        defaultContext.put("and", new Expression() {
            public Object evaluate(Evaluator evaluator, List params) {
                return evaluator.getExpression("&&").evaluate(evaluator, params);
            }
        });

        // ["||", ["=", "2", "1"], ["=", 10, ["/", 20, 2]]] => true
        defaultContext.put("||", new Expression() {
            public Object evaluate(Evaluator evaluator, List params) {
                for(Object value : params) {
                    Boolean result = (Boolean) evaluator.evaluate(value);
                    if (result) return true;
                }
                return false;
            }
        });

        // ["or", ["=", "2", "1"], ["=", 10, ["/", 20, 2]]] => true
        defaultContext.put("or", new Expression() {
            public Object evaluate(Evaluator evaluator, List params) {
                return evaluator.getExpression("||").evaluate(evaluator, params);
            }
        });

        // ["if", ["=", 1, 2], 1, 0] => 0
        defaultContext.put("if", new Expression() {
            public Object evaluate(Evaluator evaluator, List params) {
                return evaluator.getExpression("cond").evaluate(evaluator, params);
            }
        });

        /**
         * Arithmetics
         */

        // ["+", 1, 2] => 3
        defaultContext.put("+", new Expression() {
            public Object evaluate(Evaluator evaluator, List params) {
                Object v1 = params.get(0);
                Object v2 = params.get(1);

                if (Utils.isNumeric(v1) && Utils.isNumeric(v2)) {
                    if (v1 instanceof Double || v2 instanceof Double)
                        return Utils.parseDouble(v1) + Utils.parseDouble(v2);
                    return Utils.parseInt(v1) + Utils.parseInt(v2);
                }

                return params.get(0).toString() + params.get(1).toString();
            }
        });

        // ["-", 3, 2] => 1
        defaultContext.put("-", new Expression() {
            public Object evaluate(Evaluator evaluator, List params) {
                Object v1 = params.get(0);
                Object v2 = params.get(1);

                if (Utils.isNumeric(v1) && Utils.isNumeric(v2)) {
                    if (v1 instanceof Double || v2 instanceof Double)
                        return Utils.parseDouble(v1) - Utils.parseDouble(v2);
                    return Utils.parseInt(v1) - Utils.parseInt(v2);
                }

                return null;
            }
        });

        // ["*", 3, 2] => 6
        defaultContext.put("*", new Expression() {
            public Object evaluate(Evaluator evaluator, List params) {
                Object v1 = params.get(0);
                Object v2 = params.get(1);

                if (Utils.isNumeric(v1) && Utils.isNumeric(v2)) {
                    if (v1 instanceof Double || v2 instanceof Double)
                        return Utils.parseDouble(v1) * Utils.parseDouble(v2);
                    return Utils.parseInt(v1) * Utils.parseInt(v2);
                }

                return null;
            }
        });

        // ["/", 6, 2] => 3
        defaultContext.put("/", new Expression() {
            public Object evaluate(Evaluator evaluator, List params) {
                Object v1 = params.get(0);
                Object v2 = params.get(1);

                if (Utils.isNumeric(v1) && Utils.isNumeric(v2)) {
                    if (v1 instanceof Double || v2 instanceof Double)
                        return Utils.parseDouble(v1) / Utils.parseDouble(v2);
                    return Utils.parseInt(v1) / Utils.parseInt(v2);
                }

                return null;
            }
        });

        // ["%", 6, 5] => 1
        defaultContext.put("%", new Expression() {
            public Object evaluate(Evaluator evaluator, List params) {
                Object v1 = params.get(0);
                Object v2 = params.get(1);

                if (Utils.isInteger(v1) && Utils.isInteger(v2)) {
                    return Utils.parseInt(v1) % Utils.parseInt(v2);
                }

                return null;
            }
        });

        // ["mod", 6, 5] => 1
        defaultContext.put("mod", new Expression() {
            public Object evaluate(Evaluator evaluator, List params) {
                return evaluator.getExpression("%").evaluate(evaluator, params);
            }
        });


        /**
         * Date/Time functions
         */

        // ["date", "2011-01-01"] => new Date(...)
        defaultContext.put("date", new Expression() {
            public Object evaluate(Evaluator evaluator, List params) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    return sdf.parse((String) params.get(0));
                } catch (ParseException ex){
                    return null;
                }
            }
        });

        // ["date", "2011-01-01 10:9:8"] => new Date(...)
        defaultContext.put("time", new Expression() {
            public Object evaluate(Evaluator evaluator, List params) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    return sdf.parse((String) params.get(0));
                } catch (ParseException ex){
                    return null;
                }
            }
        });

        // ["now"] => new Date()
        defaultContext.put("now", new Expression() {
            public Object evaluate(Evaluator evaluator, List params) {
                return new Date();
            }
        });

        /**
         * String Manipulations
         */

        // ["append", "world", "hello "] => "hello world"
        defaultContext.put("append", new Expression() {
            public Object evaluate(Evaluator evaluator, List params) {
                return (params.get(1).toString() + params.get(0).toString());
            }
        });

        // ["prepend", "hello ", "world"] => "hello world"
        defaultContext.put("prepend", new Expression() {
            public Object evaluate(Evaluator evaluator, List params) {
                return (params.get(0).toString() + params.get(1).toString());
            }
        });

        /**
         * Regular Expressions
         */

        // ['match', '/a/', 'abc'] => true
        defaultContext.put("match", new Expression() {
            public Object evaluate(Evaluator evaluator, List params) {
                Pattern p = Utils.parsePattern((String) params.get(0));
                Matcher m = p.matcher((String) params.get(1));
                return m.find();
            }
        });

        // ['replace', '/(matr|vert|ind)ix|ex$/i', '$1ices', 'vertex'] => vertices
        defaultContext.put("replace", new Expression() {
            public Object evaluate(Evaluator evaluator, List params) {
                Pattern p = Utils.parsePattern((String) params.get(0));
                Matcher m = p.matcher((String) params.get(2));
                return m.replaceAll((String) params.get(1));
            }
        });


        // ['in', '1,2,3,5..10,20..24', '@n']
        defaultContext.put("in", new Expression() {
            public Object evaluate(Evaluator evaluator, List params) {
                String values = ("" + params.get(0)).trim();
                String search = ("" + params.get(1)).trim();
                String[] vals = values.split(",");
                for (String value : vals) {
                    value = value.trim();

                    if (value.contains("..")) {
                        String[] bounds = value.split("\\.\\.");
                        Range range = Range.closed(Integer.parseInt(bounds[0].trim()), Integer.parseInt(bounds[1].trim()));
                        if (range.contains(Integer.parseInt(search)))
                            return true;
                    } else if (value.equals(search.toString())) {
                        return true;
                    }
                }

                return false;
            }
        });

        // ['within', '0..3', '@n']
        defaultContext.put("within", new Expression() {
            public Object evaluate(Evaluator evaluator, List params) {
                String[] bounds = ((String) params.get(0)).trim().split("\\.\\.");
                String search = ((String) params.get(1)).trim();
                Double left = Double.parseDouble(bounds[0].trim());
                Double right = Double.parseDouble(bounds[1].trim());
                Double value = Double.parseDouble(search);
                return left <= value && value <= right;
            }
        });

        // ['count', '@genders']
        defaultContext.put("count", new Expression() {
            public Object evaluate(Evaluator evaluator, List params) {
                List list;
                if (params.get(0) instanceof String) {
                    list = (List) evaluator.getVariable((String)params.get(0));
                } else {
                    list = (List) params.get(0);
                }
                return list.size();
            }
        });

        // ['all', '@genders', 'male']
        defaultContext.put("all", new Expression() {
            public Object evaluate(Evaluator evaluator, List params) {
                List list;
                if (params.get(0) instanceof String) {
                    list = (List) evaluator.getVariable((String)params.get(0));
                } else {
                    list = (List) params.get(0);
                }

                if (list.size() == 0)
                    return false;

                for (Object object : list) {
                    if (!object.equals(params.get(1)))
                        return false;
                }
                return true;
            }
        });

        // ['any', '@genders', 'female']
        defaultContext.put("any", new Expression() {
            public Object evaluate(Evaluator evaluator, List params) {
                List list;
                if (params.get(0) instanceof String) {
                    list = (List) evaluator.getVariable((String)params.get(0));
                } else {
                    list = (List) params.get(0);
                }

                if (list.size() == 0)
                    return false;

                for (Object object : list) {
                    if (object.equals(params.get(1)))
                        return true;
                }
                return false;
            }
        });

        return defaultContext;
    }

    public void addNestedFunction(String fn) {
        this.nestedFunctions.add(fn);
    }

    public void removeNestedFunction(String fn) {
        this.nestedFunctions.remove(fn);
    }

    public boolean isNestedFunction(String fn) {
        return (this.nestedFunctions.indexOf(fn) != -1);
    }

    private Object applyFunction(String name, List args) {
        Expression expr = (Expression) this.context.get(name);
        return expr.evaluate(this, args);
    }

    public Expression getExpression(String name) {
        return context.get(name);
    }

    public void reset() {
        this.variables = new HashMap<String, Object>();
    }

    public Object evaluate(Object expr) {
        if (expr instanceof String) {
            String name = (String) expr;
            Object var = this.getVariable(name);
            if (var != null) return var;
            return expr;
        }

        if (!(expr instanceof List)) {
            return expr;
        }

        @SuppressWarnings("unchecked")
        List args = new ArrayList((List) expr);
        String fn = (String) args.get(0);
        args.remove(0);

        if (!this.isNestedFunction(fn)) {
            List results = new ArrayList();
            Iterator iterator = args.iterator();
            while (iterator.hasNext()) {
                results.add(this.evaluate(iterator.next()));
            }
            args = results;
        }

        return this.applyFunction(fn, args);
    }

}
