package org.apache.commons.lang3.text.translate;

import java.io.*;
import java.util.*;
import org.junit.Assert.*;
import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testIsSet() {
        // Expert 1's test case
    }

    @Test
    public void testTranslateSimple() throws IOException {
        // Expert 1's test case
    }

    @Test
    public void testTranslateHex() throws IOException {
        // Expert 1's test case
    }

    @Test
    public void testTranslateWithoutSemiColon() throws IOException {
        // Expert 1's test case
    }

    @Test
    public void testTranslateWithSemiColon() throws IOException {
        // Expert 1's test case
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithMissingSemiColon() throws IOException {
        // Expert 1's test case
    }

    // Expert 2 and Expert 3's test cases will be added here

    @Test
    public void testTranslateEmptyInput() throws IOException {
        Writer out = new StringWriter();
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        CharSequence input = new StringBuffer("");
        unescaper.translate(input, 0, out);
        assertEquals("", out.toString());
    }

    @Test
    public void testTranslateWithMultipleEntities() throws IOException {
        Writer out = new StringWriter();
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        CharSequence input = new StringBuffer("This text contains &amp;#x1F600; &amp;#x1F601;");
        unescaper.translate(input, 0, out);
        assertEquals("This text contains &#x1F600;\nThis text contains &#x1F601;\n", out.toString());
    }

    @Test
    public void testTranslateWithInvalidEntity() throws IOException {
        Writer out = new StringWriter();
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        CharSequence input = new StringBuffer("This text contains &amp;# invalid; entity");
        unescaper.translate(input, 0, out);
        assertEquals("This text contains &amp;# invalid;\n", out.toString());
    }

    @Test
    public void testTranslateWithLargeEntityValue() throws IOException {
        Writer out = new StringWriter();
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        CharSequence input = new StringBuffer("This text contains &amp;#1114111; entity");
        unescaper.translate(input, 0, out);
        assertEquals("This text contains \uD83D\uDD1E\uD83D\uDD1F\uD83D\uDD20\uD83D\uDD21\uD83D\uDD22\uD83D\uDD23\uD83D\uDD24\uD83D\uDD25\uD83D\uDD26\uD83D\uDD27\uD83D\uDD28\uD83D\uDD29;\n", out.toString());
    }
}