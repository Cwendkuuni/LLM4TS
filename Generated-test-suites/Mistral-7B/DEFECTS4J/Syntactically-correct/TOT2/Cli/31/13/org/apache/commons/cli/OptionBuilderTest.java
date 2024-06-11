package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class OptionBuilderTest
{
    @Test
    public void testOptionBuilderWithLongOpt()
    {
        Option option = OptionBuilder.withLongOpt("longopt")
                                      .hasArg()
                                      .create();
        assertEquals("longopt", option.getLongOpt());
        assertEquals("", option.getOpt());
        assertEquals("", option.getDescription());
        assertEquals(true, option.isRequired());
        assertEquals(1, option.getArgPosition());
        assertNull(option.getValue());
    }

    @Test
    public void testOptionBuilderWithOpt()
    {
        Option option = OptionBuilder.create('opt');
        assertEquals("opt", option.getOpt());
        assertNull(option.getLongOpt());
        assertEquals("", option.getDescription());
        assertEquals(false, option.isRequired());
        assertEquals(-1, option.getArgPosition());
        assertNull(option.getValue());
    }

    @Test
    public void testOptionBuilderWithLongOptAndDescription()
    {
        Option option = OptionBuilder.withLongOpt("longopt")
                                      .withDescription("Description for longopt")
                                      .create();
        assertEquals("longopt", option.getLongOpt());
        assertEquals("", option.getOpt());
        assertEquals("Description for longopt", option.getDescription());
        assertEquals(false, option.isRequired());
        assertEquals(-1, option.getArgPosition());
        assertNull(option.getValue());
    }

    @Test
    public void testOptionBuilderWithLongOptAndDescriptionAndArgName()
    {
        Option option = OptionBuilder.withLongOpt("longopt")
                                      .withArgName("argName")
                                      .withDescription("Description for longopt")
                                      .create();
        assertEquals("longopt", option.getLongOpt());
        assertEquals("argName", option.getArgName());
        assertEquals("Description for longopt", option.getDescription());
        assertEquals(false, option.isRequired());
        assertEquals(-1, option.getArgPosition());
        assertNull(option.getValue());
    }

    @Test
    public void testOptionBuilderWithLongOptAndDescriptionAndArgNameAndHasArg()
    {
        Option option = OptionBuilder.withLongOpt("longopt")
                                      .withArgName("argName")
                                      .withDescription("Description for longopt")
                                      .hasArg()
                                      .create();
        assertEquals("longopt", option.getLongOpt());
        assertEquals("argName", option.getArgName());
        assertEquals("Description for longopt", option.getDescription());
        assertEquals(true, option.isRequired());
        assertEquals(1, option.getArgPosition());
        assertNull(option.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionBuilderWithoutLongOpt()
    {
        OptionBuilder.create();
    }
}