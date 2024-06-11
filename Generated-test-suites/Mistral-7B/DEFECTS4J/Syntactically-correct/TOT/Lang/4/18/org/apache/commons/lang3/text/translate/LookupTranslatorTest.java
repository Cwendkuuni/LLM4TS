package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link LookupTranslator} class.
 */
public class LookupTranslatorTest {

    @Test
    public void testEmptyLookup() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{});
        String input = "";
        Writer out = new StringWriter();
        int index = 0;

        int translatedLength = translator.translate(StringUtils.EMPTY, index, out);
        assertEquals("", out.toString());
        assertEquals(0, translatedLength);
    }

    @Test
    public void testSingleCharacterLookup() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{"a", "z"});
        String input = "aaa";
        Writer out = new StringWriter();
        int index = 0;

        int translatedLength = translator.translate(input, index, out);
        assertEquals("zzz", out.toString());
        assertEquals(3, translatedLength);
    }

    @Test
    public void testShortestKey() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{"ab", "cd"});
        String input = "ab";
        Writer out = new StringWriter();
        int index = 0;

        int translatedLength = translator.translate(input, index, out);
        assertEquals("cd", out.toString());
        assertEquals(2, translatedLength);
    }

    @Test
    public void testLongestKey() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{"abcd", "efgh"});
        String input = "abcd";
        Writer out = new StringWriter();
        int index = 0;

        int translatedLength = translator.translate(input, index, out);
        assertEquals("efgh", out.toString());
        assertEquals(4, translatedLength);
    }

    @Test
    public void testSubsequenceNotFound() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{"ab", "cd"});
        String input = "acd";
        Writer out = new StringWriter();
        int index = 0;

        int translatedLength = translator.translate(input, index, out);
        assertEquals("", out.toString());
        assertEquals(0, translatedLength);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullLookup() {
        new LookupTranslator(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullKey() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{"ab", "cd"});
        String input = null;
        Writer out = new StringWriter();
        int index = 0;

        translator.translate(input, index, out);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testIndexOutOfBounds() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{"ab", "cd"});
        String input = "abc";
        Writer out = new StringWriter();
        int index = 2;

        translator.translate(input, index, out);
    }

    @Test(expected = IOException.class)
    public void testIOException() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{"ab", "cd"});
        String input = "ab";
        Writer out = new StringWriter() {
            @Override
            public void write(int c) throws IOException {
                throw new IOException();
            }
        };
        int index = 0;

        translator.translate(input, index, out);
    }
}