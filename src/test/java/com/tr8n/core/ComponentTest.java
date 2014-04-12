package com.tr8n.core;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by michael on 3/15/14.
 */
public class ComponentTest extends BaseTest {
      
    @Test
    public void testCreation() {
        Application app = new Application(loadJSONMap("/application.json"));

        Component comp = new Component(Utils.buildMap(
                "application", app,
                "key", "key",
                "name", "name",
                "description", "description",
                "state", "state"
        ));

        Assert.assertEquals(
                "name",
                comp.getName()
        );

        Assert.assertEquals(
                "key",
                comp.getKey()
        );

        Assert.assertEquals(
                "description",
                comp.getDescription()
        );

        Assert.assertEquals(
                "state",
                comp.getState()
        );
        
        comp = new Component(Utils.buildMap(
                "key", "key",
                "name", "name",
                "description", "description",
                "state", "state"
        ));

        Assert.assertNull(
                comp.getApplication()
        );
        
    }


    @Test
    public void testManualCreation() {
        Application app = new Application(loadJSONMap("/application.json"));

        Component comp = new Component();
        comp.setApplication(app);
        comp.setDescription("desc");
        comp.setKey("key");
        comp.setName("name");

        Assert.assertEquals(
                "name",
                comp.getName()
        );

    }
}
