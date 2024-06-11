package org.apache.commons.cli2.option;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.parse.ParseResult;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArgumentImplTest {

    @org.junit.Test
    public void testArgumentWithNoDefaults() {
        Argument argument = new ArgumentImpl("arg", "description", 1, 1, ArgumentImpl.DEFAULT_INITIAL_SEPARATOR,
                ArgumentImpl.DEFAULT_SUBSEQUENT_SEPARATOR, null, null, Arrays.asList());

        Options options = new Options();
        options.addOption(argument);

        CommandLine commandLine = new CommandLine();
        ParseResult parseResult = commandLine.parse(options, new String[]{"arg1"});

        List<String> values = commandLine.getValues(argument);
        assertEquals(1, values.size());
        assertEquals("arg1", values.get(0));

        argument.process(commandLine, commandLine.getArgs().listIterator(0));

        values = commandLine.getValues(argument);
        assertEquals(1, values.size());
        assertEquals("arg1", values.get(0));
    }

    @org.junit.Test
    public void testArgumentWithDefaults() {
        Argument argument = new ArgumentImpl("arg", "description", 1, 1, ArgumentImpl.DEFAULT_INITIAL_SEPARATOR,
                ArgumentImpl.DEFAULT_SUBSEQUENT_SEPARATOR, null, null, Arrays.asList("default1", "default2"));

        Options options = new Options();
        options.addOption(argument);

        CommandLine commandLine = new CommandLine();
        ParseResult parseResult = commandLine.parse(options, new String[]{});

        List<String> values = commandLine.getValues(argument);
        assertEquals(2, values.size());
        assertEquals("default1", values.get(0));
        assertEquals("default2", values.get(1));

        argument.process(commandLine, commandLine.getArgs().listIterator(0));

        values = commandLine.getValues(argument);
        assertEquals(2, values.size());
        assertEquals("default1", values.get(0));
        assertEquals("default2", values.get(1));
    }

    @org.junit.Test
    public void testArgumentWithMinimumAndMaximumValues() {
        Argument argument = new ArgumentImpl("arg", "description", 2, 3, ArgumentImpl.DEFAULT_INITIAL_SEPARATOR,
                ArgumentImpl.DEFAULT_SUBSEQUENT_SEPARATOR, null, null, Arrays.asList("default1", "default2"));

        Options options = new Options();
        options.addOption(argument);

        CommandLine commandLine = new CommandLine();
        ParseResult parseResult = commandLine.parse(options, new String[]{"arg1", "arg2", "arg3"});

        List<String> values = commandLine.getValues(argument);
        assertEquals(3, values.size());
        assertEquals("arg1", values.get(0));
        assertEquals("arg2", values.get(1));
        assertEquals("arg3", values.get(2));

        parseResult = commandLine.parse(options, new String[]{"arg1", "arg2"});

        values = commandLine.getValues(argument);
        assertEquals(2, values.size());
        assertEquals("arg1", values.get(0));
        assertEquals("arg2", values.get(1));

        parseResult = commandLine.parse(options, new String[]{"arg1"});

        values = commandLine.getValues(argument);
        assertEquals(1, values.size());
        assertEquals("arg1", values.get(0));

        parseResult = commandLine.parse(options, new String[]{});

        values = commandLine.getValues(argument);
        assertNull(values);
    }

    @org.junit.Test
    public void testArgumentWithCustomInitialAndSubsequentSeparator() {
        Argument argument = new ArgumentImpl("arg", "description", 1, 1, '|', ';', null, null, Arrays.asList());

        Options options = new Options();
        options.addOption(argument);

        CommandLine commandLine = new CommandLine();
        ParseResult parseResult = commandLine.parse(options, new String[]{"arg1|arg2;arg3"});

        List<String> values = commandLine.getValues(argument);
        assertEquals(3, values.size());
        assertEquals("arg1", values.get(0));
        assertEquals("arg2;arg3", values.get(1));

        argument.process(commandLine, commandLine.getArgs().listIterator(0));

        values = commandLine.getValues(argument);
        assertEquals(1, values.size());
        assertEquals("arg1", values.get(0));
    }

    @org.junit.Test
    public void testArgumentWithInvalidValues() {
        Argument argument = new ArgumentImpl("arg", "description", 1, 1, ArgumentImpl.DEFAULT_INITIAL_SEPARATOR,
                ArgumentImpl.DEFAULT_SUBSEQUENT_SEPARATOR, new MyValidator(), null, Arrays.asList());

        Options options = new Options();
        options.addOption(argument);

        CommandLine commandLine = new CommandLine();
        ParseResult parseResult = commandLine.parse(options, new String[]{"arg1", "invalid"});

        try {
            argument.process(commandLine, commandLine.getArgs().listIterator(0));
            fail();
        } catch (OptionException e) {
            assertEquals(OptionException.CODE_ILLEGAL_ARGUMENT, e.getCode());
            assertEquals("invalid", e.getArgument());
        }

        parseResult = commandLine.parse(options, new String[]{"arg1", "arg2", "invalid"});

        try {
            argument.process(commandLine, commandLine.getArgs().listIterator(2));
            fail();
        } catch (OptionException e) {
            assertEquals(OptionException.CODE_ILLEGAL_ARGUMENT, e.getCode());
            assertEquals("invalid", e.getArgument());
        }
    }

    @org.junit.Test
    public void testArgumentWithCustomConsumingOption() {
        Argument argument = new ArgumentImpl("arg", "description", 1, 1, ArgumentImpl.DEFAULT_INITIAL_SEPARATOR,
                ArgumentImpl.DEFAULT_SUBSEQUENT_SEPARATOR, null, "--consume", Arrays.asList());

        Options options = new Options();
        options.addOption(argument);

        CommandLine commandLine = new CommandLine();
        ParseResult parseResult = commandLine.parse(options, new String[]{"--consume", "arg1", "arg2"});

        List<String> values = commandLine.getValues(argument);
        assertEquals(2, values.size());
        assertEquals("arg1", values.get(0));
        assertEquals("arg2", values.get(1));

        argument.process(commandLine, commandLine.getArgs().listIterator(0));

        values = commandLine.getValues(argument);
        assertEquals(2, values.size());
        assertEquals("arg1", values.get(0));
        assertEquals("arg2", values.get(1));
    }

    private static class MyValidator implements Validator {
        @Override
        public void validate(List<String> values) throws InvalidArgumentException {
            for (String value : values) {
                if (value.length() > 10) {
                    throw new InvalidArgumentException("value too long", value);
                }
            }
        }
    }

    @org.junit.Test
    public void testArgumentPrintUsage() {
        Argument argument = new ArgumentImpl("arg", "description", 1, 1, ArgumentImpl.DEFAULT_INITIAL_SEPARATOR,
                ArgumentImpl.DEFAULT_SUBSEQUENT_SEPARATOR, null, null, Arrays.asList());

        HelpFormatter formatter = new HelpFormatter();

        Options options = new Options();
        options.addOption(argument);

        String usageHelp = formatter.printUsage(options);

        System.out.println(usageHelp);

        int argIndex = usageHelp.indexOf("arg");

        String argumentHelp = usageHelp.substring(argIndex).split("\n")[0];

        assertEquals("arg\n  arg: description\n", argumentHelp);
    }

    @org.junit.Test
    public void testArgumentPrintHelpLines() {
        Argument argument = new ArgumentImpl("arg", "description", 1, 1, ArgumentImpl.DEFAULT_INITIAL_SEPARATOR,
                ArgumentImpl.DEFAULT_SUBSEQUENT_SEPARATOR, null, null, Arrays.asList());

        HelpFormatter formatter = new HelpFormatter();

        Options options = new Options();
        options.addOption(argument);

        List<HelpLine> helpLines = argument.helpLines(1, Collections.singleton(DisplaySetting.DEFAULT), null);

        for (HelpLine helpLine : helpLines) {
            System.out.println(helpLine.getShortOption());
            System.out.println(helpLine.getLongOption());
            System.out.println(helpLine.getDescription());
        }

        assertEquals("arg", helpLines.get(0).getShortOption());
        assertEquals("arg", helpLines.get(0).getLongOption());
        assertEquals("description", helpLines.get(0).getDescription());
    }
}