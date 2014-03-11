package com.tr8n.core;

import com.tr8n.core.rulesengine.Evaluator;
import org.junit.Test;

import java.util.Arrays;

/**
 * Created by michael on 3/10/14.
 */
public class LanguageContextRuleTest extends BaseTest {

    @Test
    public void testEvaluatingRules() {
        LanguageContextRule rule = new LanguageContextRule(loadJsonMapFromString("{" +
            "\"keyword\": \"many\"," +
            "\"description\": \"{token} mod 10 is 0 or {token} mod 10 in 5..9 or {token} mod 100 in 11..14\"," +
            "\"examples\": \"0, 5-20, 25-30, 35-40...\"," +
            "\"conditions\": \"(|| (= 0 (mod @n 10)) (in '5..9' (mod @n 10)) (in '11..14' (mod @n 100)))\"" +
            "}"));

        org.junit.Assert.assertEquals(
                "many",
                rule.keyword
        );

        org.junit.Assert.assertEquals(
                "{token} mod 10 is 0 or {token} mod 10 in 5..9 or {token} mod 100 in 11..14",
                rule.description
        );

        org.junit.Assert.assertEquals(
                "0, 5-20, 25-30, 35-40...",
                rule.examples
        );

        org.junit.Assert.assertEquals(
                "(|| (= 0 (mod @n 10)) (in '5..9' (mod @n 10)) (in '11..14' (mod @n 100)))",
                rule.conditions
        );

        org.junit.Assert.assertTrue(
                (Boolean) rule.evaluate(loadJsonMapFromString("{\"@n\":5}"))
        );
        org.junit.Assert.assertTrue(
                (Boolean) rule.evaluate(loadJsonMapFromString("{\"@n\":9}"))
        );
        org.junit.Assert.assertTrue(
                (Boolean) rule.evaluate(loadJsonMapFromString("{\"@n\":11}"))
        );
        org.junit.Assert.assertTrue(
                (Boolean) rule.evaluate(loadJsonMapFromString("{\"@n\":12}"))
        );
        org.junit.Assert.assertTrue(
                (Boolean) rule.evaluate(loadJsonMapFromString("{\"@n\":14}"))
        );
        org.junit.Assert.assertTrue(
                (Boolean) rule.evaluate(loadJsonMapFromString("{\"@n\":50}"))
        );

        org.junit.Assert.assertFalse(
                (Boolean) rule.evaluate(loadJsonMapFromString("{\"@n\":1}"))
        );
        org.junit.Assert.assertFalse(
                (Boolean) rule.evaluate(loadJsonMapFromString("{\"@n\":2}"))
        );
        org.junit.Assert.assertFalse(
                (Boolean) rule.evaluate(loadJsonMapFromString("{\"@n\":4}"))
        );
        org.junit.Assert.assertFalse(
                (Boolean) rule.evaluate(loadJsonMapFromString("{\"@n\":51}"))
        );

        rule = new LanguageContextRule(loadJsonMapFromString("{" +
                "\"keyword\": \"female\"," +
                "\"conditions\": \"(= 'female' @gender)\"" +
                "}"));

        org.junit.Assert.assertFalse(
                (Boolean) rule.evaluate(loadJsonMapFromString("{\"@gender\":\"male\"}"))
        );

        org.junit.Assert.assertTrue(
                (Boolean) rule.evaluate(loadJsonMapFromString("{\"@gender\":\"female\"}"))
        );

        org.junit.Assert.assertFalse(
                (Boolean) rule.evaluate(loadJsonMapFromString("{\"@gender\":\"unknown\"}"))
        );

        rule = new LanguageContextRule(loadJsonMapFromString("{" +
                "\"keyword\": \"female\"," +
                "\"conditions\": \"(&& (= 1 (count @genders)) (all @genders 'female'))\"" +
                "}"));

        org.junit.Assert.assertTrue(
                (Boolean) rule.evaluate(loadJsonMapFromString("{\"@genders\":[\"female\"]}"))
        );
        org.junit.Assert.assertFalse(
                (Boolean) rule.evaluate(loadJsonMapFromString("{\"@genders\":[\"female\", \"female\"]}"))
        );

        rule = new LanguageContextRule(loadJsonMapFromString("{" +
                "\"keyword\": \"female\"," +
                "\"conditions\": \"(&& (> (count @genders) 1) (all @genders 'female'))\"" +
                "}"));

        org.junit.Assert.assertFalse(
                (Boolean) rule.evaluate(loadJsonMapFromString("{\"@genders\":[\"female\"]}"))
        );
        org.junit.Assert.assertTrue(
                (Boolean) rule.evaluate(loadJsonMapFromString("{\"@genders\":[\"female\", \"female\"]}"))
        );

    }

}
