package com.tr8n.core;

import org.junit.Test;

/**
 * Created by michael on 3/15/14.
 */
public class LanguageContextTest extends BaseTest {

    @Test
    public void testManualCreation() {
        Language russian = new Language(loadJSONMap("/languages/ru.json"));
        LanguageContext context = new LanguageContext();
        context.setLanguage(russian);
        context.setDescription("Test");
        context.setFallbackRule(null);
        context.setKeys(null);
        context.setKeyword("key");
        context.setRules(null);
        context.setTokenExpression(null);
        context.setTokenMapping(null);
        context.setVariableNames(null);
    }

}