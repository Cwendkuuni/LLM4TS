package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.EnumSet;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslateWithDefaultOptions() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&amp; &nbsp; &#160; &#x2020; &#xFFFD; &#x1F600; foo & bar";
        StringWriter out = new StringWriter();
        int index = 0;
        while (index < input.length()) {
            int charsTranslated = unescaper.translate(new StringReader(input), index, out);
            index += charsTranslated;
        }
        assertEquals("&amp; &nbsp; &#160; &#x2020; &#xFFFD; &#x1F600; foo & bar", out.toString());
    }

    @Test
    public void testTranslateWithSemiColonRequiredOption() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
        String input = "&amp; &nbsp; &#160; &#x2020; &#xFFFD; &#x1F600; foo";
        StringWriter out = new StringWriter();
        int index = 0;
        while (index < input.length()) {
            int charsTranslated = unescaper.translate(new StringReader(input), index, out);
            index += charsTranslated;
        }
        assertEquals("&amp; &nbsp; &#160; &#x2020; &#xFFFD; &#x1F600; foo", out.toString());
        assertThrows(IllegalArgumentException.class, () -> {
            String inputWithMissingSemiColon = "&amp; &nbsp; &#160; &#x2020; &#xFFFD; &#x1F600; foo";
            NumericEntityUnescaper unescaperWithMissingSemiColon = new NumericEntityUnescaper(OPTION.semiColonRequired);
            StringWriter outWithMissingSemiColon = new StringWriter();
            int indexWithMissingSemiColon = 0;
            while (indexWithMissingSemiColon < inputWithMissingSemiColon.length()) {
                int charsTranslatedWithMissingSemiColon = unescaperWithMissingSemiColon.translate(new StringReader(inputWithMissingSemiColon), indexWithMissingSemiColon, outWithMissingSemiColon);
                indexWithMissingSemiColon += charsTranslatedWithMissingSemiColon;
            }
        });
    }

    @Test
    public void testTranslateWithSemiColonOptionalOption() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
        String input = "&amp; &nbsp; &#160; &#x2020; &#xFFFD; &#x1F600; foo & bar";
        StringWriter out = new StringWriter();
        int index = 0;
        while (index < input.length()) {
            int charsTranslated = unescaper.translate(new StringReader(input), index, out);
            index += charsTranslated;
        }
        assertEquals("&amp; &nbsp; &#160; &#x2020; &#xFFFD; &#x1F600; foo & bar", out.toString());
        NumericEntityUnescaper unescaperWithoutSemiColonOptional = new NumericEntityUnescaper(new OPTION[]{OPTION.semiColonRequired});
        String inputWithoutSemiColonOptional = "&amp; &nbsp; &#160; &#x2020; &#xFFFD; &#x1F600; foo";
        StringWriter outWithoutSemiColonOptional = new StringWriter();
        int indexWithoutSemiColonOptional = 0;
        while (indexWithoutSemiColonOptional < inputWithoutSemiColonOptional.length()) {
            int charsTranslatedWithoutSemiColonOptional = unescaperWithoutSemiColonOptional.translate(new StringReader(inputWithoutSemiColonOptional), indexWithoutSemiColonOptional, outWithoutSemiColonOptional);
            indexWithoutSemiColonOptional += charsTranslatedWithoutSemiColonOptional;
        }
        assertEquals("&amp; &nbsp; &#160; &#x2020; &#xFFFD; &#x1F600; foo", outWithoutSemiColonOptional.toString());
    }

    @Test
    public void testTranslateWithErrorIfNoSemiColonOption() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.errorIfNoSemiColon);
        String input = "&amp; &nbsp; &#160; &#x2020; &#xFFFD; &#x1F600; foo";
        assertThrows(IllegalArgumentException.class, () -> {
            StringWriter out = new StringWriter();
            int index = 0;
            while (index < input.length()) {
                unescaper.translate(new StringReader(input), index, out);
                index++;
            }
        });
    }

    @Test
    public void testIsSet() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired, OPTION.errorIfNoSemiColon);
        assertTrue(unescaper.isSet(OPTION.semiColonRequired));
        assertTrue(unescaper.isSet(OPTION.errorIfNoSemiColon));
        NumericEntityUnescaper unescaperWithoutSemiColonRequired = new NumericEntityUnescaper(OPTION.errorIfNoSemiColon);
        assertTrue(unescaperWithoutSemiColonRequired.isSet(OPTION.errorIfNoSemiColon));
        assertFalse(unescaperWithoutSemiColonRequired.isSet(OPTION.semiColonRequired));
        NumericEntityUnescaper unescaperEmpty = new NumericEntityUnescaper();
        assertFalse(unescaperEmpty.isSet(OPTION.semiColonRequired));
        assertFalse(unescaperEmpty.isSet(OPTION.errorIfNoSemiColon));
    }
}