package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for the Parser class.
 */
public class ParserTest
{
    /**
     * Test parse method with simple options.
     */
    @Test
    public void testSimpleOptions() throws ParseException
    {
        // create the command line options
        Options options = new Options();
        options.addRequiredOption("a", "input", true, "The input file.");
        options.addOption("b", false, "Show verbose output.");

        // create the parser and parse the command line arguments
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(new String[]{"-a", "test.txt", "-b"}, options);

        // check the results
        Assert.assertEquals("test.txt", cmd.getOptionValue("input"));
        Assert.assertFalse(cmd.hasOption("b"));

        // print the help message
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("Test", options);
    }

    /**
     * Test parse method with grouped options.
     */
    @Test
    public void testGroupedOptions() throws ParseException
    {
        // create the command line options
        Options options = new Options();
        OptionGroup group = new OptionGroup("network", "Network options:");
        group.addOption("h", "host", true, "The network host.");
        group.addOption("p", "port", true, "The network port.");
        options.addOptionGroup(group);
        options.addRequiredOption("i", "input", true, "The input file.");

        // create the parser and parse the command line arguments
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(new String[]{"-i", "test.txt", "-h", "localhost", "-p", "8080"}, options);

        // check the results
        Assert.assertEquals("test.txt", cmd.getOptionValue("input"));
        Assert.assertEquals("localhost", cmd.getOptionValue("host"));
        Assert.assertEquals("8080", cmd.getOptionValue("port"));

        // print the help message
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("Test", options);
    }

    /**
     * Test parse method with multiple values for an option.
     */
    @Test
    public void testMultipleValuesForOption() throws ParseException
    {
        // create the command line options
        Options options = new Options();
        options.addOption("f", "file", true, "The input files.", 1);

        // create the parser and parse the command line arguments
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(new String[]{"-f", "test1.txt", "-f", "test2.txt"}, options);

        // check the results
        Assert.assertArrayEquals(new String[]{"test1.txt", "test2.txt"}, cmd.getOptionValues("file"));

        // print the help message
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("Test", options);
    }

    /**
     * Test parse method with required options.
     */
    @Test(expected = MissingOptionException.class)
    public void testMissingRequiredOption() throws ParseException
    {
        // create the command line options
        Options options = new Options();
        options.addRequiredOption("a", "input", true, "The input file.");

        // create the parser and parse the command line arguments
        CommandLineParser parser = new DefaultParser();
        parser.parse(new String[]{}, options);

        // this should throw a MissingOptionException
    }

    /**
     * Test parse method with incorrect option order.
     */
    @Test(expected = ParseException.class)
    public void testIncorrectOptionOrder() throws ParseException
    {
        // create the command line options
        Options options = new Options();
        options.addOption("a", "input", true, "The input file.");
        options.addOption("b", "output", true, "The output file.");

        // create the parser and parse the command line arguments
        CommandLineParser parser = new DefaultParser();
        parser.parse(new String[]{"-b", "test.txt", "-a", "input.txt"}, options);

        // this should throw a ParseException
    }

    /**
     * Test parse method with unrecognized option.
     */
    @Test(expected = UnrecognizedOptionException.class)
    public void testUnrecognizedOption() throws ParseException
    {
        // create the command line options
        Options options = new Options();
        options.addOption("a", "input", true, "The input file.");

        // create the parser and parse the command line arguments
        CommandLineParser parser = new DefaultParser();
        parser.parse(new String[]{"-x", "test.txt"}, options);

        // this should throw an UnrecognizedOptionException
    }
}