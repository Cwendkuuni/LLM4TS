package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;

import java.util.Arrays;

public class HelpFormatterTest {

    @org.junit.Before
    public void setUp() {
        HelpFormatter formatter = new HelpFormatter();
    }

    @org.junit.Test
    public void testDefaultInitialization() {
        HelpFormatter formatter = new HelpFormatter();
        assertEquals(74, formatter.getWidth());
        assertEquals(1, formatter.getLeftPadding());
        assertEquals(3, formatter.getDescPadding());
        assertEquals("usage: ", formatter.getSyntaxPrefix());
        assertEquals(System.getProperty("line.separator"), formatter.getNewLine());
        assertEquals("-", formatter.getOptPrefix());
        assertEquals("--", formatter.getLongOptPrefix());
        assertEquals("arg", formatter.getArgName());
    }

    @org.junit.Test
    public void testSettersAndGetters() {
        HelpFormatter formatter = new HelpFormatter();

        formatter.setWidth(50);
        assertEquals(50, formatter.getWidth());

        formatter.setLeftPadding(5);
        assertEquals(5, formatter.getLeftPadding());

        formatter.setDescPadding(7);
        assertEquals(7, formatter.getDescPadding());

        formatter.setSyntaxPrefix("usage: myProgram ");
        assertEquals("usage: myProgram ", formatter.getSyntaxPrefix());

        formatter.setNewLine("\nThis is a new line\n");
        assertEquals("\nThis is a new line\n", formatter.getNewLine());

        formatter.setOptPrefix("--");
        assertEquals("--", formatter.getOptPrefix());

        formatter.setLongOptPrefix("--long-");
        assertEquals("--long-", formatter.getLongOptPrefix());

        formatter.setArgName("param");
        assertEquals("param", formatter.getArgName());
    }

    @org.junit.Test
    public void testPrintHelp_NoOptions() {
        HelpFormatter formatter = new HelpFormatter();
        String help = formatter.printHelp("myCommand", null);
        assertTrue(help.contains("myCommand"));
        assertTrue(help.contains("Usage:"));
        assertTrue(help.contains("Options:"));
    }

    @org.junit.Test
    public void testPrintHelp_Options() {
        Options options = new Options();
        Option option1 = new Option("o", "output", true, "output file");
        Option option2 = new Option("h", "help", false, "Print this help message.");
        Option option3 = new Option("v", "verbose", false, "Print verbose output.");
        Option option4 = new Option("i", "input", true, "input file");

        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);
        options.addOption(option4);
        options.addOptionGroup(new OptionGroup("g", "group").addOption(new Option("a", "append", false, "Append to existing file.")));

        HelpFormatter formatter = new HelpFormatter();
        String help = formatter.printHelp("myCommand", options);

        assertTrue(help.contains("myCommand"));
        assertTrue(help.contains("Usage:"));
        assertTrue(help.contains("Options:"));
        assertTrue(help.contains("-o, --output <output file>"));
        assertTrue(help.contains("-h, --help"));
        assertTrue(help.contains("-v, --verbose"));
        assertTrue(help.contains("-i, --input <input file>"));
        assertTrue(help.contains("--group"));
        assertTrue(help.contains("--group group"));
        assertTrue(help.contains("--group:group"));
        assertTrue(help.contains("-a, --append"));
    }

    @org.junit.Test
    public void testPrintHelp_AutoUsage() {
        Options options = new Options();
        Option option1 = new Option("o", "output", true, "output file");
        Option option2 = new Option("h", "help", false, "Print this help message.");
        Option option3 = new Option("v", "verbose", false, "Print verbose output.");
        Option option4 = new Option("i", "input", true, "input file");

        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);
        options.addOption(option4);
        options.addOptionGroup(new OptionGroup("g", "group").addOption(new Option("a", "append", false, "Append to existing file.")));

        HelpFormatter formatter = new HelpFormatter();
        String help = formatter.printHelp("myCommand", options, true);

        assertTrue(help.contains("myCommand"));
        assertTrue(help.contains("Usage:"));
        assertTrue(help.contains("Options:"));
        assertTrue(help.contains("-o, --output <output file>"));
        assertTrue(help.contains("-h, --help"));
        assertTrue(help.contains("-v, --verbose"));
        assertTrue(help.contains("-i, --input <input file>"));
        assertTrue(help.contains("--group"));
        assertTrue(help.contains("--group group"));
        assertTrue(help.contains("--group:group"));
        assertTrue(help.contains("-a, --append"));
    }

    @org.junit.Test
    public void testPrintHelp_CustomWidth() {
        Options options = new Options();
        Option option1 = new Option("o", "output", true, "output file");
        Option option2 = new Option("h", "help", false, "Print this help message.");
        Option option3 = new Option("v", "verbose", false, "Print verbose output.");
        Option option4 = new Option("i", "input", true, "input file");

        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);
        options.addOption(option4);
        options.addOptionGroup(new OptionGroup("g", "group").addOption(new Option("a", "append", false, "Append to existing file.")));

        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(50);
        String help = formatter.printHelp("myCommand", options);

        assertTrue(help.length() < 100);
        assertTrue(help.contains("myCommand"));
        assertTrue(help.contains("Usage:"));
        assertTrue(help.contains("Options:"));
        assertTrue(help.contains("-o, --output <output file>"));
        assertTrue(help.contains("-h, --help"));
        assertTrue(help.contains("-v, --verbose"));
        assertTrue(help.contains("-i, --input <input file>"));
        assertTrue(help.contains("--group"));
        assertTrue(help.contains("--group group"));
        assertTrue(help.contains("--group:group"));
        assertTrue(help.contains("-a, --append"));
    }

    @org.junit.Test
    public void testPrintHelp_CustomHeaderAndFooter() {
        Options options = new Options();
        Option option1 = new Option("o", "output", true, "output file");
        Option option2 = new Option("h", "help", false, "Print this help message.");
        Option option3 = new Option("v", "verbose", false, "Print verbose output.");
        Option option4 = new Option("i", "input", true, "input file");

        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);
        options.addOption(option4);
        options.addOptionGroup(new OptionGroup("g", "group").addOption(new Option("a", "append", false, "Append to existing file.")));

        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(50);
        formatter.setSyntaxPrefix("myCommand: ");
        formatter.setLeftPadding(2);
        formatter.setDescPadding(5);
        String header = "Custom Header";
        String footer = "Custom Footer";
        String help = formatter.printHelp("myCommand", options, null, header, footer);

        assertTrue(help.length() > 100);
        assertTrue(help.contains("myCommand: "));
        assertTrue(help.contains("Custom Header"));
        assertTrue(help.contains("Usage:"));
        assertTrue(help.contains("Options:"));
        assertTrue(help.contains("Custom Footer"));
        assertTrue(help.contains("-o, --output <output file>"));
        assertTrue(help.contains("-h, --help"));
        assertTrue(help.contains("-v, --verbose"));
        assertTrue(help.contains("-i, --input <input file>"));
        assertTrue(help.contains("--group"));
        assertTrue(help.contains("--group group"));
        assertTrue(help.contains("--group:group"));
        assertTrue(help.contains("-a, --append"));
    }

    @org.junit.Test
    public void testPrintHelp_PrintWrappedText() {
        Options options = new Options();
        Option option1 = new Option("o", "output", true, "output file");
        Option option2 = new Option("h", "help", false, "Print this help message.");
        Option option3 = new Option("v", "verbose", false, "Print verbose output.");
        Option option4 = new Option("i", "input", true, "input file");

        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);
        options.addOption(option4);
        options.addOptionGroup(new OptionGroup("g", "group").addOption(new Option("a", "append", false, "Append to existing file.")));

        HelpFormatter formatter = new HelpFormatter();
        String longOptionDescription = "This is a very long option description that will be wrapped automatically.";
        Option longOption = new Option("w", "wrap", false, longOptionDescription);
        options.addOption(longOption);

        String help = formatter.printHelp("myCommand", options);
        assertTrue(help.contains("myCommand"));
        assertTrue(help.contains("Usage:"));
        assertTrue(help.contains("Options:"));
        assertTrue(help.contains("-o, --output <output file>"));
        assertTrue(help.contains("-h, --help"));
        assertTrue(help.contains("-v, --verbose"));
        assertTrue(help.contains("-i, --input <input file>"));
        assertTrue(help.contains("--group"));
        assertTrue(help.contains("--group group"));
        assertTrue(help.contains("--group:group"));
        assertTrue(help.contains("-a, --append"));
        assertTrue(help.contains("-w, --wrap"));
        assertTrue(help.contains("--wrap"));
        assertTrue(help.contains("This is a very long option description that will be wrapped automatically."));
    }

    @org.junit.Test
    public void testParseOptions() {
        String[] args = {"myCommand", "-o", "output.txt", "--help", "-v"};
        Options options = new Options();
        Option option1 = new Option("o", "output", true, "output file");
        Option option2 = new Option("h", "help", false, "Print this help message.");
        Option option3 = new Option("v", "verbose", false, "Print verbose output.");
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmdLine = parser.parse(options, args);
            assertEquals("output.txt", cmdLine.getOptionValue("output"));
            assertFalse(cmdLine.hasOption("help"));
            assertTrue(cmdLine.hasOption("verbose"));
        } catch (ParseException e) {
            fail("Parsing failed: " + e.getMessage());
        }
    }
}