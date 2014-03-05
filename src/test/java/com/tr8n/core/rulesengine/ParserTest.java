package com.tr8n.core.rulesengine;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.tr8n.core.BaseTest;

public class ParserTest extends BaseTest {

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testSplittingTokens() {
        Parser parser = new Parser("(= 1 (mod n 10))");
        org.junit.Assert.assertArrayEquals(
                new String[] {"(", "=", "1", "(", "mod", "n", "10", ")", ")"},
                parser.getTokens().toArray()
        );

        parser = new Parser("(&& (= 1 (mod @n 10)) (!= 11 (mod @n 100)))");
        org.junit.Assert.assertArrayEquals(
                new String[] {"(", "&&", "(", "=", "1", "(", "mod", "@n", "10", ")", ")", "(", "!=", "11", "(", "mod", "@n", "100", ")", ")", ")"},
                parser.getTokens().toArray()
        );

        parser = new Parser("(= '1' @value)");
        org.junit.Assert.assertArrayEquals(
                new String[] {"(", "=", "'1'", "@value", ")"},
                parser.getTokens().toArray()
        );

        parser = new Parser("(= 'abc' @value)");
        org.junit.Assert.assertArrayEquals(
                new String[] {"(", "=", "'abc'", "@value", ")"},
                parser.getTokens().toArray()
        );

        parser = new Parser("(= 'hello world' @value)");
        org.junit.Assert.assertArrayEquals(
                new String[] {"(", "=", "'hello world'", "@value", ")"},
                parser.getTokens().toArray()
        );
    }


    @SuppressWarnings("rawtypes")
    @Test
    public void testParsingExpressions() {
        Map data = loadJSONMap("/rules.json");
//		System.out.println(obj);

        Parser parser;
//		ArrayList<String> results;

        Iterator iter = data.keySet().iterator();
        while (iter.hasNext()) {
            String key = (String) iter.next();
//			System.out.println(key);
            Object expectation = (Object) data.get(key);

//			System.out.println("Test: " + key);
//			System.out.println("Expectation = " + expectation);
            parser = new Parser(key);
//			System.out.println("Parsed to: " + parser.getTokens() + "\n");

            if (expectation instanceof String) {
                org.junit.Assert.assertEquals(
                        parser.parse(),
                        expectation
                );
            } else {
                List expectationList = (List) data.get(key);

                @SuppressWarnings("unchecked")
                List<String> results = (ArrayList<String>) parser.parse();

//				System.out.println("Evaluated to: " + results);
                org.junit.Assert.assertArrayEquals(
                        results.toArray(),
                        expectationList.toArray()
                );
            }
//			System.out.println("Passed");
        }
    }

}
