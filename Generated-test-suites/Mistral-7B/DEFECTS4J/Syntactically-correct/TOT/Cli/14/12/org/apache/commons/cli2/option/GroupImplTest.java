package org.apache.commons.cli2.option;

import org.apache.commons.cli2.CommandLineParser;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Options;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class GroupImplTest {

    private Options options;
    private CommandLineParser parser;
    private HelpFormatter formatter;

    @Before
    public void setUp() {
        options = new Options();
        parser = new DefaultParser();
        formatter = new HelpFormatter();

        List<Option> optionsList = new ArrayList<>();
        optionsList.add(new Option("a", "arg1", true, "an argument"));
        optionsList.add(new Option("b", "arg2", true, "another argument"));
        optionsList.add(new OptionGroup("g")
                .requiredOption()
                .options(new Option("x", "group-x", false, "option x in group"),
                         new Option("y", "group-y", false, "option y in group")));
        options.addAll(optionsList);
    }

    @Test
    public void testGroupWithNoArguments() {
        WriteableCommandLine cmd = new DefaultCommandLine();
        parser.parse(options, cmd);
        formatter.printHelp("Test", options);

        assertTrue(cmd.getOptions().isEmpty());
        assertTrue(cmd.hasOption("g"));
        assertFalse(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("b"));
        assertFalse(cmd.hasOption("x"));
        assertFalse(cmd.hasOption("y"));
    }

    @Test
    public void testGroupWithOneArgument() {
        WriteableCommandLine cmd = new DefaultCommandLine();
        cmd.parseArgumentList(new String[]{"--arg1", "value1"});
        parser.parse(options, cmd);

        assertTrue(cmd.getOptions().isEmpty());
        assertTrue(cmd.hasOption("g"));
        assertFalse(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("b"));
        assertFalse(cmd.hasOption("x"));
        assertFalse(cmd.hasOption("y"));
        assertEquals("value1", cmd.getOptionValue("arg1"));
    }

    @Test
    public void testGroupWithBothArguments() {
        WriteableCommandLine cmd = new DefaultCommandLine();
        cmd.parseArgumentList(new String[]{"--arg1", "value1", "--arg2", "value2"});
        parser.parse(options, cmd);

        assertTrue(cmd.getOptions().isEmpty());
        assertTrue(cmd.hasOption("g"));
        assertFalse(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("b"));
        assertFalse(cmd.hasOption("x"));
        assertFalse(cmd.hasOption("y"));
        assertEquals("value1", cmd.getOptionValue("arg1"));
        assertEquals("value2", cmd.getOptionValue("arg2"));
    }

    @Test(expected = OptionException.class)
    public void testGroupWithTooManyArguments() {
        WriteableCommandLine cmd = new DefaultCommandLine();
        cmd.parseArgumentList(new String[]{"--arg1", "value1", "--arg1", "value2"});
        parser.parse(options, cmd);

        assertTrue(cmd.getOptions().isEmpty());
        assertTrue(cmd.hasOption("g"));
        assertFalse(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("b"));
        assertFalse(cmd.hasOption("x"));
        assertFalse(cmd.hasOption("y"));
        assertEquals("value1", cmd.getOptionValue("arg1"));
        assertEquals("value1", cmd.getOptionValue("arg1")); // should throw an exception
    }

    @Test(expected = OptionException.class)
    public void testGroupWithMissingArguments() {
        WriteableCommandLine cmd = new DefaultCommandLine();
        parser.parse(options, cmd);

        assertTrue(cmd.getOptions().isEmpty());
        assertTrue(cmd.hasOption("g"));
        assertFalse(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("b"));
        assertFalse(cmd.hasOption("x"));
        assertFalse(cmd.hasOption("y"));

        cmd.parseArgumentList(new String[]{"--arg1"});
        parser.parse(options, cmd);

        assertTrue(cmd.getOptions().isEmpty());
        assertTrue(cmd.hasOption("g"));
        assertFalse(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("b"));
        assertFalse(cmd.hasOption("x"));
        assertFalse(cmd.hasOption("y"));
        assertFalse(cmd.hasOption("arg1"));
    }

    @Test
    public void testGroupWithOneOption() {
        WriteableCommandLine cmd = new DefaultCommandLine();
        cmd.parseArgumentList(new String[0]);
        parser.parse(options, cmd);
        cmd.setOptionValue("g", null);
        cmd.parseArgumentList(new String[]{"--x"});
        parser.parse(options, cmd);

        assertTrue(cmd.getOptions().isEmpty());
        assertTrue(cmd.hasOption("g"));
        assertFalse(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("b"));
        assertFalse(cmd.hasOption("x"));
        assertFalse(cmd.hasOption("y"));
        assertNull(cmd.getOptionValue("g"));
        assertTrue(cmd.hasOption("x"));
    }

    @Test
    public void testGroupWithBothOptions() {
        WriteableCommandLine cmd = new DefaultCommandLine();
        cmd.parseArgumentList(new String[0]);
        parser.parse(options, cmd);
        cmd.setOptionValue("g", null);
        cmd.parseArgumentList(new String[]{"--x", "--y"});
        parser.parse(options, cmd);

        assertTrue(cmd.getOptions().isEmpty());
        assertTrue(cmd.hasOption("g"));
        assertFalse(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("b"));
        assertFalse(cmd.hasOption("x"));
        assertFalse(cmd.hasOption("y"));
        assertNull(cmd.getOptionValue("g"));
        assertTrue(cmd.hasOption("x"));
        assertTrue(cmd.hasOption("y"));
    }
}