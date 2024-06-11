package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;

import org.junit.Test;

/**
 * Test class for OptionBuilder.
 */
public class OptionBuilderTest
{
    /**
     * Test method for {@link OptionBuilder#withLongOpt(String)} .
     */
    @Test
    public void testWithLongOpt()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        String longOpt = "longOpt";

        optionBuilder.withLongOpt(longOpt);
        Option option = optionBuilder.create('L');

        assertEquals(longOpt, option.getLongOpt());
    }

    /**
     * Test method for {@link OptionBuilder#hasArg()} .
     */
    @Test
    public void testHasArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();

        optionBuilder.hasArg();
        Option option = optionBuilder.create('A');

        assertEquals(Option.UNINITIALIZED + 1, option.getArgs().length);
    }

    /**
     * Test method for {@link OptionBuilder#hasArg(boolean)} .
     */
    @Test
    public void testHasArgBoolean()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        boolean hasArg = true;

        optionBuilder.hasArg(hasArg);
        Option option = optionBuilder.create('B');

        assertEquals(Option.UNINITIALIZED + hasArg, option.getArgs().length);
    }

    /**
     * Test method for {@link OptionBuilder#withArgName(String)} .
     */
    @Test
    public void testWithArgName()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        String argName = "argName";

        optionBuilder.withArgName(argName);
        Option option = optionBuilder.create('C');

        assertEquals(argName, option.getArgName());
    }

    /**
     * Test method for {@link OptionBuilder#isRequired()} .
     */
    @Test
    public void testIsRequired()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();

        optionBuilder.isRequired();
        Option option = optionBuilder.create('D');

        assertTrue(option.isRequired());
    }

    /**
     * Test method for {@link OptionBuilder#withValueSeparator(char)} .
     */
    @Test
    public void testWithValueSeparator()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        char valueSeparator = ':';

        optionBuilder.withValueSeparator(valueSeparator);
        Option option = optionBuilder.create('E');

        assertEquals(valueSeparator, option.getValueSeparator());
    }

    /**
     * Test method for {@link OptionBuilder#withValueSeparator()} .
     */
    @Test
    public void testWithValueSeparatorEmpty()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();

        optionBuilder.withValueSeparator();
        Option option = optionBuilder.create('F');

        assertEquals('=', option.getValueSeparator());
    }

    /**
     * Test method for {@link OptionBuilder#isRequired(boolean)} .
     */
    @Test
    public void testIsRequiredBoolean()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        boolean isRequired = true;

        optionBuilder.isRequired(isRequired);
        Option option = optionBuilder.create('G');

        assertTrue(isRequired == option.isRequired());
    }

    /**
     * Test method for {@link OptionBuilder#hasArgs()} .
     */
    @Test
    public void testHasArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();

        optionBuilder.hasArgs();
        Option option = optionBuilder.create('H');

        assertEquals(Option.UNLIMITED_VALUES, option.getArgs().length);
    }

    /**
     * Test method for {@link OptionBuilder#hasArgs(int)} .
     */
    @Test
    public void testHasArgsInt()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        int numArgs = 3;

        optionBuilder.hasArgs(numArgs);
        Option option = optionBuilder.create('I');

        assertEquals(numArgs, option.getArgs().length);
    }

    /**
     * Test method for {@link OptionBuilder#hasOptionalArg()} .
     */
    @Test
    public void testHasOptionalArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();

        optionBuilder.hasOptionalArg();
        Option option = optionBuilder.create('J');

        assertTrue(option.hasOptionalArg());
    }

    /**
     * Test method for {@link OptionBuilder#hasOptionalArgs()} .
     */
    @Test
    public void testHasOptionalArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();

        optionBuilder.hasOptionalArgs();
        Option option = optionBuilder.create('K');

        assertTrue(option.hasOptionalArgs());
    }

    /**
     * Test method for {@link OptionBuilder#hasOptionalArgs(int)} .
     */
    @Test
    public void testHasOptionalArgsInt()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        int numOptionalArgs = 2;

        optionBuilder.hasOptionalArgs(numOptionalArgs);
        Option option = optionBuilder.create('L');

        assertEquals(numOptionalArgs, option.getArgs().length);
    }

    /**
     * Test method for {@link OptionBuilder#withType(Class)} .
     */
    @Test
    public void testWithType()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        String expectedTypeName = "java.lang.Integer";
        Class<Integer> expectedType = Integer.TYPE;

        optionBuilder.withType(expectedType);
        Option option = optionBuilder.create('M');

        assertEquals(expectedType, option.getType());
        assertEquals(expectedTypeName, option.getType().getName());
    }

    /**
     * Test method for {@link OptionBuilder#withDescription(String)} .
     */
    @Test
    public void testWithDescription()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        String description = "Option description";

        optionBuilder.withDescription(description);
        Option option = optionBuilder.create('N');

        assertEquals(description, option.getLongOpt());
    }

    /**
     * Test method for {@link OptionBuilder#create(char)} .
     */
    @Test
    public void testCreate()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        String expectedLongOpt = "opt";

        optionBuilder.withLongOpt(expectedLongOpt);
        optionBuilder.withDescription("Test Option");

        Option option = optionBuilder.create('O');

        assertNotNull(option);
        assertEquals(expectedLongOpt, option.getLongOpt());
        assertEquals("Test Option", option.getDescription());
    }

    /**
     * Test method for {@link OptionBuilder#create()} .
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateEmpty()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();

        Option option = optionBuilder.create();
    }
}