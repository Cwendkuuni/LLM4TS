package org.apache.commons.cli2.option;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArgumentImplTest {

    private Argument argument;
    private Options options;

    @Before
    public void setUp() {
        argument = new ArgumentImpl("a", "An argument.", 1, 3, 'a', 'b', null, "--", Arrays.asList("default1", "default2"), 0);
        options = new Options();
        options.addOption(argument);
    }

    @Test
    public void testProcessValues() {
        CommandLine cmd = null;
        List arguments = Arrays.asList("--a", "--a arg1", "--a arg1 arg2", "--a arg1 arg2 arg3");

        try {
            cmd = options.parse(arguments);
        } catch (ParseException e) {
            Assert.fail("Failed to parse command line: " + e.getMessage());
        }

        Argument a = cmd.getOption("a");
        List values = a.getValues();
        Assert.assertEquals(values.size(), 3);
        Assert.assertEquals(values.get(0), "arg1");
        Assert.assertEquals(values.get(1), "arg2");
        Assert.assertEquals(values.get(2), "arg3");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProcessValuesWithTooFewArguments() {
        CommandLine cmd = null;
        List arguments = Arrays.asList("--a");

        try {
            cmd = options.parse(arguments);
        } catch (ParseException e) {
            Assert.fail("Failed to parse command line: " + e.getMessage());
        }

        Argument a = cmd.getOption("a");
        List values = a.getValues();
        Assert.assertEquals(values.size(), 1);
        Assert.assertEquals(values.get(0), "arg1");

        // This should throw an exception
        argument.processValues(cmd, null, argument);
    }

    @Test(expected = OptionException.class)
    public void testProcessValuesWithInvalidArgument() {
        CommandLine cmd = null;
        List arguments = Arrays.asList("--a", "--a arg1", "--a arg1 arg2", "--a arg1 arg2 arg4");

        try {
            cmd = options.parse(arguments);
        } catch (ParseException e) {
            Assert.fail("Failed to parse command line: " + e.getMessage());
        }

        Argument a = cmd.getOption("a");
        List values = a.getValues();
        Assert.assertEquals(values.size(), 3);
        Assert.assertEquals(values.get(0), "arg1");
        Assert.assertEquals(values.get(1), "arg2");
        Assert.assertEquals(values.get(2), "arg3");

        try {
            argument.processValues(cmd, null, argument);
        } catch (OptionException e) {
            // expected
        }
        Assert.assertEquals(cmd.getValues("a"), Arrays.asList("arg1", "arg2", "arg3"));

        try {
            ArgumentImpl incorrectArgument = new ArgumentImpl("a", "An argument.", 1, 3, 'a', 'b', null, "--", Arrays.asList("default1", "default2"), 0);
            incorrectArgument.processValues(cmd, null, incorrectArgument);
        } catch (OptionException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }

        // This should throw an OptionException
        argument.processValues(cmd, null, argument);
    }

    @Test
    public void testProcessValuesWithRemainingArguments() {
        CommandLine cmd = null;
        List arguments = Arrays.asList("--consume", "--a arg1", "arg1", "arg2", "arg3");

        try {
            cmd = options.parse(arguments);
        } catch (ParseException e) {
            Assert.fail("Failed to parse command line: " + e.getMessage());
        }

        Argument a = cmd.getOption("a");
        List values = a.getValues();
        Assert.assertEquals(values.size(), 2);
        Assert.assertEquals(values.get(0), "arg1");
        Assert.assertEquals(values.get(1), "arg2");

        List remainingArguments = cmd.getRemainingArgs();
        Assert.assertEquals(remainingArguments.size(), 2);
        Assert.assertEquals(remainingArguments.get(0), "arg3");
        Assert.assertEquals(remainingArguments.get(1), "arg4"); // should be ignored

        argument.processValues(cmd, null, argument);
    }

    @Test
    public void testDefaultValues() {
        CommandLine cmd = null;
        List arguments = Arrays.asList("");

        try {
            cmd = options.parse(arguments);
        } catch (ParseException e) {
            Assert.fail("Failed to parse command line: " + e.getMessage());
        }

        Argument a = cmd.getOption("a");
        Assert.assertEquals(a.getValues().size(), 2);
        Assert.assertEquals(a.getValues().get(0), "default1");
        Assert.assertEquals(a.getValues().get(1), "default2");
    }

    @Test
    public void testDefaultValuesWithSpecifiedValue() {
        CommandLine cmd = null;
        List arguments = Arrays.asList("--a", "value");

        try {
            cmd = options.parse(arguments);
        } catch (ParseException e) {
            Assert.fail("Failed to parse command line: " + e.getMessage());
        }

        Argument a = cmd.getOption("a");
        Assert.assertEquals(a.getValues().size(), 1);
        Assert.assertEquals(a.getValues().get(0), "value");
    }

    @Test
    public void testHelp() {
        HelpFormatter formatter = new HelpFormatter();
        String help = formatter.printHelp("Test", options);

        Assert.assertFalse(help.isEmpty());
        Assert.assertTrue(help.contains("An argument."));
        Assert.assertTrue(help.contains("--a <arg> [arg] [arg]"));
        Assert.assertTrue(help.contains("arg - optional argument"));
        Assert.assertTrue(help.contains("arg1 arg2 arg3 - multiple arguments"));
        Assert.assertTrue(help.contains("consumes the remaining arguments"));
    }
}