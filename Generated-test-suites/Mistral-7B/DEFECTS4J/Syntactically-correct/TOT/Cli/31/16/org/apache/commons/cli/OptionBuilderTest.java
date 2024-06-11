package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;

import org.junit.Test;

/**
 * Test class for OptionBuilder.
 */
public class OptionBuilderTest
{
    /**
     * Test creating an option with no arguments.
     */
    @Test
    public void testNoArgs()
    {
        Option option = OptionBuilder.withLongOpt("testOption")
                                     .create();
        assertNotNull(option);
        assertEquals("testOption", option.getLongOpt());
        assertNull(option.getArgName());
        assertFalse(option.isRequired());
        assertFalse(option.hasArg());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.isValueSeparatorPresent());
        assertEquals(0, option.getArgs().length);
    }

    /**
     * Test creating an option with a required argument.
     */
    @Test
    public void testRequiredArg()
    {
        Option option = OptionBuilder.withLongOpt("testOption")
                                     .hasArg()
                                     .create();
        assertNotNull(option);
        assertEquals("testOption", option.getLongOpt());
        assertEquals("arg", option.getArgName());
        assertTrue(option.isRequired());
        assertTrue(option.hasArg());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.isValueSeparatorPresent());
        assertNull(option.getValue());
    }

    /**
     * Test creating an option with an optional argument.
     */
    @Test
    public void testOptionalArg()
    {
        Option option = OptionBuilder.withLongOpt("testOption")
                                     .hasOptionalArg()
                                     .create();
        assertNotNull(option);
        assertEquals("testOption", option.getLongOpt());
        assertEquals("arg", option.getArgName());
        assertFalse(option.isRequired());
        assertTrue(option.hasOptionalArg());
        assertNull(option.getValue());
    }

    /**
     * Test creating an option with multiple arguments.
     */
    @Test
    public void testMultipleArgs()
    {
        Option option = OptionBuilder.withLongOpt("testOption")
                                     .hasArgs(2)
                                     .create();
        assertNotNull(option);
        assertEquals("testOption", option.getLongOpt());
        assertEquals("arg1", option.getArgName());
        assertFalse(option.isRequired());
        assertFalse(option.hasOptionalArg());
        assertTrue(option.isValueSeparatorPresent());
        assertEquals(2, option.getArgs().length);
    }

    /**
     * Test creating an option with a type.
     */
    @Test
    public void testType()
    {
        Option option = OptionBuilder.withLongOpt("testOption")
                                     .withType(String.class)
                                     .create();
        assertNotNull(option);
        assertEquals("testOption", option.getLongOpt());
        assertNull(option.getArgName());
        assertFalse(option.isRequired());
        assertFalse(option.hasArg());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.isValueSeparatorPresent());
        assertEquals(Option.VALUE_TYPE_STRING, option.getType());
    }

    /**
     * Test creating an option with a description.
     */
    @Test
    public void testDescription()
    {
        Option option = OptionBuilder.withLongOpt("testOption")
                                     .withDescription("This is a test option")
                                     .create();
        assertNotNull(option);
        assertEquals("This is a test option", option.getDescription());
        assertEquals("testOption", option.getLongOpt());
        assertNull(option.getArgName());
        assertFalse(option.isRequired());
        assertFalse(option.hasArg());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.isValueSeparatorPresent());
        assertEquals(Option.VALUE_TYPE_NO_VALUE, option.getType());
    }
}