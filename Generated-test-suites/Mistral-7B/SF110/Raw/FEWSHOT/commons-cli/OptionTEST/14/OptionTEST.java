package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class OptionTest {

    @Test
    public void testOptionCreation() {
        Option option = new Option("opt", "option description");
        assertEquals("opt", option.getOpt());
        assertEquals(Option.UNINITIALIZED, option.numberOfArgs);
        assertNull(option.getType());
        assertEquals("option description", option.description);

        option = new Option("opt", "option description", true);
        assertEquals("opt", option.getOpt());
        assertEquals(Option.UNINITIALIZED, option.numberOfArgs);
        assertTrue(option.optionalArg);
        assertEquals("option description", option.description);

        option = new Option("opt", "long option description", true, "long opt description");
        assertEquals("opt", option.getOpt());
        assertEquals("long opt description", option.description);
        assertEquals("long opt", option.longOpt);
        assertTrue(option.optionalArg);
    }

    @Test
    public void testOptionCreationWithArgs() {
        Option option = new Option("opt", "option description", 1);
        assertEquals("opt", option.getOpt());
        assertEquals(1, option.numberOfArgs);
        assertNull(option.getType());
        assertEquals("option description", option.description);

        option = new Option("opt", "option description", 2, "option description with args");
        assertEquals("opt", option.getOpt());
        assertEquals(2, option.numberOfArgs);
        assertNull(option.getType());
        assertEquals("option description with args", option.description);
    }

    @Test
    public void testOptionCreationWithType() {
        Option option = new Option("opt", "option description", null, Integer.class);
        assertEquals("opt", option.getOpt());
        assertEquals(Option.UNINITIALIZED, option.numberOfArgs);
        assertSame(Integer.class, option.getType());
        assertEquals("option description", option.description);
    }

    @Test
    public void testOptionCreationWithValues() {
        Option option = new Option("opt", "option description", 1, Arrays.asList("value1", "value2"));
        assertEquals("opt", option.getOpt());
        assertEquals(1, option.numberOfArgs);
        assertSame(String[].class, option.getType().getClass());
        assertEquals("option description", option.description);
        assertEquals(Arrays.asList("value1", "value2"), option.values);
    }

    @Test
    public void testOptionCreationWithValueSeparator() {
        Option option = new Option("opt", "option description", 1, "value1, value2");
        assertEquals("opt", option.getOpt());
        assertEquals(1, option.numberOfArgs);
        assertSame(String[].class, option.getType().getClass());
        assertEquals("option description", option.description);
        assertEquals("value1, value2", option.valuesep.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionCreationWithInvalidArgNumber() {
        new Option("opt", "option description", -1);
        fail();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionCreationWithInvalidArgNumber2() {
        new Option("opt", "option description", Option.UNLIMITED_VALUES);
        fail();
    }

    @Test
    public void testOptionId() {
        Option option = new Option("opt", "option description");
        assertEquals('o', option.getId());

        option = new Option("longOpt", "option description");
        assertEquals('l', option.getId());
    }

    @Test
    public void testOptionGettersAndSetters() {
        Option option = new Option("opt", "option description");

        option.setOptionalArg(true);
        assertTrue(option.optionalArg);

        option.setOptionalArg(false);
        assertFalse(option.optionalArg);

        option.setDescription("new option description");
        assertEquals("new option description", option.description);

        option.setArgName("argName");
        assertEquals("argName", option.argName);

        option.setRequired(true);
        assertTrue(option.required);

        option.setRequired(false);
        assertFalse(option.required);

        option.setNumberOfArgs(1);
        assertEquals(1, option.numberOfArgs);

        option.setType(Integer.class);
        assertSame(Integer.class, option.getType());

        option.setValues(new ArrayList<String>());
        assertEquals(Arrays.asList(), option.getValuesList());

        option.setValueSeparator(';');
        assertEquals(';', option.getValueSeparator());
    }

    @Test
    public void testOptionHasArg() {
        Option option = new Option("opt", "option description");
        assertFalse(option.hasArg());

        option.setNumberOfArgs(1);
        assertTrue(option.hasArg());

        option.setNumberOfArgs(-2);
        assertTrue(option.hasArgs());
    }

    @Test
    public void testOptionHasLongOpt() {
        Option option = new Option("opt", "option description");
        assertFalse(option.hasLongOpt());

        option.setLongOpt("longOpt");
        assertTrue(option.hasLongOpt());
    }

    @Test
    public void testOptionToString() {
        Option option = new Option("opt", "option description");
        String expected = "[ option: opt  ]";
        assertEquals(expected, option.toString());

        option.setLongOpt("longOpt");
        expected = "[ option: opt  " + " longOpt " + "  ]";
        assertEquals(expected, option.toString());

        option.setNumberOfArgs(1);
        expected = "[ option: opt  [ARG]  ]";
        assertEquals(expected, option.toString());

        option.setArgName("argName");
        expected = "[ option: opt  [ARGName]  ]";
        assertEquals(expected, option.toString());

        option.setDescription("new option description");
        expected = "[ option: opt  [ARGName]  :: new option description  ]";
        assertEquals(expected, option.toString());

        option.setType(Integer.class);
        expected = "[ option: opt  [ARGName]  :: new option description  :: int  ]";
        assertEquals(expected, option.toString());
    }

    @Test
    public void testOptionEquals() {
        Option option1 = new Option("opt1", "option description 1");
        Option option2 = new Option("opt2", "option description 2");
        Option option3 = new Option("opt1", "option description 1");

        assertFalse(option1.equals(null));
        assertFalse(option1.equals(new Object()));
        assertFalse(option1.equals(option2));
        assertTrue(option1.equals(option3));
    }

    @Test
    public void testOptionHashCode() {
        Option option1 = new Option("opt1", "option description 1");
        Option option2 = new Option("opt2", "option description 2");
        Option option3 = new Option("opt1", "option description 1");

        int hashCode1 = option1.hashCode();
        int hashCode2 = option2.hashCode();
        int hashCode3 = option3.hashCode();

        assertEquals(hashCode1, option1.hashCode());
        assertEquals(hashCode2, option2.hashCode());
        assertEquals(hashCode1, option3.hashCode());
    }

    @Test
    public void testOptionClone() {
        Option option = new Option("opt", "option description");
        Option clonedOption = option.clone();
        assertEquals(option, clonedOption);

        option.setDescription("new option description");
        assertNotSame(option.description, clonedOption.description);
    }

    @Test
    public void testOptionClearValues() {
        Option option = new Option("opt", "option description");
        option.setNumberOfArgs(1);
        option.addValue("value1");
        option.addValue("value2");

        option.clearValues();
        assertEquals(Arrays.asList(), option.getValuesList());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testOptionAddValue() {
        Option option = new Option("opt", "option description");
        option.addValue("value");
    }
}