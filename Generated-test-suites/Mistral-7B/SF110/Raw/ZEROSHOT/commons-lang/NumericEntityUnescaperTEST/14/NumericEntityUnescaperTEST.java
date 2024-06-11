package org.apache.commons.lang3.text.translate;

import static org.apache.commons.lang3.text.translate.NumericEntityUnescaper.OPTION.*;
import static org.junit.Assert.*;

import java.io.StringWriter;
import java.util.EnumSet;

import org.apache.commons.lang3.text.translate.NumericEntityUnscaper;
import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslateWithDefaultOptions() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        CharSequence input = "&amp;this&lamda;&#x26;#123; &#x61;#66; &#xFF;&#xFF;";
        StringWriter out = new StringWriter();
        int index = 0;

        while (index < input.length()) {
            int length = unescaper.translate(input, index, out);
            index += length;
        }

        String expectedOutput = "this&lambda&amp;&#123;&a;&f;&FF&FF";
        assertEquals(expectedOutput, out.toString());
    }

    @Test
    public void testTranslateWithSemiColonRequired() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
        CharSequence input = "&amp;this&lamda;&#x26;#123; ";
        StringWriter out = new StringWriter();
        int index = 0;

        while (index < input.length()) {
            int length = unescaper.translate(input, index, out);
            index += length;
        }

        String expectedOutput = "this&lambda&amp;&#123;&a;&f;&FF&FF;\n";
        assertEquals(expectedOutput, out.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithSemiColonRequiredAndMissingSemiColon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
        CharSequence input = "&amp;this&lamda;&#x26;#123";
        StringWriter out = new StringWriter();
        int index = 0;

        while (index < input.length()) {
            int length = unescaper.translate(input, index, out);
            index += length;
        }
    }

    @Test
    public void testTranslateWithErrorIfNoSemiColon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.errorIfNoSemiColon);
        CharSequence input = "&amp;this&lamda;&#x26;#123";
        StringWriter out = new StringWriter();
        int index = 0;

        while (index < input.length()) {
            int length = unescaper.translate(input, index, out);
            index += length;
        }

        String expectedErrorMessage = "Semi-colon required at end of numeric entity";
        try {
            out.toString();
        } catch (IllegalArgumentException e) {
            assertEquals(expectedErrorMessage, e.getMessage());
        }
    }

    @Test
    public void testTranslateWithHex() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        CharSequence input = "&amp;#x1F44D;&#xA9;";
        StringWriter out = new StringWriter();
        int index = 0;

        while (index < input.length()) {
            int length = unescaper.translate(input, index, out);
            index += length;
        }

        String expectedOutput = "💃️";
        assertEquals(expectedOutput, out.toString());
    }

    @Test
    public void testTranslateWithHexAndSemiColonRequired() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(OPTION.semiColonRequired);
        CharSequence input = "&amp;#x1F44D;&#xA9;";
        StringWriter out = new StringWriter();
        int index = 0;

        while (index < input.length()) {
            int length = unescaper.translate(input, index, out);
            index += length;
        }

        String expectedOutput = "💃️\n";
        assertEquals(expectedOutput, out.toString());
    }
}