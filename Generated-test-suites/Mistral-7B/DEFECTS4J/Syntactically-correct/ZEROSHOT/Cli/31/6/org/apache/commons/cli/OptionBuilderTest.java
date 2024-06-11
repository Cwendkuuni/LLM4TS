package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;

import org.junit.Test;

/**
 * Test class for OptionBuilder
 */
public class OptionBuilderTest
{
    @Test
    public void testOptionBuilderReset()
    {
        OptionBuilder.reset();

        assertNull(OptionBuilder.description);
        assertNull(OptionBuilder.longopt);
        assertNull(OptionBuilder.argName);
        assertNull(OptionBuilder.type);
        assertFalse(OptionBuilder.required);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
        assertEquals(0, OptionBuilder.valuesep);
    }

    @Test
    public void testOptionBuilderWithLongOpt()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longopt");

        assertEquals("longopt", OptionBuilder.longopt);
        assertNull(OptionBuilder.description);
        assertNull(OptionBuilder.argName);
        assertNull(OptionBuilder.type);
        assertFalse(OptionBuilder.required);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
        assertEquals(0, OptionBuilder.valuesep);
    }

    @Test
    public void testOptionBuilderHasArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();

        assertEquals(Option.UNINITIALIZED, OptionBuilder.longopt);
        assertNull(OptionBuilder.description);
        assertEquals("arg", OptionBuilder.argName);
        assertNull(OptionBuilder.type);
        assertTrue(OptionBuilder.required);
        assertEquals(1, OptionBuilder.numberOfArgs);
        assertEquals(0, OptionBuilder.valuesep);
    }

    @Test
    public void testOptionBuilderHasArgWithArgName()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg().withArgName("argName");

        assertEquals(Option.UNINITIALIZED, OptionBuilder.longopt);
        assertNull(OptionBuilder.description);
        assertEquals("argName", OptionBuilder.argName);
        assertNull(OptionBuilder.type);
        assertTrue(OptionBuilder.required);
        assertEquals(1, OptionBuilder.numberOfArgs);
        assertEquals(0, OptionBuilder.valuesep);
    }

    @Test
    public void testOptionBuilderIsRequired()
    {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();

        assertEquals(Option.UNINITIALIZED, OptionBuilder.longopt);
        assertNull(OptionBuilder.description);
        assertNull(OptionBuilder.argName);
        assertNull(OptionBuilder.type);
        assertTrue(OptionBuilder.required);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
        assertEquals(0, OptionBuilder.valuesep);
    }

    @Test
    public void testOptionBuilderHasArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();

        assertEquals(Option.UNINITIALIZED, OptionBuilder.longopt);
        assertNull(OptionBuilder.description);
        assertNull(OptionBuilder.argName);
        assertNull(OptionBuilder.type);
        assertFalse(OptionBuilder.required);
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
        assertEquals(0, OptionBuilder.valuesep);
    }

    @Test
    public void testOptionBuilderHasArgsWithValueSeparator()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs().withValueSeparator(':');

        assertEquals(Option.UNINITIALIZED, OptionBuilder.longopt);
        assertNull(OptionBuilder.description);
        assertNull(OptionBuilder.argName);
        assertNull(OptionBuilder.type);
        assertFalse(OptionBuilder.required);
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
        assertEquals(':', OptionBuilder.valuesep);
    }

    @Test
    public void testOptionBuilderHasOptionalArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();

        assertEquals(Option.UNINITIALIZED, OptionBuilder.longopt);
        assertNull(OptionBuilder.description);
        assertNull(OptionBuilder.argName);
        assertNull(OptionBuilder.type);
        assertFalse(OptionBuilder.required);
        assertEquals(1, OptionBuilder.numberOfArgs);
        assertEquals(0, OptionBuilder.valuesep);
        assertTrue(OptionBuilder.optionalArg);
    }

    @Test
    public void testOptionBuilderWithType()
    {
        OptionBuilder optionBuilder = OptionBuilder.withType(Integer.class);

        assertEquals(Option.UNINITIALIZED, OptionBuilder.longopt);
        assertNull(OptionBuilder.description);
        assertNull(OptionBuilder.argName);
        assertEquals(Integer.class, OptionBuilder.type);
        assertFalse(OptionBuilder.required);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
        assertEquals(0, OptionBuilder.valuesep);
    }

    @Test
    public void testOptionBuilderWithDescription()
    {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("description");

        assertEquals(Option.UNINITIALIZED, OptionBuilder.longopt);
        assertEquals("description", OptionBuilder.description);
        assertNull(OptionBuilder.argName);
        assertNull(OptionBuilder.type);
        assertFalse(OptionBuilder.required);
        assertEquals(Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
        assertEquals(0, OptionBuilder.valuesep);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt()
    {
        OptionBuilder.reset();
        Option option = OptionBuilder.create();
    }

    @Test
    public void testCreateWithLongOpt()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longopt");
        Option option = optionBuilder.create();

        assertEquals("longopt", option.getLongOpt());
        assertNull(option.getDescription());
        assertNull(option.getArgName());
        assertNull(option.getType());
        assertFalse(option.isRequired());
        assertNull(option.getArgs());
        assertNull(option.getValueSeparator());
    }

    @Test
    public void testCreateWithLongOptAndDescription()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longopt")
                .withDescription("description");
        Option option = optionBuilder.create();

        assertEquals("longopt", option.getLongOpt());
        assertEquals("description", option.getDescription());
        assertNull(option.getArgName());
        assertNull(option.getType());
        assertFalse(option.isRequired());
        assertNull(option.getArgs());
        assertNull(option.getValueSeparator());
    }

    @Test
    public void testCreateWithLongOptArgName()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longopt")
                .withArgName("arg");
        Option option = optionBuilder.create();

        assertEquals("longopt", option.getLongOpt());
        assertNull(option.getDescription());
        assertEquals("arg", option.getArgName());
        assertNull(option.getType());
        assertFalse(option.isRequired());
        assertNull(option.getArgs());
        assertNull(option.getValueSeparator());
    }

    @Test
    public void testCreateWithLongOptDescriptionArgName()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longopt")
                .withDescription("description")
                .withArgName("arg");
        Option option = optionBuilder.create();

        assertEquals("longopt", option.getLongOpt());
        assertEquals("description", option.getDescription());
        assertEquals("arg", option.getArgName());
        assertNull(option.getType());
        assertFalse(option.isRequired());
        assertNull(option.getArgs());
        assertNull(option.getValueSeparator());
    }

    @Test
    public void testCreateWithLongOptArgNameType()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longopt")
                .withArgName("arg")
                .withType(Integer.class);
        Option option = optionBuilder.create();

        assertEquals("longopt", option.getLongOpt());
        assertNull(option.getDescription());
        assertEquals("arg", option.getArgName());
        assertEquals(Integer.class, option.getType());
        assertFalse(option.isRequired());
        assertNull(option.getArgs());
        assertNull(option.getValueSeparator());
    }

    @Test
    public void testCreateWithLongOptDescriptionArgNameTypeAndArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longopt")
                .withDescription("description")
                .withArgName("arg")
                .withType(Integer.class)
                .hasArgs();
        Option option = optionBuilder.create();

        assertEquals("longopt", option.getLongOpt());
        assertEquals("description", option.getDescription());
        assertEquals("arg", option.getArgName());
        assertEquals(Integer.class, option.getType());
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
        assertNull(option.getValueSeparator());
    }

    @Test
    public void testCreateWithLongOptDescriptionArgNameTypeAndArgsAndValueSeparator()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longopt")
                .withDescription("description")
                .withArgName("arg")
                .withType(Integer.class)
                .hasArgs()
                .withValueSeparator(':');
        Option option = optionBuilder.create();

        assertEquals("longopt", option.getLongOpt());
        assertEquals("description", option.getDescription());
        assertEquals("arg", option.getArgName());
        assertEquals(Integer.class, option.getType());
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
        assertEquals(':', option.getValueSeparator());
    }

    @Test
    public void testCreateWithLongOptDescriptionArgNameTypeAndOptionalArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longopt")
                .withDescription("description")
                .withArgName("arg")
                .withType(Integer.class)
                .hasOptionalArg();
        Option option = optionBuilder.create();

        assertEquals("longopt", option.getLongOpt());
        assertEquals("description", option.getDescription());
        assertEquals("arg", option.getArgName());
        assertEquals(Integer.class, option.getType());
        assertFalse(option.isRequired());
        assertEquals(1, option.getArgs().length);
        assertNull(option.getValueSeparator());
    }
}