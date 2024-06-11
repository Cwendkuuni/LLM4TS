package org.apache.commons.cli2.option;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.OptionGroup;
import org.apache.commons.cli2.OptionSet;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

public class GroupImplTest {

    @Test
    public void testBasicUsage() {
        final OptionGroup group = new OptionGroup("group");
        group.addOption(OptionBuilder.withName("a").withDescription("option A").create());
        group.addOption(OptionBuilder.withName("b").withDescription("option B").create());
        group.addOption(OptionBuilder.builder("c")
            .withDescription("option C with a value")
            .isRequired(false)
            .create());

        final CommandLine cmd = new CommandLine();
        final OptionSet options = new OptionSet();
        options.parse(group.parse(cmd, new String[]{"-a", "--b", "--c=test"}));

        Assert.assertEquals(2, options.getOptions().size());
        Assert.assertTrue(options.hasOption("a"));
        Assert.assertTrue(options.hasOption("b"));
        Assert.assertTrue(options.hasOption("c"));
        Assert.assertEquals("test", options.getOptionValue("c"));

        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("test", "", "1.0", "Test program", group.getOptions(), true);
    }

    @Test
    public void testMinMaxUsage() {
        final OptionGroup group = new OptionGroup("group");
        group.addOption(OptionBuilder.withName("a").withDescription("option A").create());
        group.addOption(OptionBuilder.withName("b").withDescription("option B").create());
        group.setMinimum(2);
        group.setMaximum(3);

        final CommandLine cmd = new CommandLine();
        final OptionSet options = new OptionSet();
        options.parse(group.parse(cmd, new String[]{"-a", "--b", "--a"}));

        Assert.assertEquals(2, options.getOptions().size());
        Assert.assertTrue(options.hasOption("a"));
        Assert.assertTrue(options.hasOption("b"));
        Assert.assertFalse(options.hasOption("c"));

        try {
            options.parse(group.parse(cmd, new String[]{"-a", "--a", "--a"}));
            Assert.fail();
        } catch (final OptionException e) {
            // expected
        }

        try {
            options.parse(group.parse(cmd, new String[]{"--a", "--b", "--c"}));
            Assert.fail();
        } catch (final OptionException e) {
            // expected
        }

        Assert.assertEquals(2, group.getOptions().size());

        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("test", "", "1.0", "Test program", group.getOptions(), true);
    }

    @Test
    public void testBurstingUsage() {
        final OptionGroup group = new OptionGroup("group");
        group.addOption(OptionBuilder.withName("a").withDescription("option A").create());
        group.addOption(OptionBuilder.withName("b").withDescription("option B").create());
        group.addOption(OptionBuilder.withName("c").withDescription("option C").create());
        group.addOption(OptionBuilder.withName("d").withDescription("option D").create());

        final CommandLine cmd = new CommandLine();
        final OptionSet options = new OptionSet();
        options.parse(group.parse(cmd, new String[]{"--c=x", "--b", "--a"}));

        Assert.assertEquals(4, options.getOptions().size());
        Assert.assertTrue(options.hasOption("a"));
        Assert.assertTrue(options.hasOption("b"));
        Assert.assertTrue(options.hasOption("c"));
        Assert.assertEquals("x", options.getOptionValue("c"));
        Assert.assertFalse(options.hasOption("d"));

        Assert.assertEquals(4, group.getOptions().size());

        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("test", "", "1.0", "Test program", group.getOptions(), true);
    }

    @Test
    public void testMixedUsage() {
        final OptionGroup group = new OptionGroup("group");
        group.addOption(OptionBuilder.withName("a").withDescription("option A").create());
        group.addOption(OptionBuilder.withName("b").withDescription("option B").create());
        group.addOption(OptionBuilder.withName("c").withDescription("option C with a value").create());
        group.addOption(OptionBuilder.builder("d")
            .withDescription("option D with an argument")
            .isRequired(false)
            .create());

        final CommandLine cmd = new CommandLine();
        final OptionSet options = new OptionSet();
        options.parse(group.parse(cmd, new String[]{"--c=x", "--d=y"}));

        Assert.assertEquals(4, options.getOptions().size());
        Assert.assertTrue(options.hasOption("a"));
        Assert.assertTrue(options.hasOption("b"));
        Assert.assertTrue(options.hasOption("c"));
        Assert.assertEquals("x", options.getOptionValue("c"));
        // We don't test option D in this test case, because it doesn't have a value.

        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("test", "", "1.0", "Test program", group.getOptions(), true);
    }
}