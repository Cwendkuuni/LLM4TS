package org.apache.commons.cli2.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
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
 * Test for {@link GroupImpl} class.
 */
public class GroupImplTest {

    private Group group;
    private WriteableCommandLine commandLine;

    @Before
    public void setup() {
        List<Option> options = new ArrayList<>();
        options.add(new Option("a", "option A", true, "description A"));
        options.add(new Option("b", "option B", false, "description B"));
        options.add(new Option("c", "option C", true, "description C") {
            @Override
            public void validate(WriteableCommandLine commandLine)
                    throws OptionException {
                // validate method should not be called for group options
                throw new IllegalStateException("validate method called for group");
            }
        });
        options.add(new Option("d", "option D", false, "description D") {
            @Override
            public boolean canProcess(WriteableCommandLine commandLine,
                                      String arg) {
                // canProcess method should not be called for non-option elements
                throw new IllegalStateException("canProcess method called for non-option");
            }
        });

        List<Argument> anonymousArguments = new ArrayList<>();
        anonymousArguments.add(new Argument("arg1", "argument 1", true, "description arg1"));
        anonymousArguments.add(new Argument("arg2", "argument 2", false, "description arg2"));

        group = new GroupImpl(options, null, null, 1, 2, false) {
            @Override
            public List getAnonymous() {
                return anonymousArguments;
            }
        };

        commandLine = new WriteableCommandLine("Test", true);
    }

    /**
     * Test if group can process valid command line arguments.
     */
    @Test
    public void testProcessValidArguments() {
        commandLine.parse(group.getOptions());

        commandLine.parse("--a --b --c arg1 --c arg2");

        // validate the parsed command line
        group.validate(commandLine);

        // check processed options
        assertTrue(commandLine.hasOption("a"));
        assertTrue(commandLine.hasOption("b"));
        assertTrue(commandLine.hasOption("c"));
        assertTrue(commandLine.isSetOption("c"));
        assertNotNull(commandLine.getOptionValue("c"));
        assertNull(commandLine.getOptionValue("c", null));
        assertEquals("description A", commandLine.getOptionDescription("a"));
        assertEquals("description B", commandLine.getOptionDescription("b"));
        assertEquals("description C", commandLine.getOptionDescription("c"));

        // check anonymous arguments
        assertTrue(commandLine.hasOption("arg1"));
        assertFalse(commandLine.hasOption("arg1", false));
        assertNull(commandLine.getOptionValue("arg1", null));
        assertEquals("description arg1", commandLine.getOptionDescription("arg1"));
        assertTrue(commandLine.hasOption("arg2"));
        assertFalse(commandLine.hasOption("arg2", false));
        assertNull(commandLine.getOptionValue("arg2", null));
        assertEquals("description arg2", commandLine.getOptionDescription("arg2"));
    }

    /**
     * Test if group can process valid command line arguments with default values.
     */
    @Test
    public void testProcessValidArgumentsWithDefaults() {
        commandLine.parse(group.getOptions());

        commandLine.parse("--a --b --c arg1 --d");

        // validate the parsed command line
        group.validate(commandLine);

        // check processed options
        assertTrue(commandLine.hasOption("a"));
        assertTrue(commandLine.hasOption("b"));
        assertTrue(commandLine.isSetOption("c"));
        assertNull(commandLine.getOptionValue("c"));
        assertNull(commandLine.getOptionValue("c", null));
        assertEquals("description A", commandLine.getOptionDescription("a"));
        assertEquals("description B", commandLine.getOptionDescription("b"));
        assertEquals("description C", commandLine.getOptionDescription("c"));

        // check anonymous arguments
        assertTrue(commandLine.hasOption("arg1"));
        assertFalse(commandLine.hasOption("arg1", false));
        assertNull(commandLine.getOptionValue("arg1", null));
        assertEquals("description arg1", commandLine.getOptionDescription("arg1"));
        assertFalse(commandLine.hasOption("arg2"));
        assertNull(commandLine.getOptionValue("arg2", null));
        assertNull(commandLine.getOptionValue("arg2", null));
        assertEquals("description arg2", commandLine.getOptionDescription("arg2"));
    }

    /**
     * Test if group can process valid command line arguments with minimum and
     * maximum constraint.
     */
    @Test
    public void testProcessValidArgumentsWithConstraints() {
        commandLine.parse(group.getOptions());

        commandLine.parse("--a --b --c arg1 --c arg2 --a --a");

        // validate the parsed command line
        group.validate(commandLine);

        // check processed options
        assertTrue(commandLine.hasOption("a"));
        assertTrue(commandLine.hasOption("b"));
        assertTrue(commandLine.isSetOption("c"));
        assertNull(commandLine.getOptionValue("c"));
        assertNull(commandLine.getOptionValue("c", null));
        assertEquals("description A", commandLine.getOptionDescription("a"));
        assertEquals("description B", commandLine.getOptionDescription("b"));
        assertEquals("description C", commandLine.getOptionDescription("c"));

        // check anonymous arguments
        assertTrue(commandLine.hasOption("arg1"));
        assertFalse(commandLine.hasOption("arg1", false));
        assertNull(commandLine.getOptionValue("arg1", null));
        assertEquals("description arg1", commandLine.getOptionDescription("arg1"));
        assertTrue(commandLine.hasOption("arg2"));
        assertFalse(commandLine.hasOption("arg2", false));
        assertNull(commandLine.getOptionValue("arg2", null));
        assertEquals("description arg2", commandLine.getOptionDescription("arg2"));

        // validate the minimum constraint
        assertFalse(commandLine.hasOption("a", 0));

        // validate the maximum constraint
        assertFalse(commandLine.hasOption("a", 4));
    }

    /**
     * Test if group can process an empty command line.
     */
    @Test
    public void testProcessEmptyCommandLine() {
        commandLine.parse(group.getOptions());

        commandLine.parse("");

        // validate the parsed command line
        group.validate(commandLine);

        // check processed options
        assertFalse(commandLine.hasOption("a"));
        assertFalse(commandLine.hasOption("b"));
        assertFalse(commandLine.isSetOption("c"));
        assertNull(commandLine.getOptionValue("c"));
        assertNull(commandLine.getOptionValue("c", null));
        assertNull(commandLine.getOptionValue("c", null));
        assertEquals("", commandLine.getOptionDescription("a"));
        assertEquals("", commandLine.getOptionDescription("b"));
        assertEquals("", commandLine.getOptionDescription("c"));

        // check anonymous arguments
        assertFalse(commandLine.hasOption("arg1"));
        assertFalse(commandLine.hasOption("arg1", false));
        assertNull(commandLine.getOptionValue("arg1", null));
        assertEquals("", commandLine.getOptionDescription("arg1"));
        assertFalse(commandLine.hasOption("arg2"));
        assertNull(commandLine.getOptionValue("arg2", null));
        assertNull(commandLine.getOptionValue("arg2", null));
        assertEquals("", commandLine.getOptionDescription("arg2"));
    }

    /**
     * Test if group can process an invalid command line with unrecognized option.
     */
    @Test(expected = OptionException.class)
    public void testProcessInvalidCommandLineWithUnknownOption() {
        commandLine.parse(group.getOptions());

        commandLine.parse("--x --a --b --c arg1 --c arg2");

        // validate the parsed command line
        group.validate(commandLine);
    }

    /**
     * Test if group can process an invalid command line with unrecognized argument.
     */
    @Test(expected = OptionException.class)
    public void testProcessInvalidCommandLineWithUnknownArgument() {
        commandLine.parse(group.getOptions());

        commandLine.parse("--a --b --c arg1 --x");

        // validate the parsed command line
        group.validate(commandLine);
    }

    /**
     * Test if group can process an invalid command line with missing required
     * option.
     */
    @Test(expected = OptionException.class)
    public void testProcessInvalidCommandLineWithMissingRequiredOption() {
        commandLine.parse(group.getOptions());

        commandLine.parse("--b --c arg1 --c arg2");

        // validate the parsed command line
        group.validate(commandLine);
    }

    /**
     * Test if group can process an invalid command line with missing required
     * anonymous argument.
     */
    @Test(expected = OptionException.class)
    public void testProcessInvalidCommandLineWithMissingRequiredAnonymousArgument() {
        commandLine.parse(group.getOptions());

        commandLine.parse("--a --b --c arg1");

        // validate the parsed command line
        group.validate(commandLine);
    }

    /**
     * Test if group can process an invalid command line with duplicate option.
     */
    @Test(expected = OptionException.class)
    public void testProcessInvalidCommandLineWithDuplicateOption() {
        commandLine.parse(group.getOptions());

        commandLine.parse("--a --a --b --c arg1 --c arg2");

        // validate the parsed command line
        group.validate(commandLine);
    }

    /**
     * Test if group can process an invalid command line with conflicting
     * options.
     */
    @Test(expected = OptionException.class)
    public void testProcessInvalidCommandLineWithConflictingOptions() {
        group = new GroupImpl(Arrays.asList(
                new Option("a", "option A", true, "description A"),
                new Option("a", "option A", false, "description A")),
            null, -1, -1, false) {
            @Override
            public List getAnonymous() {
                return Collections.emptyList();
            }
        };

        commandLine.parse(group.getOptions());

        commandLine.parse("--a --a");

        // validate the parsed command line
        group.validate(commandLine);
    }
}