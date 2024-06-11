package org.apache.commons.cli2.option;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.Parser;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class GroupImplTest {

    private Parser parser;
    private HelpFormatter formatter;

    @Before
    public void setUp() {
        parser = new DefaultParser();
        formatter = new HelpFormatter();
    }

    @Test
    public void testSimpleGroup() {
        final Option groupOption = OptionBuilder.withName("group")
                .withDescription("Options for the group")
                .isRequired(false)
                .hasArg(true)
                .create(OptionBuilder.NO_ARGUMENTS);

        final Option option1 = OptionBuilder.withName("option1")
                .withDescription("Option 1")
                .withLongOpt("long-option1")
                .isRequired(false)
                .create(OptionBuilder.NO_ARGUMENTS);

        final Option option2 = OptionBuilder.withName("option2")
                .withDescription("Option 2")
                .withLongOpt("long-option2")
                .create(OptionBuilder.NO_ARGUMENTS);

        final List<Option> options = new ArrayList<>();
        options.add(option1);
        options.add(option2);

        final Group group = new GroupImpl(options, "group", "Options for the group", 0, 1);

        parser.parse(new String[]{"-group", "--long-option1", "--long-option2"});

        final CommandLine commandLine = parser.getCommandLine();
        assertFalse(commandLine.hasOption("group"));
        assertTrue(commandLine.hasOption("option1"));
        assertTrue(commandLine.hasOption("option2"));

        final String usage = formatter.printHelp("Test", group.getOptions());
        assertEquals("Test\n" +
                "Options:\n" +
                "option1, --long-option1\tOption 1\n" +
                "option2, --long-option2\tOption 2\n" +
                "group\tOptions for the group\n", usage);
    }

    @Test
    public void testSimpleGroupWithArguments() {
        final Option groupOption = OptionBuilder.withName("group")
                .withDescription("Options for the group")
                .isRequired(false)
                .hasArg(true)
                .create(OptionBuilder.NO_ARGUMENTS);

        final Option option1 = OptionBuilder.withName("option1")
                .withDescription("Option 1")
                .withLongOpt("long-option1")
                .isRequired(false)
                .hasArg(1)
                .create(OptionBuilder.VALUE_OPTIONAL);

        final Option option2 = OptionBuilder.withName("option2")
                .withDescription("Option 2")
                .withLongOpt("long-option2")
                .isRequired(false)
                .hasArg(1)
                .create(OptionBuilder.VALUE_OPTIONAL);

        final List<Option> options = new ArrayList<>();
        options.add(option1);
        options.add(option2);

        final Group group = new GroupImpl(options, "group", "Options for the group", 0, 1);

        parser.parse(new String[]{"-group", "--long-option1=value1", "--long-option2=value2"});

        final CommandLine commandLine = parser.getCommandLine();
        assertFalse(commandLine.hasOption("group"));
        assertTrue(commandLine.hasOption("option1"));
        assertSame("value1", commandLine.getOptionValue("option1"));
        assertTrue(commandLine.hasOption("option2"));
        assertSame("value2", commandLine.getOptionValue("option2"));

        final String usage = formatter.printHelp("Test", group.getOptions());
        assertEquals("Test\n" +
                "Options:\n" +
                "option1, --long-option1=<value>\tOption 1\n" +
                "option2, --long-option2=<value>\tOption 2\n" +
                "group\tOptions for the group\n", usage);
    }

    @Test
    public void testGroupWithNestedOptions() {
        final Option groupOption = OptionBuilder.withName("group")
                .withDescription("Options for the group")
                .isRequired(false)
                .hasArg(true)
                .create(OptionBuilder.NO_ARGUMENTS);

        final Option option1 = OptionBuilder.withName("option1")
                .withDescription("Option 1")
                .withLongOpt("long-option1")
                .isRequired(false)
                .create(OptionBuilder.NO_ARGUMENTS);

        final Option option11 = OptionBuilder.withName("option11")
                .withDescription("Option 1.1")
                .withLongOpt("long-option11")
                .isRequired(false)
                .create(OptionBuilder.NO_ARGUMENTS);

        final Option option12 = OptionBuilder.withName("option12")
                .withDescription("Option 1.2")
                .withLongOpt("long-option12")
                .isRequired(false)
                .create(OptionBuilder.NO_ARGUMENTS);

        final List<Option> options = new ArrayList<>();
        options.add(option1);
        options.add(option11);
        options.add(option12);

        final Group group = new GroupImpl(options, "group", "Options for the group", 0, 1);

        parser.parse(new String[]{"-group", "--long-option1", "--long-option11", "--long-option12"});

        final CommandLine commandLine = parser.getCommandLine();
        assertFalse(commandLine.hasOption("group"));
        assertTrue(commandLine.hasOption("option1"));
        assertFalse(commandLine.hasOption("option11"));
        assertFalse(commandLine.hasOption("option12"));

        final String usage = formatter.printHelp("Test", group.getOptions());
        assertEquals("Test\n" +
                "Options:\n" +
                "option1, --long-option1\tOptions for the group\n" +
                "    option11, --long-option11\tOption 1.1\n" +
                "    option12, --long-option12\tOption 1.2\n", usage);
    }

    @Test
    public void testGroupWithMandatoryOption() {
        final Option groupOption = OptionBuilder.withName("group")
                .withDescription("Options for the group")
                .isRequired(true)
                .hasArg(true)
                .create(OptionBuilder.NO_ARGUMENTS);

        final Option option1 = OptionBuilder.withName("option1")
                .withDescription("Option 1")
                .withLongOpt("long-option1")
                .isRequired(false)
                .hasArg(1)
                .create(OptionBuilder.VALUE_OPTIONAL);

        final List<Option> options = new ArrayList<>();
        options.add(option1);

        final Group group = new GroupImpl(options, "group", "Options for the group", 1, 1);

        try {
            parser.parse(new String[]{});
            fail("Parsing should have failed due to missing 'group' option");
        } catch (OptionException e) {
            // Expected
        }

        parser.parse(new String[]{"-group", "--long-option1=value1"});

        final CommandLine commandLine = parser.getCommandLine();
        assertTrue(commandLine.hasOption("group"));
        assertTrue(commandLine.hasOption("option1"));
        assertNull(commandLine.getOptionValue("option1"));

        final String usage = formatter.printHelp("Test", group.getOptions());
        assertEquals("Test\n" +
                "Options:\n" +
                "group\tOptions for the group\n" +
                "option1, --long-option1=<value>\tOption 1\n", usage);
    }

    @Test
    public void testGroupWithMinAndMaxOptions() {
        final Option groupOption = OptionBuilder.withName("group")
                .withDescription("Options for the group")
                .isRequired(false)
                .hasArg(true)
                .create(OptionBuilder.NO_ARGUMENTS);

        final Option option1 = OptionBuilder.withName("option1")
                .withDescription("Option 1")
                .withLongOpt("long-option1")
                .isRequired(false)
                .hasArg(1)
                .create(OptionBuilder.VALUE_OPTIONAL);

        final Option option2 = OptionBuilder.withName("option2")
                .withDescription("Option 2")
                .withLongOpt("long-option2")
                .isRequired(false)
                .hasArg(1)
                .create(OptionBuilder.VALUE_OPTIONAL);

        final List<Option> options = new ArrayList<>();
        options.add(option1);
        options.add(option2);

        final Group group = new GroupImpl(options, "group", "Options for the group", 1, 2);

        parser.parse(new String[]{"-group", "--long-option1=value1", "--long-option2=value2"});

        final CommandLine commandLine = parser.getCommandLine();
        assertFalse(commandLine.hasOption("group"));
        assertTrue(commandLine.hasOption("option1"));
        assertSame("value1", commandLine.getOptionValue("option1"));
        assertTrue(commandLine.hasOption("option2"));
        assertSame("value2", commandLine.getOptionValue("option2"));

        try {
            parser.parse(new String[]{"-group", "--long-option1=value1", "--long-option1=value2"});
            fail("Parsing should have failed due to minimum and maximum options count");
        } catch (OptionException e) {
            // Expected
        }

        try {
            parser.parse(new String[]{"-group", "--long-option1=value1"});
            fail("Parsing should have failed due to minimum options count");
        } catch (OptionException e) {
            // Expected
        }

        final String usage = formatter.printHelp("Test", group.getOptions());
        assertEquals("Test\n" +
                "Options:\n" +
                "group\tOptions for the group\n" +
                "option1, --long-option1=<value>\tOption 1\n" +
                "option2, --long-option2=<value>\tOption 2\n", usage);
    }
}