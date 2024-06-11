package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testIsSet() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
        assertTrue(unescaper.isSet(OPTION.semiColonRequired));
        assertFalse(unescaper.isSet(OPTION.semiColonOptional));
        assertFalse(unescaper.isSet(OPTION.errorIfNoSemiColon));

        unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
        assertFalse(unescaper.isSet(OPTION.semiColonRequired));
        assertTrue(unescaper.isSet(OPTION.semiColonOptional));
        assertFalse(unescaper.isSet(OPTION.errorIfNoSemiColon));

        unescaper = new NumericEntityUnescaper(OPTION.errorIfNoSemiColon);
        assertFalse(unescaper.isSet(OPTION.semiColonRequired));
        assertFalse(unescaper.isSet(OPTION.semiColonOptional));
        assertTrue(unescaper.isSet(OPTION.errorIfNoSemiColon));
    }

    @Test
    public void testTranslateWithSemiColonRequired() throws IOException {
        Writer out = new StringWriter();
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
        CharSequence input = new StringBuilder("&amp;#123;").append("&amp;#x7f;&amp;#xFF;").toString();
        int index = 0;

        while (index < input.length()) {
            int charsWritten = unescaper.translate(input, index, out);
            index += charsWritten;
        }

        assertEquals("&#123;&#x7f;&#xFF;", out.toString());
    }

    @Test
    public void testTranslateWithSemiColonOptional() throws IOException {
        Writer out = new StringWriter();
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
        CharSequence input = new StringBuilder("&amp;#123;").append("&amp;#x7f;&amp;#xFF;").toString();
        int index = 0;

        while (index < input.length()) {
            int charsWritten = unescaper.translate(input, index, out);
            index += charsWritten;
        }

        assertEquals("&#123;&#x7f;&#xFF", out.toString());
    }

    @Test
    public void testTranslateWithErrorIfNoSemiColon() throws IOException {
        Writer out = new StringWriter();
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.errorIfNoSemiColon);
        CharSequence input = new StringBuilder("&amp;#123;").append("&amp;#x7f;").toString();
        int index = 0;

        try {
            while (index < input.length()) {
                int charsWritten = unescaper.translate(input, index, out);
                index += charsWritten;
            }
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testTranslateInvalidHex() throws IOException {
        Writer out = new StringWriter();
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        CharSequence input = new StringBuilder("&amp;#xZ;").toString();
        int index = 0;

        try {
            while (index < input.length()) {
                int charsWritten = unescaper.translate(input, index, out);
                index += charsWritten;
            }
            fail("Expected 0, but got " + out.toString().length());
        } catch (Exception e) {
            // Expected
        }

        assertEquals("", out.toString());
    }

    @Test
    public void testTranslateInvalidDecimal() throws IOException {
        Writer out = new StringWriter();
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        CharSequence input = new StringBuilder("&amp;#z;").toString();
        int index = 0;

        try {
            while (index < input.length()) {
                int charsWritten = unescaper.translate(input, index, out);
                index += charsWritten;
            }
            fail("Expected 0, but got " + out.toString().length());
        } catch (Exception e) {
            // Expected
        }

        assertEquals("", out.toString());
    }

    @Test
    public void testTranslateValidDecimal() throws IOException {
        Writer out = new StringWriter();
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        CharSequence input = new StringBuilder("&amp;#123;").toString();
        int index = 0;

        while (index < input.length()) {
            int charsWritten = unescaper.translate(input, index, out);
            index += charsWritten;
        }

        assertEquals("123", out.toString());
    }

    @Test
    public void testTranslateValidHex() throws IOException {
        Writer out = new StringWriter();
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        CharSequence input = new StringBuilder("&amp;#x41;").toString();
        int index = 0;

        while (index < input.length()) {
            int charsWritten = unescaper.translate(input, index, out);
            index += charsWritten;
        }

        assertEquals("A", out.toString());
    }

    @Test
    public void testTranslateValidDecimalWithSemiColon() throws IOException {
        Writer out = new StringWriter();
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
        CharSequence input = new StringBuilder("&amp;#123;").toString();
        int index = 0;

        while (index < input.length()) {
            int charsWritten = unescaper.translate(input, index, out);
            index += charsWritten;
        }

        assertEquals("&#123;", out.toString());
    }

    @Test
    public void testTranslateValidHexWithSemiColon() throws IOException {
        Writer out = new StringWriter();
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
        CharSequence input = new StringBuilder("&amp;#x41;").toString();
        int index = 0;

        while (index < input.length()) {
            int charsWritten = unescaper.translate(input, index, out);
            index += charsWritten;
        }

        assertEquals("&#x41;", out.toString());
    }
}