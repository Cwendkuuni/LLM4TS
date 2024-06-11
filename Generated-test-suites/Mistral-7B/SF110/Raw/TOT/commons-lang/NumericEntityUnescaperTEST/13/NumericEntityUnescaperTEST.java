package org.apache.commons.lang3.text.translate;

import java.io.*;
import java.util.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class NumericEntityUnescaperTest {
    @Test
    public void testIsSet() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired, OPTION.errorIfNoSemiColon);
        assertTrue(unescaper.isSet(OPTION.semiColonRequired));
        assertTrue(unescaper.isSet(OPTION.errorIfNoSemiColon));
    }

    @Test
    public void testTranslateWithOptionalSemicolon() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonOptional);
        String input = "&amp;#x1F602;";
        StringBuilder output = new StringBuilder();
        unescaper.translate(new StringReader(input), 0, output);
        assertEquals("😜", output.toString());
    }

    @Test
    public void testTranslateWithMissingSemicolon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
        String input = "&amp;#x1F602";
        assertThrows(IllegalArgumentException.class, () -> unescaper.translate(new StringReader(input), 0, new StringWriter()));
    }

    @Test
    public void testTranslateWithErrorIfNoSemicolon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.errorIfNoSemiColon);
        String input = "&amp;#x1F602";
        assertThrows(IllegalArgumentException.class, () -> unescaper.translate(new StringReader(input), 0, new StringWriter()));
    }

    @Test
    public void testTranslateWithValidInput() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&amp;#x1F602;";
        StringBuilder output = new StringBuilder();
        unescaper.translate(new StringReader(input), 0, output);
        assertEquals("😜", output.toString());
    }

    @Test
    public void testTranslateWithInvalidHexInput() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&amp;#xZZZ";
        StringBuilder output = new StringBuilder();
        unescaper.translate(new StringReader(input), 0, output);
        assertEquals("", output.toString());
    }

    @Test
    public void testTranslateWithDecimalInput() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&amp;#1234;";
        StringBuilder output = new StringBuilder();
        unescaper.translate(new StringReader(input), 0, output);
        assertEquals("1234", output.toString());
    }

    @Test
    public void testTranslateWithHexInputLongerThanTwoChars() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&amp;#x123456;";
        StringBuilder output = new StringBuilder();
        unescaper.translate(new StringReader(input), 0, output);
        assertEquals("", output.toString());
    }

    @Test
    public void testTranslateWithInvalidInput() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&amp;#";
        StringBuilder output = new StringBuilder();
        unescaper.translate(new StringReader(input), 0, output);
        assertEquals("", output.toString());
    }
}