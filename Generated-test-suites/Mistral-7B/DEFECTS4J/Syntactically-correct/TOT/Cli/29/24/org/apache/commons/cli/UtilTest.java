package org.apache.commons.cli.test;

import org.apache.commons.cli.Util;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for Util methods.
 */
public class UtilTest
{
    /**
     * Test for 'stripLeadingHyphens' method.
     */
    @Test
    public void testStripLeadingHyphens()
    {
        String str = "--foo--bar";
        String expected = "foo--bar";
        String result = Util.stripLeadingHyphens(str);
        Assert.assertEquals(expected, result);

        str = "foo--bar";
        expected = "foo--bar";
        result = Util.stripLeadingHyphens(str);
        Assert.assertEquals(expected, result);

        str = "foo--";
        expected = "foo";
        result = Util.stripLeadingHyphens(str);
        Assert.assertEquals(expected, result);

        str = "foo";
        expected = "foo";
        result = Util.stripLeadingHyphens(str);
        Assert.assertEquals(expected, result);

        str = null;
        expected = null;
        result = Util.stripLeadingHyphens(str);
        Assert.assertEquals(expected, result);
    }

    /**
     * Test for 'stripLeadingAndTrailingQuotes' method.
     */
    @Test
    public void testStripLeadingAndTrailingQuotes()
    {
        String str = "\"one two\"";
        String expected = "one two";
        String result = Util.stripLeadingAndTrailingQuotes(str);
        Assert.assertEquals(expected, result);

        str = "one two";
        expected = "one two";
        result = Util.stripLeadingAndTrailingQuotes(str);
        Assert.assertEquals(expected, result);

        str = "\"one\"";
        expected = "one";
        result = Util.stripLeadingAndTrailingQuotes(str);
        Assert.assertEquals(expected, result);

        str = "\"\"";
        expected = "";
        result = Util.stripLeadingAndTrailingQuotes(str);
        Assert.assertEquals(expected, result);

        str = null;
        expected = null;
        result = Util.stripLeadingAndTrailingQuotes(str);
        Assert.assertEquals(expected, result);
    }
}