package org.apache.commons.cli2.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.DisplaySetting;
import org.apache.commons.cli2.Group;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.resource.ResourceConstants;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests for {@link GroupImpl}.
 */
public class GroupImplTest {

    private static final String GROUP_NAME = "group";
    private static final String GROUP_DESCRIPTION = "group description";

    private List<Option> options;
    private Group group;
    private WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        options = new ArrayList<>();

        final Option opt1 = new Option("opt1", "option 1");
        options.add(opt1);

        final Option opt2 = new Option("opt2", "option 2")
                .required(true)
                .argName("arg1")
                .args(new Argument[0]);
        options.add(opt2);

        final Option opt3 = new Option("opt3", "option 3")
                .required(false)
                .argName("arg2")
                .args(new Argument("value"));
        options.add(opt3);

        final Option opt4 = new Option("opt4", "option 4")
                .required(false)
                .argName("arg3")
                .args(Collections.singletonList(new Argument("value", true)));
        options.add(opt4);

        final Option arg1 = new Argument("arg1");
        final Option arg2 = new Argument("arg2", true);

        group = new GroupImpl(options, GROUP_NAME, GROUP_DESCRIPTION, 1, Integer.MAX_VALUE);
        commandLine = new WriteableCommandLine();
    }

    @Test
    public void testCanProcess() {
        // canProcess: option 1
        assertTrue(group.canProcess(commandLine, "opt1"));

        // canProcess: option 2
        assertTrue(group.canProcess(commandLine, "opt2"));

        // canProcess: option 2 with value
        assertTrue(group.canProcess(commandLine, "opt2 arg1"));

        // canProcess: option 3 with value
        assertTrue(group.canProcess(commandLine, "opt3 value"));

        // canProcess: option 4 with value
        assertTrue(group.canProcess(commandLine, "opt4 arg3"));

        // canProcess: option 1 with same value
        assertTrue(group.canProcess(commandLine, "opt1"));

        // canProcess: option with same trigger
        assertFalse(group.canProcess(commandLine, "opt1"));

        // canProcess: anonymous argument
        assertTrue(group.canProcess(commandLine, "arg1"));

        // canProcess: anonymous argument with value
        assertTrue(group.canProcess(commandLine, "arg1 value"));

        // canProcess: anonymous argument with value and same value
        assertTrue(group.canProcess(commandLine, "arg1 value"));
    }

    @Test
    public void testProcess() {
        // process: option 1
        group.process(commandLine, commandLine.iterator());
        assertEquals(0, commandLine.getOptions().size());
        assertEquals(0, commandLine.getArgs().size());

        // process: option 1 with value
        commandLine.parse("--opt1");
        group.process(commandLine, commandLine.iterator());
        assertEquals(1, commandLine.getOptions().size());
        assertEquals(0, commandLine.getArgs().size());
        assertSame(opt1, commandLine.getOptionByName("opt1"));

        // process: option 2
        commandLine.clear();
        commandLine.parse("--opt2 arg1");
        group.process(commandLine, commandLine.iterator());
        assertEquals(2, commandLine.getOptions().size());
        assertEquals(0, commandLine.getArgs().size());
        assertSame(opt2, commandLine.getOptionByName("opt2"));
        assertSame(arg1, commandLine.getArgumentByName("arg1"));

        // process: option 3
        commandLine.clear();
        commandLine.parse("--opt3 value");
        group.process(commandLine, commandLine.iterator());
        assertEquals(3, commandLine.getOptions().size());
        assertEquals(0, commandLine.getArgs().size());
        assertSame(opt3, commandLine.getOptionByName("opt3"));
        assertSame(arg2, commandLine.getArgumentByName("value"));

        // process: option 4
        commandLine.clear();
        commandLine.parse("--opt4 arg3 value");
        group.process(commandLine, commandLine.iterator());
        assertEquals(4, commandLine.getOptions().size());
        assertEquals(0, commandLine.getArgs().size());
        assertSame(opt4, commandLine.getOptionByName("opt4"));
        assertSame(arg3, commandLine.getArgumentByName("arg3"));
        assertTrue(commandLine.hasOption("opt4"));
    }

    @Test
    public void testGetPrefixes() {
        final Set<String> expectedPrefixes = Collections.singleton("opt");
        Set<String> prefixes = group.getPrefixes();
        assertEquals(expectedPrefixes, prefixes);
    }

    @Test
    public void testGetTriggers() {
        final Set<String> expectedTriggers = new HashSet<>(options.stream()
                .map(Option::getLongOpt)
                .map(String::toLowerCase)
                .collect(Collectors.toList())) {
            @Override
            public boolean equals(final Object o) {
                if (this == o) {
                    return true;
                }
                if (o == null) {
                    return false;
                }
                if (getClass() != o.getClass()) {
                    return false;
                }
                final Set<?> that = (Set<?>) o;
                return Objects.equals(this.size(), that.size())
                        && this.containsAll(that);
            }

            @Override
            public int hashCode() {
                return Objects.hash(this.size(), this.toArray());
            }
        };
        Set<String> triggers = group.getTriggers();
        assertEquals(expectedTriggers, triggers);
    }

    @Test
    public void testValidate() throws OptionException {
        // validate: no options
        group.validate(commandLine);

        // validate: one option
        commandLine.parse("--opt1");
        group.validate(commandLine);

        // validate: two options
        commandLine.clear();
        commandLine.parse("--opt1 --opt2");
        group.validate(commandLine);

        // validate: two options with values
        commandLine.clear();
        commandLine.parse("--opt1 value1 --opt2 value2");
        group.validate(commandLine);

        // validate: with anonymous argument
        commandLine.clear();
        commandLine.parse("--opt1 value1 arg1");
        group.validate(commandLine);

        // validate: with anonymous argument and value
        commandLine.clear();
        commandLine.parse("--opt1 value1 arg1 value2");
        group.validate(commandLine);

        // validate: invalid number of options
        commandLine.clear();
        commandLine.parse("--opt1 --opt1");
        assertThrows(OptionException.class, () -> group.validate(commandLine));

        // validate: invalid number of options with anonymous argument
        commandLine.clear();
        commandLine.parse("--opt1 --opt1 arg1");
        assertThrows(OptionException.class, () -> group.validate(commandLine));
    }

    @Test
    public void testGetPreferredName() {
        assertEquals(GROUP_NAME, group.getPreferredName());
    }

    @Test
    public void testGetDescription() {
        assertEquals(GROUP_DESCRIPTION, group.getDescription());
    }

    @Test
    public void testAppendUsage() {
        final StringBuilder buffer = new StringBuilder();
        group.appendUsage(buffer, Collections.emptySet(), null);

        final String expectedUsage = "%s: " + GROUP_DESCRIPTION + "\n" +
                "|  " + opt1.getLongOpt() + ":\t" + opt1.getDescription() + "\n" +
                "|  " + opt2.getLongOpt() + " <arg1>:\t" + opt2.getDescription() + "\n" +
                "|  " + opt3.getLongOpt() + " <arg2>:\t" + opt3.getDescription() + "\n" +
                "|  " + opt4.getLongOpt() + " <arg3>:\t" + opt4.getDescription() + "\n";

        assertEquals(expectedUsage, buffer.toString());
    }

    @Test
    public void testHelpLines() {
        final int depth = 0;
        final Set<DisplaySetting> helpSettings = Collections.emptySet();

        final List<HelpLine> helpLines = group.helpLines(depth, helpSettings, null);

        final String expectedHelpLines = "%s: " + GROUP_DESCRIPTION + "\n" +
                "| --opt1:\toption 1\n" +
                "| --opt2 <arg1>:\toption 2\n" +
                "| --opt3 <arg2>:\toption 3\n" +
                "| --opt4 <arg3>:\toption 4\n";

        final String expectedHelpLineFormat = "%-9s: %s%n| %-15s%s%n";
        final Comparator<HelpLine> comp = (hl1, hl2) -> hl1.getLineNumber()
                .compareTo(hl2.getLineNumber());

        assertEquals(1, helpLines.size());
        assertEquals(expectedHelpLines, helpLines.get(0).getHelpString());
        assertEquals(expectedHelpLineFormat, helpLines.get(0).getHelpFormatter().format(new Object[0]));
        assertEquals(Collections.singletonList(helpLines.get(0)), group.helpLines(1, helpSettings, null));
    }

    @Test
    public void testGetOptions() {
        assertEquals(options, group.getOptions());
    }

    @Test
    public void testGetAnonymous() {
        assertEquals(Collections.unmodifiableList(Arrays.asList(arg1, arg2)), group.getAnonymous());
    }

    @Test
    public void testFindOption() {
        assertNull(group.findOption("invalidOption"));
        assertSame(opt1, group.findOption("opt1"));
        assertSame(opt2, group.findOption("opt2"));
        assertSame(opt3, group.findOption("opt3"));
        assertSame(opt4, group.findOption("opt4"));
    }

    @Test
    public void testGetMinimum() {
        assertEquals(1, group.getMinimum());
    }

    @Test
    public void testGetMaximum() {
        assertEquals(Integer.MAX_VALUE, group.getMaximum());
    }

    @Test
    public void testIsRequired() {
        assertFalse(group.isRequired());
    }

    @Test
    public void testDefaults() {
        group.defaults(commandLine);
        assertTrue(commandLine.isEmpty());
    }
}