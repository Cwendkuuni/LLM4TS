package org.apache.commons.cli;

import static org.junit.Assert.*;
import java.util.*;

public class OptionTest {

    @Test
    public void testOptionConstructorWithOptAndDescription() {
        Option option = new Option("opt", "Description for opt");
        assertEquals("opt", option.getOpt());
        assertEquals("Description for opt", option.getDescription());
        assertNull(option.getLongOpt());
        assertFalse(option.hasArg());
        assertFalse(option.isRequired());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.hasLongOpt());
        assertFalse(option.hasValueSeparator());
        assertFalse(option.hasArgs());
        assertEquals(-1, option.getArgs());
        assertNull(option.getValue());
        assertFalse(option.hasNoValues());
        assertEquals(Option.UNINITIALIZED, option.getId());
    }

    @Test
    public void testOptionConstructorWithOptHasArgAndDescription() {
        Option option = new Option("opt", true, "Description for opt");
        assertEquals("opt", option.getOpt());
        assertEquals("Description for opt", option.getDescription());
        assertNull(option.getLongOpt());
        assertTrue(option.hasArg());
        assertFalse(option.isRequired());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.hasLongOpt());
        assertFalse(option.hasValueSeparator());
        assertFalse(option.hasArgs());
        assertEquals(-1, option.getArgs());
        assertNull(option.getValue());
        assertFalse(option.hasNoValues());
        assertEquals(Option.UNINITIALIZED, option.getId());
    }

    @Test
    public void testOptionConstructorWithOptLongOptHasArgAndDescription() {
        Option option = new Option("opt", "--long-opt", true, "Description for opt");
        assertEquals("opt", option.getOpt());
        assertEquals("--long-opt", option.getLongOpt());
        assertTrue(option.hasArg());
        assertFalse(option.isRequired());
        assertFalse(option.hasOptionalArg());
        assertTrue(option.hasLongOpt());
        assertFalse(option.hasValueSeparator());
        assertFalse(option.hasArgs());
        assertEquals(-1, option.getArgs());
        assertNull(option.getValue());
        assertFalse(option.hasNoValues());
        assertEquals(Option.UNINITIALIZED, option.getId());
    }

    @Test
    public void testGetId() {
        Option option = new Option("opt", "Description for opt");
        assertEquals('o', option.getId());

        Option option2 = new Option("opt2", "Description for opt2");
        assertEquals('2', option2.getId());
    }

    @Test
    public void testGetKey() {
        Option option = new Option("opt", "Description for opt");
        assertEquals("opt", option.getKey());

        Option option2 = new Option(null, "--long-opt", "Description for opt2");
        assertEquals("--long-opt", option2.getKey());
    }

    @Test
    public void testGetOpt() {
        Option option = new Option("opt", "Description for opt");
        assertEquals("opt", option.getOpt());

        Option option2 = new Option(null, "--long-opt", "Description for opt2");
        assertNull(option2.getOpt());
    }

    @Test
    public void testGetType() {
        Option option = new Option("opt", "Description for opt");
        assertNull(option.getType());

        Option option2 = new Option("opt", "Description for opt", Integer.class, "Description for type");
        assertEquals(Integer.class, option2.getType());
    }

    @Test
    public void testSetType() {
        Option option = new Option("opt", "Description for opt");

        option.setType(Integer.class);
        assertEquals(Integer.class, option.getType());

        option.setType(String.class);
        assertEquals(String.class, option.getType());
    }

    @Test
    public void testGetLongOpt() {
        Option option = new Option("opt", "Description for opt");
        assertNull(option.getLongOpt());

        Option option2 = new Option("opt", "--long-opt", "Description for opt2");
        assertEquals("--long-opt", option2.getLongOpt());
    }

    @Test
    public void testSetLongOpt() {
        Option option = new Option("opt", "Description for opt");

        option.setLongOpt("--long-opt2");
        assertEquals("--long-opt2", option.getLongOpt());

        option.setLongOpt(null);
        assertNull(option.getLongOpt());
    }

    @Test
    public void testSetOptionalArg() {
        Option option = new Option("opt", "Description for opt");
        assertFalse(option.hasOptionalArg());

        option.setOptionalArg(true);
        assertTrue(option.hasOptionalArg());

        option.setOptionalArg(false);
        assertFalse(option.hasOptionalArg());
    }

    @Test
    public void testHasOptionalArg() {
        Option option = new Option("opt", "Description for opt");
        assertFalse(option.hasOptionalArg());

        Option option2 = new Option("opt2", "Description for opt2", false);
        assertFalse(option2.hasOptionalArg());

        Option option3 = new Option("opt3", "Description for opt3", true);
        assertTrue(option3.hasOptionalArg());
    }

    @Test
    public void testHasLongOpt() {
        Option option = new Option("opt", "Description for opt");
        assertFalse(option.hasLongOpt());

        Option option2 = new Option("opt2", "--long-opt", "Description for opt2");
        assertTrue(option2.hasLongOpt());
    }

    @Test
    public void testHasArg() {
        Option option = new Option("opt", "Description for opt");
        assertFalse(option.hasArg());

        Option option2 = new Option("opt2", "Description for opt2", false);
        assertFalse(option2.hasArg());

        Option option3 = new Option("opt3", "Description for opt3", true, "Description for arg");
        assertTrue(option3.hasArg());
    }

    @Test
    public void testSetDescription() {
        Option option = new Option("opt", "Description for opt");
        assertEquals("Description for opt", option.getDescription());

        option.setDescription("New description");
        assertEquals("New description", option.getDescription());
    }

    @Test
    public void testIsRequired() {
        Option option = new Option("opt", "Description for opt");
        assertFalse(option.isRequired());

        Option option2 = new Option("opt2", "Description for opt2", false, "Description for opt");
        assertFalse(option2.isRequired());

        Option option3 = new Option("opt3", "Description for opt3", false, "Description for opt", true);
        assertTrue(option3.isRequired());
    }

    @Test
    public void testSetRequired() {
        Option option = new Option("opt", "Description for opt");
        assertFalse(option.isRequired());

        option.setRequired(true);
        assertTrue(option.isRequired());

        option.setRequired(false);
        assertFalse(option.isRequired());
    }

    @Test
    public void testSetArgName() {
        Option option = new Option("opt", "Description for opt");
        assertEquals("arg", option.getArgName());

        option.setArgName("argName");
        assertEquals("argName", option.getArgName());
    }

    @Test
    public void testGetArgName() {
        Option option = new Option("opt", "Description for opt");
        assertEquals("arg", option.getArgName());

        Option option2 = new Option("opt2", "--long-opt", "Description for opt2", "argName");
        assertEquals("argName", option2.getArgName());
    }

    @Test
    public void testHasArgName() {
        Option option = new Option("opt", "Description for opt");
        assertFalse(option.hasArgName());

        Option option2 = new Option("opt2", "--long-opt", "Description for opt2", "argName");
        assertTrue(option2.hasArgName());
    }

    @Test
    public void testHasArgs() {
        Option option = new Option("opt", "Description for opt");
        assertFalse(option.hasArgs());

        Option option2 = new Option("opt2", "Description for opt2", true, "Description for arg");
        assertTrue(option2.hasArgs());
    }

    @Test
    public void testSetArgs() {
        Option option = new Option("opt", "Description for opt");
        assertEquals(-1, option.getArgs());

        option.setArgs(1);
        assertEquals(1, option.getArgs());

        option.setArgs(2);
        assertEquals(2, option.getArgs());
    }

    @Test
    public void testSetValueSeparator() {
        Option option = new Option("opt", "Description for opt");
        assertFalse(option.hasValueSeparator());

        option.setValueSeparator(':');
        assertTrue(option.hasValueSeparator());
        assertEquals(':', option.getValueSeparator());
    }

    @Test
    public void testGetValueSeparator() {
        Option option = new Option("opt", "Description for opt");
        assertFalse(option.hasValueSeparator());

        Option option2 = new Option("opt2", "Description for opt2", false, "Description for arg", ':');
        assertTrue(option2.hasValueSeparator());
        assertEquals(':', option2.getValueSeparator());
    }

    @Test
    public void testToString() {
        Option option = new Option("opt", "Description for opt");
        String expected = "[ option: opt :: Description for opt ]";
        assertEquals(expected, option.toString());

        Option option2 = new Option("opt2", "--long-opt", "Description for opt2");
        expected = "[ option: opt2 :: --long-opt :: Description for opt2 ]";
        assertEquals(expected, option2.toString());

        Option option3 = new Option("opt3", "--long-opt", true, "Description for opt3", ':');
        expected = "[ option: opt3 :: --long-opt :: Description for opt3 :: Description for arg:1 :: Description for arg:2 ]";
        assertEquals(expected, option3.toString());
    }

    @Test
    public void testHasNoValues() {
        Option option = new Option("opt", "Description for opt");
        assertTrue(option.hasNoValues());

        Option option2 = new Option("opt2", "--long-opt", true, "Description for opt2", ':');
        assertFalse(option2.hasNoValues());
    }

    @Test
    public void testEquals() {
        Option option1 = new Option("opt1", "Description for opt1");
        Option option2 = new Option("opt1", "Description for opt1");
        assertTrue(option1.equals(option2));

        Option option3 = new Option("opt3", "Description for opt3");
        assertFalse(option1.equals(option3));

        Option option4 = new Option("opt1", null);
        assertFalse(option1.equals(option4));
    }

    @Test
    public void testHashCode() {
        Option option1 = new Option("opt1", "Description for opt1");
        Option option2 = new Option("opt1", "Description for opt1");
        assertEquals(option1.hashCode(), option2.hashCode());

        Option option3 = new Option("opt3", "Description for opt3");
        assertNotEquals(option1.hashCode(), option3.hashCode());

        Option option4 = new Option("opt1", null);
        assertNotEquals(option1.hashCode(), option4.hashCode());
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        Option option = new Option("opt", "Description for opt");
        Option clonedOption = option.clone();
        assertEquals(option, clonedOption);
    }

    @Test(expected = RuntimeException.class)
    public void testClearValues() {
        Option option = new Option("opt", "Description for opt");
        List values = new ArrayList();
        option.setValues(values);
        option.addValue("value1");
        option.addValue("value2");
        option.clearValues();
        assertEquals(0, option.getValues().size());
    }

    @Test
    public void testAddValueForProcessing() {
        Option option = new Option("opt", "Description for opt");
        List values = new ArrayList();
        option.setValues(values);
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        assertEquals(2, option.getValues().size());
        assertEquals("value1", option.getValues().get(0));
        assertEquals("value2", option.getValues().get(1));
    }

    @Test(expected = RuntimeException.class)
    public void testAddValue() {
        Option option = new Option("opt", "Description for opt");
        List values = new ArrayList();
        option.setValues(values);
        option.addValue("value1");
        option.addValue("value2");
        option.addValue("value3");
        throw new RuntimeException("The addValue method is not intended for client use.");
    }

    @Test
    public void testGetValue() {
        Option option = new Option("opt", "Description for opt");
        List values = new ArrayList();
        option.setValues(values);
        option.addValue("value1");
        assertEquals("value1", option.getValue());

        option.setValue(new Integer(42));
        assertEquals(new Integer(42), option.getValue());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValueIndexOutOfBounds() {
        Option option = new Option("opt", "Description for opt");
        List values = new ArrayList();
        option.setValues(values);
        option.addValue("value1");
        option.addValue("value2");
        option.addValue("value3");
        option.getValue(2);
    }

    @Test
    public void testGetValueDefaultValue() {
        Option option = new Option("opt", "Description for opt");
        List values = new ArrayList();
        option.setValues(values);
        assertEquals(null, option.getValue("defaultValue"));

        option.addValue("value1");
        assertEquals("value1", option.getValue("defaultValue"));
    }

    @Test
    public void testGetValues() {
        Option option = new Option("opt", "Description for opt");
        List values = new ArrayList();
        option.setValues(values);
        option.addValue("value1");
        option.addValue("value2");
        assertArrayEquals(new String[]{"value1", "value2"}, option.getValues());
    }

    @Test
    public void testGetValuesList() {
        Option option = new Option("opt", "Description for opt");
        List values = new ArrayList();
        option.setValues(values);
        option.addValue("value1");
        option.addValue("value2");
        assertEquals(values, option.getValuesList());
    }
}