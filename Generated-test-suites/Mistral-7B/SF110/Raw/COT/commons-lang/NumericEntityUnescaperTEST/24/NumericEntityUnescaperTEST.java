package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.EnumSet;

import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper.OPTION;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testConstructorWithOptions() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired, OPTION.errorIfNoSemiColon);
        assertTrue(unescaper.isSet(OPTION.semiColonRequired));
        assertTrue(unescaper.isSet(OPTION.errorIfNoSemiColon));

        NumericEntityUnescaper unescaperWithoutOptions = new NumericEntityUnescaper();
        assertFalse(unescaperWithoutOptions.isSet(OPTION.semiColonRequired));
        assertFalse(unescaperWithoutOptions.isSet(OPTION.errorIfNoSemiColon));
    }

    @Test
    public void testIsSet() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired, OPTION.errorIfNoSemiColon);
        assertTrue(unescaper.isSet(OPTION.semiColonRequired));
        assertTrue(unescaper.isSet(OPTION.errorIfNoSemiColon));

        NumericEntityUnescaper unescaperWithoutOptions = new NumericEntityUnescaper();
        assertFalse(unescaperWithoutOptions.isSet(OPTION.semiColonRequired));
        assertFalse(unescaperWithoutOptions.isSet(OPTION.errorIfNoSemiColon));
    }

    @Test
    public void testTranslateWithValidNumericEntity() throws Exception {
        CharSequence input = "This text contains a valid numeric entity: &amp;#160;";
        StringWriter output = new StringWriter();
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        int translatedChars = unescaper.translate(input, 0, output);
        assertEquals("This text contains a valid numeric entity: \u00A0", output.toString());
        assertEquals(1, translatedChars);
    }

    @Test
    public void testTranslateWithValidHexNumericEntity() throws Exception {
        CharSequence input = "This text contains a valid hex numeric entity: &amp;#x1F44D;";
        StringWriter output = new StringWriter();
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        int translatedChars = unescaper.translate(input, 0, output);
        assertEquals("This text contains a valid hex numeric entity: 💩", output.toString());
        assertEquals(3, translatedChars);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithMissingSemiColon() throws Exception {
        CharSequence input = "This text contains a missing semi-colon: &amp;#160;";
        StringWriter output = new StringWriter();
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);

        int translatedChars = unescaper.translate(input, 0, output);
        fail("Expected an IllegalArgumentException but didn't get one.");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithInvalidNumericEntity() throws Exception {
        CharSequence input = "This text contains an invalid numeric entity: &amp;#abc;";
        StringWriter output = new StringWriter();
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        int translatedChars = unescaper.translate(input, 0, output);
        assertEquals(0, translatedChars);
        assertTrue(output.toString().isEmpty());
    }

    @Test
    public void testTranslateWithEmptyInput() throws Exception {
        CharSequence input = "";
        StringWriter output = new StringWriter();
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        int translatedChars = unescaper.translate(input, 0, output);
        assertEquals(0, translatedChars);
        assertTrue(output.toString().isEmpty());
    }

    @Test
    public void testTranslateWithInputStartingWithNumericEntity() throws Exception {
        CharSequence input = "&amp;#160;This text";
        StringWriter output = new StringWriter();
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        int translatedChars = unescaper.translate(input, 0, output);
        assertEquals("This text", output.toString());
        assertEquals(6, translatedChars);
    }
}