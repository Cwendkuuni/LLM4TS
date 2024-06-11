package org.apache.commons.cli.test;

import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest
{
    @Test
    public void testStripLeadingHyphens()
    {
        String input1 = "--foo";
        String expected1 = "foo";
        String result1 = Util.stripLeadingHyphens(input1);
        assertEquals(expected1, result1);

        String input2 = "--foo--bar";
        String expected2 = "foo bar";
        String result2 = Util.stripLeadingHyphens(input2);
        assertEquals(expected2, result2);

        String input3 = "--";
        String expected3 = "";
        String result3 = Util.stripLeadingHyphens(input3);
        assertEquals(expected3, result3);

        String input4 = "foo";
        String expected4 = "foo";
        String result4 = Util.stripLeadingHyphens(input4);
        assertEquals(expected4, result4);

        String input5 = "foo--";
        String expected5 = "foo";
        String result5 = Util.stripLeadingHyphens(input5);
        assertEquals(expected5, result5);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes()
    {
        String input1 = "\"foo bar\"";
        String expected1 = "foo bar";
        String result1 = Util.stripLeadingAndTrailingQuotes(input1);
        assertEquals(expected1, result1);

        String input2 = "\"foo bar \t\"";
        String expected2 = "foo bar";
        String result2 = Util.stripLeadingAndTrailingQuotes(input2);
        assertEquals(expected2, result2);

        String input3 = "\"\"";
        String expected3 = "";
        String result3 = Util.stripLeadingAndTrailingQuotes(input3);
        assertEquals(expected3, result3);

        String input4 = "\"foo\"";
        String expected4 = "foo";
        String result4 = Util.stripLeadingAndTrailingQuotes(input4);
        assertEquals(expected4, result4);

        String input5 = "\"foo bar \t\"";
        String expected5 = "foo bar";
        String result5 = Util.stripLeadingAndTrailingQuotes(input5);
        assertEquals(expected5, result5);
    }
}