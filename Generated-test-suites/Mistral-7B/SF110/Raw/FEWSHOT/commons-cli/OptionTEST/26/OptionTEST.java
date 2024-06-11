package org.apache.commons.cli;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class OptionTest {

    @Test
    public void testOptionConstruction() {
        Option option = new Option("opt", "description");
        assertEquals("opt", option.getOpt());
        assertEquals("description", option.getDescription());
        assertNull(option.getLongOpt());
        assertFalse(option.hasArg());
        assertFalse(option.required);
        assertNull(option.getValue());
        assertFalse(option.hasValues());
        assertFalse(option.isValueSeparated());
        assertEquals(0, option.getArgs());
        assertFalse(option.hasOptionalArg());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidOptionConstruction() {
        new Option(null, "description");
    }

    @Test
    public void testOptionConstructionWithLongOpt() {
        Option option = new Option("opt", "longOpt", false, "description");
        assertEquals("opt", option.getOpt());
        assertEquals("longOpt", option.getLongOpt());
        assertEquals("description", option.getDescription());
        assertFalse(option.hasArg());
        assertFalse(option.required);
        assertNull(option.getValue());
        assertFalse(option.hasValues());
        assertFalse(option.isValueSeparated());
        assertEquals(0, option.getArgs());
        assertFalse(option.hasOptionalArg());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidOptionConstructionWithLongOptNull() {
        new Option("opt", null, false, "description");
    }

    @Test
    public void testOptionConstructionWithArg() {
        Option option = new Option("opt", false, "description");
        assertEquals("opt", option.getOpt());
        assertEquals("description", option.getDescription());
        assertNull(option.getLongOpt());
        assertTrue(option.hasArg());
        assertFalse(option.required);
        assertNull(option.getValue());
        assertFalse(option.hasValues());
        assertFalse(option.isValueSeparated());
        assertEquals(1, option.getArgs());
        assertFalse(option.hasOptionalArg());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidOptionConstructionWithArgAndLongOpt() {
        new Option("opt", "longOpt", false, "description");
    }

    @Test
    public void testOptionConstructionWithArgAndHasOptionalArg() {
        Option option = new Option("opt", false, "description", true);
        assertEquals("opt", option.getOpt());
        assertEquals("description", option.getDescription());
        assertNull(option.getLongOpt());
        assertTrue(option.hasArg());
        assertFalse(option.required);
        assertNull(option.getValue());
        assertFalse(option.hasValues());
        assertFalse(option.isValueSeparated());
        assertEquals(1, option.getArgs());
        assertTrue(option.hasOptionalArg());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidOptionConstructionWithArgAndLongOptAndHasOptionalArg() {
        new Option("opt", "longOpt", true, "description");
    }

    @Test
    public void testOptionConstructionWithArgAndValue() {
        Option option = new Option("opt", false, "description", "value");
        assertEquals("opt", option.getOpt());
        assertEquals("description", option.getDescription());
        assertNull(option.getLongOpt());
        assertTrue(option.hasArg());
        assertFalse(option.required);
        assertEquals("value", option.getValue());
        assertFalse(option.hasValues());
        assertFalse(option.isValueSeparated());
        assertEquals(1, option.getArgs());
        assertFalse(option.hasOptionalArg());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidOptionConstructionWithArgAndLongOptAndValue() {
        new Option("opt", "longOpt", false, "description", "value");
    }

    @Test
    public void testOptionConstructionWithArgAndValues() {
        Option option = new Option("opt", false, "description", new String[]{"value1", "value2"});
        assertEquals("opt", option.getOpt());
        assertEquals("description", option.getDescription());
        assertNull(option.getLongOpt());
        assertTrue(option.hasArg());
        assertFalse(option.required);
        assertNull(option.getValue());
        assertEquals(2, option.getArgs());
        assertFalse(option.hasOptionalArg());
        assertArrayEquals(new String[]{"value1", "value2"}, option.getValues());
        assertFalse(option.isValueSeparated());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidOptionConstructionWithArgAndLongOptAndValues() {
        new Option("opt", "longOpt", false, "description", new String[]{"value1", "value2"});
    }

    @Test
    public void testOptionConstructionWithNoArg() {
        Option option = new Option("opt", false, "description");
        assertEquals("opt", option.getOpt());
        assertEquals("description", option.getDescription());
        assertNull(option.getLongOpt());
        assertFalse(option.hasArg());
        assertFalse(option.required);
        assertNull(option.getValue());
        assertFalse(option.hasValues());
        assertFalse(option.isValueSeparated());
        assertEquals(0, option.getArgs());
        assertFalse(option.hasOptionalArg());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidOptionConstructionWithArgAndNoArg() {
        new Option("opt", false, "description", false);
    }

    @Test
    public void testOptionConstructionWithLongOptAndValue() {
        Option option = new Option(null, "longOpt", false, "description", "value");
        assertEquals("longOpt", option.getLongOpt());
        assertEquals("description", option.getDescription());
        assertEquals(null, option.getOpt());
        assertFalse(option.hasArg());
        assertFalse(option.required);
        assertEquals("value", option.getValue());
        assertFalse(option.hasValues());
        assertFalse(option.isValueSeparated());
        assertEquals(0, option.getArgs());
        assertFalse(option.hasOptionalArg());
    }

    @Test
    public void testOptionConstructionWithLongOptAndValues() {
        Option option = new Option(null, "longOpt", false, "description", new String[]{"value1", "value2"});
        assertEquals("longOpt", option.getLongOpt());
        assertEquals("description", option.getDescription());
        assertEquals(null, option.getOpt());
        assertFalse(option.hasArg());
        assertFalse(option.required);
        assertArrayEquals(new String[]{"value1", "value2"}, option.getValues());
        assertFalse(option.isValueSeparated());
        assertEquals(0, option.getArgs());
        assertFalse(option.hasOptionalArg());
    }

    @Test
    public void testOptionConstructionWithArgAndLongOptAndValueAndValues() {
        Option option = new Option("opt", "longOpt", true, "description", "value", new String[]{"value1", "value2"});
        assertEquals("opt", option.getOpt());
        assertEquals("longOpt", option.getLongOpt());
        assertEquals("description", option.getDescription());
        assertTrue(option.hasArg());
        assertTrue(option.required);
        assertEquals("value", option.getValue());
        assertEquals(2, option.getArgs());
        assertTrue(option.hasOptionalArg());
        assertArrayEquals(new String[]{"value1", "value2"}, option.getValues());
        assertFalse(option.isValueSeparated());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidOptionConstructionWithArgAndLongOptAndValueAndOptionalArgAndValues() {
        new Option("opt", "longOpt", true, "description", "value", null);
    }

    @Test
    public void testOptionGettersAndSetters() {
        Option option = new Option("opt", "description");
        option.setDescription("updatedDescription");
        assertEquals("updatedDescription", option.getDescription());
        option.setArgName("argName");
        assertEquals("argName", option.getArgName());
        option.setOptionalArg(true);
        assertTrue(option.hasOptionalArg());
        option.setRequired(true);
        assertTrue(option.isRequired());
        option.setHasArg(true);
        assertTrue(option.hasArg());
        option.setArgCount(3);
        assertEquals(3, option.getArgs());
        option.setValueSeparator(';');
        assertTrue(option.hasValueSeparator());
        assertEquals(';', option.getValueSeparator());
        option.setType(String.class);
        assertEquals(String.class, option.getType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidOptionSetters() {
        Option option = new Option("opt", "description");
        option.setArgName(null);
        option.setOptionalArg(-1);
        option.setArgs(-1);
        option.setValueSeparator('a');
    }

    @Test
    public void testOptionEquals() {
        Option option1 = new Option("opt1", "description1");
        Option option2 = new Option("opt1", "description1");
        assertTrue(option1.equals(option2));
        Option option3 = new Option("opt2", "description1");
        assertFalse(option1.equals(option3));
        Option option4 = new Option("opt1", "description2");
        assertFalse(option1.equals(option4));
    }

    @Test
    public void testOptionHashCode() {
        Option option1 = new Option("opt1", "description1");
        int hashCode1 = option1.hashCode();
        Option option2 = new Option("opt1", "description1");
        int hashCode2 = option2.hashCode();
        assertEquals(hashCode1, hashCode2);
        Option option3 = new Option("opt2", "description1");
        int hashCode3 = option3.hashCode();
        assertNotEquals(hashCode1, hashCode3);
        Option option4 = new Option("opt1", "description2");
        int hashCode4 = option4.hashCode();
        assertNotEquals(hashCode1, hashCode4);
    }

    @Test
    public void testOptionClone() throws CloneNotSupportedException {
        Option option = new Option("opt", "description");
        Option clonedOption = option.clone();
        assertEquals(option, clonedOption);
    }

    @Test
    public void testOptionClearValues() {
        Option option = new Option("opt", false, "description");
        option.addValue("value1");
        option.addValue("value2");
        option.clearValues();
        assertFalse(option.hasValues());
    }

    @Test
    public void testOptionAddValue() {
        Option option = new Option("opt", false, "description");
        option.addValue("value1");
        option.addValue("value2");
        assertEquals(2, option.getArgs());
        assertTrue(option.hasValues());
        assertArrayEquals(new String[]{"value1", "value2"}, option.getValues());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testOptionAddValueThrowsException() {
        Option option = new Option("opt", false, "description");
        option.addValue("value");
        option.addValue("value");
        try {
            option.addValue("value");
        } catch (UnsupportedOperationException e) {
            return;
        }
        fail("An UnsupportedOperationException should have been thrown.");
    }

    @Test
    public void testOptionToString() {
        Option option = new Option("opt", false, "description");
        assertEquals("[ option: opt  :: description  ]", option.toString());
    }

    @Test
    public void testOptionGetId() {
        Option option = new Option("opt", "longOpt");
        assertEquals('o', option.getId());
    }

    @Test
    public void testOptionHasOptionalArg() {
        Option option = new Option("opt", false, "description");
        assertFalse(option.hasOptionalArg());
        Option option2 = new Option("opt", false, "description", true);
        assertTrue(option2.hasOptionalArg());
    }

    @Test
    public void testOptionHasArg() {
        Option option = new Option("opt", false, "description");
        assertFalse(option.hasArg());
        Option option2 = new Option("opt", true, "description");
        assertTrue(option2.hasArg());
    }

    @Test
    public void testOptionHasLongOpt() {
        Option option = new Option("opt", false, "description");
        assertFalse(option.hasLongOpt());
        Option option2 = new Option(null, "longOpt", false, "description");
        assertTrue(option2.hasLongOpt());
    }

    @Test
    public void testOptionHasValues() {
        Option option = new Option("opt", false, "description");
        assertFalse(option.hasValues());
        Option option2 = new Option("opt", false, "description", "value");
        assertTrue(option2.hasValues());
    }

    @Test
    public void testOptionIsValueSeparated() {
        Option option = new Option("opt", false, "description");
        assertFalse(option.isValueSeparated());
        Option option2 = new Option("opt", false, "description", ';');
        assertTrue(option2.isValueSeparated());
    }

    @Test
    public void testOptionGetValue() {
        Option option = new Option("opt", false, "description", "value");
        assertEquals("value", option.getValue());
        Option option2 = new Option("opt", false, "description");
        assertNull(option2.getValue());
    }

    @Test
    public void testOptionGetValueByIndex() {
        Option option = new Option("opt", false, "description", new String[]{"value1", "value2"});
        assertEquals("value1", option.getValue(0));
        Option option2 = new Option("opt", false, "description");
        assertNull(option2.getValue(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testOptionGetValueByInvalidIndex() {
        Option option = new Option("opt", false, "description", new String[]{"value1", "value2"});
        option.getValue(2);
    }

    @Test
    public void testOptionGetValues() {
        Option option = new Option("opt", false, "description", new String[]{"value1", "value2"});
        assertArrayEquals(new String[]{"value1", "value2"}, option.getValues());
        Option option2 = new Option("opt", false, "description");
        assertNull(option2.getValues());
    }

    @Test
    public void testOptionGetValuesList() {
        Option option = new Option("opt", false, "description", new String[]{"value1", "value2"});
        List valuesList = option.getValuesList();
        assertEquals(2, valuesList.size());
        assertEquals("value1", valuesList.get(0));
        assertEquals("value2", valuesList.get(1));
        Option option2 = new Option("opt", false, "description");
        assertNull(option2.getValuesList());
    }
}