package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.apache.commons.lang3.text.translate.CharArrayTranslator;
import org.apache.commons.lang3.text.translate.IdentityTranslator;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for CharSequenceTranslator.
 */
public class CharSequenceTranslatorTest {

    /**
     * Test for the identity translator.
     */
    @Test
    public void testIdentityTranslator() {
        CharSequenceTranslator translator = new IdentityTranslator();
        String input = "Hello World!";
        String result = translator.translate(input);
        assertEquals(input, result);
    }

    /**
     * Test for the char array translator.
     */
    @Test
    public void testCharArrayTranslator() {
        CharSequenceTranslator translator = new CharArrayTranslator();
        char[] input = {'H', 'e', 'l', 'l', 'o', ' ', 'W', 'o', 'r', 'l', 'd', '!'};
        char[] result = translator.translate(input, 0, input.length);
        assertArrayEquals(input, result);
    }

    /**
     * Test for the aggregate translator.
     */
    @Test
    public void testAggregateTranslator() {
        CharSequenceTranslator identityTranslator = new IdentityTranslator();
        CharSequenceTranslator hexTranslator = new CharArrayTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                if (index >= input.length()) {
                    return 0;
                }
                int codepoint = Character.codePointAt(input, index);
                String hex = CharSequenceTranslator.hex(codepoint);
                out.write(hex.toCharArray());
                return 2;
            }
        };
        CharSequenceTranslator translator = new AggregateTranslator(new CharSequenceTranslator[] {identityTranslator, hexTranslator});
        String input = "Hello World!";
        String result = translator.translate(input);
        String expected = "48656c6c6f20576f726c6421";
        assertEquals(expected, result);
    }

    /**
     * Test for the translate method with an empty CharSequence.
     */
    @Test
    public void testEmptyCharSequence() {
        CharSequenceTranslator translator = new IdentityTranslator();
        String input = "";
        String result = translator.translate(input);
        assertEquals(input, result);
    }

    /**
     * Test for the translate method with a null CharSequence.
     */
    @Test(expected = NullPointerException.class)
    public void testNullCharSequence() {
        CharSequenceTranslator translator = new IdentityTranslator();
        String input = null;
        translator.translate(input);
    }

    /**
     * Test for the translate method with a Writer that throws an IOException.
     */
    @Test(expected = RuntimeException.class)
    public void testIOException() {
        CharSequenceTranslator translator = new IdentityTranslator();
        String input = "Hello World!";
        StringWriter writer = new StringWriter() {
            @Override
            public void write(int c, CharSequence cs, int start, int length) throws IOException {
                throw new IOException();
            }
        };
        try {
            translator.translate(input, writer);
        } catch (IOException e) {
            // expected
        }
    }
}