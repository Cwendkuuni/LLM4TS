package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParse() throws ParseException {
        String[] args = {"--input", "input.txt", "--output", "output.txt", "-o", "out", "--help"};
        Parser parser = new DefaultParser();

        CommandLine commandLine = parser.parse(new Options(), args, null, false);

        Assert.assertEquals(5, commandLine.getArgs().size());
        Assert.assertEquals("input.txt", commandLine.getOptionValue("input"));
        Assert.assertEquals("output.txt", commandLine.getOptionValue("output"));
        Assert.assertEquals("out", commandLine.getOptionValue("o"));
        Assert.assertNull(commandLine.getOptionValue("nonexistent"));
        Assert.assertFalse(commandLine.hasOption("nonexistent"));
        Assert.assertTrue(commandLine.hasOption("input"));
        Assert.assertTrue(commandLine.hasOption("output"));
        Assert.assertTrue(commandLine.hasOption("o"));
        Assert.assertFalse(commandLine.hasOption("help"));
        Assert.assertFalse(commandLine.hasOption("help", true));
        Assert.assertFalse(commandLine.hasOptions());
    }

    @Test(expected = MissingArgumentException.class)
    public void testMissingArgument() throws ParseException {
        String[] args = {"--input", "input.txt", "--output"};
        Parser parser = new DefaultParser();

        parser.parse(new Options(), args, null, false);
    }

    @Test(expected = MissingOptionException.class)
    public void testMissingRequiredOption() throws ParseException {
        String[] args = {"--input", "input.txt"};
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addRequiredOption("output", true, "o", "Output file path");

        parser.parse(options, args, null, false);
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        String[] args = {"--input", "input.txt", "--output", "output.txt"};
        Parser parser = new DefaultParser();

        Properties properties = new Properties();
        properties.setProperty("output", "output2.txt");

        CommandLine commandLine = parser.parse(new Options(), args, properties, false);

        Assert.assertEquals(3, commandLine.getArgs().size());
        Assert.assertEquals("input.txt", commandLine.getOptionValue("input"));
        Assert.assertEquals("output2.txt", commandLine.getOptionValue("output"));
        Assert.assertEquals(null, commandLine.getOptionValue("o"));
        Assert.assertNull(commandLine.getOptionValue("nonexistent"));
        Assert.assertFalse(commandLine.hasOption("nonexistent"));
        Assert.assertTrue(commandLine.hasOption("input"));
        Assert.assertTrue(commandLine.hasOption("output"));
        Assert.assertTrue(commandLine.hasOption("o"));
        Assert.assertFalse(commandLine.hasOption("help"));
        Assert.assertFalse(commandLine.hasOption("help", true));
        Assert.assertFalse(commandLine.hasOptions());
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidOption() throws ParseException {
        String[] args = {"--invalid", "invalid.txt"};
        Parser parser = new DefaultParser();

        parser.parse(new Options(), args, null, false);
    }

    @Test
    public void testParseWithLongOptionName() throws ParseException {
        String[] args = {"--very-long-option-name", "value"};
        Parser parser = new DefaultParser();

        Options options = new Options();
        Option option = new Option("very-long-option-name", true, null, "value");
        options.addOption(option);

        CommandLine commandLine = parser.parse(options, args, null, false);

        Assert.assertEquals(1, commandLine.getArgs().size());
        Assert.assertEquals("value", commandLine.getOptionValue("very-long-option-name"));
        Assert.assertTrue(commandLine.hasOption("very-long-option-name"));
        Assert.assertFalse(commandLine.hasOption("help"));
        Assert.assertFalse(commandLine.hasOptions());
    }
}