package org.apache.commons.cli;

import static org.junit.Assert.*;
import java.util.*;

public class OptionTest {

    @Test
    public void testOptionConstruction() {
        Option opt = new Option("a", "option A", true, "an optional value for option A");
        assertEquals("a", opt.getOpt());
        assertEquals("option A", opt.getDescription());
        assertTrue(opt.isRequired());

        Option optWithLongName = new Option("b", "long-option-B", false, "a long option with a long name B");
        assertEquals("b", optWithLongName.getOpt());
        assertEquals("long-option-B", optWithLongName.getLongOpt());
        assertFalse(optWithLongName.isRequired());

        Option optWithValues = new Option("c", null, false, "option C with values", 3);
        assertEquals(3, optWithValues.getNumberOfArgs());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidOptionConstruction() {
        new Option(null, "description", false);
    }

    @Test
    public void testOptionGettersAndSetters() {
        Option opt = new Option("a", "option A", true, "an optional value for option A");

        opt.setDescription("updated description");
        assertEquals("updated description", opt.getDescription());

        opt.setOptionalArg(false);
        assertFalse(opt.isOptionalArg());

        opt.setHasArg(true);
        assertTrue(opt.hasArg());

        opt.setArgName("argName");
        assertEquals("argName", opt.getArgName());

        opt.setNumberOfArgs(2);
        assertEquals(2, opt.getNumberOfArgs());

        opt.setType("String.class");
        assertEquals("String.class", opt.getType());

        List values = new ArrayList();
        values.add("value1");
        values.add("value2");
        opt.setValues(values);
        assertEquals(values, opt.getValuesList());

        opt.clearValues();
        assertTrue(opt.hasNoValues());
    }

    @Test
    public void testOptionProcessingValues() {
        Option opt = new Option("a", null, true, "option A", 1);
        opt.setValueSeparator(' ');

        opt.addValueForProcessing("value1 value2");
        List values = opt.getValuesList();
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));

        opt.clearValues();
        opt.addValueForProcessing("value3");
        values = opt.getValuesList();
        assertEquals(1, values.size());
        assertEquals("value3", values.get(0));
    }

    @Test(expected = RuntimeException.class)
    public void testOptionProcessingValuesInvalidArguments() {
        Option opt = new Option("a", null, true, "option A", 1);

        opt.addValueForProcessing("value1 value2 value3");
    }
}