package com.translationexchange.core.tools;

import com.translationexchange.core.Base;

import java.util.Map;

/**
 * Created by ababenko on 9/8/16.
 */
public class Tools extends Base {
    private String translationCenter;
    private String mobileTranslationCenter;
    private String mobileTranslationCenterKey;

    /**
     * Constructor from attributes
     *
     * @param attributes a {@link Map} object.
     */
    public Tools(Map<String, Object> attributes) {
        super(attributes);
    }

    public String getTranslationCenter() {
        return translationCenter;
    }

    public void setTranslationCenter(String translationCenter) {
        this.translationCenter = translationCenter;
    }

    public String getMobileTranslationCenter() {
        return mobileTranslationCenter;
    }

    public void setMobileTranslationCenter(String mobileTranslationCenter) {
        this.mobileTranslationCenter = mobileTranslationCenter;
    }

    public String getMobileTranslationCenterKey() {
        return mobileTranslationCenterKey;
    }

    public void setMobileTranslationCenterKey(String mobileTranslationCenterKey) {
        this.mobileTranslationCenterKey = mobileTranslationCenterKey;
    }

    /**
     * Updates object's attributes
     *
     * @param attributes a {@link Map} object.
     */
    @Override
    public void updateAttributes(Map<String, Object> attributes) {
        if (attributes == null || attributes.isEmpty())
            return;
        setTranslationCenter((String) attributes.get("translation_center"));
        setMobileTranslationCenter((String) attributes.get("mobile_translation_center"));
        setMobileTranslationCenterKey((String) attributes.get("mobile_translation_center_key"));
    }
}
