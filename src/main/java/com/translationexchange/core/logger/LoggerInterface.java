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

package com.translationexchange.core.logger;

/**
 * Created by ababenko on 8/17/2016.
 */
public interface LoggerInterface {
  /**
   * <p>logException.</p>
   *
   * @param message a {@link String} object.
   * @param ex      a {@link Exception} object.
   */
  void logException(String message, Exception ex);

  /**
   * <p>logException.</p>
   *
   * @param ex a {@link Exception} object.
   */
  void logException(Exception ex);

  /**
   * <p>debug.</p>
   *
   * @param message a {@link Object} object.
   */
  void debug(Object message);

  /**
   * <p>info.</p>
   *
   * @param message a {@link Object} object.
   */
  void info(Object message);

  /**
   * <p>warn.</p>
   *
   * @param message a {@link Object} object.
   */
  void warn(Object message);

  /**
   * <p>error.</p>
   *
   * @param message a {@link Object} object.
   */
  void error(Object message);

  void error(String tag, String message);

  void error(String tag, String message, Throwable e);

  void debug(String tag, String message);

  void info(String tag, String message);

  void warn(String tag, String message);
}
