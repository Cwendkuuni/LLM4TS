package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.CharSequenceTranslator;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.apache.commons.lang3.text.translate.CharSequences;
import org.junit.Test;
import java.io.StringWriter;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Test class for CharSequenceTranslator.
 */
public class CharSequenceTranslatorTest {

    /**
     * Test translate method with empty input.
     */
    @Test
    public void testEmptyTranslate() throws Exception {
        CharSequenceTranslator translator = new IdentityTranslator();
        String expected = "";
        String actual = translator.translate(CharSequences.EMPTY);
        assertEquals(expected, actual);
    }

    /**
     * Test translate method with simple input.
     */
    @Test
    public void testSimpleTranslate() throws Exception {
        CharSequenceTranslator translator = new IdentityTranslator();
        String input = "Hello, World!";
        String expected = "Hello, World!";
        String actual = translator.translate(CharSequences.of(input));
        assertEquals(expected, actual);
    }

    /**
     * Test translate method with multi-byte characters.
     */
    @Test
    public void testMultiByteTranslate() throws Exception {
        CharSequenceTranslator translator = new IdentityTranslator();
        String input = "你好，世界！";
        String expected = "你好，世界！";
        String actual = translator.translate(CharSequences.of(input));
        assertEquals(expected, actual);
    }

    /**
     * Test translate method with error condition (null input).
     */
    @Test(expected = NullPointerException.class)
    public void testNullInputTranslate() throws Exception {
        CharSequenceTranslator translator = new IdentityTranslator();
        translator.translate(null, new StringWriter());
    }

    /**
     * Test translate method with error condition (negative index).
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testNegativeIndexTranslate() throws Exception {
        String input = "Hello, World!";
        CharSequenceTranslator translator = new IdentityTranslator();
        translator.translate(CharSequences.of(input), -1, new StringWriter());
    }

    /**
     * Test translate method with error condition (writer is null).
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullWriterTranslate() throws Exception {
        String input = "Hello, World!";
        CharSequenceTranslator translator = new IdentityTranslator();
        translator.translate(CharSequences.of(input), 0, null);
    }

    /**
     * Test with method with multiple translators.
     */
    @Test
    public void testWithTranslators() throws Exception {
        CharSequenceTranslator identity = new IdentityTranslator();
        CharSequenceTranslator hex = new HexTranslator();

        CharSequenceTranslator translator = identity.with(hex);

        String input = "1234ABCDEF";
        String expected = "123441534253434445464447444544464447";
        String actual = translator.translate(CharSequences.of(input)).toString();
        assertEquals(expected, actual);
    }

    /**
     * Identity translator that simply returns the input.
     */
    private static class IdentityTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (out == null) {
                return 0;
            }
            int length = input.length();
            if (index < 0 || index > length) {
                throw new IndexOutOfBoundsException();
            }
            char c = Character.codePointAt(input, index);
            out.write(c);
            return Character.charCount(c);
        }
    }

    /**
     * Hex translator that converts characters to their hexadecimal representation.
     */
    private static class HexTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (out == null) {
                return 0;
            }
            int length = input.length();
            if (index < 0 || index > length) {
                throw new IndexOutOfBoundsException();
            }
            char c = Character.codePointAt(input, index);
            String hex = CharSequenceTranslator.hex(Character.toInt32(c));
            out.write(hex.toCharArray(), 0, hex.length());
            return hex.length();
        }
    }
}