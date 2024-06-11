package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test the OptionBuilder class.
 */
public class OptionBuilderTest
{
    @Test
    public void testOptionBuilder()
    {
        Option option = OptionBuilder.withLongOpt("option1")
                .withDescription("This is an option")
                .isRequired()
                .create();

        // typical use case
        assertEquals("option1", option.getLongOpt());
        assertEquals("This is an option", option.getDescription());
        assertNull(option.getArgName());
        assertTrue(option.isRequired());
        assertEquals(Option.NO_ARGUMENTS, option.getArgPosition());
        assertNull(option.getType());
        assertNull(option.getValue());

        // edge case: optional argument
        Option optionWithArg = OptionBuilder.withLongOpt("option2")
                .hasArg()
                .create();

        assertEquals("option2", optionWithArg.getLongOpt());
        assertEquals("option2", optionWithArg.getArgName());
        assertTrue(optionWithArg.isRequired());
        assertEquals(Option.ONE_ARGUMENT, optionWithArg.getArgPosition());
        assertNull(optionWithArg.getType());
        assertNull(optionWithArg.getValue());

        // error case: no long opt set
        try
        {
            OptionBuilder.create();
        }
        catch (IllegalArgumentException e)
        {
            assertEquals("must specify longopt", e.getMessage());
        }
    }
}