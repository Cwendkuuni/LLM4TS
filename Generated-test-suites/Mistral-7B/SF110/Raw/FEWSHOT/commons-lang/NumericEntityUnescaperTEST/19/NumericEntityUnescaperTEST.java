package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;

import java.io.StringWriter;
import java.util.Arrays;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslateWithDefaultOptions() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        StringWriter out = new StringWriter();
        CharSequence input = "&amp;#128;&#x41;Hello &world; &&";

        int translatedLength = unescaper.translate(input, 0, out);
        assertEquals("Hello world Hello ", out.toString().substring(0, translatedLength));
        assertEquals(8, translatedLength);
    }

    @Test
    public void testTranslateWithSemiColonRequiredOption() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);

        StringWriter out = new StringWriter();
        CharSequence input = "&amp;#128;&#x41;Hello &world; &&";

        int translatedLength = unescaper.translate(input, 0, out);
        assertEquals("Hello &world;\n", out.toString().substring(0, translatedLength));
        assertEquals(10, translatedLength);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithErrorIfNoSemiColonOption() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.errorIfNoSemiColon);

        StringWriter out = new StringWriter();
        CharSequence input = "&amp;#128;Hello &world; &&";

        unescaper.translate(input, 0, out);
    }

    @Test
    public void testTranslateWithSemiColonOptionalOption() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);

        StringWriter out = new StringWriter();
        CharSequence input = "&amp;#128;&#x41;Hello &world; &&";

        int translatedLength = unescaper.translate(input, 0, out);
        assertEquals("Hello world Hello ", out.toString().substring(0, translatedLength));
        assertEquals(8, translatedLength);
    }

    @Test
    public void testTranslateEmptyInput() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        StringWriter out = new StringWriter();
        CharSequence input = "";

        int translatedLength = unescaper.translate(input, 0, out);
        assertEquals("", out.toString());
        assertEquals(0, translatedLength);
    }

    @Test
    public void testTranslateInputWithHexEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        StringWriter out = new StringWriter();
        CharSequence input = "&amp;#x1F8B0;";

        int translatedLength = unescaper.translate(input, 0, out);
        assertEquals("\uD83D\uDC1E", out.toString().substring(0, translatedLength));
        assertEquals(3, translatedLength);
    }

    @Test
    public void testTranslateInputWithInvalidEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        StringWriter out = new StringWriter();
        CharSequence input = "&amp;#x1F8BZ;";

        int translatedLength = unescaper.translate(input, 0, out);
        assertEquals("", out.toString());
        assertEquals(0, translatedLength);
    }

    @Test
    public void testTranslateInputWithMultipleEntities() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        StringWriter out = new StringWriter();
        CharSequence input = "&amp;#128;&#x41; &&#x303;m&apos; &world;&#132; &\\u00A9 &amp;#65536;";

        int translatedLength = unescaper.translate(input, 0, out);
        assertEquals("Hello ēm world ® ", out.toString().substring(0, translatedLength));
        assertEquals(16, translatedLength);
    }

    @Test
    public void testTranslateInputWithInvalidEntityAndSemiColonRequiredOption() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);

        StringWriter out = new StringWriter();
        CharSequence input = "&amp;#x1F8BZ;";

        int translatedLength = unescaper.translate(input, 0, out);
        assertEquals("", out.toString());
        assertEquals(0, translatedLength);
    }
}