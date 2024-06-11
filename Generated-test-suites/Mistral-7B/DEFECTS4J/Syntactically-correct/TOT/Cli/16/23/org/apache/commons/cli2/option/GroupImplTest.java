package org.apache.commons.cli2.option;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.ParseException;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

public class GroupImplTest {

    private List<Option> options;
    private GroupImpl group;

    @Before
    public void setUp() {
        options = new ArrayList<>();
        options.add(OptionBuilder.withName("a").withDescription("Option A").build());
        options.add(OptionBuilder.withName("b").withDescription("Option B").build());
        options.add(OptionBuilder.withName("c").withDescription("Option C").build());
        group = new GroupImpl(options, "MyGroup", null, 1, 3);
    }

    @Test
    public void testCanProcess_validOption() {
        CommandLine commandLine = new CommandLine();
        commandLine.parseArgument(null, "--a", true);
        assertTrue(group.canProcess(commandLine, "--a"));
    }

    @Test
    public void testCanProcess_burstingOption() {
        CommandLine commandLine = new CommandLine();
        commandLine.parseArgument(null, "--a", true);
        commandLine.parseArgument(null, "--b", true);
        commandLine.parseArgument(null, "--a"); // bursting
        assertTrue(group.canProcess(commandLine, "--a"));
    }

    @Test
    public void testCanProcess_validAnonymousArgument() {
        CommandLine commandLine = new CommandLine();
        commandLine.parseArgument(null, "arg");
        assertTrue(group.canProcess(commandLine, "arg"));
    }

    @Test
    public void testCanProcess_invalidOption() {
        CommandLine commandLine = new CommandLine();
        assertFalse(group.canProcess(commandLine, "--x"));
    }

    @Test
    public void testCanProcess_invalidAnonymousArgument() {
        CommandLine commandLine = new CommandLine();
        assertFalse(group.canProcess(commandLine, "invalidArg"));
    }

    @Test
    public void testCanProcess_noOptions() {
        CommandLine commandLine = new CommandLine();
        assertFalse(group.canProcess(commandLine, null));
    }

    @Test
    public void testProcess_validOption() throws OptionException {
        CommandLine commandLine = new CommandLine();
        commandLine.parseArgument(null, "--a", true);
        group.process(commandLine, commandLine.iterator());
        assertEquals(1, group.getOptions().size());
        assertEquals("Option A", group.getOptions().get(0).getDescription());
    }

    @Test
    public void testProcess_burstingOption() throws OptionException {
        CommandLine commandLine = new CommandLine();
        commandLine.parseArgument(null, "--a", true);
        commandLine.parseArgument(null, "--b", true);
        commandLine.parseArgument(null, "--a"); // bursting
        group.process(commandLine, commandLine.iterator());
        assertEquals(2, group.getOptions().size());
        assertEquals("Option A", group.getOptions().get(0).getDescription());
        assertEquals("Option A", group.getOptions().get(1).getDescription());
    }

    @Test
    public void testProcess_validAnonymousArgument() throws OptionException {
        CommandLine commandLine = new CommandLine();
        commandLine.parseArgument(null, "arg");
        group.process(commandLine, commandLine.iterator());
        assertNull(group.getAnonymous().get(0).getValue());
    }

    @Test
    public void testValidate_validOptions() throws OptionException {
        CommandLine commandLine = new CommandLine();
        commandLine.parseArgument(null, "--a", true);
        commandLine.parseArgument(null, "--b", true);
        group.validate(commandLine);
    }

    @Test
    public void testValidate_tooFewOptions() throws OptionException {
        CommandLine commandLine = new CommandLine();
        group.validate(commandLine);
        assertThrows(OptionException.class, () -> group.validate(commandLine));
    }

    @Test
    public void testValidate_tooManyOptions() throws OptionException {
        CommandLine commandLine = new CommandLine();
        commandLine.parseArgument(null, "--a", true);
        commandLine.parseArgument(null, "--b", true);
        commandLine.parseArgument(null, "--c", true);
        commandLine.parseArgument(null, "--a"); // extra option
        group.validate(commandLine);
        assertThrows(OptionException.class, () -> group.validate(commandLine));
    }
}