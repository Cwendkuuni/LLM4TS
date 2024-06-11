package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.*;

public class HelpFormatterTest {

    @Test
    public void testHelpFormatterDefaultConstruction() {
        HelpFormatter formatter = new HelpFormatter();
        assertEquals(74, formatter.getWidth());
        assertEquals(1, formatter.getLeftPadding());
        assertEquals(3, formatter.getDescPadding());
        assertEquals("usage: ", formatter.getSyntaxPrefix());
        assertNotNull(formatter.getOptionComparator());
    }

    @Test
    public void testHelpFormatterCustomWidth() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(50);
        assertEquals(50, formatter.getWidth());
    }

    @Test
    public void testHelpFormatterCustomLeftPadding() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setLeftPadding(3);
        assertEquals(3, formatter.getLeftPadding());
    }

    @Test
    public void testHelpFormatterCustomDescPadding() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setDescPadding(5);
        assertEquals(5, formatter.getDescPadding());
    }

    @Test
    public void testHelpFormatterCustomSyntaxPrefix() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setSyntaxPrefix("help: ");
        assertEquals("help: ", formatter.getSyntaxPrefix());
    }

    @Test
    public void testHelpFormatterCustomNewLine() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setNewLine("\r\n");
        assertEquals("\r\n", formatter.getNewLine());
    }

    @Test
    public void testHelpFormatterCustomOptPrefix() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setOptPrefix("/");
        assertEquals("/", formatter.getOptPrefix());
    }

    @Test
    public void testHelpFormatterCustomLongOptPrefix() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setLongOptPrefix("--");
        assertEquals("--", formatter.getLongOptPrefix());
    }

    @Test
    public void testHelpFormatterCustomArgName() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setArgName("param");
        assertEquals("param", formatter.getArgName());
    }

    @Test
    public void testPrintHelpWithNullCmdLineSyntaxAndNullOptions() {
        HelpFormatter formatter = new HelpFormatter();
        try {
            formatter.printHelp(null, null);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("cmdLineSyntax not provided", e.getMessage());
        }
    }

    @Test
    public void testPrintHelpWithNullCmdLineSyntaxAndValidOptions() {
        HelpFormatter formatter = new HelpFormatter();
        Options options = new Options();
        try {
            formatter.printHelp(null, options);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("cmdLineSyntax not provided", e.getMessage());
        }
    }

    @Test
    public void testPrintHelpWithValidCmdLineSyntaxAndNullOptions() {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "java MyClass [arg1] [arg2]";
        try {
            formatter.printHelp(cmdLineSyntax, null);
            fail("Should have printed usage information");
        } catch (Exception e) {
            // Expected exception, just check if PrintWriter was used
            assertNotNull(formatter.getDefaultPrintWriter());
        }
    }

    @Test
    public void testPrintHelpWithValidCmdLineSyntaxAndValidOptions() {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "java MyClass [arg1] [arg2]";
        Options options = new Options();
        options.addOption("a", false, "Short option example");
        options.addOption("b", "bval", true, "Long option example");
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelpWithValidCmdLineSyntaxAndValidOptionsWithHeaderAndFooter() {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "java MyClass [arg1] [arg2]";
        Options options = new Options();
        options.addOption("a", false, "Short option example");
        options.addOption("b", "bval", true, "Long option example");
        formatter.printHelp(cmdLineSyntax, options, "Header\n", "Footer\n");
    }

    @Test
    public void testPrintHelpWithValidCmdLineSyntaxAndValidOptionsWithAutoUsage() {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "java MyClass [arg1] [arg2]";
        Options options = new Options();
        options.addOption("a", false, "Short option example");
        options.addOption("b", "bval", true, "Long option example");
        formatter.printHelp(cmdLineSyntax, options, true);
    }

    @Test
    public void testPrintHelpWithInvalidCmdLineSyntax() {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "java MyClass [arg1] [arg2] arg3";
        Options options = new Options();
        try {
            formatter.printHelp(cmdLineSyntax, options);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid cmdLineSyntax: java MyClass [arg1] [arg2] arg3", e.getMessage());
        }
    }
}