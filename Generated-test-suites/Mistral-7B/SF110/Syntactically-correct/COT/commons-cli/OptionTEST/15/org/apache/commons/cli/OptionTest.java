package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionValidator;
import org.junit.Test;

public class OptionTest {

    @Test
    public void testOptionConstructionWithValidArguments() {
        Option option = new Option("a", "optA", false, "This is option A");
        assertEquals("optA", option.getOpt());
        assertEquals("a", option.getKey());
        assertFalse(option.hasArg());
        assertEquals("This is option A", option.getDescription());
        assertNull(option.getLongOpt());
        assertFalse(option.isRequired());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.hasLongOpt());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        assertNotNull(option.getType());
        assertFalse(option.hasValueSeparator());
        assertTrue(option.hasNoValues());
    }

    @Test
    public void testOptionConstructionWithOptionalArg() {
        Option option = new Option("a", "optA", true, "This is option A");
        assertEquals("optA", option.getOpt());
        assertEquals("a", option.getKey());
        assertTrue(option.hasArg());
        assertEquals("This is option A", option.getDescription());
        assertNull(option.getLongOpt());
        assertFalse(option.isRequired());
        assertTrue(option.hasOptionalArg());
        assertFalse(option.hasLongOpt());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        assertNull(option.getType());
        assertFalse(option.hasValueSeparator());
        assertTrue(option.hasNoValues());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionConstructionWithInvalidArgument() {
        new Option(null, "optA", false, "This is option A");
    }

    @Test
    public void testOptionConstructionWithLongOpt() {
        Option option = new Option("a", "longOptA", false, "This is option A", "longArg");
        assertEquals("a", option.getKey());
        assertEquals("longOptA", option.getLongOpt());
        assertFalse(option.hasArg());
        assertEquals("This is option A", option.getDescription());
        assertNull(option.getOpt());
        assertFalse(option.isRequired());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.hasLongOpt());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        assertNull(option.getType());
        assertFalse(option.hasValueSeparator());
        assertFalse(option.hasNoValues());
        assertEquals("longArg", option.getArgName());
        assertTrue(option.hasArgName());
    }

    @Test
    public void testOptionConstructionWithArgumentsAndOptionalArg() {
        Option option = new Option("a", "optA", false, "This is option A", "arg");
        assertEquals("optA", option.getOpt());
        assertEquals("a", option.getKey());
        assertFalse(option.hasArg());
        assertEquals("This is option A", option.getDescription());
        assertNull(option.getLongOpt());
        assertFalse(option.isRequired());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.hasLongOpt());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        assertNull(option.getType());
        assertFalse(option.hasValueSeparator());
        assertFalse(option.hasNoValues());
        assertEquals("arg", option.getArgName());
        assertTrue(option.hasArgName());
    }

    @Test
    public void testOptionConstructionWithArgumentsAndValue() {
        Option option = new Option("a", "optA", false, "This is option A", "arg", ';');
        assertEquals("optA", option.getOpt());
        assertEquals("a", option.getKey());
        assertFalse(option.hasArg());
        assertEquals("This is option A", option.getDescription());
        assertNull(option.getLongOpt());
        assertFalse(option.isRequired());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.hasLongOpt());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        assertNull(option.getType());
        assertTrue(option.hasValueSeparator());
        assertEquals(';', option.getValueSeparator());
        assertFalse(option.hasNoValues());
    }

    @Test
    public void testOptionGetters() {
        Option option = new Option("a", "optA", false, "This is option A");
        assertEquals("optA", option.getOpt());
        assertEquals("a", option.getKey());
        assertFalse(option.hasArg());
        assertEquals("This is option A", option.getDescription());
        assertNull(option.getLongOpt());
        assertFalse(option.isRequired());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.hasLongOpt());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        assertNull(option.getType());
        assertFalse(option.hasValueSeparator());
        assertTrue(option.hasNoValues());
        
        option.setLongOpt("longOptA");
        assertEquals("longOptA", option.getLongOpt());
        
        option.setDescription("New description");
        assertEquals("New description", option.getDescription());
        
        option.setOptionalArg(true);
        assertTrue(option.hasOptionalArg());
        
        option.setRequired(true);
        assertTrue(option.isRequired());
        
        option.setArgName("argName");
        assertEquals("argName", option.getArgName());
        
        option.setArgs(1);
        assertEquals(1, option.getArgs());
        
        option.setValueSeparator(' ');
        assertFalse(option.hasValueSeparator());
        assertEquals(' ', option.getValueSeparator());
        
        option.setType(String.class);
        assertEquals(String.class, option.getType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionInvalidArgNumber() {
        new Option("a", "optA", 3, "This is option A");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionNegativeArgNumber() {
        new Option("a", "optA", -1, "This is option A");
    }

    @Test
    public void testOptionAddValue() {
        Option option = new Option("a", "optA", false, "This is option A");
        option.addValue("value1");
        List values = option.getValuesList();
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));

        option.clearValues();
        option.addValue("value2");
        values = option.getValuesList();
        assertEquals(1, values.size());
        assertEquals("value2", values.get(0));

        option.addValue("value3");
        values = option.getValuesList();
        assertEquals(2, values.size());
        assertEquals("value2", values.get(0));
        assertEquals("value3", values.get(1));
    }

    @Test(expected = RuntimeException.class)
    public void testOptionAddValueWithFullList() {
        Option option = new Option("a", "optA", false, "This is option A");
        List values = new ArrayList();
        OptionValidator.validateOptionValues(values, 2);
        option.addValue("value1");
        option.addValue("value2");
        option.addValue("value3");
        option.addValue("value4");
    }

    @Test
    public void testOptionToString() {
        Option option = new Option("a", "optA", false, "This is option A");
        String toString = option.toString();
        assertTrue(toString.contains("option: a"));
        assertTrue(toString.contains("ARG..."));
        assertTrue(toString.contains("This is option A"));
    }
}