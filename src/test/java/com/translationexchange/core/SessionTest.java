package com.translationexchange.core;

import static org.mockito.Mockito.mock;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;


public class SessionTest extends BaseTest {
    
    @Test
    public void testCreation() {
        Map<String, Object> options = new HashMap<String, Object>(Tml.getConfig().getApplication());
        options.put("applicationClass", "com.translationexchange.core.dummy.DummyApplication");
        Session s = new Session(options);
        Assert.assertTrue(s instanceof Session);
        Assert.assertTrue(Observable.class.isAssignableFrom(s.getClass()));
    }
}
