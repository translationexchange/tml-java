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
        String htmlString = "<html><head><script id=\"script\" type=\"src\"></script></head><body><h1 id=\"test\">Mr. Belvedere Fan Club</h1><i></i><div id=\"no-translate-1\" notranslate></div><div tmltype=\"notranslate\" id=\"no-translate-2\"></div><b class=\"mapped-tag\">dasd</b><p class=\"empty\"></p><p><br class=\"self-closing\"/><span id=\"test-span\">Hmm</span></p><div><i class=\"only-child\"></i></div></body></html>";
        Document doc = Jsoup.parse(htmlString);
        Element root = doc.select("html").first();
        Element h1 = doc.getElementById("test");
        Element p = doc.getElementsByClass("empty").first();
        Element span = doc.select("span#test-span").first();
        Element ic = doc.select("i.only-child").first();
        Element br = doc.select("br.self-closing").first();
        Element b = doc.select("b.mapped-tag").first();
        Element notransNode1 = doc.getElementById("no-translate-1");
        Element notransNode2 = doc.getElementById("no-translate-2");
        Element script = doc.getElementById("script");
        
        Method isValidText = Utils.getPrivateMethod(dt, "isValidText", Element.class);
        Assert.assertTrue((Boolean) isValidText.invoke(dt, h1));
        Assert.assertFalse((Boolean) isValidText.invoke(dt, p));
        
        Method isInlineNode = Utils.getPrivateMethod(dt, "isInlineNode", Element.class);
        Assert.assertTrue((Boolean) isInlineNode.invoke(dt, span));
        Assert.assertFalse((Boolean) isInlineNode.invoke(dt, p));
        
        Method isContainerNode = Utils.getPrivateMethod(dt, "isContainerNode", Element.class);
        Assert.assertFalse((Boolean) isContainerNode.invoke(dt, span));
        Assert.assertTrue((Boolean) isContainerNode.invoke(dt, p));
        
        Method isSelfClosingNode = Utils.getPrivateMethod(dt, "isSelfClosingNode", Element.class);
        Assert.assertTrue((Boolean) isSelfClosingNode.invoke(dt, br));
        Assert.assertFalse((Boolean) isSelfClosingNode.invoke(dt, span));
        
        Method isIgnoredNode = Utils.getPrivateMethod(dt, "isIgnoredNode", Element.class);
        Assert.assertFalse((Boolean) isIgnoredNode.invoke(dt, span));
        
        Method isSeparatorNode = Utils.getPrivateMethod(dt, "isSeparatorNode", Element.class);
        Assert.assertFalse((Boolean) isSeparatorNode.invoke(dt, span));
        Assert.assertTrue((Boolean) isSeparatorNode.invoke(dt, br));
        
        Method sanitizeValue = Utils.getPrivateMethod(dt, "sanitizeValue", String.class);
        Assert.assertEquals("Hello world", sanitizeValue.invoke(dt, "    Hello world"));
        
        Method adjustName = Utils.getPrivateMethod(dt, "adjustName", Element.class);
        Assert.assertEquals("bold", adjustName.invoke(dt, b));
        Assert.assertEquals(span.tagName().toLowerCase(), adjustName.invoke(dt, span));
        
        Method hasInlineOrTextSublings = Utils.getPrivateMethod(dt, "hasInlineOrTextSublings", Element.class);
        Assert.assertFalse((Boolean) hasInlineOrTextSublings.invoke(dt, root));
        Assert.assertFalse((Boolean) hasInlineOrTextSublings.invoke(dt, span));
        Assert.assertTrue((Boolean) hasInlineOrTextSublings.invoke(dt, p));
        
        Method isOnlyChild = Utils.getPrivateMethod(dt, "isOnlyChild", Element.class);
        Assert.assertFalse((Boolean) isOnlyChild.invoke(dt, p));
        Assert.assertTrue((Boolean) isOnlyChild.invoke(dt, ic));
        
        Method isContainInProperty = Utils.getPrivateMethod(dt, "isContainInProperty", Element.class, String.class);
        Assert.assertFalse((Boolean) isContainInProperty.invoke(dt, p, "notranslate"));
        Assert.assertTrue((Boolean) isContainInProperty.invoke(dt, notransNode1, "notranslate"));
        Assert.assertTrue((Boolean) isContainInProperty.invoke(dt, notransNode2, "notranslate"));
        
        Method isNonTranslatableNode = Utils.getPrivateMethod(dt, "isNonTranslatableNode", Element.class);
        Assert.assertTrue((Boolean) isNonTranslatableNode.invoke(dt, notransNode2));
        Assert.assertTrue((Boolean) isNonTranslatableNode.invoke(dt, script));
        Assert.assertTrue((Boolean) isNonTranslatableNode.invoke(dt, p));
    }
}
