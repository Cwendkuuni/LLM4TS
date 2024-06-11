package org.apache.commons.cli.test;// for proper packaging in tests

import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for Util methods.
 */
public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String strWithDoubleHyphen = "--foo";
        String strWithSingleHyphen = "-foo";
        String strWithoutHyphen = "foo";

        assertEquals("foo", Util.stripLeadingHyphens(strWithoutHyphen));
        assertEquals("foo", Util.stripLeadingHyphens(strWithSingleHyphen));
        assertEquals("foo", Util.stripLeadingHyphens(strWithDoubleHyphen));

        String strWithDoubleLeadingHyphen = "----foo";
        assertEquals("foo", Util.stripLeadingHyphens(strWithDoubleLeadingHyphen));
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String strWithLeadingQuotes = "\"one two\"";
        String strWithTrailingQuotes = "one two\"";
        String strWithBothQuotes = "\"one two\"";
        String strWithoutQuotes = "one two";

        assertEquals("one two", Util.stripLeadingAndTrailingQuotes(strWithBothQuotes));
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes(strWithLeadingQuotes));
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes(strWithTrailingQuotes));
        assertEquals("one two", Util.stripLeadingAndTrailingQuotes(strWithoutQuotes));
    }
}