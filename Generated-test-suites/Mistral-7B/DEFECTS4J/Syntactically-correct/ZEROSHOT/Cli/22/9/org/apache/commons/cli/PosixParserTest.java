package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Test class for PosixParser.
 */
public class PosixParserTest
{
    private Options options;

    @Before
    public void setUp()
    {
        options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B with argument");
        options.addOption("c", 3, "Option C with argument");
        options.addOption("d", false, "Option D");
    }

    /**
     * Test for empty arguments array.
     */
    @Test
    public void testEmpty()
    {
        String[] arguments = {};
        String[] expected = {};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, result);
    }

    /**
     * Test for simple options.
     */
    @Test
    public void testSimple()
    {
        String[] arguments = {"-a", "--d"};
        String[] expected = {"-a", "--d"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, result);
    }

    /**
     * Test for options with arguments.
     */
    @Test
    public void testOptionsWithArguments()
    {
        String[] arguments = {"-b", "foo", "--c", "bar"};
        String[] expected = {"-b", "--c", "foo", "bar"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, result);
    }

    /**
     * Test for long options.
     */
    @Test
    public void testLongOptions()
    {
        String[] arguments = {"--option-a", "--option-b=value"};
        String[] expected = {"--option-a", "--option-b=value"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, result);
    }

    /**
     * Test for options with arguments and other arguments.
     */
    @Test
    public void testMixed()
    {
        String[] arguments = {"-a", "--option-b=value", "-c", "arg"};
        String[] expected = {"-a", "--option-b=value", "-c", "arg"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, result);
    }

    /**
     * Test for options with arguments and other arguments, stopping at the
     * first non-option.
     */
    @Test
    public void testStopAtNonOption()
    {
        String[] arguments = {"-a", "--option-b=value", "arg"};
        String[] expected = {"-a", "--option-b=value"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);

        Assert.assertArrayEquals(expected, result);
    }

    /**
     * Test for invalid option.
     */
    @Test(expected = IllegalStateException.class)
    public void testInvalidOption()
    {
        String[] arguments = {"--invalid"};

        PosixParser parser = new PosixParser();
        parser.flatten(options, arguments, false);
    }

    /**
     * Test for bursting an option.
     */
    @Test
    public void testBurstingOption()
    {
        String[] arguments = {"-abc"};
        String[] expected = {"-a", "-b", "-c"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, result);
    }

    /**
     * Test for bursting an option with argument.
     */
    @Test
    public void testBurstingOptionWithArgument()
    {
        String[] arguments = {"-acd"};
        String[] expected = {"-a", "-c", "-d"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, result);
    }

    /**
     * Test for bursting an option with multiple arguments.
     */
    @Test
    public void testBurstingOptionWithMultipleArguments()
    {
        String[] arguments = {"-abcdef"};
        String[] expected = {"-a", "-b", "-c", "-d", "-e", "-f"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, result);
    }

    /**
     * Test for bursting an option with argument and other arguments.
     */
    @Test
    public void testBurstingOptionWithOtherArguments()
    {
        String[] arguments = {"-abc arg"};
        String[] expected = {"-a", "-b", "-c", "arg"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expected, result);
    }
}