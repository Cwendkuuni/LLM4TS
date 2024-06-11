package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;

import org.junit.Test;

/**
 * Test class for OptionBuilder
 */
public class OptionBuilderTest
{
    @Test
    public void testWithLongOptAndCreate()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("option1");
        Option option = optionBuilder.create();
        assertEquals("option1", option.getLongOpt());
        assertNull(option.getValue());
    }

    @Test
    public void testHasArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Option option = optionBuilder.create();
        assertEquals(Option.UNINITIALIZED, option.getArgs(), "The option should not have an argument by default");

        OptionBuilder optionBuilderWithArg = OptionBuilder.hasArg(true);
        Option optionWithArg = optionBuilderWithArg.create();
        assertEquals(Option.VALUE_REQUIRED, optionWithArg.getArgs(), "The option should require an argument");
    }

    @Test
    public void testHasOptionalArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        Option option = optionBuilder.create();
        assertEquals(Option.VALUE_OPTIONAL, option.getArgs(), "The option should have an optional argument");

        OptionBuilder optionBuilderWithArgs = OptionBuilder.hasOptionalArgs(2);
        Option optionWithArgs = optionBuilderWithArgs.create();
        assertEquals(Option.UNLIMITED_VALUES, optionWithArgs.getArgs(), "The option should allow multiple optional arguments");
    }

    @Test
    public void testWithValueSeparator()
    {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        Option option = optionBuilder.create();
        assertEquals(':', option.getValueSeparator());

        OptionBuilder optionBuilderWithValueSeparator = OptionBuilder.withValueSeparator();
        Option optionWithValueSeparator = optionBuilderWithValueSeparator.create();
        assertEquals('=', optionWithValueSeparator.getValueSeparator());
    }

    @Test
    public void testIsRequired()
    {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        Option option = optionBuilder.create();
        assertFalse(option.isRequired());

        OptionBuilder optionBuilderIsRequired = OptionBuilder.isRequired(true);
        Option optionIsRequired = optionBuilderIsRequired.create();
        assertTrue(optionIsRequired.isRequired());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder.reset(); // Reset to default values
        optionBuilder.create();
    }
}