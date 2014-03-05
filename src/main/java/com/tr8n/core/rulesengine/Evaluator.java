package com.tr8n.core.rulesengine;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class Evaluator {

    private Map<String, Expression> context;
    private Map<String, Object> variables;

    /**
     * Default constructor
     */
    public Evaluator() {
        this.context = defaultContext();
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

    public static Map<String, Expression> defaultContext() {
        Map<String, Expression> defaultContext = new HashMap<String, Expression>();

        /**
         * McCarthy's Elementary S-functions and Predicates
         */

        // ["label", "greeting", "hello world"] => greeting="hello world"
        defaultContext.put("label", new Expression() {
            public Object evaluate(Evaluator evaluator, List<Object> objects) {
                String name = (String) objects.get(0);
                Object value = (Object) objects.get(1);
                evaluator.setVariable(name, value);
                return value;
            }
        });

        // ["quote", [1,2,3]] => [1,2,3]
        defaultContext.put("quote", new Expression() {
            public Object evaluate(Evaluator evaluator, List<Object> objects) {
                return objects.get(0);
            }
        });

        // ["car", ["+", 1, 2]] => 1
        defaultContext.put("car", new Expression() {
            public Object evaluate(Evaluator evaluator, List<Object> objects) {
                @SuppressWarnings("unchecked")
                List<Object> value = (List<Object>) objects.get(0);
                return value.get(1);
            }
        });

        // ["cdr", ["+", 1, 2]] => [1, 2]
        defaultContext.put("cdr", new Expression() {
            public Object evaluate(Evaluator evaluator, List<Object> objects) {
                @SuppressWarnings("unchecked")
                List<Object> value = (List<Object>) objects.get(0);
                ArrayList<Object> list = new ArrayList<Object>(value);
                list.remove(0);
                return list;
            }
        });

        // ["cons", 1, ["quote", [2, 3]]] => [1, 2, 3]
        defaultContext.put("cons", new Expression() {
            public Object evaluate(Evaluator evaluator, List<Object> objects) {
                Object value1 = objects.get(0);
                @SuppressWarnings("unchecked")
                List<Object> value2 = (List<Object>) objects.get(1);
                ArrayList<Object> list = new ArrayList<Object>();
                list.add(value1);
                list.addAll(value2);
                return list;
            }
        });

        // ["eq", 1, 1] => true
        defaultContext.put("eq", new Expression() {
            public Object evaluate(Evaluator evaluator, List<Object> objects) {
                Object value1 = objects.get(0);
                Object value2 = objects.get(1);
                return value1.toString().equals(value2.toString());
            }
        });

        // ["atom", "hello"] => true
        // ["atom", ["eq", 2, 3]] => false
        defaultContext.put("atom", new Expression() {
            public Object evaluate(Evaluator evaluator, List<Object> objects) {
                Object value1 = objects.get(0);
                return new Boolean(!(value1 instanceof ArrayList));
            }
        });

        // ["cond", ["eq", 1, 1], "yes", "no"] => "yes"
        defaultContext.put("cond", new Expression() {
            public Object evaluate(Evaluator evaluator, List<Object> objects) {
                Boolean result = (Boolean) evaluator.evaluate(objects.get(0));
                return (result.booleanValue() ? objects.get(1) : objects.get(2));
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
            public Object evaluate(Evaluator evaluator, List<Object> objects) {
                return evaluator.getExpression("label").evaluate(evaluator, objects);
            }
        });

        /**
         * Boolean expressions
         */

        // ["=", 1, 1] => true
        defaultContext.put("=", new Expression() {
            public Object evaluate(Evaluator evaluator, List<Object> objects) {
                return evaluator.getExpression("eq").evaluate(evaluator, objects);
            }
        });

        // ["!=", "2", "1"] => true
        defaultContext.put("!=", new Expression() {
            public Object evaluate(Evaluator evaluator, List<Object> objects) {
                Boolean result = (Boolean) evaluator.getExpression("eq").evaluate(evaluator, objects);
                return new Boolean(!result.booleanValue());
            }
        });

        // ["<", "2", "1"] => false
        defaultContext.put("<", new Expression() {
            public Object evaluate(Evaluator evaluator, List<Object> objects) {
                return new Boolean(Double.parseDouble(objects.get(0).toString()) < Double.parseDouble(objects.get(1).toString()));
            }
        });

        // ["<=", "2", "5"] => true
        defaultContext.put("<=", new Expression() {
            public Object evaluate(Evaluator evaluator, List<Object> objects) {
                Boolean result1 = (Boolean) evaluator.getExpression("<").evaluate(evaluator, objects);
                Boolean result2 = (Boolean) evaluator.getExpression("=").evaluate(evaluator, objects);
                return new Boolean(result1.booleanValue() || result2.booleanValue());
            }
        });

        // [">", "2", "5"] => false
        defaultContext.put(">", new Expression() {
            public Object evaluate(Evaluator evaluator, List<Object> objects) {
                return new Boolean(Double.parseDouble(objects.get(0).toString()) > Double.parseDouble(objects.get(1).toString()));
            }
        });

        // [">=", "5", "5"] => false
        defaultContext.put(">=", new Expression() {
            public Object evaluate(Evaluator evaluator, List<Object> objects) {
                Boolean result1 = (Boolean) evaluator.getExpression(">").evaluate(evaluator, objects);
                Boolean result2 = (Boolean) evaluator.getExpression("=").evaluate(evaluator, objects);
                return new Boolean(result1.booleanValue() || result2.booleanValue());
            }
        });

        // ["true"] => true
        defaultContext.put("true", new Expression() {
            public Object evaluate(Evaluator evaluator, List<Object> objects) {
                return new Boolean(true);
            }
        });

        // ["false"] => false
        defaultContext.put("false", new Expression() {
            public Object evaluate(Evaluator evaluator, List<Object> objects) {
                return new Boolean(false);
            }
        });

        // ["!", ["=", "1", "2"]] => true
        defaultContext.put("!", new Expression() {
            public Object evaluate(Evaluator evaluator, List<Object> objects) {
                Boolean value = (Boolean) objects.get(0);
                return new Boolean(!value.booleanValue());
            }
        });

        // ["not", ["eq", "1", "2"]] => true
        defaultContext.put("not", new Expression() {
            public Object evaluate(Evaluator evaluator, List<Object> objects) {
                return evaluator.getExpression("!").evaluate(evaluator, objects);
            }
        });

        // ["&&", ["=", "1", "1"], ["=", 10, ["/", 20, 2]]] => true
        defaultContext.put("&&", new Expression() {
            public Object evaluate(Evaluator evaluator, List<Object> objects) {
                Iterator<Object> iter = objects.iterator();
                while (iter.hasNext()) {
                    Boolean result = (Boolean) evaluator.evaluate(iter.next());
                    if (!result.booleanValue())
                        return new Boolean(false);
                }
                return new Boolean(true);
            }
        });

        // ["and", ["=", "1", "1"], ["=", 10, ["/", 20, 2]]] => true
        defaultContext.put("and", new Expression() {
            public Object evaluate(Evaluator evaluator, List<Object> objects) {
                return evaluator.getExpression("&&").evaluate(evaluator, objects);
            }
        });

        // ["||", ["=", "2", "1"], ["=", 10, ["/", 20, 2]]] => true
        defaultContext.put("||", new Expression() {
            public Object evaluate(Evaluator evaluator, List<Object> objects) {
                Iterator<Object> iter = objects.iterator();
                while (iter.hasNext()) {
                    Boolean result = (Boolean) evaluator.evaluate(iter.next());
                    if (result.booleanValue())
                        return new Boolean(true);
                }
                return new Boolean(false);
            }
        });

        // ["or", ["=", "2", "1"], ["=", 10, ["/", 20, 2]]] => true
        defaultContext.put("or", new Expression() {
            public Object evaluate(Evaluator evaluator, List<Object> objects) {
                return evaluator.getExpression("||").evaluate(evaluator, objects);
            }
        });

        // ["if", ["=", 1, 2], 1, 0] => 0
        defaultContext.put("if", new Expression() {
            public Object evaluate(Evaluator evaluator, List<Object> objects) {
                return evaluator.getExpression("cond").evaluate(evaluator, objects);
            }
        });

        /**
         * Arithmetics
         */

        // ["+", 1, 2] => 3
        defaultContext.put("+", new Expression() {
            public Object evaluate(Evaluator evaluator, List<Object> objects) {
                return new Double(Double.parseDouble(objects.get(0).toString()) + Double.parseDouble(objects.get(1).toString()));
            }
        });

        // ["-", 3, 2] => 1
        defaultContext.put("-", new Expression() {
            public Object evaluate(Evaluator evaluator, List<Object> objects) {
                return new Double(Double.parseDouble(objects.get(0).toString()) - Double.parseDouble(objects.get(1).toString()));
            }
        });

        // ["*", 3, 2] => 6
        defaultContext.put("*", new Expression() {
            public Object evaluate(Evaluator evaluator, List<Object> objects) {
                return new Double(Double.parseDouble(objects.get(0).toString()) * Double.parseDouble(objects.get(1).toString()));
            }
        });

        // ["/", 6, 2] => 3
        defaultContext.put("/", new Expression() {
            public Object evaluate(Evaluator evaluator, List<Object> objects) {
                return new Double(Double.parseDouble(objects.get(0).toString()) / Double.parseDouble(objects.get(1).toString()));
            }
        });

        // ["/", 6, 5] => 1
        defaultContext.put("%", new Expression() {
            public Object evaluate(Evaluator evaluator, List<Object> objects) {
                return new Integer(Integer.parseInt(objects.get(0).toString()) % Integer.parseInt(objects.get(1).toString()));
            }
        });

        // ["mod", 6, 5] => 1
        defaultContext.put("mod", new Expression() {
            public Object evaluate(Evaluator evaluator, List<Object> objects) {
                return evaluator.getExpression("%").evaluate(evaluator, objects);
            }
        });

        /**
         * Date/Time functions
         */

        // ["date", "2011-01-01"] => new Date(...)
        defaultContext.put("date", new Expression() {
            public Object evaluate(Evaluator evaluator, List<Object> objects) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    return sdf.parse((String) objects.get(0));
                } catch (ParseException ex){
                    return null;
                }
            }
        });

        // ["date", "2011-01-01 10:9:8"] => new Date(...)
        defaultContext.put("time", new Expression() {
            public Object evaluate(Evaluator evaluator, List<Object> objects) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    return sdf.parse((String) objects.get(0));
                } catch (ParseException ex){
                    return null;
                }
            }
        });

        // ["now"] => new Date()
        defaultContext.put("now", new Expression() {
            public Object evaluate(Evaluator evaluator, List<Object> objects) {
                return new Date();
            }
        });

        /**
         * String Manipulations
         */

        // ["append", "world", "hello "] => "hello world"
        defaultContext.put("append", new Expression() {
            public Object evaluate(Evaluator evaluator, List<Object> objects) {
                return (objects.get(1).toString() + objects.get(0).toString());
            }
        });

        // ["prepend", "hello ", "world"] => "hello world"
        defaultContext.put("prepend", new Expression() {
            public Object evaluate(Evaluator evaluator, List<Object> objects) {
                return (objects.get(0).toString() + objects.get(1).toString());
            }
        });

        /**
         * Regular Expressions
         */

        defaultContext.put("match", new Expression() {
            public Object evaluate(Evaluator evaluator, List<Object> objects) {
                // TODO: finish implementation
                return null;
            }
        });

        return defaultContext;
    }

    public boolean isNestedFunction(String fn) {
        List<String> nested = Arrays.asList("quote", "car", "cdr", "cond", "if", "&&", "||",
                "and", "or", "true", "false", "let", "count", "all", "any");
        return (nested.indexOf(fn) != -1);
    }

    public Object applyFunction(String name, List<Object> args) {
        Object var = this.getVariable(name);
        if (var != null) return var;

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

        if (!(expr instanceof ArrayList)) {
            return expr;
        }

        @SuppressWarnings("unchecked")
        List<Object> expression = (List<Object>) expr;
        List<Object> args = new ArrayList<Object>(expression);
        String fn = (String) args.get(0);
        args.remove(0);

        if (!this.isNestedFunction(fn)) {
            List<Object> results = new ArrayList<Object>();
            Iterator<Object> iter = args.iterator();
            while (iter.hasNext()) {
                results.add(this.evaluate(iter.next()));
            }
            args = results;
        }

        return this.applyFunction(fn, args);
    }

}
