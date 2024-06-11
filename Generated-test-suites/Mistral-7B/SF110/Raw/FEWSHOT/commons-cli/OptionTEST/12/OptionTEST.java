package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class OptionTest {

    @Test
    public void testOptionWithNoArgs() {
        Option option = new Option("opt1", "Description for opt1");
        assertEquals("opt1", option.getOpt());
        assertEquals("Description for opt1", option.getDescription());
        assertFalse(option.hasArg());
        assertNull(option.getValue());
        assertNull(option.getValues());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.isRequired());
        assertFalse(option.hasArgName());
        assertNull(option.getArgName());
        assertFalse(option.hasLongOpt());
        assertNull(option.getLongOpt());
        assertEquals(Option.UNINITIALIZED, option.getId());
    }

    @Test
    public void testOptionWithSingleArg() {
        Option option = new Option("opt1", "Description for opt1", true, "argName");
        assertEquals("opt1", option.getOpt());
        assertEquals("Description for opt1", option.getDescription());
        assertTrue(option.hasArg());
        assertEquals("argName", option.getArgName());
        assertNull(option.getValue());
        assertNull(option.getValues());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.isRequired());
        assertFalse(option.hasLongOpt());
        assertNull(option.getLongOpt());
        assertEquals(Option.UNINITIALIZED, option.getId());

        option.setValue("value1");
        assertEquals("value1", option.getValue());
        assertEquals(1, option.getId());
        String[] values = option.getValues();
        assertNotNull(values);
        assertEquals(1, values.length);
        assertEquals("value1", values[0]);
    }

    @Test
    public void testOptionWithMultipleArgs() {
        Option option = new Option("opt1", "Description for opt1", true, "argName", Option.UNLIMITED_VALUES, null);
        assertEquals("opt1", option.getOpt());
        assertEquals("Description for opt1", option.getDescription());
        assertTrue(option.hasArg());
        assertEquals("argName", option.getArgName());
        assertNull(option.getValue());
        assertNull(option.getValues());
        assertTrue(option.isOptionalArg());
        assertFalse(option.isRequired());
        assertFalse(option.hasLongOpt());
        assertNull(option.getLongOpt());
        assertEquals(Option.UNINITIALIZED, option.getId());

        option.setValue("value1");
        option.setValue("value2");
        String[] values = option.getValues();
        assertNotNull(values);
        assertEquals(2, values.length);
        assertEquals("value1", values[0]);
        assertEquals("value2", values[1]);
        assertEquals(Option.UNLIMITED_VALUES, option.getId());
    }

    @Test
    public void testOptionWithLongOpt() {
        Option option = new Option("opt1", "long-opt-name", true, "argName");
        assertEquals("opt1", option.getOpt());
        assertEquals("long-opt-name", option.getLongOpt());
        assertTrue(option.hasArg());
        assertEquals("argName", option.getArgName());
        assertNull(option.getValue());
        assertNull(option.getValues());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.isRequired());
        assertEquals(Option.UNINITIALIZED, option.getId());

        option.setValue("value1");
        assertEquals("value1", option.getValue());
        assertEquals(1, option.getId());
        String[] values = option.getValues();
        assertNotNull(values);
        assertEquals(1, values.length);
        assertEquals("value1", values[0]);
    }

    @Test
    public void testOptionWithType() {
        Option option = new Option("opt1", "Description for opt1", false, null, null, Integer.class);
        assertEquals("opt1", option.getOpt());
        assertEquals("Description for opt1", option.getDescription());
        assertFalse(option.hasArg());
        assertNull(option.getArgName());
        assertNull(option.getValue());
        assertNull(option.getValues());
        assertFalse(option.isRequired());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.hasLongOpt());
        assertNull(option.getLongOpt());
        assertEquals(Option.UNINITIALIZED, option.getId());

        option.setValue(Integer.valueOf(1));
        assertEquals(1, option.getId());
        assertEquals(Integer.valueOf(1), option.getValue());
        assertNull(option.getValues());
    }

    @Test
    public void testOptionWithValues() throws IOException {
        Option option = new Option("opt1", "Description for opt1", true, "argName", null, null);
        assertEquals("opt1", option.getOpt());
        assertEquals("Description for opt1", option.getDescription());
        assertTrue(option.hasArg());
        assertEquals("argName", option.getArgName());
        assertNull(option.getValue());
        assertNull(option.getValues());
        assertFalse(option.isRequired());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.hasLongOpt());
        assertNull(option.getLongOpt());
        assertEquals(Option.UNINITIALIZED, option.getId());

        List<String> values = new ArrayList<>();
        values.add("value1");
        values.add("value2");
        option.setValues(values);

        assertEquals(2, option.getId());
        String[] valuesArray = option.getValues();
        assertNotNull(valuesArray);
        assertEquals(2, valuesArray.length);
        assertEquals("value1", valuesArray[0]);
        assertEquals("value2", valuesArray[1]);
    }

    @Test
    public void testEquals() {
        Option option1 = new Option("opt1", "Description for opt1");
        Option option2 = new Option("opt1", "Description for opt1");
        assertEquals(option1, option2);

        Option option3 = new Option("opt2", "Description for opt1");
        assertNotEquals(option1, option3);

        Option option4 = new Option("opt1", "Description for opt1");
        option4.setValue("value1");
        assertNotEquals(option1, option4);

        Option option5 = new Option("opt1", "Description for opt1");
        option5.setValue("value1");
        option1.setValue("value1");
        assertEquals(option1, option5);

        Option option6 = new Option("opt1", "Description for opt1");
        option6.setLongOpt("longOpt");
        assertFalse(option1.equals(option6));

        Option option7 = new Option("opt1", "Description for opt1");
        option7.setOptionalArg(true);
        assertFalse(option1.equals(option7));

        Option option8 = new Option("opt1", "Description for opt1");
        option8.setArgName("argName2");
        assertFalse(option1.equals(option8));

        Option option9 = new Option("opt1", "Description for opt1");
        option9.setRequired(true);
        assertFalse(option1.equals(option9));

        Option option10 = new Option("opt1", "Description for opt1");
        option10.setType(Integer.class);
        assertFalse(option1.equals(option10));
    }

    @Test
    public void testHashCode() {
        Option option1 = new Option("opt1", "Description for opt1");
        Option option2 = new Option("opt1", "Description for opt1");
        assertEquals(option1.hashCode(), option2.hashCode());

        Option option3 = new Option("opt2", "Description for opt1");
        assertNotEquals(option1.hashCode(), option3.hashCode());

        Option option4 = new Option("opt1", "Description for opt1");
        option4.setValue("value1");
        assertNotEquals(option1.hashCode(), option4.hashCode());

        Option option5 = new Option("opt1", "Description for opt1");
        option5.setValue("value1");
        option1.setValue("value1");
        assertEquals(option1.hashCode(), option5.hashCode());

        Option option6 = new Option("opt1", "Description for opt1");
        option6.setLongOpt("longOpt");
        assertNotEquals(option1.hashCode(), option6.hashCode());

        Option option7 = new Option("opt1", "Description for opt1");
        option7.setOptionalArg(true);
        assertNotEquals(option1.hashCode(), option7.hashCode());

        Option option8 = new Option("opt1", "Description for opt1");
        option8.setArgName("argName2");
        assertNotEquals(option1.hashCode(), option8.hashCode());

        Option option9 = new Option("opt1", "Description for opt1");
        option9.setRequired(true);
        assertNotEquals(option1.hashCode(), option9.hashCode());

        Option option10 = new Option("opt1", "Description for opt1");
        option10.setType(Integer.class);
        assertNotEquals(option1.hashCode(), option10.hashCode());
    }

    @Test
    public void testClearValues() {
        Option option = new Option("opt1", "Description for opt1", true, "argName", null, null);
        option.setValue("value1");
        option.setValue("value2");
        List<String> values = option.getValues();
        assertEquals(2, values.size());

        option.clearValues();
        values = option.getValues();
        assertEquals(0, values.size());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddValue() {
        Option option = new Option("opt1", "Description for opt1", true, "argName", null, null);
        option.addValue("value1");
    }
}