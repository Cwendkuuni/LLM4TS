package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class OptionBuilderTest
{
    @Test
    public void testOptionBuilderWithLongOpt()
    {
        Option option = OptionBuilder.withLongOpt("myLongOpt")
                .create();
        assertEquals("myLongOpt", option.getLongOpt());
    }

    @Test
    public void testOptionBuilderWithArg()
    {
        Option option = OptionBuilder.hasArg().create();
        assertEquals(Option.UNINITIALIZED, option.getArgs());

        Option optionWithArg = OptionBuilder.hasArg().withArgName("argName")
                .create();
        assertEquals("argName", optionWithArg.getArgName());

        Option optionWithTwoArgs = OptionBuilder.hasArgs(2).create();
        assertEquals(2, optionWithTwoArgs.getArgs());

        Option optionWithOptionalArg = OptionBuilder.hasOptionalArg().create();
        assertEquals(Option.UNINITIALIZED, optionWithOptionalArg.getArgs());
        assertTrue(optionWithOptionalArg.isOptional());

        Option optionWithValueSeparator = OptionBuilder.withValueSeparator(':')
                .create();
        assertEquals(':', optionWithValueSeparator.getValueSeparator());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionBuilderWithNoLongOpt()
    {
        OptionBuilder.create();
    }

    @Test
    public void testOptionBuilderWithDescription()
    {
        Option option = OptionBuilder.withLongOpt("myLongOpt")
                .withDescription("My long option description")
                .create();
        assertEquals("My long option description", option.getDescription());
    }

    @Test
    public void testOptionBuilderIsRequired()
    {
        Option option = OptionBuilder.isRequired().create();
        assertTrue(option.isRequired());

        Option optionNotRequired = OptionBuilder.create();
        assertFalse(optionNotRequired.isRequired());
    }

    @Test
    public void testOptionBuilderIsOptional()
    {
        Option optionRequired = OptionBuilder.isRequired().create();
        assertFalse(optionRequired.isOptional());

        Option optionOptional = OptionBuilder.hasOptionalArg().create();
        assertTrue(optionOptional.isOptional());
    }
}