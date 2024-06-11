package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for CharSequenceTranslator.
 */
public class CharSequenceTranslatorTest {

    /**
     * Test translate method with default translator.
     */
    @Test
    public void testTranslateDefault() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                out.write(input.charAt(index));
                return Character.isSurrogate(input.charAt(index)) ? 2 : 1;
            }
        };
        CharSequence input = "Hi ñello 世界";
        String expected = "Hi \u00F1ello \u4E16\u4E1A";
        String actual = translator.translate(input);
        assertEquals(expected, actual);
    }

    /**
     * Test translate method with custom translator.
     */
    @Test
    public void testTranslateCustom() {
        CharSequenceTranslator lowerCaseTranslator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                char c = input.charAt(index);
                if (Character.isUpperCase(c)) {
                    out.write(Character.toLowerCase(c));
                    return 1;
                } else {
                    out.write(c);
                    return Character.isSurrogate(c) ? 2 : 1;
                }
            }
        };
        CharSequence input = "HELLO 世界";
        String expected = "hello 世界";
        String actual = lowerCaseTranslator.translate(input);
        assertEquals(expected, actual);
    }

    /**
     * Test translate method with error scenario.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateError() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                throw new IllegalArgumentException("Writer cannot be null");
            }
        };
        CharSequence input = "Hello";
        translator.translate(input, null);
    }

    /**
     * Test translate method with AggregateTranslator.
     */
    @Test
    public void testTranslateAggregate() {
        CharSequenceTranslator lowerCaseTranslator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                char c = input.charAt(index);
                if (Character.isUpperCase(c)) {
                    out.write(Character.toLowerCase(c));
                    return 1;
                } else {
                    out.write(c);
                    return Character.isSurrogate(c) ? 2 : 1;
                }
            }
        };

        CharSequenceTranslator upperCaseTranslator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                char c = input.charAt(index);
                if (Character.isLowerCase(c)) {
                    out.write(Character.toUpperCase(c));
                    return 1;
                } else {
                    out.write(c);
                    return Character.isSurrogate(c) ? 2 : 1;
                }
            }
        };

        CharSequenceTranslator translator = new AggregateTranslator(new CharSequenceTranslator[] { lowerCaseTranslator, upperCaseTranslator });
        CharSequence input = "HeLLo 世界";
        String expected = "hELLO 世界";
        String actual = translator.translate(input);
        assertEquals(expected, actual);
    }

    /**
     * Test translate method with empty input.
     */
    @Test
    public void testTranslateEmptyInput() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {};
        String expected = "";
        String actual = translator.translate(StringUtils.EMPTY);
        assertEquals(expected, actual);
    }
}