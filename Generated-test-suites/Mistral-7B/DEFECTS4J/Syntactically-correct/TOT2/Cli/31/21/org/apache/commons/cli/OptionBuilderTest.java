package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link OptionBuilder}.
 */
public class OptionBuilderTest
{
    // Basic test cases for each method

    @Test
    public void testWithLongOpt()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder instance = optionBuilder.withLongOpt("longOpt");

        assertEquals("longOpt", instance.getLongOpt());
    }

    @Test
    public void testHasArg()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder instance = optionBuilder.hasArg();

        assertEquals(1, instance.getNumberOfArgs());
    }

    @Test
    public void testHasArgBoolean()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder instance = optionBuilder.hasArg(true);

        assertEquals(1, instance.getNumberOfArgs());
        assertTrue(instance.isOptionalArg());
    }

    @Test
    public void testWithArgName()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder instance = optionBuilder.withArgName("argName");

        assertEquals("argName", instance.getArgName());
    }

    @Test
    public void testIsRequired()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder instance = optionBuilder.isRequired();

        assertTrue(instance.isRequired());
    }

    @Test
    public void testWithValueSeparator()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder instance = optionBuilder.withValueSeparator(':');

        assertEquals(':', instance.getValueSeparator());
    }

    @Test
    public void testHasArgs()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder instance = optionBuilder.hasArgs();

        assertEquals(Option.UNLIMITED_VALUES, instance.getNumberOfArgs());
    }

    @Test
    public void testHasArgsInt()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder instance = optionBuilder.hasArgs(2);

        assertEquals(2, instance.getNumberOfArgs());
    }

    @Test
    public void testHasOptionalArg()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder instance = optionBuilder.hasOptionalArg();

        assertTrue(instance.isOptionalArg());
    }

    @Test
    public void testHasOptionalArgs()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder instance = optionBuilder.hasOptionalArgs();

        assertEquals(Option.UNLIMITED_VALUES, instance.getNumberOfArgs());
        assertTrue(instance.isOptionalArg());
    }

    @Test
    public void testWithType()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder instance = optionBuilder.withType(Integer.class);

        assertEquals(Integer.class, instance.getType());
    }

    @Test
    public void testWithDescription()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder instance = optionBuilder.withDescription("Description");

        assertEquals("Description", instance.getDescription());
    }

    // Edge cases and exception handling test cases

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithNoLongOpt()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder.reset();
        OptionBuilder.instance = null;

        OptionBuilder.create();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithInvalidLongOpt()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder.reset();
        OptionBuilder.instance = new OptionBuilder();

        OptionBuilder.create('a');
    }

    @Test
    public void testCreateWithNoDescription()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder.reset();

        Option opt = OptionBuilder.create('o');
        assertNull(opt.getDescription());
    }

    @Test
    public void testCreateWithEmptyDescription()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder.reset();

        Option opt = OptionBuilder.withDescription("")
                                .create('o');
        assertNull(opt.getDescription());
    }

    @Test
    public void testCreateWithNullDescription()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder.reset();

        Option opt = OptionBuilder.withDescription(null)
                                .create('o');
        assertNull(opt.getDescription());
    }

    @Test
    public void testCreateWithLongOptAndDescription()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder.reset();

        Option opt = OptionBuilder.withLongOpt("longOpt")
                                .withDescription("Description")
                                .create('o');

        assertEquals("Description", opt.getDescription());
        assertEquals("longOpt", opt.getLongOpt());
    }

    @Test
    public void testCreateWithInvalidArgName()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder.reset();

        Option opt = OptionBuilder.create('o');
        assertNull(opt.getArgName());
    }

    @Test
    public void testCreateWithEmptyArgName()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder.reset();
        OptionBuilder.instance = new OptionBuilder();

        Option opt = OptionBuilder.withArgName("")
                                .create('o');
        assertNull(opt.getArgName());
    }

    @Test
    public void testCreateWithNullArgName()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder.reset();

        Option opt = OptionBuilder.withArgName(null)
                                .create('o');
        assertNull(opt.getArgName());
    }

    @Test
    public void testCreateWithLongOptArgNameAndDescription()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder.reset();

        Option opt = OptionBuilder.withLongOpt("longOpt")
                                .withArgName("argName")
                                .withDescription("Description")
                                .create('o');

        assertEquals("argName", opt.getArgName());
        assertEquals("Description", opt.getDescription());
        assertEquals("longOpt", opt.getLongOpt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithInvalidNumberOfArgs()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder.reset();
        OptionBuilder.instance = new OptionBuilder();

        OptionBuilder.create(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithNegativeNumberOfArgs()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder.reset();
        OptionBuilder.instance = new OptionBuilder();

        OptionBuilder.create(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithNumberOfArgsGreaterThanExpected()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder.reset();
        OptionBuilder.instance = new OptionBuilder();

        OptionBuilder.create(3);
    }

    @Test
    public void testCreateWithNoArgName()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder.reset();
        OptionBuilder.instance = new OptionBuilder();

        Option opt = OptionBuilder.create('o');
        assertNull(opt.getArgName());
    }

    @Test
    public void testCreateWithOptionalArgFalse()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder.reset();

        Option opt = OptionBuilder.create('o');
        assertFalse(opt.isOptionalArg());
    }

    @Test
    public void testCreateWithOptionalArgTrue()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder.reset();

        Option opt = OptionBuilder.hasOptionalArg()
                                .create('o');
        assertTrue(opt.isOptionalArg());
    }

    @Test
    public void testCreateWithValueSeparator()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder.reset();

        Option opt = OptionBuilder.create('o');
        assertNull(opt.getValueSeparator());
    }

    @Test
    public void testCreateWithValueSeparatorChar()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder.reset();

        Option opt = OptionBuilder.withValueSeparator('=')
                                .create('o');
        assertEquals('=', opt.getValueSeparator());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithInvalidValueSeparator()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder.reset();

        OptionBuilder.create('o', (Object)'a');
    }

    @Test
    public void testCreateWithType()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder.reset();

        Option opt = OptionBuilder.create('o');
        assertNull(opt.getType());
    }

    @Test
    public void testCreateWithTypeClass()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder.reset();

        Option opt = OptionBuilder.withType(Integer.class)
                                .create('o');

        assertEquals(Integer.class, opt.getType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithInvalidType()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder.reset();

        OptionBuilder.create('o', (Object)"String");
    }
}