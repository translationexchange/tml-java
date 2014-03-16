package com.tr8n.core;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by michael on 3/10/14.
 */
public class ApplicationTest extends BaseTest {

    @Test
    public void testCreatingApplication() {
        Application app = new Application(loadJSONMap("/application.json"));

        Assert.assertEquals(
                "Tr8n Translation Service",
                app.getName()
        );

        Assert.assertEquals(
                null,
                app.getHost()
        );

        Assert.assertEquals(
                "default",
                app.getKey()
        );

        Assert.assertEquals(
                "",
                app.getDescription()
        );

        Assert.assertEquals(
                Utils.buildMap(
                        "data", Utils.buildMap(
                            "nbsp", "&nbsp;"
                        ),
                        "decoration", Utils.buildMap(
                            "link", "<a href=\"{$href}\">{$0}</a>",
                            "strong", "<strong>{$0}</strong>"
                        )
                ),
                app.getTokens()
        );

        Assert.assertEquals(
                new Long(1),
                app.getThreshold()
        );

        Assert.assertEquals(
                new Long(1),
                app.getTranslatorLevel()
        );

        Assert.assertEquals(
                "en-US",
                app.getDefaultLocale()
        );

        Assert.assertEquals(
                null,
                app.getCss()
        );

        Assert.assertEquals(
                null,
                app.getShortcuts()
        );

        Assert.assertEquals(
                13,
                app.getFeatures().size()
        );

        Assert.assertEquals(
                null,
                app.getSources()
        );

        Assert.assertEquals(
                null,
                app.getComponents()
        );

        Assert.assertEquals(
                null,
                app.getFeaturedLocales()
        );

        Assert.assertEquals(
                11,
                app.getLanguages().size()
        );

    }
}
