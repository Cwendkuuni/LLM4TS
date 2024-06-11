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
 * Testing the OptionBuilder class
 */
public class OptionBuilderTest
{
    /**
     * Testing the withLongOpt method
     */
    @Test
    public void testWithLongOpt()
    {
        OptionBuilder builder = OptionBuilder.withLongOpt("--option");
        Option option = builder.create();
        assertEquals("--option", option.getLongOpt());
    }

    /**
     * Testing the hasArg method
     */
    @Test
    public void testHasArg()
    {
        OptionBuilder builder = new OptionBuilder();
        builder = builder.hasArg();
        Option option = builder.create();
        assertEquals(Option.ONE_VALUE_OPTION, option.getOptType());
    }

    /**
     * Testing the hasOptionalArg method
     */
    @Test
    public void testHasOptionalArg()
    {
        OptionBuilder builder = new OptionBuilder();
        builder = builder.hasOptionalArg();
        Option option = builder.create();
        assertEquals(Option.NO_ARGUMENTS, option.getOptType());
        assertFalse(option.isRequired());
    }

    /**
     * Testing the hasArgs method
     */
    @Test
    public void testHasArgs()
    {
        OptionBuilder builder = new OptionBuilder();
        builder = builder.hasArgs();
        Option option = builder.create();
        assertEquals(Option.UNLIMITED_VALUES, option.getOptType());
    }

    /**
     * Testing the isRequired method
     */
    @Test
    public void testIsRequired()
    {
        OptionBuilder builder = new OptionBuilder();
        builder = builder.isRequired();
        Option option = builder.create();
        assertTrue(option.isRequired());
    }

    /**
     * Testing the withValueSeparator method
     */
    @Test
    public void testWithValueSeparator()
    {
        OptionBuilder builder = new OptionBuilder();
        builder = builder.withValueSeparator(':');
        Option option = builder.create();
        assertEquals(':', option.getValueSeparator());
    }

    /**
     * Testing the create method with no arguments
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateNoLongOpt()
    {
        OptionBuilder builder = new OptionBuilder();
        builder.create();
    }

    /**
     * Testing the create method with optional arg and no value
     */
    @Test
    public void testCreateOptionalNoValue()
    {
        OptionBuilder builder = new OptionBuilder();
        builder = builder.hasOptionalArg();
        Option option = builder.create();
        assertNull(option.getValue());
    }

    /**
     * Testing the create method with optional arg and a value
     */
    @Test
    public void testCreateOptionalValue()
    {
        OptionBuilder builder = new OptionBuilder();
        builder = builder.hasOptionalArg();
        Option option = builder.create();
        option.setValue("test");
        assertEquals("test", option.getValue());
    }

    /**
     * Testing the create method with required arg and no value
     * @throws IllegalArgumentException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateRequiredNoValue()
    {
        OptionBuilder builder = new OptionBuilder();
        builder = builder.isRequired();
        Option option = builder.create();
    }

    /**
     * Testing the create method with required arg and a value
     */
    @Test
    public void testCreateRequiredValue()
    {
        OptionBuilder builder = new OptionBuilder();
        builder = builder.isRequired();
        Option option = builder.create();
        option.setValue("test");
        assertEquals("test", option.getValue());
    }

    /**
     * Testing the create method with multiple arguments
     */
    @Test
    public void testCreateMultipleArgs()
    {
        OptionBuilder builder = new OptionBuilder();
        builder = builder.hasArgs();
        Option option = builder.create();
        option.setValue("test1 test2");
        String[] expected = {"test1", "test2"};
        assertArrayEquals(expected, option.getArgs());
    }

    /**
     * Testing the create method with type
     */
    @Test
    public void testCreateType()
    {
        OptionBuilder builder = new OptionBuilder();
        builder = builder.withType(String.class);
        Option option = builder.create();
        assertEquals(String.class, option.getValueType());
    }
}