/*
 * Copyright (c) 2018 Translation Exchange, Inc. All rights reserved.
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
 * @author Michael Berkovich
 * @version $Id: $Id
 */

package com.translationexchange.core.tokenizers;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.translationexchange.core.Tml;
import com.translationexchange.core.languages.Language;
import com.translationexchange.core.tokens.Token;

public class DataTokenizer extends Tokenizer {
  /**
   * Constant <code>TOKEN_BRACKET="{"</code>
   */
  public static final String TOKEN_BRACKET = "{";
  /**
   * Constant <code>EXPRESSION_METHOD="getExpression"</code>
   */
  public static final String EXPRESSION_METHOD = "getExpression";

  /**
   * Token objects generated from the label
   */
  List<Token> tokens;

  /**
   * Default constructor
   */
  public DataTokenizer() {
    super();
  }

  /**
   * Constructs Base with label
   *
   * @param label label to be tokenized
   */
  public DataTokenizer(String label) {
    super(label, null);
  }

  /**
   * <p>Constructor for DataTokenizer.</p>
   *
   * @param label             label to be tokenized
   * @param allowedTokenNames list of allowed token names
   */
  public DataTokenizer(String label, List<String> allowedTokenNames) {
    super(label, allowedTokenNames);
  }

  /**
   * Extract tokens from a string
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  protected void tokenize() {
    try {
      this.tokens = new ArrayList<Token>();
      List<String> tokenMatches = new ArrayList<String>();
      String matchingLabel = this.label;
      for (String token : Tml.getConfig().getTokenClasses()) {
        Class tokenClass = Class.forName(token);
        Method method = tokenClass.getMethod(EXPRESSION_METHOD);
        String expression = (String) method.invoke(null);

        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(matchingLabel);

        while (matcher.find()) {
          String match = matcher.group();
          if (tokenMatches.contains(match))
            continue;
          tokenMatches.add(match);
          Constructor<Token> constructor = tokenClass.getConstructor(String.class, String.class);
          addToken(constructor.newInstance(match, this.label));
          matchingLabel = matchingLabel.replaceAll(Pattern.quote(match), "");
        }
      }
    } catch (Exception ex) {
      logException("Failed to tokenize a label: " + label, ex);
    }
  }

  /**
   * Returns list of tokens found in the label
   *
   * @return a {@link java.util.List} object.
   */
  public List<Token> getTokens() {
    if (this.tokens == null)
      this.tokens = new ArrayList<Token>();
    return this.tokens;
  }

  /**
   * Adds a token to the list of tokens
   *
   * @param token
   */
  private void addToken(Token token) {
    getTokens().add(token);
  }

  /**
   * <p>getTokenNames.</p>
   *
   * @return List of token names derived from the label
   */
  public List<String> getTokenNames() {
    if (this.tokenNames == null) {
      this.tokenNames = new ArrayList<String>();
      for (Token token : this.tokens) {
        this.tokenNames.add(token.getName());
      }
    }
    return this.tokenNames;
  }

  /**
   * {@inheritDoc}
   */
  public Object substitute(Map<String, Object> tokensData, Language language, Map<String, Object> options) {
    this.tokensData = tokensData;
    this.options = options;

    String translatedLabel = this.label;
    for (Token token : this.tokens) {
      translatedLabel = token.substitute(translatedLabel, tokensData, language, options);
    }
    return translatedLabel;
  }

  /**
   * {@inheritDoc}
   */
  public static boolean isApplicable(String label) {
    return label != null && label.contains(TOKEN_BRACKET);
  }

}
