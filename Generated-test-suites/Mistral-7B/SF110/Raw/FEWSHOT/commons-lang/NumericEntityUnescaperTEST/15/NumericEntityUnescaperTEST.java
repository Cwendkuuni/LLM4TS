package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.StringWriter;
import java.util.Arrays;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testNewInstanceWithEmptyArray() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(new OPTION[0]);
        assertTrue(unescaper.isSet(OPTION.semiColonRequired));
    }

    @Test
    public void testNewInstanceWithCustomOptions() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional, OPTION.errorIfNoSemiColon);
        assertFalse(unescaper.isSet(OPTION.semiColonRequired));
        assertTrue(unescaper.isSet(OPTION.semiColonOptional));
        assertTrue(unescaper.isSet(OPTION.errorIfNoSemiColon));
    }

    @Test
    public void testIsSetWithUnsupportedOption() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(new OPTION[0]);
        assertFalse(unescaper.isSet(OPTION.semiColonOptional));
    }

    @Test
    public void testTranslateWithValidNumericEntity() throws Exception {
        String input = "&amp;#48;&#x31;#35;";
        StringWriter out = new StringWriter();
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(new OPTION[0]);
        int translatedChars = unescaper.translate(new StringReader(input), 0, out);
        assertEquals(5, translatedChars);
        assertEquals("2.5", out.toString());
    }

    @Test
    public void testTranslateWithInvalidNumericEntityStart() throws Exception {
        String input = "&amp;#";
        StringWriter out = new StringWriter();
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(new OPTION[0]);
        int translatedChars = unescaper.translate(new StringReader(input), 0, out);
        assertEquals(0, translatedChars);
        assertEquals("", out.toString());
    }

    @Test
    public void testTranslateWithInvalidNumericEntityEnd1() throws Exception {
        String input = "&amp;#123;";
        StringWriter out = new StringWriter();
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(new OPTION[0]);
        int translatedChars = unescaper.translate(new StringReader(input), 0, out);
        assertEquals(0, translatedChars);
        assertEquals("", out.toString());
    }

    @Test
    public void testTranslateWithInvalidNumericEntityEnd2() throws Exception {
        String input = "&amp;#123";
        StringWriter out = new StringWriter();
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(new OPTION[0]);
        int translatedChars = unescaper.translate(new StringReader(input), 0, out);
        assertEquals(0, translatedChars);
        assertEquals("", out.toString());
    }

    @Test
    public void testTranslateWithInvalidNumericEntityNoSemiColon() throws Exception {
        String input = "&amp;#123";
        StringWriter out = new StringWriter();
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
        int translatedChars = unescaper.translate(new StringReader(input), 0, out);
        assertEquals(0, translatedChars);
        assertEquals("", out.toString());
    }

    @Test
    public void testTranslateWithValidHexNumericEntity() throws Exception {
        String input = "&amp;#x31&amp;#x35;";
        StringWriter out = new StringWriter();
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(new OPTION[0]);
        int translatedChars = unescaper.translate(new StringReader(input), 0, out);
        assertEquals(5, translatedChars);
        assertEquals("2.5", out.toString());
    }

    @Test
    public void testTranslateWithInvalidHexNumericEntityStart() throws Exception {
        String input = "&amp;#x";
        StringWriter out = new StringWriter();
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(new OPTION[0]);
        int translatedChars = unescaper.translate(new StringReader(input), 0, out);
        assertEquals(0, translatedChars);
        assertEquals("", out.toString());
    }

    @Test
    public void testTranslateWithInvalidHexNumericEntityMiddle() throws Exception {
        String input = "&amp;#x31&amp;#a";
        StringWriter out = new StringWriter();
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(new OPTION[0]);
        int translatedChars = unescaper.translate(new StringReader(input), 0, out);
        assertEquals(0, translatedChars);
        assertEquals("", out.toString());
    }

    @Test
    public void testTranslateWithValidNumericEntityWithSemiColon() throws Exception {
        String input = "&amp;#31;";
        StringWriter out = new StringWriter();
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(new OPTION[0]);
        int translatedChars = unescaper.translate(new StringReader(input), 0, out);
        assertEquals(2, translatedChars);
        assertEquals("1;", out.toString());
    }

    @Test
    public void testTranslateWithValidNumericEntityWithSemiColonAndCustomOptions() throws Exception {
        String input = "&amp;#31;";
        StringWriter out = new StringWriter();
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional, OPTION.errorIfNoSemiColon);
        int translatedChars = unescaper.translate(new StringReader(input), 0, out);
        assertEquals(1, translatedChars);
        assertEquals("1", out.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithInvalidNumericEntityWithErrorIfNoSemiColonOption() throws Exception {
        String input = "&amp;#31;";
        StringWriter out = new StringWriter();
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.errorIfNoSemiColon);
        int translatedChars = unescaper.translate(new StringReader(input), 0, out);
        fail("Expected IllegalArgumentException, but got 0 translated characters and an empty output string");
    }

    @Test
    public void testTranslateWithLargeValue() throws Exception {
        String input = "&amp;#65536;";
        StringWriter out = new StringWriter();
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(new OPTION[0]);
        int translatedChars = unescaper.translate(new StringReader(input), 0, out);
        assertEquals(3, translatedChars);
        assertEquals("\u00fd\u00fd", out.toString());
    }

    @Test
    public void testTranslateWithLargeValueWithCustomOptions() throws Exception {
        String input = "&amp;#65536;";
        StringWriter out = new StringWriter();
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
        int translatedChars = unescaper.translate(new StringReader(input), 0, out);
        assertEquals(4, translatedChars);
        assertEquals("\u00fd\u00fd;", out.toString());
    }

}