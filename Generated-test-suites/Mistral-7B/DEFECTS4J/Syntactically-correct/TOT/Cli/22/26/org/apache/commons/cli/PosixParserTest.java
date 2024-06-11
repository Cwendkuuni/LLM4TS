package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for {@link PosixParser}.
 */
public class PosixParserTest
{
    /**
     * Test parsing standard options.
     */
    @Test
    public void testStandardOptions() throws ParseException
    {
        String[] args = {"-a", "--optionA=valueA", "--optionB=valueB"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("B", false, "Option B");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args);

        Assert.assertArrayEquals(new String[]{"-a", "--optionA=valueA", "--optionB"}, parsedArgs);
        Assert.assertTrue(options.isSet("a"));
        Assert.assertFalse(options.isSet("B"));
    }

    /**
     * Test parsing long and short options with values.
     */
    @Test
    public void testLongAndShortOptionsWithValues() throws ParseException
    {
        String[] args = {"--optionA=valueA", "-a=valueA", "--optionB=valueB", "-b=valueB"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("B", true, "Option B");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args);

        Assert.assertArrayEquals(new String[]{"--optionA=valueA", "--optionB=valueB"}, parsedArgs);
        Assert.assertEquals("valueA", options.getOptionValue("a"));
        Assert.assertEquals("valueB", options.getOptionValue("B"));
    }

    /**
     * Test parsing invalid options.
     */
    @Test(expected = ParseException.class)
    public void testInvalidOptions() throws ParseException
    {
        String[] args = {"--invalid", "-invalid"};
        Options options = new Options();

        PosixParser parser = new PosixParser();
        parser.parse(options, args);
    }

    /**
     * Test parsing long options with no value.
     */
    @Test
    public void testLongOptionsWithoutValues() throws ParseException
    {
        String[] args = {"--optionA", "--optionB"};
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("B", false, "Option B");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args);

        Assert.assertArrayEquals(new String[]{"--optionA", "--optionB"}, parsedArgs);
        Assert.assertTrue(options.isSet("a"));
        Assert.assertTrue(options.isSet("B"));
    }

    /**
     * Test handling of multiple values for an option.
     */
    @Test
    public void testMultipleValuesForAnOption() throws ParseException
    {
        String[] args = {"--optionA=value1", "--optionA=value2"};
        Options options = new Options();
        options.addOption("a", true, "Option A");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args);

        Assert.assertArrayEquals(new String[]{"--optionA=value1", "--optionA=value2"}, parsedArgs);
        Assert.assertEquals(Arrays.asList("value1", "value2"), options.getValues("a"));
    }

    /**
     * Test handling of a single value for an option that can have multiple values.
     */
    @Test
    public void testSingleValueForAnOptionWithMultipleValues() throws ParseException
    {
        String[] args = {"--optionA=value"};
        Options options = new Options();
        options.addOption("a", true, "Option A");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args);

        Assert.assertArrayEquals(new String[]{"--optionA=value"}, parsedArgs);
        Assert.assertEquals("value", options.getOptionValue("a"));
        Assert.assertNull(options.getValues("a"));
    }

    /**
     * Test handling of an option with a single character short name.
     */
    @Test
    public void testSingleCharacterShortNameOption() throws ParseException
    {
        String[] args = {"-a"};
        Options options = new Options();
        options.addOption("a", true, "Option A");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args);

        Assert.assertArrayEquals(new String[]{"-a"}, parsedArgs);
        Assert.assertTrue(options.isSet("a"));
    }

    /**
     * Test handling of an option with a long and short name.
     */
    @Test
    public void testLongAndShortNameOption() throws ParseException
    {
        String[] args = {"--optionA", "-a"};
        Options options = new Options();
        options.addOption("a", true, "Option A");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args);

        Assert.assertArrayEquals(new String[]{"--optionA", "-a"}, parsedArgs);
        Assert.assertTrue(options.isSet("a"));
    }

    /**
     * Test handling of a non-option argument.
     */
    @Test
    public void testNonOptionArgument() throws ParseException
    {
        String[] args = {"argument"};

        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args);

        Assert.assertEquals(new String[]{"argument"}, parsedArgs);
        Assert.assertNull(options);
    }

    /**
     * Test handling of an empty argument list.
     */
    @Test
    public void testEmptyArgumentList() throws ParseException
    {
        String[] args = {};

        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args);

        Assert.assertArrayEquals(new String[0], parsedArgs);
        Assert.assertNull(options);
    }
}