package org.apache.commons.cli.test;

import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String input = "--str";
        String expected = "str";
        String output = Util.stripLeadingHyphens(input);
        assertEquals(expected, output);
    }

    @Test
    public void testStripLeadingHyphensEmptyString() {
        String input = "";
        String expected = "";
        String output = Util.stripLeadingHyphens(input);
        assertEquals(expected, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStripLeadingHyphensNullString() {
        String input = null;
        Util.stripLeadingHyphens(input);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String input = '"one two"';
        String expected = "one two";
        String output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expected, output);
    }

    @Test
    public void testStripLeadingAndTrailingQuotesEmptyString() {
        String input = "";
        String expected = "";
        String output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expected, output);
    }

    @Test
    public void testStripLeadingAndTrailingQuotesNoQuotes() {
        String input = "one two";
        String expected = "one two";
        String output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expected, output);
    }

    @Test
    public void testStripLeadingAndTrailingQuotesSingleLeadingQuote() {
        String input = "'one two'";
        String expected = "one two";
        String output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expected, output);
    }

    @Test
    public void testStripLeadingAndTrailingQuotesSingleTrailingQuote() {
        String input = "one two'";
        String expected = "one two";
        String output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expected, output);
    }
}