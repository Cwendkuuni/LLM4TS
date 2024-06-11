package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslateWithDefaultOptions() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        Writer out = new StringWriter();
        for (int i = 0; i < 100; ++i) {
            CharSequence input = "&" + Integer.toHexString(Math.abs(i)) + (i % 2 == 0 ? ";" : "");
            int length = unescaper.translate(input, 0, out);
            String result = out.toString();
            assertEquals(Character.toString((char) i), result.substring(0, length - 1));
            out.getBuffer().setLength(0);
        }
    }

    @Test
    public void testTranslateWithSemiColonRequiredOption() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);

        Writer out = new StringWriter();
        for (int i = 0; i < 100; ++i) {
            CharSequence input = "&" + Integer.toHexString(Math.abs(i));
            int length = unescaper.translate(input, 0, out);
            String result = out.toString();
            assertEquals(Character.toString((char) i) + ";", result);
            out.getBuffer().setLength(0);
        }
    }

    @Test
    public void testTranslateWithSemiColonOptionalOption() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);

        Writer out = new StringWriter();
        for (int i = 0; i < 100; ++i) {
            CharSequence input = "&" + Integer.toHexString(Math.abs(i)) + (i % 2 == 0 ? ";" : "");
            int length = unescaper.translate(input, 0, out);
            String result = out.toString();
            assertEquals(Character.toString((char) i), result.substring(0, length));
            out.getBuffer().setLength(0);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithNoSemiColonAndErrorIfNoSemiColonOption() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.errorIfNoSemiColon);

        Writer out = new StringWriter();
        for (int i = 0; i < 100; ++i) {
            CharSequence input = "&" + Integer.toHexString(Math.abs(i));
            unescaper.translate(input, 0, out);
        }
    }

    @Test
    public void testTranslateWithInvalidEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        Writer out = new StringWriter();
        CharSequence input = "&%#";
        int length = unescaper.translate(input, 0, out);
        assertEquals(0, length);
    }

    @Test
    public void testTranslateWithHexEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        Writer out = new StringWriter();
        CharSequence input = "&#x1234";
        int length = unescaper.translate(input, 0, out);
        String result = out.toString();
        assertEquals("\u1234", result.substring(0, length));
        out.getBuffer().setLength(0);
    }

    @Test
    public void testTranslateWithOctalEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        Writer out = new StringWriter();
        CharSequence input = "&#0123";
        int length = unescaper.translate(input, 0, out);
        String result = out.toString();
        assertEquals("\u0123", result.substring(0, length));
        out.getBuffer().setLength(0);
    }
}