package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for {@link CharSequenceTranslator}
 */
public class CharSequenceTranslatorTest {

    /**
     * Test for {@link CharSequenceTranslator#translate(CharSequence, int, Writer)} method.
     */
    @Test
    public void testTranslateMethod() {
        CharSequenceTranslator translator = new Translator(); // Assuming Translator is a concrete implementation of CharSequenceTranslator
        String input = "Hello, World! 你好，世界！";
        String expectedOutput = "Hello, World! %&#x4F60%&#x597D%&#x8A9E%&#x5341%&#x4E8C%&#x4E16%&#x4F1A%&#x4E2D%&#x5B54%&#x7574%&#x200D%&#x7684%&#x540D%&#x65E5%&#x672C%&#x200D%&#x7684%&#x597D%&#x200D%&#x7684%&#x4E16%&#x5341%&#x7574%&#x6863%&#x6527%&#x6124%&#x672C%&#x200D%&#x7684%&#x540D%&#x65E5%&#x6772%&#x5230%&#x2122%&#x200D%&#x7684%&#x4E16%&#x4E1A%&#x200D%&#x7684%&#x5B8B%&#x5168%&#x6863%";
        String output = "";

        try (StringWriter writer = new StringWriter()) {
            int index = 0;
            while (index < input.length()) {
                int consumed = translator.translate(CharSequenceUtils.subsequence(input, index), index, writer);
                index += consumed;
            }
            output = writer.toString();
        } catch (IOException e) {
            assertFalse("IOException should not be thrown", e.isInstancesOf(IOException.class));
        }

        assertEquals(expectedOutput, output);
    }

    /**
     * Test for {@link CharSequenceTranslator#translate(CharSequence)} method.
     */
    @Test
    public void testTranslateMethodWithStringWriter() {
        CharSequenceTranslator translator = new Translator(); // Assuming Translator is a concrete implementation of CharSequenceTranslator
        String input = "Hello, World! 你好，世界！";
        String expectedOutput = "Hello, World! %&#x4F60%&#x597D%&#x8A9E%&#x5341%&#x4E8C%&#x4E16%&#x4F1A%&#x4E2D%&#x5B54%&#x7574%&#x200D%&#x7684%&#x540D%&#x65E5%&#x672C%&#x200D%&#x7684%&#x4E16%&#x5341%&#x7574%&#x6863%&#x6527%&#x6124%&#x672C%&#x200D%&#x7684%&#x540D%&#x65E5%&#x6772%&#x5230%&#x2122%&#x200D%&#x7684%&#x4E16%&#x4E1A%&#x200D%&#x7684%&#x5B8B%&#x5168%&#x6863%";
        String output = "";

        output = translator.translate(input);

        assertEquals(expectedOutput, output);
    }

    /**
     * Test for an empty CharSequence input.
     */
    @Test
    public void testEmptyCharSequenceInput() {
        CharSequenceTranslator translator = new Translator(); // Assuming Translator is a concrete implementation of CharSequenceTranslator
        String input = "";
        String expectedOutput = "";

        String output = translator.translate(input);

        assertEquals(expectedOutput, output);
    }

    /**
     * Test for a null CharSequence input.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullCharSequenceInput() {
        CharSequenceTranslator translator = new Translator(); // Assuming Translator is a concrete implementation of CharSequenceTranslator
        String input = null;

        translator.translate(input);
    }

    /**
     * Test for {@link CharSequenceTranslator#translate(CharSequence, Writer)} method with a null Writer.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullWriterInput() {
        CharSequenceTranslator translator = new Translator(); // Assuming Translator is a concrete implementation of CharSequenceTranslator
        String input = "Hello, World! 你好，世界！";
        Writer out = null;

        translator.translate(CharSequenceUtils.subsequence(input, 0), out);
    }

    /**
     * Test merging translators.
     */
    @Test
    public void testMergeTranslators() {
        CharSequenceTranslator translator1 = new Translator1(); // Assuming Translator1 is a concrete implementation of CharSequenceTranslator
        CharSequenceTranslator translator2 = new Translator2(); // Assuming Translator2 is a concrete implementation of CharSequenceTranslator
        String input = "Hello, World! 你好，世界！";
        String expectedOutput = "Hello, World! %&#x4F60%&#x597D%&#x8A9E%&#x5341%&#x4E8C%&#x4E16%&#x4F1A%&#x4E2D%&#x5B54%&#x7574%&#x200D%&#x7684%&#x540D%&#x65E5%&#x672C%&#x200D%&#x7684%&#x4E16%&#x5341%&#x7574%&#x6863%&#x6527%&#x6124%&#x672C%&#x200D%&#x7684%&#x540D%&#x65E5%&#x6772%&#x5230%&#x2122%&#x200D%&#x7684%&#x4E16%&#x4E1A%&#x200D%&#x7684%&#x5B8B%&#x5168%&#x6863%";
        String output = "";

        CharSequenceTranslator mergedTranslator = translator1.with(translator2);

        try (StringWriter writer = new StringWriter()) {
            int index = 0;
            while (index < input.length()) {
                int consumed = mergedTranslator.translate(CharSequenceUtils.subsequence(input, index), index, writer);
                index += consumed;
            }
            output = writer.toString();
        } catch (IOException e) {
            assertFalse("IOException should not be thrown", e.isInstancesOf(IOException.class));
        }

        assertEquals(expectedOutput, output);
    }
}