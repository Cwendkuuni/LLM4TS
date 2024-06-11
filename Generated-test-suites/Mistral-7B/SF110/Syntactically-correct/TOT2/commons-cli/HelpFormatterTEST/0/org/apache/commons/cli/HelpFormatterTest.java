package org.junit.test;

import org.apache.commons.cli.HelpFormatter;
import org.junit.Test;
import static org.junit.Assert.*;

public class HelpFormatterTest {

    @Test
    public void testGetWidth() {
        HelpFormatter formatter = new HelpFormatter();
        int width = formatter.getWidth();
        assertEquals(74, width);
    }

    @Test
    public void testSetWidth() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(56);
        int width = formatter.getWidth();
        assertEquals(56, width);
    }

    @Test
    public void testNegativeWidth() {
        HelpFormatter formatter = new HelpFormatter();
        try {
            formatter.setWidth(-1);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Test passed
        }
    }

    // Test other getter and setter methods with similar edge cases

    // Test other methods (printHelp, printUsage, etc.) with various inputs

    @Test
    public void testPrintHelpWithNullOptions() {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "java MyClass";
        formatter.printHelp(cmdLineSyntax, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPrintHelpWithNullCmdLineSyntax() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(null, null);
    }

    // Test other printHelp methods with various inputs

}