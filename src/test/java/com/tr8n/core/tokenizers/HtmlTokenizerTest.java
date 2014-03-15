package com.tr8n.core.tokenizers;

import com.tr8n.core.Tr8n;
import com.tr8n.core.Utils;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by michael on 3/14/14.
 */
public class HtmlTokenizerTest {

    @Test
    public void testSubstitution() {
        HtmlTokenizer dt = new HtmlTokenizer("Hello [bold: World]");
        Assert.assertEquals(
                "Hello <strong>World</strong>",
                dt.substitute(Utils.buildMap("bold", "<strong>{$0}</strong>"))
        );

        Assert.assertEquals(
                "Hello <b>World</b>",
                dt.substitute(Utils.buildMap("bold", new DecorationTokenValue() {
                    public String getSubstitutionValue(String text) {
                        return "<b>" + text + "</b>";
                    }
                }))
        );

        // using default decorators
        Assert.assertEquals(
                "Hello <strong>World</strong>",
                dt.substitute(Utils.buildMap())
        );

        Assert.assertEquals(
                "Hello <strong>World</strong>",
                dt.substitute(null)
        );

        Tr8n.getConfig().addDefaultTokenValue("link", "decoration", "html", "<a href=\"{$href}\">{$0}</a>");

        dt.tokenize("[link]you have {count} messages[/link]");
        Assert.assertEquals(
            "<a href=\"www.google.com\">you have {count} messages</a>",
            dt.substitute(Utils.buildMap("link", Utils.buildMap("href", "www.google.com")))
        );

        dt.tokenize("[link]you have [italic: [bold: {count}] messages] [light: in your mailbox][/link]");
        Assert.assertEquals(
                "<a href=\"www.google.com\">you have <i><b>{count}</b> messages</i> <l>in your mailbox</l></a>",
                dt.substitute(Utils.buildMap(
                        "link", Utils.buildMap("href", "www.google.com"),
                        "italic", "<i>{$0}</i>",
                        "bold", "<b>{$0}</b>",
                        "light", "<l>{$0}</l>"
                ))
        );


        dt.tokenize("[custom: test]");
        Assert.assertEquals(
                "test",
                dt.substitute(null)
        );

    }
}
