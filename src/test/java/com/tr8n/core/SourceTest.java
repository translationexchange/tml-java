package com.tr8n.core;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by michael on 3/15/14.
 */
public class SourceTest extends BaseTest {

    @Test
    public void testCreation() {
        Application app = new Application(loadJSONMap("/application.json"));

        Source source = new Source(Utils.buildMap(
                "application", app,
                "locale", "ru",
                "key", "key",
                "name", "name",
                "description", "description"
        ));

        Assert.assertEquals(
                "ru",
                source.getLocale()
        );

    }


    @Test
    public void testManualCreation() {
        Application app = new Application(loadJSONMap("/application.json"));

        Source source = new Source();
        source.setApplication(app);
        source.setDescription("desc");
        source.setLocale("ru");
        source.setKey("key");
        source.setName("name");

        Assert.assertEquals(
                "ru",
                source.getLocale()
        );

    }
}
