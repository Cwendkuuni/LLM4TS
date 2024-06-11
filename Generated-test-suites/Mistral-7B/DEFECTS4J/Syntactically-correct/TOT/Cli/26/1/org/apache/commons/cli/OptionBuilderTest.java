package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test cases for OptionBuilder class.
 */
public class OptionBuilderTest
{
    /**
     * Test creating an Option with mandatory argument.
     */
    @Test
    public void testMandatoryOption()
    {
        OptionBuilder builder = OptionBuilder.withLongOpt("input")
                .isRequired()
                .hasArg()
                .withArgName("file");
        Option option = builder.create('i');

        Assert.assertEquals("input", option.getLongOpt());
        Assert.assertEquals("Input file", option.getDescription());
        Assert.assertTrue(option.isRequired());
        Assert.assertEquals(1, option.getArgs().length);
        Assert.assertNotNull(option.getValue());
    }

    /**
     * Test creating an Option with optional argument.
     */
    @Test
    public void testOptionalOption()
    {
        OptionBuilder builder = OptionBuilder.withLongOpt("output")
                .isRequired(false)
                .hasArg()
                .withArgName("path");
        Option option = builder.create('o');

        Assert.assertEquals("output", option.getLongOpt());
        Assert.assertEquals("Output path", option.getDescription());
        Assert.assertFalse(option.isRequired());
        Assert.assertNull(option.getValue());
    }

    /**
     * Test creating an Option without an argument.
     */
    @Test
    public void testNoArgOption()
    {
        OptionBuilder builder = OptionBuilder.withLongOpt("verbose")
                .isRequired(false)
                .hasArg(false);
        Option option = builder.create('v');

        Assert.assertEquals("verbose", option.getLongOpt());
        Assert.assertEquals("Verbose mode", option.getDescription());
        Assert.assertFalse(option.isRequired());
        Assert.assertNull(option.getValue());
    }

    /**
     * Test creating an Option with multiple arguments.
     */
    @Test
    public void testMultiArgOption()
    {
        OptionBuilder builder = OptionBuilder.withLongOpt("ports")
                .isRequired(false)
                .hasArgs(3)
                .withValueSeparator(',');
        Option option = builder.create('p');

        Assert.assertEquals("ports", option.getLongOpt());
        Assert.assertEquals("Ports to listen on", option.getDescription());
        Assert.assertFalse(option.isRequired());
        Assert.assertNotNull(option.getArgs());
        Assert.assertEquals(3, option.getArgs().length);
        Assert.assertEquals(',', option.getValueSeparator());
    }

    /**
     * Test creating an Option with a custom argument name.
     */
    @Test
    public void testCustomArgNameOption()
    {
        OptionBuilder builder = OptionBuilder.withLongOpt("server")
                .isRequired(false)
                .hasArg()
                .withArgName("address");
        Option option = builder.create('s');

        Assert.assertEquals("server", option.getLongOpt());
        Assert.assertEquals("Server address", option.getDescription());
        Assert.assertFalse(option.isRequired());
        Assert.assertEquals("address", option.getArgName());
        Assert.assertNull(option.getValue());
    }

    /**
     * Test creating an Option with a custom value separator.
     */
    @Test
    public void testCustomValueSeparatorOption()
    {
        OptionBuilder builder = OptionBuilder.withLongOpt("delimiters")
                .isRequired(false)
                .hasArgs(2)
                .withValueSeparator(';');
        Option option = builder.create('d');

        Assert.assertEquals("delimiters", option.getLongOpt());
        Assert.assertEquals("Delimiters to use", option.getDescription());
        Assert.assertFalse(option.isRequired());
        Assert.assertNotNull(option.getArgs());
        Assert.assertEquals(2, option.getArgs().length);
        Assert.assertEquals(';', option.getValueSeparator());
    }

    /**
     * Test creating an Option with a custom value type.
     */
    @Test
    public void testCustomValueTypeOption()
    {
        OptionBuilder builder = OptionBuilder.withLongOpt("timeout")
                .isRequired(false)
                .hasArg()
                .withType(Integer.TYPE);
        Option option = builder.create('t');

        Assert.assertEquals("timeout", option.getLongOpt());
        Assert.assertEquals("Timeout in milliseconds", option.getDescription());
        Assert.assertFalse(option.isRequired());
        Assert.assertEquals(Integer.TYPE, option.getType());
        Assert.assertNull(option.getValue());
    }

    /**
     * Test creating an Option with an empty description.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEmptyDescriptionOption()
    {
        OptionBuilder builder = OptionBuilder.withLongOpt("");
        Option option = builder.create('x');
    }

    /**
     * Test creating an Option without setting a long option.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNoLongOptOption()
    {
        OptionBuilder builder = OptionBuilder.isRequired(false);
        Option option = builder.create('y');
    }
}