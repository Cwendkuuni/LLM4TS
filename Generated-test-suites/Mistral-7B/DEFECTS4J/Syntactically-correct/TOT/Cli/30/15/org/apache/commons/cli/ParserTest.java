package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.NoSuchOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link Parser}.
 */
public class ParserTest
{
    private Parser parser;

    @Before
    public void setUp()
    {
        parser = new DefaultParser();
    }

    /**
     * Test if the parser correctly handles a simple command line with no options.
     */
    @Test
    public void testNullOptions()
    {
        String[] arguments = new String[0];
        CommandLine cmd = parser.parse(new Options(), arguments);
        assertEquals(new CommandLine(), cmd);
    }

    /**
     * Test if the parser correctly handles a command line with a single option.
     */
    @Test
    public void testSimpleOption() throws ParseException
    {
        String[] arguments = {"--option"};
        Options options = new Options();
        Option opt = new Option("o", "option", false, "An option");
        options.addOption(opt);
        CommandLine cmd = parser.parse(options, arguments);
        assertTrue(cmd.hasOption("option"));
    }

    /**
     * Test if the parser correctly handles a command line with a single option with an argument.
     */
    @Test
    public void testSimpleOptionWithArg() throws ParseException
    {
        String[] arguments = {"--option", "value"};
        Options options = new Options();
        Option opt = new Option("o", "option", true, "An option with an argument");
        options.addOption(opt);
        CommandLine cmd = parser.parse(options, arguments);
        assertEquals("value", cmd.getOptionValue("option"));
    }

    /**
     * Test if the parser correctly handles a command line with multiple options.
     */
    @Test
    public void testMultipleOptions() throws ParseException
    {
        String[] arguments = {"--option1", "--option2"};
        Options options = new Options();
        Option opt1 = new Option("o1", "option1", false, "Option 1");
        Option opt2 = new Option("o2", "option2", false, "Option 2");
        options.addOption(opt1);
        options.addOption(opt2);
        CommandLine cmd = parser.parse(options, arguments);
        assertTrue(cmd.hasOption("option1"));
        assertTrue(cmd.hasOption("option2"));
    }

    /**
     * Test if the parser correctly handles a command line with multiple options with arguments.
     */
    @Test
    public void testMultipleOptionsWithArgs() throws ParseException
    {
        String[] arguments = {"--option1", "value1", "--option2", "value2"};
        Options options = new Options();
        Option opt1 = new Option("o1", "option1", true, "Option 1 with an argument");
        Option opt2 = new Option("o2", "option2", true, "Option 2 with an argument");
        options.addOption(opt1);
        options.addOption(opt2);
        CommandLine cmd = parser.parse(options, arguments);
        assertEquals("value1", cmd.getOptionValue("option1"));
        assertEquals("value2", cmd.getOptionValue("option2"));
    }

    /**
     * Test if the parser correctly handles a command line with a required option.
     */
    @Test(expected = MissingOptionException.class)
    public void testMissingRequiredOption() throws ParseException
    {
        String[] arguments = {"--option2"};
        Options options = new Options();
        Option opt = new Option("o", "option", true, "An option", true);
        options.addOption(opt);
        CommandLine cmd = parser.parse(options, arguments);
        fail("Missing required option 'option'.");
    }

    /**
     * Test if the parser correctly handles a command line with a required option that is present.
     */
    @Test
    public void testPresentRequiredOption() throws ParseException
    {
        String[] arguments = {"--option", "value"};
        Options options = new Options();
        Option opt = new Option("o", "option", true, "An option", true);
        options.addOption(opt);
        CommandLine cmd = parser.parse(options, arguments);
        assertEquals("value", cmd.getOptionValue("option"));
    }

    /**
     * Test if the parser correctly handles a command line with an option group.
     */
    @Test
    public void testOptionGroup() throws ParseException
    {
        String[] arguments = {"--group", "--option"};
        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "A group");
        Option opt = new Option("o", "option", false, "An option in the group");
        group.addOption(opt);
        options.addOptionGroup(group);
        CommandLine cmd = parser.parse(options, arguments);
        assertTrue(cmd.hasOption("option"));
        assertTrue(cmd.hasOptionGroup("group"));
    }

    /**
     * Test if the parser correctly handles a command line with an option group with a selected option.
     */
    @Test
    public void testOptionGroupWithSelectedOption() throws ParseException
    {
        String[] arguments = {"--group", "--option"};
        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "A group");
        Option opt = new Option("o", "option", false, "An option in the group");
        group.addOption(opt);
        group.setSelected(opt);
        options.addOptionGroup(group);
        CommandLine cmd = parser.parse(options, arguments);
        assertTrue(cmd.hasOption("option"));
        assertTrue(cmd.hasOptionGroup("group"));
        assertEquals(opt, cmd.getOptionGroup("group").getSelectedOption());
    }

    /**
     * Test if the parser correctly handles a command line with an option group and a selected option and an option
     * from the group.
     */
    @Test
    public void testOptionGroupWithSelectedOptionAndOption() throws ParseException
    {
        String[] arguments = {"--group", "--option1", "--option2"};
        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "A group");
        Option opt1 = new Option("o1", "option1", false, "Option 1 in the group");
        Option opt2 = new Option("o2", "option2", false, "Option 2 in the group");
        group.addOption(opt1);
        group.addOption(opt2);
        group.setSelected(opt1);
        options.addOptionGroup(group);
        CommandLine cmd = parser.parse(options, arguments);
        assertTrue(cmd.hasOption("option1"));
        assertTrue(cmd.hasOptionGroup("group"));
        assertEquals(opt1, cmd.getOptionGroup("group").getSelectedOption());
        assertFalse(cmd.hasOption("option2"));
    }

    /**
     * Test if the parser correctly handles a command line with an unknown option.
     */
    @Test(expected = UnrecognizedOptionException.class)
    public void testUnknownOption() throws ParseException
    {
        String[] arguments = {"--unknown"};
        Options options = new Options();
        CommandLine cmd = parser.parse(options, arguments);
        fail("Unknown option: unknown.");
    }

    /**
     * Test if the parser correctly handles a command line with an unknown option and other options.
     */
    @Test
    public void testUnknownOptionWithOtherOptions() throws ParseException
    {
        String[] arguments = {"--unknown", "--option1", "--option2"};
        Options options = new Options();
        Option opt1 = new Option("o1", "option1", false, "Option 1");
        Option opt2 = new Option("o2", "option2", false, "Option 2");
        options.addOption(opt1);
        options.addOption(opt2);
        CommandLine cmd = parser.parse(options, arguments);
        assertFalse(cmd.hasOption("unknown"));
        assertTrue(cmd.hasOption("option1"));
        assertTrue(cmd.hasOption("option2"));
    }

    /**
     * Test if the parser correctly handles a command line with an option that is not recognized as an option but
     * is part of the command line arguments.
     */
    @Test
    public void testNonOptionArgument() throws ParseException
    {
        String[] arguments = {"--option", "value", "arg"};
        Options options = new Options();
        Option opt = new Option("o", "option", true, "An option", true);
        options.addOption(opt);
        CommandLine cmd = parser.parse(options, arguments);
        assertEquals("value", cmd.getOptionValue("option"));
        assertEquals("arg", cmd.getArgList().get(0));
    }

    /**
     * Test if the parser correctly handles a command line with an unknown property.
     */
    @Test(expected = ParseException.class)
    public void testUnknownProperty() throws ParseException
    {
        String[] arguments = {};
        Properties properties = new Properties();
        properties.setProperty("unknown", "value");
        parser.parse(new Options(), arguments, properties, false);
        fail("Unknown property: unknown.");
    }

    /**
     * Test if the parser correctly handles a command line with a property that is recognized as an option.
     */
    @Test
    public void testPropertyAsOption() throws ParseException
    {
        String[] arguments = {"--property", "value"};
        Options options = new Options();
        Option opt = new Option("p", "property", true, "An option with a property name");
        options.addOption(opt);
        CommandLine cmd = parser.parse(options, arguments, null, false);
        assertEquals("value", cmd.getOptionValue("property"));
    }

    /**
     * Test if the parser correctly handles a command line with a property that is not recognized as an option.
     */
    @Test
    public void testPropertyNotAsOption() throws ParseException
    {
        String[] arguments = {"--property", "value"};
        Options options = new Options();
        Option opt = new Option("o", "option", true, "An option", true);
        options.addOption(opt);
        CommandLine cmd = parser.parse(options, arguments, null, false);
        assertEquals("value", cmd.getOptionValue("property"));
        assertFalse(cmd.hasOption("property"));
    }
}