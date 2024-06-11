package org.apache.commons.cli.test;

import org.apache.commons.cli.Util;
import org.junit.Assert;
import org.junit.Test;

public class UtilTest
{
    @Test
    public void testStripLeadingHyphens()
    {
        // typical use case
        String input = "--test";
        String expectedOutput = "test";
        String output = Util.stripLeadingHyphens(input);
        Assert.assertEquals(expectedOutput, output);

        // edge cases
        String input1 = "test";
        String expectedOutput1 = "test";
        String output1 = Util.stripLeadingHyphens(input1);
        Assert.assertEquals(expectedOutput1, output1);

        String input2 = "--test--";
        String expectedOutput2 = "test";
        String output2 = Util.stripLeadingHyphens(input2);
        Assert.assertEquals(expectedOutput2, output2);

        String input3 = "---test---";
        String expectedOutput3 = "test";
        String output3 = Util.stripLeadingHyphens(input3);
        Assert.assertEquals(expectedOutput3, output3);

        // error cases
        String input4 = null;
        String output4 = Util.stripLeadingHyphens(input4);
        Assert.assertNull(output4);

        String input5 = "";
        String output5 = Util.stripLeadingHyphens(input5);
        Assert.assertEquals("", output5);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes()
    {
        // typical use case
        String input = "'test'";
        String expectedOutput = "test";
        String output = Util.stripLeadingAndTrailingQuotes(input);
        Assert.assertEquals(expectedOutput, output);

        // edge cases
        String input1 = "test";
        String expectedOutput1 = "test";
        String output1 = Util.stripLeadingAndTrailingQuotes(input1);
        Assert.assertEquals(expectedOutput1, output1);

        String input2 = "test''";
        String expectedOutput2 = "test";
        String output2 = Util.stripLeadingAndTrailingQuotes(input2);
        Assert.assertEquals(expectedOutput2, output2);

        String input3 = "test'''";
        String expectedOutput3 = "test";
        String output3 = Util.stripLeadingAndTrailingQuotes(input3);
        Assert.assertEquals(expectedOutput3, output3);

        String input4 = "\"test\"";
        String expectedOutput4 = "test";
        String output4 = Util.stripLeadingAndTrailingQuotes(input4);
        Assert.assertEquals(expectedOutput4, output4);

        // error cases
        String input5 = null;
        String output5 = Util.stripLeadingAndTrailingQuotes(input5);
        Assert.assertNull(output5);

        String input6 = "";
        String output6 = Util.stripLeadingAndTrailingQuotes(input6);
        Assert.assertEquals("", output6);

        String input7 = "\"test\"abc";
        String expectedOutput7 = "testabc";
        String output7 = Util.stripLeadingAndTrailingQuotes(input7);
        Assert.assertEquals(expectedOutput7, output7);
    }
}