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

import com.tr8n.core.*;

import java.lang.reflect.Array;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class PipedToken extends DataToken {

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
            this.separator = getFullName().contains("||") ? "||" : "|";
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
                    )[1].split(","))
            );
        }
        return this.parameters;
    }

    
    public String getPipelessName() {
        if (this.pipelessName == null) {
            this.pipelessName = getParenslessName().split("\\|")[0];
        }
        return this.pipelessName;

    }
    
    /**
     * 
     */
    public String getName() {
        if (this.shortName == null) {
            this.shortName = getPipelessName().split(":")[0].trim();
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
               Arrays.asList(getPipelessName().split("::")[0].split(":"))
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
               Arrays.asList(getPipelessName().split("::"))
           );
           this.languageCaseKeys.remove(0);
       }
       return this.languageCaseKeys;
   }
    
   
    /**
	 * token:      {count|| one: message, many: messages}
	 * results in: {"one": "message", "many": "messages"}
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
    public Map<String, String> getParameterMap(Object tokenMappingOptions, Language language, Map options) {
        Map<String, String> values = new HashMap<String, String>();

        if (getParameters().get(0).contains(":")) {
            for (String param : getParameters()) {
                List parts = Utils.trimListValues(Arrays.asList(param.split(":")));
                values.put((String)parts.get(0), (String)parts.get(1));
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

        Map tokenMapping = null;

        if (tokenMappingOptions instanceof List) {
            List tokenMappingArray = (List) tokenMappingOptions;
            if (this.getParameters().size() > tokenMappingArray.size()) {
                Tr8n.getLogger().error(getFullName() + " context parameter sequence is not supported");
                return null;
            }

            if (!(tokenMappingArray.get(this.getParameters().size()-1) instanceof Map)) {
                Tr8n.getLogger().error(getFullName() + " context parameter sequence is not supported");
                return null;
            }

            tokenMapping = (Map) tokenMappingArray.get(this.getParameters().size()-1);
        } else if (tokenMappingOptions instanceof Map) {
            tokenMapping = (Map) tokenMappingOptions;
        }

        if (tokenMapping != null) {
            Iterator entries = tokenMapping.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry entry = (Map.Entry) entries.next();
                String key = (String) entry.getKey();
                String value = (String) entry.getValue();
                values.put(key, value);

                Pattern pattern = Pattern.compile("\\{\\$\\d(::[\\w]+)*\\}");
                Matcher matcher = pattern.matcher(value);
                while(matcher.find()) {
                    String match = matcher.group();
                    List<String> parts = new ArrayList<String>(Arrays.asList(match.replaceAll("[\\{\\}]", "").split("::")));
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

                    values.put(key, values.get(key).replaceAll(Pattern.quote(match), val));
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
    public String substitute(String translatedLabel, Map tokensData, Language language, Map options) {
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
        if (getSeparator().equals("||")) {
            replacementValue.append(getValue(tokensData));
            replacementValue.append(" ");
        }
        replacementValue.append(value);

        return translatedLabel.replaceAll(Pattern.quote(getFullName()), replacementValue.toString());
    }
}
