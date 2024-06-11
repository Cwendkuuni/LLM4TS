package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;

/**
 * Tests for {@link NumericEntityUnescaper} class
 *
 * @author John Doe
 */
public class NumericEntityUnescaperTest {

    private static final String TEST_INPUT = "&#[x00F0]xy&#[x26];Hello &#[x22]World&#[x22];!";
    private static final String TEST_OUTPUT = "élx&y&€" + "Hello " + "\"World\"";

    @Test
    public void testUnescapeNumericEntities() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = TEST_INPUT;
        String output = TEST_OUTPUT;

        StringBuilder result = new StringBuilder();
        int index = 0;
        int length = input.length();
        while (index < length) {
            int translatedLength = unescaper.translate(new CharSequence() {
                @Override
                public CharSequence subSequence(int start, int end) {
                    return CharSequenceUtils.subSequence(input, start, end);
                }

                @Override
                public int length() {
                    return length;
                }
            }, index, new StringBuilder(output.substring(0, result.length())));

            result.append(output.substring(result.length(), result.length() + translatedLength));
            index += translatedLength;
        }

        assertEquals(output, result.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidNumericEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&invalid";
        CharSequence sequence = () -> input;

        unescaper.translate(sequence, 0, new StringBuilder());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testNegativeIndex() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&#[x00F0]";
        CharSequence sequence = () -> input;

        unescaper.translate(sequence, -1, new StringBuilder());
    }

    @Test(expected = NullPointerException.class)
    public void testNullWriter() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&#[x00F0]";
        CharSequence sequence = () -> input;

        unescaper.translate(sequence, 0, null);
    }

    @Test(expected = IOException.class)
    public void testIOException() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&#[x00F0]";
        Writer writer = new Writer() {
            @Override
            public void write(int c) throws IOException {
                throw new IOException();
            }

            @Override
            public void write(char[] cbuf, int off, int len) throws IOException {
                throw new IOException();
            }

            @Override
            public void flush() throws IOException {
                // Do nothing
            }

            @Override
            public void close() throws IOException {
                // Do nothing
            }
        };
        CharSequence sequence = () -> input;

        unescaper.translate(sequence, 0, writer);
    }
}