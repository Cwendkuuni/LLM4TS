package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest
{
    @Test
    public void testSimpleUsage() throws ParseException
    {
        String[] args = new String[]{"-a", "1", "--b", "2"};

        Options options = new Options();
        options.addOption("a", true, "option A");
        options.addOption("b", true, "option B");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        Assert.assertEquals("1", cmd.getOptionValue("a"));
        Assert.assertEquals("2", cmd.getOptionValue("b"));
        Assert.assertNull(cmd.getOptionValue("c"));

        List<String> argsList = cmd.getArgs();
        Assert.assertEquals(0, argsList.size());
    }

    @Test(expected = ParseException.class)
    public void testMissingRequiredOption() throws ParseException
    {
        String[] args = new String[]{"--app"};

        Options options = new Options();
        options.addRequiredOption("name", true, "name of the application");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        // this test should fail with a ParseException
    }

    @Test
    public void testOptionalOptionWithValue() throws ParseException
    {
        String[] args = new String[]{"--app", "MyApp"};

        Options options = new Options();
        options.addOption("app", true, "name of the application");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        Assert.assertEquals("MyApp", cmd.getOptionValue("app"));
    }

    @Test
    public void testMissingOptionalOption() throws ParseException
    {
        String[] args = new String[]{"--app"};

        Options options = new Options();
        options.addOption("app", true, "name of the application");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        Assert.assertNull(cmd.getOptionValue("app"));
    }

    @Test(expected = ParseException.class)
    public void testInvalidOption() throws ParseException
    {
        String[] args = new String[]{"--invalid"};

        Options options = new Options();

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        // this test should fail with a ParseException
    }

    @Test
    public void testInvalidOptionWithValue() throws ParseException
    {
        String[] args = new String[]{"--invalid", "value"};

        Options options = new Options();

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        // this test should fail with a ParseException
    }

    @Test
    public void testHelp()
    {
        String[] args = new String[]{"--help"};

        Options options = new Options();
        options.addOption("help", false, "print this message");

        CommandLineParser parser = new DefaultParser();

        try
        {
            CommandLine cmd = parser.parse(options, args, false);
            Assert.fail();
        }
        catch (ParseException e)
        {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("Test", options);
            Assert.assertEquals("print this message\n", formatter.getHelpMessage());
        }
    }

    @Test
    public void testOptionsInGroup() throws ParseException
    {
        String[] args = new String[]{"--group", "--option1", "1", "--option2", "2"};

        Options options = new Options();
        OptionGroup group = new OptionGroup("group1", "A group of options");
        group.setRequired(true);

        group.addOption("option1", true, "Option 1");
        group.addOption("option2", true, "Option 2");

        options.addOptionGroup(group);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        Assert.assertTrue(group.isSelected());
        Assert.assertEquals("1", cmd.getOptionValue("option1"));
        Assert.assertEquals("2", cmd.getOptionValue("option2"));
    }
}