package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for LookupTranslator.
 */
public class LookupTranslatorTest {

    /**
     * Test empty lookup table.
     */
    @Test
    public void testEmptyLookupTable() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{});
        String input = "";
        String expectedOutput = "";
        Writer out = new StringWriter();
        int result = translator.translate(StringUtils.wrap(input), 0, out);
        assertEquals(expectedOutput, out.toString());
        assertEquals(0, result);
    }

    /**
     * Test basic lookup.
     */
    @Test
    public void testBasicLookup() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"hello", "hi"}, {"world", "planet"}});
        String input = "hello world";
        String expectedOutput = "hi planet";
        Writer out = new StringWriter();
        int result = translator.translate(StringUtils.wrap(input), 0, out);
        assertEquals(expectedOutput, out.toString());
        assertEquals(11, result);
    }

    /**
     * Test lookup with overlapping keys.
     */
    @Test
    public void testLookupWithOverlappingKeys() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"hello", "hi"}, {"world", "planet"}, {"ello", "hey"}});
        String input = "hello world hello";
        String expectedOutput = "hi planet hey";
        Writer out = new StringWriter();
        int result = translator.translate(StringUtils.wrap(input), 0, out);
        assertEquals(expectedOutput, out.toString());
        assertEquals(18, result);
    }

    /**
     * Test lookup with key shorter than input.
     */
    @Test
    public void testLookupWithKeyShorterThanInput() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"hello", "hi"}, {"world", "planet"}});
        String input = "helloworld";
        String expectedOutput = "hi planet";
        Writer out = new StringWriter();
        int result = translator.translate(StringUtils.wrap(input), 0, out);
        assertEquals(expectedOutput, out.toString());
        assertEquals(7, result);
    }

    /**
     * Test lookup with key longer than input.
     */
    @Test
    public void testLookupWithKeyLongerThanInput() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"hello", "hi"}, {"world", "planet"}});
        String input = "hello";
        String expectedOutput = "hi";
        Writer out = new StringWriter();
        int result = translator.translate(StringUtils.wrap(input), 0, out);
        assertEquals(expectedOutput, out.toString());
        assertEquals(5, result);
    }

    /**
     * Test lookup with empty key.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testLookupWithEmptyKey() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"hello", "hi"}, {"world", "planet"}});
        String input = "";
        Writer out = new StringWriter();
        translator.translate(StringUtils.wrap(input), 0, out);
    }

    /**
     * Test lookup with null key.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testLookupWithNullKey() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"hello", "hi"}, {"world", "planet"}});
        String input = null;
        Writer out = new StringWriter();
        translator.translate(StringUtils.wrap(input), 0, out);
    }

    /**
     * Test lookup with null lookup table.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testLookupWithNullLookupTable() {
        LookupTranslator translator = new LookupTranslator(null);
        String input = "helloworld";
        Writer out = new StringWriter();
        translator.translate(StringUtils.wrap(input), 0, out);
    }

    /**
     * Test lookup with negative index.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testLookupWithNegativeIndex() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"hello", "hi"}, {"world", "planet"}});
        String input = "helloworld";
        Writer out = new StringWriter();
        translator.translate(StringUtils.wrap(input), -1, out);
    }
}