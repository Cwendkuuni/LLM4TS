package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslateWithSemiColonRequired() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
        char[] input = {'&', '#', 'x', '3', 'A', ';'};
        StringWriter out = new StringWriter();
        unescaper.translate(new String(input), 0, out);
        assertEquals("\u003A", out.toString());
    }

    @Test
    public void testTranslateWithoutSemiColonRequired() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        char[] input = {'&', '#', 'x', '3', 'A'};
        StringWriter out = new StringWriter();
        unescaper.translate(new String(input), 0, out);
        assertEquals('\u0033', out.toString().charAt(0));
        assertEquals('\u0041', out.toString().charAt(1));
    }

    @Test
    public void testTranslateWithErrorIfNoSemiColon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.errorIfNoSemiColon);
        char[] input = {'&', '#', 'x', '3'};
        StringWriter out = new StringWriter();
        assertThrows(IllegalArgumentException.class, () -> unescaper.translate(new String(input), 0, out));
    }

    @Test
    public void testTranslateWithSemiColonOptional() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
        char[] input = {'&', '#', 'x', '3'};
        StringWriter out = new StringWriter();
        unescaper.translate(new String(input), 0, out);
        assertEquals('\u0033', out.toString().charAt(0));
        assertEquals('\u0033', out.toString().charAt(out.toString().length() - 1));
    }

    @Test
    public void testTranslateHexWithSemiColonRequired() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
        char[] input = {'&', '#', 'x', '2', 'a', ';'};
        StringWriter out = new StringWriter();
        unescaper.translate(new String(input), 0, out);
        assertEquals("\u0026\u0023\u0061", out.toString());
    }

    @Test
    public void testTranslateHexWithErrorIfNoSemiColon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.errorIfNoSemiColon);
        char[] input = {'&', '#', 'x', '2', 'a'};
        StringWriter out = new StringWriter();
        assertThrows(IllegalArgumentException.class, () -> unescaper.translate(new String(input), 0, out));
    }

    @Test
    public void testTranslateInvalidHex() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        char[] input = {'&', '#', 'x', 'z'};
        StringWriter out = new StringWriter();
        assertThrows(NumberFormatException.class, () -> unescaper.translate(new String(input), 0, out));
    }

    @Test
    public void testTranslateOutOfRange() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        char[] input = {'&', '#', 'x', '100000'};
        StringWriter out = new StringWriter();
        assertThrows(IllegalArgumentException.class, () -> unescaper.translate(new String(input), 0, out));
    }

    @Test
    public void testTranslateLargeEntityWithSemiColonRequired() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
        char[] input = {'&', '#', 'x', '65535'};
        StringWriter out = new StringWriter();
        unescaper.translate(new String(input), 0, out);
        assertEquals("\uFFFE\uFFFE", out.toString());
    }

    @Test
    public void testTranslateLargeEntityWithSemiColonOptional() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
        char[] input = {'&', '#', 'x', '65535'};
        StringWriter out = new StringWriter();
        unescaper.translate(new String(input), 0, out);
        assertEquals("\uFFFE", out.toString().charAt(0));
        assertEquals("\uFFFE", out.toString().charAt(out.toString().length() - 1));
    }
}