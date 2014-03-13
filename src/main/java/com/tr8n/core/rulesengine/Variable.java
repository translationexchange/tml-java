package com.tr8n.core.rulesengine;

import com.tr8n.core.LanguageContext;

public interface Variable {

    public Object getValue(LanguageContext context, Object object);

}
