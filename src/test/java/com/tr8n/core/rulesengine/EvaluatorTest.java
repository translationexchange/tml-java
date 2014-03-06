package com.tr8n.core.rulesengine;

import java.util.*;

import org.junit.Before;
import org.junit.Test;

import com.tr8n.core.BaseTest;

public class EvaluatorTest extends BaseTest {

    @Test
    public void testEvaluatingStandardExpressions() {
        Evaluator e = new Evaluator();

        org.junit.Assert.assertEquals(
                "Michael",
                e.getExpression("label").evaluate(e, Arrays.asList("name", "Michael"))
        );
        org.junit.Assert.assertEquals(
                "Michael",
                e.getVariable("name")
        );

        List list = Arrays.asList("one", "two");
        org.junit.Assert.assertEquals(
                list,
                e.getExpression("quote").evaluate(e, Arrays.asList(list))
        );

        list = Arrays.asList("+", "one", "two");
        org.junit.Assert.assertEquals(
                "one",
                e.getExpression("car").evaluate(e, Arrays.asList(list))
        );

        org.junit.Assert.assertEquals(
                e.getExpression("cdr").evaluate(e, Arrays.asList(list)),
                Arrays.asList("one", "two")
        );

        // ["cons", 1, ["quote", [2, 3]]] => [1, 2, 3]
        org.junit.Assert.assertEquals(
                Arrays.asList("1", "2", "3"),
                e.getExpression("cons").evaluate(e, Arrays.asList("1", Arrays.asList("2", "3")))
        );

        org.junit.Assert.assertTrue(
                (Boolean) e.getExpression("eq").evaluate(e, Arrays.asList("1", "1"))
        );

        org.junit.Assert.assertTrue(
                (Boolean) e.getExpression("eq").evaluate(e, Arrays.asList(1, 1))
        );

        org.junit.Assert.assertFalse(
                (Boolean) e.getExpression("eq").evaluate(e, Arrays.asList(1, 2))
        );

        org.junit.Assert.assertTrue(
                (Boolean) e.getExpression("eq").evaluate(e, Arrays.asList(parseDate("2010-01-01"), parseDate("2010-01-01")))
        );

        org.junit.Assert.assertFalse(
                (Boolean) e.getExpression("eq").evaluate(e, Arrays.asList(parseDate("2010-02-01"), parseDate("2010-01-02")))
        );

        org.junit.Assert.assertTrue((Boolean) e.getExpression("atom").evaluate(e, Arrays.asList(1)));
        org.junit.Assert.assertTrue((Boolean) e.getExpression("atom").evaluate(e, Arrays.asList(true)));
        org.junit.Assert.assertTrue((Boolean) e.getExpression("atom").evaluate(e, Arrays.asList(1.4)));
        org.junit.Assert.assertTrue((Boolean) e.getExpression("atom").evaluate(e, Arrays.asList(new Date())));
        org.junit.Assert.assertFalse((Boolean) e.getExpression("atom").evaluate(e, Arrays.asList(new ArrayList())));

        org.junit.Assert.assertEquals(
            "yes",
            e.getExpression("cond").evaluate(e, Arrays.asList(true, "yes", "no"))
        );

        org.junit.Assert.assertEquals(
            "no",
            e.getExpression("cond").evaluate(e, Arrays.asList(false, "yes", "no"))
        );


        org.junit.Assert.assertEquals(
            "no",
            e.getExpression("cond").evaluate(e, Arrays.asList(false, "yes", "no"))
        );


        org.junit.Assert.assertEquals(
            3,
            e.getExpression("+").evaluate(e, Arrays.asList(1, 2))
        );

    }

    @Test
    public void testEvaluatingAssignmentExpressions() {
        Evaluator e = new Evaluator();
        org.junit.Assert.assertEquals(
            "Michael",
            e.getExpression("let").evaluate(e, Arrays.asList("@name", "Michael"))
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
                (Boolean) e.getExpression("=").evaluate(e, Arrays.asList("a", "a"))
        );

        org.junit.Assert.assertTrue(
                (Boolean) e.getExpression("!=").evaluate(e, Arrays.asList(1, 2))
        );

        org.junit.Assert.assertFalse(
                (Boolean) e.getExpression("!=").evaluate(e, Arrays.asList(1, 1))
        );

        org.junit.Assert.assertTrue(
                (Boolean) e.getExpression("<").evaluate(e, Arrays.asList("ab", "abc"))
        );

        org.junit.Assert.assertTrue(
                (Boolean) e.getExpression("<").evaluate(e, Arrays.asList(parseDate("2010-01-01"), parseDate("2011-01-01")))
        );

        org.junit.Assert.assertTrue(
                (Boolean) e.getExpression("<").evaluate(e, Arrays.asList(1, 2))
        );

        org.junit.Assert.assertFalse(
                (Boolean) e.getExpression("<").evaluate(e, Arrays.asList(2, 2))
        );

        org.junit.Assert.assertFalse(
                (Boolean) e.getExpression("<").evaluate(e, Arrays.asList(5, 2))
        );

        org.junit.Assert.assertTrue(
                (Boolean) e.getExpression("<=").evaluate(e, Arrays.asList(2, 2))
        );

        org.junit.Assert.assertFalse(
                (Boolean) e.getExpression("<=").evaluate(e, Arrays.asList(4, 2))
        );

        org.junit.Assert.assertTrue(
                (Boolean) e.getExpression(">").evaluate(e, Arrays.asList(3, 2))
        );

        org.junit.Assert.assertTrue(
                (Boolean) e.getExpression(">=").evaluate(e, Arrays.asList(2, 2))
        );

        org.junit.Assert.assertFalse(
                (Boolean) e.getExpression(">=").evaluate(e, Arrays.asList(4, 12))
        );

        org.junit.Assert.assertTrue(
                (Boolean) e.getExpression("true").evaluate(e, Arrays.asList())
        );

        org.junit.Assert.assertFalse(
                (Boolean) e.getExpression("false").evaluate(e, Arrays.asList())
        );

        org.junit.Assert.assertFalse(
                (Boolean) e.getExpression("!").evaluate(e, Arrays.asList(true))
        );

        org.junit.Assert.assertTrue(
                (Boolean) e.getExpression("!").evaluate(e, Arrays.asList(false))
        );

        org.junit.Assert.assertTrue(
                (Boolean) e.getExpression("not").evaluate(e, Arrays.asList(false))
        );

        org.junit.Assert.assertTrue(
                (Boolean) e.getExpression("&&").evaluate(e, Arrays.asList(true))
        );

        org.junit.Assert.assertTrue(
                (Boolean) e.getExpression("&&").evaluate(e, Arrays.asList(true, true, true))
        );

        org.junit.Assert.assertFalse(
                (Boolean) e.getExpression("&&").evaluate(e, Arrays.asList(true, true, false))
        );

        org.junit.Assert.assertFalse(
                (Boolean) e.getExpression("and").evaluate(e, Arrays.asList(true, true, false))
        );

        org.junit.Assert.assertTrue(
                (Boolean) e.getExpression("||").evaluate(e, Arrays.asList(true, false, false))
        );

        org.junit.Assert.assertFalse(
                (Boolean) e.getExpression("||").evaluate(e, Arrays.asList(false, false))
        );

        org.junit.Assert.assertFalse(
                (Boolean) e.getExpression("or").evaluate(e, Arrays.asList(false, false))
        );

        org.junit.Assert.assertTrue(
                (Boolean) e.getExpression("if").evaluate(e, Arrays.asList(true, true, false))
        );

        org.junit.Assert.assertFalse(
                (Boolean) e.getExpression("if").evaluate(e, Arrays.asList(false, true, false))
        );
    }

    @Test
    public void testEvaluatingArithmeticExpressions() {
        Evaluator e = new Evaluator();
        org.junit.Assert.assertEquals(
                2,
                e.getExpression("+").evaluate(e, Arrays.asList(1, 1))
        );

        org.junit.Assert.assertEquals(
                0,
                e.getExpression("+").evaluate(e, Arrays.asList(-1, 1))
        );

        org.junit.Assert.assertEquals(
                4.2,
                e.getExpression("+").evaluate(e, Arrays.asList(3.2, 1))
        );

        org.junit.Assert.assertEquals(
                "Hello World",
                e.getExpression("+").evaluate(e, Arrays.asList("Hello ", "World"))
        );

        org.junit.Assert.assertEquals(
                9,
                e.getExpression("-").evaluate(e, Arrays.asList(10, 1))
        );

        org.junit.Assert.assertEquals(
                9.1,
                e.getExpression("-").evaluate(e, Arrays.asList(10.1, 1))
        );

        org.junit.Assert.assertEquals(
                null,
                e.getExpression("-").evaluate(e, Arrays.asList(10.1, "abc"))
        );

        org.junit.Assert.assertEquals(
                11,
                e.getExpression("-").evaluate(e, Arrays.asList(10, -1))
        );

        org.junit.Assert.assertEquals(
                50,
                e.getExpression("*").evaluate(e, Arrays.asList(10, 5))
        );

        org.junit.Assert.assertEquals(
                52.0,
                e.getExpression("*").evaluate(e, Arrays.asList(10, 5.2))
        );

        org.junit.Assert.assertEquals(
                null,
                e.getExpression("*").evaluate(e, Arrays.asList(10, "a"))
        );

        org.junit.Assert.assertEquals(
                2,
                e.getExpression("/").evaluate(e, Arrays.asList(10, 5))
        );

        org.junit.Assert.assertEquals(
                0.5,
                e.getExpression("/").evaluate(e, Arrays.asList(5, 10.0))
        );

        org.junit.Assert.assertEquals(
                null,
                e.getExpression("/").evaluate(e, Arrays.asList(10, "a"))
        );

        org.junit.Assert.assertEquals(
                0,
                e.getExpression("%").evaluate(e, Arrays.asList(10, 5))
        );

        org.junit.Assert.assertEquals(
                null,
                e.getExpression("%").evaluate(e, Arrays.asList(10, 5.5))
        );

        org.junit.Assert.assertEquals(
                1,
                e.getExpression("%").evaluate(e, Arrays.asList(10, 3))
        );

        org.junit.Assert.assertEquals(
                1,
                e.getExpression("mod").evaluate(e, Arrays.asList(10, 3))
        );

    }

    @Test
    public void testEvaluatingDateExpressions() {
        Evaluator e = new Evaluator();
        org.junit.Assert.assertEquals(
                parseDate("2011-01-01"),
                e.getExpression("date").evaluate(e, Arrays.asList("2011-01-01"))
        );

        org.junit.Assert.assertEquals(
                null,
                e.getExpression("date").evaluate(e, Arrays.asList("2011-01"))
        );

        org.junit.Assert.assertEquals(
                parseTime("2011-01-01 10:01:02"),
                e.getExpression("time").evaluate(e, Arrays.asList("2011-01-01 10:01:02"))
        );

        org.junit.Assert.assertEquals(
                null,
                e.getExpression("time").evaluate(e, Arrays.asList("2011-01"))
        );

        org.junit.Assert.assertEquals(
                new Date(),
                e.getExpression("now").evaluate(e, Arrays.asList())
        );

    }

    @Test
    public void testEvaluatingStringExpressions() {
        Evaluator e = new Evaluator();
        org.junit.Assert.assertEquals(
                "Hello World",
                e.getExpression("append").evaluate(e, Arrays.asList(" World", "Hello"))
        );

        org.junit.Assert.assertEquals(
                "Hello World",
                e.getExpression("prepend").evaluate(e, Arrays.asList("Hello", " World"))
        );
    }

    @Test
    public void testEvaluatingRegularExpressions() {
        Evaluator e = new Evaluator();
        org.junit.Assert.assertTrue(
                (Boolean) e.getExpression("match").evaluate(e, Arrays.asList("/ello/", "Hello World"))
        );

        org.junit.Assert.assertTrue(
                (Boolean) e.getExpression("match").evaluate(e, Arrays.asList("/ello/i", "Hello World"))
        );

        org.junit.Assert.assertTrue(
                (Boolean) e.getExpression("match").evaluate(e, Arrays.asList("/^He/", "Hello World"))
        );

        org.junit.Assert.assertTrue(
                (Boolean) e.getExpression("match").evaluate(e, Arrays.asList("/([m|l])ouse$/i", "mouse"))
        );

        org.junit.Assert.assertTrue(
                (Boolean) e.getExpression("match").evaluate(e, Arrays.asList("/World$/", "Hello World"))
        );

        org.junit.Assert.assertFalse(
                (Boolean) e.getExpression("match").evaluate(e, Arrays.asList("/none/", "Hello World"))
        );

        org.junit.Assert.assertEquals(
                "Hi World",
                e.getExpression("replace").evaluate(e, Arrays.asList("/Hello/", "Hi", "Hello World"))
        );

        org.junit.Assert.assertEquals(
                "Hi World",
                e.getExpression("replace").evaluate(e, Arrays.asList("/Hello/", "Hi", "Hello World"))
        );

        org.junit.Assert.assertEquals(
                "mice",
                e.getExpression("replace").evaluate(e, Arrays.asList("/([m|l])ouse$/i", "$1ice", "mouse"))
        );

        org.junit.Assert.assertEquals(
                "vertices",
                e.getExpression("replace").evaluate(e, Arrays.asList("/(matr|vert|ind)ix|ex$/i", "$1ices", "vertex"))
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
}