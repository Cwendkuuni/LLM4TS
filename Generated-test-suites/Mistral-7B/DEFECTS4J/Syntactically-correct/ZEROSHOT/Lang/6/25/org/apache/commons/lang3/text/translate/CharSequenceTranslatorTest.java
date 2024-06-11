package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslatorFactory;
import org.junit.Test;

/**
 * Test class for CharSequenceTranslator.
 */
public class CharSequenceTranslatorTest {

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, int, Writer)}.
     */
    @Test
    public void testTranslate() throws Exception {
        CharSequenceTranslator translator = new IdentityTranslator();
        String input = "This is a test";

        Writer out = new StringWriter();
        int consumed = translator.translate(input, 0, out);
        assertEquals(input.length(), consumed);
        assertEquals(input, out.toString());

        out = new StringWriter();
        consumed = translator.translate(input, 5, out);
        assertEquals(1, consumed);
        assertEquals("test", out.toString());

        out = new StringWriter();
        consumed = translator.translate(input, 11, out);
        assertEquals(3, consumed);
        assertEquals("is a ", out.toString());
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence)}.
     */
    @Test
    public void testTranslateCharSequence() throws Exception {
        CharSequenceTranslator translator = new IdentityTranslator();
        String input = "This is a test";

        String output = translator.translate(input);
        assertEquals(input, output);

        output = translator.translate(input.substring(5));
        assertEquals("test", output);

        output = translator.translate(input.substring(11, 14));
        assertEquals("is a ", output);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)}.
     */
    @Test
    public void testTranslateCharSequenceWriter() throws Exception {
        CharSequenceTranslator translator = new IdentityTranslator();
        String input = "This is a test";

        StringWriter out = new StringWriter();
        translator.translate(input, out);
        assertEquals(input, out.toString());

        out = new StringWriter();
        translator.translate(input.substring(5), out);
        assertEquals("test", out.toString());

        out = new StringWriter();
        translator.translate(input.substring(11, 14), out);
        assertEquals("is a ", out.toString());
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with
     * IOException.
     */
    @Test(expected = RuntimeException.class)
    public void testTranslateCharSequenceWriterIOException() throws Exception {
        CharSequenceTranslator translator = new IdentityTranslator();
        String input = "This is a test";

        StringWriter out = new StringWriter() {
            @Override
            public void write(int c) throws IOException {
                throw new IOException();
            }
        };
        translator.translate(input, out);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)}.
     */
    @Test
    public void testAggregateTranslator() throws Exception {
        CharSequenceTranslator identity = new IdentityTranslator();
        CharSequenceTranslator upperCase = new UpperCaseTranslator();

        CharSequenceTranslator translator = identity.with(upperCase);
        String input = "This is a test";

        Writer out = new StringWriter();
        translator.translate(input, out);
        assertEquals("THIS IS A TEST", out.toString());

        out = new StringWriter();
        translator.translate(input.substring(5), out);
        assertEquals("TEST", out.toString());

        out = new StringWriter();
        translator.translate(input.substring(11, 14), out);
        assertEquals("IS A ", out.toString());
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence)}.
     */
    @Test
    public void testAggregateTranslatorCharSequence() throws Exception {
        CharSequenceTranslator identity = new IdentityTranslator();
        CharSequenceTranslator upperCase = new UpperCaseTranslator();

        CharSequenceTranslator translator = identity.with(upperCase);
        String input = "This is a test";

        String output = translator.translate(input);
        assertEquals("THIS IS A TEST", output);

        output = translator.translate(input.substring(5));
        assertEquals("TEST", output);

        output = translator.translate(input.substring(11, 14));
        assertEquals("IS A ", output);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with
     * IOException.
     */
    @Test(expected = RuntimeException.class)
    public void testAggregateTranslatorCharSequenceWriterIOException() throws Exception {
        CharSequenceTranslator identity = new IdentityTranslator();
        CharSequenceTranslator upperCase = new UpperCaseTranslator();

        CharSequenceTranslator translator = identity.with(upperCase);
        String input = "This is a test";

        StringWriter out = new StringWriter() {
            @Override
            public void write(int c) throws IOException {
                throw new IOException();
            }
        };
        translator.translate(input, out);
    }

    /**
     * Test method for {@link CharSequenceTranslator#hex(int)}.
     */
    @Test
    public void testHex() {
        for (int i = 0; i <= 0xFF; i++) {
            String hex = CharSequenceTranslator.hex(i);
            int dec = Integer.parseInt(hex, 16);
            assertEquals(i, dec);
            assertEquals(hex, CharSequenceTranslator.hex((int) Character.toUpperCase(Character.forDigit(hex.charAt(0), 16))
                    * 0x10 + Character.digit(hex.charAt(1), 16)));
        }
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, int, Writer)} with
     * empty input.
     */
    @Test
    public void testTranslateEmptyInput() throws Exception {
        CharSequenceTranslator translator = new IdentityTranslator();
        StringWriter out = new StringWriter();
        translator.translate(CharSequenceUtils.EMPTY_STRING, 0, out);
        assertNull(out.toString());
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with
     * empty input.
     */
    @Test
    public void testTranslateEmptyInputCharSequence() throws Exception {
        CharSequenceTranslator translator = new IdentityTranslator();
        String output = translator.translate(CharSequenceUtils.EMPTY_STRING);
        assertNull(output);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with
     * empty Writer.
     */
    @Test
    public void testTranslateEmptyWriter() throws Exception {
        CharSequenceTranslator translator = new IdentityTranslator();
        Writer out = null;
        translator.translate(CharSequenceUtils.EMPTY_STRING, out);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with
     * empty Writer.
     */
    @Test
    public void testTranslateEmptyWriterCharSequence() throws Exception {
        CharSequenceTranslator translator = new IdentityTranslator();
        String output = translator.translate(CharSequenceUtils.EMPTY_STRING);
        assertNull(output);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, int, Writer)} with
     * negative index.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testTranslateNegativeIndex() throws Exception {
        CharSequenceTranslator translator = new IdentityTranslator();
        String input = "This is a test";
        Writer out = new StringWriter();
        translator.translate(input, -1, out);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with
     * negative index.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testTranslateNegativeIndexCharSequence() throws Exception {
        CharSequenceTranslator translator = new IdentityTranslator();
        String input = "This is a test";
        translator.translate(input, -1);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with
     * null Writer.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateNullWriter() throws Exception {
        CharSequenceTranslator translator = new IdentityTranslator();
        String input = "This is a test";
        translator.translate(input, null);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with
     * null Writer.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateNullWriterCharSequence() throws Exception {
        CharSequenceTranslator translator = new IdentityTranslator();
        String input = "This is a test";
        translator.translate(input, null);
    }

    private static class IdentityTranslator extends CharSequenceTranslator {

        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            char c = Character.codePointAt(input, index);
            out.write(c);
            return Character.charCount(c);
        }

        @Override
        public String translate(CharSequence input) {
            return input.toString();
        }
    }

    private static class UpperCaseTranslator extends CharSequenceTranslator {

        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            char c = Character.codePointAt(input, index);
            out.write(Character.toUpperCase(c));
            return Character.charCount(c);
        }

        @Override
        public String translate(CharSequence input) {
            return new String(Character.toChars(input.chars().map(Character::toUpperCase).toArray()));
        }
    }
}