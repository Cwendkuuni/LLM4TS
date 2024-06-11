package org.apache.commons.cli.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.ParseException;

import java.util.ArrayList;
import java.util.List;

public class OptionTest {

    @Test
    public void testOptionCreation() {
        Option option = new Option("a", "optA", false, "Option A");
        assertEquals("optA", option.getOpt());
        assertEquals("--optA", option.getLongOpt());
        assertFalse(option.hasArg());
        assertNull(option.getValue());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.isRequired());
        assertEquals("Option A", option.getDescription());
        assertEquals('-', option.getValueSeparator());
        assertFalse(option.hasValueSeparator());
        assertFalse(option.hasArgs());
        assertEquals(-1, option.getArgs());
        assertFalse(option.hasArgName());
        assertEquals("arg", option.getArgName());
        assertFalse(option.hasValues());
        assertEquals(0, option.getValues().size());
        assertEquals("Option A", option.toString());

        Option optionWithArg = new Option("b", "optB", true, "Option B with argument");
        assertEquals("optB", optionWithArg.getOpt());
        assertEquals("--optB", optionWithArg.getLongOpt());
        assertEquals("ARG", optionWithArg.getArgName());
        assertEquals("Option B with argument", optionWithArg.getDescription());
        assertTrue(optionWithArg.hasArg());
        assertNull(optionWithArg.getValue());
        assertTrue(optionWithArg.hasOptionalArg());
        assertFalse(optionWithArg.isRequired());
        assertEquals(-1, optionWithArg.getArgs());
        assertEquals("ARG", optionWithArg.toString());

        Option optionWithValues = new Option("c", "optC", false, "Option C with values", 2, ';');
        assertEquals("optC", optionWithValues.getOpt());
        assertEquals("--optC", optionWithValues.getLongOpt());
        assertEquals("ARG1;ARG2", optionWithValues.toString());
        assertTrue(optionWithValues.hasArg());
        assertNull(optionWithValues.getValue());
        assertFalse(optionWithValues.hasOptionalArg());
        assertFalse(optionWithValues.isRequired());
        assertEquals(2, optionWithValues.getArgs());
        assertEquals("ARG1;ARG2", optionWithValues.getValue(null));
        List<String> values = optionWithValues.getValuesList();
        values.add("VALUE1");
        values.add("VALUE2");
        assertEquals("VALUE1;VALUE2", optionWithValues.getValue(null));
        assertEquals("VALUE1", optionWithValues.getValue(0));
        assertEquals("VALUE2", optionWithValues.getValue(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidOptionCreation() {
        new Option(null, "", false, "");
    }

    @Test
    public void testOptionProcessing() {
        Option option = new Option("a", "optA", false, "Option A");
        option.addValueForProcessing("VALUE1");
        assertEquals("[VALUE1]", option.toString());
        assertEquals("VALUE1", option.getValue());
        try {
            option.addValueForProcessing("VALUE2");
            fail("Should throw an exception");
        } catch (RuntimeException e) {
            assertEquals("NO_ARGS_ALLOWED", e.getMessage());
        }

        Option optionWithArg = new Option("b", "optB", true, "Option B with argument");
        optionWithArg.addValueForProcessing("VALUE1 ARG1");
        assertEquals("[VALUE1]", optionWithArg.getValues().get(0));
        assertEquals("ARG1", optionWithArg.getValues().get(1));
        assertEquals("[VALUE1 ARG1]", optionWithArg.toString());

        Option optionWithValues = new Option("c", "optC", false, "Option C with values", 2, ';');
        optionWithValues.addValueForProcessing("VALUE1;VALUE2");
        assertEquals("[VALUE1;VALUE2]", optionWithValues.toString());
        assertEquals("VALUE1", optionWithValues.getValue(0));
        assertEquals("VALUE2", optionWithValues.getValue(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValueWithInvalidIndex() {
        Option option = new Option("a", "optA", false, "Option A");
        option.addValueForProcessing("VALUE1");
        option.addValueForProcessing("VALUE2");
        option.addValueForProcessing("VALUE3");
        String value = option.getValue(3);
    }

    @Test(expected = ParseException.class)
    public void testParseException() {
        Option option = new Option("a", "optA", false, "Option A");
        option.addValueForProcessing("VALUE1,VALUE2");
    }
}