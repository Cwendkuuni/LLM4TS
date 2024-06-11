package org.apache.commons.cli2.option;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.ParseException;
import org.apache.commons.cli2.Parser;
import org.apache.commons.cli2.ResourceHelper;
import org.apache.commons.cli2.TestUtils;
import org.apache.commons.cli2.validation.MockValidator;

public class ArgumentImplTest {

    private static final ResourceHelper resources = ResourceHelper.getResourceHelper();

    @org.junit.Test
    public void testArgumentWithNoDefaults() {
        Argument option = new ArgumentImpl(
                "arg",
                "description for arg",
                1,
                1,
                ' ',
                ' ',
                new MockValidator(),
                "--",
                null,
                0);

        Parser parser = TestUtils.createParser();
        parser.parse(parser.parseArguments(new String[]{"--arg", "--arg2"}));
        CommandLine commandLine = parser.getCommandLine();
        List<String> values = commandLine.getValues("arg");
        assertEquals(Arrays.asList("arg2"), values);
        assertNull(commandLine.getValues("nonExistentArg"));
    }

    @org.junit.Test
    public void testArgumentWithDefaultValues() {
        Argument option = new ArgumentImpl(
                "arg",
                "description for arg",
                1,
                1,
                ' ',
                ' ',
                new MockValidator(),
                "--",
                Arrays.asList("default1", "default2"),
                0);

        Parser parser = TestUtils.createParser();
        CommandLine commandLine = parser.parseArguments(new String[]{"--arg", "argValue1"});
        List<String> values = commandLine.getValues("arg");
        assertEquals(Arrays.asList("argValue1"), values);
        commandLine = parser.parseArguments(new String[]{"--arg"});
        values = commandLine.getValues("arg");
        assertEquals(Arrays.asList("default1", "default2"), values);
        commandLine = parser.parseArguments(new String[]{"--arg", "--arg2", "argValue2"});
        assertThrows(ParseException.class, () -> parser.parseArguments(new String[]{"--arg", "argValue2", "argValue3"}));
    }

    @org.junit.Test
    public void testArgumentWithMinimumAndMaximum() {
        Argument option = new ArgumentImpl(
                "arg",
                "description for arg",
                1,
                3,
                ' ',
                ' ',
                new MockValidator(),
                "--",
                Arrays.asList("default1", "default2"),
                0);

        Parser parser = TestUtils.createParser();
        List<String> args = Arrays.asList("--arg1", "--arg2", "--arg3", "--arg4");
        CommandLine commandLine = parser.parseArguments(args.toArray(new String[0]));
        List<String> values = commandLine.getValues("arg");
        assertEquals(Arrays.asList("arg4"), values);
        assertNull(commandLine.getValues("nonExistentArg"));
    }

    @org.junit.Test
    public void testArgumentWithInitialSeparator() {
        Argument option = new ArgumentImpl(
                "arg",
                "description for arg",
                1,
                1,
                ':',
                ' ',
                new MockValidator(),
                "--",
                null,
                0);

        List<String> args = Arrays.asList("--arg:argValue1", "--arg:argValue2");
        Parser parser = TestUtils.createParser();
        CommandLine commandLine = parser.parseArguments(args.toArray(new String[0]));
        List<String> values = commandLine.getValues("arg");
        assertEquals(Arrays.asList("argValue1", "argValue2"), values);
    }

    @org.junit.Test
    public void testArgumentWithSubsequentSeparator() {
        Argument option = new ArgumentImpl(
                "arg",
                "description for arg",
                1,
                1,
                ' ',
                ',',
                new MockValidator(),
                "--",
                null,
                0);

        List<String> args = Arrays.asList("--arg argValue1,argValue2");
        Parser parser = TestUtils.createParser();
        CommandLine commandLine = parser.parseArguments(args.toArray(new String[0]));
        List<String> values = commandLine.getValues("arg");
        assertEquals(Arrays.asList("argValue1", "argValue2"), values);
    }

    @org.junit.Test
    public void testArgumentWithCustomValidators() {
        Argument option = new ArgumentImpl(
                "arg",
                "description for arg",
                1,
                1,
                ' ',
                ' ',
                new MyCustomValidator(),
                "--",
                null,
                0);

        List<String> args = Arrays.asList("--arg argValue1", "--arg argValue2");
        Parser parser = TestUtils.createParser();
        CommandLine commandLine = parser.parseArguments(args.toArray(new String[0]));
        List<String> values = commandLine.getValues("arg");
        assertEquals(Arrays.asList("argValue1", "argValue2"), values);

        List<String> argsWithInvalidValue = Arrays.asList("--arg argValue1", "--arg argValue2", "--arg invalidValue");
        assertThrows(ParseException.class, () -> parser.parseArguments(argsWithInvalidValue.toArray(new String[0])));
    }

    @org.junit.Test
    public void testArgumentWithConsumeRemaining() {
        Argument option = new ArgumentImpl(
                "arg",
                "description for arg",
                1,
                1,
                ' ',
                ' ',
                new MockValidator(),
                "--",
                null,
                0);

        List<String> args = Arrays.asList("--arg", "--consumeRemaining");
        Parser parser = TestUtils.createParser();
        CommandLine commandLine = parser.parseArguments(args.toArray(new String[0]));
        List<String> values = commandLine.getValues("arg");
        assertEquals(Arrays.asList("consumeRemaining"), values);

        List<String> argsWithInvalidConsumeRemaining = Arrays.asList("--arg consumeRemaining", "--arg argValue1");
        assertThrows(ParseException.class, () -> parser.parseArguments(argsWithInvalidConsumeRemaining.toArray(new String[0])));
    }

    @org.junit.Test
    public void testArgumentWithHelp() {
        Argument option = new ArgumentImpl(
                "arg",
                "description for arg",
                1,
                1,
                ' ',
                ' ',
                new MockValidator(),
                "--",
                null,
                0);

        Parser parser = TestUtils.createParser();
        HelpFormatter formatter = new HelpFormatter();
        String help = formatter.printHelp("MyApplication", Arrays.asList("--arg argValue"), option.getHelpLines(0, null, null).get(0));
        assertTrue(help.contains("description for arg"));
        assertTrue(help.contains("argValue"));
    }

    private static class MyCustomValidator implements Validator {
        @Override
        public boolean validate(List<?> values) {
            List<String> stringValues = (List<String>) values;
            for (String value : stringValues) {
                if (!value.equals("validValue")) {
                    throw new InvalidArgumentException("validation error");
                }
            }
            return true;
        }
    }
}