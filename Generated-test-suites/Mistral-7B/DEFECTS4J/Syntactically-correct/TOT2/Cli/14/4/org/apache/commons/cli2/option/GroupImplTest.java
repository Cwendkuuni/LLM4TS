package org.apache.commons.cli2.option;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.ParseContext;
import org.apache.commons.cli2.exceptions.OptionException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class GroupImplTest {

    private List<Option> options;

    @Before
    public void setup() {
        options = Arrays.asList(
                OptionBuilder.withLongOpt("option1")
                        .withArgName("ARG1")
                        .withDescription("Option 1")
                        .create(),
                OptionBuilder.withLongOpt("option2")
                        .withArgName("ARG2")
                        .withDescription("Option 2")
                        .create(),
                OptionBuilder.withLongOpt("group")
                        .withDescription("Group")
                        .create(),
                OptionBuilder.withLongOpt("arg")
                        .withDescription("Arg for group")
                        .isRequired(false)
                        .create()
        );
    }

    @Test
    public void testGroupWithValidArguments() throws OptionException {
        final GroupImpl group = new GroupImpl(options, "group", null, 1, 1);

        final CommandLine commandLine = new CommandLine();
        group.process(commandLine, commandLine.parser().getInput());

        assertEquals("Group", group.getPreferredName());
        assertEquals("Option 1 ARG1, Option 2 ARG2", group.getDescription());
        assertTrue(commandLine.hasOption("option1"));
        assertTrue(commandLine.hasOption("option2"));
        assertNull(commandLine.getOptionValue("arg"));
    }

    @Test
    public void testGroupWithInvalidArguments() throws OptionException {
        final GroupImpl group = new GroupImpl(options, "group", null, 1, 1);

        final CommandLine commandLine = new CommandLine();
        commandLine.parse(group.parser().parse("--group invalid"));

        assertFalse(commandLine.isValid());
        assertFalse(commandLine.hasOption("option1"));
        assertFalse(commandLine.hasOption("option2"));
        assertNull(commandLine.getOptionValue("arg"));
    }

    @Test(expected = OptionException.class)
    public void testGroupWithMissingRequiredArguments() throws OptionException {
        final GroupImpl group = new GroupImpl(options, "group", null, 1, 1);

        final CommandLine commandLine = new CommandLine();
        commandLine.parse(group.parser().parse("--group"));
    }

    @Test
    public void testGroupWithMissingOptionalArguments() throws OptionException {
        final GroupImpl group = new GroupImpl(options, "group", null, 1, Integer.MAX_VALUE);

        final CommandLine commandLine = new CommandLine();
        group.process(commandLine, commandLine.parser().getInput());

        assertEquals("Group", group.getPreferredName());
        assertEquals("Option 1 ARG1, Option 2 ARG2", group.getDescription());
        assertFalse(commandLine.hasOption("option1"));
        assertFalse(commandLine.hasOption("option2"));
        assertNull(commandLine.getOptionValue("arg"));
    }

    @Test
    public void testGroupWithMultipleValidArguments() throws OptionException {
        final GroupImpl group = new GroupImpl(options, "group", null, 1, 1);

        final CommandLine commandLine = new CommandLine();
        commandLine.parse(group.parser().parse("--group option1 arg1 --group option2 arg2"));

        assertEquals("Group", group.getPreferredName());
        assertEquals("Option 1 ARG1, Option 2 ARG2", group.getDescription());
        assertTrue(commandLine.hasOption("option1"));
        assertTrue(commandLine.hasOption("option2"));
        assertEquals("arg1", commandLine.getOptionValue("arg"));
    }

    @Test
    public void testGroupWithBurstedArguments() throws OptionException {
        final List<Option> groupOptions = new ArrayList<>();
        groupOptions.addAll(options);
        groupOptions.add(OptionBuilder.withLongOpt("burst")
                .withArgName("ARG")
                .withDescription("Burst")
                .create());

        final GroupImpl group = new GroupImpl(groupOptions, "group", null, 1, Integer.MAX_VALUE);

        final CommandLine commandLine = new CommandLine();
        commandLine.parse(group.parser().parse("--group option1 arg1 --group option1 arg2 --group burst arg3"));

        assertEquals("Group", group.getPreferredName());
        assertEquals("Option 1 ARG1, Option 1 ARG2, Burst ARG", group.getDescription());
        assertTrue(commandLine.hasOption("option1"));
        assertTrue(commandLine.hasOption("burst"));
        assertEquals("arg3", commandLine.getOptionValue("arg"));
    }

    @Test
    public void testGroupWithNestedGroups() throws OptionException {
        final List<Option> groupOptions = new ArrayList<>();
        final List<Option> groupOptions2 = Arrays.asList(
                OptionBuilder.withLongOpt("group2")
                        .withDescription("Group 2")
                        .create(),
                OptionBuilder.withLongOpt("arg2")
                        .withDescription("Arg for group 2")
                        .isRequired(false)
                        .create()
        );

        groupOptions.addAll(options);
        groupOptions.addAll(groupOptions2);

        final GroupImpl group = new GroupImpl(groupOptions, "group", null, 1, Integer.MAX_VALUE);

        final CommandLine commandLine = new CommandLine();
        commandLine.parse(group.parser().parse("--group option1 arg1 --group group2 arg3 --group group2 arg4"));

        assertEquals("Group", group.getPreferredName());
        assertEquals("Option 1 ARG1, Group 2 ARG3, Arg for group 2 ARG4", group.getDescription());
        assertTrue(commandLine.hasOption("option1"));
        assertTrue(commandLine.hasOption("arg2"));
        assertTrue(commandLine.hasOption("group2"));
        assertTrue(commandLine.hasOption("arg2"));
        assertEquals("arg4", commandLine.getOptionValue("arg"));
    }

    @Test
    public void testParseHelp() {
        final ParseContext parseContext = new ParseContext();
        final HelpFormatter formatter = new HelpFormatter();

        final GroupImpl group = new GroupImpl(options, "group", null, 1, Integer.MAX_VALUE);

        formatter.printHelp("Test", group.parser().getUsageLine(), true);
        System.out.println("");
        formatter.printHelp("Test", group.parser().getUsageLine(), false);

        final String help = Arrays.toString(System.in.readAllLines());

        final String expectedHelp = "Test:\n" +
                "optional: group (Option 1 ARG1, Option 2 ARG2)\n" +
                "java Test --help\n" +
                "java Test [options] args...\n\n" +
                "Options:\n" +
                "  -option1, --option1 ARG1\n" +
                "      Option 1\n" +
                "  -option2, --option2 ARG2\n" +
                "      Option 2\n" +
                "  --group\n" +
                "      Group\n" +
                "  --arg\n" +
                "      Arg for group (default: null)\n";

        assertEquals(expectedHelp, help);
    }
}