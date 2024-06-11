package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;

import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.junit.Test;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslateMethod() {
        CharSequenceTranslator translator = new MyCharSequenceTranslator();
        String input = "Hello World!";
        String expectedOutput = "48656c6c6f20576f726c6421";

        String output = translator.translate(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testEmptyInput() {
        CharSequenceTranslator translator = new MyCharSequenceTranslator();
        String input = "";
        String expectedOutput = "";

        String output = translator.translate(input);
        assertEquals(expectedOutput, output);
    }

    @Test(expected = NullPointerException.class)
    public void testNullInput() {
        CharSequenceTranslator translator = new MyCharSequenceTranslator();
        String input = null;

        translator.translate(input, null, null);
    }

    @Test
    public void testEmptyWriter() {
        CharSequenceTranslator translator = new MyCharSequenceTranslator();
        String input = "Hello World!";
        Writer out = new StringWriter();

        int codepointsConsumed = translator.translate(input, 0, out);
        assertEquals(0, codepointsConsumed);
        assertEquals("", out.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullWriter() {
        CharSequenceTranslator translator = new MyCharSequenceTranslator();
        String input = "Hello World!";
        Writer out = null;

        translator.translate(input, 0, out);
    }

    @Test
    public void testSurrogatePairs() {
        CharSequenceTranslator translator = new MyCharSequenceTranslator();
        String input = "\uD800\uDC00";
        String expectedOutput = "D800DC00";

        String output = translator.translate(input);
        assertEquals(expectedOutput, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidUnicodeCodePoint() {
        CharSequenceTranslator translator = new MyCharSequenceTranslator();
        String input = "\uFFFF";

        translator.translate(input, 0, new StringWriter());
    }

    @Test
    public void testLeadingAndTrailingWhitespaces() {
        CharSequenceTranslator translator = new MyCharSequenceTranslator();
        String input = "   Hello World!   ";
        String expectedOutput = "20202048656c6c6f20576f726c64202020";

        String output = translator.translate(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testASCIIAndUnicodeCharacters() {
        CharSequenceTranslator translator = new MyCharSequenceTranslator();
        String input = "Hello World!@#$%&";
        String expectedOutput = "48656c6c6f20576f726c6421%23%25%26";

        String output = translator.translate(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testControlCharacters() {
        CharSequenceTranslator translator = new MyCharSequenceTranslator();
        String input = "\u0001Hello World!\u0007";
        String expectedOutput = "\u000141\u0001H\u0001e\u0001l\u0001l\u0001o\u0001
Space\u0001W\u0001o\u0001r\u0001l\u0001d\u0001!\u0007";

        String output = translator.translate(input);
        assertEquals(expectedOutput, output);
    }
}