package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;

import java.lang.reflect.Type;

import org.junit.Test;

/**
 * Test class for OptionBuilder.
 */
public class OptionBuilderTest
{
    @Test
    public void testReset()
    {
        OptionBuilder.reset();
        assertNull(OptionBuilder.longopt);
        assertNull(OptionBuilder.description);
        assertNull(OptionBuilder.argName);
        assertNull(OptionBuilder.type);
        assertFalse(OptionBuilder.required);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
        assertEquals(Character.MIN_VALUE, OptionBuilder.valuesep);
    }

    @Test
    public void testWithLongOpt()
    {
        OptionBuilder.withLongOpt("my-option");
        assertEquals("my-option", OptionBuilder.longopt);
    }

    @Test
    public void testHasArg()
    {
        OptionBuilder.hasArg();
        assertEquals(1, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testHasArgWithArgName()
    {
        OptionBuilder.withArgName("my-arg");
        OptionBuilder.hasArg();
        assertEquals("my-arg", OptionBuilder.argName);
        assertEquals(1, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testIsRequired()
    {
        OptionBuilder.isRequired();
        assertTrue(OptionBuilder.required);
    }

    @Test
    public void testWithValueSeparator()
    {
        OptionBuilder.withValueSeparator(':');
        assertEquals(':', OptionBuilder.valuesep);
    }

    @Test
    public void testHasArgsWithValueSeparator()
    {
        OptionBuilder.hasArgs(':');
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
        assertEquals(':', OptionBuilder.valuesep);
    }

    @Test
    public void testHasOptionalArg()
    {
        OptionBuilder.hasOptionalArg();
        assertTrue(OptionBuilder.optionalArg);
        assertEquals(1, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testHasOptionalArgsWithValueSeparator()
    {
        OptionBuilder.hasOptionalArgs(':');
        assertTrue(OptionBuilder.optionalArg);
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
        assertEquals(':', OptionBuilder.valuesep);
    }

    @Test
    public void testHasOptionalArgsWithNumArgs()
    {
        OptionBuilder.hasOptionalArgs(5);
        assertTrue(OptionBuilder.optionalArg);
        assertEquals(5, OptionBuilder.numberOfArgs);
    }

    @Test
    public void testWithType()
    {
        Class<Integer> intType = Integer.class;
        OptionBuilder.withType(intType);
        assertSame(intType, OptionBuilder.type);
    }

    @Test
    public void testWithDescription()
    {
        OptionBuilder.withDescription("My option description");
        assertEquals("My option description", OptionBuilder.description);
    }

    @Test
    public void testCreate() throws IllegalArgumentException
    {
        OptionBuilder.withLongOpt("my-option");
        OptionBuilder.hasArg();
        OptionBuilder.isRequired();
        OptionBuilder.withValueSeparator(':');
        OptionBuilder.hasOptionalArg();
        OptionBuilder.withType(String.class);
        OptionBuilder.withDescription("My option description");
        Option option = OptionBuilder.create('m');
        assertEquals("My option description", option.getDescription());
        assertEquals('m', option.getOpt());
        assertEquals(Option.UNINITIALIZED, option.getArgs().length);
        assertTrue(option.isRequired());
        assertFalse(option.hasArg());
        assertFalse(option.hasOptionalArg());
        assertEquals(':', option.getValueSeparator());
        assertSame(String.class, option.getType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt() throws IllegalArgumentException
    {
        OptionBuilder.hasArg();
        OptionBuilder.create();
    }
}