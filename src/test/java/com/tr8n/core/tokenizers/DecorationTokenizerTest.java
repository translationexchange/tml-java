package com.tr8n.core.tokenizers;

import com.tr8n.core.Utils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * Created by michael on 3/14/14.
 */
public class DecorationTokenizerTest {

    @Test
    public void testTokenization() {
        DecorationTokenizer dt = new DecorationTokenizer("Hello [bold: World]");

        Assert.assertEquals(
                Arrays.asList("bold"),
                dt.getTokenNames()
        );

        dt = new DecorationTokenizer("Hello [bold: How are [italic: you?]]");

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

        // broken
        dt.tokenize("[bold: Hello [strong: World]");
        Assert.assertEquals(
                Arrays.asList("bold", "strong"),
                dt.getTokenNames()
        );

        dt.tokenize("[bold1: Hello [strong22: World]]");
        Assert.assertEquals(
                Arrays.asList("bold1", "strong22"),
                dt.getTokenNames()
        );

        dt.tokenize("[bold: Hello, [strong: how] [weak: are] you?]");
        Assert.assertEquals(
                Arrays.asList("bold", "strong", "weak"),
                dt.getTokenNames()
        );

        dt.tokenize("[bold: Hello, [strong: how [weak: are] you?]");
        Assert.assertEquals(
                Arrays.asList("bold", "strong", "weak"),
                dt.getTokenNames()
        );

        dt.tokenize("[link: you have [italic: [bold: {count}] messages] [light: in your mailbox]]");
        Assert.assertEquals(
                Arrays.asList("link", "italic", "bold", "light"),
                dt.getTokenNames()
        );

        dt.tokenize("[link] you have [italic: [bold: {count}] messages] [light: in your mailbox] [/link]");
        Assert.assertEquals(
                Arrays.asList("link", "italic", "bold", "light"),
                dt.getTokenNames()
        );
    }

    @Test
    public void testSubstitution() {
        DecorationTokenizer dt = new DecorationTokenizer("Hello [bold: World]");
        Assert.assertEquals(
                "Hello World",
                dt.substitute(Utils.buildMap())
        );

        dt.tokenize("[link] you have [italic: [bold: {count}] messages] [light: in your mailbox] [/link]");
        Assert.assertEquals(
                " you have {count} messages in your mailbox ",
                dt.substitute(Utils.buildMap())
        );

        dt.tokenize("[link]you have [italic: [bold: {count}] messages] [light: in your mailbox][/link]");
        Assert.assertEquals(
                "you have {count} messages in your mailbox",
                dt.substitute(Utils.buildMap())
        );
    }

}
