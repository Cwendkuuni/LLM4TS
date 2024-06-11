package org.apache.commons.cli.test;

import org.apache.commons.cli.*;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test cases for Options class from Apache Commons CLI library.
 */
public class OptionsTest {

    @Test
    public void testAddOptions() {
        Options options = new Options();

        // Test adding a single option with short name and no argument
        Option option1 = new Option("i", "inputFile", false, "Path to input file");
        options.addOption(option1);

        // Test adding a single option with long name and no argument
        Option option2 = new Option("input", false, "Path to input file");
        options.addOption(option2);

        // Test adding an option with short name and argument
        Option option3 = new Option("o", "outputFile", true, "Path to output file");
        options.addOption(option3);

        // Test adding an option with long name and argument
        Option option4 = new Option("output", "file", true, "Path to output file");
        options.addOption(option4);

        // Test adding an option group
        OptionGroup optionGroup = new OptionGroup("group", "My Option Group");
        optionGroup.addOption(new Option("g", "group", false, "Join the group"));
        options.addOptionGroup(optionGroup);

        // Test adding multiple options with the same long name but different short names
        Option option5 = new Option("h", "help", false, "Print this help message");
        Option option6 = new Option("h", "header", false, "Print header");
        options.addOption(option5);
        options.addOption(option6);

        // Test adding an option that is already added
        Option option7 = new Option("i", "inputFile", false, "Path to input file");
        options.addOption(option7);

        // Test adding a required option
        Option option8 = new Option("r", "required", true, "Required option");
        options.addOption(option8);

        // Test checking for the presence of an option
        assertTrue(options.hasOption("i"));
        assertTrue(options.hasOption("input"));
        assertTrue(options.hasOption("o"));
        assertTrue(options.hasOption("output"));
        assertTrue(options.hasOption("g"));
        assertTrue(options.hasOption("h"));
        assertTrue(options.hasOption("help"));
        assertFalse(options.hasOption("nonexistent"));

        // Test retrieving an option
        Option option1Retrieved = options.getOption("i");
        assertEquals("Path to input file", option1Retrieved.getLongOpt());
        assertEquals(false, option1Retrieved.hasArg());

        // Test getting options group
        OptionGroup optionGroupRetrieved = options.getOptionGroup("group");
        assertEquals("My Option Group", optionGroupRetrieved.getLongOpt());

        // Test get required options
        List<Option> requiredOptions = options.getRequiredOptions();
        assertEquals(1, requiredOptions.size());
        assertEquals(option8, requiredOptions.get(0));

        // Test parsing options from command line
        String[] cmdLineArgs = {"-i", "input.txt", "-o", "output.txt", "--help"};
        CommandLineParser parser = new DefaultParser();
        CommandLine parsedCommandLine = parser.parse(options, cmdLineArgs);
        assertEquals("input.txt", parsedCommandLine.getOptionValue("inputFile"));
        assertEquals("output.txt", parsedCommandLine.getOptionValue("outputFile"));
        assertTrue(parsedCommandLine.hasOption("help"));

        // Test parsing options from command line with group
        OptionGroup optionGroup = new OptionGroup("group", "My Option Group");
        optionGroup.addOption(new Option("g", "group", false, "Join the group"));
        options.addOptionGroup(optionGroup);
        String[] cmdLineArgsWithGroup = {"-g", "-i", "input.txt", "-o", "output.txt", "--help"};
        CommandLine parsedCommandLineWithGroup = parser.parse(options, cmdLineArgsWithGroup);
        assertTrue(parsedCommandLineWithGroup.isSet("group"));
    }
}