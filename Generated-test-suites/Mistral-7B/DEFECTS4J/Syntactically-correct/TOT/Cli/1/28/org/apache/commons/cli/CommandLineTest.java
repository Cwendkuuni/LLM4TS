package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test suite for {@link CommandLine} class.
 */
public class CommandLineTest {

    @Test
    public void testSimpleOption() {
        String[] args = {"-a", "1"};

        try {
            CommandLine cmd = new CommandLine();
            Options options = new Options();
            options.addOption("a", true, "Simple option test");
            CommandLineParser parser = new DefaultParser();
            cmd = parser.parse(options, args);
            Assert.assertTrue(cmd.hasOption("a"));
            Assert.assertEquals("1", cmd.getOptionValue("a"));
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testMissingOption() {
        String[] args = {"someArg"};

        try {
            CommandLine cmd = new CommandLine();
            Options options = new Options();
            CommandLineParser parser = new DefaultParser();
            cmd = parser.parse(options, args);
            Assert.assertFalse(cmd.hasOption("someArg"));
            Assert.assertNull(cmd.getOptionValue("someArg"));
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testLongOption() {
        String[] args = {"--longOption", "someValue"};

        try {
            CommandLine cmd = new CommandLine();
            Options options = new Options();
            options.addOption("longOption", true, "Long option test");
            CommandLineParser parser = new DefaultParser();
            cmd = parser.parse(options, args);
            Assert.assertTrue(cmd.hasOption("longOption"));
            Assert.assertEquals("someValue", cmd.getOptionValue("longOption"));
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testMultipleOptions() {
        String[] args = {"-a", "1", "-b", "2"};

        try {
            CommandLine cmd = new CommandLine();
            Options options = new Options();
            options.addOption("a", true, "Option A");
            options.addOption("b", true, "Option B");
            CommandLineParser parser = new DefaultParser();
            cmd = parser.parse(options, args);
            Assert.assertTrue(cmd.hasOption("a"));
            Assert.assertEquals("1", cmd.getOptionValue("a"));
            Assert.assertTrue(cmd.hasOption("b"));
            Assert.assertEquals("2", cmd.getOptionValue("b"));
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testMissingValueOption() {
        String[] args = {"--missingValueOption"};

        try {
            CommandLine cmd = new CommandLine();
            Options options = new Options();
            Option option = new Option("missingValueOption", true, "Missing value option test");
            option.setRequired(false);
            options.addOption(option);
            CommandLineParser parser = new DefaultParser();
            cmd = parser.parse(options, args);
            Assert.assertTrue(cmd.hasOption("missingValueOption"));
            Assert.assertNull(cmd.getOptionValue("missingValueOption"));
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testInvalidOption() {
        String[] args = {"--invalidOption"};

        try {
            CommandLine cmd = new CommandLine();
            Options options = new Options();
            CommandLineParser parser = new DefaultParser();
            cmd = parser.parse(options, args);
            Assert.assertFalse(cmd.hasOption("invalidOption"));
        } catch (ParseException e) {
            // Expected exception
        }
    }

    @Test
    public void testUnrecognizedOption() {
        String[] args = {"--unrecognizedOption", "someValue"};

        try {
            CommandLine cmd = new CommandLine();
            Options options = new Options();
            CommandLineParser parser = new DefaultParser();
            cmd = parser.parse(options, args);
            Assert.assertFalse(cmd.hasOption("unrecognizedOption"));
            Assert.assertNull(cmd.getOptionValue("unrecognizedOption"));
            Assert.assertEquals(args.length - 1, cmd.getArgs().length);
            Assert.assertEquals(args[args.length - 1], cmd.getArgs()[0]);
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testEmptyArgs() {
        String[] args = {};

        try {
            CommandLine cmd = new CommandLine();
            Options options = new Options();
            CommandLineParser parser = new DefaultParser();
            cmd = parser.parse(options, args);
            Assert.assertTrue(cmd.getArgs().isEmpty());
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }
}