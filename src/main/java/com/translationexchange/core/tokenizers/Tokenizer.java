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

import java.util.List;
import java.util.Map;

import com.translationexchange.core.languages.Language;
import com.translationexchange.core.tokens.Token;

/**
 * Base class for all tokenizers
 *
 * @author Michael Berkovich
 * @version $Id: $Id
 */
public abstract class Tokenizer {

  /**
   * Label from which the tokens were extracted (original or translated)
   */
  protected String label;

  /**
   * List of allowed token names from the original label
   */
  protected List<String> allowedTokenNames;

  /**
   * Names of all registered tokens
   */
  protected List<String> tokenNames;

  /**
   * Tokens data map, used internally
   */
  protected Map<String, Object> tokensData;

  /**
   * Substitution options, used internally
   */
  protected Map<String, Object> options;

  /**
   * Default constructor
   */
  public Tokenizer() {
  }

  /**
   * Constructs tokenizer with label
   *
   * @param label label to be tokenized
   */
  public Tokenizer(String label) {
    this(label, null);
  }

  /**
   * Constructs tokenizer with label and list of allowed token names
   *
   * @param label             a {@link java.lang.String} object.
   * @param allowedTokenNames a {@link java.util.List} object.
   */
  public Tokenizer(String label, List<String> allowedTokenNames) {
    tokenize(label, allowedTokenNames);
  }

  /**
   * <p>tokenize.</p>
   *
   * @param label a {@link java.lang.String} object.
   */
  public void tokenize(String label) {
    tokenize(label, null);
  }

  /**
   * <p>tokenize.</p>
   *
   * @param label             a {@link java.lang.String} object.
   * @param allowedTokenNames a {@link java.util.List} object.
   */
  public void tokenize(String label, List<String> allowedTokenNames) {
    this.label = label;
    this.allowedTokenNames = allowedTokenNames;
    this.tokenNames = null;
    tokenize();
  }

  /**
   * Tokenizes the expression
   */
  protected abstract void tokenize();

  /**
   * <p>Getter for the field <code>tokenNames</code>.</p>
   *
   * @return a {@link java.util.List} object.
   */
  public List<String> getTokenNames() {
    return this.tokenNames;
  }

  /**
   * <p>Getter for the field <code>allowedTokenNames</code>.</p>
   *
   * @return a {@link java.util.List} object.
   */
  public List<String> getAllowedTokenNames() {
    return this.allowedTokenNames;
  }

  /**
   * <p>isTokenAllowed.</p>
   *
   * @param token a {@link com.translationexchange.core.tokens.Token} object.
   * @return a boolean.
   */
  public boolean isTokenAllowed(Token token) {
    if (this.getAllowedTokenNames() == null)
      return true;

    return this.getAllowedTokenNames().contains(token.getName());
  }

  /**
   * <p>substitute.</p>
   *
   * @param tokensData a {@link java.util.Map} object.
   * @return a {@link java.lang.Object} object.
   */
  public Object substitute(Map<String, Object> tokensData) {
    return substitute(tokensData, null);
  }

  /**
   * <p>substitute.</p>
   *
   * @param tokensData a {@link java.util.Map} object.
   * @param language   a {@link com.translationexchange.core.languages.Language} object.
   * @return a {@link java.lang.Object} object.
   */
  public Object substitute(Map<String, Object> tokensData, Language language) {
    return substitute(tokensData, language, null);
  }

  /**
   * <p>substitute.</p>
   *
   * @param tokensData a {@link java.util.Map} object.
   * @param language   a {@link com.translationexchange.core.languages.Language} object.
   * @param options    a {@link java.util.Map} object.
   * @return a {@link java.lang.Object} object.
   */
  public abstract Object substitute(Map<String, Object> tokensData, Language language, Map<String, Object> options);

  /**
   * Returns true/false whether the tokenizer is applicable to the label
   *
   * @param label a {@link java.lang.String} object.
   * @return a boolean.
   */
  public static boolean isApplicable(String label) {
    return false;
  }

  /**
   * Logs error message
   *
   * @param msg a {@link java.lang.String} object
   * @param ex  a {@link java.lang.Exception} object.
   */
  protected void logException(String msg, Exception ex) {
//    	Tml.getLogger().logException(msg, ex);
  }

  /**
   * Logs error message
   *
   * @param msg a {@link java.lang.String} object
   */
  protected void logException(String msg) {
//    	Tml.getLogger().logError(msg);
  }
}

