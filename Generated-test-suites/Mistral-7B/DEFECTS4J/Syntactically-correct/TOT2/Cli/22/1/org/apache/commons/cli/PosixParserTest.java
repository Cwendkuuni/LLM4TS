package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Test cases for the PosixParser class.
 */
public class PosixParserTest
{
    /**
     * Test case for the flatten method with no options.
     */
    @Test
    public void testFlattenNoOptions()
    {
        String[] args = new String[]{"java", "MyProgram"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"java", "MyProgram"}, result);
    }

    /**
     * Test case for the flatten method with single option.
     */
    @Test
    public void testFlattenSingleOption()
    {
        String[] args = new String[]{"java", "-a", "value"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", true, "option A");
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"java", "--a", "value"}, result);
    }

    /**
     * Test case for the flatten method with multiple options.
     */
    @Test
    public void testFlattenMultipleOptions()
    {
        String[] args = new String[]{"java", "-a", "value1", "-b", "value2"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", true, "option A");
        options.addOption("b", true, "option B");
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"java", "--a", "value1", "--b", "value2"}, result);
    }

    /**
     * Test case for the flatten method with long options.
     */
    @Test
    public void testFlattenLongOptions()
    {
        String[] args = new String[]{"java", "--long-option", "value"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("longOption", true, "long option");
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"java", "--long-option", "value"}, result);
    }

    /**
     * Test case for the flatten method with an option and its argument.
     */
    @Test
    public void testFlattenOptionWithArgument()
    {
        String[] args = new String[]{"java", "-a", "value"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", true, "option A");
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"java", "--a", "value"}, result);
    }

    /**
     * Test case for the flatten method with an option and its multiple arguments.
     */
    @Test
    public void testFlattenOptionWithMultipleArguments()
    {
        String[] args = new String[]{"java", "-a", "arg1", "arg2"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", true, "option A");
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"java", "--a", "arg1", "arg2"}, result);
    }

    /**
     * Test case for the flatten method with an option that requires no argument.
     */
    @Test
    public void testFlattenOptionWithoutArgument()
    {
        String[] args = new String[]{"java", "-a"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "option A");
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"java", "--a"}, result);
    }

    /**
     * Test case for the flatten method with multiple options that require no arguments.
     */
    @Test
    public void testFlattenMultipleOptionsWithoutArguments()
    {
        String[] args = new String[]{"java", "-a", "-b", "-c"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "option A");
        options.addOption("b", false, "option B");
        options.addOption("c", false, "option C");
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"java", "--a", "--b", "--c"}, result);
    }

    /**
     * Test case for the flatten method with an option and its argument and multiple other arguments.
     */
    @Test
    public void testFlattenOptionWithArgumentAndOtherArguments()
    {
        String[] args = new String[]{"java", "-a", "arg1", "arg2", "arg3"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", true, "option A");
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"java", "--a", "arg1", "arg2", "arg3"}, result);
    }

    /**
     * Test case for the flatten method with an option that requires an argument and an option that does not require an argument.
     */
    @Test
    public void testFlattenOptionWithArgumentAndNoArgumentOption()
    {
        String[] args = new String[]{"java", "-a", "arg1", "-b"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", true, "option A");
        options.addOption("b", false, "option B");
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"java", "--a", "arg1", "--b"}, result);
    }

    /**
     * Test case for the flatten method with an option that requires an argument and multiple other arguments.
     */
    @Test
    public void testFlattenOptionWithArgumentAndOtherArguments()
    {
        String[] args = new String[]{"java", "-a", "arg1", "arg2", "arg3"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", true, "option A");
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"java", "--a", "arg1", "arg2", "arg3"}, result);
    }

    /**
     * Test case for the flatten method with no options but with the special "--" token.
     */
    @Test
    public void testFlattenNoOptionsWithSpecialToken()
    {
        String[] args = new String[]{"java", "--"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"java", "--"}, result);
    }

    /**
     * Test case for the flatten method with an invalid option.
     */
    @Test(expected = ParseException.class)
    public void testFlattenInvalidOption()
    {
        String[] args = new String[]{"java", "-x"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        parser.flatten(options, args, false);
    }

    /**
     * Test case for the flatten method with an option that requires an argument and an invalid option.
     */
    @Test(expected = ParseException.class)
    public void testFlattenOptionWithArgumentAndInvalidOption()
    {
        String[] args = new String[]{"java", "-a", "arg1", "-x"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        parser.flatten(options, args, false);
    }

    /**
     * Test case for the flatten method with an option that requires an argument and an option that requires no argument.
     */
    @Test
    public void testFlattenOptionWithArgumentAndNoArgumentOption2()
    {
        String[] args = new String[]{"java", "-a", "arg1", "-b"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", true, "option A");
        options.addOption("b", false, "option B");
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"java", "--a", "arg1", "--b"}, result);
    }

    /**
     * Test case for the flatten method with an option that requires an argument and an option that requires no argument, and an invalid option.
     */
    @Test(expected = ParseException.class)
    public void testFlattenOptionWithArgumentAndInvalidOption2()
    {
        String[] args = new String[]{"java", "-a", "arg1", "x"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", true, "option A");
        parser.flatten(options, args, false);
    }
}