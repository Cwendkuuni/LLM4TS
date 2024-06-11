package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;

import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.apache.commons.lang3.text.translate.CharSetTranslator;
import org.apache.commons.lang3.text.translate.SpecialCharTranslator;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

/**
 * Test file for CharSequenceTranslator class.
 */
public class CharSequenceTranslatorTest {

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @org.junit.Test
    public void testTranslate() throws Exception {
        // Test empty input
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };
        String expected = "";
        String result = translator.translate(null);
        assertEquals(expected, result);

        // Test translation with no translator
        result = translator.translate(Character.toString('A'));
        assertEquals("A", result);

        // Test CharSetTranslator
        translator = new CharSetTranslator();
        expected = "%25";
        result = translator.translate(Character.toString('%'));
        assertEquals(expected, result);

        // Test SpecialCharTranslator
        translator = new SpecialCharTranslator();
        expected = "&#39;";
        result = translator.translate(Character.toString('\''));
        assertEquals(expected, result);

        // Test AggregateTranslator
        CharSequenceTranslator translator1 = new CharSetTranslator();
        CharSequenceTranslator translator2 = new SpecialCharTranslator();
        CharSequenceTranslator expectedTranslator = new AggregateTranslator(translator1, translator2);
        translator = expectedTranslator;
        String testString = "&'%";
        StringWriter stringWriter = new StringWriter();
        translator.translate(testString, stringWriter);
        expected = "%26%27%25";
        String resultString = stringWriter.toString();
        assertEquals(expected, resultString);
    }

    @org.junit.Test
    public void testTranslateWithWriter() throws Exception {
        // Test empty input
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };
        Writer writer = new StringWriter();
        translator.translate(null, writer);
        assertEquals("", writer.toString());

        // Test translation with no translator
        translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                out.write(Character.toString(Character.codePointAt(input, index)).charAt(0));
                return Character.charCount(Character.codePointAt(input, index));
            }
        };
        String testString = "A";
        writer = new StringWriter();
        translator.translate(testString, writer);
        assertEquals("A", writer.toString());

        // Test CharSetTranslator
        translator = new CharSetTranslator();
        testString = "%";
        writer = new StringWriter();
        translator.translate(testString, writer);
        String expected = "%25";
        String result = writer.toString();
        assertEquals(expected, result);

        // Test SpecialCharTranslator
        translator = new SpecialCharTranslator();
        testString = "\'";
        writer = new StringWriter();
        translator.translate(testString, writer);
        expected = "&#39;";
        result = writer.toString();
        assertEquals(expected, result);

        // Test AggregateTranslator
        CharSequenceTranslator translator1 = new CharSetTranslator();
        CharSequenceTranslator translator2 = new SpecialCharTranslator();
        translator = new AggregateTranslator(translator1, translator2);
        testString = "&'%";
        writer = new StringWriter();
        translator.translate(testString, writer);
        expected = "%26%27%25";
        result = writer.toString();
        assertEquals(expected, result);
    }

    @org.junit.Test(expected = RuntimeException.class)
    public void testTranslateWithWriterIOException() throws Exception {
        // Test IOException when writing to StringWriter
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                throw new IOException();
            }
        };
        StringWriter stringWriter = new StringWriter();
        translator.translate(Character.toString('A'), stringWriter);
    }

    @org.junit.Test
    public void testWith() throws Exception {
        // Test merging translators
        CharSequenceTranslator translator1 = new CharSetTranslator();
        CharSequenceTranslator translator2 = new SpecialCharTranslator();
        CharSequenceTranslator expectedTranslator = new AggregateTranslator(translator1, translator2);
        CharSequenceTranslator actualTranslator = translator1.with(translator2);
        assertEquals(expectedTranslator.getClass(), actualTranslator.getClass());
    }
}