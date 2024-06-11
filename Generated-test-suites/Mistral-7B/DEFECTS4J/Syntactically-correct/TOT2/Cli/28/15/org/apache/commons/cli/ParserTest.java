package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class ParserTest
{
    @Test
    public void testParserWithBasicOptions() throws ParseException
    {
        Options options = new Options();
        options.addOption("a", true, "An option with a flag");
        options.addOption("b", 1, "An option with an integer value");

        DefaultParser parser = new DefaultParser();
        String[] args = {"-a", "--b=2"};
        CommandLine cmd = parser.parse(options, args, true);

        assertEquals(true, cmd.hasOption("a"));
        assertEquals(true, cmd.hasOption("b"));
        assertEquals(2, cmd.getOptionValue("b", 0));
    }

    @Test
    public void testParserWithRequiredOptions() throws ParseException
    {
        Options options = new Options();
        options.addRequiredOption("a", true, "A required option");

        DefaultParser parser = new DefaultParser();
        String[] args = {};
        assertThrows(MissingOptionException.class, () -> parser.parse(options, args, true));

        args = new String[]{"-a"};
        cmd = parser.parse(options, args, true);
        assertEquals(true, cmd.hasOption("a"));
    }

    @Test
    public void testParserWithOptionGroups() throws ParseException
    {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group1", "Options for group1");
        group.addOption("a", true, "An option in group1");
        group.addOption("b", true, "Another option in group1");
        options.addOptionGroup(group);

        DefaultParser parser = new DefaultParser();
        String[] args = {"--group1", "-a"};
        CommandLine cmd = parser.parse(options, args, true);
        assertEquals(true, cmd.hasOption("a"));
    }

    @Test
    public void testParserWithLongOptions() throws ParseException
    {
        Options options = new Options();
        options.addOption("long-option", true, "A long option");

        DefaultParser parser = new DefaultParser();
        String[] args = {"--long-option"};
        CommandLine cmd = parser.parse(options, args, true);
        assertEquals(true, cmd.hasOption("long-option"));
    }

    @Test
    public void testParserWithValueOptions() throws ParseException
    {
        Options options = new Options();
        options.addOption("a", true, "An option with a value");
        options.addOption("b", 1, "An option with an integer value");

        DefaultParser parser = new DefaultParser();
        String[] args = {"-a", "value-a", "--b=2"};
        CommandLine cmd = parser.parse(options, args, true);

        assertEquals(true, cmd.hasOption("a"));
        assertEquals("value-a", cmd.getOptionValue("a"));
        assertEquals(2, cmd.getOptionValue("b", 0));
    }

    @Test
    public void testParserWithMixedOptions() throws ParseException
    {
        Options options = new Options();
        options.addOption("a", true, "An option with a flag");
        options.addOption("b", 1, "An option with an integer value");

        DefaultParser parser = new DefaultParser();
        String[] args = {"-a", "--b=2", "arg1"};
        CommandLine cmd = parser.parse(options, args, true);

        assertEquals(true, cmd.hasOption("a"));
        assertEquals(true, cmd.hasOption("b"));
        assertEquals(2, cmd.getOptionValue("b", 0));
        assertEquals(1, cmd.getArgs().size());
        assertEquals("arg1", cmd.getArgs().get(0));
    }

    @Test
    public void testParserWithProperties() throws ParseException
    {
        Options options = new Options();
        options.addOption("a", true, "An option with a flag");
        options.addOption("b", 1, "An option with an integer value");

        Properties properties = new Properties();
        properties.setProperty("a", "value-a");
        properties.setProperty("b", "2");

        DefaultParser parser = new DefaultParser();
        String[] args = {"--propertyFile", "test.properties"};
        CommandLine cmd = parser.parse(options, args, properties, true);

        assertEquals(true, cmd.hasOption("a"));
        assertEquals("value-a", cmd.getOptionValue("a"));
        assertEquals(2, cmd.getOptionValue("b", 0));
    }

    @Test
    public void testParserWithStopAtNonOption() throws ParseException
    {
        Options options = new Options();
        options.addOption("a", true, "An option with a flag");
        options.addOption("b", 1, "An option with an integer value");

        DefaultParser parser = new DefaultParser();
        String[] args = {"-a", "arg1", "--", "arg2", "arg3"};
        CommandLine cmd = parser.parse(options, args, true, true);

        assertEquals(true, cmd.hasOption("a"));
        assertEquals("arg1", cmd.getOptionValue("a"));
        assertEquals(2, cmd.getArgs().size());
        assertEquals("arg2", cmd.getArgs().get(0));
        assertEquals("arg3", cmd.getArgs().get(1));
    }

    @Test(expected = ParseException.class)
    public void testParserWithUnknownOption() throws ParseException
    {
        Options options = new Options();
        DefaultParser parser = new DefaultParser();
        String[] args = {"--unknown-option"};
        parser.parse(options, args, true);
    }
}