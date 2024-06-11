package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test class for the Options class.
 */
public class OptionsTest
{
    /**
     * Test the creation of a simple Options instance with no options.
     */
    @Test
    public void testEmptyOptions()
    {
        Options options = new Options();

        assertTrue(options.getOptions().isEmpty());
        assertTrue(options.getRequiredOptions().isEmpty());
        assertEquals(0, options.size());
    }

    /**
     * Test the creation of an Options instance with a single option.
     */
    @Test
    public void testSingleOption()
    {
        Options options = new Options();
        Option option = new Option("i", "inputFile", true, "File to input data from.");
        options.addOption(option);

        assertEquals(1, options.size());
        assertEquals(1, options.getOptions().size());
        assertTrue(options.hasOption("i"));
        assertTrue(options.hasOption("inputFile"));
        assertFalse(options.hasOption("o"));
        assertFalse(options.hasLongOption("inputFile"));
        assertFalse(options.hasLongOption("outputFile"));
        assertNull(options.getOption("o"));
        assertEquals(option, options.getOption("i"));
        assertEquals(option, options.getOption("inputFile"));
    }

    /**
     * Test the creation of an Options instance with multiple options, including an OptionGroup.
     */
    @Test
    public void testMultipleOptions()
    {
        Options options = new Options();

        Option inputOption = new Option("i", "inputFile", true, "File to input data from.");
        Option outputOption = new Option("o", "outputFile", true, "File to output data to.");
        Option groupOption = new OptionGroup("g", "MyGroup", "Options related to my group.");

        Option groupInputOption = new Option("gi", "groupInputFile", true, "File for group input.");
        Option groupOutputOption = new Option("go", "groupOutputFile", true, "File for group output.");
        groupOption.addOption(groupInputOption);
        groupOption.addOption(groupOutputOption);

        options.addOption(inputOption);
        options.addOption(outputOption);
        options.addOptionGroup(groupOption);

        assertEquals(4, options.size());
        assertEquals(3, options.getOptions().size());
        assertTrue(options.hasOption("i"));
        assertTrue(options.hasOption("inputFile"));
        assertTrue(options.hasOption("o"));
        assertTrue(options.hasOption("outputFile"));
        assertFalse(options.hasOption("g"));
        assertFalse(options.hasOption("groupInputFile"));
        assertFalse(options.hasOption("groupOutputFile"));
        assertNull(options.getOption("g"));
        assertEquals(inputOption, options.getOption("i"));
        assertEquals(inputOption, options.getOption("inputFile"));
        assertEquals(outputOption, options.getOption("o"));
        assertEquals(outputOption, options.getOption("outputFile"));
        assertNull(options.getOption("g"));
        assertEquals(groupOption, options.getOptionGroup("g"));
        assertEquals(groupInputOption, groupOption.getOptions().get(0));
        assertEquals(groupOutputOption, groupOption.getOptions().get(1));
    }

    /**
     * Test parsing an invalid command line with no options.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCommandLineNoOptions()
    {
        Options options = new Options();
        CommandLineParser parser = new DefaultParser();
        String[] args = {};

        parser.parse(options, args);
    }

    /**
     * Test parsing a command line with a single valid option.
     */
    @Test
    public void testValidCommandLineSingleOption()
    {
        Options options = new Options();
        Option inputOption = new Option("i", "inputFile", true, "File to input data from.");
        options.addOption(inputOption);
        CommandLineParser parser = new DefaultParser();
        String[] args = {"--inputFile", "input.txt"};

        CommandLine cmd = parser.parse(options, args);
        assertEquals("input.txt", cmd.getOptionValue("inputFile"));
        assertNull(cmd.getOptionValue("outputFile"));
    }

    /**
     * Test parsing a command line with multiple valid options.
     */
    @Test
    public void testValidCommandLineMultipleOptions()
    {
        Options options = new Options();
        Option inputOption = new Option("i", "inputFile", true, "File to input data from.");
        Option outputOption = new Option("o", "outputFile", true, "File to output data to.");
        options.addOption(inputOption);
        options.addOption(outputOption);
        CommandLineParser parser = new DefaultParser();
        String[] args = {"--inputFile", "input.txt", "--outputFile", "output.txt"};

        CommandLine cmd = parser.parse(options, args);
        assertEquals("input.txt", cmd.getOptionValue("inputFile"));
        assertEquals("output.txt", cmd.getOptionValue("outputFile"));
    }

    /**
     * Test parsing a command line with an invalid option.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCommandLineInvalidOption()
    {
        Options options = new Options();
        Option inputOption = new Option("i", "inputFile", true, "File to input data from.");
        options.addOption(inputOption);
        CommandLineParser parser = new DefaultParser();
        String[] args = {"--badOption"};

        parser.parse(options, args);
    }

    /**
     * Test parsing a command line with no options specified.
     */
    @Test
    public void testValidCommandLineNoOptions()
    {
        Options options = new Options();
        CommandLineParser parser = new DefaultParser();
        String[] args = {};

        CommandLine cmd = parser.parse(options, args);
        assertTrue(cmd.getArgs().isEmpty());
    }

    /**
     * Test printing help.
     */
    @Test
    public void testHelp()
    {
        Options options = new Options();
        Option inputOption = new Option("i", "inputFile", true, "File to input data from.");
        Option outputOption = new Option("o", "outputFile", true, "File to output data to.");
        options.addOption(inputOption);
        options.addOption(outputOption);

        HelpFormatter formatter = new DefaultHelpFormatter();
        String help = formatter.printHelp("MyApp", options);

        assertTrue(help.contains("MyApp"));
        assertTrue(help.contains("inputFile"));
        assertTrue(help.contains("outputFile"));
        assertTrue(help.contains("--inputFile"));
        assertTrue(help.contains("--outputFile"));
    }
}