package com.tr8n.core.tokenizers;

import org.apache.commons.lang.StringUtils;

import java.awt.font.TextAttribute;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AttributedStringTokenizer extends DecorationTokenizer {

    Map attributes;

    /**
     *
     * @param label
     */
    public AttributedStringTokenizer(String label) {
        this(label, null);
    }

    /**
     *
     * @param label
     * @param allowedTokenNames
     */
    public AttributedStringTokenizer(String label, List<String> allowedTokenNames) {
        super(label, allowedTokenNames);
    }

    /**
     *
     * @param tokensData
     * @return
     */
    public Object substitute(Map tokensData) {
        return substitute(tokensData, null);
    }

    /**
     *
     * @param expr
     * @param location
     * @return
     */
    protected String evaluate(Object expr, int location) {
        if (!(expr instanceof List))
            return expr.toString();

        List args = new ArrayList((List) expr);
        String token = (String) args.remove(0);

        List attributedSet = (List) this.attributes.get(token);
        if (attributedSet == null) {
            attributedSet = new ArrayList();
            this.attributes.put(attributedSet, token);
        }

        Map attribute = new HashMap();
        attribute.put("location", location);

        List processedValues = new ArrayList();
        for (Object arg : args) {
            String value = evaluate(arg, location);
            location += value.length();
            processedValues.add(value);
        }

        String value = StringUtils.join(processedValues.toArray(), "");

        attribute.put("length", value.length());
        attributedSet.add(attribute);

        return applyToken(token, value);
    }


    /**
     *
     * @param tokensData
     * @param options
     * @return
     */
    public Object substitute(Map tokensData, Map options) {
        this.tokensData = tokensData;
        this.options = options;
        this.attributes = new HashMap();

        String result = evaluate(this.expression, 0);

        AttributedString attributedString = new AttributedString(result);

        for (String tokenName : this.tokenNames) {
            if (!isTokenAllowed(tokenName))
                continue;

            Map styles = (Map) tokensData.get(tokenName);
            if (styles == null)
                continue;

            List ranges = (List) this.attributes.get(tokenName);
            if (ranges == null)
                continue;

            applyStyles(attributedString, styles, ranges);
        }

        return attributedString;
    }


//    public static final java.awt.font.TextAttribute FAMILY;
//    public static final java.awt.font.TextAttribute WEIGHT;
//    public static final java.lang.Float WEIGHT_EXTRA_LIGHT;
//    public static final java.lang.Float WEIGHT_LIGHT;
//    public static final java.lang.Float WEIGHT_DEMILIGHT;
//    public static final java.lang.Float WEIGHT_REGULAR;
//    public static final java.lang.Float WEIGHT_SEMIBOLD;
//    public static final java.lang.Float WEIGHT_MEDIUM;
//    public static final java.lang.Float WEIGHT_DEMIBOLD;
//    public static final java.lang.Float WEIGHT_BOLD;
//    public static final java.lang.Float WEIGHT_HEAVY;
//    public static final java.lang.Float WEIGHT_EXTRABOLD;
//    public static final java.lang.Float WEIGHT_ULTRABOLD;
//    public static final java.awt.font.TextAttribute WIDTH;
//    public static final java.lang.Float WIDTH_CONDENSED;
//    public static final java.lang.Float WIDTH_SEMI_CONDENSED;
//    public static final java.lang.Float WIDTH_REGULAR;
//    public static final java.lang.Float WIDTH_SEMI_EXTENDED;
//    public static final java.lang.Float WIDTH_EXTENDED;
//    public static final java.awt.font.TextAttribute POSTURE;
//    public static final java.lang.Float POSTURE_REGULAR;
//    public static final java.lang.Float POSTURE_OBLIQUE;
//    public static final java.awt.font.TextAttribute SIZE;
//    public static final java.awt.font.TextAttribute TRANSFORM;
//    public static final java.awt.font.TextAttribute SUPERSCRIPT;
//    public static final java.lang.Integer SUPERSCRIPT_SUPER;
//    public static final java.lang.Integer SUPERSCRIPT_SUB;
//    public static final java.awt.font.TextAttribute FONT;
//    public static final java.awt.font.TextAttribute CHAR_REPLACEMENT;
//    public static final java.awt.font.TextAttribute FOREGROUND;
//    public static final java.awt.font.TextAttribute BACKGROUND;
//    public static final java.awt.font.TextAttribute UNDERLINE;
//    public static final java.lang.Integer UNDERLINE_ON;
//    public static final java.awt.font.TextAttribute STRIKETHROUGH;
//    public static final java.lang.Boolean STRIKETHROUGH_ON;
//    public static final java.awt.font.TextAttribute RUN_DIRECTION;
//    public static final java.lang.Boolean RUN_DIRECTION_LTR;
//    public static final java.lang.Boolean RUN_DIRECTION_RTL;
//    public static final java.awt.font.TextAttribute BIDI_EMBEDDING;
//    public static final java.awt.font.TextAttribute JUSTIFICATION;
//    public static final java.lang.Float JUSTIFICATION_FULL;
//    public static final java.lang.Float JUSTIFICATION_NONE;
//    public static final java.awt.font.TextAttribute INPUT_METHOD_HIGHLIGHT;
//    public static final java.awt.font.TextAttribute INPUT_METHOD_UNDERLINE;
//    public static final java.lang.Integer UNDERLINE_LOW_ONE_PIXEL;
//    public static final java.lang.Integer UNDERLINE_LOW_TWO_PIXEL;
//    public static final java.lang.Integer UNDERLINE_LOW_DOTTED;
//    public static final java.lang.Integer UNDERLINE_LOW_GRAY;
//    public static final java.lang.Integer UNDERLINE_LOW_DASHED;
//    public static final java.awt.font.TextAttribute SWAP_COLORS;
//    public static final java.lang.Boolean SWAP_COLORS_ON;
//    public static final java.awt.font.TextAttribute NUMERIC_SHAPING;
//    public static final java.awt.font.TextAttribute KERNING;
//    public static final java.lang.Integer KERNING_ON;
//    public static final java.awt.font.TextAttribute LIGATURES;
//    public static final java.lang.Integer LIGATURES_ON;
//    public static final java.awt.font.TextAttribute TRACKING;
//    public static final java.lang.Float TRACKING_TIGHT;
//    public static final java.lang.Float TRACKING_LOOSE;
//
    private void applyStyles(AttributedString attributedString, Map styles, List ranges) {
//        attributedString.addAttribute(TextAttribute.FONT);
    }



}
