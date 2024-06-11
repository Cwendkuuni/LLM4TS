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
    public void testOptionConstruction() {
        Option option = new Option("o", "opt", false, "Option with no argument");
        assertEquals("o", option.getOpt());
        assertEquals("opt", option.getLongOpt());
        assertFalse(option.hasArg());
        assertEquals("Option with no argument", option.getDescription());
        assertNull(option.getType());
        assertFalse(option.isRequired());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.hasLongOpt());
        assertFalse(option.hasArgName());
        assertFalse(option.hasArgs());
        assertEquals(Optional.empty(), option.getValuesList());

        option = new Option("o", "opt", true, "Option with required argument");
        assertEquals("o", option.getOpt());
        assertEquals("opt", option.getLongOpt());
        assertTrue(option.hasArg());
        assertEquals("Option with required argument", option.getDescription());
        assertNull(option.getType());
        assertTrue(option.isRequired());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.hasLongOpt());
        assertFalse(option.hasArgName());
        assertFalse(option.hasArgs());
        assertEquals(Optional.empty(), option.getValuesList());

        option = new Option("o", null, false, "Option with no argument");
        assertEquals(null, option.getOpt());
        assertNull(option.getLongOpt());
        assertFalse(option.hasArg());
        assertEquals("Option with no argument", option.getDescription());
        assertNull(option.getType());
        assertFalse(option.isRequired());
        assertFalse(option.hasOptionalArg());
        assertTrue(option.hasLongOpt());
        assertFalse(option.hasArgName());
        assertFalse(option.hasArgs());
        assertEquals(Optional.empty(), option.getValuesList());

        option = new Option("o", "longopt", false, "Option with long option");
        assertEquals("o", option.getOpt());
        assertEquals("longopt", option.getLongOpt());
        assertFalse(option.hasArg());
        assertEquals("Option with long option", option.getDescription());
        assertNull(option.getType());
        assertFalse(option.isRequired());
        assertFalse(option.hasOptionalArg());
        assertTrue(option.hasArgName());
        assertFalse(option.hasArgs());
        assertEquals(Optional.empty(), option.getValuesList());

        option = new Option("o", null, false, "Option with no argument", "defaultValue");
        assertEquals("o", option.getOpt());
        assertNull(option.getLongOpt());
        assertFalse(option.hasArg());
        assertEquals("Option with no argument", option.getDescription());
        assertNull(option.getType());
        assertFalse(option.isRequired());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.hasLongOpt());
        assertFalse(option.hasArgName());
        assertFalse(option.hasArgs());
        assertEquals(Optional.of("defaultValue"), option.getValuesList());
    }

    @Test
    public void testOptionSettersAndGetters() {
        Option option = new Option("o", "opt", false, "Option with no argument");
        option.setDescription("Updated Option with no argument");
        assertEquals("Updated Option with no argument", option.getDescription());

        option.setRequired(true);
        assertTrue(option.isRequired());

        option.setOptionalArg(true);
        assertTrue(option.hasOptionalArg());

        option.setArgName("argName");
        assertEquals("argName", option.getArgName());

        option.setArgs(1);
        assertEquals(1, option.getArgs());

        option.setValueSeparator(';');
        assertTrue(option.hasValueSeparator());

        List<String> values = new ArrayList<>();
        values.add("value1");
        values.add("value2");
        option.setValues(values);
        assertEquals(values, option.getValuesList());

        option.addValue("value3");
        List<String> updatedValues = new ArrayList<>(values);
        updatedValues.add("value3");
        assertEquals(updatedValues, option.getValuesList());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionValidation() {
        new Option(null, "opt", false, "Option with no argument");
    }

    @Test
    public void testOptionProcessing() throws ParseException {
        Option option = OptionBuilder.hasOption("o")
                .withDescription("Option with no argument")
                .create();

        option.parseArgument("test", null);
        assertFalse(option.hasValues());

        List<String> values = new ArrayList<>();
        values.add("value1");
        values.add("value2");
        option.parseArgument("test", values.toArray(new String[values.size()]));
        assertEquals(values, option.getValuesList());

        option.clearValues();
        assertFalse(option.hasValues());
    }

    @Test
    public void testOptionEqualsAndHashCode() {
        Option option1 = new Option("o", "opt", false, "Option with no argument");
        Option option2 = new Option("o", "opt", false, "Option with no argument");
        assertTrue(option1.equals(option2));
        assertEquals(option1.hashCode(), option2.hashCode());

        option1 = new Option("o", "opt", false, "Option with no argument");
        Option option3 = new Option("O", "opt", false, "Option with no argument");
        assertFalse(option1.equals(option3));
        assertFalse(option1.hashCode() == option3.hashCode());

        option1 = new Option("o", "opt", false, "Option with no argument");
        Option option4 = new Option("o", null, false, "Option with no argument");
        assertFalse(option1.equals(option4));
        assertFalse(option1.hashCode() == option4.hashCode());

        option1 = new Option("o", "opt", false, "Option with no argument");
        Option option5 = new Option("o", "opt", true, "Option with required argument");
        assertFalse(option1.equals(option5));
        assertFalse(option1.hashCode() == option5.hashCode());
    }

    @Test
    public void testOptionToString() {
        Option option = new Option("o", "opt", false, "Option with no argument");
        String expectedToString = "[ option: o  :: Option with no argument ]";
        assertEquals(expectedToString, option.toString());

        option = new Option("o", null, false, "Option with no argument");
        expectedToString = "[ option: null :: Option with no argument ]";
        assertEquals(expectedToString, option.toString());

        option = new Option("o", "opt", false, "Option with long option");
        expectedToString = "[ option: o  :: Option with long option ]";
        assertEquals(expectedToString, option.toString());

        option = new Option("o", "opt", false, "Option with no argument", "defaultValue");
        expectedToString = "[ option: o  :: Option with no argument :: defaultValue ]";
        assertEquals(expectedToString, option.toString());

        option = new Option("o", null, false, "Option with no argument", "defaultValue");
        expectedToString = "[ option: null :: Option with no argument :: defaultValue ]";
        assertEquals(expectedToString, option.toString());

        option = new Option("o", "opt", false, "Option with no argument", null, "type");
        expectedToString = "[ option: o  :: Option with no argument :: type ]";
        assertEquals(expectedToString, option.toString());
    }

    @Test
    public void testOptionCloning() {
        Option originalOption = new Option("o", "opt", false, "Option with no argument");
        Option clonedOption = originalOption.clone();

        assertEquals(originalOption, clonedOption);
        assertFalse(originalOption == clonedOption);

        List<String> originalValues = new ArrayList<>();
        originalValues.add("value1");
        originalValues.add("value2");
        originalOption.setValues(originalValues);

        List<String> clonedValues = new ArrayList<>(originalValues);
        clonedOption.setValues(clonedValues.toArray(new String[clonedValues.size()]));

        assertEquals(originalValues, clonedOption.getValuesList());
    }
}