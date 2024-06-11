package org.apache.commons.cli.test;

import org.apache.commons.cli.Util;
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
        String input = "'one two'";
        String expectedOutput = "one two";
        String output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testStripLeadingHyphensEmptyString() {
        String input = "";
        String expectedOutput = "";
        String output = Util.stripLeadingHyphens(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testStripLeadingHyphensNoLeadingHyphens() {
        String input = "test";
        String expectedOutput = "test";
        String output = Util.stripLeadingHyphens(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testStripLeadingHyphensMultipleLeadingHyphens() {
        String input = "---test";
        String expectedOutput = "test";
        String output = Util.stripLeadingHyphens(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testStripLeadingAndTrailingQuotesEmptyString() {
        String input = "";
        String expectedOutput = "";
        String output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testStripLeadingAndTrailingQuotesSingleQuotes() {
        String input = "'one two'";
        String expectedOutput = "one two";
        String output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testStripLeadingAndTrailingQuotesMultipleQuotes() {
        String input = "''one two''";
        String expectedOutput = "one two";
        String output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testStripLeadingAndTrailingQuotesNoQuotes() {
        String input = "onetwothree";
        String expectedOutput = "onetwothree";
        String output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expectedOutput, output);
    }
}