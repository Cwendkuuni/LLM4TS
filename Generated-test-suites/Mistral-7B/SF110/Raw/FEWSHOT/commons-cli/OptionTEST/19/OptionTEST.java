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
    public void testOptionConstruction() {
        Option option = new Option("a", "Option A", true);
        assertEquals("a", option.getOpt());
        assertEquals("Option A", option.getDescription());
        assertTrue(option.hasOptionalArg());
        assertFalse(option.hasArg());
        assertNull(option.getValue());
        assertNull(option.getValues());

        Option longOption = new Option("b", "Option B", false, "long-b");
        assertEquals("b", option.getOpt());
        assertEquals("Option B", option.getDescription());
        assertFalse(option.hasOptionalArg());
        assertTrue(option.hasLongOpt());
        assertEquals("long-b", option.getLongOpt());
        assertNull(option.getValue());
        assertNull(option.getValues());

        Option optWithValue = new Option("c", "Option C", false, "", "valueC");
        assertEquals("c", option.getOpt());
        assertEquals("Option C", option.getDescription());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.hasArg());
        assertEquals("valueC", option.getValue());
        assertNull(option.getValues());

        Option optWithArgs = new Option("d", "Option D", false, "");
        assertEquals("d", option.getOpt());
        assertEquals("Option D", option.getDescription());
        assertFalse(option.hasOptionalArg());
        assertTrue(option.hasArgs());
        assertNull(option.getValue());
        assertNull(option.getValues());

        Option optWithArgsAndValue = new Option("e", "Option E", false, "", "", "valueE");
        assertEquals("e", option.getOpt());
        assertEquals("Option E", option.getDescription());
        assertFalse(option.hasOptionalArg());
        assertTrue(option.hasArgs());
        assertEquals("valueE", option.getValue());
        assertNull(option.getValues());

        Option optWithValues = new Option("f", "Option F", false, "", "", null, 2);
        assertEquals("f", option.getOpt());
        assertEquals("Option F", option.getDescription());
        assertFalse(option.hasOptionalArg());
        assertTrue(option.hasArgs());
        assertNull(option.getValue());
        assertNotNull(option.getValues());
        assertTrue(option.getValues().isEmpty());

        Option optWithValuesAndValueSeparator = new Option("g", "Option G", false, "", "", null, 2, ';');
        assertEquals("g", option.getOpt());
        assertEquals("Option G", option.getDescription());
        assertFalse(option.hasOptionalArg());
        assertTrue(option.hasArgs());
        assertNull(option.getValue());
        assertNotNull(option.getValues());
        assertTrue(option.getValues().isEmpty());
        assertTrue(option.hasValueSeparator());
        assertEquals(';', option.getValueSeparator());

        Option validatorError = new Option(null, null);
        fail("Expected IllegalArgumentException");
    }

    @Test
    public void testOptionEqualsHashcode() {
        Option option1 = new Option("a", "Option A", true);
        Option option2 = new Option("a", "Option A", true);
        Option option3 = new Option("b", "Option B", false);

        assertEquals(option1, option1);
        assertEquals(option1, option2);
        assertEquals(option1.hashCode(), option2.hashCode());
        assertNotEquals(option1, option3);
        assertNotEquals(option1.hashCode(), option3.hashCode());
    }

    @Test
    public void testAddValueForProcessing() {
        Option option = new Option("a", "Option A", false, "");
        String[] values = {"val1", "val2", "val3"};

        option.addValueForProcessing("val1");
        option.addValueForProcessing("val2");
        option.addValueForProcessing("val3");

        assertEquals(Arrays.asList(values), option.getValuesList());
        assertEquals(values[0], option.getValue());
    }

    @Test(expected = RuntimeException.class)
    public void testAddValueForProcessingWithNoArgs() {
        Option option = new Option("a", "Option A", false, "", null, 0);
        option.addValueForProcessing("val");
    }

    @Test
    public void testAddValueForProcessingWithMultipleValues() {
        Option option = new Option("a", "Option A", false, "", null, 2);
        String[] values = {"val1", "val2", "val3"};

        option.addValueForProcessing("val1");
        option.addValueForProcessing("val2");
        option.addValueForProcessing("val3");

        assertEquals(Arrays.asList(values), option.getValuesList());
        assertEquals(values[0], option.getValue());
        assertEquals(Arrays.asList(Arrays.copyOfRange(values, 1, values.length)), option.getValues());
    }

    @Test
    public void testAddValueForProcessingWithValueSeparator() {
        Option option = new Option("a", "Option A", false, "", null, 2, ';');
        String[] values = {"val1;val2;val3"};

        option.addValueForProcessing("val1;val2;val3");

        assertEquals(Arrays.asList(values), option.getValuesList());
        assertEquals(values[0], option.getValue());
        assertEquals(Arrays.asList(Arrays.copyOfRange(values, 0, values.length)), option.getValues());
    }

    @Test
    public void testGetValue() {
        Option option = new Option("a", "Option A", false, "", null, 1);

        option.addValueForProcessing("val");
        assertEquals("val", option.getValue());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValueWithNoValues() {
        Option option = new Option("a", "Option A", false, "");
        option.addValueForProcessing("val1");
        option.addValueForProcessing("val2");

        option.clearValues();

        option.getValue();
    }

    @Test
    public void testGetValueWithMultipleValues() {
        Option option = new Option("a", "Option A", false, "", null, 1);

        option.addValueForProcessing("val1");
        option.addValueForProcessing("val2");

        assertEquals(option.getValue(0), option.getValue());
        assertEquals(option.getValue(1), null);
    }

    @Test
    public void testGetValues() {
        Option option = new Option("a", "Option A", false, "", null, 1);

        option.addValueForProcessing("val1");
        option.addValueForProcessing("val2");

        String[] values = option.getValues();
        assertEquals(values[0], option.getValue());
        assertEquals(Arrays.asList(Arrays.copyOfRange(values, 0, values.length)), option.getValues());
    }

    @Test
    public void testGetValuesList() {
        Option option = new Option("a", "Option A", false, "", null, 1);

        option.addValueForProcessing("val1");
        option.addValueForProcessing("val2");

        List valuesList = option.getValuesList();
        assertEquals(valuesList, Arrays.asList("val1", "val2"));
    }

    @Test
    public void testClearValues() {
        Option option = new Option("a", "Option A", false, "", null, 1);

        option.addValueForProcessing("val1");
        option.addValueForProcessing("val2");

        option.clearValues();

        assertNull(option.getValue());
        assertNull(option.getValues());
        assertNull(option.getValuesList());
    }

    @Test
    public void testClone() {
        Option option = new Option("a", "Option A", false, "", null, 1);

        option.addValueForProcessing("val1");
        option.addValueForProcessing("val2");

        Option clonedOption = option.clone();

        assertSame(option, clonedOption);

        clonedOption.clearValues();

        assertNotSame(option, clonedOption);

        assertEquals("val1", option.getValue());
        assertEquals(Arrays.asList("val1", "val2"), option.getValuesList());

        assertEquals(null, clonedOption.getValue());
        assertEquals(null, clonedOption.getValuesList());
    }

    @Test
    public void testToString() {
        Option option = new Option("a", "Option A", false, "", null, 1);

        option.addValueForProcessing("val1");
        option.addValueForProcessing("val2");

        assertEquals("[ option: a :: Option A :: [ARG] ]", option.toString());
    }
}