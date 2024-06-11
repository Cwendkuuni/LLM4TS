package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Test class for OptionBuilder.
 */
public class OptionBuilderTest {

    /**
     * Test creating an option with a required argument.
     */
    @Test
    public void testCreateWithRequiredArg() {
        OptionBuilder builder = OptionBuilder.withLongOpt("test")
                .hasArg()
                .isRequired()
                .withDescription("Test option");
        Option option = builder.create('t');

        assertSame("Test option", option.getLongOpt());
        assertSame("t", option.getValueOperator());
        assertTrue(option.isRequired());
        assertEquals(1, option.getArgs().length);
        assertNull(option.getValue());
    }

    /**
     * Test creating an option with an optional argument.
     */
    @Test
    public void testCreateWithOptionalArg() {
        OptionBuilder builder = OptionBuilder.withLongOpt("test")
                .hasArg()
                .hasOptionalArg()
                .withDescription("Test option");
        Option option = builder.create('t');

        assertSame("Test option", option.getLongOpt());
        assertSame("t", option.getValueOperator());
        assertFalse(option.isRequired());
        assertNull(option.getValue());
    }

    /**
     * Test creating an option with a value separator.
     */
    @Test
    public void testCreateWithValueSeparator() {
        OptionBuilder builder = OptionBuilder.withLongOpt("test")
                .hasArgs(2)
                .withValueSeparator(',')
                .withDescription("Test option");
        Option option = builder.create('t');

        assertSame("Test option", option.getLongOpt());
        assertSame(",", option.getValueSeparator());
        assertEquals(2, option.getArgs().length);
        assertNull(option.getValue());
    }

    /**
     * Test creating an option with a type.
     */
    @Test
    public void testCreateWithType() {
        OptionBuilder builder = OptionBuilder.withLongOpt("test")
                .hasArg()
                .withType(int.class)
                .withDescription("Test option");
        Option option = builder.create('t');

        assertSame("Test option", option.getLongOpt());
        assertEquals(OptionType.NUMBER, option.getType());
        assertEquals(Integer.class, option.getTypeClass());
        assertTrue(option.isRequired());
        assertNull(option.getValue());
    }

    /**
     * Test creating an option with a description.
     */
    @Test
    public void testCreateWithDescription() {
        OptionBuilder builder = OptionBuilder.withLongOpt("test")
                .withDescription("Test option")
                .create('t');

        assertSame("Test option", option.getLongOpt());
        assertNull(option.getValueOperator());
        assertNull(option.getType());
        assertNull(option.getValue());
        assertNull(option.getDescription());
        assertEquals("Test option", option.getDescription());
    }

    /**
     * Test creating an option with no arguments.
     */
    @Test
    public void testCreateWithNoArgs() {
        OptionBuilder builder = OptionBuilder.withLongOpt("test")
                .withDescription("Test option");
        Option option = builder.create('t');

        assertSame("Test option", option.getLongOpt());
        assertSame("t", option.getValueOperator());
        assertNull(option.getValue());
        assertNull(option.getArgs());
        assertNull(option.getType());
        assertNull(option.getValueSeparator());
        assertNull(option.getArgName());
        assertNull(option.getDescription());
    }

    /**
     * Test creating an option with a long opt and no short opt.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithLongOptOnly() {
        OptionBuilder builder = OptionBuilder.withLongOpt("test");
        builder.create(' ');
    }

    /**
     * Test creating an option with a short opt and no long opt.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithShortOptOnly() {
        OptionBuilder builder = OptionBuilder.withLongOpt("")
                .create(' ');
    }

    /**
     * Test creating an option with an invalid value separator.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithInvalidValueSeparator() {
        OptionBuilder builder = OptionBuilder.withLongOpt("test")
                .hasArgs(2)
                .withValueSeparator('\0')
                .withDescription("Test option");
        Option option = builder.create('t');
    }
}