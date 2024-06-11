package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class ParserTest
{
    @Test
    public void testParseSimple() throws ParseException
    {
        String[] arguments = new String[]{"-a", "1", "--option", "value"};
        CommandLineParser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("option", true, "Option with long name");

        parser.parse(options, arguments, false);

        CommandLine cmd = parser.getCommandLine();

        assertEquals(Arrays.asList("1"), cmd.getOptions().getOptionValues("a"));
        assertEquals("value", cmd.getOptions().getOptionValues("option")[0]);
    }

    @Test
    public void testParseMissingOption() throws ParseException
    {
        String[] arguments = new String[]{"--missing"};
        CommandLineParser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");

        assertThrows(MissingOptionException.class, () -> parser.parse(options, arguments, false));
    }

    @Test
    public void testParseMissingRequiredOption() throws ParseException
    {
        String[] arguments = new String[]{"--option"};
        CommandLineParser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Option A", true, "Required option");

        assertThrows(MissingOptionException.class, () -> parser.parse(options, arguments, false));
    }

    @Test
    public void testParseUnrecognizedOption() throws ParseException
    {
        String[] arguments = new String[]{"--unknown"};
        CommandLineParser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");

        assertThrows(UnrecognizedOptionException.class, () -> parser.parse(options, arguments, false));
    }

    @Test
    public void testParseWithProperties() throws ParseException
    {
        String[] arguments = new String[]{"--option1", "1", "--option2", "2"};
        Properties properties = new Properties();
        properties.setProperty("option1", "value1");
        properties.setProperty("option2", "value2");

        CommandLineParser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("option1", true, "Option 1");
        options.addOption("option2", true, "Option 2");

        CommandLine cmd = parser.parse(options, arguments, properties, false);

        assertEquals(Arrays.asList("1"), cmd.getOptions().getOptionValues("option1"));
        assertEquals("value1", cmd.getOptions().getOptionValues("option1")[0]);
        assertEquals(Arrays.asList("2"), cmd.getOptions().getOptionValues("option2"));
        assertEquals("value2", cmd.getOptions().getOptionValues("option2")[0]);
    }

    @Test
    public void testParseWithOptionalArg() throws ParseException
    {
        String[] arguments = new String[]{"-a", "value"};
        CommandLineParser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Option A", true, "Optional arg");

        CommandLine cmd = parser.parse(options, arguments, false);

        assertEquals("value", cmd.getOptions().getOptionValues("a")[0]);
        assertNull(cmd.getOptions().getOptionValues("a")[1]);
    }

    @Test
    public void testParseWithRequiredArg() throws ParseException
    {
        String[] arguments = new String[]{"-a", "arg1", "-a", "arg2"};
        CommandLineParser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Option A", true, "Required arg");

        CommandLine cmd = parser.parse(options, arguments, false);

        assertEquals(Arrays.asList(new String[]{"arg1", "arg2"}), cmd.getOptions().getOptionValues("a"));
    }

    @Test
    public void testParseWithOptionGroup() throws ParseException
    {
        String[] arguments = new String[]{"--group", "-g", "1", "--option1", "1"};
        CommandLineParser parser = new DefaultParser();
        Options options = new Options();

        OptionGroup group = new OptionGroup("group", "Group");
        group.setRequired(true);
        group.addOption(new Option("g", "group", false, "Group option"));
        options.addOptionGroup(group);
        options.addOption("option1", true, "Option 1");

        CommandLine cmd = parser.parse(options, arguments, false);

        Option groupOpt = cmd.getOption("group");
        assertEquals(true, groupOpt.isSelected());
        assertEquals(Arrays.asList("1"), cmd.getOptions().getOptionValues("option1"));
    }

    @Test
    public void testParseWithMultipleOptionGroups() throws ParseException
    {
        String[] arguments = new String[]{"--group1", "-g1", "1", "--group2", "-g2", "2"};
        CommandLineParser parser = new DefaultParser();
        Options options = new Options();

        OptionGroup group1 = new OptionGroup("group1", "Group 1");
        group1.setRequired(false);
        group1.addOption(new Option("g1", "group1", false, "Group option 1"));
        options.addOptionGroup(group1);

        OptionGroup group2 = new OptionGroup("group2", "Group 2");
        group2.setRequired(false);
        group2.addOption(new Option("g2", "group2", false, "Group option 2"));
        options.addOptionGroup(group2);

        CommandLine cmd = parser.parse(options, arguments, false);

        Option group1Opt = cmd.getOption("group1");
        Option group2Opt = cmd.getOption("group2");

        assertEquals(true, group1Opt != null);
        assertEquals(true, group2Opt != null);
        assertEquals("1", group1Opt.getValue());
        assertEquals("2", group2Opt.getValue());
    }

    @Test
    public void testParseWithStopAtNonOption() throws ParseException
    {
        String[] arguments = new String[]{"--option", "value", "arg1", "arg2"};
        CommandLineParser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("option", true, "Option");

        CommandLine cmd = parser.parse(options, arguments, true);

        assertEquals(Arrays.asList("value"), cmd.getOptions().getOptionValues("option"));
        assertEquals(Arrays.asList("arg1", "arg2"), cmd.getArgs());
    }
}