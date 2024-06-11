package org.apache.commons.cli2.option;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.HelpLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.Options;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for the GroupImpl class
 */
public class GroupImplTest {
    // Test data
    private static final String GROUP_NAME = "testGroup";
    private static final String OPTION_NAME_1 = "opt1";
    private static final String OPTION_NAME_2 = "opt2";
    private static final String ANONYMOUS_ARG_1 = "arg1";
    private static final String ANONYMOUS_ARG_2 = "arg2";

    /**
     * Test that a group with no options and anonymous arguments can be parsed correctly
     */
    @Test
    public void testGroupWithoutOptions() {
        final Options options = new Options();
        final Group group = new GroupImpl(Collections.emptyList(), GROUP_NAME, null, 0, 1);
        options.addGroup(group);

        final CommandLine cmd = new CommandLine(null);

        cmd.parse(options, new String[]{"test", ANONYMOUS_ARG_1, ANONYMOUS_ARG_2});
        Assert.assertEquals(GROUP_NAME, cmd.getOptionValue(GROUP_NAME));
        Assert.assertEquals(ANONYMOUS_ARG_1, cmd.getOptionValue(ANONYMOUS_ARG_1));
        Assert.assertEquals(ANONYMOUS_ARG_2, cmd.getOptionValue(ANONYMOUS_ARG_2));
    }

    /**
     * Test that a group with options and anonymous arguments can be parsed correctly
     */
    @Test
    public void testGroupWithOptions() {
        final Options options = new Options();
        final List<Option> optionsList = new ArrayList<>();

        final Option opt1 = new OptionBuilder()
                .withLongOpt(OPTION_NAME_1)
                .withValueSeparator('=')
                .withDescription("Option 1 description")
                .build();
        optionsList.add(opt1);

        final Option opt2 = new OptionBuilder()
                .withLongOpt(OPTION_NAME_2)
                .withDescription("Option 2 description")
                .build();
        optionsList.add(opt2);

        final Group group = new GroupImpl(optionsList, GROUP_NAME, null, 0, Integer.MAX_VALUE);
        options.addGroup(group);

        final CommandLine cmd = new CommandLine(null);

        cmd.parse(options, new String[]{"test", "-" + OPTION_NAME_1 + "=value1", "-" + OPTION_NAME_2});
        Assert.assertEquals("value1", cmd.getOptionValue(OPTION_NAME_1));
        Assert.assertEquals("value2", cmd.getOptionValue(OPTION_NAME_2));
        Assert.assertNull(cmd.getOptionValue(ANONYMOUS_ARG_1));
        Assert.assertNull(cmd.getOptionValue(ANONYMOUS_ARG_2));
    }

    /**
     * Test that a group with options requiring a minimum and a maximum number of options is parsed correctly
     */
    @Test
    public void testGroupWithMinMaxOptions() {
        final Options options = new Options();
        final List<Option> optionsList = new ArrayList<>();

        final Option opt1 = new OptionBuilder()
                .withLongOpt(OPTION_NAME_1)
                .withValueSeparator('=')
                .withDescription("Option 1 description")
                .isRequired()
                .build();
        optionsList.add(opt1);

        final Option opt2 = new OptionBuilder()
                .withLongOpt(OPTION_NAME_2)
                .withValueSeparator('=')
                .withDescription("Option 2 description")
                .build();
        optionsList.add(opt2);

        final Group group = new GroupImpl(optionsList, GROUP_NAME, null, 1, 2);
        options.addGroup(group);

        // Test valid case
        final CommandLine cmdValid = new CommandLine(null);
        cmdValid.parse(options, new String[]{"test", "-" + OPTION_NAME_1, "-" + OPTION_NAME_1, "-" + OPTION_NAME_2});
        Assert.assertEquals("value1", cmdValid.getOptionValue(OPTION_NAME_1));
        Assert.assertEquals("value2", cmdValid.getOptionValue(OPTION_NAME_2));

        // Test minimum case
        try {
            final CommandLine cmdMin = new CommandLine(null);
            cmdMin.parse(options, new String[]{"test"});
            Assert.fail("Parsing should have failed due to minimum constraint");
        } catch (OptionException e) {
            // Expected
        }

        // Test maximum case
        try {
            final CommandLine cmdMax = new CommandLine(null);
            cmdMax.parse(options, new String[]{"test", "-" + OPTION_NAME_1, "-" + OPTION_NAME_1, "-" + OPTION_NAME_1, "-" + OPTION_NAME_1});
            Assert.fail("Parsing should have failed due to maximum constraint");
        } catch (OptionException e) {
            // Expected
        }
    }

    /**
     * Test that a group with options and anonymous arguments can be parsed incorrectly
     */
    @Test
    public void testGroupWithOptionsParseError() {
        final Options options = new Options();
        final List<Option> optionsList = new ArrayList<>();

        final Option opt1 = new OptionBuilder()
                .withLongOpt(OPTION_NAME_1)
                .withValueSeparator('=')
                .withDescription("Option 1 description")
                .build();
        optionsList.add(opt1);

        final Option opt2 = new OptionBuilder()
                .withLongOpt(OPTION_NAME_2)
                .withDescription("Option 2 description")
                .build();
        optionsList.add(opt2);

        final Group group = new GroupImpl(optionsList, GROUP_NAME, null, 0, Integer.MAX_VALUE);
        options.addGroup(group);

        // Test parse error when unrecognized option is used
        try {
            final CommandLine cmdError = new CommandLine(null);
            cmdError.parse(options, new String[]{"test", "-invalidOption"});
            Assert.fail("Parsing should have failed due to unrecognized option");
        } catch (OptionException e) {
            // Expected
            Assert.assertEquals(OptionException.Type.UNRECOGNIZED, e.getType());
        }

        // Test parse error when invalid value is provided for an option
        try {
            final CommandLine cmdErrorValue = new CommandLine(null);
            cmdErrorValue.parse(options, new String[]{"test", "-" + OPTION_NAME_1 + "=invalidValue"});
            Assert.fail("Parsing should have failed due to invalid value");
        } catch (OptionException e) {
            // Expected
            Assert.assertEquals(OptionException.Type.INVALID_VALUE, e.getType());
        }

        // Test parse error when an anonymous argument is provided with an option
        try {
            final CommandLine cmdErrorArg = new CommandLine(null);
            cmdErrorArg.parse(options, new String[]{"test", "-" + OPTION_NAME_1, ANONYMOUS_ARG_1});
            Assert.fail("Parsing should have failed due to conflicting option and argument");
        } catch (OptionException e) {
            // Expected
        }
    }

    /**
     * Test that help is generated correctly for a group with options and anonymous arguments
     */
    @Test
    public void testGroupHelp() {
        final Options options = new Options();
        final List<Option> optionsList = new ArrayList<>();

        final Option opt1 = new OptionBuilder()
                .withLongOpt(OPTION_NAME_1)
                .withValueSeparator('=')
                .withDescription("Option 1 description")
                .build();
        optionsList.add(opt1);

        final Option opt2 = new OptionBuilder()
                .withLongOpt(OPTION_NAME_2)
                .withDescription("Option 2 description")
                .build();
        optionsList.add(opt2);

        final Group group = new GroupImpl(optionsList, GROUP_NAME, null, 0, Integer.MAX_VALUE);
        options.addGroup(group);

        HelpFormatter formatter = new HelpFormatter();

        final String help = formatter.printHelp("test", "Test command line options", true, options);
        Assert.assertTrue(help.contains(GROUP_NAME));
        Assert.assertTrue(help.contains(OPTION_NAME_1 + ": [value]"));
        Assert.assertTrue(help.contains(OPTION_NAME_2 + ":"));
        Assert.assertTrue(help.contains(ANONYMOUS_ARG_1));
        Assert.assertTrue(help.contains(ANONYMOUS_ARG_2));
    }

    /**
     * Test that a group with options and anonymous arguments can be processed correctly
     */
    @Test
    public void testGroupProcess() {
        final Options options = new Options();
        final List<Option> optionsList = new ArrayList<>();

        final Option opt1 = new OptionBuilder()
                .withLongOpt(OPTION_NAME_1)
                .withValueSeparator('=')
                .withDescription("Option 1 description")
                .build();
        optionsList.add(opt1);

        final Option opt2 = new OptionBuilder()
                .withLongOpt(OPTION_NAME_2)
                .withDescription("Option 2 description")
                .build();
        optionsList.add(opt2);

        final Group group = new GroupImpl(optionsList, GROUP_NAME, null, 0, Integer.MAX_VALUE);
        options.addGroup(group);

        final CommandLine cmd = new CommandLine(null);

        cmd.parse(options, new String[]{"test", "-" + OPTION_NAME_1 + "=value1", "-" + OPTION_NAME_2, ANONYMOUS_ARG_1, ANONYMOUS_ARG_2});

        group.process(cmd, cmd.argsList.iterator());

        Assert.assertEquals("value1", cmd.getOptionValue(OPTION_NAME_1));
        Assert.assertEquals("value2", cmd.getOptionValue(OPTION_NAME_2));
        Assert.assertEquals(ANONYMOUS_ARG_1, cmd.getOptionValue(ANONYMOUS_ARG_1));
        Assert.assertEquals(ANONYMOUS_ARG_2, cmd.getOptionValue(ANONYMOUS_ARG_2));
    }
}