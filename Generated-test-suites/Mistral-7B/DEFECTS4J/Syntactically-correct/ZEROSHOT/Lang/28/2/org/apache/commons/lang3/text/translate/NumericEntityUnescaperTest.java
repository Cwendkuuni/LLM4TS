package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.test.util.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;

import java.io.StringWriter;
import java.util.Arrays;

/**
 * Test class for NumericEntityUnescaper
 */
public class NumericEntityUnescaperTest {

    private NumericEntityUnescaper unescaper;
    private StringWriter writer;

    @Before
    public void setUp() {
        unescaper = new NumericEntityUnescaper();
        writer = new StringWriter();
    }

    @Test
    public void testTranslateSimple() {
        CharSequence input = "&amp; &lt; &gt; &apos; &quot; &#32; &#13; &#38; &#x26; &#xa;";
        int expectedLength = 13;
        int actualLength = unescaper.translate(input, 0, writer);
        String actualResult = writer.getBuffer().toString();
        assertEquals(expectedLength, actualLength);
        assertEquals("abc\r\n&amp;\r\n", actualResult);
    }

    @Test
    public void testTranslateSimpleHex() {
        CharSequence input = "&amp; #x26; &#x61; #x63; #x63; #x65;";
        int expectedLength = 6;
        int actualLength = unescaper.translate(input, 0, writer);
        String actualResult = writer.getBuffer().toString();
        assertEquals(expectedLength, actualLength);
        assertEquals("abcdef", actualResult);
    }

    @Test
    public void testTranslateMultipleEntities() {
        CharSequence input = "&amp; #10; &#x26; &apos; &quot; &#x3C3; &#x2018; &#x2019;";
        int expectedLength = 10;
        int actualLength = unescaper.translate(input, 0, writer);
        String actualResult = writer.getBuffer().toString();
        assertEquals(expectedLength, actualLength);
        assertEquals(" \r\n&amp;\r\n\u03C3\u2018\u2019", actualResult);
    }

    @Test
    public void testTranslateEmpty() {
        CharSequence input = "";
        int expectedLength = 0;
        int actualLength = unescaper.translate(input, 0, writer);
        assertEquals(expectedLength, actualLength);
        assertEquals("", writer.getBuffer().toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateNullInput() {
        unescaper.translate(null, 0, writer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateNullWriter() {
        unescaper.translate(CharSequenceUtils.quotedPrintableDecode("&amp;"), 0, null);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testTranslateInvalidEntity() {
        CharSequence input = "&invalidEntity;";
        unescaper.translate(input, 0, writer);
    }

    @Test
    public void testPerformance() {
        String input = StringUtils.repeat("&amp; #x26; ", 10000);
        long startTime = System.currentTimeMillis();
        unescaper.translate(CharSequenceUtils.create(input), 0, writer);
        long endTime = System.currentTimeMillis();
        System.out.println("Performance test result: " + (endTime - startTime) + "ms");
    }
}