package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * Test cases for OptionBuilder class.
 */
public class OptionBuilderTest
{
    /**
     * Test creating a required option with one argument.
     */
    @Test
    public void testRequiredOptionWithOneArgument()
    {
        OptionBuilder optBuilder = OptionBuilder.withLongOpt("inputFile")
                .isRequired()
                .hasArg()
                .withArgName("file")
                .withDescription("specify input file");

        Option option = optBuilder.create('i');

        Assert.assertEquals("inputFile", option.getLongOpt());
        Assert.assertEquals("specify input file", option.getDescription());
        Assert.assertFalse(option.isValueSepPresent());
        Assert.assertEquals(1, option.getArgPosition().length);
        Assert.assertFalse(option.isUnboundedValues());
        Assert.assertEquals("file", option.getArgName());
        Assert.assertTrue(option.isRequired());
    }

    /**
     * Test creating an optional option with one argument.
     */
    @Test
    public void testOptionalOptionWithOneArgument()
    {
        OptionBuilder optBuilder = OptionBuilder.withLongOpt("outputFile")
                .hasArg()
                .withArgName("file")
                .withDescription("specify output file")
                .hasOptionalArg();

        Option option = optBuilder.create('o');

        Assert.assertEquals("outputFile", option.getLongOpt());
        Assert.assertEquals("specify output file", option.getDescription());
        Assert.assertFalse(option.isValueSepPresent());
        Assert.assertEquals("file", option.getArgName());
        Assert.assertTrue(option.isOptional());
    }

    /**
     * Test creating an option with unlimited arguments.
     */
    @Test
    public void testOptionWithUnlimitedArguments()
    {
        OptionBuilder optBuilder = OptionBuilder.withLongOpt("properties")
                .hasArgs()
                .withDescription("specify properties");

        Option option = optBuilder.create('p');

        Assert.assertEquals("properties", option.getLongOpt());
        Assert.assertEquals("specify properties", option.getDescription());
        Assert.assertTrue(option.isValueSepPresent());
        Assert.assertEquals(Option.UNLIMITED_VALUES, option.getArgs().length);
    }

    /**
     * Test creating an option with a specific number of arguments.
     */
    @Test
    public void testOptionWithSpecifiedNumberOfArguments()
    {
        OptionBuilder optBuilder = OptionBuilder.withLongOpt("Filters")
                .hasArgs(2)
                .withDescription("specify filters")
                .withValueSeparator(':');

        Option option = optBuilder.create('f');

        Assert.assertEquals("Filters", option.getLongOpt());
        Assert.assertEquals("specify filters", option.getDescription());
        Assert.assertEquals(":", option.getValueSeparator());
        Assert.assertEquals(new int[]{2}, option.getArgs().clone());
    }

    /**
     * Test creating an option with a value of custom type.
     */
    @Test
    public void testOptionWithCustomValueType()
    {
        OptionBuilder optBuilder = OptionBuilder.withLongOpt("threshold")
                .withType(Double.TYPE)
                .isRequired()
                .hasArg()
                .withArgName("thresholdValue")
                .withDescription("specify threshold value");

        Option option = optBuilder.create('t');

        Assert.assertEquals("threshold", option.getLongOpt());
        Assert.assertEquals("specify threshold value", option.getDescription());
        Assert.assertEquals(Double.TYPE, option.getType());
        Assert.assertFalse(option.isValueSepPresent());
        Assert.assertEquals("thresholdValue", option.getArgName());
        Assert.assertTrue(option.isRequired());
    }

    /**
     * Test creating an option with a value with optional argument.
     */
    @Test
    public void testOptionWithOptionalArgument()
    {
        OptionBuilder optBuilder = OptionBuilder.withLongOpt("encoding")
                .hasArgs()
                .withDescription("specify encoding");

        Option option = optBuilder.create('e');

        Assert.assertEquals("encoding", option.getLongOpt());
        Assert.assertEquals("specify encoding", option.getDescription());
        Assert.assertTrue(option.isOptional());
        Assert.assertTrue(option.isUnboundedValues());
    }
}