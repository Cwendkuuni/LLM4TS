package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test case for OptionBuilder class.
 */
public class OptionBuilderTest
{
    /**
     * Tests OptionBuilder withSimpleOption method.
     */
    @Test
    public void testWithSimpleOption()
    {
        Option option = OptionBuilder.withLongOpt("option1")
                                      .withDescription("A simple option")
                                      .create();

        assertEquals("option1", option.getLongOpt());
        assertEquals("A simple option", option.getDescription());
        assertNull(option.getValue());
        assertNull(option.getArgs());
        assertNull(option.getType());
    }

    /**
     * Tests OptionBuilder withArg method.
     */
    @Test
    public void testWithArg()
    {
        Option option = OptionBuilder.withLongOpt("option2")
                                      .hasArg()
                                      .withDescription("An option with an argument")
                                      .create();

        assertEquals("option2", option.getLongOpt());
        assertEquals("An option with an argument", option.getDescription());
        assertNull(option.getValue());
        assertEquals(1, option.getArgs().length);
    }

    /**
     * Tests OptionBuilder hasArg method with zero args.
     */
    @Test
    public void testHasArgZeroArgs()
    {
        Option option = OptionBuilder.withLongOpt("option3")
                                      .hasArg(false)
                                      .withDescription("An option with no argument")
                                      .create();

        assertEquals("option3", option.getLongOpt());
        assertEquals("An option with no argument", option.getDescription());
        assertNull(option.getValue());
        assertNull(option.getArgs());
    }

    /**
     * Tests OptionBuilder hasArg method with one arg.
     */
    @Test
    public void testHasArgOneArg()
    {
        Option option = OptionBuilder.withLongOpt("option4")
                                      .hasArg(true)
                                      .withDescription("An option with one argument")
                                      .create();

        assertEquals("option4", option.getLongOpt());
        assertEquals("An option with one argument", option.getDescription());
        assertNull(option.getValue());
        assertEquals(1, option.getArgs().length);
    }

    /**
     * Tests OptionBuilder hasArgs method.
     */
    @Test
    public void testHasArgs()
    {
        Option option = OptionBuilder.withLongOpt("option5")
                                      .hasArgs()
                                      .withDescription("An option with multiple arguments")
                                      .create();

        assertEquals("option5", option.getLongOpt());
        assertEquals("An option with multiple arguments", option.getDescription());
        assertNull(option.getValue());
        assertNull(option.getArgs()[0]);
    }

    /**
     * Tests OptionBuilder isRequired method.
     */
    @Test
    public void testIsRequired()
    {
        Option option = OptionBuilder.withLongOpt("option6")
                                      .isRequired()
                                      .withDescription("An required option")
                                      .create();

        assertEquals("option6", option.getLongOpt());
        assertEquals("An required option", option.getDescription());
        assertNull(option.getValue());
        assertNull(option.getArgs());
        assertTrue(option.isRequired());
    }

    /**
     * Tests OptionBuilder hasOptionalArg method.
     */
    @Test
    public void testHasOptionalArg()
    {
        Option option = OptionBuilder.withLongOpt("option7")
                                      .hasOptionalArg()
                                      .withDescription("An optional option with an argument")
                                      .create();

        assertEquals("option7", option.getLongOpt());
        assertEquals("An optional option with an argument", option.getDescription());
        assertNull(option.getValue());
        assertEquals(1, option.getArgs().length);
        assertTrue(option.hasOptionalArg());
    }

    /**
     * Tests OptionBuilder withValueSeparator method.
     */
    @Test
    public void testWithValueSeparator()
    {
        Option option = OptionBuilder.withLongOpt("option8")
                                      .withDescription("An option with a value separator")
                                      .withValueSeparator(':')
                                      .create();

        assertEquals("option8", option.getLongOpt());
        assertEquals("An option with a value separator", option.getDescription());
        assertNull(option.getValue());
        assertNull(option.getArgs());
    }

    /**
     * Tests OptionBuilder withValueSeparator method with default separator.
     */
    @Test
    public void testWithValueSeparatorDefault()
    {
        Option option = OptionBuilder.withLongOpt("option9")
                                      .withDescription("An option with a value separator")
                                      .create();

        assertEquals("option9", option.getLongOpt());
        assertEquals("An option with a value separator", option.getDescription());
        assertNull(option.getValue());
        assertNull(option.getArgs());
    }

    /**
     * Tests OptionBuilder create method with no arguments.
     */
    @Test
    public void testCreateNoArgs()
    {
        Option option = OptionBuilder.create();

        assertNull(option);
    }

    /**
     * Tests OptionBuilder create method with invalid long opt.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateInvalidLongOpt()
    {
        OptionBuilder.create();
    }
}