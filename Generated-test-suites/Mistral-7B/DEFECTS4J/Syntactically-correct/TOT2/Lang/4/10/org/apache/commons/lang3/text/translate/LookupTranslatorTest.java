package org.junit.test;

import org.apache.commons.lang3.text.translate.LookupTranslator;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;

public class LookupTranslatorTest {

    @Test
    public void testEmptyLookupTable() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{}});
        StringWriter out = new StringWriter();
        CharSequence input = "apple";
        int index = 0;
        assertFalse(translator.translate(input, index, out) > 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidLookupTable() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{null}, {}});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDuplicateKeysInLookupTable() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"apple", "apple"}, {"banana", "banana"}, {"apple", "orange"}});
    }

    @Test(expected = NullPointerException.class)
    public void testInputCharSequenceIsNull() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"apple", "apple"}, {"banana", "banana"}, {"orange", "orange"}});
        StringWriter out = new StringWriter();
        CharSequence input = null;
        int index = 0;
        translator.translate(input, index, out);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testInputCharSequenceIndexOutOfBounds() {
        CharSequence[][] lookup = new CharSequence[][]{{"apple", "apple"}, {"banana", "banana"}, {"orange", "orange"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter out = new StringWriter();
        CharSequence input = "bananana";
        int index = 6;
        translator.translate(input, index, out);
    }

    @Test(expected = NullPointerException.class)
    public void testWriterIsNull() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"apple", "apple"}, {"banana", "banana"}, {"orange", "orange"}});
        CharSequence input = "banana";
        int index = 0;
        Writer out = null;
        translator.translate(input, index, out);
    }

    @Test
    public void testInputCharSequenceContainsCharactersNotInLookupTable() {
        CharSequence[][] lookup = new CharSequence[][]{{"apple", "apple"}, {"banana", "banana"}, {"orange", "orange"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter out = new StringWriter();
        CharSequence input = "appleorange";
        int index = 0;
        int translatedLength = translator.translate(input, index, out);
        assertEquals(0, translatedLength);
    }

    @Test
    public void testInputCharSequenceContainsNonPrintableCharacters() {
        CharSequence[][] lookup = new CharSequence[][]{{"apple", "apple"}, {"banana", "banana"}, {"orange", "orange"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter out = new StringWriter();
        CharSequence input = "\u0001banana";
        int index = 0;
        int translatedLength = translator.translate(input, index, out);
        assertEquals(0, translatedLength);
    }

    @Test
    public void testInputCharSequenceContainsAMixOfPrintableAndNonPrintableCharacters() {
        CharSequence[][] lookup = new CharSequence[][]{{"apple", "apple"}, {"banana", "banana"}, {"orange", "orange"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter out = new StringWriter();
        CharSequence input = "apple\u0001banana";
        int index = 0;
        int translatedLength = translator.translate(input, index, out);
        assertEquals(5, translatedLength);
    }

    @Test
    public void testInputCharSequenceLengthLessThanShortestLookupValue() {
        CharSequence[][] lookup = new CharSequence[][]{{"apple", "apple"}, {"banana", "banana"}, {"orange", "orange"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter out = new StringWriter();
        CharSequence input = "ap";
        int index = 0;
        int translatedLength = translator.translate(input, index, out);
        assertEquals(0, translatedLength);
    }

    @Test
    public void testInputCharSequenceLengthGreaterThanLongestLookupValue() {
        CharSequence[][] lookup = new CharSequence[][]{{"apple", "apple"}, {"banana", "banana"}, {"orange", "orange"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter out = new StringWriter();
        CharSequence input = "applegreenapple";
        int index = 0;
        int translatedLength = translator.translate(input, index, out);
        assertEquals("applegreenapple", out.toString());
        assertEquals(15, translatedLength);
    }

    @Test
    public void testInputCharSequenceContainsLeadingOrTrailingWhitespaces() {
        CharSequence[][] lookup = new CharSequence[][]{{"apple", "apple"}, {"banana", "banana"}, {"orange", "orange"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter out = new StringWriter();
        CharSequence input = "   applebanana   ";
        int index = 0;
        int translatedLength = translator.translate(input, index, out);
        assertEquals("applebanana", out.toString());
        assertEquals(11, translatedLength);
    }

    @Test
    public void testTranslateMethod() throws IOException {
        CharSequence[][] lookup = new CharSequence[][]{{"apple", "apple"}, {"banana", "banana"}, {"orange", "orange"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter out = new StringWriter();
        CharSequence input = "banana";
        int index = 0;
        String expectedOutput = "banana";
        int translatedLength = translator.translate(input, index, out);
        assertEquals(expectedOutput, out.toString());
        assertEquals(5, translatedLength);
    }
}