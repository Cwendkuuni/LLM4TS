package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class OptionBuilderTest
{
    @Test
    public void testBasicOption()
    {
        Option option = OptionBuilder.withLongOpt("optionA")
                .hasArg()
                .create('A');

        assertEquals("optionA", option.getLongOpt());
        assertEquals("an argument", option.getArgName());
        assertNull(option.getValue());
        assertEquals(1, option.getNumberOfArgs());
        assertEquals(false, option.isRequired());
        assertEquals(false, option.hasOptionalArg());
        assertEquals('A', option.getOpt());
    }

    @Test
    public void testOptionalArgOption()
    {
        Option option = OptionBuilder.withLongOpt("optionB")
                .hasOptionalArg()
                .create('B');

        assertEquals("optionB", option.getLongOpt());
        assertEquals("an argument", option.getArgName());
        assertNull(option.getValue());
        assertEquals(Option.UNINITIALIZED, option.getNumberOfArgs());
        assertEquals(false, option.isRequired());
        assertEquals(true, option.hasOptionalArg());
        assertEquals('B', option.getOpt());
    }

    @Test
    public void testValueSeparatedOption()
    {
        Option option = OptionBuilder.withLongOpt("optionC")
                .withValueSeparator(':')
                .create('C');

        assertEquals("optionC", option.getLongOpt());
        assertEquals("an argument", option.getArgName());
        assertNull(option.getValue());
        assertEquals(Option.UNINITIALIZED, option.getNumberOfArgs());
        assertEquals(false, option.isRequired());
        assertEquals(false, option.hasOptionalArg());
        assertEquals('C', option.getOpt());
    }

    @Test
    public void testRequiredArgOption()
    {
        Option option = OptionBuilder.withLongOpt("optionD")
                .isRequired()
                .create('D');

        assertEquals("optionD", option.getLongOpt());
        assertEquals("an argument", option.getArgName());
        assertNull(option.getValue());
        assertEquals(1, option.getNumberOfArgs());
        assertEquals(true, option.isRequired());
        assertEquals(false, option.hasOptionalArg());
        assertEquals('D', option.getOpt());
    }

    @Test
    public void testArgumentTypeOption()
    {
        Option option = OptionBuilder.withLongOpt("optionE")
                .withType(Integer.class)
                .create('E');

        assertEquals("optionE", option.getLongOpt());
        assertEquals("an argument", option.getArgName());
        assertNull(option.getValue());
        assertEquals(1, option.getNumberOfArgs());
        assertEquals(false, option.isRequired());
        assertEquals(false, option.hasOptionalArg());
        assertEquals('E', option.getOpt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingLongOpt()
    {
        OptionBuilder.create();
    }
}