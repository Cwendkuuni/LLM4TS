package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.junit.Test;
import org.junit.Assert.*;
import static org.junit.Assert.*;

public class OptionTest {

    private Option option;

    @Before
    public void setup() {
        option = new Option("-o", "Option description");
    }

    @Test
    public void testOptionConstruction() {
        Option opt = new Option("-o", "Option description");
        assertEquals("-o", opt.getOpt());
        assertEquals("Option description", opt.getDescription());
        assertFalse(opt.hasArg());
        assertNull(opt.getLongOpt());
        assertEquals(0, opt.getArgs());
    }

    @Test
    public void testGetId() {
        assertEquals('-', option.getId());
    }

    @Test
    public void testAddValueForProcessing() {
        option.addValueForProcessing("value1");
        assertEquals(1, option.getValues().size());
        assertEquals("value1", option.getValues().get(0));
    }

    @Test(expected = RuntimeException.class)
    public void testAddValueForProcessingNoArgsAllowed() {
        option.setArgs(0);
        option.addValueForProcessing("value");
    }

    @Test
    public void testAddValueForProcessingMultipleValues() {
        option.setArgs(-2);
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        assertEquals(2, option.getValues().size());
        assertEquals("value1", option.getValues().get(0));
        assertEquals("value2", option.getValues().get(1));
    }

    // Add more test cases for other methods and edge cases as needed
}