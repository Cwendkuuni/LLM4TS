package org.apache.commons.cli2.option;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.OptionGroup;
import org.apache.commons.cli2.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class GroupImplTest {

    @Test
    public void testBasicGroup() throws ParseException {
        final OptionGroup group = new OptionGroup("group");

        final Option option1 = OptionBuilder.withLongOpt("option1")
                .withDescription("Option 1")
                .create(true);
        final Option option2 = OptionBuilder.withLongOpt("option2")
                .withDescription("Option 2")
                .create(true);

        group.addOption(option1);
        group.addOption(option2);

        final CommandLine cmd = new CommandLine("test");
        group.parse(cmd);

        Assert.assertTrue(cmd.hasOption("group.option1"));
        Assert.assertTrue(cmd.hasOption("group.option2"));

        final HelpFormatter formatter = new HelpFormatter();
        group.printHelp("test", formatter);
    }

    @Test
    public void testGroupWithMinimumMaximum() throws ParseException {
        final OptionGroup group = new OptionGroup("group");

        final Option option1 = OptionBuilder.withLongOpt("option1")
                .withDescription("Option 1")
                .create(true);
        final Option option2 = OptionBuilder.withLongOpt("option2")
                .withDescription("Option 2")
                .create(true);

        group.setMinimum(1);
        group.setMaximum(2);

        group.addOption(option1);
        group.addOption(option2);

        final CommandLine cmd = new CommandLine("test");

        try {
            group.parse(cmd);
            Assert.fail("Expected ParseException");
        } catch (ParseException e) {
            // expected
        }

        Assert.assertEquals(2, cmd.getNumOptions());

        cmd.parse(group.parseArguments(new String[]{"test", "--group", "option1", "option2"}));
        Assert.assertTrue(cmd.hasOption("group.option1"));
        Assert.assertTrue(cmd.hasOption("group.option2"));

        cmd.parse(group.parseArguments(new String[]{"test", "--group", "option1"}));
        Assert.assertTrue(cmd.hasOption("group.option1"));
        Assert.assertFalse(cmd.hasOption("group.option2"));
    }

    @Test
    public void testGroupWithAnonymous() throws ParseException {
        final OptionGroup group = new OptionGroup("group");

        final Option option1 = OptionBuilder.withLongOpt("option1")
                .withDescription("Option 1 with argument")
                .hasArg()
                .create(true);

        group.addOption(option1);

        final CommandLine cmd = new CommandLine("test");

        try {
            group.parse(cmd);
            Assert.fail("Expected ParseException");
        } catch (ParseException e) {
            // expected
        }

        cmd.parse(group.parseArguments(new String[]{"test", "--group", "option1", "arg"}));
        Assert.assertTrue(cmd.hasOption("group.option1"));
        Assert.assertEquals("arg", cmd.getOptionValue("group.option1"));

        try {
            cmd.parse(group.parseArguments(new String[]{"test", "--group", "option1"}));
            Assert.fail("Expected ParseException");
        } catch (ParseException e) {
            // expected
        }
    }

    @Test
    public void testGroupWithNestedOption() throws ParseException {
        final OptionGroup group = new OptionGroup("group");

        final Option groupOption = OptionBuilder.withLongOpt("group")
                .withDescription("Group option")
                .create(true);

        final Option option1 = OptionBuilder.withLongOpt("option1")
                .withDescription("Option 1")
                .create(true);

        groupOption.addOption(option1);

        group.addOption(groupOption);

        final CommandLine cmd = new CommandLine("test");

        cmd.parse(group.parseArguments(new String[]{"test", "--group", "option1"}));
        Assert.assertTrue(cmd.hasOption("group.group.option1"));
    }
}