package org.apache.commons.cli;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class OptionTest {

    @Test
    public void testOptionWithNoArgs() {
        Option option = new Option("-a", "Option A");
        assertNull(option.getValue());
        assertEquals("Option A", option.getDescription());
        assertFalse(option.hasArg());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.isRequired());
        assertFalse(option.hasLongOpt());
        assertNull(option.getLongOpt());
        assertFalse(option.hasArgName());
        assertNull(option.getArgName());
        assertEquals(Character.MIN_VALUE, option.getId());
        assertEquals(option.getClass().getName(), option.toString());
        assertEquals(Option.class, option.getClass());
        assertFalse(option.equals(null));
        assertTrue(option.equals(option));
        assertNotSame(option, new Object());
        assertEquals(option.hashCode(), option.hashCode());
        Option clonedOption = option.clone();
        assertEquals(option, clonedOption);
        assertEquals(Option.class, clonedOption.getClass());
        assertTrue(option.clearValues());
        assertEquals(0, option.getArgs());
        assertFalse(option.hasValueSeparator());
        assertNull(option.getValueSeparator());
    }

    @Test
    public void testOptionWithOneArg() {
        Option option = new Option("-a", "<arg>", "Option A");
        assertNull(option.getValue());
        assertEquals("<arg>", option.getArgName());
        assertTrue(option.hasArg());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.isRequired());
        assertFalse(option.hasLongOpt());
        assertNull(option.getLongOpt());
        assertFalse(option.hasArgName());
        assertNull(option.getArgName());
        assertEquals(Character.MIN_VALUE, option.getId());
        assertEquals(option.getClass().getName(), option.toString());
        assertEquals(Option.class, option.getClass());
        assertFalse(option.equals(null));
        assertTrue(option.equals(option));
        assertNotSame(option, new Object());
        assertEquals(option.hashCode(), option.hashCode());
        Option clonedOption = option.clone();
        assertEquals(option, clonedOption);
        assertEquals(Option.class, clonedOption.getClass());
        String value = "test";
        option.addValueForProcessing(value);
        assertEquals(value, option.getValue());
        List<String> values = option.getValuesList();
        assertEquals(value, values.get(0));
        assertEquals(1, option.getArgs());
        assertFalse(option.hasValueSeparator());
        assertNull(option.getValueSeparator());
        clearValues(option);
        assertFalse(option.hasValues());
    }

    @Test
    public void testOptionWithMultipleValues() {
        Option option = new Option("-a", "<arg1> <arg2>", "Option A");
        List<String> values = new ArrayList<>();
        values.add("test1");
        values.add("test2");
        option.setValues(values);
        assertEquals("test1 test2", option.getValue());
        assertEquals(2, option.getArgs());
        assertFalse(option.hasValueSeparator());
        assertNull(option.getValueSeparator());
    }

    @Test(expected = RuntimeException.class)
    public void testAddValueWithNoCapacity() {
        Option option = new Option("-a", "<arg>", "Option A");
        option.setArgs(0);
        String value = "test";
        option.addValueForProcessing(value);
        fail("Should throw RuntimeException: Cannot add value, list full.");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionWithInvalidArgument() {
        Option option = new Option("", "Option A");
        try {
            option.toString();
            fail("Should throw IllegalArgumentException: Invalid option: null");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        Option optionWithEmptyDescription = new Option("-a", "", "Option A");
        try {
            optionWithEmptyDescription.toString();
            fail("Should throw IllegalArgumentException: Invalid description: empty string");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        Option optionWithLongOption = new Option("-a", "--long-option", "Option A");
        try {
            optionWithLongOption.toString();
            fail("Should throw IllegalArgumentException: Option with long option and no short option");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    private void clearValues(Option option) {
        option.clearValues();
    }
}