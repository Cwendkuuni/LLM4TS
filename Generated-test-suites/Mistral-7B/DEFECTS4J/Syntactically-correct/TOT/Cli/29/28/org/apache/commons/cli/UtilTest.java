package org.apache.commons.cli.test;

import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for Util methods.
 */
public class UtilTest
{
    @Test
    public void testStripLeadingHyphens()
    {
        String str = "--foo";
        String expected = "foo";
        String result = Util.stripLeadingHyphens(str);
        assertEquals(expected, result);

        str = "foo";
        expected = "foo";
        result = Util.stripLeadingHyphens(str);
        assertEquals(expected, result);

        str = "--";
        expected = "";
        result = Util.stripLeadingHyphens(str);
        assertEquals(expected, result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes()
    {
        String str = "\"" + "one two" + "\"";
        String expected = "one two";
        String result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, result);

        str = "one two";
        expected = "one two";
        result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, result);

        str = "";
        expected = "";
        result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, result);
    }

    @Test(expected = AssertionError.class)
    public void testStripLeadingHyphensWithNull()
    {
        String str = null;
        Util.stripLeadingHyphens(str);
    }

    @Test(expected = AssertionError.class)
    public void testStripLeadingAndTrailingQuotesWithNull()
    {
        String str = null;
        Util.stripLeadingAndTrailingQuotes(str);
    }
}