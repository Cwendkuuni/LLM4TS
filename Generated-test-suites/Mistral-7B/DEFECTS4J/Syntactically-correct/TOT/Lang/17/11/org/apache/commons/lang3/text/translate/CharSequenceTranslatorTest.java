package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for CharSequenceTranslator.
 */
public class CharSequenceTranslatorTest {

    /**
     * Test for translate method with empty input.
     */
    @Test
    public void testTranslateEmptyInput() {
        CharSequenceTranslator translator = new IdentityTranslator();
        String expected = "";
        String actual = translator.translate(CharSequence.EMPTY);
        assertEquals(expected, actual);
    }

    /**
     * Test for translate method with simple input.
     */
    @Test
    public void testTranslateSimpleInput() {
        CharSequenceTranslator translator = new IdentityTranslator();
        String input = "Hello World!";
        String expected = "Hello World!";
        String actual = translator.translate(input);
        assertEquals(expected, actual);
    }

    /**
     * Test for translate method with surrogate pairs input.
     */
    @Test
    public void testTranslateSurrogatePairsInput() {
        CharSequenceTranslator translator = new IdentityTranslator();
        String input = "😃";
        String expected = "😃";
        String actual = translator.translate(input);
        assertEquals(expected, actual);
    }

    /**
     * Test for translate method with error input - null CharSequence.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateNullInput() {
        CharSequenceTranslator translator = new IdentityTranslator();
        translator.translate(null);
    }

    /**
     * Test for translate method with error input - null Writer.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateNullWriter() {
        CharSequenceTranslator translator = new IdentityTranslator();
        translator.translate("Test", null);
    }

    /**
     * Test for translate method with error input - IOException.
     */
    @Test(expected = IOException.class)
    public void testTranslateIOException() throws IOException {
        StringWriter writer = new StringWriter();
        CharSequenceTranslator translator = new ExceptionThrowingTranslator();
        translator.translate("Test", writer);
    }

    /**
     * Test for with method.
     */
    @Test
    public void testWith() {
        CharSequenceTranslator translator1 = new IdentityTranslator();
        CharSequenceTranslator translator2 = new IdentityTranslator();
        CharSequenceTranslator mergedTranslator = translator1.with(translator2);
        CharSequenceTranslator expectedMergedTranslator = new AggregateTranslator(new CharSequenceTranslator[] {translator1, translator2});
        assertEquals(expectedMergedTranslator.getClass(), mergedTranslator.getClass());
    }

    /**
     * Identity translator that does not change any codepoints.
     */
    public static class IdentityTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            return Character.isSurrogate(Character.codePointAt(input, index)) ? Character.toInt32(Character.highSurrogate(input, index)) + Character.toInt32(Character.lowSurrogate(input, index + 1)) : Character.codePointCount(Character.toChars(Character.codePointAt(input, index))) * Character.charCount(Character.toChars(Character.codePointAt(input, index)));
        }

        @Override
        public String translate(CharSequence input) {
            return input.toString();
        }

        @Override
        public void translate(CharSequence input, Writer out) throws IOException {
            super.translate(input, out);
        }
    }

    /**
     * Exception throwing translator that throws IOException for every call.
     */
    public static class ExceptionThrowingTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            throw new IOException();
        }
    }
}