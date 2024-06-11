package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class OptionBuilderTest
{

    @Test
    public void testOptionBuilderWithLongOpt()
    {
        Option option = OptionBuilder.withLongOpt("longOpt")
                .create();
        assertNotNull(option);
        assertEquals("longOpt", option.getLongOpt());
    }

    @Test
    public void testOptionBuilderHasArg()
    {
        Option option = OptionBuilder.hasArg()
                .create();
        assertNotNull(option);
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        option = OptionBuilder.hasArg(true)
                .create();
        assertEquals(1, option.getArgs());
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
    public void testOptionBuilderIsRequired()
    {
        Option option = OptionBuilder.isRequired()
                .create();
        assertNotNull(option);
        assertEquals(true, option.isRequired());
    }

    @Test
    public void testOptionBuilderWithValueSeparator()
    {
        Option option = OptionBuilder.withValueSeparator(':')
                .create();
        assertNotNull(option);
        assertEquals(':', option.getValueSeparator());
    }

    @Test
    public void testOptionBuilderHasArgs()
    {
        Option option = OptionBuilder.hasArgs()
                .create();
        assertNotNull(option);
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
    }

    @Test
    public void testOptionBuilderHasArgsWithNum()
    {
        Option option = OptionBuilder.hasArgs(3)
                .create();
        assertNotNull(option);
        assertEquals(3, option.getArgs());
    }

    @Test
    public void testOptionBuilderHasOptionalArg()
    {
        Option option = OptionBuilder.hasOptionalArg()
                .create();
        assertNotNull(option);
        assertEquals(true, option.isOptional());
    }

    @Test
    public void testOptionBuilderHasOptionalArgs()
    {
        Option option = OptionBuilder.hasOptionalArgs()
                .create();
        assertNotNull(option);
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
        assertEquals(true, option.isOptional());
    }

    @Test
    public void testOptionBuilderHasOptionalArgsWithNum()
    {
        Option option = OptionBuilder.hasOptionalArgs(2)
                .create();
        assertNotNull(option);
        assertEquals(2, option.getArgs());
        assertEquals(true, option.isOptional());
    }

    @Test
    public void testOptionBuilderWithType()
    {
        Option option = OptionBuilder.withType(String.class)
                .create();
        assertNotNull(option);
        assertEquals(String.class, option.getType());
    }

    @Test
    public void testOptionBuilderWithDescription()
    {
        Option option = OptionBuilder.withDescription("description")
                .create();
        assertNotNull(option);
        assertEquals("description", option.getDescription());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionBuilderNoLongOpt()
    {
        OptionBuilder.reset();
        Option option = OptionBuilder.create();
        throw new AssertionError("should throw IllegalArgumentException");
    }
}