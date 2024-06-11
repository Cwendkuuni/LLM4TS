package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class CommandLineTest {

    @Test
    public void testBasicUsage() throws ParseException {
        String[] args = new String[]{"-a", "1", "-b", "2"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        CommandLine cmd = new CommandLineParser().parse(options, args);

        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertTrue(cmd.hasOption("b"));
        Assert.assertEquals("1", cmd.getOptionValue("a"));
        Assert.assertEquals("2", cmd.getOptionValue("b"));
        Assert.assertNull(cmd.getOptionValue("c"));
    }

    @Test
    public void testMissingOption() throws ParseException {
        String[] args = new String[]{"-x", "3"};
        Options options = new Options();
        options.addOption("a", true, "Option A");

        try {
            new CommandLineParser().parse(options, args);
            Assert.fail();
        } catch (ParseException e) {
            Assert.assertEquals("-x is not recognized as an internal or external command, operable program or batch file.", e.getMessage());
        }
    }

    @Test
    public void testLongOption() throws ParseException {
        String[] args = new String[]{"--longOption", "value"};
        Options options = new Options();
        options.addOption("longOption", true, "Long option");

        CommandLine cmd = new CommandLineParser().parse(options, args);

        Assert.assertTrue(cmd.hasOption("longOption"));
        Assert.assertEquals("value", cmd.getOptionValue("longOption"));
    }

    @Test
    public void testMultipleValuesForOption() throws ParseException {
        String[] args = new String[]{"-a", "1", "--optionA", "value1", "value2"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("optionA", true, "Option with multiple values");

        CommandLine cmd = new CommandLineParser().parse(options, args);

        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertTrue(cmd.hasOption("optionA"));
        Assert.assertEquals(Arrays.asList("value1", "value2"), cmd.getOptionValues("optionA"));
    }

    @Test
    public void testEmptyOption() throws ParseException {
        String[] args = new String[]{"--emptyOption"};
        Options options = new Options();
        options.addOption("emptyOption", true, "Empty option");

        CommandLine cmd = new CommandLineParser().parse(options, args);

        Assert.assertTrue(cmd.hasOption("emptyOption"));
        Assert.assertNull(cmd.getOptionValue("emptyOption"));
    }

    @Test
    public void testUnrecognizedOption() throws ParseException {
        String[] args = new String[]{"--unrecognizedOption"};
        Options options = new Options();
        options.addOption("optionA", true, "Option A");

        CommandLine cmd = new CommandLineParser().parse(options, args);

        Assert.assertFalse(cmd.hasOption("unrecognizedOption"));
    }

    @Test
    public void testNullOptionValue() throws ParseException {
        String[] args = new String[]{"-a", null};
        Options options = new Options();
        options.addOption("a", true, "Option A");

        try {
            new CommandLineParser().parse(options, args);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Option 'a' must have a value.", e.getMessage());
        }
    }

    @Test
    public void testOptionValueOfWrongType() throws ParseException {
        String[] args = new String[]{"-a", "true"};
        Options options = new Options();
        options.addOption("a", Integer.class, "Option A");

        try {
            new CommandLineParser().parse(options, args);
            Assert.fail();
        } catch (ParseException e) {
            Assert.assertEquals("The value for option 'a' is not an instance of java.lang.Integer.", e.getMessage());
        }
    }

    @Test
    public void testParseCommandLineWithArguments() throws ParseException {
        String[] args = new String[]{"--input", "input.txt", "--output", "output.txt"};
        Options options = new Options();
        options.addOption("input", true, "input file");
        options.addOption("output", true, "output file");

        CommandLine cmd = new CommandLineParser().parse(options, args);

        Assert.assertEquals("input.txt", cmd.getOptionValue("input"));
        Assert.assertEquals("output.txt", cmd.getOptionValue("output"));
        Assert.assertNull(cmd.getOptionValue("optionA"));
    }
}