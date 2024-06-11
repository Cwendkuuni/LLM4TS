package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Locale;

/**
 * Tests for the {@link CharSequenceTranslator} class.
 */
public class CharSequenceTranslatorTest {

    /**
     * Test method for the {@link CharSequenceTranslator#translate(CharSequence, Writer)} method.
     */
    @Test
    public void testTranslate() {
        CharSequenceTranslator translator = new IdentityTranslator();
        String input = " Hello, World! ";
        String expected = input;

        String result = translator.translate(input);
        Assert.assertEquals(expected, result);
    }

    /**
     * Test method for the {@link CharSequenceTranslator#translate(CharSequence, Writer)} method with {@link IOException}.
     */
    @Test(expected = RuntimeException.class)
    public void testTranslateIOException() throws IOException {
        CharSequenceTranslator translator = new IOExceptionTranslator();
        String input = "Hello, World!";
        translator.translate(input, new StringWriter(input.length() * 2));
    }

    /**
     * Test method for the {@link CharSequenceTranslator#translate(CharSequence, int, Writer)} method.
     */
    @Test
    public void testTranslateWithIndex() {
        CharSequenceTranslator translator = new IdentityTranslator();
        String input = "Hello, World!";
        StringWriter output = new StringWriter();

        int index = 0;
        int consumed = translator.translate(input, index, output);
        Assert.assertEquals(1, consumed);
        Assert.assertEquals("H", output.toString());

        index += consumed;
        consumed = translator.translate(input, index, output);
        Assert.assertEquals(1, consumed);
        Assert.assertEquals("e", output.toString());

        index += consumed;
        consumed = translator.translate(input, index, output);
        Assert.assertEquals(1, consumed);
        Assert.assertEquals("l", output.toString());

        index += consumed;
        consumed = translator.translate(input, index, output);
        Assert.assertEquals(1, consumed);
        Assert.assertEquals("l", output.toString());

        index += consumed;
        consumed = translator.translate(input, index, output);
        Assert.assertEquals(1, consumed);
        Assert.assertEquals("o", output.toString());

        index += consumed;
        consumed = translator.translate(input, index, output);
        Assert.assertEquals(1, consumed);
        Assert.assertEquals(" ", output.toString());

        index += consumed;
        consumed = translator.translate(input, index, output);
        Assert.assertEquals(1, consumed);
        Assert.assertEquals("H", output.toString());

        index += consumed;
        consumed = translator.translate(input, index, output);
        Assert.assertEquals(5, consumed);
        Assert.assertEquals("ello, W", output.toString());

        index += consumed;
        consumed = translator.translate(input, index, output);
        Assert.assertEquals(1, consumed);
        Assert.assertEquals("o", output.toString());

        index += consumed;
        consumed = translator.translate(input, index, output);
        Assert.assertEquals(4, consumed);
        Assert.assertEquals("ld!", output.toString());

        index += consumed;
        consumed = translator.translate(input, index, output);
        Assert.assertEquals(0, consumed);
    }

    /**
     * Test method for the {@link CharSequenceTranslator#translate(CharSequence)} method.
     */
    @Test
    public void testTranslateToString() {
        CharSequenceTranslator translator = new IdentityTranslator();
        String input = " Hello, World! ";
        String expected = input;

        String result = translator.translate(input);
        Assert.assertEquals(expected, result);
    }

    /**
     * Test method for the {@link CharSequenceTranslator#translate(CharSequence)} method with {@link IOException}.
     */
    @Test(expected = RuntimeException.class)
    public void testTranslateToStringIOException() throws IOException {
        CharSequenceTranslator translator = new IOExceptionTranslator();
        String input = "Hello, World!";
        translator.translate(input);
    }

    /**
     * Test method for the {@link CharSequenceTranslator#translate(CharSequence, Writer)} method with multiple translators.
     */
    @Test
    public void testTranslateWithMultipleTranslators() {
        CharSequenceTranslator translator1 = new UpperCaseTranslator();
        CharSequenceTranslator translator2 = new IdentityTranslator();
        CharSequenceTranslator translator3 = new LowerCaseTranslator();

        String input = "Hello, World!";
        StringWriter output = new StringWriter();

        CharSequenceTranslator translator = translator1.with(translator2, translator3);
        translator.translate(input, output);

        String expected = "HELLO, world!";
        String result = output.toString();
        Assert.assertEquals(expected, result);
    }

    /**
     * Test method for the {@link CharSequenceTranslator#hex(int)} method.
     */
    @Test
    public void testHex() {
        for (int i = 0; i < 256; i++) {
            String hex = CharSequenceTranslator.hex(i);
            int decimal = Integer.parseInt(hex, 16);
            Assert.assertEquals(i, decimal);
        }
    }

    /**
     * Identity translator that does not translate any sequence.
     */
    private static class IdentityTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            char c = Character.codePointAt(input, index);
            out.write(Character.toChars(c));
            return Character.charCount(c);
        }

        @Override
        public String translate(CharSequence input) {
            return input.toString();
        }
    }

    /**
     * Translator that throws an {@link IOException} when used.
     */
    private static class IOExceptionTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            throw new IOException();
        }

        @Override
        public String translate(CharSequence input) {
            throw new RuntimeException("Should not be called.");
        }
    }

    /**
     * Translator that translates any sequence to its upper case.
     */
    private static class UpperCaseTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            char c = Character.codePointAt(input, index);
            out.write(Character.toChars(Character.toUpperCase(c)));
            return Character.charCount(c);
        }

        @Override
        public String translate(CharSequence input) {
            return StringUtils.capitalize(input.toString());
        }
    }

    /**
     * Translator that translates any sequence to its lower case.
     */
    private static class LowerCaseTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            char c = Character.codePointAt(input, index);
            out.write(Character.toChars(Character.toLowerCase(c)));
            return Character.charCount(c);
        }

        @Override
        public String translate(CharSequence input) {
            return StringUtils.toLowerCase(input.toString());
        }
    }

    /**
     * Translator that merges a set of translators.
     */
    private static class AggregateTranslator extends CharSequenceTranslator {
        private final CharSequenceTranslator[] translators;

        public AggregateTranslator(CharSequenceTranslator[] translators) {
            this.translators = translators;
        }

        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            int consumed = 0;
            for (CharSequenceTranslator translator : translators) {
                consumed += translator.translate(input, index, out);
            }
            return consumed;
        }

        @Override
        public String translate(CharSequence input) {
            StringWriter output = new StringWriter();
            for (CharSequenceTranslator translator : translators) {
                translator.translate(input, output);
            }
            return output.toString();
        }
    }
}