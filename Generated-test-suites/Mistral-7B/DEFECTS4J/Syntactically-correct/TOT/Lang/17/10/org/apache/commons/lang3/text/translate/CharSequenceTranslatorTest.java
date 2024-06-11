package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringWriter;
import java.util.Locale;

/**
 * Test class for CharSequenceTranslator.
 */
public class CharSequenceTranslatorTest {

    /**
     * Tests the basic functionality of CharSequenceTranslator.
     */
    @Test
    public void testBasicFunctionality() {
        CharSequenceTranslator translator = new IdentityTranslator();
        CharSequence input = "Hello, World!";
        String expected = "Hello, World!";
        String actual = translator.translate(input);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Tests the translation of a CharSequence with special characters.
     */
    @Test
    public void testSpecialCharacters() {
        CharSequenceTranslator translator = new IdentityTranslator();
        CharSequence input = "Hello, 世界! 你好！";
        String expected = "Hello, 世界! 你好！";
        String actual = translator.translate(input);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Tests the translation of a empty CharSequence.
     */
    @Test
    public void testEmptyCharSequence() {
        CharSequenceTranslator translator = new IdentityTranslator();
        CharSequence input = CharSequenceUtils.EMPTY_CHAR_SEQUENCE;
        String expected = "";
        String actual = translator.translate(input);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Tests the translation of a null CharSequence.
     */
    @Test(expected = NullPointerException.class)
    public void testNullCharSequence() {
        CharSequenceTranslator translator = new IdentityTranslator();
        CharSequence input = null;
        translator.translate(input);
    }

    /**
     * Tests the translation of a CharSequence with an illegal Writer.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testIllegalWriter() {
        CharSequenceTranslator translator = new IdentityTranslator();
        CharSequence input = "Hello, World!";
        Writer out = null;
        translator.translate(input, out);
    }

    /**
     * Tests the aggregation of multiple translators.
     */
    @Test
    public void testAggregation() {
        CharSequenceTranslator uppercaseTranslator = new UppercaseTranslator();
        CharSequenceTranslator identityTranslator = new IdentityTranslator();
        CharSequenceTranslator lowercaseTranslator = new LowercaseTranslator();

        CharSequenceTranslator aggregateTranslator = identityTranslator.with(uppercaseTranslator, lowercaseTranslator);

        CharSequence input = "Hello, World!";
        String expectedUpper = "HELLO, WORLD!";
        String expectedLower = "hello, world!";
        String actual = aggregateTranslator.translate(input);

        String actualUpper = uppercaseTranslator.translate(input);
        String actualLower = lowercaseTranslator.translate(input);

        Assert.assertEquals(expectedUpper, actual.toUpperCase(Locale.ENGLISH));
        Assert.assertEquals(expectedLower, actual.toLowerCase(Locale.ENGLISH));
        Assert.assertEquals(expectedUpper, actualUpper);
        Assert.assertEquals(expectedLower, actualLower);
    }

    /**
     * Identity translator translates a CharSequence to itself.
     */
    static class IdentityTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (index >= input.length()) {
                return 0;
            }
            out.write(Character.toChars(Character.codePointAt(input, index)));
            return Character.charCount(Character.codePointAt(input, index));
        }
    }

    /**
     * Uppercase translator translates a CharSequence to its uppercase.
     */
    static class UppercaseTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (index >= input.length()) {
                return 0;
            }
            int consumed = Character.charCount(Character.codePointAt(input, index));
            out.write(Character.toChars(Character.toUpperCase(Character.codePointAt(input, index))));
            return consumed;
        }
    }

    /**
     * Lowercase translator translates a CharSequence to its lowercase.
     */
    static class LowercaseTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (index >= input.length()) {
                return 0;
            }
            int consumed = Character.charCount(Character.codePointAt(input, index));
            out.write(Character.toChars(Character.toLowerCase(Character.codePointAt(input, index))));
            return consumed;
        }
    }

    /**
     * Aggregate translator aggregates multiple translators.
     */
    static class AggregateTranslator extends CharSequenceTranslator {
        private final CharSequenceTranslator[] translators;

        AggregateTranslator(CharSequenceTranslator[] translators) {
            this.translators = translators;
        }

        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            int length = translators.length;
            for (int i = 0; i < length; i++) {
                int consumed = translators[i].translate(input, index, out);
                if (consumed > 0) {
                    index += consumed;
                    break;
                }
            }
            if (index < input.length()) {
                throw new RuntimeException("Unexpected character found: " + input.charAt(index));
            }
            return Character.charCount(Character.codePointAt(input, index - 1));
        }
    }
}