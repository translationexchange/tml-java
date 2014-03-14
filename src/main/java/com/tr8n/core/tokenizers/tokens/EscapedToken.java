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

import com.tr8n.core.Language;

import java.util.Map;

/**
 * Not yet supported by the service
 */
public class EscapedToken extends BaseToken {

    /**
     *
     * @return
     */
    public static String getExpression() {
        return "(\\^\\{[^}.]*\\})";
    }

    /**
     *
     * @param token
     */
    public EscapedToken(String token) {
        this(token, null);
    }

    /**
     *
     * @param token
     * @param label
     */
    public EscapedToken(String token, String label) {
        super(token, label);
    }

    /**
     *
     * @param translatedLabel
     * @param tokensData
     * @param language
     * @param options
     * @return
     */
    public String substitute(String translatedLabel, Map tokensData, Language language, Map options) {
        return translatedLabel;
    }

}
