package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class OptionTest {

    @Test
    public void testOptionWithNoArg() {
        Option option = new Option("-t", "Test Option", false);
        assertEquals("-t", option.getOpt());
        assertEquals("Test Option", option.getDescription());
        assertFalse(option.hasArg());
        assertFalse(option.hasArgs());
        assertFalse(option.isRequired());
        assertNull(option.getValue());
        assertNull(option.getValues());
        assertFalse(option.hasValueSeparator());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
    }

    @Test
    public void testOptionWithRequiredArg() {
        Option option = new Option("-i", "Input File", true);
        assertEquals("-i", option.getOpt());
        assertEquals("Input File", option.getDescription());
        assertTrue(option.hasArg());
        assertFalse(option.hasArgs());
        assertTrue(option.isRequired());
        assertNull(option.getValue());
        assertNull(option.getValues());
        assertFalse(option.hasValueSeparator());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
    }

    @Test
    public void testOptionWithOptionalArg() {
        Option option = new Option("-p", "Path", false, "default");
        assertEquals("-p", option.getOpt());
        assertEquals("Path", option.getDescription());
        assertFalse(option.isRequired());
        assertEquals("default", option.getArgName());
        assertFalse(option.hasArgs());
        assertNull(option.getValue());
        assertNull(option.getValues());
        assertFalse(option.hasValueSeparator());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
    }

    @Test
    public void testOptionWithLongOpt() {
        Option option = new Option("--input-file", "Input File", false);
        assertEquals("--input-file", option.getLongOpt());
        assertEquals("Input File", option.getDescription());
        assertFalse(option.hasOpt());
        assertFalse(option.hasArgs());
        assertFalse(option.isRequired());
        assertNull(option.getValue());
        assertNull(option.getValues());
        assertFalse(option.hasValueSeparator());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
    }

    @Test
    public void testOptionWithLongOptAndShortOpt() {
        Option option = new Option("--input-file", "--if", "Input File", false);
        assertEquals("--input-file", option.getLongOpt());
        assertEquals("--if", option.getOpt());
        assertEquals("Input File", option.getDescription());
        assertFalse(option.hasOpt());
        assertFalse(option.hasArgs());
        assertFalse(option.isRequired());
        assertNull(option.getValue());
        assertNull(option.getValues());
        assertFalse(option.hasValueSeparator());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
    }

    @Test
    public void testOptionWithOptionalArgAndValues() {
        Option option = new Option("-c", "Character, comma-separated", false, null);
        assertEquals("-c", option.getOpt());
        assertEquals("Character, comma-separated", option.getDescription());
        assertFalse(option.isRequired());
        assertNull(option.getArgName());
        assertFalse(option.hasArgs());
        assertFalse(option.hasValueSeparator());
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
    }

    @Test
    public void testOptionWithOptionalArgAndValues_1() {
        Option option = new Option("-c", "Character, comma-separated", false, "-");
        assertEquals("-c", option.getOpt());
        assertEquals("Character, comma-separated", option.getDescription());
        assertFalse(option.isRequired());
        assertEquals("-", option.getArgName());
        assertFalse(option.hasArgs());
        assertTrue(option.hasValueSeparator());
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
    }

    @Test
    public void testOptionWithOptionalArgAndValues_2() {
        Option option = new Option("-c", "Character, comma-separated", false, "-", "char");
        assertEquals("-c", option.getOpt());
        assertEquals("Character, comma-separated", option.getDescription());
        assertFalse(option.isRequired());
        assertEquals("char", option.getArgName());
        assertFalse(option.hasArgs());
        assertTrue(option.hasValueSeparator());
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
    }

    @Test
    public void testOptionWithOptionalArgAndValues_3() {
        Option option = new Option("-c", "Character, comma-separated", false, "-", "char", new String[0]);
        assertEquals("-c", option.getOpt());
        assertEquals("Character, comma-separated", option.getDescription());
        assertFalse(option.isRequired());
        assertEquals("char", option.getArgName());
        assertFalse(option.hasArgs());
        assertTrue(option.hasValueSeparator());
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
        assertSame(option.getValues(), Arrays.asList(new String[0]));
    }

    @Test
    public void testOptionWithOptionalArgAndValues_4() {
        Option option = new Option("-c", "Character, comma-separated", false, "-", "char", new String[]{"a", "b"});
        assertEquals("-c", option.getOpt());
        assertEquals("Character, comma-separated", option.getDescription());
        assertFalse(option.isRequired());
        assertEquals("char", option.getArgName());
        assertFalse(option.hasArgs());
        assertTrue(option.hasValueSeparator());
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
        assertSame(option.getValues(), Arrays.asList(new String[]{"a", "b"}));
    }

    @Test
    public void testOptionWithRequiredArgAndValues() {
        Option option = new Option("-d", "Directory", true, null);
        assertEquals("-d", option.getOpt());
        assertEquals("Directory", option.getDescription());
        assertTrue(option.isRequired());
        assertNull(option.getArgName());
        assertFalse(option.hasArgs());
        assertFalse(option.hasValueSeparator());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionWithInvalidArgName() {
        new Option("--this-option-name-is-invalid", null, false, null);
    }

    @Test
    public void testOptionCloning() {
        Option originalOption = new Option("-t", "Test Option", false);
        Option clonedOption = originalOption.clone();
        assertEquals(originalOption.getOpt(), clonedOption.getOpt());
        assertEquals(originalOption.getDescription(), clonedOption.getDescription());
        assertEquals(originalOption.hasArg(), clonedOption.hasArg());
        assertEquals(originalOption.hasArgs(), clonedOption.hasArgs());
        assertEquals(originalOption.isRequired(), clonedOption.isRequired());
        assertNull(originalOption.getValue());
        assertNull(originalOption.getValues());
        assertFalse(originalOption.hasValueSeparator());
        assertEquals(Option.UNINITIALIZED, originalOption.getArgs());
        assertNull(clonedOption.getValue());
        assertNull(clonedOption.getValues());
        assertFalse(clonedOption.hasValueSeparator());
        assertEquals(Option.UNINITIALIZED, clonedOption.getArgs());
    }

    @Test
    public void testOptionToString() {
        Option option = new Option("-i", "Input File", false);
        assertEquals("[ option: -i :: Input File ]", option.toString());
    }

    @Test
    public void testOptionEquals() {
        Option option1 = new Option("-i", "Input File", false);
        Option option2 = new Option("-i", "Input File", false);
        Option option3 = new Option("-I", "Input File", false);
        Option option4 = new Option("-i", "Input File", true);
        Option option5 = new Option("-i", "Input File", false, Arrays.asList(new String[]{"a", "b"}));

        assertTrue(option1.equals(option1));
        assertTrue(option1.equals(option2));
        assertFalse(option1.equals(option3));
        assertFalse(option1.equals(option4));
        assertFalse(option1.equals(option5));
    }

    @Test
    public void testOptionHashCode() {
        Option option1 = new Option("-i", "Input File", false);
        Option option2 = new Option("-i", "Input File", false);
        Option option3 = new Option("-I", "Input File", false);
        Option option4 = new Option("-i", "Input File", true);
        Option option5 = new Option("-i", "Input File", false, Arrays.asList(new String[]{"a", "b"}));

        int hashCode1 = option1.hashCode();
        int hashCode2 = option2.hashCode();
        int hashCode3 = option3.hashCode();
        int hashCode4 = option4.hashCode();
        int hashCode5 = option5.hashCode();

        assertTrue(hashCode1 == hashCode2);
        assertFalse(hashCode1 == hashCode3);
        assertFalse(hashCode1 == hashCode4);
        assertFalse(hashCode1 == hashCode5);
    }
}