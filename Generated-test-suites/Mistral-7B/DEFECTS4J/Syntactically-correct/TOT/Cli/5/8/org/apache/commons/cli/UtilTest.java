package org.apache.commons.cli.test;

import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test cases for the Util class.
 */
public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        // Typical use case
        String str = "--option";
        String expected = "option";
        String result = Util.stripLeadingHyphens(str);
        assertEquals(expected, result);

        // Empty string
        str = "";
        expected = "";
        result = Util.stripLeadingHyphens(str);
        assertEquals(expected, result);

        // No leading hyphens
        str = "option";
        expected = "option";
        result = Util.stripLeadingHyphens(str);
        assertEquals(expected, result);

        // Leading single hyphen
        str = "-option";
        expected = "option";
        result = Util.stripLeadingHyphens(str);
        assertEquals(expected, result);

        // Leading double hyphens
        str = "--option";
        expected = "option";
        result = Util.stripLeadingHyphens(str);
        assertEquals(expected, result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        // Typical use case
        String str = "'one two'";
        String expected = "one two";
        String result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, result);

        // Empty string
        str = "";
        expected = "";
        result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, result);

        // No quotes
        str = "one two";
        expected = "one two";
        result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, result);

        // Leading quotes
        str = "'one two'";
        expected = "one two";
        result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, result);

        // Trailing quotes
        str = "one two'";
        expected = "one two";
        result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, result);

        // Leading and trailing quotes
        str = "'one two'";
        expected = "one two";
        result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotesNull() {
        String str = null;
        String expected = null;
        String result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, result);
    }
}