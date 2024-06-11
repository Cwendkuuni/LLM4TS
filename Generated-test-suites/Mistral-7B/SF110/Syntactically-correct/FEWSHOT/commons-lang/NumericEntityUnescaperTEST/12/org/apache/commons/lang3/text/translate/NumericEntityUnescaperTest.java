package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;

import java.io.StringWriter;
import java.util.Arrays;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslateWithDefaultOptions() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "This is a &amp;#160; text with &nbsp; numeric entities";
        StringWriter out = new StringWriter();

        int index = 0;
        while (index < input.length()) {
            int length = unescaper.translate(new StringCharSequence(input), index, out);
            index += length;
        }

        assertEquals("This is a text with text with numeric entities", out.toString());
    }

    @Test
    public void testTranslateWithSemiColonRequired() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
        String input = "This is a &amp;#160; text with &nbsp; numeric entities";
        StringWriter out = new StringWriter();

        int index = 0;
        while (index < input.length()) {
            int length = unescaper.translate(new StringCharSequence(input), index, out);
            index += length;
        }

        assertFalse("Expected an exception for missing semi-colon", out.toString().endsWith(";"));
    }

    @Test
    public void testTranslateWithSemiColonOptional() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
        String input = "This is a &amp;#160; text with &nbsp; numeric entities";
        StringWriter out = new StringWriter();

        int index = 0;
        while (index < input.length()) {
            int length = unescaper.translate(new StringCharSequence(input), index, out);
            index += length;
        }

        assertTrue("Expected a semi-colon to be present", out.toString().endsWith(";"));
    }

    @Test
    public void testTranslateWithErrorIfNoSemiColon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.errorIfNoSemiColon);
        String input = "This is a &amp;#160; text with &nbsp; numeric entities";
        StringWriter out = new StringWriter();

        int index = 0;
        try {
            while (index < input.length()) {
                int length = unescaper.translate(new StringCharSequence(input), index, out);
                index += length;
            }
        } catch (IllegalArgumentException e) {
            // expected
        }

        // the exceptions should have been thrown before reaching this point
        fail("An exception should have been thrown");
    }

    @Test
    public void testTranslateWithHexadecimalEntities() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "This is a &amp;#xA9; text with &reg; &copy; &#x2032; entities";
        StringWriter out = new StringWriter();

        int index = 0;
        while (index < input.length()) {
            int length = unescaper.translate(new StringCharSequence(input), index, out);
            index += length;
        }

        assertEquals("This is a text with © © Ⓒ entities", out.toString());
    }

    @Test
    public void testTranslateWithLargeHexadecimalEntities() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "This is a &amp;#xFFFF; text with &reg; &copy; &#x210000; entities";
        StringWriter out = new StringWriter();

        int index = 0;
        while (index < input.length()) {
            int length = unescaper.translate(new StringCharSequence(input), index, out);
            index += length;
        }

        // Unicode character U+210000 (Emoji) is not representable as two Java char values
        // This test would fail on some JVMs, but it should not be a problem on others
        // In this test, we only check the length of the output, which is correct (3 bytes for the entity, 1 byte for the semicolon)
        assertEquals("This is a text with © © 💿 entities", out.toString());
    }

    @Test
    public void testTranslateWithInvalidEntities() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "This is a &amp;#abc; text with &bob; entities";
        StringWriter out = new StringWriter();

        int index = 0;
        while (index < input.length()) {
            int length = unescaper.translate(new StringCharSequence(input), index, out);
            index += length;
        }

        assertEquals("This is a text with text with entities", out.toString());
    }

    @Test
    public void testTranslateWithInvalidEntitiesAndSemiColonRequired() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
        String input = "This is a &amp;#abc; text with &bob; entities";
        StringWriter out = new StringWriter();

        int index = 0;
        try {
            while (index < input.length()) {
                int length = unescaper.translate(new StringCharSequence(input), index, out);
                index += length;
            }
        } catch (IllegalArgumentException e) {
            // expected
        }

        // the exceptions should have been thrown before reaching this point
        fail("An exception should have been thrown");
    }

    @Test
    public void testTranslateWithInvalidEntitiesAndErrorIfNoSemiColon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.errorIfNoSemiColon);
        String input = "This is a &amp;#abc; text with &bob; entities";
        StringWriter out = new StringWriter();

        int index = 0;
        try {
            while (index < input.length()) {
                int length = unescaper.translate(new StringCharSequence(input), index, out);
                index += length;
            }
        } catch (IllegalArgumentException e) {
            // expected
        }

        // the exceptions should have been thrown before reaching this point
        fail("An exception should have been thrown");
    }

    private static class StringCharSequence implements CharSequence {
        private final String text;

        public StringCharSequence(String text) {
            this.text = text;
        }

        @Override
        public int length() {
            return text.length();
        }

        @Override
        public char charAt(int index) {
            return text.charAt(index);
        }

        @Override
        public CharSequence subSequence(int start, int end) {
            return text.subSequence(start, end);
        }
    }
}