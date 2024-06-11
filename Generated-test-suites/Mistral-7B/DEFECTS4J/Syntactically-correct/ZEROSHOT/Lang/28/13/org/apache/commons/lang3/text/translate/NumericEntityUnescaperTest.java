package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;

import java.io.StringWriter;
import java.io.Writer;

import org.junit.Test;

/**
 * Test class for {@link NumericEntityUnescaper}
 *
 * @author Johnson.dong
 */
public class NumericEntityUnescaperTest {

    /**
     * Test translating a simple numeric entity.
     */
    @Test
    public void testTranslateSimpleNumericEntity() {
        String input = "&amp;#160;";
        String expected = " ";
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();
        CharSequence cs = CharSequenceUtils.subSequence(input, 0, input.length());

        int length = translator.translate(cs, 0, out);
        assertEquals(expected, out.toString());
        assertEquals(1, length);
    }

    /**
     * Test translating a hexadecimal numeric entity.
     */
    @Test
    public void testTranslateHexadecimalNumericEntity() {
        String input = "&amp;#x27;";
        String expected = "\u0027";
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();
        CharSequence cs = CharSequenceUtils.subSequence(input, 0, input.length());

        int length = translator.translate(cs, 0, out);
        assertEquals(expected, out.toString());
        assertEquals(1, length);
    }

    /**
     * Test translating a numeric entity with a leading '&' and a trailing ';'.
     */
    @Test
    public void testTranslateNumericEntityWithLeadingAndTrailingSymbols() {
        String input = "&amp;#123;&#123;";
        String expected = "\u007B\u007B";
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();
        CharSequence cs = CharSequenceUtils.subSequence(input, 0, input.length());

        int length = translator.translate(cs, 0, out);
        assertEquals(expected, out.toString());
        assertEquals(4, length);
    }

    /**
     * Test translating a numeric entity with a leading '&' but no trailing ';'.
     */
    @Test
    public void testTranslateNumericEntityWithLeadingSymbolButNoTrailingSymbol() {
        String input = "&amp;#123";
        String expected = "";
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();
        CharSequence cs = CharSequenceUtils.subSequence(input, 0, input.length());

        int length = translator.translate(cs, 0, out);
        assertEquals(expected, out.toString());
        assertEquals(0, length);
    }

    /**
     * Test translating a non-numeric entity.
     */
    @Test
    public void testTranslateNonNumericEntity() {
        String input = "&amp;#45;";
        String expected = "";
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();
        CharSequence cs = CharSequenceUtils.subSequence(input, 0, input.length());

        int length = translator.translate(cs, 0, out);
        assertEquals(expected, out.toString());
        assertEquals(0, length);
    }

    /**
     * Test translating an empty input.
     */
    @Test
    public void testTranslateEmptyInput() {
        String input = "";
        String expected = "";
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();
        CharSequence cs = CharSequenceUtils.subSequence(input, 0, input.length());

        int length = translator.translate(cs, 0, out);
        assertEquals(expected, out.toString());
        assertEquals(0,
            length);
    }
}