package com.tr8n.core;

import com.tr8n.core.rulesengine.Variable;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

public class ConfigurationTest {

    @Test
    public void testRulesEngineVariables() {
        Configuration config = new Configuration();

        Variable var = config.getContextVariable("number", "@n");
        Assert.assertEquals(
                1,
                var.getValue(null, 1)
        );

        var = config.getContextVariable("gender", "@gender");
        Assert.assertEquals(
                "male",
                var.getValue(null, Utils.buildMap("gender", "male"))
        );

        var = config.getContextVariable("gender", "@gender");
        Assert.assertEquals(
                "female",
                var.getValue(null, Utils.buildMap("object", Utils.buildMap("gender", "female", "name", "Michael"), "attribute", "name"))
        );

        var = config.getContextVariable("genders", "@genders");
        Assert.assertEquals(
                Utils.buildList("male"),
                var.getValue(null, Utils.buildMap("gender", "male"))
        );

        Date today = new Date();

        var = config.getContextVariable("date", "@date");
        Assert.assertEquals(
                today,
                var.getValue(null, today)
        );

        var = config.getContextVariable("list", "@count");
        Assert.assertEquals(
                1,
                var.getValue(null, Utils.buildList("one"))
        );

        Assert.assertEquals(
                2,
                var.getValue(null, Utils.buildList("one", "two"))
        );

        Assert.assertEquals(
                1,
                var.getValue(null, "one")
        );
    }

    @Test
    public void testDefaultTokenValues() {
        Configuration config = new Configuration();

        Assert.assertEquals(
                "&nbsp;",
                config.getDefaultTokenValue("nbsp")
        );

        Assert.assertEquals(
                "&nbsp;",
                config.getDefaultTokenValue("nbsp", "data")
        );

        Assert.assertEquals(
                "&nbsp;",
                config.getDefaultTokenValue("nbsp", "data", "html")
        );

        Assert.assertEquals(
                " ",
                config.getDefaultTokenValue("nbsp", "data", "text")
        );

        Assert.assertEquals(
                "<strong>{$0}</strong>",
                config.getDefaultTokenValue("strong", "decoration", "html")
        );

        config.addDefaultTokenValue("test", "data", "html", "hello world");

        Assert.assertEquals(
                "hello world",
                config.getDefaultTokenValue("test", "data", "html")
        );

    }
}
