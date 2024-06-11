package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;

import java.io.*;
import java.util.Arrays;
import java.util.EnumSet;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testNumericEntityUnescaper() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        NumericEntityUnescaper unescaperWithOptions = new NumericEntityUnescaper(OPTION.semiColonRequired, OPTION.errorIfNoSemiColon);

        assertEquals(0, unescaper.translate(new StringReader(""), 0, new StringWriter()));
        assertEquals(0, unescaper.translate(new StringReader(""), 0, new StringWriter()));

        String input = "&amp; &lt; &gt; &apos; &quot; &amp;#128; &amp;#x7F; &amp;#xyz; &amp;#x{ABCD}; &amp;#xFFFF; &amp;#x10FFFF; &amp;#123456; &amp;#x123456; &amp;#x{1234ABCD}; ";
        String expectedOutput = "&amp; &lt; &gt; &apos; &quot; &#128; &#x7F; &#xyz; &#xABCD; &#xFFFF; &#x10FFFF; &#123456; &#x123456; &#x{1234ABCD}; ";
        Writer writer = new StringWriter();
        CharSequence inputSequence = new StringReader(input);

        int index = 0;
        int length = input.length();
        int translatedLength = 0;

        while (index < length) {
            int result = unescaper.translate(inputSequence, index, writer);
            translatedLength += result;
            index += result;
        }

        assertEquals(expectedOutput, writer.toString());

        writer.getBuffer().setLength(0);
        index = 0;
        translatedLength = 0;

        while (index < length) {
            int result = unescaperWithOptions.translate(inputSequence, index, writer);
            translatedLength += result;
            index += result;
        }

        expectedOutput += ";";
        assertEquals(expectedOutput, writer.toString());

        input = "&amp; &lt; &gt; &apos; &quot; &amp;#128; &amp;#x7F; &amp;#xyz; &amp;#x{ABCD}; &amp;#xFFFF; &amp;#x10FFFF; &amp;#123456; &amp;#x123456; &amp;#x{1234ABCD}; &amp; & ";
        writer.getBuffer().setLength(0);
        index = 0;
        translatedLength = 0;

        while (index < length) {
            int result = unescaperWithOptions.translate(inputSequence, index, writer);
            translatedLength += result;
            index += result;
        }

        assertEquals(expectedOutput, writer.toString());

        try {
            NumericEntityUnescaper unescaperWithInvalidOptions = new NumericEntityUnescaper(OPTION.semiColonRequired, OPTION.semiColonOptional, OPTION.errorIfNoSemiColon);
            fail("Should not be able to instantiate NumericEntityUnescaper with multiple options set");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            NumericEntityUnescaper unescaperWithMissingOption = new NumericEntityUnescaper(OPTION.semiColonOptional);
            assertEquals(OPTION.semiColonRequired, unescaperWithMissingOption.isSet(OPTION.semiColonRequired));
            assertEquals(true, unescaperWithMissingOption.isSet(OPTION.semiColonOptional));
            assertEquals(false, unescaperWithMissingOption.isSet(OPTION.errorIfNoSemiColon));
        } catch (Exception e) {
            fail("Should not throw an exception when instantiating NumericEntityUnescaper with missing option");
        }
    }

    @Test
    public void testNumericEntityUnescaperWithInvalidInput() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer writer = new StringWriter();

        String invalidInput = "&amp; &lt; &gt; &apos; &quot; &amp;#128; &amp;#x7F; &amp;#xyz; &amp;#x{ABCD}; &amp;#xFFFF; &amp;#x10FFFF; &amp;#123456; &amp;#x123456; &amp;#x{1234ABCD}; &amp; & &";
        CharSequence inputSequence = new StringReader(invalidInput);

        int index = 0;
        int length = invalidInput.length();

        while (index < length) {
            int result = unescaper.translate(inputSequence, index, writer);
            index += result;
        }

        assertEquals("", writer.toString());
    }
}