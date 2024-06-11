package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for {@link Parser} class.
 */
public class ParserTest {

    @Test
    public void testSimpleOption() throws ParseException {
        String[] args = {"--input", "input.txt"};
        Options options = new Options();
        options.addRequiredOption("input", true, "i")
                .desc("Input file");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        Assert.assertEquals("input.txt", cmd.getOptionValue("input"));
    }

    @Test
    public void testOptionalOption() throws ParseException {
        String[] args = {"--output", "output.txt", "--help"};
        Options options = new Options();
        options.addOption("output", true, "o")
                .desc("Output file")
                .required(false);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        Assert.assertEquals("output.txt", cmd.getOptionValue("output"));
        Assert.assertTrue(cmd.hasOption("help"));
    }

    @Test(expected = ParseException.class)
    public void testMissingOption() throws ParseException {
        String[] args = {"--no-such-option"};
        Options options = new Options();

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);
    }

    @Test(expected = ParseException.class)
    public void testMissingRequiredOption() throws ParseException {
        String[] args = {};
        Options options = new Options();
        options.addRequiredOption("input", true, "i")
                .desc("Input file");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);
    }

    @Test
    public void testLongOption() throws ParseException {
        String[] args = {"--long-option"};
        Options options = new Options();
        options.addOption("longOption", true, null)
                .desc("Long Option");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        Assert.assertTrue(cmd.hasOption("longOption"));
    }

    @Test
    public void testShortOption() throws ParseException {
        String[] args = {"-i", "input.txt"};
        Options options = new Options();
        options.addOption("i", true, "i")
                .desc("Input file");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        Assert.assertEquals("input.txt", cmd.getOptionValue("input"));
    }

    @Test
    public void testShortOptionWithValue() throws ParseException {
        String[] args = {"-o", "output.txt"};
        Options options = new Options();
        options.addOption("o", true, "o")
                .desc("Output file");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        Assert.assertEquals("output.txt", cmd.getOptionValue("output"));
    }

    @Test
    public void testShortOptionWithLongOption() throws ParseException {
        String[] args = {"-i", "input.txt", "--input-file", "file.txt"};
        Options options = new Options();
        options.addOption("i", true, "i")
                .desc("Input file")
                .aliases("input-file");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        Assert.assertEquals("input.txt", cmd.getOptionValue("input"));
        Assert.assertEquals("file.txt", cmd.getOptionValue("input-file"));
    }

    @Test
    public void testMultiValueOption() throws ParseException {
        String[] args = {"--values", "val1", "val2"};
        Options options = new Options();
        options.addOption("values", true, null)
                .desc("Values")
                .valueSeparator(',');

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        Assert.assertArrayEquals(new String[]{"val1", "val2"}, cmd.getValues("values").toArray(new String[0]));
    }

    @Test
    public void testNullOption() throws ParseException {
        String[] args = {"--null-option", "null"};
        Options options = new Options();
        options.addOption("null-option", null)
                .desc("Null Option");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        Assert.assertNull(cmd.getOptionValue("null-option"));
    }

    @Test
    public void testMixedOptions() throws ParseException {
        String[] args = {"--input", "input.txt", "-o", "output.txt", "--values", "val1,val2"};
        Options options = new Options();
        options.addOption("input", true, "i")
                .desc("Input file");
        options.addOption("o", true, "o")
                .desc("Output file");
        options.addOption("values", true, null)
                .desc("Values")
                .valueSeparator(',');

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        Assert.assertEquals("input.txt", cmd.getOptionValue("input"));
        Assert.assertEquals("output.txt", cmd.getOptionValue("o"));
        Assert.assertArrayEquals(new String[]{"val1", "val2"}, cmd.getValues("values").toArray(new String[0]));
    }

    @Test
    public void testHelp() throws ParseException {
        String[] args = {"--help"};
        Options options = new Options();

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, true);

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("parser", options);
    }
}