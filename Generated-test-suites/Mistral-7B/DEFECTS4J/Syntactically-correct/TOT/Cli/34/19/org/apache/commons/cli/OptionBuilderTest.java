package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Parser;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests for the OptionBuilder class.
 */
public class OptionBuilderTest
{
    /** The parser object used to parse test cases. */
    private static Parser parser;

    /**
     * Sets up the Test class.
     */
    @BeforeClass
    public static void setUp()
    {
        OptionBuilder.reset();
        OptionBuilder.withLongOpt("testLongOpt");
        OptionBuilder.hasArg();
        OptionBuilder.withArgName("testArgName");
        OptionBuilder.isRequired();
        OptionBuilder.withValueSeparator(':');
        parser = new Parser();
    }

    /**
     * Tests the creation of an Option with no arguments.
     */
    @Test
    public void testCreateNoArg()
    {
        Option option = OptionBuilder.create('T');

        assertNotNull(option);
        assertEquals("No description provided", option.getDescription());
        assertNull(option.getLongOpt());
        assertFalse(option.isRequired());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        assertNull(option.getType());
        assertNull(option.getValueSeparator());
        assertNull(option.getArgName());
    }

    /**
     * Tests the creation of an Option with a long opt.
     */
    @Test
    public void testCreateWithLongOpt()
    {
        Option option = OptionBuilder.create('T');
        Option testOption = OptionBuilder.create('T', "testLongOpt");

        assertNotNull(testOption);
        assertEquals("testLongOpt", testOption.getLongOpt());
        assertEquals("testLongOpt", testOption.getOpt());
        assertSame(option, testOption.getOptSet().findOption("testLongOpt"));
        assertEquals("No description provided", testOption.getDescription());
        assertFalse(testOption.isRequired());
        assertEquals(Option.UNINITIALIZED, testOption.getArgs());
        assertNull(testOption.getType());
        assertNull(testOption.getValueSeparator());
        assertNull(testOption.getArgName());
    }

    /**
     * Tests the creation of an Option that is required.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateRequiredWithoutArg()
    {
        OptionBuilder.isRequired();
        OptionBuilder.create();
    }

    /**
     * Tests the creation of an Option with an argument.
     */
    @Test
    public void testCreateWithArg()
    {
        Option option = OptionBuilder.create('T');
        Option testOption = OptionBuilder.create('T').hasArg();

        assertNotNull(testOption);
        assertEquals("testArgName", testOption.getArgName());
        assertEquals("No description provided", testOption.getDescription());
        assertNull(testOption.getLongOpt());
        assertFalse(testOption.isRequired());
        assertEquals(1, testOption.getArgs());
        assertNull(testOption.getType());
        assertNull(testOption.getValueSeparator());
    }

    /**
     * Tests the creation of an Option with a value separator.
     */
    @Test
    public void testCreateWithValueSeparator()
    {
        Option option = OptionBuilder.create('T');
        Option testOption = OptionBuilder.create('T').withValueSeparator(':');

        assertNotNull(testOption);
        assertEquals("No description provided", testOption.getDescription());
        assertNull(testOption.getLongOpt());
        assertFalse(testOption.isRequired());
        assertEquals(Option.UNINITIALIZED, testOption.getArgs());
        assertEquals(':', testOption.getValueSeparator());
        assertNull(testOption.getType());
        assertNull(testOption.getArgName());
    }

    /**
     * Tests the creation of an Option with a type.
     */
    @Test
    public void testCreateWithType()
    {
        Option option = OptionBuilder.create('T');
        Option testOption = OptionBuilder.create('T').withType(String.class);

        assertNotNull(testOption);
        assertEquals("No description provided", testOption.getDescription());
        assertNull(testOption.getLongOpt());
        assertFalse(testOption.isRequired());
        assertEquals(Option.UNINITIALIZED, testOption.getArgs());
        assertSame(String.class, testOption.getType());
        assertNull(testOption.getValueSeparator());
        assertNull(testOption.getArgName());
    }

    /**
     * Tests the creation of an Option with a description.
     */
    @Test
    public void testCreateWithDescription()
    {
        Option option = OptionBuilder.create('T');
        Option testOption = OptionBuilder.create('T').withDescription("test description");

        assertNotNull(testOption);
        assertEquals("test description", testOption.getDescription());
        assertNull(testOption.getLongOpt());
        assertFalse(testOption.isRequired());
        assertEquals(Option.UNINITIALIZED, testOption.getArgs());
        assertNull(testOption.getType());
        assertNull(testOption.getValueSeparator());
        assertNull(testOption.getArgName());
    }

    /**
     * Tests the creation of an Option with an optional argument.
     */
    @Test
    public void testCreateWithOptionalArg()
    {
        Option option = OptionBuilder.create('T');
        Option testOption = OptionBuilder.create('T').hasOptionalArg();

        assertNotNull(testOption);
        assertEquals("testArgName", testOption.getArgName());
        assertEquals("No description provided", testOption.getDescription());
        assertNull(testOption.getLongOpt());
        assertFalse(testOption.isRequired());
        assertEquals(1, testOption.getArgs());
        assertNull(testOption.getType());
        assertNull(testOption.getValueSeparator());
        assertTrue(testOption.isOptional());
    }

    /**
     * Tests the creation of an Option with multiple optional arguments.
     */
    @Test
    public void testCreateWithMultipleOptionalArgs()
    {
        OptionBuilder.hasOptionalArgs(2);
        Option testOption = OptionBuilder.create('T');

        assertNotNull(testOption);
        assertEquals("testArgName", testOption.getArgName());
        assertEquals("No description provided", testOption.getDescription());
        assertNull(testOption.getLongOpt());
        assertFalse(testOption.isRequired());
        assertEquals(2, testOption.getArgs());
        assertNull(testOption.getType());
        assertNull(testOption.getValueSeparator());
        assertTrue(testOption.isOptional());
    }

    /**
     * Tests the creation of an Option with an argument that can be set.
     */
    @Test
    public void testCreateWithArgThatCanBeSet() throws Exception
    {
        OptionBuilder.withArgName("testArgName");
        OptionBuilder.hasArg();
        OptionBuilder.isRequired();
        OptionBuilder.withType(Integer.class);

        Option testOption = OptionBuilder.create('T');

        assertNotNull(testOption);
        assertEquals("testArgName", testOption.getArgName());
        assertEquals("No description provided", testOption.getDescription());
        assertNull(testOption.getLongOpt());
        assertTrue(testOption.isRequired());
        assertEquals(1, testOption.getArgs());
        assertSame(Integer.class, testOption.getType());
        assertNull(testOption.getValueSeparator());

        parser.parse(new String[]{"-T", "1"});
        assertEquals(1, testOption.getValue().length);
        assertEquals(1, Integer.parseInt(testOption.getValue()[0]));

        parser.parse(new String[]{"-T", "1", "2"});
        assertEquals(2, testOption.getValue().length);
        assertEquals(1, Integer.parseInt(testOption.getValue()[0]));
        assertEquals(2, Integer.parseInt(testOption.getValue()[1]));
    }

    /**
     * Tests the creation of an Option with an argument that cannot be set.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithArgThatCannotBeSet()
    {
        OptionBuilder.withArgName("testArgName");
        OptionBuilder.hasArgs();

        Option testOption = OptionBuilder.create('T');

        parser.parse(new String[]{"-T", "1"});
        parser.parse(new String[]{"-T", "2"});

        throw new AssertionError();
    }

    /**
     * Tests the creation of an Option with an argument that has a value separator.
     */
    @Test
    public void testCreateWithArgThatHasValueSeparator()
    {
        OptionBuilder.withArgName("testArgName");
        OptionBuilder.hasArg();
        OptionBuilder.isRequired();
        OptionBuilder.withValueSeparator(',');

        Option testOption = OptionBuilder.create('T');

        assertNotNull(testOption);
        assertEquals("testArgName", testOption.getArgName());
        assertEquals("No description provided", testOption.getDescription());
        assertNull(testOption.getLongOpt());
        assertTrue(testOption.isRequired());
        assertEquals(Option.UNINITIALIZED, testOption.getArgs());
        assertEquals(',', testOption.getValueSeparator());
        assertNull(testOption.getType());

        parser.parse(new String[]{"-T", "testArgName1,testArgName2"});
        assertEquals(2, testOption.getValue().length);
        assertEquals("testArgName1", testOption.getValue()[0]);
        assertEquals("testArgName2", testOption.getValue()[1]);

        parser.parse(new String[]{"-T", "testArgName1,testArgName2,testArgName3"});
        assertEquals(3, testOption.getValue().length);
        assertEquals("testArgName1", testOption.getValue()[0]);
        assertEquals("testArgName2", testOption.getValue()[1]);
        assertEquals("testArgName3", testOption.getValue()[2]);
    }
}