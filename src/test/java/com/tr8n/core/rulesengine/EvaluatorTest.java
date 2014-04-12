package com.tr8n.core.rulesengine;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.tr8n.core.BaseTest;
import com.tr8n.core.Utils;

public class EvaluatorTest extends BaseTest {

    @Test
    public void testEvaluatingStandardExpressions() {
        Evaluator e = new Evaluator();

        org.junit.Assert.assertEquals(
                "Michael",
                e.getExpression("label").evaluate(e, Utils.buildList("name", "Michael"))
        );
        org.junit.Assert.assertEquals(
                "Michael",
                e.getVariable("name")
        );

        List<Object> list = Utils.buildList("one", "two");
        org.junit.Assert.assertEquals(
                list,
                e.getExpression("quote").evaluate(e, Utils.buildList(list))
        );

        list = Utils.buildList("+", "one", "two");
        org.junit.Assert.assertEquals(
                "one",
                e.getExpression("car").evaluate(e, Utils.buildList(list))
        );

        org.junit.Assert.assertEquals(
                e.getExpression("cdr").evaluate(e,Utils.buildList(list)),
                Utils.buildList("one", "two")
        );

        // ["cons", 1, ["quote", [2, 3]]] => [1, 2, 3]
        org.junit.Assert.assertEquals(
                Utils.buildList("1", "2", "3"),
                e.getExpression("cons").evaluate(e, Utils.buildList("1", Utils.buildList("2", "3")))
        );

        org.junit.Assert.assertTrue(
                (Boolean) e.getExpression("eq").evaluate(e, Utils.buildList("1", "1"))
        );

        org.junit.Assert.assertTrue(
                (Boolean) e.getExpression("eq").evaluate(e, Utils.buildList(1, 1))
        );

        org.junit.Assert.assertFalse(
                (Boolean) e.getExpression("eq").evaluate(e, Utils.buildList(1, 2))
        );

        org.junit.Assert.assertTrue(
                (Boolean) e.getExpression("eq").evaluate(e, Utils.buildList(parseDate("2010-01-01"), parseDate("2010-01-01")))
        );

        org.junit.Assert.assertFalse(
                (Boolean) e.getExpression("eq").evaluate(e, Utils.buildList(parseDate("2010-02-01"), parseDate("2010-01-02")))
        );

        org.junit.Assert.assertTrue((Boolean) e.getExpression("atom").evaluate(e, Utils.buildList(1)));
        org.junit.Assert.assertTrue((Boolean) e.getExpression("atom").evaluate(e, Utils.buildList(true)));
        org.junit.Assert.assertTrue((Boolean) e.getExpression("atom").evaluate(e, Utils.buildList(1.4)));
        org.junit.Assert.assertTrue((Boolean) e.getExpression("atom").evaluate(e, Utils.buildList(new Date())));
        org.junit.Assert.assertFalse((Boolean) e.getExpression("atom").evaluate(e, Utils.buildList(new ArrayList())));

        org.junit.Assert.assertEquals(
            "yes",
            e.getExpression("cond").evaluate(e, Utils.buildList(true, "yes", "no"))
        );

        org.junit.Assert.assertEquals(
            "no",
            e.getExpression("cond").evaluate(e, Utils.buildList(false, "yes", "no"))
        );


        org.junit.Assert.assertEquals(
            "no",
            e.getExpression("cond").evaluate(e, Utils.buildList(false, "yes", "no"))
        );


        org.junit.Assert.assertEquals(
            3,
            e.getExpression("+").evaluate(e, Utils.buildList(1, 2))
        );

    }

    @Test
    public void testEvaluatingAssignmentExpressions() {
        Evaluator e = new Evaluator();
        org.junit.Assert.assertEquals(
            "Michael",
            e.getExpression("let").evaluate(e, Utils.buildList("@name", "Michael"))
        );
        org.junit.Assert.assertEquals(
            "Michael",
            e.getVariable("@name")
        );

    }

    @Test
    public void testEvaluatingBooleanExpressions() {
        Evaluator e = new Evaluator();
        org.junit.Assert.assertTrue(
                (Boolean) e.getExpression("=").evaluate(e, Utils.buildList("a", "a"))
        );

        org.junit.Assert.assertTrue(
                (Boolean) e.getExpression("!=").evaluate(e, Utils.buildList(1, 2))
        );

        org.junit.Assert.assertFalse(
                (Boolean) e.getExpression("!=").evaluate(e, Utils.buildList(1, 1))
        );

        org.junit.Assert.assertTrue(
                (Boolean) e.getExpression("<").evaluate(e, Utils.buildList("ab", "abc"))
        );

        org.junit.Assert.assertTrue(
                (Boolean) e.getExpression("<").evaluate(e, Utils.buildList(parseDate("2010-01-01"), parseDate("2011-01-01")))
        );

        org.junit.Assert.assertTrue(
                (Boolean) e.getExpression("<").evaluate(e, Utils.buildList(1, 2))
        );

        org.junit.Assert.assertFalse(
                (Boolean) e.getExpression("<").evaluate(e, Utils.buildList(2, 2))
        );

        org.junit.Assert.assertFalse(
                (Boolean) e.getExpression("<").evaluate(e, Utils.buildList(5, 2))
        );

        org.junit.Assert.assertTrue(
                (Boolean) e.getExpression("<=").evaluate(e, Utils.buildList(2, 2))
        );

        org.junit.Assert.assertFalse(
                (Boolean) e.getExpression("<=").evaluate(e, Utils.buildList(4, 2))
        );

        org.junit.Assert.assertTrue(
                (Boolean) e.getExpression(">").evaluate(e, Utils.buildList(3, 2))
        );

        org.junit.Assert.assertTrue(
                (Boolean) e.getExpression(">=").evaluate(e, Utils.buildList(2, 2))
        );

        org.junit.Assert.assertFalse(
                (Boolean) e.getExpression(">=").evaluate(e, Utils.buildList(4, 12))
        );

        org.junit.Assert.assertTrue(
                (Boolean) e.getExpression("true").evaluate(e, Utils.buildList())
        );

        org.junit.Assert.assertFalse(
                (Boolean) e.getExpression("false").evaluate(e, Utils.buildList())
        );

        org.junit.Assert.assertFalse(
                (Boolean) e.getExpression("!").evaluate(e, Utils.buildList(true))
        );

        org.junit.Assert.assertTrue(
                (Boolean) e.getExpression("!").evaluate(e, Utils.buildList(false))
        );

        org.junit.Assert.assertTrue(
                (Boolean) e.getExpression("not").evaluate(e, Utils.buildList(false))
        );

        org.junit.Assert.assertTrue(
                (Boolean) e.getExpression("&&").evaluate(e, Utils.buildList(true))
        );

        org.junit.Assert.assertTrue(
                (Boolean) e.getExpression("&&").evaluate(e, Utils.buildList(true, true, true))
        );

        org.junit.Assert.assertFalse(
                (Boolean) e.getExpression("&&").evaluate(e, Utils.buildList(true, true, false))
        );

        org.junit.Assert.assertFalse(
                (Boolean) e.getExpression("and").evaluate(e, Utils.buildList(true, true, false))
        );

        org.junit.Assert.assertTrue(
                (Boolean) e.getExpression("||").evaluate(e, Utils.buildList(true, false, false))
        );

        org.junit.Assert.assertFalse(
                (Boolean) e.getExpression("||").evaluate(e, Utils.buildList(false, false))
        );

        org.junit.Assert.assertFalse(
                (Boolean) e.getExpression("or").evaluate(e, Utils.buildList(false, false))
        );

        org.junit.Assert.assertTrue(
                (Boolean) e.getExpression("if").evaluate(e, Utils.buildList(true, true, false))
        );

        org.junit.Assert.assertFalse(
                (Boolean) e.getExpression("if").evaluate(e, Utils.buildList(false, true, false))
        );
    }

    @Test
    public void testEvaluatingArithmeticExpressions() {
        Evaluator e = new Evaluator();
        org.junit.Assert.assertEquals(
                2,
                e.getExpression("+").evaluate(e, Utils.buildList(1, 1))
        );

        org.junit.Assert.assertEquals(
                0,
                e.getExpression("+").evaluate(e, Utils.buildList(-1, 1))
        );

        org.junit.Assert.assertEquals(
                4.2,
                e.getExpression("+").evaluate(e, Utils.buildList(3.2, 1))
        );

        org.junit.Assert.assertEquals(
                "Hello World",
                e.getExpression("+").evaluate(e, Utils.buildList("Hello ", "World"))
        );

        org.junit.Assert.assertEquals(
                9,
                e.getExpression("-").evaluate(e, Utils.buildList(10, 1))
        );

        org.junit.Assert.assertEquals(
                9.1,
                e.getExpression("-").evaluate(e, Utils.buildList(10.1, 1))
        );

        org.junit.Assert.assertEquals(
                null,
                e.getExpression("-").evaluate(e, Utils.buildList(10.1, "abc"))
        );

        org.junit.Assert.assertEquals(
                11,
                e.getExpression("-").evaluate(e, Utils.buildList(10, -1))
        );

        org.junit.Assert.assertEquals(
                50,
                e.getExpression("*").evaluate(e, Utils.buildList(10, 5))
        );

        org.junit.Assert.assertEquals(
                52.0,
                e.getExpression("*").evaluate(e, Utils.buildList(10, 5.2))
        );

        org.junit.Assert.assertEquals(
                null,
                e.getExpression("*").evaluate(e, Utils.buildList(10, "a"))
        );

        org.junit.Assert.assertEquals(
                2,
                e.getExpression("/").evaluate(e, Utils.buildList(10, 5))
        );

        org.junit.Assert.assertEquals(
                0.5,
                e.getExpression("/").evaluate(e, Utils.buildList(5, 10.0))
        );

        org.junit.Assert.assertEquals(
                null,
                e.getExpression("/").evaluate(e, Utils.buildList(10, "a"))
        );

        org.junit.Assert.assertEquals(
                0,
                e.getExpression("%").evaluate(e, Utils.buildList(10, 5))
        );

        org.junit.Assert.assertEquals(
                null,
                e.getExpression("%").evaluate(e, Utils.buildList(10, 5.5))
        );

        org.junit.Assert.assertEquals(
                1,
                e.getExpression("%").evaluate(e, Utils.buildList(10, 3))
        );

        org.junit.Assert.assertEquals(
                1,
                e.getExpression("mod").evaluate(e, Utils.buildList(10, 3))
        );

    }

    @Test
    public void testEvaluatingDateExpressions() {
        Evaluator e = new Evaluator();
        org.junit.Assert.assertEquals(
                parseDate("2011-01-01"),
                e.getExpression("date").evaluate(e, Utils.buildList("2011-01-01"))
        );

        org.junit.Assert.assertEquals(
                null,
                e.getExpression("date").evaluate(e, Utils.buildList("2011-01"))
        );

        org.junit.Assert.assertEquals(
                parseTime("2011-01-01 10:01:02"),
                e.getExpression("time").evaluate(e, Utils.buildList("2011-01-01 10:01:02"))
        );

        org.junit.Assert.assertEquals(
                null,
                e.getExpression("time").evaluate(e, Utils.buildList("2011-01"))
        );

        org.junit.Assert.assertEquals(
                new Date(),
                e.getExpression("now").evaluate(e, Utils.buildList())
        );

    }

    @Test
    public void testEvaluatingStringExpressions() {
        Evaluator e = new Evaluator();
        org.junit.Assert.assertEquals(
                "Hello World",
                e.getExpression("append").evaluate(e, Utils.buildList(" World", "Hello"))
        );

        org.junit.Assert.assertEquals(
                "Hello World",
                e.getExpression("prepend").evaluate(e, Utils.buildList("Hello", " World"))
        );
    }

    @Test
    public void testEvaluatingRegularExpressions() {
        Evaluator e = new Evaluator();
        org.junit.Assert.assertTrue(
                (Boolean) e.getExpression("match").evaluate(e, Utils.buildList("/ello/", "Hello World"))
        );

        org.junit.Assert.assertTrue(
                (Boolean) e.getExpression("match").evaluate(e, Utils.buildList("/ello/i", "Hello World"))
        );

        org.junit.Assert.assertTrue(
                (Boolean) e.getExpression("match").evaluate(e, Utils.buildList("/^He/", "Hello World"))
        );

        org.junit.Assert.assertTrue(
                (Boolean) e.getExpression("match").evaluate(e, Utils.buildList("/([m|l])ouse$/i", "mouse"))
        );

        org.junit.Assert.assertTrue(
                (Boolean) e.getExpression("match").evaluate(e, Utils.buildList("/World$/", "Hello World"))
        );

        org.junit.Assert.assertFalse(
                (Boolean) e.getExpression("match").evaluate(e, Utils.buildList("/none/", "Hello World"))
        );

        org.junit.Assert.assertEquals(
                "Hi World",
                e.getExpression("replace").evaluate(e, Utils.buildList("/Hello/", "Hi", "Hello World"))
        );

        org.junit.Assert.assertEquals(
                "Hi World",
                e.getExpression("replace").evaluate(e, Utils.buildList("/Hello/", "Hi", "Hello World"))
        );

        org.junit.Assert.assertEquals(
                "mice",
                e.getExpression("replace").evaluate(e, Utils.buildList("/([m|l])ouse$/i", "$1ice", "mouse"))
        );

        org.junit.Assert.assertEquals(
                "vertices",
                e.getExpression("replace").evaluate(e, Utils.buildList("/(matr|vert|ind)ix|ex$/i", "$1ices", "vertex"))
        );
    }

    @Test
    public void testEvaluatingHelperExpressions() {
        Evaluator e = new Evaluator();
        org.junit.Assert.assertTrue(
                (Boolean) e.getExpression("in").evaluate(e, Utils.buildList("1,2,3,5..10,20..24", "1"))
        );
        org.junit.Assert.assertTrue(
                (Boolean) e.getExpression("in").evaluate(e, Utils.buildList("1,2,3,5..10,20..24", "5"))
        );
        org.junit.Assert.assertTrue(
                (Boolean) e.getExpression("in").evaluate(e, Utils.buildList("1,2,3,5..10,20..24", "6"))
        );
        org.junit.Assert.assertTrue(
                (Boolean) e.getExpression("in").evaluate(e, Utils.buildList("1,2,3,5..10,20..24", "24"))
        );
        org.junit.Assert.assertFalse(
                (Boolean) e.getExpression("in").evaluate(e, Utils.buildList("1,2,3,5..10,20..24", "25"))
        );

        org.junit.Assert.assertFalse(
                (Boolean) e.getExpression("within").evaluate(e, Utils.buildList("0..3", "4"))
        );

        org.junit.Assert.assertTrue(
                (Boolean) e.getExpression("within").evaluate(e, Utils.buildList("0..3", "3"))
        );

        org.junit.Assert.assertTrue(
                (Boolean) e.getExpression("within").evaluate(e, Utils.buildList("0..3", "2.5"))
        );

        e.setVariable("@genders", Utils.buildList("male", "male", "female"));
        org.junit.Assert.assertEquals(
                3,
                e.getExpression("count").evaluate(e, Utils.buildList("@genders"))
        );

        org.junit.Assert.assertEquals(
                3,
                e.getExpression("count").evaluate(e, Utils.buildList(Utils.buildList("male", "male", "female")))
        );

        org.junit.Assert.assertEquals(
                0,
                e.getExpression("count").evaluate(e, Utils.buildList(Utils.buildList()))
        );

        e.setVariable("@genders", Utils.buildList("male", "male", "female"));
        org.junit.Assert.assertTrue(
                (Boolean) e.getExpression("any").evaluate(e, Utils.buildList("@genders", "male"))
        );

        e.setVariable("@genders", Utils.buildList("female", "female", "female"));
        org.junit.Assert.assertFalse(
                (Boolean) e.getExpression("any").evaluate(e, Utils.buildList("@genders", "male"))
        );


        org.junit.Assert.assertFalse(
                (Boolean) e.getExpression("any").evaluate(e, Utils.buildList(Utils.buildList("female", "female", "female"), "male"))
        );

        org.junit.Assert.assertFalse(
                (Boolean) e.getExpression("any").evaluate(e, Utils.buildList(Utils.buildList(), "male"))
        );

        e.setVariable("@genders", Utils.buildList("female", "female", "female"));
        org.junit.Assert.assertFalse(
                (Boolean) e.getExpression("all").evaluate(e, Utils.buildList("@genders", "male"))
        );

        e.setVariable("@genders", Utils.buildList("female", "female", "male"));
        org.junit.Assert.assertFalse(
                (Boolean) e.getExpression("all").evaluate(e, Utils.buildList("@genders", "female"))
        );

        e.setVariable("@genders", Utils.buildList("female", "female", "female"));
        org.junit.Assert.assertTrue(
                (Boolean) e.getExpression("all").evaluate(e, Utils.buildList("@genders", "female"))
        );

        org.junit.Assert.assertTrue(
                (Boolean) e.getExpression("all").evaluate(e, Utils.buildList(Utils.buildList("female", "female", "female"), "female"))
        );

        org.junit.Assert.assertFalse(
                (Boolean) e.getExpression("all").evaluate(e, Utils.buildList(Utils.buildList(), "female"))
        );

    }

    @Test
    public void testEvaluatingFullExpressions() {
        Evaluator e = new Evaluator();
        Parser p = new Parser();

        e.setVariable("@n", 1);
        org.junit.Assert.assertTrue(
                (Boolean) e.evaluate(p.parse("(&& (= 1 (mod @n 10)) (!= 11 (mod @n 100)))"))
        );

        e.setVariable("@n", 10);
        org.junit.Assert.assertFalse(
                (Boolean) e.evaluate(p.parse("(&& (= 1 (mod @n 10)) (!= 11 (mod @n 100)))"))
        );

    }

    @Test
    public void testResettingEvaluator() {
        HashMap<String, Object> vars = new HashMap<String, Object>();
        vars.put("@n", 1);

        Evaluator e = new Evaluator(vars);

        org.junit.Assert.assertEquals(
                1,
                e.getVariable("@n")
        );
        e.reset();

        org.junit.Assert.assertNull(
                e.getVariable("@n")
        );
    }

    @Test
    public void testEvaluatorExtensions() {
        HashMap<String, Object> vars = new HashMap<String, Object>();
        vars.put("@n", 5);

        Map<String, Expression> ext = new HashMap<String, Expression>();
        ext.put("print", new Expression() {
            public Object evaluate(Evaluator evaluator, List params) {
                String varName = (String) params.get(0);
                String varValue = "" + evaluator.getVariable(varName);
                return varName + " = " + varValue;
            }
        });

        Evaluator e = new Evaluator(vars, ext);
        e.addNestedFunction("print");

        Parser p = new Parser();

        org.junit.Assert.assertEquals(
                "@n = 5",
                e.evaluate(p.parse("(print @n)"))
        );

        e.removeNestedFunction("print");
    }
}