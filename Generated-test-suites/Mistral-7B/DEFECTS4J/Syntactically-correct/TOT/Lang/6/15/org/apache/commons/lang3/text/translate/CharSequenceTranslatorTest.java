package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test class for CharSequenceTranslator.
 */
public class CharSequenceTranslatorTest {

    /**
     * Test for default translator.
     */
    @Test
    public void testDefaultTranslator() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                if (index >= input.length()) {
                    return 0;
                }
                out.write(input.charAt(index));
                return Character.isSurrogate(input.charAt(index)) ? Character.charCount(input.charAt(index)) : 1;
            }
        };

        String testString = "abcdé ";
        String result = translator.translate(testString);
        String expected = new String(testString.getBytes());
        assertEquals(expected, result);
    }

    /**
     * Test for AggregateTranslator.
     */
    @Test
    public void testAggregateTranslator() {
        CharSequenceTranslator identityTranslator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                out.write(input.charAt(index));
                return Character.isSurrogate(input.charAt(index)) ? Character.charCount(input.charAt(index)) : 1;
            }
        };

        CharSequenceTranslator uppercaseTranslator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                if (index >= input.length()) {
                    return 0;
                }
                out.write(Character.toUpperCase(input.charAt(index)));
                return Character.isSurrogate(input.charAt(index)) ? Character.charCount(input.charAt(index)) : 1;
            }
        };

        CharSequenceTranslator translator = new AggregateTranslator(identityTranslator, uppercaseTranslator);

        String testString = "abcdé ";
        String result = translator.translate(testString);
        String expected = "ABCDÉ ";
        assertEquals(expected, result);
    }

    /**
     * Test for null input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullInput() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                throw new UnsupportedOperationException();
            }
        };
        translator.translate(null, 0, null);
    }

    /**
     * Test for empty input.
     */
    @Test
    public void testEmptyInput() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                if (input.length() > index) {
                    out.write(input.charAt(index));
                    return Character.isSurrogate(input.charAt(index)) ? Character.charCount(input.charAt(index)) : 1;
                } else {
                    return 0;
                }
            }
        };

        String result = translator.translate("");
        assertNull(result);
    }

    /**
     * Test for Writer failure.
     */
    @Test(expected = IOException.class)
    public void testWriterFailure() throws IOException {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                if (index >= input.length()) {
                    return 0;
                }
                out.write(input.charAt(index));
                return Character.isSurrogate(input.charAt(index)) ? Character.charCount(input.charAt(index)) : 1;
            }
        };

        Writer writer = new Writer() {
            @Override
            public void write(int c) throws IOException {
                throw new IOException("Writer failure");
            }

            @Override
            public void flush() throws IOException {
                // not used
            }

            @Override
            public void close() throws IOException {
                // not used
            }
        };

        translator.translate("test", 0, writer);
    }
}