package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;

import static org.junit.Assert.*;

public class OptionBuilderTest {

    @Test
    public void testEmptyOption() {
        Option option = OptionBuilder.create();
        assertNull(option.getLongOpt());
        assertNull(option.getArgName());
        assertFalse(option.isRequired());
        assertNull(option.getType());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        assertFalse(option.hasOptionalArg());
        assertNull(option.getValue());
        assertNull(option.getValueSeparator());
    }

    @Test
    public void testSimpleOption() {
        Option option = OptionBuilder.withLongOpt("testLongOpt")
                .isRequired()
                .hasArg()
                .withArgName("testArg")
                .create();

        assertEquals("testLongOpt", option.getLongOpt());
        assertEquals("testArg", option.getArgName());
        assertTrue(option.isRequired());
        assertNull(option.getType());
        assertEquals(1, option.getArgs());
        assertFalse(option.hasOptionalArg());
        assertNull(option.getValue());
        assertNull(option.getValueSeparator());
    }

    @Test
    public void testOptionalOption() {
        Option option = OptionBuilder.withLongOpt("testLongOpt")
                .hasArg()
                .withArgName("testArg")
                .hasOptionalArg()
                .create();

        assertEquals("testLongOpt", option.getLongOpt());
        assertEquals("testArg", option.getArgName());
        assertFalse(option.isRequired());
        assertNull(option.getType());
        assertEquals(1, option.getArgs());
        assertTrue(option.hasOptionalArg());
        assertNull(option.getValue());
        assertNull(option.getValueSeparator());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingLongOpt() {
        OptionBuilder.create();
    }

    @Test
    public void testValueSeparatorOption() {
        Option option = OptionBuilder.withLongOpt("testLongOpt")
                .hasArg()
                .withArgName("testArg")
                .isRequired()
                .withValueSeparator(':')
                .create();

        assertEquals("testLongOpt", option.getLongOpt());
        assertEquals("testArg", option.getArgName());
        assertTrue(option.isRequired());
        assertNull(option.getType());
        assertEquals(1, option.getArgs());
        assertFalse(option.hasOptionalArg());
        assertNotNull(option.getValue());
        assertEquals(':', option.getValueSeparator());
        assertNull(option.getValue(0));
        assertNull(option.getValue(1));
        String[] args = {"--testLongOpt:test1"};
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(args, OptionHandler.createForObject());
            assertEquals("test1", option.getValue(0));
        } catch (ParseException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testValueSeparatorWithMultipleArguments() {
        Option option = OptionBuilder.withLongOpt("testLongOpt")
                .hasArgs()
                .isRequired()
                .withValueSeparator(':')
                .create();

        assertEquals("testLongOpt", option.getLongOpt());
        assertTrue(option.isRequired());
        assertNull(option.getType());
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
        assertFalse(option.hasOptionalArg());
        assertNotNull(option.getValue());
        assertEquals(':', option.getValueSeparator());
        assertNull(option.getValue(0));
        String[] args = {"--testLongOpt=test1:test2"};
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(args, OptionHandler.createForObject());
            assertEquals(2, option.getValues().length);
            assertEquals("test1", option.getValue(0));
            assertEquals("test2", option.getValue(1));
        } catch (ParseException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testTypeOption() {
        Option option = OptionBuilder.withLongOpt("testLongOpt")
                .isRequired()
                .hasArg()
                .withArgName("testArg")
                .withType(Integer.TYPE)
                .create();

        assertEquals("testLongOpt", option.getLongOpt());
        assertEquals("testArg", option.getArgName());
        assertTrue(option.isRequired());
        assertEquals(Integer.TYPE, option.getType());
        assertEquals(1, option.getArgs());
        assertFalse(option.hasOptionalArg());
        assertNull(option.getValue());
        assertNull(option.getValueSeparator());
        String[] args = {"--testLongOpt=123"};
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(args, OptionHandler.createForObject());
            assertEquals(123, (int) option.getValue());
        } catch (ParseException e) {
            fail(e.getMessage());
        }
    }

}