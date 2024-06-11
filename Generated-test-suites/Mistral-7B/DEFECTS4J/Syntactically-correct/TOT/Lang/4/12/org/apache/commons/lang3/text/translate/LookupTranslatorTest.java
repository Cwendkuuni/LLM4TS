package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.text.translate.LookupTranslator;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for LookupTranslator.
 */
public class LookupTranslatorTest {

    /**
     * Test translate method with valid lookup table and input sequence.
     */
    @Test
    public void testTranslateValidLookupTable() {
        CharSequence[][] lookup = new CharSequence[][]{{"Hello", "Hi"}, {"World", "Earth"}, {"Java", "Python"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        CharSequence input = "Hello World Java";

        Writer writer = new StringWriter();
        int index = 0;
        translator.translate(input, index, writer);

        String expected = "Hi Earth Python";
        String actual = writer.toString();

        assertEquals(expected, actual);
    }

    /**
     * Test translate method with empty lookup table.
     */
    @Test
    public void testTranslateEmptyLookupTable() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{});
        CharSequence input = "Test";

        Writer writer = new StringWriter();
        int index = 0;
        translator.translate(input, index, writer);

        String actual = writer.toString();
        assertEquals("", actual);
    }

    /**
     * Test translate method with input sequence shorter than the shortest lookup table key.
     */
    @Test
    public void testTranslateInputSequenceShorterThanShortestLookupTableKey() {
        CharSequence[][] lookup = new CharSequence[][]{{"Hello", "Hi"}, {"World", "Earth"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        CharSequence input = "Hello";

        Writer writer = new StringWriter();
        int index = 0;
        translator.translate(input, index, writer);

        String actual = writer.toString();
        assertEquals("Hi", actual);
    }

    /**
     * Test translate method with input sequence longer than the longest lookup table key.
     */
    @Test
    public void testTranslateInputSequenceLongerThanLongestLookupTableKey() {
        CharSequence[][] lookup = new CharSequence[][]{{"Hello", "Hi"}, {"World", "Earth"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        CharSequence input = "HelloWorld";

        Writer writer = new StringWriter();
        int index = 0;
        translator.translate(input, index, writer);

        String actual = writer.toString();
        assertEquals("HiEarth", actual);
    }

    /**
     * Test translate method with invalid lookup table key.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateInvalidLookupTableKey() {
        CharSequence[][] lookup = new CharSequence[][]{{"Hello", "Hi"}, {"World", "Earth"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        CharSequence input = "Invalid";

        Writer writer = new StringWriter();
        int index = 0;
        translator.translate(input, index, writer);

        // This test should fail with an IllegalArgumentException as the key "Invalid" is not defined in the lookup table.
    }
}