package com.translationexchange.core.tokenizers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import org.apache.tools.ant.DirectoryScanner;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.junit.Assert;
import org.junit.Test;

import com.translationexchange.core.BaseTest;
import com.translationexchange.core.Tml;
import com.translationexchange.core.Utils;
import com.translationexchange.core.tokenizers.DomTokenizer;

public class DomTokenizerTest extends BaseTest {
    
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
        
        Method contextualize = Utils.getPrivateMethod(dt, "contextualize", String.class, String.class);
        Assert.assertEquals("key", contextualize.invoke(dt, "key", "value1"));
        Assert.assertEquals("key1", contextualize.invoke(dt, "key", "value2"));
        Assert.assertEquals("key1", contextualize.invoke(dt, "key1", "value2"));
        Assert.assertEquals("key2", contextualize.invoke(dt, "key1", "value3"));
        
        Method generateDataTokens = Utils.getPrivateMethod(dt, "generateDataTokens", String.class);
        Assert.assertEquals("{a} {ab}{abcd}", generateDataTokens.invoke(dt, "&a; &ab;&abcd;"));
        
        Assert.assertEquals("{date}", generateDataTokens.invoke(dt, "January 15, 2017"));
        Assert.assertEquals("{date1}  {date2}", generateDataTokens.invoke(dt, "Jan 15, 2017  18 Oct, 2019"));
        Assert.assertEquals("{date3}", generateDataTokens.invoke(dt, "Jan 12, 2017"));
        
        Assert.assertEquals("{time} {phone} {email}", generateDataTokens.invoke(dt, "14:05 am 777-295-2190 r.kamun@toptal.com"));
        
    }
    
    @Test
    public void testJSoupSpec() throws Exception {
        DomTokenizer dt = new DomTokenizer(Utils.buildMap(), Tml.getConfig().getTranslatorOptions());
        String htmlString = "<html><head><script id=\"script\" type=\"src\"></script></head><body><h1 id=\"test\">Mr. Belvedere Fan Club</h1><i></i><div id=\"no-translate-1\" notranslate></div><div tmltype=\"notranslate\" id=\"no-translate-2\"></div><b class=\"mapped-tag\">dasd</b><p class=\"empty\"></p><p><br class=\"self-closing\"/><span id=\"test-span\">Hmm</span></p><div id=\"with-child\"><i class=\"only-child\"></i></div></body></html>";
        Document doc = Jsoup.parse(htmlString);
        Element root = doc.select("html").first();
        Element h1 = doc.getElementById("test");
        Element div = doc.getElementById("with-child");
        Element p = doc.getElementsByClass("empty").first();
        Element span = doc.select("span#test-span").first();
        Element ic = doc.select("i.only-child").first();
        Element br = doc.select("br.self-closing").first();
        Element b = doc.select("b.mapped-tag").first();
        Element notransNode1 = doc.getElementById("no-translate-1");
        Element notransNode2 = doc.getElementById("no-translate-2");
        Element script = doc.getElementById("script");
        
        Method isValidText = Utils.getPrivateMethod(dt, "isValidText", Node.class);
        Assert.assertTrue((Boolean) isValidText.invoke(dt, h1.textNodes().get(0)));
//        Assert.assertFalse((Boolean) isValidText.invoke(dt, h1.));
        
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
        
        Method generateHtmlToken = Utils.getPrivateMethod(dt, "generateHtmlToken", Element.class, String.class);
        Assert.assertEquals("<h1 id=\"test\">{$0}</h1>", generateHtmlToken.invoke(dt, h1, null));
        Assert.assertEquals("<h1 id=\"test\">hi</h1>", generateHtmlToken.invoke(dt, h1, "hi"));
        Assert.assertEquals("<br class=\"self-closing\"/>", generateHtmlToken.invoke(dt, br, null));
        
        Method generateTmlTags = Utils.getPrivateMethod(dt, "generateTmlTags", Element.class);
        Assert.assertEquals("[h1]Mr. Belvedere Fan Club[/h1]", generateTmlTags.invoke(dt, h1));
        Assert.assertEquals("[div: {italic}]", generateTmlTags.invoke(dt, div));
        div.child(0).text("hello");
        Assert.assertEquals("[div: [italic1: hello]]", generateTmlTags.invoke(dt, div));
    }
    
    @Test
    public void testIntegration() {
        Map<String, Object> transOptions = Utils.extendMap(Tml.getConfig().getTranslatorOptions(),
                        "debug", true,
                        "debug_format", "{{{{$0}}}}");
        DomTokenizer dt = new DomTokenizer(Utils.buildMap(), transOptions);
        Assert.assertEquals(
                "<h1>{{{Mr. Belvedere Fan Club}}}</h1>",
                dt.translate("<html><body><h1>Mr. Belvedere Fan Club</h1></body></html>"));
        Assert.assertEquals(
                "{{{Mr. Belvedere Fan Club}}}",
                dt.translate("Mr. Belvedere Fan Club"));
        Assert.assertEquals(
                "<h1>{{{Mr. Belvedere Fan Club}}}</h1>",
                dt.translate("<h1>Mr. Belvedere Fan Club</h1>"));
//        Assert.assertEquals(
//                "<p><a class='the-link' href='https://github.com/tmpvar/jsdom'>{{{jsdom's Homepage}}}</a></p>",
//                dt.translate("<p><a class='the-link' href='https://github.com/tmpvar/jsdom'>jsdom's Homepage</a></p>"));
//        
        DirectoryScanner scanner = new DirectoryScanner();
        scanner.setIncludes(new String[]{"dom/**/*.html"});
        scanner.setBasedir("src/test/resources");
        scanner.scan();
        String[] files = scanner.getIncludedFiles();
        for(String file : files) {
            String actual = loadResource("/" + file);
            String expected = loadResource("/" + file.replace("html", "tml"));
            Assert.assertEquals(expected, dt.translate(actual));
        }
    }
}
