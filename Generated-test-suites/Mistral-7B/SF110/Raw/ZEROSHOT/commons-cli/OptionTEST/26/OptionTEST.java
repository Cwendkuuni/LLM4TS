package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.ParseException;
import org.junit.Before;
import org.junit.Test;

public class OptionTest {

    Option option;

    @Before
    public void setUp() {
        option = new Option("o", "opt", false, "Option with no argument");
    }

    @Test
    public void testOptionConstructorWithNoArguments() {
        assertEquals("Option with no argument", option.getDescription());
        assertFalse(option.hasArg());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.isRequired());
        assertNull(option.getValue());
        assertFalse(option.hasLongOpt());
        assertEquals('o', option.getId());
        assertFalse(option.hasValueSeparator());
        assertEquals(0, option.getArgs());
        assertEquals(0, option.getValuesList().size());
        assertFalse(option.hasArgs());
        assertFalse(option.hasArgName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionConstructorWithInvalidOptionName() {
        new Option("", "Description");
    }

    @Test
    public void testOptionHasOptionalArg() {
        Option optionWithOptionalArg = new Option("o", "opt", true, "Option with optional argument");
        assertTrue(optionWithOptionalArg.hasOptionalArg());
        assertFalse(optionWithOptionalArg.isRequired());
        assertNull(optionWithOptionalArg.getValue());
        assertEquals(1, optionWithOptionalArg.getArgs());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionConstructorWithNegativeNumberOfArgs() {
        new Option("o", null, 5, "Option with negative number of arguments");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionConstructorWithNegativeNumberOfValues() {
        new Option("o", null, false, null, -2);
    }

    @Test
    public void testOptionWithSingleValue() {
        option.addValue("value");
        assertEquals("value", option.getValue());
        assertEquals(1, option.getValuesList().size());
        assertEquals("value", option.getValues()[0]);
        assertEquals("value", option.getValuesList().get(0));
    }

    @Test
    public void testOptionWithMultipleValues() {
        option.addValue("value1");
        option.addValue("value2");
        assertEquals(new String[]{"value1", "value2"}, option.getValues());
        assertEquals(2, option.getValuesList().size());
        assertEquals("value1", option.getValuesList().get(0));
        assertEquals("value2", option.getValuesList().get(1));
    }

    @Test
    public void testOptionWithValueSeparator() {
        String valuesSep = ';';
        option.setValueSeparator(valuesSep);
        List<String> expectedValues = new ArrayList<>();
        expectedValues.add("value1");
        expectedValues.add("value2");
        option.addValue("value1");
        option.addValue("value2");
        assertEquals(expectedValues.toArray(new String[expectedValues.size()]), option.getValues());
        assertEquals(2, option.getValuesList().size());
        assertEquals("value1", option.getValuesList().get(0));
        assertEquals("value2", option.getValuesList().get(1));
    }

    @Test(expected = RuntimeException.class)
    public void testAddValueForProcessingWithInvalidNumberOfArgs() {
        option.setArgs(3);
        option.addValue("value");
    }

    @Test
    public void testToString() {
        assertEquals("[ option: o :: Option with no argument ]", option.toString());
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        Option clonedOption = option.clone();
        assertEquals(option.getId(), clonedOption.getId());
        assertEquals(option.getArgs(), clonedOption.getArgs());
        assertEquals(option.getDescription(), clonedOption.getDescription());
        assertEquals(option.getArgName(), clonedOption.getArgName());
        assertEquals(option.hasOptionalArg(), clonedOption.hasOptionalArg());
        assertEquals(option.hasArg(), clonedOption.hasArg());
        assertEquals(option.hasValueSeparator(), clonedOption.hasValueSeparator());
        assertEquals(option.getValue(), clonedOption.getValue());
        assertEquals(option.getValuesList(), clonedOption.getValuesList());
    }

    @Test(expected = RuntimeException.class)
    public void testAddValueForProcessingWithFullValuesList() {
        option.setArgs(2);
        option.addValue("value1");
        option.addValue("value2");
        option.addValue("value3");
    }

    @Test
    public void testEquals() {
        Option option1 = new Option("o", "opt", false, "Option with no argument");
        Option option2 = new Option("o", "opt", false, "Option with no argument");
        assertTrue(option1.equals(option2));

        Option option3 = new Option("o", "opt1", false, "Option with no argument");
        assertFalse(option1.equals(option3));

        Option option4 = new Option("o1", "opt", false, "Option with no argument");
        assertFalse(option1.equals(option4));
    }

    @Test
    public void testHashCode() {
        Option option1 = new Option("o", "opt", false, "Option with no argument");
        Option option2 = new Option("o", "opt", false, "Option with no argument");
        assertEquals(option1.hashCode(), option2.hashCode());

        Option option3 = new Option("o", "opt1", false, "Option with no argument");
        assertNotEquals(option1.hashCode(), option3.hashCode());

        Option option4 = new Option("o1", "opt", false, "Option with no argument");
        assertNotEquals(option1.hashCode(), option4.hashCode());
    }

    @Test
    public void testClearValues() {
        option.addValue("value1");
        option.clearValues();
        assertNull(option.getValue());
        assertTrue(option.getValuesList().isEmpty());
    }
}