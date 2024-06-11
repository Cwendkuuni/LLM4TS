package org.apache.commons.lang3.text.translate;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for LookupTranslator.
 */
public class LookupTranslatorTest {

    @Test
    public void testEmptyLookupTable() {
        CharSequence[][] lookup = new CharSequence[0][2];
        LookupTranslator translator = new LookupTranslator(lookup);
        CharSequence input = "";
        Writer out = new StringWriter();

        int translationLength = translator.translate(input, 0, out);
        assertEquals("", out.toString());
        assertEquals(0, translationLength);
    }

    @Test
    public void testSingleCharacterLookup() {
        CharSequence[][] lookup = new CharSequence[][]{{"A", "Z"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        CharSequence input = "A";
        Writer out = new StringWriter();

        int translationLength = translator.translate(input, 0, out);
        assertEquals("Z", out.toString());
        assertEquals(1, translationLength);
    }

    @Test
    public void testMultipleCharacterLookup() {
        CharSequence[][] lookup = new CharSequence[][]{{"hi", "hello"}, {"there", "greetings"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        CharSequence input = "hi there";
        Writer out = new StringWriter();

        int translationLength = translator.translate(input, 0, out);
        assertEquals("hello greetings", out.toString());
        assertEquals(7, translationLength);
    }

    @Test
    public void testLookupWithShortInput() {
        CharSequence[][] lookup = new CharSequence[][]{{"hi", "hello"}, {"there", "greetings"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        CharSequence input = "h";
        Writer out = new StringWriter();

        int translationLength = translator.translate(input, 0, out);
        assertEquals("", out.toString());
        assertEquals(0, translationLength);
    }

    @Test
    public void testLookupWithLongInput() {
        CharSequence[][] lookup = new CharSequence[][]{{"hi", "hello"}, {"there", "greetings"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        CharSequence input = "this is a test of the lookup translator";
        Writer out = new StringWriter();

        int translationLength = translator.translate(input, 0, out);
        assertEquals("this is a test of the lookup translator", out.toString());
        assertEquals(43, translationLength);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullInput() {
        CharSequence[][] lookup = new CharSequence[][]{{"hi", "hello"}, {"there", "greetings"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        CharSequence input = null;
        Writer out = new StringWriter();

        translator.translate(input, 0, out);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testIndexOutOfBounds() {
        CharSequence[][] lookup = new CharSequence[][]{{"hi", "hello"}, {"there", "greetings"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        CharSequence input = "there";
        Writer out = new StringWriter();

        int index = input.length();
        translator.translate(input, index, out);
    }

    @Test(expected = IOException.class)
    public void testIOException() {
        CharSequence[][] lookup = new CharSequence[][]{{"hi", "hello"}, {"there", "greetings"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        CharSequence input = "hi there";
        Writer out = new StringWriter() {
            @Override
            public void write(int c) throws IOException {
                throw new IOException();
            }
        };

        translator.translate(input, 0, out);
    }
}