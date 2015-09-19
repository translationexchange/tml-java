
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
 *
 * @author Berk
 * @version $Id: $Id
 */

package com.translationexchange.core.tokens;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.translationexchange.core.Language;
import com.translationexchange.core.LanguageCase;
import com.translationexchange.core.LanguageContext;
import com.translationexchange.core.Tml;
import com.translationexchange.core.Utils;
import com.translationexchange.core.decorators.Decorator;
import com.translationexchange.core.tokenizers.DataTokenValue;
public class DataToken extends Token {
	
    /** Constant <code>SEPARATOR_CONTEXT=":"</code> */
    public static final String SEPARATOR_CONTEXT = ":";
    /** Constant <code>SEPARATOR_CASE="::"</code> */
    public static final String SEPARATOR_CASE = "::";

    private static final String MAP_OBJECT_NAME = "object";
    private static final String MAP_OBJECT_VALUE = "value";
    private static final String MAP_PROPERTY_NAME = "property";
    private static final String MAP_ATTRIBUTE_NAME = "attribute";

    /**
     * List of language cases keys, if any
     */
    protected List<String> languageCaseKeys;

    /**
     * List of language context keys
     */
    protected List<String> languageContextKeys;

    /**
     * <p>getExpression.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public static String getExpression() {
    	return "(%?\\{{1,2}\\s*\\w*\\s*(:\\s*\\w+)*\\s*(::\\s*\\w+)*\\s*\\}{1,2})";
    }

    /**
     * <p>Constructor for DataToken.</p>
     *
     * @param token a {@link java.lang.String} object.
     */
    public DataToken(String token) {
        super(token);
    }

    /**
     * <p>Constructor for DataToken.</p>
     *
     * @param token a {@link java.lang.String} object.
     * @param label a {@link java.lang.String} object.
     */
    public DataToken(String token, String label) {
        super(token, label);
    }

    /**
     * <p>Getter for the field <code>languageContextKeys</code>.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<String> getLanguageContextKeys() {
        if (this.languageContextKeys == null) {
            this.languageContextKeys = Utils.trimListValues(
                Arrays.asList(getParenslessName().split(SEPARATOR_CASE)[0].split(SEPARATOR_CONTEXT))
            );
            this.languageContextKeys.remove(0);
        }
        return this.languageContextKeys;
    }

    /**
     * <p>Getter for the field <code>languageCaseKeys</code>.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<String> getLanguageCaseKeys() {
        if (this.languageCaseKeys == null) {
            this.languageCaseKeys = Utils.trimListValues(
                Arrays.asList(getParenslessName().split(SEPARATOR_CASE))
            );
            this.languageCaseKeys.remove(0);
        }
        return this.languageCaseKeys;
    }

    /** {@inheritDoc} */
    public String getName(Map<String, Object> options) {
        StringBuilder sb = new StringBuilder();

        if (options.get("parens") != null && options.get("parens").equals(true))
            sb.append("{");

        sb.append(getName());

        if (options.get("context_keys") != null && options.get("context_keys").equals(true) && this.getLanguageContextKeys().size() > 0) {
            sb.append(":");
            sb.append(Utils.join(this.getLanguageContextKeys().toArray(), SEPARATOR_CONTEXT));
        }

        if (options.get("case_keys") != null && options.get("case_keys").equals(true) && this.getLanguageCaseKeys().size() > 0) {
            sb.append("::");
            sb.append(Utils.join(this.getLanguageCaseKeys().toArray(), SEPARATOR_CASE));
        }

        if (options.get("parens") != null && options.get("parens").equals(true))
            sb.append("}");

        return sb.toString();
    }

    /**
     * Token objects can be passed to the translation method using any of the following approaches:
     *
     * - if an object is passed without a substitution value, it will use toString() to get the value:
     *
     *     Tml.translate("Hello {user}", Utils.buildMap("user", current_user))
     *     Tml.translate("{count||message}", Utils.buildMap("count", counter))
     *
     * - if an object is a list, the first value is the context object, the second value is the substitution value:
     *
     *     Tml.translate("Hello {user}", Utils.buildMap("user", Utils.buildList(current_user, "Michael")))
     *     Tml.translate("Hello {user}", Utils.buildMap("user", Utils.buildList(current_user, current_user.name)))
     *
     * - if an object is a map (mostly used for JSON), it must provide the object and the value/attribute for substitution:
     *
     *     Tml.translate("Hello {user}", Utils.buildMap("user", Utils.buildMap(
     *                                       "object", Utils.buildMap(
     *                                           "name", "Michael",
     *                                           "gender", "male"
     *                                       ),
     *                                       "value", "Michael"
     *                                    ))
     *
     *     Tml.translate("Hello {user}", Utils.buildMap("user", Utils.buildMap(
     *                                       "object", Utils.buildMap(
     *                                           "name", "Michael",
     *                                           "gender", "male"
     *                                       ),
     *                                       "attribute", "name"
     *                                    ))
     *
     * - if you don't need the substitution, you can provide an object directly:
     *
     *     Tml.translate("{user| He, She}", Utils.buildMap("user", Utils.buildMap(
     *                                           "name", "Michael",
     *                                           "gender", "male"
     *                                       ))
     *
     * - the most explicit way is to use the DataTokenValue interface:
     *
     *     Tml.translate("Hello {user}", Utils.buildMap("user", new DataTokenValue() {
     *                                        public Object getContextObject() {
     *                                           return user;
     *                                        }
     *                                        public String getSubstitutionValue() {
     *                                           return user.getName();
     *                                        }
     *                                    }))
     *
     * @param tokenMap a {@link java.util.Map} object.
     * @return a {@link java.lang.Object} object.
     */
    public Object getContextObject(Map<String, Object> tokenMap) {
        return getContextObject(tokenMap, this.getObjectName());
    }

    /**
     * <p>getContextObject.</p>
     *
     * @param tokenMap a {@link java.util.Map} object.
     * @param name a {@link java.lang.String} object.
     * @return a {@link java.lang.Object} object.
     */
    @SuppressWarnings("unchecked")
	public static Object getContextObject(Map<String, Object> tokenMap, String name) {
        if (tokenMap == null)
            return null;

        Object object = tokenMap.get(name);
        if (object == null)
            return null;

        if (object instanceof DataTokenValue) {
            DataTokenValue dtv = (DataTokenValue) object;
            return dtv.getContextObject();
        }

        if (object instanceof List) {
            List<Object> list = (List<Object>) object;
            return list.get(0);
        }

        if (object instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) object;
            if (map.get(MAP_OBJECT_NAME) != null)
                return map.get(MAP_OBJECT_NAME);
            return map;
        }

        return object;
    }

    /**
     * <p>getValue.</p>
     *
     * @param tokenMap map of token names to values
     * @return value of the token
     */
    @SuppressWarnings("unchecked")
	public String getValue(Map<String, Object> tokenMap) {
        if (tokenMap == null || tokenMap.get(this.getName()) == null) {
            String tokenValue = Tml.getConfig().getDefaultTokenValue(getName());
            if (tokenValue != null) return tokenValue;
            return this.toString();
        }

        Object object = tokenMap.get(this.getName());

        if (object instanceof DataTokenValue) {
            DataTokenValue dtv = (DataTokenValue) object;
            return dtv.getSubstitutionValue();
        }

        if (object instanceof List) {
            List<Object> list = (List<Object>) object;
            if (list.size() != 2) {
            	logError("{" + getName() + "} in " + getOriginalLabel() + " : list substitution value is not provided}");
                return getFullName();
            }
            object = list.get(1);
        }

        if (object instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) object;

            if (map.get(MAP_OBJECT_VALUE) != null) {
                return map.get(MAP_OBJECT_VALUE).toString();
            }

            if (map.get(MAP_OBJECT_NAME) == null) {
            	logError("{" + getName() + "} in " + getOriginalLabel() + " : substitution object is not provided}");
                return getFullName();
            }

            if (!(map.get(MAP_OBJECT_NAME) instanceof Map)) {
            	logError("{" + getName() + "} in " + getOriginalLabel() + " : substitution object is not a map}");
                return getFullName();
            }

            Map<String, Object> obj = (Map<String, Object>) map.get(MAP_OBJECT_NAME);

            if (map.get(MAP_ATTRIBUTE_NAME) != null) {
               return obj.get(map.get(MAP_ATTRIBUTE_NAME)).toString();
            }

            if (map.get(MAP_PROPERTY_NAME) != null) {
                return obj.get(map.get(MAP_PROPERTY_NAME)).toString();
            }

            logError("{" + getName() + "} in " + getOriginalLabel() + " : substitution property/value is not provided}");
            return getFullName();
        }

        return object.toString();
    }

    /**
     * <p>applyLanguageCases.</p>
     *
     * @param tokenValue a {@link java.lang.String} object.
     * @param object a {@link java.lang.Object} object.
     * @param language a {@link com.translationexchange.core.Language} object.
     * @param options a {@link java.util.Map} object.
     * @param languageCaseKeys a {@link java.util.List} object.
     * @return a {@link java.lang.String} object.
     */
    public String applyLanguageCases(String tokenValue, Object object, Language language, List<String> languageCaseKeys, Map<String, Object> options) {
        if (languageCaseKeys.size() == 0)
            return tokenValue;

        for (String keyword : languageCaseKeys) {
            LanguageCase languageCase = language.getLanguageCaseByKeyword(keyword);
            if (languageCase == null) continue;
            tokenValue = languageCase.apply(tokenValue, object, options);
        }

        return tokenValue;
    }

    /**
     * <p>applyLanguageCases.</p>
     *
     * @param tokenValue a {@link java.lang.String} object.
     * @param object a {@link java.lang.Object} object.
     * @param language a {@link com.translationexchange.core.Language} object.
     * @param options a {@link java.util.Map} object.
     * @return a {@link java.lang.String} object.
     */
    public String applyLanguageCases(String tokenValue, Object object, Language language, Map<String, Object> options) {
       return applyLanguageCases(tokenValue, object, language, this.getLanguageCaseKeys(), options);
    }

    /**
     * For transform tokens, we can only use the first context key, if it is not mapped in the context itself.
     *
     * {user:gender | male: , female: ... }
     *
     * It is not possible to apply multiple context rules on a single token at the same time:
     *
     * {user:gender:value | .... hah?}
     *
     * It is still possible to setup dependencies on multiple contexts.
     *
     * {user:gender:value}   - just not with piped tokens
     *
     * @param language a {@link com.translationexchange.core.Language} object.
     * @return a {@link com.translationexchange.core.LanguageContext} object.
     */
    public LanguageContext getLanguageContext(Language language) {
        if (this.getLanguageContextKeys().size() > 0)
            return language.getContextByKeyword(getLanguageContextKeys().get(0));

        return language.getContextByTokenName(this.getName());
    }

    /** {@inheritDoc} */
    public String substitute(String translatedLabel, Map<String, Object> tokensData, Language language, Map<String, Object> options) {
        String value = getValue(tokensData);
        
        if (value.equals(getFullName()))
        	return translatedLabel;
        
        value = applyLanguageCases(value, getContextObject(tokensData), language, options);
        
    	Decorator decorator = Tml.getConfig().getDecorator();
        return translatedLabel.replaceAll(Pattern.quote(getFullName()), decorator.decorateToken(this, value, options));
    }

}
