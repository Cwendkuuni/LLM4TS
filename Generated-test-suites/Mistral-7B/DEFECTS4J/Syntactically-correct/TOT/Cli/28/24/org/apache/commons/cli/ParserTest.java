package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest
{
    @Test
    public void testSimpleOption() throws ParseException
    {
        String[] args = {"-a", "value"};
        Options options = new Options();
        options.addOption("a", true, "simpleOption");
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);
        assertEquals("value", cmd.getOptionValue("a"));
        assertFalse(cmd.hasOption("b"));
    }

    @Test
    public void testRequiredOption() throws ParseException
    {
        String[] args = {"--input", "input.txt"};
        Options options = new Options();
        options.addOption("input", true, true, "An input file");
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);
        assertEquals("input.txt", cmd.getOptionValue("input"));
        assertTrue(cmd.hasOption("input"));
    }

    @Test(expected = ParseException.class)
    public void testMissingRequiredOption() throws ParseException
    {
        String[] args = {"--input"};
        Options options = new Options();
        options.addOption("input", true, true, "An input file");
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);
        fail("Expected ParseException but no exception was thrown");
    }

    @Test
    public void testLongOption() throws ParseException
    {
        String[] args = {"--long-option", "value"};
        Options options = new Options();
        options.addOption("longOption", true, "longOption");
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);
        assertEquals("value", cmd.getOptionValue("longOption"));
        assertFalse(cmd.hasOption("shortOption"));
    }

    @Test
    public void testShortOption() throws ParseException
    {
        String[] args = {"-o", "value"};
        Options options = new Options();
        options.addOption("o", true, "shortOption");
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);
        assertEquals("value", cmd.getOptionValue("o"));
        assertFalse(cmd.hasOption("longOption"));
    }

    @Test
    public void testShortOptionWithLongOption() throws ParseException
    {
        String[] args = {"-o", "value", "--long-option", "value2"};
        Options options = new Options();
        options.addOption("o", true, "shortOption");
        options.addOption("longOption", true, "longOption");
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);
        assertEquals("value", cmd.getOptionValue("o"));
        assertEquals("value2", cmd.getOptionValue("longOption"));
    }

    @Test(expected = ParseException.class)
    public void testInvalidOption() throws ParseException
    {
        String[] args = {"--invalid"};
        Options options = new Options();
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);
        fail("Expected ParseException but no exception was thrown");
    }

    @Test
    public void testEmptyArgument() throws ParseException
    {
        String[] args = {"--option", ""};
        Options options = new Options();
        options.addOption("option", true, "option");
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);
        assertNull(cmd.getOptionValue("option"));
    }

    @Test
    public void testArgument() throws ParseException
    {
        String[] args = {"--option", "value"};
        Options options = new Options();
        options.addOption("option", true, "option");
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);
        assertEquals("value", cmd.getOptionValue("option"));
    }

    @Test
    public void testDoubleDash() throws ParseException
    {
        String[] args = {"--", "--option", "value"};
        Options options = new Options();
        options.addOption("option", true, "option");
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);
        assertEquals("value", cmd.getOptionValue("option"));
    }

    @Test
    public void testDoubleDashWithOption() throws ParseException
    {
        String[] args = {"--", "--option", "value", "--option"};
        Options options = new Options();
        options.addOption("option", true, "option");
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);
        assertEquals("value", cmd.getOptionValue("option"));
        assertTrue(cmd.hasOption("option"));
    }

    @Test
    public void testProperties() throws ParseException
    {
        String[] args = {"--input", "input.txt", "--output", "output.txt"};
        Properties properties = new Properties();
        properties.setProperty("input", "input.txt");
        properties.setProperty("output", "output.txt");
        Options options = new Options();
        options.addOptions(new OptionGroup("Properties"));
        options.addOption("input", true, true, "An input file", true, "input", properties, "i");
        options.addOption("output", true, true, "An output file", true, "output", properties, "o");
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, (String[]) null, properties, false);
        assertEquals("input.txt", cmd.getOptionValue("input"));
        assertEquals("output.txt", cmd.getOptionValue("output"));
    }
}