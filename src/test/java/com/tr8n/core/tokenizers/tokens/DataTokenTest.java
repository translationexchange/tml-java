package com.tr8n.core.tokenizers.tokens;

import com.tr8n.core.Utils;
import com.tr8n.core.models.User;
import com.tr8n.core.tokenizers.DataTokenValue;
import com.tr8n.core.tokenizers.tokens.BaseToken;
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
    public void testGettingTokenValue() {
        /**
         * Returns a value from values hash.
         *
         * Token objects can be passed using any of the following forms:
         *
         * - if an object is passed without a substitution value, it will use toString() to get the value:
         *
         *     Tr8n.translate("Hello {user}", Utils.buildMap("user", current_user))
         *     Tr8n.translate("{count||message}", Utils.buildMap("count", counter))
         *
         * - if an object is a list, the first value is the context object, the second value is the substitution value:
         *
         *     Tr8n.translate("Hello {user}", Utils.buildMap("user", Utils.buildList(current_user, "Michael")))
         *     Tr8n.translate("Hello {user}", Utils.buildMap("user", Utils.buildList(current_user, current_user.name)))
         *
         * - if an object is a map (mostly used for JSON), it must provide the object and the value/attribute for substitution:
         *
         *     Tr8n.translate("Hello {user}", Utils.buildMap("user", Utils.buildMap(
         *                                       "object", Utils.buildMap(
         *                                           "name", "Michael",
         *                                           "gender", "male"
         *                                       ),
         *                                       "value", "Michael"
         *                                    ))
         *
         *     Tr8n.translate("Hello {user}", Utils.buildMap("user", Utils.buildMap(
         *                                       "object", Utils.buildMap(
         *                                           "name", "Michael",
         *                                           "gender", "male"
         *                                       ),
         *                                       "attribute", "name"
         *                                    ))
         *
         * - if you don't need the substitution, you can provide an object directly:
         *
         *     Tr8n.translate("{user| He, She}", Utils.buildMap("user", Utils.buildMap(
         *                                           "name", "Michael",
         *                                           "gender", "male"
         *                                       ))
         *
         * - the most explicit way is to use the DataTokenValue interface:
         *
         *     Tr8n.translate("Hello {user}", Utils.buildMap("user", new DataTokenValue() {
         *                                        public Object getContextObject() {
         *                                           return user;
         *                                        }
         *                                        public String getSubstitutionValue() {
         *                                           return user.getName();
         *                                        }
         *                                    }))
         *
         */

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

        

    }

}
