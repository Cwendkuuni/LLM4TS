package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class ParserTest
{
    @Test
    public void testSimpleParse() throws ParseException
    {
        String[] args = {"--input", "input.txt"};

        Options options = new Options();
        options.addRequiredOption("input", true, "i", "input file");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        assertNotNull(cmd);
        assertEquals("input.txt", cmd.getOptionValue("input"));
    }

    @Test
    public void testMissingOption() throws ParseException
    {
        String[] args = {"--output"};

        Options options = new Options();
        options.addRequiredOption("input", true, "i", "input file");

        CommandLineParser parser = new DefaultParser();

        assertThrows(MissingOptionException.class, () -> parser.parse(options, args));
    }

    @Test
    public void testMissingArgument() throws ParseException
    {
        String[] args = {"--output"};

        Options options = new Options();
        options.addOption("output", true, "o", "output file");

        CommandLineParser parser = new DefaultParser();

        assertThrows(MissingArgumentException.class, () -> parser.parse(options, args));
    }

    @Test
    public void testExtraArgument() throws ParseException
    {
        String[] args = {"--input", "input.txt", "--extra"};

        Options options = new Options();
        options.addRequiredOption("input", true, "i", "input file");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        assertNotNull(cmd);
        assertEquals("input.txt", cmd.getOptionValue("input"));
        assertNull(cmd.getOptionValue("extra"));
    }

    @Test
    public void testHelp() throws ParseException
    {
        String[] args = {"--help"};

        Options options = new Options();
        options.addRequiredOption("input", true, "i", "input file");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("test", options);
    }

    @Test
    public void testComplexParse() throws ParseException
    {
        String[] args = {"--input", "input.txt", "--output", "output.txt", "--boolean", "true", "--integer", "123", "--string", "Hello World"};

        Options options = new Options();
        options.addRequiredOption("input", true, "i", "input file");
        options.addRequiredOption("output", true, "o", "output file");
        options.addOption("boolean", false, null, "boolean value");
        options.addOption("integer", true, "n", "integer value");
        options.addOption("string", true, null, "string value");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        assertNotNull(cmd);
        assertEquals("input.txt", cmd.getOptionValue("input"));
        assertEquals("output.txt", cmd.getOptionValue("output"));
        assertEquals("true", cmd.getOptionValue("boolean"));
        assertEquals("123", cmd.getOptionValue("integer"));
        assertEquals("Hello World", cmd.getOptionValue("string"));
    }
}