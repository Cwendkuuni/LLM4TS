package org.apache.commons.cli.test;

import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String input = "--test";
        String expectedOutput = "test";
        String output = Util.stripLeadingHyphens(input);
        assertEquals(expectedOutput, output);
    }

    @Test(expected = NullPointerException.class)
    public void testStripLeadingHyphensWithNullInput() {
        String input = null;
        String expectedOutput = null;
        String output = Util.stripLeadingHyphens(input);
        assertNull(output);
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
    public void testStripLeadingAndTrailingQuotes() {
        String input = "\"one two\"";
        String expectedOutput = "one two";
        String output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expectedOutput, output);
    }

    @Test(expected = NullPointerException.class)
    public void testStripLeadingAndTrailingQuotesWithNullInput() {
        String input = null;
        String expectedOutput = null;
        String output = Util.stripLeadingAndTrailingQuotes(input);
        assertNull(output);
    }

    @Test
    public void testStripLeadingAndTrailingQuotesEmptyString() {
        String input = "";
        String expectedOutput = "";
        String output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testStripLeadingAndTrailingQuotesNoLeadingQuotes() {
        String input = "one two";
        String expectedOutput = "one two";
        String output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testStripLeadingAndTrailingQuotesNoTrailingQuotes() {
        String input = "\"one two";
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