package com.tr8n.core.tokenizers.tokens;

import com.tr8n.core.Utils;
import com.tr8n.core.models.User;
import com.tr8n.core.tokenizers.DataTokenValue;
import com.tr8n.core.tokenizers.tokens.DataToken;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class DataTokenTest {

    @Test
    public void testTokenNames() {
        DataToken token = new DataToken("{user}");

        Assert.assertEquals(
                "{user}",
                token.getFullName()
        );

        Assert.assertEquals(
                "user",
                token.getName()
        );

        Assert.assertEquals(
                "user",
                token.getObjectName()
        );

        token = new DataToken("{ user }");

        Assert.assertEquals(
                "{ user }",
                token.getFullName()
        );

        Assert.assertEquals(
                "user",
                token.getName()
        );

        Assert.assertEquals(
                "user",
                token.getName(Utils.buildMap())
        );

        Assert.assertEquals(
                "{user}",
                token.getName(Utils.buildMap("parens", true))
        );

    }

    @Test
    public void testTokenContexts() {
        DataToken token = new DataToken("{user}");

        token = new DataToken("{user:gender}");
        Assert.assertEquals(
                Arrays.asList("gender"),
                token.getLanguageContextKeys()
        );

        token = new DataToken("{user : gender}");
        Assert.assertEquals(
                Arrays.asList("gender"),
                token.getLanguageContextKeys()
        );

        token = new DataToken("{ user : gender }");
        Assert.assertEquals(
                Arrays.asList("gender"),
                token.getLanguageContextKeys()
        );

        token = new DataToken("{user:gender:value}");
        Assert.assertEquals(
                Arrays.asList("gender", "value"),
                token.getLanguageContextKeys()
        );

        token = new DataToken("{user : gender : value}");
        Assert.assertEquals(
                Arrays.asList("gender", "value"),
                token.getLanguageContextKeys()
        );

        Assert.assertEquals(
                Arrays.asList("gender", "value"),
                token.getLanguageContextKeys()
        );
    }

    @Test
    public void testTokenCases() {
        DataToken token = new DataToken("{count::ord}");

        Assert.assertEquals(
                Arrays.asList("ord"),
                token.getLanguageCaseKeys()
        );

        token = new DataToken("{count::ordinal::ord}");
        Assert.assertEquals(
                Arrays.asList("ordinal", "ord"),
                token.getLanguageCaseKeys()
        );

        token = new DataToken("{count :: ordinal :: ord}");
        Assert.assertEquals(
                Arrays.asList("ordinal", "ord"),
                token.getLanguageCaseKeys()
        );

        token = new DataToken("{count :number ::ordinal ::ord}");
        Assert.assertEquals(
                Arrays.asList("number"),
                token.getLanguageContextKeys()
        );

        Assert.assertEquals(
                Arrays.asList("ordinal", "ord"),
                token.getLanguageCaseKeys()
        );

        Assert.assertEquals(
                Arrays.asList("ordinal", "ord"),
                token.getLanguageCaseKeys()
        );

        Assert.assertEquals(
                "{count}",
                token.getName(Utils.buildMap("parens", true))
        );

        Assert.assertEquals(
                "{count:number}",
                token.getName(Utils.buildMap("parens", true, "context_keys", true))
        );

        Assert.assertEquals(
                "{count::ordinal::ord}",
                token.getName(Utils.buildMap("parens", true, "case_keys", true))
        );

        Assert.assertEquals(
                "{count:number::ordinal::ord}",
                token.getName(Utils.buildMap("parens", true, "context_keys", true, "case_keys", true))
        );

        Assert.assertEquals(
                "count:number::ordinal::ord",
                token.getName(Utils.buildMap("parens", false, "context_keys", true, "case_keys", true))
        );
    }

    @Test
    public void testTokenValue() {
        DataToken token = new DataToken("{count}");
        Assert.assertEquals(
                "{count}",
                token.getValue(Utils.buildMap("user", "Michael"))
        );

        Assert.assertEquals(
                "1",
                token.getValue(Utils.buildMap("count", "1"))
        );

        Assert.assertEquals(
                "1",
                token.getValue(Utils.buildMap("count", 1))
        );

        Assert.assertEquals(
                "01",
                token.getValue(Utils.buildMap("count", Utils.buildList(1, "01")))
        );

        Assert.assertEquals(
                "Hello",
                token.getValue(Utils.buildMap("count", new DataTokenValue() {
                    public Object getContextObject() {
                        return 1;
                    }

                    public String getSubstitutionValue() {
                        return "Hello";
                    }
                }))
        );

        final User user = new User("Michael", "male");
        token = new DataToken("{user}");
        Assert.assertEquals(
                "Michael",
                token.getValue(Utils.buildMap("user", user))
        );

        token = new DataToken("{user}");
        Assert.assertEquals(
                "Michael",
                token.getValue(Utils.buildMap("user", new DataTokenValue() {
                    public Object getContextObject() {
                        return user;
                    }

                    public String getSubstitutionValue() {
                        return user.getFirstName();
                    }
                }))
        );

        Assert.assertEquals(
                "Berk",
                token.getValue(Utils.buildMap("user",  Utils.buildList(user, "Berk")))
        );

        Assert.assertEquals(
                "{user}",
                token.getValue(Utils.buildMap("user",  Utils.buildList(user)))
        );

        Assert.assertEquals(
                "Michael",
                token.getValue(Utils.buildMap("user",  Utils.buildMap(
                        "object", Utils.buildMap(
                            "name", "Michael",
                            "gender", "male"
                        ),
                        "attribute", "name"
                       )
                    )
                )
        );

        Assert.assertEquals(
                "Michael",
                token.getValue(Utils.buildMap("user",  Utils.buildMap(
                        "object", Utils.buildMap(
                            "name", "Michael",
                            "gender", "male"
                        ),
                        "property", "name"
                    )
                )
            )
        );

        Assert.assertEquals(
                "Mike",
                token.getValue(Utils.buildMap("user",  Utils.buildMap(
                        "object", Utils.buildMap(
                            "name", "Michael",
                            "gender", "male"
                        ),
                        "value", "Mike"
                    )
                )
            )
        );

        Assert.assertEquals(
                "{user}",
                token.getValue(Utils.buildMap("user",  Utils.buildMap(
                        "object", Utils.buildMap(
                           "name", "Michael",
                            "gender", "male"
                        )
                )
             )
           )
        );


        Assert.assertEquals(
                "{user}",
                token.getValue(Utils.buildMap("user",  Utils.buildMap(
                        "object", user,
                        "property", "name"
                )
             )
          )
        );

        Assert.assertEquals(
                "{user}",
                token.getValue(Utils.buildMap("user",  Utils.buildMap()))
        );

        token = new DataToken("{nbsp}");
        Assert.assertEquals(
                "nbsp",
                token.getValue(Utils.buildMap("nbsp", "nbsp"))
        );
        Assert.assertEquals(
                "&nbsp;",
                token.getValue(null)
        );
        Assert.assertEquals(
                "&nbsp;",
                token.getValue(Utils.buildMap("abc", "abc"))
        );
    }

    @Test
    public void testContextObject() {
        DataToken token = new DataToken("{count::ord}");

        Assert.assertEquals(
                null,
                token.getContextObject(Utils.buildMap())
        );

        Assert.assertEquals(
                null,
                token.getContextObject(null)
        );

        Assert.assertEquals(
                1,
                token.getContextObject(Utils.buildMap("count", 1))
        );

        Assert.assertEquals(
                10,
                token.getContextObject(Utils.buildMap("count", new DataTokenValue() {
                    public Object getContextObject() {
                        return 10;
                    }

                    public String getSubstitutionValue() {
                        return "10";
                    }
                }))
        );

        Assert.assertEquals(
                1,
                token.getContextObject(Utils.buildMap("count", Utils.buildList(1, 1)))
        );


        Assert.assertEquals(
                1,
                token.getContextObject(Utils.buildMap("count", Utils.buildMap("object", 1)))
        );

        Assert.assertEquals(
                Utils.buildMap("a","b"),
                token.getContextObject(Utils.buildMap("count", Utils.buildMap("a","b")))
        );

    }

}
