package org.apache.commons.cli.test;

import org.apache.commons.cli.Util;
import org.junit.Assert;
import org.junit.Test;

public class UtilTest
{
    @Test
    public void testStripLeadingHyphens()
    {
        String input1 = "--test";
        String expected1 = "test";
        String result1 = Util.stripLeadingHyphens(input1);
        Assert.assertEquals(expected1, result1);

        String input2 = "---test";
        String expected2 = "test";
        String result2 = Util.stripLeadingHyphens(input2);
        Assert.assertEquals(expected2, result2);

        String input3 = "test";
        String expected3 = "test";
        String result3 = Util.stripLeadingHyphens(input3);
        Assert.assertEquals(expected3, result3);

        String inputNull = null;
        String expectedNull = null;
        String resultNull = Util.stripLeadingHyphens(inputNull);
        Assert.assertEquals(expectedNull, resultNull);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes()
    {
        String input1 = "'one two'";
        String expected1 = "one two";
        String result1 = Util.stripLeadingAndTrailingQuotes(input1);
        Assert.assertEquals(expected1, result1);

        String input2 = "\"one two\"";
        String expected2 = "one two";
        String result2 = Util.stripLeadingAndTrailingQuotes(input2);
        Assert.assertEquals(expected2, result2);

        String input3 = "one two";
        String expected3 = "one two";
        String result3 = Util.stripLeadingAndTrailingQuotes(input3);
        Assert.assertEquals(expected3, result3);

        String inputEmpty = "";
        String expectedEmpty = "";
        String resultEmpty = Util.stripLeadingAndTrailingQuotes(inputEmpty);
        Assert.assertEquals(expectedEmpty, resultEmpty);

        String inputQuotesOnly = "\"\"";
        String expectedQuotesOnly = "";
        String resultQuotesOnly = Util.stripLeadingAndTrailingQuotes(inputQuotesOnly);
        Assert.assertEquals(expectedQuotesOnly, resultQuotesOnly);
    }
}