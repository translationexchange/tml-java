package com.tr8n.core;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by michael on 3/15/14.
 */
public class LanguageTest extends BaseTest {

    @Test
    public void testCreation() {
        Language russian = new Language(loadJSONMap("/languages/ru.json"));

        Assert.assertEquals(
                "ru",
                russian.getLocale()
        );

        Assert.assertEquals(
                "Russian (ru)",
                russian.toString()
        );
        
    }

    
    @Test
    public void testContext() {
        Language russian = new Language(loadJSONMap("/languages/ru.json"));

        Assert.assertEquals(
                "gender",
                russian.getContextByTokenName("user").getKeyword()
        );
    	
    }    
}

