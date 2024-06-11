package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslatorFactory;
import org.apache.commons.lang3.text.translate.Translation;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Locale;

/**
 * Test class for CharSequenceTranslator.
 */
public class CharSequenceTranslatorTest {

    private CharSequenceTranslator translator;

    @Before
    public void setUp() {
        translator = new CharSequenceTranslatorFactory().getInstance();
    }

    @Test
    public void testTranslate() throws IOException {
        String input = "Hello, World!";
        String expectedOutput = "Hello, World!";

        String output = translator.translate(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testTranslateWithWriter() throws IOException {
        String input = "Hello, World!";
        StringWriter writer = new StringWriter();

        int length = translator.translate(CharSequenceProvider.create(input), 0, writer);
        String output = writer.toString();

        assertEquals(input, output);
        assertEquals(input.length(), length);
    }

    @Test
    public void testTranslateWithNullInput() {
        String output = translator.translate(null);
        assertNull(output);
    }

    @Test
    public void testTranslateWithEmptyInput() {
        String output = translator.translate(CharSequenceProvider.create(""));
        assertEquals("", output);
    }

    @Test
    public void testTranslateWithSingleCharacterInput() throws IOException {
        char input = 'A';
        StringWriter writer = new StringWriter();

        int length = translator.translate(CharSequenceProvider.create(Character.toString(input)), 0, writer);
        String output = writer.toString();

        assertEquals(Character.toString(input), output);
        assertEquals(1, length);
    }

    @Test
    public void testTranslateWithMultiCharacterInput() throws IOException {
        String input = "ABC";
        StringWriter writer = new StringWriter();

        int length = translator.translate(CharSequenceProvider.create(input), 0, writer);
        String output = writer.toString();

        assertEquals(input, output);
        assertEquals(input.length(), length);
    }

    @Test
    public void testTranslateWithSurrogatePairInput() throws IOException {
        String input = "\uD800\uDC00";
        StringWriter writer = new StringWriter();

        int length = translator.translate(CharSequenceProvider.create(input), 0, writer);
        String output = writer.toString();

        assertEquals("\uD800\uDC00", output);
        assertEquals(2, length);
    }

    @Test
    public void testTranslateWithAggregatedTranslators() throws IOException {
        CharSequenceTranslator translator1 = new ASCIITranslator();
        CharSequenceTranslator translator2 = new HTMLTranslator();

        CharSequenceTranslator translator3 = translator1.with(translator2);

        String input = "<Hello, World!>";
        StringWriter writer = new StringWriter();

        int length = translator3.translate(CharSequenceProvider.create(input), 0, writer);
        String output = writer.toString();

        assertEquals("&lt;Hello, World!&gt;", output);
        assertEquals(input.length(), length);
    }

    private static class CharSequenceProvider implements CharSequence {
        private final CharSequence charSequence;

        private CharSequenceProvider(CharSequence charSequence) {
            this.charSequence = charSequence;
        }

        private static CharSequenceProvider create(CharSequence charSequence) {
            return new CharSequenceProvider(charSequence);
        }

        @Override
        public int length() {
            return charSequence.length();
        }

        @Override
        public char charAt(int index) {
            return charSequence.charAt(index);
        }

        @Override
        public CharSequence subSequence(int start, int end) {
            return charSequence.subSequence(start, end);
        }
    }
}

class ASCIITranslator extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        if (index >= input.length()) {
            return 0;
        }
        char c = input.charAt(index);
        out.write(Character.forDigit(Character.toUpperCase((char) Character.digit(c)), 10));
        return 1;
    }
}

class HTMLTranslator extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        if (index >= input.length()) {
            return 0;
        }
        char c = input.charAt(index);
        switch (c) {
            case '<':
                out.write("&lt;");
                break;
            case '>':
                out.write("&gt;");
                break;
            default:
                out.write(c);
                break;
        }
        return 1;
    }
}

class Translation {
    // This is a placeholder for actual translators.
}