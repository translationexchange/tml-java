
/**
 * Copyright (c) 2016 Translation Exchange, Inc. All rights reserved.
 * <p/>
 * _______                  _       _   _             ______          _
 * |__   __|                | |     | | (_)           |  ____|        | |
 * | |_ __ __ _ _ __  ___| | __ _| |_ _  ___  _ __ | |__  __  _____| |__   __ _ _ __   __ _  ___
 * | | '__/ _` | '_ \/ __| |/ _` | __| |/ _ \| '_ \|  __| \ \/ / __| '_ \ / _` | '_ \ / _` |/ _ \
 * | | | | (_| | | | \__ \ | (_| | |_| | (_) | | | | |____ >  < (__| | | | (_| | | | | (_| |  __/
 * |_|_|  \__,_|_| |_|___/_|\__,_|\__|_|\___/|_| |_|______/_/\_\___|_| |_|\__,_|_| |_|\__, |\___|
 * __/ |
 * |___/
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * <p/>
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

package com.translationexchange.core;

import com.translationexchange.core.logger.LoggerInterface;

import org.slf4j.LoggerFactory;

public class Logger implements LoggerInterface {
    private org.slf4j.Logger theLogger;

    /**
     * <p>Constructor for Logger.</p>
     */
    public Logger() {
        this.theLogger = LoggerFactory.getLogger(Logger.class);
        System.out.println(theLogger.getClass().getName());
    }

    /**
     * <p>logException.</p>
     *
     * @param message a {@link java.lang.String} object.
     * @param ex      a {@link java.lang.Exception} object.
     */
    public void logException(String message, Exception ex) {
        if (message != null) error(message);
        error(ex);
        error(Utils.join(ex.getStackTrace(), "\n"));
    }

    /**
     * <p>logException.</p>
     *
     * @param ex a {@link java.lang.Exception} object.
     */
    public void logException(Exception ex) {
        this.logException(null, ex);
    }

    /**
     * <p>debug.</p>
     *
     * @param message a {@link java.lang.Object} object.
     */
    public void debug(Object message) {
        theLogger.debug(message.toString());
    }

    /**
     * <p>info.</p>
     *
     * @param message a {@link java.lang.Object} object.
     */
    public void info(Object message) {
        theLogger.info(message.toString());
    }

    /**
     * <p>warn.</p>
     *
     * @param message a {@link java.lang.Object} object.
     */
    public void warn(Object message) {
        theLogger.warn(message.toString());
    }

    /**
     * <p>error.</p>
     *
     * @param message a {@link java.lang.Object} object.
     */
    public void error(Object message) {
        theLogger.error(message.toString());
    }

    public void error(String tag, String message) {

    }

    public void error(String tag, String message, Throwable e) {

    }

    public void debug(String tag, String message) {

    }

    public void info(String tag, String message) {

    }

    public void warn(String tag, String message) {

    }
}
