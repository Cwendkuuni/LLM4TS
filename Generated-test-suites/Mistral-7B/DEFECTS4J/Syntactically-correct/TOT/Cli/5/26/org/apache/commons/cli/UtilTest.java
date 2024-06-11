package org.apache.commons.cli.test;

import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test cases for Util class.
 */
public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String str = "--some-string";
        String expected = "some-string";
        String result = Util.stripLeadingHyphens(str);
        assertEquals(expected, result);

        str = "-some-string";
        expected = "some-string";
        result = Util.stripLeadingHyphens(str);
        assertEquals(expected, result);

        str = "some-string";
        expected = "some-string";
        result = Util.stripLeadingHyphens(str);
        assertEquals(expected, result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String str = "\"one two\"";
        String expected = "one two";
        String result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, result);

        str = "\"one\"";
        expected = "one";
        result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, result);

        str = "";
        expected = "";
        result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, result);

        str = "\"\"";
        expected = "";
        result = Util.stripLeadingAndTrailingQuotes(str);
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStripLeadingAndTrailingQuotesWithEmptyString() {
        String str = "";
        Util.stripLeadingAndTrailingQuotes(str);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStripLeadingHyphensWithEmptyString() {
        String str = "";
        Util.stripLeadingHyphens(str);
    }
}