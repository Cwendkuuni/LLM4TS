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
 * Test for OptionBuilder class.
 */
public class OptionBuilderTest
{
    /**
     * Test for OptionBuilder.withLongOpt(String newLongopt) method.
     */
    @Test
    public void testWithLongOpt()
    {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        optionBuilder.withLongOpt("optLong");

        Option option = optionBuilder.create('o');
        assertEquals("optLong", option.getLongOpt());
    }

    /**
     * Test for OptionBuilder.hasArg() method.
     */
    @Test
    public void testHasArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        optionBuilder.hasArg();

        Option option = optionBuilder.create('a');
        assertEquals(1, option.getArgs().length);
    }

    /**
     * Test for OptionBuilder.hasArg(boolean hasArg) method.
     */
    @Test
    public void testHasArgBoolean()
    {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        optionBuilder.hasArg(true);

        Option option = optionBuilder.create('b');
        assertEquals(1, option.getArgs().length);

        OptionBuilder optionBuilder2 = OptionBuilder.getInstance();
        optionBuilder2.hasArg(false);

        Option option2 = optionBuilder2.create('c');
        assertEquals(0, option2.getArgs().length);
    }

    /**
     * Test for OptionBuilder.withArgName(String name) method.
     */
    @Test
    public void testWithArgName()
    {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        optionBuilder.withArgName("argName");

        Option option = optionBuilder.create('x');
        assertEquals("argName", option.getArgName());
    }

    /**
     * Test for OptionBuilder.isRequired() method.
     */
    @Test
    public void testIsRequired()
    {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        optionBuilder.isRequired();

        Option option = optionBuilder.create('r');
        assertTrue(option.isRequired());

        Option option2 = OptionBuilder.getInstance().create('s');
        assertFalse(option2.isRequired());
    }

    /**
     * Test for OptionBuilder.withValueSeparator(char sep) method.
     */
    @Test
    public void testWithValueSeparator()
    {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        optionBuilder.withValueSeparator(':');

        Option option = optionBuilder.create('v');
        assertEquals(':', option.getValueSeparator());

        OptionBuilder optionBuilder2 = OptionBuilder.getInstance();
        optionBuilder2.withValueSeparator();

        Option option2 = optionBuilder2.create('w');
        assertEquals('=', option2.getValueSeparator());
    }

    /**
     * Test for OptionBuilder.isRequired(boolean newRequired) method.
     */
    @Test
    public void testIsRequiredBoolean()
    {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        optionBuilder.isRequired(true);

        Option option = optionBuilder.create('t');
        assertTrue(option.isRequired());

        OptionBuilder optionBuilder2 = OptionBuilder.getInstance();
        optionBuilder2.isRequired(false);

        Option option2 = optionBuilder2.create('u');
        assertFalse(option2.isRequired());
    }

    /**
     * Test for OptionBuilder.hasArgs() method.
     */
    @Test
    public void testHasArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        optionBuilder.hasArgs();

        Option option = optionBuilder.create('z');
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs().length);
    }

    /**
     * Test for OptionBuilder.hasArgs(int num) method.
     */
    @Test
    public void testHasArgsInt()
    {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        optionBuilder.hasArgs(2);

        Option option = optionBuilder.create('y');
        assertEquals(2, option.getArgs().length);

        OptionBuilder optionBuilder2 = OptionBuilder.getInstance();
        optionBuilder2.hasArgs(0);

        Option option2 = optionBuilder2.create('e');
        assertEquals(0, option2.getArgs().length);
    }

    /**
     * Test for OptionBuilder.hasOptionalArg() method.
     */
    @Test
    public void testHasOptionalArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        optionBuilder.hasOptionalArg();

        Option option = optionBuilder.create('q');
        assertEquals(1, option.getArgs().length);
        assertTrue(option.optionalArg);

        OptionBuilder optionBuilder2 = OptionBuilder.getInstance();
        optionBuilder2.hasOptionalArgs();

        Option option2 = optionBuilder2.create('f');
        assertEquals(Option.UNLIMITED_VALUES, option2.getArgs().length);
        assertTrue(option2.optionalArg);
    }

    /**
     * Test for OptionBuilder.withType(Class type) method.
     */
    @Test
    public void testWithType()
    {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        optionBuilder.withType(Integer.TYPE);

        Option option = optionBuilder.create('i');
        assertEquals(Integer.TYPE, option.getType());

        OptionBuilder optionBuilder2 = OptionBuilder.getInstance();
        optionBuilder2.withType(String.class);

        Option option2 = optionBuilder2.create('s');
        assertEquals(String.class, option2.getType());
    }

    /**
     * Test for OptionBuilder.withDescription(String description) method.
     */
    @Test
    public void testWithDescription()
    {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        optionBuilder.withDescription("test description");

        Option option = optionBuilder.create('g');
        assertSame("test description", option.getDescription());

        OptionBuilder optionBuilder2 = OptionBuilder.getInstance();
        optionBuilder2.withDescription(null);

        Option option2 = optionBuilder2.create('h');
        assertNull(option2.getDescription());
    }

    /**
     * Test for OptionBuilder.create() method with no arguments.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateNoArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        optionBuilder.create();
    }

    /**
     * Test for OptionBuilder.create() method with longOpt not set.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateNoLongOpt()
    {
        OptionBuilder optionBuilder = OptionBuilder.getInstance();
        optionBuilder.create('j');
    }
}