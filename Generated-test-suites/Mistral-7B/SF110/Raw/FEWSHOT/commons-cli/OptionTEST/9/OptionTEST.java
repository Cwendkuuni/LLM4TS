package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

public class OptionTest {

    @Test
    public void testOptionConstruction() {
        Option option = new Option("a", "option A", false);
        assertEquals("a", option.getOpt());
        assertEquals("option A", option.getDescription());
        assertFalse(option.hasArg());
        assertFalse(option.isRequired());
        assertNull(option.getType());
        assertNull(option.getLongOpt());
        assertFalse(option.hasOptionalArg());
        assertNull(option.getArgName());
        assertFalse(option.hasValueSeparator());
        assertFalse(option.hasArgs());
        assertFalse(option.hasLongOpt());
        assertFalse(option.hasArgName());
        assertEquals(0, option.getArgs());
        assertNull(option.getValue());
        assertNull(option.getValues());
        assertFalse(option.equals(null));
        assertFalse(option.equals(""));
        assertEquals(option.hashCode(), option.hashCode());
    }

    @Test
    public void testOptionConstructionWithArg() {
        Option option = new Option("a", true, "option A", "arg");
        assertEquals("a", option.getOpt());
        assertEquals("option A", option.getDescription());
        assertTrue(option.hasArg());
        assertFalse(option.isRequired());
        assertNull(option.getType());
        assertNull(option.getLongOpt());
        assertFalse(option.hasOptionalArg());
        assertEquals("arg", option.getArgName());
        assertFalse(option.hasValueSeparator());
        assertFalse(option.hasArgs());
        assertFalse(option.hasLongOpt());
        assertFalse(option.hasArgName());
        assertEquals(1, option.getArgs());
        assertNull(option.getValue());
        assertNull(option.getValues());
        assertFalse(option.equals(null));
        assertFalse(option.equals(""));
        assertEquals(option.hashCode(), option.hashCode());
    }

    @Test
    public void testOptionConstructionWithLongOpt() {
        Option option = new Option("a", null, false, "long-option-A"); ;
        assertEquals("a", option.getOpt());
        assertEquals("long-option-A", option.getLongOpt());
        assertFalse(option.hasArg());
        assertFalse(option.isRequired());
        assertNull(option.getType());
        assertFalse(option.hasOptionalArg());
        assertNull(option.getArgName());
        assertFalse(option.hasValueSeparator());
        assertFalse(option.hasArgs());
        assertFalse(option.hasLongOpt());
        assertFalse(option.hasArgName());
        assertEquals(0, option.getArgs());
        assertNull(option.getValue());
        assertNull(option.getValues());
        assertFalse(option.equals(null));
        assertFalse(option.equals(""));
        assertEquals(option.hashCode(), option.hashCode());
    }

    @Test
    public void testOptionConstructionWithArgAndLongOpt() {
        Option option = new Option("a", "long-option-A", true, "arg", "option A");
        assertEquals("a", option.getOpt());
        assertEquals("option A", option.getDescription());
        assertTrue(option.hasArg());
        assertFalse(option.isRequired());
        assertNull(option.getType());
        assertEquals("long-option-A", option.getLongOpt());
        assertFalse(option.hasOptionalArg());
        assertEquals("arg", option.getArgName());
        assertFalse(option.hasValueSeparator());
        assertFalse(option.hasArgs());
        assertFalse(option.hasLongOpt());
        assertFalse(option.hasArgName());
        assertEquals(1, option.getArgs());
        assertNull(option.getValue());
        assertNull(option.getValues());
        assertFalse(option.equals(null));
        assertFalse(option.equals(""));
        assertEquals(option.hashCode(), option.hashCode());
    }

    @Test
    public void testOptionConstructionWithType() {
        Option option = new Option("a", "option A", false, null, null);
        assertEquals("a", option.getOpt());
        assertEquals("option A", option.getDescription());
        assertFalse(option.hasArg());
        assertFalse(option.isRequired());
        assertEquals(String.class, option.getType());
        assertNull(option.getLongOpt());
        assertFalse(option.hasOptionalArg());
        assertNull(option.getArgName());
        assertFalse(option.hasValueSeparator());
        assertFalse(option.hasArgs());
        assertFalse(option.hasLongOpt());
        assertFalse(option.hasArgName());
        assertEquals(0, option.getArgs());
        assertNull(option.getValue());
        assertNull(option.getValues());
        assertFalse(option.equals(null));
        assertFalse(option.equals(""));
        assertEquals(option.hashCode(), option.hashCode());
    }

    @Test
    public void testOptionConstructionWithTypeAndArg() {
        Option option = new Option("a", "option A", false, null, null, "arg");
        assertEquals("a", option.getOpt());
        assertEquals("option A", option.getDescription());
        assertFalse(option.hasArg());
        assertFalse(option.isRequired());
        assertEquals(String.class, option.getType());
        assertNull(option.getLongOpt());
        assertFalse(option.hasOptionalArg());
        assertEquals("arg", option.getArgName());
        assertFalse(option.hasValueSeparator());
        assertFalse(option.hasArgs());
        assertFalse(option.hasLongOpt());
        assertFalse(option.hasArgName());
        assertEquals(0, option.getArgs());
        assertNull(option.getValue());
        assertNull(option.getValues());
        assertFalse(option.equals(null));
        assertFalse(option.equals(""));
        assertEquals(option.hashCode(), option.hashCode());
    }

    @Test
    public void testOptionConstructionWithTypeAndArgAndOptionalArg() {
        Option option = new Option("a", "option A", false, null, null, "arg", true);
        assertEquals("a", option.getOpt());
        assertEquals("option A", option.getDescription());
        assertTrue(option.hasOptionalArg());
        assertFalse(option.isRequired());
        assertEquals(String.class, option.getType());
        assertNull(option.getLongOpt());
        assertFalse(option.hasArgName());
        assertFalse(option.hasValueSeparator());
        assertFalse(option.hasArgs());
        assertFalse(option.hasLongOpt());
        assertEquals("arg", option.getArgName());
        assertEquals(0, option.getArgs());
        assertNull(option.getValue());
        assertNull(option.getValues());
        assertFalse(option.equals(null));
        assertFalse(option.equals(""));
        assertEquals(option.hashCode(), option.hashCode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionConstructionWithInvalidArg() {
        Option option = new Option("", "option A", false, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionConstructionWithLongOptAndInvalidArg() {
        Option option = new Option("a", "", null, "long-option-A"); ;
    }

    @Test
    public void testOptionGettersAndSetters() {
        Option option = new Option("a", "option A", false, null);
        assertEquals("a", option.getOpt());
        option.setOpt("b");
        assertEquals("b", option.getOpt());
        assertEquals("option A", option.getDescription());
        option.setDescription("option B");
        assertEquals("option B", option.getDescription());
        assertFalse(option.hasArg());
        option.setHasArg(true);
        assertTrue(option.hasArg());
        assertFalse(option.isRequired());
        option.setRequired(true);
        assertTrue(option.isRequired());
        assertNull(option.getType());
        Object type = new Object();
        option.setType(type);
        assertSame(type, option.getType());
        assertNull(option.getLongOpt());
        option.setLongOpt("long-option-B");
        assertEquals("long-option-B", option.getLongOpt());
        assertFalse(option.hasOptionalArg());
        option.setOptionalArg(true);
        assertTrue(option.hasOptionalArg());
        assertNull(option.getArgName());
        option.setArgName("arg-name");
        assertEquals("arg-name", option.getArgName());
        assertFalse(option.hasValueSeparator());
        option.setValueSeparator('x');
        assertTrue(option.hasValueSeparator());
        assertEquals('x', option.getValueSeparator());
        assertFalse(option.hasArgs());
        option.setArgs(-1);
        assertFalse(option.hasArgs());
        option.setArgs(1);
        assertTrue(option.hasArgs());
        assertFalse(option.hasLongOpt());
        option.setHasLongOpt(true);
        assertTrue(option.hasLongOpt());
        assertFalse(option.hasArgName());
        option.setHasArgName(true);
        assertTrue(option.hasArgName());
        assertEquals(0, option.getArgs());
        option.setArgs(2);
        assertEquals(2, option.getArgs());
        assertNull(option.getValue());
        option.setValue("value");
        assertEquals("value", option.getValue());
        assertNull(option.getValues());
        List<String> values = Arrays.asList("value1", "value2");
        option.setValues(values);
        assertEquals(values, option.getValues());
        assertEquals("[" + Option.class.getName() + " option: a  :: option A ]", option.toString());
    }

    @Test
    public void testOptionEquals() {
        Option option1 = new Option("a", "option A", false);
        Option option2 = new Option("a", "option A", false);
        assertTrue(option1.equals(option2));
        assertTrue(option2.equals(option1));
        Option option3 = new Option("b", "option B", false);
        assertFalse(option1.equals(option3));
        assertFalse(option3.equals(option1));
        Option option4 = new Option("a", "option A", true);
        assertFalse(option1.equals(option4));
        assertFalse(option4.equals(option1));
    }

    @Test
    public void testOptionHashCode() {
        Option option1 = new Option("a", "option A", false);
        Option option2 = new Option("a", "option A", false);
        assertEquals(option1.hashCode(), option2.hashCode());
        Option option3 = new Option("b", "option B", false);
        assertNotEquals(option1.hashCode(), option3.hashCode());
        Option option4 = new Option("a", "option A", true);
        assertNotEquals(option1.hashCode(), option4.hashCode());
    }

    @Test
    public void testOptionClone() {
        Option option = new Option("a", "option A", false);
        Option cloned = option.clone();
        assertEquals(option, cloned);
        assertEquals(option.getValues(), cloned.getValues());
    }

    @Test
    public void testOptionClearValues() {
        Option option = new Option("a", "option A", false);
        List<String> values = Arrays.asList("value1", "value2");
        option.setValues(values);
        option.clearValues();
        assertNull(option.getValue());
        assertNull(option.getValues());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testOptionAddValue() {
        Option option = new Option("a", "option A", false);
        option.addValue("value1");
    }

    @Test
    public void testOptionAddValueForProcessing() {
        Option option = new Option("a", "option A", false);
        List<String> values = new ArrayList<>();
        option.setValues(values);
        option.addValueForProcessing("value1");
        assertEquals("value1", option.getValue());
        assertEquals(Arrays.asList("value1"), option.getValues());
    }

    @Test
    public void testOptionProcessValue() {
        Option option = new Option("a", "option A", false, null, null, null, 'x');
        option.setValues(new ArrayList<>());
        option.processValue("value1xvalue2xvalue3");
        assertEquals(Arrays.asList("value1", "value2", "value3"), option.getValues());
    }

    @Test
    public void testOptionToString() {
        Option option = new Option("a", "option A", false, null, null, null, 'x');
        assertEquals("[ option: a  :: option A ]", option.toString());
    }
}