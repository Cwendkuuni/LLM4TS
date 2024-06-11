package org.apache.commons.cli;

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
    public void testConstructorWithOneArg() {
        Option option = new Option("a", "option A");
        assertEquals("a", option.getOpt());
        assertEquals("option A", option.getDescription());
        assertFalse(option.hasLongOpt());
        assertFalse(option.isRequired());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.hasArgName());
        assertFalse(option.hasArgs());
        assertNull(option.getType());
        assertNull(option.getLongOpt());
        assertNull(option.getValue());
        assertEquals(-1, option.getArgs());
    }

    @Test
    public void testConstructorWithOneArgAndValue() {
        Option option = new Option("a", "option A", true, "value");
        assertEquals("a", option.getOpt());
        assertEquals("option A", option.getDescription());
        assertTrue(option.isRequired());
        assertTrue(option.hasArg());
        assertNull(option.getArgName());
        assertFalse(option.hasArgs());
        assertNull(option.getType());
        assertNull(option.getLongOpt());
        assertEquals("value", option.getValue());
        assertEquals(1, option.getArgs());
    }

    @Test
    public void testConstructorWithTwoArgs() {
        Option option = new Option("a", "long option A", false, "option A");
        assertEquals("a", option.getOpt());
        assertEquals("long option A", option.getLongOpt());
        assertEquals("option A", option.getDescription());
        assertFalse(option.isRequired());
        assertFalse(option.hasArg());
        assertNull(option.getArgName());
        assertFalse(option.hasArgs());
        assertNull(option.getType());
        assertNull(option.getValue());
        assertEquals(0, option.getArgs());
    }

    @Test
    public void testConstructorWithTwoArgsAndValue() {
        Option option = new Option("a", "long option A", false, "option A", "value");
        assertEquals("a", option.getOpt());
        assertEquals("long option A", option.getLongOpt());
        assertEquals("option A", option.getDescription());
        assertFalse(option.isRequired());
        assertTrue(option.hasArg());
        assertEquals("value", option.getArgName());
        assertFalse(option.hasArgs());
        assertNull(option.getType());
        assertEquals("value", option.getValue());
        assertEquals(1, option.getArgs());
    }

    @Test
    public void testConstructorWithThreeArgsAndValue() {
        Option option = new Option("a", "long option A", false, "option A", "value", String.class);
        assertEquals("a", option.getOpt());
        assertEquals("long option A", option.getLongOpt());
        assertEquals("option A", option.getDescription());
        assertFalse(option.isRequired());
        assertTrue(option.hasArg());
        assertEquals("value", option.getArgName());
        assertFalse(option.hasArgs());
        assertSame(String.class, option.getType());
        assertEquals("value", option.getValue());
        assertEquals(1, option.getArgs());
    }

    @Test
    public void testGetId() {
        Option option = new Option("a", "option A");
        assertEquals('a', option.getId());
    }

    @Test
    public void testGetKey() {
        Option option = new Option("a", "option A");
        assertEquals("a", option.getKey());
        Option optionLong = new Option("a", "long option A");
        assertEquals("long option A", optionLong.getKey());
    }

    @Test
    public void testGetOpt() {
        Option option = new Option("a", "option A");
        assertEquals("a", option.getOpt());
    }

    @Test
    public void testGetLongOpt() {
        Option option = new Option("a", "option A");
        assertNull(option.getLongOpt());
        Option optionLong = new Option("a", "long option A");
        assertEquals("long option A", optionLong.getLongOpt());
    }

    @Test
    public void testSetOptionalArg() {
        Option option = new Option("a", "option A");
        assertFalse(option.hasOptionalArg());
        option.setOptionalArg(true);
        assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testHasOptionalArg() {
        Option option = new Option("a", "option A");
        assertFalse(option.hasOptionalArg());
        Option optionWithArg = new Option("a", "option A", true, "arg");
        assertTrue(optionWithArg.hasOptionalArg());
    }

    @Test
    public void testHasLongOpt() {
        Option option = new Option("a", "option A");
        assertFalse(option.hasLongOpt());
        Option optionLong = new Option("a", "long option A");
        assertTrue(optionLong.hasLongOpt());
    }

    @Test
    public void testHasArg() {
        Option option = new Option("a", "option A");
        assertFalse(option.hasArg());
        Option optionWithArg = new Option("a", "option A", true, "arg");
        assertTrue(optionWithArg.hasArg());
    }

    @Test
    public void testGetDescription() {
        Option option = new Option("a", "option A");
        assertEquals("option A", option.getDescription());
    }

    @Test
    public void testSetDescription() {
        Option option = new Option("a", "option A");
        assertEquals("option A", option.getDescription());
        option.setDescription("new description");
        assertEquals("new description", option.getDescription());
    }

    @Test
    public void testIsRequired() {
        Option option = new Option("a", "option A");
        assertFalse(option.isRequired());
        Option optionRequired = new Option("a", "option A", true, "arg");
        assertTrue(optionRequired.isRequired());
    }

    @Test
    public void testSetRequired() {
        Option option = new Option("a", "option A");
        assertFalse(option.isRequired());
        option.setRequired(true);
        assertTrue(option.isRequired());
    }

    @Test
    public void testGetArgName() {
        Option option = new Option("a", "option A");
        assertNull(option.getArgName());
        Option optionWithArgName = new Option("a", "option A", false, "arg");
        assertEquals("arg", optionWithArgName.getArgName());
    }

    @Test
    public void testSetArgName() {
        Option option = new Option("a", "option A");
        assertNull(option.getArgName());
        option.setArgName("arg");
        assertEquals("arg", option.getArgName());
    }

    @Test
    public void testHasArgName() {
        Option option = new Option("a", "option A");
        assertFalse(option.hasArgName());
        Option optionWithArgName = new Option("a", "option A", false, "arg");
        assertTrue(optionWithArgName.hasArgName());
    }

    @Test
    public void testHasArgs() {
        Option option = new Option("a", "option A");
        assertFalse(option.hasArgs());
        Option optionWithArgs = new Option("a", "option A", true, "arg1 arg2");
        assertTrue(optionWithArgs.hasArgs());
    }

    @Test
    public void testSetArgs() {
        Option option = new Option("a", "option A");
        assertEquals(-1, option.getArgs());
        option.setArgs(2);
        assertEquals(2, option.getArgs());
    }

    @Test
    public void testSetValueSeparator() {
        Option option = new Option("a", "option A");
        assertFalse(option.hasValueSeparator());
        option.setValueSeparator(':');
        assertTrue(option.hasValueSeparator());
    }

    @Test
    public void testGetValueSeparator() {
        Option option = new Option("a", "option A");
        assertNull(option.getValueSeparator());
        Option optionWithSeparator = new Option("a", "option A", false, "arg");
        optionWithSeparator.setValueSeparator(':');
        assertEquals(':', optionWithSeparator.getValueSeparator());
    }

    @Test
    public void testAddValueForProcessing() {
        Option option = new Option("a", "option A");
        try {
            option.addValueForProcessing("value1 value2");
            fail("Expected IllegalArgumentException");
        } catch (RuntimeException e) {
            // Expected
        }
        try {
            option.addValueForProcessing("value1");
            fail("Expected IllegalStateException");
        } catch (RuntimeException e) {
            // Expected
        }
        Option optionWithOneArg = new Option("a", "option A", true, "arg");
        optionWithOneArg.addValueForProcessing("value");
    }

    @Test
    public void testProcessValue() {
        Option option = new Option("a", "option A");
        option.processValue("value1 value2");
        assertEquals(2, option.getValues().size());
        assertEquals("value1", option.getValues().get(0));
        assertEquals("value2", option.getValues().get(1));
        Option optionWithSeparator = new Option("a", "option A", false, "arg");
        optionWithSeparator.setValueSeparator(':');
        optionWithSeparator.processValue("value1:value2");
        assertEquals(2, optionWithSeparator.getValues().size());
        assertEquals("value1", optionWithSeparator.getValues().get(0));
        assertEquals("value2", optionWithSeparator.getValues().get(1));
    }

    @Test
    public void testAddValue() {
        try {
            Option option = new Option("a", "option A");
            option.addValue("value");
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public void testGetValue() {
        Option option = new Option("a", "option A");
        assertNull(option.getValue());
        Option optionWithValue = new Option("a", "option A", true, "arg");
        optionWithValue.addValueForProcessing("value");
        assertEquals("value", optionWithValue.getValue());
    }

    @Test
    public void testGetValueIndexOutOfBoundsException() {
        Option option = new Option("a", "option A");
        assertNull(option.getValue(0));
    }

    @Test
    public void testGetValues() {
        Option option = new Option("a", "option A");
        assertNull(option.getValues());
        Option optionWithValues = new Option("a", "option A", true, "arg1 arg2");
        optionWithValues.addValueForProcessing("value1");
        optionWithValues.addValueForProcessing("value2");
        List values = optionWithValues.getValues();
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetValuesList() {
        Option option = new Option("a", "option A");
        assertNull(option.getValuesList());
        Option optionWithValues = new Option("a", "option A", true, "arg1 arg2");
        optionWithValues.addValueForProcessing("value1");
        optionWithValues.addValueForProcessing("value2");
        List values = optionWithValues.getValuesList();
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testToString() {
        Option option = new Option("a", "option A");
        String toString = option.toString();
        assertTrue(toString.contains("[ option: a ]"));
        Option optionWithLongOpt = new Option("a", "long option A", false, "option A");
        String toStringLongOpt = optionWithLongOpt.toString();
        assertTrue(toStringLongOpt.contains("[ option: a ]"));
        assertTrue(toStringLongOpt.contains(" long option A "));
        Option optionWithArg = new Option("a", "option A", true, "arg");
        String toStringArg = optionWithArg.toString();
        assertTrue(toStringArg.contains("[ option: a ]"));
        assertTrue(toStringArg.contains("[ARG]"));
        Option optionWithArgs = new Option("a", "option A", true, "arg1 arg2");
        String toStringArgs = optionWithArgs.toString();
        assertTrue(toStringArgs.contains("[ option: a ]"));
        assertTrue(toStringArgs.contains("[ARG1...]"));
        Option optionWithType = new Option("a", "option A", false, "arg", String.class);
        String toStringType = optionWithType.toString();
        assertTrue(toStringType.contains("[ option: a ]"));
        assertTrue(toStringType.contains("[ARG :: java.lang.String ]"));
    }

    @Test
    public void testEquals() {
        Option option1 = new Option("a", "option A");
        Option option2 = new Option("a", "option A");
        assertTrue(option1.equals(option2));
        Option option3 = new Option("b", "option B");
        assertFalse(option1.equals(option3));
        Option option4 = new Option("a", "long option A");
        assertFalse(option1.equals(option4));
    }

    @Test
    public void testHashCode() {
        Option option1 = new Option("a", "option A");
        Option option2 = new Option("a", "option A");
        assertEquals(option1.hashCode(), option2.hashCode());
    }

    @Test
    public void testClone() {
        Option option = new Option("a", "option A");
        Option clonedOption = option.clone();
        assertEquals(option, clonedOption);
    }

    @Test
    public void testClearValues() {
        Option option = new Option("a", "option A");
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        option.clearValues();
        assertEquals(0, option.getValues().size());
    }
}