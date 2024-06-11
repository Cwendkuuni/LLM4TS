package org.apache.commons.cli;

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
    public void testOptionCreation() {
        Option option = new Option("-t", "test option", true);
        assertEquals("-t", option.getOpt());
        assertEquals("test option", option.getDescription());
        assertTrue(option.isRequired());

        option = new Option("--test", "test long option", false);
        assertEquals("--test", option.getLongOpt());
        assertEquals("test long option", option.getDescription());
        assertFalse(option.isRequired());

        option = new Option("--test", "test long option", false, "value");
        assertEquals("value", option.getArgName());
        assertEquals("test long option [VALUE]", option.toString());

        try {
            option = new Option("-", "test empty opt", false, "value");
            fail("Option constructor should not accept empty option string.");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        option = new Option("-t", "test option", false, "value");
        assertEquals("value", option.getArgName());
        assertEquals("test option [VALUE]", option.toString());
    }

    @Test
    public void testOptionEqualsHashCode() {
        Option option1 = new Option("-a", "test option 1");
        Option option2 = new Option("-a", "test option 1");

        assertEquals(option1, option2);
        assertEquals(option1.hashCode(), option2.hashCode());

        Option option3 = new Option("-b", "test option 2");
        assertNotEquals(option1, option3);
        assertNotEquals(option1.hashCode(), option3.hashCode());

        Option option4 = new Option("-a", "test option 1");
        assertEquals(option1, option4);
        assertEquals(option1.hashCode(), option4.hashCode());
    }

    @Test
    public void testOptionGetKey() {
        Option option = new Option("-a", "test option");
        assertEquals("-a", option.getKey());

        option = new Option("--test", "test long option");
        assertEquals("--test", option.getKey());
    }

    @Test
    public void testOptionId() {
        Option option = new Option("-a", "test option");
        assertEquals('-', option.getId());

        option = new Option("--test", "test long option");
        assertEquals('--', option.getId());
    }

    @Test
    public void testOptionHasArg() {
        Option option = new Option("-a", "test option");
        assertFalse(option.hasArg());

        option = new Option("-a:value", "test option with argument");
        assertTrue(option.hasArg());

        option = new Option("-a:value1,value2", "test option with multiple arguments");
        assertTrue(option.hasArgs());
    }

    @Test
    public void testOptionHasOptionalArg() {
        Option option = new Option("-a", "test option");
        assertFalse(option.hasOptionalArg());

        option = new Option("-a:value", "test option with optional argument");
        assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testOptionHasLongOpt() {
        Option option = new Option("-a", "test option");
        assertFalse(option.hasLongOpt());

        option = new Option("-a", "--long-opt", false);
        assertTrue(option.hasLongOpt());
    }

    @Test
    public void testOptionDescription() {
        Option option = new Option("-a", "test option");
        assertEquals("test option", option.getDescription());

        option = new Option("-a", "test option", null, "value");
        assertEquals("test option [VALUE]", option.getDescription());
    }

    @Test
    public void testOptionSetDescription() {
        Option option = new Option("-a", "test option");
        option.setDescription("new description");
        assertEquals("new description", option.getDescription());
    }

    @Test
    public void testOptionRequired() {
        Option option = new Option("-a", "test option");
        assertFalse(option.isRequired());

        option = new Option("-a:value", "test option with required argument");
        assertTrue(option.isRequired());
    }

    @Test
    public void testOptionSetRequired() {
        Option option = new Option("-a", "test option");
        option.setRequired(true);
        assertTrue(option.isRequired());
    }

    @Test
    public void testOptionSetArgName() {
        Option option = new Option("-a:value", "test option with argument");
        option.setArgName("new-arg-name");
        assertEquals("new-arg-name", option.getArgName());
    }

    @Test
    public void testOptionHasArgName() {
        Option option = new Option("-a", "test option");
        assertFalse(option.hasArgName());

        option = new Option("-a:value", "test option with argument");
        assertTrue(option.hasArgName());
    }

    @Test
    public void testOptionSetArgs() {
        Option option = new Option("-a:value1,value2", "test option with multiple arguments");
        option.setArgs(2);
        assertEquals(2, option.getArgs());
        assertFalse(option.hasArgs());
    }

    @Test
    public void testOptionGetArgs() {
        Option option = new Option("-a:value1,value2", "test option with multiple arguments");
        assertEquals(2, option.getArgs());

        option = new Option("-a:value", "test option with single argument");
        assertEquals(1, option.getArgs());

        option = new Option("-a", "test option without argument");
        assertEquals(0, option.getArgs());
    }

    @Test
    public void testOptionAddValueForProcessing() {
        Option option = new Option("-a:value1,value2", "test option with multiple arguments");

        option.addValueForProcessing("value1");
        assertEquals(1, option.values.size());
        assertEquals("value1", option.values.get(0));

        option.addValueForProcessing("value2");
        assertEquals(2, option.values.size());
        assertEquals("value2", option.values.get(1));
    }

    @Test(expected = RuntimeException.class)
    public void testOptionAddValueForProcessingListFull() {
        Option option = new Option("-a:value1,value2", "test option with multiple arguments");
        option.setArgs(1);
        option.addValueForProcessing("value3");
    }

    @Test
    public void testOptionGetValue() {
        Option option = new Option("-a:value", "test option with single argument");
        option.addValueForProcessing("test-value");

        assertEquals("test-value", option.getValue());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testOptionGetValueNegativeIndex() {
        Option option = new Option("-a:value1,value2", "test option with multiple arguments");
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");

        option.getValue(-1);
    }

    @Test
    public void testOptionGetValues() {
        Option option = new Option("-a:value1,value2", "test option with multiple arguments");
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");

        String[] values = option.getValues();
        assertEquals(2, values.length);
        assertEquals("value1", values[0]);
        assertEquals("value2", values[1]);
    }

    @Test
    public void testOptionGetValuesList() {
        Option option = new Option("-a:value1,value2", "test option with multiple arguments");
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");

        List valuesList = option.getValuesList();
        assertEquals(2, valuesList.size());
        assertEquals("value1", valuesList.get(0));
        assertEquals("value2", valuesList.get(1));
    }

    @Test
    public void testOptionClearValues() {
        Option option = new Option("-a:value1,value2", "test option with multiple arguments");
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");

        option.clearValues();
        assertTrue(option.hasNoValues());
    }

    @Test
    public void testOptionToString() {
        Option option = new Option("-a:value", "test option with single argument");
        option.addValueForProcessing("test-value");

        String expected = "[ option: -a :: test option [test-value] :: java.lang.String ]";
        assertEquals(expected, option.toString());
    }
}