package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.ParseException;
import org.junit.Test;

public class OptionTest {

    @Test
    public void testOptionConstruction() throws IllegalArgumentException {
        Option option = new Option("opt", "description");
        assertEquals("opt", option.getOpt());
        assertEquals("description", option.getDescription());
        assertNull(option.getLongOpt());
        assertFalse(option.hasArg());
        assertFalse(option.isRequired());
        assertNull(option.getType());
        assertEquals(0, option.getArgs());
        assertFalse(option.hasValueSeparator());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.hasLongOpt());
        assertFalse(option.hasArgName());
        assertNull(option.getArgName());
        assertTrue(option.hasArgs());
        assertFalse(option.hasValues());
        assertFalse(option.isSet());
    }

    @Test
    public void testOptionConstructionWithLongOpt() throws IllegalArgumentException {
        Option option = new Option("opt", "long-opt", false, "description");
        assertEquals("opt", option.getOpt());
        assertEquals("long-opt", option.getLongOpt());
        assertFalse(option.hasArg());
        assertFalse(option.isRequired());
        assertNull(option.getType());
        assertEquals(0, option.getArgs());
        assertFalse(option.hasValueSeparator());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.hasLongOpt());
        assertFalse(option.hasArgName());
        assertNull(option.getArgName());
        assertTrue(option.hasArgs());
        assertFalse(option.hasValues());
        assertFalse(option.isSet());
    }

    @Test
    public void testOptionConstructionWithArg() throws IllegalArgumentException {
        Option option = new Option("opt", false, "description");
        assertEquals("opt", option.getOpt());
        assertFalse(option.hasArg());
        assertFalse(option.isRequired());
        assertNull(option.getType());
        assertEquals(0, option.getArgs());
        assertFalse(option.hasValueSeparator());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.hasLongOpt());
        assertFalse(option.hasArgName());
        assertNull(option.getArgName());
        assertFalse(option.hasArgs());
        assertFalse(option.hasValues());
        assertFalse(option.isSet());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidOptionConstruction() throws IllegalArgumentException {
        new Option(null, "description");
    }

    @Test
    public void testOptionGettersAndSetters() {
        Option option = new Option("opt", "description");
        option.setDescription("new-description");
        assertEquals("new-description", option.getDescription());
        option.setLongOpt("new-long-opt");
        assertEquals("new-long-opt", option.getLongOpt());
        option.setArgName("new-arg-name");
        assertEquals("new-arg-name", option.getArgName());
        option.setType(Integer.class);
        assertEquals(Integer.class, option.getType());
        option.setRequired(true);
        assertTrue(option.isRequired());
        option.setOptionalArg(true);
        assertTrue(option.hasOptionalArg());
        option.setHasArg(true);
        assertTrue(option.hasArg());
        option.setNumberOfArgs(3);
        assertEquals(3, option.getArgs());
        option.setValueSeparator('_');
        assertTrue(option.hasValueSeparator());
        option.setValueSeparator((char)0);
        assertTrue(option.hasValueSeparator());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidOptionGetter() {
        Option option = new Option("opt", "description");
        option.setArgName("new-arg-name");
        option.getArgName("invalid-name");
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
        Option option3 = new Option("opt2", "description2");
        int hashCode1 = option1.hashCode();
        int hashCode2 = option1.hashCode();
        int hashCode3 = option2.hashCode();
        int hashCode4 = option3.hashCode();
        assertEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
        assertNotEquals(hashCode1, hashCode4);
    }

    @Test
    public void testOptionClone() {
        Option option = new Option("opt", "description");
        Option clonedOption = option.clone();
        assertEquals(option, clonedOption);
        assertNotSame(option, clonedOption);
    }

    @Test
    public void testOptionClearValues() {
        Option option = new Option("opt", "description");
        option.addValue("value1");
        option.addValue("value2");
        option.clearValues();
        assertFalse(option.hasValues());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testOptionAddValue() {
        Option option = new Option("opt", "description");
        option.addValue("value");
    }

    @Test
    public void testOptionAddValues() throws ParseException {
        Option option = new Option("opt", "description");
        option.setArgs(2);
        option.setValueSeparator('-');
        option.addValues(new String[]{"value1", "value2"});
        String expectedValue = "value1-value2";
        String actualValue = option.getValue();
        assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testOptionGetValue() {
        Option option = new Option("opt", "description");
        option.addValue("value");
        String expectedValue = "value";
        String actualValue = option.getValue();
        assertEquals(expectedValue, actualValue);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testOptionGetValueOutOfBounds() {
        Option option = new Option("opt", "description");
        option.addValue("value1");
        option.addValue("value2");
        String actualValue = option.getValue(1);
    }

    @Test
    public void testOptionGetValues() {
        Option option = new Option("opt", "description");
        option.setArgs(2);
        option.setValueSeparator('-');
        option.addValues(new String[]{"value1", "value2"});
        String[] expectedValues = {"value1", "value2"};
        String[] actualValues = option.getValues();
        assertArrayEquals(expectedValues, actualValues);
    }

    @Test
    public void testOptionGetValuesList() {
        Option option = new Option("opt", "description");
        option.setArgs(2);
        option.setValueSeparator('-');
        option.addValues(new String[]{"value1", "value2"});
        List<String> expectedValues = new ArrayList<>();
        expectedValues.add("value1");
        expectedValues.add("value2");
        List<String> actualValues = option.getValuesList();
        assertEquals(expectedValues, actualValues);
    }

    @Test
    public void testOptionToString() {
        Option option = new Option("opt", "description");
        String expectedString = "[ option: opt :: description ]";
        String actualString = option.toString();
        assertEquals(expectedString, actualString);
    }

    @Test
    public void testOptionHasNoValues() {
        Option option = new Option("opt", "description");
        assertTrue(option.hasNoValues());
        option.addValue("value");
        assertFalse(option.hasNoValues());
    }

    @Test
    public void testAddValueForProcessingWithNoArgsAllowed() throws ParseException {
        Option option = new Option("opt", "description");
        option.setArgs(-1);
        String value = "value";
        try {
            option.addValueForProcessing(value);
            fail("An exception should have been thrown");
        } catch (RuntimeException e) {
            assertEquals("NO_ARGS_ALLOWED", e.getMessage());
        }
    }

    @Test
    public void testAddValueForProcessingWithOneArg() throws ParseException {
        Option option = new Option("opt", "description");
        option.setArgs(1);
        String value = "value";
        option.addValueForProcessing(value);
        assertEquals(value, option.getValue());
    }

    @Test
    public void testAddValueForProcessingWithMultipleValues() throws ParseException {
        Option option = new Option("opt", "description");
        option.setArgs(2);
        String value1 = "value1";
        String value2 = "value2";
        option.addValueForProcessing(value1);
        option.addValueForProcessing(value2);
        List<String> expectedValues = new ArrayList<>();
        expectedValues.add(value1);
        expectedValues.add(value2);
        List<String> actualValues = option.getValuesList();
        assertEquals(expectedValues, actualValues);
    }

    @Test
    public void testAddValueForProcessingWithValueSeparator() throws ParseException {
        Option option = new Option("opt", "description");
        option.setArgs(2);
        option.setValueSeparator('-');
        String value1 = "value1";
        String value2 = "value2";
        option.addValueForProcessing(value1);
        option.addValueForProcessing(value2);
        String expectedValue = value1 + "-" + value2;
        String actualValue = option.getValue();
        assertEquals(expectedValue, actualValue);
    }
}