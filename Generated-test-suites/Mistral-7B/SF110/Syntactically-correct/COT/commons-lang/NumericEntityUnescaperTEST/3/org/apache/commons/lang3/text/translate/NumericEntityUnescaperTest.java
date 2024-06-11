package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;

import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper.OPTION;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.EnumSet;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testNumericEntityUnescaperConstructorWithOptions() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired, OPTION.errorIfNoSemiColon);
        assertTrue(unescaper.isSet(OPTION.semiColonRequired));
        assertTrue(unescaper.isSet(OPTION.errorIfNoSemiColon));
    }

    @Test
    public void testNumericEntityUnescaperConstructorWithoutOptions() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        assertFalse(unescaper.isSet(OPTION.semiColonRequired));
        assertFalse(unescaper.isSet(OPTION.errorIfNoSemiColon));
    }

    @Test
    public void testIsSet() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired, OPTION.errorIfNoSemiColon);
        assertTrue(unescaper.isSet(OPTION.semiColonRequired));
        assertTrue(unescaper.isSet(OPTION.errorIfNoSemiColon));

        unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
        assertTrue(unescaper.isSet(OPTION.semiColonOptional));
        assertFalse(unescaper.isSet(OPTION.semiColonRequired));
        assertFalse(unescaper.isSet(OPTION.errorIfNoSemiColon));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithSemiColonRequiredAndMissingSemiColon() throws IOException {
        CharSequence input = "This text contains a numeric entity &amp;#123; without a semi-colon at the end.";
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
        StringWriter out = new StringWriter();
        int translatedLength = unescaper.translate(input, 0, out);
        fail("Expected IllegalArgumentException but no exception was thrown.");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithErrorIfNoSemiColonAndMissingSemiColon() throws IOException {
        CharSequence input = "This text contains a numeric entity &amp;#123; without a semi-colon at the end.";
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.errorIfNoSemiColon);
        StringWriter out = new StringWriter();
        int translatedLength = unescaper.translate(input, 0, out);
        fail("Expected IllegalArgumentException but no exception was thrown.");
    }

    @Test
    public void testTranslateWithValidNumericEntity() throws IOException {
        CharSequence input = "This text contains a numeric entity &amp;#1234;";
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();
        int translatedLength = unescaper.translate(input, 0, out);
        assertEquals("This text contains a numeric entity ", input.subSequence(0, input.length() - 6).toString(), out.toString());
        assertEquals(6, translatedLength);
    }

    @Test
    public void testTranslateWithValidNumericEntityHex() throws IOException {
        CharSequence input = "This text contains a numeric entity &amp;#x1F4A9;";
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();
        int translatedLength = unescaper.translate(input, 0, out);
        assertEquals("This text contains a numeric entity 𙿓", out.toString(), input.subSequence(0, input.length() - 6).toString());
        assertEquals(7, translatedLength);
    }

    @Test
    public void testTranslateWithMissingEntity() throws IOException {
        CharSequence input = "This text contains a missing entity &amp;#9999;";
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter out = new StringWriter();
        int translatedLength = unescaper.translate(input, 0, out);
        assertEquals("This text contains a missing entity ", input.subSequence(0, input.length() - 7).toString(), out.toString());
        assertEquals(0, translatedLength);
    }

    @Test
    public void testTranslateWithSemiColonRequired() throws IOException {
        CharSequence input = "This text contains a numeric entity &amp;#1234;";
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
        StringWriter out = new StringWriter();
        int translatedLength = unescaper.translate(input, 0, out);
        assertEquals("This text contains a numeric entity ", input.subSequence(0, input.length() - 6).toString(), out.toString());
        assertEquals(6, translatedLength);
        assertEquals(';', out.toString().charAt(out.toString().length() - 1));
    }

    @Test
    public void testTranslateWithSemiColonOptional() throws IOException {
        Charsequence input = "This text contains a numeric entity &amp;#1234;";
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
        StringWriter out = new StringWriter();
        int translatedLength = unescaper.translate(input, 0, out);
        assertEquals("This text contains a numeric entity ", input.subSequence(0, input.length() - 6).toString(), out.toString());
        assertEquals(6, translatedLength);
    }
}