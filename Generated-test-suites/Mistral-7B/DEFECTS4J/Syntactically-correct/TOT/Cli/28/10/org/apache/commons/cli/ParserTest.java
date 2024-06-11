package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.apache.commons.cli.parser.DefaultParser;

public class ParserTest
{
    @org.junit.Test
    public void testParseWithEmptyArguments() throws ParseException
    {
        Options options = new Options();
        CommandLineParser parser = new DefaultParser();
        String[] args = {};

        CommandLine cmd = parser.parse(options, args, false);

        assertEquals(0, cmd.getArgs().size());
        assertNull(cmd.getOptionValues("help"));
        assertFalse(cmd.hasOption("help"));
    }

    @org.junit.Test
    public void testParseWithHelpOption() throws ParseException
    {
        Options options = new Options();
        options.addOption("h", "help", false, "Display this help message");
        CommandLineParser parser = new DefaultParser();
        String[] args = {"-h"};

        CommandLine cmd = parser.parse(options, args, false);

        assertEquals(0, cmd.getArgs().size());
        assertTrue(cmd.hasOption("help"));
        assertNull(cmd.getOptionValues("help"));
    }

    @org.junit.Test
    public void testParseWithHelpOptionAndArgument() throws ParseException
    {
        Options options = new Options();
        options.addOption("h", "help", false, "Display this help message");
        options.addOption("a", "argument", true, "An argument for the option a");
        CommandLineParser parser = new DefaultParser();
        String[] args = {"-h", "--argument", "value"};

        CommandLine cmd = parser.parse(options, args, false);

        assertEquals(0, cmd.getArgs().size());
        assertTrue(cmd.hasOption("help"));
        assertNull(cmd.getOptionValues("help"));
        assertEquals(1, cmd.getOptionValues("argument").length);
        assertEquals("value", cmd.getOptionValues("argument")[0]);
    }

    @org.junit.Test
    public void testParseWithRequiredOption() throws ParseException
    {
        Options options = new Options();
        options.addOption("o", "option", true, "An option requiring an argument");
        options.addRequiredOption("a", "argument", true, "An argument required argument");
        CommandLineParser parser = new DefaultParser();
        String[] args = {"--option", "value"};

        assertThrows(MissingArgumentException.class, () -> parser.parse(options, args, false));

        args = new String[]{"--option", "value", "--argument", "argValue"};

        CommandLine cmd = parser.parse(options, args, false);

        assertEquals(0, cmd.getArgs().size());
        assertTrue(cmd.hasOption("option"));
        assertEquals(1, cmd.getOptionValues("option").length);
        assertEquals("value", cmd.getOptionValues("option")[0]);
        assertTrue(cmd.hasOption("argument"));
        assertEquals(1, cmd.getOptionValues("argument").length);
        assertEquals("argValue", cmd.getOptionValues("argument")[0]);
    }

    @org.junit.Test
    public void testParseWithOptionalOption() throws ParseException
    {
        Options options = new Options();
        options.addOption("o", "option", false, "An optional option");
        CommandLineParser parser = new DefaultParser();
        String[] args = {"--option"};

        CommandLine cmd = parser.parse(options, args, false);

        assertEquals(0, cmd.getArgs().size());
        assertTrue(cmd.hasOption("option"));
        assertNull(cmd.getOptionValues("option"));

        args = new String[]{"--option", "value"};

        cmd = parser.parse(options, args, false);

        assertEquals(0, cmd.getArgs().size());
        assertTrue(cmd.hasOption("option"));
        assertEquals(1, cmd.getOptionValues("option").length);
        assertEquals("value", cmd.getOptionValues("option")[0]);

        args = new String[]{"--option"};

        cmd = parser.parse(options, args, true);

        assertEquals(0, cmd.getArgs().size());
        assertFalse(cmd.hasOption("option"));
    }

    @org.junit.Test
    public void testParseWithMixedOptions() throws ParseException
    {
        Options options = new Options();
        options.addOption("h", "help", false, "Display this help message");
        options.addOption("o", "option", false, "An optional option");
        options.addOption("a", "argument", true, "An argument for the option a");
        CommandLineParser parser = new DefaultParser();
        String[] args = {"--option", "--help"};

        CommandLine cmd = parser.parse(options, args, false);

        assertEquals(0, cmd.getArgs().size());
        assertTrue(cmd.hasOption("option"));
        assertNull(cmd.getOptionValues("option"));
        assertFalse(cmd.hasOption("help"));
    }

    @org.junit.Test
    public void testParseWithPropertyValues() throws ParseException
    {
        Options options = new Options();
        options.addOption("p", "property", true, "A property option with value");
        CommandLineParser parser = new DefaultParser();
        Properties properties = new Properties();
        properties.setProperty("p", "value");

        CommandLine cmd = parser.parse(options, new String[0], properties, false);

        assertEquals(0, cmd.getArgs().size());
        assertFalse(cmd.hasOption("property"));
        assertNull(cmd.getOptionValues("property"));

        options.addOption("p", "property", true, "A property option with values");
        cmd = parser.parse(options, new String[0], properties, false);

        assertEquals(0, cmd.getArgs().size());
        assertTrue(cmd.hasOption("property"));
        assertNull(cmd.getOptionValues("property"));

        properties.setProperty("p", "value1,value2");

        cmd = parser.parse(options, new String[0], properties, false);

        assertEquals(0, cmd.getArgs().size());
        assertTrue(cmd.hasOption("property"));
        assertEquals(2, cmd.getOptionValues("property").length);
        List<String> values = new ArrayList<>(Arrays.asList(cmd.getOptionValues("property")));
        assertTrue(values.contains("value1"));
        assertTrue(values.contains("value2"));
    }

    @org.junit.Test
    public void testParseWithUnrecognizedOption() throws ParseException
    {
        Options options = new Options();
        CommandLineParser parser = new DefaultParser();
        String[] args = {"--unrecognized"};

        assertThrows(UnrecognizedOptionException.class, () -> parser.parse(options, args, false));

        args = new String[]{"--unrecognized", "value"};

        assertThrows(UnrecognizedOptionException.class, () -> parser.parse(options, args, false));

        args = new String[]{"--unrecognized"};

        assertThrows(ParseException.class, () -> parser.parse(options, args, true));

        args = new String[]{"--unrecognized", "value"};

        assertThrows(ParseException.class, () -> parser.parse(options, args, true));
    }

    @org.junit.Test
    public void testParseWithOptionsInGroup() throws ParseException
    {
        Options options = new Options();
        OptionGroup group = new OptionGroup();
        group.setName("group");
        group.addOption("g", "groupOption", false, "An option in the group");
        options.addOptionGroup(group);

        CommandLineParser parser = new DefaultParser();
        String[] args = {"--groupOption"};

        CommandLine cmd = parser.parse(options, args, false);

        assertEquals(0, cmd.getArgs().size());
        assertTrue(cmd.hasOption("groupOption"));
        assertNull(cmd.getOptionValues("groupOption"));
        assertTrue(cmd.hasOptionGroup("group"));
        assertTrue(cmd.getOptionGroup("group").isSelected());

        cmd = parser.parse(options, args, true);

        assertEquals(0, cmd.getArgs().size());
        assertFalse(cmd.hasOption("groupOption"));
        assertNull(cmd.getOptionValues("groupOption"));
        assertFalse(cmd.getOptionGroup("group").isSelected());

        group.setRequired(true);

        assertThrows(MissingOptionException.class, () -> parser.parse(options, args, false));

        args = new String[]{"--groupOption"};

        cmd = parser.parse(options, args, false);

        assertEquals(0, cmd.getArgs().size());
        assertTrue(cmd.hasOption("groupOption"));
        assertNull(cmd.getOptionValues("groupOption"));
        assertTrue(cmd.hasOptionGroup("group"));
        assertTrue(cmd.getOptionGroup("group").isSelected());
    }

    @org.junit.Test
    public void testParseWithMissingRequiredOption() throws ParseException
    {
        Options options = new Options();
        options.addOption("a", "argument", true, "An argument for the option a");
        options.addRequiredOption("o", "option", true, "An option requiring an argument");
        CommandLineParser parser = new DefaultParser();
        String[] args = {"--option"};

        assertThrows(MissingOptionException.class, () -> parser.parse(options, args, false));

        args = new String[]{"--option", "value"};

        assertThrows(MissingArgumentException.class, () -> parser.parse(options, args, false));
    }
}