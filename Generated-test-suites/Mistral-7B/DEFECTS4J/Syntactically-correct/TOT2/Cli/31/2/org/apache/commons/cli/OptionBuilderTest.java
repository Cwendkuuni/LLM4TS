package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Assert;
import org.junit.Test;

public class OptionBuilderTest
{
    @Test
    public void testOptionBuilderWithLongOpt()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longopt");
        Option option = optionBuilder.create('L');
        Assert.assertEquals("longopt", option.getLongOpt());
    }

    @Test
    public void testOptionBuilderHasArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Option option = optionBuilder.create('A');
        Assert.assertEquals(Option.UNINITIALIZED, option.getArgs().length);
        Assert.assertTrue(option.hasArg());
    }

    @Test
    public void testOptionBuilderHasOptionalArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        Option option = optionBuilder.create('O');
        Assert.assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testOptionBuilderWithValueSeparator()
    {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        Option option = optionBuilder.create('V');
        Assert.assertEquals(':', option.getValueSeparator());
    }

    @Test
    public void testOptionBuilderIsRequired()
    {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        Option option = optionBuilder.create('R');
        Assert.assertTrue(option.isRequired());
    }

    @Test
    public void testOptionBuilderHasArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        Option option = optionBuilder.create('H');
        Assert.assertEquals(Option.UNLIMITED_VALUES, option.getArgs().length);
       Assert.assertTrue(option.hasArgs());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionBuilderMissingLongOpt()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        optionBuilder.create('M');
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionBuilderInvalidChar()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("invalid");
        optionBuilder.create('%');
    }
}