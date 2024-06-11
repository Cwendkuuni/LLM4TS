package org.apache.commons.cli;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class OptionTest {

    @Test
    public void testOptionConstructor() {
        Option option = new Option("opt", "description");
        assertEquals("opt", option.getOpt());
        assertEquals("description", option.getDescription());
        assertNull(option.getLongOpt());
        assertFalse(option.hasArg());
        assertFalse(option.isRequired());
        assertEquals(Option.UNINITIALIZED, option.numberOfArgs);
        assertNull(option.getType());
        assertNull(option.getValues());
        assertEquals(0, option.getArgs());
        assertFalse(option.hasValueSeparator());
        assertFalse(option.hasArgs());
        assertFalse(option.hasArgName());
        assertNull(option.getArgName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionConstructorWithInvalidOpt() {
        new Option(null, "description");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionConstructorWithEmptyOpt() {
        new Option("", "description");
    }

    @Test
    public void testOptionConstructorWithHasArg() {
        Option option = new Option("opt", "description", true);
        assertEquals("opt", option.getOpt());
        assertEquals("description", option.getDescription());
        assertNull(option.getLongOpt());
        assertTrue(option.hasArg());
        assertFalse(option.isRequired());
        assertEquals(1, option.numberOfArgs);
        assertNull(option.getType());
        assertNull(option.getValues());
        assertEquals(0, option.getArgs());
        assertFalse(option.hasValueSeparator());
        assertFalse(option.hasArgs());
        assertFalse(option.hasArgName());
        assertNull(option.getArgName());
    }

    @Test
    public void testOptionConstructorWithLongOpt() {
        Option option = new Option("opt", "longOpt", false, "description");
        assertEquals("opt", option.getOpt());
        assertEquals("longOpt", option.getLongOpt());
        assertEquals("description", option.getDescription());
        assertFalse(option.hasArg());
        assertFalse(option.isRequired());
        assertEquals(Option.UNINITIALIZED, option.numberOfArgs);
        assertNull(option.getType());
        assertNull(option.getValues());
        assertEquals(0, option.getArgs());
        assertFalse(option.hasValueSeparator());
        assertFalse(option.hasArgs());
        assertFalse(option.hasArgName());
        assertNull(option.getArgName());
    }

    @Test
    public void testOptionGettersAndSetters() {
        Option option = new Option("opt", "description");
        option.setLongOpt("longOpt");
        option.setHasArg(true);
        option.setDescription("newDescription");
        option.setRequired(true);
        option.setNumberOfArgs(2);
        option.setType("String.class");
        List<String> values = new ArrayList<>();
        values.add("value1");
        values.add("value2");
        option.setValues(values);
        option.setValueSeparator('-');
        option.setArgs(5);

        assertEquals("opt", option.getOpt());
        assertEquals("longOpt", option.getLongOpt());
        assertEquals("newDescription", option.getDescription());
        assertTrue(option.isRequired());
        assertEquals(2, option.numberOfArgs);
        assertEquals("String.class", option.getType());
        assertEquals(values, option.getValues());
        assertEquals('-', option.getValueSeparator());
        assertEquals(5, option.getArgs());
    }

    @Test
    public void testOptionHasArg() {
        Option option = new Option("opt", "description");
        Option hasArgOption = new Option("opt2", "description2", true, "");

        assertFalse(option.hasArg());
        assertTrue(hasArgOption.hasArg());
    }

    @Test
    public void testOptionIsRequired() {
        Option notRequiredOption = new Option("opt", "description");
        Option requiredOption = new Option("opt2", "description2", false, "", true);

        assertFalse(notRequiredOption.isRequired());
        assertTrue(requiredOption.isRequired());
    }

    @Test
    public void testOptionGetId() {
        Option option = new Option("opt", "description");
        assertEquals('o', option.getId());
    }

    @Test
    public void testOptionGetKey() {
        Option option = new Option("opt", null);
        assertEquals("opt", option.getKey());

        Option longOptOption = new Option(null, "longOpt");
        assertEquals("longOpt", option.getKey());
    }

    @Test
    public void testOptionGetOpt() {
        Option option = new Option("opt", "description");
        assertEquals("opt", option.getOpt());
    }

    @Test
    public void testOptionGetLongOpt() {
        Option option = new Option("opt", "description");
        assertNull(option.getLongOpt());

        Option longOptOption = new Option(null, "longOpt");
        assertEquals("longOpt", option.getLongOpt());
    }

    @Test
    public void testOptionGetDescription() {
        Option option = new Option("opt", "description");
        assertEquals("description", option.getDescription());
    }

    @Test
    public void testOptionGetType() {
        Option option = new Option("opt", "description");
        assertNull(option.getType());

        Option typeOption = new Option("opt", "description", null, "", null, "Type.class");
        assertEquals("Type.class", option.getType());
    }

    @Test
    public void testOptionSetType() {
        Option option = new Option("opt", "description");
        option.setType("String.class");
        assertEquals("String.class", option.getType());
    }

    @Test
    public void testOptionGetValuesList() {
        Option option = new Option("opt", "description");
        List<String> values = new ArrayList<>();
        values.add("value1");
        values.add("value2");
        option.setValues(values);
        assertEquals(values, option.getValuesList());
    }

    @Test
    public void testOptionGetValues() {
        Option option = new Option("opt", "description");
        List<String> values = new ArrayList<>();
        values.add("value1");
        values.add("value2");
        option.setValues(values);
        String[] expectedValues = {"value1", "value2"};
        assertArrayEquals(expectedValues, option.getValues());
    }

    @Test
    public void testOptionToString() {
        Option option = new Option("opt", "description");
        assertEquals("[ option: opt :: description ]", option.toString());

        Option longOptOption = new Option(null, "longOpt");
        assertEquals("[ option: opt :: longOpt :: description ]", longOptOption.toString());

        Option hasArgOption = new Option("opt2", "description2", true, "");
        assertEquals("[ option: opt2 [ARG] :: description2 ]", hasArgOption.toString());
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

        assertEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
    }

    @Test
    public void testOptionClone() {
        Option option = new Option("opt", "description");
        try {
            Option clonedOption = (Option) option.clone();
            assertEquals(option, clonedOption);
        } catch (CloneNotSupportedException e) {
            fail("CloneNotSupportedException should not be thrown");
        }
    }

    @Test
    public void testOptionClearValues() {
        Option option = new Option("opt", "description");
        List<String> values = new ArrayList<>();
        values.add("value1");
        values.add("value2");
        option.setValues(values);

        option.clearValues();
        assertEquals(0, option.getValuesList().size());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testOptionAddValue() {
        Option option = new Option("opt", "description");
        option.addValue("value");
    }
}