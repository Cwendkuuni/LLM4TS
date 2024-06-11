package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OptionTest {

    @Test
    public void testOptionWithNoArg() {
        Option option = new Option("a", "optionA", false, "This is option A");
        Assert.assertEquals("optionA", option.getOpt());
        Assert.assertNull(option.getLongOpt());
        Assert.assertFalse(option.hasArg());
        Assert.assertFalse(option.hasOptionalArg());
        Assert.assertFalse(option.isRequired());
        Assert.assertEquals("This is option A", option.getDescription());
        Assert.assertNull(option.getType());
        Assert.assertFalse(option.hasLongOpt());
        Assert.assertFalse(option.hasArgName());
        Assert.assertFalse(option.hasArgs());
        Assert.assertEquals(-1, option.getArgs());
        Assert.assertEquals(false, option.hasValueSeparator());
        Assert.assertNull(option.getValue());
        Assert.assertEquals(0, option.values.size());
        Assert.assertTrue(option.equals(option));
        Assert.assertEquals(option.hashCode(), option.hashCode());
    }

    @Test
    public void testOptionWithSingleArg() {
        Option option = new Option("b", "optionB", true, "This is option B with an argument");
        Assert.assertEquals("b", option.getOpt());
        Assert.assertNull(option.getLongOpt());
        Assert.assertTrue(option.hasArg());
        Assert.assertFalse(option.hasOptionalArg());
        Assert.assertFalse(option.isRequired());
        Assert.assertEquals("This is option B with an argument", option.getDescription());
        Assert.assertNull(option.getType());
        Assert.assertFalse(option.hasLongOpt());
        Assert.assertFalse(option.hasArgName());
        Assert.assertFalse(option.hasArgs());
        Assert.assertEquals(1, option.getArgs());
        Assert.assertEquals(true, option.hasValueSeparator());
        Assert.assertNull(option.getValue());
        Assert.assertEquals(0, option.values.size());
        Assert.assertFalse(option.equals(new Option("a", null, false, "")));
    }

    @Test
    public void testOptionWithSingleArgAndValue() {
        Option option = new Option("c", "optionC", true, "This is option C with an argument");
        option.addValue("value1");
        Assert.assertEquals("c", option.getOpt());
        Assert.assertNull(option.getLongOpt());
        Assert.assertTrue(option.hasArg());
        Assert.assertFalse(option.hasOptionalArg());
        Assert.assertFalse(option.isRequired());
        Assert.assertEquals("This is option C with an argument", option.getDescription());
        Assert.assertNull(option.getType());
        Assert.assertFalse(option.hasLongOpt());
        Assert.assertFalse(option.hasArgName());
        Assert.assertFalse(option.hasArgs());
        Assert.assertEquals(1, option.getArgs());
        Assert.assertEquals(true, option.hasValueSeparator());
        Assert.assertEquals("value1", option.getValue());
        Assert.assertEquals(1, option.values.size());
        Assert.assertEquals("value1", option.values.get(0));
        Assert.assertTrue(option.equals(option));
        Assert.assertEquals(option.hashCode(), option.hashCode());
    }

    @Test(expected = RuntimeException.class)
    public void testAddValueWhenNotAllowed() {
        Option option = new Option("d", null, false, "This is option D");
        option.addValue("value1");
    }

    @Test
    public void testOptionWithMultipleArgs() {
        Option option = new Option("e", "optionE", false, "This is option E with multiple arguments");
        option.setArgs(2);
        Assert.assertEquals("e", option.getOpt());
        Assert.assertNull(option.getLongOpt());
        Assert.assertFalse(option.hasArg());
        Assert.assertFalse(option.hasOptionalArg());
        Assert.assertFalse(option.isRequired());
        Assert.assertEquals("This is option E with multiple arguments", option.getDescription());
        Assert.assertNull(option.getType());
        Assert.assertFalse(option.hasLongOpt());
        Assert.assertFalse(option.hasArgName());
        Assert.assertTrue(option.hasArgs());
        Assert.assertEquals(2, option.getArgs());
        Assert.assertEquals(false, option.hasValueSeparator());
        Assert.assertNull(option.getValue());
        Assert.assertEquals(0, option.values.size());
    }

    @Test
    public void testOptionWithMultipleArgsAndValues() {
        Option option = new Option("f", "optionF", false, "This is option F with multiple arguments");
        option.setArgs(2);
        option.addValue("value1");
        option.addValue("value2");
        Assert.assertEquals("f", option.getOpt());
        Assert.assertNull(option.getLongOpt());
        Assert.assertFalse(option.hasArg());
        Assert.assertFalse(option.hasOptionalArg());
        Assert.assertFalse(option.isRequired());
        Assert.assertEquals("This is option F with multiple arguments", option.getDescription());
        Assert.assertNull(option.getType());
        Assert.assertFalse(option.hasLongOpt());
        Assert.assertFalse(option.hasArgName());
        Assert.assertTrue(option.hasArgs());
        Assert.assertEquals(2, option.getArgs());
        Assert.assertEquals(false, option.hasValueSeparator());
        Assert.assertNull(option.getValue());
        Assert.assertEquals(2, option.values.size());
        Assert.assertEquals(Arrays.asList("value1", "value2"), option.values);
        Assert.assertTrue(option.equals(option));
        Assert.assertEquals(option.hashCode(), option.hashCode());
    }

    @Test
    public void testOptionWithLongOpt() {
        Option option = new Option("g", null, false, "This is option G with a long option name");
        Assert.assertNull(option.getOpt());
        Assert.assertEquals("This is option G with a long option name", option.getDescription());
        Assert.assertEquals("longOptionG", option.getLongOpt());
        Assert.assertFalse(option.hasArg());
        Assert.assertFalse(option.hasOptionalArg());
        Assert.assertFalse(option.isRequired());
        Assert.assertNull(option.getType());
        Assert.assertTrue(option.hasLongOpt());
        Assert.assertFalse(option.hasArgName());
        Assert.assertFalse(option.hasArgs());
        Assert.assertEquals(-1, option.getArgs());
        Assert.assertEquals(false, option.hasValueSeparator());
        Assert.assertNull(option.getValue());
        Assert.assertEquals(0, option.values.size());
        Assert.assertTrue(option.equals(option));
        Assert.assertEquals(option.hashCode(), option.hashCode());
    }

    @Test
    public void testOptionWithLongOptAndArg() {
        Option option = new Option("h", null, true, "This is option H with a long option name and an argument");
        Assert.assertNull(option.getOpt());
        Assert.assertEquals("This is option H with a long option name and an argument", option.getDescription());
        Assert.assertEquals("longOptionH", option.getLongOpt());
        Assert.assertTrue(option.hasArg());
        Assert.assertFalse(option.hasOptionalArg());
        Assert.assertFalse(option.isRequired());
        Assert.assertNull(option.getType());
        Assert.assertTrue(option.hasLongOpt());
        Assert.assertFalse(option.hasArgName());
        Assert.assertFalse(option.hasArgs());
        Assert.assertEquals(1, option.getArgs());
        Assert.assertEquals(true, option.hasValueSeparator());
        Assert.assertNull(option.getValue());
        Assert.assertEquals(0, option.values.size());
    }

    @Test
    public void testOptionWithOptionalArg() {
        Option option = new Option("i", "optionI", false, "This is option I with an optional argument", true);
        Assert.assertEquals("i", option.getOpt());
        Assert.assertNull(option.getLongOpt());
        Assert.assertTrue(option.hasArg());
        Assert.assertTrue(option.hasOptionalArg());
        Assert.assertFalse(option.isRequired());
        Assert.assertEquals("This is option I with an optional argument", option.getDescription());
        Assert.assertNull(option.getType());
        Assert.assertFalse(option.hasLongOpt());
        Assert.assertFalse(option.hasArgName());
        Assert.assertFalse(option.hasArgs());
        Assert.assertEquals(-1, option.getArgs());
        Assert.assertEquals(false, option.hasValueSeparator());
        Assert.assertNull(option.getValue());
        Assert.assertEquals(0, option.values.size());
        Assert.assertEquals("[arg]", option.toString());
    }

    @Test
    public void testOptionWithManyProperties() {
        Option option = new Option("j", "optionJ", false, "This is option J", true, 5, 's', new String[]{"value1", "value2"});
        Assert.assertEquals("j", option.getOpt());
        Assert.assertEquals("optionJ", option.getLongOpt());
        Assert.assertTrue(option.hasArgName());
        Assert.assertEquals("arg", option.getArgName());
        Assert.assertTrue(option.hasOptionalArg());
        Assert.assertFalse(option.isRequired());
        Assert.assertEquals("This is option J", option.getDescription());
        Assert.assertEquals(Integer.TYPE, option.getType().getClass());
        Assert.assertEquals(5, ((Number)option.getType()).intValue());
        Assert.assertFalse(option.hasLongOpt());
        Assert.assertEquals('s', option.getValueSeparator());
        Assert.assertNotNull(option.values);
        Assert.assertEquals(2, option.values.size());
        Assert.assertEquals(Arrays.asList("value1", "value2"), option.values);
        Assert.assertEquals("[optionJ :: This is option J :: arg :: [value1, value2] :: Integer :: 5 :: s]", option.toString());
    }

    @Test
    public void testClone() {
        Option option = new Option("k", null, false, "This is option K");
        option.addValue("value1");
        Option clonedOption = option.clone();
        Assert.assertEquals(option, clonedOption);
        Assert.assertEquals(option.hashCode(), clonedOption.hashCode());
        Assert.assertEquals(option.values, clonedOption.values);
    }

    @Test
    public void testClearValues() {
        Option option = new Option("l", null, false, "This is option L");
        option.addValue("value1");
        option.clearValues();
        Assert.assertEquals(0, option.values.size());
        Assert.assertNull(option.getValue());
    }
}