package com.translationexchange.core;

import static org.mockito.Mockito.mock;

import java.util.Observable;

import org.junit.Assert;
import org.junit.Test;
import org.junit.BeforeClass;
import org.junit.AfterClass;


public class SessionTest extends BaseTest {
    
    @BeforeClass
    public static void configureTml() {
        Tml.getConfig().setApplicationClass("com.translationexchange.core.dummy.DummyApplication");
    }
    
    @Test
    public void testCreation() {
        Session s = new Session();
        Assert.assertTrue(s instanceof Session);
        Assert.assertTrue(Observable.class.isAssignableFrom(s.getClass()));
    }
    
    @AfterClass
    public static void deconfigureTml() {
        Tml.getConfig().setApplicationClass("com.translationexchange.core.Application");
    }
    
}
