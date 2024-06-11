package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for NumericEntityUnescaper.
 */
public class NumericEntityUnescaperTest {

    @Test
    public void testTranslateSimple() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = new StringWriter();

        String input = "&#[x00A9];"; // Copyright symbol
        int lengthBefore = out.length();
        unescaper.translate(new StringReader(input), 0, out);
        String result = out.toString();

        assertEquals("Copyright symbol should be translated correctly\n", "&#169;", result.substring(lengthBefore, lengthBefore + 2));
        assertEquals("Input length should be the same after translation\n", input.length(), result.length());
    }

    @Test
    public void testTranslateError() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = new StringWriter();

        String input = "&%#@!;"; // Invalid entity

        int lengthBefore = out.length();
        unescaper.translate(new StringReader(input), 0, out);
        String result = out.toString();

        assertEquals("Invalid entity should not be translated\n", input, result);
        assertEquals("Input length should be the same after translation\n", input.length(), result.length());
    }

    @Test
    public void testTranslateLongEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = new StringWriter();

        String input = "&#[x110000];"; // Longest Unicode character (U+1F000)

        int lengthBefore = out.length();
        unescaper.translate(new StringReader(input), 0, out);
        String result = out.toString();

        assertEquals("Longest Unicode character should be translated correctly\n", "\uD800\uDC00", result.substring(lengthBefore, lengthBefore + 2));
        assertEquals("Input length should be the same after translation\n", input.length(), result.length());
    }

    @Test
    public void testTranslateMixedEntities() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer out = new StringWriter();

        String input = "&#[x00A9] &#[x26] &#[x1F1F4] &#[#032] &#[x1F600];"; // Copyright symbol, ampersand, Unicode character U+1F1F4, space, Unicode character U+1F600

        int lengthBefore = out.length();
        unescaper.translate(new StringReader(input), 0, out);
        String result = out.toString();

        assertEquals("Mixed entities should be translated correctly\n", "&#169; &amp; &#128516; &nbsp; &#128576;", result);
        assertEquals("Input length should be the same after translation\n", input.length(), result.length());
    }
}