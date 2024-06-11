package org.apache.commons.cli.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Test;

/**
 * Test for Parser class
 */
public class ParserTest {

    /**
     * Test parse method with valid options and arguments
     */
    @Test
    public void testParseValid() {
        // create command line options
        Options options = new Options();
        options.addRequiredOption("o", "option", true, "an option");
        options.addRequiredOption("a", "argument", true, "an argument");

        // create command line parser
        CommandLineParser parser = new DefaultParser();

        // create command line arguments
        String[] args = {"-o", "value1", "-a", "value2"};

        // parse command line arguments
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args, true);
        } catch (ParseException e) {
            // should not throw any exception
            assertFalse(e.getMessage().length() > 0);
        }

        // check command line options
        assertSame(args[0], cmd.getOptionValue("o"));
        assertSame(args[1], cmd.getOptionValue("a"));
        assertNull(cmd.getOptionValue("unknownOption"));
        assertTrue(cmd.getArgs().length == 0);
    }

    /**
     * Test parse method with valid options and arguments with stopAtNonOption=false
     */
    @Test
    public void testParseValidWithNonOptions() {
        // create command line options
        Options options = new Options();
        options.addRequiredOption("o", "option", true, "an option");
        options.addRequiredOption("a", "argument", true, "an argument");

        // create command line parser
        CommandLineParser parser = new DefaultParser();

        // create command line arguments
        String[] args = {"-o", "value1", "--", "value3", "value4"};

        // parse command line arguments
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args, false);
        } catch (ParseException e) {
            // should not throw any exception
            assertFalse(e.getMessage().length() > 0);
        }

        // check command line options
        assertSame(args[0], cmd.getOptionValue("o"));
        assertSame(args[1], cmd.getOptionValue("a"));
        assertSame(args[args.length - 2], cmd.getOptionValue("--"));
        assertSame(args[args.length - 1], cmd.getOptionValue("value3"));
        assertSame(args[args.length - 2], cmd.getOptionValue("--"));
        assertNull(cmd.getOptionValue("unknownOption"));
        assertTrue(cmd.getArgs().length == 2);
        assertSame("value4", cmd.getArgs()[0]);
        assertSame("value3", cmd.getArgs()[1]);
    }

    /**
     * Test parse method with valid options and properties
     */
    @Test
    public void testParseWithProperties() {
        // create command line options
        Options options = new Options();
        options.addRequiredOption("o", "option", true, "an option");
        options.addRequiredOption("a", "argument", true, "an argument");

        // create command line parser
        CommandLineParser parser = new DefaultParser();

        // create command line properties
        Properties properties = new Properties();
        properties.setProperty("o", "value1");
        properties.setProperty("a", "value2");

        // create command line arguments
        String[] args = {"--"};

        // parse command line arguments and properties
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args, properties, false);
        } catch (ParseException e) {
            // should not throw any exception
            assertFalse(e.getMessage().length() > 0);
        }

        // check command line options
        assertSame(properties.getProperty("o"), cmd.getOptionValue("o"));
        assertSame(properties.getProperty("a"), cmd.getOptionValue("a"));
        assertNull(cmd.getOptionValue("unknownOption"));
        assertTrue(cmd.getArgs().length == 0);
    }

    /**
     * Test parse method with missing required options
     */
    @Test
    public void testParseMissingRequiredOptions() {
        // create command line options
        Options options = new Options();
        options.addRequiredOption("o", "option", true, "an option");
        options.addRequiredOption("a", "argument", true, "an argument");

        // create command line parser
        CommandLineParser parser = new DefaultParser();

        // create command line arguments
        String[] args = {"--"};

        // parse command line arguments
        assertThrows(MissingOptionException.class, () -> {
            parser.parse(options, args, true);
        });
    }

    /**
     * Test parse method with missing required options and stopAtNonOption=true
     */
    @Test
    public void testParseMissingRequiredOptionsWithStopAtNonOption() {
        // create command line options
        Options options = new Options();
        options.addRequiredOption("o", "option", true, "an option");
        options.addRequiredOption("a", "argument", true, "an argument");

        // create command line parser
        CommandLineParser parser = new DefaultParser();

        // create command line arguments
        String[] args = {"--", "value1"};

        // parse command line arguments
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args, true);
        } catch (ParseException e) {
            // should not throw any exception
            assertFalse(e.getMessage().length() > 0);
        }

        // check command line options
        assertNull(cmd.getOptionValue("o"));
        assertNull(cmd.getOptionValue("a"));
        assertNull(cmd.getOptionValue("unknownOption"));
        assertTrue(cmd.getArgs().length == 2);
        assertSame("value1", cmd.getArgs()[0]);
        assertSame("value1", cmd.getArgs()[1]);
    }

    /**
     * Test parse method with missing required options and properties
     */
    @Test
    public void testParseMissingRequiredOptionsWithProperties() {
        // create command line options
        Options options = new Options();
        options.addRequiredOption("o", "option", true, "an option");
        options.addRequiredOption("a", "argument", true, "an argument");

        // create command line parser
        CommandLineParser parser = new DefaultParser();

        // create command line properties
        Properties properties = new Properties();

        // parse command line arguments and properties
        assertThrows(MissingOptionException.class, () -> {
            parser.parse(options, new String[0], properties, true);
        });
    }

    /**
     * Test parse method with missing argument
     */
    @Test
    public void testParseMissingArgument() {
        // create command line options
        Options options = new Options();
        options.addRequiredOption("o", "option", true, "an option");
        options.addOption("a", "argument", false, "an optional argument");

        // create command line parser
        CommandLineParser parser = new DefaultParser();

        // create command line arguments
        String[] args = {"-o", "value1"};

        // parse command line arguments
        assertThrows(MissingArgumentException.class, () -> {
            parser.parse(options, args, true);
        });
    }

    /**
     * Test parse method with unrecognized option
     */
    @Test
    public void testParseUnrecognizedOption() {
        // create command line options
        Options options = new Options();
        options.addOption("o", "option", false, "an option");

        // create command line parser
        CommandLineParser parser = new DefaultParser();

        // create command line arguments
        String[] args = {"--unknown", "value1"};

        // parse command line arguments
        assertThrows(UnrecognizedOptionException.class, () -> {
            parser.parse(options, args, true);
        });
    }
}