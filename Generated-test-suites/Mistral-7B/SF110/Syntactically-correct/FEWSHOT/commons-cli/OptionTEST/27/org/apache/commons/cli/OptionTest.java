package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class OptionTest {

    @Test
    public void testOptionConstruction() throws IllegalArgumentException {
        Option option1 = new Option("opt", "description1");
        Option option2 = new Option("opt2", "description2");
        Option option3 = new Option("opt3", null, true, "description3");
        Option option4 = new Option("opt4", "longOpt4", false, "description4");

        assertEquals("opt", option1.getOpt());
        assertEquals("description1", option1.getDescription());

        assertEquals("opt2", option2.getOpt());
        assertNull(option2.getLongOpt());

        assertEquals("opt3", option3.getOpt());
        assertEquals("description3", option3.getDescription());
        assertTrue(option3.hasArg());

        assertEquals("opt4", option4.getOpt());
        assertEquals("longOpt4", option4.getLongOpt());
        assertFalse(option4.hasArg());
    }

    @Test
    public void testOptionGetters() {
        Option option = new Option("opt", "description", false, null);

        assertEquals("opt", option.getOpt());
        assertNull(option.getLongOpt());
        assertNull(option.getType());
        assertEquals(false, option.hasOptionalArg());
        assertEquals(false, option.hasLongOpt());
        assertEquals(false, option.hasArg());
        assertNull(option.getDescription());
        assertFalse(option.isRequired());
        assertNull(option.getArgName());
        assertFalse(option.hasArgs());
        assertFalse(option.hasValueSeparator());
        assertEquals(0, option.getArgs());
        assertNull(option.getValue());
        assertNull(option.getValues());
        assertEquals(0, option.getValues().size());
        assertEquals("", option.toString());

        option.setDescription("updatedDescription");
        assertEquals("updatedDescription", option.getDescription());

        option.setArgName("argName");
        assertEquals("argName", option.getArgName());
        assertFalse(option.hasArgName());

        option.setArgs(2);
        assertEquals(2, option.getArgs());
        assertFalse(option.hasArgs());

        option.setValueSeparator('-');
        assertTrue(option.hasValueSeparator());
        assertEquals('-', option.getValueSeparator());

        option.setType("String.class");
        assertEquals("String.class", option.getType().toString());

        option.setRequired(true);
        assertTrue(option.isRequired());
    }

    @Test
    public void testOptionEquals() {
        Option option1 = new Option("opt1", "description1");
        Option option2 = new Option("opt1", "description1");
        Option option3 = new Option("opt2", "description2");

        assertTrue(option1.equals(option1));
        assertTrue(option1.equals(option2));
        assertFalse(option1.equals(option3));
    }

    @Test
    public void testOptionHashCode() {
        Option option1 = new Option("opt1", "description1");
        Option option2 = new Option("opt1", "description1");

        assertEquals(option1.hashCode(), option2.hashCode());
    }

    @Test
    public void testOptionClone() {
        Option option = new Option("opt", "description", false, null);

        Option clonedOption = option.clone();
        assertEquals(option, clonedOption);

        option.setDescription("updatedDescription");
        assertFalse(option.equals(clonedOption));
    }

    @Test
    public void testOptionAddValue() throws IllegalArgumentException {
        Option option = new Option("opt", "description", false, null);

        String[] expectedValues = {"value1", "value2"};

        option.addValue("value1");
        option.addValue("value2");
        assertArrayEquals(expectedValues, option.getValues().toArray(new String[0]));

        try {
            option.addValueForProcessing("value3");
            assertArrayEquals(Arrays.asList("value1", "value2", "value3").toArray(new String[0]), option.getValues().toArray(new String[0]));
        } catch (RuntimeException e) {
            fail("addValueForProcessing should not throw a RuntimeException: " + e.getMessage());
        }
    }

    @Test(expected = RuntimeException.class)
    public void testOptionAddValueFailed() throws IllegalArgumentException {
        Option option = new Option("opt", null, false, null);

        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        option.addValueForProcessing("value3");

        try {
            option.addValueForProcessing("value4");
            fail("addValueForProcessing should have thrown a RuntimeException: LIST_FULL.");
        } catch (RuntimeException e) {
            assertEquals("LIST_FULL", e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionAddValueInvalidArguments() {
        Option option = new Option("opt", null, false, null);

        try {
            option.addValue("value");
            fail("addValue should have thrown an IllegalArgumentException: Arguments to addValue are not valid.");
        } catch (IllegalArgumentException e) {
            assertEquals("Arguments to addValue are not valid.", e.getMessage());
        }

        try {
            option.addValueForProcessing(123);
            fail("addValueForProcessing should have thrown an IllegalArgumentException: Arguments to addValueForProcessing are not valid.");
        } catch (IllegalArgumentException e) {
            assertEquals("Arguments to addValueForProcessing are not valid.", e.getMessage());
        }
    }
}