package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.Option;

public class OptionTest {

    @org.junit.Test
    public void testConstructorWithoutArg() throws IllegalArgumentException {
        Option option = new Option("opt", "description");
        assertEquals("opt", option.getOpt());
        assertEquals("description", option.getDescription());
        assertNull(option.getLongOpt());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.hasArg());
        assertNull(option.getType());
        assertNull(option.getValues());
        assertFalse(option.hasValueSeparator());
        assertFalse(option.isRequired());
         assertEquals("Option: opt :: description", option.toString());
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidOption() throws IllegalArgumentException {
        Option option = new Option("", "description");
    }

    @org.junit.Test
    public void testConstructorWithShortOptAndDescription() throws IllegalArgumentException {
        Option option = new Option("opt", "description");
        assertEquals("opt", option.getOpt());
        assertEquals("description", option.getDescription());
        assertNull(option.getLongOpt());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.hasArg());
        assertNull(option.getType());
        assertNull(option.getValues());
        assertFalse(option.hasValueSeparator());
        assertFalse(option.isRequired());
         assertEquals("Option: opt :: description", option.toString());
    }

    @org.junit.Test
    public void testConstructorWithShortOptLongOptAndDescription() throws IllegalArgumentException {
        Option option = new Option("opt", "longOpt", false, "description");
        assertEquals("opt", option.getOpt());
        assertEquals("longOpt", option.getLongOpt());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.hasArg());
        assertNull(option.getType());
        assertNull(option.getValues());
        assertFalse(option.hasValueSeparator());
        assertFalse(option.isRequired());
         assertEquals("Option: opt :: longOpt :: description", option.toString());
    }

    @org.junit.Test
    public void testConstructorWithShortOptAndHasArg() throws IllegalArgumentException {
        Option option = new Option("opt", false, "description");
        assertEquals("opt", option.getOpt());
        assertNull(option.getLongOpt());
        assertTrue(option.hasOptionalArg());
        assertTrue(option.hasArg());
        assertNull(option.getType());
        assertNull(option.getValues());
        assertFalse(option.hasValueSeparator());
        assertFalse(option.isRequired());
         assertEquals("Option: opt :: [ARG] :: description", option.toString());
    }

    @org.junit.Test
    public void testGetId() {
        Option option = new Option("opt", null, false, "description");
        assertEquals('o', option.getId());
    }

    @org.junit.Test
    public void testGetKey() {
        Option option = new Option("opt", null, false, "description");
        assertEquals("opt", option.getKey());
        Option optionLong = new Option(null, "longOpt", false, "description");
        assertEquals("longOpt", optionLong.getKey());
    }

    @org.junit.Test
    public void testGetOpt() {
        Option option = new Option("opt", null, false, "description");
        assertEquals("opt", option.getOpt());
    }

    @org.junit.Test
    public void testGetType() {
        Option option = new Option("opt", null, false, "description");
        assertNull(option.getType());
        Option optionWithType = new Option("opt", null, false, "description", String.class);
        assertEquals(String.class, optionWithType.getType());
    }

    @org.junit.Test
    public void testSetType() {
        Option option = new Option("opt", null, false, "description");
        assertNull(option.getType());
        option.setType(String.class);
        assertEquals(String.class, option.getType());
    }

    @org.junit.Test
    public void testGetLongOpt() {
        Option option = new Option("opt", null, false, "description");
        assertNull(option.getLongOpt());
        Option optionLong = new Option(null, "longOpt", false, "description");
        assertEquals("longOpt", optionLong.getLongOpt());
    }

    @org.junit.Test
    public void testSetLongOpt() {
        Option option = new Option("opt", null, false, "description");
        assertNull(option.getLongOpt());
        option.setLongOpt("longOpt");
        assertEquals("longOpt", option.getLongOpt());
    }

    @org.junit.Test
    public void testSetOptionalArg() {
        Option option = new Option("opt", null, false, "description");
        assertFalse(option.hasOptionalArg());
        option.setOptionalArg(true);
        assertTrue(option.hasOptionalArg());
    }

    @org.junit.Test
    public void testHasOptionalArg() {
        Option option = new Option("opt", null, false, "description");
        assertFalse(option.hasOptionalArg());
        Option optionWithArg = new Option("opt", null, true, "description");
        assertTrue(optionWithArg.hasOptionalArg());
    }

    @org.junit.Test
    public void testHasLongOpt() {
        Option option = new Option("opt", null, false, "description");
        assertFalse(option.hasLongOpt());
        Option optionLong = new Option(null, "longOpt", false, "description");
        assertTrue(optionLong.hasLongOpt());
    }

    @org.junit.Test
    public void testHasArg() {
        Option option = new Option("opt", null, false, "description");
        assertFalse(option.hasArg());
        Option optionWithArg = new Option("opt", null, false, "description", true);
        assertTrue(optionWithArg.hasArg());
    }

    @org.junit.Test
    public void testHasArgName() {
        Option option = new Option("opt", null, false, "description");
        assertFalse(option.hasArgName());
        Option optionWithArgName = new Option("opt", "argName", false, "description");
        assertTrue(optionWithArgName.hasArgName());
    }

    @org.junit.Test
    public void testGetArgName() {
        Option option = new Option("opt", "argName", false, "description");
        assertEquals("argName", option.getArgName());
    }

    @org.junit.Test
    public void testHasArgs() {
        Option option = new Option("opt", null, false, "description");
        assertFalse(option.hasArgs());
        Option optionWithArgs = new Option("opt", null, 2, "description");
        assertTrue(optionWithArgs.hasArgs());
    }

    @org.junit.Test
    public void testSetArgs() {
        Option option = new Option("opt", null, false, "description");
        assertEquals(-1, option.getArgs());
        option.setArgs(2);
        assertEquals(2, option.getArgs());
    }

    @org.junit.Test(expected = RuntimeException.class)
    public void testAddValueForProcessingWithNoArgs() {
        Option option = new Option("opt", null, false, "description");
        option.addValueForProcessing("value");
    }

    @org.junit.Test
    public void testProcessValue() {
        Option option = new Option("opt", null, false, "description", ';');
        List expectedValues = new ArrayList();
        expectedValues.add("value1");
        expectedValues.add("value2");
        expectedValues.add("value3");
        option.processValue("value1;value2;value3");
        assertEquals(expectedValues, option.getValuesList());
    }

    @org.junit.Test(expected = RuntimeException.class)
    public void testAddValueForProcessingWithMoreValuesThanAllowed() {
        Option option = new Option("opt", null, false, "description", 2);
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        option.addValueForProcessing("value3");
    }

    @org.junit.Test
    public void testGetValue() {
        Option option = new Option("opt", null, false, "description");
        option.addValueForProcessing("value");
        assertEquals("value", option.getValue());
    }

    @org.junit.Test
    public void testGetValueWithIndex() {
        Option option = new Option("opt", null, false, "description");
        List values = new ArrayList();
        values.add("value1");
        values.add("value2");
        option.setValues(values);
        assertEquals("value1", option.getValue(0));
    }

    @org.junit.Test(expected = IndexOutOfBoundsException.class)
    public void testGetValueWithInvalidIndex() {
        Option option = new Option("opt", null, false, "description");
        List values = new ArrayList();
        values.add("value1");
        option.setValues(values);
        option.getValue(1);
    }

    @org.junit.Test
    public void testGetValues() {
        Option option = new Option("opt", null, false, "description");
        List values = new ArrayList();
        values.add("value1");
        values.add("value2");
        option.setValues(values);
        assertEquals(values, option.getValuesList());
    }

    @org.junit.Test
    public void testGetValuesArray() {
        Option option = new Option("opt", null, false, "description");
        String[] valuesArray = {"value1", "value2"};
        option.setValues(Arrays.asList(valuesArray));
        assertArrayEquals(valuesArray, option.getValues());
    }

    @org.junit.Test
    public void testToString() {
        Option option = new Option("opt", null, false, "description");
        assertEquals("Option: opt :: description", option.toString());
    }

    @org.junit.Test
    public void testEquals() {
        Option option1 = new Option("opt1", null, false, "description1");
        Option option2 = new Option("opt2", null, false, "description2");
        Option option3 = new Option("opt1", null, false, "description1");
        assertTrue(option1.equals(option3));
        assertFalse(option1.equals(option2));
    }

    @org.junit.Test
    public void testHashCode() {
        Option option1 = new Option("opt1", null, false, "description1");
        Option option2 = new Option("opt2", null, false, "description2");
        Option option3 = new Option("opt1", null, false, "description1");
        assertEquals(option1.hashCode(), option3.hashCode());
        assertNotEquals(option1.hashCode(), option2.hashCode());
    }

    @org.junit.Test
    public void testClone() {
        Option option = new Option("opt", null, false, "description");
        Option clonedOption = (Option) option.clone();
        assertEquals(option.toString(), clonedOption.toString());
    }

    @org.junit.Test
    public void testClearValues() {
        Option option = new Option("opt", null, false, "description");
        List values = new ArrayList();
        values.add("value1");
        values.add("value2");
        option.setValues(values);
        option.clearValues();
        assertTrue(option.hasNoValues());
    }
}