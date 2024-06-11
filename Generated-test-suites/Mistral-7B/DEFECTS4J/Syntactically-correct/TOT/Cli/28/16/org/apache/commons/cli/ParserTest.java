package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ParserTest
{
    @Test
    public void testSimpleParse() throws ParseException
    {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options.toArray(new String[0]), new String[]{"-a"});

        assertEquals("Option A should be set to true", true, cmd.hasOption("a"));
        assertNull("No other options should be set", cmd.getOptionValue("b"));
    }

    @Test(expected = ParseException.class)
    public void testMissingRequiredOption() throws ParseException
    {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addRequiredOption("b", true, "Option B");

        CommandLineParser parser = new DefaultParser();
        parser.parse(options.toArray(new String[0]), new String[]{"-a"});
    }

    @Test
    public void testParseWithValue() throws ParseException
    {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options.toArray(new String[0]), new String[]{"-a", "value"});

        assertEquals("Option A value should be set to value", "value", cmd.getOptionValue("a"));
        assertNull("No other options should be set", cmd.getOptionValue("b"));
    }

    @Test
    public void testParseWithGroup() throws ParseException
    {
        Options options = new Options();

        OptionGroup group = new OptionGroup("groupName", "Group description");
        group.addOption("a", true, "Option A");
        group.addOption("b", true, "Option B");

        options.addOptionGroup(group);
        options.addRequiredOption("c", true, "Option C");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options.toArray(new String[0]), new String[]{"-a", "-b", "--groupName"});

        assertEquals("Option A should be set to true", true, cmd.hasOption("a"));
        assertEquals("Option B should be set to true", true, cmd.hasOption("b"));
        assertNull("Option C should be required and not set", cmd.getOptionValue("c"));
        assertNull("No other options should be set", cmd.getOptionValue("d"));
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidOption() throws ParseException
    {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        CommandLineParser parser = new DefaultParser();
        parser.parse(options.toArray(new String[0]), new String[]{"-x"});
    }

    @Test(expected = ParseException.class)
    public void testParseWithUnrecognizedOption() throws ParseException
    {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        CommandLineParser parser = new DefaultParser();
        parser.parse(options.toArray(new String[0]), new String[]{"--unrecognizedOption"});
    }

    @Test
    public void testParseWithMultipleValues() throws ParseException
    {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("a", true, "Option A");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options.toArray(new String[0]), new String[]{"-a", "value1", "-a", "value2"});

        String values = cmd.getOptionValue("a");
        String[] expectedValues = new String[]{"value1", "value2"};

        assertEquals("Number of values for option A should be 2", expectedValues.length, cmd.getValues("a").length);
        assertArrayEquals("Values for option A should be equal to expected values", expectedValues, cmd.getValues("a"));
    }

    @Test
    public void testParseWithMixedValues() throws ParseException
    {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options.toArray(new String[0]), new String[]{"-a", "value1", "-a", "value2", "--"});

        String values = cmd.getOptionValue("a");
        String[] expectedValues = new String[]{"value1", "value2"};

        assertEquals("Number of values for option A should be 2", expectedValues.length, cmd.getValues("a").length);
        assertArrayEquals("Values for option A should be equal to expected values", expectedValues, cmd.getValues("a"));
    }
}