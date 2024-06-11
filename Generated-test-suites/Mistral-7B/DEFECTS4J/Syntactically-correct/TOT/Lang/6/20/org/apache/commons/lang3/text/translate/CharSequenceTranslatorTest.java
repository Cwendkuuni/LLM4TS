package org.apache.commons.lang3.test.text.translate;

import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for CharSequenceTranslator.
 */
public class CharSequenceTranslatorTest {

    private static final CharSequenceTranslator UNDER_TEST = new ClassNameUnderTest();
    private static final CharSequenceTranslator TRANSLATOR_A = new TranslatorA();
    private static final CharSequenceTranslator TRANSLATOR_B = new TranslatorB();

    /**
     * Test method for {@link CharSequenceTranslator#translate(CharSequence, int, Writer)}.
     *
     * @throws Exception if an error occurs.
     */
    @Test
    public void testTranslate() throws Exception {
        String input = "Hello, 世界! 你好！";
        Writer output = new StringWriter();

        // Translate using the given CharSequenceTranslator.
        UNDER_TEST.translate(input, 0, output);

        // Check the output.
        String expectedOutput = "Hello,\\uD840\\uDC07\\uD841\\uDC00! \\u4E16\\u754C! \\u4F60\\u597D!";
        String actualOutput = output.toString();
        assertEquals(expectedOutput, actualOutput);

        // Test error scenario: Writer produces an IOException.
        output = new StringWriter() {
            @Override
            public void write(int c) throws IOException {
                throw new IOException();
            }
        };
        try {
            UNDER_TEST.translate(input, 0, output);
            fail("Expecting IOException from Writer.");
        } catch (IOException ioe) {
            // Expected.
        }

        // Merge translators A and B.
        CharSequenceTranslator mergedTranslator = UNDER_TEST.with(TRANSLATOR_A, TRANSLATOR_B);

        // Test merger functionality: Translate using the merged translator.
        output = new StringWriter();
        mergedTranslator.translate(input, 0, output);
        expectedOutput += " ABCDEFGHIJKLMNOPQRSTUVWXYZ"; // Translator A adds this.
        assertEquals(expectedOutput, output.toString());
    }

    /**
     * Test class for a custom translator A.
     */
    private static class TranslatorA extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            char c = Character.codePointAt(input, index);
            if (Character.isDigit(c)) {
                out.write(Character.toUpperCase(c));
                return 1;
            }
            return 0;
        }
    }

    /**
     * Test class for a custom translator B.
     */
    private static class TranslatorB extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            char c = Character.codePointAt(input, index);
            if (c == '\uD840\uDC07' || c == '\uD841\uDC00') {
                out.write(" ABCDEFGHIJKLMNOPQRSTUVWXYZ".charAt(Character.codePointAt(input, index + 1) - '\uD800'));
                return 2;
            }
            out.write(c);
            return Character.charCount(c);
        }
    }

    /**
     * Test class for a custom CharSequenceTranslator, UNDER_TEST.
     */
    private static class ClassNameUnderTest extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            if (Character.isDigit(Character.codePointAt(input, index))) {
                out.write(Character.toUpperCase(Character.codePointAt(input, index)));
                return 1;
            }
            return Character.charCount(Character.codePointAt(input, index));
        }
    }
}