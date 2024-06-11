package org.apache.commons.lang3.text.translate;

import static org.apache.commons.lang3.text.translate.NumericEntityUnescaper.OPTION.*;
import static org.junit.Assert.*;

import java.io.StringWriter;
import java.util.Arrays;

import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;

/**
 * NumericEntityUnescaperTest
 */
public class NumericEntityUnescaperTest {

    @Test
    public void testNumericEntityUnescaperWithDefaultOptions() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&num;Hello &#10;World! &#x1f60a;";
        StringWriter outWriter = new StringWriter();
        int index = 0;

        while (index < input.length()) {
            int length = unescaper.translate(new String(input), index, outWriter);
            index += length;
            String expected = Character.isDefinedChar(input.charAt(index))
                    ? Character.toString(input.charAt(index))
                    : "";
            String actual = outWriter.toString();
            assertEquals("Unexpected output for index [" + index + "] of input [" + input + "]", expected, actual);
        }

        assertEquals("", outWriter.toString());
    }

    @Test
    public void testNumericEntityUnescaperWithSemiColonRequiredOption() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(SEMI_COLON_REQUIRED);
        String input = "&num;Hello &#10;World! &#x1f60a";
        StringWriter outWriter = new StringWriter();
        int index = 0;

        while (index < input.length()) {
            int length = unescaper.translate(new String(input), index, outWriter);
            index += length;
            String expected = Character.isDefinedChar(input.charAt(index))
                    ? Character.toString(input.charAt(index))
                    : "";
            String actual = outWriter.toString();
            assertEquals("Unexpected output for index [" + index + "] of input [" + input + "]", expected, actual);
        }

        assertEquals("", outWriter.toString());

        // Test with missing semi-colon
        input = "&num;Hello &#10 World!";
        outWriter = new StringWriter();
        index = 0;

        try {
            while (index < input.length()) {
                int length = unescaper.translate(new String(input), index, outWriter);
                index += length;
            }
            fail("Expected IllegalArgumentException due to missing semi-colon");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        assertEquals("", outWriter.toString());
    }

    @Test
    public void testNumericEntityUnescaperWithSemiColonOptionalOption() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(SEMI_COLON_OPTIONAL);
        String input = "&num;Hello &#10;World! &#x1f60a";
        StringWriter outWriter = new StringWriter();
        int index = 0;

        while (index < input.length()) {
            int length = unescaper.translate(new String(input), index, outWriter);
            index += length;
            String expected = Character.isDefinedChar(input.charAt(index))
                    ? Character.toString(input.charAt(index))
                    : "";
            String actual = outWriter.toString();
            assertEquals("Unexpected output for index [" + index + "] of input [" + input + "]", expected, actual);
        }

        assertEquals("", outWriter.toString());

        // Test with missing semi-colon
        input = "&num;Hello &#10 World!";
        outWriter = new StringWriter();
        index = 0;

        while (index < input.length()) {
            int length = unescaper.translate(new String(input), index, outWriter);
            index += length;
            String expected = Character.isDefinedChar(input.charAt(index))
                    ? Character.toString(input.charAt(index))
                    : "";
            String actual = outWriter.toString();
            assertEquals("Unexpected output for index [" + index + "] of input [" + input + "]", expected, actual);
        }

        assertEquals("", outWriter.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNumericEntityUnescaperWithErrorIfNoSemiColonOption() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(ERROR_IF_NO_SEMI_COLON);
        String input = "&num;Hello &#10 World!";
        StringWriter outWriter = new StringWriter();
        int index = 0;

        try {
            while (index < input.length()) {
                int length = unescaper.translate(new String(input), index, outWriter);
                index += length;
            }
        } catch (IllegalArgumentException e) {
            throw e; // Re-throw to test the expected exception
        } finally {
            fail("Expected IllegalArgumentException due to missing semi-colon");
        }
    }

    @Test
    public void testNumericEntityUnescaperWithHexOption() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&num;Hello &#x1f60a;World!";
        StringWriter outWriter = new StringWriter();
        int index = 0;

        while (index < input.length()) {
            int length = unescaper.translate(new String(input), index, outWriter);
            index += length;
            String expected = Character.isDefinedChar(input.charAt(index))
                    ? Character.toString(input.charAt(index))
                    : "";
            String actual = outWriter.toString();
            assertEquals("Unexpected output for index [" + index + "] of input [" + input + "]", expected, actual);
        }

        assertEquals("", outWriter.toString());

        // Test with invalid hex value
        input = "&num;Hello &#x1f60z;World!";
        outWriter = new StringWriter();
        index = 0;

        try {
            while (index < input.length()) {
                int length = unescaper.translate(new String(input), index, outWriter);
                index += length;
            }
            fail("Expected NumberFormatException for invalid hex value 'z'");
        } catch (NumberFormatException e) {
            // Expected
        }

        assertEquals("", outWriter.toString());
    }

    @Test
    public void testNumericEntityUnescaperWithMultipleNumericEntities() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&num;Hello &#10;World! &#123; &#x1f60a;&#x1f642; &#65271; &#x1f6ff;";
        StringWriter outWriter = new StringWriter();
        int index = 0;

        while (index < input.length()) {
            int length = unescaper.translate(new String(input), index, outWriter);
            index += length;
            String expected = Character.isDefinedChar(input.charAt(index))
                    ? Character.toString(input.charAt(index))
                    : "";
            String actual = outWriter.toString();
            assertEquals("Unexpected output for index [" + index + "] of input [" + input + "]", expected, actual);
        }

        assertEquals("", outWriter.toString());

        // Test with invalid numeric entity
        input = "&num;Hello &#10;World! &#x1f60z; &#6527z;";
        outWriter = new StringWriter();
        index = 0;

        try {
            while (index < input.length()) {
                int length = unescaper.translate(new String(input), index, outWriter);
                index += length;
            }
            fail("Expected NumberFormatException for invalid numeric entities 'z'");
        } catch (NumberFormatException e) {
            // Expected
        }

        assertEquals("", outWriter.toString());
    }

}