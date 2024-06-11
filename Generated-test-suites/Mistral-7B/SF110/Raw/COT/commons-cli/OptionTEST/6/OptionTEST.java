package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.cli.Option;

import org.junit.Test;

public class OptionTest {

    @Test
    public void testOptionWithNoArguments() throws IllegalArgumentException {
        Option option = new Option("a", "option-a", false, "This is option A");
        assertEquals("option-a", option.getOpt());
        assertEquals("This is option A", option.getDescription());
        assertFalse(option.hasArg());
        assertFalse(option.hasOptionalArg());
        assertNull(option.getValue());
        assertFalse(option.hasLongOpt());
        assertFalse(option.isRequired());
        assertFalse(option.hasArgName());
        assertEquals(-1, option.getArgs());
        assertFalse(option.hasValueSeparator());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionWithInvalidOpt() throws IllegalArgumentException {
        new Option("", "option-a", false, "This is option A");
    }

    @Test
    public void testOptionWithOneArgument() throws IllegalArgumentException {
        Option option = new Option("b", "option-b", true, "This is option B with an argument");
        assertEquals("option-b", option.getOpt());
        assertEquals("This is option B with an argument", option.getDescription());
        assertTrue(option.hasArg());
        assertTrue(option.hasOptionalArg());
        assertNull(option.getValue());
        assertFalse(option.hasLongOpt());
        assertFalse(option.isRequired());
        assertFalse(option.hasArgName());
        assertEquals(1, option.getArgs());
        assertFalse(option.hasValueSeparator());
    }

    @Test
    public void testOptionWithOneLongOption() throws IllegalArgumentException {
        Option option = new Option("c", null, false, "This is option C");
        assertEquals(null, option.getOpt());
        assertEquals("This is option C", option.getDescription());
        assertFalse(option.hasArg());
        assertFalse(option.hasOptionalArg());
        assertNull(option.getValue());
        assertTrue(option.hasLongOpt());
        assertEquals("option-C", option.getLongOpt());
        assertFalse(option.isRequired());
        assertFalse(option.hasArgName());
        assertEquals(-1, option.getArgs());
        assertFalse(option.hasValueSeparator());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionWithInvalidNumberOfArgs() throws IllegalArgumentException {
        new Option("d", null, 3, "This is option D");
    }

    @Test
    public void testOptionWithMultipleArguments() throws IllegalArgumentException {
        Option option = new Option("e", "option-e", true, "This is option E with multiple arguments");
        assertEquals("option-e", option.getOpt());
        assertEquals("This is option E with multiple arguments", option.getDescription());
        assertTrue(option.hasArg());
        assertTrue(option.hasOptionalArg());
        assertNull(option.getValue());
        assertFalse(option.hasLongOpt());
        assertFalse(option.isRequired());
        assertFalse(option.hasArgName());
        assertEquals(-1, option.getArgs());
        assertFalse(option.hasValueSeparator());

        List<String> expectedValues = new ArrayList<>(Arrays.asList("arg1", "arg2"));
        option.addValueForProcessing("arg1 arg2");
        List<String> actualValues = option.getValuesList();
        assertEquals(expectedValues, actualValues);
    }

    @Test
    public void testOptionWithValueSeparator() throws IllegalArgumentException {
        Option option = new Option("f", null, false, "This is option F with a value separator");
        assertEquals(null, option.getOpt());
        assertEquals("This is option F with a value separator", option.getDescription());
        assertFalse(option.hasArg());
        assertFalse(option.hasOptionalArg());
        assertNull(option.getValue());
        assertFalse(option.hasLongOpt());
        assertFalse(option.isRequired());
        assertFalse(option.hasArgName());
        assertEquals(-1, option.getArgs());
        assertTrue(option.hasValueSeparator());
        assertEquals(',', option.getValueSeparator());

        List<String> expectedValues = new ArrayList<>(Arrays.asList("val1", "val2"));
        option.addValueForProcessing("val1,val2");
        List<String> actualValues = option.getValuesList();
        assertEquals(expectedValues, actualValues);
    }

    @Test(expected = RuntimeException.class)
    public void testOptionWithInvalidNumberOfValues() throws IllegalArgumentException {
        Option option = new Option("g", null, 1, "This is option G");
        option.addValueForProcessing("val1,val2");
    }

    @Test
    public void testOptionWithRequiredArgument() throws IllegalArgumentException {
        Option option = new Option("h", "option-h", true, "This is option H with a required argument", true);
        assertEquals("option-h", option.getOpt());
        assertEquals("This is option H with a required argument", option.getDescription());
        assertTrue(option.hasArg());
        assertTrue(option.hasOptionalArg());
        assertNull(option.getValue());
        assertFalse(option.hasLongOpt());
        assertTrue(option.isRequired());
        assertFalse(option.hasArgName());
        assertEquals(-1, option.getArgs());
        assertFalse(option.hasValueSeparator());

        String requiredValue = "arg1";
        option.addValueForProcessing(requiredValue);
        String actualValue = option.getValue();
        assertEquals(requiredValue, actualValue);
    }

    @Test
    public void testOptionWithOptionalArgument() throws IllegalArgumentException {
        Option option = new Option("i", "option-i", true, "This is option I with an optional argument", false);
        assertEquals("option-i", option.getOpt());
        assertEquals("This is option I with an optional argument", option.getDescription());
        assertTrue(option.hasArg());
        assertTrue(option.hasOptionalArg());
        assertNull(option.getValue());
        assertFalse(option.hasLongOpt());
        assertFalse(option.isRequired());
        assertFalse(option.hasArgName());
        assertEquals(-1, option.getArgs());
        assertFalse(option.hasValueSeparator());

        String optionalValue = "arg1";
        option.addValueForProcessing(optionalValue);
        String actualValue = option.getValue();
        assertNull(actualValue);
        List<String> actualValues = option.getValuesList();
        assertEquals(Optional.of(optionalValue), Arrays.asList(optionalValue).stream().map(Optional::of).collect(Collectors.toList()));
        assertEquals(optionalValue, option.getValue(optionalValue));
    }

    @Test
    public void testOptionWithArgName() throws IllegalArgumentException {
        Option option = new Option("j", null, false, "This is option J", "argName");
        assertEquals("argName", option.getArgName());
        assertEquals("This is option J", option.getDescription());
        assertFalse(option.hasArg());
        assertFalse(option.hasOptionalArg());
        assertNull(option.getValue());
        assertFalse(option.hasLongOpt());
        assertFalse(option.isRequired());
        assertTrue(option.hasArgName());
        assertEquals(-1, option.getArgs());
        assertFalse(option.hasValueSeparator());
    }

    @Test
    public void testOptionEquals() {
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("a", "option1", false, "Option 1");
        Option option3 = new Option("b", "option1", false, "Option 1");

        assertTrue(option1.equals(option1));
        assertTrue(option1.equals(option2));
        assertFalse(option1.equals(option3));
    }

    @Test
    public void testOptionHashCode() {
        Option option1 = new Option("a", "option1", false, "Option 1");
        Option option2 = new Option("a", "option1", false, "Option 1");
        Option option3 = new Option("b", "option1", false, "Option 1");

        assertEquals(option1.hashCode(), option1.hashCode());
        assertEquals(option1.hashCode(), option2.hashCode());
        assertNotEquals(option1.hashCode(), option3.hashCode());
    }

}