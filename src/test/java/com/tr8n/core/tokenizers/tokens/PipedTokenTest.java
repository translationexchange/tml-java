package com.tr8n.core.tokenizers.tokens;

import com.tr8n.core.Utils;
import com.tr8n.core.tokenizers.tokens.DataToken;
import com.tr8n.core.tokenizers.tokens.PipedToken;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by michael on 3/12/14.
 */
public class PipedTokenTest {

    @Test
    public void testTokenNames() {
        PipedToken token = new PipedToken("{count|| item, items}");

        Assert.assertEquals(
                "{count|| item, items}",
                token.getFullName()
        );
    }

    @Test
    public void testSeparator() {
        PipedToken token = new PipedToken("{count|| item, items}");

        Assert.assertEquals(
                "||",
                token.getSeparator()
        );

        token = new PipedToken("{count| item, items}");

        Assert.assertEquals(
                "|",
                token.getSeparator()
        );
    }


    /**
     * token:      {count|| one: message, many: messages}
     * results in: {"one": "message", "many": "messages"}
     *
     * token:      {count|| message}
     * transform:  [{"one": "{$0}", "other": "{$0::plural}"}, {"one": "{$0}", "other": "{$1}"}]
     * results in: {"one": "message", "other": "messages"}
     *
     * token:      {count|| message, messages}
     * transform:  [{"one": "{$0}", "other": "{$0::plural}"}, {"one": "{$0}", "other": "{$1}"}]
     * results in: {"one": "message", "other": "messages"}
     *
     * token:      {user| Dorogoi, Dorogaya}
     * transform:  ["unsupported", {"male": "{$0}", "female": "{$1}", "other": "{$0}/{$1}"}]
     * results in: {"male": "Dorogoi", "female": "Dorogaya", "other": "Dorogoi/Dorogaya"}
     *
     * token:      {actors:|| likes, like}
     * transform:  ["unsupported", {"one": "{$0}", "other": "{$1}"}]
     * results in: {"one": "likes", "other": "like"}
     *
     */
    @Test
    public void testParameters() {
        PipedToken token = new PipedToken("{count|| item, items}");

        Assert.assertEquals(
                Utils.buildList("item", "items"),
                token.getParameters()
        );

        token = new PipedToken("{count|| one: message, many: messages}");
        Assert.assertEquals(
                Utils.buildList("one: message", "many: messages"),
                token.getParameters()
        );

        token = new PipedToken("{count|| message}");
        Assert.assertEquals(
                Utils.buildList("message"),
                token.getParameters()
        );
    }


   @Test
    public void testParameterValueMap() {
       PipedToken token = new PipedToken("{count|| one: message, many: messages}");

       Assert.assertEquals(
               Utils.buildMap("one", "message", "many", "messages"),
               token.getParameterMap(null)
       );

       token = new PipedToken("{count|| item, items}");

       Assert.assertEquals(
               null,
               token.getParameterMap(null)
       );

       Assert.assertEquals(
               null,
               token.getParameterMap(null)
       );

       token = new PipedToken("{count|| message}");

       Assert.assertEquals(
               Utils.buildMap("one", "message", "other", "message"),
               token.getParameterMap(Utils.buildMap("one", "{$0}", "other", "{$0::plural}"))
       );

       Assert.assertEquals(
               Utils.buildMap("one", "message", "other", "message"),
               token.getParameterMap(Utils.buildList(Utils.buildMap("one", "{$0}", "other", "{$0::plural}"), Utils.buildMap("one", "{$0}", "other", "{$1}")))
       );

       token = new PipedToken("{count|| message, messages}");

       Assert.assertEquals(
               Utils.buildMap("one", "message", "other", "messages"),
               token.getParameterMap(Utils.buildList(Utils.buildMap("one", "{$0}", "other", "{$0::plural}"), Utils.buildMap("one", "{$0}", "other", "{$1}")))
       );

       Assert.assertEquals(
               null,
               token.getParameterMap(Utils.buildList(Utils.buildMap("one", "{$0}", "other", "{$0::plural}"), "undefined"))
       );

       Assert.assertEquals(
               null,
               token.getParameterMap("undefined")
       );

       Assert.assertEquals(
               null,
               token.getParameterMap(Utils.buildList(Utils.buildMap("one", "{$0}", "other", "{$0::plural}")))
       );

       Assert.assertEquals(
               null,
               token.getParameterMap(Utils.buildList(Utils.buildMap("one", "{$0}", "other", "{$3::plural}")))
       );

    }
}
