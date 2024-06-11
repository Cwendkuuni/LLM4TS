package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testSimpleParse() throws ParseException {
        Options options = new Options();
        options.addOption("o", "output", true, "output file");

        CommandLineParser parser = new DefaultParser();

        String[] args = {"-o", "output.txt"};
        CommandLine cmd = parser.parse(options, args, true);

        Assert.assertEquals("output.txt", cmd.getOptionValue("output"));
    }

    @Test
    public void testMissingOption() throws ParseException {
        Options options = new Options();
        options.addOption("o", "output", true, "output file");

        CommandLineParser parser = new DefaultParser();

        String[] args = {"--input", "input.txt"};

        try {
            parser.parse(options, args, true);
            Assert.fail("Expected ParseException");
        } catch (ParseException e) {
            Assert.assertEquals("No option '--input'", e.getMessage());
        }
    }

    @Test
    public void testMissingArgument() throws ParseException {
        Options options = new Options();
        options.addOption("o", "output", true, "output file");
        options.addOption("i", "input", true, "input file");

        CommandLineParser parser = new DefaultParser();

        String[] args = {"-o", "output.txt", "--input"};

        try {
            parser.parse(options, args, true);
            Assert.fail("Expected ParseException");
        } catch (ParseException e) {
            Assert.assertEquals("The option '--input' requires an argument.", e.getMessage());
        }
    }

    @Test
    public void testInvalidArgument() throws ParseException {
        Options options = new Options();
        options.addOption("o", "output", true, "output file");

        CommandLineParser parser = new DefaultParser();

        String[] args = {"-o", "output.txt", "--invalid"};

        try {
            parser.parse(options, args, true);
            Assert.fail("Expected ParseException");
        } catch (ParseException e) {
            Assert.assertEquals("Unrecognized option: --invalid", e.getMessage());
        }
    }

    @Test
    public void testMultipleOptions() throws ParseException {
        Options options = new Options();
        options.addOption("o", "output", true, "output file");
        options.addOption("i", "input", true, "input file");

        CommandLineParser parser = new DefaultParser();

        String[] args = {"-o", "output.txt", "--input", "input.txt"};
        CommandLine cmd = parser.parse(options, args, true);

        Assert.assertEquals("output.txt", cmd.getOptionValue("output"));
        Assert.assertEquals("input.txt", cmd.getOptionValue("input"));
    }

    @Test
    public void testProperties() throws ParseException {
        Options options = new Options();
        options.addOption("o", "output", true, "output file");

        CommandLineParser parser = new DefaultParser();

        String[] args = {"--input", "input.txt", "--output", "output.txt"};
        Properties properties = new Properties();
        properties.setProperty("output", "output_file.txt");

        CommandLine cmd = parser.parse(options, args, properties, true);

        Assert.assertEquals("output_file.txt", cmd.getOptionValue("output"));
    }

    @Test
    public void testStopAtNonOption() throws ParseException {
        Options options = new Options();
        options.addOption("o", "output", true, "output file");

        CommandLineParser parser = new DefaultParser();

        String[] args = {"--input", "input.txt", "-o", "output.txt"};
        CommandLine cmd = parser.parse(options, args, true, true);

        List<String> argsList = cmd.getArgs();
        Assert.assertEquals(1, argsList.size());
        Assert.assertEquals("input.txt", argsList.get(0));
    }

    @Test(expected = MissingOptionException.class)
    public void testMissingRequiredOption() throws ParseException {
        Options options = new Options();
        options.addOption("o", "output", true, "output file");
        options.addRequiredOption("i", "input", true, "input file");

        CommandLineParser parser = new DefaultParser();

        String[] args = {"-o", "output.txt"};
        parser.parse(options, args, true);

        Assert.fail("Expected MissingOptionException");
    }
}