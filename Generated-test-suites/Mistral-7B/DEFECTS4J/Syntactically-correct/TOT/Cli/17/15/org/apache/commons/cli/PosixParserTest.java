package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for PosixParser.
 */
public class PosixParserTest {

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}.
     */
    @Test
    public void testFlatten() {
        // create command line arguments
        String[] arguments = new String[]{"--verbose", "-a", "-b", "--foo=bar", "-c=one --two=three", "--", "arg1", "arg2", "--", "-d"};

        // initialize options
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C");
        options.addOption("d", true, "Option D");
        options.addOption("e", true, "Option E");
        options.addOption("f", true, "Option F", true, "value");

        // create parser and call flatten method
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        // check expected result
        Assert.assertArrayEquals(new String[]{"--verbose", "-a", "--", "--foo=bar", "-c", "--two=three", "--", "arg1", "arg2", "--", "--d"}, result);
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)} with stopAtNonOption set to true.
     */
    @Test
    public void testFlattenWithStopAtNonOption() {
        // create command line arguments
        String[] arguments = new String[]{"--verbose", "-a", "-b", "--foo=bar", "-c=one --two=three", "--", "arg1", "arg2", "--", "-d"};

        // initialize options
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C");
        options.addOption("d", true, "Option D");
        options.addOption("e", true, "Option E");
        options.addOption("f", true, "Option F", true, "value");

        // create parser and call flatten method with stopAtNonOption set to true
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);

        // check expected result
        Assert.assertArrayEquals(new String[]{"--verbose", "-a", "-b", "--foo=bar", "-c=one", "--two=three", "--", "arg1", "arg2", "--", "-d"}, result);
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)} with invalid token.
     */
    @Test(expected = ParseException.class)
    public void testFlattenWithInvalidToken() {
        // create command line arguments
        String[] arguments = new String[]{"--verbose", "-a", "-b", "--foo=bar", "-c=one --two=three", "--", "arg1", "arg2", "--", "-x"};

        // initialize options
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C");
        options.addOption("d", true, "Option D");
        options.addOption("e", true, "Option E");
        options.addOption("f", true, "Option F", true, "value");

        // create parser and call flatten method
        PosixParser parser = new PosixParser();
        parser.flatten(options, arguments, false);
    }
}