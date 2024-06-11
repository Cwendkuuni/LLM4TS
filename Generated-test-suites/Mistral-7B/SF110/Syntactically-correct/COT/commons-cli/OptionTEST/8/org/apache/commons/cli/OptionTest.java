package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionValidator;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class OptionTest {

    @Test
    public void testOptionConstructorWithOptAndDescription() throws IllegalArgumentException {
        Option option = new Option("opt", "description");
        assertEquals("opt", option.getOpt());
        assertEquals("description", option.getDescription());
        assertNull(option.getLongOpt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionConstructorWithInvalidOpt() throws IllegalArgumentException {
        Option option = new Option("", "description");
        fail("IllegalArgumentException expected");
    }

    @Test
    public void testOptionConstructorWithOptHasArgAndDescription() throws IllegalArgumentException {
        Option option = new Option("opt", true, "description");
        assertEquals("opt", option.getOpt());
        assertEquals("description", option.getDescription());
        assertTrue(option.hasArg());
        assertFalse(option.hasOptionalArg());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionConstructorWithInvalidHasArgValue() throws IllegalArgumentException {
        Option option = new Option("opt", true, "");
        fail("IllegalArgumentException expected");
    }

    @Test
    public void testOptionConstructorWithOptLongNameAndDescription() throws IllegalArgumentException {
        Option option = new Option("opt", "longOpt", "description");
        assertEquals("opt", option.getOpt());
        assertEquals("longOpt", option.getLongOpt());
        assertTrue(option.hasLongOpt());
        assertFalse(option.hasArg());
        assertFalse(option.hasOptionalArg());
    }

    @Test
    public void testOptionGetId() {
        Option option = new Option("opt");
        assertEquals('o', option.getId());
    }

    @Test
    public void testOptionGetKey() {
        Option option = new Option("opt");
        assertEquals("opt", option.getKey());

        Option optionWithLongOpt = new Option("opt", "longOpt");
        assertEquals("opt", optionWithLongOpt.getKey());
        assertEquals("longOpt", optionWithLongOpt.getLongOpt());
    }

    @Test
    public void testOptionGetValue() {
        Option option = new Option("opt");
        assertNull(option.getValue());

        List<String> values = new ArrayList<>();
        values.add("value1");
        option.setValue(values);
        assertEquals("value1", option.getValue());
    }

    @Test
    public void testOptionGetValues() {
        Option option = new Option("opt");
        assertNull(option.getValues());

        List<String> values = new ArrayList<>();
        values.add("value1");
        option.setValue(values);
        String[] expectedValues = {"value1"};
        assertArrayEquals(expectedValues, option.getValues());
    }

    @Test
    public void testOptionSetType() {
        Option option = new Option("opt");
        assertNull(option.getType());

        String type = "String";
        option.setType(type);
        assertEquals(type, option.getType());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testOptionAddValueWithMoreThanAllowedValues() {
        Option option = new Option("opt");
        option.setArgs(1);
        String value1 = "value1";
        String value2 = "value2";
        option.addValue(value1);
        option.addValue(value2);
        fail("UnsupportedOperationException expected");
    }

    @Test
    public void testOptionAddValueWithAllowedValues() {
        Option option = new Option("opt");
        option.setArgs(-1);
        String value1 = "value1";
        String value2 = "value2";
        option.addValue(value1);
        option.addValue(value2);
        List<String> values = option.getValuesList();
        assertEquals(2, values.size());
        assertEquals(value1, values.get(0));
        assertEquals(value2, values.get(1));
    }

    @Test
    public void testOptionAddValueWithValueSeparator() {
        Option option = new Option("opt");
        option.setArgs(-1);
        String value1 = "value1";
        String value2 = "value2";
        option.setValueSeparator(';');
        option.addValue(value1 + ";" + value2);
        List<String> values = option.getValuesList();
        assertEquals(2, values.size());
        assertEquals(value1, values.get(0));
        assertEquals(value2, values.get(1));
    }

    @Test
    public void testOptionEquals() {
        Option option1 = new Option("opt1");
        Option option2 = new Option("opt2");
        assertFalse(option1.equals(option2));

        Option option3 = new Option("opt1");
        assertTrue(option1.equals(option3));
    }

    @Test
    public void testOptionHashCode() {
        Option option1 = new Option("opt1");
        Option option2 = new Option("opt2");
        int hashcode1 = option1.hashCode();
        int hashcode2 = option2.hashCode();
        assertNotSame(hashcode1, hashcode2);

        Option option3 = new Option("opt1");
        assertEquals(hashcode1, option3.hashCode());
    }

    @Test
    public void testOptionToString() {
        Option option = new Option("opt");
        String expected = "[ option: opt :: ]";
        assertEquals(expected, option.toString());

        String longOpt = "longOpt";
        option = new Option("opt", longOpt);
        expected = "[ option: opt :: longOpt ]";
        assertEquals(expected, option.toString());

        String description = "description";
        option = new Option("opt", "", true, description);
        expected = "[ option: opt :: [ARG] :: description ]";
        assertEquals(expected, option.toString());

        String[] values = {"value1", "value2"};
        option.setValues(values);
        option.setValueSeparator(';');
        expected = "[ option: opt :: [value1;value2] ]";
        assertEquals(expected, option.toString());

        String type = "Integer";
        option.setType(type);
        expected = "[ option: opt :: [ARG] :: description :: Integer ]";
        assertEquals(expected, option.toString());
    }
}