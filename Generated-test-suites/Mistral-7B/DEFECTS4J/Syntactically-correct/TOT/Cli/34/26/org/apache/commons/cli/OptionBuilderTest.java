package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;

import org.junit.Test;

/**
 * Test cases for OptionBuilder class.
 */
public class OptionBuilderTest
{
    /**
     * Test for OptionBuilder withLongOpt method.
     */
    @Test
    public void testWithLongOpt()
    {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("test-longopt");
        Option expectedOption = new Option("test", "test-longopt", false, "test long opt");
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);
    }

    /**
     * Test for OptionBuilder hasArg method.
     */
    @Test
    public void testHasArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Option expectedOption = new Option("test", "test", true, "test option requires arg");
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);

        // Test with an argument value
        OptionBuilder optionBuilderWithArgValue = OptionBuilder.hasArg("arg-name");
        Option actualOptionWithArgValue = optionBuilderWithArgValue.create();
        assertSame("Expected created Option objects to be the same", actualOption, actualOptionWithArgValue);
        assertEquals("arg-name", actualOptionWithArgValue.getArgName());
    }

    /**
     * Test for OptionBuilder hasArg and isRequired methods.
     */
    @Test
    public void testHasArgAndIsRequired()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArg().isRequired();
        Option expectedOption = new Option("test", "test", true, "test option requires arg");
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);

        // Test with an argument value
        OptionBuilder optionBuilderWithArgValue = OptionBuilder.hasArg("arg-name").isRequired();
        Option actualOptionWithArgValue = optionBuilderWithArgValue.create();
        assertSame("Expected created Option objects to be the same", actualOption, actualOptionWithArgValue);
        assertEquals("arg-name", actualOptionWithArgValue.getArgName());
        assertTrue("Expected option to be required", actualOptionWithArgValue.isRequired());
    }

    /**
     * Test for OptionBuilder hasOptionalArg method.
     */
    @Test
    public void testHasOptionalArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        Option expectedOption = new Option("test", "test", false, "test option has optional arg");
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);

        // Test with an argument value
        OptionBuilder optionBuilderWithArgValue = OptionBuilder.hasOptionalArg("arg-name");
        Option actualOptionWithArgValue = optionBuilderWithArgValue.create();
        assertSame("Expected created Option objects to be the same", actualOption, actualOptionWithArgValue);
        assertEquals("arg-name", actualOptionWithArgValue.getArgName());
        assertFalse("Expected option to not be required", actualOptionWithArgValue.isRequired());
    }

    /**
     * Test for OptionBuilder hasArgs method.
     */
    @Test
    public void testHasArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        Option expectedOption = new Option("test", "test", true, "test option accepts multiple args");
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);

        // Test with multiple arguments
        OptionBuilder optionBuilderWithArgs = OptionBuilder.hasArgs("arg-name");
        Option actualOptionWithArgs = optionBuilderWithArgs.create();
        assertSame("Expected created Option objects to be the same", actualOption, actualOptionWithArgs);
        assertEquals("arg-name", actualOptionWithArgs.getArgName());
        assertSame("Expected number of args to be unlimited", Option.UNLIMITED_VALUES, actualOptionWithArgs.getArgs());
    }

    /**
     * Test for OptionBuilder hasArgs and isRequired methods.
     */
    @Test
    public void testHasArgsAndIsRequired()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs().isRequired();
        Option expectedOption = new Option("test", "test", true, "test option accepts multiple args and is required");
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);

        // Test with multiple arguments
        OptionBuilder optionBuilderWithArgs = OptionBuilder.hasArgs("arg-name").isRequired();
        Option actualOptionWithArgs = optionBuilderWithArgs.create();
        assertSame("Expected created Option objects to be the same", actualOption, actualOptionWithArgs);
        assertEquals("arg-name", actualOptionWithArgs.getArgName());
        assertSame("Expected number of args to be unlimited", Option.UNLIMITED_VALUES, actualOptionWithArgs.getArgs());
        assertTrue("Expected option to be required", actualOptionWithArgs.isRequired());
    }

    /**
     * Test for OptionBuilder hasOptionalArgs method.
     */
    @Test
    public void testHasOptionalArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs();
        Option expectedOption = new Option("test", "test", false, "test option accepts optional args");
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);

        // Test with multiple optional arguments
        OptionBuilder optionBuilderWithOptionalArgs = OptionBuilder.hasOptionalArgs("arg-name");
        Option actualOptionWithOptionalArgs = optionBuilderWithOptionalArgs.create();
        assertSame("Expected created Option objects to be the same", actualOption, actualOptionWithOptionalArgs);
        assertEquals("arg-name", actualOptionWithOptionalArgs.getArgName());
        assertSame("Expected number of optional args to be unlimited", Option.UNLIMITED_VALUES, actualOptionWithOptionalArgs.getArgs());
        assertFalse("Expected option to not be required", actualOptionWithOptionalArgs.isRequired());
    }

    /**
     * Test for OptionBuilder hasOptionalArgs and isRequired methods.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testHasOptionalArgsAndIsRequired()
    {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs().isRequired();
        OptionBuilder.reset(); // Reset the OptionBuilder after each test
        Option actualOption = optionBuilder.create();
        assertNull("Expected option to be null, as it should throw an exception", actualOption);
    }

    /**
     * Test for OptionBuilder withType method.
     */
    @Test
    public void testWithType()
    {
        Option typeClass = String.class;

        OptionBuilder optionBuilder = OptionBuilder.withType(typeClass);
        Option expectedOption = new Option("test", "test", false, "test option with string type arg");
        expectedOption.setValueClass(String.class);
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);
    }

    /**
     * Test for OptionBuilder withDescription method.
     */
    @Test
    public void testWithDescription()
    {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("Test description");
        Option expectedOption = new Option("test", "test", false, "Test description");
        Option actualOption = optionBuilder.create();
        assertEquals(expectedOption, actualOption);
    }
}