package org.apache.commons.cli.test;

import junit.framework.Assert;
import junit.framework.TestCase;
import org.apache.commons.cli.Option;
import java.util.ArrayList;
import java.util.List;

public class OptionTest extends TestCase {

    @Test
    public void testOptionCreation() {
        Option option = new Option("a", "optionA", false, "This is option A");
        Assert.assertEquals("optionA", option.getOpt());
        Assert.assertTrue(option.hasOptionalArg());
        Assert.assertNull(option.getLongOpt());
        Assert.assertEquals("This is option A", option.getDescription());
        Assert.assertFalse(option.isRequired());
        Assert.assertNull(option.getArgName());
        Assert.assertFalse(option.hasArgs());
        Assert.assertEquals(-1, option.getArgs());
        Assert.assertNull(option.getType());
        Assert.assertEquals(0, option.getValueList().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionCreationWithInvalidOptionName() {
        new Option("", "optionA", false, "This is option A");
    }

    @Test
    public void testOptionCreationWithLongOpt() {
        Option option = new Option("a", "longOptionA", false, "This is option A", "longArg");
        Assert.assertEquals("longOptionA", option.getLongOpt());
        Assert.assertEquals("longArg", option.getArgName());
        Assert.assertTrue(option.hasOptionalArg());
        Assert.assertEquals("This is option A", option.getDescription());
        Assert.assertFalse(option.isRequired());
        Assert.assertFalse(option.hasArgs());
        Assert.assertEquals(-1, option.getArgs());
        Assert.assertNull(option.getType());
        Assert.assertEquals(0, option.getValueList().size());
    }

    @Test
    public void testOptionCreationWithArg() {
        Option option = new Option("a", false, "This is option A");
        Assert.assertNull(option.getOpt());
        Assert.assertFalse(option.hasOptionalArg());
        Assert.assertNull(option.getLongOpt());
        Assert.assertEquals("This is option A", option.getDescription());
        Assert.assertFalse(option.isRequired());
        Assert.assertNull(option.getArgName());
        Assert.assertFalse(option.hasArgs());
        Assert.assertEquals(-1, option.getArgs());
        Assert.assertNull(option.getType());
        Assert.assertEquals(0, option.getValueList().size());
    }

    @Test
    public void testOptionCreationWithArgAndLongOpt() {
        Option option = new Option("a", "longOptionA", false, "This is option A", "longArg", "shortArg");
        Assert.assertEquals("longArg", option.getArgName());
        Assert.assertEquals("shortArg", option.getOpt());
        Assert.assertEquals("longOptionA", option.getLongOpt());
        Assert.assertTrue(option.hasOptionalArg());
        Assert.assertEquals("This is option A", option.getDescription());
        Assert.assertFalse(option.isRequired());
        Assert.assertFalse(option.hasArgs());
        Assert.assertEquals(-1, option.getArgs());
        Assert.assertNull(option.getType());
        Assert.assertEquals(0, option.getValueList().size());
    }

    @Test
    public void testOptionCreationWithValue() {
        Option option = new Option("a", "optionA", false, "This is option A", "value");
        Assert.assertEquals("optionA", option.getOpt());
        Assert.assertTrue(option.hasOptionalArg());
        Assert.assertNull(option.getLongOpt());
        Assert.assertEquals("This is option A", option.getDescription());
        Assert.assertFalse(option.isRequired());
        Assert.assertNull(option.getArgName());
        Assert.assertFalse(option.hasArgs());
        Assert.assertEquals(-1, option.getArgs());
        Assert.assertEquals("value", option.getValueList().get(0));
        Assert.assertNull(option.getType());
    }

    @Test
    public void testOptionCreationWithValueAndLongOpt() {
        Option option = new Option("a", "longOptionA", false, "This is option A", "value", "longArg");
        Assert.assertEquals("longArg", option.getArgName());
        Assert.assertEquals("a", option.getOpt());
        Assert.assertEquals("longOptionA", option.getLongOpt());
        Assert.assertTrue(option.hasOptionalArg());
        Assert.assertEquals("This is option A", option.getDescription());
        Assert.assertFalse(option.isRequired());
        Assert.assertFalse(option.hasArgs());
        Assert.assertEquals(-1, option.getArgs());
        Assert.assertEquals("value", option.getValueList().get(0));
        Assert.assertNull(option.getType());
    }

    @Test
    public void testOptionCreationWithValueAndArg() {
        Option option = new Option("a", false, "This is option A", "value", "arg");
        Assert.assertNull(option.getOpt());
        Assert.assertFalse(option.hasOptionalArg());
        Assert.assertNull(option.getLongOpt());
        Assert.assertEquals("This is option A", option.getDescription());
        Assert.assertFalse(option.isRequired());
        Assert.assertEquals("arg", option.getArgName());
        Assert.assertFalse(option.hasArgs());
        Assert.assertEquals(-1, option.getArgs());
        Assert.assertEquals(1, option.getValueList().size());
        Assert.assertEquals("value", option.getValueList().get(0));
        Assert.assertNull(option.getType());
    }

    @Test
    public void testOptionCreationWithValueArgAndType() {
        Option option = new Option("a", false, "This is option A", "value", "arg", String.class);
        Assert.assertNull(option.getOpt());
        Assert.assertFalse(option.hasOptionalArg());
        Assert.assertNull(option.getLongOpt());
        Assert.assertEquals("This is option A", option.getDescription());
        Assert.assertFalse(option.isRequired());
        Assert.assertEquals("arg", option.getArgName());
        Assert.assertFalse(option.hasArgs());
        Assert.assertEquals(-1, option.getArgs());
        Assert.assertEquals(1, option.getValueList().size());
        Assert.assertEquals("value", option.getValueList().get(0));
        Assert.assertEquals(String.class, option.getType());
    }

    @Test
    public void testOptionCreationWithMultipleValues() {
        Option option = new Option("a", false, "This is option A", null, null, String.class, ',');
        Assert.assertNull(option.getOpt());
        Assert.assertFalse(option.hasOptionalArg());
        Assert.assertNull(option.getLongOpt());
        Assert.assertEquals("This is option A", option.getDescription());
        Assert.assertFalse(option.isRequired());
        Assert.assertNull(option.getArgName());
        Assert.assertTrue(option.hasArgs());
        Assert.assertEquals(1, option.getArgs());
        Assert.assertEquals(',', option.getValueSeparator());
        Assert.assertTrue(option.hasValueSeparator());
        Assert.assertEquals(0, option.getValueList().size());
    }

    @Test
    public void testOptionProcessingValue() {
        Option option = new Option("a", false, "This is option A", null, null, String.class);
        option.addValue("value1");
        option.addValue("value2");
        Assert.assertEquals(2, option.getValueList().size());
        Assert.assertEquals("value1", option.getValueList().get(0));
        Assert.assertEquals("value2", option.getValueList().get(1));
    }

    @Test(expected = RuntimeException.class)
    public void testOptionProcessingValueWithNoArg() {
        Option option = new Option("a", false, "This is option A");
        option.addValue("value");
    }

    @Test(expected = RuntimeException.class)
    public void testOptionProcessingValueWithTooManyArgs() {
        Option option = new Option("a", false, "This is option A", null, null, String.class, ',');
        option.addValue("value1");
        option.addValue("value2");
        option.addValue("value3");
    }

    @Test
    public void testOptionToString() {
        Option option = new Option("a", false, "This is option A", null, null, String.class);
        option.addValue("value1");
        option.addValue("value2");
        String expected = "[ option: a :: This is option A :: [value1, value2] ]";
        Assert.assertEquals(expected, option.toString());
    }
}