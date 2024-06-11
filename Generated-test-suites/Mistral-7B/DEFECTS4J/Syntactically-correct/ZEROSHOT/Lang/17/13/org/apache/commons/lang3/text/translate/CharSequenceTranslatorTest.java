package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Unit tests for {@link CharSequenceTranslator} class.
 */
public class CharSequenceTranslatorTest {

    private CharSequenceTranslator translator;

    @Before
    public void setup() {
        translator = new IdentityCharSequenceTranslator();
    }

    @Test
    public void testTranslate() {
        CharSequence input = "Hello, World!";
        String expected = "Hello, World!";
        String result = translator.translate(input);
        assertEquals(expected, result);
    }

    @Test
    public void testTranslateToWriter() throws IOException {
        CharSequence input = "Hello, World!";
        StringWriter output = new StringWriter();
        translator.translate(input, output);
        String result = output.toString();
        String expected = "Hello, World!";
        assertEquals(expected, result);
    }

    @Test
    public void testTranslateWithCustomWriter() throws IOException {
        CharSequence input = "Hello, World!";
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        translator.translate(input, new Writer() {
            @Override
            public void write(int c) throws IOException {
                output.write(c);
            }

            @Override
            public void flush() throws IOException {
                // not used
            }

            @Override
            public void close() throws IOException {
                // not used
            }
        });
        String result = new String(output.toByteArray());
        String expected = "Hello, World!";
        assertEquals(expected, result);
    }

    @Test
    public void testTranslateWithNullInput() {
        String result = translator.translate(null);
        assertNull(result);
    }

    @Test
    public void testTranslateWithNullOutput() {
        CharSequence input = "Hello, World!";
        try {
            translator.translate(input, null);
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testTranslateWithAggregateTranslator() {
        CharSequenceTranslator translator1 = new ToUpperCaseCharSequenceTranslator();
        CharSequenceTranslator translator2 = new IdentityCharSequenceTranslator();
        CharSequenceTranslator compositeTranslator = translator1.with(translator2);

        CharSequence input = "Hello, World!";
        String expected = "HELLO, WORLD!";
        String result = compositeTranslator.translate(input);
        assertEquals(expected, result);

        assertEquals(translator1.getClass(), compositeTranslator.getClass().getSuperclass());
        assertEquals(3, compositeTranslator.getClass().getInterfaces().length);
    }

    @Test
    public void testToString() {
        CharSequenceTranslator translator = new IdentityCharSequenceTranslator();
        String expected = ReflectionToStringBuilder.toString(translator, ToStringStyle.SHORT_PREFIX_STYLE);
        String result = translator.toString();
        assertEquals(expected, result);
    }

    private static class IdentityCharSequenceTranslator extends CharSequenceTranslator {

        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (input == null || index < 0 || index >= input.length()) {
                return 0;
            }
            char c = Character.codePointAt(input, index);
            out.write(c);
            return Character.charCount(c);
        }
    }

    private static class ToUpperCaseCharSequenceTranslator extends CharSequenceTranslator {

        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (input == null || index < 0 || index >= input.length()) {
                return 0;
            }
            char c = Character.codePointAt(input, index);
            char upperCase = Character.toUpperCase(c);
            out.write(upperCase);
            return Character.charCount(upperCase);
        }
    }

    private static class AggregateTranslator extends CharSequenceTranslator {

        private final CharSequenceTranslator[] translators;

        public AggregateTranslator(CharSequenceTranslator[] translators) {
            this.translators = translators;
        }

        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            CharSequenceTranslator currentTranslator = this;
            int consumeCount = 0;
            while (consumeCount < input.length()) {
                CharSequenceTranslator nextTranslator = null;
                for (CharSequenceTranslator translator : translators) {
                    nextTranslator = translator;
                    int translated = nextTranslator.translate(input, index + consumeCount, out);
                    consumeCount += translated;
                    if (consumeCount == input.length()) {
                        break;
                    }
                }
                if (nextTranslator == null) {
                    throw new IllegalStateException("Unexpected end of input found.");
                }
                currentTranslator = nextTranslator;
            }
            return consumeCount;
        }
    }
}