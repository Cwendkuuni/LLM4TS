package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;

import org.junit.Test;

/**
 * Test class for OptionBuilder
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
        OptionBuilder expectedOptionBuilder = OptionBuilder.instance();
        expectedOptionBuilder.withLongOpt(longOpt);

        OptionBuilder actualOptionBuilder = optionBuilder.withLongOpt(longOpt);

        assertSame(expectedOptionBuilder, actualOptionBuilder);
        assertEquals(longOpt, actualOptionBuilder.getLongOpt());
    }

    /**
     * Test method for {@link OptionBuilder#hasArg()}.
     */
    @Test
    public void testHasArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        OptionBuilder expectedOptionBuilder = OptionBuilder.instance();
        expectedOptionBuilder.hasArg();

        OptionBuilder actualOptionBuilder = optionBuilder.hasArg();

        assertSame(expectedOptionBuilder, actualOptionBuilder);
        assertEquals(Option.UNINITIALIZED + 1, actualOptionBuilder.getNumberOfArgs());
    }

    /**
     * Test method for {@link OptionBuilder#hasArg(boolean)}.
     */
    @Test
    public void testHasArgBoolean()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        OptionBuilder expectedOptionBuilder = OptionBuilder.instance();
        expectedOptionBuilder.hasArg(true);

        OptionBuilder actualOptionBuilder = optionBuilder.hasArg(true);

        assertSame(expectedOptionBuilder, actualOptionBuilder);
        assertEquals(1, actualOptionBuilder.getNumberOfArgs());
    }

    /**
     * Test method for {@link OptionBuilder#withArgName(String)}.
     */
    @Test
    public void testWithArgName()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        String argName = "argName";
        OptionBuilder expectedOptionBuilder = OptionBuilder.instance();
        expectedOptionBuilder.withArgName(argName);

        OptionBuilder actualOptionBuilder = optionBuilder.withArgName(argName);

        assertSame(expectedOptionBuilder, actualOptionBuilder);
        assertEquals(argName, actualOptionBuilder.getArgName());
    }

    /**
     * Test method for {@link OptionBuilder#isRequired()}.
     */
    @Test
    public void testIsRequired()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        OptionBuilder expectedOptionBuilder = OptionBuilder.instance();
        expectedOptionBuilder.isRequired();

        OptionBuilder actualOptionBuilder = optionBuilder.isRequired();

        assertSame(expectedOptionBuilder, actualOptionBuilder);
        assertTrue(actualOptionBuilder.isRequired());
    }

    /**
     * Test method for {@link OptionBuilder#withValueSeparator(char)}.
     */
    @Test
    public void testWithValueSeparator()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        char valuesep = ':';
        OptionBuilder expectedOptionBuilder = OptionBuilder.instance();
        expectedOptionBuilder.withValueSeparator(valuesep);

        OptionBuilder actualOptionBuilder = optionBuilder.withValueSeparator(valuesep);

        assertSame(expectedOptionBuilder, actualOptionBuilder);
        assertEquals(valuesep, actualOptionBuilder.getValueSeparator());
    }

    /**
     * Test method for {@link OptionBuilder#withValueSeparator()}.
     */
    @Test
    public void testWithValueSeparatorEmpty()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        OptionBuilder expectedOptionBuilder = OptionBuilder.instance();
        expectedOptionBuilder.withValueSeparator();

        OptionBuilder actualOptionBuilder = optionBuilder.withValueSeparator();

        assertSame(expectedOptionBuilder, actualOptionBuilder);
        assertEquals('=', actualOptionBuilder.getValueSeparator());
    }

    /**
     * Test method for {@link OptionBuilder#isRequired(boolean)}.
     */
    @Test
    public void testIsRequiredBoolean()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        OptionBuilder expectedOptionBuilder = OptionBuilder.instance();
        expectedOptionBuilder.isRequired(true);

        OptionBuilder actualOptionBuilder = optionBuilder.isRequired(true);

        assertSame(expectedOptionBuilder, actualOptionBuilder);
        assertTrue(actualOptionBuilder.isRequired());
    }

    /**
     * Test method for {@link OptionBuilder#hasArgs()}.
     */
    @Test
    public void testHasArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        OptionBuilder expectedOptionBuilder = OptionBuilder.instance();

        OptionBuilder actualOptionBuilder = optionBuilder.hasArgs();

        assertSame(expectedOptionBuilder, actualOptionBuilder);
        assertEquals(Option.UNLIMITED_VALUES, actualOptionBuilder.getNumberOfArgs());
    }

    /**
     * Test method for {@link OptionBuilder#hasArgs(int)}.
     */
    @Test
    public void testHasArgsInt()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        int numArgs = 3;
        OptionBuilder expectedOptionBuilder = OptionBuilder.instance();
        expectedOptionBuilder.hasArgs(numArgs);

        OptionBuilder actualOptionBuilder = optionBuilder.hasArgs(numArgs);

        assertSame(expectedOptionBuilder, actualOptionBuilder);
        assertEquals(numArgs, actualOptionBuilder.getNumberOfArgs());
    }

    /**
     * Test method for {@link OptionBuilder#hasOptionalArg()}.
     */
    @Test
    public void testHasOptionalArg()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        OptionBuilder expectedOptionBuilder = OptionBuilder.instance();
        expectedOptionBuilder.hasOptionalArg();

        OptionBuilder actualOptionBuilder = optionBuilder.hasOptionalArg();

        assertSame(expectedOptionBuilder, actualOptionBuilder);
        assertEquals(Option.UNINITIALIZED + 1, actualOptionBuilder.getNumberOfArgs());
        assertFalse(actualOptionBuilder.isRequired());
    }

    /**
     * Test method for {@link OptionBuilder#hasOptionalArgs()}.
     */
    @Test
    public void testHasOptionalArgs()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        OptionBuilder expectedOptionBuilder = OptionBuilder.instance();
        expectedOptionBuilder.hasOptionalArgs();

        OptionBuilder actualOptionBuilder = optionBuilder.hasOptionalArgs();

        assertSame(expectedOptionBuilder, actualOptionBuilder);
        assertEquals(Option.UNLIMITED_VALUES, actualOptionBuilder.getNumberOfArgs());
        assertFalse(actualOptionBuilder.isRequired());
    }

    /**
     * Test method for {@link OptionBuilder#hasOptionalArgs(int)}.
     */
    @Test
    public void testHasOptionalArgsInt()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        int numArgs = 5;
        OptionBuilder expectedOptionBuilder = OptionBuilder.instance();
        expectedOptionBuilder.hasOptionalArgs(numArgs);

        OptionBuilder actualOptionBuilder = optionBuilder.hasOptionalArgs(numArgs);

        assertSame(expectedOptionBuilder, actualOptionBuilder);
        assertEquals(numArgs, actualOptionBuilder.getNumberOfArgs());
        assertFalse(actualOptionBuilder.isRequired());
    }

    /**
     * Test method for {@link OptionBuilder#withType(Class)}.
     */
    @Test
    public void testWithType()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        Class type = String.class;
        OptionBuilder expectedOptionBuilder = OptionBuilder.instance();
        expectedOptionBuilder.withType(type);

        OptionBuilder actualOptionBuilder = optionBuilder.withType(type);

        assertSame(expectedOptionBuilder, actualOptionBuilder);
        assertSame(type, actualOptionBuilder.getType());
    }

    /**
     * Test method for {@link OptionBuilder#withDescription(String)}.
     */
    @Test
    public void testWithDescription()
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        String description = "Description";
        OptionBuilder expectedOptionBuilder = OptionBuilder.instance();
        expectedOptionBuilder.withDescription(description);

        OptionBuilder actualOptionBuilder = optionBuilder.withDescription(description);

        assertSame(expectedOptionBuilder, actualOptionBuilder);
        assertEquals(description, actualOptionBuilder.getDescription());
    }

    /**
     * Test method for {@link OptionBuilder#create()}.
     */
    @Test
    public void testCreate() throws IllegalArgumentException
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        OptionBuilder expectedOptionBuilder = OptionBuilder.instance();
        expectedOptionBuilder.withLongOpt("longOpt");
        expectedOptionBuilder.isRequired();

        Option actualOption = optionBuilder.create();

        Option expectedOption = expectedOptionBuilder.create();

        assertSame(expectedOption, actualOption);
        assertEquals("longOpt", actualOption.getLongOpt());
        assertTrue(actualOption.isRequired());
    }

    /**
     * Test method for {@link OptionBuilder#create(char)}.
     */
    @Test
    public void testCreateChar() throws IllegalArgumentException
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        char opt = 'opt';
        Option actualOption = optionBuilder.create(opt);

        Option expectedOption = new Option(null, opt, false, null, null);

        assertNull(actualOption.getLongOpt());
        assertEquals(opt, actualOption.getValue());
        assertFalse(actualOption.isRequired());
        assertNull(actualOption.getDescription());
        assertNull(actualOption.getArgName());
        assertNull(actualOption.getType());
        assertNull(actualOption.getValueSeparator());
        assertFalse(actualOption.isOptionalArg());
        assertEquals(Option.UNINITIALIZED, actualOption.getNumberOfArgs());
    }

    /**
     * Test method for {@link OptionBuilder#create(String)}.
     */
    @Test
    public void testCreateString() throws IllegalArgumentException
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        Option actualOption = optionBuilder.create("opt");

        Option expectedOption = new Option("opt", null, false, null, null);

        assertNull(actualOption.getLongOpt());
        assertEquals("opt", actualOption.getValue());
        assertFalse(actualOption.isRequired());
        assertNull(actualOption.getDescription());
        assertNull(actualOption.getArgName());
        assertNull(actualOption.getType());
        assertNull(actualOption.getValueSeparator());
        assertFalse(actualOption.isOptionalArg());
        assertEquals(Option.UNINITIALIZED, actualOption.getNumberOfArgs());
    }

    /**
     * Test method for {@link OptionBuilder#create(String, String)}.
     */
    @Test
    public void testCreateStringString() throws IllegalArgumentException
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        String description = "Description";
        Option actualOption = optionBuilder.create("opt", description);

        Option expectedOption = new Option("opt", null, false, description, null);

        assertEquals("opt", actualOption.getValue());
        assertEquals(description, actualOption.getDescription());
        assertNull(actualOption.getLongOpt());
        assertFalse(actualOption.isRequired());
        assertNull(actualOption.getArgName());
        assertNull(actualOption.getType());
        assertNull(actualOption.getValueSeparator());
        assertFalse(actualOption.isOptionalArg());
        assertEquals(Option.UNINITIALIZED, actualOption.getNumberOfArgs());
    }

    /**
     * Test method for {@link OptionBuilder#create(String, String, boolean)} .
     */
    @Test
    public void testCreateStringStringBoolean() throws IllegalArgumentException
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        String description = "Description";
        boolean required = true;
        Option actualOption = optionBuilder.create("opt", description, required);

        Option expectedOption = new Option("opt", null, required, description, null);

        assertEquals("opt", actualOption.getValue());
        assertEquals(description, actualOption.getDescription());
        assertNull(actualOption.getLongOpt());
        assertTrue(actualOption.isRequired());
        assertNull(actualOption.getArgName());
        assertNull(actualOption.getType());
        assertNull(actualOption.getValueSeparator());
        assertFalse(actualOption.isOptionalArg());
        assertEquals(Option.UNINITIALIZED, actualOption.getNumberOfArgs());
    }

    /**
     * Test method for {@link OptionBuilder#create(String, String, boolean, int)} .
     */
    @Test
    public void testCreateStringStringBooleanInt() throws IllegalArgumentException
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        String description = "Description";
        boolean required = true;
        int numberOfArgs = 3;
        Option actualOption = optionBuilder.create("opt", description, required, numberOfArgs);

        Option expectedOption = new Option("opt", null, required, description, null);
        expectedOption.setArgs(numberOfArgs);

        assertEquals("opt", actualOption.getValue());
        assertEquals(description, actualOption.getDescription());
        assertNull(actualOption.getLongOpt());
        assertTrue(actualOption.isRequired());
        assertNull(actualOption.getArgName());
        assertNull(actualOption.getType());
        assertNull(actualOption.getValueSeparator());
        assertFalse(actualOption.isOptionalArg());
        assertEquals(numberOfArgs, actualOption.getNumberOfArgs());
    }

    /**
     * Test method for {@link OptionBuilder#create(String, String, boolean, int, Class)} .
     */
    @Test
    public void testCreateStringStringBooleanIntClass() throws IllegalArgumentException
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        String description = "Description";
        boolean required = true;
        int numberOfArgs = 3;
        Class type = Integer.class;
        Option actualOption = optionBuilder.create("opt", description, required, numberOfArgs, type);

        Option expectedOption = new Option("opt", null, required, description, null);
        expectedOption.setArgs(numberOfArgs);
        expectedOption.setType(type);

        assertEquals("opt", actualOption.getValue());
        assertEquals(description, actualOption.getDescription());
        assertNull(actualOption.getLongOpt());
        assertTrue(actualOption.isRequired());
        assertNull(actualOption.getArgName());
        assertSame(type, actualOption.getType());
        assertNull(actualOption.getValueSeparator());
        assertFalse(actualOption.isOptionalArg());
        assertEquals(numberOfArgs, actualOption.getNumberOfArgs());
    }

    /**
     * Test method for {@link OptionBuilder#create(String, String, boolean, int, Class, char)} .
     */
    @Test
    public void testCreateStringStringBooleanIntClassChar() throws IllegalArgumentException
    {
        OptionBuilder optionBuilder = OptionBuilder.instance();
        String description = "Description";
        boolean required = true;
        int numberOfArgs = 3;
        Class type = Integer.class;
        char valuesep = ':';
        Option actualOption = optionBuilder.create("opt", description, required, numberOfArgs, type, valuesep);

        Option expectedOption = new Option("opt", null, required, description, null);
        expectedOption.setArgs(numberOfArgs);
        expectedOption.setType(type);
        expectedOption.setValueSeparator(valuesep);

        assertEquals("opt", actualOption.getValue());
        assertEquals(description, actualOption.getDescription());
        assertNull(actualOption.getLongOpt());
        assertTrue(actualOption.isRequired());
        assertNull(actualOption.getArgName());
        assertSame(type, actualOption.getType());
        assertSame(valuesep, actualOption.getValueSeparator());
        assertFalse(actualOption.isOptionalArg());
        assertEquals(numberOfArgs, actualOption.getNumberOfArgs());
    }
}