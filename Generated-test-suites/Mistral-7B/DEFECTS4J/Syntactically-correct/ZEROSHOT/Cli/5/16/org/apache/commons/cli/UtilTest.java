package org.apache.commons.cli.test;

import org.apache.commons.cli.Util;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for Util methods.
 */
public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String inputStr = "--test";
        String expectedOutput = "test";
        String outputStr = Util.stripLeadingHyphens(inputStr);
        assertEquals("Unexpected output of stripLeadingHyphens method for input --test.", expectedOutput, outputStr);

        inputStr = "test";
        outputStr = Util.stripLeadingHyphens(inputStr);
        assertEquals("Unexpected output of stripLeadingHyphens method for input test.", inputStr, outputStr);

        inputStr = "--test--";
        expectedOutput = "test";
        outputStr = Util.stripLeadingHyphens(inputStr);
        assertEquals("Unexpected output of stripLeadingHyphens method for input --test--.", expectedOutput, outputStr);

        inputStr = "-- test";
        expectedOutput = " test";
        outputStr = Util.stripLeadingHyphens(inputStr);
        assertEquals("Unexpected output of stripLeadingHyphens method for input -- test.", expectedOutput, outputStr);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        String inputStr = "\"test\"";
        String expectedOutput = "test";
        String outputStr = Util.stripLeadingAndTrailingQuotes(inputStr);
        assertEquals("Unexpected output of stripLeadingAndTrailingQuotes method for input \"test\".", expectedOutput, outputStr);

        inputStr = "test \"one two\"";
        expectedOutput = "test one two";
        outputStr = Util.stripLeadingAndTrailingQuotes(inputStr);
        assertEquals("Unexpected output of stripLeadingAndTrailingQuotes method for input test \"one two\".", expectedOutput, outputStr);

        inputStr = "test \"one two\"";
        expectedOutput = "test one two";
        outputStr = Util.stripLeadingAndTrailingQuotes(inputStr);
        assertEquals("Unexpected output of stripLeadingAndTrailingQuotes method for input test \"one two\".", expectedOutput, outputStr);

        inputStr = "test\"one two";
        expectedOutput = "test one two";
        outputStr = Util.stripLeadingAndTrailingQuotes(inputStr);
        assertEquals("Unexpected output of stripLeadingAndTrailingQuotes method for input test\"one two\".", expectedOutput, outputStr);

        inputStr = "test\"one two\"";
        expectedOutput = "test one two";
        outputStr = Util.stripLeadingAndTrailingQuotes(inputStr);
        assertEquals("Unexpected output of stripLeadingAndTrailingQuotes method for input test\"one two\".", expectedOutput, outputStr);
    }
}