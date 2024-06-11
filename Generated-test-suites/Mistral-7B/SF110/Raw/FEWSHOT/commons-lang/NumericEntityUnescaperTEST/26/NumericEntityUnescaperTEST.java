package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;

import java.io.StringWriter;
import java.util.EnumSet;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    private static final String TEST_STRING = "&amp;&#x61;&#x62;&#x63;&#x64;&#xA0;&#x00A7;&#x00A9;&#x00AE;&#x00B4;&#x02C6;&#x02DC;&#x201C;&#x201D;&#x2032;&#x2033;&#x2039;&#x203A;&#x203E;&#x20AC;&#x2122;&#x2190;&#x2191;&#x2192;&#x21D2;";

    @Test
    public void testTranslate_noOptions() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();
        int length = TEST_STRING.length();
        for (int i = 0; i < length; ++i) {
            unescaper.translate(new StringCharSequence(TEST_STRING), 0, out);
        }
        assertEquals("ampampabcdefg....", out.toString());
    }

    @Test
    public void testTranslate_semiColonRequired() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
        StringWriter out = new StringWriter();
        int length = TEST_STRING.length();
        for (int i = 0; i < length; ++i) {
            unescaper.translate(new StringCharSequence(TEST_STRING), 0, out);
        }
        assertEquals("ampampabcdefg....\n", out.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslate_semiColonRequired_noSemiColon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
        StringWriter out = new StringWriter();
        int length = TEST_STRING.length();
        for (int i = 0; i < length - 1; ++i) {
            unescaper.translate(new StringCharSequence(TEST_STRING), 0, out);
        }
        unescaper.translate(new StringCharSequence(TEST_STRING), 0, out); // no semi-colon
    }

    @Test
    public void testTranslate_errorIfNoSemiColon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.errorIfNoSemiColon);
        StringWriter out = new StringWriter();
        int length = TEST_STRING.length();
        for (int i = 0; i < length; ++i) {
            unescaper.translate(new StringCharSequence(TEST_STRING), 0, out);
        }
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            unescaper.translate(new StringCharSequence(TEST_STRING), 0, out);
        });
        assertEquals("Semi-colon required at end of numeric entity", exception.getMessage());
    }

    @Test
    public void testTranslate_optionalSemiColon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
        StringWriter out = new StringWriter();
        int length = TEST_STRING.length();
        for (int i = 0; i < length; ++i) {
            unescaper.translate(new StringCharSequence(TEST_STRING), 0, out);
        }
        assertEquals("ampampabcdefg....", out.toString());
    }

    static class StringCharSequence implements CharSequence {
        private final String string;

        StringCharSequence(String string) {
            this.string = string;
        }

        @Override
        public int length() {
            return string.length();
        }

        @Override
        public char charAt(int index) {
            return string.charAt(index);
        }

        @Override
        public CharSequence subSequence(int start, int end) {
            return new StringCharSequence(string.subSequence(start, end));
        }
    }
}