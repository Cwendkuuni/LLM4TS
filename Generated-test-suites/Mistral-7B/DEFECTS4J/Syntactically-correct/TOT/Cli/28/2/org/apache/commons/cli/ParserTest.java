package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli.*;
import org.apache.commons.cli.ParseException;
import org.junit.Test;

public class ParserTest
{
    @Test
    public void testParseWithBasicOptions() throws ParseException
    {
        String[] args = {"-a", "1", "--b=2", "-c", "true", "--d"};

        Parser parser = new DefaultParser();

        CommandLine result = parser.parse(new Options(), args);

        assertEquals("1", result.getOptionValue("a"));
        assertEquals("2", result.getOptionValue("b"));
        assertFalse(result.hasOption("c"));
        assertTrue(result.hasOption("d"));
        assertNull(result.getOptionValue("e"));
    }

    @Test
    public void testParseWithOptionalArgOptions() throws ParseException
    {
        String[] args = {"--foo=42", "--bar=abc"};

        Parser parser = new DefaultParser();

        Options options = new Options();
        Option fooOption = new Option("f", "foo", true, "Default value");
        Option barOption = new Option("b", "bar", true, "Default value");

        options.addOption(fooOption);
        options.addOption(barOption);

        CommandLine result = parser.parse(options, args);

        assertEquals("42", result.getOptionValue("foo"));
        assertEquals("abc", result.getOptionValue("bar"));
    }

    @Test
    public void testParseWithRequiredOptions() throws ParseException
    {
        String[] args = {"--foo", "--bar"};

        Parser parser = new DefaultParser();

        Options options = new Options();
        Option fooOption = new Option("f", "foo");
        Option barOption = new Option("b", "bar");

        fooOption.setRequired(true);
        barOption.setRequired(true);

        options.addOption(fooOption);
        options.addOption(barOption);

        assertThrows(MissingOptionException.class, () -> parser.parse(options, args));
    }

    @Test
    public void testParseWithGroupedOptions() throws ParseException
    {
        String[] args = {"--group", "-a=1", "-b=2", "-c=3"};

        Parser parser = new DefaultParser();

        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "Grouped options");
        Option aOption = new Option("a", "a");
        Option bOption = new Option("b", "b");

        group.addOption(aOption);
        group.addOption(bOption);

        options.addOption(group);

        CommandLine result = parser.parse(options, args);

        assertTrue(result.hasOption("group"));
        assertEquals("1", result.getOptionValue("a"));
        assertEquals("2", result.getOptionValue("b"));
    }

    @Test
    public void testParseWithMixedLongAndShortOptions() throws ParseException
    {
        String[] args = {"-a", "1", "--b=2", "-c", "--d=3", "-e", "--foo=42", "--qux"};

        Parser parser = new DefaultParser();

        CommandLine result = parser.parse(new Options(), args);

        assertEquals("1", result.getOptionValue("a"));
        assertEquals("2", result.getOptionValue("b"));
        assertFalse(result.hasOption("c"));
        assertTrue(result.hasOption("d"));
        assertEquals("3", result.getOptionValue("d"));
        assertFalse(result.hasOption("e"));
        assertNull(result.getOptionValue("foo"));
        assertTrue(result.hasOption("qux"));
    }

    @Test
    public void testParseWithTrailingArgs() throws ParseException
    {
        String[] args = {"-a", "1", "--b=2", "-c", "--d=3", "--", "arg1", "arg2"};

        Parser parser = new DefaultParser();

        CommandLine result = parser.parse(new Options(), args);

        assertEquals("1", result.getOptionValue("a"));
        assertEquals("2", result.getOptionValue("b"));
        assertFalse(result.hasOption("c"));
        assertTrue(result.hasOption("d"));
        assertEquals("3", result.getOptionValue("d"));
        assertTrue(result.getArgs().size() > 0);
        assertEquals("arg1", result.getArgs().get(0));
        assertEquals("arg2", result.getArgs().get(1));
    }
}