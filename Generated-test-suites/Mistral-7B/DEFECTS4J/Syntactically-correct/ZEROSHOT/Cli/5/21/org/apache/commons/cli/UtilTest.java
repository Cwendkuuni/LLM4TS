package org.apache.commons.cli.test; // please note you should replace the package name with the actual package name of your test class

import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test class for Util class.
 */
public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String inputStr1 = "--foo";
        String expectedOutput1 = "foo";
        String outputStr1 = Util.stripLeadingHyphens(inputStr1);
        assertEquals(expectedOutput1, outputStr1);

        String inputStr2 = "--bar--baz";
        String expectedOutput2 = "bar-baz";
        String outputStr2 = Util.stripLeadingHyphens(inputStr2);
        assertEquals(expectedOutput2, outputStr2);

        String inputStr3 = "foo";
        String expectedOutput3 = "foo";
        String outputStr3 = Util.stripLeadingHyphens(inputStr3);
        assertEquals(expectedOutput3, outputStr3);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String inputStr1 = "\"one two\"";
        String expectedOutput1 = "one two";
        String outputStr1 = Util.stripLeadingAndTrailingQuotes(inputStr1);
        assertEquals(expectedOutput1, outputStr1);

        String inputStr2 = "\"one\"";
        String expectedOutput2 = "one";
        String outputStr2 = Util.stripLeadingAndTrailingQuotes(inputStr2);
        assertEquals(expectedOutput2, outputStr2);

        String inputStr3 = "one two";
        String expectedOutput3 = "one two";
        String outputStr3 = Util.stripLeadingAndTrailingQuotes(inputStr3);
        assertEquals(expectedOutput3, outputStr3);

        String inputStr4 = "\"\"";
        String expectedOutput4 = "";
        String outputStr4 = Util.stripLeadingAndTrailingQuotes(inputStr4);
        assertEquals(expectedOutput4, outputStr4);
    }
}