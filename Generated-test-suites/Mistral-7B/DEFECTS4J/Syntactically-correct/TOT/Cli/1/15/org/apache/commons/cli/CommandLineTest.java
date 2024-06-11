package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Test for {@link CommandLine} class.
 */
public class CommandLineTest {

    @Test
    public void testParsingSimpleCommandLine() {
        String[] args = {"-a", "1", "--b", "2"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
            Assert.assertTrue(cmd.hasOption("a"));
            Assert.assertEquals("1", cmd.getOptionValue("a"));
            Assert.assertTrue(cmd.hasOption("b"));
            Assert.assertEquals("2", cmd.getOptionValue("b"));
            Assert.assertEquals(0, cmd.getArgs().length);
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testParsingCommandLineWithUnrecognizedOption() {
        String[] args = {"-a", "1", "--c", "3", "unrecognized"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
            Assert.assertTrue(cmd.hasOption("a"));
            Assert.assertEquals("1", cmd.getOptionValue("a"));
            Assert.assertTrue(cmd.hasOption("b", false));
            Assert.assertNull(cmd.getOptionValue("c"));
            Assert.assertEquals("unrecognized", cmd.getArgs()[0]);
            Assert.assertEquals(2, cmd.getArgs().length);
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testParsingCommandLineWithMandatoryOption() {
        String[] args = {"--c", "3"};
        Options options = new Options();
        options.addOption("c", true, "Option C with mandatory argument");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
            Assert.fail("Expected ParseException: Mandatory option --c is missing");
        } catch (ParseException e) {
            Assert.assertEquals("Mandatory option --c is missing", e.getMessage());
        }
    }

    @Test
    public void testParsingCommandLineWithMandatoryOptionAndValue() {
        String[] args = {"--c", "InvalidValue"};
        Options options = new Options();
        options.addOption("c", true, "Option C with mandatory argument");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
            Assert.fail("Expected ParseException: Invalid value for option --c: InvalidValue");
        } catch (ParseException e) {
            Assert.assertEquals("Invalid value for option --c: InvalidValue", e.getMessage());
        }
    }

    @Test
    public void testParsingCommandLineWithMandatoryOptionAndValueProvided() {
        String[] args = {"--c", "ValidValue"};
        Options options = new Options();
        options.addOption("c", true, "Option C with mandatory argument");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
            Assert.assertTrue(cmd.hasOption("c"));
            Assert.assertEquals("ValidValue", cmd.getOptionValue("c"));
            Assert.assertEquals(0, cmd.getArgs().length);
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }
}