package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.StringWriter;
import java.util.HashMap;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.text.translate.LookupTranslator;
import org.junit.Test;

/**
 * Test class for {@link LookupTranslator}
 *
 * @since 3.0
 */
public class LookupTranslatorTest {

    /**
     * Test constructor with empty lookup table
     */
    @Test
    public void testEmptyLookupTable() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] {});
        assertNull(translator.lookupMap);
        assertNull(translator.shortest);
        assertNull(translator.longest);
    }

    /**
     * Test constructor with lookup table of size 1
     */
    @Test
    public void testLookupTableOfSizeOne() {
        CharSequence key = "test";
        CharSequence value = "replace";
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] { { key, value } });
        assertEquals(value, translator.lookupMap.get(key));
        assertEquals(key.length(), translator.shortest);
        assertEquals(key.length(), translator.longest);
    }

    /**
     * Test constructor with lookup table of size 2
     */
    @Test
    public void testLookupTableOfSizeTwo() {
        CharSequence key1 = "test1";
        CharSequence value1 = "replace1";
        CharSequence key2 = "test2";
        CharSequence value2 = "replace2";

        LookupTranslator translator = new LookupTranslator(new CharSequence[][] { { key1, value1 }, { key2, value2 } });

        assertEquals(value1, translator.lookupMap.get(key1));
        assertEquals(value2, translator.lookupMap.get(key2));
        assertEquals(key1.length(), translator.shortest);
        assertEquals(Math.max(key1.length(), key2.length()), translator.longest);
    }

    /**
     * Test {@link LookupTranslator#translate(CharSequence, int, Writer)} method
     * with empty lookup table
     */
    @Test
    public void testEmptyTranslation() throws Exception {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] {});

        StringWriter writer = new StringWriter();
        CharSequence input = CharSequenceUtils.EMPTY;
        int index = 0;

        int lengthTranslated = translator.translate(input, index, writer);
        assertEquals(0, lengthTranslated);
        assertEquals(Character.toString(''), writer.toString());
    }

    /**
     * Test {@link LookupTranslator#translate(CharSequence, int, Writer)} method
     * with lookup table of size 1
     */
    @Test
    public void testTranslationWithSingleEntry() throws Exception {
        CharSequence key = "test";
        CharSequence value = "replace";

        LookupTranslator translator = new LookupTranslator(new CharSequence[][] { { key, value } });

        StringWriter writer = new StringWriter();
        CharSequence input = "test";
        int index = 0;

        int lengthTranslated = translator.translate(input, index, writer);
        assertEquals(key.length(), lengthTranslated);
        assertEquals(value.toString(), writer.toString());
    }

    /**
     * Test {@link LookupTranslator#translate(CharSequence, int, Writer)} method
     * with lookup table of size 2
     */
    @Test
    public void testTranslationWithMultipleEntries() throws Exception {
        CharSequence key1 = "test1";
        CharSequence value1 = "replace1";
        CharSequence key2 = "test2";
        CharSequence value2 = "replace2";

        LookupTranslator translator = new LookupTranslator(new CharSequence[][] { { key1, value1 }, { key2, value2 } });

        StringWriter writer = new StringWriter();
        CharSequence input = "test2test1";
        int index = 0;

        int lengthTranslated = translator.translate(input, index, writer);
        assertEquals(key1.length() + key2.length(), lengthTranslated);
        assertEquals(value1.toString() + value2.toString(), writer.toString());
    }

    /**
     * Test {@link LookupTranslator#translate(CharSequence, int, Writer)} method
     * with input larger than the longest lookup entry
     */
    @Test
    public void testTranslationWithInputLargerThanLookupEntry() throws Exception {
        CharSequence key = "test";
        CharSequence value = "replace";

        LookupTranslator translator = new LookupTranslator(new CharSequence[][] { { key, value } });

        StringWriter writer = new StringWriter();
        CharSequence input = "testtesttest";
        int index = 0;

        int lengthTranslated = translator.translate(input, index, writer);
        assertEquals(value.length() * 3, lengthTranslated); // Three instances of 'value' should be translated
        assertEquals(value.toString() * 3, writer.toString());
    }

    /**
     * Test {@link LookupTranslator#translate(CharSequence, int, Writer)} method
     * with input smaller than the shortest lookup entry
     */
    @Test
    public void testTranslationWithInputSmallerThanLookupEntry() throws Exception {
        CharSequence key1 = "test1";
        CharSequence value1 = "replace1";
        CharSequence key2 = "test2";
        CharSequence value2 = "replace2";

        LookupTranslator translator = new LookupTranslator(new CharSequence[][] { { key1, value1 }, { key2, value2 } });

        StringWriter writer = new StringWriter();
        CharSequence input = "test";
        int index = 0;

        int lengthTranslated = translator.translate(input, index, writer);
        assertEquals(0, lengthTranslated);
        assertEquals(Character.toString(''), writer.toString());
    }
}