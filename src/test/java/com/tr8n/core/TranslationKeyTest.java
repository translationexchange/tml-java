package com.tr8n.core;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by michael on 3/11/14.
 */
public class TranslationKeyTest {

    @Test
    public void testKeyGeneration() {
        Assert.assertEquals(
                "d541c79af1be6a05b1f16fca8b5730de",
                TranslationKey.generateKey("Hello World")
        );
    }

}
