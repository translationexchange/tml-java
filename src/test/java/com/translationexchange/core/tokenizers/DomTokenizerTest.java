package com.translationexchange.core.tokenizers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Assert;
import org.junit.Test;

import com.translationexchange.core.Tml;
import com.translationexchange.core.Utils;
import com.translationexchange.core.tokenizers.DomTokenizer;

public class DomTokenizerTest {
    
    @Test
    public void testSpec() throws Exception {
        DomTokenizer dt = new DomTokenizer(Utils.buildMap(), Tml.getConfig().getTranslatorOptions());
        
        Method isEmptyString = Utils.getPrivateMethod(dt, "isEmptyString", String.class);
        Assert.assertTrue((Boolean) isEmptyString.invoke(dt, "            \n\n"));
        
        Method option = Utils.getPrivateMethod(dt, "option", String.class);
        Assert.assertTrue((Boolean) option.invoke(dt, "data_tokens.date.enabled"));
        Assert.assertEquals(Utils.buildList("style", "script", "code", "pre"), option.invoke(dt, "nodes.scripts"));
        
        Method debugTranslation = Utils.getPrivateMethod(dt, "debugTranslation", String.class);
        Assert.assertEquals("{{{Hello}}}", debugTranslation.invoke(dt, "Hello"));
        
        Method isShortToken = Utils.getPrivateMethod(dt, "isShortToken", String.class, String.class);
        Assert.assertTrue((Boolean) isShortToken.invoke(dt, "i", "hello"));
        Assert.assertTrue((Boolean) isShortToken.invoke(dt, "b", "hi"));
        Assert.assertFalse((Boolean) isShortToken.invoke(dt, "unknown", "abracadabraabracadabra"));
    }
    
    @Test
    public void testJSoupSpec() throws Exception {
        DomTokenizer dt = new DomTokenizer(Utils.buildMap(), Tml.getConfig().getTranslatorOptions());
        String htmlString = "<html><body><h1 id=\"test\">Mr. Belvedere Fan Club</h1><p class=\"empty\"></p><p><br/><span id=\"test-span\">Hmm</span></p></body></html>";
        Document doc = Jsoup.parse(htmlString);
        
        Element h1 = doc.getElementById("test");
        Element p = doc.select("p.empty").first();
        Element span = doc.select("span#test-span").first();
        
        Method isValidText = Utils.getPrivateMethod(dt, "isValidText", Element.class);
        Assert.assertTrue((Boolean) isValidText.invoke(dt, h1));
        Assert.assertFalse((Boolean) isValidText.invoke(dt, p));
        
        Method isInlineNode = Utils.getPrivateMethod(dt, "isInlineNode", Element.class);
        Assert.assertTrue((Boolean) isInlineNode.invoke(dt, span));
        Assert.assertFalse((Boolean) isInlineNode.invoke(dt, p));
    }
}
