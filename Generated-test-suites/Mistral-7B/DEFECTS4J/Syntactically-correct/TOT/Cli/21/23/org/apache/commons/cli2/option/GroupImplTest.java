package org.apache.commons.cli2.option;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.parser.DefaultParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GroupImplTest {

    @Test
    public void testGroupWithOptions() throws Exception {
        final List<Option> options = Arrays.asList(
                new OptionBuilder("a")
                        .withLongOpt("long-a")
                        .withDescription("an option")
                        .withRequiredArg()
                        .create(),
                new OptionBuilder("b")
                        .withLongOpt("long-b")
                        .withDescription("another option")
                        .create(),
                new OptionBuilder("c")
                        .withLongOpt("long-c")
                        .withDescription("yet another option")
                        .withMinValues(2)
                        .withMaxValues(3)
                        .create());

        final String[] args = {"-a", "argA", "-b", "--long-c", "argC1", "argC2"};
        final CommandLine commandLine = new DefaultParser().parse(new GroupParser(), args);

        final Group group = new GroupImpl(options, "group", "a group", 1, Integer.MAX_VALUE, false);
        group.validate(commandLine);

        Assert.assertEquals("argA", commandLine.getOptionValue("a"));
        Assert.assertEquals("argC1", commandLine.getOptionValue("long-c", 0));
        Assert.assertEquals("argC2", commandLine.getOptionValue("long-c", 1));

        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("test", "Test the group", group, true);
        System.out.println(formatter.getHelpText());
    }

    @Test
    public void testGroupWithAnonymous() throws Exception {
        final List<Option> options = Arrays.asList(
                new OptionBuilder("a")
                        .withLongOpt("long-a")
                        .withDescription("an option")
                        .create(),
                new OptionBuilder("g")
                        .withLongOpt("long-g")
                        .withDescription("a group")
                        .withArgName("arg")
                        .withArguments(1)
                        .create());

        final String[] args = {"-g", "arg1"};
        final CommandLine commandLine = new DefaultParser().parse(new GroupParser(), args);

        final Group group = new GroupImpl(options, null, null, 1, Integer.MAX_VALUE, false);
        group.validate(commandLine);

        final Argument arg = (Argument) group.findOption("long-g").getValue();
        Assert.assertEquals("arg1", arg.getValue());

        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("test", "Test the group", group, true);
        System.out.println(formatter.getHelpText());
    }

    @Test(expected = OptionException.class)
    public void testGroupWithInsufficientOptions() throws Exception {
        final List<Option> options = Arrays.asList(
                new OptionBuilder("a")
                        .withLongOpt("long-a")
                        .withDescription("an option")
                        .create(),
                new OptionBuilder("g")
                        .withLongOpt("long-g")
                        .withDescription("a group")
                        .withArgName("arg")
                        .withArguments(1)
                        .create());

        final String[] args = {"-a"};
        final CommandLine commandLine = new DefaultParser().parse(new GroupParser(), args);

        final Group group = new GroupImpl(options, null, null, 1, 1, false);
        group.validate(commandLine);
    }

    @Test(expected = OptionException.class)
    public void testGroupWithExcessiveOptions() throws Exception {
        final List<Option> options = Arrays.asList(
                new OptionBuilder("a")
                        .withLongOpt("long-a")
                        .withDescription("an option")
                        .create(),
                new OptionBuilder("g")
                        .withLongOpt("long-g")
                        .withDescription("a group")
                        .withArgName("arg")
                        .withArguments(1)
                        .create());

        final String[] args = {"-a", "--long-a", "-g", "--long-g", "arg1", "arg2", "arg3"};
        final CommandLine commandLine = new DefaultParser().parse(new GroupParser(), args);

        final Group group = new GroupImpl(options, null, null, 1, 2, false);
        group.validate(commandLine);
    }

    @Test
    public void testGroupWithMixedOptions() throws Exception {
        final List<Option> options = Arrays.asList(
                new OptionBuilder("a")
                        .withLongOpt("long-a")
                        .withDescription("an option")
                        .create(),
                new OptionBuilder("g")
                        .withLongOpt("long-g")
                        .withDescription("a group")
                        .withArgName("arg")
                        .withArguments(1)
                        .create());

        final String[] args = {"-a", "argA", "-g", "--long-g", "arg1"};
        final CommandLine commandLine = new DefaultParser().parse(new GroupParser(), args);

        final Group group = new GroupImpl(options, null, null, 1, Integer.MAX_VALUE, false);
        group.validate(commandLine);

        Assert.assertEquals("argA", commandLine.getOptionValue("a"));
        Assert.assertNull(commandLine.getOptionValue("long-a"));
        Assert.assertEquals("arg1", commandLine.getOptionValue("long-g"));

        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("test", "Test the group", group, true);
        System.out.println(formatter.getHelpText());
    }
}