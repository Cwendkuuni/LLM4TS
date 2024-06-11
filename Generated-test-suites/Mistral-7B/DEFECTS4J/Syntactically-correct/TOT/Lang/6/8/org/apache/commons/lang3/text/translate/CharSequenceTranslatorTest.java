package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for CharSequenceTranslator.
 */
public class CharSequenceTranslatorTest {

    private static CharSequenceTranslator translator;
    private static CharSequenceTranslator escapingTranslator;
    private static CharSequenceTranslator unescapingTranslator;

    static {
        translator = new Translator();
        escapingTranslator = translator.with(new EscapeTranslator(), new HexTranslator());
        unescapingTranslator = translator.with(new HexTranslator(), new UnescapeTranslator());
    }

    /**
     * Test translate method for plain CharSequence.
     */
    @Test
    public void testTranslatePlain() {
        String input = "Hello, World!";
        String expectedOutput = "Hello, World!";
        String output = escapingTranslator.translate(input);
        String unescapedOutput = unescapingTranslator.translate(output);
        assertEquals(expectedOutput, unescapedOutput);
    }

    /**
     * Test translate method for CharSequence with special characters.
     */
    @Test
    public void testTranslateSpecialCharacters() {
        String input = "Hello, 世界! 你好, 世界!";
        String expectedOutput = "Hello, \uD74C\uD960\uD56F\u4E16! \u4E2D\u6587\u5B8B\u4E16!";
        String output = escapingTranslator.translate(input);
        String unescapedOutput = unescapingTranslator.translate(output);
        assertEquals(expectedOutput, unescapedOutput);
    }

    /**
     * Test translate method for empty CharSequence.
     */
    @Test
    public void testTranslateEmpty() {
        String input = "";
        String expectedOutput = "";
        String output = escapingTranslator.translate(input);
        String unescapedOutput = unescapingTranslator.translate(output);
        assertEquals(expectedOutput, unescapedOutput);
    }

    /**
     * Test translate method for CharSequence with only whitespace.
     */
    @Test
    public void testTranslateWhitespace() {
        String input = "   \t Hello  \t World  \n";
        String expectedOutput = "   \t Hello  \t World  \n";
        String output = escapingTranslator.translate(input);
        String unescapedOutput = unescapingTranslator.translate(output);
        assertEquals(expectedOutput, unescapedOutput);
    }

    /**
     * Test translate method for CharSequence with invalid UTF-8 characters.
     */
    @Test(expected = java.util.UnsupportedOperationException.class)
    public void testTranslateInvalidUTF8() {
        String input = "Hello, \uFDD0\uD800 world!";
        escapingTranslator.translate(input);
    }

    /**
     * Test translate method for CharSequence with surrogate pairs.
     */
    @Test
    public void testTranslateSurrogatePairs() {
        String input = "Hello, 你好, \uD840\uDC00\uD840\uDC01 World!";
        String expectedOutput = "Hello, 你好, \uD840\uDC00\uD840\uDC01 World!";
        String output = escapingTranslator.translate(input);
        String unescapedOutput = unescapingTranslator.translate(output);
        assertEquals(expectedOutput, unescapedOutput);
    }

    /**
     * Test translate method for CharSequence with a mix of plain text and special characters.
     */
    @Test
    public void testTranslateMixed() {
        String input = "Hello, World! Hello, \uD74C\uD960\uD56F\u4E16!";
        String expectedOutput = "Hello, World! Hello, \uD74C\uD960\uD56F\u4E16!";
        String output = escapingTranslator.translate(input);
        String unescapedOutput = unescapingTranslator.translate(output);
        assertEquals(expectedOutput, unescapedOutput);
    }

    /**
     * Test with method for creating a merger of translators.
     */
    @Test
    public void testWith() {
        CharSequenceTranslator translator1 = new Translator();
        CharSequenceTranslator translator2 = new Translator();
        String input = "Hello, World!";

        CharSequenceTranslator mergedTranslator = translator1.with(translator2);

        String expectedOutput = "Hello, World!";
        String output = mergedTranslator.translate(input);
        String unescapedOutput = unescapingTranslator.translate(output);
        assertEquals(expectedOutput, unescapedOutput);
    }

    /**
     * Test with method for creating a merger of more than two translators.
     */
    @Test
    public void testWithMultiple() {
        CharSequenceTranslator translator1 = new Translator();
        CharSequenceTranslator translator2 = new Translator();
        CharSequenceTranslator translator3 = new Translator();

        CharSequenceTranslator mergedTranslator = translator1.with(translator2, translator3);

        String input = "Hello, World!";

        String expectedOutput = "Hello, World!";
        String output = mergedTranslator.translate(input);
        String unescapedOutput = unescapingTranslator.translate(output);
        assertEquals(expectedOutput, unescapedOutput);
    }

    /**
     * Test that unsupported translators are not added to the merger.
     */
    @Test(expected = java.lang.IllegalArgumentException.class)
    public void testWithUnsupported() {
        CharSequenceTranslator translator = new Translator();
        AggregateTranslator unsupportedTranslator = new AggregateTranslator(new CharSequenceTranslator[0]);

        CharSequenceTranslator mergedTranslator = translator.with(unsupportedTranslator);
    }

    /**
     * Test that identical translators are not duplicated in the merger.
     */
    @Test
    public void testWithIdentical() {
        CharSequenceTranslator translator = new Translator();
        CharSequenceTranslator identicalTranslator = translator;

        CharSequenceTranslator mergedTranslator = translator.with(identicalTranslator);

        String input = "Hello, World!";

        String expectedOutput = "Hello, World!";
        String output = mergedTranslator.translate(input);
        String unescapedOutput = unescapingTranslator.translate(output);
        assertEquals(expectedOutput, unescapedOutput);
    }
}

class Translator extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        if (index >= input.length()) {
            return 0;
        }

        char c = Character.toChars(Character.codePointAt(input, index))[0];
        out.write(c);
        return 1;
    }
}

class EscapeTranslator extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        if (index >= input.length()) {
            return 0;
        }

        char c = Character.toChars(Character.codePointAt(input, index))[0];

        if (Character.isWhitespace(c)) {
            out.write("\\s");
            return 1;
        } else if (Character.isDigit(c)) {
            out.write(c);
            return 1;
        } else {
            String escapedChar = Character.isISOControl(c) ? Character.name(c) : "\\u" + Integer.toHexString(Character.codePointAt(input, index)).toUpperCase();
            out.write(escapedChar.length() > 1 ? '{' + escapedChar + '}' : escapedChar);
            return Character.charCount(c);
        }
    }
}

class HexTranslator extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        if (index >= input.length()) {
            return 0;
        }

        char c = Character.toChars(Character.codePointAt(input, index))[0];

        if (Character.isWhitespace(c)) {
            out.write("\\s");
            return 1;
        } else {
            switch (c) {
                case '\\':
                    out.write("\\\\");
                    return 1;
                case '{':
                    int start = index;
                    index++;
                    while (index < input.length() && Character.isDigit(Character.toChars(Character.codePointAt(input, index))[0])) {
                        index++;
                    }
                    String hexCode = input.subSequence(start, index).toString();
                    int codePoint = Character.parseUnicode(hexCode);
                    out.write(Character.toChars(codePoint)[0]);
                    return Character.charCount(codePoint);
                default:
                    out.write(c);
                    return 1;
            }
        }
    }
}

class UnescapeTranslator extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        if (index >= input.length()) {
            return 0;
        }

        char c = Character.toChars(Character.codePointAt(input, index))[0];

        if (c == '\\') {
            index++;
            switch (Character.toChars(Character.codePointAt(input, index))[0]) {
                case 's':
                    out.write(" ");
                    return 1;
                case 'u':
                    int start = index;
                    index += 4;
                    String hexCode = input.subSequence(start, index).toString();
                    int codePoint = Character.parseUnicode(hexCode);
                    out.write(Character.toChars(codePoint)[0]);
                    return Character.charCount(codePoint);
                default:
                    out.write(c);
                    return 1;
            }
        } else {
            out.write(c);
            return 1;
        }
    }
}

class AggregateTranslator extends CharSequenceTranslator {

    private CharSequenceTranslator[] translators;

    public AggregateTranslator(CharSequenceTranslator[] translators) {
        this.translators = translators;
    }

    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        int totalConsumed = 0;
        for (CharSequenceTranslator translator : translators) {
            int consumed = translator.translate(input, index, out);
            if (consumed > 0) {
                totalConsumed += consumed;
                index += consumed;
            }
        }
        return totalConsumed;
    }
}