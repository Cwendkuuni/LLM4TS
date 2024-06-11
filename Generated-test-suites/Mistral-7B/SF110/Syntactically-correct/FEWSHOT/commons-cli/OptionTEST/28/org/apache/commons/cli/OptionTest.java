package org.apache.commons.cli;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class OptionTest {

    @Test
    public void testOptionConstruction() {
        Option option = new Option("opt", "Option description");
        assertEquals("opt", option.getOpt());
        assertEquals("Option description", option.getDescription());
        assertNull(option.getLongOpt());
        assertFalse(option.hasLongOpt());
        assertFalse(option.hasArg());
        assertNull(option.getType());
        assertEquals(-1, option.getArgs());
        assertTrue(option.hasOptionalArg());
        assertEquals("arg", option.getArgName());
        assertFalse(option.hasArgName());
        assertFalse(option.hasArgs());
        assertFalse(option.isRequired());
        assertEquals(new ArrayList<>(), option.getValuesList());
        assertNull(option.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionConstructionWithInvalidOpt() {
        new Option("", "Option description");
    }

    @Test
    public void testOptionConstructionWithShortOptAndDescription() {
        Option option = new Option('o', "Option description");
        assertEquals('o', option.getOpt());
        assertEquals("Option description", option.getDescription());
        assertNull(option.getLongOpt());
        assertFalse(option.hasLongOpt());
        assertFalse(option.hasArg());
        assertNull(option.getType());
        assertEquals(-1, option.getArgs());
        assertTrue(option.hasOptionalArg());
        assertEquals("arg", option.getArgName());
        assertFalse(option.hasArgName());
        assertFalse(option.hasArgs());
        assertFalse(option.isRequired());
        assertEquals(new ArrayList<>(), option.getValuesList());
        assertNull(option.getValue());
    }

    @Test
    public void testOptionConstructionWithShortOptLongOptAndDescription() {
        Option option = new Option('o', "long-opt", "Option description");
        assertEquals('o', option.getOpt());
        assertEquals("long-opt", option.getLongOpt());
        assertEquals("Option description", option.getDescription());
        assertFalse(option.hasLongOpt());
        assertFalse(option.hasArg());
        assertNull(option.getType());
        assertEquals(-1, option.getArgs());
        assertTrue(option.hasOptionalArg());
        assertEquals("arg", option.getArgName());
        assertFalse(option.hasArgName());
        assertFalse(option.hasArgs());
        assertFalse(option.isRequired());
        assertEquals(new ArrayList<>(), option.getValuesList());
        assertNull(option.getValue());
    }

    @Test
    public void testOptionConstructionWithShortOptLongOptArgAndDescription() {
        Option option = new Option('o', "long-opt", false, "Option description");
        assertEquals('o', option.getOpt());
        assertEquals("long-opt", option.getLongOpt());
        assertEquals("Option description", option.getDescription());
        assertFalse(option.hasLongOpt());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.hasArg());
        assertNull(option.getType());
        assertEquals(0, option.getArgs());
        assertFalse(option.hasArgName());
        assertFalse(option.hasArgs());
        assertFalse(option.isRequired());
        assertEquals(new ArrayList<>(), option.getValuesList());
        assertNull(option.getValue());
    }

    @Test
    public void testOptionConstructionWithShortOptLongOptArgValuesAndDescription() {
        Option option = new Option('o', "long-opt", false, "Option description", new String[]{"value1", "value2"});
        assertEquals('o', option.getOpt());
        assertEquals("long-opt", option.getLongOpt());
        assertEquals("Option description", option.getDescription());
        assertFalse(option.hasLongOpt());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.hasArg());
        assertNull(option.getType());
        assertEquals(2, option.getArgs());
        assertEquals("arg", option.getArgName());
        assertFalse(option.hasArgName());
        assertFalse(option.hasArgs());
        assertFalse(option.isRequired());
        List valuesList = option.getValuesList();
        assertEquals(2, valuesList.size());
        assertEquals("value1", valuesList.get(0));
        assertEquals("value2", valuesList.get(1));
        assertEquals("value1 value2", option.toString());
    }

    @Test
    public void testOptionConstructionWithShortOptLongOptArgValuesAndDescriptionType() {
        Option option = new Option('o', "long-opt", false, "Option description", new String[]{"value1", "value2"}, Integer.class);
        assertEquals('o', option.getOpt());
        assertEquals("long-opt", option.getLongOpt());
        assertEquals("Option description", option.getDescription());
        assertFalse(option.hasLongOpt());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.hasArg());
        assertEquals(Integer.class, option.getType());
        assertEquals(2, option.getArgs());
        assertEquals("arg", option.getArgName());
        assertFalse(option.hasArgName());
        assertFalse(option.hasArgs());
        assertFalse(option.isRequired());
        List valuesList = option.getValuesList();
        assertEquals(2, valuesList.size());
        assertEquals("value1", valuesList.get(0));
        assertEquals("value2", valuesList.get(1));
        assertEquals("Option [ option: o :: Option description :: integer :: [arg] ]", option.toString());
    }

    @Test
    public void testOptionGettersAndSetters() {
        Option option = new Option('o', "long-opt", false, "Option description");
        assertEquals('o', option.getOpt());
        assertEquals("long-opt", option.getLongOpt());
        assertEquals("Option description", option.getDescription());
        assertFalse(option.hasLongOpt());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.hasArg());
        assertNull(option.getType());
        assertEquals(-1, option.getArgs());
        assertEquals("arg", option.getArgName());
        assertFalse(option.hasArgName());
        assertFalse(option.hasArgs());
        assertFalse(option.isRequired());
        assertEquals(new ArrayList<>(), option.getValuesList());
        assertNull(option.getValue());

        option.setOpt("newOpt");
        assertEquals("newOpt", option.getOpt());

        option.setLongOpt("newLongOpt");
        assertEquals("newLongOpt", option.getLongOpt());

        option.setDescription("newDescription");
        assertEquals("newDescription", option.getDescription());

        option.setOptionalArg(true);
        assertTrue(option.hasOptionalArg());

        option.setArgName("newArgName");
        assertEquals("newArgName", option.getArgName());

        option.setArgs(3);
        assertEquals(3, option.getArgs());

        option.setType(Integer.class);
        assertEquals(Integer.class, option.getType());

        option.setRequired(true);
        assertTrue(option.isRequired());

        List valuesList = new ArrayList<>();
        valuesList.add("value1");
        option.setValues(valuesList);
        assertEquals(valuesList, option.getValuesList());
        assertEquals("value1", option.getValue(0));

        option.clearValues();
        assertEquals(new ArrayList<>(), option.getValuesList());
        assertNull(option.getValue());
    }

    @Test
    public void testOptionEqualsAndHashCode() {
        Option option1 = new Option('o', "long-opt", false, "Option description");
        Option option2 = new Option('o', "long-opt", false, "Option description");
        assertTrue(option1.equals(option2));
        assertEquals(option1.hashCode(), option2.hashCode());

        Option option3 = new Option('o', "long-opt2", false, "Option description");
        assertFalse(option1.equals(option3));
        assertFalse(option1.hashCode() == option3.hashCode());

        Option option4 = new Option('o', "long-opt", false, "Option description2");
        assertFalse(option1.equals(option4));
        assertFalse(option1.hashCode() == option4.hashCode());

        Option option5 = new Option('o', "long-opt", false, "Option description");
        option5.setLongOpt("long-opt2");
        assertFalse(option1.equals(option5));
        assertFalse(option1.hashCode() == option5.hashCode());

        Option option6 = new Option('o', "long-opt", false, "Option description");
        option6.setDescription("Option description2");
        assertFalse(option1.equals(option6));
        assertFalse(option1.hashCode() == option6.hashCode());

        Option option7 = new Option('o', "long-opt", false, "Option description");
        option7.setArgName("newArgName");
        assertFalse(option1.equals(option7));
        assertFalse(option1.hashCode() == option7.hashCode());

        Option option8 = new Option('o', "long-opt", false, "Option description");
        option8.setType(Integer.class);
        assertFalse(option1.equals(option8));
        assertFalse(option1.hashCode() == option8.hashCode());

        Option option9 = new Option('o', "long-opt", false, "Option description");
        option9.setValues(new ArrayList<>(Arrays.asList("value1", "value2")));
        assertFalse(option1.equals(option9));
        assertFalse(option1.hashCode() == option9.hashCode());
    }

    @Test
    public void testOptionToString() {
        Option option = new Option('o', "long-opt", false, "Option description");
        assertEquals("[ option: o :: Option description :: :: [arg] ]", option.toString());

        option.setArgName("argName");
        assertEquals("[ option: o :: Option description :: :: argName ]", option.toString());

        option.setType(Integer.class);
        assertEquals("[ option: o :: Option description :: integer :: argName ]", option.toString());

        List valuesList = new ArrayList<>();
        valuesList.add("value1");
        option.setValues(valuesList);
        assertEquals("[ option: o :: Option description :: integer :: argName :: value1 ]", option.toString());

        option.setDescription("newDescription");
        assertEquals("[ option: o :: newDescription :: integer :: argName :: value1 ]", option.toString());

        option.setOptionalArg(false);
        assertEquals("[ option: o :: newDescription :: integer :: argName :: value1 ]", option.toString());

        option.setArgs(3);
        assertEquals("[ option: o :: newDescription :: integer :: argName :: value1 ]", option.toString());

        option.setRequired(true);
        assertEquals("[ option: o :: newDescription :: integer :: argName :: value1 ]", option.toString());
    }

    @Test
    public void testOptionClone() throws CloneNotSupportedException {
        Option option = new Option('o', "long-opt", false, "Option description");
        Option clonedOption = option.clone();
        assertEquals(option, clonedOption);
        assertEquals(option.hashCode(), clonedOption.hashCode());

        List valuesList = new ArrayList<>();
        valuesList.add("value1");
        option.setValues(valuesList);
        List clonedValuesList = ((Option)clonedOption).getValuesList();
        assertEquals(valuesList, clonedValuesList);
        assertEquals(valuesList.get(0), clonedValuesList.get(0));
    }
}