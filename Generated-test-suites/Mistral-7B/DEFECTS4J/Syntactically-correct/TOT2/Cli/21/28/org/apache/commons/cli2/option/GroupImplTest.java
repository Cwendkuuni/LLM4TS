package org.apache.commons.cli2.option;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.ParseException;
import org.apache.commons.cli2.TestUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class GroupImplTest {

    private List<Option> options;
    private Group group;

    @Before
    public void setup() {
        options = new ArrayList<>();
        options.add(OptionBuilder.withLongOpt("opt1")
                .withDescription("Option 1")
                .withArgName("arg1")
                .create());
        options.add(OptionBuilder.withLongOpt("opt2")
                .withDescription("Option 2")
                .withArgName("arg2")
                .create());
        group = new GroupImpl(options, "MyGroup", "This is my group", 1, 2, true);
    }

    @Test
    public void testCanProcessSuccess() {
        final CommandLine commandLine = new CommandLine();
        commandLine.parseArgument(0, "opt1", "arg1");
        assertTrue(group.canProcess(commandLine, "opt1"));
    }

    @Test
    public void testCanProcessFailure() {
        final CommandLine commandLine = new CommandLine();
        assertFalse(group.canProcess(commandLine, "arg1"));
        assertFalse(group.canProcess(commandLine, "opt3"));
    }

    @Test(expected = OptionException.class)
    public void testValidateRequiredOptionMissing() throws OptionException {
        final CommandLine commandLine = new CommandLine();
        group.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidateRequiredOptionTooMany() throws OptionException {
        final CommandLine commandLine = new CommandLine();
        commandLine.parseArgument(0, "opt1", "arg1");
        commandLine.parseArgument(0, "opt1", "arg2");
        group.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidateOptionalOptionMissing() throws OptionException {
        final CommandLine commandLine = new CommandLine();
        group.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidateOptionalOptionTooMany() throws OptionException {
        final CommandLine commandLine = new CommandLine();
        commandLine.parseArgument(0, "opt1", "arg1");
        commandLine.parseArgument(0, "opt1", "arg2");
        commandLine.parseArgument(0, "opt1", "arg3");
        group.validate(commandLine);
    }

    @Test
    public void testProcessSuccess() {
        final CommandLine commandLine = new CommandLine();
        commandLine.parseArgument(0, "opt1", "arg1");
        commandLine.parseArgument(1, "opt2", "arg2");
        group.process(commandLine, commandLine.iterator());

        assertEquals(2, group.getOptions().size());
        assertEquals("Option 1", group.getOptions().get(0).getLongOpt());
        assertEquals("arg1", group.getOptions().get(0).getValue());
        assertEquals("Option 2", group.getOptions().get(1).getLongOpt());
        assertEquals("arg2", group.getOptions().get(1).getValue());
    }

    @Test
    public void testProcessWithAnonymousOptions() {
        final CommandLine commandLine = new CommandLine();
        options.add(OptionBuilder.withLongOpt("opt1")
                .withDescription("Option 1")
                .withArgName("arg1")
                .create());
        options.add(new OptionBuilder("a")
                .withDescription("Anonymous Option A")
                .create());
        options.add(new OptionBuilder("b")
                .withDescription("Anonymous Option B")
                .create());
        group = new GroupImpl(options, "MyGroup", "This is my group", 1, 2, true);

        commandLine.parseArgument(0, "opt1", "arg1");
        commandLine.parseArgument(1, "a", "argA");
        group.process(commandLine, commandLine.iterator());

        assertEquals(3, group.getOptions().size());
        assertEquals("Option 1", group.getOptions().get(0).getLongOpt());
        assertEquals("arg1", group.getOptions().get(0).getValue());
        assertEquals("Anonymous Option A", group.getOptions().get(1).getLongOpt());
        assertNull(group.getOptions().get(1).getValue());
        assertEquals("Anonymous Option B", group.getOptions().get(2).getLongOpt());
        assertNull(group.getOptions().get(2).getValue());
    }

    @Test
    public void testProcessWithOutOfOrderAnonymousOptions() {
        final CommandLine commandLine = new CommandLine();
        options.add(OptionBuilder.withLongOpt("opt1")
                .withDescription("Option 1")
                .withArgName("arg1")
                .create());
        options.add(new OptionBuilder("a")
                .withDescription("Anonymous Option A")
                .create());
        options.add(new OptionBuilder("b")
                .withDescription("Anonymous Option B")
                .create());
        group = new GroupImpl(options, "MyGroup", "This is my group", 1, 2, true);

        commandLine.parseArgument(0, "opt1", "arg1");
        commandLine.parseArgument(1, "b", "argB");
        commandLine.parseArgument(2, "a", "argA");
        group.process(commandLine, commandLine.iterator());

        assertEquals(3, group.getOptions().size());
        assertEquals("Option 1", group.getOptions().get(0).getLongOpt());
        assertEquals("arg1", group.getOptions().get(0).getValue());
        assertEquals("Anonymous Option A", group.getOptions().get(1).getLongOpt());
        assertNull(group.getOptions().get(1).getValue());
        assertEquals("Anonymous Option B", group.getOptions().get(2).getLongOpt());
        assertNull(group.getOptions().get(2).getValue());
    }

    @Test
    public void testHelp() {
        final HelpFormatter formatter = new HelpFormatter();
        final String help = formatter.printHelp("MyGroupTest", group, true);
        TestUtils.assertHelpLines(
                "MyGroupTest (help)\n" +
                "This is my group\n" +
                "\n" +
                "options:\n" +
                "  -opt1 <arg1>        Option 1\n" +
                "  --opt2 <arg2>        Option 2\n", help);
    }
}