package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class OptionTest {

    @Test
    public void testOptionConstruction() {
        Option option = new Option("a", "option A", true);
        assertEquals("a", option.getOpt());
        assertEquals("option A", option.getDescription());
        assertTrue(option.hasArg());
        assertFalse(option.hasLongOpt());
        assertFalse(option.isRequired());
        assertNull(option.getType());
        assertNull(option.getArgName());
        assertFalse(option.hasArgs());
        assertFalse(option.hasValueSeparator());
        assertNull(option.getValue());
        assertNull(option.getValues());
        assertFalse(option.hasValues());

        option = new Option("--long-option", "long option", false);
        assertEquals("--long-option", option.getOpt());
        assertEquals("long option", option.getDescription());
        assertFalse(option.hasArg());
        assertTrue(option.hasLongOpt());
        assertFalse(option.isRequired());
        assertNull(option.getType());
        assertNull(option.getArgName());
        assertFalse(option.hasArgs());
        assertFalse(option.hasValueSeparator());
        assertNull(option.getValue());
        assertNull(option.getValues());
        assertFalse(option.hasValues());

        option = new Option("a", "option A", false, "argName");
        assertEquals("a", option.getOpt());
        assertEquals("option A", option.getDescription());
        assertFalse(option.hasArg());
        assertFalse(option.hasLongOpt());
        assertFalse(option.isRequired());
        assertEquals("argName", option.getArgName());
        assertFalse(option.hasArgs());
        assertFalse(option.hasValueSeparator());
        assertNull(option.getValue());
        assertNull(option.getValues());
        assertFalse(option.hasValues());

        try {
            new Option(null, "option A", false);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            new Option("", "option A", false);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            new Option("a", "", false);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testOptionWithValues() {
        Option option = new Option("a", "option A", false);
        option.setType(String.class);
        option.setArgName("arg");
        option.setArgs(2);
        option.setValueSeparator('c');

        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        option.addValueForProcessing("value3");

        assertEquals(3, option.getArgs());
        assertEquals('c', option.getValueSeparator());
        assertEquals(Arrays.asList("value1", "value2", "value3"), option.getValuesList());

        String[] expectedValues = new String[]{"value1", "value2", "value3"};
        String[] actualValues = option.getValues();
        assertArrayEquals(expectedValues, actualValues);

        assertEquals("value1", option.getValue());
        assertEquals("value2", option.getValue(1));
        assertEquals("value3", option.getValue(2));
    }

    @Test
    public void testOptionWithNoValues() {
        Option option = new Option("a", "option A", true);
        assertEquals(1, option.getArgs());
        assertNull(option.getValue());
        assertNull(option.getValues());
    }

    @Test
    public void testOptionEquals() {
        Option option1 = new Option("a", "option A", true);
        Option option2 = new Option("a", "option A", true);
        Option option3 = new Option("b", "option B", true);

        assertEquals(option1, option1);
        assertEquals(option1, option2);
        assertNotEquals(option1, option3);
    }

    @Test
    public void testOptionHashCode() {
        Option option1 = new Option("a", "option A", true);
        Option option2 = new Option("a", "option A", true);
        Option option3 = new Option("b", "option B", true);

        int hashCode1 = option1.hashCode();
        int hashCode2 = option1.hashCode();
        int hashCode3 = option2.hashCode();
        int hashCode4 = option3.hashCode();

        assertEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
        assertNotEquals(hashCode1, hashCode4);
    }

    @Test
    public void testOptionToString() {
        Option option = new Option("a", "option A", true);
        String expected = "[ option: a ][ARG] :: option A ]";
        assertEquals(expected, option.toString());

        option.setLongOpt("--long-option");
        expected = "[ option: a ][ARG] :: option A ]";
        assertEquals(expected, option.toString());

        option.setArgName("argName");
        expected = "[ option: a ][argName] :: option A ]";
        assertEquals(expected, option.toString());

        option.setArgs(2);
        option.setValueSeparator('c');
        expected = "[ option: a ][argName...] :: option A :: java.lang.String ]";
        assertEquals(expected, option.toString());

        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        expected = "[ option: a [value1, value2] ]";
        assertEquals(expected, option.toString());

        option.clearValues();
        expected = "[ option: a [ARG...] ]";
        assertEquals(expected, option.toString());
    }
}