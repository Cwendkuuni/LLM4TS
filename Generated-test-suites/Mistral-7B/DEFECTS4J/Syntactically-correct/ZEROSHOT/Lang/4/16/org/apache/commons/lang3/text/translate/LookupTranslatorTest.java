package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.StringWriter;
import java.util.HashMap;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.text.translate.LookupTranslator;

import org.junit.Test;

/**
 * Tests for {@link LookupTranslator} class
 */
public class LookupTranslatorTest {

    /**
     * Tests constructor
     */
    @Test
    public void testConstructor() {
        CharSequence[][] lookup = new CharSequence[][] { { CharSequenceUtils.quotedPrintable("abc:def"),
                CharSequenceUtils.quotedPrintable("xyz") }, { CharSequenceUtils.quotedPrintable("ABC:DEF"),
                CharSequenceUtils.quotedPrintable("XYZ") } };
        LookupTranslator translator = new LookupTranslator(lookup);
        assertEquals(2, translator.getLookupMap().size());
        assertEquals(6, translator.getShortest());
        assertEquals(6, translator.getLongest());
    }

    /**
     * Tests {@link LookupTranslator#translate(CharSequence, int, Writer)} method with a valid lookup table
     */
    @Test
    public void testTranslateWithValidLookupTable() throws Exception {
        CharSequence[][] lookup = new CharSequence[][] { { CharSequenceUtils.quotedPrintable("abc:def"),
                CharSequenceUtils.quotedPrintable("xyz") }, { CharSequenceUtils.quotedPrintable("ABC:DEF"),
                CharSequenceUtils.quotedPrintable("XYZ") } };
        LookupTranslator translator = new LookupTranslator(lookup);

        StringWriter writer = new StringWriter();
        int index = 0;
        String input = "abc:defABC:DEF";

        translator.translate(CharSequenceUtils.quotedPrintable(input), index, writer);
        assertEquals("xyzXYZ", writer.toString());
    }

    /**
     * Tests {@link LookupTranslator#translate(CharSequence, int, Writer)} method with an empty lookup table
     */
    @Test
    public void testTranslateWithEmptyLookupTable() throws Exception {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] {});

        StringWriter writer = new StringWriter();
        int index = 0;
        String input = "abc:defABC:DEF";

        translator.translate(CharSequenceUtils.quotedPrintable(input), index, writer);
        assertNull(writer.toString());
    }

    /**
     * Tests {@link LookupTranslator#translate(CharSequence, int, Writer)} method with an invalid lookup index
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testTranslateWithInvalidLookupIndex() throws Exception {
        CharSequence[][] lookup = new CharSequence[][] { { CharSequenceUtils.quotedPrintable("abc:def"),
                CharSequenceUtils.quotedPrintable("xyz") }, { CharSequenceUtils.quotedPrintable("ABC:DEF"),
                CharSequenceUtils.quotedPrintable("XYZ") } };
        LookupTranslator translator = new LookupTranslator(lookup);

        StringWriter writer = new StringWriter();
        int index = 15;
        String input = "abc:defABC:DEF";

        translator.translate(CharSequenceUtils.quotedPrintable(input), index, writer);
    }

    /**
     * Tests {@link LookupTranslator#translate(CharSequence, int, Writer)} method with an empty input sequence
     */
    @Test
    public void testTranslateWithEmptyInputSequence() throws Exception {
        CharSequence[][] lookup = new CharSequence[][] { { CharSequenceUtils.quotedPrintable("abc:def"),
                CharSequenceUtils.quotedPrintable("xyz") }, { CharSequenceUtils.quotedPrintable("ABC:DEF"),
                CharSequenceUtils.quotedPrintable("XYZ") } };
        LookupTranslator translator = new LookupTranslator(lookup);

        StringWriter writer = new StringWriter();
        int index = 0;
        String input = "";

        translator.translate(CharSequenceUtils.quotedPrintable(input), index, writer);
        assertNull(writer.toString());
    }
}