package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test for OptionBuilder class.
 */
public class OptionBuilderTest
{
    @Test
    public void testSimpleOption()
    {
        Option option = OptionBuilder.withLongOpt("optionA")
                .withDescription("This is an option A")
                .create();

        assertEquals("optionA", option.getLongOpt());
        assertEquals("This is an option A", option.getDescription());
        assertFalse(option.isRequired());
        assertNull(option.getValue());
    }

    @Test
    public void testRequiredOption()
    {
        Option option = OptionBuilder.withLongOpt("optionB")
                .withDescription("This is an option B")
                .isRequired()
                .create();

        assertEquals("optionB", option.getLongOpt());
        assertEquals("This is an option B", option.getDescription());
        assertTrue(option.isRequired());
        assertNull(option.getValue());

        try {
            OptionBuilder.create();
            fail("An exception should have been thrown because the option is required and no long opt has been specified.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testHasArgOption()
    {
        Option option = OptionBuilder.withLongOpt("optionC")
                .withDescription("This is an option C with an argument")
                .hasArg()
                .create();

        assertEquals("optionC", option.getLongOpt());
        assertEquals("This is an option C with an argument", option.getDescription());
        assertTrue(option.isRequired());
        assertNull(option.getValue());
        assertEquals("arg", option.getArgName());
        assertFalse(option.isOptionalArg());
    }

    @Test
    public void testHasOptionalArgOption()
    {
        Option option = OptionBuilder.withLongOpt("optionD")
                .withDescription("This is an option D with an optional argument")
                .hasOptionalArg()
                .create();

        assertEquals("optionD", option.getLongOpt());
        assertEquals("This is an option D with an optional argument", option.getDescription());
        assertTrue(option.isRequired());
        assertNull(option.getValue());
        assertEquals("arg", option.getArgName());
        assertTrue(option.isOptionalArg());
    }

    @Test
    public void testHasArgsOption()
    {
        Option option = OptionBuilder.withLongOpt("optionE")
                .withDescription("This is an option E with multiple arguments")
                .hasArgs(2)
                .create();

        assertEquals("optionE", option.getLongOpt());
        assertEquals("This is an option E with multiple arguments", option.getDescription());
        assertTrue(option.isRequired());
        assertNull(option.getValue());
        assertEquals("arg0 arg1", option.getArgName());
        assertTrue(option.isOptionalArg());
    }

    @Test
    public void testHasOptionalArgsOption()
    {
        Option option = OptionBuilder.withLongOpt("optionF")
                .withDescription("This is an option F with multiple optional arguments")
                .hasOptionalArgs(3)
                .create();

        assertEquals("optionF", option.getLongOpt());
        assertEquals("This is an option F with multiple optional arguments", option.getDescription());
        assertTrue(option.isRequired());
        assertNull(option.getValue());
        assertEquals("arg0 arg1 arg2", option.getArgName());
        assertTrue(option.isOptionalArg());
    }

    @Test
    public void testValueSeparatorOption()
    {
        Option option = OptionBuilder.withLongOpt("optionG")
                .withDescription("This is an option G with an argument separated by :")
                .withValueSeparator(':')
                .create();

        assertEquals("optionG", option.getLongOpt());
        assertEquals("This is an option G with an argument separated by :", option.getDescription());
        assertTrue(option.isRequired());
        assertNull(option.getValue());
        assertEquals("arg:", option.getValueSeparator());
    }

    @Test
    public void testTypeOption()
    {
        Option option = OptionBuilder.withLongOpt("optionH")
                .withDescription("This is an option H with an int argument")
                .withType(Integer.class)
                .create();

        assertEquals("optionH", option.getLongOpt());
        assertEquals("This is an option H with an int argument", option.getDescription());
        assertTrue(option.isRequired());
        assertNull(option.getValue());
        assertNotNull(option.getType());
        assertEquals(Integer.class, option.getType());
    }

    @Test
    public void testDescriptionOption()
    {
        Option option = OptionBuilder.withLongOpt("optionI")
                .withDescription("This is an option I with a custom description")
                .withDescription("Custom description for option I")
                .create();

        assertEquals("optionI", option.getLongOpt());
        assertEquals("Custom description for option I", option.getDescription());
        assertTrue(option.isRequired());
        assertNull(option.getValue());
    }
}