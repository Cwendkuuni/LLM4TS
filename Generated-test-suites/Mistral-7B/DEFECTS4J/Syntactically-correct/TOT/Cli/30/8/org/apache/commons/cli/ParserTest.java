package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.NoSuchOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for the Parser class.
 */
public class ParserTest
{
    /**
     * Tests the parsing of command line arguments with required options.
     */
    @Test
    public void testParsingWithRequiredOptions() throws Exception
    {
        // create test options
        Options options = new Options();
        options.addRequiredOption("i", "input", true, "input file");
        options.addRequiredOption("o", "output", true, "output file");

        // create test parser
        Parser parser = new DefaultParser();
        parser.setOptions(options);

        // test parsing with valid arguments
        String[] args = new String[]{"-i", "/path/to/input.txt", "-o", "/path/to/output.txt"};
        CommandLine cmd = parser.parse(options, args, null);
        Assert.assertEquals("/path/to/input.txt", cmd.getOptionValue("input"));
        Assert.assertEquals("/path/to/output.txt", cmd.getOptionValue("output"));

        // test parsing with missing required options
        args = new String[]{"-o", "/path/to/output.txt"};
        try
        {
            parser.parse(options, args, null);
            Assert.fail("MissingRequiredOptionException expected.");
        }
        catch (MissingOptionException e)
        {
            // expected
            Assert.assertEquals("The following required options do not have a value, are missing or invalid:\ninput\n", e.getMessage());
        }

        // test parsing with unrecognized option
        args = new String[]{"--unknown"};
        try
        {
            parser.parse(options, args, null);
            Assert.fail("UnrecognizedOptionException expected.");
        }
        catch (UnrecognizedOptionException e)
        {
            // expected
            Assert.assertEquals("Invalid option: unknown\n", e.getMessage());
        }
    }

    /**
     * Tests the parsing of command line arguments with optional options.
     */
    @Test
    public void testParsingWithOptionalOptions() throws Exception
    {
        // create test options
        Options options = new Options();
        options.addOption("i", "input", false, "input file");
        options.addOption("o", "output", true, "output file");

        // create test parser
        Parser parser = new DefaultParser();
        parser.setOptions(options);

        // test parsing with valid arguments
        String[] args = new String[]{"-i", "/path/to/input.txt", "-o", "/path/to/output.txt"};
        CommandLine cmd = parser.parse(options, args, null);
        Assert.assertEquals("/path/to/input.txt", cmd.getOptionValue("input"));
        Assert.assertEquals("/path/to/output.txt", cmd.getOptionValue("output"));

        // test parsing with missing optional option
        args = new String[]{"-o", "/path/to/output.txt"};
        CommandLine cmd2 = parser.parse(options, args, null);
        Assert.assertNull(cmd2.getOptionValue("input"));

        // test parsing with multiple values for optional option
        args = new String[]{"-o", "/path/to/output1.txt", "/path/to/output2.txt"};
        CommandLine cmd3 = parser.parse(options, args, null);
        String[] values = cmd3.getOptionValues("output");
        Assert.assertArrayEquals(new String[]{"/path/to/output1.txt", "/path/to/output2.txt"}, values);

        // test parsing with unrecognized option
        args = new String[]{"--unknown"};
        try
        {
            parser.parse(options, args, null);
            Assert.fail("UnrecognizedOptionException expected.");
        }
        catch (UnrecognizedOptionException e)
        {
            // expected
            Assert.assertEquals("Invalid option: unknown\n", e.getMessage());
        }
    }

    /**
     * Tests the parsing of command line arguments with no options.
     */
    @Test
    public void testParsingWithNoOptions() throws Exception
    {
        // create test parser
        Parser parser = new DefaultParser();

        // test parsing with empty arguments
        String[] args = new String[0];
        CommandLine cmd = parser.parse(null, args, null);
        Assert.assertTrue(cmd.getArgs().isEmpty());

        // test parsing with arguments
        args = new String[]{"arg1", "arg2"};
        CommandLine cmd2 = parser.parse(null, args, null);
        Assert.assertEquals(2, cmd2.getArgs().size());
        Assert.assertEquals("arg1", cmd2.getArgs().get(0));
        Assert.assertEquals("arg2", cmd2.getArgs().get(1));

        // test parsing with unrecognized option
        args = new String[]{"--unknown"};
        try
        {
            parser.parse(null, args, null);
            Assert.fail("UnrecognizedOptionException expected.");
        }
        catch (UnrecognizedOptionException e)
        {
            // expected
            Assert.assertEquals("Invalid option: unknown\n", e.getMessage());
        }
    }
}