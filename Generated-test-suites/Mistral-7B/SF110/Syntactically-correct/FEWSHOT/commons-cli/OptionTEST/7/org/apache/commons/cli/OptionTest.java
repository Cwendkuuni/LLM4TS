package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class OptionTest {

    @Test
    public void testDefaultConstructor() {
        Option option = new Option();
        assertNull(option.getOpt());
        assertNull(option.getLongOpt());
        assertNull(option.getDescription());
        assertFalse(option.isRequired());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.hasArg());
        assertNull(option.getType());
        assertNull(option.getValues());
        assertEquals(0, option.getArgs());
        assertFalse(option.hasValueSeparator());
    }

    @Test
    public void testConstructorWithOpt() {
        Option option = new Option("opt", "description");
        assertEquals("opt", option.getOpt());
        assertNull(option.getLongOpt());
        assertEquals("description", option.getDescription());
        assertFalse(option.isRequired());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.hasArg());
        assertNull(option.getType());
        assertNull(option.getValues());
        assertEquals(0, option.getArgs());
        assertFalse(option.hasValueSeparator());
    }

    @Test
    public void testConstructorWithOptAndHasArg() {
        Option option = new Option("opt", true, "description");
        assertEquals("opt", option.getOpt());
        assertNull(option.getLongOpt());
        assertEquals("description", option.getDescription());
        assertTrue(option.isRequired());
        assertTrue(option.hasOptionalArg());
        assertEquals(1, option.getArgs());
        assertFalse(option.hasValueSeparator());
    }

    @Test
    public void testConstructorWithOptLong() {
        Option option = new Option("opt", "--optLong", true, "description");
        assertEquals("opt", option.getOpt());
        assertEquals("--optLong", option.getLongOpt());
        assertEquals("description", option.getDescription());
        assertTrue(option.isRequired());
        assertTrue(option.hasOptionalArg());
        assertEquals(1, option.getArgs());
        assertFalse(option.hasValueSeparator());
    }

    @Test
    public void testGetId() {
        Option option = new Option("opt");
        assertEquals('o', option.getId());
    }

    @Test
    public void testGetKey() {
        Option option = new Option("opt");
        assertEquals("opt", option.getKey());
        Option optionLong = new Option(null, "--optLong");
        assertEquals("optLong", optionLong.getKey());
    }

    @Test
    public void testGetOpt() {
        Option option = new Option("opt");
        assertEquals("opt", option.getOpt());
        Option optionLong = new Option(null, "--optLong");
        assertNull(optionLong.getOpt());
    }

    @Test
    public void testGetType() {
        Option option = new Option("opt");
        assertNull(option.getType());
        Option optionWithType = new Option("opt", null, null, null, "String.class");
        assertEquals(String.class, optionWithType.getType());
    }

    @Test
    public void testSetType() {
        Option option = new Option("opt");
        assertNull(option.getType());
        option.setType("String.class");
        assertEquals("String.class", option.getType());
    }

    @Test
    public void testGetLongOpt() {
        Option option = new Option("opt");
        assertNull(option.getLongOpt());
        Option optionLong = new Option(null, "--optLong");
        assertEquals("--optLong", optionLong.getLongOpt());
    }

    @Test
    public void testSetLongOpt() {
        Option option = new Option("opt");
        assertNull(option.getLongOpt());
        option.setLongOpt("--optLong");
        assertEquals("--optLong", option.getLongOpt());
    }

    @Test
    public void testSetOptionalArgFalse() {
        Option option = new Option("opt");
        assertFalse(option.hasOptionalArg());
        option.setOptionalArg(false);
        assertFalse(option.hasOptionalArg());
    }

    @Test
    public void testSetOptionalArgTrue() {
        Option option = new Option("opt");
        assertFalse(option.hasOptionalArg());
        option.setOptionalArg(true);
        assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testHasOptionalArg() {
        Option option = new Option("opt");
        assertFalse(option.hasOptionalArg());
        Option optionWithArg = new Option("opt", true, null, null);
        assertTrue(optionWithArg.hasOptionalArg());
    }

    @Test
    public void testHasLongOpt() {
        Option option = new Option("opt");
        assertFalse(option.hasLongOpt());
        Option optionLong = new Option(null, "--optLong");
        assertTrue(optionLong.hasLongOpt());
    }

    @Test
    public void testHasArg() {
        Option option = new Option("opt");
        assertFalse(option.hasArg());
        Option optionWithArg = new Option("opt", true, null, null);
        assertTrue(optionWithArg.hasArg());
    }

    @Test
    public void testSetArgName() {
        Option option = new Option("opt");
        assertNull(option.getArgName());
        option.setArgName("argName");
        assertEquals("argName", option.getArgName());
    }

    @Test
    public void testGetArgName() {
        Option option = new Option("opt");
        assertNull(option.getArgName());
        Option optionWithArgName = new Option("opt", null, false, null, null, "argName");
        assertEquals("argName", optionWithArgName.getArgName());
    }

    @Test
    public void testHasArgName() {
        Option option = new Option("opt");
        assertFalse(option.hasArgName());
        Option optionWithArgName = new Option("opt", null, false, null, null, "argName");
        assertTrue(optionWithArgName.hasArgName());
    }

    @Test
    public void testGetDescription() {
        Option option = new Option("opt");
        assertNull(option.getDescription());
        Option optionWithDescription = new Option("opt", null, false, "description");
        assertEquals("description", optionWithDescription.getDescription());
    }

    @Test
    public void testSetDescription() {
        Option option = new Option("opt");
        assertNull(option.getDescription());
        option.setDescription("description");
        assertEquals("description", option.getDescription());
    }

    @Test
    public void testIsRequired() {
        Option option = new Option("opt");
        assertFalse(option.isRequired());
        Option optionRequired = new Option("opt", true, null, null);
        assertTrue(optionRequired.isRequired());
    }

    @Test
    public void testSetRequired() {
        Option option = new Option("opt");
        assertFalse(option.isRequired());
        option.setRequired(true);
        assertTrue(option.isRequired());
    }

    @Test
    public void testToString() {
        Option option = new Option("opt");
        assertEquals("[ option: opt :: description ]", option.toString());
        Option optionWithArg = new Option("opt", true, null, null);
        assertEquals("[ option: opt [ARG] :: description ]", optionWithArg.toString());
        Option optionWithLongOpt = new Option(null, "--optLong", true, null);
        assertEquals("[ option: optLong :: description ]", optionWithLongOpt.toString());
    }

    @Test
    public void testHasNoValues() {
        Option option = new Option("opt");
        assertTrue(option.hasNoValues());
        Option optionWithValue = new Option("opt", null, false, null, null, null, new ArrayList<>());
        assertFalse(optionWithValue.hasNoValues());
    }

    @Test
    public void testEquals() {
        Option opt1 = new Option("opt1");
        Option opt2 = new Option("opt1");
        assertTrue(opt1.equals(opt2));
        Option opt3 = new Option("opt2");
        assertFalse(opt1.equals(opt3));
    }

    @Test
    public void testHashCode() {
        Option opt1 = new Option("opt1");
        int hashCode1 = opt1.hashCode();
        Option opt2 = new Option("opt1");
        int hashCode2 = opt2.hashCode();
        assertEquals(hashCode1, hashCode2);
        Option opt3 = new Option("opt2");
        int hashCode3 = opt3.hashCode();
        assertNotEquals(hashCode1, hashCode3);
    }

    @Test
    public void testClone() {
        Option opt = new Option("opt", "--optLong", true, null);
        Option cloned = opt.clone();
        assertEquals(opt.getOpt(), cloned.getOpt());
        assertEquals(opt.getLongOpt(), cloned.getLongOpt());
        assertEquals(opt.isRequired(), cloned.isRequired());
        assertEquals(opt.hasOptionalArg(), cloned.hasOptionalArg());
        assertEquals(opt.getArgs(), cloned.getArgs());
        assertEquals(opt.hasValueSeparator(), cloned.hasValueSeparator());
        assertEquals(opt.getValueSeparator(), cloned.getValueSeparator());
        assertEquals(opt.getType(), cloned.getType());
        assertEquals(opt.getValues(), cloned.getValues());
    }

    @Test
    public void testClearValues() {
        Option opt = new Option("opt", null, false, null, null, null, new ArrayList<>());
        opt.clearValues();
        assertTrue(opt.hasNoValues());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddValue() {
        Option opt = new Option("opt");
        opt.addValue("value");
        fail("This method should throw an UnsupportedOperationException.");
    }

    @Test
    public void testAddValueForProcessing() {
        Option opt = new Option("opt", null, false, null, null, null, new ArrayList<>());
        opt.addValueForProcessing("value1");
        opt.addValueForProcessing("value2");
        List<String> values = opt.getValuesList();
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test(expected = RuntimeException.class)
    public void testAddValueForProcessingTooManyValues() {
        Option opt = new Option("opt", null, false, null, null, null, new ArrayList<>());
        opt.numberOfArgs = 1;
        opt.addValueForProcessing("value1");
        opt.addValueForProcessing("value2");
        opt.addValueForProcessing("value3");
        fail("This method should throw a RuntimeException.");
    }

    @Test
    public void testProcessValue() {
        Option opt = new Option("opt", null, false, null, null, null, new ArrayList<>());
        opt.processValue("value1 --anotherValue");
        List<String> values = opt.getValuesList();
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("anotherValue", values.get(1));
    }

    @Test
    public void testAdd() {
        Option opt = new Option("opt", null, false, null, null, null, new ArrayList<>());
        opt.add("value1");
        opt.add("value2");
        List<String> values = opt.getValuesList();
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test(expected = RuntimeException.class)
    public void testAddTooManyValues() {
        Option opt = new Option("opt", null, false, null, null, null, new ArrayList<>());
        opt.numberOfArgs = 1;
        opt.add("value1");
        opt.add("value2");
        opt.add("value3");
        fail("This method should throw a RuntimeException.");
    }

    @Test
    public void testGetValue() {
        Option opt = new Option("opt", null, false, null, null, null, Arrays.asList("value1"));
        assertEquals("value1", opt.getValue());
        Option optWithNoValues = new Option("opt", null, false, null, null, null, Arrays.asList());
        assertNull(optWithNoValues.getValue());
    }

    @Test
    public void testGetValueIndexOutOfBounds() {
        Option opt = new Option("opt", null, false, null, null, null, Arrays.asList("value1", "value2"));
        try {
            opt.getValue(1);
            fail("This method should throw an IndexOutOfBoundsException.");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }

    @Test
    public void testGetValues() {
        Option opt = new Option("opt", null, false, null, null, null, Arrays.asList("value1", "value2"));
        String[] values = opt.getValues();
        assertEquals(2, values.length);
        assertEquals("value1", values[0]);
        assertEquals("value2", values[1]);
        Option optWithNoValues = new Option("opt", null, false, null, null, null, Arrays.asList());
        assertNull(optWithNoValues.getValues());
    }

    @Test
    public void testGetValuesList() {
        Option opt = new Option("opt", null, false, null, null, null, Arrays.asList("value1", "value2"));
        List<String> values = opt.getValuesList();
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
        Option optWithNoValues = new Option("opt", null, false, null, null, null, Arrays.asList());
        assertEquals(0, optWithNoValues.getValuesList().size());
    }
}