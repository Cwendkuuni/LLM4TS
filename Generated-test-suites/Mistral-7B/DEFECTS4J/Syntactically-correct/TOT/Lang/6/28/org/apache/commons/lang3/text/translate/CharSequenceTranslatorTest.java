package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for {@link CharSequenceTranslator}.
 */
public class CharSequenceTranslatorTest {

    /**
     * Test for the abstract {@link CharSequenceTranslator#translate(CharSequence, int, Writer)} method.
     */
    @Test
    public void testAbstractTranslateMethod() {
        final String input = "Hello, 世界! 你好!";
        final StringWriter outputWriter = new StringWriter();
        final CharSequenceTranslator translator = new TranslatingCharSequenceTranslator();

        // Test translation with normal characters.
        translator.translate(input, 0, outputWriter);
        assertEquals("Hello, 世界! 你好!", input);
        assertEquals("Hello, \uD74C\uD604!\u4E2D\u6587!", outputWriter.toString());

        // Test translation with special characters.
        outputWriter.getBuffer().setLength(0);
        translator.translate(input, 6, outputWriter);
        assertEquals("你好!", input.subSequence(6, 8));
        assertEquals("\u5B8C\u5BD1!", outputWriter.toString());

        // Test translation with error scenarios.
        try {
            translator.translate(null, 0, outputWriter);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        try {
            translator.translate(input, input.length() + 1, outputWriter);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }

        try {
            outputWriter.close();
            translator.translate(input, 0, outputWriter);
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }

    /**
     * Test for the {@link CharSequenceTranslator#translate(CharSequence)} method.
     */
    @Test
    public void testTranslateMethod() {
        final CharSequenceTranslator translator = new TranslatingCharSequenceTranslator();

        // Test translation with normal characters.
        assertEquals("Hello, 世界! 你好!", translator.translate("Hello, 世界! 你好!"));

        // Test translation with special characters.
        assertEquals("\u5B8C\u5BD1!", translator.translate("你好!"));

        // Test translation with empty input.
        assertEquals("", translator.translate(""));

        // Test translation with null input.
        assertNull(translator.translate(null));
    }

    /**
     * Test for the {@link CharSequenceTranslator#with(CharSequenceTranslator...)} method.
     */
    @Test
    public void testWithMethod() {
        final CharSequenceTranslator translator1 = new TranslatingCharSequenceTranslator();
        final CharSequenceTranslator translator2 = new TranslatingCharSequenceTranslator();

        // Test merging two translators.
        final CharSequenceTranslator mergedTranslator = translator1.with(translator2);

        // Test translation with normal characters.
        final String input = "Hello, 世界! 你好!";
        final StringWriter outputWriter = new StringWriter();
        mergedTranslator.translate(input, 0, outputWriter);
        assertEquals("Hello, 世界! 你好!", input);
        assertEquals("Hello, \uD74C\uD604!\u4E2D\u6587!\u5B8C\u5BD1!", outputWriter.toString());

        // Test translation with special characters.
        outputWriter.getBuffer().setLength(0);
        mergedTranslator.translate(input, 6, outputWriter);
        assertEquals("你好!", input.subSequence(6, 8));
        assertEquals("\u5B8C\u5BD1!\u5B8C\u5BD1!", outputWriter.toString());
    }

    /**
     * Test for the helper methods {@link CharSequenceTranslator#translate(CharSequence, Writer)} and {@link CharSequenceTranslator#hex(int)}.
     */
    @Test
    public void testHelperMethods() {
        assertEquals("", CharSequenceUtils.emptyQuotedString(CharSequenceUtils.EMPTY));
        assertEquals("\"\"", CharSequenceUtils.emptyQuotedString(CharSequenceUtils.QUOTED_EMPTY));

        assertEquals("Hello, \\uD74C\\uD604!\\u4E2D\\u6587!", CharSequenceUtils.quoteString(CharSequenceUtils.QUOTED_EMPTY + "Hello, 世界! 你好!"));

        assertEquals("\u0026amp;\u0026lt;\u0026gt;", CharSequenceUtils.escapeString("&<>"));

        assertEquals("\u0026amp;\u0026lt;\u0026gt;", CharSequenceUtils.unquoteString(CharSequenceUtils.quoteString("&<>")));

        assertEquals("\u0026amp;\u0026lt;\u0026gt;", CharSequenceUtils.escapeString(CharSequenceUtils.unquoteString(CharSequenceUtils.quoteString("&<>"))));

        assertEquals("\u0026amp;\u0026lt;\u0026gt;", CharSequenceUtils.unquoteString(CharSequenceUtils.escapeString(CharSequenceUtils.quoteString("&<>"))));

        assertEquals("\\uD74C\\uD604\\u4E2D\\u6587\\u5B8C\\u5BD1", CharSequenceUtils.quoteString("\uD74C\uD604\u4E2D\u6587\u5B8C\u5BD1"));

        assertEquals("\u0026amp;\u0026lt;\u0026gt;", CharSequenceUtils.escapeString("&<>"));
        assertEquals("\u0026amp;\\u0026lt;\\u0026gt;", CharSequenceUtils.escapeString("&<>&lt;>&gt;"));
        assertEquals("\u0026amp;\\u0026lt;\\u0026gt;\\u0026amp;\\u0026lt;\\u0026gt;", CharSequenceUtils.escapeString("&<>&lt;&gt;&<>&gt;"));

        assertEquals("\u0026amp;", CharSequenceUtils.unquoteString("&amp;"));
        assertEquals("\u0026lt;", CharSequenceUtils.unquoteString("&lt;"));
        assertEquals("\u0026gt;", CharSequenceUtils.unquoteString("&gt;"));
        assertEquals("\u0026lt;\\u0026gt;", CharSequenceUtils.unquoteString("&lt;&gt;"));

        assertEquals("\u0026amp;", CharSequenceUtils.unquoteString(CharSequenceUtils.escapeString("&amp;")));
        assertEquals("\u0026lt;", CharSequenceUtils.unquoteString(CharSequenceUtils.escapeString("&lt;")));
        assertEquals("\u0026gt;", CharSequenceUtils.unquoteString(CharSequenceUtils.escapeString("&gt;")));
        assertEquals("\u0026lt;\\u0026gt;", CharSequenceUtils.unquoteString(CharSequenceUtils.escapeString("&lt;&gt;")));

        assertEquals("\uD74C\uD604", CharSequenceUtils.quoteString("\uD74C\uD604"));
        assertEquals("\u4E2D\u6587", CharSequenceUtils.quoteString("\u4E2D\u6587"));
        assertEquals("\u5B8C\u5BD1", CharSequenceUtils.quoteString("\u5B8C\u5BD1"));

        assertEquals("\uD74C\uD604", CharSequenceUtils.unquoteString(CharSequenceUtils.escapeString("\uD74C\uD604")));
        assertEquals("\u4E2D\u6587", CharSequenceUtils.unquoteString(CharSequenceUtils.escapeString("\u4E2D\u6587")));
        assertEquals("\u5B8C\u5BD1", CharSequenceUtils.unquoteString(CharSequenceUtils.escapeString("\u5B8C\u5BD1")));

        assertEquals("\uD74C\uD604", CharSequenceUtils.unquoteString(CharSequenceUtils.quoteString("\uD74C\uD604")));
        assertEquals("\u4E2D\u6587", CharSequenceUtils.unquoteString(CharSequenceUtils.quoteString("\u4E2D\u6587")));
        assertEquals("\u5B8C\u5BD1", CharSequenceUtils.unquoteString(CharSequenceUtils.quoteString("\u5B8C\u5BD1")));

        assertEquals("\uD74C\uD604", CharSequenceUtils.unquoteString(CharSequenceUtils.unquoteString(CharSequenceUtils.quoteString("\uD74C\uD604"))));
        assertEquals("\u4E2D\u6587", CharSequenceUtils.unquoteString(CharSequenceUtils.unquoteString(CharSequenceUtils.quoteString("\u4E2D\u6587"))));
        assertEquals("\u5B8C\u5BD1", CharSequenceUtils.unquoteString(CharSequenceUtils.unquoteString(CharSequenceUtils.quoteString("\u5B8C\u5BD1"))));

        assertEquals("\uD74C\uD604", CharSequenceUtils.unquoteString(CharSequenceUtils.escapeString(CharSequenceUtils.quoteString("\uD74C\uD604"))));
        assertEquals("\u4E2D\u6587", CharSequenceUtils.unquoteString(CharSequenceUtils.escapeString(CharSequenceUtils.quoteString("\u4E2D\u6587"))));
        assertEquals("\u5B8C\u5BD1", CharSequenceUtils.unquoteString(CharSequenceUtils.escapeString(CharSequenceUtils.quoteString("\u5B8C\u5BD1"))));

        assertEquals("\uD74C\uD604", CharSequenceUtils.unquoteString(CharSequenceUtils.quoteString(CharSequenceUtils.escapeString("\uD74C\uD604"))));
        assertEquals("\u4E2D\u6587", CharSequenceUtils.unquoteString(CharSequenceUtils.quoteString(CharSequenceUtils.escapeString("\u4E2D\u6587"))));
        assertEquals("\u5B8C\u5BD1", CharSequenceUtils.unquoteString(CharSequenceUtils.quoteString(CharSequenceUtils.escapeString("\u5B8C\u5BD1"))));

        assertEquals("\uD74C\uD604", CharSequenceUtils.unquoteString(CharSequenceUtils.unquoteString(CharSequenceUtils.quoteString(CharSequenceUtils.escapeString("\uD74C\uD604")))));
        assertEquals("\u4E2D\u6587", CharSequenceUtils.unquoteString(CharSequenceUtils.unquoteString(CharSequenceUtils.quoteString(CharSequenceUtils.escapeString("\u4E2D\u6587")))));
        assertEquals("\u5B8C\u5BD1", CharSequenceUtils.unquoteString(CharSequenceUtils.unquoteString(CharSequenceUtils.quoteString(CharSequenceUtils.escapeString("\u5B8C\u5BD1")))));

        assertEquals("\uD74C\uD604", CharSequenceUtils.unquoteString(CharSequenceUtils.escapeString(CharSequenceUtils.unquoteString(CharSequenceUtils.quoteString("\uD74C\uD604")))));
        assertEquals("\u4E2D\u6587", CharSequenceUtils.unquoteString(CharSequenceUtils.escapeString(CharSequenceUtils.unquoteString(CharSequenceUtils.quoteString("\u4E2D\u6587")))));
        assertEquals("\u5B8C\u5BD1", CharSequenceUtils.unquoteString(CharSequenceUtils.escapeString(CharSequenceUtils.unquoteString(CharSequenceUtils.quoteString("\u5B8C\u5BD1")))));

        assertEquals("\u0026amp;\u0026lt;\u0026gt;", CharSequenceUtils.quoteString("\u0026amp;\u0026lt;\u0026gt;"));
        assertEquals("\u0026amp;", CharSequenceUtils.unquoteString(CharSequenceUtils.quoteString("\u0026amp;")));
        assertEquals("\u0026lt;", CharSequenceUtils.unquoteString(CharSequenceUtils.quoteString("\u0026lt;")));
        assertEquals("\u0026gt;", CharSequenceUtils.unquoteString(CharSequenceUtils.quoteString("\u0026gt;")));

        assertEquals("\uD74C\uD604", CharSequenceUtils.hex(Character.codePointAt("你好!", 1)));
        assertEquals("\uD74C\uD604", CharSequenceUtils.hex(Character.codePointAt("你好!", 3)));
    }
}

class TranslatingCharSequenceTranslator extends CharSequenceTranslator {

    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        if (index >= input.length() || Character.isSurrogate(Character.codePointAt(input, index))) {
            return 0;
        }

        char currentChar = Character.codePointAt(input, index);
        if (Character.isHighSurrogate(currentChar)) {
            int nextChar = Character.codePointAt(input, index + 1);
            if (Character.isLowSurrogate(nextChar)) {
                out.write(Character.toChars(Character.toIntBitMask(currentChar) << 10 | Character.toIntBitMask(nextChar) & 0xFFFC));
                index += Character.charCount(currentChar) + Character.charCount(nextChar);
                return Character.charCount(currentChar) + Character.charCount(nextChar);
            }
        }

        out.write(Character.toChars(currentChar));
        return Character.charCount(currentChar);
    }
}

class AggregateTranslator implements CharSequenceTranslator {

    private final CharSequenceTranslator[] translators;

    public AggregateTranslator(CharSequenceTranslator... translators) {
        this.translators = translators;
    }

    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        for (CharSequenceTranslator translator : translators) {
            int translatedLength = translator.translate(input, index, out);
            if (translatedLength > 0) {
                return translatedLength;
            }
            index += translatedLength;
        }
        return 0;
    }
}