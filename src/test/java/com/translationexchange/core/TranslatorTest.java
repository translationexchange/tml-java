package com.translationexchange.core;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;


public class TranslatorTest {
    
    @Test
    public void testCreation() {
        Translator t = new Translator();
        Assert.assertTrue(t instanceof Translator);
        
        Translator translator = new Translator(Utils.map(
                "name", "rustem",
                "email", "r.kamun@gmail.com",
                "manager", null,
                "inline", null,
                "features", null));
        
        Assert.assertEquals("rustem", translator.getName());
        Assert.assertEquals("r.kamun@gmail.com", translator.getEmail());
        Assert.assertEquals(false, translator.isManager());
        Assert.assertEquals(false, translator.isInline());
        Assert.assertEquals(null, translator.getFeatures());

        // add later
        Assert.assertEquals(null, translator.getMugshot());
        Assert.assertEquals(null, translator.getGender());
        Assert.assertEquals(null, translator.getVotingPower());
        Assert.assertEquals(null, translator.getRank());
        Assert.assertEquals(null, translator.getLevel());
        Assert.assertEquals(null, translator.getLocale());
        Assert.assertEquals(null, translator.getCode());
        Assert.assertEquals(null, translator.getAccessToken());
        
        // features
        translator.setFeatures((Map) Utils.map());
        Assert.assertEquals(Utils.map(), translator.getFeatures());
        Assert.assertEquals(false, translator.isFeatureEnabled("f2"));
        translator.setFeatures((Map) Utils.map("f1", false, "f2", true));
        Assert.assertEquals(true, translator.isFeatureEnabled("f2"));
        Assert.assertEquals(false, translator.isFeatureEnabled("f1"));
        
        // setters
        translator.setName("Rustem");
        translator.setEmail("rustem@toptal.com");
        translator.setLocale("ru");
        translator.setCode("0012312312");
        translator.setLevel(new Long(11));
        translator.setRank(new Long(1));
        translator.setVotingPower(new Long(1));
        translator.setGender("male");
        translator.setMugshot("no mugshot");
        translator.setAccessToken("");
        
        // boolean setters/getters
        translator.setManager(true);
        Assert.assertTrue(translator.isManager());
        translator.setInline(true);
        Assert.assertTrue(translator.isInline());
        
        
    }
}
