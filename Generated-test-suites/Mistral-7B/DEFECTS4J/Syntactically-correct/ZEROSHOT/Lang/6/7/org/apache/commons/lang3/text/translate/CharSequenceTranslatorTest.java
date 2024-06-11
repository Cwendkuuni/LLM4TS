package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.StringWriter;
import java.util.Locale;

import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator.AggregateTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator.DefaultTranslator;
import org.junit.Test;

/**
 * Unit tests for {@link CharSequenceTranslator} class.
 */
public class CharSequenceTranslatorTest {

    @Test
    public void testEmptyTranslation() {
        CharSequenceTranslator translator = new DefaultTranslator();
        String input = "";
        String expectedOutput = "";
        String output = translator.translate(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testTranslation() {
        CharSequenceTranslator translator = new DefaultTranslator();
        String input = "Hello World!";
        String expectedOutput = "Hello World!";
        String output = translator.translate(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testTranslationWithWriter() throws IOException {
        CharSequenceTranslator translator = new DefaultTranslator();
        String input = "Hello World!";
        StringWriter writer = new StringWriter();
        int consumed = translator.translate(input, 0, writer);
        String output = writer.toString();
        assertEquals("Hello World!", output);
        assertEquals(input.length(), consumed);
    }

    @Test
    public void testTranslationWithAggregateTranslator() {
        CharSequenceTranslator identityTranslator = new DefaultTranslator();
        CharSequenceTranslator uppercaseTranslator = new TranslateUpperCase();
        CharSequenceTranslator translator = identityTranslator.with(uppercaseTranslator);
        String input = "Hello World!";
        String expectedOutput = "HELLO WORLD!";
        String output = translator.translate(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testTranslationWithNullInput() {
        CharSequenceTranslator translator = new DefaultTranslator();
        String input = null;
        String output = translator.translate(input);
        assertNull(output);
    }

    @Test
    public void testTranslationWithNullOutput() {
        CharSequenceTranslator translator = new DefaultTranslator();
        String input = "Hello World!";
        Writer output = null;
        try {
            translator.translate(input, output);
            fail("Expected IOException");
        } catch (IOException ex) {
            // Expected
        }
    }

    @Test
    public void testTranslationWithEmptyOutput() {
        CharSequenceTranslator translator = new DefaultTranslator();
        String input = "Hello World!";
        Writer output = new StringWriter(0);
        try {
            translator.translate(input, output);
            fail("Expected IOException");
        } catch (IOException ex) {
            // Expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslationWithNullOutputWriter() {
        CharSequenceTranslator translator = new DefaultTranslator();
        String input = "Hello World!";
        Writer output = null;
        translator.translate(input, output);
        fail("Expected IllegalArgumentException");
    }

    /**
     * Translator that translates characters to uppercase.
     */
    private static class TranslateUpperCase extends CharSequenceTranslator {

        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            char c = Character.codePointAt(input, index);
            if (Character.isUpperCase(c)) {
                // Consume the character
                return Character.charCount(c);
            }
            char[] chars = Character.toChars(Character.toUpperCase(c));
            out.write(chars);
            return chars.length;
        }

    }

}