package com.translationexchange.core.tokenizers;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.jsoup.nodes.Element;

import com.translationexchange.core.Utils;

public class DomTokenizer {
    /**
     * Tokens data map: would be auto-generated
    */
    private Map<String, Object> tokensData;
    
    /**
     * Context: default token data map
    */
    private Map<String, Object> context;
    
    /**
     * Substitution options, used internally
     */
    private Map<String, Object> options;
    
    /**
     * Default constructor
     */
    public DomTokenizer() {
        this(Utils.buildMap(), Utils.buildMap());
    }
    
    /**
     * Constructor with some context
     * 
     * @param context
     */
    
    public DomTokenizer(Map<String, Object> context) {
        this(context, Utils.buildMap());
    }

    /**
     * Constructs Base with label
     *
     * @param context
     * @param options
     */
    public DomTokenizer(Map<String, Object> context, Map<String, Object> options) {
        this.context = context;
        this.options = options;
        this.resetContext();
    }

    private void resetContext() {
        Map<String, Object> map = Utils.buildMap();
        map.putAll(this.context);
        this.tokensData = map;
    }
    
    private boolean isEmptyString(String tmlString) {
        Pattern pattern = Pattern.compile("[\\s\\n\\r\\t]");
        return pattern.matcher(tmlString).replaceAll("").equals("");
    }
    
    private Object option(String key) {
        return Utils.getNestedMapValue(this.options, key);
    }
    
    private String debugTranslation(String tmlString) {
        String format = (String) this.option("debug_format");
        return format.replace("{$0}", tmlString);
    }
    
    private boolean isShortToken(String token, String value) {
        List<String> l = (List) this.option("nodes.short");
        if(l.indexOf(token) > -1) {
            return true;
        }
        return value.length() < 20;
    }
    
    private boolean isOnlyChild(Element node) {
        if(node.parent() == null) {
            return false;
        }
        return node.parent().children().size() == 1;
    }
    
    private boolean hasInlineOrTextSublings(Element node) {
        if(node.parent() == null) {
            return false;
        }
        Iterator<Element> elements = node.parent().children().iterator();
        while(elements.hasNext()) {
            Element childEl = elements.next();
            if(childEl == node) {
                continue;
            }
            if(this.isInlineNode(childEl)) {
                return true;
            }
        }
        if(this.isValidText(node.parent())) {
            return true;
        }
        return false;
    }
    
    private boolean isInlineNode(Element node) {
        List<String> inlineNodes = (List) this.option("nodes.inline");
        return inlineNodes.indexOf(node.tagName().toLowerCase()) > -1 && !this.isOnlyChild(node);
    }
    
    private boolean isValidText(Element node) {
        String text = node.ownText();
        if(text.equals("")) {
            return false;
        }
        return !this.isEmptyString(text);
    }
}
