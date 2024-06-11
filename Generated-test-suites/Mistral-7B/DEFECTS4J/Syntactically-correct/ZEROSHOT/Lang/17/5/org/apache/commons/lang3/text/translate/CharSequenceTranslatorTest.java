package org.apache.commons.lang3.text.translate;

import static org.apache.commons.lang3.text.translate.CharSequenceTranslator.hex;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Locale;

import org.apache.commons.lang3.CharSequences;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslatorFactory;
import org.apache.commons.lang3.text.translate.SimpleTranslator;
import org.junit.Test;

/**
 * Test file for {@link CharSequenceTranslator} class.
 */
public class CharSequenceTranslatorTest {

    /**
     * Test for {@link CharSequenceTranslator#translate(CharSequence, int, Writer)} method.
     * @throws IOException
     */
    @Test
    public void testTranslateCharSequenceWriter() throws IOException {
        CharSequenceTranslator translator = new SimpleTranslator();
        StringWriter stringWriter = new StringWriter();

        int inputLength = "testString".length();
        CharSequence input = CharSequences.subsequence("testString", 0, inputLength);

        int codePointsConsumed = translator.translate(input, 0, stringWriter);
        String expectedOutput = "test%String";
        String actualOutput = stringWriter.toString();

        assertEquals("Unexpected output from translate method", expectedOutput, actualOutput);
        assertEquals("Unexpected number of code points consumed", inputLength, codePointsConsumed);
    }

    /**
     * Test for {@link CharSequenceTranslator#translate(CharSequence)} method.
     */
    @Test
    public void testTranslateCharSequence() {
        CharSequenceTranslator translator = new SimpleTranslator();
        String expectedOutput = "test%String";

        String input = "testString";
        String actualOutput = translator.translate(input);

        assertEquals("Unexpected output from translate method", expectedOutput, actualOutput);
    }

    /**
     * Test for {@link CharSequenceTranslator#translate(CharSequence, Writer)} method with null input.
     * @throws IOException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateNullCharSequence() throws IOException {
        CharSequenceTranslator translator = new SimpleTranslator();
        StringWriter stringWriter = new StringWriter();

        translator.translate(null, stringWriter);
    }

    /**
     * Test for {@link CharSequenceTranslator#translate(CharSequence, Writer)} method with null Writer.
     * @throws IOException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateNullWriter() throws IOException {
        CharSequenceTranslator translator = new SimpleTranslator();
        CharSequence input = CharSequences.subsequence("testString", 0, 5);

        translator.translate(input, null);
    }

    /**
     * Test for {@link CharSequenceTranslator#with(CharSequenceTranslator...) method}.
     */
    @Test
    public void testComposeTranslators() {
        CharSequenceTranslator translator1 = new SimpleTranslator();
        CharSequenceTranslator translator2 = new SimpleTranslator();

        CharSequenceTranslator composedTranslator = translator1.with(translator2);
        String input = "test%String";
        String expectedOutput = "test%%String";

        String actualOutput = composedTranslator.translate(input);

        assertEquals("Unexpected output from composed translators", expectedOutput, actualOutput);
    }

    /**
     * Test for {@link CharSequenceTranslatorFactory#createCharSequenceTranslator()} method.
     */
    @Test
    public void testCreateCharSequenceTranslator() {
        CharSequenceTranslator translator = CharSequenceTranslatorFactory.createCharSequenceTranslator();
        String input = "test%String";
        String expectedOutput = "test%String";

        String actualOutput = translator.translate(input);

        assertEquals("Unexpected output from created translator", expectedOutput, actualOutput);
    }
}

class SimpleTranslator extends CharSequenceTranslator {
    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        char c = Character.isSurrogate(Character.codePointAt(input, index)) ? Character.highSurrogate(input, index) : Character.codePointAt(input, index);
        int codePointsConsumed = Character.isSurrogate(Character.codePointAt(input, index + 1)) ? 2 : 1;

        for (int i = 0; i < codePointsConsumed; i++) {
            out.write(hex(Character.codePointAt(input, index++)).toCharArray());
        }

        return codePointsConsumed;
    }
}