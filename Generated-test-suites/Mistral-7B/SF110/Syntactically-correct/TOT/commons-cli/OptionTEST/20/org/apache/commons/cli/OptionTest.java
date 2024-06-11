package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class OptionTest {

    @Test
    public void testOptionCreationWithNoArgs() {
        Option option = new Option("a", "option-a", false, "Description for option A");
        Assert.assertEquals("option-a", option.getOpt());
        Assert.assertNull(option.getLongOpt());
        Assert.assertFalse(option.hasOptionalArg());
        Assert.assertEquals(0, option.getArgs());
        Assert.assertEquals(false, option.isRequired());
        Assert.assertEquals("Description for option A", option.getDescription());
        Assert.assertNull(option.getType());
        Assert.assertEquals(-1, option.getNumberOfArgs());
        Assert.assertEquals(0, option.getValues().size());
        Assert.assertEquals(false, option.hasValueSeparator());
        Assert.assertEquals(-1, option.getId());
        Assert.assertEquals("a", option.getKey());
        Assert.assertEquals(false, option.hasLongOpt());
        Assert.assertEquals(false, option.hasArg());
        Assert.assertEquals(false, option.hasArgs());
    }

    @Test
    public void testOptionCreationWithOneArg() {
        Option option = new Option("b", "option-b", true, "Description for option B");
        Assert.assertEquals("b", option.getOpt());
        Assert.assertNull(option.getLongOpt());
        Assert.assertTrue(option.hasOptionalArg());
        Assert.assertEquals(1, option.getArgs());
        Assert.assertEquals(false, option.isRequired());
        Assert.assertEquals("Description for option B", option.getDescription());
        Assert.assertNull(option.getType());
        Assert.assertEquals(-1, option.getNumberOfArgs());
        Assert.assertEquals(0, option.getValues().size());
        Assert.assertEquals(false, option.hasValueSeparator());
        Assert.assertEquals(-1, option.getId());
        Assert.assertEquals("b", option.getKey());
        Assert.assertEquals(false, option.hasLongOpt());
        Assert.assertEquals(true, option.hasArg());
        Assert.assertEquals(false, option.hasArgs());
    }

    @Test
    public void testOptionCreationWithOneArgAndLongOpt() {
        Option option = new Option("c", "option-c", true, "Description for option C")
                .longOpt("long-option-c");
        Assert.assertEquals("c", option.getOpt());
        Assert.assertEquals("long-option-c", option.getLongOpt());
        Assert.assertTrue(option.hasOptionalArg());
        Assert.assertEquals(1, option.getArgs());
        Assert.assertEquals(false, option.isRequired());
        Assert.assertEquals("Description for option C", option.getDescription());
        Assert.assertNull(option.getType());
        Assert.assertEquals(-1, option.getNumberOfArgs());
        Assert.assertEquals(0, option.getValues().size());
        Assert.assertEquals(false, option.hasValueSeparator());
        Assert.assertEquals(-1, option.getId());
        Assert.assertEquals("c", option.getKey());
        Assert.assertEquals(true, option.hasLongOpt());
        Assert.assertEquals(true, option.hasArg());
        Assert.assertEquals(false, option.hasArgs());
    }

    @Test
    public void testOptionCreationWithMultipleValues() {
        Option option = OptionBuilder.withLongOpt("option-d")
                .hasArg(true)
                .valueSeparator('=')
                .create();
        Assert.assertEquals(null, option.getOpt());
        Assert.assertEquals("option-d", option.getLongOpt());
        Assert.assertTrue(option.hasOptionalArg());
        Assert.assertEquals(-1, option.getArgs());
        Assert.assertEquals(false, option.isRequired());
        Assert.assertEquals("", option.getDescription());
        Assert.assertNull(option.getType());
        Assert.assertEquals(-1, option.getNumberOfArgs());
        Assert.assertEquals(0, option.getValues().size());
        Assert.assertEquals(true, option.hasValueSeparator());
        Assert.assertEquals('=', option.getValueSeparator());
        Assert.assertEquals(-1, option.getId());
        Assert.assertEquals("option-d", option.getKey());
        Assert.assertEquals(true, option.hasLongOpt());
        Assert.assertEquals(true, option.hasArg());
        Assert.assertEquals(true, option.hasArgs());

        option.addValueForProcessing("value1=value1a");
        option.addValueForProcessing("value2=value2a");
        option.addValueForProcessing("value3");

        Assert.assertEquals(3, option.getValues().size());
        Assert.assertEquals("value1a", option.getValue(0));
        Assert.assertEquals("value1a", option.getValues().get(0));
        Assert.assertEquals("value2a", option.getValue(1));
        Assert.assertEquals("value2a", option.getValues().get(1));
        Assert.assertEquals("value3", option.getValue(2));
        Assert.assertEquals("value3", option.getValues().get(2));
    }
}