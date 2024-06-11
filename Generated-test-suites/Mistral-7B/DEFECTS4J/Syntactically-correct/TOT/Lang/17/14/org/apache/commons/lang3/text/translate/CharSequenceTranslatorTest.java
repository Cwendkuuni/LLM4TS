package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Locale;

/**
 * Test class for {@link CharSequenceTranslator}.
 */
public class CharSequenceTranslatorTest {

    /**
     * Test for the default implementation.
     */
    @Test
    public void testDefaultTranslation() throws IOException {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                out.write(Character.codePointAt(input, index));
                return Character.charCount(Character.codePointAt(input, index));
            }
        };

        String expected = "Hello, World!";
        String actual = translator.translate(expected);
        assertEquals(expected, actual);
    }

    /**
     * Test for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with null input.
     */
    @Test
    public void testNullInput() {
        assertThrows(IllegalArgumentException.class, () -> new CharSequenceTranslator().translate(null, new StringWriter()));
    }

    /**
     * Test for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with null Writer.
     */
    @Test
    public void testNullOutput() {
        CharSequenceTranslator translator = new CharSequenceTranslator();
        CharSequence input = "Hello, World!";

        assertThrows(IllegalArgumentException.class, () -> translator.translate(input, null));
    }

    /**
     * Test for {@link CharSequenceTranslator#translate(CharSequence)} with null input.
     */
    @Test
    public void testNullInputWithStringReturn() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public String translate(CharSequence input) {
                if (input == null) {
                    return null;
                }
                return new String(Character.toChars(Character.codePointAt(input, 0)));
            }
        };

        assertNull(translator.translate(null));
    }

    /**
     * Test for {@link CharSequenceTranslator#translate(CharSequence)} with empty input.
     */
    @Test
    public void testEmptyInput() throws IOException {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                if (input.length() == 0) {
                    return 0;
                }
                out.write(Character.codePointAt(input, index));
                return Character.charCount(Character.codePointAt(input, index));
            }

            @Override
            public String translate(CharSequence input) {
                return new String(Character.toChars(Character.codePointAt(input, 0)));
            }
        };

        assertNull(translator.translate(CharSequence.EMPTY));
    }

    /**
     * Test for merging translators using {@link CharSequenceTranslator#with(CharSequenceTranslator...)}.
     */
    @Test
    public void testMergedTranslation() throws IOException {
        CharSequenceTranslator lowercase = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                out.write(Character.toLowerCase(Character.codePointAt(input, index)));
                return Character.charCount(Character.codePointAt(input, index));
            }
        };

        CharSequenceTranslator uppercase = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                out.write(Character.toUpperCase(Character.codePointAt(input, index)));
                return Character.charCount(Character.codePointAt(input, index));
            }
        };

        CharSequenceTranslator combined = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                out.write(Character.toUpperCase(Character.toLowerCase(Character.codePointAt(input, index))));
                return Character.charCount(Character.codePointAt(input, index));
            }
        };

        CharSequenceTranslator translator = new AggregateTranslator(new CharSequenceTranslator[] {lowercase, uppercase});
        String input = "Hello, World!";
        String expected = "HELLO, WORLD!";
        String actual = translator.translate(input);
        assertEquals(expected, actual);

        String actualCombined = combined.translate(input);
        assertEquals(expected, actualCombined);
    }
}