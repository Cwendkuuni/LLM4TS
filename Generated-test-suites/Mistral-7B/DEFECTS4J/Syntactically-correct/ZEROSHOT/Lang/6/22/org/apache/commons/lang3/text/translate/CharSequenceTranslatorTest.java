package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.StringWriter;
import java.util.Locale;

import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator.AggregateTranslator;
import org.junit.Test;

/**
 * Unit tests for the {@link CharSequenceTranslator} class.
 *
 * @author John Reese
 */
public class CharSequenceTranslatorTest {

    /**
     * Test the translate method of the {@link CharSequenceTranslator} class.
     */
    @Test
    public void testTranslate() {
        // Create a translator instance
        CharSequenceTranslator translator = new IdentityTranslator();

        // Test translating an empty sequence
        String result = translator.translate((CharSequence) "");
        assertEquals("", result);

        // Test translating a sequence of length one
        CharSequence input = "A";
        result = translator.translate(input);
        assertEquals(input.toString(), result);

        // Test translating a sequence of length two with no translation needed
        input = "AB";
        result = translator.translate(input);
        assertEquals(input.toString(), result);

        // Test translating a sequence of length two with translation needed
        input = "\u007F\u0041";
        result = translator.translate(input);
        assertEquals("\u007F\u0041", result);

        // Test translating a sequence with multiple translations needed
        input = "\uD800\uDC00\uD800\uDCDA";
        result = translator.translate(input);
        assertEquals("\uD800\uDC00\uD800\uDCDA", result);

        // Test translating a sequence of length zero
        input = "";
        result = translator.translate(input);
        assertNull(result);

        // Test translating a sequence of length one with an exception
        input = "\uFFFE";
        try {
            result = translator.translate(input);
            fail("Expected IOException");
        } catch (Exception e) {
            assertEquals(IOException.class, e.getClass());
        }
    }

    /**
     * Test the translate method of the {@link CharSequenceTranslator} class with
     * multiple translators merged.
     */
    @Test
    public void testTranslateWithMergedTranslators() {
        // Create a translator instance
        CharSequenceTranslator identityTranslator = new IdentityTranslator();
        CharSequenceTranslator hexTranslator = new HexTranslator();

        // Merge the translators
        CharSequenceTranslator mergedTranslator = identityTranslator.with(hexTranslator);

        // Test translating a sequence with no translation needed
        CharSequence input = "AB";
        String result = mergedTranslator.translate(input);
        assertEquals(input.toString(), result);

        // Test translating a sequence with translation needed using the identity
        // translator
        input = "AB";
        result = identityTranslator.translate(input);
        assertEquals(input.toString(), result);

        // Test translating a sequence with translation needed using the hex
        // translator
        input = "AB";
        result = hexTranslator.translate(input);
        assertEquals("4142", result);

        // Test translating a sequence with translation needed using the merged
        // translators
        input = "AB";
        result = mergedTranslator.translate(input);
        assertEquals(result, "4142");

        // Test translating a sequence of length one with no translation needed
        input = "A";
        result = mergedTranslator.translate(input);
        assertEquals(input.toString(), result);

        // Test translating a sequence of length one with translation needed using
        // the identity translator
        input = "\u007F";
        result = identityTranslator.translate(input);
        assertEquals("\u007F", result);

        // Test translating a sequence of length one with translation needed using
        // the hex translator
        input = "\u007F";
        result = hexTranslator.translate(input);
        assertEquals("7F", result);

        // Test translating a sequence of length one with translation needed using
        // the merged translators
        input = "\u007F";
        result = mergedTranslator.translate(input);
        assertEquals("7F", result);

        // Test translating a sequence of length two with no translation needed
        input = "AB";
        result = mergedTranslator.translate(input);
        assertEquals(input.toString(), result);

        // Test translating a sequence of length two with translation needed using
        // the identity translator
        input = "\u007F\u0041";
        result = identityTranslator.translate(input);
        assertEquals("\u007F\u0041", result);

        // Test translating a sequence of length two with translation needed using
        // the hex translator
        input = "\u007F\u0041";
        result = hexTranslator.translate(input);
        assertEquals("7F41", result);

        // Test translating a sequence of length two with translation needed using
        // the merged translators
        input = "\u007F\u0041";
        result = mergedTranslator.translate(input);
        assertEquals("7F41", result);

        // Test translating a sequence of length two with multiple translations needed
        input = "\uD800\uDC00\uD800\uDCDA";
        result = mergedTranslator.translate(input);
        assertEquals("\uD800\uDC00\uD800\uDCDA", result);

        // Test translating a sequence of length zero
        input = "";
        result = mergedTranslator.translate(input);
        assertNull(result);

        // Test translating a sequence of length one with an exception
        input = "\uFFFE";
        try {
            result = mergedTranslator.translate(input);
            fail("Expected IOException");
        } catch (Exception e) {
            assertEquals(IOException.class, e.getClass());
        }
    }

    /**
     * A {@link CharSequenceTranslator} that does not translate any characters.
     */
    public static class IdentityTranslator extends CharSequenceTranslator {

        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            char c = Character.codePointAt(input, index);
            int len = Character.charCount(c);
            out.write(c);
            return len;
        }
    }

    /**
     * A {@link CharSequenceTranslator} that translates characters to their upper
     * case hexadecimal representation.
     */
    public static class HexTranslator extends CharSequenceTranslator {

        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            char c = Character.codePointAt(input, index);
            String hex = hex(c);
            out.write(hex);
            out.write(58); // write the semicolon separator
            int len = Character.charCount(c) + 2;
            return len;
        }
    }

    /**
     * A {@link CharSequenceTranslator} that merges multiple {@link CharSequenceTranslator}
     * instances.
     */
    public static class AggregateTranslator extends CharSequenceTranslator {

        private final CharSequenceTranslator[] translators;

        public AggregateTranslator(CharSequenceTranslator... translators) {
            this.translators = translators;
        }

        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            int len = 0;
            for (CharSequenceTranslator translator : translators) {
                len += translator.translate(input, index, out);
                index += len;
            }
            return len;
        }
    }
}