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

package com.tr8n.core;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public abstract class Base {

    /**
     * Default constructor
     */
    public Base() {

    }

    /**
     * Constructor from attributes
     * @param attributes
     */
    public Base(Map<String, Object> attributes) {
        this();
        updateAttributes(attributes);
    }

    /**
     * Updates object's attributes
     * @param attributes
     */
    public abstract void updateAttributes(Map<String, Object> attributes);

    /**
     *
     */
    public void load() {
        // Optionally can be overloaded by the extending class
    }

    /**
     *
     * @return
     */
    public Map toMap() {
        // Optionally can be overloaded by the extending class
        return null;
    }

    /**
     *
     * @return
     */
    public String toJSON() {
        return "";
    }




}
