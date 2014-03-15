package com.tr8n.core.tokenizers;

import com.tr8n.core.Utils;
import com.tr8n.core.tokenizers.tokens.Token;
import com.tr8n.core.tokenizers.tokens.DataToken;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * Created by michael on 3/11/14.
 */
public class DataTokenizerTest {

    @Test
    public void testTokenization() {
        DataTokenizer dt = new DataTokenizer("Hello {user}");

        Assert.assertEquals(
                1,
                dt.tokens.size()
        );

        Token token = (Token) dt.tokens.get(0);
        Assert.assertEquals(
                "{user}",
                token.getFullName()
        );

        dt = new DataTokenizer();

        dt.tokenize("{user} has {count} messages");
        Assert.assertEquals(
                Arrays.asList("user", "count"),
                dt.getTokenNames()
        );

        dt.tokenize("Hello {user}, how are you {user}");
        Assert.assertEquals(
                Arrays.asList("user"),
                dt.getTokenNames()
        );

        Assert.assertEquals(
                Arrays.asList("user"),
                dt.getTokenNames()
        );

        dt = new DataTokenizer("Hello {user}", Utils.buildStringList("user"));

        Assert.assertEquals(
                Arrays.asList("user"),
                dt.getAllowedTokenNames()
        );

    }

    @Test
    public void testSubstitution() {
        DataTokenizer dt = new DataTokenizer("Hello {user}");

        Assert.assertEquals(
                "Hello Michael",
                dt.substitute(Utils.buildMap("user", "Michael"))
        );

    }

}
