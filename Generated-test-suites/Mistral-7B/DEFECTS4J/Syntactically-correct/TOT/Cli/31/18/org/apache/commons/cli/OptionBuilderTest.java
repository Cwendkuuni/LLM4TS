package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;

import org.junit.Test;

/**
 * Test cases for OptionBuilder class.
 */
public class OptionBuilderTest
{
    @Test
    public void testWithLongOpt()
    {
        Option option = OptionBuilder.withLongOpt("longOpt")
                                     .create();
        assertEquals("longOpt", option.getLongOpt());
    }

    @Test
    public void testHasArg()
    {
        Option option = OptionBuilder.hasArg()
                                     .create();
        assertEquals(Option.VALUE_REQUIRED, option.getArgs());
    }

    @Test
    public void testHasOptionalArg()
    {
        Option option = OptionBuilder.hasOptionalArg()
                                      .create();
        assertEquals(Option.VALUE_OPTIONAL, option.getArgs());
    }

    @Test
    public void testHasArgs()
    {
        Option option = OptionBuilder.hasArgs()
                                      .create();
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
    }

    @Test
    public void testHasArgsWithNum()
    {
        Option option = OptionBuilder.hasArgs(3)
                                      .create();
        assertEquals(3, option.getArgs());
    }

    @Test
    public void testIsRequired()
    {
        Option option = OptionBuilder.isRequired()
                                     .create();
        assertTrue(option.isRequired());
    }

    @Test
    public void testWithDescription()
    {
        Option option = OptionBuilder.withDescription("My option description")
                                     .create();
        assertEquals("My option description", option.getDescription());
    }

    @Test
    public void testWithType()
    {
        Option option = OptionBuilder.withType(Integer.class)
                                     .create();
        assertEquals(Integer.class, option.getType());
    }

    @Test
    public void testWithValueSeparator()
    {
        Option option = OptionBuilder.withValueSeparator(':')
                                      .create();
        assertEquals(':', option.getValueSeparator());
    }

    @Test
    public void testCreate()
    {
        Option option = OptionBuilder.withLongOpt("longOpt")
                                     .hasArg()
                                     .create();
        assertEquals("longOpt", option.getLongOpt());
        assertEquals(Option.VALUE_REQUIRED, option.getArgs());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt()
    {
        OptionBuilder.reset();
        OptionBuilder.hasArg();
        Option option = OptionBuilder.create();
        assertThrows(IllegalArgumentException.class, () -> option);
    }
}