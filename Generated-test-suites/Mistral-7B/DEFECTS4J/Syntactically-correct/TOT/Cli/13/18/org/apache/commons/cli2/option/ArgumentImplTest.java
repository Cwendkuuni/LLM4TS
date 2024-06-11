package org.apache.commons.cli2.option;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

public class ArgumentImplTest {

    @Test
    public void testArgumentWithMinimumOneAndMaximumOne() {
        Argument arg = new ArgumentImpl("arg", "an argument", 1, 1, '--', ' ', null, null, null, 0);
        WriteableCommandLine commandLine = new BasicParseResult();

        arg.defaults(commandLine);

        commandLine.parse(new String[]{"--arg"});
        Assert.assertEquals("--arg", commandLine.getValues("arg").get(0));

        try {
            commandLine.parse(new String[]{"--arg", "--arg"});
            Assert.fail();
        } catch (OptionException e) {
            Assert.assertEquals("The option 'arg' accepts exactly one value", e.getMessage());
        }
    }

    @Test
    public void testArgumentWithMinimumOneAndMaximumMany() {
        Argument arg = new ArgumentImpl("arg", "an argument", 1, 3, '--', ' ', null, null, null, 0);
        WriteableCommandLine commandLine = new BasicParseResult();

        commandLine.parse(new String[]{"--arg", "--arg", "--arg"});
        Assert.assertEquals(3, commandLine.getValues("arg").size());

        try {
            commandLine.parse(new String[]{"--arg", "--arg", "--arg", "--arg"});
            Assert.fail();
        } catch (OptionException e) {
            Assert.assertEquals("The option 'arg' accepts a maximum of 3 values", e.getMessage());
        }
    }

    @Test
    public void testArgumentWithMinimumZeroAndMaximumZero() {
        Argument arg = new ArgumentImpl("arg", "an argument", 0, 0, '--', ' ', null, null, null, 0);
        WriteableCommandLine commandLine = new BasicParseResult();

        commandLine.parse(new String[]{"--arg"});
        Assert.assertEquals(0, commandLine.getValues("arg").size());

        commandLine.parse(new String[]{"--arg", "--arg"});
        Assert.assertEquals(0, commandLine.getValues("arg").size());
    }

    @Test
    public void testArgumentWithMinimumZeroAndMaximumOne() {
        Argument arg = new ArgumentImpl("arg", "an argument", 0, 1, '--', ' ', null, null, null, 0);
        WriteableCommandLine commandLine = new BasicParseResult();

        commandLine.parse(new String[]{"--arg", "--arg"});
        Assert.assertEquals(0, commandLine.getValues("arg").size());

        commandLine.parse(new String[]{"--arg"});
        Assert.assertEquals(1, commandLine.getValues("arg").size());

        commandLine.parse(new String[]{"--arg", "--arg", "--arg"});
        Assert.assertEquals(1, commandLine.getValues("arg").size());
    }

    @Test
    public void testArgumentWithMinimumZeroAndMaximumMany() {
        Argument arg = new ArgumentImpl("arg", "an argument", 0, 3, '--', ' ', null, null, null, 0);
        WriteableCommandLine commandLine = new BasicParseResult();

        commandLine.parse(new String[]{"--arg", "--arg", "--arg", "--arg"});
        Assert.assertEquals(3, commandLine.getValues("arg").size());

        commandLine.parse(new String[]{"--arg", "--arg", "--arg", "--arg", "--arg"});
        Assert.assertEquals(3, commandLine.getValues("arg").size());
    }

    @Test
    public void testArgumentWithMinimumOneAndMaximumMany() {
        Argument arg = new ArgumentImpl("arg", "an argument", 1, 3, '--', ' ', null, null, null, 0);
        WriteableCommandLine commandLine = new BasicParseResult();

        commandLine.parse(new String[]{"--arg", "--arg", "--arg"});
        Assert.assertEquals(3, commandLine.getValues("arg").size());

        try {
            commandLine.parse(new String[]{"--arg", "--arg", "--arg", "--arg", "--arg"});
            Assert.fail();
        } catch (OptionException e) {
            Assert.assertEquals("The option 'arg' accepts a maximum of 3 values", e.getMessage());
        }
    }

    @Test
    public void testArgumentWithMinimumOneAndEmptyDefaultValues() {
        Argument arg = new ArgumentImpl("arg", "an argument", 1, 1, '--', ' ', null, null, null, 0);
        WriteableCommandLine commandLine = new BasicParseResult();

        arg.defaults(commandLine);

        commandLine.parse(new String[]{"--arg"});
        Assert.assertEquals(1, commandLine.getValues("arg").size());
        Assert.assertEquals("", commandLine.getValues("arg").get(0));

        commandLine.parse(new String[]{"--arg", "--arg"});
        Assert.assertEquals(2, commandLine.getValues("arg").size());
        Assert.assertEquals("", commandLine.getValues("arg").get(0));
        Assert.assertEquals("", commandLine.getValues("arg").get(1));
    }

    @Test
    public void testArgumentWithMinimumOneAndNonEmptyDefaultValues() {
        Argument arg = new ArgumentImpl("arg", "an argument", 1, 1, '--', ' ', null, null, Arrays.asList("value"), 0);
        WriteableCommandLine commandLine = new BasicParseResult();

        arg.defaults(commandLine);

        commandLine.parse(new String[]{"--arg"});
        Assert.assertEquals(1, commandLine.getValues("arg").size());
        Assert.assertEquals("value", commandLine.getValues("arg").get(0));

        commandLine.parse(new String[]{"--arg", "--arg"});
        Assert.assertEquals(2, commandLine.getValues("arg").size());
        Assert.assertEquals("value", commandLine.getValues("arg").get(0));
        Assert.assertEquals("", commandLine.getValues("arg").get(1));
    }

    @Test
    public void testArgumentWithConsumerOption() {
        Argument arg1 = new ArgumentImpl("arg1", "arg1", 0, 1, '--', ' ', null, "--consumer", null, 0);
        Argument arg2 = new ArgumentImpl("arg2", "arg2", 1, 1, '--', ' ', null, null, null, 0);
        WriteableCommandLine commandLine = new BasicParseResult();

        commandLine.parse(new String[]{"--consumer", "--arg1", "value1", "--arg2"});
        Assert.assertEquals(2, commandLine.size());
        Assert.assertEquals("value1", commandLine.getValues("arg1").get(0));
        Assert.assertEquals("", commandLine.getValues("arg2").get(0));

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("test", "Test command line parsing", true, commandLine, "");
    }

    @Test(expected = OptionException.class)
    public void testArgumentWithConsumerOptionAndMissingArg1() {
        Argument arg1 = new ArgumentImpl("arg1", "arg1", 0, 1, '--', ' ', null, "--consumer", null, 0);
        Argument arg2 = new ArgumentImpl("arg2", "arg2", 1, 1, '--', ' ', null, null, null, 0);
        WriteableCommandLine commandLine = new BasicParseResult();

        commandLine.parse(new String[]{"--consumer"});
    }

    @Test(expected = OptionException.class)
    public void testArgumentWithConsumerOptionAndMissingArg2() {
        Argument arg1 = new ArgumentImpl("arg1", "arg1", 0, 1, '--', ' ', null, "--consumer", null, 0);
        Argument arg2 = new ArgumentImpl("arg2", "arg2", 1, 1, '--', ' ', null, null, null, 0);
        WriteableCommandLine commandLine = new BasicParseResult();

        commandLine.parse(new String[]{"--consumer", "--arg1"});
    }

    @Test
    public void testArgumentWithConsumerOptionAndEmptyArg1() {
        Argument arg1 = new ArgumentImpl("arg1", "arg1", 0, 1, '--', ' ', null, "--consumer", null, 0);
        Argument arg2 = new ArgumentImpl("arg2", "arg2", 1, 1, '--', ' ', null, null, null, 0);
        WriteableCommandLine commandLine = new BasicParseResult();

        commandLine.parse(new String[]{"--consumer", "--arg1"});
        Assert.assertEquals(2, commandLine.size());
        Assert.assertEquals(0, commandLine.getValues("arg1").size());
        Assert.assertEquals("", commandLine.getValues("arg2").get(0));

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("test", "Test command line parsing", true, commandLine, "");
    }

    @Test
    public void testArgumentWithConsumerOptionAndValueArg1() {
        Argument arg1 = new ArgumentImpl("arg1", "arg1", 0, 1, '--', ' ', null, "--consumer", null, 0);
        Argument arg2 = new ArgumentImpl("arg2", "arg2", 1, 1, '--', ' ', null, null, null, 0);
        WriteableCommandLine commandLine = new BasicParseResult();

        commandLine.parse(new String[]{"--consumer", "--arg1", "value1", "--arg2"});
        Assert.assertEquals(2, commandLine.size());
        Assert.assertEquals("value1", commandLine.getValues("arg1").get(0));
        Assert.assertEquals("", commandLine.getValues("arg2").get(0));

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("test", "Test command line parsing", true, commandLine, "");
    }

    @Test
    public void testArgumentWithConsumerOptionAndValueArg1AndArg2() {
        Argument arg1 = new ArgumentImpl("arg1", "arg1", 0, 1, '--', ' ', null, "--consumer", null, 0);
        Argument arg2 = new ArgumentImpl("arg2", "arg2", 1, 1, '--', ' ', null, null, null, 0);
        WriteableCommandLine commandLine = new BasicParseResult();

        commandLine.parse(new String[]{"--consumer", "--arg1", "value1", "--arg2", "value2"});
        Assert.assertEquals(2, commandLine.size());
        Assert.assertEquals("value1", commandLine.getValues("arg1").get(0));
        Assert.assertEquals("value2", commandLine.getValues("arg2").get(0));

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("test", "Test command line parsing", true, commandLine, "");
    }
}