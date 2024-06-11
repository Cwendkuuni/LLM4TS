package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class OptionTest {

    @Test
    public void testOptionConstruction() {
        Option option = new Option("opt", "Option description");
        Assert.assertEquals("opt", option.getOpt());
        Assert.assertEquals("Option description", option.getDescription());
        Assert.assertNull(option.getLongOpt());
        Assert.assertFalse(option.hasArg());
        Assert.assertFalse(option.isRequired());
        Assert.assertEquals(-1, option.getArgs());
        Assert.assertNull(option.getValue());
        Assert.assertEquals(0, option.getValues().size());

        Option longOption = new Option("longOpt", "Long option description", true, "arg");
        Assert.assertEquals("longOpt", option.getOpt());
        Assert.assertEquals("Long option description", option.getDescription());
        Assert.assertEquals("arg", option.getArgName());
        Assert.assertTrue(option.isRequired());
        Assert.assertTrue(option.hasArgName());
        Assert.assertEquals(1, option.getArgs());
        Assert.assertNull(option.getValue());
        Assert.assertEquals(0, option.getValues().size());

        Option hasValueOption = new Option("valueOpt", "Option with value", true, "value");
        Assert.assertEquals("valueOpt", option.getOpt());
        Assert.assertEquals("Option with value", option.getDescription());
        Assert.assertEquals("value", option.getArgName());
        Assert.assertTrue(option.isRequired());
        Assert.assertTrue(option.hasArgName());
        Assert.assertEquals(1, option.getArgs());
        Assert.assertNull(option.getValue());
        Assert.assertEquals(0, option.getValues().size());

        Option hasValuesOption = new Option("valuesOpt", "Option with multiple values", false, "value");
        Assert.assertEquals("valuesOpt", option.getOpt());
        Assert.assertEquals("Option with multiple values", option.getDescription());
        Assert.assertEquals("value", option.getArgName());
        Assert.assertFalse(option.isRequired());
        Assert.assertTrue(option.hasArgName());
        Assert.assertEquals(-2, option.getArgs());
        Assert.assertNull(option.getValue());
        Assert.assertEquals(0, option.getValues().size());
    }

    @Test
    public void testOptionGettersAndSetters() {
        Option option = new Option("opt", "Option description");
        Assert.assertEquals("Option description", option.getDescription());
        option.setDescription("New option description");
        Assert.assertEquals("New option description", option.getDescription());

        Assert.assertFalse(option.isRequired());
        option.setRequired(true);
        Assert.assertTrue(option.isRequired());

        Assert.assertFalse(option.hasArg());
        option.setHasArg(true);
        Assert.assertTrue(option.hasArg());

        Assert.assertEquals(-1, option.getArgs());
        option.setArgs(3);
        Assert.assertEquals(3, option.getArgs());

        Assert.assertNull(option.getType());
        option.setType(String.class);
        Assert.assertEquals(String.class, option.getType());

        List values = new ArrayList();
        option.setValues(values);
        Assert.assertEquals(values, option.getValuesList());

        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        Assert.assertEquals(2, option.getValues().size());
        Assert.assertEquals("value1", option.getValue(0));
        Assert.assertEquals("value2", option.getValue(1));

        try {
            option.addValueForProcessing("value3");
            Assert.fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }
}