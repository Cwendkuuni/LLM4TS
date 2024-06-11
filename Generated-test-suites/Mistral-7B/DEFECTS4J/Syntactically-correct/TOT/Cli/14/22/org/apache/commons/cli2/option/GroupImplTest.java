package org.apache.commons.cli2.option;

import org.apache.commons.cli2.CommandLineParser;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.ParseContext;
import org.apache.commons.cli2.options.GroupImpl;
import org.apache.commons.cli2.options.OptionBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class GroupImplTest {

    private ParseContext context;

    @Before
    public void setup() {
        context = new ParseContext();
        context.setHelpFormatter(new HelpFormatter());
    }

    @Test
    public void testValidGroup() throws Exception {
        final List options = Arrays.asList(
                OptionBuilder.withLongOpt("group1")
                        .withDescription("Option for Group 1")
                        .createOption(),
                OptionBuilder.withLongOpt("group2")
                        .withDescription("Option for Group 2")
                        .createOption(),
                OptionBuilder.withLongOpt("group-arg")
                        .withDescription("Argument for Group")
                        .isRequired(false)
                        .withArgName("ARG")
                        .createOption());

        final GroupImpl group = new GroupImpl(options, "Group", "A Group of Options", 0, Integer.MAX_VALUE);

        final CommandLineParser parser = new DefaultParser();
        parser.parse(group, new String[]{"-group1", "-group2"}, context);
        Assert.assertEquals(2, context.getParsedArgs().size());
    }

    @Test(expected = OptionException.class)
    public void testMinimum() throws Exception {
        final List options = Arrays.asList(
                OptionBuilder.withLongOpt("group1")
                        .withDescription("Option for Group 1")
                        .createOption(),
                OptionBuilder.withLongOpt("group2")
                        .withDescription("Option for Group 2")
                        .createOption());

        final GroupImpl group = new GroupImpl(options, "Group", "A Group of Options", 2, Integer.MAX_VALUE);

        final CommandLineParser parser = new DefaultParser();
        parser.parse(group, new String[0], context);
        Assert.fail();
    }

    @Test(expected = OptionException.class)
    public void testMaximum() throws Exception {
        final List options = Arrays.asList(
                OptionBuilder.withLongOpt("group1")
                        .withDescription("Option for Group 1")
                        .createOption(),
                OptionBuilder.withLongOpt("group2")
                        .withDescription("Option for Group 2")
                        .createOption(),
                OptionBuilder.withLongOpt("group3")
                        .withDescription("Option for Group 3")
                        .createOption());

        final GroupImpl group = new GroupImpl(options, "Group", "A Group of Options", 2, 2);

        final CommandLineParser parser = new DefaultParser();
        parser.parse(group, new String[]{"-group1", "-group1", "-group2"}, context);
        Assert.fail();
    }

    @Test(expected = OptionException.class)
    public void testIncorrectSyntax() throws Exception {
        final List options = Arrays.asList(
                OptionBuilder.withLongOpt("group1")
                        .withDescription("Option for Group 1")
                        .createOption(),
                OptionBuilder.withLongOpt("group2")
                        .withDescription("Option for Group 2")
                        .createOption());

        final GroupImpl group = new GroupImpl(options, "Group", "A Group of Options", 0, 0);

        final CommandLineParser parser = new DefaultParser();
        parser.parse(group, new String[]{"-group1", "-group2", "-group3"}, context);
        Assert.fail();
    }

    @Test(expected = OptionException.class)
    public void testMissingName() throws Exception {
        final List options = Arrays.asList(
                OptionBuilder.withLongOpt("group1")
                        .withDescription("Option for Group 1")
                        .createOption(),
                OptionBuilder.withLongOpt("group2")
                        .withDescription("Option for Group 2")
                        .createOption());

        final GroupImpl group = new GroupImpl(options, null, "A Group of Options", 0, Integer.MAX_VALUE);

        final CommandLineParser parser = new DefaultParser();
        parser.parse(group, new String[0], context);
        Assert.fail();
    }

    @Test
    public void testDescription() throws Exception {
        final List options = Arrays.asList(
                OptionBuilder.withLongOpt("group1")
                        .withDescription("Option for Group 1")
                        .createOption(),
                OptionBuilder.withLongOpt("group2")
                        .withDescription("Option for Group 2")
                        .createOption());

        final GroupImpl group = new GroupImpl(options, "Group", "A Group of Options with Description", 0, Integer.MAX_VALUE);

        final CommandLineParser parser = new DefaultParser();
        parser.parse(group, new String[0], context);
        Assert.assertEquals("A Group of Options with Description", context.getOptions().getDescription());
    }

    @Test
    public void testAnonymousArgument() throws Exception {
        final List options = Arrays.asList(
                OptionBuilder.withLongOpt("group")
                        .withDescription("A Group of Options")
                        .createOption(),
                OptionBuilder.withLongOpt("arg")
                        .withDescription("Argument for Group")
                        .isRequired(false)
                        .withArgName("ARG")
                        .createOption());

        final GroupImpl group = new GroupImpl(options, "Group", "A Group of Options", 0, Integer.MAX_VALUE);

        final CommandLineParser parser = new DefaultParser();
        parser.parse(group, new String[]{"-group", "-arg", "VALUE"}, context);
        Assert.assertEquals("VALUE", context.getParsedArgs().getOptionValues("arg")[0]);
    }

    @Test
    public void testHelp() throws Exception {
        final List options = Arrays.asList(
                OptionBuilder.withLongOpt("group1")
                        .withDescription("Option for Group 1")
                        .createOption(),
                OptionBuilder.withLongOpt("group2")
                        .withDescription("Option for Group 2")
                        .createOption());

        final GroupImpl group = new GroupImpl(options, "Group", "A Group of Options", 0, Integer.MAX_VALUE);

        final String help = group.getHelp();
        System.out.println(help);
    }
}