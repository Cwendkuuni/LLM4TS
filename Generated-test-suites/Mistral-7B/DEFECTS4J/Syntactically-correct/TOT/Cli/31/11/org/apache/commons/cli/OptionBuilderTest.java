package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest
{
    @Test
    public void testOptionBuilderWithSingleRequiredArgument()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("inputFile")
                .isRequired()
                .hasArg()
                .withArgName("file");
        Option option = optionBuilder.create('i');

        // Test valid usage
        CommandLine cmd = null;
        try
        {
            cmd = new DefaultParser().parse(new String[]{"-i", "test.txt"});
            assertTrue(cmd.hasOption("inputFile"));
            assertEquals("test.txt", cmd.getOptionValue("inputFile"));
        }
        catch (ParseException e)
        {
            fail("Test should not throw an exception for valid usage.");
        }

        // Test missing argument
        try
        {
            cmd = new DefaultParser().parse(new String[]{"-i"});
            fail("Test should throw an exception for missing argument.");
        }
        catch (ParseException e)
        {
            assertEquals("Required option 'inputFile' is missing.", e.getMessage());
        }
    }

    @Test
    public void testOptionBuilderWithSingleOptionalArgument()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("outputDir")
                .hasArg(true)
                .withArgName("directory");
        Option option = optionBuilder.create('o');

        // Test valid usage
        CommandLine cmd = null;
        try
        {
            cmd = new DefaultParser().parse(new String[]{"-o", "output"});
            assertTrue(cmd.hasOption("outputDir"));
            assertEquals("output", cmd.getOptionValue("outputDir"));
        }
        catch (ParseException e)
        {
            fail("Test should not throw an exception for valid usage.");
        }

        // Test missing argument
        try
        {
            cmd = new DefaultParser().parse(new String[]{"-o"});
        }
        catch (ParseException e)
        {
            // Optional argument missing is not an error
        }

        // Test valid usage with argument
        try
        {
            cmd = new DefaultParser().parse(new String[]{"-o", "output", "subdir"});
            assertTrue(cmd.hasOption("outputDir"));
            assertEquals("output/subdir", cmd.getOptionValue("outputDir"));
        }
        catch (ParseException e)
        {
            fail("Test should not throw an exception for valid usage with argument.");
        }
    }

    @Test
    public void testOptionBuilderWithMultipleOptionalArguments()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("numbers")
                .hasArgs(2)
                .withArgName("number")
                .isRequired(false);
        Option option = optionBuilder.create('n');

        // Test valid usage with two arguments
        CommandLine cmd = null;
        try
        {
            cmd = new DefaultParser().parse(new String[]{"-n", "1", "2"});
            assertTrue(cmd.hasOption("numbers"));
            assertArrayEquals(new String[]{"1", "2"}, cmd.getOptionValues("numbers"));
        }
        catch (ParseException e)
        {
            fail("Test should not throw an exception for valid usage with two arguments.");
        }

        // Test valid usage with one argument
        try
        {
            cmd = new DefaultParser().parse(new String[]{"-n", "1"});
            assertTrue(cmd.hasOption("numbers"));
            assertArrayEquals(new String[]{"1"}, cmd.getOptionValues("numbers"));
        }
        catch (ParseException e)
        {
            fail("Test should not throw an exception for valid usage with one argument.");
        }

        // Test missing arguments
        try
        {
            cmd = new DefaultParser().parse(new String[]{"-n"});
        }
        catch (ParseException e)
        {
            // Missing arguments is not an error for optional arguments
        }
    }

    @Test
    public void testOptionBuilderWithValueSeparator()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("delimiters")
                .hasArgs()
                .withValueSeparator(',');
        Option option = optionBuilder.create('d');

        // Test valid usage with one argument
        CommandLine cmd = null;
        try
        {
            cmd = new DefaultParser().parse(new String[]{"-d", "a,b,c"});
            assertTrue(cmd.hasOption("delimiters"));
            assertEquals("a,b,c", cmd.getOptionValue("delimiters"));
        }
        catch (ParseException e)
        {
            fail("Test should not throw an exception for valid usage with one argument.");
        }

        // Test valid usage with two arguments
        try
        {
            cmd = new DefaultParser().parse(new String[]{"-d", "a,b", "c,d"});
            assertTrue(cmd.hasOption("delimiters"));
            String[] values = cmd.getOptionValues("delimiters");
            assertArrayEquals(new String[]{"a,b", "c,d"}, values);
        }
        catch (ParseException e)
        {
            fail("Test should not throw an exception for valid usage with two arguments.");
        }
    }
}