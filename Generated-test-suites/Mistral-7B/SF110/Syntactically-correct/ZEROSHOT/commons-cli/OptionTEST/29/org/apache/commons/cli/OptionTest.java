package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.ParseException;

import java.util.ArrayList;
import java.util.List;

public class OptionTest {

    @org.junit.Test
    public void testOptionConstruction() {
        Option option = new Option("o", "opt", false, "Option with no args");
        assertEquals("o", option.getOpt());
        assertEquals("opt", option.getLongOpt());
        assertFalse(option.hasArg());
        assertEquals("Option with no args", option.getDescription());
        assertFalse(option.isRequired());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.hasLongOpt());
        assertFalse(option.hasArgName());
        assertFalse(option.hasArgs());
        assertNull(option.getValue());
        assertFalse(option.hasValueSeparator());
        assertFalse(option.isRequired());
        assertFalse(option.equals(null));
        assertFalse(option.equals(""));
        assertTrue(option.equals(option));
        assertEquals(0, option.hashCode());
        Option clone = option.clone();
        assertEquals(option, clone);
        assertNotNull(clone.values);
        assertEquals(0, clone.values.size());
    }

    @org.junit.Test
    public void testOptionConstructionWithArg() {
        Option option = new Option("o", "opt", true, "Option with one arg");
        assertEquals("o", option.getOpt());
        assertEquals("opt", option.getLongOpt());
        assertTrue(option.hasArg());
        assertEquals("Option with one arg", option.getDescription());
        assertFalse(option.isRequired());
        assertTrue(option.hasOptionalArg());
        assertFalse(option.hasLongOpt());
        assertFalse(option.hasArgName());
        assertFalse(option.hasArgs());
        assertNull(option.getValue());
        assertFalse(option.hasValueSeparator());
        assertEquals(1, option.getArgs());
        Option clone = option.clone();
        assertEquals(option, clone);
        assertNotNull(clone.values);
        assertEquals(0, clone.values.size());
    }

    @org.junit.Test
    public void testOptionConstructionWithArgAndLongOpt() {
        Option option = new Option("o", "opt", true, "Option with one arg")
                .longOpt("longOpt");
        assertEquals("o", option.getOpt());
        assertEquals("opt", option.getLongOpt());
        assertTrue(option.hasArg());
        assertEquals("Option with one arg", option.getDescription());
        assertFalse(option.isRequired());
        assertTrue(option.hasOptionalArg());
        assertTrue(option.hasLongOpt());
        assertEquals("longOpt", option.getLongOpt());
        assertFalse(option.hasArgName());
        assertFalse(option.hasArgs());
        assertNull(option.getValue());
        assertFalse(option.hasValueSeparator());
        assertEquals(1, option.getArgs());
        Option clone = option.clone();
        assertEquals(option, clone);
        assertNotNull(clone.values);
        assertEquals(0, clone.values.size());
    }

    @org.junit.Test
    public void testGetId() {
        Option option = new Option("o", "opt", true, "Option with one arg");
        assertEquals('o', option.getId());
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testInvalidOptionConstruction() {
        new Option("", "opt", false, "Option with no args");
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testInvalidOptionConstructionWithArg() {
        new Option("o", "", true, "Option with one arg");
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testInvalidOptionConstructionWithLongOpt() {
        new Option("o", "longOpt", true, "Option with one arg");
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testInvalidOptionConstructionWithDescription() {
        new Option("o", "opt", false, "");
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testInvalidOptionConstructionWithArgAndDescription() {
        new Option("o", "opt", true, "");
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testInvalidOptionConstructionWithHasArgAndDescription() {
        new Option("o", "opt", true, "Option with no args");
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testInvalidOptionConstructionWithArgAndHasArg() {
        new Option("o", "opt", true, true);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testInvalidOptionConstructionWithArgAndHasOptionalArg() {
        new Option("o", "opt", true, false);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testInvalidOptionConstructionWithArgAndDescriptionAndHasOptionalArg() {
        new Option("o", "opt", true, "Option with one arg", false);
    }

    @org.junit.Test
    public void testGetKey() {
        Option option = new Option("o", "opt", true, "Option with one arg");
        assertEquals("opt", option.getKey());
    }

    @org.junit.Test
    public void testGetOpt() {
        Option option = new Option("o", "opt", true, "Option with one arg");
        assertEquals("o", option.getOpt());
    }

    @org.junit.Test
    public void testGetLongOpt() {
        Option option = new Option("o", "opt", true, "Option with one arg")
                .longOpt("longOpt");
        assertEquals("longOpt", option.getLongOpt());
    }

    @org.junit.Test
    public void testSetOptionalArg() {
        Option option = new Option("o", "opt", true, "Option with one arg");
        option.setOptionalArg(true);
        assertTrue(option.hasOptionalArg());
    }

    @org.junit.Test
    public void testHasOptionalArg() {
        Option option = new Option("o", "opt", true, "Option with one arg");
        assertFalse(option.hasOptionalArg());
        option.setOptionalArg(true);
        assertTrue(option.hasOptionalArg());
    }

    @org.junit.Test
    public void testHasLongOpt() {
        Option option = new Option("o", "opt", true, "Option with one arg")
                .longOpt("longOpt");
        assertTrue(option.hasLongOpt());
    }

    @org.junit.Test
    public void testHasArg() {
        Option option = new Option("o", "opt", true, "Option with one arg");
        assertTrue(option.hasArg());
    }

    @org.junit.Test
    public void testGetDescription() {
        Option option = new Option("o", "opt", true, "Option with one arg");
        assertEquals("Option with one arg", option.getDescription());
    }

    @org.junit.Test
    public void testSetDescription() {
        Option option = new Option("o", "opt", true, "Option with one arg");
        option.setDescription("New description");
        assertEquals("New description", option.getDescription());
    }

    @org.junit.Test
    public void testIsRequired() {
        Option option = new Option("o", "opt", true, "Option with one arg");
        assertFalse(option.isRequired());
        option.setRequired(true);
        assertTrue(option.isRequired());
    }

    @org.junit.Test
    public void testSetRequired() {
        Option option = new Option("o", "opt", true, "Option with one arg");
        option.setRequired(true);
        assertTrue(option.isRequired());
    }

    @org.junit.Test
    public void testGetArgName() {
        Option option = new Option("o", "opt", true, "Option with one arg"
                .argName("argName"));
        assertEquals("argName", option.getArgName());
    }

    @org.junit.Test
    public void testSetArgName() {
        Option option = new Option("o", "opt", true, "Option with one arg");
        option.setArgName("argName");
        assertEquals("argName", option.getArgName());
    }

    @org.junit.Test
    public void testHasArgName() {
        Option option = new Option("o", "opt", true, "Option with one arg"
                .argName("argName"));
        assertTrue(option.hasArgName());
    }

    @org.junit.Test
    public void testHasArgs() {
        Option option = new Option("o", "opt", true, "Option with multiple args")
                .setArgs(2);
        assertTrue(option.hasArgs());
    }

    @org.junit.Test
    public void testSetArgs() {
        Option option = new Option("o", "opt", true, "Option with multiple args");
        option.setArgs(2);
        assertEquals(2, option.getArgs());
    }

    @org.junit.Test
    public void testSetValueSeparator() {
        Option option = new Option("o", "opt", true, "Option with value separator")
                .setValueSeparator(',');
        assertEquals(',', option.getValueSeparator());
    }

    @org.junit.Test
    public void testGetValueSeparator() {
        Option option = new Option("o", "opt", true, "Option with value separator")
                .setValueSeparator(',');
        assertEquals(',', option.getValueSeparator());
    }

    @org.junit.Test
    public void testHasValueSeparator() {
        Option option = new Option("o", "opt", true, "Option with value separator")
                .setValueSeparator(',');
        assertTrue(option.hasValueSeparator());
    }

    @org.junit.Test
    public void testGetArgs() {
        Option option = new Option("o", "opt", true, "Option with multiple args")
                .setArgs(2);
        assertEquals(2, option.getArgs());
    }

    @org.junit.Test
    public void testAddValueForProcessing() {
        Option option = new Option("o", "opt", true, "Option with multiple args")
                .setArgs(2)
                .setValueSeparator(',');
        List<String> values = new ArrayList<String>();
        option.setValueList(values);
        option.addValueForProcessing("val1");
        option.addValueForProcessing("val2");
        assertEquals(2, values.size());
        assertEquals("val1", values.get(0));
        assertEquals("val2", values.get(1));
    }

    @org.junit.Test
    public void testGetValue() {
        Option option = new Option("o", "opt", true, "Option with multiple args")
                .setArgs(2)
                .setValueSeparator(',');
        List<String> values = new ArrayList<String>();
        option.setValueList(values);
        option.addValueForProcessing("val1");
        option.addValueForProcessing("val2");
        assertEquals("val1", option.getValue());
    }

    @org.junit.Test
    public void testGetValueIndexOutOfBoundsException() {
        Option option = new Option("o", "opt", true, "Option with multiple args")
                .setArgs(2)
                .setValueSeparator(',');
        List<String> values = new ArrayList<String>();
        option.setValueList(values);
        option.addValueForProcessing("val1");
        option.addValueForProcessing("val2");
        try {
            option.getValue(2);
            fail("IndexOutOfBoundsException expected");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }

    @org.junit.Test
    public void testGetValueDefaultValue() {
        Option option = new Option("o", "opt", true, "Option with multiple args")
                .setArgs(2)
                .setValueSeparator(',');
        List<String> values = new ArrayList<String>();
        option.setValueList(values);
        option.addValueForProcessing("val1");
        option.addValueForProcessing("val2");
        assertEquals("val1", option.getValue("default"));
    }

    @org.junit.Test
    public void testGetValues() {
        Option option = new Option("o", "opt", true, "Option with multiple args")
                .setArgs(2)
                .setValueSeparator(',');
        List<String> values = new ArrayList<String>();
        option.setValueList(values);
        option.addValueForProcessing("val1");
        option.addValueForProcessing("val2");
        String[] expectedValues = new String[]{"val1", "val2"};
        assertArrayEquals(expectedValues, option.getValues());
    }

    @org.junit.Test
    public void testGetValuesList() {
        Option option = new Option("o", "opt", true, "Option with multiple args")
                .setArgs(2)
                .setValueSeparator(',');
        List<String> values = new ArrayList<String>();
        option.setValueList(values);
        option.addValueForProcessing("val1");
        option.addValueForProcessing("val2");
        assertEquals(values, option.getValuesList());
    }

    @org.junit.Test
    public void testClearValues() {
        Option option = new Option("o", "opt", true, "Option with multiple args")
                .setArgs(2)
                .setValueSeparator(',');
        List<String> values = new ArrayList<String>();
        option.setValueList(values);
        option.addValueForProcessing("val1");
        option.addValueForProcessing("val2");
        option.clearValues();
        assertEquals(0, values.size());
    }

    @org.junit.Test
    public void testAddValueUnsupportedOperationException() {
        Option option = new Option("o", "opt", true, "Option with multiple args")
                .setArgs(2)
                .setValueSeparator(',');
        Option clone = option.clone();
        List<String> values = new ArrayList<String>();
        clone.setValueList(values);
        try {
            clone.addValue("val");
            fail("UnsupportedOperationException expected");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }
}