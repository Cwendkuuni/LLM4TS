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
    @Test
    public void testOptionBuilderDefault()
    {
        Option option = OptionBuilder.create();
        Assert.assertNull(option.getLongOpt());
        Assert.assertFalse(option.isRequired());
        Assert.assertFalse(option.hasArg());
        Assert.assertEquals(Option.UNINITIALIZED, option.getArgs());
        Assert.assertNull(option.getType());
        Assert.assertFalse(option.hasOptionalArg());
        Assert.assertEquals(Option.NO_ARGUMENTS, option.getValueSeparator());
        Assert.assertEquals("arg", option.getArgName());
    }

    @Test
    public void testOptionBuilderLongOpt()
    {
        Option option = OptionBuilder.withLongOpt("myopt")
                                     .create();
        Assert.assertEquals("myopt", option.getLongOpt());
        Assert.assertFalse(option.isRequired());
        Assert.assertFalse(option.hasArg());
        Assert.assertEquals(Option.UNINITIALIZED, option.getArgs());
        Assert.assertNull(option.getType());
        Assert.assertFalse(option.hasOptionalArg());
        Assert.assertEquals(Option.NO_ARGUMENTS, option.getValueSeparator());
        Assert.assertEquals("arg", option.getArgName());
    }

    @Test
    public void testOptionBuilderHasArg()
    {
        Option option = OptionBuilder.hasArg()
                                     .create();
        Assert.assertFalse(option.isRequired());
        Assert.assertTrue(option.hasArg());
        Assert.assertEquals(Option.ONE_ARGUMENT, option.getArgs());
        Assert.assertNull(option.getType());
        Assert.assertFalse(option.hasOptionalArg());
        Assert.assertEquals(Option.NO_ARGUMENTS, option.getValueSeparator());
        Assert.assertEquals("arg", option.getArgName());
    }

    @Test
    public void testOptionBuilderIsRequired()
    {
        Option option = OptionBuilder.isRequired()
                                     .create();
        Assert.assertTrue(option.isRequired());
        Assert.assertFalse(option.hasArg());
        Assert.assertEquals(Option.UNINITIALIZED, option.getArgs());
        Assert.assertNull(option.getType());
        Assert.assertFalse(option.hasOptionalArg());
        Assert.assertEquals(Option.NO_ARGUMENTS, option.getValueSeparator());
        Assert.assertEquals("arg", option.getArgName());
    }

    @Test
    public void testOptionBuilderValueSeparator()
    {
        Option option = OptionBuilder.withValueSeparator(':')
                                     .create();
        Assert.assertFalse(option.isRequired());
        Assert.assertFalse(option.hasArg());
        Assert.assertEquals(Option.UNINITIALIZED, option.getArgs());
        Assert.assertNull(option.getType());
        Assert.assertFalse(option.hasOptionalArg());
        Assert.assertEquals(':', option.getValueSeparator());
        Assert.assertEquals("arg", option.getArgName());
    }

    @Test
    public void testOptionBuilderHasOptionalArg()
    {
        Option option = OptionBuilder.hasOptionalArg()
                                     .create();
        Assert.assertFalse(option.isRequired());
        Assert.assertTrue(option.hasArg());
        Assert.assertEquals(Option.ONE_ARGUMENT, option.getArgs());
        Assert.assertFalse(option.hasOptionalArg());
        Assert.assertEquals(Option.NO_ARGUMENTS, option.getValueSeparator());
        Assert.assertEquals("arg", option.getArgName());
    }

    @Test
    public void testOptionBuilderHasArgs()
    {
        Option option = OptionBuilder.hasArgs()
                                     .create();
        Assert.assertFalse(option.isRequired());
        Assert.assertTrue(option.hasArg());
        Assert.assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
        Assert.assertNull(option.getType());
        Assert.assertFalse(option.hasOptionalArg());
        Assert.assertEquals(Option.NO_ARGUMENTS, option.getValueSeparator());
        Assert.assertEquals("arg", option.getArgName());
    }

    @Test
    public void testOptionBuilderHasArgsNum()
    {
        Option option = OptionBuilder.hasArgs(2)
                                     .create();
        Assert.assertFalse(option.isRequired());
        Assert.assertTrue(option.hasArg());
        Assert.assertEquals(2, option.getArgs());
        Assert.assertNull(option.getType());
        Assert.assertFalse(option.hasOptionalArg());
        Assert.assertEquals(Option.NO_ARGUMENTS, option.getValueSeparator());
        Assert.assertEquals("arg", option.getArgName());
    }

    @Test
    public void testOptionBuilderWithType()
    {
        Option option = OptionBuilder.withType(Integer.class)
                                     .create();
        Assert.assertFalse(option.isRequired());
        Assert.assertTrue(option.hasArg());
        Assert.assertEquals(Option.UNINITIALIZED, option.getArgs());
        Assert.assertEquals(Integer.class, option.getType());
        Assert.assertFalse(option.hasOptionalArg());
        Assert.assertEquals(Option.NO_ARGUMENTS, option.getValueSeparator());
        Assert.assertEquals("arg", option.getArgName());
    }

    @Test
    public void testOptionBuilderWithDescription()
    {
        Option option = OptionBuilder.withDescription("My Option Description")
                                     .create();
        Assert.assertEquals("My Option Description", option.getDescription());
        Assert.assertFalse(option.isRequired());
        Assert.assertFalse(option.hasArg());
        Assert.assertEquals(Option.UNINITIALIZED, option.getArgs());
        Assert.assertNull(option.getType());
        Assert.assertFalse(option.hasOptionalArg());
        Assert.assertEquals(Option.NO_ARGUMENTS, option.getValueSeparator());
        Assert.assertEquals("arg", option.getArgName());
    }

    @Test
    public void testOptionBuilderWithLongOptAndDescription()
    {
        Option option = OptionBuilder.withLongOpt("myopt")
                                     .withDescription("My Option Description")
                                     .create();
        Assert.assertEquals("My Option Description", option.getDescription());
        Assert.assertEquals("myopt", option.getLongOpt());
        Assert.assertFalse(option.isRequired());
        Assert.assertFalse(option.hasArg());
        Assert.assertEquals(Option.UNINITIALIZED, option.getArgs());
        Assert.assertNull(option.getType());
        Assert.assertFalse(option.hasOptionalArg());
        Assert.assertEquals(Option.NO_ARGUMENTS, option.getValueSeparator());
        Assert.assertEquals("arg", option.getArgName());
    }

    @Test
    public void testOptionBuilderHasOptionalArgs()
    {
        Option option = OptionBuilder.hasOptionalArgs()
                                     .create();
        Assert.assertFalse(option.isRequired());
        Assert.assertTrue(option.hasArg());
        Assert.assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
        Assert.assertFalse(option.hasOptionalArg());
        Assert.assertEquals(Option.NO_ARGUMENTS, option.getValueSeparator());
        Assert.assertEquals("arg", option.getArgName());
    }

    @Test
    public void testOptionBuilderHasOptionalArgsNum()
    {
        Option option = OptionBuilder.hasOptionalArgs(3)
                                     .create();
        Assert.assertFalse(option.isRequired());
        Assert.assertTrue(option.hasArg());
        Assert.assertEquals(3, option.getArgs());
        Assert.assertFalse(option.hasOptionalArg());
        Assert.assertEquals(Option.NO_ARGUMENTS, option.getValueSeparator());
        Assert.assertEquals("arg", option.getArgName());
    }

    @Test
    public void testOptionBuilderWithArgName()
    {
        Option option = OptionBuilder.withArgName("username")
                                     .create();
        Assert.assertFalse(option.isRequired());
        Assert.assertTrue(option.hasArg());
        Assert.assertEquals(Option.UNINITIALIZED, option.getArgs());
        Assert.assertNull(option.getType());
        Assert.assertFalse(option.hasOptionalArg());
        Assert.assertEquals(Option.NO_ARGUMENTS, option.getValueSeparator());
        Assert.assertEquals("username", option.getArgName());
    }

    @Test
    public void testOptionBuilderWithArgNameAndDesc()
    {
        Option option = OptionBuilder.withArgName("username")
                                     .withDescription("User name")
                                     .create();
        Assert.assertEquals("User name", option.getDescription());
        Assert.assertEquals("username", option.getArgName());
        Assert.assertFalse(option.isRequired());
        Assert.assertTrue(option.hasArg());
        Assert.assertEquals(Option.UNINITIALIZED, option.getArgs());
        Assert.assertNull(option.getType());
        Assert.assertFalse(option.hasOptionalArg());
        Assert.assertEquals(Option.NO_ARGUMENTS, option.getValueSeparator());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionBuilderWithoutLongOpt()
    {
        OptionBuilder.reset();
        OptionBuilder.hasArg();
        OptionBuilder.create();
    }

    @Test
    public void testOptionBuilderCreateWithArgs()
    {
        Option option = OptionBuilder.hasArgs(2)
                                     .create();
        Assert.assertFalse(option.isRequired());
        Assert.assertTrue(option.hasArg());
        Assert.assertEquals(2, option.getArgs());
        Assert.assertNull(option.getType());
        Assert.assertFalse(option.hasOptionalArg());
        Assert.assertEquals(Option.NO_ARGUMENTS, option.getValueSeparator());
        Assert.assertEquals("arg", option.getArgName());

        Option parsedOption = option.parse("--myopt arg1 arg2");
        Assert.assertEquals("arg1", parsedOption.getValue());
        Assert.assertEquals("arg2", parsedOption.getValue(1));
    }

    @Test
    public void testOptionBuilderCreateWithOptionalArg()
    {
        Option option = OptionBuilder.hasOptionalArg()
                                     .create();
        Assert.assertFalse(option.isRequired());
        Assert.assertTrue(option.hasArg());
        Assert.assertEquals(Option.UNINITIALIZED, option.getArgs());
        Assert.assertFalse(option.hasOptionalArg());
        Assert.assertEquals(Option.NO_ARGUMENTS, option.getValueSeparator());
        Assert.assertEquals("arg", option.getArgName());

        Option parsedOption = option.parse("--myopt");
        Assert.assertNull(parsedOption.getValue());

        parsedOption = option.parse("--myopt arg1");
        Assert.assertEquals("arg1", parsedOption.getValue());
    }

    @Test
    public void testOptionBuilderCreateWithType()
    {
        Option option = OptionBuilder.withType(String.class)
                                     .create();
        Assert.assertFalse(option.isRequired());
        Assert.assertTrue(option.hasArg());
        Assert.assertEquals(Option.UNINITIALIZED, option.getArgs());
        Assert.assertEquals(String.class, option.getType());
        Assert.assertFalse(option.hasOptionalArg());
        Assert.assertEquals(Option.NO_ARGUMENTS, option.getValueSeparator());
        Assert.assertEquals("arg", option.getArgName());

        Object parsedValue = option.parseValue("--myopt arg1");
        Assert.assertEquals("arg1", parsedValue);
    }

    @Test
    public void testOptionBuilderCreateWithValueSeparator()
    {
        Option option = OptionBuilder.withValueSeparator(' ')
                                     .create();
        Assert.assertFalse(option.isRequired());
        Assert.assertTrue(option.hasArg());
        Assert.assertEquals(Option.UNINITIALIZED, option.getArgs());
        Assert.assertFalse(option.hasOptionalArg());
        Assert.assertEquals(' ', option.getValueSeparator());
        Assert.assertEquals("arg", option.getArgName());

        Object[] parsedValues = option.parseValues("--myopt arg1 arg2");
        Assert.assertEquals(2, parsedValues.length);
        Assert.assertEquals("arg1", parsedValues[0]);
        Assert.assertEquals("arg2", parsedValues[1]);
    }

    @Test
    public void testOptionBuilderCreateWithValueSeparatorAndType()
    {
        Option option = OptionBuilder.withType(Double.class)
                                     .withValueSeparator(',')
                                     .create();
        Assert.assertFalse(option.isRequired());
        Assert.assertTrue(option.hasArg());
        Assert.assertEquals(Option.UNINITIALIZED, option.getArgs());
        Assert.assertEquals(Double.class, option.getType());
        Assert.assertFalse(option.hasOptionalArg());
        Assert.assertEquals(',', option.getValueSeparator());
        Assert.assertEquals("arg", option.getArgName());

        Double[] parsedValues = option.parseValues("--myopt arg1,arg2");
        Assert.assertEquals(2, parsedValues.length);
        Assert.assertEquals(Double.parseDouble("arg1"), parsedValues[0]);
        Assert.assertEquals(Double.parseDouble("arg2"), parsedValues[1]);
    }
}