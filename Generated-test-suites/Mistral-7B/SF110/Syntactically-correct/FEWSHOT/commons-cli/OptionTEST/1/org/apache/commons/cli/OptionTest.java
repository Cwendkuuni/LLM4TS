package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class OptionTest {

    @Test
    public void testOptionConstructorWithShortOptAndNoArg() {
        Option option = new Option("a", "Option A");
        assertEquals("a", option.getOpt());
        assertEquals("Option A", option.getDescription());
        assertFalse(option.hasArg());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.isRequired());
        assertNull(option.getValue());
        assertNull(option.getValues());
        assertEquals(1, option.getId());
        assertEquals("a", option.getKey());
        assertNull(option.getLongOpt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionConstructorWithInvalidShortOpt() {
        new Option("", "Option A");
    }

    @Test
    public void testOptionConstructorWithShortOptAndOneArg() {
        Option option = new Option("b", "Option B", true, "Argument for option B");
        assertEquals("b", option.getOpt());
        assertEquals("Argument for option B", option.getDescription());
        assertTrue(option.hasArg());
        assertTrue(option.isRequired());
        assertNull(option.getValue());
        assertNull(option.getValues());
        assertEquals(2, option.getId());
        assertEquals("b", option.getKey());
        assertNull(option.getLongOpt());
    }

    @Test
    public void testOptionConstructorWithShortOptAndOptionalArg() {
        Option option = new Option("c", "Option C", true, "Argument for option C [optional]");
        assertEquals("c", option.getOpt());
        assertEquals("Argument for option C [optional]", option.getDescription());
        assertTrue(option.hasArg());
        assertFalse(option.isRequired());
        assertNull(option.getValue());
        assertNull(option.getValues());
        assertEquals(3, option.getId());
        assertEquals("c", option.getKey());
        assertNull(option.getLongOpt());
    }

    @Test
    public void testOptionConstructorWithShortOptAndLongOpt() {
        Option option = new Option("d", "Option D [long]", true, "Argument for option D [long]", 'd');
        assertEquals("d", option.getOpt());
        assertEquals("Option D [long]", option.getDescription());
        assertTrue(option.hasArg());
        assertTrue(option.isRequired());
        assertNull(option.getValue());
        assertNull(option.getValues());
        assertEquals(4, option.getId());
        assertEquals("d", option.getKey());
        assertEquals("Option D [long]", option.getLongOpt());
    }

    @Test
    public void testOptionGettersAndSetters() {
        Option option = new Option("e", "Option E", true, "Argument for option E");
        option.setDescription("Updated description for option E");
        assertEquals("Option E", option.getDescription());
        option.setDescription("Updated description for option E");
        assertEquals("Updated description for option E", option.getDescription());
        option.setOptionalArg(false);
        assertFalse(option.hasOptionalArg());
        option.setOptionalArg(true);
        assertTrue(option.hasOptionalArg());
        option.setRequired(true);
        assertTrue(option.isRequired());
        option.setRequired(false);
        assertFalse(option.isRequired());
        option.setArgName("argName");
        assertEquals("argName", option.getArgName());
        option.setArgName(null);
        assertNull(option.getArgName());
        option.setArgs(1);
        assertEquals(1, option.getArgs());
        option.setArgs(2);
        assertEquals(2, option.getArgs());
        option.setValueSeparator(';');
        assertTrue(option.hasValueSeparator());
        assertEquals(';', option.getValueSeparator());
        option.clearValues();
        assertNull(option.getValue());
        assertNull(option.getValues());
    }

    @Test
    public void testOptionEqualsAndHashCode() {
        Option option1 = new Option("f", "Option F");
        Option option2 = new Option("f", "Option F");
        Option option3 = new Option("g", "Option G");

        assertTrue(option1.equals(option1));
        assertTrue(option1.equals(option2));
        assertFalse(option1.equals(option3));

        int hashCode1 = option1.hashCode();
        int hashCode2 = option2.hashCode();
        int hashCode3 = option3.hashCode();
        assertEquals(hashCode1, option1.hashCode());
        assertEquals(hashCode1, option2.hashCode());
        assertNotEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
    }

    @Test
    public void testOptionClone() {
        Option option = new Option("h", "Option H", true, "Argument for option H");
        Option clonedOption = option.clone();
        assertEquals(option, clonedOption);
        assertNotSame(option, clonedOption);
        assertEquals(option.getOpt(), clonedOption.getOpt());
        assertEquals(option.getDescription(), clonedOption.getDescription());
        assertEquals(option.hasArg(), clonedOption.hasArg());
        assertEquals(option.isRequired(), clonedOption.isRequired());
        assertEquals(option.getValue(), clonedOption.getValue());
        assertEquals(option.getValuesList(), clonedOption.getValuesList());
    }

    @Test
    public void testOptionAddValue() {
        Option option = new Option("i", "Option I", true, "Argument for option I");
        List<String> values = new ArrayList<>();
        option.setValues(values);
        option.addValue("value1");
        assertEquals(1, option.getArgs());
        assertEquals("value1", option.getValue());
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));

        try {
            option.addValue("value2");
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public void testOptionAddValues() {
        Option option = new Option("j", "Option J", true, "Argument for option J");
        List<String> values = new ArrayList<>();
        option.setValues(values);
        option.addValues(new String[]{"value1", "value2"});
        assertEquals(2, option.getArgs());
        assertEquals(2, values.size());
        assertArrayEquals(new String[]{"value1", "value2"}, values.toArray(new String[0]));
    }
}