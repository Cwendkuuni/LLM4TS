package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for CharSequenceTranslator
 */
public class CharSequenceTranslatorTest {

    /**
     * Test for translate method of default CharSequenceTranslator
     */
    @Test
    public void testDefaultTranslator() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                if (index >= input.length()) {
                    return -1;
                }
                char c = input.charAt(index);
                out.write(c);
                return Character.isSurrogate(c) ? 2 : 1;
            }
        };

        String input = "abcdefghijklmnopqrstuvwxyz";
        String expectedOutput = "abcdefghijklmnopqrstuvwxyz";
        String output = translator.translate(input);
        assertEquals(expectedOutput, output);
    }

    /**
     * Test for translate method with AggregateTranslator
     */
    @Test
    public void testAggregateTranslator() {
        CharSequenceTranslator translator1 = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                if (index >= input.length()) {
                    return -1;
                }
                char c = input.charAt(index);
                out.write('A');
                return 1;
            }
        };
        CharSequenceTranslator translator2 = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                if (index >= input.length()) {
                    return -1;
                }
                char c = input.charAt(index);
                out.write('B');
                return 1;
            }
        };

        CharSequenceTranslator aggregatedTranslator = new CharSequenceTranslator[] {translator1, translator2}.length > 0 ?
                new AggregateTranslator(new CharSequenceTranslator[] {translator1, translator2}) : null;

        String input = "abcdefghijklmnopqrstuvwxyz";
        String expectedOutput = "ABABABABABABABABABABAB";
        String output = aggregatedTranslator.translate(input);
        assertEquals(expectedOutput, output);

        CharSequenceTranslator[] emptyTranslator = new CharSequenceTranslator[0];
        String emptyInput = "";
        assertNull(emptyTranslator.length > 0 ? new AggregateTranslator(emptyTranslator).translate(emptyInput) : null);
    }

    /**
     * Test for translate method with null input
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullInput() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                if (index >= input.length()) {
                    return -1;
                }
                char c = input.charAt(index);
                out.write(c);
                return Character.isSurrogate(c) ? 2 : 1;
            }
        };

        String nullInput = null;
        translator.translate(nullInput, 0, new StringWriter());
    }

    /**
     * Test for translate method with null Writer
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullWriter() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                if (index >= input.length()) {
                    return -1;
                }
                char c = input.charAt(index);
                out.write(c);
                return Character.isSurrogate(c) ? 2 : 1;
            }
        };

        String input = "abc";
        translator.translate(input, 0, null);
    }

    /**
     * Test for translate method with empty input
     */
    @Test
    public void testEmptyInput() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                if (index >= input.length()) {
                    return -1;
                }
                char c = input.charAt(index);
                out.write(c);
                return Character.isSurrogate(c) ? 2 : 1;
            }
        };

        String input = "";
        String expectedOutput = "";
        String output = translator.translate(input);
        assertEquals(expectedOutput, output);
    }
}