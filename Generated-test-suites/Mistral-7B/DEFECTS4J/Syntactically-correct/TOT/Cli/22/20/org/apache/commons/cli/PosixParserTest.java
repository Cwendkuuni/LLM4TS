package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test class for PosixParser.
 */
public class PosixParserTest
{
    /**
     * Test for parsing simple options.
     */
    @Test
    public void testSimpleOptions() throws ParseException
    {
        String[] args = {"test", "--foo", "--bar", "--baz=qux"};
        Options options = new Options();
        options.addOption("f", "foo", false, "A test option");
        options.addOption("b", "bar", false, "Another test option");
        options.addOption("z", "baz", true, "A test option that accepts an argument");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(args, options, true);

        Assertions.assertArrayEquals(new String[]{"test", "--foo", "--bar", "--baz=qux"}, parsedArgs);
        Assertions.assertTrue(options.isSetOption("foo"));
        Assertions.assertTrue(options.isSetOption("bar"));
        Assertions.assertEquals("qux", options.getOptionValue("baz"));
    }

    /**
     * Test for parsing long options.
     */
    @Test
    public void testLongOptions() throws ParseException
    {
        String[] args = {"test", "--foo=bar", "--long-option", "--long-option=baz"};
        Options options = new Options();
        options.addOption("f", "foo", true, "A test option that accepts an argument");
        options.addOption("lo", "long-option", true, "A test option that accepts an argument");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(args, options, true);

        Assertions.assertArrayEquals(new String[]{"test", "--foo=bar", "--long-option=baz"}, parsedArgs);
        Assertions.assertEquals("bar", options.getOptionValue("foo"));
        Assertions.assertEquals("baz", options.getOptionValue("long-option"));
    }

    /**
     * Test for parsing mixed short and long options.
     */
    @Test
    public void testMixedOptions() throws ParseException
    {
        String[] args = {"test", "-f", "--foo=bar", "-b", "--long-option=baz"};
        Options options = new Options();
        options.addOption("f", "foo", true, "A test option that accepts an argument");
        options.addOption("b", "bar", false, "Another test option");
        options.addOption("l", "long-option", true, "A test option that accepts an argument");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(args, options, true);

        Assertions.assertArrayEquals(new String[]{"test", "-f", "--foo=bar", "-b", "--long-option=baz"}, parsedArgs);
        Assertions.assertTrue(options.isSetOption("foo"));
        Assertions.assertEquals("bar", options.getOptionValue("foo"));
        Assertions.assertTrue(options.isSetOption("bar"));
        Assertions.assertEquals("baz", options.getOptionValue("long-option"));
    }

    /**
     * Test for handling invalid options.
     */
    @Test
    public void testInvalidOptions() throws ParseException
    {
        String[] args = {"test", "--invalid", "option"};
        Options options = new Options();
        options.addOption("f", "foo", false, "A test option");

        PosixParser parser = new PosixParser();

        // This should throw a ParseException.
        Assertions.assertThrows(ParseException.class, () -> parser.parse(args, options, true));

        // No options should be set.
        Assertions.assertFalse(options.isSetOption("invalid"));
    }

    /**
     * Test for handling missing required options.
     */
    @Test
    public void testMissingRequiredOptions() throws ParseException
    {
        String[] args = {"test"};
        Options options = new Options();
        options.addOption("f", "foo", true, "A test option that requires an argument");

        // This should throw a ParseException.
        Assertions.assertThrows(ParseException.class, () -> new PosixParser().parse(args, options, true));

        // No options should be set.
        Assertions.assertFalse(options.isSetOption("foo"));
    }
}