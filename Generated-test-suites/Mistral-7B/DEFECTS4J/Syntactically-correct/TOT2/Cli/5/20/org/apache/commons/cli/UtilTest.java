package org.apache.commons.cli.test;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String input = "--test-string";
        String expectedOutput = "test-string";
        String actualOutput = Util.stripLeadingHyphens(input);
        assertEquals(expectedOutput, actualOutput);
    }

    // Edge case test for an empty string
    @Test
    public void testStripLeadingHyphensEmptyString() {
        String input = "";
        String expectedOutput = "";
        String actualOutput = Util.stripLeadingHyphens(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test(expected = NullPointerException.class)
    public void testStripLeadingHyphensNullInput() {
        String input = null;
        Util.stripLeadingHyphens(input);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String input = "'one two'";
        String expectedOutput = "one two";
        String actualOutput = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expectedOutput, actualOutput);
    }

    // Edge case test for an empty string
    @Test
    public void testStripLeadingAndTrailingQuotesEmptyString() {
        String input = "";
        String expectedOutput = "";
        String actualOutput = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test(expected = NullPointerException.class)
    public void testStripLeadingAndTrailingQuotesNullInput() {
        String input = null;
        Util.stripLeadingAndTrailingQuotes(input);
    }
}