package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
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
    public void testOptionConstruction() {
        Option option = new Option("a", "Option A", true);
        assertEquals("a", option.getOpt());
        assertNull(option.getLongOpt());
        assertTrue(option.hasArg());
        assertEquals("Option A", option.getDescription());
        assertFalse(option.hasLongOpt());
        assertFalse(option.isRequired());
        assertFalse(option.hasOptionalArg());
        assertNull(option.getType());
        assertNull(option.getArgName());
        assertFalse(option.hasArgs());
        assertEquals(1, option.getArgs());
        assertEquals('', option.getValueSeparator());
        assertFalse(option.hasValueSeparator());
        assertTrue(option.hasNoValues());
        assertFalse(option.hasValues());
        assertFalse(option.equals(null));
        assertEquals(option, option);
        assertFalse(option.equals(""));
        assertFalse(option.equals("foo"));
    }

    @Test
    public void testOptionConstructionWithLongOpt() {
        Option option = new Option("a", "long-option-a", true, "Long option A");
        assertEquals("a", option.getOpt());
        assertEquals("long-option-a", option.getLongOpt());
        assertTrue(option.hasArg());
        assertEquals("Long option A", option.getDescription());
        assertTrue(option.hasLongOpt());
        assertFalse(option.isRequired());
        assertFalse(option.hasOptionalArg());
        assertNull(option.getType());
        assertNull(option.getArgName());
        assertFalse(option.hasArgs());
        assertEquals(1, option.getArgs());
        assertEquals('', option.getValueSeparator());
        assertFalse(option.hasValueSeparator());
        assertTrue(option.hasNoValues());
        assertFalse(option.hasValues());
        assertTrue(option.equals(new Option("a", "long-option-a", true, "Long option A")));
    }

    @Test
    public void testOptionConstructionWithValueType() {
        Option option = new Option("a", "Option A", true, "Option A", Integer.TYPE);
        assertEquals("a", option.getOpt());
        assertNull(option.getLongOpt());
        assertTrue(option.hasArg());
        assertEquals("Option A", option.getDescription());
        assertFalse(option.hasLongOpt());
        assertFalse(option.isRequired());
        assertFalse(option.hasOptionalArg());
        assertSame(Integer.TYPE, option.getType());
        assertNull(option.getArgName());
        assertFalse(option.hasArgs());
        assertEquals(1, option.getArgs());
        assertEquals('', option.getValueSeparator());
        assertFalse(option.hasValueSeparator());
        assertTrue(option.hasNoValues());
        assertFalse(option.hasValues());
    }

    @Test
    public void testOptionConstructionWithArgName() {
        Option option = new Option("a", null, true, "Option A", null, "argName");
        assertEquals("a", option.getOpt());
        assertNull(option.getLongOpt());
        assertTrue(option.hasArg());
        assertEquals("Option A", option.getDescription());
        assertFalse(option.hasLongOpt());
        assertFalse(option.isRequired());
        assertTrue(option.hasOptionalArg());
        assertNull(option.getType());
        assertEquals("argName", option.getArgName());
        assertFalse(option.hasArgs());
        assertEquals(1, option.getArgs());
        assertEquals('', option.getValueSeparator());
        assertFalse(option.hasValueSeparator());
        assertTrue(option.hasNoValues());
        assertFalse(option.hasValues());
    }

    @Test
    public void testOptionConstructionWithArgs() {
        Option option = new Option("a", null, true, "Option A", null, null, 2);
        assertEquals("a", option.getOpt());
        assertNull(option.getLongOpt());
        assertTrue(option.hasArg());
        assertEquals("Option A", option.getDescription());
        assertFalse(option.hasLongOpt());
        assertFalse(option.isRequired());
        assertFalse(option.hasOptionalArg());
        assertNull(option.getType());
        assertNull(option.getArgName());
        assertTrue(option.hasArgs());
        assertEquals(2, option.getArgs());
        assertEquals('', option.getValueSeparator());
        assertFalse(option.hasValueSeparator());
        assertTrue(option.hasNoValues());
        assertFalse(option.hasValues());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionConstructionWithInvalidArgNumber() {
        new Option("a", null, true, "Option A", null, null, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionConstructionWithNegativeArgNumber() {
        new Option("a", null, true, "Option A", null, null, -1);
    }

    @Test
    public void testOptionGetId() {
        Option option = new Option("a", null, true, "Option A", null, null);
        assertEquals('a', option.getId());
    }

    @Test
    public void testOptionToString() {
        Option option = new Option("a", null, true, "Option A", null, null);
        String expected = "[ option: a :: Option A ]";
        assertEquals(expected, option.toString());
    }

    @Test
    public void testOptionGetKey() {
        Option option = new Option("a", null, true, "Option A", null, null);
        assertEquals("a", option.getKey());
    }

    @Test
    public void testOptionGetOpt() {
        Option option = new Option("a", null, true, "Option A", null, null);
        assertEquals("a", option.getOpt());
    }

    @Test
    public void testOptionGetLongOpt() {
        Option option = new Option("a", "long-option-a", true, "Option A", null, null);
        assertEquals("long-option-a", option.getLongOpt());
    }

    @Test
    public void testOptionGetDescription() {
        Option option = new Option("a", null, true, "Option A", null, null);
        assertEquals("Option A", option.getDescription());
    }

    @Test
    public void testOptionIsRequired() {
        Option option = new Option("a", null, true, "Option A", null, null);
        assertFalse(option.isRequired());
    }

    @Test
    public void testOptionSetRequiredTrue() {
        Option option = new Option("a", null, true, "Option A", null, null);
        option.setRequired(true);
        assertTrue(option.isRequired());
    }

    @Test
    public void testOptionSetRequiredFalse() {
        Option option = new Option("a", null, true, "Option A", null, null);
        option.setRequired(false);
        assertFalse(option.isRequired());
    }

    @Test
    public void testOptionHasOptionalArg() {
        Option option = new Option("a", null, true, "Option A", null, null);
        assertFalse(option.hasOptionalArg());
    }

    @Test
    public void testOptionSetOptionalArgTrue() {
        Option option = new Option("a", null, true, "Option A", null, null);
        option.setOptionalArg(true);
        assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testOptionSetOptionalArgFalse() {
        Option option = new Option("a", null, true, "Option A", null, null);
        option.setOptionalArg(false);
        assertFalse(option.hasOptionalArg());
    }

    @Test
    public void testOptionHasArg() {
        Option option = new Option("a", null, true, "Option A", null, null);
        assertTrue(option.hasArg());
    }

    @Test
    public void testOptionHasArgs() {
        Option option = new Option("a", null, true, "Option A", null, null, 2);
        assertTrue(option.hasArgs());
    }

    @Test
    public void testOptionGetType() {
        Option option = new Option("a", null, true, "Option A", Integer.TYPE, null);
        assertEquals(Integer.TYPE, option.getType());
    }

    @Test
    public void testOptionSetType() {
        Option option = new Option("a", null, true, "Option A", null, null);
        option.setType(Boolean.TYPE);
        assertEquals(Boolean.TYPE, option.getType());
    }

    @Test
    public void testOptionGetValues() {
        Option option = new Option("a", null, true, "Option A", null, null);
        assertArrayEquals(new String[0], option.getValues());
    }

    @Test
    public void testOptionHasNoValues() {
        Option option = new Option("a", null, true, "Option A", null, null);
        assertTrue(option.hasNoValues());
    }

    @Test
    public void testOptionHasValues() {
        Option option = new Option("a", null, true, "Option A", null, null);
        assertFalse(option.hasValues());
    }

    @Test
    public void testOptionAddValue() {
        Option option = new Option("a", null, true, "Option A", null, null);
        option.addValue("value1");
        option.addValue("value2");
        String[] expected = new String[]{"value1", "value2"};
        assertArrayEquals(expected, option.getValues());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testOptionAddValueUnsupported() {
        Option option = new Option("a", null, true, "Option A", null, null);
        option.addValue("value1");
    }

    @Test
    public void testOptionClearValues() {
        Option option = new Option("a", null, true, "Option A", null, null);
        option.addValue("value1");
        option.addValue("value2");
        option.clearValues();
        assertArrayEquals(new String[0], option.getValues());
    }

    @Test
    public void testOptionGetValue() {
        Option option = new Option("a", null, true, "Option A", null, null);
        option.addValue("value1");
        assertEquals("value1", option.getValue());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testOptionGetValueOutOfBounds() {
        Option option = new Option("a", null, true, "Option A", null, null);
        option.addValue("value1");
        throw new IndexOutOfBoundsException("Index: 1, Size: 1");
    }

    @Test
    public void testOptionGetValueNull() {
        Option option = new Option("a", null, true, "Option A", null, null);
        assertEquals(null, option.getValue());
    }

    @Test
    public void testOptionGetValueSeparator() {
        Option option = new Option("a", null, true, "Option A", null, null);
        assertEquals('', option.getValueSeparator());
    }

    @Test
    public void testOptionSetValueSeparator() {
        Option option = new Option("a", null, true, "Option A", null, null);
        option.setValueSeparator('x');
        assertEquals('x', option.getValueSeparator());
    }

    @Test
    public void testOptionEquals() {
        Option option1 = new Option("a", null, true, "Option A", null, null);
        Option option2 = new Option("a", null, true, "Option A", null, null);
        Option option3 = new Option("b", null, true, "Option B", null, null);
        assertTrue(option1.equals(option1));
        assertTrue(option1.equals(option2));
        assertFalse(option1.equals(option3));
        assertFalse(option1.equals(""));
        assertFalse(option1.equals(null));
    }

    @Test
    public void testOptionHashCode() {
        Option option1 = new Option("a", null, true, "Option A", null, null);
        Option option2 = new Option("a", null, true, "Option A", null, null);
        Option option3 = new Option("b", null, true, "Option B", null, null);
        int hash1 = option1.hashCode();
        int hash2 = option2.hashCode();
        int hash3 = option3.hashCode();
        assertEquals(hash1, option1.hashCode());
        assertEquals(hash1, option2.hashCode());
        assertNotEquals(hash1, hash2);
        assertNotEquals(hash1, hash3);
    }

    @Test
    public void testOptionClone() {
        Option option = new Option("a", null, true, "Option A", null, null);
        Option cloned = option.clone();
        assertEquals(option, cloned);
        assertNotSame(option.values, cloned.values);
    }

    @Test
    public void testOptionClearValues() {
        Option option = new Option("a", null, true, "Option A", null, null);
        option.addValue("value1");
        option.clearValues();
        assertFalse(option.hasValues());
    }

}