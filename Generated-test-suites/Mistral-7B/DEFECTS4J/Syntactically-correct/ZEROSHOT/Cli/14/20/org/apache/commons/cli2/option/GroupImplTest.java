package org.apache.commons.cli2.option;

import static org.junit.Assert.*;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.DisplaySetting;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.util.ReverseStringComparator;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Test for GroupImpl class
 */
public class GroupImplTest {

    private List<Option> options;
    private List<Argument> arguments;
    private List<Option> anonymousOptions;

    @Before
    public void setup() {
        // create list of options
        options = new ArrayList<>();
        options.add(new Option("a", "optionA", false, "Option A"));
        options.add(new Option("b", "optionB", true, "Option B with argument"));
        options.add(new Option("c", "optionC", false, "Option C"));

        // create list of arguments
        arguments = new ArrayList<>();
        arguments.add(new Argument("arg1", "Argument 1", true, "Argument 1 description"));
        arguments.add(new Argument("arg2", "Argument 2", false, "Argument 2 description"));

        // create list of anonymous options
        anonymousOptions = new ArrayList<>();
        anonymousOptions.add(new Option("anon1", "Anonymous Option 1", false, "Anonymous Option 1 description"));
        anonymousOptions.add(new Option("anon2", "Anonymous Option 2", false, "Anonymous Option 2 description"));
    }

    @Test
    public void testGroupImplCreation() {
        List<Option> groupOptions = new ArrayList<>(options);
        Group group = new GroupImpl(groupOptions, null, null, 1, Integer.MAX_VALUE);
        assertEquals(group.getClass(), GroupImpl.class);
        assertEquals(group.getName(), null);
        assertEquals(group.getDescription(), null);
        assertEquals(group.getOptions(), groupOptions);
        assertEquals(group.getMinimum(), 1);
        assertEquals(group.getMaximum(), Integer.MAX_VALUE);
        assertEquals(group.getAnonymous(), Collections.emptyList());
    }

    @Test
    public void testCanProcess() {
        List<Option> groupOptions = new ArrayList<>(options);
        final Group group = new GroupImpl(groupOptions, null, null, 1, Integer.MAX_VALUE);

        // test canProcess with valid options
        WriteableCommandLine commandLine = new WriteableCommandLine();
        commandLine.addOption("a");
        commandLine.addOption("c");
        List<String> argumentsList = commandLine.getArgs();

        assertTrue(group.canProcess(commandLine, argumentsList.get(0)));
        assertTrue(group.canProcess(commandLine, argumentsList.get(1)));

        // test canProcess with invalid option
        assertFalse(group.canProcess(commandLine, "invalidOption"));

        // test canProcess with argument
        assertFalse(group.canProcess(commandLine, "arg1"));
        assertFalse(group.canProcess(commandLine, "arg2"));

        // test canProcess with anonymous option
        assertTrue(group.canProcess(commandLine, "anon1"));
        assertTrue(group.canProcess(commandLine, "anon2"));
    }

    @Test
    public void testGetPrefixes() {
        List<Option> groupOptions = new ArrayList<>(options);
        final Group group = new GroupImpl(groupOptions, null, null, 1, Integer.MAX_VALUE);

        Set<String> prefixes = group.getPrefixes();
        assertTrue(prefixes.isEmpty());
    }

    @Test
    public void testGetTriggers() {
        List<Option> groupOptions = new ArrayList<>(options);
        final Group group = new GroupImpl(groupOptions, null, null, 1, Integer.MAX_VALUE);

        Set<String> triggers = group.getTriggers();
        assertEquals(triggers.size(), 3);
        assertTrue(triggers.containsAll(Arrays.asList("a", "b", "c")));
    }

    @Test
    public void testProcess() throws OptionException {
        List<Option> groupOptions = new ArrayList<>(options);
        final Group group = new GroupImpl(groupOptions, null, null, 1, Integer.MAX_VALUE);

        WriteableCommandLine commandLine = new WriteableCommandLine();
        commandLine.addOption("a");
        commandLine.addOption("c");
        List<String> argumentsList = commandLine.getArgs();

        group.process(commandLine, argumentsList.listIterator());

        // test if all options were processed correctly
        for (Option option : groupOptions) {
            assertTrue(commandLine.hasOption(option.getOpt()));
        }

        // test if arguments were processed correctly
        for (Argument argument : arguments) {
            assertTrue(commandLine.hasOption(argument.getOpt()));
        }
    }

    @Test
    public void testValidate() throws OptionException {
        List<Option> groupOptions = new ArrayList<>(options);
        final Group group = new GroupImpl(groupOptions, null, null, 1, Integer.MAX_VALUE);

        WriteableCommandLine commandLine = new WriteableCommandLine();
        commandLine.addOption("a");
        commandLine.addOption("c");
        List<String> argumentsList = commandLine.getArgs();

        group.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithTooFewOptions() throws OptionException {
        List<Option> groupOptions = new ArrayList<>(options);
        final Group group = new GroupImpl(groupOptions, null, null, 1, Integer.MAX_VALUE);

        WriteableCommandLine commandLine = new WriteableCommandLine();
        commandLine.addOption("a");
        List<String> argumentsList = commandLine.getArgs();

        group.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testValidateWithTooManyOptions() throws OptionException {
        List<Option> groupOptions = new ArrayList<>(options);
        final Group group = new GroupImpl(groupOptions, null, null, 1, 1);

        WriteableCommandLine commandLine = new WriteableCommandLine();
        commandLine.addOption("a");
        commandLine.addOption("c");
        List<String> argumentsList = commandLine.getArgs();

        group.validate(commandLine);
    }

    @Test
    public void testAppendUsage() {
        List<Option> groupOptions = new ArrayList<>(options);
        final Group group = new GroupImpl(groupOptions, null, null, 1, Integer.MAX_VALUE);

        final StringBuilder buffer = new StringBuilder();
        group.appendUsage(buffer, null, null);

        String usage = buffer.toString();
        assertTrue(usage.contains("OPTIONS"));
        assertTrue(usage.contains("optionA"));
        assertTrue(usage.contains("optionB"));
        assertTrue(usage.contains("optionC"));
        assertTrue(usage.contains("Argument 1"));
        assertTrue(usage.contains("Argument 2"));
        assertTrue(usage.contains("Anonymous Option 1"));
        assertTrue(usage.contains("Anonymous Option 2"));
    }

    @Test
    public void testHelpLines() {
        List<Option> groupOptions = new ArrayList<>(options);
        final Group group = new GroupImpl(groupOptions, null, null, 1, Integer.MAX_VALUE);

        List<HelpLine> helpLines = group.helpLines(0, null, null);

        assertEquals(helpLines.size(), 5);
        assertTrue(helpLines.get(0).getOpt().equals(""));
        assertTrue(helpLines.get(1).getOpt().equals("a"));
        assertTrue(helpLines.get(1).getShortOpt().equals("a"));
        assertTrue(helpLines.get(2).getOpt().equals("b"));
        assertTrue(helpLines.get(2).getShortOpt().equals("b"));
        assertTrue(helpLines.get(3).getOpt().equals("c"));
        assertTrue(helpLines.get(3).getShortOpt().equals("c"));
        assertTrue(helpLines.get(4).getOpt().equals("arg1"));
        assertTrue(helpLines.get(4).getShortOpt().equals("arg1"));
        assertTrue(helpLines.get(4).getLongOpt().equals("arg1"));
        assertTrue(helpLines.get(4).getDescription().contains("Argument 1 description"));
        assertTrue(helpLines.get(4).getArgName().equals("arg1"));
        assertTrue(helpLines.get(5).getOpt().equals("arg2"));
        assertTrue(helpLines.get(5).getShortOpt().equals("arg2"));
        assertTrue(helpLines.get(5).getLongOpt().equals("arg2"));
        assertTrue(helpLines.get(5).getDescription().contains("Argument 2 description"));
    }

    @Test
    public void testFindOption() {
        List<Option> groupOptions = new ArrayList<>(options);
        final Group group = new GroupImpl(groupOptions, null, null, 1, Integer.MAX_VALUE);

        Option option = group.findOption("a");
        assertEquals(option.getOpt(), "a");
    }

    @Test
    public void testGetMinimum() {
        List<Option> groupOptions = new ArrayList<>(options);
        final Group group = new GroupImpl(groupOptions, null, null, 1, Integer.MAX_VALUE);

        assertEquals(group.getMinimum(), 1);
    }

    @Test
    public void testGetMaximum() {
        List<Option> groupOptions = new ArrayList<>(options);
        final Group group = new GroupImpl(groupOptions, null, null, 1, Integer.MAX_VALUE);

        assertEquals(group.getMaximum(), Integer.MAX_VALUE);
    }

    @Test
    public void testIsRequired() {
        List<Option> groupOptions = new ArrayList<>(options);
        final Group group = new GroupImpl(groupOptions, null, null, 1, Integer.MAX_VALUE);

        assertFalse(group.isRequired());
    }

    @Test
    public void testDefaults() {
        List<Option> groupOptions = new ArrayList<>(options);
        final Group group = new GroupImpl(groupOptions, null, null, 1, Integer.MAX_VALUE);

        WriteableCommandLine commandLine = new WriteableCommandLine();
        group.defaults(commandLine);

        for (Option option : groupOptions) {
            if (option instanceof ArgumentsOption) {
                continue;
            }
            assertTrue(commandLine.hasOption(option.getOpt()));
            assertEquals(option.getDefaultValue(), commandLine.getOptionValue(option.getOpt()));
        }

        for (Argument argument : arguments) {
            assertFalse(commandLine.hasOption(argument.getOpt()));
        }
    }
}