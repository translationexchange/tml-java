/*
 *  Copyright (c) 2014 Michael Berkovich, http://tr8nhub.com All rights reserved.
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */

package com.tr8n.core.tokenizers.tokens;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.tr8n.core.Language;
import com.tr8n.core.LanguageContext;
import com.tr8n.core.LanguageContextRule;
import com.tr8n.core.Tr8n;
import com.tr8n.core.Utils;

/**
 *
 */
public class PipedToken extends DataToken {

    public static final String SEPARATOR_INCLUSIVE = "||";
    public static final String SEPARATOR_EXCLUSIVE = "|";
    public static final String SEPARATOR_PARAMS = ",";
    public static final String SEPARATOR_PARAM_VALUES = ":";
    
    /**
     * Separator | or ||
     */
    String separator;

    /**
     * Parameters from the token
     */
    List<String> parameters;

    /**
     * Name without pipes
     */
    String pipelessName;
    
    /**
     *
     * @return
     */
    public static String getExpression() {
        return "(\\{[^_:|][\\w]*(:[\\w]+)*(::[\\w]+)*\\s*\\|\\|?[^{^}]+\\})";
    }

    /**
     *
     * @param token
     */
    public PipedToken(String token) {
        super(token);
    }

    /**
     *
     * @param token
     * @param label
     */
    public PipedToken(String token, String label) {
        super(token, label);
    }


    /**
     *
     * @return
     */
    public String getSeparator() {
        if (this.separator == null) {
            this.separator = getFullName().contains(SEPARATOR_INCLUSIVE) ? SEPARATOR_INCLUSIVE : SEPARATOR_EXCLUSIVE;
        }
        return this.separator;
    }

    /**
     *
     * @return
     */
    public List<String> getParameters() {
        if (this.parameters == null) {
            this.parameters = Utils.trimListValues(Arrays.asList(
                    getParenslessName().split(
                            Pattern.quote(getSeparator())
                    )[1].split(SEPARATOR_PARAMS))
            );
        }
        return this.parameters;
    }

    
    public String getPipelessName() {
        if (this.pipelessName == null) {
            this.pipelessName = getParenslessName().split(Pattern.quote(SEPARATOR_EXCLUSIVE))[0];
        }
        return this.pipelessName;

    }
    
    /**
     * 
     */
    public String getName() {
        if (this.shortName == null) {
            this.shortName = getPipelessName().split(Pattern.quote(SEPARATOR_CONTEXT))[0].trim();
        }
        return this.shortName;
    }    

    /**
    *
    * @return
    */
   public List<String> getLanguageContextKeys() {
       if (this.languageContextKeys == null) {
           this.languageContextKeys = Utils.trimListValues(
               Arrays.asList(getPipelessName().split(Pattern.quote(SEPARATOR_CASE))[0].split(Pattern.quote(SEPARATOR_CONTEXT)))
           );
           this.languageContextKeys.remove(0);
       }
       return this.languageContextKeys;
   }

   /**
    *
    * @return
    */
   public List<String> getLanguageCaseKeys() {
       if (this.languageCaseKeys == null) {
           this.languageCaseKeys = Utils.trimListValues(
               Arrays.asList(getPipelessName().split(Pattern.quote(SEPARATOR_CASE)))
           );
           this.languageCaseKeys.remove(0);
       }
       return this.languageCaseKeys;
   }
    
   
    /**
	 * token:      {count|| one: message, many: messages}
	 * results in: {"one": "message", "many": "messages"}
	 *
	 * token:      {count| one: a message, other: $count messages}
	 * results in: {"one": "a message", "other": "$count messages"}
	 * 
	 * token:      {count|| message}
	 * transform:  [{"one": "{$0}", "other": "{$0::plural}"}, {"one": "{$0}", "other": "{$1}"}]
	 * results in: {"one": "message", "other": "messages"}
	 *
	 * token:      {count|| message, messages}
	 * transform:  [{"one": "{$0}", "other": "{$0::plural}"}, {"one": "{$0}", "other": "{$1}"}]
	 * results in: {"one": "message", "other": "messages"}
	 *
	 * token:      {user| Dorogoi, Dorogaya}
	 * transform:  ["unsupported", {"male": "{$0}", "female": "{$1}", "other": "{$0}/{$1}"}]
	 * results in: {"male": "Dorogoi", "female": "Dorogaya", "other": "Dorogoi/Dorogaya"}
	 *
	 * token:      {actors:|| likes, like}
	 * transform:  ["unsupported", {"one": "{$0}", "other": "{$1}"}]
	 * results in: {"one": "likes", "other": "like"}
	 *
	 *
	 * @param tokenMappingOptions
	 * @param language
	 * @param options
	 * @return
	 */
    @SuppressWarnings("unchecked")
	public Map<String, String> getParameterMap(Object tokenMappingOptions, Language language, Map<String, Object> options) {
        Map<String, String> values = new HashMap<String, String>();

        if (getParameters().get(0).contains(SEPARATOR_PARAM_VALUES)) {
            for (String param : getParameters()) {
                List<String> parts = Utils.trimListValues(Arrays.asList(param.split(Pattern.quote(SEPARATOR_PARAM_VALUES))));
                values.put(parts.get(0), parts.get(1));
            }
            return values;
        }

        if (tokenMappingOptions == null) {
            Tr8n.getLogger().error(getFullName() + " is not associated with any context");
            return null;
        }

        if (tokenMappingOptions instanceof String) {
            Tr8n.getLogger().error(getFullName() + " context parameter sequence is not supported");
            return null;
        }

        Map<String, String> tokenMapping = null;

        if (tokenMappingOptions instanceof List) {
            List<Object> tokenMappingArray = (List<Object>) tokenMappingOptions;
            if (this.getParameters().size() > tokenMappingArray.size()) {
                Tr8n.getLogger().error(getFullName() + " context parameter sequence is not supported");
                return null;
            }

            if (!(tokenMappingArray.get(this.getParameters().size()-1) instanceof Map)) {
                Tr8n.getLogger().error(getFullName() + " context parameter sequence is not supported");
                return null;
            }

            tokenMapping = (Map<String, String>) tokenMappingArray.get(this.getParameters().size()-1);
        } else if (tokenMappingOptions instanceof Map) {
            tokenMapping = (Map<String, String>) tokenMappingOptions;
        }

        if (tokenMapping != null) {
            Iterator<Map.Entry<String, String>> entries = tokenMapping.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry<String, String> entry = entries.next();
                values.put(entry.getKey(), entry.getValue());

                Pattern pattern = Pattern.compile("\\{\\$\\d(::[\\w]+)*\\}");
                Matcher matcher = pattern.matcher(entry.getValue());
                while(matcher.find()) {
                    String match = matcher.group();
                    List<String> parts = new ArrayList<String>(Arrays.asList(match.replaceAll("[\\{\\}]", "").split(Pattern.quote(SEPARATOR_CASE))));
                    String indexValue = parts.get(0);
                    Integer index = Integer.parseInt(indexValue.trim().replaceAll("\\$", ""));

                    if (getParameters().size() < index) {
                        Tr8n.getLogger().error(getFullName() + " invalid context parameter sequence mapping");
                        return null;
                    }

                    String val = getParameters().get(index);
                    parts.remove(0);

                    if (language != null) {
                        val = applyLanguageCases(val, null, language, parts, options);
                    }

                    values.put(entry.getKey(), values.get(entry.getKey()).replaceAll(Pattern.quote(match), val));
                }
            }
        }

        return values;
    }

    /**
     *
     * @param tokenMappingOptions
     * @return
     */
    public Map<String, String> getParameterMap(Object tokenMappingOptions) {
       return getParameterMap(tokenMappingOptions, null, null);
    }

    /**
     *
     * @param translatedLabel   label in which the substitution happens
     * @param tokensData        map of data tokens
     * @param language          language in which the substitution happens
     * @param options           options for substitutions
     * @return
     */
    public String substitute(String translatedLabel, Map<String, Object> tokensData, Language language, Map<String, Object> options) {
        Object object = getContextObject(tokensData);

        if (object == null) {
            Tr8n.getLogger().error("missing token value in " + getFullName() + " of " + getOriginalLabel());
            return translatedLabel;
        }

        if (this.getParameters().size() == 0) {
            Tr8n.getLogger().error("missing piped params in " + getFullName() + " of " + getOriginalLabel());
            return translatedLabel;
        }

        LanguageContext context = getLanguageContext(language);
        if (context == null) {
            Tr8n.getLogger().error("unknown language context in " + getFullName() + " of " + getOriginalLabel());
            return translatedLabel;
        }

        Map<String, String> valueMap = getParameterMap(context.getTokenMapping(), language, options);
        if (valueMap == null) {
            Tr8n.getLogger().error("invalid context or piped params in " + getFullName() + " of " + getOriginalLabel());
            return translatedLabel;
        }

        LanguageContextRule matchedRule = context.findMatchingRule(object);

        if (matchedRule == null) {
            Tr8n.getLogger().error("no context rule matched in " + getFullName() + " of " + getOriginalLabel());
            return translatedLabel;
        }

        String value = valueMap.get(matchedRule.getKeyword());
        if (value == null) {
          LanguageContextRule fallbackRule = context.getFallbackRule();
            if (fallbackRule != null && valueMap.get(fallbackRule.getKeyword()) != null) {
                value = valueMap.get(fallbackRule.getKeyword());
            }
        }

        if (value == null) {
            Tr8n.getLogger().error("no value matched in " + getFullName() + " of " + getOriginalLabel());
            return translatedLabel;
        }

        StringBuilder replacementValue = new StringBuilder();
        if (getSeparator().equals(SEPARATOR_INCLUSIVE)) {
            replacementValue.append(getValue(tokensData));
            replacementValue.append(" ");
        } else {
            value = value.replaceAll(Pattern.quote("#" + getName() + "#"), getValue(tokensData));
        }
        replacementValue.append(value);

        return translatedLabel.replaceAll(Pattern.quote(getFullName()), replacementValue.toString());
    }
}
