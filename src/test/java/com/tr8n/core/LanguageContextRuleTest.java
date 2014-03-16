package com.tr8n.core;

import com.tr8n.core.rulesengine.Evaluator;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * Created by michael on 3/10/14.
 */
public class LanguageContextRuleTest extends BaseTest {

    @Test
    public void testEvaluatingRules() {
        LanguageContextRule rule = new LanguageContextRule(Utils.buildMap(
                "keyword", "many",
                "description", "{token} mod 10 is 0 or {token} mod 10 in 5..9 or {token} mod 100 in 11..14",
                "examples", "0, 5-20, 25-30, 35-40...",
                "conditions", "(|| (= 0 (mod @n 10)) (in '5..9' (mod @n 10)) (in '11..14' (mod @n 100)))"
        ));

        Assert.assertEquals(
                "many",
                rule.getKeyword()
        );

        Assert.assertEquals(
                "{token} mod 10 is 0 or {token} mod 10 in 5..9 or {token} mod 100 in 11..14",
                rule.getDescription()
        );

        Assert.assertEquals(
                "0, 5-20, 25-30, 35-40...",
                rule.getExamples()
        );

        Assert.assertEquals(
                "(|| (= 0 (mod @n 10)) (in '5..9' (mod @n 10)) (in '11..14' (mod @n 100)))",
                rule.getConditions()
        );

        Assert.assertTrue(
                (Boolean) rule.evaluate(Utils.buildMap("@n", 5))
        );
        Assert.assertTrue(
                (Boolean) rule.evaluate(Utils.buildMap("@n", 9))
        );
        Assert.assertTrue(
                (Boolean) rule.evaluate(Utils.buildMap("@n", 11))
        );
        Assert.assertTrue(
                (Boolean) rule.evaluate(Utils.buildMap("@n", 12))
        );
        Assert.assertTrue(
                (Boolean) rule.evaluate(Utils.buildMap("@n", 14))
        );
        Assert.assertTrue(
                (Boolean) rule.evaluate(Utils.buildMap("@n", 50))
        );

        Assert.assertFalse(
                (Boolean) rule.evaluate(Utils.buildMap("@n", 1))
        );
        Assert.assertFalse(
                (Boolean) rule.evaluate(Utils.buildMap("@n", 2))
        );
        Assert.assertFalse(
                (Boolean) rule.evaluate(Utils.buildMap("@n", 4))
        );
        Assert.assertFalse(
                (Boolean) rule.evaluate(Utils.buildMap("@n", 51))
        );

        rule = new LanguageContextRule(Utils.buildMap("keyword", "female", "conditions", "(= 'female' @gender)"));

        Assert.assertFalse(
                (Boolean) rule.evaluate(Utils.buildMap("@gender", "male"))
        );

        Assert.assertTrue(
                (Boolean) rule.evaluate(Utils.buildMap("@gender", "female"))
        );

        Assert.assertFalse(
                (Boolean) rule.evaluate(Utils.buildMap("@gender", "unknown"))
        );


        rule = new LanguageContextRule(Utils.buildMap("keyword", "female", "conditions", "(&& (= 1 (count @genders)) (all @genders 'female'))"));

        Assert.assertTrue(
                (Boolean) rule.evaluate(Utils.buildMap("@genders", Utils.buildList("female")))
        );
        Assert.assertFalse(
                (Boolean) rule.evaluate(Utils.buildMap("@genders", Utils.buildList("female", "female")))
        );

        rule = new LanguageContextRule(Utils.buildMap("keyword", "female", "conditions", "(&& (> (count @genders) 1) (all @genders 'female'))"));

        Assert.assertFalse(
                (Boolean) rule.evaluate(Utils.buildMap("@genders", Utils.buildList("female")))
        );
        Assert.assertTrue(
                (Boolean) rule.evaluate(Utils.buildMap("@genders", Utils.buildList("female", "female")))
        );


    }

}
