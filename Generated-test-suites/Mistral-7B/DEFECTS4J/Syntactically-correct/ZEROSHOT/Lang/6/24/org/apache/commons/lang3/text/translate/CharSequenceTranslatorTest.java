package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator.AggregateTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator.CodePointTranslator;
import org.junit.Test;

/**
 * Test class for {@link CharSequenceTranslator}.
 */
public class CharSequenceTranslatorTest {

    /**
     * Test for {@link CharSequenceTranslator#translate(CharSequence, Writer)} method.
     */
    @Test
    public void testTranslateMethod() {
        CharSequenceTranslator translator = new CodePointTranslator();
        Writer writer = new StringWriter();
        CharSequence input = "你好，世界!";
        int len = translator.translate(input, 0, writer) + translator.translate(input, input.length(), writer);
        String expectedOutput = "\u4F60\u597D\u4E2D\u4E5F\u4EBA\u4E2C\u7576\u672C\u200D\u00A0!";
        String output = writer.toString();
        assertEquals(expectedOutput, output);
        assertEquals(len, input.length() * 2);
    }

    /**
     * Test for {@link CharSequenceTranslator#translate(CharSequence)} method.
     */
    @Test
    public void testTranslateMethodWithStringWriter() {
        CharSequenceTranslator translator = new CodePointTranslator();
        String output = translator.translate(new String("你好，世界!"));
        String expectedOutput = "\u4F60\u597D\u4E2D\u4E5F\u4EBA\u4E2C\u7576\u672C\u200D\u00A0!";
        assertEquals(expectedOutput, output);
    }

    /**
     * Test for {@link CharSequenceTranslator#with(CharSequenceTranslator...)} method.
     */
    @Test
    public void testWithMethod() {
        CharSequenceTranslator firstTranslator = new CodePointTranslator();
        CharSequenceTranslator secondTranslator = new CodePointTranslator();
        CharSequenceTranslator expectedOutput = new AggregateTranslator(new CharSequenceTranslator[] { firstTranslator, secondTranslator });
        CharSequenceTranslator output = firstTranslator.with(secondTranslator);
        assertSame(expectedOutput, output);
    }

    /**
     * Test for empty constructor.
     */
    @Test
    public void testEmptyConstructor() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {};
        assertNull(translator);
    }

    /**
     * Test for null Writer input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullWriterInput() {
        CharSequenceTranslator translator = new CodePointTranslator();
        translator.translate(new String("你好，世界!"), null);
    }

    /**
     * Test for empty CharSequence input.
     */
    @Test
    public void testEmptyCharSequenceInput() {
        CharSequenceTranslator translator = new CodePointTranslator();
        StringWriter writer = new StringWriter();
        int len = translator.translate(Character.DEAD, 0, writer) + translator.translate(Character.DEAD, Character.DEAD.length(), writer);
        String expectedOutput = "";
        String output = writer.toString();
        assertEquals(expectedOutput, output);
        assertEquals(len, 0);
    }

    /**
     * Test for null CharSequence input.
     */
    @Test
    public void testNullCharSequenceInput() {
        CharSequenceTranslator translator = new CodePointTranslator();
        Writer writer = new StringWriter();
        int len = translator.translate(null, 0, writer) + translator.translate(null, 0, writer);
        String output = writer.toString();
        assertNull(output);
        assertEquals(len, 0);
    }

    /**
     * Test for empty input.
     */
    @Test
    public void testEmptyInput() {
        CharSequenceTranslator translator = new CodePointTranslator();
        StringWriter writer = new StringWriter();
        int len = translator.translate(Character.DEAD, 0, writer) + translator.translate(Character.DEAD, Character.DEAD.length(), writer);
        String expectedOutput = "";
        String output = writer.toString();
        assertEquals(expectedOutput, output);
        assertEquals(len, 0);
    }

    /**
     * Test for no codepoint consumed.
     */
    @Test
    public void testNoCodepointConsumed() {
        CharSequenceTranslator translator = new CodePointTranslator();
        CharSequence input = Character.toString('\u0000');
        StringWriter writer = new StringWriter();
        int len = translator.translate(input, input.length(), writer);
        assertEquals(len, 0);
        String output = writer.toString();
        assertTrue(output.isEmpty());
    }
}