package org.apache.commons.cli2.option;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.OptionGroup;
import org.apache.commons.cli2.ParseException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test for {@link OptionGroupImpl} class.
 */
public class GroupImplTest {

    @Test
    public void testSimpleGroup() throws ParseException {
        final OptionGroup optionGroup = new OptionGroupImpl(
                new ArrayList<>(),
                "test-group",
                "Test group",
                0,
                1);

        final Option option1 = OptionBuilder
                .withLongOpt("option1")
                .withDescription("Test option 1")
                .createOption();

        final Option option2 = OptionBuilder
                .withLongOpt("option2")
                .withDescription("Test option 2")
                .createOption();

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        final CommandLine cmd = new CommandLine();

        // parse valid command line
        optionGroup.parse(cmd, new String[]{"--test-group", "--option1"});
        assertEquals(1, cmd.getOptions().size());
        assertEquals(option1, cmd.getOptionValue("option1"));
        assertEquals(false, cmd.hasOption("option2"));

        // parse command line with both options
        optionGroup.parse(cmd, new String[]{"--test-group", "--option1", "--option2"});
        assertEquals(2, cmd.getOptions().size());
        assertEquals(option1, cmd.getOptionValue("option1"));
        assertEquals(option2, cmd.getOptionValue("option2"));

        // parse command line with just the group name
        optionGroup.parse(cmd, new String[]{"--test-group"});
        assertEquals(0, cmd.getOptions().size());
        assertNull(cmd.getOptionValue("option1"));
        assertNull(cmd.getOptionValue("option2"));

        // parse command line with incorrect option
        optionGroup.parse(cmd, new String[]{"--test-group", "--incorrect-option"});
        assertEquals(0, cmd.getOptions().size());
        assertNull(cmd.getOptionValue("option1"));
        assertNull(cmd.getOptionValue("option2"));

        // parse empty command line
        optionGroup.parse(cmd, new String[]{});
        assertEquals(0, cmd.getOptions().size());
    }

    @Test
    public void testAnonymousGroup() throws ParseException {
        final OptionGroup optionGroup = new OptionGroupImpl(
                new ArrayList<>(),
                null,
                null,
                0,
                1);

        final Option option1 = OptionBuilder
                .withLongOpt("option1")
                .withDescription("Test option 1")
                .createOption();

        final Option option2 = OptionBuilder
                .withLongOpt("option2")
                .withDescription("Test option 2")
                .createOption();

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        // parse command line with both options
        final CommandLine cmd = new CommandLine();
        final List<String> args = new ArrayList<>();
        args.add("test");
        args.add("--option1");
        args.add("value1");
        args.add("--option2");
        args.add("value2");

        optionGroup.parse(cmd, args.toArray(new String[0]));
        assertEquals(2, cmd.getOptions().size());
        assertEquals(option1, cmd.getOptionValue("option1"));
        assertEquals(option2, cmd.getOptionValue("option2"));

        // parse command line with only one option
        final CommandLine cmd1 = new CommandLine();
        final List<String> args1 = new ArrayList<>();
        args1.add("test");
        args1.add("--option1");
        args1.add("value1");

        optionGroup.parse(cmd1, args1.toArray(new String[0]));
        assertEquals(1, cmd1.getOptions().size());
        assertEquals(option1, cmd1.getOptionValue("option1"));
        assertEquals(false, cmd1.hasOption("option2"));

        // parse command line without any option
        final CommandLine cmd2 = new CommandLine();
        final List<String> args2 = new ArrayList<>();
        args2.add("test");

        optionGroup.parse(cmd2, args2.toArray(new String[0]));
        assertEquals(0, cmd2.getOptions().size());

        // parse command line with incorrect option
        final CommandLine cmd3 = new CommandLine();
        final List<String> args3 = new ArrayList<>();
        args3.add("test");
        args3.add("--incorrect-option");

        try {
            optionGroup.parse(cmd3, args3.toArray(new String[0]));
            assert False : "Should throw ParseException";
        } catch (ParseException e) {
            // Expected
        }
    }

    @Test
    public void testGroupNaming() throws ParseException {
        final OptionGroup optionGroup1 = new OptionGroupImpl(
                new ArrayList<>(),
                "test-group1",
                "Test group 1",
                0,
                1);

        final OptionGroup optionGroup2 = new OptionGroupImpl(
                new ArrayList<>(),
                null,
                "Test group 2",
                0,
                1);

        final Option option1 = OptionBuilder
                .withLongOpt("option1")
                .withDescription("Test option 1")
                .createOption();

        final Option option2 = OptionBuilder
                .withLongOpt("option2")
                .withDescription("Test option 2")
                .createOption();

        optionGroup1.addOption(option1);
        optionGroup1.addOption(option2);
        optionGroup2.addOption(option1);
        optionGroup2.addOption(option2);

        final CommandLine cmd = new CommandLine();

        // parse command line with --test-group1 option
        optionGroup1.parse(cmd, new String[]{"--test-group1", "--option1", "value1"});
        assertEquals("Test group 1", cmd.getOptionLabel("test-group1"));
        assertEquals(1, cmd.getOptions().size());
        assertEquals(option1, cmd.getOptionValue("option1"));
        assertEquals(false, cmd.hasOption("option2"));

        // parse command line with --test-group2 option
        optionGroup2.parse(cmd, new String[]{"--test-group2", "--option1", "value1"});
        assertEquals("Test group 2", cmd.getOptionLabel("test-group2"));
        assertEquals(1, cmd.getOptions().size());
        assertEquals(option1, cmd.getOptionValue("option1"));
        assertEquals(false, cmd.hasOption("option2"));

        // parse command line with both --test-group1 and --test-group2
        final CommandLine cmd2 = new CommandLine();
        optionGroup1.parse(cmd2, new String[]{"--test-group1", "--option1", "value1"});
        optionGroup2.parse(cmd2, new String[]{"--test-group2", "--option1", "value1", "--option2"});
        assertEquals(2, cmd2.getOptions().size());
        assertEquals(option1, cmd2.getOptionValue("option1"));
        assertEquals(option2, cmd2.getOptionValue("option2"));
    }

    @Test
    public void testHelp() {
        final OptionGroup optionGroup = new OptionGroupImpl(
                new ArrayList<>(),
                null,
                null,
                0,
                1);

        final Option option1 = OptionBuilder
                .withLongOpt("option1")
                .withDescription("Test option 1")
                .createOption();

        final Option option2 = OptionBuilder
                .withLongOpt("option2")
                .withDescription("Test option 2")
                .createOption();

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        final CommandLine cmd = new CommandLine();
        optionGroup.parse(cmd, new String[]{"--help"});

        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("java", "", true);
        formatter.printHelp("java", optionGroup);

        final String helpContent = formatter.toHelpString(new PrintWriter(System.out), true);

        assertTrue(helpContent.contains("Test group (test-group)"));
        assertTrue(helpContent.contains("  Test option 1 (--option1)"));
        assertTrue(helpContent.contains("    Test option 1 description"));
        assertTrue(helpContent.contains("  Test option 2 (--option2)"));
        assertTrue(helpContent.contains("    Test option 2 description"));

        final List<String> args = new ArrayList<>();
        args.add("java");
        args.add("--help");
        args.add("--test-group");

        optionGroup.parse(cmd, args.toArray(new String[0]));

        final String helpContentGroup = formatter.toHelpString(new PrintWriter(System.out), true);

        assertTrue(helpContentGroup.contains("Test group (test-group)"));
        assertTrue(helpContentGroup.contains("Test option 1 (--option1)"));
        assertTrue(helpContentGroup.contains("Test option 1 description"));
        assertTrue(helpContentGroup.contains("Test option 2 (--option2)"));
        assertTrue(helpContentGroup.contains("Test option 2 description"));
    }

    @Test
    public void testHelpWithAnonymousGroup() {
        final OptionGroup optionGroup = new OptionGroupImpl(
                new ArrayList<>(),
                null,
                null,
                0,
                1);

        final Option option1 = OptionBuilder
                .withLongOpt("option1")
                .withDescription("Test option 1")
                .createOption();

        final Option option2 = OptionBuilder
                .withLongOpt("option2")
                .withDescription("Test option 2")
                .createOption();

        optionGroup.addOption(option1);
        optionGroup.addOption(option2);

        final CommandLine cmd = new CommandLine();
        final List<String> args = new ArrayList<>();
        args.add("java");
        args.add("--help");
        args.add("--");

        optionGroup.parse(cmd, args.toArray(new String[0]));

        final HelpFormatter formatter = new HelpFormatter();
        final String helpContent = formatter.toHelpString(new PrintWriter(System.out), true);

        assertTrue(helpContent.contains("Test group (--)"));
        assertTrue(helpContent.contains("  Test option 1 (--option1)"));
        assertTrue(helpContent.contains("    Test option 1 description"));
        assertTrue(helpContent.contains("  Test option 2 (--option2)"));
        assertTrue(helpContent.contains("    Test option 2 description"));
    }
}