package org.apache.commons.cli.test;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for Util class methods.
 */
public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String input = "--foo";
        String expectedOutput = "foo";
        String output = Util.stripLeadingHyphens(input);
        Assert.assertEquals(expectedOutput, output);

        input = "foo";
        expectedOutput = "foo";
        output = Util.stripLeadingHyphens(input);
        Assert.assertEquals(expectedOutput, output);

        input = "-foo";
        expectedOutput = "foo";
        output = Util.stripLeadingHyphens(input);
        Assert.assertEquals(expectedOutput, output);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String input = "\"--foo\"";
        String expectedOutput = "--foo";
        String output = Util.stripLeadingAndTrailingQuotes(input);
        Assert.assertEquals(expectedOutput, output);

        input = "\"--foo--\"";
        expectedOutput = "--foo--";
        output = Util.stripLeadingAndTrailingQuotes(input);
        Assert.assertEquals(expectedOutput, output);

        input = "\"foo\"";
        expectedOutput = "foo";
        output = Util.stripLeadingAndTrailingQuotes(input);
        Assert.assertEquals(expectedOutput, output);

        input = "\"foo bar\"";
        expectedOutput = "foo bar";
        output = Util.stripLeadingAndTrailingQuotes(input);
        Assert.assertEquals(expectedOutput, output);

        input = "\"foo bar \":\"";
        expectedOutput = "foo bar :";
        output = Util.stripLeadingAndTrailingQuotes(input);
        Assert.assertEquals(expectedOutput, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStripLeadingHyphensNullInput() {
        Util. stripLeadingHyphens(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStripLeadingAndTrailingQuotesNullInput() {
        Util.stripLeadingAndTrailingQuotes(null);
    }
}