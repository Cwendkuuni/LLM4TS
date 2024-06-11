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
 * Test class for OptionBuilder.
 */
public class OptionBuilderTest
{
    /**
     * Test for withLongOpt method.
     */
    @Test
    public void testWithLongOpt()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder instance = optionBuilder.withLongOpt("longOpt1");

        assertEquals("longOpt1", instance.longopt);

        Option opt = instance.create();
        assertEquals("longOpt1", opt.getLongOpt());
    }

    /**
     * Test for hasArg method.
     */
    @Test
    public void testHasArg()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder instance = optionBuilder.hasArg();

        assertEquals(1, instance.numberOfArgs);

        Option opt = instance.create();
        assertEquals(Option.VALUE_REQUIRED, opt.getArgName().length());
    }

    /**
     * Test for isRequired method.
     */
    @Test
    public void testIsRequired()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder instance = optionBuilder.isRequired();

        assertTrue(instance.required);

        Option opt = instance.create();
        assertTrue(opt.isRequired());
    }

    /**
     * Test for hasOptionalArg method.
     */
    @Test
    public void testHasOptionalArg()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder instance = optionBuilder.hasOptionalArg();

        assertTrue(instance.optionalArg);
        assertEquals(1, instance.numberOfArgs);

        Option opt = instance.create();
        assertEquals(Option.VALUE_OPTIONAL, opt.getArgName().length());
    }

    /**
     * Test for hasArgs method.
     */
    @Test
    public void testHasArgs()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder instance = optionBuilder.hasArgs();

        assertEquals(Option.UNLIMITED_VALUES, instance.numberOfArgs);

        Option opt = instance.create();
        assertEquals(Option.UNLIMITED_VALUES, opt.getArgs().length());
    }

    /**
     * Test for hasArgs method with a specified number of arguments.
     */
    @Test
    public void testHasArgsWithNumArgs()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder instance = optionBuilder.hasArgs(2);

        assertEquals(2, instance.numberOfArgs);

        Option opt = instance.create();
        assertEquals(2, opt.getArgs().length);
    }

    /**
     * Test for hasOptionalArgs method.
     */
    @Test
    public void testHasOptionalArgs()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder instance = optionBuilder.hasOptionalArgs();

        assertEquals(Option.UNLIMITED_VALUES, instance.numberOfArgs);
        assertTrue(instance.optionalArg);

        Option opt = instance.create();
        assertEquals(Option.VALUE_OPTIONAL, opt.getArgName().length());
    }

    /**
     * Test for hasOptionalArgs method with a specified number of arguments.
     */
    @Test
    public void testHasOptionalArgsWithNumArgs()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder instance = optionBuilder.hasOptionalArgs(3);

        assertEquals(3, instance.numberOfArgs);
        assertTrue(instance.optionalArg);

        Option opt = instance.create();
        assertEquals(Option.VALUE_OPTIONAL, opt.getArgName().length());
    }

    /**
     * Test for withType method.
     */
    @Test
    public void testWithType()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        Integer type = 1;
        OptionBuilder instance = optionBuilder.withType(type);

        assertEquals(type, instance.type);

        Option opt = instance.create();
        assertEquals(type.getClass(), opt.getValue().getClass());
    }

    /**
     * Test for withDescription method.
     */
    @Test
    public void testWithDescription()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        String description = "description";
        OptionBuilder instance = optionBuilder.withDescription(description);

        assertEquals(description, instance.description);

        Option opt = instance.create();
        assertEquals(description, opt.getDescription());
    }

    /**
     * Test for create method with no long option.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithNoLongOpt()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder instance = optionBuilder;

        instance.create();
    }
}