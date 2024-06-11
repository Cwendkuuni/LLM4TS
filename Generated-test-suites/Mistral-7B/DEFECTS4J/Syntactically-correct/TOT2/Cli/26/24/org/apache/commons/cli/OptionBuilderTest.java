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
    /**
     * Test method for {@link OptionBuilder#withLongOpt(String)}
     */
    @Test
    public void testWithLongOpt()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        Option option = optionBuilder.withLongOpt("longOptTest").create();
        assertEquals("longOptTest", option.getLongOpt());
    }

    /**
     * Test method for {@link OptionBuilder#hasArg()}
     */
    @Test
    public void testHasArg()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        Option option = optionBuilder.hasArg().create();
        assertEquals(1, option.getArgs().length);
    }

    /**
     * Test method for {@link OptionBuilder#isRequired()}
     */
    @Test
    public void testIsRequired()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        Option option = optionBuilder.isRequired().create();
        assertFalse(option.isOptional());
    }

    /**
     * Test method for {@link OptionBuilder#hasOptionalArg()}
     */
    @Test
    public void testHasOptionalArg()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        Option option = optionBuilder.hasOptionalArg().create();
        assertTrue(option.isOptional());
    }

    /**
     * Test method for {@link OptionBuilder#hasArgs()}
     */
    @Test
    public void testHasArgs()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        Option option = optionBuilder.hasArgs().create();
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs().length);
    }

    /**
     * Test method for {@link OptionBuilder#hasArgs(int)}
     */
    @Test
    public void testHasArgsWithNumber()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        Option option = optionBuilder.hasArgs(3).create();
        assertEquals(3, option.getArgs().length);
    }

    /**
     * Test method for {@link OptionBuilder#withType(Class)}
     */
    @Test
    public void testWithType()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        Option option = optionBuilder.withType(Integer.class).create();
        assertEquals(Integer.class, option.getType());
    }

    /**
     * Test method for {@link OptionBuilder#withDescription(String)}
     */
    @Test
    public void testWithDescription()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        Option option = optionBuilder.withDescription("Test Description").create();
        assertEquals("Test Description", option.getDescription());
    }

    /**
     * Test method for {@link OptionBuilder#create()}
     */
    @Test
    public void testCreate()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        Option option = optionBuilder.create();
        assertNull(option.getLongOpt());
    }

    /**
     * Test method for {@link OptionBuilder#create(String)}
     */
    @Test
    public void testCreateWithLongOpt()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        Option option = optionBuilder.withLongOpt("longOptTest").create();
        assertEquals("longOptTest", option.getLongOpt());
    }

    /**
     * Test method for {@link OptionBuilder#create(String)} with invalid long opt
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithInvalidLongOpt()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        optionBuilder.create("1");
    }

    /**
     * Test method for {@link OptionBuilder#isRequired(boolean)}
     */
    @Test
    public void testIsRequiredWithBoolean()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        Option option = optionBuilder.isRequired(true).create();
        assertTrue(option.isRequired());
    }
}