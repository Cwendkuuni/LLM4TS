package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class OptionTest {

    @Test
    public void testOptionConstruction() {
        Option option = new Option("-t", "Test Option", true);
        assertEquals("-t", option.getOpt());
        assertNull(option.getLongOpt());
        assertFalse(option.hasArg());
        assertNull(option.getDescription());
        assertTrue(option.isRequired());
        assertFalse(option.hasOptionalArg());
        assertNull(option.getType());
        assertFalse(option.hasValueSeparator());
        assertFalse(option.hasValue());
        assertFalse(option.hasArgs());
        assertEquals("Test Option", option.getDescription());

        option = new Option("--test", "Test Option", false, "test");
        assertEquals("--test", option.getOpt());
        assertEquals("test", option.getLongOpt());
        assertFalse(option.hasArg());
        assertEquals("Test Option", option.getDescription());
        assertFalse(option.isRequired());
        assertTrue(option.hasOptionalArg());
        assertNull(option.getType());
        assertFalse(option.hasValueSeparator());
        assertNull(option.getValue());
        assertFalse(option.hasArgs());

        option = new Option("--test", "Test Option", false, "test", "Test Arg");
        assertEquals("--test", option.getOpt());
        assertEquals("test", option.getLongOpt());
        assertEquals("Test Arg", option.getArgName());
        assertEquals("Test Option", option.getDescription());
        assertFalse(option.isRequired());
        assertTrue(option.hasOptionalArg());
        assertNull(option.getType());
        assertFalse(option.hasValueSeparator());
        assertNull(option.getValue());
        assertFalse(option.hasArgs());

        option = new Option("--test", "Test Option", false, "test", "Test Arg", 'a');
        assertEquals("--test", option.getOpt());
        assertEquals("test", option.getLongOpt());
        assertEquals("Test Arg", option.getArgName());
        assertEquals("Test Option", option.getDescription());
        assertFalse(option.isRequired());
        assertTrue(option.hasOptionalArg());
        assertEquals('a', option.getValueSeparator());
        assertFalse(option.hasValue());
        assertFalse(option.hasArgs());

        option = new Option("-t", "Test Option", false, "test", "Test Arg", 'a');
        assertEquals("-t", option.getOpt());
        assertNull(option.getLongOpt());
        assertEquals("Test Arg", option.getArgName());
        assertEquals("Test Option", option.getDescription());
        assertFalse(option.isRequired());
        assertTrue(option.hasOptionalArg());
        assertEquals('a', option.getValueSeparator());
        assertFalse(option.hasValue());
        assertFalse(option.hasArgs());

        option = new Option("-t:i", "Test Option with argument", false, "test", null, 'i');
        assertEquals("-t:i", option.getOpt());
        assertNull(option.getLongOpt());
        assertNull(option.getArgName());
        assertEquals("Test Option with argument", option.getDescription());
        assertFalse(option.isRequired());
        assertTrue(option.hasOptionalArg());
        assertEquals('i', option.getValueSeparator());
        assertFalse(option.hasValue());
        assertFalse(option.hasArgs());

        option = new Option("-t:s", "Test Option with argument", false, "test", "Test Arg", 's');
        assertEquals("-t:s", option.getOpt());
        assertNull(option.getLongOpt());
        assertEquals("Test Arg", option.getArgName());
        assertEquals("Test Option with argument", option.getDescription());
        assertFalse(option.isRequired());
        assertTrue(option.hasOptionalArg());
        assertEquals('s', option.getValueSeparator());
        assertNull(option.getValue());
        assertFalse(option.hasArgs());

        option = new Option("-t:s", "Test Option with argument", false, "test", "Test Arg", 's', "Test Value");
        assertEquals("-t:s", option.getOpt());
        assertNull(option.getLongOpt());
        assertEquals("Test Arg", option.getArgName());
        assertEquals("Test Option with argument", option.getDescription());
        assertFalse(option.isRequired());
        assertTrue(option.hasOptionalArg());
        assertEquals('s', option.getValueSeparator());
        assertNull(option.getValue());
        assertFalse(option.hasArgs());

        option = new Option("-t:s", "Test Option with argument", false, "test", "Test Arg", 's', new String[]{"Test Val1", "Test Val2"});
        assertEquals("-t:s", option.getOpt());
        assertNull(option.getLongOpt());
        assertEquals("Test Arg", option.getArgName());
        assertEquals("Test Option with argument", option.getDescription());
        assertFalse(option.isRequired());
        assertTrue(option.hasOptionalArg());
        assertEquals('s', option.getValueSeparator());
        assertEquals(new String[]{"Test Val1", "Test Val2"}, option.getValues());
        assertFalse(option.hasArgs());

        option = new Option("-t:s", "Test Option with argument", false, "test", "Test Arg", 's', new String[]{"Test Val1", "Test Val2"}, 2);
        assertEquals("-t:s", option.getOpt());
        assertNull(option.getLongOpt());
        assertEquals("Test Arg", option.getArgName());
        assertEquals("Test Option with argument", option.getDescription());
        assertFalse(option.isRequired());
        assertTrue(option.hasOptionalArg());
        assertEquals('s', option.getValueSeparator());
        assertEquals(new String[]{"Test Val1", "Test Val2"}, option.getValues());
        assertTrue(option.hasArgs());
    }

    @Test
    public void testOptionEquality() {
        Option option1 = new Option("-t", "Test Option", true);
        Option option2 = new Option("-t", "Test Option", true);
        Option option3 = new Option("-T", "Test Option", true);
        Option option4 = new Option("-t", "Test Option 2", true);
        
        assertEquals(option1, option1);
        assertEquals(option1, option2);
        assertNotEquals(option1, option3);
        assertNotEquals(option1, option4);
        
        option1 = new Option("--test", "Test Option", false, "test");
        option2 = new Option("--test", "Test Option", false, "test");
        option3 = new Option("--TEST", "Test Option", false, "test");
        option4 = new Option("--test", "Test Option 2", false, "test");
        
        assertEquals(option1, option1);
        assertEquals(option1, option2);
        assertNotEquals(option1, option3);
        assertNotEquals(option1, option4);
        
        option1 = new Option("--test", "Test Option", false, "test", "Test Arg");
        option2 = new Option("--test", "Test Option", false, "test", "Test Arg");
        option3 = new Option("--test", "Test Option", false, "test", "Test Arg 2");
        option4 = new Option("--test", "Test Option 2", false, "test", "Test Arg");
        
        assertEquals(option1, option1);
        assertEquals(option1, option2);
        assertNotEquals(option1, option3);
        assertNotEquals(option1, option4);
        
        option1 = new Option("--test", "Test Option", false, "test", "Test Arg", 'a');
        option2 = new Option("--test", "Test Option", false, "test", "Test Arg", 'a');
        option3 = new Option("--test", "Test Option", false, "test", "Test Arg", 'b');
        option4 = new Option("--test", "Test Option 2", false, "test", "Test Arg", 'a');
        
        assertEquals(option1, option1);
        assertEquals(option1, option2);
        assertNotEquals(option1, option3);
        assertNotEquals(option1, option4);
        
        option1 = new Option("--test", "Test Option", false, "test", "Test Arg", 'a', new String[]{"Test Val1", "Test Val2"});
        option2 = new Option("--test", "Test Option", false, "test", "Test Arg", 'a', new String[]{"Test Val1", "Test Val2"});
        option3 = new Option("--test", "Test Option", false, "test", "Test Arg", 'a', new String[]{"Test Val3", "Test Val4"});
        option4 = new Option("--test", "Test Option 2", false, "test", "Test Arg", 'a', new String[]{"Test Val1", "Test Val2"});
        
        assertEquals(option1, option1);
        assertEquals(option1, option2);
        assertNotEquals(option1, option3);
        assertNotEquals(option1, option4);
    }

    @Test
    public void testOptionHashCode() {
        Option option1 = new Option("-t", "Test Option", true);
        Option option2 = new Option("-t", "Test Option", true);
        Option option3 = new Option("-T", "Test Option", true);
        Option option4 = new Option("-t", "Test Option 2", true);
        
        assertEquals(option1.hashCode(), option2.hashCode());
        assertNotEquals(option1.hashCode(), option3.hashCode());
        assertNotEquals(option1.hashCode(), option4.hashCode());
        
        option1 = new Option("--test", "Test Option", false, "test");
        option2 = new Option("--test", "Test Option", false, "test");
        option3 = new Option("--TEST", "Test Option", false, "test");
        option4 = new Option("--test", "Test Option 2", false, "test");
        
        assertEquals(option1.hashCode(), option2.hashCode());
        assertNotEquals(option1.hashCode(), option3.hashCode());
        assertNotEquals(option1.hashCode(), option4.hashCode());
        
        option1 = new Option("--test", "Test Option", false, "test", "Test Arg");
        option2 = new Option("--test", "Test Option", false, "test", "Test Arg");
        option3 = new Option("--test", "Test Option", false, "test", "Test Arg 2");
        option4 = new Option("--test", "Test Option 2", false, "test", "Test Arg");
        
        assertEquals(option1.hashCode(), option2.hashCode());
        assertNotEquals(option1.hashCode(), option3.hashCode());
        assertNotEquals(option1.hashCode(), option4.hashCode());
        
        option1 = new Option("--test", "Test Option", false, "test", "Test Arg", 'a');
        option2 = new Option("--test", "Test Option", false, "test", "Test Arg", 'a');
        option3 = new Option("--test", "Test Option", false, "test", "Test Arg", 'b');
        option4 = new Option("--test", "Test Option 2", false, "test", "Test Arg", 'a');
        
        assertEquals(option1.hashCode(), option2.hashCode());
        assertNotEquals(option1.hashCode(), option3.hashCode());
        assertNotEquals(option1.hashCode(), option4.hashCode());
        
        option1 = new Option("--test", "Test Option", false, "test", "Test Arg", 'a', new String[]{"Test Val1", "Test Val2"});
        option2 = new Option("--test", "Test Option", false, "test", "Test Arg", 'a', new String[]{"Test Val1", "Test Val2"});
        option3 = new Option("--test", "Test Option", false, "test", "Test Arg", 'a', new String[]{"Test Val3", "Test Val4"});
        option4 = new Option("--test", "Test Option 2", false, "test", "Test Arg", 'a', new String[]{"Test Val1", "Test Val2"});
        
        assertEquals(option1.hashCode(), option2.hashCode());
        assertNotEquals(option1.hashCode(), option3.hashCode());
        assertNotEquals(option1.hashCode(), option4.hashCode());
    }

    @Test
    public void testOptionToString() {
        Option option = new Option("-t", "Test Option", false);
        String expected = "[ option: -t :: Test Option ]";
        assertEquals(expected, option.toString());
        
        option = new Option("-t", "Test Option", false, "test", "Test Arg");
        expected = "[ option: -t :: Test Option [ARG] ]";
        assertEquals(expected, option.toString());
        
        option = new Option("-t:i", "Test Option with integer argument", false, "test", null, 'i');
        expected = "[ option: -t:i :: Test Option with integer argument ]";
        assertEquals(expected, option.toString());
        
        option = new Option("-t:s", "Test Option with string argument", false, "test", "Test Arg", 's');
        expected = "[ option: -t:s :: Test Option with string argument [ARG] ]";
        assertEquals(expected, option.toString());
        
        option = new Option("-t:s", "Test Option with string argument", false, "test", "Test Arg", 's', new String[]{"Test Val1", "Test Val2"});
        expected = "[ option: -t:s :: Test Option with string argument [ARG] :: Test Val1 Test Val2 ]";
        assertEquals(expected, option.toString());
    }

    @Test
    public void testOptionClone() throws CloneNotSupportedException {
        Option option = new Option("-t", "Test Option", false);
        Option clonedOption = option.clone();
        
        assertEquals(option, clonedOption);
        assertNotSame(option, clonedOption);
        
        option = new Option("--test", "Test Option", false, "test", "Test Arg", 's', new String[]{"Test Val1", "Test Val2"});
        clonedOption = option.clone();
        
        assertEquals(option, clonedOption);
        assertNotSame(option, clonedOption);
        assertEquals(option.getValuesList(), clonedOption.getValuesList());
    }

    @Test
    public void testOptionClearValues() {
        Option option = new Option("-t", "Test Option", false);
        option.clearValues();
        assertFalse(option.hasValue());
        
        option = new Option("--test", "Test Option", false, "test", "Test Arg", 's', new String[]{"Test Val1", "Test Val2"});
        option.clearValues();
        assertFalse(option.hasValue());
    }

    @Test
    public void testOptionAddValueForProcessing() {
        Option option = new Option("-t", "Test Option", false);
        option.addValueForProcessing("Test Val");
        assertTrue(option.hasValue());
        assertEquals("Test Val", option.getValue());
        
        option = new Option("--test", "Test Option", false, "test", "Test Arg", 's');
        option.addValueForProcessing("Test Val1");
        option.addValueForProcessing("Test Val2");
        assertEquals(new String[]{"Test Val1", "Test Val2"}, option.getValues());
    }

    @Test
    public void testOptionAddValue() {
        throw new UnsupportedOperationException("The addValue method is not intended for client use. Subclasses should use the addValueForProcessing method instead. ");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testOptionAddValueException() {
        Option option = new Option("-t", "Test Option", false);
        option.addValue("Test Val");
    }
}