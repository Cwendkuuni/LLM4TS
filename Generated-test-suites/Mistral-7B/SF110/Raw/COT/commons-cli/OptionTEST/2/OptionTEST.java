package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;

public class OptionTest {

    @Test
    public void testOptionConstructorWithSingleArgument() throws IllegalArgumentException {
        Option option = new Option("option1", "Option 1", true);
        assertEquals("option1", option.getOpt());
        assertEquals("Option 1", option.getDescription());
        assertTrue(option.hasArg());
    }

    @Test
    public void testOptionConstructorWithNoArgument() throws IllegalArgumentException {
        Option option = new Option("option2", "Option 2");
        assertEquals("option2", option.getOpt());
        assertEquals("Option 2", option.getDescription());
        assertFalse(option.hasArg());
    }

    @Test
    public void testOptionConstructorWithLongOption() throws IllegalArgumentException {
        Option option = new Option("option3", "long-option-3", false, "lo3");
        assertEquals("option3", option.getOpt());
        assertEquals("long-option-3", option.getLongOpt());
        assertFalse(option.hasArg());
    }

    @Test
    public void testOptionId() {
        Option option = new Option("option4", null, false, "description4");
        assertEquals('4', option.getId());
    }

    @Test
    public void testOptionGetKey() {
        Option option = new Option("option5", null, false, "description5");
        assertEquals("option5", option.getKey());

        Option optionWithLongOpt = new Option("option6", "longOption6", false, "description6");
        assertEquals("option6", optionWithLongOpt.getKey());
        assertEquals("longOption6", optionWithLongOpt.getLongOpt());
    }

    @Test
    public void testOptionGetType() {
        Option option = new Option("option7", null, false, "description7");
        assertNull(option.getType());

        Option optionWithType = new Option("option8", null, false, "description8", Integer.class);
        assertSame(Integer.class, optionWithType.getType());
    }

    @Test
    public void testOptionSetType() {
        Option option = new Option("option9", null, false, "description9");
        assertNull(option.getType());

        option.setType("String");
        assertEquals("String", option.getType().getClass().getSimpleName());
    }

    @Test
    public void testOptionHasOptionalArg() {
        Option option = new Option("option10", null, false, "description10");
        assertFalse(option.hasOptionalArg());

        Option optionWithArg = new Option("option11", null, true, "description11");
        assertTrue(optionWithArg.hasOptionalArg());
    }

    @Test
    public void testOptionHasLongOpt() {
        Option option = new Option("option12", null, false, "description12");
        assertFalse(option.hasLongOpt());

        Option optionWithLongOpt = new Option("option13", "longOption13", false, "description13");
        assertTrue(optionWithLongOpt.hasLongOpt());
    }

    @Test
    public void testOptionHasArg() {
        Option option = new Option("option14", null, false, "description14");
        assertFalse(option.hasArg());

        Option optionWithArg = new Option("option15", null, true, "description15");
        assertTrue(optionWithArg.hasArg());

        Option optionWithNoArgs = new Option("option16", null, false, "description16", 0);
        assertFalse(optionWithNoArgs.hasArg());
    }

    @Test
    public void testOptionGetDescription() {
        Option option = new Option("option17", null, false, "description17");
        assertEquals("description17", option.getDescription());
    }

    @Test
    public void testOptionSetDescription() {
        Option option = new Option("option18", null, false, "description18");
        assertEquals("description18", option.getDescription());

        option.setDescription("updatedDescription");
        assertEquals("updatedDescription", option.getDescription());
    }

    @Test
    public void testOptionIsRequired() {
        Option option = new Option("option19", null, false, "description19");
        assertFalse(option.isRequired());

        Option optionRequired = new Option("option20", null, false, "description20", true);
        assertTrue(optionRequired.isRequired());
    }

    @Test
    public void testOptionSetRequired() {
        Option option = new Option("option21", null, false, "description21");
        assertFalse(option.isRequired());

        option.setRequired(true);
        assertTrue(option.isRequired());
    }

    @Test
    public void testOptionSetArgName() {
        Option option = new Option("option22", null, false, "description22");
        assertNull(option.getArgName());

        option.setArgName("argName");
        assertEquals("argName", option.getArgName());
    }

    @Test
    public void testOptionHasArgName() {
        Option option = new Option("option23", null, false, "description23");
        assertFalse(option.hasArgName());

        Option optionWithArgName = new Option("option24", null, false, "description24", "argName");
        assertTrue(optionWithArgName.hasArgName());
    }

    @Test
    public void testOptionHasArgs() {
        Option option = new Option("option25", null, false, "description25");
        assertFalse(option.hasArgs());

        Option optionWithTwoArgs = new Option("option26", null, true, "description26", 2);
        assertTrue(optionWithTwoArgs.hasArgs());
    }

    @Test
    public void testOptionSetArgs() {
        Option option = new Option("option27", null, false, "description27");
        assertFalse(option.hasArgs());

        option.setArgs(2);
        assertTrue(option.hasArgs());
    }

    @Test
    public void testOptionSetValueSeparator() {
        Option option = new Option("option28", null, false, "description28");
        assertFalse(option.hasValueSeparator());

        option.setValueSeparator(';');
        assertTrue(option.hasValueSeparator());
    }

    @Test
    public void testOptionGetValueSeparator() {
        Option option = new Option("option29", null, false, "description29");
        assertFalse(option.hasValueSeparator());

        option.setValueSeparator(':');
        assertEquals(':', option.getValueSeparator());
    }

    @Test
    public void testOptionGetArgs() {
        Option option = new Option("option30", null, false, "description30");
        assertEquals(0, option.getArgs());

        Option optionWithTwoArgs = new Option("option31", null, true, "description31", 2);
        assertEquals(2, optionWithTwoArgs.getArgs());
    }

    @Test
    public void testOptionAddValueForProcessingSingleValue() {
        Option option = new Option("option32", null, false, "description32");
        List<String> values = new ArrayList<>();
        option.clearValues();

        option.addValueForProcessing("value1");
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));
    }

    @Test
    public void testOptionAddValueForProcessingMultipleValues() {
        Option option = new Option("option33", null, false, "description33");
        List<String> values = new ArrayList<>();
        option.clearValues();

        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        option.addValueForProcessing("value3");

        assertEquals(3, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
        assertEquals("value3", values.get(2));
    }

    @Test(expected = RuntimeException.class)
    public void testOptionAddValueForProcessingMoreValuesThanAllowed() {
        Option option = new Option("option34", null, false, "description34", 1);
        List<String> values = new ArrayList<>();
        option.clearValues();

        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        option.addValueForProcessing("value3");
    }

    @Test
    public void testOptionGetValue() {
        Option option = new Option("option35", null, false, "description35");
        List<String> values = new ArrayList<>();
        option.clearValues();

        option.addValueForProcessing("value1");

        assertEquals("value1", option.getValue());
    }

    @Test
    public void testOptionGetValueEmptyList() {
        Option option = new Option("option36", null, false, "description36");
        List<String> values = new ArrayList<>();
        option.clearValues();

        assertEquals(null, option.getValue());
    }

    @Test
    public void testOptionGetValueNullValue() {
        Option option = new Option("option37", null, false, "description37");
        List<String> values = new ArrayList<>();
        option.clearValues();

        option.addValueForProcessing(null);

        assertEquals(null, option.getValue());
    }

    @Test
    public void testOptionGetValues() {
        Option option = new Option("option38", null, false, "description38");
        List<String> values = new ArrayList<>();
        option.clearValues();

        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");

        assertEquals(Arrays.asList("value1", "value2"), option.getValuesList());
    }

    @Test
    public void testOptionGetValuesEmptyList() {
        Option option = new Option("option39", null, false, "description39");
        List<String> values = new ArrayList<>();
        option.clearValues();

        assertEquals(Arrays.asList(), option.getValuesList());
    }

    @Test
    public void testOptionToString() {
        Option option = new Option("option40", null, false, "description40");

        assertEquals("[ option: option40 :: Option 40 ]", option.toString());
    }

    @Test
    public void testOptionEquals() {
        Option option1 = new Option("option41", null, false, "description41");
        Option option2 = new Option("option42", null, false, "description42");

        Option option3 = new Option("option41", null, false, "description41");
        Option option4 = new Option("option43", null, false, "description43");

        assertTrue(option1.equals(option3));
        assertFalse(option1.equals(option2));
        assertFalse(option1.equals(option4));
    }

    @Test
    public void testOptionHashCode() {
        Option option1 = new Option("option44", null, false, "description44");
        Option option2 = new Option("option45", null, false, "description45");

        assertEquals(option1.hashCode(), option1.hashCode());
        assertNotEquals(option1.hashCode(), option2.hashCode());
    }

    @Test
    public void testOptionClone() {
        Option option = new Option("option46", null, false, "description46");

        Option clonedOption = option.clone();

        assertEquals(option, clonedOption);
    }

    @Test
    public void testOptionClearValues() {
        Option option = new Option("option47", null, false, "description47");
        List<String> values = new ArrayList<>();
        option.clearValues();

        assertTrue(values.isEmpty());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testOptionAddValue() {
        Option option = new Option("option48", null, false, "description48");

        option.addValue("value1");
    }
}