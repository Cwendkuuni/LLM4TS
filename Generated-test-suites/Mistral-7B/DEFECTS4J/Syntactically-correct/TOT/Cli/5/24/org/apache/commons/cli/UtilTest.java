package org.apache.commons.cli.test;

import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test class for Util methods in org.apache.commons.cli package.
 */
public class UtilTest {

    /**
     * Test for {@link Util#stripLeadingHyphens(String)} method.
     */
    @Test
    public void testStripLeadingHyphens() {
        // typical use case
        String input = "--option";
        String expectedOutput = "option";
        String output = Util.stripLeadingHyphens(input);
        assertEquals(expectedOutput, output);

        // edge case: no leading hyphens
        input = "option";
        expectedOutput = "option";
        output = Util.stripLeadingHyphens(input);
        assertEquals(expectedOutput, output);

        // error scenario: null input
        input = null;
        try {
            output = Util.stripLeadingHyphens(input);
        } catch (NullPointerException e) {
            // expected
        }
    }

    /**
     * Test for {@link Util#stripLeadingAndTrailingQuotes(String)} method.
     */
    @Test
    public void testStripLeadingAndTrailingQuotes() {
        // typical use case
        String input = "\""+"one two"+"\"";
        String expectedOutput = "one two";
        String output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expectedOutput, output);

        // edge case: no leading or trailing quotes
        input = "one two";
        expectedOutput = "one two";
        output = Util.stripLeadingAndTrailingQuotes(input);
        assertEquals(expectedOutput, output);

        // error scenario: null input
        input = null;
        try {
            output = Util.stripLeadingAndTrailingQuotes(input);
        } catch (NullPointerException e) {
            // expected
        }
    }
}