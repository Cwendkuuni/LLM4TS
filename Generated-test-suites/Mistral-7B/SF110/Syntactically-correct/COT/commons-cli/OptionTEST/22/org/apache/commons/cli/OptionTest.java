package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.cli.Option;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class OptionTest {

    @Test
    public void testOptionConstructorWithOptAndDescription() throws IllegalArgumentException {
        Option option = new Option("opt", "Description");
        assertEquals("opt", option.getOpt());
        assertEquals("Description", option.getDescription());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionConstructorWithInvalidOpt() throws IllegalArgumentException {
        Option option = new Option("", "Description");
        fail("Should throw an IllegalArgumentException with the message 'Option definition must not be empty'");
    }

    @Test
    public void testOptionConstructorWithOptHasArgTrueAndDescription() throws IllegalArgumentException {
        Option option = new Option("opt", true, "Description");
        assertEquals("opt", option.getOpt());
        assertEquals("Description", option.getDescription());
        assertEquals(1, option.getArgs());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionConstructorWithOptHasArgTrueAndInvalidDescription() throws IllegalArgumentException {
        Option option = new Option("opt", true, "");
        fail("Should throw an IllegalArgumentException with the message 'Description cannot be empty'");
    }

    @Test
    public void testOptionConstructorWithOptLongOptHasArgTrueAndDescription() throws IllegalArgumentException {
        Option option = new Option("opt", "longOpt", true, "Description");
        assertEquals("opt", option.getOpt());
        assertEquals("longOpt", option.getLongOpt());
        assertEquals("Description", option.getDescription());
        assertEquals(1, option.getArgs());
    }

    @Test
    public void testOptionGetLongOpt() {
        Option option = new Option("opt", "longOpt", true, "Description");
        assertEquals("longOpt", option.getLongOpt());
    }

    @Test
    public void testOptionHasLongOpt() {
        Option option = new Option("opt", true, "Description");
        assertFalse(option.hasLongOpt());
        Option optionLong = new Option("opt", "longOpt", true, "Description");
        assertTrue(optionLong.hasLongOpt());
    }

    @Test
    public void testOptionGetArgName() {
        Option option = new Option("opt", true, "Description");
        assertNull(option.getArgName());
        Option optionArgName = new Option("opt", true, "Description", "argName");
        assertEquals("argName", optionArgName.getArgName());
    }

    @Test
    public void testOptionHasArgName() {
        Option option = new Option("opt", true, "Description");
        assertFalse(option.hasArgName());
        Option optionArgName = new Option("opt", true, "Description", "argName");
        assertTrue(optionArgName.hasArgName());
    }

    @Test
    public void testOptionIsRequired() {
        Option option = new Option("opt", true, "Description");
        assertFalse(option.isRequired());
        Option optionRequired = new Option("opt", true, "Description", true);
        assertTrue(optionRequired.isRequired());
    }

    @Test
    public void testOptionSetRequired() {
        Option option = new Option("opt", true, "Description");
        assertFalse(option.isRequired());
        option.setRequired(true);
        assertTrue(option.isRequired());
    }

    @Test
    public void testOptionHasArg() {
        Option option = new Option("opt", true, "Description");
        assertFalse(option.hasArg());
        Option optionArg = new Option("opt", true, "Description", "arg");
        assertTrue(optionArg.hasArg());
    }

    @Test
    public void testOptionSetArgName() {
        Option option = new Option("opt", true, "Description");
        assertNull(option.getArgName());
        Option optionArgName = new Option("opt", true, "Description", "argName");
        assertEquals("argName", optionArgName.getArgName());
        Option optionNewArgName = new Option("opt", true, "Description", "newArgName");
        assertEquals("newArgName", optionNewArgName.getArgName());
    }

    @Test
    public void testOptionHasValues() {
        Option option = new Option("opt", true, "Description");
        assertFalse(option.hasValues());
        Option optionValue = new Option("opt", true, "Description", new String[]{"value1", "value2"});
        assertTrue(optionValue.hasValues());
    }

    @Test   
    public void testOptionAddValueForProcessing() {
        Option option = new Option("opt", true, "Description");
        List<String> expectedValues = new ArrayList<>();
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        List<String> actualValues = new ArrayList<>(option.getValuesList());
        assertEquals(expectedValues, actualValues);
    }

    @Test(expected = RuntimeException.class)
    public void testOptionAddValueForProcessingWithNoArgsAllowed() {
        Option option = new Option("opt", true, "Description");
        option.setArgs(Option.UNINITIALIZED);
        option.addValueForProcessing("value");
        fail("Should throw a RuntimeException with the message 'NO_ARGS_ALLOWED'");
    }

    @Test
    public void testOptionGetValue() {
        Option option = new Option("opt", true, "Description", "value");
        assertEquals("value", option.getValue());
    }

    @Test
    public void testOptionGetValueNull() {
        Option option = new Option("opt", true, "Description");
        assertNull(option.getValue());
    }

    @Test
    public void testOptionGetValues() {
        Option option = new Option("opt", true, "Description", new String[]{"value1", "value2"});
        String[] expectedValues = {"value1", "value2"};
        assertArrayEquals(expectedValues, option.getValues());
    }

    @Test
    public void testOptionGetValuesEmptyList() {
        Option option = new Option("opt", true, "Description");
        String[] expectedValues = {};
        assertArrayEquals(expectedValues, option.getValues());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testOptionGetValueIndexOutOfBounds() {
        Option option = new Option("opt", true, "Description", new String[]{"value1", "value2"});
        option.addValueForProcessing("value3");
        String value = option.getValue(2);
        fail("Should throw an IndexOutOfBoundsException");
    }

    @Test
    public void testOptionToString() {
        Option option = new Option("opt", true, "Description");
        String expectedToString = "[ option: opt  :: Description ]";
        assertEquals(expectedToString, option.toString());
    }

    @Test
    public void testOptionEquals() {
        Option option1 = new Option("opt1", true, "Description1");
        Option option2 = new Option("opt1", true, "Description1");
        Option option3 = new Option("opt2", true, "Description2");
        assertTrue(option1.equals(option2));
        assertFalse(option1.equals(option3));
    }

    @Test
    public void testOptionHashCode() {
        Option option1 = new Option("opt1", true, "Description1");
        Option option2 = new Option("opt1", true, "Description1");
        Option option3 = new Option("opt2", true, "Description2");
        int expectedHashcode1 = option1.hashCode();
        int expectedHashcode2 = option2.hashCode();
        int expectedHashcode3 = option3.hashCode();
        assertEquals(expectedHashcode1, expectedHashcode2);
        assertNotEquals(expectedHashcode1, expectedHashcode3);
    }

    @Test
    public void testOptionClone() throws CloneNotSupportedException {
        Option option = new Option("opt", true, "Description");
        Option clonedOption = (Option)option.clone();
        List<String> expectedValues = new ArrayList<>();
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        List<String> actualValues = new ArrayList<>(option.getValuesList());
        assertEquals(expectedValues, actualValues);
        List<String> clonedValues = new ArrayList<>(clonedOption.getValuesList());
        assertEquals(actualValues, clonedValues);
    }

    @Test
    public void testOptionClearValues() {
        Option option = new Option("opt", true, "Description");
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        option.clearValues();
        List<String> expectedValues = new ArrayList<>();
        List<String> actualValues = new ArrayList<>(option.getValuesList());
        assertEquals(expectedValues, actualValues);
    }
}