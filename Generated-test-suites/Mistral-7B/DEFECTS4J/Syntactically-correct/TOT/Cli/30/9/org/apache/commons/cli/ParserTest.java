package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class ParserTest
{
    @Test
    public void testParseSimple() throws ParseException
    {
        String[] args = new String[]{"-a", "1", "--optionB", "2"};
        Options options = new Options();
        options.addOption("a", true, "A value");
        options.addOption("b", true, "B value");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        assertEquals(2, cmd.getArgs().size());
        assertEquals("1", cmd.getOptionValue("a"));
        assertEquals("2", cmd.getOptionValue("b"));
        assertNull(cmd.getOptionValue("nonExistentOption"));
        assertFalse(cmd.hasOption("nonExistentOption"));
    }

    @Test(expected = ParseException.class)
    public void testParseMissingRequiredOption() throws ParseException
    {
        String[] args = new String[]{"--optionA"};
        Options options = new Options();
        options.addOption("a", true, "A value").isRequired();

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        // this test should fail with a ParseException
    }

    @Test
    public void testParseMultipleRequiredOptions() throws ParseException
    {
        String[] args = new String[]{"-a", "1", "-b", "2"};
        Options options = new Options();
        options.addOption("a", true, "A value").isRequired();
        options.addOption("b", true, "B value").isRequired();

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        assertEquals(0, cmd.getArgs().size());
        assertEquals("1", cmd.getOptionValue("a"));
        assertEquals("2", cmd.getOptionValue("b"));
    }

    @Test
    public void testParseOptionalOptions() throws ParseException
    {
        String[] args = new String[]{"--optionA", "1", "--optionB", "2"};
        Options options = new Options();
        options.addOption("a", true, "A value");
        options.addOption("b", true, "B value");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        assertEquals(0, cmd.getArgs().size());
        assertEquals("1", cmd.getOptionValue("a"));
        assertEquals("2", cmd.getOptionValue("b"));
        assertNull(cmd.getOptionValue("nonExistentOption"));
        assertFalse(cmd.hasOption("nonExistentOption"));

        // checking if options are correctly parsed when not provided
        options = new Options();
        options.addOption("a", true, "A value");
        options.addOption("b", true, "B value");

        cmd = parser.parse(options, new String[0]);
        assertNull(cmd.getOptionValue("a"));
        assertNull(cmd.getOptionValue("b"));
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidOption() throws ParseException
    {
        String[] args = new String[]{"--invalidOption"};
        Options options = new Options();

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        // this test should fail with a ParseException
    }

    @Test
    public void testParseInvalidOptionWithValue() throws ParseException
    {
        String[] args = new String[]{"--invalidOption", "value"};
        Options options = new Options();

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        assertNull(cmd.getOptionValue("invalidOption"));
        // this test should fail with a ParseException
    }

    @Test
    public void testParseWithProperties() throws ParseException
    {
        String[] args = new String[]{"--optionA=valueA", "--optionB=valueB"};
        Properties properties = new Properties();
        properties.setProperty("optionA", "valueA");
        properties.setProperty("optionB", "valueB");

        Options options = new Options();
        options.addOption("a", true, "A value");
        options.addOption("b", true, "B value");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, properties, false);

        assertEquals(0, cmd.getArgs().size());
        assertEquals("valueA", cmd.getOptionValue("a"));
        assertEquals("valueB", cmd.getOptionValue("b"));
    }

    @Test(expected = ParseException.class)
    public void testParseWithPropertiesMissingOption() throws ParseException
    {
        String[] args = new String[]{"--optionA=valueA"};
        Properties properties = new Properties();
        properties.setProperty("optionB", "valueB");

        Options options = new Options();
        options.addOption("a", true, "A value");
        options.addOption("b", true, "B value");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, properties, false);

        // this test should fail with a ParseException
    }
}