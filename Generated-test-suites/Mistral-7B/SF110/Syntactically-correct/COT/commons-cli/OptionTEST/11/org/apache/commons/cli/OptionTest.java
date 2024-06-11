package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Parser;
import org.junit.Test;

public class OptionTest {

    @Test
    public void testOptionConstructor() {
        Option option = new Option("o", "opt", false, "Option description");
        assertEquals("o", option.getOpt());
        assertEquals("opt", option.getLongOpt());
        assertFalse(option.hasArg());
        assertEquals("Option description", option.getDescription());
        assertNull(option.getType());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.hasLongOpt());
        assertFalse(option.isRequired());
        assertNull(option.getArgName());
        assertFalse(option.hasArgs());
        assertFalse(option.hasValueSeparator());
        assertFalse(option.hasNoValues());
    }

    @Test
    public void testOptionRequiredConstructor() {
        Option option = new Option("o", "opt", true, "Option description");
        assertEquals("o", option.getOpt());
        assertEquals("opt", option.getLongOpt());
        assertFalse(option.hasArg());
        assertEquals("Option description", option.getDescription());
        assertNull(option.getType());
        assertTrue(option.isRequired());
    }

    @Test
    public void testOptionWithArgConstructor() {
        Option option = new Option("o", "opt", true, "Option description", "arg");
        assertEquals("o", option.getOpt());
        assertEquals("opt", option.getLongOpt());
        assertTrue(option.hasArg());
        assertEquals("arg", option.getArgName());
        assertEquals("Option description", option.getDescription());
        assertNull(option.getType());
        assertTrue(option.isRequired());
    }

    @Test
    public void testOptionWithArgAndTypeConstructor() {
        Option option = new Option("o", "opt", true, "Option description", "arg", Integer.TYPE);
        assertEquals("o", option.getOpt());
        assertEquals("opt", option.getLongOpt());
        assertTrue(option.hasArg());
        assertEquals("arg", option.getArgName());
        assertEquals("Option description", option.getDescription());
        assertEquals(Integer.TYPE, option.getType());
        assertTrue(option.isRequired());
    }

    @Test
    public void testOptionWithArgAndValuesConstructor() {
        Option option = new Option("o", "opt", true, "Option description", "arg") {
            @Override
            public void addValueForProcessing(String value) {
                super.addValueForProcessing(value);
                this.values.add(value.toUpperCase());
            }
        };
        assertEquals("o", option.getOpt());
        assertEquals("opt", option.getLongOpt());
        assertTrue(option.hasArg());
        assertEquals("arg", option.getArgName());
        assertEquals("Option description", option.getDescription());
        assertNull(option.getType());
        assertTrue(option.isRequired());

        OptionParser parser = new Parser();
        List<Object> args = new ArrayList<>();
        args.add("--opt=a");
        args.add("--opt=b");
        args.add("--opt=C");
        parser.parse(args, OptionTest.class.getDeclaredField("option"), false);

        List<String> values = option.getValuesList();
        assertEquals(3, values.size());
        assertEquals("a", values.get(0));
        assertEquals("b", values.get(1));
        assertEquals("C", values.get(2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidOptionName() {
        new Option("", "opt", false, "Option description");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidLongOptionName() {
        new Option("o", "", false, "Option description");
    }

    @Test
    public void testOptionGetId() {
        Option option = new Option("o", "opt", false, "Option description");
        assertEquals('o', option.getId());
    }

    @Test
    public void testOptionGetKey() {
        Option option = new Option("o", "opt", false, "Option description");
        assertEquals("opt", option.getKey());
        option.setLongOpt("longOpt");
        assertEquals("longOpt", option.getKey());
        option.setOpt("newOpt");
        assertEquals("newOpt", option.getKey());
    }

    @Test
    public void testOptionGetType() {
        Option option = new Option("o", "opt", false, "Option description");
        assertNull(option.getType());
        option.setType(Integer.TYPE);
        assertEquals(Integer.TYPE, option.getType());
    }

    @Test
    public void testOptionSetType() {
        Option option = new Option("o", "opt", false, "Option description");
        assertNull(option.getType());
        option.setType(Integer.TYPE);
        assertEquals(Integer.TYPE, option.getType());
    }

    @Test
    public void testOptionGetLongOpt() {
        Option option = new Option("o", "opt", false, "Option description");
        assertNull(option.getLongOpt());
        option.setLongOpt("longOpt");
        assertEquals("longOpt", option.getLongOpt());
    }

    @Test
    public void testOptionSetLongOpt() {
        Option option = new Option("o", "opt", false, "Option description");
        assertNull(option.getLongOpt());
        option.setLongOpt("longOpt");
        assertEquals("longOpt", option.getLongOpt());
    }

    @Test
    public void testOptionSetOptionalArg() {
        Option option = new Option("o", "opt", false, "Option description");
        assertFalse(option.hasOptionalArg());
        option.setOptionalArg(true);
        assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testOptionHasOptionalArg() {
        Option option = new Option("o", "opt", false, "Option description");
        assertFalse(option.hasOptionalArg());
        option.setOptionalArg(true);
        assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testOptionHasLongOpt() {
        Option option = new Option("o", "opt", false, "Option description");
        assertFalse(option.hasLongOpt());
        option.setLongOpt("longOpt");
        assertTrue(option.hasLongOpt());
    }

    @Test
    public void testOptionHasArg() {
        Option option = new Option("o", "opt", false, "Option description");
        assertFalse(option.hasArg());
        option.setArgName("arg");
        option.setArgs(1);
        assertTrue(option.hasArg());
        option.setArgs(2);
        assertTrue(option.hasArgs());
    }

    @Test
    public void testOptionGetDescription() {
        Option option = new Option("o", "opt", false, "Option description");
        assertEquals("Option description", option.getDescription());
        option.setDescription("New description");
        assertEquals("New description", option.getDescription());
    }

    @Test
    public void testOptionSetDescription() {
        Option option = new Option("o", "opt", false, "Option description");
        assertEquals("Option description", option.getDescription());
        option.setDescription("New description");
        assertEquals("New description", option.getDescription());
    }

    @Test
    public void testOptionIsRequired() {
        Option option = new Option("o", "opt", false, "Option description");
        assertFalse(option.isRequired());
        option.setRequired(true);
        assertTrue(option.isRequired());
    }

    @Test
    public void testOptionSetRequired() {
        Option option = new Option("o", "opt", false, "Option description");
        assertFalse(option.isRequired());
        option.setRequired(true);
        assertTrue(option.isRequired());
    }

    @Test
    public void testOptionSetArgName() {
        Option option = new Option("o", "opt", false, "Option description");
        assertNull(option.getArgName());
        option.setArgName("arg");
        assertEquals("arg", option.getArgName());
    }

    @Test
    public void testOptionGetArgName() {
        Option option = new Option("o", "opt", false, "Option description");
        assertNull(option.getArgName());
        option.setArgName("arg");
        assertEquals("arg", option.getArgName());
    }

    @Test
    public void testOptionHasArgs() {
        Option option = new Option("o", "opt", false, "Option description");
        assertFalse(option.hasArgs());
        option.setArgs(1);
        assertTrue(option.hasArgs());
        option.setArgs(2);
        assertTrue(option.hasArgs());
    }

    @Test
    public void testOptionGetArgs() {
        Option option = new Option("o", "opt", false, "Option description");
        assertEquals(-1, option.getArgs());
        option.setArgs(1);
        assertEquals(1, option.getArgs());
        option.setArgs(2);
        assertEquals(2, option.getArgs());
    }

    @Test
    public void testOptionSetArgs() {
        Option option = new Option("o", "opt", false, "Option description");
        assertEquals(-1, option.getArgs());
        option.setArgs(1);
        assertEquals(1, option.getArgs());
        option.setArgs(2);
        assertEquals(2, option.getArgs());
    }

    @Test
    public void testOptionSetValueSeparator() {
        Option option = new Option("o", "opt", false, "Option description");
        assertEquals(' ', option.getValueSeparator());
        option.setValueSeparator(',');
        assertEquals(',', option.getValueSeparator());
    }

    @Test
    public void testOptionGetValueSeparator() {
        Option option = new Option("o", "opt", false, "Option description");
        assertEquals(' ', option.getValueSeparator());
        option.setValueSeparator(',');
        assertEquals(',', option.getValueSeparator());
    }

    @Test
    public void testOptionHasValueSeparator() {
        Option option = new Option("o", "opt", false, "Option description");
        assertFalse(option.hasValueSeparator());
        option.setValueSeparator(',');
        assertTrue(option.hasValueSeparator());
    }

    @Test
    public void testOptionGetValue() {
        Option option = new Option("o", "opt", false, "Option description");
        assertNull(option.getValue());
        option.addValueForProcessing("value");
        assertEquals("value", option.getValue());
        option.addValueForProcessing("value2");
        assertEquals("value", option.getValue());
    }

    @Test
    public void testOptionGetValueIndex() {
        Option option = new Option("o", "opt", false, "Option description");
        assertNull(option.getValue(0));
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        assertEquals("value1", option.getValue(0));
        assertEquals("value2", option.getValue(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testOptionGetValueIndexOutOfBounds() {
        Option option = new Option("o", "opt", false, "Option description");
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        option.getValue(2);
    }

    @Test
    public void testOptionGetValues() {
        Option option = new Option("o", "opt", false, "Option description");
        assertNull(option.getValues());
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        assertArrayEquals(new String[]{"value1", "value2"}, option.getValues());
    }

    @Test
    public void testOptionGetValuesList() {
        Option option = new Option("o", "opt", false, "Option description");
        assertNull(option.getValuesList());
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        List<String> values = option.getValuesList();
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testOptionToString() {
        Option option = new Option("o", "opt", false, "Option description");
        String toString = option.toString();
        assertEquals("[ option: o :: Option description ]", toString);
        option.setLongOpt("longOpt");
        toString = option.toString();
        assertEquals("[ option: o :: Option description ]", toString);
        option.setOpt("newOpt");
        toString = option.toString();
        assertEquals("[ option: newOpt :: Option description ]", toString);
        option.setArgName("arg");
        toString = option.toString();
        assertEquals("[ option: o :: [ARG] :: Option description ]", toString);
        option.setArgs(1);
        toString = option.toString();
        assertEquals("[ option: o :: [ARG] :: Option description ]", toString);
        option.setArgs(2);
        toString = option.toString();
        assertEquals("[ option: o :: [ARG, ARG] :: Option description ]", toString);
    }

    @Test
    public void testOptionClearValues() {
        Option option = new Option("o", "opt", false, "Option description");
        assertNull(option.getValues());
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        option.clearValues();
        assertNull(option.getValues());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testOptionAddValue() {
        Option option = new Option("o", "opt", false, "Option description");
        option.addValue("value");
    }

    private Option option;

    static {
        try {
            OptionTest.class.getDeclaredField("option");
            OptionTest.class.getDeclaredField("option").setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new Error(e.getMessage());
        }
    }
}