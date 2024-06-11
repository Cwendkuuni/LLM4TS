package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.Option;

public class OptionTest {

    @org.junit.Test
    public void testOptionConstructionWithNoArguments() {
        Option option = new Option("opt", "description");
        assertEquals("Description for option: opt", option.getDescription());
        assertNull(option.getLongOpt());
        assertFalse(option.hasLongOpt());
        assertFalse(option.hasArg());
        assertFalse(option.hasArgName());
        assertNull(option.getValue());
        assertNull(option.getValues());
        assertFalse(option.isRequired());
    }

    @org.junit.Test
    public void testOptionConstructionWithOneArgument() {
        Option option = new Option("opt", "description", true, "argName");
        assertEquals("Description for option: opt", option.getDescription());
        assertNull(option.getLongOpt());
        assertFalse(option.hasLongOpt());
        assertTrue(option.hasArg());
        assertEquals("argName", option.getArgName());
        assertNull(option.getValue());
        assertNull(option.getValues());
        assertFalse(option.isRequired());
    }

    @org.junit.Test
    public void testOptionConstructionWithOneArgumentAndLongOpt() {
        Option option = new Option("opt", "description", true, "argName", "longOpt");
        assertEquals("Description for option: opt", option.getDescription());
        assertEquals("longOpt", option.getLongOpt());
        assertTrue(option.hasLongOpt());
        assertEquals("argName", option.getArgName());
        assertNull(option.getValue());
        assertNull(option.getValues());
        assertFalse(option.isRequired());
    }

    @org.junit.Test
    public void testGetId() {
        Option option = new Option("opt", "description");
        assertEquals('o', option.getId());
    }

    @org.junit.Test
    public void testGetKey() {
        Option option = new Option("opt", "description");
        assertEquals("opt", option.getKey());

        Option optionLong = new Option("opt", null, false, "description", "longOpt");
        assertEquals("longOpt", optionLong.getKey());
    }

    @org.junit.Test
    public void testGetOpt() {
        Option option = new Option("opt", "description");
        assertEquals("opt", option.getOpt());

        Option optionLong = new Option(null, "description", false, null, "longOpt");
        assertEquals("longOpt", optionLong.getOpt());
    }

    @org.junit.Test
    public void testGetType() {
        Option option = new Option("opt", "description");
        assertNull(option.getType());

        Option optionWithType = new Option("opt", "description", null, null, null, "String.class");
        assertSame(String.class, optionWithType.getType());
    }

    @org.junit.Test
    public void testSetType() {
        Option option = new Option("opt", "description");
        assertNull(option.getType());

        Option optionWithType = new Option("opt", "description");
        optionWithType.setType("String");
        assertEquals("String", optionWithType.getType().getClass().getName());
    }

    @org.junit.Test
    public void testGetLongOpt() {
        Option option = new Option("opt", "description");
        assertNull(option.getLongOpt());

        Option optionLong = new Option("opt", null, false, null, "longOpt");
        assertEquals("longOpt", optionLong.getLongOpt());
    }

    @org.junit.Test
    public void testSetLongOpt() {
        Option option = new Option("opt", "description");
        assertNull(option.getLongOpt());

        Option optionLong = new Option("opt", null, false, null, "longOpt");
        optionLong.setLongOpt("longOpt2");
        assertEquals("longOpt2", optionLong.getLongOpt());
    }

    @org.junit.Test
    public void testSetOptionalArgWithFalse() {
        Option option = new Option("opt", "description");
        assertFalse(option.hasOptionalArg());

        option.setOptionalArg(false);
        assertFalse(option.hasOptionalArg());
    }

    @org.junit.Test
    public void testSetOptionalArgWithTrue() {
        Option option = new Option("opt", "description");
        assertFalse(option.hasOptionalArg());

        option.setOptionalArg(true);
        assertTrue(option.hasOptionalArg());
    }

    @org.junit.Test
    public void testHasLongOpt() {
        Option option = new Option("opt", "description");
        assertFalse(option.hasLongOpt());

        Option optionLong = new Option("opt", null, false, null, "longOpt");
        assertTrue(optionLong.hasLongOpt());
    }

    @org.junit.Test
    public void testHasArg() {
        Option option = new Option("opt", "description");
        assertFalse(option.hasArg());

        Option optionWithArg = new Option("opt", "description", true, null);
        assertTrue(optionWithArg.hasArg());
    }

    @org.junit.Test
    public void testGetDescription() {
        Option option = new Option("opt", "description");
        assertEquals("description", option.getDescription());
    }

    @org.junit.Test
    public void testSetDescription() {
        Option option = new Option("opt", "description");
        assertEquals("description", option.getDescription());

        option.setDescription("description2");
        assertEquals("description2", option.getDescription());
    }

    @org.junit.Test
    public void testIsRequiredWithFalse() {
        Option option = new Option("opt", "description");
        assertFalse(option.isRequired());

        option.setRequired(false);
        assertFalse(option.isRequired());
    }

    @org.junit.Test
    public void testIsRequiredWithTrue() {
        Option option = new Option("opt", "description");
        assertFalse(option.isRequired());

        option.setRequired(true);
        assertTrue(option.isRequired());
    }

    @org.junit.Test
    public void testSetArgNameWithNull() {
        Option option = new Option("opt", "description");
        assertFalse(option.hasArgName());

        option.setArgName(null);
        assertFalse(option.hasArgName());
    }

    @org.junit.Test
    public void testSetArgNameWithEmptyString() {
        Option option = new Option("opt", "description");
        assertFalse(option.hasArgName());

        option.setArgName("");
        assertFalse(option.hasArgName());
    }

    @org.junit.Test
    public void testSetArgNameWithValue() {
        Option option = new Option("opt", "description");
        assertFalse(option.hasArgName());

        option.setArgName("argName");
        assertTrue(option.hasArgName());
        assertEquals("argName", option.getArgName());
    }

    @org.junit.Test
    public void testHasArgs() {
        Option option = new Option("opt", "description");
        assertFalse(option.hasArgs());

        Option optionWithArgs = new Option("opt", "description", 2, null);
        assertTrue(optionWithArgs.hasArgs());
    }

    @org.junit.Test
    public void testSetArgsWithNegativeValue() {
        Option option = new Option("opt", "description");
        option.setArgs(-1);
        fail("An exception should be thrown for setting negative number of args.");
    }

    @org.junit.Test
    public void testSetArgsWithZeroValue() {
        Option option = new Option("opt", "description");
        option.setArgs(0);
        assertFalse(option.hasArgs());
    }

    @org.junit.Test
    public void testSetArgsWithPositiveValue() {
        Option option = new Option("opt", "description");
        option.setArgs(1);
        assertTrue(option.hasArgs());
    }

    @org.junit.Test
    public void testSetValueSeparatorWithChar() {
        Option option = new Option("opt", "description");
        assertFalse(option.hasValueSeparator());

        option.setValueSeparator(';');
        assertTrue(option.hasValueSeparator());
        assertEquals(';', option.getValueSeparator());
    }

    @org.junit.Test
    public void testSetValueSeparatorWithEmptyString() {
        Option option = new Option("opt", "description");
        assertFalse(option.hasValueSeparator());

        option.setValueSeparator("");
        assertFalse(option.hasValueSeparator());
    }

    @org.junit.Test
    public void testAddValueForProcessingWithNoArgs() {
        Option option = new Option("opt", "description");
        option.setArgs(-1);
        try {
            option.addValueForProcessing("value");
            fail("An exception should be thrown for adding a value when no args are allowed.");
        } catch (RuntimeException e) {
            assertEquals("NO_ARGS_ALLOWED", e.getMessage());
        }
    }

    @org.junit.Test
    public void testAddValueForProcessingWithOneArg() {
        Option option = new Option("opt", "description");
        option.setArgs(1);
        Option optionWithValue = new Option("opt", "description");
        optionWithValue.addValueForProcessing("value");

        option.addValueForProcessing("value");
        List values = option.getValuesList();
        assertEquals(1, values.size());
        assertEquals("value", values.get(0));
    }

    @org.junit.Test
    public void testAddValueForProcessingWithMoreValuesThanAllowed() {
        Option option = new Option("opt", "description");
        option.setArgs(1);
        option.addValueForProcessing("value1");

        try {
            option.addValueForProcessing("value2");
            fail("An exception should be thrown for adding more values than allowed.");
        } catch (RuntimeException e) {
            assertEquals("Cannot add value, list full.", e.getMessage());
        }
    }

    @org.junit.Test
    public void testGetValue() {
        Option option = new Option("opt", "description");
        option.addValueForProcessing("value");

        String value = option.getValue();
        assertEquals("value", value);
    }

    @org.junit.Test
    public void testGetValueWithNullValue() {
        Option option = new Option("opt", "description");
        option.addValueForProcessing(null);

        String value = option.getValue();
        assertNull(value);
    }

    @org.junit.Test
    public void testGetValueWithDefaultValue() {
        Option option = new Option("opt", "description");
        option.addValueForProcessing("value");

        String value = option.getValue("defaultValue");
        assertEquals("value", value);
    }

    @org.junit.Test
    public void testGetValues() {
        Option option = new Option("opt", "description");
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");

        String[] values = option.getValues();
        assertArrayEqual(new String[]{"value1", "value2"}, values);
    }

    @org.junit.Test
    public void testGetValuesList() {
        Option option = new Option("opt", "description");
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");

        List values = option.getValuesList();
        List expectedValues = new ArrayList();
        expectedValues.add("value1");
        expectedValues.add("value2");
        assertEquals(expectedValues, values);
    }

    private void assertArrayEqual(Object[] expected, Object[] actual) {
        if (expected.length != actual.length) {
            fail("Expected and actual arrays have different lengths.");
        }
        for (int i = 0; i < expected.length; i++) {
            if (!expected[i].equals(actual[i])) {
                fail("Expected and actual arrays differ at index " + i + ".");
            }
        }
    }
}