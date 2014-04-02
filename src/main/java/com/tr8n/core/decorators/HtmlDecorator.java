package com.tr8n.core.decorators;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.tr8n.core.Language;
import com.tr8n.core.Session;
import com.tr8n.core.TranslationKey;

public class HtmlDecorator implements Decorator {

	public Object decorate(Object translatedLabel, Language translationLanguage, Language targetLanguage, TranslationKey translationKey, Map<String, Object> options) {
		String label = (String) translatedLabel;
		
		if (options == null) return label;
		if (options.get("skip_decorations") != null) return label;
		if (translationKey.getLocale().equals(targetLanguage.getLocale())) return label;
		
		Session session = (Session) options.get("session");
		if (session == null) return label;
		if (session.getCurrentTranslator() == null) return label;
		if (!session.getCurrentTranslator().isInline()) return label;
		if (translationKey.isLocked() && !session.getCurrentTranslator().isManager()) return label;

		String element = "span";
		if (options.get("use_div") != null)
			element = "div";

		StringBuilder sb = new StringBuilder();
		
//		if (!translationKey.isRegistered()) {
//			sb.append("<" + element + " class='tr8n_pending'>");
//			sb.append(label);
//			sb.append("</" + element + ">");
//			return sb.toString();
//		}
		
		List<String> classes = new ArrayList<String>();
		classes.add("tr8n_translatable");
		
		if (translationKey.isLocked()) {
			if (!session.getCurrentTranslator().isFeatureEnabled("show_locked_keys")) return label;
			classes.add("tr8n_locked");
		} else if (translationLanguage.getLocale().equals(translationKey.getLocale())) {
			classes.add("tr8n_not_translated");
		} else if (translationLanguage.getLocale().equals(targetLanguage.getLocale())) {
			classes.add("tr8n_translated");
		} else {
			classes.add("tr8n_fallback");
		}
		
		sb.append("<" + element);
		sb.append(" class='" + StringUtils.join(classes.toArray(), " ") + "'");
		sb.append(" data-translation_key='" + translationKey.getKey() + "'");
		sb.append(">");
		sb.append(label);
		sb.append("</" + element + ">");
		return sb.toString();
	}

}
