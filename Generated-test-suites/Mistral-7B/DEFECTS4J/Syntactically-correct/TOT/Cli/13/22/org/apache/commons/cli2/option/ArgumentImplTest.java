package org.apache.commons.cli2.option;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class ArgumentImplTest {

    private Argument argument;
    private Options options;
    private HelpFormatter formatter;
    private WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        argument = new ArgumentImpl("arg", "An argument", 1, 1, 'a', 'b', null, "--", Collections.emptyList(), 0);
        options = new Options();
        formatter = new HelpFormatter();
        commandLine = new CommandLine(options);
    }

    @Test
    public void testValidArgument() throws Exception {
        options.addOption(argument);
        commandLine.parse("--arg value1 value2");
        List<String> args = commandLine.getArgs();
        Assert.assertEquals(2, args.size());
        Assert.assertEquals("value1", args.get(0));
        Assert.assertEquals("value2", args.get(1));
    }

    @Test
    public void testOptionalArgument() throws Exception {
        argument = new ArgumentImpl("arg", "An argument", 0, 1, 'a', 'b', null, "--", Collections.emptyList(), 0);
        options.addOption(argument);
        commandLine.parse("--arg value");
        List<String> args = commandLine.getArgs();
        Assert.assertEquals(0, args.size());
        commandLine.parse("--arg value1 value2");
        args = commandLine.getArgs();
        Assert.assertEquals(2, args.size());
        Assert.assertEquals("value1", args.get(0));
        Assert.assertEquals("value2", args.get(1));
    }

    @Test(expected = OptionException.class)
    public void testTooFewArguments() throws Exception {
        argument = new ArgumentImpl("arg", "An argument", 1, 1, 'a', 'b', null, "--", Collections.emptyList(), 0);
        options.addOption(argument);
        commandLine.parse("--arg");
    }

    @Test(expected = OptionException.class)
    public void testTooManyArguments() throws Exception {
        argument = new ArgumentImpl("arg", "An argument", 1, 1, 'a', 'b', null, "--", Collections.emptyList(), 0);
        options.addOption(argument);
        commandLine.parse("--arg value1 value2 value3");
    }

    @Test
    public void testConsumeRemaining() throws Exception {
        argument = new ArgumentImpl("arg", "An argument", 0, Integer.MAX_VALUE, 'a', 'b', null, "--consume", Collections.emptyList(), 0);
        options.addOption(argument);
        commandLine.parse("--arg value1 --consume value3 value4 value5");
        List<String> args = commandLine.getArgs();
        Assert.assertEquals(2, args.size());
        Assert.assertEquals("value1", args.get(0));
        Assert.assertEquals("value3 value4 value5", args.get(1));
    }

    @Test
    public void testHelp() {
        argument = new ArgumentImpl("arg", "An argument", 1, 1, 'a', 'b', null, "--", Collections.emptyList(), 0);
        options.addOption(argument);
        formatter.printHelp("Test", options);
    }
}