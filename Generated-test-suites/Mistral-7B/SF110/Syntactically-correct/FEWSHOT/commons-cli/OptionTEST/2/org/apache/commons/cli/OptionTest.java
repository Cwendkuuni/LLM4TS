package org.apache.commons.cli;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class OptionTest {

    @Test
    public void testOptionConstructor() {
        Option option = new Option("opt", "Description");
        assertEquals("opt", option.getOpt());
        assertEquals("Description", option.getDescription());
        assertNull(option.getLongOpt());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.hasArg());
        assertFalse(option.isRequired());
        assertFalse(option.hasArgName());
        assertFalse(option.hasArgs());
        assertEquals(-1, option.getArgs());
        assertNull(option.getType());
        assertNull(option.getValues());
        assertFalse(option.hasValueSeparator());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionConstructorWithNullDescription() {
        new Option(null, "Description");
    }

    @Test
    public void testOptionConstructorWithArg() {
        Option option = new Option("opt", true, "Description");
        assertEquals("opt", option.getOpt());
        assertEquals("Description", option.getDescription());
        assertTrue(option.hasOptionalArg());
        assertFalse(option.hasArg());
        assertFalse(option.isRequired());
        assertFalse(option.hasArgName());
        assertFalse(option.hasArgs());
        assertEquals(1, option.getArgs());
        assertNull(option.getType());
        assertNull(option.getValues());
        assertFalse(option.hasValueSeparator());
    }

    @Test
    public void testOptionConstructorWithLongOpt() {
        Option option = new Option("opt", null, false, "Description");
        assertEquals("opt", option.getOpt());
        assertNull(option.getLongOpt());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.hasArg());
        assertFalse(option.isRequired());
        assertFalse(option.hasArgName());
        assertFalse(option.hasArgs());
        assertEquals(-1, option.getArgs());
        assertNull(option.getType());
        assertNull(option.getValues());
        assertFalse(option.hasValueSeparator());

        Option option2 = new Option("opt", "--long-opt", false, "Description");
        assertEquals("opt", option2.getOpt());
        assertEquals("--long-opt", option2.getLongOpt());
        assertFalse(option2.hasOptionalArg());
        assertFalse(option2.hasArg());
        assertFalse(option2.isRequired());
        assertFalse(option2.hasArgName());
        assertFalse(option2.hasArgs());
        assertEquals(-1, option2.getArgs());
        assertNull(option2.getType());
        assertNull(option2.getValues());
        assertFalse(option2.hasValueSeparator());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionConstructorWithFalseHasArgAndNoLongOpt() {
        new Option("opt", false, null);
    }

    @Test
    public void testOptionConstructorWithArgAndLongOpt() {
        Option option = new Option("opt", null, true, "Description");
        assertEquals("opt", option.getOpt());
        assertNull(option.getLongOpt());
        assertTrue(option.hasOptionalArg());
        assertTrue(option.hasArg());
        assertFalse(option.isRequired());
        assertFalse(option.hasArgName());
        assertFalse(option.hasArgs());
        assertEquals(1, option.getArgs());
        assertNull(option.getType());
        assertNull(option.getValues());
        assertFalse(option.hasValueSeparator());

        Option option2 = new Option("opt", "--long-opt", true, "Description");
        assertEquals("opt", option2.getOpt());
        assertEquals("--long-opt", option2.getLongOpt());
        assertTrue(option2.hasOptionalArg());
        assertTrue(option2.hasArg());
        assertFalse(option2.isRequired());
        assertFalse(option2.hasArgName());
        assertFalse(option2.hasArgs());
        assertEquals(1, option2.getArgs());
        assertNull(option2.getType());
        assertNull(option2.getValues());
        assertFalse(option2.hasValueSeparator());
    }

    @Test
    public void testOptionGetId() {
        Option option = new Option("opt", null, false, "Description");
        assertEquals('o', option.getId());
    }

    @Test
    public void testOptionGetKey() {
        Option option = new Option("opt", null, false, "Description");
        assertEquals("opt", option.getKey());

        Option option2 = new Option("opt", "--long-opt", false, "Description");
        assertEquals("opt", option2.getKey());
    }

    @Test
    public void testOptionGetOpt() {
        Option option = new Option("opt", null, false, "Description");
        assertEquals("opt", option.getOpt());

        Option option2 = new Option("opt", "--long-opt", false, "Description");
        assertEquals("opt", option2.getOpt());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testOptionAddValueWithNoArgs() {
        Option option = new Option("opt", null, false, "Description");
        option.addValueForProcessing("value");
    }

    @Test
    public void testOptionAddValueForProcessing() {
        Option option = new Option("opt", null, false, "Description");
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        List values = option.getValuesList();
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testOptionGetValue() {
        Option option = new Option("opt", null, false, "Description");
        option.addValueForProcessing("value");
        assertEquals("value", option.getValue());
    }

    @Test
    public void testOptionGetValueWithIndex() {
        Option option = new Option("opt", null, false, "Description");
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        assertEquals("value1", option.getValue(0));
        assertEquals("value2", option.getValue(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testOptionGetValueWithNegativeIndex() {
        Option option = new Option("opt", null, false, "Description");
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        option.getValue(-1);
    }

    @Test
    public void testOptionGetStringValues() {
        Option option = new Option("opt", null, false, "Description");
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        String[] values = option.getValues();
        assertEquals(2, values.length);
        assertEquals("value1", values[0]);
        assertEquals("value2", values[1]);
    }

    @Test
    public void testOptionGetListValues() {
        Option option = new Option("opt", null, false, "Description");
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        List values = option.getValuesList();
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testOptionToString() {
        Option option = new Option("opt", null, false, "Description");
        assertEquals("[ option: opt  :: Description ]", option.toString());

        Option option2 = new Option("opt", "--long-opt", false, "Description");
        assertEquals("[ option: opt  :: Description ]", option2.toString());

        Option option3 = new Option("opt", null, true, "Description [ARG]");
        assertEquals("[ option: opt  :: Description [ARG] ]", option3.toString());

        Option option4 = new Option("opt", "--long-opt", true, "Description [ARG]");
        assertEquals("[ option: opt  :: Description [ARG] ]", option4.toString());

        Option option5 = new Option("opt", null, false, "Description [ARG1 ARG2]", 2);
        assertEquals("[ option: opt  :: Description [ARG1 ARG2] ]", option5.toString());

        Option option6 = new Option("opt", "--long-opt", false, "Description [ARG1 ARG2]", 2);
        assertEquals("[ option: opt  :: Description [ARG1 ARG2] ]", option6.toString());

        Option option7 = new Option("opt", "--long-opt", true, "Description [ARG] :: int", null);
        assertEquals("[ option: opt  :: Description [ARG] :: int ]", option7.toString());
    }

    @Test
    public void testOptionEquals() {
        Option option1 = new Option("opt1", null, false, "Description1");
        Option option2 = new Option("opt1", null, false, "Description1");
        assertTrue(option1.equals(option2));

        Option option3 = new Option("opt2", null, false, "Description2");
        assertFalse(option1.equals(option3));

        Option option4 = new Option("opt1", null, false, null);
        assertFalse(option1.equals(option4));

        Option option5 = new Option("opt1", null, false, "Description1");
        Option option6 = new Option("opt1", null, true, "Description1");
        assertFalse(option1.equals(option6));
    }

    @Test
    public void testOptionHashCode() {
        Option option1 = new Option("opt1", null, false, "Description1");
        Option option2 = new Option("opt1", null, false, "Description1");
        assertEquals(option1.hashCode(), option2.hashCode());

        Option option3 = new Option("opt2", null, false, "Description2");
        assertNotEquals(option1.hashCode(), option3.hashCode());

        Option option4 = new Option("opt1", null, false, null);
        assertNotEquals(option1.hashCode(), option4.hashCode());
    }

    @Test
    public void testOptionClone() throws CloneNotSupportedException {
        Option option = new Option("opt", null, false, "Description");
        Option clonedOption = option.clone();
        assertEquals(option, clonedOption);
    }

    @Test
    public void testOptionClearValues() {
        Option option = new Option("opt", null, false, "Description");
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        option.clearValues();
        List values = option.getValuesList();
        assertEquals(0, values.size());
    }
}