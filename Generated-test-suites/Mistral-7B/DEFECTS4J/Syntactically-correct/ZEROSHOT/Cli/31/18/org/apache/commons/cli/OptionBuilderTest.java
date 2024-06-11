package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test class for OptionBuilder.
 */
public class OptionBuilderTest
{

    @Test
    public void testOptionBuilderWithLongOpt()
    {
        Option option = OptionBuilder.withLongOpt("myLongOpt")
                .create();
        assertNotNull(option);
        assertEquals("myLongOpt", option.getLongOpt());
    }

    @Test
    public void testOptionBuilderHasArg()
    {
        Option option = OptionBuilder.hasArg()
                .create();
        assertNotNull(option);
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        assertEquals(false, option.isRequired());
    }

    @Test
    public void testOptionBuilderIsRequired()
    {
        Option option = OptionBuilder.isRequired()
                .create();
        assertNotNull(option);
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        assertEquals(true, option.isRequired());
    }

    @Test
    public void testOptionBuilderHasArgs()
    {
        Option option = OptionBuilder.hasArgs()
                .create();
        assertNotNull(option);
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
        assertEquals(false, option.isRequired());
    }

    @Test
    public void testOptionBuilderHasOptionalArg()
    {
        Option option = OptionBuilder.hasOptionalArg()
                .create();
        assertNotNull(option);
        assertEquals(1, option.getArgs());
        assertEquals(true, option.isOptional());
    }

    @Test
    public void testOptionBuilderWithArgName()
    {
        Option option = OptionBuilder.withArgName("argName")
                .create();
        assertNotNull(option);
        assertEquals("argName", option.getArgName());
    }

    @Test
    public void testOptionBuilderWithValueSeparator()
    {
        Option option = OptionBuilder.withValueSeparator(':')
                .create('D');
        assertNotNull(option);
        assertEquals(':', option.getValueSeparator());
    }

    @Test
    public void testOptionBuilderHasArgsWithNumber()
    {
        Option option = OptionBuilder.hasArgs(3)
                .create();
        assertNotNull(option);
        assertEquals(3, option.getArgs());
    }

    @Test
    public void testOptionBuilderWithType()
    {
        Option<Integer> option = OptionBuilder.withType(Integer.class)
                .create();
        assertNotNull(option);
        assertEquals(Integer.class, option.getType());
    }

    @Test
    public void testOptionBuilderWithDescription()
    {
        Option option = OptionBuilder.withDescription("description")
                .create();
        assertNotNull(option);
        assertEquals("description", option.getDescription());
    }
}