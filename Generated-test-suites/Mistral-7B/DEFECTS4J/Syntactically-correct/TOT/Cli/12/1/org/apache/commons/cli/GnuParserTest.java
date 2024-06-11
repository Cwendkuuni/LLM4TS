package org.apache.commons.cli.test;

import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Test cases for {@link GnuParser#flatten(Options, String[], boolean)} method.
 */
public class GnuParserTest {

    /**
     * Test flatten method with valid arguments.
     */
    @Test
    public void testFlattenValidArguments() {
        String[] args = {"-Dprop1=val1", "-Dprop2", "--", "-opt", "arg1", "-opt2=arg2", "arg3"};
        Options options = new Options();
        String[] expected = {"-D", "prop1", "val1", "-D", "prop2", "--", "-opt", "arg1", "-opt", "arg2"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, args, false);

        assertArrayEquals(expected, result);
    }

    /**
     * Test flatten method with stopAtNonOption=true.
     */
    @Test
    public void testFlattenStopAtNonOption() {
        String[] args = {"-Dprop1=val1", "-Dprop2", "--", "nonOption", "arg1", "-opt2=arg2", "arg3"};
        Options options = new Options();
        String[] expected = {"-D", "prop1", "val1", "-D", "prop2", "--", "nonOption"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, args, true);

        assertArrayEquals(expected, result);
    }

    /**
     * Test flatten method with empty arguments.
     */
    @Test
    public void testFlattenEmptyArguments() {
        String[] args = {};
        Options options = new Options();
        String[] expected = {};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, args, false);

        assertArrayEquals(expected, result);
    }

    /**
     * Test flatten method with malformed arguments.
     */
    @Test(expected = ParseException.class)
    public void testFlattenMalformedArguments() {
        String[] args = {"-Dprop1=val1", "--", "-opt=arg2", "arg3"}; // Missing hyphen for --
        Options options = new Options();

        GnuParser parser = new GnuParser();
        parser.flatten(options, args, false);
    }

    /**
     * Test flatten method with undefined options.
     */
    @Test(expected = ParseException.class)
    public void testFlattenUndefinedOptions() {
        String[] args = {"-nonOption", "arg1", "-opt", "arg2", "arg3"};
        Options options = new Options();

        GnuParser parser = new GnuParser();
        parser.flatten(options, args, false);
    }

    /**
     * Test flatten method with long options.
     */
    @Test
    public void testFlattenLongOptions() {
        String[] args = {"--longOption1", "--longOption2=val1", "--", "-opt", "arg1", "-opt2=arg2", "arg3"};
        Options options = new Options();
        options.addOption("longOption1", false, false, "Long option 1");
        options.addOption("longOption2", true, "value", "Long option 2");

        GnuParser parser = new GnuParser();
        String[] expected = {"--", "--longOption1", "--longOption2", "-opt", "arg1", "-opt", "arg2"};
        String[] result = parser.flatten(options, args, false);

        assertArrayEquals(expected, result);
    }
}