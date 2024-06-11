package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.*;

public class HelpFormatterTest {

    @Test
    public void testDefaultConstructor() {
        HelpFormatter formatter = new HelpFormatter();
        assertEquals(74, formatter.getDefaultWidth());
        assertEquals(1, formatter.getDefaultLeftPad());
        assertEquals(3, formatter.getDefaultDescPad());
        assertEquals("usage: ", formatter.getSyntaxPrefix());
        assertNotNull(formatter.getOptionComparator());
    }

    @Test
    public void testSettersAndGetters() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(50);
        formatter.setLeftPadding(3);
        formatter.setDescPadding(5);
        formatter.setSyntaxPrefix("help: ");
        formatter.setNewLine("\n");
        formatter.setOptPrefix("--");
        formatter.setLongOptPrefix("-");
        formatter.setArgName("parameter");

        assertEquals(50, formatter.getDefaultWidth());
        assertEquals(3, formatter.getDefaultLeftPad());
        assertEquals(5, formatter.getDefaultDescPad());
        assertEquals("help: ", formatter.getSyntaxPrefix());
        assertEquals("\n", formatter.getNewLine());
        assertEquals("--", formatter.getDefaultOptPrefix());
        assertEquals("-", formatter.getDefaultLongOptPrefix());
        assertEquals("parameter", formatter.getDefaultArgName());
    }

    @Test
    public void testPrintHelpWithDefaults() {
        String cmdLineSyntax = "java MyApp -a 10 -b 20";
        Options options = new Options();
        options.addOption("a", true, "An option with an argument");
        options.addOption("b", false, "An option without an argument");

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(cmdLineSyntax, options);

        // Check the output with a threshold limit (you can increase it for more detailed comparison)
        String expected = "usage: java MyApp [-a <An option with an argument>] [-b]\n"
                + "\noptions:\n"
                + "  -a, --An option with an argument\n"
                + "      An option with an argument\n"
                + "  -b, --An option without an argument\n"
                + "      An option without an argument\n";
        String actual = System.out.getBuffer().toString();
        assertTrue(expected.substring(0, 500).equals(actual.substring(0, 500)));
    }

    @Test
    public void testPrintHelpWithCustomValues() {
        String cmdLineSyntax = "java MyApp -p <profile> [--configuration <config>] [--logfile <logfile>]";
        Options options = new Options();
        options.addOption("p", true, "Profile name");
        options.addOption("c", true, "Configuration file");
        options.addOption("l", true, "Log file");

        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(100);
        formatter.setLeftPadding(5);
        formatter.setDescPadding(7);
        formatter.setSyntaxPrefix("usage: ");
        formatter.setNewLine("\n\n");
        formatter.setOptPrefix("-");
        formatter.setLongOptPrefix("--");
        formatter.setArgName("VALUE");

        formatter.printHelp(cmdLineSyntax, options);

        String expected = "usage: java MyApp [-p <Profile name>]\n"
                + "\noptions:\n"
                + "  -p, --Profile name\n"
                + "      Profile name\n"
                + "\n  --configuration <CONFIG>\n"
                + "      Configuration file\n"
                + "\n  --logfile <LOGFILE>\n"
                + "      Log file\n";

        String actual = System.out.getBuffer().toString();
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPrintHelpWithNullCmdLineSyntax() {
        Options options = new Options();
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(null, options);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPrintHelpWithNullOptions() {
        String cmdLineSyntax = "java MyApp -a 10 -b 20";
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(cmdLineSyntax, null);
    }
}