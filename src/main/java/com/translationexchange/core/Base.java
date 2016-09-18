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
 */

package com.translationexchange.core;

import java.util.Map;

/**
 * Base class for all TML library objects
 *
 * @author Michael Berkovich
 * @version $Id: $Id
 */
public abstract class Base {

    private Boolean loaded;

    /**
     * Default constructor
     */
    public Base() {

    }

    /**
     * Constructor from attributes
     *
     * @param attributes a {@link java.util.Map} object.
     */
    public Base(Map<String, Object> attributes) {
        this();
        updateAttributes(attributes);
    }

    /**
     * Updates object's attributes
     *
     * @param attributes a {@link java.util.Map} object.
     */
    public abstract void updateAttributes(Map<String, Object> attributes);

    /**
     * Checks if the model has been loaded from the server
     *
     * @return a {@link java.lang.Boolean} object.
     */
    public Boolean isLoaded() {
        return loaded;
    }

    /**
     * Indicates that the model has been loaded from Cache or API
     *
     * @param loaded a {@link java.lang.Boolean} object.
     */
    public void setLoaded(Boolean loaded) {
        this.loaded = loaded;
    }

}
