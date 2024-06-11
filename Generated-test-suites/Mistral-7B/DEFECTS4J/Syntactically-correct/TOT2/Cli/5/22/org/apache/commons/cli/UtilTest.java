package org.apache.commons.cli.test;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String input = "--test";
        String expectedOutput = "test";
        String output = Util.stripLeadingHyphens(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String input = "\"test\"";
        String expectedOutput = "test";
        String output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expectedOutput, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStripLeadingHyphensWithNullString() {
        String input = null;
        Util.stripLeadingHyphens(input);
    }

    @Test
    public void testStripLeadingHyphensWithEmptyString() {
        String input = "";
        String expectedOutput = "";
        String output = Util.stripLeadingHyphens(input);
        assertEquals(expectedOutput, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStripLeadingAndTrailingQuotesWithNullString() {
        String input = null;
        Util.stripLeadingAndTrailingQuotes(input);
    }

    @Test
    public void testStripLeadingAndTrailingQuotesWithEmptyString() {
        String input = "";
        String expectedOutput = "";
        String output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expectedOutput, output);
    }
}