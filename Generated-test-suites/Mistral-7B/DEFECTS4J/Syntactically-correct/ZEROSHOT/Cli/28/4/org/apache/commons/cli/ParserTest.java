package org.apache.commons.cli.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.SimpleOption;
import org.apache.commons.cli.TestUtils;
import org.junit.Test;

public class ParserTest
{
    @Test
    public void testParse() throws ParseException
    {
        String[] args = { "-a", "1", "--b", "2", "--c", "3", "--d", "4", "--e" };
        Options options = new Options();
        options.addOption("a", true, "Option A.");
        options.addOption("b", false, "Option B.");
        options.addOption("c", null, "Option C.", true);
        options.addOption("d", null, "Option D.", true);
        options.addOption("e", null, "Option E.", true);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertTrue(cmd.hasOption("c"));
        assertTrue(cmd.hasOption("d"));
        assertTrue(cmd.hasOption("e"));
        assertSame(args[0], cmd.getOptionValue("a"));
        assertNull(cmd.getOptionValue("b"));
        assertSame(args[2], cmd.getOptionValue("c"));
        assertSame(args[4], cmd.getOptionValue("d"));
        assertSame(args[6], cmd.getOptionValue("e"));
        assertFalse(cmd.hasArgs());
    }

    @Test
    public void testParseWithMissingArgument() throws ParseException
    {
        String[] args = { "--c" };
        Options options = new Options();
        options.addOption("c", true, "Option C.", true);

        CommandLineParser parser = new DefaultParser();
        assertThrows(MissingArgumentException.class, () -> parser.parse(options, args, false));
    }

    @Test
    public void testParseWithMissingOption() throws ParseException
    {
        String[] args = { "-x", "1" };
        Options options = new Options();
        options.addOption("a", true, "Option A.");

        CommandLineParser parser = new DefaultParser();
        assertThrows(UnrecognizedOptionException.class, () -> parser.parse(options, args, false));
    }

    @Test
    public void testParseWithMissingRequiredOption() throws ParseException
    {
        String[] args = { "--d" };
        Options options = new Options();
        options.addOption("d", true, "Option D.");
        options.addOption("e", true, "Option E.");

        CommandLineParser parser = new DefaultParser();
        assertThrows(MissingOptionException.class, () -> parser.parse(options, args, false));
    }

    @Test
    public void testParseWithMultipleValues() throws ParseException
    {
        String[] args = { "-a", "1", "2", "-a", "3", "4" };
        Options options = new Options();
        options.addOption("a", true, "Option A.");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        List<String> values = cmd.getOptionValues("a");
        assertTrue(values.size() == 4);
        assertSame(args[0], values.get(0));
        assertSame(args[2], values.get(1));
        assertSame(args[4], values.get(2));
        assertSame(args[6], values.get(3));
    }

    @Test
    public void testParseWithProperties() throws ParseException
    {
        String[] args = { "--prop1", "value1", "--prop2", "value2" };
        Properties props = new Properties();
        props.setProperty("prop1", "value1");
        props.setProperty("prop2", "value2");

        Options options = new Options();
        options.addOption("prop1", true, "Option Prop1.", true);
        options.addOption("prop2", true, "Option Prop2.", true);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, props, false);

        assertTrue(cmd.hasOption("prop1"));
        assertTrue(cmd.hasOption("prop2"));
        assertSame(props.getProperty("prop1"), cmd.getOptionValue("prop1"));
        assertSame(props.getProperty("prop2"), cmd.getOptionValue("prop2"));
    }

    @Test
    public void testParseWithGroup() throws ParseException
    {
        String[] args = { "-g", "group1", "-a", "1", "-g", "group2" };
        Options options = new Options();

        OptionGroup group1 = new OptionGroup("group1", "", "Group 1", "Options for group 1.");
        group1.addOption(new SimpleOption("a", true, "Option A."));
        options.addOptionGroup(group1);

        OptionGroup group2 = new OptionGroup("group2", "", "Group 2", "Options for group 2.");
        options.addOptionGroup(group2);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOptionGroup("group1"));
        assertFalse(cmd.hasOptionGroup("group2"));
    }

    @Test
    public void testParseWithHelpFormat() throws ParseException
    {
        String[] args = {};
        Options options = new Options();
        options.addOption("h", "help", false, "Prints this help message.");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, null, false);

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("java.exe", options);

        String help = TestUtils.getHelpText(System.out);
        assertTrue(help.contains("java.exe"));
        assertTrue(help.contains("Option A."));
        assertTrue(help.contains("Prints this help message."));
    }
}