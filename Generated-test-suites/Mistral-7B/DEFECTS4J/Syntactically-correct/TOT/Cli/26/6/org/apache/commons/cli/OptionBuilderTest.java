package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;

import org.junit.Test;

/**
 * Test cases for OptionBuilder class.
 */
public class OptionBuilderTest
{
    /**
     * Test creating an Option with no arguments.
     */
    @Test
    public void testCreateNoArgs()
    {
        Option option = OptionBuilder.withLongOpt("testOption")
                                      .create();
        assertSame(Option.UNINITIALIZED, option.getArgs());
        assertFalse(option.hasArg());
        assertFalse(option.isRequired());
        assertNull(option.getValue());
        assertEquals("testOption", option.getLongOpt());
        assertEquals("Test Option", option.getDescription());
    }

    /**
     * Test creating an Option with one required argument.
     */
    @Test
    public void testCreateOneArgRequired()
    {
        Option option = OptionBuilder.withLongOpt("testOption")
                                      .hasArg()
                                      .create();
        assertEquals(1, option.getArgs());
        assertTrue(option.hasArg());
        assertFalse(option.isRequired());
        assertNull(option.getValue());
        assertEquals("testOption", option.getLongOpt());
        assertEquals("Test Option", option.getDescription());
    }

    /**
     * Test creating an Option with one optional argument.
     */
    @Test
    public void testCreateOneArgOptional()
    {
        Option option = OptionBuilder.withLongOpt("testOption")
                                      .hasOptionalArg()
                                      .create();
        assertEquals(1, option.getArgs());
        assertTrue(option.hasOptionalArg());
        assertFalse(option.isRequired());
        assertNull(option.getValue());
        assertEquals("testOption", option.getLongOpt());
        assertEquals("Test Option", option.getDescription());
    }

    /**
     * Test creating an Option with multiple optional arguments.
     */
    @Test
    public void testCreateManyArgsOptional()
    {
        Option option = OptionBuilder.withLongOpt("testOption")
                                      .hasOptionalArgs(2)
                                      .create();
        assertEquals(2, option.getArgs());
        assertTrue(option.hasOptionalArgs());
        assertFalse(option.isRequired());
        assertNull(option.getValue());
        assertEquals("testOption", option.getLongOpt());
        assertEquals("Test Option", option.getDescription());
    }

    /**
     * Test creating an Option with a required argument of a specific type.
     */
    @Test
    public void testCreateArgType()
    {
        Option option = OptionBuilder.withLongOpt("testOption")
                                      .hasArg()
                                      .withType(String.class)
                                      .create();
        assertEquals(1, option.getArgs());
        assertTrue(option.hasArg());
        assertFalse(option.isRequired());
        assertNull(option.getValue());
        assertEquals("testOption", option.getLongOpt());
        assertEquals("Test Option", option.getDescription());
        assertEquals(String.class, option.getType());
    }

    /**
     * Test creating an Option with a value separator.
     */
    @Test
    public void testCreateValueSeparator()
    {
        Option option = OptionBuilder.withLongOpt("testOption")
                                      .withValueSeparator(':')
                                      .hasArg()
                                      .create();
        assertEquals(1, option.getArgs());
        assertTrue(option.hasArg());
        assertFalse(option.isRequired());
        assertNull(option.getValue());
        assertEquals("testOption", option.getLongOpt());
        assertEquals("Test Option", option.getDescription());
        assertEquals(':', option.getValueSeparator());
    }

    /**
     * Test creating an Option with a required argument and a value separator.
     */
    @Test
    public void testCreateArgTypeValueSeparator()
    {
        Option option = OptionBuilder.withLongOpt("testOption")
                                      .isRequired()
                                      .hasArg()
                                      .withValueSeparator(':')
                                      .withType(String[].class)
                                      .create();
        assertEquals(1, option.getArgs());
        assertTrue(option.isRequired());
        assertTrue(option.hasArg());
        assertNull(option.getValue());
        assertEquals("testOption", option.getLongOpt());
        assertEquals("Test Option", option.getDescription());
        assertEquals(':', option.getValueSeparator());
        assertEquals(String[].class, option.getType());
    }
}