package org.apache.commons.cli.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for OptionBuilder.
 *
 * @author John Doe
 */
public class OptionBuilderTest
{
    private OptionBuilder optionBuilder;

    /**
     * Set up before each test method.
     */
    @Before
    public void setUp()
    {
        optionBuilder = new OptionBuilder();
        OptionBuilder.reset();
    }

    /**
     * Test of withLongOpt method, of class OptionBuilder.
     */
    @Test
    public void testWithLongOpt()
    {
        optionBuilder = OptionBuilder.withLongOpt("option1");
        String expectedLongOpt = "option1";
        assertSame(expectedLongOpt, OptionBuilder.longopt);
    }

    /**
     * Test of hasArg method, of class OptionBuilder.
     */
    @Test
    public void testHasArg()
    {
        optionBuilder = OptionBuilder.hasArg();
        int expectedNumberOfArgs = 1;
        int actualNumberOfArgs = OptionBuilder.numberOfArgs;
        assertEquals("number of arguments should be 1", expectedNumberOfArgs, actualNumberOfArgs);
    }

    /**
     * Test of hasArg method with false parameter, of class OptionBuilder.
     */
    @Test
    public void testHasArgFalse()
    {
        optionBuilder = OptionBuilder.hasArg(false);
        int expectedNumberOfArgs = Option.UNINITIALIZED;
        int actualNumberOfArgs = OptionBuilder.numberOfArgs;
        assertEquals("number of arguments should be uninitialized", expectedNumberOfArgs, actualNumberOfArgs);
    }

    /**
     * Test of withArgName method, of class OptionBuilder.
     */
    @Test
    public void testWithArgName()
    {
        optionBuilder = OptionBuilder.withArgName("argName1");
        String expectedArgName = "argName1";
        assertSame(expectedArgName, OptionBuilder.argName);
    }

    /**
     * Test of isRequired method, of class OptionBuilder.
     */
    @Test
    public void testIsRequired()
    {
        optionBuilder = OptionBuilder.isRequired();
        boolean expectedRequired = true;
        boolean actualRequired = OptionBuilder.required;
        assertTrue("Option should be required", actualRequired);
    }

    /**
     * Test of withValueSeparator method, of class OptionBuilder.
     */
    @Test
    public void testWithValueSeparator()
    {
        optionBuilder = OptionBuilder.withValueSeparator(':');
        char expectedValuesep = ':';
        char actualValuesep = OptionBuilder.valuesep;
        assertEquals("value separator should be :", expectedValuesep, actualValuesep);

        optionBuilder = OptionBuilder.withValueSeparator();
        char expectedValuesep2 = '=';
        char actualValuesep2 = OptionBuilder.valuesep;
        assertEquals("value separator should be =", expectedValuesep2, actualValuesep2);
    }

    /**
     * Test of hasArgs method, of class OptionBuilder.
     */
    @Test
    public void testHasArgs()
    {
        optionBuilder = OptionBuilder.hasArgs();
        int expectedNumberOfArgs = Option.UNLIMITED_VALUES;
        int actualNumberOfArgs = OptionBuilder.numberOfArgs;
        assertEquals("number of arguments should be unlimited", expectedNumberOfArgs, actualNumberOfArgs);
    }

    /**
     * Test of hasArgs method with a number parameter, of class OptionBuilder.
     */
    @Test
    public void testHasArgsWithNumber()
    {
        optionBuilder = OptionBuilder.hasArgs(2);
        int expectedNumberOfArgs = 2;
        int actualNumberOfArgs = OptionBuilder.numberOfArgs;
        assertEquals("number of arguments should be 2", expectedNumberOfArgs, actualNumberOfArgs);
    }

    /**
     * Test of hasOptionalArg method, of class OptionBuilder.
     */
    @Test
    public void testHasOptionalArg()
    {
        optionBuilder = OptionBuilder.hasOptionalArg();
        boolean expectedOptionalArg = true;
        boolean actualOptionalArg = OptionBuilder.optionalArg;
        assertTrue("Option should have optional argument", actualOptionalArg);

        optionBuilder = OptionBuilder.hasOptionalArgs();
        boolean expectedOptionalArgs = true;
        boolean actualOptionalArgs = OptionBuilder.optionalArg;
        assertTrue("Options should have optional arguments", actualOptionalArgs);
    }

    /**
     * Test of withType method, of class OptionBuilder.
     */
    @Test
    public void testWithType()
    {
        Class<String> stringClass = String.class;
        optionBuilder = OptionBuilder.withType(stringClass);
        Class<?> expectedType = stringClass;
        Class<?> actualType = OptionBuilder.type;
        assertEquals("Type should be String", expectedType, actualType);
    }

    /**
     * Test of withDescription method, of class OptionBuilder.
     */
    @Test
    public void testWithDescription()
    {
        String description = "Test description";
        optionBuilder = OptionBuilder.withDescription(description);
        String expectedDescription = description;
        String actualDescription = OptionBuilder.description;
        assertEquals("Description should be Test description", expectedDescription, actualDescription);
    }

    /**
     * Test of create method, of class OptionBuilder.
     * @throws IllegalArgumentException
     */
    @Test
    public void testCreate() throws IllegalArgumentException
    {
        String longOpt = "testLongOpt";
        String description = "Test description";
        optionBuilder = OptionBuilder.withLongOpt(longOpt).withDescription(description);
        Option option = optionBuilder.create('T');

        String expectedLongOpt = longOpt;
        String expectedDescription = description;
        String actualDescription = option.getLongOpt();
        String actualDescription2 = option.getDescription();

        assertEquals("long opt should be testLongOpt", expectedLongOpt, actualDescription);
        assertEquals("description should be Test description", expectedDescription, actualDescription2);
    }

    /**
     * Test of create method, of class OptionBuilder, with long opt not set.
     * @throws IllegalArgumentException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt() throws IllegalArgumentException
    {
        optionBuilder = OptionBuilder.create();
    }

    /**
     * Test of create method, of class OptionBuilder, with long opt and no description.
     * @throws IllegalArgumentException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithLongOptButNoDescription() throws IllegalArgumentException
    {
        optionBuilder = OptionBuilder.withLongOpt("testLongOpt");
        optionBuilder.create();
    }

    /**
     * Test of create method, of class OptionBuilder, with no long opt.
     * @throws IllegalArgumentException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt2() throws IllegalArgumentException
    {
        optionBuilder = OptionBuilder.create('T');
    }

    /**
     * Test of create method, of class OptionBuilder, with no description.
     * @throws IllegalArgumentException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithNoDescription() throws IllegalArgumentException
    {
        optionBuilder = OptionBuilder.withLongOpt("testLongOpt");
        optionBuilder.create('T');
    }
}