package com.tr8n.core;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by michael on 3/10/14.
 */
public class LanguageCaseRuleTest extends BaseTest {

    @Test
    public void testEvaluatingRules() {
       LanguageCaseRule rule = new LanguageCaseRule();
       Assert.assertNull(
               rule.getDescription()
       );

    		   
       rule = new LanguageCaseRule(Utils.buildMap(
    		   "conditions", "(= 1 1)",
    		   "operations", "(+ 1 1)"
       ));

       Assert.assertEquals(
    		   Utils.buildList("=", 1l, 1l),
               rule.getConditionsExpression()
       );
       
       Assert.assertEquals(
    		   Utils.buildList("+", 1l, 1l),
               rule.getOperationsExpression()
       );
       
       
       rule = new LanguageCaseRule(Utils.buildMap(
    		   "conditions", "(= @value 'Michael')",
    		   "operations", "(prepend 'Hello ' @value)"
       ));

       Assert.assertTrue(
    		   rule.evaluate("Michael")
       );
       
       Assert.assertEquals(
    		   "Hello Michael",
    		   rule.apply("Michael")
       );
       
    }

}
