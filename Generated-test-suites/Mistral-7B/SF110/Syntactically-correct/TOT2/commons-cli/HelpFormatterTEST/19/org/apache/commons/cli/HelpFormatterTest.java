package org.apache.commons.cli.test;

import org.apache.commons.cli.HelpFormatter;
import org.junit.Test;
import static org.junit.Assert.*;

public class HelpFormatterTest {

    @Test
    public void testHelpFormatterConstructor() {
        HelpFormatter hf = new HelpFormatter();
        assertNotNull(hf);
    }

    // All the previous test cases

    @Test(expected = IllegalArgumentException.class)
    public void testHelpFormatterWithNegativeWidth() {
        HelpFormatter hf = new HelpFormatter();
        hf.setWidth(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHelpFormatterWithNullCmdLineSyntax() {
        HelpFormatter hf = new HelpFormatter();
        try {
            hf.printHelp(null, null);
        } catch (IllegalArgumentException e) {
            assertEquals("cmdLineSyntax not provided", e.getMessage());
        }
    }

    @Test
    public void testHelpFormatterWithEmptyCmdLineSyntax() {
        HelpFormatter hf = new HelpFormatter();
        hf.printHelp("", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHelpFormatterWithNullOptions() {
        HelpFormatter hf = new HelpFormatter();
        try {
            hf.printHelp(null, null);
        } catch (IllegalArgumentException e) {
            assertEquals("options not provided", e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHelpFormatterWithInvalidOptions() {
        HelpFormatter hf = new HelpFormatter();
        try {
            hf.printHelp("", new Object());
        } catch (IllegalArgumentException e) {
            assertEquals("options must be an Options object", e.getMessage());
        }
    }

    // Add test cases for other methods, edge cases, and exceptions here...

}