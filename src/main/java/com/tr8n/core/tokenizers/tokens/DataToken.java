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

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.tr8n.core.Language;
import com.tr8n.core.LanguageCase;
import com.tr8n.core.LanguageContext;
import com.tr8n.core.Tr8n;
import com.tr8n.core.Utils;
import com.tr8n.core.tokenizers.DataTokenValue;

public class DataToken extends Token {
	
    public static final String SEPARATOR_CONTEXT = ":";
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
     *
     * @return
     */
    public static String getExpression() {
        return "(\\{[^_:][\\w]*(:[\\w]+)*(::[\\w]+)*\\})";
    }

    /**
     *
     * @param token
     */
    public DataToken(String token) {
        super(token);
    }

    /**
     *
     * @param token
     * @param label
     */
    public DataToken(String token, String label) {
        super(token, label);
    }

    /**
     *
     * @return
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
     *
     * @return
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

    /**
     *
     * @param options
     * @return
     */
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
     *     Tr8n.translate("Hello {user}", Utils.buildMap("user", current_user))
     *     Tr8n.translate("{count||message}", Utils.buildMap("count", counter))
     *
     * - if an object is a list, the first value is the context object, the second value is the substitution value:
     *
     *     Tr8n.translate("Hello {user}", Utils.buildMap("user", Utils.buildList(current_user, "Michael")))
     *     Tr8n.translate("Hello {user}", Utils.buildMap("user", Utils.buildList(current_user, current_user.name)))
     *
     * - if an object is a map (mostly used for JSON), it must provide the object and the value/attribute for substitution:
     *
     *     Tr8n.translate("Hello {user}", Utils.buildMap("user", Utils.buildMap(
     *                                       "object", Utils.buildMap(
     *                                           "name", "Michael",
     *                                           "gender", "male"
     *                                       ),
     *                                       "value", "Michael"
     *                                    ))
     *
     *     Tr8n.translate("Hello {user}", Utils.buildMap("user", Utils.buildMap(
     *                                       "object", Utils.buildMap(
     *                                           "name", "Michael",
     *                                           "gender", "male"
     *                                       ),
     *                                       "attribute", "name"
     *                                    ))
     *
     * - if you don't need the substitution, you can provide an object directly:
     *
     *     Tr8n.translate("{user| He, She}", Utils.buildMap("user", Utils.buildMap(
     *                                           "name", "Michael",
     *                                           "gender", "male"
     *                                       ))
     *
     * - the most explicit way is to use the DataTokenValue interface:
     *
     *     Tr8n.translate("Hello {user}", Utils.buildMap("user", new DataTokenValue() {
     *                                        public Object getContextObject() {
     *                                           return user;
     *                                        }
     *                                        public String getSubstitutionValue() {
     *                                           return user.getName();
     *                                        }
     *                                    }))
     *
     * @param tokenMap
     * @return
     */
    public Object getContextObject(Map<String, Object> tokenMap) {
        return getContextObject(tokenMap, this.getObjectName());
    }

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
     *
     * @param tokenMap map of token names to values
     * @return value of the token
     */
    @SuppressWarnings("unchecked")
	public String getValue(Map<String, Object> tokenMap) {
        if (tokenMap == null || tokenMap.get(this.getName()) == null) {
            String tokenValue = Tr8n.getConfig().getDefaultTokenValue(getName());
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
                Tr8n.getLogger().error("{" + getName() + "} in " + getOriginalLabel() + " : list substitution value is not provided}");
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
                Tr8n.getLogger().error("{" + getName() + "} in " + getOriginalLabel() + " : substitution object is not provided}");
                return getFullName();
            }

            if (!(map.get(MAP_OBJECT_NAME) instanceof Map)) {
                Tr8n.getLogger().error("{" + getName() + "} in " + getOriginalLabel() + " : substitution object is not a map}");
                return getFullName();
            }

            Map<String, Object> obj = (Map<String, Object>) map.get(MAP_OBJECT_NAME);

            if (map.get(MAP_ATTRIBUTE_NAME) != null) {
               return obj.get(map.get(MAP_ATTRIBUTE_NAME)).toString();
            }

            if (map.get(MAP_PROPERTY_NAME) != null) {
                return obj.get(map.get(MAP_PROPERTY_NAME)).toString();
            }

            Tr8n.getLogger().error("{" + getName() + "} in " + getOriginalLabel() + " : substitution property/value is not provided}");
            return getFullName();
        }

        return object.toString();
    }

    /**
     *
     * @param tokenValue
     * @param object
     * @param language
     * @param options
     * @return
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
     *
     * @param tokenValue
     * @param object
     * @param language
     * @param options
     * @return
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
     * @param language
     * @return
     */
    public LanguageContext getLanguageContext(Language language) {
        if (this.getLanguageContextKeys().size() > 0)
            return language.getContextByKeyword(getLanguageContextKeys().get(0));

        return language.getContextByTokenName(this.getName());
    }

    /**
     *
     * @param translatedLabel   label in which the substitution happens
     * @param tokensData        map of data tokens
     * @param language          language in which the substitution happens
     * @param options           options for substitutions
     * @return                  label with substituted tokens
     */
    public String substitute(String translatedLabel, Map<String, Object> tokensData, Language language, Map<String, Object> options) {
        String value = applyLanguageCases(getValue(tokensData), getContextObject(tokensData), language, options);
        return translatedLabel.replaceAll(Pattern.quote(getFullName()), value);
    }

}
