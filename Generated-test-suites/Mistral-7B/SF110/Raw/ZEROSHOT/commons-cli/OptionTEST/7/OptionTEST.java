package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class OptionTest {

    @Test
    public void testOptionConstruction() {
        Option option = new Option("a", "option-a", false, "This is an option.");
        Assert.assertEquals("option-a", option.getOpt());
        Assert.assertEquals("option-a", option.getKey());
        Assert.assertFalse(option.hasArg());
        Assert.assertFalse(option.hasLongOpt());
        Assert.assertFalse(option.isRequired());
        Assert.assertNull(option.getValue());
        Assert.assertNull(option.getDescription());
        Assert.assertFalse(option.hasValueSeparator());
        Assert.assertEquals(0, option.getArgs());
        Assert.assertFalse(option.hasArgName());
        Assert.assertFalse(option.hasArgs());
        Assert.assertFalse(option.hasOptionalArg());
        Assert.assertNull(option.getType());
        Assert.assertNotNull(option.getValuesList());
        Assert.assertEquals(0, option.getValues().size());
        Assert.assertEquals("[ option: a :: This is an option. ]", option.toString());
    }

    @Test
    public void testOptionConstructionWithArg() {
        Option option = new Option("b", "option-b", true, "This is an option with an argument.");
        Assert.assertEquals("b", option.getOpt());
        Assert.assertEquals("option-b", option.getKey());
        Assert.assertTrue(option.hasArg());
        Assert.assertFalse(option.hasLongOpt());
        Assert.assertFalse(option.isRequired());
        Assert.assertNull(option.getValue());
        Assert.assertNull(option.getDescription());
        Assert.assertFalse(option.hasValueSeparator());
        Assert.assertEquals(1, option.getArgs());
        Assert.assertFalse(option.hasArgName());
        Assert.assertFalse(option.hasArgs());
        Assert.assertFalse(option.hasOptionalArg());
        Assert.assertNull(option.getType());
        Assert.assertNotNull(option.getValuesList());
        Assert.assertEquals(0, option.getValues().size());
        Assert.assertEquals("[ option: b [ARG] :: This is an option with an argument. ]", option.toString());
    }

    @Test
    public void testOptionConstructionWithLongOpt() {
        Option option = new Option("c", null, false, "This is an option with a long option.");
        Assert.assertEquals("", option.getOpt());
        Assert.assertEquals("--option-c", option.getKey());
        Assert.assertFalse(option.hasArg());
        Assert.assertTrue(option.hasLongOpt());
        Assert.assertFalse(option.isRequired());
        Assert.assertNull(option.getValue());
        Assert.assertNull(option.getDescription());
        Assert.assertFalse(option.hasValueSeparator());
        Assert.assertEquals(0, option.getArgs());
        Assert.assertFalse(option.hasArgName());
        Assert.assertFalse(option.hasArgs());
        Assert.assertFalse(option.hasOptionalArg());
        Assert.assertNull(option.getType());
        Assert.assertNotNull(option.getValuesList());
        Assert.assertEquals(0, option.getValues().size());
        Assert.assertEquals("[ option: --option-c :: This is an option with a long option. ]", option.toString());
    }

    @Test
    public void testOptionConstructionWithLongOptAndArg() {
        Option option = new Option("d", "--option-d", true, "This is an option with a long option and an argument.");
        Assert.assertEquals("", option.getOpt());
        Assert.assertEquals("--option-d", option.getKey());
        Assert.assertTrue(option.hasArg());
        Assert.assertTrue(option.hasLongOpt());
        Assert.assertFalse(option.isRequired());
        Assert.assertNull(option.getValue());
        Assert.assertNull(option.getDescription());
        Assert.assertFalse(option.hasValueSeparator());
        Assert.assertEquals(1, option.getArgs());
        Assert.assertFalse(option.hasArgName());
        Assert.assertFalse(option.hasArgs());
        Assert.assertFalse(option.hasOptionalArg());
        Assert.assertNull(option.getType());
        Assert.assertNotNull(option.getValuesList());
        Assert.assertEquals(0, option.getValues().size());
        Assert.assertEquals("[ option: --option-d [ARG] :: This is an option with a long option and an argument. ]", option.toString());
    }

    @Test
    public void testOptionGettersAndSetters() {
        Option option = new Option("e", "option-e", false, "This is an option with a getter and a setter.");
        Assert.assertEquals("option-e", option.getOpt());
        Assert.assertEquals("option-e", option.getKey());
        Assert.assertFalse(option.hasArg());
        Assert.assertFalse(option.hasLongOpt());
        Assert.assertFalse(option.isRequired());
        Assert.assertNull(option.getValue());
        Assert.assertNull(option.getDescription());
        Assert.assertFalse(option.hasValueSeparator());
        Assert.assertEquals(0, option.getArgs());
        Assert.assertFalse(option.hasArgName());
        Assert.assertFalse(option.hasArgs());
        Assert.assertFalse(option.hasOptionalArg());
        Assert.assertNull(option.getType());
        Assert.assertNotNull(option.getValuesList());
        Assert.assertEquals(0, option.getValues().size());
        option.setDescription("This is an updated option description.");
        Assert.assertEquals("This is an updated option description.", option.getDescription());
        option.setRequired(true);
        Assert.assertTrue(option.isRequired());
        option.setArgName("arg-name");
        Assert.assertEquals("arg-name", option.getArgName());
        option.setHasArg(true);
        Assert.assertTrue(option.hasArg());
        option.setHasLongOpt(true);
        Assert.assertTrue(option.hasLongOpt());
        option.setHasOptionalArg(true);
        Assert.assertTrue(option.hasOptionalArg());
        option.setType(Integer.class);
        Assert.assertEquals(Integer.class, option.getType());
        option.setValuesSeparator(';');
        Assert.assertEquals(';', option.getValueSeparator());
        option.setValueSeparator('=');
        Assert.assertEquals('=', option.getValueSeparator());
        option.setArgs(2);
        Assert.assertEquals(2, option.getArgs());
        option.clearValues();
        Assert.assertEquals(0, option.getValues().size());
        List<String> values = new ArrayList<>();
        values.add("value1");
        values.add("value2");
        option.setValues(values);
        Assert.assertEquals(2, option.getValues().size());
        Assert.assertEquals("value1", option.getValue(0));
        Assert.assertEquals("value2", option.getValue(1));
        option.setValue("newValue");
        Assert.assertEquals("newValue", option.getValue());
        option.removeValue("value1");
        Assert.assertEquals(1, option.getValues().size());
        Assert.assertEquals("value2", option.getValue(0));
        option.removeValueAt(0);
        Assert.assertEquals(0, option.getValues().size());
        Assert.assertNull(option.getValue());
    }
}