package org.apache.commons.cli;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class OptionTest {

    @Test
    public void testOptionCreation() {
        Option option = new Option("opt", "Description");
        assertEquals("opt", option.getOpt());
        assertEquals("Description", option.getDescription());
        assertNull(option.getLongOpt());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.hasArg());
        assertFalse(option.isRequired());
        assertTrue(option.hasArgName());
        assertNull(option.getArgName());
        assertFalse(option.hasValues());
        assertEquals(-1, option.getArgs());
        assertFalse(option.hasValueSeparator());
        assertFalse(option.equals(null));
        assertEquals(option.hashCode(), option.hashCode());

        Option longOptOption = new Option("opt", "long-opt", false, "Description");
        assertEquals("opt", option.getOpt());
        assertEquals("Description", option.getDescription());
        assertEquals("long-opt", option.getLongOpt());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.hasArg());
        assertFalse(option.isRequired());
        assertTrue(option.hasArgName());
        assertNull(option.getArgName());
        assertFalse(option.hasValues());
        assertEquals(-1, option.getArgs());
        assertFalse(option.hasValueSeparator());
        assertFalse(option.equals(null));
        assertEquals(option.hashCode(), option.hashCode());
        assertNotEquals(option, longOptOption);

        Option hasArgOption = new Option("opt", false, "Description");
        assertEquals("opt", option.getOpt());
        assertEquals("Description", option.getDescription());
        assertNull(option.getLongOpt());
        assertTrue(option.hasOptionalArg());
        assertTrue(option.hasArg());
        assertFalse(option.isRequired());
        assertTrue(option.hasArgName());
        assertNull(option.getArgName());
        assertFalse(option.hasValues());
        assertEquals(-1, option.getArgs());
        assertFalse(option.hasValueSeparator());
        assertFalse(option.equals(null));
        assertEquals(option.hashCode(), option.hashCode());
        assertNotEquals(option, hasArgOption);
    }

    @Test
    public void testOptionGettersAndSetters() {
        Option option = new Option("opt", "Description");
        assertEquals("opt", option.getOpt());
        assertEquals("Description", option.getDescription());
        assertNull(option.getLongOpt());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.hasArg());
        assertFalse(option.isRequired());
        assertTrue(option.hasArgName());
        assertNull(option.getArgName());
        assertFalse(option.hasValues());
        assertEquals(-1, option.getArgs());
        assertFalse(option.hasValueSeparator());

        option.setLongOpt("long-opt");
        assertEquals("opt", option.getOpt());
        assertEquals("Description", option.getDescription());
        assertEquals("long-opt", option.getLongOpt());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.hasArg());
        assertFalse(option.isRequired());
        assertTrue(option.hasArgName());
        assertNull(option.getArgName());
        assertFalse(option.hasValues());
        assertEquals(-1, option.getArgs());
        assertFalse(option.hasValueSeparator());

        option.setArgName("arg");
        assertEquals("opt", option.getOpt());
        assertEquals("Description", option.getDescription());
        assertNull(option.getLongOpt());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.hasArg());
        assertFalse(option.isRequired());
        assertEquals("arg", option.getArgName());
        assertFalse(option.hasValues());
        assertEquals(-1, option.getArgs());
        assertFalse(option.hasValueSeparator());

        option.setDescription("newDescription");
        assertEquals("opt", option.getOpt());
        assertEquals("newDescription", option.getDescription());
        assertNull(option.getLongOpt());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.hasArg());
        assertFalse(option.isRequired());
        assertEquals("arg", option.getArgName());
        assertFalse(option.hasValues());
        assertEquals(-1, option.getArgs());
        assertFalse(option.hasValueSeparator());

        option.setRequired(true);
        assertEquals("opt", option.getOpt());
        assertEquals("newDescription", option.getDescription());
        assertNull(option.getLongOpt());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.hasArg());
        assertTrue(option.isRequired());
        assertEquals("arg", option.getArgName());
        assertFalse(option.hasValues());
        assertEquals(-1, option.getArgs());
        assertFalse(option.hasValueSeparator());

        option.setOptionalArg(true);
        assertEquals("opt", option.getOpt());
        assertEquals("newDescription", option.getDescription());
        assertNull(option.getLongOpt());
        assertTrue(option.hasOptionalArg());
        assertFalse(option.hasArg());
        assertFalse(option.isRequired());
        assertEquals("arg", option.getArgName());
        assertFalse(option.hasValues());
        assertEquals(-1, option.getArgs());
        assertFalse(option.hasValueSeparator());

        option.setArgs(1);
        assertEquals("opt", option.getOpt());
        assertEquals("newDescription", option.getDescription());
        assertNull(option.getLongOpt());
        assertFalse(option.hasOptionalArg());
        assertTrue(option.hasArg());
        assertFalse(option.isRequired());
        assertEquals("arg", option.getArgName());
        assertFalse(option.hasValues());
        assertEquals(1, option.getArgs());
        assertFalse(option.hasValueSeparator());

        option.setType("String.class");
        assertEquals("opt", option.getOpt());
        assertEquals("newDescription", option.getDescription());
        assertNull(option.getLongOpt());
        assertFalse(option.hasOptionalArg());
        assertTrue(option.hasArg());
        assertFalse(option.isRequired());
        assertEquals("arg", option.getArgName());
        assertFalse(option.hasValues());
        assertEquals(1, option.getArgs());
        assertFalse(option.hasValueSeparator());
        assertEquals("String.class", option.getType());

        List values = new ArrayList();
        option.setValues(values);
        assertEquals("opt", option.getOpt());
        assertEquals("newDescription", option.getDescription());
        assertNull(option.getLongOpt());
        assertFalse(option.hasOptionalArg());
        assertTrue(option.hasArg());
        assertFalse(option.isRequired());
        assertEquals("arg", option.getArgName());
        assertTrue(option.hasValues());
        assertEquals(values, option.getValuesList());
        assertEquals(0, option.getArgs());
        assertFalse(option.hasValueSeparator());
        assertEquals("String.class", option.getType());

        option.addValue("value");
        assertEquals("opt", option.getOpt());
        assertEquals("newDescription", option.getDescription());
        assertNull(option.getLongOpt());
        assertFalse(option.hasOptionalArg());
        assertTrue(option.hasArg());
        assertFalse(option.isRequired());
        assertEquals("arg", option.getArgName());
        assertTrue(option.hasValues());
        assertEquals(1, option.getArgs());
        assertFalse(option.hasValueSeparator());
        assertEquals("String.class", option.getType());
        assertEquals(1, option.getValues().size());
        assertEquals("value", option.getValue(0));

        option.clearValues();
        assertEquals("opt", option.getOpt());
        assertEquals("newDescription", option.getDescription());
        assertNull(option.getLongOpt());
        assertFalse(option.hasOptionalArg());
        assertTrue(option.hasArg());
        assertFalse(option.isRequired());
        assertEquals("arg", option.getArgName());
        assertTrue(option.hasValues());
        assertEquals(0, option.getArgs());
        assertFalse(option.hasValueSeparator());
        assertEquals("String.class", option.getType());
        assertEquals(0, option.getValues().size());

        option.setValueSeparator(';');
        assertEquals("opt", option.getOpt());
        assertEquals("newDescription", option.getDescription());
        assertNull(option.getLongOpt());
        assertFalse(option.hasOptionalArg());
        assertTrue(option.hasArg());
        assertFalse(option.isRequired());
        assertEquals("arg", option.getArgName());
        assertTrue(option.hasValues());
        assertEquals(0, option.getArgs());
        assertTrue(option.hasValueSeparator());
        assertEquals(';', option.getValueSeparator());
        assertEquals("String.class", option.getType());
        assertEquals(0, option.getValues().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionCreationWithInvalidArguments() {
        new Option(null, "Description");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionCreationWithDuplicateOption() {
        Option option1 = new Option("opt", "Description");
        Option option2 = new Option("opt", "Description");
    }

    @Test
    public void testOptionToString() {
        Option option = new Option("opt", "Description");
        String toString = option.toString();
        assertTrue(toString.contains("[ option: opt "));
        assertTrue(toString.contains("Description]"));
    }
}