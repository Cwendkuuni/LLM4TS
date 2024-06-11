package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;

import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslatorFactory;
import org.apache.commons.lang3.text.translate.HtmlTranslator;
import org.junit.Test;

import java.io.StringWriter;
import java.util.Locale;

/**
 * Test class for CharSequenceTranslator.
 */
public class CharSequenceTranslatorTest {

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, int, Writer)} method.
     */
    @Test
    public void testTranslate() throws Exception {
        CharSequenceTranslator translator = new HtmlTranslator();
        String testInput = "Hello, <world>! 你好，世界!";
        StringWriter writer = new StringWriter();
        int index = 0;
        int expectedConsumed = 6; // 'H' and 'e'
        int actualConsumed = translator.translate(new StringCharSequence(testInput), index, writer);
        assertEquals("Unexpected number of codepoints consumed.", expectedConsumed, actualConsumed);
        assertEquals("Unexpected translated output.", "Hello, &lt;world&gt;! \\u4F60\\u597D\\u4E2D\\u4EBA!", writer.toString());

        index += expectedConsumed;
        expectedConsumed = 11; // '世' and '界'
        actualConsumed = translator.translate(new StringCharSequence(testInput), index, writer);
        assertEquals("Unexpected number of codepoints consumed.", expectedConsumed, actualConsumed);
        assertEquals("Unexpected translated output.", "Hello, &lt;world&gt;! \\u4F60\\u597D\\u4E2D\\u4EBA! \\u5B8B\\u4EBA!", writer.toString());

        index += expectedConsumed;
        expectedConsumed = 2; // '!'
        actualConsumed = translator.translate(new StringCharSequence(testInput), index, writer);
        assertEquals("Unexpected number of codepoints consumed.", expectedConsumed, actualConsumed);
        assertEquals("Unexpected translated output.", "Hello, &lt;world&gt;! \\u4F60\\u597D\\u4E2D\\u4EBA! \\u5B8B\\u4EBA!", writer.toString());
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence)} method.
     */
    @Test
    public void testTranslateWithWriter() throws Exception {
        CharSequenceTranslator translator = new HtmlTranslator();
        String testInput = "Hello, <world>! 你好，世界!";
        String expectedOutput = "Hello, &lt;world&gt;! \\u4F60\\u597D\\u4E2D\\u4EBA! \\u5B8B\\u4EBA!";
        String actualOutput = translator.translate(new StringCharSequence(testInput));
        assertEquals("Unexpected translated output.", expectedOutput, actualOutput);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} method.
     */
    @Test
    public void testTranslateWithWriter2() throws Exception {
        CharSequenceTranslator translator = new HtmlTranslator();
        String testInput = "Hello, <world>! 你好，世界!";
        StringWriter writer = new StringWriter();
        translator.translate(new StringCharSequence(testInput), writer);
        String expectedOutput = "Hello, &lt;world&gt;! \\u4F60\\u597D\\u4E2D\\u4EBA! \\u5B8B\\u4EBA!";
        String actualOutput = writer.toString();
        assertEquals("Unexpected translated output.", expectedOutput, actualOutput);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence)} method with empty string.
     */
    @Test
    public void testTranslateEmptyString() throws Exception {
        CharSequenceTranslator translator = new HtmlTranslator();
        String testInput = "";
        String expectedOutput = "";
        String actualOutput = translator.translate(new StringCharSequence(testInput));
        assertEquals("Unexpected translated output.", expectedOutput, actualOutput);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence)} method with null string.
     */
    @Test(expected = NullPointerException.class)
    public void testTranslateNullString() throws Exception {
        CharSequenceTranslator translator = new HtmlTranslator();
        translator.translate(null);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} method with null writer.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateNullWriter() throws Exception {
        CharSequenceTranslator translator = new HtmlTranslator();
        String testInput = "Hello, <world>! 你好，世界!";
        translator.translate(new StringCharSequence(testInput), null);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence)} method with empty CharSequence.
     */
    @Test
    public void testTranslateEmptyCharSequence() throws Exception {
        CharSequenceTranslator translator = new HtmlTranslator();
        CharSequence testInput = Character.toString((char) 0);
        String expectedOutput = "";
        String actualOutput = translator.translate(testInput);
        assertEquals("Unexpected translated output.", expectedOutput, actualOutput);
    }

    /**
     * Test method for {@link CharSequenceTranslator#with(CharSequenceTranslator...)} method.
     */
    @Test
    public void testWith() throws Exception {
        CharSequenceTranslator translator1 = new HtmlTranslator();
        CharSequenceTranslator translator2 = new ReplacementTranslator("world", "earth");
        CharSequenceTranslator combinedTranslator = translator1.with(translator2);
        String testInput = "Hello, <world>! 你好，world!";
        String expectedOutput = "Hello, &lt;world&gt;! \\u4F60\\u597D\\u5272\\u7684earth! \\u5B8B\\u4EBA!";
        String actualOutput = combinedTranslator.translate(new StringCharSequence(testInput));
        assertEquals("Unexpected translated output.", expectedOutput, actualOutput);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} method with empty CharSequence.
     */
    @Test
    public void testTranslateEmptyCharSequenceWithWriter() throws Exception {
        CharSequenceTranslator translator = new HtmlTranslator();
        StringWriter writer = new StringWriter();
        CharSequence testInput = Character.toString((char) 0);
        translator.translate(testInput, writer);
        String expectedOutput = "";
        String actualOutput = writer.toString();
        assertEquals("Unexpected translated output.", expectedOutput, actualOutput);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} method with null CharSequence.
     */
    @Test(expected = NullPointerException.class)
    public void testTranslateNullCharSequence() throws Exception {
        CharSequenceTranslator translator = new HtmlTranslator();
        StringWriter writer = new StringWriter();
        translator.translate(null, writer);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence)} method with null CharSequence.
     */
    @Test(expected = NullPointerException.class)
    public void testTranslateNullCharSequence2() throws Exception {
        CharSequenceTranslator translator = new HtmlTranslator();
        translator.translate(null);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} method with empty writer.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateEmptyWriter() throws Exception {
        CharSequenceTranslator translator = new HtmlTranslator();
        String testInput = "Hello, <world>! 你好，世界!";
        translator.translate(new StringCharSequence(testInput), new StringWriter());
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence)} method with empty writer.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateEmptyWriter2() throws Exception {
        CharSequenceTranslator translator = new HtmlTranslator();
        translator.translate(new StringCharSequence(""), new StringWriter());
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} method with null writer and empty CharSequence.
     */
    @Test(expected = NullPointerException.class)
    public void testTranslateNullWriterEmptyCharSequence() throws Exception {
        CharSequenceTranslator translator = new HtmlTranslator();
        translator.translate(Character.toString((char) 0), null);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence)} method with null writer and empty CharSequence.
     */
    @Test(expected = NullPointerException.class)
    public void testTranslateNullWriterEmptyCharSequence2() throws Exception {
        CharSequenceTranslator translator = new HtmlTranslator();
        translator.translate(null, new StringWriter());
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} method with null CharSequence and null writer.
     */
    @Test(expected = NullPointerException.class)
    public void testTranslateNullCharSequenceNullWriter() throws Exception {
        CharSequenceTranslator translator = new HtmlTranslator();
        translator.translate(null, null);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence)} method with null CharSequence and null writer.
     */
    @Test(expected = NullPointerException.class)
    public void testTranslateNullCharSequenceNullWriter2() throws Exception {
        CharSequenceTranslator translator = new HtmlTranslator();
        translator.translate(null);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} method with empty CharSequence and non-empty writer.
     */
    @Test
    public void testTranslateEmptyCharSequenceNonEmptyWriter() throws Exception {
        CharSequenceTranslator translator = new HtmlTranslator();
        StringWriter writer = new StringWriter();
        String expectedOutput = "";
        translator.translate(Character.toString((char) 0), writer);
        String actualOutput = writer.toString();
        assertEquals("Unexpected translated output.", expectedOutput, actualOutput);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence)} method with empty CharSequence and non-empty writer.
     */
    @Test
    public void testTranslateEmptyCharSequenceNonEmptyWriter2() throws Exception {
        CharSequenceTranslator translator = new HtmlTranslator();
        translator.translate(Character.toString((char) 0));
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} method with non-empty CharSequence and empty writer.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateNonEmptyCharSequenceEmptyWriter() throws Exception {
        CharSequenceTranslator translator = new HtmlTranslator();
        String testInput = "Hello, <world>! 你好，世界!";
        StringWriter writer = new StringWriter();
        translator.translate(new StringCharSequence(testInput), new StringWriter());
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence)} method with non-empty CharSequence and empty writer.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateNonEmptyCharSequenceEmptyWriter2() throws Exception {
        CharSequenceTranslator translator = new HtmlTranslator();
        translator.translate(new StringCharSequence("Hello, <world>! 你好，世界!"));
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)} method with non-empty CharSequence and non-empty writer.
     */
    @Test
    public void testTranslateNonEmptyCharSequenceNonEmptyWriter() throws Exception {
        CharSequenceTranslator translator = new HtmlTranslator();
        StringWriter writer = new StringWriter();
        String testInput = "Hello, <world>! 你好，世界!";
        String expectedOutput = "Hello, &lt;world&gt;! \\u4F60\\u597D\\u5272\\u7684world! \\u5B8B\\u4EBA!";
        translator.translate(new StringCharSequence(testInput), writer);
        String actualOutput = writer.toString();
        assertEquals("Unexpected translated output.", expectedOutput, actualOutput);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence)} method with non-empty CharSequence and non-empty writer.
     */
    @Test
    public void testTranslateNonEmptyCharSequenceNonEmptyWriter2() throws Exception {
        CharSequenceTranslator translator = new HtmlTranslator();
        String testInput = "Hello, <world>! 你好，世界!";
        String expectedOutput = "Hello, &lt;world&gt;! \\u4F60\\u597D\\u5272\\u7684world! \\u5B8B\\u4EBA!";
        String actualOutput = translator.translate(new StringCharSequence(testInput)).toString();
        assertEquals("Unexpected translated output.", expectedOutput, actualOutput);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence)} method with null CharSequence and non-empty writer.
     */
    @Test(expected = NullPointerException.class)
    public void testTranslateNullCharSequenceNonEmptyWriter() throws Exception {
        CharSequenceTranslator translator = new HtmlTranslator();
        StringWriter writer = new StringWriter();
        translator.translate(null, writer);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence)} method with non-empty CharSequence and null writer.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateNonEmptyCharSequenceNullWriter() throws Exception {
        CharSequenceTranslator translator = new HtmlTranslator();
        String testInput = "Hello, <world>! 你好，世界!";
        translator.translate(new StringCharSequence(testInput));
    }
}

class StringCharSequence implements CharSequence {
    private final String string;

    public StringCharSequence(String string) {
        this.string = string;
    }

    @Override
    public int length() {
        return string.length();
    }

    @Override
    public char charAt(int index) {
        return string.charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return new StringCharSequence(string.substring(start, end));
    }

    @Override
    public String toString() {
        return string;
    }
}