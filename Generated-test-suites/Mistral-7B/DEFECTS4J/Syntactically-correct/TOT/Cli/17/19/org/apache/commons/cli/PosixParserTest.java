package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests the PosixParser class.
 */
public class PosixParserTest {

    /**
     * Test the parsing of a simple command line without any options.
     */
    @Test
    public void testSimpleCommandLine() {
        String[] args = new String[]{"MyProgram"};
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] parsedArgs;
        try {
            parsedArgs = parser.parse(options, args, false);
            Assert.assertArrayEquals(args, parsedArgs);
        } catch (ParseException e) {
            Assert.fail("Unexpected ParseException: " + e.getMessage());
        }
    }

    /**
     * Test the parsing of a command line with a single option.
     */
    @Test
    public void testSingleOption() {
        String[] args = new String[]{"--option"};
        Options options = new Options();
        options.addOption("option", true, "an option");

        PosixParser parser = new PosixParser();
        String[] parsedArgs;
        try {
            parsedArgs = parser.parse(options, args, false);
            Assert.assertArrayEquals(new String[]{"--option"}, parsedArgs);
        } catch (ParseException e) {
            Assert.fail("Unexpected ParseException: " + e.getMessage());
        }
    }

    /**
     * Test the parsing of a command line with a single option and an argument.
     */
    @Test
    public void testSingleOptionWithArgument() {
        String[] args = new String[]{"--option=value"};
        Options options = new Options();
        options.addOption("option", true, "an option");

        PosixParser parser = new PosixParser();
        String[] parsedArgs;
        try {
            parsedArgs = parser.parse(options, args, false);
            Assert.assertArrayEquals(new String[]{"--option", "value"}, parsedArgs);
        } catch (ParseException e) {
            Assert.fail("Unexpected ParseException: " + e.getMessage());
        }
    }

    /**
     * Test the parsing of a command line with multiple options.
     */
    @Test
    public void testMultipleOptions() {
        String[] args = new String[]{"--option1", "--option2"};
        Options options = new Options();
        options.addOption("option1", true, "Option 1");
        options.addOption("option2", true, "Option 2");

        PosixParser parser = new PosixParser();
        String[] parsedArgs;
        try {
            parsedArgs = parser.parse(options, args, false);
            Assert.assertArrayEquals(new String[]{"--option1", "--option2"}, parsedArgs);
        } catch (ParseException e) {
            Assert.fail("Unexpected ParseException: " + e.getMessage());
        }
    }

    /**
     * Test the parsing of a command line with multiple options and arguments.
     */
    @Test
    public void testMultipleOptionsWithArguments() {
        String[] args = new String[]{"--option1=value1", "--option2=value2"};
        Options options = new Options();
        options.addOption("option1", true, "Option 1");
        options.addOption("option2", true, "Option 2");

        PosixParser parser = new PosixParser();
        String[] parsedArgs;
        try {
            parsedArgs = parser.parse(options, args, false);
            Assert.assertArrayEquals(new String[]{"--option1", "value1", "--option2", "value2"}, parsedArgs);
        } catch (ParseException e) {
            Assert.fail("Unexpected ParseException: " + e.getMessage());
        }
    }

    /**
     * Test the parsing of a command line with long options.
     */
    @Test
    public void testLongOptions() {
        String[] args = new String[]{"--long-option1", "--long-option2=value"};
        Options options = new Options();
        options.addOption("longOption1", true, "Long Option 1");
        options.addOption("longOption2", true, "Long Option 2");

        PosixParser parser = new PosixParser();
        String[] parsedArgs;
        try {
            parsedArgs = parser.parse(options, args, false);
            Assert.assertArrayEquals(new String[]{"--long-option1", "--long-option2=value"}, parsedArgs);
        } catch (ParseException e) {
            Assert.fail("Unexpected ParseException: " + e.getMessage());
        }
    }

    /**
     * Test the parsing of a command line with a single hyphen.
     */
    @Test
    public void testSingleHyphen() {
        String[] args = new String[]{"-"};
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] parsedArgs;
        try {
            parsedArgs = parser.parse(options, args, false);
            Assert.assertEquals("", parsedArgs[0]);
        } catch (ParseException e) {
            Assert.fail("Unexpected ParseException: " + e.getMessage());
        }
    }

    /**
     * Test the parsing of a command line with two hyphens.
     */
    @Test
    public void testDoubleHyphen() {
        String[] args = new String[]{"--"};
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] parsedArgs;
        try {
            parsedArgs = parser.parse(options, args, false);
            Assert.assertEquals("", parsedArgs[0]);
        } catch (ParseException e) {
            Assert.fail("Unexpected ParseException: " + e.getMessage());
        }
    }

    /**
     * Test the parsing of a command line with a stop-at-non-option flag.
     */
    @Test
    public void testStopAtNonOption() {
        String[] args = new String[]{"--option", "non-option"};
        Options options = new Options();
        options.addOption("option", true, "an option");

        PosixParser parser = new PosixParser();
        String[] parsedArgs;
        try {
            parsedArgs = parser.parse(options, args, true);
            Assert.assertArrayEquals(new String[]{"--option"}, parsedArgs);
        } catch (ParseException e) {
            Assert.fail("Unexpected ParseException: " + e.getMessage());
        }
    }
}