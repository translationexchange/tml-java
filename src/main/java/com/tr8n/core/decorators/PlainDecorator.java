package com.tr8n.core.decorators;

import java.util.Map;

import com.tr8n.core.Language;
import com.tr8n.core.TranslationKey;

public class PlainDecorator implements Decorator {

	public Object decorate(Object translatedLabel, Language translationLanguage, Language targetLanguage, TranslationKey translationKey, Map<String, Object> options) {
		return translatedLabel;
	}

}
