package org.apache.commons.cli.test; // replace "test" with your test package name if different

import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test class for Util class.
 */
public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String str1 = "--foo";
        String str2 = "--bar--baz";
        String str3 = "--";
        String str4 = "foo";

        assertEquals("foo", Util.stripLeadingHyphens(str4));
        assertEquals("foo", Util.stripLeadingHyphens(str3));
        assertEquals("bar-baz", Util.stripLeadingHyphens(str2));
        assertEquals("foo", Util.stripLeadingHyphens(str1));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String str1 = "";
        String str2 = "\"one two\"";
        String str3 = "\"one two three\"";
        String str4 = "\"one two\"\"";
        String str5 = "\"";

        assertEquals("", Util.stripLeadingAndTrailingQuotes(str1));
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes(str2));
        assertEquals("one two three", Util.stripLeadingAndTrailingQuotes(str3));
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes(str4));
        assertEquals("", Util.stripLeadingAndTrailingQuotes(str5));
    }
}