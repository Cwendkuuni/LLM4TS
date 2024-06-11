package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Assert;
import org.junit.Test;

import static org.apache.commons.cli.Option.UNINITIALIZED;
import static org.apache.commons.cli.Option.UNLIMITED_VALUES;

public class OptionBuilderTest
{
    @Test
    public void testOptionBuilderWithLongOpt()
    {
        Option option = OptionBuilder.withLongOpt("optionLong")
                .create();
        Assert.assertEquals("optionLong", option.getLongOpt());
    }

    @Test
    public void testOptionBuilderHasArg()
    {
        Option option = OptionBuilder.hasArg()
                .create();
        Assert.assertEquals(UNINITIALIZED, option.getArgs());
        Assert.assertTrue(option.isRequired());
    }

    @Test
    public void testOptionBuilderHasArgWithName()
    {
        Option option = OptionBuilder.hasArg()
                .withArgName("argName")
                .create();
        Assert.assertEquals("argName", option.getArgName());
        Assert.assertEquals(UNINITIALIZED, option.getArgs());
        Assert.assertTrue(option.isRequired());
    }

    @Test
    public void testOptionBuilderHasArgRequiredFalse()
    {
        Option option = OptionBuilder.hasArg(false)
                .create();
        Assert.assertEquals(UNINITIALIZED, option.getArgs());
        Assert.assertFalse(option.isRequired());
    }

    @Test
    public void testOptionBuilderHasArgs()
    {
        Option option = OptionBuilder.hasArgs()
                .create();
        Assert.assertEquals(UNLIMITED_VALUES, option.getArgs());
        Assert.assertFalse(option.isRequired());
    }

    @Test
    public void testOptionBuilderHasArgsWithName()
    {
        Option option = OptionBuilder.hasArgs()
                .withArgName("argName")
                .create();
        Assert.assertEquals(UNLIMITED_VALUES, option.getArgs());
        Assert.assertFalse(option.isRequired());
        Assert.assertEquals("argName", option.getArgName());
    }

    @Test
    public void testOptionBuilderHasOptionalArg()
    {
        Option option = OptionBuilder.hasOptionalArg()
                .create();
        Assert.assertEquals(UNINITIALIZED, option.getArgs());
        Assert.assertFalse(option.isRequired());
        Assert.assertTrue(option.isOptional());
    }

    @Test
    public void testOptionBuilderHasOptionalArgs()
    {
        Option option = OptionBuilder.hasOptionalArgs()
                .create();
        Assert.assertEquals(UNLIMITED_VALUES, option.getArgs());
        Assert.assertFalse(option.isRequired());
        Assert.assertTrue(option.isOptional());
    }

    @Test
    public void testOptionBuilderHasOptionalArgsWithName()
    {
        Option option = OptionBuilder.hasOptionalArgs(2)
                .withArgName("argName")
                .create();
        Assert.assertEquals(2, option.getArgs());
        Assert.assertFalse(option.isRequired());
        Assert.assertTrue(option.isOptional());
        Assert.assertEquals("argName", option.getArgName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionBuilderHasOptionalArgsWithInvalidNumber()
    {
        OptionBuilder.hasOptionalArgs(-1);
    }

    @Test
    public void testOptionBuilderWithValueSeparator()
    {
        Option option = OptionBuilder.withValueSeparator(':')
                .create();
        Assert.assertEquals(':', option.getValueSeparator());
    }

    @Test
    public void testOptionBuilderWithValueSeparatorEmpty()
    {
        Option option = OptionBuilder.withValueSeparator()
                .create();
        Assert.assertEquals('=', option.getValueSeparator());
    }

    @Test
    public void testOptionBuilderIsRequired()
    {
        Option option = OptionBuilder.isRequired()
                .create();
        Assert.assertTrue(option.isRequired());
    }

    @Test
    public void testOptionBuilderWithDescription()
    {
        Option option = OptionBuilder.withDescription("Option description")
                .create();
        Assert.assertEquals("Option description", option.getDescription());
    }

    @Test
    public void testOptionBuilderWithType()
    {
        Class type = String.class;
        Option option = OptionBuilder.withType(type)
                .create();
        Assert.assertEquals(type, option.getValueType());
    }
}