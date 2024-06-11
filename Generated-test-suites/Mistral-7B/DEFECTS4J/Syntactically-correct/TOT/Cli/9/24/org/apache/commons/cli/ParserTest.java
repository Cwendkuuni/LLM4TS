package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testSimpleParsing() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "option A");
        options.addOption("b", 1, "option B with an argument");

        CommandLineParser parser = new DefaultParser();
        String[] args = {"-a", "1", "--b", "2"};
        CommandLine cmd = parser.parse(options, args, false);

        Assert.assertEquals(true, cmd.hasOption("a"));
        Assert.assertEquals(true, cmd.getOptionValue("a"));
        Assert.assertEquals(1, cmd.getArgs().length);
        Assert.assertEquals("2", cmd.getOptionValue("b"));
    }

    @Test(expected = ParseException.class)
    public void testMissingRequiredOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "option A");
        options.addOption("b", 1, "option B with an argument");
        options.addRequiredOption("c", true, "option C");

        CommandLineParser parser = new DefaultParser();
        String[] args = {"-a", "1", "--b", "2"};

        try {
            parser.parse(options, args, false);
        } catch (MissingOptionException e) {
            throw new ParseException("Expected MissingOptionException", e);
        }

        Assert.assertFalse(options.hasOption("c"));
    }

    @Test(expected = ParseException.class)
    public void testMissingValueForOptionalOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "option A");
        options.addOption("b", 1, "option B with an argument");
        options.addOption("c", 1, "option C with an argument");

        CommandLineParser parser = new DefaultParser();
        String[] args = {"-a", "1", "--c"};

        try {
            parser.parse(options, args, false);
        } catch (MissingArgumentException e) {
            throw new ParseException("Expected MissingArgumentException", e);
        }

        Assert.assertFalse(cmd.hasOption("c"));
    }

    @Test(expected = ParseException.class)
    public void testInvalidOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "option A");
        options.addOption("b", 1, "option B with an argument");

        CommandLineParser parser = new DefaultParser();
        String[] args = {"--x", "1"};

        try {
            parser.parse(options, args, false);
        } catch (UnrecognizedOptionException e) {
            throw new ParseException("Expected UnrecognizedOptionException", e);
        }

        Assert.assertFalse(options.hasOption("x"));
    }

    @Test
    public void testParsingWithProperties() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "option A");
        options.addOption("b", 1, "option B with an argument");

        Properties properties = new Properties();
        properties.setProperty("a", "true");
        properties.setProperty("b", "2");

        CommandLineParser parser = new DefaultParser();
        String[] args = {};
        CommandLine cmd = parser.parse(options, args, properties, false);

        Assert.assertEquals(true, cmd.hasOption("a"));
        Assert.assertEquals(true, cmd.getOptionValue("a"));
        Assert.assertEquals(1, cmd.getArgs().length);
        Assert.assertEquals("2", cmd.getOptionValue("b"));
    }
}