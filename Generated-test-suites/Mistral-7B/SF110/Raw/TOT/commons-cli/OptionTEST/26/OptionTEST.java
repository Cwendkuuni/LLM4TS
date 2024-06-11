package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class OptionTest {

    @Test
    public void testSimpleOption() throws ParseException {
        Option option = new Option("a", "arg", false, "An option with an argument");
        Assert.assertEquals('a', option.getId());
        Assert.assertEquals("arg", option.getArgName());
        Assert.assertFalse(option.hasOptionalArg());
        Assert.assertFalse(option.hasArgs());
        Assert.assertNull(option.getValue());
    }

    @Test
    public void testOptionalOptionWithArg() throws ParseException {
        Option option = new Option("b", "longArg", true, "An optional option with an argument");
        Assert.assertEquals('b', option.getId());
        Assert.assertEquals("longArg", option.getLongOpt());
        Assert.assertTrue(option.hasOptionalArg());
        Assert.assertTrue(option.hasArgs());
        option.setValue('x');
        Assert.assertEquals('x', option.getValue().charAt(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidOption() {
        new Option("", "", false, "");
    }

    @Test
    public void testOptionWithMultipleValues() throws ParseException {
        Option option = new Option("c", null, false, "An option with multiple values");
        option.setType(String[].class);
        option.setValueSeparator(' ');
        option.addValue("value1");
        option.addValue("value2");
        Assert.assertEquals(2, option.getArgs());
        String[] values = option.getValues();
        Assert.assertEquals("value1", values[0]);
        Assert.assertEquals("value2", values[1]);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetValueOutOfBounds() {
        Option option = new Option("d", null, false, "An option with a value");
        option.setValue("value");
        option.addValue("value2");
        try {
            option.getValue(1);
        } catch (IndexOutOfBoundsException e) {
            Assert.assertEquals(1, option.getArgs());
            Assert.assertEquals("value2", e.getMessage());
        }
    }
}