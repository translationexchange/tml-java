package com.tr8n.core.tokenizers;

import com.tr8n.core.Tr8n;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by michael on 3/13/14.
 */
public class HtmlTokenizer extends DecorationTokenizer {

    /**
     *
     * @param label
     */
    public HtmlTokenizer(String label) {
        this(label, null);
    }

    /**
     *
     * @param label
     * @param allowedTokenNames
     */
    public HtmlTokenizer(String label, List<String> allowedTokenNames) {
        super(label, allowedTokenNames);
    }

    /**
     *
     * @param token
     * @param value
     * @return
     */
    protected String applyToken(String token, String value) {
        if (token.equals(RESERVED_TOKEN) || !isTokenAllowed(token))
            return value;

        Object object = this.tokensData.get(token);

        if (object == null || object instanceof Map) {
            String defaultValue = Tr8n.getConfig().getDefaultTokenValue(token, "decoration", "html");
            if (defaultValue == null) return value;

            if (object instanceof Map) {
                Map map = (Map) object;
                Iterator entries = map.entrySet().iterator();
                while (entries.hasNext()) {
                    Map.Entry entry = (Map.Entry) entries.next();
                    defaultValue = defaultValue.replaceAll(Pattern.quote((String) entry.getKey()), (String) entry.getValue());
                }
            }
            return defaultValue;
        }

        if (object instanceof DecorationTokenValue) {
            DecorationTokenValue dtv = (DecorationTokenValue) object;
            return dtv.getSubstitutionValue(value);
        }

        if (object instanceof String) {
            String str = (String) object;
            return str.replaceAll(Pattern.quote(PLACEHOLDER), value);
        }

        return value;
    }


}
