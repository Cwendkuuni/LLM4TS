package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.ParseException;
import org.junit.Test;

public class OptionTest {

    @Test
    public void testBasicOptionConstruction() {
        Option option = new Option("o", "opt", false, "Option description");
        assertEquals("o", option.getOpt());
        assertEquals("opt", option.getLongOpt());
        assertFalse(option.hasArg());
        assertNull(option.getValue());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.isRequired());
        assertEquals("Option description", option.getDescription());
        assertNull(option.getArgName());
        assertFalse(option.hasArgs());
        assertFalse(option.hasValueSeparator());
        assertTrue(option.toString().contains("Option description"));
    }

    @Test
    public void testOptionWithArgumentConstruction() {
        Option option = new Option("o", "opt", true, "Option description");
        assertEquals("o", option.getOpt());
        assertEquals("opt", option.getLongOpt());
        assertEquals("arg", option.getArgName());
        assertEquals(1, option.getArgs());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.isRequired());
        assertEquals("Option description", option.getDescription());
        assertNull(option.getValue());
        assertTrue(option.toString().contains("ARG"));
    }

    @Test
    public void testOptionWithMultiArgumentConstruction() {
        Option option = new Option("o", "opt", -2, "Option description");
        assertEquals("o", option.getOpt());
        assertEquals("opt", option.getLongOpt());
        assertEquals("args", option.getArgName());
        assertEquals(-2, option.getArgs());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.isRequired());
        assertEquals("Option description", option.getDescription());
        assertNull(option.getValue());
        assertFalse(option.toString().contains("ARG"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidOptionConstruction() {
        new Option(null, "opt", false, "Option description");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidOptionConstructionWithLongOpt() {
        new Option("o", null, false, "Option description");
    }

    @Test
    public void testIdGetters() {
        Option option = new Option("o", "opt", false, "Option description");
        assertEquals('o', option.getId());
        Option longOption = new Option("--long-opt", "longOpt", false, "Option description");
        assertEquals('-', longOption.getId());
        assertEquals('L', longOption.getId(true));
    }

    @Test
    public void testGettersAndSetters() {
        Option option = new Option("o", "opt", false, "Option description");
        option.setArgName("argName");
        option.setDescription("new description");
        option.setRequired(true);
        option.setOptionalArg(true);
        option.setArgs(2);
        option.setValueSeparator(';');
        assertEquals("argName", option.getArgName());
        assertEquals("new description", option.getDescription());
        assertTrue(option.isRequired());
        assertTrue(option.hasOptionalArg());
        assertEquals(2, option.getArgs());
        assertEquals(';', option.getValueSeparator());
        assertFalse(option.hasValueSeparator());
    }

    @Test
    public void testGettersAndSettersWithValues() {
        Option option = new Option("o", "opt", false, "Option description");
        option.setValue("value");
        option.setValue("newValue");
        assertEquals("value", option.getValue());
        assertEquals("newValue", option.getValue());
        Option optionWithValues = new Option("o", "opt", false, "Option description", "value1", "value2", "value3");
        assertEquals("value1", optionWithValues.getValue(0));
        assertEquals("value2", optionWithValues.getValue(1));
        assertEquals("value3", optionWithValues.getValue(2));
        List<String> values = optionWithValues.getValuesList();
        assertEquals(3, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
        assertEquals("value3", values.get(2));
        List<String> expectedValues = new ArrayList<>();
        expectedValues.add("value1");
        expectedValues.add("value2");
        expectedValues.add("value3");
        assertSame(expectedValues, optionWithValues.getValues());
    }

    @Test(expected = RuntimeException.class)
    public void testAddValueWithNoArgsAllowed() {
        Option option = new Option("o", "opt", false, "Option description");
        option.setArgs(0);
        option.addValue("value");
    }

    @Test
    public void testAddValuesWithMultipleArgs() {
        Option option = new Option("o", "opt", -2, "Option description");
        option.setValueSeparator(';');
        option.addValue("value1");
        option.addValue("value2");
        List<String> expectedValues = new ArrayList<>();
        expectedValues.add("value1");
        expectedValues.add("value2");
        assertSame(expectedValues, option.getValuesList());
        assertEquals("value1;value2", option.toString());
    }

    @Test
    public void testEqualsAndHashCode() {
        Option option1 = new Option("o", "opt", false, "Option description");
        Option option2 = new Option("o", "opt", false, "Option description");
        Option option3 = new Option("o1", "opt", false, "Option description");
        Option option4 = new Option("o", "opt1", false, "Option description");
        assertTrue(option1.equals(option2));
        assertTrue(option1.hashCode() == option2.hashCode());
        assertFalse(option1.equals(option3));
        assertFalse(option1.equals(option4));
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        Option option = new Option("o", "opt", false, "Option description");
        Option clonedOption = (Option)option.clone();
        assertEquals("o", clonedOption.getOpt());
        assertEquals("opt", clonedOption.getLongOpt());
        assertEquals(false, clonedOption.hasArg());
        assertEquals(false, clonedOption.hasOptionalArg());
        assertEquals(false, clonedOption.isRequired());
        assertEquals("Option description", clonedOption.getDescription());
        assertEquals(null, clonedOption.getValue());
    }

    @Test
    public void testClearValues() {
        Option option = new Option("o", "opt", false, "Option description");
        option.setValue("value1");
        option.setValue("value2");
        option.clearValues();
        assertNull(option.getValue());
        assertEquals(0, option.getValuesList().size());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddValueWithSetter() {
        Option option = new Option("o", "opt", false, "Option description");
        option.setArgs(1);
        option.addValue("value");
    }
}