package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class OptionTest {

    @org.junit.Test
    public void testOptionConstruction() throws Exception {
        Option option = new Option("opt", "Option description.");
        assertEquals("opt", option.getOpt());
        assertEquals("Option description.", option.getDescription());
        assertNull(option.getLongOpt());
        assertFalse(option.hasArg());
        assertFalse(option.hasLongOpt());
        assertFalse(option.isRequired());
        assertFalse(option.hasOptionalArg());
        assertNull(option.getType());
        assertNull(option.getArgName());
        assertFalse(option.hasArgName());
        assertFalse(option.hasArgs());
        assertFalse(option.hasValueSeparator());
        assertNull(option.getValue());
        assertNull(option.getValues());
        assertFalse(option.hasNoValues());

        Option longOption = new Option("opt", "Long option description.", true, "LONG_ARG");
        assertEquals("opt", option.getOpt());
        assertEquals("Long option description.", option.getDescription());
        assertEquals("LONG_ARG", option.getLongOpt());
        assertTrue(option.hasOptionalArg());
        assertFalse(option.isRequired());
        assertNull(option.getType());
        assertNull(option.getArgName());
        assertFalse(option.hasArgName());
        assertFalse(option.hasArgs());
        assertFalse(option.hasValueSeparator());
        assertNull(option.getValue());
        assertNull(option.getValues());
        assertFalse(option.hasNoValues());

        Option hasArgOption = new Option("opt", "Option description.", false, "ARG");
        assertEquals("opt", option.getOpt());
        assertEquals("Option description.", option.getDescription());
        assertNull(option.getLongOpt());
        assertTrue(option.hasArg());
        assertFalse(option.isRequired());
        assertNull(option.getType());
        assertEquals("ARG", option.getArgName());
        assertFalse(option.hasArgName());
        assertFalse(option.hasArgs());
        assertFalse(option.hasValueSeparator());
        assertNull(option.getValue());
        assertNull(option.getValues());
        assertFalse(option.hasNoValues());

        Option hasArgAndLongOptOption = new Option("opt", "Option with long opt.", false, "ARG", "LONG_OPT", "Option description with long opt.");
        assertEquals("opt", option.getOpt());
        assertEquals("Option description with long opt.", option.getDescription());
        assertEquals("LONG_OPT", option.getLongOpt());
        assertTrue(option.hasArg());
        assertTrue(option.hasLongOpt());
        assertFalse(option.isRequired());
        assertNull(option.getType());
        assertEquals("ARG", option.getArgName());
        assertFalse(option.hasArgName());
        assertFalse(option.hasArgs());
        assertFalse(option.hasValueSeparator());
        assertNull(option.getValue());
        assertNull(option.getValues());
        assertFalse(option.hasNoValues());
    }

    @org.junit.Test
    public void testOptionId() throws Exception {
        Option option = new Option("opt", "Option description.");
        assertEquals('o', option.getId());

        Option longOption = new Option("opt", "Long option description.", true, "LONG_ARG");
        assertEquals('o', longOption.getId());
    }

    @org.junit.Test
    public void testOptionGettersAndSetters() throws Exception {
        Option option = new Option("opt", "Option description.");
        option.setDescription("Changed option description.");
        assertEquals("Changed option description.", option.getDescription());

        Option longOption = new Option("opt", "Long option description.", true, "LONG_ARG");
        longOption.setLongOpt("CHANGED_LONG_OPT");
        assertEquals("CHANGED_LONG_OPT", longOption.getLongOpt());

        Option hasArgOption = new Option("opt", "Option description.", false, "ARG");
        hasArgOption.setArgName("ARG_NAME");
        assertEquals("ARG_NAME", hasArgOption.getArgName());

        Option hasArgAndLongOptOption = new Option("opt", "Option with long opt.", false, "ARG", "LONG_OPT", "Option description with long opt.");
        hasArgAndLongOptOption.setOptionalArg(true);
        assertTrue(hasArgAndLongOptOption.hasOptionalArg());

        Option hasValueOption = new Option("opt", "Option description.", false, null, null, "VALUE", false, 1);
        assertEquals("VALUE", hasValueOption.getValue());

        Option hasValueListOption = new Option("opt", "Option description.", false, null, null, "VALUE1, VALUE2", false, 2);
        String[] values = hasValueListOption.getValues();
        assertEquals(2, values.length);
        assertEquals("VALUE1", values[0]);
        assertEquals("VALUE2", values[1]);

        Option hasValueSeparatorOption = new Option("opt", "Option description.", false, null, null, null, true, ',');
        List<String> valuesList = hasValueSeparatorOption.getValuesList();
        assertEquals(2, valuesList.size());
        assertEquals("VALUE1", valuesList.get(0));
        assertEquals("VALUE2", valuesList.get(1));
    }

    @org.junit.Test
    public void testOptionValidation() throws Exception {
        try {
            Option option = new Option("", "Option description.");
            fail("Expected IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            Option option = new Option("opt", "");
            fail("Expected IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            Option option = new Option("opt", "Option description.", true, "");
            fail("Expected IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            Option option = new Option("opt", "Option description.", false, null);
            fail("Expected IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            Option option = new Option("opt", "Option description.", false, null, null, null, true, ',', -1);
            fail("Expected IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            Option option = new Option("opt", "Option description.", false, null, null, null, true, ',', 0);
            fail("Expected IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            Option option = new Option("opt", "Option description.", false, null, null, null, true, ',', 1);
            List<String> values = new ArrayList<String>();
            option.addValueForProcessing("VALUE");
            option.addValueForProcessing("VALUE2");
            assertEquals(2, values.size());
            assertEquals("VALUE", values.get(0));
            assertEquals("VALUE2", values.get(1));
        } catch (RuntimeException e) {
            fail("Expected no exception.");
        }

        try {
            Option option = new Option("opt", "Option description.", false, null, null, null, true, ',', 2);
            List<String> values = new ArrayList<String>();
            option.addValueForProcessing("VALUE");
            option.addValueForProcessing("VALUE2");
            option.addValueForProcessing("VALUE3");
            assertEquals(3, values.size());
            assertEquals("VALUE", values.get(0));
            assertEquals("VALUE2", values.get(1));
            assertEquals("VALUE3", values.get(2));
        } catch (RuntimeException e) {
            fail("Expected no exception.");
        }

        try {
            Option option = new Option("opt", "Option description.", false, null, null, null, true, ',', 3);
            List<String> values = new ArrayList<String>();
            option.addValueForProcessing("VALUE");
            option.addValueForProcessing("VALUE2");
            option.addValueForProcessing("VALUE3");
            option.addValueForProcessing("VALUE4");
            assertEquals(4, values.size());
            assertEquals("VALUE", values.get(0));
            assertEquals("VALUE2", values.get(1));
            assertEquals("VALUE3", values.get(2));
            assertEquals("VALUE4", values.get(3));
        } catch (RuntimeException e) {
            fail("Expected no exception.");
        }

        try {
            Option option = new Option("opt", "Option description.", false, null, null, null, true, ',', 4);
            List<String> values = new ArrayList<String>();
            option.addValueForProcessing("VALUE");
            option.addValueForProcessing("VALUE2");
            option.addValueForProcessing("VALUE3");
            option.addValueForProcessing("VALUE4");
            option.addValueForProcessing("VALUE5");
            assertEquals(5, values.size());
            assertEquals("VALUE", values.get(0));
            assertEquals("VALUE2", values.get(1));
            assertEquals("VALUE3", values.get(2));
            assertEquals("VALUE4", values.get(3));
            assertEquals("VALUE5", values.get(4));
        } catch (RuntimeException e) {
            fail("Expected no exception.");
        }
    }

    @org.junit.Test
    public void testOptionToString() throws Exception {
        Option option = new Option("opt", "Option description.");
        assertEquals("[ option: opt :: Option description. ]", option.toString());

        Option longOption = new Option("opt", "Option description.", true, "LONG_ARG");
        assertEquals("[ option: opt :: Option description. [LONG_ARG]]", longOption.toString());

        Option hasArgOption = new Option("opt", "Option description.", false, "ARG");
        assertEquals("[ option: opt :: Option description. [ARG]]", hasArgOption.toString());

        Option hasArgAndLongOptOption = new Option("opt", "Option with long opt.", false, "ARG", "LONG_OPT", "Option description with long opt.");
        assertEquals("[ option: opt :: Option description with long opt. [ARG] ]", hasArgAndLongOptOption.toString());

        Option hasValueOption = new Option("opt", "Option description.", false, null, null, "VALUE", false, 1);
        assertEquals("[ option: opt :: Option description. VALUE ]", hasValueOption.toString());

        Option hasValueListOption = new Option("opt", "Option description.", false, null, null, "VALUE1, VALUE2", false, 2);
        assertEquals("[ option: opt :: Option description. VALUE1, VALUE2 ]", hasValueListOption.toString());

        Option hasValueSeparatorOption = new Option("opt", "Option description.", false, null, null, null, true, ',');
        assertEquals("[ option: opt :: Option description. ]", hasValueSeparatorOption.toString());
    }

    @org.junit.Test
    public void testOptionEqualsAndHashCode() throws Exception {
        Option option1 = new Option("opt1", "Option description.");
        Option option2 = new Option("opt1", "Option description.");
        assertTrue(option1.equals(option2));
        assertEquals(option1.hashCode(), option2.hashCode());

        Option option3 = new Option("opt2", "Option description.");
        assertFalse(option1.equals(option3));
        assertNotEquals(option1.hashCode(), option3.hashCode());

        Option option4 = new Option("opt1", "Option description.");
        assertTrue(option1.equals(option4));
        assertEquals(option1.hashCode(), option4.hashCode());

        Option longOption1 = new Option("opt1", "Option description.", true, "LONG_ARG1");
        Option longOption2 = new Option("opt1", "Option description.", true, "LONG_ARG2");
        assertFalse(longOption1.equals(longOption2));
        assertNotEquals(longOption1.hashCode(), longOption2.hashCode());

        Option longOption3 = new Option("opt1", "Option description.", true, "LONG_ARG1");
        assertTrue(longOption1.equals(longOption3));
        assertEquals(longOption1.hashCode(), longOption3.hashCode());

        Option argOption1 = new Option("opt1", "Option description.", false, "ARG1");
        Option argOption2 = new Option("opt1", "Option description.", false, "ARG2");
        assertFalse(argOption1.equals(argOption2));
        assertNotEquals(argOption1.hashCode(), argOption2.hashCode());

        Option argOption3 = new Option("opt1", "Option description.", false, "ARG1");
        assertTrue(argOption1.equals(argOption3));
        assertEquals(argOption1.hashCode(), argOption3.hashCode());
    }

    @org.junit.Test
    public void testOptionClone() throws Exception {
        Option option = new Option("opt", "Option description.");
        Option clonedOption = option.clone();
        assertEquals(option, clonedOption);
        assertNotSame(option, clonedOption);

        Option longOption = new Option("opt", "Option description.", true, "LONG_ARG");
        Option clonedLongOption = longOption.clone();
        assertEquals(longOption, clonedLongOption);
        assertNotSame(longOption, clonedLongOption);

        Option argOption = new Option("opt", "Option description.", false, "ARG");
        Option clonedArgOption = argOption.clone();
        assertEquals(argOption, clonedArgOption);
        assertNotSame(argOption, clonedArgOption);
    }

    @org.junit.Test
    public void testOptionClearValues() throws Exception {
        Option option = new Option("opt", "Option description.");
        option.addValueForProcessing("VALUE1");
        option.addValueForProcessing("VALUE2");
        option.clearValues();
        assertFalse(option.hasNoValues());
        assertNull(option.getValue());
        assertNull(option.getValues());
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testOptionAddValue() throws Exception {
        Option option = new Option("opt", "Option description.");
        option.addValue("VALUE");
    }
}