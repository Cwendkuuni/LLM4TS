package org.apache.commons.cli.test;

import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {
    @Test
    public void testStripLeadingHyphens() {
        String str = "--test-string";
        String expected = "test-string";
        assertEquals(expected, Util.stripLeadingHyphens(str));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String str = "\"test-string\"";
        String expected = "test-string";
        assertEquals(expected, Util.stripLeadingAndTrailingQuotes(str));
    }

    @Test
    public void testStripLeadingHyphensNullInput() {
        String str = null;
        assertNull(Util.stripLeadingHyphens(str));
    }

    @Test
    public void testStripLeadingHyphensEmptyString() {
        String str = "";
        assertEquals("", Util.stripLeadingHyphens(str));
    }

    @Test
    public void testStripLeadingHyphensHyphenAtSecondPosition() {
        String str = "ab--cd";
        String expected = "abcd";
        assertEquals(expected, Util.stripLeadingHyphens(str));
    }

    @Test
    public void testStripLeadingAndTrailingQuotesEmptyString() {
        String str = "";
        assertEquals("", Util.stripLeadingAndTrailingQuotes(str));
    }

    @Test
    public void testStripLeadingAndTrailingQuotesOpeningQuoteOnly() {
        String str = "\"";
        assertEquals("", Util.stripLeadingAndTrailingQuotes(str));
    }

    @Test
    public void testStripLeadingAndTrailingQuotesClosingQuoteOnly() {
        String str = "\"";
        assertEquals("", Util.stripLeadingAndTrailingQuotes(str));
    }

    @Test
    public void testStripLeadingAndTrailingQuotesMultipleQuotes() {
        String str = "\"\"test-string\"\"";
        String expected = "test-string";
        assertEquals(expected, Util.stripLeadingAndTrailingQuotes(str));
    }
}