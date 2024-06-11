package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for NumericEntityUnescaper.
 */
public class NumericEntityUnescaperTest {

    private NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

    /**
     * Test translating a simple numeric entity.
     */
    @Test
    public void testSimpleNumericEntity() {
        StringBuilder result = new StringBuilder();
        CharSequence input = "&amp;#160;";
        int length = unescaper.translate(input, 0, result);
        assertEquals("The length of the result should be 2.", 2, length);
        assertEquals("The result should be the character with codepoint 160.", "\u00A0", result.toString());
    }

    /**
     * Test translating a hexadecimal numeric entity.
     */
    @Test
    public void testHexadecimalNumericEntity() {
        StringBuilder result = new StringBuilder();
        CharSequence input = "&amp;#x61;";
        int length = unescaper.translate(input, 0, result);
        assertEquals("The length of the result should be 2.", 2, length);
        assertEquals("The result should be the character with codepoint 97.", "ao", result.toString());
    }

    /**
     * Test translating a numeric entity with an invalid format.
     */
    @Test(expected = NumberFormatException.class)
    public void testInvalidNumericEntity() {
        StringBuilder result = new StringBuilder();
        CharSequence input = "&amp;#x invalidFormat;";
        unescaper.translate(input, 0, result);
    }

    /**
     * Test translating a numeric entity with an empty character sequence.
     */
    @Test
    public void testEmptyNumericEntity() {
        StringBuilder result = new StringBuilder();
        CharSequence input = "";
        int length = unescaper.translate(input, 0, result);
        assertEquals("The length of the result should be 0.", 0, length);
        assertEquals("The result should be an empty string.", "", result.toString());
    }

    /**
     * Test translating a numeric entity with a negative index.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testNegativeIndexNumericEntity() {
        StringBuilder result = new StringBuilder();
        CharSequence input = "input";
        unescaper.translate(input, -1, result);
    }

    /**
     * Test translating a numeric entity at the end of the character sequence.
     */
    @Test
    public void testEndNumericEntity() {
        StringBuilder result = new StringBuilder();
        CharSequence input = "input&amp;#123;";
        int length = unescaper.translate(input, input.length() - 2, result);
        assertEquals("The length of the result should be 1.", 1, length);
        assertEquals("The result should be an empty string or a character with codepoint 123.", "\u007B", result.toString());
    }
}