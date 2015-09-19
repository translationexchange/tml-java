/**
 * Copyright (c) 2015 Translation Exchange, Inc. All rights reserved.
 *
 *  _______                  _       _   _             ______          _
 * |__   __|                | |     | | (_)           |  ____|        | |
 *    | |_ __ __ _ _ __  ___| | __ _| |_ _  ___  _ __ | |__  __  _____| |__   __ _ _ __   __ _  ___
 *    | | '__/ _` | '_ \/ __| |/ _` | __| |/ _ \| '_ \|  __| \ \/ / __| '_ \ / _` | '_ \ / _` |/ _ \
 *    | | | | (_| | | | \__ \ | (_| | |_| | (_) | | | | |____ >  < (__| | | | (_| | | | | (_| |  __/
 *    |_|_|  \__,_|_| |_|___/_|\__,_|\__|_|\___/|_| |_|______/_/\_\___|_| |_|\__,_|_| |_|\__, |\___|
 *                                                                                        __/ |
 *                                                                                       |___/
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.translationexchange.core.tokens;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.translationexchange.core.Language;
import com.translationexchange.core.LanguageContext;
import com.translationexchange.core.LanguageContextRule;
import com.translationexchange.core.Tml;
import com.translationexchange.core.Utils;
import com.translationexchange.core.decorators.Decorator;

/**
 * <p>PipedToken class.</p>
 *
 * @author Berk
 * @version $Id: $Id
 */
public class PipedToken extends DataToken {

    /** Constant <code>SEPARATOR_INCLUSIVE="||"</code> */
    public static final String SEPARATOR_INCLUSIVE = "||";
    /** Constant <code>SEPARATOR_EXCLUSIVE="|"</code> */
    public static final String SEPARATOR_EXCLUSIVE = "|";
    /** Constant <code>SEPARATOR_PARAMS=","</code> */
    public static final String SEPARATOR_PARAMS = ",";
    /** Constant <code>SEPARATOR_PARAM_VALUES=":"</code> */
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
     * <p>getExpression.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public static String getExpression() {
        return "(%?\\{{1,2}\\s*[\\w]*\\s*(:\\s*\\w+)*\\s*\\|\\|?[^\\{\\}\\|]+\\}{1,2})";
    }

    /**
     * <p>Constructor for PipedToken.</p>
     *
     * @param token a {@link java.lang.String} object.
     */
    public PipedToken(String token) {
        super(token);
    }

    /**
     * <p>Constructor for PipedToken.</p>
     *
     * @param token a {@link java.lang.String} object.
     * @param label a {@link java.lang.String} object.
     */
    public PipedToken(String token, String label) {
        super(token, label);
    }


    /**
     * <p>Getter for the field <code>separator</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getSeparator() {
        if (this.separator == null) {
            this.separator = getFullName().contains(SEPARATOR_INCLUSIVE) ? SEPARATOR_INCLUSIVE : SEPARATOR_EXCLUSIVE;
        }
        return this.separator;
    }

    /**
     * <p>Getter for the field <code>parameters</code>.</p>
     *
     * @return a {@link java.util.List} object.
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

    
    /**
     * <p>Getter for the field <code>pipelessName</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getPipelessName() {
        if (this.pipelessName == null) {
            this.pipelessName = getParenslessName().split(Pattern.quote(SEPARATOR_EXCLUSIVE))[0];
        }
        return this.pipelessName;

    }
    
    /**
     * <p>getName.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getName() {
        if (this.shortName == null) {
            this.shortName = getPipelessName().split(Pattern.quote(SEPARATOR_CONTEXT))[0].trim();
        }
        return this.shortName;
    }    

   /**
    * <p>getLanguageContextKeys.</p>
    *
    * @return a {@link java.util.List} object.
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
    * <p>getLanguageCaseKeys.</p>
    *
    * @return a {@link java.util.List} object.
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
     * @param tokenMappingOptions a {@link java.lang.Object} object.
     * @param language a {@link com.translationexchange.core.Language} object.
     * @param options a {@link java.util.Map} object.
     * @return a {@link java.util.Map} object.
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
        	logError(getFullName() + " is not associated with any context");
            return null;
        }

        if (tokenMappingOptions instanceof String) {
        	logError(getFullName() + " context parameter sequence is not supported");
            return null;
        }

        Map<String, String> tokenMapping = null;

        if (tokenMappingOptions instanceof List) {
            List<Object> tokenMappingArray = (List<Object>) tokenMappingOptions;
            if (this.getParameters().size() > tokenMappingArray.size()) {
            	logError(getFullName() + " context parameter sequence is not supported");
                return null;
            }

            if (!(tokenMappingArray.get(this.getParameters().size()-1) instanceof Map)) {
            	logError(getFullName() + " context parameter sequence is not supported");
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
                    	logError(getFullName() + " invalid context parameter sequence mapping");
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
     * <p>getParameterMap.</p>
     *
     * @param tokenMappingOptions a {@link java.lang.Object} object.
     * @return a {@link java.util.Map} object.
     */
    public Map<String, String> getParameterMap(Object tokenMappingOptions) {
       return getParameterMap(tokenMappingOptions, null, null);
    }

    /**
     * <p>getDecorationName.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getDecorationName() {
    	return "piped";
    }
    
    /** {@inheritDoc} */
    public String substitute(String translatedLabel, Map<String, Object> tokensData, Language language, Map<String, Object> options) {
        Object object = getContextObject(tokensData);

        if (object == null) {
        	logError("missing token value in " + getFullName() + " of " + getOriginalLabel());
            return translatedLabel;
        }

        if (this.getParameters().size() == 0) {
        	logError("missing piped params in " + getFullName() + " of " + getOriginalLabel());
            return translatedLabel;
        }

        LanguageContext context = getLanguageContext(language);
        if (context == null) {
        	logError("unknown language context in " + getFullName() + " of " + getOriginalLabel());
            return translatedLabel;
        }

        Map<String, String> valueMap = getParameterMap(context.getTokenMapping(), language, options);
        if (valueMap == null) {
        	logError("invalid context or piped params in " + getFullName() + " of " + getOriginalLabel());
            return translatedLabel;
        }

        LanguageContextRule matchedRule = context.findMatchingRule(object);

        if (matchedRule == null) {
        	logError("no context rule matched in " + getFullName() + " of " + getOriginalLabel());
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
        	logError("no value matched in " + getFullName() + " of " + getOriginalLabel());
            return translatedLabel;
        }

    	Decorator decorator = Tml.getConfig().getDecorator();

    	StringBuilder replacementValue = new StringBuilder();
        if (getSeparator().equals(SEPARATOR_INCLUSIVE)) {
            replacementValue.append(decorator.decorateToken(this, getValue(tokensData), options));
            replacementValue.append(" ");
        } else {
            value = value.replaceAll(Pattern.quote("#" + getName() + "#"), decorator.decorateToken(this, getValue(tokensData), options));
        }
        replacementValue.append(value);

        return translatedLabel.replaceAll(Pattern.quote(getFullName()), replacementValue.toString());
    }
}
