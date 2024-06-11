package org.apache.commons.cli.parser;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;

/**
 * Tests for {@link Parser} class.
 */
public class ParserTest
{
    private CommandLineParser parser;
    private Options options;

    @Before
    public void setUp()
    {
        options = new Options();
        options.addRequiredOption("i", "input", true, "input file")
                .setValueSeparator(':');
        parser = new DefaultParser();
    }

    /**
     * Tests parsing with valid options and arguments.
     */
    @Test
    public void testParseWithValidOptionsAndArguments()
            throws ParseException
    {
        String[] arguments = {"-i", "test.txt", "--no-option", "arg1", "--input",
                "test2.txt"};

        CommandLine commandLine = parser.parse(options, arguments);

        assertEquals(2, commandLine.getArgs().size());
        assertEquals("test.txt", commandLine.getOptionValues("input")[0]);
        assertFalse(commandLine.hasOption("no-option"));
    }

    /**
     * Tests parsing with no options and arguments.
     */
    @Test
    public void testParseWithNoOptionsAndArguments()
            throws ParseException
    {
        String[] arguments = {"arg1", "arg2"};

        CommandLine commandLine = parser.parse(options, arguments);

        assertEquals(2, commandLine.getArgs().size());
        assertNull(commandLine.getOptionValues("input"));
    }

    /**
     * Tests parsing with missing input option.
     */
    @Test
    public void testParseWithMissingInputOption()
            throws ParseException
    {
        String[] arguments = {"arg1", "arg2"};

        assertThrows(MissingArgumentException.class, () ->
                parser.parse(options, arguments));
    }

    /**
     * Tests parsing with unrecognized option.
     */
    @Test
    public void testParseWithUnrecognizedOption()
            throws ParseException
    {
        String[] arguments = {"--unknown-option"};

        assertThrows(UnrecognizedOptionException.class, () ->
                parser.parse(options, arguments));
    }

    /**
     * Tests parsing with required option missing.
     */
    @Test
    public void testParseWithRequiredOptionMissing()
            throws ParseException, MissingOptionException
    {
        String[] arguments = {"arg1", "arg2"};

        assertThrows(MissingOptionException.class, () ->
                parser.parse(options, arguments));
    }

    /**
     * Tests parsing with optional option missing.
     */
    @Test
    public void testParseWithOptionalOptionMissing()
            throws ParseException
    {
        String[] arguments = {"-i", "test.txt"};

        CommandLine commandLine = parser.parse(options, arguments);

        assertSame(options.getOption("input"), commandLine.getOption("input"));
        assertNull(commandLine.getOptionValues("no-option"));
    }

    /**
     * Tests parsing with multiple occurrences of the same option.
     */
    @Test
    public void testParseWithMultipleOccurrencesOfTheSameOption()
            throws ParseException
    {
        String[] arguments = {"-i", "test.txt", "-i", "test2.txt"};

        CommandLine commandLine = parser.parse(options, arguments);

        assertEquals(Arrays.asList("test.txt", "test2.txt"),
                Arrays.asList(commandLine.getOptionValues("input")));
    }

    /**
     * Tests parsing with multiple options having the same long option name.
     */
    @Test
    public void testParseWithMultipleOptionsHavingTheSameLongOptionName()
            throws ParseException
    {
        Options optionGroupOptions = new Options();
        optionGroupOptions.addOption("g", "group", true, "group option");
        optionGroupOptions.addOption("g", "group2", true, "group option 2");

        Options options = new Options();
        options.addOptionGroup(optionGroupOptions);

        String[] arguments = {"--group", "value1", "--group2", "value2"};

        CommandLine commandLine = parser.parse(options, arguments);

        assertEquals(2, commandLine.getOptions().size());
        assertSame(optionGroupOptions.getOption("group"),
                commandLine.getOption("group"));
        assertSame(optionGroupOptions.getOption("group2"),
                commandLine.getOption("group2"));
    }

    /**
     * Tests parsing with multiple options having the same short option name.
     */
    @Test(expected = ParseException.class)
    public void testParseWithMultipleOptionsHavingTheSameShortOptionName()
            throws ParseException
    {
        Options options = new Options();
        options.addOption("i", "input", true, "input file");
        options.addOption("i", "input2", true, "input file 2");

        String[] arguments = {"-i", "test.txt", "--input2", "test2.txt"};

        parser.parse(options, arguments);
    }

    /**
     * Tests parsing with an empty option group.
     */
    @Test
    public void testParseWithEmptyOptionGroup()
            throws ParseException
    {
        Options options = new Options();
        options.addOptionGroup(new Options());

        String[] arguments = {};

        CommandLine commandLine = parser.parse(options, arguments);

        assertEquals(0, commandLine.getOptions().size());
    }
}