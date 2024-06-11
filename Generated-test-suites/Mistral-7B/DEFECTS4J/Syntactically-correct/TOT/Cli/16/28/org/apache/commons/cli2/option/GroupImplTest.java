package org.apache.commons.cli2.option;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
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
        options.add(createOption("option1", "desc1", false));
        options.add(createOption("option2", "desc2", true));
        options.add(createOption("arg1", null, Argument.TYPE_STRING, "arg1desc"));
        group = new GroupImpl(options, "group", "group description", 0, 1);
    }

    @Test
    public void testCanProcessWithNoArguments() {
        CommandLine commandLine = new CommandLine();
        assertTrue(group.canProcess(commandLine, null));
    }

    @Test
    public void testCanProcessWithSingleArgument() {
        CommandLine commandLine = new CommandLine();
        commandLine.parse("--option1 value1");
        assertTrue(group.canProcess(commandLine, "option1"));
    }

    @Test
    public void testCanProcessWithMultipleArguments() {
        CommandLine commandLine = new CommandLine();
        commandLine.parse("--option1 value1 --option1 value2");
        assertTrue(group.canProcess(commandLine, "option1"));
    }

    @Test
    public void testCanProcessWithInvalidArgument() {
        CommandLine commandLine = new CommandLine();
        commandLine.parse("--invalid");
        assertFalse(group.canProcess(commandLine, "option1"));
    }

    @Test
    public void testCanProcessWithOptionAfterArgument() {
        CommandLine commandLine = new CommandLine();
        commandLine.parse("--arg1 arg1value --option1");
        assertTrue(group.canProcess(commandLine, "option1"));
    }

    @Test(expected = OptionException.class)
    public void testValidateWithTooFewArguments() {
        CommandLine commandLine = new CommandLine();
        group.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithTooManyArguments() {
        CommandLine commandLine = new CommandLine();
        commandLine.parse("--option1 value1 --option1 value2 --option1 value3");
        group.validate(commandLine);
    }

    @Test
    public void testProcessWithNoArguments() {
        CommandLine commandLine = new CommandLine();
        group.process(commandLine, commandLine.iterator());
        assertNull(commandLine.getOptionValue("option1"));
    }

    @Test
    public void testProcessWithSingleArgument() {
        CommandLine commandLine = new CommandLine();
        commandLine.parse("--option1 value1");
        group.process(commandLine, commandLine.iterator());
        assertEquals("value1", commandLine.getOptionValue("option1"));
    }

    @Test
    public void testProcessWithMultipleArguments() {
        CommandLine commandLine = new CommandLine();
        commandLine.parse("--option1 value1 --option1 value2");
        group.process(commandLine, commandLine.iterator());
        List<String> values = commandLine.getListOptions("option1");
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testProcessWithInvalidArgument() {
        CommandLine commandLine = new CommandLine();
        commandLine.parse("--invalid");
        group.process(commandLine, commandLine.iterator());
        assertNull(commandLine.getOptionValue("option1"));
    }

    @Test
    public void testProcessWithArgumentAfterOption() {
        CommandLine commandLine = new CommandLine();
        commandLine.parse("--arg1 arg1value --option1");
        group.process(commandLine, commandLine.iterator());
        assertNull(commandLine.getOptionValue("option1"));
    }

    @Test
    public void testAppendUsage() {
        final StringBuilder buffer = new StringBuilder();
        group.appendUsage(buffer, Collections.emptySet(null), null);
        String expected =
                "group description\n" +
                "------------------\n" +
                "\n" +
                "option1: desc1\n" +
                "    (value)\n" +
                "\n" +
                "arg1: arg1desc\n";
        assertEquals(expected, buffer.toString());
    }

    @Test
    public void testHelpLines() {
        final StringBuilder buffer = new StringBuilder();
        group.helpLines(1, Collections.emptySet(null), null, "\n");
        String expected =
                "group description\n" +
                "------------------\n" +
                "\n" +
                "option1: desc1\n" +
                "    (value)\n" +
                "\n" +
                "arg1: arg1desc\n";
        assertEquals(expected, buffer.toString());
    }

    private Option createOption(String longName, String description, boolean isOptional) {
        return OptionBuilder.withLongOpt(longName)
                .withDescription(description)
                .isOptional(isOptional)
                .create(false);
    }
}