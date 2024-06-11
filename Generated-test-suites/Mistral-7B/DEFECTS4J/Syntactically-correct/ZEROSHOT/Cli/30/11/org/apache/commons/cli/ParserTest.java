package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Test class for the Parser class
 */
public class ParserTest
{
    @Test
    public void testParseWithBasicOptions() throws ParseException
    {
        String[] arguments = new String[]{"-a", "1", "-b", "2"};
        Options options = new Options();
        options.addOption("a", true, "A value");
        options.addOption("b", true, "B value");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(arguments);

        Assert.assertEquals(cmd.getArgs().size(), 0);
        Assert.assertEquals(cmd.getOptionValues("a"), new String[]{"1"});
        Assert.assertEquals(cmd.getOptionValues("b"), new String[]{"2"});
    }

    @Test(expected = ParseException.class)
    public void testParseWithUnrecognizedOption() throws ParseException
    {
        String[] arguments = new String[]{"--x", "1"};
        Options options = new Options();
        options.addOption("a", true, "A value");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        parser.parse(arguments, false);
    }

    @Test(expected = ParseException.class)
    public void testParseWithUnrecognizedOptionAndStopAtNonOption() throws ParseException
    {
        String[] arguments = new String[]{"--x", "1"};
        Options options = new Options();
        options.addOption("a", true, "A value");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        parser.parse(arguments, true);
    }

    @Test
    public void testParseWithLongOptions() throws ParseException
    {
        String[] arguments = new String[]{"--long-option", "value"};
        Options options = new Options();
        options.addOption("longOption", true, "Long option value");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(arguments);

        Assert.assertEquals(cmd.getArgs().size(), 0);
        Assert.assertEquals(cmd.getOptionValues("longOption"), new String[]{"value"});
    }

    @Test
    public void testParseWithShortAndLongOptions() throws ParseException
    {
        String[] arguments = new String[]{"-a", "1", "--long-option", "value"};
        Options options = new Options();
        options.addOption("a", true, "A value");
        options.addOption("l", "longOption", true, "Long option value");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(arguments);

        Assert.assertEquals(cmd.getArgs().size(), 0);
        Assert.assertEquals(cmd.getOptionValues("a"), new String[]{"1"});
        Assert.assertEquals(cmd.getOptionValues("longOption"), new String[]{"value"});
    }

    @Test
    public void testParseWithRequiredOptions() throws ParseException
    {
        String[] arguments = new String[]{"-a", "1", "-b"};
        Options options = new Options();
        options.addOption("a", true, "A value");
        options.addOption("b", true, "B value").isRequired(true);

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        try
        {
            parser.parse(arguments, false);
            Assert.fail();
        }
        catch (MissingOptionException e)
        {
            List<String> missingOptions = Arrays.asList(e.getMissingOptions());
            Assert.assertEquals(missingOptions.size(), 1);
            Assert.assertEquals(missingOptions.get(0), "b");
        }

        parser.parse(arguments, true);
        CommandLine cmd = parser.parse(arguments);

        Assert.assertEquals(cmd.getArgs().size(), 0);
        Assert.assertEquals(cmd.getOptionValues("a"), new String[]{"1"});
        Assert.assertNull(cmd.getOptionValues("b"));
    }

    @Test
    public void testParseWithGroupOptions() throws ParseException
    {
        String[] arguments = new String[]{"--group", "-g", "1", "--group1", "-g1", "1"};
        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "Group options");
        group.setRequired(false);
        group.addOption(new Option("g", "group1", false, "Group option 1"));
        options.addOptionGroup(group);
        options.addOption("a", true, "A value");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(arguments);

        Assert.assertEquals(cmd.getArgs().size(), 0);
        Assert.assertEquals(cmd.getOptionValues("g"), new String[]{"1"});
        Assert.assertEquals(cmd.getOptionValues("group1"), new String[]{"1"});
        Assert.assertEquals(cmd.getOptionValues("a"), null);
    }

    @Test(expected = MissingOptionException.class)
    public void testParseWithMissingGroupOption() throws ParseException
    {
        String[] arguments = new String[]{"--group", "-g", "1"};
        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "Group options");
        group.setRequired(false);
        group.addOption(new Option("g", "group1", false, "Group option 1"));
        options.addOptionGroup(group);
        options.addOption("a", true, "A value");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        parser.parse(arguments, false);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseWithUnrecognizedOptionInGroup() throws ParseException
    {
        String[] arguments = new String[]{"--group", "-g", "--x", "1"};
        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "Group options");
        group.setRequired(false);
        group.addOption(new Option("g", "group1", false, "Group option 1"));
        options.addOptionGroup(group);
        options.addOption("a", true, "A value");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        parser.parse(arguments, false);
    }

    @Test
    public void testParseWithProperties() throws ParseException
    {
        String[] arguments = new String[]{"--propertyFile", "test.properties"};
        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        properties.setProperty("property2", "value2");

        Options options = new Options();
        options.addOption("p", "propertyFile", true, "Property file path");
        options.addOption("property1", true, "Property1 value");
        options.addOption("property2", true, "Property2 value");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(arguments, properties);

        Assert.assertEquals(cmd.getArgs().size(), 0);
        Assert.assertEquals(cmd.getOptionValues("propertyFile"), new String[]{"test.properties"});
        Assert.assertEquals(cmd.getOptionValues("property1"), new String[]{"value1"});
        Assert.assertEquals(cmd.getOptionValues("property2"), new String[]{"value2"});
    }

    @Test
    public void testParseWithUnresolvedProperties() throws ParseException
    {
        String[] arguments = new String[]{"--propertyFile", "test.properties"};
        Properties properties = new Properties();
        properties.setProperty("property1", null);
        properties.setProperty("property2", null);

        Options options = new Options();
        options.addOption("p", "propertyFile", true, "Property file path");
        options.addOption("property1", true, "Property1 value");
        options.addOption("property2", true, "Property2 value");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        try
        {
            parser.parse(arguments, properties, false);
            Assert.fail();
        }
        catch (ParseException e)
        {
            Assert.assertTrue(e.getMessage().contains("Unresolved property"));
        }

        try
        {
            parser.parse(arguments, properties, true);
            Assert.fail();
        }
        catch (MissingOptionException e)
        {
            Assert.assertArrayEquals(e.getMissingOptions().toArray(), new String[]{"property1", "property2"});
        }
    }

    @Test
    public void testParseWithStopAtNonOption() throws ParseException
    {
        String[] arguments = new String[]{"--", "value"};
        Options options = new Options();
        options.addOption("a", true, "A value");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(arguments, true);

        Assert.assertEquals(cmd.getArgs().size(), 1);
        Assert.assertEquals(cmd.getArgs().get(0), "value");
    }
}