package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the Options class.
 */
public class OptionsTest {

    /**
     * Test adding a single option.
     */
    @Test
    public void testAddSingleOption() {
        Options options = new Options();
        options.addOption("a", "optionA", false, "An option A.");

        // check the option was added
        Option optionA = options.getOption("a");
        assertEquals("optionA", optionA.getLongOpt());
        assertEquals(false, optionA.hasArg());
        assertEquals("An option A.", optionA.getDescription());

        // check the option can be retrieved by its short name
        Option retrievedOptionA = options.getOption("a");
        assertSame(optionA, retrievedOptionA);

        // check the option cannot be retrieved by a different short name
        assertNull(options.getOption("b"));

        // check the option cannot be retrieved by a different long name
        assertNull(options.getOption("optionB"));
    }

    /**
     * Test adding an option with an argument.
     */
    @Test
    public void testAddOptionWithArgument() {
        Options options = new Options();
        options.addOption("a", null, true, "An option A with an argument.");

        // check the option was added
        Option optionA = options.getOption("a");
        assertNull(optionA.getLongOpt());
        assertTrue(optionA.hasArg());
        assertEquals("An option A with an argument.", optionA.getDescription());

        // check the option can be retrieved by its short name
        Option retrievedOptionA = options.getOption("a");
        assertSame(optionA, retrievedOptionA);

        // check parsing an argument with the option
        String[] args = {"-a", "arg"};
        Options parsedOptions = new Options().parse(args);
        Option parsedOptionA = parsedOptions.getOption("a");
        assertEquals("arg", parsedOptionA.getValue());

        // check an error is thrown when an argument is missing
        String[] missingArgArgs = {"-a"};
        try {
            Options missingArgOptions = new Options().parse(missingArgArgs);
        } catch (ParseException e) {
            // expected
        }
    }

    /**
     * Test adding an option group.
     */
    @Test
    public void testAddOptionGroup() {
        OptionGroup group = new OptionGroup("group1", "Group 1", true, "Options in group 1.");
        group.addOption(new Option("g1a", "groupOptionA", false, "An option in group 1."));

        Options options = new Options();
        options.addOptionGroup(group);

        // check the option group was added
        OptionGroup retrievedGroup = options.getOptionGroups().iterator().next();
        assertEquals("group1", retrievedGroup.getLongOpt());
        assertEquals("Group 1", retrievedGroup.getDescription());
        assertTrue(retrievedGroup.isRequired());

        // check the option in the group was added
        Option optionA = retrievedGroup.getOption("g1a");
        assertEquals("g1a", optionA.getShortOpt());
        assertEquals("groupOptionA", optionA.getLongOpt());
        assertEquals(false, optionA.hasArg());

        // check the option in the group can be retrieved by its short name
        Option retrievedOptionA = options.getOption("g1a");
        assertSame(optionA, retrievedOptionA);

        // check parsing an argument with the option in the group
        String[] args = {"--group1", "arg"};
        Options parsedOptions = new Options().parse(args);
        Option parsedOptionA = parsedOptions.getOption("g1a");
        assertEquals("arg", parsedOptionA.getValue());
    }

    /**
     * Test parsing an empty command line.
     */
    @Test
    public void testParseEmptyCommandLine() {
        Options options = new Options();

        try {
            options.parse(new String[0]);
        } catch (ParseException e) {
            // expected
        }
    }

    /**
     * Test parsing a command line with options.
     */
    @Test
    public void testParseCommandLineWithOptions() {
        Options options = new Options();
        options.addOption("a", null, true, "An option A with an argument.");

        String[] args = {"-a", "arg1", "-a", "arg2"};
        Options parsedOptions = options.parse(args);

        Option optionA1 = parsedOptions.getOption("a");
        assertEquals("arg1", optionA1.getValue());

        Option optionA2 = parsedOptions.getOption("a");
        assertEquals("arg2", optionA2.getValue());
    }

    /**
     * Test parsing a command line with conflicting options.
     */
    @Test(expected = ParseException.class)
    public void testParseCommandLineWithConflictingOptions() {
        Options options = new Options();
        options.addOption("a", null, true, "An option A with an argument.");
        options.addOption("a", null, false, "Another option A without an argument.");

        String[] args = {"-a", "arg"};
        options.parse(args);
    }

    /**
     * Test parsing a command line with an invalid option.
     */
    @Test(expected = ParseException.class)
    public void testParseCommandLineWithInvalidOption() {
        Options options = new Options();
        options.addOption("a", null, false, "An option A without an argument.");

        String[] args = {"-z", "arg"};
        options.parse(args);
    }

    /**
     * Test parsing a command line with missing options.
     */
    @Test(expected = ParseException.class)
    public void testParseCommandLineWithMissingOptions() {
        Options options = new Options();
        options.addOption("a", null, false, "An option A without an argument.");

        String[] args = {"-b"};
        options.parse(args);
    }
}