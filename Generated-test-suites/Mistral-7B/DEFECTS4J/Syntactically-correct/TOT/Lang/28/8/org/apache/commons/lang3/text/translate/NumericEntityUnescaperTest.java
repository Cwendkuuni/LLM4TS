package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for NumericEntityUnescaper.
 */
public class NumericEntityUnescaperTest {

    private final NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

    @Test
    public void testTranslateAsciiEntity() throws IOException {
        String input = "&amp;";
        Writer output = new StringBuilder();
        int length = unescaper.translate(new String(input), 0, output);
        assertEquals("&amp;", input);
        assertEquals("&amp;", output.toString());
        assertEquals(1, length);
    }

    @Test
    public void testTranslateHexEntity() throws IOException {
        String input = "&#[x32];";
        Writer output = new StringBuilder();
        int length = unescaper.translate(new String(input), 0, output);
        assertEquals("&#[x32];", input);
        assertEquals("AB", output.toString());
        assertEquals(2, length);
    }

    @Test
    public void testTranslateInvalidEntity() throws IOException {
        String input = "&asd";
        Writer output = new StringBuilder();
        int length = unescaper.translate(new String(input), 0, output);
        assertEquals("&asd", input);
        assertEquals("", output.toString());
        assertEquals(0, length);
    }

    @Test(expected = IOException.class)
    public void testTranslateMalformedEntity() throws IOException {
        String input = "&asd#";
        Writer output = new StringBuilder();
        unescaper.translate(new String(input), 0, output);
    }

    @Test
    public void testTranslateEmptyInput() throws IOException {
        String input = "";
        Writer output = new StringBuilder();
        int length = unescaper.translate(new String(input), 0, output);
        assertEquals("", input);
        assertEquals("", output.toString());
        assertEquals(0, length);
    }

    @Test
    public void testTranslateLongEntity() throws IOException {
        String input = "&#[x3F80];";
        Writer output = new StringBuilder();
        int length = unescaper.translate(new String(input), 0, output);
        assertEquals("&#[x3F80];", input);
        assertEquals("\uD840\uDC00", output.toString());
        assertEquals(2, length);
    }

    @Test
    public void testTranslateEntityInsideEntity() throws IOException {
        String input = "&amp;#x41;";
        Writer output = new StringBuilder();
        int length = unescaper.translate(new String(input), 0, output);
        assertEquals("&amp;#x41;", input);
        assertEquals("&amp;A", output.toString());
        assertEquals(3, length);
    }

    @Test
    public void testTranslateEntityWithWhitespace() throws IOException {
        String input = "&#[x20];";
        Writer output = new StringBuilder();
        int length = unescaper.translate(new String(input), 0, output);
        assertEquals("&#[x20];", input);
        assertEquals(" ", output.toString());
        assertEquals(1, length);
    }

    @Test
    public void testTranslateEntityWithTrailingSemicolon() throws IOException {
        String input = "&#[x32];;";
        Writer output = new StringBuilder();
        int length = unescaper.translate(new String(input), 0, output);
        assertEquals("&#[x32];;", input);
        assertEquals("AB", output.toString());
        assertEquals(2, length);
    }
}