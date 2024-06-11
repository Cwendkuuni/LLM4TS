package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class ParserTest
{
    @Test
    public void testSimpleParse() throws ParseException
    {
        // Set up the command line options
        Options options = new Options();
        options.addOption("a", "optionA", true, "Option A");
        options.addOption("b", "optionB", false, "Option B");
        options.addOption("c", "optionC", false, "Option C");

        // Set up the Parser instance
        Parser parser = new DefaultParser();
        parser.setOptions(options);

        // Set up the command line arguments
        String[] arguments = new String[]{"-a", "1", "--optionB", "-c"};

        // Parse the command line arguments
        CommandLine cmd = parser.parse(options, arguments, null, false);

        // Assert the parsed command line options
        Assert.assertEquals("1", cmd.getOptionValue("a"));
        Assert.assertNull(cmd.getOptionValue("b"));
        Assert.assertNull(cmd.getOptionValue("c"));
        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertTrue(cmd.hasOption("b"));
        Assert.assertTrue(cmd.hasOption("c"));
    }

    @Test
    public void testMissingOption() throws ParseException
    {
        // Set up the command line options
        Options options = new Options();
        options.addOption("a", "optionA", true, "Option A");

        // Set up the Parser instance
        Parser parser = new DefaultParser();
        parser.setOptions(options);

        // Set up the command line arguments
        String[] arguments = new String[]{"-b", "1"};

        try
        {
            // Parse the command line arguments
            parser.parse(options, arguments, null, false);
            Assert.fail();
        }
        catch (ParseException e)
        {
            // Expected ParseException
        }

        // Assert the error message
        Assert.assertEquals("Unrecognized option: b", e.getMessage());
    }

    @Test
    public void testMissingRequiredOption() throws ParseException
    {
        // Set up the command line options
        Options options = new Options();
        options.addRequiredOption("a", "optionA", true, "Option A");

        // Set up the Parser instance
        Parser parser = new DefaultParser();
        parser.setOptions(options);

        // Set up the command line arguments
        String[] arguments = new String[0];

        try
        {
            // Parse the command line arguments
            parser.parse(options, arguments, null, false);
            Assert.fail();
        }
        catch (MissingOptionException e)
        {
            // Expected MissingOptionException
            String[] expectedOptions = new String[]{"optionA"};
            Assert.assertArrayEquals(expectedOptions, e.getMissingOptions());
        }
    }

    @Test
    public void testParseWithProperties() throws ParseException
    {
        // Set up the command line options
        Options options = new Options();
        options.addOption("a", "optionA", true, "Option A");

        // Set up the Parser instance
        Parser parser = new DefaultParser();
        parser.setOptions(options);

        // Set up the command line arguments and properties
        String[] arguments = new String[]{"-a", "1"};
        Properties properties = new Properties();
        properties.setProperty("optionA", "valueA");

        // Parse the command line arguments and properties
        CommandLine cmd = parser.parse(options, arguments, properties, false);

        // Assert the parsed command line options
        Assert.assertEquals("1", cmd.getOptionValue("a"));
        Assert.assertEquals("valueA", cmd.getOptionValue("a", "defaultValue"));
        Assert.assertTrue(cmd.hasOption("a"));
    }

    @Test
    public void testStopAtNonOption() throws ParseException
    {
        // Set up the command line options
        Options options = new Options();
        options.addOption("a", "optionA", true, "Option A");
        options.addOption("b", "optionB", false, "Option B");

        // Set up the Parser instance
        Parser parser = new DefaultParser();
        parser.setOptions(options);

        // Set up the command line arguments
        String[] arguments = new String[]{"-a", "1", "--", "nonOption"};

        // Parse the command line arguments
        CommandLine cmd = parser.parse(options, arguments, null, true);

        // Assert the parsed command line options
        Assert.assertEquals("1", cmd.getOptionValue("a"));
        Assert.assertNull(cmd.getOptionValue("b"));
        Assert.assertNull(cmd.getOptionValue("nonOption"));
        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertFalse(cmd.hasOption("b"));
        Assert.assertFalse(cmd.hasOption("nonOption"));
        Assert.assertEquals(Arrays.asList(new String[]{"nonOption"}), cmd.getArgs());
    }

    @Test
    public void testParseWithGroup() throws ParseException
    {
        // Set up the command line options
        Options options = new Options();

        OptionGroup group = new OptionGroup("group", "Group");
        group.addOption(new Option("a", "optionA", true, "Option A"));
        group.addOption(new Option("b", "optionB", true, "Option B"));
        options.addOptionGroup(group);

        // Set up the Parser instance
        Parser parser = new DefaultParser();
        parser.setOptions(options);

        // Set up the command line arguments
        String[] arguments = new String[]{"--group", "-a", "1", "-b", "2"};

        // Parse the command line arguments
        CommandLine cmd = parser.parse(options, arguments, null, false);

        // Assert the parsed command line options
        Assert.assertTrue(cmd.hasOption("group"));
        Assert.assertTrue(group.isSelected());
        Assert.assertEquals("1", cmd.getOptionValue("optionA"));
        Assert.assertEquals("2", cmd.getOptionValue("optionB"));
        Assert.assertFalse(cmd.hasOption("a"));
        Assert.assertFalse(cmd.hasOption("b"));
    }
}