package org.apache.commons.cli;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class HelpFormatterTest {

    private HelpFormatter formatter;

    @Before
    public void setUp() {
        formatter = new HelpFormatter();
    }

    

    // Test constructor
    @Test
    public void testHelpFormatterConstructor() {
        HelpFormatter formatter = new HelpFormatter();
        assertNotNull(formatter);
    }

    // Test getters and setters
    @Test
    public void testGettersAndSetters() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(100);
        assertEquals(100, formatter.getWidth());
    }

    // Test printHelp methods with default arguments
    @Test
    public void testPrintHelpWithDefaultArguments() {
        final String cmdLineSyntax = "java MyApp";
        final Options options = new Options();
        formatter.printHelp(cmdLineSyntax, options);
    }

    // Test printHelp methods with custom arguments
    @Test
    public void testPrintHelpWithCustomArguments() {
        final String cmdLineSyntax = "java MyApp arg1 arg2";
        final Options options = new Options();
        formatter.printHelp(cmdLineSyntax, options, true, 50, "Custom header", "Custom footer");
    }

    // Test printUsage methods with default arguments
    @Test
    public void testPrintUsageWithDefaultArguments() {
        final PrintWriter pw = new PrintWriter(System.out);
        final String cmdLineSyntax = "java MyApp";
        final Options options = new Options();
        formatter.printUsage(pw, 50, cmdLineSyntax, options);
    }

    // Test printUsage methods with custom arguments
    @Test
    public void testPrintUsageWithCustomArguments() {
        final PrintWriter pw = new PrintWriter(System.out);
        final String cmdLineSyntax = "java MyApp arg1 arg2";
        final Options options = new Options();
        formatter.printUsage(pw, 50, cmdLineSyntax, options, "Custom header", "Custom footer");
    }

    // Test printOptions method
    @Test
    public void testPrintOptions() {
        final Options options = new Options();
        formatter.printOptions(System.out, 50, options, 5, 10);
    }

    // Test printWrapped method
    @Test
    public void testPrintWrapped() {
        final String text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.";
        assertEquals(text, formatter.printWrapped(System.out, 50, text));
    }

    // Test findWrapPos method
    @Test
    public void testFindWrapPos() {
        assertEquals(-1, formatter.findWrapPos("", 10));
        assertEquals(-1, formatter.findWrapPos("Lorem ipsum", 10));
        assertEquals(11, formatter.findWrapPos("Lorem ipsum dolor sit amet", 10));
        assertEquals(13, formatter.findWrapPos("Lorem ipsum dolor sit amet consectetur adipiscing elit", 10));
    }

    // Test createPadding method
    @Test
    public void testCreatePadding() {
        assertEquals("", formatter.createPadding(0));
        assertEquals("   ", formatter.createPadding(3));
        assertEquals("----------", formatter.createPadding(8));
    }

    // Test rtrim method
    @Test
    public void testRtrim() {
        assertEquals("", formatter.rtrim(""));
        assertEquals("Hello", formatter.rtrim("Hello    "));
    }

    // Test OptionComparator class
    @Test
    public void testOptionComparator() {
        final Options options = new Options();
        options.addOption("opt1", false, "o1", "opt1");
        options.addOption("opt2", true, "o2", "opt2");
        assertEquals(0, formatter.getOptionComparator().compare(options.findOption("opt1"), options.findOption("opt2")));
        assertEquals(-1, formatter.getOptionComparator().compare(options.findOption("opt2"), options.findOption("opt1")));
    }

    

    // Edge cases and exception handling tests

    // Test printHelp method with null cmdLineSyntax or options
    @Test(expected = IllegalArgumentException.class)
    public void testPrintHelpWithNullArguments() {
        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(null, null);
    }

    // Test printHelp method with empty cmdLineSyntax or options
    @Test
    public void testPrintHelpWithEmptyArguments() {
        final String cmdLineSyntax = "";
        final Options options = new Options();
        final PrintWriter pw = new PrintWriter(System.out);
        formatter.printHelp(pw, 50, cmdLineSyntax, options);
    }

    // Test printHelp method with null header or footer
    @Test
    public void testPrintHelpWithNullHeaderOrFooter() {
        final String cmdLineSyntax = "java MyApp arg1 arg2";
        final Options options = new Options();
        final PrintWriter pw = new PrintWriter(System.out);
        formatter.printHelp(pw, 50, cmdLineSyntax, options, null, true);
        formatter.printHelp(pw, 50, cmdLineSyntax, options, "Custom header", null);
    }

    // Test printUsage method with null pw or cmdLineSyntax
    @Test(expected = IllegalArgumentException.class)
    public void testPrintUsageWithNullArguments() {
        final HelpFormatter formatter = new HelpFormatter();
        formatter.printUsage(null, 50, null, null);
    }

    // Test printUsage method with empty cmdLineSyntax
    @Test
    public void testPrintUsageWithEmptyCmdLineSyntax() {
        final PrintWriter pw = new PrintWriter(System.out);
        final String cmdLineSyntax = "";
        formatter.printUsage(pw, 50, cmdLineSyntax);
    }

    // Test printUsage method with null header or footer
    @Test
    public void testPrintUsageWithNullHeaderOrFooter() {
        final PrintWriter pw = new PrintWriter(System.out);
        final String cmdLineSyntax = "java MyApp arg1 arg2";
        formatter.printUsage(pw, 50, cmdLineSyntax, null, "Custom header", null);
    }

    // Test printOptions method with null pw or options
    @Test(expected = IllegalArgumentException.class)
    public void testPrintOptionsWithNullArguments() {
        final PrintWriter pw = null;
        final Options options = new Options();
        formatter.printOptions(pw, 50, options, 5, 10);
    }

    // Test printOptions method with empty options
    @Test
    public void testPrintOptionsWithEmptyOptions() {
        final PrintWriter pw = new PrintWriter(System.out);
        final Options options = new Options();
        formatter.printOptions(pw, 50, options, 5, 10);
    }

    // Test printWrapped method with null text or pw
    @Test(expected = IllegalArgumentException.class)
    public void testPrintWrappedWithNullArguments() {
        final PrintWriter pw = null;
        formatter.printWrapped(pw, 50, null);
    }

    // Test printWrapped method with empty text
    @Test
    public void testPrintWrappedWithEmptyText() {
        final String text = "";
        final PrintWriter pw = new PrintWriter(System.out);
        formatter.printWrapped(pw, 50, text);
    }
}