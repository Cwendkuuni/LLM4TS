package org.apache.commons.cli;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class OptionTest {

    @Test
    public void testOptionWithNoArgs() throws IllegalArgumentException {
        Option option = new Option("a", "Option A", false);
        assertEquals("Option A", option.getDescription());
        assertNull(option.getValue());
        assertFalse(option.hasArgs());
        assertFalse(option.hasArg());
        assertFalse(option.hasValueSeparator());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.isRequired());
        assertNull(option.getArgName());
        assertEquals('a', option.getId());
        assertEquals("a", option.getKey());
        assertEquals(false, option.hasLongOpt());
        assertNull(option.getLongOpt());
        assertEquals(0, option.getArgs());
        assertEquals("Option: a", option.toString());
        assertTrue(option.equals(option));
        assertTrue(option.hashCode() == option.hashCode());
        Option clone = option.clone();
        assertEquals(option, clone);
    }

    @Test
    public void testOptionWithOneMandatoryArg() throws IllegalArgumentException {
        Option option = new Option("b", "Option B", true);
        assertEquals("Option B", option.getDescription());
        assertNull(option.getValue());
        assertTrue(option.hasArgs());
        assertFalse(option.hasArg());
        assertFalse(option.hasValueSeparator());
        assertTrue(option.isRequired());
        assertNull(option.getArgName());
        assertEquals('b', option.getId());
        assertEquals("b", option.getKey());
        assertNull(option.getLongOpt());
        assertEquals(1, option.getArgs());
        assertEquals("Option: b [ARG]", option.toString());
        assertTrue(option.equals(option));
        assertTrue(option.hashCode() == option.hashCode());
        Option clone = option.clone();
        assertEquals(option, clone);
    }

    @Test
    public void testOptionWithOneOptionalArg() throws IllegalArgumentException {
        Option option = new Option("c", "Option C", false, "arg");
        assertEquals("Option C", option.getDescription());
        assertNull(option.getValue());
        assertTrue(option.hasArgs());
        assertTrue(option.hasArg());
        assertFalse(option.hasValueSeparator());
        assertFalse(option.isRequired());
        assertEquals("arg", option.getArgName());
        assertEquals('c', option.getId());
        assertEquals("c", option.getKey());
        assertNull(option.getLongOpt());
        assertEquals(1, option.getArgs());
        assertEquals("Option: c [ARG]", option.toString());
        assertTrue(option.equals(option));
        assertTrue(option.hashCode() == option.hashCode());
        Option clone = option.clone();
        assertEquals(option, clone);
    }

    @Test
    public void testOptionWithMultipleMandatoryArgs() throws IllegalArgumentException {
        Option option = new Option("d", "Option D", false, 3);
        assertEquals("Option D", option.getDescription());
        assertNull(option.getValue());
        assertTrue(option.hasArgs());
        assertFalse(option.hasArg());
        assertFalse(option.hasValueSeparator());
        assertFalse(option.isRequired());
        assertNull(option.getArgName());
        assertEquals('d', option.getId());
        assertEquals("d", option.getKey());
        assertNull(option.getLongOpt());
        assertEquals(3, option.getArgs());
        assertEquals("Option: d [ARG1] [ARG2] [ARG3]", option.toString());
        assertTrue(option.equals(option));
        assertTrue(option.hashCode() == option.hashCode());
        Option clone = option.clone();
        assertEquals(option, clone);
    }

    @Test
    public void testOptionWithValueSeparator() throws IllegalArgumentException {
        Option option = new Option("e", "Option E", false, 1, ',');
        assertEquals("Option E", option.getDescription());
        assertNull(option.getValue());
        assertTrue(option.hasArgs());
        assertFalse(option.hasArg());
        assertTrue(option.hasValueSeparator());
        assertFalse(option.isRequired());
        assertNull(option.getArgName());
        assertEquals('e', option.getId());
        assertEquals("e", option.getKey());
        assertNull(option.getLongOpt());
        assertEquals(1, option.getArgs());
        assertEquals("Option: e [ARG]", option.toString());
        String[] values = {"value1", "value2"};
        option.setValues(Arrays.asList(values));
        assertEquals(Arrays.asList(values), option.getValuesList());
        assertEquals("value1, value2", option.getValue(null));
        assertEquals("value1", option.getValue(0));
        assertEquals("value2", option.getValue(1));
    }

    @Test
    public void testOptionWithLongName() throws IllegalArgumentException {
        Option option = new Option("--longOption", "Option with long name", false);
        assertEquals("Option with long name", option.getDescription());
        assertNull(option.getValue());
        assertFalse(option.hasArgs());
        assertFalse(option.hasArg());
        assertFalse(option.hasValueSeparator());
        assertFalse(option.isRequired());
        assertNull(option.getArgName());
        assertEquals(0, option.getId());
        assertEquals("--longOption", option.getKey());
        assertNull(option.getLongOpt());
        assertEquals(0, option.getArgs());
        assertEquals("Option: --longOption", option.toString());
        assertTrue(option.equals(option));
        assertTrue(option.hashCode() == option.hashCode());
        Option clone = option.clone();
        assertEquals(option, clone);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidOption() throws IllegalArgumentException {
        Option option = new Option(null, "Option with null name", false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidOptionWithInvalidArg() throws IllegalArgumentException {
        Option option = new Option("invalidOption", "Option with invalid arg", false, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidOptionWithInvalidNumberOfArgs() throws IllegalArgumentException {
        Option option = new Option("invalidOption", "Option with invalid number of args", false, 0, '');
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidOptionWithNullDescription() throws IllegalArgumentException {
        Option option = new Option("invalidOption", null, false);
    }
}