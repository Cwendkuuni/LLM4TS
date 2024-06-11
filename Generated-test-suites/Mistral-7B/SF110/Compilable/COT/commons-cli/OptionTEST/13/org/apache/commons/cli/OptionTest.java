package org.apache.commons.cli;
import org.apache.commons.cli.Option;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OptionTest {

    @Test
    public void testOptionConstruction() {
        Option option = new Option("a", "optionA", false, "Description for option A");
        Assert.assertEquals("optionA", option.getOpt());
        Assert.assertNull(option.getLongOpt());
        Assert.assertFalse(option.hasArg());
        Assert.assertFalse(option.isRequired());
        Assert.assertFalse(option.hasOptionalArg());
        Assert.assertFalse(option.hasLongOpt());
        Assert.assertFalse(option.hasArgs());
        Assert.assertNull(option.getType());
        Assert.assertEquals("Description for option A", option.getDescription());
        Assert.assertFalse(option.hasArgName());
        Assert.assertEquals(0, option.getArgs());
        Assert.assertEquals("[ option: a :: Description for option A ]", option.toString());
    }

    @Test
    public void testOptionConstructionWithLongOpt() {
        Option option = new Option("a", "longOptionA", false, "Description for option A");
        Assert.assertEquals("a", option.getOpt());
        Assert.assertEquals("longOptionA", option.getLongOpt());
        Assert.assertFalse(option.hasArg());
        Assert.assertFalse(option.isRequired());
        Assert.assertFalse(option.hasOptionalArg());
        Assert.assertTrue(option.hasLongOpt());
        Assert.assertFalse(option.hasArgs());
        Assert.assertNull(option.getType());
        Assert.assertEquals("Description for option A", option.getDescription());
        Assert.assertFalse(option.hasArgName());
        Assert.assertEquals(0, option.getArgs());
        Assert.assertEquals("[ option: a :: longOptionA :: Description for option A ]", option.toString());
    }

    @Test
    public void testOptionConstructionWithArg() {
        Option option = new Option("a", false, "Description for option A with arg");
        Assert.assertEquals("a", option.getOpt());
        Assert.assertNull(option.getLongOpt());
        Assert.assertTrue(option.hasArg());
        Assert.assertFalse(option.isRequired());
        Assert.assertFalse(option.hasOptionalArg());
        Assert.assertFalse(option.hasLongOpt());
        Assert.assertFalse(option.hasArgs());
        Assert.assertNull(option.getType());
        Assert.assertEquals("Description for option A with arg", option.getDescription());
        Assert.assertFalse(option.hasArgName());
        Assert.assertEquals(1, option.getArgs());
        Assert.assertEquals("[ option: a :: Description for option A with arg ]", option.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidOptionConstruction() {
        new Option(null, "longOptionA", false, "Description for option A");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidOptionConstructionWithLongOptNull() {
        new Option("a", null, false, "Description for option A");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidOptionConstructionWithOptionArgAndDescriptionNull() {
        new Option("a", null, true, null);
    }

    @Test
    public void testOptionSetters() {
        Option option = new Option("a", false, "Description for option A");
        Assert.assertEquals("optionA", option.getOpt());
        Assert.assertNull(option.getLongOpt());
        option.setLongOpt("longOptionA");
        Assert.assertEquals("optionA", option.getOpt());
        Assert.assertEquals("longOptionA", option.getLongOpt());
        option.setDescription("New description for option A");
        Assert.assertEquals("New description for option A", option.getDescription());
        option.setOptionalArg(true);
        Assert.assertTrue(option.hasOptionalArg());
        option.setArgName("argName");
        Assert.assertEquals("argName", option.getArgName());
        Assert.assertTrue(option.hasArgName());
        option.setArgs(3);
        Assert.assertEquals(3, option.getArgs());
        option.setType(Integer.class);
        Assert.assertEquals(Integer.class, option.getType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidOptionSetter() {
        Option option = new Option("a", false, "Description for option A");
        option.setArgs(-1);
    }

    @Test
    public void testOptionGetters() {
        Option option = new Option("a", false, "Description for option A");
        Assert.assertEquals("optionA", option.getOpt());
        Assert.assertEquals(0, option.getArgs());
        Assert.assertEquals(false, option.hasOptionalArg());
        Assert.assertEquals(false, option.isRequired());
        Assert.assertEquals(false, option.hasLongOpt());
        Assert.assertEquals(false, option.hasArgs());
        Assert.assertEquals(null, option.getType());
        Assert.assertEquals("Description for option A", option.getDescription());
        Assert.assertEquals(false, option.hasArgName());
        List<String> values = Arrays.asList("value1", "value2", "value3");
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        option.addValueForProcessing("value3");
        Assert.assertEquals(values, option.getValuesList());
        Assert.assertEquals(3, option.getArgs());
        option.clearValues();
        Assert.assertEquals(0, option.getArgs());
        Assert.assertEquals(null, option.getValue());
        Assert.assertEquals(null, option.getValue(0));
        Assert.assertEquals(null, option.getValue(1));
        Assert.assertEquals(null, option.getValue(2));
        Assert.assertEquals(null, option.getValues());
        Assert.assertEquals(null, option.toString());
    }

    @Test(expected = RuntimeException.class)
    public void testAddingValueToFixedArgOption() {
        Option option = new Option("a", false, "Description for option A");
        option.setArgs(1);
        option.addValueForProcessing("value1");
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGettingValueOutOfBounds() {
        Option option = new Option("a", false, "Description for option A");
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        Assert.assertEquals("value1", option.getValue(0));
        Assert.assertEquals("value2", option.getValue(1));
        Assert.assertNull(option.getValue(2));
    }

    @Test
    public void testOptionToString() {
        Option option = new Option("a", false, "Description for option A");
        option.addValueForProcessing("value1");
        option.addValueForProcessing("value2");
        Assert.assertEquals("[ option: a :: Description for option A :: [value1, value2] ]", option.toString());
    }
}