package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * CharSequenceTranslatorTest class contains test cases for the CharSequenceTranslator abstract class.
 */
public class CharSequenceTranslatorTest {

    /**
     * Test for the default translator.
     */
    @Test
    public void testDefaultTranslator() throws Exception {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                if (index >= input.length()) {
                    return 0;
                }
                char c = Character.toChars(Character.codePointAt(input, index))[0];
                out.write(c);
                return Character.charCount(c);
            }
        };

        String input = "Hello World!";
        String expectedOutput = "Hello World!";
        String output = translator.translate(input);
        assertEquals(expectedOutput, output);
    }

    /**
     * Test for the AggregateTranslator.
     */
    @Test
    public void testAggregateTranslator() throws Exception {
        CharSequenceTranslator translator1 = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                if (index >= input.length()) {
                    return 0;
                }
                char c = Character.toChars(Character.codePointAt(input, index))[0];
                out.write(c + 1);
                return Character.charCount(c);
            }
        };

        CharSequenceTranslator translator2 = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                if (index >= input.length()) {
                    return 0;
                }
                char c = Character.toChars(Character.codePointAt(input, index))[0];
                out.write(c - 1);
                return Character.charCount(c);
            }
        };

        CharSequenceTranslator translator = new AggregateTranslator(new CharSequenceTranslator[] {translator1, translator2});

        String input = "Hello World!";
        String expectedOutput = "HmLmMlMnLnNoRlRk";
        String output = translator.translate(input);
        assertEquals(expectedOutput, output);
    }

    /**
     * Test for the empty CharSequence.
     */
    @Test
    public void testEmptyCharSequence() throws Exception {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                if (index >= input.length()) {
                    return 0;
                }
                char c = Character.toChars(Character.codePointAt(input, index))[0];
                out.write(c);
                return Character.charCount(c);
            }
        };

        String input = "";
        assertNull(translator.translate(input));
    }

    /**
     * Test for the null CharSequence.
     */
    @Test(expected = NullPointerException.class)
    public void testNullCharSequence() throws Exception {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                if (index >= input.length()) {
                    return 0;
                }
                char c = Character.toChars(Character.codePointAt(input, index))[0];
                out.write(c);
                return Character.charCount(c);
            }
        };

        CharSequence input = null;
        translator.translate(input);
    }

    /**
     * Test for the IOException when writing to the Writer.
     */
    @Test(expected = IOException.class)
    public void testIOException() throws Exception {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                throw new IOException();
            }
        };

        String input = "Hello World!";
        translator.translate(input);
    }

    /**
     * Test for the empty Writer.
     */
    @Test
    public void testEmptyWriter() throws Exception {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                if (index >= input.length()) {
                    return 0;
                }
                char c = Character.toChars(Character.codePointAt(input, index))[0];
                out.write(c);
                return Character.charCount(c);
            }
        };

        Writer out = StringUtils.EMPTY_WRITER;
        String input = "Hello World!";
        String expectedOutput = "Hello World!";
        String output = translator.translate(input);
        assertEquals(expectedOutput, output);
        assertNotNull(output);
    }
}