package com.tr8n.core.tokenizers;

import com.tr8n.core.tokenizers.tokens.BaseToken;
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

        BaseToken token = (BaseToken) dt.tokens.get(0);
        Assert.assertEquals(
                "{user}",
                token.getFullName()
        );

        dt.tokenize("{user} has {count} messages");
        Assert.assertEquals(
                Arrays.asList("user", "count"),
                dt.getTokenNames()
        );

    }


}
