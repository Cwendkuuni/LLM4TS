package org.junit.tests.NumericEntityUnescaper;

import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper.OPTION;
import org.junit.Test;
import org.junit.Assert.*;
import java.io.*;
import static org.junit.Assert.assertEquals;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
        String testString = "&amp;#x1F601; &amp;#128575; &amp;#10066; ";
        StringWriter out = new StringWriter();
        CharSequence input = new StringReader(testString);
        unescaper.translate(input, 0, out);
        String expected = "\uD83D\uDE01 \uD83D\uDECB \uD83C\uDF21 ";
        assertEquals(expected, out.toString());
    }

    // Test case for empty input
    @Test
    public void testTranslateEmptyInput() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
        String testString = "";
        StringWriter out = new StringWriter();
        CharSequence input = new StringReader(testString);
        unescaper.translate(input, 0, out);
        assertEquals("", out.toString());
    }

    // Test case for input with multiple numeric entities
    @Test
    public void testTranslateMultipleEntities() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
        String testString = "&amp;#x1F601;&amp;#128575;&amp;#10066; &amp;#x1F602;&amp;#128576;&amp;#10067; ";
        StringWriter out = new StringWriter();
        CharSequence input = new StringReader(testString);
        unescaper.translate(input, 0, out);
        String expected = "\uD83D\uDE01\uD83D\uDECB \uD83D\uDE02\uD83D\uDECB";
        assertEquals(expected, out.toString());
    }

    // Test case for input with invalid numeric entity
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateInvalidEntity() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
        String testString = "&amp;#x1F601; &amp;#abcd; &amp;#10066; ";
        StringWriter out = new StringWriter();
        CharSequence input = new StringReader(testString);
        unescaper.translate(input, 0, out);
    }

    // Test case for input with invalid options
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateInvalidOptions() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.ABC);
        String testString = "&amp;#x1F601;";
        StringWriter out = new StringWriter();
        CharSequence input = new StringReader(testString);
        unescaper.translate(input, 0, out);
    }

    // Test case for input with no semicolon at the end
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateNoSemiColon() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
        String testString = "&amp;#x1F601; &amp;#128575; &amp;#10066;";
        StringWriter out = new StringWriter();
        CharSequence input = new StringReader(testString);
        unescaper.translate(input, 0, out);
    }

    // Test case for input with an extra semicolon at the end
    @Test
    public void testTranslateExtraSemiColon() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
        String testString = "&amp;#x1F601; &amp;#128575; &amp;#10066; &amp;";
        StringWriter out = new StringWriter();
        CharSequence input = new StringReader(testString);
        unescaper.translate(input, 0, out);
        assertEquals("", out.toString());
    }
}