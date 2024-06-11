package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class OptionBuilderTest
{
    @Test
    public void testOptionBuilderWithLongOpt()
    {
        Option option = OptionBuilder.withLongOpt("mylongopt")
                                     .isRequired()
                                     .create();
        assertEquals("mylongopt", option.getLongOpt());
        assertTrue(option.isRequired());
        assertNull(option.getArgName());
        assertNull(option.getValue());
    }

    @Test
    public void testOptionBuilderWithLongOptAndArgName()
    {
        Option option = OptionBuilder.withLongOpt("mylongopt")
                                     .withArgName("argName")
                                     .create();
        assertEquals("mylongopt", option.getLongOpt());
        assertEquals("argName", option.getArgName());
        assertNull(option.getValue());
    }

    @Test
    public void testOptionBuilderWithLongOptAndHasArg()
    {
        Option option = OptionBuilder.withLongOpt("mylongopt")
                                     .hasArg()
                                     .create();
        assertEquals("mylongopt", option.getLongOpt());
        assertEquals("arg", option.getArgName());
        assertNull(option.getValue());
        assertTrue(option.isRequired());
        assertTrue(option.hasArg());
    }

    @Test
    public void testOptionBuilderWithLongOptAndHasOptionalArg()
    {
        Option option = OptionBuilder.withLongOpt("mylongopt")
                                     .hasOptionalArg()
                                     .create();
        assertEquals("mylongopt", option.getLongOpt());
        assertEquals("arg", option.getArgName());
        assertNull(option.getValue());
        assertFalse(option.isRequired());
        assertTrue(option.isOptional());
    }

    @Test
    public void testOptionBuilderWithLongOptAndValueSeparator()
    {
        Option option = OptionBuilder.withLongOpt("mylongopt")
                                     .withValueSeparator(':')
                                     .create();
        assertEquals("mylongopt", option.getLongOpt());
        assertEquals("arg:", option.getArgName());
        assertNull(option.getValue());
        assertTrue(option.hasValueSeparator());
        assertEquals(':', option.getValueSeparator());
    }

    @Test
    public void testOptionBuilderWithLongOptAndType()
    {
        String expectedType = Integer.class.getName();
        Option option = OptionBuilder.withLongOpt("mylongopt")
                                     .withType(Integer.class)
                                     .create();
        assertEquals("mylongopt", option.getLongOpt());
        assertEquals("arg", option.getArgName());
        assertNull(option.getValue());
        assertTrue(option.hasArg());
        assertEquals(expectedType, option.getType().getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionBuilderWithLongOptMissing()
    {
        OptionBuilder.reset();
        OptionBuilder.withType(Integer.class);
        OptionBuilder.create('x');
    }
}