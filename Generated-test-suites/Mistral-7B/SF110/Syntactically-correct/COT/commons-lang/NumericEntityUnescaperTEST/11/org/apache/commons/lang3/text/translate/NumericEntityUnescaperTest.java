package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.EnumSet;

import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper.OPTION;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testConstructorWithNoOptions() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        assertTrue(unescaper.isSet(OPTION.semiColonRequired));
    }

    @Test
    public void testConstructorWithOptions() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional, OPTION.errorIfNoSemiColon);
        assertFalse(unescaper.isSet(OPTION.semiColonRequired));
        assertTrue(unescaper.isSet(OPTION.semiColonOptional));
        assertTrue(unescaper.isSet(OPTION.errorIfNoSemiColon));
    }

    @Test
    public void testIsSet() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
        assertTrue(unescaper.isSet(OPTION.semiColonRequired));

        unescaper = new NumericEntityUnescaper();
        assertFalse(unescaper.isSet(OPTION.semiColonRequired));
    }

    @Test
    public void testTranslate_ValidNumericEntity_DefaultOptions() throws Exception {
        String input = "This text contains &amp;#x1F344;&#x1F602; entities.";
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        StringWriter out = new StringWriter();
        unescaper.translate(new CharSequence() {
            @Override
            public int length() {
                return input.length();
            }

            @Override
            public char charAt(int index) {
                return input.charAt(index);
            }

            @Override
            public CharSequence subSequence(int start, int end) {
                return new String(input.substring(start, end));
            }

            @Override
            public String toString() {
                return input;
            }
        }, 0, out);

        String expectedOutput = "This text contains ⚡️❤️ entities.";
        assertEquals(expectedOutput, out.toString());
    }

    @Test
    public void testTranslate_ValidNumericEntity_SemiColonRequiredOption() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);

        String input = "This text contains &amp;#x1F344; entities.";
        StringWriter out = new StringWriter();
        unescaper.translate(new CharSequence() {
            @Override
            public int length() {
                return input.length();
            }

            @Override
            public char charAt(int index) {
                return input.charAt(index);
            }

            @Override
            public CharSequence subSequence(int start, int end) {
                return new String(input.substring(start, end));
            }

            @Override
            public String toString() {
                return input;
            }
        }, 0, out);

        String expectedOutput = "This text contains ⚡️❤️; entities.";
        assertEquals(expectedOutput, out.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslate_MissingSemiColon_SemiColonRequiredOption() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);

        String input = "This text contains &amp;#x1F344; entities.";
        StringWriter out = new StringWriter();
        unescaper.translate(new CharSequence() {
            @Override
            public int length() {
                return input.length();
            }

            @Override
            public char charAt(int index) {
                return input.charAt(index);
            }

            @Override
            public CharSequence subSequence(int start, int end) {
                return new String(input.substring(start, end));
            }

            @Override
            public String toString() {
                return input;
            }
        }, 0, out);

        fail("Expected IllegalArgumentException but got: " + out.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslate_MissingSemiColon_ErrorIfNoSemiColonOption() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.errorIfNoSemiColon);

        String input = "This text contains &amp;#x1F344; entities.";
        StringWriter out = new StringWriter();
        unescaper.translate(new CharSequence() {
            @Override
            public int length() {
                return input.length();
            }

            @Override
            public char charAt(int index) {
                return input.charAt(index);
            }

            @Override
            public CharSequence subSequence(int start, int end) {
                return new String(input.substring(start, end));
            }

            @Override
            public String toString() {
                return input;
            }
        }, 0, out);

        fail("Expected IllegalArgumentException but got: " + out.toString());
    }

    @Test
    public void testTranslate_InvalidHexNumericEntity() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        String input = "This text contains &amp;#xZz entities.";
        StringWriter out = new StringWriter();
        unescaper.translate(new CharSequence() {
            @Override
            public int length() {
                return input.length();
            }

            @Override
            public char charAt(int index) {
                return input.charAt(index);
            }

            @Override
            public CharSequence subSequence(int start, int end) {
                return new String(input.substring(start, end));
            }

            @Override
            public String toString() {
                return input;
            }
        }, 0, out);

        String expectedOutput = "This text contains &amp;#Zz entities.";
        assertEquals(expectedOutput, out.toString());
    }

    @Test
    public void testTranslate_ValidDecimalNumericEntity() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        String input = "This text contains &amp;#12345 entities.";
        StringWriter out = new StringWriter();
        unescaper.translate(new CharSequence() {
            @Override
            public int length() {
                return input.length();
            }

            @Override
            public char charAt(int index) {
                return input.charAt(index);
            }

            @Override
            public CharSequence subSequence(int start, int end) {
                return new String(input.substring(start, end));
            }

            @Override
            public String toString() {
                return input;
            }
        }, 0, out);

        String expectedOutput = "This text contains 🔵 entities.";
        assertEquals(expectedOutput, out.toString());
    }

    @Test
    public void testTranslate_ValidDecimalNumericEntityWithSemiColon() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);

        String input = "This text contains &amp;#12345; entities.";
        StringWriter out = new StringWriter();
        unescaper.translate(new CharSequence() {
            @Override
            public int length() {
                return input.length();
            }

            @Override
            public char charAt(int index) {
                return input.charAt(index);
            }

            @Override
            public CharSequence subSequence(int start, int end) {
                return new String(input.substring(start, end));
            }

            @Override
            public String toString() {
                return input;
            }
        }, 0, out);

        String expectedOutput = "This text contains 🔵; entities.";
        assertEquals(expectedOutput, out.toString());
    }

    @Test
    public void testTranslate_ValidHexNumericEntityWithSemiColon() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);

        String input = "This text contains &amp;#x1F344; entities.";
        StringWriter out = new StringWriter();
        unescaper.translate(new CharSequence() {
            @Override
            public int length() {
                return input.length();
            }

            @Override
            public char charAt(int index) {
                return input.charAt(index);
            }

            @Override
            public CharSequence subSequence(int start, int end) {
                return new String(input.substring(start, end));
            }

            @Override
            public String toString() {
                return input;
            }
        }, 0, out);

        String expectedOutput = "This text contains ⚡️❤️; entities.";
        assertEquals(expectedOutput, out.toString());
    }

    @Test
    public void testTranslate_InvalidDecimalNumericEntity() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        String input = "This text contains &amp;#29999 entities.";
        StringWriter out = new StringWriter();
        unescaper.translate(new CharSequence() {
            @Override
            public int length() {
                return input.length();
            }

            @Override
            public char charAt(int index) {
                return input.charAt(index);
            }

            @Override
            public CharSequence subSequence(int start, int end) {
                return new String(input.substring(start, end));
            }

            @Override
            public String toString() {
                return input;
            }
        }, 0, out);

        String expectedOutput = "This text contains &amp;#29999 entities.";
        assertEquals(expectedOutput, out.toString());
    }

    @Test
    public void testTranslate_InvalidHexNumericEntity() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        String input = "This text contains &amp;#xZZZZ entities.";
        StringWriter out = new StringWriter();
        unescaper.translate(new CharSequence() {
            @Override
            public int length() {
                return input.length();
            }

            @Override
            public char charAt(int index) {
                return input.charAt(index);
            }

            @Override
            public CharSequence subSequence(int start, int end) {
                return new String(input.substring(start, end));
            }

            @Override
            public String toString() {
                return input;
            }
        }, 0, out);

        String expectedOutput = "This text contains &amp;#xZZZZ entities.";
        assertEquals(expectedOutput, out.toString());
    }

    @Test
    public void testTranslate_EmptyInput() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        String input = "";
        StringWriter out = new StringWriter();
        unescaper.translate(new CharSequence() {
            @Override
            public int length() {
                return input.length();
            }

            @Override
            public char charAt(int index) {
                return input.charAt(index);
            }

            @Override
            public CharSequence subSequence(int start, int end) {
                return new String(input.substring(start, end));
            }

            @Override
            public String toString() {
                return input;
            }
        }, 0, out);

        String expectedOutput = "";
        assertEquals(expectedOutput, out.toString());
    }

    @Test
    public void testTranslate_InputWithMultipleNumericEntities() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        String input = "This text contains &amp;#123 &amp;#x1F344; &amp;#12345 entities.";
        StringWriter out = new StringWriter();
        unescaper.translate(new CharSequence() {
            @Override
            public int length() {
                return input.length();
            }

            @Override
            public char charAt(int index) {
                return input.charAt(index);
            }

            @Override
            public CharSequence subSequence(int start, int end) {
                return new String(input.substring(start, end));
            }

            @Override
            public String toString() {
                return input;
            }
        }, 0, out);

        String expectedOutput = "This text contains 🔵 ⚡️❤️ 🔵45687 entities.";
        assertEquals(expectedOutput, out.toString());
    }
}