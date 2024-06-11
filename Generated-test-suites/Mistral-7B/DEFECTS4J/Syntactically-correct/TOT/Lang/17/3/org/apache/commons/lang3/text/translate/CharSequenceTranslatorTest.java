package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.apache.commons.lang3.text.translate.ReplaceTranslator;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for CharSequenceTranslator.
 */
public class CharSequenceTranslatorTest {

    @Test
    public void testBasicTranslation() throws Exception {
        CharSequenceTranslator translator = new ReplaceTranslator("Hello", "Hi");
        CharSequence input = "Hello World!";
        String expectedOutput = "Hi World!";
        String actualOutput = translator.translate(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testTranslationWithAggregateTranslator() throws Exception {
        CharSequenceTranslator translator = new AggregateTranslator(
                new ReplaceTranslator("Hello", "Hi"),
                new ReplaceTranslator("World", "Planet")
        );
        CharSequence input = "Hello World!";
        String expectedOutput = "Hi Planet!";
        String actualOutput = translator.translate(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullWriter() throws Exception {
        CharSequenceTranslator translator = new ReplaceTranslator("Hello", "Hi");
        Writer out = null;
        translator.translate(CharSequenceUtils.EMPTY_STRING, out);
    }

    @Test(expected = IOException.class)
    public void testIOError() throws Exception {
        CharSequenceTranslator translator = new PrintingTranslator(new StringWriter(10));
        CharSequence input = "\uD800\uDC00HelloWorld!";
        translator.translate(input, translator.getWriter());
    }

    @Test
    public void testTranslationWithSurrogatePair() throws Exception {
        CharSequenceTranslator translator = new ReplaceTranslator("Hello", "Hi");
        CharSequence input = "Hello世界!";
        String expectedOutput = "Hi世界!";
        String actualOutput = translator.translate(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testTranslationWithEmptyInput() throws Exception {
        CharSequenceTranslator translator = new ReplaceTranslator("Hello", "Hi");
        CharSequence input = CharSequenceUtils.EMPTY_STRING;
        String expectedOutput = CharSequenceUtils.EMPTY_STRING;
        String actualOutput = translator.translate(input);
        assertEquals(expectedOutput, actualOutput);
    }

    private static class PrintingTranslator extends CharSequenceTranslator {
        private final Writer writer;

        public PrintingTranslator(Writer writer) {
            this.writer = writer;
        }

        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            int consumed;
            while ((consumed = super.translate(input, index, out)) > 0) {
                index += consumed;
            }
            return index;
        }

        public Writer getWriter() {
            return writer;
        }
    }
}