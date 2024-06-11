package org.apache.commons.cli2.option;

import static org.junit.Assert.*;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.TestUtils;
import org.apache.commons.cli2.exceptions.OptionException;
import org.apache.commons.cli2.validators.MaxSizeValidator;
import org.apache.commons.cli2.validators.MinSizeValidator;

import java.util.Arrays;
import java.util.List;

public class ArgumentImplTest {

    @org.junit.Test
    public void testSimpleArgument() {
        Argument arg = new ArgumentImpl("arg", "An argument", 0, Integer.MAX_VALUE, 'a', 'a', null, null, null, 0);

        CommandLine cmd = TestUtils.createCommandLine();
        arg.process(cmd, Arrays.asList("--arg", "value1", "value2").iterator());
        assertEquals(2, cmd.getValues(arg).size());
        assertEquals("value1", cmd.getValues(arg).get(0));
        assertEquals("value2", cmd.getValues(arg).get(1));
    }

    @org.junit.Test
    public void testMinimumValueArgument() {
        Argument arg = new ArgumentImpl("arg", "An argument", 1, Integer.MAX_VALUE, 'a', 'a', null, null, null, 0);

        CommandLine cmd = TestUtils.createCommandLine();
        arg.process(cmd, Arrays.asList("--arg", "value1").iterator());
        assertEquals(1, cmd.getValues(arg).size());
        assertEquals("value1", cmd.getValues(arg).get(0));

        try {
            arg.process(cmd, Arrays.asList("--arg").iterator());
            fail();
        } catch (OptionException e) {
            // expected
        }
    }

    @org.junit.Test
    public void testMaximumValueArgument() {
        Argument arg = new ArgumentImpl("arg", "An argument", 0, 1, 'a', 'a', null, null, null, 0);

        CommandLine cmd = TestUtils.createCommandLine();
        arg.process(cmd, Arrays.asList("--arg", "value1", "value2").iterator());
        assertEquals(1, cmd.getValues(arg).size());
        assertEquals("value1", cmd.getValues(arg).get(0));

        try {
            arg.process(cmd, Arrays.asList("--arg", "value1", "value2", "value3").iterator());
            fail();
        } catch (OptionException e) {
            // expected
        }
    }

    @org.junit.Test
    public void testConsumeRemainingArgument() {
        Argument arg = new ArgumentImpl("arg", "An argument", 0, Integer.MAX_VALUE, 'a', 'a', null, "--", null, 0);

        CommandLine cmd = TestUtils.createCommandLine();
        arg.process(cmd, Arrays.asList("--arg--", "value1", "value2", "value3").iterator());
        assertEquals(3, cmd.getValues(arg).size());
        assertEquals("value1", cmd.getValues(arg).get(0));
        assertEquals("value2", cmd.getValues(arg).get(1));
        assertEquals("value3", cmd.getValues(arg).get(2));
    }

    @org.junit.Test
    public void testErrorOnInvalidValue() {
        Argument arg = new ArgumentImpl("arg", "An argument", 0, Integer.MAX_VALUE, 'a', 'a', null, null, null, 0);

        try {
            arg.validate(TestUtils.createCommandLine(), arg);
            fail();
        } catch (OptionException e) {
            // expected
        }

        CommandLine cmd = TestUtils.createCommandLine();
        arg.process(cmd, Arrays.asList("--arg", "value1", "value2", "--arg2").iterator());
        arg.validate(cmd, arg);

        try {
            arg.validate(TestUtils.createCommandLine(), arg);
            fail();
        } catch (OptionException e) {
            // expected
        }
    }

    @org.junit.Test
    public void testValidateWithMinSizeValidator() {
        Argument arg = new ArgumentImpl("arg", "An argument", 1, Integer.MAX_VALUE, 'a', 'a', new MinSizeValidator(1), null, null, 0);

        CommandLine cmd = TestUtils.createCommandLine();
        arg.process(cmd, Arrays.asList("--arg").iterator());

        try {
            arg.validate(cmd, arg);
            fail();
        } catch (OptionException e) {
            // expected
        }

        arg.process(cmd, Arrays.asList("--arg", "value1").iterator());
        arg.validate(cmd, arg);
    }

    @org.junit.Test
    public void testValidateWithMaxSizeValidator() {
        Argument arg = new ArgumentImpl("arg", "An argument", 0, 1, 'a', 'a', new MaxSizeValidator(1), null, null, 0);

        CommandLine cmd = TestUtils.createCommandLine();
        arg.process(cmd, Arrays.asList("--arg", "value1", "value2").iterator());

        try {
            arg.validate(cmd, arg);
            fail();
        } catch (OptionException e) {
            // expected
        }

        arg.process(cmd, Arrays.asList("--arg", "value1").iterator());
        arg.validate(cmd, arg);
    }

    @org.junit.Test
    public void testHelp() {
        Argument arg = new ArgumentImpl("arg", "An argument", 0, Integer.MAX_VALUE, 'a', 'a', null, null, null, 0);

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("test", Arrays.asList(arg), true);
        String expected = "-arg: " +
                "An argument\n" +
                "                  [--]  consume remaining arguments as values\n";
        assertTrue(expected.equals(formatter.getDisplayText()));
    }

    @org.junit.Test
    public void testHelpWithValues() {
        Argument arg = new ArgumentImpl("arg", "An argument", 0, Integer.MAX_VALUE, 'a', 'a', null, null, null, 0);

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("test", Arrays.asList(arg, OptionBuilder.withLongOpt("help").hasArg(false).isRequired(false)), true);
        String expected = "-arg:\n" +
                "  An argument\n" +
                "                  [--]  consume remaining arguments as values\n" +
                "\n" +
                "--help: \n" +
                "  print this help message";
        assertTrue(expected.equals(formatter.getDisplayText()));
    }
}