package com.translationexchange.core.tokenizers;

import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import com.translationexchange.core.Tml;
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
        this(Utils.map(), Utils.map());
    }
    
    /**
     * Constructor with some context
     * 
     * @param context
     */
    
    public DomTokenizer(Map<String, Object> context) {
        this(context, Utils.map());
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
        Map<String, Object> map = Utils.map();
        map.putAll(this.context);
        this.tokensData = map;
    }
    
    public String translate(String htmlString) {
        Document doc = Jsoup.parse(htmlString);
        return translateTree(doc.body());
    }
    
    public String translateTree(Node node) {
        if(node instanceof Element) {
            Element _node = (Element) node;
            if(isNonTranslatableNode(_node)) {
                return (_node).html();
            }
        }
        if(node instanceof TextNode) {
            TextNode _node = (TextNode) node;
            return translateTml(_node.text());
        } else {
            String html = "";
            String buffer = "";
            for(Node child : node.childNodes()) {
                if(child instanceof TextNode) {
                    buffer += ((TextNode) child).text();
                } else if(child instanceof Element) {
                    Element _child = (Element) child;
                    if(isInlineNode(_child) && hasInlineOrTextSublings(_child) && !isBetweenSeparators(_child)) {
                        buffer += generateTmlTags(_child);
                    } else if(isSeparatorNode(_child)) {
                        if(!buffer.equals("")) {
                            html += translateTml(buffer);
                        }
                        html += generateHtmlToken(_child, null);
                        buffer = "";
                    } else {
                        if(!buffer.equals("")) {
                            html += translateTml(buffer);
                        }
                        
                        String containerValue = translateTree(_child);
                        if(isIgnoredNode(_child)) {
                            html += containerValue;
                        } else {
                            html += generateHtmlToken(_child, containerValue);
                        }
                        buffer = "";
                    }
                }
            }
            if(!buffer.equals("")) {
                html += translateTml(buffer);
            }
            return html;
        }
    }
    
    private boolean isBetweenSeparators(Node node) {
        Node prev = node.previousSibling();
        Node next = node.nextSibling();
        if(prev instanceof Element && isSeparatorNode((Element) prev) && !isValidText(next)) {
            return true;
        }
        if(next instanceof Element && isSeparatorNode((Element) next) && !isValidText(prev)) {
            return true;
        }
        return false;
    }
    
    @SuppressWarnings("unchecked")
	private boolean isNonTranslatableNode(Element node) {
        List<String> scripts = (List<String>) option("nodes.scripts");
        if(scripts.indexOf(node.tagName().toLowerCase()) > -1) {
            return true;
        }
        if(node.childNodeSize() == 0 && node.ownText().equals("")) {
            return true;
        }
        if(isContainInProperty(node, "notranslate")) {
            return true;
        }
        return false;
    }
    
    private boolean isContainInProperty(Element node, String word) {
        for(Attribute attr : node.attributes()) {
            if(attr.getKey().equals(word)) {
                return true;
            }
            if(attr.getValue().contains(word)) {
                return true;
            }
        }
        return false;
    }
    
    public String translateTml(String tml) {
        if(isEmptyString(tml)) {
            return tml;
        }
        tml = generateDataTokens(tml);
        if((Boolean) option("split_sentences")) {
            List<String> sentences = Utils.splitSentences(tml);
            String translation = tml;
            for(String sent : sentences) {
                String sentTrans = "";
                if((Boolean) option("debug")) {
                    sentTrans = debugTranslation(tml);
                } else {
                    sentTrans = (String) Tml.getCurrentLanguage().translate(sent, tokensData);
                }
                translation = translation.replaceAll(sent, sentTrans);
            }
            resetContext();
            return translation;
        }
        String translation = "";
        final Map<String, String> sanitizers = Utils.buildStringMap(
                "[\n]", "",
                "[\\s\\s+]", " ");
        for(Map.Entry<String, String> entry : sanitizers.entrySet()) {
            tml = Pattern.compile(entry.getKey()).matcher(tml).replaceAll(entry.getValue());
        }
        tml = tml.trim();
        if((Boolean) option("debug")) {
            translation = debugTranslation(tml);
        } else {
            translation = (String) Tml.getCurrentLanguage().translate(tml, tokensData);
        }
        resetContext();
        return translation;
    }
    
    private String generateTmlTags(Element node) {
        String buf = "";
        for(Node childNode : node.childNodes()) {
            if(childNode instanceof TextNode) {
                buf += ((TextNode) childNode).text();
            } else if(childNode instanceof Element) {
                buf += generateTmlTags((Element) childNode);
            }
        }
        String tokenContext = generateHtmlToken(node, null);
        String token = contextualize(adjustName(node), tokenContext);
        String value = sanitizeValue(buf);
        if(isSelfClosingNode(node)) {
            return String.format("{%s}", token);
        }
        if(isShortToken(token, value)) {
            return String.format("[%s: %s]", token, value);
        }
        return String.format("[%s]%s[/%s]", token, value, token);
    }
    
    private String generateHtmlToken(Element node, String nodeValue) {
        String nodeName = node.tagName().toLowerCase();
        Map<String, String> attributeMap = Utils.buildStringMap();
        nodeValue = (nodeValue == null || nodeValue.equals("")) ? "{$0}" : nodeValue;
        if(node.attributes().size() == 0) {
            if(isSelfClosingNode(node)) {
                if(isSeparatorNode(node)) {
                    return String.format("<%s/>", nodeName);
                }else {
                    return String.format("<%s></%s>", nodeName, nodeName);
                }
            }
            return String.format("<%s>%s</%s>", nodeName, nodeValue, nodeName);
        }
        for(Attribute attr : node.attributes()) {
            attributeMap.put(attr.getKey(), attr.getValue());
        }
        SortedSet<String> attrKeys = new TreeSet<String>(attributeMap.keySet());
        StringBuilder attrBuilder = new StringBuilder();
        String sep = "";
        for(String key : attrKeys) {
            String quote = attributeMap.get(key).indexOf("\'") > -1 ? "\'" : "\"";
            attrBuilder
            .append(sep)
            .append(String.format("%s=%s", key, (quote + attributeMap.get(key) + quote)));
            sep = " ";
        }
        String attrString = attrBuilder.toString();
        if(isSelfClosingNode(node)) {
            if(isSeparatorNode(node)) {
                return String.format("<%s %s/>", nodeName, attrString);
            } else {
                return String.format("<%s %s></%s>", nodeName, attrString, nodeName);
            }
        }
        return String.format("<%s %s>%s</%s>", nodeName, attrString, nodeValue, nodeName);
    }
    
    @SuppressWarnings("unchecked")
	private String generateDataTokens(String text) {
        if((Boolean) option("data_tokens.special.enabled")) {
            Matcher specialMatcher = Pattern.compile((String) option("data_tokens.special.regex")).matcher(text);
            while(specialMatcher.find()) {   // &dasd;
                String matched = specialMatcher.group();
                String token = matched.substring(1, matched.length() - 1);
                context.put(token, matched);
                text = text.replaceAll(matched, String.format("{%s}", token));
            }
        }
        
        if((Boolean) option("data_tokens.date.enabled")) {
            String tokenName = (String) option("data_tokens.date.name");
            List<List<String>> formats = (List<List<String>>) option("data_tokens.date.formats");
            for(List<String> fmt : formats) {
                Matcher dateMatcher = Pattern.compile(fmt.get(0)).matcher(text);
                while(dateMatcher.find()) {
                    String matched = dateMatcher.group();
                    if(matched.equals(""))
                        continue;
                    String date = dateMatcher.group(0);
                    String token = contextualize(tokenName, date);
                    text = text.replaceAll(date, String.format("{%s}", token));
                }
            }
        }
        List<Map<String, Object>> rules = (List<Map<String, Object>>) option("data_tokens.rules");
        if(rules != null && rules.size() > 0) {
            for(Map<String, Object> rule : rules) {
                if(!((Boolean)rule.get("enabled"))) {
                    continue;
                }
                Matcher ruleMatcher = Pattern.compile((String) rule.get("regex")).matcher(text);
                while(ruleMatcher.find()) {
                    String matched = ruleMatcher.group();
                    if(matched.equals(""))
                        continue;
                     String value = ruleMatcher.group(0);
                     if(value.equals(""))
                         continue;
                     String token = contextualize((String) rule.get("name"), sanitizeValue(value));
                     text = text.replaceAll(value, value.replace(value, String.format("{%s}", token)));
                }
            }
        }
        return text;
    }
    
    private String contextualize(String name, String context) {
        if(this.tokensData.containsKey(name) && !this.tokensData.get(name).equals(context)) {
            int index = 0;
            Matcher matches = Pattern.compile("\\d+$").matcher(name);
            if(matches.find()) {
                index = Integer.parseInt(matches.group());
                name = name.replace(index + "", "");
            }
            name += (index + 1) + "";
            
            return contextualize(name, context);
        }
        this.tokensData.put(name, context);
        return name;
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
    
    @SuppressWarnings("unchecked")
	private boolean isShortToken(String token, String value) {
        List<String> l = (List<String>) this.option("nodes.short");
        if(l.indexOf(token) > -1) {
            return true;
        }
        return value.length() < 20;
    }
    
    private boolean isOnlyChild(Element node) {
        if(node.parent() == null) {
            return false;
        }
        return node.parent().childNodeSize() == 1;
    }
    
    private boolean hasInlineOrTextSublings(Element node) {
        if(node.parent() == null) {
            return false;
        }
        for(Node child : node.parentNode().childNodes()) {
            if(child == node) {
                continue;
            }
            if(child instanceof Element && isInlineNode((Element) child)) {
                return true;
            }
            if(isValidText(child)) {
                return true;
            }
        }
        return false;
    }
    
    @SuppressWarnings("unchecked")
	private boolean isInlineNode(Element node) {
        List<String> inlineNodes = (List<String>) this.option("nodes.inline");
        return inlineNodes.indexOf(node.tagName().toLowerCase()) > -1 && !this.isOnlyChild(node);
    }
    
    private boolean isContainerNode(Element node) {
        return !isInlineNode(node);
    }
    
    private boolean isSelfClosingNode(Element node) {
        return node.childNodeSize() == 0;
    }
    
    @SuppressWarnings("unchecked")
	private boolean isIgnoredNode(Element node) {
        List<String> lst = (List<String>) option("nodes.ignored");
        return lst.indexOf(node.tagName().toLowerCase()) > -1;
    }
    
    @SuppressWarnings("unchecked")
	private boolean isSeparatorNode(Element node) {
        List<String> lst = (List<String>) option("nodes.splitters");
        return lst.indexOf(node.tagName().toLowerCase()) > -1;
    }
    
    private boolean isValidText(Node node) {
        if(node == null)
            return false;
        return node instanceof TextNode && !isEmptyString(((TextNode) node).text());
    }
    
    private String sanitizeValue(String value) {
        Pattern p = Pattern.compile("^\\s+");
        return p.matcher(value).replaceAll("");
    }
    
    @SuppressWarnings("unchecked")
	private String adjustName(Element node) {
        String name = node.tagName().toLowerCase();
        Map<String, String> map = (Map<String, String>) option("name_mapping");
        return map.getOrDefault(name, name);
    }
    
}
