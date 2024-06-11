package org.apache.commons.cli.test;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class HelpFormatterTest {

    private HelpFormatter formatter;
    private Options options;

    @Before
    public void setUp() {
        formatter = new HelpFormatter();
        options = new Options();
    }

    @Test
    public void testPrintHelpWithEmptyOptions() {
        final String cmdLineSyntax = "java MyClass";
        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelpWithNormalOptions() {
        final String cmdLineSyntax = "java MyClass [options]";

        options.addOption("a", "alias", true, "This is an alias option");
        options.addOption("b", "boolean", false, "This is a boolean option");
        options.addOption("c", "count", true, "This is a count option");

        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelpWithRequiredOptions() {
        final String cmdLineSyntax = "java MyClass [options]";

        options.addOption("a", true, "This is a required option");

        try {
            formatter.printHelp(cmdLineSyntax, options);
            fail("Expected PrintHelpException");
        } catch (PrintHelpException e) {
            // Expected
        }

        final PrintWriter printWriter = new PrintWriter(System.out);
        final String header = "Options:";
        formatter.printHelp(printWriter, 80, cmdLineSyntax, header, options);
    }

    @Test
    public void testPrintHelpWithOptionGroups() {
        final String cmdLineSyntax = "java MyClass [options]";

        final OptionGroup group = new OptionGroup("network", false, "Networking related options");
        group.addOption("h", "host", true, "The host to connect to");
        group.addOption("p", "port", true, "The port to connect to");
        options.addOptionGroup(group);

        formatter.printHelp(cmdLineSyntax, options);
    }

    @Test
    public void testPrintHelpWithInvalidOptions() {
        final String cmdLineSyntax = "java MyClass [options]";

        options.addOption("a", "invalidOption", 1, "This is an invalid option");

        try {
            formatter.printHelp(cmdLineSyntax, options);
            fail("Expected PrintHelpException");
        } catch (PrintHelpException e) {
            // Expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPrintHelpWithNullCmdLineSyntax() {
        formatter.printHelp(null, options);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPrintHelpWithNullOptions() {
        formatter.printHelp("java MyClass [options]", null);
    }
}