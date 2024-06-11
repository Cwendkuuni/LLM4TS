package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.Option;
import org.junit.Test;

public class OptionTest {

    @Test
    public void testOptionWithNoArg() {
        Option option = new Option("a", "optionA", false, "Description for optionA");
        assertEquals("optionA", option.getOpt());
        assertEquals("Description for optionA", option.getDescription());
        assertFalse(option.hasArg());
        assertFalse(option.isRequired());
        assertNull(option.getValue());
        assertFalse(option.hasValues());
        assertFalse(option.hasLongOpt());
        assertNull(option.getLongOpt());
        assertEquals('a', option.getId());
        assertTrue(option.equals(option));
        assertTrue(option.hashCode() == option.hashCode());
        Option clonedOption = option.clone();
        assertTrue(option.equals(clonedOption));
        assertTrue(option.hashCode() == clonedOption.hashCode());
    }

    @Test
    public void testOptionWithOneArg() {
        Option option = new Option("b", "optionB", true, "Description for optionB");
        assertEquals("optionB", option.getOpt());
        assertEquals("Description for optionB", option.getDescription());
        assertTrue(option.hasArg());
        assertFalse(option.isRequired());
        assertNull(option.getValue());
        assertFalse(option.hasValues());
        assertFalse(option.hasLongOpt());
        assertNull(option.getLongOpt());
        assertEquals('b', option.getId());
        assertTrue(option.equals(option));
        assertTrue(option.hashCode() == option.hashCode());
        Option clonedOption = option.clone();
        assertTrue(option.equals(clonedOption));
        assertTrue(option.hashCode() == clonedOption.hashCode());
    }

    @Test
    public void testOptionWithOneArgAndDescription() {
        Option option = new Option("c", "optionC", true, "Description for optionC with arg");
        assertEquals("optionC", option.getOpt());
        assertEquals("Description for optionC with arg", option.getDescription());
        assertTrue(option.hasArg());
        assertFalse(option.isRequired());
        assertNull(option.getValue());
        assertFalse(option.hasValues());
        assertFalse(option.hasLongOpt());
        assertNull(option.getLongOpt());
        assertEquals('c', option.getId());
        assertTrue(option.equals(option));
        assertTrue(option.hashCode() == option.hashCode());
        Option clonedOption = option.clone();
        assertTrue(option.equals(clonedOption));
        assertTrue(option.hashCode() == clonedOption.hashCode());
    }

    @Test
    public void testOptionWithLongOpt() {
        Option option = new Option("d", null, false, "Description for optionD with long opt");
        assertEquals("d", option.getOpt());
        assertEquals("Description for optionD with long opt", option.getDescription());
        assertFalse(option.hasArg());
        assertFalse(option.isRequired());
        assertNull(option.getValue());
        assertFalse(option.hasValues());
        assertTrue(option.hasLongOpt());
        assertEquals("longOptionD", option.getLongOpt());
        assertEquals('d', option.getId());
        assertTrue(option.equals(option));
        assertTrue(option.hashCode() == option.hashCode());
        Option clonedOption = option.clone();
        assertTrue(option.equals(clonedOption));
        assertTrue(option.hashCode() == clonedOption.hashCode());
    }

    @Test
    public void testOptionWithLongOptAndArg() {
        Option option = new Option("e", "longOptionE", true, "Description for optionE with long opt and arg");
        assertEquals("e", option.getOpt());
        assertEquals("Description for optionE with long opt and arg", option.getDescription());
        assertTrue(option.hasArg());
        assertFalse(option.isRequired());
        assertNull(option.getValue());
        assertFalse(option.hasValues());
        assertTrue(option.hasLongOpt());
        assertEquals("longOptionE", option.getLongOpt());
        assertEquals('e', option.getId());
        assertTrue(option.equals(option));
        assertTrue(option.hashCode() == option.hashCode());
        Option clonedOption = option.clone();
        assertTrue(option.equals(clonedOption));
        assertTrue(option.hashCode() == clonedOption.hashCode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidOptionConstruction() {
        new Option(null, null, false, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidOptionConstructionWithArg() {
        new Option(null, null, true, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidOptionConstructionWithLongOpt() {
        new Option("f", null, false, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidOptionConstructionWithLongOptAndArg() {
        new Option("g", null, true, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidOptionConstructionWithDuplicateLongOpt() {
        new Option("h", "longOptionH", false, "Description for optionH");
        new Option("h", "longOptionH", false, "Description for optionH");
    }

    @Test
    public void testSettingType() {
        Option option = new Option("i", null, false, "Description for optionI");
        option.setType(String.class);
        assertEquals(String.class, option.getType());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddValueUnsupported() {
        Option option = new Option("j", null, false, "Description for optionJ");
        option.addValue("value1");
    }

    @Test
    public void testAddValueForProcessing() {
        Option option = new Option("k", null, false, "Description for optionK");
        option.addValueForProcessing("value1");
        assertEquals(1, option.getValues().size());
        assertEquals("value1", option.getValue(0));
    }

    @Test
    public void testAddValueForProcessingWithSeparators() {
        Option option = new Option("l", null, false, "Description for optionL", '|');
        option.addValueForProcessing("value1|value2");
        assertEquals(2, option.getValues().size());
        assertEquals("value1", option.getValue(0));
        assertEquals("value2", option.getValue(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValueOutOfBounds() {
        Option option = new Option("m", null, false, "Description for optionM");
        option.addValueForProcessing("value1|value2|value3");
        option.addValueForProcessing("value4");
        try {
            option.getValue(3);
        } catch (IndexOutOfBoundsException e) {
            fail("Expected IndexOutOfBoundsException, but got: " + e.getMessage());
        }
    }

    @Test
    public void testClearValues() {
        Option option = new Option("n", null, false, "Description for optionN");
        option.addValueForProcessing("value1|value2|value3");
        option.clearValues();
        assertEquals(0, option.getValues().size());
        assertNull(option.getValue());
    }
}