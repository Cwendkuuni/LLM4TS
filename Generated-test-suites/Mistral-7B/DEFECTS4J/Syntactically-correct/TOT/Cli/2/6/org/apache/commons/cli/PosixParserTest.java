package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * Test cases for PosixParser class.
 */
public class PosixParserTest {

    /**
     * Test flattening of basic command line options.
     */
    @Test
    public void testBasicFlattening() {
        String[] args = {"--foo", "-a", "-b", "--bar=test", "-c", "--", "-d"};
        Options options = new Options();
        options.addOption("foo", true, "Foo option");
        options.addOption("bar", true, "Bar option");

        PosixParser parser = new PosixParser();
        String[] flattened = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--foo", "-a", "-b", "--bar=test", "-c", "--", "-d"}, flattened);
    }

    /**
     * Test handling of short options with arguments.
     */
    @Test
    public void testShortOptionWithArgument() {
        String[] args = {"--foo", "-a", "--bar=test", "-c", "-d", "--", "-e=-f"};
        Options options = new Options();
        options.addOption("foo", true, "Foo option");
        options.addOption("bar", true, "Bar option", true, "BAR_ARG");

        PosixParser parser = new PosixParser();
        String[] flattened = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--foo", "-a", "--bar=test", "-c", "--", "-e=-f"}, flattened);

        // Check parsed options
        Assert.assertEquals("test", parser.parse(args).getOptionValue("bar"));
        Assert.assertEquals("f", parser.parse(args).getOptionValue("e"));
    }

    /**
     * Test handling of long options with arguments.
     */
    @Test
    public void testLongOptionWithArgument() {
        String[] args = {"--foo", "--bar=test", "-c", "--", "--baz=-qux"};
        Options options = new Options();
        options.addOption("foo", true, "Foo option");
        options.addOption("bar", true, "Bar option", true, "BAR_ARG");
        options.addOption("baz", true, "Baz option", true, "BAZ_ARG");

        PosixParser parser = new PosixParser();
        String[] flattened = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--foo", "--bar=test", "--baz=-qux"}, flattened);

        // Check parsed options
        Assert.assertEquals("test", parser.parse(args).getOptionValue("bar"));
        Assert.assertEquals("qux", parser.parse(args).getOptionValue("baz"));
    }

    /**
     * Test handling of short options without arguments.
     */
    @Test
    public void testShortOptionWithoutArgument() {
        String[] args = {"--foo", "-a", "-b", "--", "-c"};
        Options options = new Options();
        options.addOption("foo", true, "Foo option");
        options.addOption("c", false, "C option");

        PosixParser parser = new PosixParser();
        String[] flattened = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--foo", "-a", "-b", "--c"}, flattened);

        // Check parsed options
        Assert.assertTrue(parser.parse(args).hasOption("c"));
    }

    /**
     * Test handling of long options without arguments.
     */
    @Test
    public void testLongOptionWithoutArgument() {
        String[] args = {"--foo", "--bar", "--", "--baz"};
        Options options = new Options();
        options.addOption("foo", true, "Foo option");
        options.addOption("bar", false, "Bar option");
        options.addOption("baz", false, "Baz option");

        PosixParser parser = new PosixParser();
        String[] flattened = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--foo", "--bar", "--baz"}, flattened);

        // Check parsed options
        Assert.assertTrue(parser.parse(args).hasOption("baz"));
    }

    /**
     * Test handling of non-option arguments.
     */
    @Test
    public void testNonOptionArguments() {
        String[] args = {"--foo", "arg1", "arg2", "--", "arg3"};
        Options options = new Options();
        options.addOption("foo", true, "Foo option");

        PosixParser parser = new PosixParser();
        String[] flattened = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--foo", "arg1", "arg2", "arg3"}, flattened);

        // Check parsed options
        Assert.assertNull(parser.parse(args).getOptionValue("foo"));
    }

    /**
     * Test handling of options with conflicting long names.
     */
    @Test(expected = ParseException.class)
    public void testConflictingLongNames() {
        String[] args = {"--foo", "--bar", "--"};
        Options options = new Options();
        options.addOption("foo", true, "Foo option");
        options.addOption("bar", true, "Bar option");

        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    /**
     * Test handling of options with conflicting short names.
     */
    @Test(expected = ParseException.class)
    public void testConflictingShortNames() {
        String[] args = {"-a", "-b", "--"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    /**
     * Test handling of arguments following an option with an argument.
     */
    @Test
    public void testArgumentsFollowingOptionWithArgument() {
        String[] args = {"--foo=arg1", "arg2"};
        Options options = new Options();
        options.addOption("foo", true, "Foo option", true, "ARG");

        PosixParser parser = new PosixParser();
        String[] flattened = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--foo=arg1", "arg2"}, flattened);

        // Check parsed options
        Assert.assertEquals("arg1", parser.parse(args).getOptionValue("foo"));
        Assert.assertEquals("arg2", parser.parse(args).getArgs()[0]);
    }

    /**
     * Test handling of multiple options with the same long name.
     */
    @Test
    public void testMultipleOptionsWithSameLongName() {
        String[] args = {"--foo", "--foo"};
        Options options = new Options();
        options.addOption("foo", true, "Foo option");

        PosixParser parser = new PosixParser();
        String[] flattened = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"--foo", "--foo"}, flattened);

        // Check parsed options
        Assert.assertEquals(2, parser.parse(args).getOptions().size());
    }
}