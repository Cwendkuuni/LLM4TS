package org.apache.commons.cli.test;

import org.junit.Test;
import org.apache.commons.cli.Util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class UtilTest
{
    @Test
    public void testStripLeadingHyphens()
    {
        String inputNull = null;
        String inputNoLeadingHyphens = "abc";
        String inputWithOneLeadingHyphen = "--abc";
        String inputWithTwoLeadingHyphens = "----abc";
        String expectedOutputNoLeadingHyphens = "abc";
        String expectedOutputOneLeadingHyphen = "abc";
        String expectedOutputTwoLeadingHyphens = "abc";

        assertEquals(expectedOutputNoLeadingHyphens, Util.stripLeadingHyphens(inputNoLeadingHyphens));
        assertEquals(expectedOutputOneLeadingHyphen, Util.stripLeadingHyphens(inputWithOneLeadingHyphen));
        assertEquals(expectedOutputTwoLeadingHyphens, Util.stripLeadingHyphens(inputWithTwoLeadingHyphens));
        assertNull(Util.stripLeadingHyphens(inputNull));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes()
    {
        String inputNull = null;
        String inputQuotesAtBeginning = "\"abc\"";
        String inputQuotesAtEnd = "abc\"";
        String inputQuotesAtBeginningAndEnd = "\"abc\"";
        String expectedOutputWithNoQuotes = "abc";

        assertEquals(expectedOutputWithNoQuotes, Util.stripLeadingAndTrailingQuotes(inputQuotesAtBeginning));
        assertEquals(expectedOutputWithNoQuotes, Util.stripLeadingAndTrailingQuotes(inputQuotesAtEnd));
        assertEquals(expectedOutputWithNoQuotes, Util.stripLeadingAndTrailingQuotes(inputQuotesAtBeginningAndEnd));
        assertNull(Util.stripLeadingAndTrailingQuotes(inputNull));
    }
}