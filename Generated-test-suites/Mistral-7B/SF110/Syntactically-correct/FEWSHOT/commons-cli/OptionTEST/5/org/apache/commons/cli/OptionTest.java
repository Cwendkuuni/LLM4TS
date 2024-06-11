package org.apache.commons.cli;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class OptionTest {

    @Test
    public void testOptionConstruction() {
        Option opt = new Option("opt", "Option description");
        assertEquals("opt", opt.getOpt());
        assertEquals("Option description", opt.getDescription());
        assertNull(opt.getLongOpt());
        assertFalse(opt.hasArg());
        assertFalse(opt.isRequired());
        assertFalse(opt.hasOptionalArg());
        assertNull(opt.getType());
        assertNull(opt.getArgName());
        assertFalse(opt.hasArgs());
        assertFalse(opt.hasValueSeparator());
        assertFalse(opt.hasLongOpt());
        assertTrue(opt.hasArgName());
        assertFalse(opt.hasValues());
        assertFalse(opt.equals("foo"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionConstructionWithInvalidOpt() {
        new Option("", "Option description");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionConstructionWithEmptyDescription() {
        new Option("opt", "");
    }

    @Test
    public void testOptionConstructionWithHasArg() {
        Option opt = new Option("opt", true, "Option description");
        assertEquals("opt", opt.getOpt());
        assertEquals("Option description", opt.getDescription());
        assertEquals("arg", opt.getArgName());
        assertTrue(opt.hasArg());
        assertFalse(opt.isRequired());
        assertFalse(opt.hasOptionalArg());
        assertNull(opt.getType());
        assertFalse(opt.hasArgs());
        assertFalse(opt.hasValueSeparator());
        assertFalse(opt.hasLongOpt());
        assertTrue(opt.hasArgName());
        assertFalse(opt.hasValues());
        assertFalse(opt.equals("foo"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionConstructionWithInvalidHasArg() {
        new Option("opt", true, "");
    }

    @Test
    public void testOptionConstructionWithLongOpt() {
        Option opt = new Option("opt", null, false, "Long Option description");
        assertEquals("opt", opt.getOpt());
        assertEquals(null, opt.getLongOpt());
        assertEquals("Option description", opt.getDescription());
        assertFalse(opt.hasArg());
        assertFalse(opt.isRequired());
        assertFalse(opt.hasOptionalArg());
        assertNull(opt.getType());
        assertFalse(opt.hasArgs());
        assertFalse(opt.hasValueSeparator());
        assertTrue(opt.hasLongOpt());
        assertFalse(opt.hasArgName());
        assertFalse(opt.hasValues());
        assertFalse(opt.equals("foo"));
    }

    @Test
    public void testOptionConstructionWithHasArgAndLongOpt() {
        Option opt = new Option("opt", "--long-opt", true, "Long Option description");
        assertEquals("opt", opt.getOpt());
        assertEquals("--long-opt", opt.getLongOpt());
        assertEquals("Option description", opt.getDescription());
        assertEquals("arg", opt.getArgName());
        assertTrue(opt.hasArg());
        assertFalse(opt.isRequired());
        assertFalse(opt.hasOptionalArg());
        assertNull(opt.getType());
        assertFalse(opt.hasArgs());
        assertFalse(opt.hasValueSeparator());
        assertTrue(opt.hasLongOpt());
        assertFalse(opt.hasArgName());
        assertFalse(opt.hasValues());
        assertFalse(opt.equals("foo"));
    }

    @Test
    public void testOptionId() {
        Option opt = new Option("opt", "Option description");
        assertEquals('o', opt.getId());
    }

    @Test
    public void testOptionGetKey() {
        Option opt = new Option("opt", "Option description");
        assertEquals("opt", opt.getKey());
    }

    @Test
    public void testOptionSetOptionalArg() {
        Option opt = new Option("opt", "Option description");
        opt.setOptionalArg(true);
        assertTrue(opt.hasOptionalArg());
    }

    @Test
    public void testOptionIsRequired() {
        Option opt = new Option("opt", "Option description");
        opt.setRequired(true);
        assertTrue(opt.isRequired());
    }

    @Test
    public void testOptionSetArgName() {
        Option opt = new Option("opt", "Option description");
        opt.setArgName("argName");
        assertEquals("argName", opt.getArgName());
    }

    @Test
    public void testOptionSetArgs() {
        Option opt = new Option("opt", "Option description");
        opt.setArgs(1);
        assertEquals(1, opt.getArgs());
    }

    @Test
    public void testOptionSetValueSeparator() {
        Option opt = new Option("opt", "Option description");
        opt.setValueSeparator('s');
        assertEquals('s', opt.getValueSeparator());
        assertTrue(opt.hasValueSeparator());
    }

    @Test
    public void testOptionGetValues() {
        Option opt = new Option("opt", "Option description");
        opt.setValueSeparator('s');
        opt.addValueForProcessing("value1");
        opt.addValueForProcessing("value2");
        String[] expectedValues = {"value1", "value2"};
        assertArrayEquals(expectedValues, opt.getValues());
    }

    @Test
    public void testOptionGetValuesList() {
        Option opt = new Option("opt", "Option description");
        opt.setValueSeparator('s');
        opt.addValueForProcessing("value1");
        opt.addValueForProcessing("value2");
        List<String> expectedValues = new ArrayList<>();
        expectedValues.add("value1");
        expectedValues.add("value2");
        assertEquals(expectedValues, opt.getValuesList());
    }

    @Test(expected = RuntimeException.class)
    public void testOptionAddValueWithNoArgsAllowed() {
        Option opt = new Option("opt", "Option description");
        opt.setArgs(-1);
        opt.addValueForProcessing("value");
    }

    @Test
    public void testOptionToString() {
        Option opt = new Option("opt", "Option description");
        assertEquals("[ option: opt :: Option description ]", opt.toString());
    }

    @Test
    public void testOptionEquals() {
        Option opt1 = new Option("opt1", "Option description");
        Option opt2 = new Option("opt1", "Option description");
        Option opt3 = new Option("opt2", "Option description");
        assertTrue(opt1.equals(opt1));
        assertTrue(opt1.equals(opt2));
        assertFalse(opt1.equals(opt3));
    }

    @Test
    public void testOptionClone() {
        Option opt = new Option("opt", "Option description");
        Option clonedOpt = opt.clone();
        assertEquals(opt, clonedOpt);
    }
}