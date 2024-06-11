package org.apache.commons.cli;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class OptionTest {

    @Test
    public void testOptionConstructionWithOneArgument() {
        Option option = new Option("a", "option A", true);
        assertEquals("a", option.getOpt());
        assertEquals("option A", option.getDescription());
        assertTrue(option.hasArg());
        assertFalse(option.hasLongOpt());
        assertNull(option.getLongOpt());
        assertFalse(option.isRequired());
        assertEquals(1, option.getArgs());
        assertNull(option.getType());
        assertTrue(option.hasValueSeparator());
        assertEquals(' ', option.getValueSeparator());
        assertEquals(0, option.getValueList().size());
    }

    @Test
    public void testOptionConstructionWithNoArguments() {
        Option option = new Option("b", "option B");
        assertEquals("b", option.getOpt());
        assertEquals("option B", option.getDescription());
        assertFalse(option.hasArg());
        assertFalse(option.hasLongOpt());
        assertNull(option.getLongOpt());
        assertFalse(option.isRequired());
        assertEquals(0, option.getArgs());
        assertNull(option.getType());
        assertFalse(option.hasValueSeparator());
        assertEquals(0, option.getValueList().size());
    }

    @Test
    public void testOptionConstructionWithLongOption() {
        Option option = new Option("c", "long-option C", false, "long-arg");
        assertEquals("c", option.getOpt());
        assertEquals("option C", option.getDescription());
        assertFalse(option.hasArg());
        assertTrue(option.hasLongOpt());
        assertEquals("long-arg", option.getLongOpt());
        assertFalse(option.isRequired());
        assertEquals(0, option.getArgs());
        assertNull(option.getType());
        assertFalse(option.hasValueSeparator());
        assertEquals(0, option.getValueList().size());
    }

    @Test
    public void testOptionConstructionWithArgumentAndLongOption() {
        Option option = new Option("d", "long-option D", true, "long-arg");
        assertEquals("d", option.getOpt());
        assertEquals("option D", option.getDescription());
        assertTrue(option.hasArg());
        assertTrue(option.hasLongOpt());
        assertEquals("long-arg", option.getLongOpt());
        assertFalse(option.isRequired());
        assertEquals(1, option.getArgs());
        assertNull(option.getType());
        assertTrue(option.hasValueSeparator());
        assertEquals(' ', option.getValueSeparator());
        assertEquals(0, option.getValueList().size());
    }

    @Test
    public void testOptionId() {
        Option option1 = new Option("e", "option E", false);
        Option option2 = new Option('e', "option F", false);
        assertEquals(69, option1.getId());
        assertEquals(69, option2.getId());

        Option option3 = new Option("g", "option G", false);
        Option option4 = new Option('g', "option H", false);
        assertEquals(103, option3.getId());
        assertEquals(103, option4.getId());
    }

    @Test
    public void testOptionEquals() {
        Option option1 = new Option("h", "option H", false);
        Option option2 = new Option("h", "option H", false);
        Option option3 = new Option("i", "option I", false);

        assertTrue(option1.equals(option2));
        assertFalse(option1.equals(option3));
    }

    @Test
    public void testOptionHashCode() {
        Option option1 = new Option("i", "option I", false);
        Option option2 = new Option("i", "option I", false);
        assertEquals(65536 + 73, option1.hashCode());
        assertEquals(65536 + 73, option2.hashCode());

        Option option3 = new Option("j", "option J", false);
        assertEquals(65536 + 74, option3.hashCode());
    }

    @Test
    public void testOptionToString() {
        Option option = new Option("k", "option K", true);
        String expected = "[ option: k :: option K :: [ARG] ]";
        assertEquals(expected, option.toString());
    }

    @Test
    public void testOptionAddValue() {
        Option option = new Option("l", "option L", true);
        option.addValue("value1");
        option.addValue("value2");
        List<String> expected = new ArrayList<>(Arrays.asList("value1", "value2"));
        assertEquals(expected, option.getValueList());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testOptionAddValueUnsupported() {
        Option option = new Option("m", "option M", true);
        option.addValue("value1");
        option.addValue("value2");
        option.addValue("value3");
    }

    @Test
    public void testOptionClearValues() {
        Option option = new Option("n", "option N", true);
        option.addValue("value1");
        option.addValue("value2");
        List<String> expected = new ArrayList<>();
        option.clearValues();
        assertEquals(expected, option.getValueList());
    }
}