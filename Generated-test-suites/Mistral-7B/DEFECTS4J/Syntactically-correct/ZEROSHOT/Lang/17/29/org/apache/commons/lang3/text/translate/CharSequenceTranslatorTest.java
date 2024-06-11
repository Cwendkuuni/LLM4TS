package org.apache.commons.lang3.text.translate;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.text.translate.CharSequenceTranslator.hex;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.StringWriter;
import java.util.Locale;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.HtmlTranslator;
import org.apache.commons.lang3.text.translate.StringEscapeUtils;

import org.junit.Test;

/**
 * Test class for CharSequenceTranslator.
 */
public class CharSequenceTranslatorTest {

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, int, Writer)}.
     */
    @Test
    public void testTranslate() throws Exception {
        // Test empty CharSequence
        CharSequenceTranslator translator = new StringEscapeUtils();
        CharSequence input = EMPTY;
        StringWriter output = new StringWriter();
        int index = 0;
        int consumed = translator.translate(input, index, output);
        assertEquals(0, consumed);
        assertEquals(EMPTY, output.toString());

        // Test null CharSequence and Writer
        input = null;
        output = null;
        translator = new HtmlTranslator();
        consumed = translator.translate(input, index, output);
        assertNull(output);
        assertNull(input);

        // Test simple case
        input = "abc";
        output = new StringWriter();
        translator = new HtmlTranslator();
        consumed = translator.translate(input, index, output);
        assertEquals(3, consumed);
        assertEquals("&amp;a;&amp;b;&amp;c", output.toString());

        // Test surrogate pairs
        input = "abc\uD800\uDC00";
        output = new StringWriter();
        translator = new HtmlTranslator();
        consumed = translator.translate(input, index, output);
        assertEquals(5, consumed);
        assertEquals("&amp;#xF0900;&#xF0901;c", output.toString());

        // Test merge translators
        CharSequenceTranslator translator1 = new StringEscapeUtils();
        CharSequenceTranslator translator2 = new HtmlTranslator();
        CharSequenceTranslator translator3 = new HtmlTranslator();
        CharSequenceTranslator mergedTranslator = new AggregateTranslator(translator1, translator2, translator3);
        input = "ab<c>d&e";
        output = new StringWriter();
        index = 0;
        consumed = mergedTranslator.translate(input, index, output);
        assertEquals(5, consumed);
        assertEquals("&amp;#x61;#x62;<>&amp;#x65;e", output.toString());
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence)}.
     */
    @Test
    public void testTranslateCharSequence() throws Exception {
        // Test empty CharSequence
        CharSequenceTranslator translator = new StringEscapeUtils();
        CharSequence input = EMPTY;
        String result = translator.translate(input);
        assertNull(result);

        // Test null CharSequence
        input = null;
        result = translator.translate(input);
        assertNull(result);

        // Test simple case
        input = "abc";
        result = translator.translate(input);
        assertEquals("&amp;a;&amp;b;&amp;c", result);

        // Test surrogate pairs
        input = "abc\uD800\uDC00";
        result = translator.translate(input);
        assertEquals("&amp;#xF0900;&#xF0901;c", result);

        // Test merge translators
        CharSequenceTranslator translator1 = new StringEscapeUtils();
        CharSequenceTranslator translator2 = new HtmlTranslator();
        CharSequenceTranslator translator3 = new HtmlTranslator();
        CharSequenceTranslator mergedTranslator = new AggregateTranslator(translator1, translator2, translator3);
        input = "ab<c>d&e";
        result = mergedTranslator.translate(input);
        assertEquals("&amp;#x61;#x62;<>&amp;#x65;e", result);
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, Writer)}.
     */
    @Test
    public void testTranslateWithWriter() throws Exception {
        // Test empty CharSequence
        CharSequenceTranslator translator = new StringEscapeUtils();
        CharSequence input = EMPTY;
        Writer output = new StringWriter();
        int index = 0;
        translator.translate(input, output);
        assertEquals(0, output.toString().length());

        // Test null CharSequence and Writer
        input = null;
        output = null;
        translator = new HtmlTranslator();
        translator.translate(input, output);

        // Test simple case
        input = "abc";
        output = new StringWriter();
        translator = new HtmlTranslator();
        translator.translate(input, output);
        assertEquals("&amp;a;&amp;b;&amp;c", output.toString());

        // Test surrogate pairs
        input = "abc\uD800\uDC00";
        output = new StringWriter();
        translator = new HtmlTranslator();
        translator.translate(input, output);
        assertEquals("&amp;#xF0900;&#xF0901;c", output.toString());

        // Test merge translators
        CharSequenceTranslator translator1 = new StringEscapeUtils();
        CharSequenceTranslator translator2 = new HtmlTranslator();
        CharSequenceTranslator translator3 = new HtmlTranslator();
        CharSequenceTranslator mergedTranslator = new AggregateTranslator(translator1, translator2, translator3);
        input = "ab<c>d&e";
        output = new StringWriter();
        mergedTranslator.translate(input, output);
        assertEquals("&amp;#x61;#x62;<>&amp;#x65;e", output.toString());
    }

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence)}.
     */
    @Test
    public void testTranslateString() throws Exception {
        // Test empty String
        CharSequenceTranslator translator = new StringEscapeUtils();
        String input = EMPTY;
        String result = translator.translate(input);
        assertNull(result);

        // Test null String
        input = null;
        result = translator.translate(input);
        assertNull(result);

        // Test simple case
        input = "abc";
        result = translator.translate(input);
        assertEquals("&amp;a;&amp;b;&amp;c", result);

        // Test surrogate pairs
        input = "abc\uD800\uDC00";
        result = translator.translate(input);
        assertEquals("&amp;#xF0900;&#xF0901;c", result);

        // Test merge translators
        CharSequenceTranslator translator1 = new StringEscapeUtils();
        CharSequenceTranslator translator2 = new HtmlTranslator();
        CharSequenceTranslator translator3 = new HtmlTranslator();
        CharSequenceTranslator mergedTranslator = new AggregateTranslator(translator1, translator2, translator3);
        input = "ab<c>d&e";
        result = mergedTranslator.translate(input);
        assertEquals("&amp;#x61;#x62;<>&amp;#x65;e", result);
    }

}