package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.ParseException;
import org.junit.Test;

public class OptionTest {

    @Test
    public void testOptionConstructorWithOptAndDescription() {
        Option option = new Option("opt", "description");
        assertEquals("opt", option.getOpt());
        assertEquals("description", option.getDescription());
        assertNull(option.getLongOpt());
        assertFalse(option.hasArg());
        assertFalse(option.isRequired());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.hasLongOpt());
        assertFalse(option.hasArgName());
        assertFalse(option.hasArgs());
        assertFalse(option.hasValueSeparator());
        assertFalse(option.hasValues());
        assertTrue(option.toString().contains("[ option: opt :: description ]"));
    }

    @Test
    public void testOptionConstructorWithOptHasArgAndDescription() {
        Option option = new Option("opt", true, "description");
        assertEquals("opt", option.getOpt());
        assertEquals("description", option.getDescription());
        assertTrue(option.hasArg());
        assertFalse(option.isRequired());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.hasLongOpt());
        assertFalse(option.hasArgName());
        assertFalse(option.hasArgs());
        assertFalse(option.hasValueSeparator());
        assertFalse(option.hasValues());
        assertTrue(option.toString().contains("[ option: opt :: ARG :: description ]"));
    }

    @Test
    public void testOptionConstructorWithOptLongOptHasArgAndDescription() {
        Option option = new Option("opt", "long-opt", true, "description");
        assertEquals("opt", option.getOpt());
        assertEquals("long-opt", option.getLongOpt());
        assertTrue(option.hasArg());
        assertFalse(option.isRequired());
        assertFalse(option.hasOptionalArg());
        assertTrue(option.hasLongOpt());
        assertFalse(option.hasArgName());
        assertFalse(option.hasArgs());
        assertFalse(option.hasValueSeparator());
        assertFalse(option.hasValues());
        assertTrue(option.toString().contains("[ option: opt :: long-opt :: ARG :: description ]"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionConstructorWithInvalidOpt() {
        Option option = new Option("", "description");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionConstructorWithInvalidOptAndLongOpt() {
        Option option = new Option("", "description", false, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionConstructorWithInvalidOptLongOptAndHasArg() {
        Option option = new Option("", "description", true, "");
    }

    @Test
    public void testOptionGetId() {
        Option option = new Option("opt", "description");
        assertEquals('o', option.getId());
    }

    @Test
    public void testOptionGetKey() {
        Option option = new Option("opt", "description");
        assertEquals("opt", option.getKey());
        Option longOption = new Option("", "long-opt");
        assertEquals("long-opt", option.getKey());
        Option longOptionWithOpt = new Option("opt2", "long-opt");
        assertEquals("long-opt", longOptionWithOpt.getKey());
    }

    @Test
    public void testOptionGetOpt() {
        Option option = new Option("opt", "description");
        assertEquals("opt", option.getOpt());
    }

    @Test
    public void testOptionGetLongOpt() {
        Option option = new Option("opt", null, false, "description");
        assertNull(option.getLongOpt());
        Option longOption = new Option(null, "long-opt");
        assertEquals("long-opt", option.getLongOpt());
        Option longOptionWithOpt = new Option("opt2", "long-opt");
        assertEquals(null, longOptionWithOpt.getLongOpt());
    }

    @Test
    public void testOptionSetOptionalArg() {
        Option option = new Option("opt", "description");
        assertFalse(option.hasOptionalArg());
        option.setOptionalArg(true);
        assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testOptionHasOptionalArg() {
        Option option = new Option("opt", "description");
        assertFalse(option.hasOptionalArg());
        Option optionalOption = new Option("opt", "description", true, "description");
        assertTrue(optionalOption.hasOptionalArg());
    }

    @Test
    public void testOptionHasLongOpt() {
        Option option = new Option("opt", "description");
        assertFalse(option.hasLongOpt());
        Option longOption = new Option(null, "long-opt");
        assertTrue(longOption.hasLongOpt());
        Option longOptionWithOpt = new Option("opt2", "long-opt");
        assertFalse(longOptionWithOpt.hasLongOpt());
    }

    @Test
    public void testOptionHasArg() {
        Option option = new Option("opt", "description");
        assertFalse(option.hasArg());
        Option argOption = new Option("opt", true, "description");
        assertTrue(argOption.hasArg());
    }

    @Test
    public void testOptionGetDescription() {
        Option option = new Option("opt", "description");
        assertEquals("description", option.getDescription());
    }

    @Test
    public void testOptionSetDescription() {
        Option option = new Option("opt", "description");
        assertEquals("description", option.getDescription());
        option.setDescription("new-description");
        assertEquals("new-description", option.getDescription());
    }

    @Test
    public void testOptionIsRequired() {
        Option option = new Option("opt", "description");
        assertFalse(option.isRequired());
        Option requiredOption = new Option("opt", "description", true, "description");
        assertTrue(requiredOption.isRequired());
    }

    @Test
    public void testOptionSetRequired() {
        Option option = new Option("opt", "description");
        assertFalse(option.isRequired());
        option.setRequired(true);
        assertTrue(option.isRequired());
    }

    @Test
    public void testOptionSetArgName() {
        Option option = new Option("opt", "description");
        assertNull(option.getArgName());
        Option argNameOption = new Option("opt", true, "description", "argName");
        assertEquals("argName", option.getArgName());
    }

    @Test
    public void testOptionHasArgName() {
        Option option = new Option("opt", "description");
        assertFalse(option.hasArgName());
        Option argNameOption = new Option("opt", true, "description", "argName");
        assertTrue(option.hasArgName());
    }

    @Test
    public void testOptionHasArgs() {
        Option option = new Option("opt", "description");
        assertFalse(option.hasArgs());
        Option argOption = new Option("opt", true, "description", "argName");
        assertFalse(argOption.hasArgs());
        Option argsOption = new Option("opt", true, "description", "argName", 2);
        assertTrue(argsOption.hasArgs());
    }

    @Test
    public void testOptionSetArgs() {
        Option option = new Option("opt", "description");
        assertFalse(option.hasArgs());
        option.setArgs(2);
        assertTrue(option.hasArgs());
    }

    @Test
    public void testOptionSetValueSeparator() {
        Option option = new Option("opt", "description");
        assertFalse(option.hasValueSeparator());
        option.setValueSeparator(':');
        assertTrue(option.hasValueSeparator());
    }

    @Test
    public void testOptionGetValueSeparator() {
        Option option = new Option("opt", "description");
        assertFalse(option.hasValueSeparator());
        option.setValueSeparator(':');
        assertTrue(option.hasValueSeparator());
        assertEquals(':', option.getValueSeparator());
    }

    @Test
    public void testOptionGetArgs() {
        Option option = new Option("opt", "description");
        assertEquals(-1, option.getArgs());
        Option argOption = new Option("opt", true, "description");
        assertEquals(1, option.getArgs());
        Option argsOption = new Option("opt", true, "description", null, 2);
        assertEquals(2, option.getArgs());
    }

    @Test(expected = RuntimeException.class)
    public void testOptionAddValueForProcessingWithNoArgsAllowed() {
        Option option = new Option("opt", "description");
        assertEquals(-1, option.getArgs());
        String value = "value";
        option.addValueForProcessing(value);
    }

    @Test
    public void testOptionProcessValue() {
        Option option = new Option("opt", null, false, null, 3, ',');
        option.processValue("value1,value2,value3");
        List<String> expectedValues = new ArrayList<String>();
        expectedValues.add("value1");
        expectedValues.add("value2");
        expectedValues.add("value3");
        assertEquals(expectedValues, option.getValuesList());
    }

    @Test
    public void testOptionAddValue() {
        throw new UnsupportedOperationException("The addValue method is not intended for client use. Subclasses should use the addValueForProcessing method instead. ");
    }

    @Test
    public void testOptionGetValue() {
        Option option = new Option("opt", null, false, null, 1);
        option.addValue("value");
        assertEquals("value", option.getValue());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testOptionGetValueWithIndexOutOfBounds() {
        Option option = new Option("opt", null, false, null, 1);
        option.addValue("value1");
        option.addValue("value2");
        String value = option.getValue(1);
    }

    @Test
    public void testOptionGetValueWithDefaultValue() {
        Option option = new Option("opt", null, false, null, 1);
        option.addValue("value1");
        String defaultValue = "default";
        assertEquals("value1", option.getValue(null));
        assertEquals("default", option.getValue(defaultValue));
    }

    @Test
    public void testOptionGetValues() {
        Option option = new Option("opt", null, false, null, 1);
        option.addValue("value1");
        String[] expectedValues = new String[]{"value1"};
        assertArrayEquals(expectedValues, option.getValues());
    }

    @Test
    public void testOptionGetValuesList() {
        Option option = new Option("opt", null, false, null, 1);
        option.addValue("value1");
        List<String> expectedValues = new ArrayList<String>();
        expectedValues.add("value1");
        assertEquals(expectedValues, option.getValuesList());
    }

    @Test
    public void testOptionToString() {
        Option option = new Option("opt", null, false, null, 1);
        option.setDescription("description");
        option.setValueSeparator(':');
        assertEquals("[ option: opt :: description :: ARG :: ]", option.toString());
    }

    @Test
    public void testOptionClearValues() {
        Option option = new Option("opt", null, false, null, 1);
        option.addValue("value1");
        option.clearValues();
        assertEquals(0, option.getValuesList().size());
    }
}