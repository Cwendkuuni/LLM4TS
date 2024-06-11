package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest
{
    @Test
    public void testParseSimple() throws ParseException
    {
        Options options = new Options();
        options.addOption("a", true, "option A");
        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] args = {"-a"};
        CommandLine cmd = parser.parse(options, args);
        assertTrue(cmd.hasOption("a"));
    }

    @Test(expected = ParseException.class)
    public void testParseMissingOption() throws ParseException
    {
        Options options = new Options();
        options.addOption("a", true, "option A");
        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] args = {};
        parser.parse(options, args);
        fail("Missing required option: a");
    }

    @Test
    public void testParseOptionalArg() throws ParseException
    {
        Options options = new Options();
        options.addOption("a", true, "option A");
        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] args = {"-a", "value"};
        CommandLine cmd = parser.parse(options, args);
        assertEquals("value", cmd.getOptionValue("a"));
    }

    @Test(expected = ParseException.class)
    public void testParseMissingOptionalArg() throws ParseException
    {
        Options options = new Options();
        options.addOption("a", true, "option A");
        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] args = {"-a"};
        parser.parse(options, args);
        fail("Missing argument for option: a");
    }

    @Test
    public void testParseMultipleOptions() throws ParseException
    {
        Options options = new Options();
        options.addOption("a", true, "option A");
        options.addOption("b", false, "option B");
        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] args = {"-a", "-b"};
        CommandLine cmd = parser.parse(options, args);
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
    }

    @Test
    public void testParseMultipleOptionsWithValue() throws ParseException
    {
        Options options = new Options();
        options.addOption("a", true, "option A");
        options.addOption("b", true, "option B");
        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] args = {"-a", "valueA", "-b", "valueB"};
        CommandLine cmd = parser.parse(options, args);
        assertEquals("valueA", cmd.getOptionValue("a"));
        assertEquals("valueB", cmd.getOptionValue("b"));
    }

    @Test
    public void testParseLongOptions() throws ParseException
    {
        Options options = new Options();
        options.addOption("long-option-a", true, "long option A");
        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] args = {"--long-option-a"};
        CommandLine cmd = parser.parse(options, args);
        assertTrue(cmd.hasOption("long-option-a"));
    }

    @Test
    public void testParseMultipleLongOptions() throws ParseException
    {
        Options options = new Options();
        options.addOption("long-option-a", true, "long option A");
        options.addOption("long-option-b", true, "long option B");
        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] args = {"--long-option-a", "--long-option-b"};
        CommandLine cmd = parser.parse(options, args);
        assertTrue(cmd.hasOption("long-option-a"));
        assertTrue(cmd.hasOption("long-option-b"));
    }

    @Test(expected = ParseException.class)
    public void testParseUnknownLongOption() throws ParseException
    {
        Options options = new Options();
        options.addOption("long-option-a", true, "long option A");
        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] args = {"--unknown-long-option"};
        parser.parse(options, args);
        fail("Unrecognized option: unknown-long-option");
    }

    @Test(expected = ParseException.class)
    public void testParseMissingRequiredOption() throws ParseException
    {
        Options options = new Options();
        options.addOption("a", true, "option A");
        options.addRequiredOption("b", false, "option B");
        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] args = {"-a"};
        parser.parse(options, args);
        fail("Missing required option: b");
    }

    @Test
    public void testParseProperties() throws ParseException
    {
        Options options = new Options();
        options.addOption("a", true, "option A");
        Parser parser = new DefaultParser();
        parser.setOptions(options);

        Properties properties = new Properties();
        properties.setProperty("a", "value");

        String[] args = {};
        CommandLine cmd = parser.parse(options, args, properties);
        assertTrue(cmd.hasOption("a"));
        assertEquals("value", cmd.getOptionValue("a"));
    }
}