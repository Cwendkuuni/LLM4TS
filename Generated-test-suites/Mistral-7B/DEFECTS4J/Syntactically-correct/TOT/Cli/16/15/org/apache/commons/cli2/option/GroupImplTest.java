package org.apache.commons.cli2.test;

import org.apache.commons.cli2.CommandLineParser;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.OptionGroup;
import org.apache.commons.cli2.SystemOutStream;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class GroupImplTest {

    private static final String GROUP_NAME = "group";
    private static final String GROUP_DESCRIPTION = "A test group";

    private CommandLineParser parser;
    private HelpFormatter formatter;

    @Before
    public void setUp() {
        List<Option> options = new ArrayList<>();

        Option option1 = OptionBuilder.withLongOpt("option1")
                .withDescription("Option 1")
                .create(false);

        Option option2 = OptionBuilder.withLongOpt("option2")
                .withDescription("Option 2")
                .create(false);

        Option option3 = OptionBuilder.withLongOpt("option3")
                .withDescription("Option 3")
                .create(false);

        Option argument = OptionBuilder.withLongOpt("argument")
                .withDescription("Argument")
                .isRequired(false)
                .create(null);

        Option group = new OptionGroupImpl(new ArrayList<>(List.of(option1, option2, option3)),
                GROUP_NAME, GROUP_DESCRIPTION, 1, Integer.MAX_VALUE);
        options.add(argument);

        parser = new DefaultParser();
        formatter = new HelpFormatter();
    }

    @Test
    public void testValidGroupUsage() {
        try {
            WriteableCommandLine commandLine = new WriteableCommandLine('-', System.out);
            parser.parse(commandLine, options.toArray(new Option[]{}), null);
            formatter.printHelp("Test", options, true);
        } catch (ParseException e) {
            fail("Invalid parsing: " + e.getMessage());
        }
    }

    @Test
    public void testMinimumGroupUsage() {
        try {
            WriteableCommandLine commandLine = new WriteableCommandLine('-', System.out);
            parser.parse(commandLine, new Object[]{OptionBuilder.withLongOpt("option1").create(false)}, null);
            formatter.printHelp("Test", options, true);
            fail("Expected OptionException but no error was thrown.");
        } catch (ParseException e) {
            if (!e.getMessage().equals("Missing option: " + OptionBuilder.withLongOpt("option2").getLongOpt())) {
                fail("Unexpected error message: " + e.getMessage());
            }
        }
    }

    @Test
    public void testMaximumGroupUsage() {
        try {
            WriteableCommandLine commandLine = new WriteableCommandLine('-', System.out);
            parser.parse(commandLine, new Object[]{OptionBuilder.withLongOpt("option1").create(false),
                                                   OptionBuilder.withLongOpt("option1").create(false)}, null);
            formatter.printHelp("Test", options, true);
            fail("Expected OptionException but no error was thrown.");
        } catch (ParseException e) {
            if (!e.getMessage().equals("Duplicate option: " + OptionBuilder.withLongOpt("option1").getLongOpt())) {
                fail("Unexpected error message: " + e.getMessage());
            }
        }
    }

    @Test
    public void testGroupWithArgument() {
        try {
            WriteableCommandLine commandLine = new WriteableCommandLine('-', System.out);
            parser.parse(commandLine, options.toArray(new Option[]{}), null);
            commandLine.parseArgument(0, "test");
            formatter.printHelp("Test", options, true);

            assertEquals(1, commandLine.getArgs().size());
            assertEquals("test", commandLine.getArgs().get(0));

        } catch (ParseException e) {
            fail("Invalid parsing: " + e.getMessage());
        }
    }

    @Test
    public void testMixedUsage() {
        try {
            WriteableCommandLine commandLine = new WriteableCommandLine('-', System.out);
            parser.parse(commandLine, new Object[]{OptionBuilder.withLongOpt("option1").create(false),
                                                   OptionBuilder.withLongOpt("option2").create(false)}, null);
            commandLine.parseArgument(0, "test");
            formatter.printHelp("Test", options, true);

            assertEquals(1, commandLine.getArgs().size());
            assertEquals("test", commandLine.getArgs().get(0));

            formatter.printHelp("Test", options, true);

        } catch (ParseException e) {
            fail("Invalid parsing: " + e.getMessage());
        }
    }

    @Test
    public void testInvalidGroupUsage() {
        try {
            WriteableCommandLine commandLine = new WriteableCommandLine('-', System.out);
            parser.parse(commandLine, options.toArray(new Option[]{}), null);
            commandLine.parseArgument(0, "invalid");
            formatter.printHelp("Test", options, true);
            fail("Expected OptionException but no error was thrown.");
        } catch (ParseException e) {
            if (!e.getMessage().equals("Invalid option: 'invalid'")) {
                fail("Unexpected error message: " + e.getMessage());
            }
        }
    }
}