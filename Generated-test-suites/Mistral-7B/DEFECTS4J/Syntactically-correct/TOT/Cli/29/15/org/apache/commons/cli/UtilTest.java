package org.apache.commons.cli.test;

import org.apache.commons.cli.Util;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for Util helper methods in org.apache.commons.cli package.
 */
public class UtilTest
{
    @Test
    public void testStripLeadingHyphens()
    {
        String input1 = "--test1";
        String expectedOutput1 = "test1";
        String output1 = Util.stripLeadingHyphens(input1);
        Assert.assertEquals(expectedOutput1, output1);

        String input2 = "test2";
        String expectedOutput2 = "test2";
        String output2 = Util.stripLeadingHyphens(input2);
        Assert.assertEquals(expectedOutput2, output2);

        String input3 = "--test3";
        String expectedOutput3 = "test3";
        String output3 = Util.stripLeadingHyphens(input3);
        Assert.assertEquals(expectedOutput3, output3);

        String input4 = "";
        String expectedOutput4 = "";
        String output4 = Util.stripLeadingHyphens(input4);
        Assert.assertEquals(expectedOutput4, output4);

        String input5 = "--";
        String expectedOutput5 = "";
        String output5 = Util.stripLeadingHyphens(input5);
        Assert.assertEquals(expectedOutput5, output5);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes()
    {
        String input1 = "\"one two\"";
        String expectedOutput1 = "one two";
        String output1 = Util.stripLeadingAndTrailingQuotes(input1);
        Assert.assertEquals(expectedOutput1, output1);

        String input2 = "one two";
        String expectedOutput2 = "one two";
        String output2 = Util.stripLeadingAndTrailingQuotes(input2);
        Assert.assertEquals(expectedOutput2, output2);

        String input3 = "\"one two\"";
        String expectedOutput3 = "one two";
        String output3 = Util.stripLeadingAndTrailingQuotes(input3);
        Assert.assertEquals(expectedOutput3, output3);

        String input4 = "";
        String expectedOutput4 = "";
        String output4 = Util.stripLeadingAndTrailingQuotes(input4);
        Assert.assertEquals(expectedOutput4, output4);

        String input5 = "\"\"";
        String expectedOutput5 = "";
        String output5 = Util.stripLeadingAndTrailingQuotes(input5);
        Assert.assertEquals(expectedOutput5, output5);

        String input6 = "\"one\"";
        String expectedOutput6 = "one";
        String output6 = Util.stripLeadingAndTrailingQuotes(input6);
        Assert.assertEquals(expectedOutput6, output6);
    }

    @Test(expected = NullPointerException.class)
    public void testStripLeadingHyphensWithNullInput()
    {
        String input = null;
        Util.stripLeadingHyphens(input);
    }

    @Test(expected = NullPointerException.class)
    public void testStripLeadingAndTrailingQuotesWithNullInput()
    {
        String input = null;
        Util.stripLeadingAndTrailingQuotes(input);
    }
}