package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;

import java.io.*;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper.OPTION;
import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testConstructorWithOptions() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired, OPTION.errorIfNoSemiColon);
        assertTrue(unescaper.isSet(OPTION.semiColonRequired));
        assertTrue(unescaper.isSet(OPTION.errorIfNoSemiColon));
    }

    @Test
    public void testConstructorWithoutOptions() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        assertFalse(unescaper.isSet(OPTION.semiColonRequired));
        assertTrue(unescaper.isSet(OPTION.semiColonRequired));
    }

    @Test
    public void testIsSet() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired, OPTION.errorIfNoSemiColon);
        assertTrue(unescaper.isSet(OPTION.semiColonRequired));
        assertTrue(unescaper.isSet(OPTION.errorIfNoSemiColon));
        assertFalse(unescaper.isSet(OPTION.semiColonOptional));
    }

    @Test
    public void testTranslate_ValidNumericEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder output = new StringBuilder();
        String input = "&amp;#123;";
        int length = unescaper.translate(new StringReader(input), 0, new StringWriter(output));
        assertEquals(4, length);
        assertEquals("\u007B", output.toString());
    }

    @Test
    public void testTranslate_ValidNumericEntityWithSemiColon() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
        StringBuilder output = new StringBuilder();
        String input = "&amp;#123;\n";
        int length = unescaper.translate(new StringReader(input), 0, new StringWriter(output));
        assertEquals(5, length);
        assertEquals("\u007B\n", output.toString());
    }

    @Test
    public void testTranslate_InvalidNumericEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder output = new StringBuilder();
        String input = "&amp;#abc";
        int length = unescaper.translate(new StringReader(input), 0, new StringWriter(output));
        assertEquals(0, length);
        assertEquals("", output.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslate_InvalidNumericEntityMissingSemiColon() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.errorIfNoSemiColon);
        StringBuilder output = new StringBuilder();
        String input = "&amp;#abc";
        unescaper.translate(new StringReader(input), 0, new StringWriter(output));
    }

    @Test
    public void testTranslate_HexNumericEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder output = new StringBuilder();
        String input = "&amp;#x4141;";
        int length = unescaper.translate(new StringReader(input), 0, new StringWriter(output));
        assertEquals(5, length);
        assertEquals("\u00AA\u00AA", output.toString());
    }

    @Test
    public void testTranslate_LargeNumericEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder output = new StringBuilder();
        String input = "&amp;#65536;";
        int length = unescaper.translate(new StringReader(input), 0, new StringWriter(output));
        assertEquals(3, length);
        char[] expectedChars = Character.toChars(0x10000);
        assertArrayEquals(expectedChars, output.toString().toCharArray());
    }

    @Test
    public void testTranslate_LargeHexNumericEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder output = new StringBuilder();
        String input = "&amp;#x10FFFF;";
        int length = unescaper.translate(new StringReader(input), 0, new StringWriter(output));
        assertEquals(6, length);
        char[] expectedChars = Character.toChars(0x10FFFF);
        assertArrayEquals(expectedChars, output.toString().toCharArray());
    }

    @Test(expected = NumberFormatException.class)
    public void testTranslate_InvalidHexNumericEntity() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder output = new StringBuilder();
        String input = "&amp;#xABC";
        unescaper.translate(new StringReader(input), 0, new StringWriter(output));
    }
}