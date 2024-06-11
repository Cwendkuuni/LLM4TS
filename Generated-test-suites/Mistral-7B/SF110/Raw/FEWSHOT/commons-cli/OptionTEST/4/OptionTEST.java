package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OptionTest {

    @Test
    public void testOptionCreation() {
        Option option = new Option("a", "Option A");
        assertEquals("a", option.getOpt());
        assertEquals("Option A", option.getDescription());
        assertNull(option.getLongOpt());
        assertFalse(option.hasArg());
        assertFalse(option.isRequired());
        assertEquals(-1, option.getArgs());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.hasValueSeparator());
        assertFalse(option.hasArgName());
        assertTrue(option.hasArgs());
        assertEquals("arg", option.getArgName());
        assertEquals(Arrays.asList(), option.getValuesList());

        Option longOption = new Option("b", "Long Option B", true, "long-b");
        assertEquals("b", option.getOpt());
        assertEquals("Long Option B", option.getDescription());
        assertEquals("long-b", option.getLongOpt());
        assertTrue(option.hasArg());
        assertFalse(option.isRequired());
        assertEquals(-1, option.getArgs());
        assertTrue(option.hasOptionalArg());
        assertFalse(option.hasValueSeparator());
        assertFalse(option.hasArgName());
        assertFalse(option.hasArgs());

        Option hasArgOption = new Option("c", "Option C", false, "long-c", "arg");
        assertEquals("c", option.getOpt());
        assertEquals("Option C", option.getDescription());
        assertEquals("long-c", option.getLongOpt());
        assertFalse(option.hasArg());
        assertFalse(option.isRequired());
        assertEquals(1, option.getArgs());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.hasValueSeparator());
        assertEquals("arg", option.getArgName());
        assertFalse(option.hasArgs());
        assertEquals(Arrays.asList(), option.getValuesList());
    }

    @Test
    public void testOptionId() {
        Option optionA = new Option("a", "Option A");
        assertEquals('a', optionA.getId());

        Option optionB = new Option("b", "Option B");
        assertEquals('b', optionB.getId());

        Option optionC = new Option("c", "Option C");
        assertEquals('c', optionC.getId());

        Option longOptionA = new Option("a", "Long Option A", true, "long-a");
        assertEquals('a', longOptionA.getId());

        Option longOptionB = new Option("b", "Long Option B", true, "long-b");
        assertEquals('b', longOptionB.getId());

        Option longOptionC = new Option("c", "Long Option C", true, "long-c");
        assertEquals('c', longOptionC.getId());
    }

    @Test
    public void testOptionGettersAndSetters() {
        Option option = new Option("a", "Option A");

        assertEquals("Option A", option.getDescription());
        option.setDescription("New Option A");
        assertEquals("New Option A", option.getDescription());

        assertNull(option.getType());
        option.setType(Integer.class);
        assertEquals(Integer.class, option.getType());

        assertNull(option.getLongOpt());
        option.setLongOpt("long-option-a");
        assertEquals("long-option-a", option.getLongOpt());

        assertFalse(option.isRequired());
        option.setRequired(true);
        assertTrue(option.isRequired());

        assertFalse(option.hasOptionalArg());
        option.setOptionalArg(true);
        assertTrue(option.hasOptionalArg());

        assertFalse(option.hasArg());
        option.setArgName("arg");
        option.setArgs(1);
        assertTrue(option.hasArg());

        assertFalse(option.hasValueSeparator());
        option.setValueSeparator(' ');
        assertTrue(option.hasValueSeparator());

        assertFalse(option.hasArgName());
        option.setArgName("arg-name");
        assertTrue(option.hasArgName());

        assertFalse(option.hasArgs());
        option.setArgs(2);
        assertTrue(option.hasArgs());

        List<String> values = new ArrayList<>();
        values.add("value1");
        values.add("value2");
        option.setValues(values);
        assertEquals(values, option.getValuesList());

        option.addValueForProcessing("value3");
        option.addValueForProcessing("value4");
        assertEquals(Arrays.asList("value1", "value2", "value3", "value4"), option.getValuesList());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidOptionCreation() {
        new Option(null, "Description");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidOptionCreationWithArg() {
        new Option("", "Description");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidOptionCreationWithoutDescription() {
        new Option("option", "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidOptionCreationWithNegativeNumberOfArgs() {
        new Option("option", "", 0, "long-option");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidOptionCreationWithNonZeroNumberOfArgsAndNoValues() {
        new Option("option", "", 2, "long-option");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidOptionCreationWithInvalidValueSeparator() {
        new Option("option", "", 1, "long-option", ';');
    }
}