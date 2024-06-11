package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for CharSequenceTranslator.
 */
public class CharSequenceTranslatorTest {

    /**
     * Test for translate method with normal characters.
     */
    @Test
    public void testTranslateNormal() throws Exception {
        CharSequenceTranslator translator = new DefaultTranslationCharSequenceTranslator();
        String input = "Hello World!";
        String expectedOutput = "Hello%20World%21";
        String actualOutput = translator.translate(input);
        assertEquals(expectedOutput, actualOutput);
    }

    /**
     * Test for translate method with multi-byte characters.
     */
    @Test
    public void testTranslateMultiByte() throws Exception {
        CharSequenceTranslator translator = new DefaultTranslationCharSequenceTranslator();
        String input = "こんにちは世界";
        String expectedOutput = "こんにちは%E3%81%A7%E3%82%A2";
        String actualOutput = translator.translate(input);
        assertEquals(expectedOutput, actualOutput);
    }

    /**
     * Test for translate method with empty input.
     */
    @Test
    public void testTranslateEmpty() throws Exception {
        CharSequenceTranslator translator = new DefaultTranslationCharSequenceTranslator();
        String input = "";
        String expectedOutput = "";
        String actualOutput = translator.translate(input);
        assertEquals(expectedOutput, actualOutput);
    }

    /**
     * Test for translate method with null input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateNull() throws Exception {
        CharSequenceTranslator translator = new DefaultTranslationCharSequenceTranslator();
        String input = null;
        translator.translate(input);
    }

    /**
     * Test for translate method with Writer argument.
     */
    @Test
    public void testTranslateWriter() throws Exception {
        CharSequenceTranslator translator = new DefaultTranslationCharSequenceTranslator();
        String input = "Hello%20World%21";
        StringWriter writer = new StringWriter();
        int consumed = translator.translate(new StringReader(input), 0, writer);
        String output = writer.toString();
        String expectedOutput = "Hello World!";
        assertEquals(expectedOutput, output);
        assertEquals(input.length() * 2, consumed);
    }

    /**
     * Test for translate method with invalid Writer argument.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateInvalidWriter() throws Exception {
        CharSequenceTranslator translator = new DefaultTranslationCharSequenceTranslator();
        StringWriter writer = null;
        translator.translate(new StringReader("Test"), 0, writer);
    }

    /**
     * Test for with method.
     */
    @Test
    public void testWith() throws Exception {
        CharSequenceTranslator translator1 = new DefaultTranslationCharSequenceTranslator();
        CharSequenceTranslator translator2 = new HtmlEscapingTranslationCharSequenceTranslator();
        CharSequenceTranslator combinedTranslator = translator1.with(translator2);
        String input = "<img src=\"image.jpg\">";
        String expectedOutput = "&lt;img src=&quot;image.jpg&quot;&gt;";
        String actualOutput = combinedTranslator.translate(input);
        assertEquals(expectedOutput, actualOutput);
    }

}