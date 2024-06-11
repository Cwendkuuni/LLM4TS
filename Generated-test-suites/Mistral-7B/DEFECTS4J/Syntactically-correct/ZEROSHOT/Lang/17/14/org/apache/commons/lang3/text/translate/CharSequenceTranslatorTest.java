package org.apache.commons.lang3.text.translate;

import static org.apache.commons.lang3.text.translate.CharSequenceTranslator.hex;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Locale;

import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.junit.Test;

/**
 * Test class for {@link CharSequenceTranslator}.
 */
public class CharSequenceTranslatorTest {

    /**
     * Test translate method with empty input.
     */
    @Test
    public void testTranslateEmptyInput() {
        CharSequenceTranslator translator = new IdentityTranslator();
        String expected = "";
        String actual = translator.translate(CharSequence.EMPTY);
        assertEquals(expected, actual);
    }

    /**
     * Test translate method with null input.
     */
    @Test
    public void testTranslateNullInput() {
        CharSequenceTranslator translator = new IdentityTranslator();
        assertNull(translator.translate(null));
    }

    /**
     * Test translate method with single character input.
     */
    @Test
    public void testTranslateSingleCharacterInput() {
        CharSequenceTranslator translator = new IdentityTranslator();
        char input = 'A';
        String expected = String.valueOf(input);
        String actual = translator.translate(input);
        assertEquals(expected, actual);
    }

    /**
     * Test translate method with multi-character input.
     */
    @Test
    public void testTranslateMultiCharacterInput() {
        CharSequenceTranslator translator = new IdentityTranslator();
        String input = "ABC";
        String expected = input;
        String actual = translator.translate(input);
        assertEquals(expected, actual);
    }

    /**
     * Test translate method with custom translator.
     */
    @Test
    public void testTranslateCustomTranslator() throws IOException {
        CharSequenceTranslator inputTranslator = new IdentityTranslator();
        CharSequenceTranslator outputTranslator = new HexTranslator();
        CharSequenceTranslator compositeTranslator = inputTranslator.with(outputTranslator);
        String input = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String expected = "4142434445464748494A4B4C";
        String actual = compositeTranslator.translate(input).toString();
        assertEquals(expected, actual);
    }

    /**
     * Test abstract translate method with empty input.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testAbstractTranslateEmptyInput() throws IOException {
        AbstractCharSequenceTranslator translator = new AbstractCharSequenceTranslator() {
            @Override
            protected void writeTo(Writer out, char[] buffer, int offset, int length) throws IOException {
                // not implemented
            }
        };
        StringWriter writer = new StringWriter();
        CharSequence input = CharSequence.EMPTY;
        int index = 0;
        translator.translate(input, index, writer);
    }

    /**
     * Test abstract translate method with null output.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAbstractTranslateNullOutput() throws IOException {
        AbstractCharSequenceTranslator translator = new AbstractCharSequenceTranslator() {
            @Override
            protected void writeTo(Writer out, char[] buffer, int offset, int length) throws IOException {
                // not implemented
            }
        };
        CharSequence input = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        int index = 0;
        translator.translate(input, index, null);
    }

    /**
     * Test abstract translate method with single character input.
     */
    @Test
    public void testAbstractTranslateSingleCharacterInput() throws IOException {
        AbstractCharSequenceTranslator translator = new AbstractCharSequenceTranslator() {
            @Override
            protected void writeTo(Writer out, char[] buffer, int offset, int length) throws IOException {
                out.write(getCharacterAt(buffer, offset));
            }
        };
        StringWriter writer = new StringWriter();
        CharSequence input = Character.of(0x41);
        int index = 0;
        int consumed = translator.translate(input, index, writer);
        assertEquals(1, consumed);
        String expected = Character.valueOf(0x41).toString();
        String actual = writer.toString();
        assertEquals(expected, actual);
    }

    /**
     * Test abstract translate method with multi-character input.
     */
    @Test
    public void testAbstractTranslateMultiCharacterInput() throws IOException {
        AbstractCharSequenceTranslator translator = new AbstractCharSequenceTranslator() {
            @Override
            protected void writeTo(Writer out, char[] buffer, int offset, int length) throws IOException {
                for (int i = offset; i < offset + length; i++) {
                    out.write(getCharacterAt(buffer, i));
                }
            }
        };
        StringWriter writer = new StringWriter();
        CharSequence input = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        int index = 0;
        int consumed = translator.translate(input, index, writer);
        assertEquals(input.length(), consumed);
        String expected = input.toString();
        String actual = writer.toString();
        assertEquals(expected, actual);
    }

    /**
     * Test abstract translate method with surrogate pair input.
     */
    @Test
    public void testAbstractTranslateSurrogatePairInput() throws IOException {
        AbstractCharSequenceTranslator translator = new AbstractCharSequenceTranslator() {
            @Override
            protected void writeTo(Writer out, char[] buffer, int offset, int length) throws IOException {
                for (int i = offset; i < offset + length; i++) {
                    out.write(getCharacterAt(buffer, i));
                }
            }
        };
        StringWriter writer = new StringWriter();
        CharSequence input = Character.toString((char) 0xD800, (char) 0xDBFF);
        int index = 0;
        int consumed = translator.translate(input, index, writer);
        assertEquals(input.length() * 2, consumed);
        String expected = Character.toString((char) 0x10000 + Character.codePointAt(input, 0), Character.codePointAt(input, 1)).toString();
        String actual = writer.toString();
        assertEquals(expected, actual);
    }

    private char getCharacterAt(char[] buffer, int index) {
        return buffer[index];
    }

    /**
     * Identity translator that does not change the input.
     */
    private static class IdentityTranslator extends AbstractCharSequenceTranslator {
        @Override
        protected void writeTo(Writer out, char[] buffer, int offset, int length) throws IOException {
            for (int i = offset; i < offset + length; i++) {
                out.write(getCharacterAt(buffer, i));
            }
        }
    }

    /**
     * Hex translator that converts each character to its hexadecimal representation.
     */
    private static class HexTranslator extends AbstractCharSequenceTranslator {
        @Override
        protected void writeTo(Writer out, char[] buffer, int offset, int length) throws IOException {
            for (int i = offset; i < offset + length; i++) {
                out.write(hex(getCharacterAt(buffer, i)).toCharArray());
                out.write(" ");
            }
        }
    }

    /**
     * Abstract translator with writeTo method for testing abstract translate method.
     */
    private abstract static class AbstractCharSequenceTranslator extends CharSequenceTranslator {
        @Override
        protected void writeTo(Writer out, char[] buffer, int offset, int length) throws IOException {
            // not implemented
        }
    }

}