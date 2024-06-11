package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.StringWriter;
import java.util.Locale;

import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator.AggregateTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator.IdentityTranslator;
import org.junit.Test;

/**
 * Tests for {@link CharSequenceTranslator}
 */
public class CharSequenceTranslatorTest {

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)}
     */
    @Test
    public void testTranslate() {
        // Test with default translator
        CharSequenceTranslator translator = new IdentityTranslator();
        String input = "test";
        String expected = "test";
        String output = translator.translate(input);
        assertEquals(expected, output);

        // Test with custom translator
        translator = new AggregateTranslator(new CharSequenceTranslator[] { new IdentityTranslator(), new ToUpperCaseTranslator() });
        input = "test";
        expected = "TEST";
        output = translator.translate(input);
        assertEquals(expected, output);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with null input
     */
    @Test(expected = NullPointerException.class)
    public void testTranslateNullInput() {
        CharSequenceTranslator translator = new IdentityTranslator();
        translator.translate(null, new StringWriter());
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with empty input
     */
    @Test
    public void testTranslateEmptyInput() {
        CharSequenceTranslator translator = new IdentityTranslator();
        String output = translator.translate(Character.toString(new char[0]));
        assertNull(output);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with null writer
     */
    @Test(expected = NullPointerException.class)
    public void testTranslateNullWriter() {
        CharSequenceTranslator translator = new IdentityTranslator();
        translator.translate("test", null);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with empty writer
     */
    @Test
    public void testTranslateEmptyWriter() {
        CharSequenceTranslator translator = new IdentityTranslator();
        StringWriter writer = new StringWriter();
        translator.translate("test", writer);
        assertEquals("", writer.toString());
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} with IO exception
     */
    @Test(expected = IOException.class)
    public void testTranslateIOException() throws IOException {
        StringWriter writer = new StringWriter() {
            @Override
            public void write(int c) throws IOException {
                if (c == 'a') {
                    throw new IOException("Test IOException");
                }
                super.write(c);
            }
        };
        CharSequenceTranslator translator = new IdentityTranslator();
        translator.translate("test", writer);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence)}
     */
    @Test
    public void testTranslateWithoutWriter() {
        CharSequenceTranslator translator = new IdentityTranslator();
        String input = "test";
        String expected = "test";
        String output = translator.translate(input);
        assertEquals(expected, output);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence)} with null input
     */
    @Test
    public void testTranslateNullInputWithoutWriter() {
        CharSequenceTranslator translator = new IdentityTranslator();
        String output = translator.translate(null);
        assertNull(output);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence)} with empty input
     */
    @Test
    public void testTranslateEmptyInputWithoutWriter() {
        CharSequenceTranslator translator = new IdentityTranslator();
        String output = translator.translate(Character.toString(new char[0]));
        assertNull(output);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence)} with IO exception
     */
    @Test(expected = RuntimeException.class)
    public void testTranslateIOExceptionWithoutWriter() throws IOException {
        StringWriter writer = new StringWriter() {
            @Override
            public void write(int c) throws IOException {
                if (c == 'a') {
                    throw new IOException("Test IOException");
                }
                super.write(c);
            }
        };
        CharSequenceTranslator translator = new IdentityTranslator() {
            @Override
            void translate(CharSequence input, Writer out) throws IOException {
                super.translate(input, out);
            }
        };
        String input = "test";
        translator.translate(input, writer);
    }

}

class ToUpperCaseTranslator extends CharSequenceTranslator {

    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        char ch = Character.codePointAt(input, index);
        int consumed = 1;
        if (Character.isUpperCase(ch)) {
            consumed = 1;
        } else {
            char upper = Character.toUpperCase(ch);
            out.write(upper);
            consumed += Character.charCount(upper);
        }
        return consumed;
    }
}