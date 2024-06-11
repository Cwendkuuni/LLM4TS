package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.apache.commons.lang3.text.translate.NoOpTranslator;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test class for {@link CharSequenceTranslator}.
 */
public class CharSequenceTranslatorTest {

    private CharSequenceTranslator translator;
    private CharSequenceTranslator noOpTranslator;

    @Before
    public void setUp() {
        translator = new AggregateTranslator(new CharSequenceTranslator[] { new Translator1(), new Translator2() });
        noOpTranslator = new NoOpTranslator();
    }

    @Test
    public void testTranslateCharSequence() throws IOException {
        String input = "TEST String with ε-λ-ι-ο-π-ς-υ-ω characters: ελληνικά";
        String expected = "TES%20TES%20t%20s%20s%20u%20o%20p%20s%20u%20o";
        String result = translator.translate(input);
        assertEquals(expected, result);

        // Null input test
        assertNull(translator.translate(null));
    }

    @Test
    public void testTranslateWriter() throws IOException {
        String input = "TEST String with ε-λ-ι-ο-π-ς-υ-ω characters: ελληνικά";
        StringWriter writer = new StringWriter();

        translator.translate(input, new OutputStreamWriter(new ByteArrayOutputStream(), Locale.ENGLISH));
        String expected = "TES%20TES%20t%20s%20s%20u%20o%20p%20s%20u%20o";
        String result = writer.toString();

        assertEquals(expected, result);
    }

    @Test
    public void testTranslateCharSequenceWithNoOpTranslator() throws IOException {
        String input = "TEST String with ε-λ-ι-ο-π-ς-υ-ω characters: ελληνικά";
        String result = translator.translate(input, noOpTranslator);
        assertEquals(input, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWriterNull() throws IOException {
        translator.translate("input", null);
    }

    @Test
    public void testTranslateEmptyCharSequence() throws IOException {
        String input = "";
        String result = translator.translate(input);
        assertEquals("", result);
    }

    @Test
    public void testTranslateSingleCharacterCharSequence() throws IOException {
        char c = 'A';
        String result = translator.translate(Character.toString(c));
        assertEquals(Character.isUpperCase(c) ? "41" : "61", result);
    }

    @Test
    public void testTranslateMultipleCharactersCharSequence() throws IOException {
        String input = "ABC";
        String result = translator.translate(input);
        String expected = "414243";
        assertEquals(expected, result);
    }

    @Test
    public void testTranslateComplexCharSequence() throws IOException {
        String input = "👍🏽👋🏽 TEST 🌎🌏 ελληνικά 🐶🐺 👩🏼👨🏻👩🏽👨🏾";
        String expected = "1F44D1F41F\uD83C\uDF21\uD83C\uDFFB\uD83C\uDFFC\uD83C\uDF20\uD83C\uDFFB\uD83C\uDF21\uD83C\uDF39\uD83C\uDF34\uD83C\uDF31\uD83C\uDF30\uD83C\uDF37\uD83C\uDF3A\uD83C\uDF38\uD83C\uDF35\uD83C\uDF36\uD83C\uDF39\uD83C\uDF37\uD83C\uDF35\uD83C\uDF33\uD83C\uDF3C\uD83C\uDF37\uD83C\uDF35\uD83C\uDF33";
        String result = translator.translate(input);
        assertEquals(expected, result);
    }

    private static class Translator1 extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (index >= input.length()) {
                return 0;
            }

            int codePoint = Character.codePointAt(input, index);
            if (Character.isDigit(codePoint)) {
                return 1;
            }

            int length = Character.charCount(codePoint);
            if (input.length() - index < length) {
                throw new IndexOutOfBoundsException("Input sequence too short: " + input.subSequence(index, input.length()));
            }

            CharSequence subSequence = input.subSequence(index, index + length);
            String hex = CharSequenceUtils.subSequenceToString(subSequence, 0, length / 2);
            out.write(hex.charAt(0));
            out.write(hex.charAt(1));
            index += length;

            return length;
        }

        @Override
        public String translate(CharSequence input) {
            throw new UnsupportedOperationException();
        }
    }

    private static class Translator2 extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (input.length() - index < 2) {
                return 0;
            }

            int codePoint1 = Character.codePointAt(input, index);
            int codePoint2 = Character.codePointAt(input, index + 1);

            if (Character.isUpperCase(codePoint1) && Character.isDigit(codePoint2)) {
                out.write(codePoint1);
                return 1;
            }

            index += Character.charCount(codePoint1);

            return 0;
        }

        @Override
        public String translate(CharSequence input) {
            throw new UnsupportedOperationException();
        }
    }

    private static class NoOpTranslator implements CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            return 0;
        }

        @Override
        public String translate(CharSequence input) {
            return "";
        }
    }

    private static class AggregateTranslator extends CharSequenceTranslator {
        private final CharSequenceTranslator[] translators;

        public AggregateTranslator(CharSequenceTranslator[] translators) {
            this.translators = translators;
        }

        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            int consumed = 0;
            for (CharSequenceTranslator translator : translators) {
                int translated = translator.translate(input, index, out);
                if (translated > 0) {
                    index += translated;
                    consumed += translated;
                } else {
                    break;
                }
            }
            return consumed;
        }

        @Override
        public String translate(CharSequence input) {
            StringBuilder builder = new StringBuilder();
            int index = 0;
            for (CharSequenceTranslator translator : translators) {
                String translated = translator.translate(input, index);
                if (translated != null) {
                    builder.append(translated);
                    index += translated.length();
                } else {
                    index += Character.charCount(Character.codePointAt(input, index));
                }
            }
            return builder.toString();
        }
    }
}