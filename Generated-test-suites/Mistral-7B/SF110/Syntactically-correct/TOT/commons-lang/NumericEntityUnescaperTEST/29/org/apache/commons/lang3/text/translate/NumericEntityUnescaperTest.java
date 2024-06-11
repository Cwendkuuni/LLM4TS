package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.EnumSet;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_SimpleNumericEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
        StringWriter out = new StringWriter();
        CharSequence input = "&amp;#123;";
        int translatedLength = unescaper.translate(input, 0, out);
        Assertions.assertEquals(3, translatedLength);
        Assertions.assertEquals("\u007B", out.toString());
    }

    @Test
    public void testTranslate_HexNumericEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
        StringWriter out = new StringWriter();
        CharSequence input = "&amp;#x41;";
        int translatedLength = unescaper.translate(input, 0, out);
        Assertions.assertEquals(2, translatedLength);
        Assertions.assertEquals("A", out.toString());
    }

    @Test
    public void testTranslate_NumericEntityWithSemiColon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
        StringWriter out = new StringWriter();
        CharSequence input = "&amp;#123;";
        int translatedLength = unescaper.translate(input, 0, out);
        Assertions.assertEquals(4, translatedLength);
        String translated = out.toString();
        Assertions.assertTrue(StringUtils.endsWith(translated, ";"));
    }

    @Test
    public void testTranslate_NumericEntityWithoutSemiColon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
        StringWriter out = new StringWriter();
        CharSequence input = "&amp;#123";
        int translatedLength = unescaper.translate(input, 0, out);
        Assertions.assertEquals(3, translatedLength);
        String translated = out.toString();
        // It doesn't matter if the test passes or fails here, as long as it doesn't throw an exception
        Assertions.assertFalse(StringUtils.endsWith(translated, ";"));
    }

    @Test
    public void testTranslate_InvalidNumericEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.errorIfNoSemiColon);
        StringWriter out = new StringWriter();
        CharSequence input = "&amp;#123";
        Assertions.assertThrows(IllegalArgumentException.class, () -> unescaper.translate(input, 0, out));
    }

    @Test
    public void testTranslate_EmptyInput() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
        StringWriter out = new StringWriter();
        CharSequence input = "";
        int translatedLength = unescaper.translate(input, 0, out);
        Assertions.assertEquals(0, translatedLength);
        Assertions.assertEquals("", out.toString());
    }

    @Test
    public void testTranslate_NumericEntitiesInSequence() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
        StringWriter out = new StringWriter();
        CharSequence input = "&amp;#123; &amp;#x41; &amp;#127;";
        int translatedLength = unescaper.translate(input, 0, out);
        Assertions.assertEquals(11, translatedLength);
        String translated = out.toString();
        Assertions.assertTrue(StringUtils.endsWith(translated, ";"));
        StringBuilder expected = new StringBuilder();
        expected.append("\u007B\u0041\u007F");
        Assertions.assertEquals(expected.toString(), translated.substring(0, translated.length() - 1));
    }
}