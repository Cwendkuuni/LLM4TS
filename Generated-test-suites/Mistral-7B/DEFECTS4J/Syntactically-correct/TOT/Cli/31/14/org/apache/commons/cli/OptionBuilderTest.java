package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test for OptionBuilder class.
 */
public class OptionBuilderTest
{
    /**
     * Test withLongOpt method.
     */
    @Test
    public void testWithLongOpt()
    {
        Option option = OptionBuilder.withLongOpt("testLongOpt")
                .create();
        assertEquals("testLongOpt", option.getLongOpt());

        Option option2 = OptionBuilder.withLongOpt("testLongOpt2")
                .create();
        assertEquals("testLongOpt2", option2.getLongOpt());
    }

    /**
     * Test hasArg method.
     */
    @Test
    public void testHasArg()
    {
        Option option = OptionBuilder.hasArg()
                .create();
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        assertTrue(option.isRequired());

        Option optionWithArg = OptionBuilder.hasArg()
                .withArgName("testArg")
                .create();
        assertEquals(1, optionWithArg.getArgs());
        assertFalse(optionWithArg.isRequired());
    }

    /**
     * Test hasOptionalArg method.
     */
    @Test
    public void testHasOptionalArg()
    {
        Option option = OptionBuilder.create();
        assertNull(option.getValue());

        Option optionWithOptionalArg = OptionBuilder.hasOptionalArg()
                .create();
        assertNull(optionWithOptionalArg.getValue());

        Option optionWithOptionalArgValue = OptionBuilder.hasOptionalArg()
                .withArgName("testArg")
                .create();
        assertNull(optionWithOptionalArgValue.getValue());

        Option optionWithOptionalArgValue2 = OptionBuilder.hasOptionalArg()
                .withArgName("testArg2")
                .create("testValue");
        assertEquals("testValue", optionWithOptionalArgValue2.getValue());
    }

    /**
     * Test isRequired method.
     */
    @Test
    public void testIsRequired()
    {
        Option option = OptionBuilder.create();
        assertFalse(option.isRequired());

        Option optionRequired = OptionBuilder.isRequired()
                .create();
        assertTrue(optionRequired.isRequired());
    }

    /**
     * Test hasArgs method.
     */
    @Test
    public void testHasArgs()
    {
        Option option = OptionBuilder.create();
        assertNull(option.getValue());

        Option optionWithArgs = OptionBuilder.hasArgs()
                .create();
        assertEquals(Option.UNLIMITED_VALUES, optionWithArgs.getArgs());

        Option optionWithArgs2 = OptionBuilder.hasArgs(2)
                .create("testValue1", "testValue2");
        assertEquals(2, optionWithArgs2.getArgs().length);
        assertEquals("testValue1", optionWithArgs2.getValue()[0]);
        assertEquals("testValue2", optionWithArgs2.getValue()[1]);
    }

    /**
     * Test withValueSeparator method.
     */
    @Test
    public void testWithValueSeparator()
    {
        Option option = OptionBuilder.create();
        assertNull(option.getValue());

        Option optionWithValueSeparator = OptionBuilder.withValueSeparator(':')
                .create("testValue1:testValue2");
        assertEquals("testValue1", optionWithValueSeparator.getValue()[0]);
        assertEquals("testValue2", optionWithValueSeparator.getValue()[1]);

        Option optionWithValueSeparator2 = OptionBuilder.withValueSeparator()
                .create("testValue1=testValue2");
        assertEquals("testValue1", optionWithValueSeparator2.getValue()[0]);
        assertEquals("testValue2", optionWithValueSeparator2.getValue()[1]);
    }

    /**
     * Test withType method.
     */
    @Test
    public void testWithType()
    {
        Option option = OptionBuilder.create();
        assertNull(option.getValue());

        Option optionWithType = OptionBuilder.withType(String.class)
                .create("testValue");
        assertEquals(String.class, optionWithType.getType());
        assertEquals("testValue", optionWithType.getValue());
    }

    /**
     * Test withDescription method.
     */
    @Test
    public void testWithDescription()
    {
        Option option = OptionBuilder.create();
        assertNull(option.getDescription());

        Option optionWithDescription = OptionBuilder.withDescription("Test option description")
                .create();
        assertEquals("Test option description", optionWithDescription.getDescription());
    }
}