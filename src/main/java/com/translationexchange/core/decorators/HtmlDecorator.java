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

package com.translationexchange.core.decorators;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.translationexchange.core.Language;
import com.translationexchange.core.Session;
import com.translationexchange.core.Tml;
import com.translationexchange.core.TranslationKey;
import com.translationexchange.core.Utils;

public class HtmlDecorator implements Decorator {

	public Object decorate(Object translatedLabel, Language translationLanguage, Language targetLanguage, TranslationKey translationKey, Map<String, Object> options) {
		String label = (String) translatedLabel;
		
		// Tml.getLogger().debug(label + " : " + translationLanguage.getLocale() + " :: " + translationKey.getLocale() + " => " + targetLanguage.getLocale());
		
		if (options == null) return label;
		if (options.get("skip_decorations") != null) return label;
		if (translationKey.getLocale().equals(targetLanguage.getLocale())) return label;
		
		Session session = (Session) options.get("session");
		if (session == null) return label;
		if (session.getCurrentTranslator() == null) return label;
		if (!session.getCurrentTranslator().isInline()) return label;

		String element = "tml:label";
		if (options.get("use_div") != null)
			element = "div";
		else if (options.get("use_span") != null)
			element = "span";

		StringBuilder sb = new StringBuilder();
				
		List<String> classes = new ArrayList<String>();
		classes.add("tml_translatable");
		
		if (options.get("locked") != null && options.get("locked").equals("true")) {
			classes.add("tml_locked");
		} else if (translationLanguage.getLocale().equals(translationKey.getLocale())) {
			if (options.get("pending") != null && options.get("pending").equals("true")) {
				classes.add("tml_pending");
			} else {
				classes.add("tml_not_translated");
			}
		} else if (translationLanguage.getLocale().equals(targetLanguage.getLocale())) {
			classes.add("tml_translated");
		} else {
			classes.add("tml_fallback");
		}

		sb.append("<" + element);
		sb.append(" class='" + Utils.join(classes.toArray(), " ") + "'");
		sb.append(" data-translation_key='" + translationKey.getKey() + "'");
		sb.append(">");
		sb.append(label);
		sb.append("</" + element + ">");
		return sb.toString();
	}

}
