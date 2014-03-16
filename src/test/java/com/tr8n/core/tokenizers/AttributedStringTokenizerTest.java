package com.tr8n.core.tokenizers;

import com.tr8n.core.Utils;
import org.junit.Assert;
import org.junit.Test;

import java.text.AttributedString;
import java.util.Arrays;

/**
 * Created by michael on 3/15/14.
 */
public class AttributedStringTokenizerTest {
    @Test
    public void testTokenization() {
        AttributedStringTokenizer dt = new AttributedStringTokenizer("Hello [bold: World]");

        Assert.assertEquals(
                Arrays.asList("bold"),
                dt.getTokenNames()
        );

        dt = new AttributedStringTokenizer("Hello [bold: How are [italic: you?]]");

        Assert.assertEquals(
                Arrays.asList("bold", "italic"),
                dt.getTokenNames()
        );


        // broken
        dt.tokenize("[bold: Hello World");
        Assert.assertEquals(
                Arrays.asList("bold"),
                dt.getTokenNames()
        );

        dt.tokenize("[bold: Hello [strong: World]]");
        Assert.assertEquals(
                Arrays.asList("bold", "strong"),
                dt.getTokenNames()
        );
    }

    @Test
    public void testSubstitution() {
        AttributedStringTokenizer dt = new AttributedStringTokenizer("Hello [bold: World]");
        Assert.assertNotNull(
                dt.generateAttributedString(Utils.buildMap())
        );

    }
}
