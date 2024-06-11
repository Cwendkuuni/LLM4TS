package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.test.SimpleTest;
import org.apache.commons.lang3.text.translate.LookupTranslator;

import java.io.StringWriter;
import java.util.Arrays;

/**
 * Tests for {@link LookupTranslator}.
 */
public class LookupTranslatorTest extends SimpleTest {

    /**
     * Test translation with empty input.
     */
    public void testEmptyInput() {
        final CharSequence[][] lookup = new CharSequence[][]{{""}, {""}};
        final LookupTranslator translator = new LookupTranslator(lookup);
        final StringWriter out = new StringWriter();
        assertEquals(0, translator.translate(CharSequence.EMPTY, 0, out), "Translation of empty input should return 0");
        assertEquals("", out.toString(), "Translation of empty input should return empty String");
    }

    /**
     * Test translation with valid lookup table.
     */
    public void testValidLookupTable() {
        final CharSequence[][] lookup = new CharSequence[][]{{"abc", "def"}, {"123", "456"}};
        final LookupTranslator translator = new LookupTranslator(lookup);
        final StringWriter out = new StringWriter();
        assertEquals(3, translator.translate(CharSequence.valueOf("abc"), 0, out), "Translation of 'abc' should return 3");
        assertEquals("def", out.toString(), "Translation of 'abc' should return 'def'");

        assertEquals(3, translator.translate(CharSequence.valueOf("123"), 0, out), "Translation of '123' should return 3");
        assertEquals("456", out.toString(), "Translation of '123' should return '456'");
    }

    /**
     * Test translation with invalid index.
     */
    public void testInvalidIndex() {
        final CharSequence[][] lookup = new CharSequence[][]{{"abc", "def"}, {"123", "456"}};
        final LookupTranslator translator = new LookupTranslator(lookup);
        final StringWriter out = new StringWriter();
        assertEquals(0, translator.translate(CharSequence.valueOf("abcdef"), 3, out), "Translation with invalid index should return 0");
        assertEquals("", out.toString(), "Translation with invalid index should return empty String");
    }

    /**
     * Test translation with lookup table larger than input.
     */
    public void testLookupTableLargerThanInput() {
        final CharSequence[][] lookup = new CharSequence[][]{{"abc", "def"}, {"123", "456"}, {"abcdef", "12345"}};
        final LookupTranslator translator = new LookupTranslator(lookup);
        final StringWriter out = new StringWriter();
        assertEquals(3, translator.translate(CharSequence.valueOf("abc"), 0, out), "Translation of 'abc' should return 3");
        assertEquals("def", out.toString(), "Translation of 'abc' should return 'def'");

        assertEquals(6, translator.translate(CharSequence.valueOf("abcdef"), 0, out), "Translation of 'abcdef' should return 6");
        assertEquals("12345", out.toString(), "Translation of 'abcdef' should return '12345'");

        assertEquals(0, translator.translate(CharSequence.valueOf("abcdefgh"), 0, out), "Translation with larger input than lookup table should return 0");
        assertEquals("", out.toString(), "Translation with larger input than lookup table should return empty String");
    }

    /**
     * Test translation with lookup table smaller than input.
     */
    public void testLookupTableSmallerThanInput() {
        final CharSequence[][] lookup = new CharSequence[][]{{"abc", "def"}, {"123", "456"}};
        final LookupTranslator translator = new LookupTranslator(lookup);
        final StringWriter out = new StringWriter();
        assertEquals(3, translator.translate(CharSequence.valueOf("abcdef"), 0, out), "Translation with smaller lookup table than input should try next match");
        assertEquals("def", out.toString(), "Translation with smaller lookup table than input should return 'def'");

        assertEquals(3, translator.translate(CharSequence.valueOf("abc123"), 0, out), "Translation with smaller lookup table than input should try next match");
        assertEquals("def", out.toString(), "Translation with smaller lookup table than input should return 'def'");

        assertEquals(6, translator.translate(CharSequence.valueOf("abc12345"), 0, out), "Translation with smaller lookup table than input should try next match");
        assertEquals("456", out.toString(), "Translation with smaller lookup table than input should return '456'");

        assertEquals(0, translator.translate(CharSequence.valueOf("abc123456"), 0, out), "Translation with larger input than lookup table should return 0");
        assertEquals("", out.toString(), "Translation with larger input than lookup table should return empty String");
    }

    /**
     * Test translation with empty lookup table.
     */
    public void testEmptyLookupTable() {
        final LookupTranslator translator = new LookupTranslator(null);
        final StringWriter out = new StringWriter();
        assertEquals(0, translator.translate(CharSequence.EMPTY, 0, out), "Translation with empty lookup table should return 0");
        assertEquals("", out.toString(), "Translation with empty lookup table should return empty String");
    }

    /**
     * Test translation with null input.
     */
    public void testNullInput() {
        final CharSequence[][] lookup = new CharSequence[][]{{"abc", "def"}, {"123", "456"}};
        final LookupTranslator translator = new LookupTranslator(lookup);
        final StringWriter out = new StringWriter();
        assertEquals(0, translator.translate(null, 0, out), "Translation with null input should return 0");
        assertEquals("", out.toString(), "Translation with null input should return empty String");
    }

    /**
     * Test translation with null lookup table.
     */
    public void testNullLookupTable() {
        final LookupTranslator translator = new LookupTranslator(null);
        final StringWriter out = new StringWriter();
        assertEquals(0, translator.translate(CharSequence.EMPTY, 0, out), "Translation with null lookup table should return 0");
        assertEquals("", out.toString(), "Translation with null lookup table should return empty String");
    }

    /**
     * Test translation with empty lookup table content.
     */
    public void testEmptyLookupTableContent() {
        final CharSequence[][] lookup = new CharSequence[][]{{}, {}};
        final LookupTranslator translator = new LookupTranslator(lookup);
        final StringWriter out = new StringWriter();
        assertEquals(0, translator.translate(CharSequence.EMPTY, 0, out), "Translation with empty lookup table content should return 0");
        assertEquals("", out.toString(), "Translation with empty lookup table content should return empty String");
    }

    /**
     * Test translation with null output writer.
     */
    public void testNullOutputWriter() {
        final CharSequence[][] lookup = new CharSequence[][]{{"abc", "def"}, {"123", "456"}};
        final LookupTranslator translator = new LookupTranslator(lookup);
        assertEquals(0, translator.translate(CharSequence.EMPTY, 0, null), "Translation with null output writer should return 0");
    }

    /**
     * Test translation with empty output writer.
     */
    public void testEmptyOutputWriter() {
        final CharSequence[][] lookup = new CharSequence[][]{{"abc", "def"}, {"123", "456"}};
        final LookupTranslator translator = new LookupTranslator(lookup);
        final StringWriter out = new StringWriter();
        assertEquals(0, translator.translate(CharSequence.EMPTY, 0, out), "Translation with empty output writer should return 0");
        assertEquals("", out.toString(), "Translation with empty output writer should return empty String");
    }

    /**
     * Test translation with I/O error.
     */
    public void testIOError() {
        final CharSequence[][] lookup = new CharSequence[][]{{"abc", "def"}, {"123", "456"}};
        final LookupTranslator translator = new LookupTranslator(lookup);
        final StringWriter out = new StringWriter() {
            @Override
            public void write(int c) throws IOException {
                throw new IOException("I/O error");
            }
        };
        try {
            translator.translate(CharSequence.valueOf("abc"), 0, out);
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }
}