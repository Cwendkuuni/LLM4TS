package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class ParserTest
{
    @Test
    public void testParserWithSimpleOptions() throws ParseException
    {
        String[] arguments = new String[]{"-a", "1", "-b", "2"};

        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, true);

        Assert.assertEquals(true, cmd.hasOption("a"));
        Assert.assertEquals("1", cmd.getOptionValue("a"));
        Assert.assertEquals(true, cmd.hasOption("b"));
        Assert.assertEquals("2", cmd.getOptionValue("b"));
        Assert.assertNull(cmd.getOptionValue("c"));
        Assert.assertNull(cmd.getArgs());
    }

    @Test(expected = ParseException.class)
    public void testParserWithMissingArgument() throws ParseException
    {
        String[] arguments = new String[]{"-a", "1", "--"};

        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C");
        options.addRequiredOption("c", true, "Required Option C");

        CommandLineParser parser = new DefaultParser();
        parser.parse(options, arguments, true);
    }

    @Test(expected = ParseException.class)
    public void testParserWithMissingOption() throws ParseException
    {
        String[] arguments = new String[]{"--option", "value"};

        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        CommandLineParser parser = new DefaultParser();
        parser.parse(options, arguments, true);
    }

    @Test(expected = ParseException.class)
    public void testParserWithInvalidOption() throws ParseException
    {
        String[] arguments = new String[]{"--invalidOption", "value"};

        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        CommandLineParser parser = new DefaultParser();
        parser.parse(options, arguments, true);
    }

    @Test
    public void testParserWithProperties() throws ParseException
    {
        String[] arguments = new String[]{"--foo=value1", "--bar=value2"};

        Properties props = new Properties();
        props.setProperty("foo", "value1");
        props.setProperty("bar", "value2");

        Options options = new Options();
        options.addOption("foo", true, "Option Foo");
        options.addOption("bar", true, "Option Bar");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, props, true);

        Assert.assertEquals(true, cmd.hasOption("foo"));
        Assert.assertEquals("value1", cmd.getOptionValue("foo"));
        Assert.assertEquals(true, cmd.hasOption("bar"));
        Assert.assertEquals("value2", cmd.getOptionValue("bar"));
        Assert.assertNull(cmd.getOptionValue("c"));
        Assert.assertNull(cmd.getArgs());
    }

    @Test
    public void testParserWithLongOptions() throws ParseException
    {
        String[] arguments = new String[]{"--longOptionA", "value1", "--longOptionB=value2"};

        Options options = new Options();
        options.addOption("longOptionA", true, "Long Option A");
        options.addOption("longOptionB", true, "Long Option B");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, true);

        Assert.assertEquals(true, cmd.hasOption("longOptionA"));
        Assert.assertEquals("value1", cmd.getOptionValue("longOptionA"));
        Assert.assertEquals(true, cmd.hasOption("longOptionB"));
        Assert.assertEquals("value2", cmd.getOptionValue("longOptionB"));
        Assert.assertNull(cmd.getOptionValue("c"));
        Assert.assertNull(cmd.getArgs());
    }

    @Test
    public void testParserWithOptionGroup() throws ParseException
    {
        String[] arguments = new String[]{"-g", "--groupOption1", "--groupOption2"};

        Options options = new Options();
        OptionGroup group = new OptionGroup("group");
        group.setRequired(false);
        group.addOption(new Option("groupOption1", true, "Group Option 1"));
        group.addOption(new Option("groupOption2", true, "Group Option 2"));
        options.addOptionGroup(group);
        options.addOption("a", true, "Option A");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, true);

        Assert.assertEquals(true, cmd.hasOption("g"));
        Assert.assertEquals(true, cmd.hasOption("groupOption1"));
        Assert.assertEquals(true, cmd.hasOption("groupOption2"));
        Assert.assertEquals(false, cmd.hasOption("a"));
        Assert.assertNull(cmd.getArgs());
    }

    @Test
    public void testParserWithMixedOptions() throws ParseException
    {
        String[] arguments = new String[]{"-a", "1", "--longOption", "value1", "2", "--groupOption1", "--groupOption2"};

        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("longOption", true, "Long Option");
        OptionGroup group = new OptionGroup("group");
        group.setRequired(false);
        group.addOption(new Option("groupOption1", true, "Group Option 1"));
        group.addOption(new Option("groupOption2", true, "Group Option 2"));
        options.addOptionGroup(group);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, true);

        Assert.assertEquals(true, cmd.hasOption("a"));
        Assert.assertEquals("1", cmd.getOptionValue("a"));
        Assert.assertEquals(true, cmd.hasOption("longOption"));
        Assert.assertEquals("value1", cmd.getOptionValue("longOption"));
        Assert.assertEquals(true, cmd.hasOption("groupOption1"));
        Assert.assertEquals(true, cmd.hasOption("groupOption2"));
        Assert.assertEquals(Arrays.asList("2").iterator().next(), cmd.getArg(0));
    }

    @Test
    public void testParserWithUnknownOptions() throws ParseException
    {
        String[] arguments = new String[]{"-a", "1", "--unknownOption", "2"};

        Options options = new Options();
        options.addOption("a", true, "Option A");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, true);

        Assert.assertEquals(true, cmd.hasOption("a"));
        Assert.assertEquals("1", cmd.getOptionValue("a"));
        Assert.assertNull(cmd.getOptionValue("unknownOption"));
        Assert.assertEquals(Collections.singletonList("2"), cmd.getArgs());
    }
}