package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class OptionBuilderTest
{
    @Test
    public void testBasicOption()
    {
        Option option = OptionBuilder.withLongOpt("testOption")
                .hasArg()
                .withArgName("argName")
                .create();

        assertEquals("testOption", option.getLongOpt());
        assertEquals("Arg name for testOption", option.getDescription());
        assertEquals("argName", option.getArgName());
        assertEquals(1, option.getArgCount());
        assertNull(option.getValue());

        option.setValue("testValue");
        assertEquals("testValue", option.getValue());
    }

    @Test
    public void testOptionalArgumentOption()
    {
        Option option = OptionBuilder.withLongOpt("testOption")
                .hasOptionalArg()
                .create();

        assertNull(option.getValue());
        assertEquals("testOption", option.getLongOpt());
        assertEquals(1, option.getArgCount());
        assertEquals(Option.UNINITIALIZED, option.getNumberOfArgs());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingLongOpt()
    {
        OptionBuilder.create();
    }

    @Test
    public void testMultipleArgumentsOption()
    {
        Option option = OptionBuilder.withLongOpt("testOption")
                .hasArgs(2)
                .create();

        assertEquals("testOption", option.getLongOpt());
        assertEquals("Test option which accepts two arguments", option.getDescription());
        assertEquals(2, option.getArgCount());
        assertEquals(Option.UNINITIALIZED, option.getValue());
    }

    @Test
    public void testValueSeparatorOption()
    {
        Option option = OptionBuilder.withLongOpt("testOption")
                .withValueSeparator('=')
                .create();

        assertEquals("testOption", option.getLongOpt());
        assertEquals("Test option which accepts an argument value that is separated by '='", option.getDescription());
        assertEquals(1, option.getArgCount());
        assertEquals("argValue=value", option.getValue());
    }

    @Test
    public void testRequiredOption()
    {
        Option option = OptionBuilder.withLongOpt("testOption")
                .isRequired()
                .create();

        assertEquals("testOption", option.getLongOpt());
        assertEquals("Test option which is required", option.getDescription());
        assertEquals("argName", option.getArgName());
        assertEquals(1, option.getArgCount());
        assertNull(option.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidNumberOfArguments()
    {
        OptionBuilder.hasArgs(3).create();
    }

    @Test
    public void testOptionWithType()
    {
        Option option = OptionBuilder.withLongOpt("testOption")
                .withType("java.lang.Integer")
                .create();

        assertEquals("testOption", option.getLongOpt());
        assertEquals("Test option which accepts an Integer as an argument", option.getDescription());
        assertEquals(1, option.getArgCount());
        assertEquals(Option.TYPE_INT, option.getType());
        assertNull(option.getValue());
    }
}