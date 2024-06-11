package org.apache.commons.cli.test;

import org.apache.commons.cli. Util;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for Util methods: stripLeadingHyphens and stripLeadingAndTrailingQuotes.
 */
public class UtilTest
{
    @Test
    public void testStripLeadingHyphens()
    {
        String str = "--test";
        String expected = "test";
        String actual = Util.stripLeadingHyphens(str);
        Assert.assertEquals(expected, actual);

        str = "-test";
        expected = "test";
        actual = Util.stripLeadingHyphens(str);
        Assert.assertEquals(expected, actual);

        str = "test";
        expected = "test";
        actual = Util.stripLeadingHyphens(str);
        Assert.assertEquals(expected, actual);

        str = null;
        expected = null;
        actual = Util.stripLeadingHyphens(str);
        Assert.assertNull(actual);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes()
    {
        String str = "\"--test\"";
        String expected = "--test";
        String actual = Util.stripLeadingAndTrailingQuotes(str);
        Assert.assertEquals(expected, actual);

        str = "\"-test\"";
        expected = "-test";
        actual = Util.stripLeadingAndTrailingQuotes(str);
        Assert.assertEquals(expected, actual);

        str = "\"test\"";
        expected = "test";
        actual = Util.stripLeadingAndTrailingQuotes(str);
        Assert.assertEquals(expected, actual);

        str = "\"\"";
        expected = "";
        actual = Util.stripLeadingAndTrailingQuotes(str);
        Assert.assertEquals(expected, actual);

        str = null;
        expected = null;
        actual = Util.stripLeadingAndTrailingQuotes(str);
        Assert.assertNull(actual);
    }
}