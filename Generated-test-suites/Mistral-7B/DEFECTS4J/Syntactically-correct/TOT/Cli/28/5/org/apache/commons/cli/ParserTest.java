package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest
{
    @Test
    public void testSimpleOption() throws ParseException
    {
        String[] args = new String[] {"-a", "value"};
        Options options = new Options();
        options.addOption("a", "anOption", true, "A value option");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);
        Assert.assertEquals("value", cmd.getOptionValue("a"));
    }

    @Test
    public void testSimpleRequiredOption() throws ParseException
    {
        String[] args = new String[] {"--inputFile"};
        Options options = new Options();
        options.addRequiredOption("i", "inputFile", true, "An input file");

        Parser parser = new DefaultParser();
        try
        {
            parser.parse(options, args, false);
            Assert.fail("A required option was not provided");
        }
        catch (MissingOptionException e)
        {
            List<String> missingOptions = e.getMissingOptions();
            Assert.assertEquals(1, missingOptions.size());
            Assert.assertEquals("inputFile", missingOptions.get(0));
        }
    }

    @Test
    public void testSimpleOptionalArgOption() throws ParseException
    {
        String[] args = new String[] {"-a", "value1", "-b", "value2"};
        Options options = new Options();
        options.addOption("a", "anOption", true, "A value option");
        options.addOption("b", "anotherOption", true, "Another value option");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        Assert.assertEquals("value1", cmd.getOptionValue("a"));
        Assert.assertEquals("value2", cmd.getOptionValue("b"));
    }

    @Test
    public void testGroupOption() throws ParseException
    {
        String[] args = new String[] {"--group", "group1", "-A", "value1", "-B", "value2"};
        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "A group of options");
        group.addOption(new Option("A", "anOptionA", true, "An option A"));
        group.addOption(new Option("B", "anOptionB", true, "An option B"));
        options.addOptionGroup(group);

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);
        Assert.assertEquals("value1", cmd.getOptionValue("A"));
        Assert.assertEquals("value2", cmd.getOptionValue("B"));
    }

    @Test
    public void testHelp() throws ParseException
    {
        String[] args = new String[] {"--help"};
        Options options = new Options();
        options.addHelp(true);

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("parser", options);

        // print the help text out to the console
        System.out.println(formatter.getHelpText());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullOptions() throws ParseException
    {
        String[] args = new String[] {};
        Options options = null;

        Parser parser = new DefaultParser();
        parser.parse(options, args, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullArguments() throws ParseException
    {
        String[] args = null;
        Options options = new Options();

        Parser parser = new DefaultParser();
        parser.parse(options, args, false);
    }

    @Test(expected = ParseException.class)
    public void testUnknownOption() throws ParseException
    {
        String[] args = new String[] {"--unknown"};
        Options options = new Options();
        options.addOption("a", "anOption", true, "A value option");

        Parser parser = new DefaultParser();
        parser.parse(options, args, false);
    }

    @Test(expected = ParseException.class)
    public void testMixedOptionsAndArguments() throws ParseException
    {
        String[] args = new String[] {"--inputFile", "input.txt", "-a", "value"};
        Options options = new Options();
        options.addRequiredOption("i", "inputFile", true, "An input file");
        options.addOption("a", "anOption", true, "A value option");

        Parser parser = new DefaultParser();
        try
        {
            parser.parse(options, args, false);
            Assert.fail("An option and an argument were mixed");
        }
        catch (ParseException e)
        {
            // expected
        }
    }

    @Test(expected = ParseException.class)
    public void testMissingRequiredOption() throws ParseException
    {
        String[] args = new String[] {};
        Options options = new Options();
        options.addRequiredOption("a", "anOption", true, "An option");

        Parser parser = new DefaultParser();
        try
        {
            parser.parse(options, args, false);
            Assert.fail("A required option was not provided");
        }
        catch (MissingOptionException e)
        {
            // expected
        }
    }

    @Test(expected = ParseException.class)
    public void testMissingValueForOption() throws ParseException
    {
        String[] args = new String[] {"-a"};
        Options options = new Options();
        options.addOption("a", "anOption", true, "An option with a value");

        Parser parser = new DefaultParser();
        try
        {
            parser.parse(options, args, false);
            Assert.fail("A value was not provided for an option");
        }
        catch (MissingArgumentException e)
        {
            // expected
        }
    }

    @Test(expected = ParseException.class)
    public void testInvalidValueForOption() throws ParseException
    {
        String[] args = new String[] {"-a", "invalid"};
        Options options = new Options();
        options.addOption("a", "anOption", true, "An option with a valid value");

        Parser parser = new DefaultParser();
        try
        {
            parser.parse(options, args, false);
            Assert.fail("An invalid value was provided for an option");
        }
        catch (ParseException e)
        {
            // expected
        }
    }

    @Test(expected = ParseException.class)
    public void testMixedValueForOption() throws ParseException
    {
        String[] args = new String[] {"-a", "value1", "-a", "value2"};
        Options options = new Options();
        options.addOption("a", "anOption", true, "An option with a value");

        Parser parser = new DefaultParser();
        try
        {
            parser.parse(options, args, false);
            Assert.fail("Multiple values were provided for an option");
        }
        catch (ParseException e)
        {
            // expected
        }
    }
}