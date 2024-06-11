package org.apache.commons.cli.test;

import org.apache.commons.cli.Util;
import org.junit.Assert;
import org.junit.Test;

public class UtilTest
{
    @Test
    public void testStripLeadingHyphens()
    {
        String str = "--test";
        String expected = "test";
        String actual = Util.stripLeadingHyphens(str);
        Assert.assertEquals(expected, actual);

        str = "---test";
        expected = "test";
        actual = Util.stripLeadingHyphens(str);
        Assert.assertEquals(expected, actual);

        str = "test";
        expected = "test";
        actual = Util.stripLeadingHyphens(str);
        Assert.assertEquals(expected, actual);

        str = null;
        actual = Util.stripLeadingHyphens(str);
        Assert.assertNull(actual);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes()
    {
        String str = "\"test1 test2\"";
        String expected = "test1 test2";
        String actual = Util.stripLeadingAndTrailingQuotes(str);
        Assert.assertEquals(expected, actual);

        str = "\"\"test1 test2\"\"";
        expected = "test1 test2";
        actual = Util.stripLeadingAndTrailingQuotes(str);
        Assert.assertEquals(expected, actual);

        str = "test1 test2";
        expected = "test1 test2";
        actual = Util.stripLeadingAndTrailingQuotes(str);
        Assert.assertEquals(expected, actual);

        str = "";
        actual = Util.stripLeadingAndTrailingQuotes(str);
        Assert.assertEquals("", actual);
    }

    @Test(expected = AssertionError.class)
    public void testStripLeadingHyphensNegative()
    {
        String str = "test";
        String actual = Util.stripLeadingHyphens(str);
        Assert.assertNotEquals("--test", actual);
    }

    @Test(expected = AssertionError.class)
    public void testStripLeadingAndTrailingQuotesNegative()
    {
        String str = "test";
        String actual = Util.stripLeadingAndTrailingQuotes(str);
        Assert.assertNotEquals("\"test\"", actual);
    }

    @Test
    public void testNullInput()
    {
        String input = null;
        try {
            Util.stripLeadingHyphens(input);
        } catch (NullPointerException e) {
            //expected
        }

        try {
            Util.stripLeadingAndTrailingQuotes(input);
        } catch (NullPointerException e) {
            //expected
        }
    }
}