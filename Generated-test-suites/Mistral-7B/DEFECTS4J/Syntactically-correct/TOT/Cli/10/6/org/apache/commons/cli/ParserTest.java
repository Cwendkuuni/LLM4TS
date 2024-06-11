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

    /**
     * Test parsing command line arguments with valid options and values.
     */
    @Test
    public void testParseValidOptions() throws ParseException {
        Options options = new Options();
        options.addRequiredOption("a", "apple", true, "an apple");
        options.addOption("b", "banana", false, "a banana");

        CommandLineParser parser = new DefaultParser();
        String[] args = {"-a", "testApple", "--banana", "testBanana"};
        CommandLine cmd = parser.parse(options, args);

        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertEquals("testApple", cmd.getOptionValue("a"));
        Assert.assertFalse(cmd.hasOption("b"));
        Assert.assertNull(cmd.getOptionValue("b"));

        options.getOption("a").setRequired(false);

        cmd = parser.parse(options, args);

        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertEquals("testApple", cmd.getOptionValue("a"));
        Assert.assertTrue(cmd.hasOption("b"));
        Assert.assertEquals("testBanana", cmd.getOptionValue("b"));
    }

    /**
     * Test parsing command line arguments with valid options and no values.
     */
    @Test
    public void testParseValidOptionsNoValues() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "an apple");
        options.addOption("b", false, "a banana");

        CommandLineParser parser = new DefaultParser();
        String[] args = {"-a", "--b"};
        CommandLine cmd = parser.parse(options, args);

        Assert.assertTrue(cmd.hasOption("a"));

        Assert.assertFalse(cmd.hasOption("b"));
    }

    /**
     * Test parsing command line arguments with invalid options.
     */
    @Test(expected = UnrecognizedOptionException.class)
    public void testParseInvalidOptions() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "an apple");

        CommandLineParser parser = new DefaultParser();
        String[] args = {"-c", "test"};
        CommandLine cmd = parser.parse(options, args);
    }

    /**
     * Test parsing command line arguments with missing required options.
     */
    @Test(expected = MissingOptionException.class)
    public void testParseMissingRequiredOptions() throws ParseException {
        Options options = new Options();
        options.addRequiredOption("a", true, "an apple");

        CommandLineParser parser = new DefaultParser();
        String[] args = {};
        CommandLine cmd = parser.parse(options, args);
    }

    /**
     * Test parsing command line arguments with conflicting options.
     */
    @Test(expected = ParseException.class)
    public void testParseConflictingOptions() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "an apple");
        options.addOption("a", true, "an apple2");

        CommandLineParser parser = new DefaultParser();
        String[] args = {"-a"};
        parser.parse(options, args);
    }

    /**
     * Test parsing command line arguments with long options.
     */
    @Test
    public void testParseLongOptions() throws ParseException {
        Options options = new Options();
        options.addLongOption("longOptionA", true, "long option A");
        options.addLongOption("longOptionB", false, "long option B");

        CommandLineParser parser = new DefaultParser();
        String[] args = {"--longOptionA", "--longOptionB"};
        CommandLine cmd = parser.parse(options, args);

        Assert.assertTrue(cmd.hasOption("longOptionA"));
        Assert.assertTrue(cmd.hasOption("longOptionB"));
    }

    /**
     * Test parsing command line arguments with options and arguments.
     */
    @Test
    public void testParseOptionsWithArguments() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "an apple");
        options.addOption("b", true, "a banana");
        options.addOption("c", true, "a color");

        CommandLineParser parser = new DefaultParser();
        String[] args = {"-a", "apple1", "-b", "banana1", "--c", "red"};
        CommandLine cmd = parser.parse(options, args);

        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertEquals("apple1", cmd.getOptionValue("a"));
        Assert.assertTrue(cmd.hasOption("b"));
        Assert.assertEquals("banana1", cmd.getOptionValue("b"));
        Assert.assertTrue(cmd.hasOption("c"));
        Assert.assertEquals("red", cmd.getOptionValue("c"));
    }

    /**
     * Test parsing command line arguments with options that have default values.
     */
    @Test
    public void testParseOptionsWithDefaultValues() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "an apple")
                .setValueByDefault("defaultApple");
        options.addOption("b", true, "a banana")
                .setValueByDefault("defaultBanana");

        CommandLineParser parser = new DefaultParser();
        String[] args = {"-a", "apple1"};
        CommandLine cmd = parser.parse(options, args);

        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertEquals("apple1", cmd.getOptionValue("a"));
        Assert.assertEquals("defaultBanana", cmd.getOptionValue("b"));
    }

    /**
     * Test parsing command line arguments with options that have optional arguments.
     */
    @Test
    public void testParseOptionsWithOptionalArguments() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "an apple")
                .hasArg(true)
                .setValueByDefault("defaultApple");

        CommandLineParser parser = new DefaultParser();
        String[] args = {"-a", "apple1", "-a", "apple2"};
        CommandLine cmd = parser.parse(options, args);

        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertEquals("apple1", cmd.getOptionValue("a", "defaultApple"));
        Assert.assertEquals("apple2", cmd.getOptionValue("a", "defaultApple"));
    }

    /**
     * Test parsing command line arguments with missing arguments for optional options.
     */
    @Test
    public void testParseMissingArgumentsForOptionalOptions() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "an apple")
                .hasArg(true);

        CommandLineParser parser = new DefaultParser();
        String[] args = {"-a"};
        CommandLine cmd = parser.parse(options, args);

        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertNull(cmd.getOptionValue("a"));
    }

    /**
     * Test parsing command line arguments with long options that have optional arguments.
     */
    @Test
    public void testParseLongOptionsWithOptionalArguments() throws ParseException {
        Options options = new Options();
        options.addLongOption("longOptionA", true, "long option A")
                .hasArg(true)
                .setValueByDefault("defaultValue");

        CommandLineParser parser = new DefaultParser();
        String[] args = {"--longOptionA", "longValue1", "--longOptionA", "longValue2"};
        CommandLine cmd = parser.parse(options, args);

        Assert.assertTrue(cmd.hasOption("longOptionA"));
        Assert.assertEquals("longValue1", cmd.getOptionValue("longOptionA"));
        Assert.assertEquals("longValue2", cmd.getOptionValue("longOptionA"));
    }
}