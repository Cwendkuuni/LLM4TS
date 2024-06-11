package org.apache.commons.cli;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class OptionTest {

    @Test
    public void testOptionConstructionWithNoArgumentsAndDescription() {
        Option option = new Option("opt", "Description");
        assertEquals("opt", option.getOpt());
        assertEquals("Description", option.getDescription());
        assertEquals(-1, option.getArgs());
        assertNull(option.getLongOpt());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.hasLongOpt());
        assertFalse(option.hasArg());
        assertNull(option.getType());
        assertTrue(option.hasArgName());
        assertNull(option.getArgName());
        assertFalse(option.hasArgs());
        assertFalse(option.isRequired());
        assertEquals("[ option: opt :: Description ]", option.toString());
    }

    @Test
    public void testOptionConstructionWithSingleArgumentAndDescription() {
        Option option = new Option("opt", "Description", true);
        assertEquals("opt", option.getOpt());
        assertEquals("Description", option.getDescription());
        assertEquals(1, option.getArgs());
        assertNull(option.getLongOpt());
        assertTrue(option.hasOptionalArg());
        assertFalse(option.hasLongOpt());
        assertTrue(option.hasArg());
        assertNull(option.getType());
        assertTrue(option.hasArgName());
        assertNull(option.getArgName());
        assertFalse(option.hasArgs());
        assertFalse(option.isRequired());
        assertEquals("[ option: opt [ARG] :: Description ]", option.toString());
    }

    @Test
    public void testOptionConstructionWithSingleArgumentAndLongOptionAndDescription() {
        Option option = new Option("opt", "--longopt", true, "Description");
        assertEquals("opt", option.getOpt());
        assertEquals("--longopt", option.getLongOpt());
        assertEquals(1, option.getArgs());
        assertTrue(option.hasOptionalArg());
        assertTrue(option.hasLongOpt());
        assertTrue(option.hasArg());
        assertNull(option.getType());
        assertTrue(option.hasArgName());
        assertNull(option.getArgName());
        assertFalse(option.hasArgs());
        assertFalse(option.isRequired());
        assertEquals("[ option: opt [ARG] :: --longopt Description ]", option.toString());
    }

    @Test
    public void testOptionConstructionWithMultipleArgumentsAndDescription() {
        Option option = new Option("opt", "Description", -2);
        assertEquals("opt", option.getOpt());
        assertEquals("Description", option.getDescription());
        assertEquals(-2, option.getArgs());
        assertNull(option.getLongOpt());
        assertTrue(option.hasOptionalArg());
        assertFalse(option.hasLongOpt());
        assertFalse(option.hasArg());
        assertNull(option.getType());
        assertTrue(option.hasArgName());
        assertNull(option.getArgName());
        assertTrue(option.hasArgs());
        assertFalse(option.isRequired());
        assertEquals("[ option: opt [ARG...] :: Description ]", option.toString());
    }

    @Test
    public void testOptionGetId() {
        Option option = new Option("opt", "Description");
        assertEquals('o', option.getId());
    }

    @Test
    public void testOptionGetKey() {
        Option option = new Option("opt", "Description");
        assertEquals("opt", option.getKey());
    }

    @Test
    public void testOptionGetLongOpt() {
        Option option = new Option("opt", "--longopt", false, "Description");
        assertEquals("--longopt", option.getLongOpt());
    }

    @Test
    public void testOptionSetLongOpt() {
        Option option = new Option("opt", "Description");
        option.setLongOpt("--longopt");
        assertEquals("--longopt", option.getLongOpt());
    }

    @Test
    public void testOptionHasOptionalArg() {
        Option option = new Option("opt", "Description");
        assertFalse(option.hasOptionalArg());

        option = new Option("opt", "Description", true);
        assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testOptionHasLongOpt() {
        Option option = new Option("opt", "Description");
        assertFalse(option.hasLongOpt());

        option = new Option("opt", "--longopt", false, "Description");
        assertTrue(option.hasLongOpt());
    }

    @Test
    public void testOptionHasArg() {
        Option option = new Option("opt", "Description");
        assertFalse(option.hasArg());

        option = new Option("opt", "Description", true);
        assertTrue(option.hasArg());

        option = new Option("opt", "Description", 2);
        assertTrue(option.hasArgs());
    }

    @Test
    public void testOptionGetDescription() {
        Option option = new Option("opt", "Description");
        assertEquals("Description", option.getDescription());
    }

    @Test
    public void testOptionSetDescription() {
        Option option = new Option("opt", "Description");
        option.setDescription("New Description");
        assertEquals("New Description", option.getDescription());
    }

    @Test
    public void testOptionIsRequired() {
        Option option = new Option("opt", "Description");
        assertFalse(option.isRequired());

        option.setRequired(true);
        assertTrue(option.isRequired());
    }

    @Test
    public void testOptionHasArgName() {
        Option option = new Option("opt", "Description");
        assertFalse(option.hasArgName());

        option.setArgName("argName");
        assertTrue(option.hasArgName());

        assertEquals("argName", option.getArgName());
    }

    @Test
    public void testOptionGetArgName() {
        Option option = new Option("opt", "Description");
        assertNull(option.getArgName());

        option.setArgName("argName");
        assertEquals("argName", option.getArgName());
    }

    @Test
    public void testOptionToString() {
        Option option = new Option("opt", "Description");
        assertEquals("[ option: opt :: Description ]", option.toString());

        option.setArgName("argName");
        assertEquals("[ option: opt [ARG] :: Description ]", option.toString());

        option.setLongOpt("--longopt");
        assertEquals("[ option: opt [ARG] :: --longopt Description ]", option.toString());

        option.setOptionalArg(false);
        assertEquals("[ option: opt [ARG] :: --longopt Description ]", option.toString());

        option.setType(String.class);
        assertEquals("[ option: opt [ARG] :: --longopt Description ] (type: java.lang.String)", option.toString());

        option.setValues(new ArrayList<String>());
        option.setValueSeparator(';');
        option.addValue("value1");
        option.addValue("value2");
        option.addValue("value3");
        assertEquals("[ option: opt [ARG] :: --longopt Description ] (type: java.lang.String, values: [value1, value2, value3])", option.toString());
    }

    @Test
    public void testOptionEquals() {
        Option option1 = new Option("opt1", "Description1");
        Option option2 = new Option("opt1", "Description1");

        Option option3 = new Option("opt2", "Description2");

        assertTrue(option1.equals(option2));

        assertFalse(option1.equals(option3));

        assertFalse(option1.equals(null));
    }

    @Test
    public void testOptionHashCode() {
        Option option1 = new Option("opt1", "Description1");
        Option option2 = new Option("opt1", "Description1");

        Option option3 = new Option("opt2", "Description2");

        int hashCode1 = option1.hashCode();
        int hashCode2 = option2.hashCode();
        int hashCode3 = option3.hashCode();

        assertEquals(hashCode1, hashCode2);

        assertNotEquals(hashCode1, hashCode3);
    }

    @Test
    public void testOptionClone() throws CloneNotSupportedException {
        Option option = new Option("opt", "Description");
        Option clonedOption = option.clone();

        assertEquals(option, clonedOption);
    }

    @Test(expected = RuntimeException.class)
    public void testAddValueMethodIsNotIntendedForClientUse() {
        Option option = new Option("opt", "Description");
        option.addValue("value");
    }

    @Test
    public void testAddValueForProcessingMethod() {
        Option option = new Option("opt", "Description");
        option.setValueSeparator(';');

        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        option.addValueForProcessing("value3");

        List<String> values = option.getValuesList();

        assertEquals(3, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
        assertEquals("value3", values.get(2));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValueOutOfBounds() {
        Option option = new Option("opt", "Description");
        option.setValue("value");

        option.addValue("value1");
        option.addValue("value2");

        String value = option.getValue(2);
    }
}