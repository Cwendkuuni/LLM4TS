package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Test for {@link CommandLine} class.
 */
public class CommandLineTest {

    /**
     * Test method for {@link CommandLine#CommandLine()}.
     */
    @Test
    public void testEmptyCommandLine() {
        CommandLine commandLine = new CommandLine();
        Assert.assertEquals("", commandLine.getArgs().length > 0 ? Arrays.toString(commandLine.getArgs()) : null, null);
    }

    /**
     * Test method for {@link CommandLine#CommandLine(String[])}.
     */
    @Test
    public void testCommandLineWithArguments() {
        String[] args = {"--option1", "value1", "--option2", "value2", "arg1", "arg2"};
        CommandLine commandLine = new CommandLine(args);

        Assert.assertEquals(args.length - 2, commandLine.getArgs().length);
        Assert.assertArrayEquals(Arrays.copyOfRange(args, 2, args.length), commandLine.getArgs());

        Option option1 = new Option("o", "option1", false, "Option 1 description");
        commandLine.parse(new DefaultParser().parse(new Options().addOption(option1), args));
        Assert.assertTrue(commandLine.hasOption("option1"));
        Assert.assertNull(commandLine.getOptionValue("option1"));

        Option option2 = new Option("o", "option2", true, "Option 2 description");
        commandLine = new CommandLine(args);
        commandLine.parse(new DefaultParser().parse(new Options().addOption(option2), args));
        Assert.assertTrue(commandLine.hasOption("option2"));
        Assert.assertEquals("value1", commandLine.getOptionValue("option1"));
        Assert.assertEquals("value2", commandLine.getOptionValue("option2"));
    }

    /**
     * Test method for {@link CommandLine#CommandLine(String[])}.
     */
    @Test
    public void testCommandLineWithUnrecognizedArguments() {
        String[] args = {"--option1", "value1", "--unknown-option", "arg1", "arg2"};
        CommandLine commandLine = new CommandLine(args);
        commandLine.parse(new DefaultParser().parse(new Options(), args));

        Assert.assertEquals(args.length - 2, commandLine.getArgs().length);
        Assert.assertArrayEquals(Arrays.copyOfRange(args, 2, args.length), commandLine.getArgs());

        Assert.assertTrue(commandLine.hasOption("option1"));
        Assert.assertEquals("value1", commandLine.getOptionValue("option1"));
        Assert.assertNull(commandLine.getOptionValue("unknown-option"));
        Assert.assertArrayEquals(Arrays.copyOfRange(new String[]{"arg1", "arg2"}, 0, 2), commandLine.getArgs());
    }

    /**
     * Test method for {@link CommandLine#CommandLine(String[])}.
     */
    @Test(expected = ParseException.class)
    public void testCommandLineWithInvalidOption() {
        String[] args = {"--invalid-option", "value1", "--option1", "value2"};
        CommandLine commandLine = new CommandLine(args);
        commandLine.parse(new DefaultParser().parse(new Options(), args));
    }
}