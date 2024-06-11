package org.apache.commons.cli2.option;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class GroupImplTest {

    @Test
    public void testGroupImplCreation() {
        final List<Option> options = new ArrayList<>();
        options.add(new Option("a", "option A", true, "A"));
        options.add(new Option("b", "option B", false, "B"));
        options.add(new Option("c", "option C", true, "C"));

        final Group group = new GroupImpl(options, "MyGroup", "Description of my group", 1, 3, true);

        assertEquals("MyGroup", group.getPreferredName());
        assertEquals("Description of my group", group.getDescription());
        assertEquals(1, group.getMinimum());
        assertEquals(3, group.getMaximum());
        assertEquals(Collections.unmodifiableList(options), group.getOptions());
        assertEquals(Collections.unmodifiableList(Collections.emptyList()), group.getAnonymous());
        assertEquals(Collections.unmodifiableSet(Collections.singleton("--")), group.getPrefixes());
    }

    @Test
    public void testCanProcess() {
        final List<Option> options = new ArrayList<>();
        options.add(new Option("a", "option A", true, "A"));
        options.add(new Option("b", "option B", false, "B"));
        options.add(new Option("c", "option C", true, "C"));

        final Group group = new GroupImpl(options, "MyGroup", "Description of my group", 1, 3, true);

        // test with valid options
        final WriteableCommandLine commandLine = new DefaultCommandLine();
        commandLine.parseArgument(new String[]{"--a"});
        assertTrue(group.canProcess(commandLine, "--a"));

        // test with options that trigger other options
        commandLine.parseArgument(new String[]{"--b"});
        assertTrue(group.canProcess(commandLine, "--c"));

        // test with valid triggers
        assertTrue(group.canProcess(commandLine, "a"));
        assertTrue(group.canProcess(commandLine, "b"));
        assertTrue(group.canProcess(commandLine, "c"));

        // test with invalid triggers
        assertFalse(group.canProcess(commandLine, "d"));

        // test if it processes the same trigger multiple times
        assertFalse(group.canProcess(commandLine, "a"));

        // test with empty command line
        assertFalse(group.canProcess(commandLine, ""));

        // test with null command line
        assertFalse(group.canProcess(null, ""));
    }

    @Test
    public void testProcess() {
        final List<Option> options = new ArrayList<>();
        options.add(new Option("a", "option A", true, "A"));
        options.add(new Option("b", "option B", false, "B"));
        options.add(new Option("c", "option C", true, "C"));

        final Group group = new GroupImpl(options, "MyGroup", "Description of my group", 1, 3, true);

        final WriteableCommandLine commandLine = new DefaultCommandLine();

        // test with valid options
        commandLine.parseArgument(new String[]{"--a"});
        group.process(commandLine, commandLine.getArgs().listIterator());
        assertEquals(1, commandLine.getOptions().size());
        assertEquals("--a", commandLine.getOptions().get(0).getOpt());

        // test with options that trigger other options
        commandLine.parseArgument(new String[]{"--b"});
        group.process(commandLine, commandLine.getArgs().listIterator());
        assertEquals(1, commandLine.getOptions().size());
        assertEquals("--b", commandLine.getOptions().get(0).getOpt());

        // test with valid triggers
        commandLine.parseArgument(new String[]{"--a"});
        group.process(commandLine, commandLine.getArgs().listIterator());
        assertEquals(1, commandLine.getOptions().size());
        assertEquals("--a", commandLine.getOptions().get(0).getOpt());
        commandLine.parseArgument(new String[]{"--c"});
        group.process(commandLine, commandLine.getArgs().listIterator());
        assertEquals(2, commandLine.getOptions().size());
        assertEquals("--a", commandLine.getOptions().get(0).getOpt());
        assertEquals("--c", commandLine.getOptions().get(1).getOpt());

        // test with empty command line
        commandLine.clear();
        assertThrows(OptionException.class, () -> group.process(commandLine, commandLine.getArgs().listIterator()));

        // test with null command line
        commandLine = null;
        assertThrows(NullPointerException.class, () -> group.process(commandLine, commandLine.getArgs().listIterator()));
    }

    @Test
    public void testValidate() {
        final List<Option> options = new ArrayList<>();
        options.add(new Option("a", "option A", true, "A"));
        options.add(new Option("b", "option B", false, "B"));
        options.add(new Option("c", "option C", true, "C"));

        final Group group = new GroupImpl(options, "MyGroup", "Description of my group", 1, 3, true);

        final WriteableCommandLine commandLine = new DefaultCommandLine();

        // test with valid arguments
        commandLine.parseArgument(new String[]{"--a", "arg1"});
        group.validate(commandLine);

        // test with too few arguments
        commandLine.clear();
        assertThrows(OptionException.class, () -> group.validate(commandLine));

        // test with too many arguments
        commandLine.parseArgument(new String[]{"--a", "arg1", "arg2"});
        assertThrows(OptionException.class, () -> group.validate(commandLine));

        // test with multiple triggers
        commandLine.parseArgument(new String[]{"--a", "--b"});
        assertThrows(OptionException.class, () -> group.validate(commandLine));

        // test with invalid triggers
        commandLine.parseArgument(new String[]{"--d"});
        assertThrows(OptionException.class, () -> group.validate(commandLine));
    }

    @Test
    public void testGetOptions() {
        final List<Option> options = new ArrayList<>();
        options.add(new Option("a", "option A", true, "A"));
        options.add(new Option("b", "option B", false, "B"));
        options.add(new Option("c", "option C", true, "C"));

        final Group group = new GroupImpl(options, "MyGroup", "Description of my group", 1, 3, true);

        assertEquals(Collections.unmodifiableList(options), group.getOptions());
    }

    @Test
    public void testFindOption() {
        final List<Option> options = new ArrayList<>();
        options.add(new Option("a", "option A", true, "A"));
        options.add(new Option("b", "option B", false, "B"));
        options.add(new Option("c", "option C", true, "C"));

        final Group group = new GroupImpl(options, "MyGroup", "Description of my group", 1, 3, true);

        assertEquals(new Option("a", "option A", true, "A"), group.findOption("a"));
        assertEquals(new Option("b", "option B", false, "B"), group.findOption("b"));
        assertNull(group.findOption("d"));
    }

    @Test
    public void testGetMinimum() {
        final List<Option> options = new ArrayList<>();
        options.add(new Option("a", "option A", true, "A"));
        options.add(new Option("b", "option B", false, "B"));
        options.add(new Option("c", "option C", true, "C"));

        final Group group = new GroupImpl(options, "MyGroup", "Description of my group", 1, 3, true);

        assertEquals(1, group.getMinimum());
    }

    @Test
    public void testGetMaximum() {
        final List<Option> options = new ArrayList<>();
        options.add(new Option("a", "option A", true, "A"));
        options.add(new Option("b", "option B", false, "B"));
        options.add(new Option("c", "option C", true, "C"));

        final Group group = new GroupImpl(options, "MyGroup", "Description of my group", 1, 3, true);

        assertEquals(3, group.getMaximum());
    }

    @Test
    public void testIsRequired() {
        final List<Option> options = new ArrayList<>();
        options.add(new Option("a", "option A", true, "A"));
        options.add(new Option("b", "option B", false, "B"));
        options.add(new Option("c", "option C", true, "C"));

        final Group group = new GroupImpl(options, "MyGroup", "Description of my group", 1, 3, true);

        assertTrue(group.isRequired());
    }

    @Test
    public void testAppendUsage() {
        final List<Option> options = new ArrayList<>();
        options.add(new Option("a", "option A", true, "A"));
        options.add(new Option("b", "option B", false, "B"));
        options.add(new Option("c", "option C", true, "C"));

        final Group group = new GroupImpl(options, "MyGroup", "Description of my group", 1, 3, true);

        final Map<String, String> helpSettings = new HashMap<>();
        final StringBuffer buffer = new StringBuffer();

        group.appendUsage(buffer, helpSettings, null);

        assertEquals("--MyGroup\n" +
                "\tdescription of my group\n" +
                "\t[--a ARG]\n" +
                "\toption A\n" +
                "\t--b\n" +
                "\toption B\n" +
                "\t--c\n" +
                "\toption C\n", buffer.toString());
    }

    @Test
    public void testHelpLines() {
        final List<Option> options = new ArrayList<>();
        options.add(new Option("a", "option A", true, "A"));
        options.add(new Option("b", "option B", false, "B"));
        options.add(new Option("c", "option C", true, "C"));

        final Group group = new GroupImpl(options, "MyGroup", "Description of my group", 1, 3, true);

        final List<HelpLine> helpLines = group.helpLines(1, Collections.emptySet(), null);

        assertEquals(1, helpLines.size());
        assertEquals("--MyGroup\n" +
                "\tdescription of my group\n" +
                "\t[--a ARG]\n" +
                "\toption A\n" +
                "\t--b\n" +
                "\toption B\n" +
                "\t--c\n" +
                "\toption C\n", helpLines.get(0).getHelp());
    }
}